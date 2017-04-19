package com.todo.todo.utils.adapters;

import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.R;
import com.todo.todo.models.subTask_item;
import com.todo.todo.viewholder.subtaskViewHolder;


public class SubtasksRVAdapter {

    private String type;
    private String key;
    private DatabaseReference mDatabase;

    public SubtasksRVAdapter(String type, String key) {
        this.type = type;
        this.key = key;
        mDatabase = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(type).child(key);
        mDatabase.keepSynced(true);
    }

    public FirebaseRecyclerAdapter<subTask_item, subtaskViewHolder> getAdapter()

    {
        return new FirebaseRecyclerAdapter<subTask_item, subtaskViewHolder>(

                subTask_item.class,
                R.layout.row_todo,
                subtaskViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(subtaskViewHolder viewHolder, subTask_item model, int position) {
                Log.d("detail ", model.getTitle() + "\t" + model.getStatus());
                viewHolder.setLayout(model.getStatus());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setCheckbox(FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(type).child(key).child(model.getKey()).child("status"));

            }
        };
    }
}

