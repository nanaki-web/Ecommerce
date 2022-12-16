package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CreateAccountActivity extends AppCompatActivity {

    private TextView alreadyHasAccountBtn;
    private TextView errorCreateAccountTextView;
    private Button createAccountBtn;

    private EditText usernameEditText;
    private EditText passwordEditText;

    private String username;
    private String password;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        //lier layout à java
        alreadyHasAccountBtn = findViewById(R.id.alreadyHasAccountBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        usernameEditText = findViewById(R.id.createUsernameEditText);
        passwordEditText = findViewById(R.id.createPasswordEditText);
        errorCreateAccountTextView = findViewById(R.id.errorCreateAccountTextView);

        databaseManager = new DatabaseManager(getApplicationContext());



        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                createAccount();
                //lancer la requête pour inscrire l'utilisateur

            }
        });
        //lier le textview Créer un compte au java
        alreadyHasAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent sert a changer l'activity actuelle a celle CreateAccountActivity
                Intent connectToAccountActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(connectToAccountActivity);
            }
        });
    }

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
                errorCreateAccountTextView.setVisibility(View.VISIBLE);//rendre visite l'erreur
                errorCreateAccountTextView.setText(error);//afficher l'erreur
            }

        }catch (JSONException e){
            e.printStackTrace();//voir mieux les erreurs
            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void createAccount(){
        String url = "http://10.0.2.2/apituto/actions/createAccount.php";
        //envoyer en json à notre api
        Map<String, String> params = new HashMap<>();
        //ajouter des paramètres
        params.put("username", username);
        params.put("password", password);
        //transformer en json
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onApiResponse(response); //appel
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        databaseManager.queue.add(jsonObjectRequest);
    }

}