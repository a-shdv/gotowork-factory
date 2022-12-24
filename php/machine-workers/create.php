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

if (isset($_POST['machine_id']) && isset($_POST['worker_id'])
    && isset($_POST['worker_name']) && isset($_POST['hours'])) {

    // Call validate, pass form data as parameter and store the returned value
    $machine_id = $_POST['machine_id'];
    $worker_id = $_POST['worker_id'];
    $worker_name = $_POST['worker_name'];
    $hours = $_POST['hours'];
/*    $machine_id = 5;
    $worker_id = 1;
    $worker_name = 'Ivanov';
    $hours = 8;*/

    // Create the SQL query string
    $sql = "insert into machine_workers 
            values('', '$machine_id', '$worker_id',
                           '$worker_name', '$hours')";

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
