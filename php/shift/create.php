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

if (isset($_POST['type']) && isset($_POST['shift_date']) && isset($_POST['boss_id'])) {
    // Call validate, pass form data as parameter and store the returned value
    $type = $_POST['type'];
    $shift_date = $_POST['shift_date'];
    $boss_id = $_POST['boss_id'];


    // Create the SQL query string
    $sql = "insert into shift values('', '$type', '$shift_date', '$boss_id')";

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

mysqli_close($connection);
