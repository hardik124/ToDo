package com.todo.todo.ui.view;

public interface BaseActivityCallback {

    void showProgressDialog(String message);

    void hideProgressDialog();

    void setToolbarTitle(String title);
}