package com.todo.todo;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;


public class ToDo extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
