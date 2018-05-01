package com.android.sagot.mynews.Controllers.Activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.sagot.mynews.Adapters.PageAdapter;
import com.android.sagot.mynews.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //NAVIGATION DRAWER Design
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NAVIGATION DRAWER
        // Configure all views of the Navigation Drawer
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        // VIEW PAGER
        //Configure ViewPager with tab layout
        this.configureViewPagerAndTabs();
    }

    // ---------------------------------------------------------------------------------------------
    //                                     TOOLBAR
    // ---------------------------------------------------------------------------------------------

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private void configureToolBar(){
        // Get the toolbar view inside the activity layout
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Change the toolbar Tittle
        setTitle("My News");
        // Sets the Toolbar
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the toolbar  and add it to the Toolbar
        // With one search button
        // With one options menu button
        getMenuInflater().inflate(R.menu.activity_main_menu_toolbar, menu);
        return true;
    }

    // ---------------------
    // ACTIONS
    // ---------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.activity_main_menu_toolbar_search:
                Intent searchActivity = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchActivity);
                return true;
            case R.id.activity_main_menu_toolbar_overflow_notifications:
                Toast.makeText(this, "Select Notifications", Toast.LENGTH_LONG).show();
                searchActivity = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchActivity);
                return true;
            case R.id.activity_main_menu_toolbar_overflow_help:
                Toast.makeText(this, "Select Help", Toast.LENGTH_LONG).show();
                return true;
            case R.id.activity_main_menu_toolbar_overflow_about:
                Toast.makeText(this, "Select About", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // ---------------------------------------------------------------------------------------------
    //                                     NAVIGATION DRAWER
    // ---------------------------------------------------------------------------------------------

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // Configure Drawer Layout and connects him the ToolBar and the NavigationView
    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // ---------------------
    // ACTIONS
    // ---------------------
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle Navigation Item Click
        int id = item.getItemId();

        /*switch (id){
            case R.id.activity_main_drawer_news :
                break;
            case R.id.activity_main_drawer_profile:
                break;
            case R.id.activity_main_drawer_settings:
                break;
            default:
                break;
        }*/

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        // Close the menu so open and if the touch return is pushed
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // ---------------------------------------------------------------------------------------------
    //                                     VIEW PAGER
    // ---------------------------------------------------------------------------------------------

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private void configureViewPagerAndTabs(){
        // Get ViewPager from layout
        ViewPager pager = (ViewPager)findViewById(R.id.activity_main_viewpager);
        // Set Adapter PageAdapter and glue it together
        pager.setAdapter(new PageAdapter(getSupportFragmentManager()) {
        });

        // TAB_LAYOUT Implementation
        // Get TabLayout from layout
        TabLayout tabs= (TabLayout)findViewById(R.id.activity_main_tabs);
        // Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);
        // Design purpose. Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }
}
