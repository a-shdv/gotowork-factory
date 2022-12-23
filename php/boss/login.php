<?php
$server = "localhost";
$username = "root";
$password = "";
$database = "gotowork_db";

// Create connection
$connection = new mysqli($server, $username, $password, $database);

// Check connection
if ($connection->connect_error) {
    die("Connection failed: " . $connection->connect_error);
}

if (isset($_POST['login']) && isset($_POST['password'])) {
    // Include the necessary files
    require_once "validate.php";

    // Call validate, pass form data as parameter and store the returned value
    $login = validate($_POST['login']);
    $password = validate($_POST['password']);
    // Create the SQL query string
    $sql = "select * from boss where login='$login' and password='" . md5($password) . "'";

    // Execute the query
    $result = $connection->query($sql);
    // If number of rows returned is greater than 0 (that is, if the record is found), we'll print "success", otherwise "failure"
    if ($result->num_rows > 0) {
        echo "success";
    } else {
        // If no record is found, print "failure"
        echo "failure";
    }
}
