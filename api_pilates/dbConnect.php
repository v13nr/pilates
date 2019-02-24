<?php

	//Defining Constants
	define('HOST','localhost');
	define('USER','root');
	define('PASS','');
	define('DB','bookingmanajer');
	
	//Connecting to Database
	$con = mysqli_connect(HOST,USER,PASS,DB) or die('Problemas dengan Server Database.');
	
?>