package com.example.todolistapp5.ui.home.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.todolistapp5.R;
import com.example.todolistapp5.entities.Task;
import com.example.todolistapp5.entities.TodoList;
import com.example.todolistapp5.recyclerview.TasksAdapterInterface;
import com.example.todolistapp5.recyclerview.TodoListsTitlesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertDataViewModel extends AndroidViewModel {
    public InsertDataViewModel(@NonNull Application application) {
        super(application);
    }


    public void newTodoListEmpty (String newTodoListTitle, boolean isShared) {
        Context context = getApplication().getApplicationContext();
        SharedPreferences sp = getApplication().
                getApplicationContext().getSharedPreferences(context.getString(R.string.com_example_todolistapp5_preferences), Context.MODE_PRIVATE);

        String username = sp.getString("username", null);
        String password = sp.getString("password", null);

        String url = "http://10.0.2.2:8080/TodoListApp/todolists?todoListTitle="+ newTodoListTitle + "&shared=" + isShared;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.PUT, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("TAG", "onResponse: " + response.toString());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("TAG", "onErrorResponse: " + error);

                        try {
                            int status = error.networkResponse.statusCode;
                            if (status == 409) {
                                Toast.makeText(context, newTodoListTitle + " already exists!", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ignored) {}
                    }

                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", username + ":" + password);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(jsonObjectRequest);
    }
}
