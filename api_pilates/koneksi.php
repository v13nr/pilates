<?php

header("Access-Control-Allow-Origin: *");

	$server 	= "localhost";
	$username 	= "root";
	$password	= "";
	$database 	= "bookingmanajer";
	
	$con = mysqli_connect($server, $username, $password, $database);
	if (mysqli_connect_errno()) {
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}
	
	mysql_connect($server,$username, $password) or die("tidak terkoneksi dengan server");
	mysql_select_db($database) or die("tidak terkoneksi dengan database");
?>