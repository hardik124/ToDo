package com.todo.todo.ui.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.R;
import com.todo.todo.models.subTask_item;
import com.todo.todo.models.todo_item;
import com.todo.todo.ui.activities.base.BaseActivity;

import java.util.ArrayList;

public class addTask extends BaseActivity {

    ;
    EditText title, description, subtaskTitle;
    ArrayList<String> subtasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        subtasks = new ArrayList<>();
        subtaskTitle = (EditText) findViewById(R.id.subtaskTitle);
        setToolbar();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        showBackButton();
        initLayout();

    }

    private void initLayout() {
        ImageView doneButton = (ImageView) findViewById(R.id.doneButton);
        title = (EditText) findViewById(R.id.et_title);
        description = (EditText) findViewById(R.id.et_desc);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneButtonClick();
            }
        });
        ImageView addSubtask = (ImageView) findViewById(R.id.addSubTask);
        addSubtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = subtaskTitle.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    showToast("Title empty , Cannot add subtask");
                } else {
                    subtasks.add(title);
                    addLayout(title);
                    subtaskTitle.setText("");
                }
            }
        });

    }

    private void addLayout(String title) {
        ViewGroup mSubtasks = (ViewGroup) findViewById(R.id.subTaskLayout);
        View subTask = LayoutInflater.from(this).inflate(R.layout.row_todo_subtask, mSubtasks, false);
        ((TextView) subTask.findViewById(R.id.todo_title)).setText(title);
        //((RadioButton) subTask.findViewById(R.id.todo_radio_button)).setChecked(true);
        mSubtasks.addView(subTask);
    }

    private void onDoneButtonClick() {
        if (TextUtils.isEmpty(title.getText())) {
            showSnack("Title empty , cannot add task");
        } else if (TextUtils.isEmpty(description.getText())) {
            showSnack("Description empty , Do you want to continue?");
            getSnack().setAction("Yes", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postData();
                }
            });
        } else
            postData();
    }

    private void postData() {
        showProgressDialog();
        DatabaseReference mTask = FirebaseDatabase.getInstance().getReference(getString(R.string.databseKey)).push();
        DatabaseReference mSubTask = FirebaseDatabase.getInstance().getReference(getString(R.string.databaseKeySubtasks)).push();

        todo_item task = new todo_item();
        task.setTitle(title.getText().toString());
        task.setDescription(description.getText().toString());
        task.setKey(mTask.getKey());
        task.setSubtaskKey(mSubTask.getKey());
        task.setStatus(getString(R.string.pendingTask));

        //Post subtask items

        for (String title : subtasks) {
            DatabaseReference newTask = mSubTask.push();
            subTask_item item = new subTask_item();
            item.setStatus(getString(R.string.pendingTask));
            item.setKey(newTask.getKey());
            item.setTitle(title);
            newTask.setValue(item);
        }
        mTask.setValue(task);
        hideProgressDialog();
        finish();
    }
}
