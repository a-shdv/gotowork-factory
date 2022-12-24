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

$query = mysqli_query($connection, "SELECT id, machine_type,
       shift_begin_time, shift_end_time, shift_id, shift_name FROM machine");

$flag = array();
if ($query) {
    while ($row = mysqli_fetch_array($query)) {
        $flag[] = $row;
    }
    print(json_encode($flag));
}

mysqli_close($connection);
