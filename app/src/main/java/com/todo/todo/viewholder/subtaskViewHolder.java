package com.todo.todo.viewholder;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.todo.todo.R;


public class subtaskViewHolder extends RecyclerView.ViewHolder {

    private View mView;
    private TextView mTitle;
    private CheckBox checkBox;

    public subtaskViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mTitle = (TextView) mView.findViewById(R.id.todo_title);
        checkBox = (CheckBox) mView.findViewById(R.id.todo_checkbox);
        checkBox.setChecked(true);

    }

    public void setCheckbox(final DatabaseReference mData) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    mTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    mData.setValue(mView.getResources().getString(R.string.completedTask));
                } else {
                    mTitle.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                    mData.setValue(mView.getResources().getString(R.string.pendingTask));
                }

            }
        });


    }

    public void setLayout(String status) {

        if (status.equals(mView.getResources().getString(R.string.pendingTask))) {
            checkBox.setChecked(false);
            mTitle.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        } else if (status.equals(mView.getResources().getString(R.string.completedTask))) {
            mTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            checkBox.setChecked(true);
        }
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }
}

