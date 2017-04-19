package com.todo.todo.ui.view;

import android.content.Context;

public interface BaseActivityCallback {

    void showProgressDialog(String message, Context ctx);

    void hideProgressDialog();

    void setToolbarTitle(String title);
}