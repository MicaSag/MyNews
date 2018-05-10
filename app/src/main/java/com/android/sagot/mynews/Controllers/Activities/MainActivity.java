package com.android.sagot.mynews.Controllers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.sagot.mynews.Adapters.PageAdapter;
import com.android.sagot.mynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,TabLayout.OnTabSelectedListener {

    // FOR TRACES
    private static final String TAG = MainActivity.class.getSimpleName();

    //NAVIGATION DRAWER Design
    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_nav_view) NavigationView mNavigationView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    // TABLAYOUT for glue to ViewPager
    private TabLayout tabs;

    // VIEWPAGER
    private ViewPager pager;

    // VIEWPAGER FRAGMENTS
    // Identify each fragment of the ViewPager with a number
    private static final int FRAGMENT_TOP_STORIES = 0;
    private static final int FRAGMENT_MOST_POPULAR = 1;
    private static final int FRAGMENT_BUSINESS = 2;
    private static final int FRAGMENT_SPORTS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get & serialise all views
        ButterKnife.bind(this);

        // NAVIGATION DRAWER
        // Configure all views of the Navigation Drawer
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        // VIEW PAGER & TAB LAYOUT
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
        Log.d(TAG, "configureToolBar: ");
        // Get the toolbar view inside the activity layout

        // Change the toolbar Tittle
        setTitle("My NYTimesNews");
        // Sets the Toolbar
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
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
        Log.d(TAG, "onOptionsItemSelected: ");
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
        Log.d(TAG, "configureDrawerLayout: ");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView() {
        Log.d(TAG, "configureNavigationView: ");
        // Subscribes to listen the navigationView
        mNavigationView.setNavigationItemSelectedListener(this);
        // Mark as selected the menu item corresponding to First tab 'TOP STORIES'
        this.mNavigationView.getMenu().getItem(0).setChecked(true);
    }

    // ---------------------
    // ACTIONS
    // ---------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: ");

        // Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_top_stories :
                // Positioning the Top Stories Page
                pager.setCurrentItem(FRAGMENT_TOP_STORIES);
                break;
            case R.id.activity_main_drawer_most_popular:
                // Positioning the Most Popular Stories Page
                pager.setCurrentItem(FRAGMENT_MOST_POPULAR);
                break;
            case R.id.activity_main_drawer_business:
                // Positioning the Business Page
                pager.setCurrentItem(FRAGMENT_BUSINESS);
                break;
            case R.id.activity_main_drawer_sports:
                // Positioning the Sports Page
                pager.setCurrentItem(FRAGMENT_SPORTS);
                break;
            default:
                break;
        }

        // Close the Drawer Menu
        this.mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        // Close the menu so open and if the touch return is pushed
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    // ---------------------------------------------------------------------------------------------
    //                                     VIEW PAGER & TAB LAYOUT
    // ---------------------------------------------------------------------------------------------

    private void configureViewPagerAndTabs(){
        Log.d(TAG, "configureViewPagerAndTabs: ");
       this.configureViewPager();
       this.configureTabLayout();
    }
    // ---------------------------------------------------------------------------------------------
    //                                     VIEW PAGER
    // ---------------------------------------------------------------------------------------------

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private void configureViewPager(){
        Log.d(TAG, "configureViewPagerAndTabs: ");
        // Get ViewPager from layout
        pager = findViewById(R.id.activity_main_viewpager);
        // Set Adapter PageAdapter and glue it together
        pager.setAdapter(new PageAdapter(getSupportFragmentManager()) {
        });

    }
    // ---------------------------------------------------------------------------------------------
    //                                     TAB LAYOUT
    // ---------------------------------------------------------------------------------------------

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private void configureTabLayout(){
        Log.d(TAG, "configureTabLayout() called");
        // Get TabLayout from layout
        tabs= findViewById(R.id.activity_main_tabs);
        // Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);
        // Design purpose. Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        // Subscribes to listen the tab layout
        tabs.addOnTabSelectedListener(this);
    }

    // ---------------------
    // ACTIONS
    // ---------------------

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d(TAG, "onTabSelected() called with: tab = [" + tab + "]");
        int position = tab.getPosition();
        // Mark as selected the menu item corresponding to First tab 'TOP STORIES'
        this.mNavigationView.getMenu().getItem(position).setChecked(true);

        // Change Color of the Tab selected
        this.tabs.setBackgroundColor(getResources().obtainTypedArray(R.array.colorTabsLayout)
                .getColor(position,0));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
