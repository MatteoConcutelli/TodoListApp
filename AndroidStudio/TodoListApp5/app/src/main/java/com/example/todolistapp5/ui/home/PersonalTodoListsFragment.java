package com.example.todolistapp5.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todolistapp5.databinding.FragmentHomePersonalBinding;
import com.example.todolistapp5.ui.home.viewmodels.GetDataViewModel;
import com.example.todolistapp5.recyclerview.TodoListsTitlesAdapter;

public class PersonalTodoListsFragment extends Fragment {

    private FragmentHomePersonalBinding binding;
    private GetDataViewModel getDataViewModel;
    TodoListsTitlesAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDataViewModel = new ViewModelProvider(this).get(GetDataViewModel.class);
        adapter = new TodoListsTitlesAdapter(null);
        getDataViewModel.getTodoLists(adapter);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentHomePersonalBinding.inflate(inflater, container, false);

        binding.rvPersonal.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPersonal.setAdapter(adapter);

        return binding.getRoot();
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
