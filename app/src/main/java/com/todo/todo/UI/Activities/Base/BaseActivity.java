package com.todo.todo.UI.Activities.Base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.todo.R;
import com.todo.todo.UI.Views.BaseActivityCallback;

public abstract class BaseActivity extends AppCompatActivity implements BaseActivityCallback {

    protected Toolbar toolbar;
    private ProgressDialog progress;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
            getWindow().setStatusBarColor(colorPrimary);
            getWindow().setNavigationBarColor(colorPrimary);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


    }


    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
        }
    }
    protected void showBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays a toast in current activity. In this method the duration
     * supplied is Short by default. If you want to specify duration
     * use {@link BaseActivity#showToast(String, int)} method.
     *
     * @param message Message that the toast must show.
     */
    public void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * Displays a toast in current activity. The duration can of two types:
     * <ul>
     * <li>SHORT</li>
     * <li>LONG</li>
     * </ul>
     *
     * @param message   Message that the toast must show.
     * @param toastType Duration for which the toast must be visible.
     */
    public void showToast(@NonNull String message, @NonNull int toastType) {
        Toast.makeText(BaseActivity.this, message, toastType).show();
    }

    public void showSnack(String message, int length)
    {
        try {
            Snackbar snack = Snackbar.make(getCurrentFocus(), message, length);
            TextView snackBarText = (TextView) snack.getView().findViewById(android.support.design.R.id.snackbar_text);
            snackBarText.setTextColor(Color.WHITE);
            snack.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackBar));
            snack.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean showSnack(String message)
    {
        showSnack(message,Snackbar.LENGTH_LONG);
        return true;
    }


    public void showProgressDialog() {
        showProgressDialog(getString(R.string.working));
    }

    @Override
    public void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
        }
        progress.setMessage(message);
        progress.show();

    }

    @Override
    public void hideProgressDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            setTitle(title);
        }
    }

    protected void setActionBarTitle(int title) {
        getSupportActionBar().setTitle(getResources().getText(title));
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void setToolbarTitle(String title) {
        setActionBarTitle(title);
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())) {
            showSnack("No internet available", Snackbar.LENGTH_INDEFINITE);
            hideProgressDialog();
            return false;
        } else {
            return true;
        }

    }


}