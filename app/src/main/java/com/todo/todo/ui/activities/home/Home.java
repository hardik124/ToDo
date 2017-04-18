package com.todo.todo.ui.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.todo.todo.R;
import com.todo.todo.ui.activities.addTask;
import com.todo.todo.ui.activities.base.BaseActivity;
import com.todo.todo.ui.activities.logIn;
import com.todo.todo.ui.fragment.TaskList;
import com.todo.todo.utils.adapters.ViewPagerAdapter;


public class Home extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton addFAB;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setToolbar();
        getToolbar().setTitle(getString(R.string.app_name));
        showBackButton();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
        } else {
                super.onBackPressed();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        initLayout();
        toggle = new ActionBarDrawerToggle(
                this, drawer,getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Home.this, addTask.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
        });
    }

    private void initLayout() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        addFAB = (FloatingActionButton) findViewById(R.id.addFab);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.Completed:
                viewPager.setCurrentItem(1);
                drawer.closeDrawers();
                break;
            case R.id.Pending:
                viewPager.setCurrentItem(0);
                drawer.closeDrawers();
                break;
            case R.id.profile:
                //TODo add page
                break;
            case R.id.signOut:
                showProgressDialog(getString(R.string.signOutMessage));
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(Home.this, logIn.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                showToast("Signed out");
                startActivity(loginIntent);
                hideProgressDialog();

                break;
        }
        return false;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TaskList.newInstance(getString(R.string.pendingTask)),
                getString(R.string.pendingTask));
        adapter.addFragment(TaskList.newInstance(getResources().getString(R.string.completedTask)),
                getResources().getString(R.string.completedTask));
        viewPager.setAdapter(adapter);
    }

}
