package com.example.todolistapp5.ui.home.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolistapp5.databinding.FragmentAddingTodolistBinding;
import com.example.todolistapp5.ui.home.viewmodels.InsertDataViewModel;

public class NewTodoListFragment extends Fragment {

    private FragmentAddingTodolistBinding binding;
    private InsertDataViewModel insertDataViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        insertDataViewModel = new ViewModelProvider(this).get(InsertDataViewModel.class);
        binding = FragmentAddingTodolistBinding.inflate(inflater, container, false);

        binding.btCreateTodolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newListTitle = binding.etTodolistTitle.getText().toString();
                boolean isShared = binding.swShare.isChecked();

                insertDataViewModel.newTodoListEmpty(newListTitle, isShared);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
