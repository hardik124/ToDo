package com.todo.todo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.ui.activities.tasks.Home;

public class tasksWidget extends AppWidgetProvider {
    RemoteViews views;

    protected void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                   int appWidgetId) {


        views = new RemoteViews(context.getPackageName(), R.layout.tasks_widget);
        Intent intent = new Intent(context, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        views.setOnClickPendingIntent(R.id.LVlist, pendingIntent);
        views.setEmptyView(R.id.LVlist, R.id.empty);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        onDisabled(context);

    }

    @Override
    public void onEnabled(Context context) {

        FirebaseDatabase.getInstance().goOnline();
    }

    @Override
    public void onDisabled(Context context) {
        FirebaseDatabase.getInstance().goOffline();
    }

}

