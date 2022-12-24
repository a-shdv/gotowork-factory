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

if (isset($_POST['id']) && isset($_POST['machine_type']) && isset($_POST['shift_begin_time'])
    && isset($_POST['shift_end_time']) && isset($_POST['shift_id']) && isset($_POST['shift_name'])) {

    // Call validate, pass form data as parameter and store the returned value
    $id = $_POST['id'];
    $machine_type = $_POST['machine_type'];
    $shift_begin_time = $_POST['shift_begin_time'];
    $shift_end_time = $_POST['shift_end_time'];
    $shift_id = $_POST['shift_id'];
    $shift_name = $_POST['shift_name'];

    // Create the SQL query string
    $sql = "insert into machine values('$id', '$machine_type', '$shift_begin_time',
                           '$shift_end_time', '$shift_id', '$shift_name')";

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
