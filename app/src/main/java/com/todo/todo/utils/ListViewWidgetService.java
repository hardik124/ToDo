package com.todo.todo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.todo.todo.R;
import com.todo.todo.models.todo_item;

import java.util.ArrayList;

class ListViewData implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Intent mIntent;
    private ArrayList<todo_item> mTasks;


    public ListViewData(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        mIntent = intent;
    }

    @Override
    public void onCreate() {
        mTasks = new ArrayList<>();
    }

    public RemoteViews getViewAt(int position) {


        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.tasks_widget);

        // feed row

        todo_item data = mTasks.get(position);
        // end feed row

        // Next, set a fill-intent, which will be used to fill in the pending intent template

        // that is set on the collection view in ListViewWidgetProvider.

        Bundle extras = new Bundle();

        extras.putInt(mContext.getString(R.string.extraPos), position);

        Intent fillInIntent = new Intent();

        fillInIntent.putExtra(mContext.getString(R.string.widget_extra), data);

        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.LVlist, fillInIntent);


        return rv;

    }

    public int getCount() {


        return mTasks.size();

    }

    public void onDataSetChanged() {

        // Fetching JSON data from server and add them to records arraylist


    }

    public int getViewTypeCount() {

        return 1;

    }

    public long getItemId(int position) {

        return position;

    }

    public void onDestroy() {

        mTasks.clear();

    }

    public boolean hasStableIds() {

        return true;

    }

    public RemoteViews getLoadingView() {

        return null;

    }
}

public class ListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListViewData(this.getApplicationContext(), intent));
    }
}