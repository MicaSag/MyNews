package com.android.sagot.mynews.Controllers.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.sagot.mynews.Adapters.PageAdapter;
import com.android.sagot.mynews.Models.DataModel;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NotificationsCriteria;
import com.android.sagot.mynews.Models.SearchCriteria;
import com.android.sagot.mynews.R;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.VISIBLE;
import static com.android.sagot.mynews.Utils.UIUtilities.changeStatusBarColor;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,TabLayout.OnTabSelectedListener {

    // FOR TRACES
    private static final String TAG = MainActivity.class.getSimpleName();

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.activity_main_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.activity_main_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    // VIEWPAGER FRAGMENTS
    // Identify each fragment of the ViewPager with a number
    private static final int FRAGMENT_TOP_STORIES = 0;
    private static final int FRAGMENT_MOST_POPULAR = 1;
    private static final int FRAGMENT_BUSINESS = 2;
    private static final int FRAGMENT_SPORTS = 3;

    // SHARED PREFERENCES KEYS
    public static final String SHARED_PREF_MODEL = "SHARED_PREF_MODEL";

    // Defined DataModel of the application
    SharedPreferences mSharedPreferences;

    // Colors Tab of the items menu navigation drawer
    int[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get & serialise all views
        ButterKnife.bind(this);

        // Retrieves the DataModel
        this.retrievesPreferences();

        // NAVIGATION DRAWER
        // Configure all views of the Navigation Drawer
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureNavigationMenuItem();

        // VIEW PAGER & TAB LAYOUT
        //Configure ViewPager with tab layout
        this.configureViewPagerAndTabs();
    }
    // ---------------------------------------------------------------------------------------------
    //                                     PREFERENCES
    // ---------------------------------------------------------------------------------------------
    // >> PREFERENCES RETRIEVES <-------
    private void retrievesPreferences() {
        Log.d(TAG, "retrievesPreferences: ");
        // READ SharedPreferences
        mSharedPreferences = getPreferences(MODE_PRIVATE);
        Model.getInstance().setSharedPreferences(mSharedPreferences);
        // TEST == >>> Allows to erase all the preferences ( Useful for the test phase )
        //Log.i("MOOD","CLEAR COMMIT PREFERENCES");
        //mSharedPreferences.edit().clear().commit();

        //Model retrieves
        this.retrieveModel();
    }

    // >> MODEL RETRIEVES <-------
    private void retrieveModel() {
        Log.d(TAG, "retrieveModels: ");
        String modelPreferences = mSharedPreferences.getString(SHARED_PREF_MODEL, null);
        // Restoring the preferences with a Gson Object
        Gson gson = new Gson();

        if (modelPreferences != null) {
            Log.d(TAG, "retrievesPreferences: model Restoration");
            // Retrieves the model of the SharedPreferences
            Model.getInstance().setDataModel(gson.fromJson(modelPreferences, DataModel.class));
        }else
        {
            Log.d(TAG, "retrieveModels: First call of the App, No Model saved");
            // First call of the app, not model saved
            // INSTANTIATE DataModel Object in the Model Singleton
            Model.getInstance().setDataModel(new DataModel());
        }
        
        // Instantiate SearchCriteria
        instantiateSearchCriteria();
        // Instantiate NotificationsCriteria
        instantiateNotificationsCriteria();
    }
    
    // Instantiate SearchCriteria
    private void instantiateSearchCriteria() {
        // If searchCriteria not exist then instantiate it
        if (Model.getInstance().getDataModel().getSearchCriteria() == null)
            Model.getInstance().getDataModel().setSearchCriteria(new SearchCriteria());
    }
    
    // Instantiate NotificationsCriteria
    private void instantiateNotificationsCriteria() {
        // If notificationsCriteria not exist then instantiate it
        if (Model.getInstance().getDataModel().getNotificationsCriteria() == null)
            Model.getInstance().getDataModel().setNotificationsCriteria(new NotificationsCriteria());
    }
        
    // ---------------------------------------------------------------------------------------------
    //                                     TOOLBAR
    // ---------------------------------------------------------------------------------------------
    // >> CONFIGURATION <-------
    private void configureToolBar() {
        Log.d(TAG, "configureToolBar: ");
        // Get the toolbar view inside the activity layout

        // Change the toolbar Tittle
        setTitle("My News");
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

    // >> ACTIONS <-------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected() called with: item = [" + item + "]");
        // Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.activity_main_menu_toolbar_search:
                callSearchActivity();
                return true;
            case R.id.activity_main_menu_toolbar_overflow_notifications:
                callNotificationsActivity();
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
    
    // Call Search Activity
    private void callSearchActivity() {
        Intent searchActivity = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(searchActivity);
    }
    
    // Call Notifications Activity
    private void callNotificationsActivity() {
        Intent notificationsActivity = new Intent(MainActivity.this, NotificationsActivity.class);
         startActivity(notificationsActivity);
    }
    // ---------------------------------------------------------------------------------------------
    //                                     NAVIGATION DRAWER
    // ---------------------------------------------------------------------------------------------
    // >> CONFIGURATION <-------
    // Configure Drawer Layout and connects him the ToolBar and the NavigationView
    private void configureDrawerLayout() {
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

    // Configure NavigationView
    private void configureNavigationMenuItem() {
        //Disable tint icons
        this.mNavigationView.setItemIconTintList(null);

        // The item menu selected take the color of the background
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},  // item checked
                new int[]{android.R.attr.state_enabled} // default state
        };
        colors = new int[]{
                getResources().getColor(R.color.topStoriesPrimary),
                getResources().getColor(android.R.color.black)
        };

        ColorStateList myList = new ColorStateList(states, colors);
        this.mNavigationView.setItemTextColor(myList);
        this.mNavigationView.setItemIconTintList(myList);
    }

    // >> ACTIONS <-------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: ");

        // Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_top_stories:
                // Change the color of the text menu item selected
                colors[0] = getResources().getColor(R.color.topStoriesPrimary);
                // Positioning the Top Stories Page
                mViewPager.setCurrentItem(FRAGMENT_TOP_STORIES);
                break;
            case R.id.activity_main_drawer_most_popular:
                // Change the color of the text menu item selected
                colors[0] = getResources().getColor(R.color.mostPopularPrimary);
                // Positioning the Most Popular Stories Page
                mViewPager.setCurrentItem(FRAGMENT_MOST_POPULAR);
                break;
            case R.id.activity_main_drawer_business:
                // Change the color of the text menu item selected
                colors[0] = getResources().getColor(R.color.businessPrimary);
                // Positioning the Business Page
                mViewPager.setCurrentItem(FRAGMENT_BUSINESS);
                break;
            case R.id.activity_main_drawer_sports:
                // Change the color of the text menu item selected
                colors[0] = getResources().getColor(R.color.sportsPrimary);
                // Positioning the Sports Page
                mViewPager.setCurrentItem(FRAGMENT_SPORTS);
                break;
            case R.id.activity_main_drawer_article_search:
                // Change the color of the text menu item selected
                colors[0] = getResources().getColor(R.color.searchPrimary);
                // Call Article Search Activity
                callSearchActivity();
                break;
            case R.id.activity_main_drawer_notification:
                // Change the color of the text menu item selected
                colors[0] = getResources().getColor(R.color.notificationsPrimary);
                // Call Notification Activity
                callNotificationsActivity();
                break;
            default:
                break;
        }

        // Close menu drawer
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
    private void configureViewPagerAndTabs() {
        Log.d(TAG, "configureViewPagerAndTabs: ");
        this.configureViewPager();
        this.configureTabLayout();
    }

    // ---------------------------------------------------------------------------------------------
    //                                     VIEW PAGER
    // ---------------------------------------------------------------------------------------------
    // >> CONFIGURATION <-------
    private void configureViewPager() {
        Log.d(TAG, "configureViewPagerAndTabs: ");
        // Set Adapter PageAdapter and glue it together
        mViewPager.setAdapter(new PageAdapter(getSupportFragmentManager()) {
        });
        // Number of pages initially created 3 ( page 0 1 2 )
        // 2 by default ( page 0 1 ) if the parameter is not used
        // to avoid the code http 429 on the API searchArticle ( if business & Sports requests are executed in parallel )
        mViewPager.setOffscreenPageLimit(2);
    }

    // ---------------------------------------------------------------------------------------------
    //                                     TAB LAYOUT
    // ---------------------------------------------------------------------------------------------
    // >> CONFIGURATION <-------
    private void configureTabLayout() {
        Log.d(TAG, "configureTabLayout() called");
        // Glue TabLayout and ViewPager together
        mTabLayout.setupWithViewPager(mViewPager);
        // Design purpose. Tabs have the same width
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // Subscribes to listen the tab layout
        mTabLayout.addOnTabSelectedListener(this);
    }

    // >> ACTIONS <-------
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d(TAG, "onTabSelected() called with: tab = [" + tab + "]");
        int position = tab.getPosition();

        // Mark as selected the menu item corresponding to the current tab selected
        this.mNavigationView.getMenu().getItem(position).setChecked(true);

        // Change Color of the Tab selected
        this.mTabLayout.setBackgroundColor(getResources()
                .obtainTypedArray(R.array.primary_colors).getColor(position, 0));

        // Change Color of the Toolbar
        this.mToolbar.setBackgroundColor(getResources()
                .obtainTypedArray(R.array.primary_colors).getColor(position, 0));

        // Change Color of the Status Toolbar
        changeStatusBarColor(this, getResources()
                .obtainTypedArray(R.array.primaryDark_colors).getColor(position, 0));
    }
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}
    @Override
    public void onTabReselected(TabLayout.Tab tab) {}
}
