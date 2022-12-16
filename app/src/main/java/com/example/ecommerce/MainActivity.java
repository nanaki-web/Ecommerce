package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView errorConnectAccountTextView;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private Button connectBtn;
    private TextView createAccountBtn;

    private String username;
    private String password;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorConnectAccountTextView = findViewById(R.id.errorConnectAccountTextView);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        connectBtn = findViewById(R.id.connectBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);

        //créer une instance de DatabaseManager
        databaseManager = new DatabaseManager(getApplicationContext());

        //pour récupérer les valeurs utilisateur et mot de passe
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                //appel de la methode connectUser() quand l'utilisateur clique sur le bouton
                connectUser();
                //lancer la requête pour connecter l'utilisateur

            }
        });
        //lier le textview Créer un compte au java
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent sert a changer l'activity actuelle a celle CreateAccountActivity
                Intent createAccountActivity = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(createAccountActivity);
                finish();//detruit l'activity
            }
        });

    }
    //methode requete pour recevoir la reponse de l'api
    public void onApiResponse(JSONObject response){
        Boolean success = null;
        String error = "";

        try{
            success = response.getBoolean("success");

            if(success == true){
                Intent interfaceActivity = new Intent(getApplicationContext(), InterfaceActivity.class);//aller sur cette activity
                interfaceActivity.putExtra("username", username);//ajoute la variable username
                startActivity(interfaceActivity);//ouvre une nouvelle activity
                finish();//sert a detruire l'activity

            }else{
                error = response.getString("error");
                errorConnectAccountTextView.setVisibility(View.VISIBLE);//rendre visite l'erreur
                errorConnectAccountTextView.setText(error);//afficher l'erreur
            }

        }catch (JSONException e){
            e.printStackTrace();//voir mieux les erreurs
            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    //connection de l'api
    public void connectUser(){
        // 10.0.2.2 correspond en fait à localhost dans la barre url de google chrome
        String url = "http://10.0.2.2/apituto/actions/connectUser.php";
        //envoyer en json à notre api
        Map<String, String> params = new HashMap<>();
        //ajouter des paramètres
        params.put("username", username);
        params.put("password", password);
        //transformer en json
        JSONObject parameters = new JSONObject(params);

        //requete en json
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponse(response);
                Toast.makeText(getApplicationContext(), "OPERATION SUCCESSFUL", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
        //passe la requete en json
        databaseManager.queue.add(jsonObjectRequest);
    }
}