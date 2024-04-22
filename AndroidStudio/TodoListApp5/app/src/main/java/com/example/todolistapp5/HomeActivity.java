package com.example.todolistapp5;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.todolistapp5.databinding.ActivityHomeBinding;


public class HomeActivity extends AppCompatActivity {

    private static final String ADDING_TYPE = "ADDING_TYPE";
    private static final int ADDING_TODOLIST = 0;

    private ActivityHomeBinding binding;

    @Override
    protected void onStart() {
        super.onStart();

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home_personal, R.id.navigation_home_shared, R.id.navigation_home_shared_with_me)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewHome, navController);


        binding.fabNewTodoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddingActivity.class);
                intent.putExtra(AddingActivity.ADDING_TYPE, AddingActivity.ADDING_TODOLIST);
                startActivity(intent);
            }
        });

    }

}
