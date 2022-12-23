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

    // Create the SQL query string. We'll use md5() function for data security. It calculates and returns the MD5 hash of a string
    $sql = "insert into boss values('','$login', '" . md5($password) . "')";
    // Execute the query. Print "success" on a successful execution, otherwise "failure".
    if (!$connection->query($sql)) {
        echo "failure";
    } else {
        echo "success";
    }
}