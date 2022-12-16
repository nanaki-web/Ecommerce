<?php
//preciser au site qu'on utilise du json
header('Content-type: application/json');

include_once '../config/Database.php';
//convertir le json en array/tableau
$json = json_decode(file_get_contents('php://input'), true);

if(isset($json['username']) and isset($json['password'])){
    $username = htmlspecialchars($json["username"]);
    $password = htmlspecialchars($json["password"]);
    $passwordHashed = password_hash($password, PASSWORD_DEFAULT);
    
    $success = "";

    if($username == "" or $password ==""){
        $result['success'] = false;
        $result['error'] = "le mot de passe et /ou le nom d'utilisateur est vide";
    }else{
        $checkIfUserNameExists = $bdd->prepare('SELECT *  FROM users WHERE username = ?');
        $checkIfUserNameExists->execute(array($username));
    }

    //verifier en comptant le nombre de ligne ,si username existe deja ou pas 
        if($checkIfUserNameExists->rowCount() > 0){
            $result["success"] = false;
            $result["error"] = "Cet identifiant existe déjà";
        }else{

            try{
                $createAccount = $bdd->prepare("INSERT INTO `users` (`id`, `username`, `userPassword`) VALUES (NULL, ?, ?);");
                $createAccount->execute(array($username, $passwordHashed));
                $result["success"] = true;
            }catch(Exception $e){
                $result["success"] = false;
                $result["error"] = "erreur lors de la création du compte...";
            }
        }
}else{
    $result["success"] = false;
    $result["error"] = "Veuillez complétez tous les champs...";
}

echo json_encode($result);