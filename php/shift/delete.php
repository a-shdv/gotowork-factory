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

$id = isset($_POST["id"]) ? $_POST["id"] : '';

$sql = "delete from shift where id = '$id'";

if(mysqli_query($connection,$sql))
{

    echo "Successfully deleted";

}
else
{
    echo "Try again Later..." .mysqli_error($connection) ;
}