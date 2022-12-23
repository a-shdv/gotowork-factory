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

if (isset($_POST['name']) && isset($_POST['shift_date'])) {
// Call validate, pass form data as parameter and store the returned value
    $name = $_POST['name'];
    $shift_date = $_POST['shift_date'];
// Create the SQL query string
    $sql = "insert into shift values('', '$name', '$shift_date')";

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
