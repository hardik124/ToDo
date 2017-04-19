package com.todo.todo.ui.activities.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.todo.todo.R;
import com.todo.todo.models.userDetailsModel;
import com.todo.todo.ui.activities.base.BaseActivity;
import com.todo.todo.ui.activities.user.UserDetails;
import com.todo.todo.ui.activities.user.logIn;
import com.todo.todo.ui.activities.user.setDetails;
import com.todo.todo.ui.fragment.TaskList;
import com.todo.todo.utils.adapters.ViewPagerAdapter;


public class Home extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton addFAB;
    private DrawerLayout drawer;
    private userDetailsModel currentUser;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = new userDetailsModel();
        checkUser();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setToolbar();
        getToolbar().setTitle(getString(R.string.app_name));
        showBackButton();
        setSupportActionBar(toolbar);
        showProgressDialog("Loading", Home.this);
        initLayout();
        toggle = new ActionBarDrawerToggle(Home.this, drawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        setupViewPager(viewPager);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Home.this, addTask.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);

            }
        });
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
                Intent userPage = new Intent(Home.this, UserDetails.class);
                startActivity(userPage);
                // hideProgressDialog();

                break;
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(Home.this, logIn.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                showToast("Signed out");
                startActivity(loginIntent);
                // hideProgressDialog();

                break;
        }
        return false;
    }

    private void setNavigationHeader() {
        FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(userDetailsModel.class);
                ((TextView) findViewById(R.id.userName)).setText(currentUser.getName());

                if (currentUser.getImageUrl() != null) {
                    Picasso.with(getApplicationContext()).load(currentUser.getImageUrl()).into(((ImageView) findViewById(R.id.UserPic)));
                } else {
                    (((ImageView) findViewById(R.id.UserPic))).setImageDrawable(ContextCompat.getDrawable(Home.this, R.drawable.defaultprofile));
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showSnack(getString(R.string.Error_retrieving));
            }
        });

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TaskList.newInstance(getString(R.string.pendingTask)),
                getString(R.string.pendingTask));
        adapter.addFragment(TaskList.newInstance(getResources().getString(R.string.completedTask)),
                getResources().getString(R.string.completedTask));
        viewPager.setAdapter(adapter);
    }

    private void checkUser() {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        if (!getIntent().hasExtra("user")) {
                            Intent setDetailsIntent = new Intent(Home.this, setDetails.class);
                            setDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            hideProgressDialog();
                            startActivity(setDetailsIntent);
                        }
                    } else {
                        setNavigationHeader();
                        assert tabLayout != null;
                        tabLayout.setupWithViewPager(viewPager);
                        viewPager.setCurrentItem(0);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    hideProgressDialog();
                    showSnack(getString(R.string.Error_retrieving));
                }
            });
        } else {
            Intent loginIntent = new Intent(Home.this, logIn.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            hideProgressDialog();
            startActivity(loginIntent);
        }

    }
}
