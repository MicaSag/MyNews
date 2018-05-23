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
import android.widget.Toast;

import com.android.sagot.mynews.Adapters.PageAdapter;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.Preferences;
import com.android.sagot.mynews.Models.SearchCriteria;
import com.android.sagot.mynews.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.sagot.mynews.Utils.UIUtilities.changeStatusBarColor;
import static junit.framework.Assert.assertSame;


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
    public static final String SHARED_PREF_MODELS = "SHARED_PREF_MODELS";

    // Defined Preferences of the application
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

        // Retrieves the Preferences
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
    private void retrievesPreferences() {
        Log.d(TAG, "retrievesPreferences: ");
        // INSTANTIATE Preferences Object in the Model Singleton
        Model.getInstance().setPreferences(new Preferences());

        // READ SharedPreferences
        mSharedPreferences = getPreferences(MODE_PRIVATE);
        // TEST == >>> Allows to erase all the preferences ( Useful for the test phase )
        //Log.i("MOOD","CLEAR COMMIT PREFERENCES");
        //mSharedPreferences.edit().clear().commit();

        //Model retrieves
        this.retrieveModels();
    }

    // SEARCH_CRITERIA RETRIEVES
    private void retrieveModels() {
        Log.d(TAG, "retrieveModels: ");
        String modelPreferences = mSharedPreferences.getString(SHARED_PREF_MODELS, null);
        // Restoring the preferences with a Gson Object
        Gson gson = new Gson();

        if (modelPreferences != null) {
            Log.d(TAG, "retrievesPreferences: model Restoration");
            // Retrieves the modelPreferences of the SharedPreferences
            Model.getInstance().setPreferences(gson.fromJson(modelPreferences, Preferences.class));
        } else {
            Log.d(TAG, "retrievesPreferences: model Creation");
            Model.getInstance().setPreferences(new Preferences());
        }

        // If searchCriteria not exist then instantiate it
        if (Model.getInstance().getPreferences().getSearchCriteria() == null)
            Model.getInstance().getPreferences().setSearchCriteria(new SearchCriteria());

        displaySearchCriteria();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        // SAVE MODELS PREFERENCES IN THE SHARED_PREFERENCES
        // Create à SHARED_PREF_MODELS String with a Gson Object
        this.displaySearchCriteria();
        final Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(Model.getInstance().getPreferences());

        // Add the Model Preferences in shared Preferences
        mSharedPreferences.edit().putString(SHARED_PREF_MODELS, json).apply();

        super.onStop();
    }

    // ---------------------------------------------------------------------------------------------
    //                                     TOOLBAR
    // ---------------------------------------------------------------------------------------------
    // ---------------------
    // CONFIGURATION
    // ---------------------
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

    // ---------------------
    // ACTIONS
    // ---------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected() called with: item = [" + item + "]");
        // Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.activity_main_menu_toolbar_search:
                Intent searchActivity = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchActivity);
                return true;
            case R.id.activity_main_menu_toolbar_overflow_notifications:
                //searchActivity = new Intent(MainActivity.this, SearchActivity.class);
                //startActivity(searchActivity);
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

    // ---------------------
    // ACTIONS
    // ---------------------
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
    // ---------------------
    // CONFIGURATION
    // ---------------------
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
    // ---------------------
    // CONFIGURATION
    // ---------------------
    private void configureTabLayout() {
        Log.d(TAG, "configureTabLayout() called");
        // Glue TabLayout and ViewPager together
        mTabLayout.setupWithViewPager(mViewPager);
        // Design purpose. Tabs have the same width
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // Subscribes to listen the tab layout
        mTabLayout.addOnTabSelectedListener(this);
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
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    private void displaySearchCriteria() {
        Log.d(TAG, "displaySearchCriteria: Query               = " + Model.getInstance().getPreferences().getSearchCriteria().getSearchQueryTerm());
        Log.d(TAG, "displaySearchCriteria: checkArts           = " + Model.getInstance().getPreferences().getSearchCriteria().isArts());
        Log.d(TAG, "displaySearchCriteria: checkBusiness       = " + Model.getInstance().getPreferences().getSearchCriteria().isBusiness());
        Log.d(TAG, "displaySearchCriteria: checkEntrepreneurs  = " + Model.getInstance().getPreferences().getSearchCriteria().isEntrepreneurs());
        Log.d(TAG, "displaySearchCriteria: checkPolitics       = " + Model.getInstance().getPreferences().getSearchCriteria().isPolitics());
        Log.d(TAG, "displaySearchCriteria: checkSports         = " + Model.getInstance().getPreferences().getSearchCriteria().isSports());
        Log.d(TAG, "displaySearchCriteria: checkTravels        = " + Model.getInstance().getPreferences().getSearchCriteria().isTravel());
        Log.d(TAG, "displaySearchCriteria: Begin Date          = " + Model.getInstance().getPreferences().getSearchCriteria().getBeginDate());
        Log.d(TAG, "displaySearchCriteria: End Date            = " + Model.getInstance().getPreferences().getSearchCriteria().getEndDate());
    }
}