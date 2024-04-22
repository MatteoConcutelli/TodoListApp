package com.example.todolistapp5.access;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.todolistapp5.R;

import org.json.JSONObject;

public class AccessViewModel extends AndroidViewModel {
    public AccessViewModel(@NonNull Application application) {
        super(application);
    }

    Context context;
    /*
        Login account
     */
    public void login(String username, String password) {

        context = getApplication().getApplicationContext();
        String url = new String(context.getString(R.string.url_accesstodolist) + "username=" + username + "&password=" + password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        context =  getApplication().getApplicationContext();
                        SharedPreferences sharedPref = context.getSharedPreferences("ShaderPref", Context.MODE_PRIVATE);
                        sharedPref.edit().putString("username", username);
                        sharedPref.edit().putString("password", password);
                        sharedPref.edit().apply();

                        Toast.makeText(context, "success!", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("TAG", "onErrorResponse: " + error);

                        try {
                            int status = error.networkResponse.statusCode;
                            if (status == 404) {
                                Toast.makeText(context, "incorrect credentials!", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {}
                    }

                });

        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(jsonObjectRequest);

    }

    /*
        Registration account
     */
    public void register(String username, String password) {

        context = getApplication().getApplicationContext();
        String url = new String(context.getString(R.string.url_accesstodolist) + "username=" + username + "&password=" + password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences sharedPref = context.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
                        sharedPref.edit().putString("username", username);
                        sharedPref.edit().putString("password", password);
                        sharedPref.edit().apply();

                        Toast.makeText(context, "success!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("TAG", "onErrorResponse: " + error);

                        try {
                            int status = error.networkResponse.statusCode;
                            if (status == 409) {
                                Toast.makeText(context, "account already exists!", Toast.LENGTH_LONG).show();
                            }
                            else if (status == 400) {
                                Toast.makeText(context, "password to small!", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {}
                    }

                });

        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(jsonObjectRequest);
    }
}
