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

public class GetDataViewModel extends AndroidViewModel {
    public GetDataViewModel(@NonNull Application application) {
        super(application);
    }

    public void getTodoLists(TodoListsTitlesAdapter adapter) {

        Context context = getApplication().getApplicationContext();
        SharedPreferences sp = getApplication().
                getApplicationContext().getSharedPreferences(context.getString(R.string.com_example_todolistapp5_preferences), Context.MODE_PRIVATE);

        String username = sp.getString("username", null);
        String password = sp.getString("password", null);

        String url = context.getString(R.string.url_personal_todolists);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("TAG", "onResponse: " + response.toString());

                        List<TodoList> todoLists = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject item = response.getJSONObject(i);
                                String ownerUsername = item.getString("ownerUsername");
                                String newList = item.getString("title");
                                boolean shared = item.getBoolean("shared");

                                TodoList todoList = new TodoList(ownerUsername, newList, shared);
                                todoLists.add(todoList);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapter.setListTitles(todoLists);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("TAG", "onErrorResponse: " + error);

                        try {
                            int status = error.networkResponse.statusCode;
                            if (status == 404) {
                                Toast.makeText(context, "Empty", Toast.LENGTH_LONG).show();
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

    public void getSharedTodoLists(TodoListsTitlesAdapter adapter) {
        Context context = getApplication().getApplicationContext();
        SharedPreferences sp = getApplication().
                getApplicationContext().getSharedPreferences(context.getString(R.string.com_example_todolistapp5_preferences), Context.MODE_PRIVATE);

        String username = sp.getString("username", null);
        String password = sp.getString("password", null);

        String url = context.getString(R.string.url_shared_todolists);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("TAG", "onResponse: " + response.toString());

                        List<TodoList> todoLists = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject item = response.getJSONObject(i);
                                String ownerUsername = item.getString("ownerUsername");
                                String newList = item.getString("title");
                                boolean shared = item.getBoolean("shared");

                                TodoList todoList = new TodoList(ownerUsername, newList, shared);
                                todoLists.add(todoList);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapter.setListTitles(todoLists);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("TAG", "onErrorResponse: " + error);

                        try {
                            int status = error.networkResponse.statusCode;
                            if (status == 404) {
                                Toast.makeText(context, "Empty", Toast.LENGTH_LONG).show();
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

    public void getSharedWithMeTodoLists(TodoListsTitlesAdapter adapter) {

        Context context = getApplication().getApplicationContext();
        SharedPreferences sp = getApplication().
                getApplicationContext().getSharedPreferences(context.getString(R.string.com_example_todolistapp5_preferences), Context.MODE_PRIVATE);

        String username = sp.getString("username", null);
        String password = sp.getString("password", null);

        String url = "http://10.0.2.2:8080/TodoListApp/sharedtodolists?withMe=false";

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("TAG", "onResponse: " + response.toString());

                        List<TodoList> todoLists = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject item = response.getJSONObject(i);
                                String ownerUsername = item.getString("ownerUsername");
                                String newList = item.getString("title");
                                boolean shared = item.getBoolean("shared");

                                TodoList todoList = new TodoList(ownerUsername, newList, shared);
                                todoLists.add(todoList);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapter.setListTitles(todoLists);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("TAG", "onErrorResponse: " + error);

                        try {
                            int status = error.networkResponse.statusCode;
                            if (status == 404) {
                                Toast.makeText(context, "Empty", Toast.LENGTH_LONG).show();
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

    public void getTodoList(TasksAdapterInterface adapter, String listTitle, boolean doneTasks) {

        Context context = getApplication().getApplicationContext();
        SharedPreferences sp = getApplication().
                getApplicationContext().getSharedPreferences(context.getString(R.string.com_example_todolistapp5_preferences), Context.MODE_PRIVATE);

        String username = sp.getString("username", null);
        String password = sp.getString("password", null);

        String url = "http://10.0.2.2:8080/TodoListApp/todolists?todoListTitle=" + listTitle + "&isDone=" + doneTasks;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("TAG", "onResponse: " + response.toString());

                        JSONObject jsonObject = response;
                        TodoList todoList = null;

                        try {

                            String ownerUsername = response.getString("ownerUsername");
                            String newList = response.getString("title");
                            boolean shared = response.getBoolean("shared");

                            todoList = new TodoList(ownerUsername, newList, shared);

                            JSONArray tasksJA = response.getJSONArray("tasks");

                            for (int j = 0; j < tasksJA.length(); j++) {
                                Task task = Task.fromJSON(tasksJA.get(j).toString());
                                todoList.add(task);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.setTodoList(todoList);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("TAG", "onErrorResponse: " + error);

                        try {
                            int status = error.networkResponse.statusCode;
                            if (status == 404) {
                                Toast.makeText(context, "Empty", Toast.LENGTH_LONG).show();
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
