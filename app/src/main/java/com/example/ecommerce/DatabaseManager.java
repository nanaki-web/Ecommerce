package com.example.ecommerce;

import android.content.Context;

import com.android.volley.RequestQueue;//libraie volley ajouté dans build.gradle(module)
import com.android.volley.toolbox.Volley;

public class DatabaseManager {
    private Context context;

    //file d'attente pour les requetes si souci , si une n'est pas fini ,la suivante est en attente ...
    public RequestQueue queue;

    //constructeur:permet de mettre un parmètre lors de l'instantion de la classe
    public DatabaseManager(Context context){
        this.context = context;//le context créé lors de la création de la classe = context mis en paramètre
        this.queue = Volley.newRequestQueue(context);
    }
}
