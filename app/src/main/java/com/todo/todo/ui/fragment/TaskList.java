package com.todo.todo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.todo.todo.R;
import com.todo.todo.utils.adapters.todoRVAdapter;


public class TaskList extends Fragment {

    private static final String param = "status";

    private String status;
    private RecyclerView mTodoList;
    private todoRVAdapter mRVAdapter;
    private View mView;

    public TaskList() {
        // Required empty public constructor
    }

    public static TaskList newInstance(String param1) {
        TaskList fragment = new TaskList();
        Bundle args = new Bundle();
        args.putString(param, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString(param);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_todo_list, container, false);
        mTodoList = (RecyclerView) mView.findViewById(R.id.todoRecycler);
        mTodoList.setHasFixedSize(true);

        LinearLayoutManager productLinearLayout = new LinearLayoutManager(getContext());
        productLinearLayout.setReverseLayout(false);
        productLinearLayout.setStackFromEnd(false);

        mTodoList.setLayoutManager(productLinearLayout);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mRVAdapter = new todoRVAdapter(getString(R.string.databseKey), status);
            mTodoList.setAdapter(mRVAdapter.getAdapter());
        }
        handleDataChange();
        return mView;
    }

    private void handleDataChange() {
        if (mTodoList.getLayoutManager().getItemCount() > 0)
            mView.findViewById(R.id.tasksUnavailable).setVisibility(View.INVISIBLE);
        else
            mView.findViewById(R.id.tasksUnavailable).setVisibility(View.VISIBLE);


        FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mTodoList.getLayoutManager().getItemCount() > 0)
                    mView.findViewById(R.id.tasksUnavailable).setVisibility(View.INVISIBLE);
                else
                    mView.findViewById(R.id.tasksUnavailable).setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



}
