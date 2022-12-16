<?php

//eviter les ennuis avec les caractères spéciaux 
header('content-type: text/html; charset=utf-8');

$bdd = new PDO('mysql:host=localhost;dbname=apituto;charset=utf8;', 'root', '');