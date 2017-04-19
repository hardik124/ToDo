package com.todo.todo.ui.activities.tasks;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.R;
import com.todo.todo.models.todo_item;
import com.todo.todo.ui.activities.base.BaseActivity;
import com.todo.todo.utils.adapters.SubtasksRVAdapter;

public class ShowTask extends BaseActivity {
    private todo_item task;
    private RecyclerView mSubtasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);
        setToolbar();
        showBackButton();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            showToast(getString(R.string.TaskUnavailable));
            finish();
        } else {
            setSupportActionBar(getToolbar());
            if (extras.containsKey(getString(R.string.taskExtraKey)))
                task = (todo_item) extras.get(getString(R.string.taskExtraKey));
            getToolbar().setTitle(task.getTitle());

            setLayout();
        }
    }

    private void setLayout() {
        EditText title = (EditText) findViewById(R.id.et_title);
        title.setText(task.getTitle());
        title.setEnabled(false);

        EditText desc = (EditText) findViewById(R.id.et_desc);
        desc.setText(task.getDescription());
        desc.setEnabled(false);

        ImageView mDel = (ImageView) findViewById(R.id.delButton);
        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.databseKey)).child(task.getKey()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        showSnack(getString(R.string.taskDeleted));
                        finish();
                    }
                });
                hideProgressDialog();
            }
        });

        mSubtasks = (RecyclerView) findViewById(R.id.subTasks);
        mSubtasks.setHasFixedSize(false);

        LinearLayoutManager productLinearLayout = new LinearLayoutManager(this);
        productLinearLayout.setReverseLayout(true);
        productLinearLayout.setStackFromEnd(true);

        mSubtasks.setLayoutManager(productLinearLayout);
        if (task.getSubtaskKey() != null) {
            SubtasksRVAdapter mRVAdapter = new SubtasksRVAdapter(getString(R.string.databaseKeySubtasks), task.getSubtaskKey());
            mSubtasks.setAdapter(mRVAdapter.getAdapter());
        }
    }


}
