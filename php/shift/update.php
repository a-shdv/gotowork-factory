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

$id = $_POST["id"];
$type = $_POST["type"];
$shift_date = $_POST["shift_date"];

$sql = "update shift set type = '$type', shift_date = '$shift_date' where id = '$id'";

if ($connection->query($sql)) {

    echo " Successfully updated";

} else {
    echo "Try again Later..." . mysqli_error($connection);
}
