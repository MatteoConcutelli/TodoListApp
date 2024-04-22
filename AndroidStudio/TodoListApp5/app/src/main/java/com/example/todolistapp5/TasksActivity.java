package com.example.todolistapp5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.todolistapp5.databinding.ActivityTasksBinding;

public class TasksActivity extends AppCompatActivity {

    ActivityTasksBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTasksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_tasks_todo, R.id.navigation_tasks_done)
                .build();

        // SETTO LO SWITCH DEI FRAGMENT
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_tasks);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewTasks, navController);


        binding.fabNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TasksActivity.this, AddingActivity.class);
                intent.putExtra(AddingActivity.ADDING_TYPE, AddingActivity.ADDING_TASK);
                startActivity(intent);
            }
        });
    }

}
