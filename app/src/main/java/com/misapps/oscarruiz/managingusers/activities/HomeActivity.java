package com.misapps.oscarruiz.managingusers.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.misapps.oscarruiz.managingusers.R;
import com.misapps.oscarruiz.managingusers.controllers.NavigationController;
import com.misapps.oscarruiz.managingusers.fragments.AddFragment;
import com.misapps.oscarruiz.managingusers.fragments.HomeFragment;
import com.misapps.oscarruiz.managingusers.utils.Constants;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Navigation Controller
     */
    private NavigationController navigationController;

    /**
     * FrameLayout
     */
    FrameLayout fragmentContainer;

    /**
     * Actionbar Drawer Toggle
     */
    ActionBarDrawerToggle toggle;

    /**
     * Navigation View
     */
    private NavigationView navigationView;

    /**
     * Floating Action Button
     */
    private FloatingActionButton fab;

    /**
     * DrawerLayout
     */
    private DrawerLayout drawer;

    /**
     * Toolbar
     */
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getViews();
        setListeners();
        setInfo();
    }

    /**
     * Method to get Views
     */
    private void getViews () {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
    }

    /**
     * Method to set listeners
     */
    private void setListeners() {
        //Navigation listener
        navigationView.setNavigationItemSelectedListener(this);

        //Set toggle to drawer
        drawer.setDrawerListener(toggle);

        //Floating action button listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setTitle(getString(R.string.add_user));
                //Go to add user
                Fragment addFragment = new AddFragment();
                navigationController.changeFragment(HomeActivity.this, addFragment, null, Constants.APLICATION_STATES.ADD_USER_STATE);
            }
        });
    }

    /**
     * Method to set info
     */
    private void setInfo() {
        //Set action bar
        setSupportActionBar(toolbar);

        //Create toggle
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        //Set navigation controller
        navigationController = new NavigationController();

        //Init navigation
        Fragment homeFragment = new HomeFragment();
        navigationController.changeFragment(this, homeFragment, null, Constants.APLICATION_STATES.HOME_STATE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            navigationController.backPressed(HomeActivity.this);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation
        switch (item.getItemId()) {
            case R.id.users_list:
                toolbar.setTitle(getString(R.string.app_name));
                //Go to Home Fragment
                Fragment homeFragment = new HomeFragment();
                navigationController.changeFragment(this, homeFragment, null, Constants.APLICATION_STATES.HOME_STATE);
                break;
            case R.id.add_user:
                toolbar.setTitle(getString(R.string.add_user));
                //Go to add user
                Fragment addFragment = new AddFragment();
                navigationController.changeFragment(this, addFragment, null, Constants.APLICATION_STATES.ADD_USER_STATE);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Method to hide Floating Action Button
     */
    public void hideFAB() {
        fab.hide();
    }

    /**
     * Method to show Floating Action Button
     */
    public void showFAB() {
        fab.show();
    }

    /**
     * Method to set toolbar title
     */
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

}
