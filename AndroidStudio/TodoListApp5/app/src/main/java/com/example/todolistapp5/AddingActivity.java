package com.example.todolistapp5;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.todolistapp5.databinding.ActivityAddingBinding;


public class AddingActivity extends AppCompatActivity {

    public static final String ADDING_TYPE = "ADDING_TYPE";
    public static final int ADDING_TASK = 1;
    public static final int ADDING_TODOLIST = 0;
    private ActivityAddingBinding binding;

    @Override
    protected void onStart() {
        super.onStart();

        binding = ActivityAddingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_adding);

        int addingType = getIntent().getIntExtra(ADDING_TYPE, -1);
        if (addingType == ADDING_TODOLIST) {
            navController.navigate(R.id.navigation_adding_todolist);
        }
        else if (addingType == ADDING_TASK) {
            navController.navigate(R.id.navigation_adding_task);
        }
        else if (addingType == -1) {
            // TODO gestire
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
