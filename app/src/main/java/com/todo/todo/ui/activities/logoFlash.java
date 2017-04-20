package com.todo.todo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.R;
import com.todo.todo.ui.activities.tasks.Home;

import java.util.Timer;
import java.util.TimerTask;


public class logoFlash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logo_flash);
        // Setting full screen view
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        openLogin();
    }

    void openLogin() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(logoFlash.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 2800);
    }
}

