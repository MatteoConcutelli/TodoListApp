package com.example.todolistapp5.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp5.R;
import com.example.todolistapp5.entities.TodoList;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> implements TasksAdapterInterface{

    TodoList todoList;

    public TasksAdapter(TodoList todoList) {
        this.todoList = todoList;
        if (todoList == null) {
            this.todoList = new TodoList("mia", "mia", false);
        }
    }

    public void setTodoList(TodoList todoList) {
        this.todoList.clear();
        this.todoList.addAll(todoList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_task_item, parent, false);

        return new TasksAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(todoList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    /*
    * VIEW HOLDER collegato con il layout : "data_task_row"
    */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView card;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.tv_task);
            card = view.findViewById(R.id.cv_task);

        }

        public TextView getTextView() {
            return textView;
        }

    }
}
