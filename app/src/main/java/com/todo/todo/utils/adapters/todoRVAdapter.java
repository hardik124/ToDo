package com.todo.todo.utils.adapters;

import android.content.Intent;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.todo.todo.R;
import com.todo.todo.models.todo_item;
import com.todo.todo.ui.activities.tasks.ShowTask;
import com.todo.todo.viewholder.todoViewHolder;


public class todoRVAdapter {

    private String type;
    private Query mType;

    public todoRVAdapter(String type, String status) {
        this.type = type;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(type);
        mType = mDatabase.orderByChild("status").equalTo(status);
        mType.keepSynced(true);
    }

    public FirebaseRecyclerAdapter<todo_item, todoViewHolder> getAdapter()

    {
        return new FirebaseRecyclerAdapter<todo_item, todoViewHolder>(

                todo_item.class,
                R.layout.row_todo,
                todoViewHolder.class,
                mType
        ) {
            @Override
            protected void populateViewHolder(final todoViewHolder viewHolder, final todo_item model, int position) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent showDetails = new Intent(viewHolder.itemView.getContext(), ShowTask.class);
                        showDetails.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        showDetails.putExtra(viewHolder.itemView.getContext().getString(R.string.taskExtraKey), model);
                        viewHolder.itemView.getContext().startActivity(showDetails);
                    }
                });
                viewHolder.setLayout(model.getStatus());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setCheckbox(FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(type).child(model.getKey()).child("status"));

            }

        };
    }
}
