package com.example.todolistapp5.ui.home.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todolistapp5.databinding.FragmentTasksPersonalBinding;
import com.example.todolistapp5.ui.home.viewmodels.GetDataViewModel;
import com.example.todolistapp5.recyclerview.TasksAdapter;

public class ToDoTasksFragment extends Fragment {

    private FragmentTasksPersonalBinding binding;
    private GetDataViewModel getDataViewModel;
    TasksAdapter adapter;
    
    private String listTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ottieni il titolo della lista
        listTitle = getActivity().getIntent().getStringExtra("listTitle");
        getActivity().setTitle(listTitle);

        getDataViewModel = new ViewModelProvider(this).get(GetDataViewModel.class);
        adapter = new TasksAdapter(null);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentTasksPersonalBinding.inflate(inflater, container, false);

        binding.rvTasksPersonal.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTasksPersonal.setAdapter(adapter);

        getDataViewModel.getTodoList(adapter, listTitle, false);

        return binding.getRoot();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
