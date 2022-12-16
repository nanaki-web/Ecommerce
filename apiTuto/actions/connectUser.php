<?php

header('Content-Type: application/json');
include_once '../config/Database.php';
$json = json_decode(file_get_contents('php://input', true));

if(isset($json['username']) and isset($json['password'])){
    $username = htmlspecialchars($json['username']);
    $password = htmlspecialchars($json['password']);

    $getUser = $bdd->prepare("SELECT userPassword FROM users WHERE username = ?");
    $getUser->execute(array($username));

    #vérifier si le nom existe
    if($getUser->rowCount() > 0){
        $user = $getUser->fetch();

        //verifie si le mot de passe = mot de passe encrypté
        if(password_verify($password, $user['userPassword'])){
            $result = true;
        }else{
            $result["success"] = false;
            $result["error"] = "Mot de passe incorrect";
        }
    }else{
        $result["success"] = false;
        $result["error"] = "Ce nom d'utilisateur n'existe pas ";
    }
}else{
    $result["success"] = false;
    $result["error"] = "champs non remplis";
}

echo json_encode($result);