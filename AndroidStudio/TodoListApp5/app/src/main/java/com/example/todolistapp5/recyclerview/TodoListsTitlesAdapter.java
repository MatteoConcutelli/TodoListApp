package com.example.todolistapp5.recyclerview;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp5.R;
import com.example.todolistapp5.TasksActivity;
import com.example.todolistapp5.entities.TodoList;

import java.util.ArrayList;
import java.util.List;

public class TodoListsTitlesAdapter extends RecyclerView.Adapter<TodoListsTitlesAdapter.ViewHolder> {

    private List<TodoList> listTitles;

    public TodoListsTitlesAdapter(List<TodoList> listTitles) {
        this.listTitles = listTitles;
        if (listTitles == null) {
            this.listTitles = new ArrayList<>();
        }
    }

    // UTILITIES
    public void setListTitles(List<TodoList> listTitles) {
        this.listTitles.clear();
        this.listTitles.addAll(listTitles);
        notifyDataSetChanged();
    }

    public void add(TodoList todoListTitle){
        listTitles.add(todoListTitle);
        notifyDataSetChanged();
    }

    public void remove(int position){
        listTitles.remove(listTitles.get(position));
        notifyItemRemoved(position);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.data_todolist_title, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.setText(listTitles.get(position).getTitle());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listTitles.size();
    }


    /*
     * VIEW HOLDER collegato con il layout : "data_todolist_row"
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private final CardView card;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            textView = (TextView) view.findViewById(R.id.tv_todolist_title);
            card = view.findViewById(R.id.cv_item);
            card.setOnClickListener(this);
        }

        public TextView getTextView() {
            return textView;
        }


        @Override
        public void onClick(View v) {
            Log.d("TAG", "onClick: " + v);

            Intent intent = new Intent(v.getContext(), TasksActivity.class);
            intent.putExtra("listTitle", textView.getText());
            v.getContext().startActivity(intent);

        }
    }

}
