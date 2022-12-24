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
$name = $_POST["name"];
$salary = $_POST["salary"];

$sql = "update worker set name = '$name', salary = '$salary' where id = '$id'";

if ($connection->query($sql)) {

    echo " Successfully updated";

} else {
    echo "Try again Later..." . mysqli_error($connection);
}
