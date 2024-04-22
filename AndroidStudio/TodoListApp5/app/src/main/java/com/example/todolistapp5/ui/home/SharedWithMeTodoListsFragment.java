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

import com.example.todolistapp5.databinding.FragmentHomeSharedWithMeBinding;
import com.example.todolistapp5.ui.home.viewmodels.GetDataViewModel;
import com.example.todolistapp5.recyclerview.TodoListsTitlesAdapter;


public class SharedWithMeTodoListsFragment extends Fragment {

    FragmentHomeSharedWithMeBinding binding;
    GetDataViewModel getDataViewModel;
    TodoListsTitlesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentHomeSharedWithMeBinding.inflate(inflater, container, false);

        binding.rvSharedWithMe.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSharedWithMe.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDataViewModel = new ViewModelProvider(this).get(GetDataViewModel.class);
        adapter = new TodoListsTitlesAdapter(null);

        // richiesta
        getDataViewModel.getSharedWithMeTodoLists(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
