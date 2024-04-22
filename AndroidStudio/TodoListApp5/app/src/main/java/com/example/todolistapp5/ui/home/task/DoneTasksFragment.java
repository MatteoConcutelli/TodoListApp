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

import com.example.todolistapp5.databinding.FragmentTasksPersonalDoneBinding;
import com.example.todolistapp5.ui.home.viewmodels.GetDataViewModel;
import com.example.todolistapp5.recyclerview.DoneTasksAdapter;

public class DoneTasksFragment extends Fragment {
    private FragmentTasksPersonalDoneBinding binding;
    private GetDataViewModel getDataViewModel;
    DoneTasksAdapter adapter;

    private String listTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        listTitle = getActivity().getIntent().getStringExtra("listTitle");
        getActivity().setTitle(listTitle);

        getDataViewModel = new ViewModelProvider(this).get(GetDataViewModel.class);
        adapter = new DoneTasksAdapter(null);

        binding = FragmentTasksPersonalDoneBinding.inflate(inflater, container, false);

        binding.rvTasksPersonalDone.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTasksPersonalDone.setAdapter(adapter);

        getDataViewModel.getTodoList(adapter, listTitle, true);

        return binding.getRoot();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
