package com.android.sagot.mynews.Controllers.Fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.sagot.mynews.Controllers.Activities.WebViewActivity;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.ItemClickSupport;
import com.android.sagot.mynews.Utils.UIUtilities;
import com.android.sagot.mynews.Views.NYTimesNewsAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import static com.android.sagot.mynews.Utils.UIUtilities.isNetworkAvailable;

/**
 *  BASE FRAGMENT
 */
public abstract class BaseNewsFragment extends Fragment {

    // Force developer implement those methods
    protected abstract void executeHttpRequestWithRetrofit();                          // Execute the request of the NYTimes
    protected abstract void createListNYTimesNews(Object news);                        // Create list of news to display
    protected abstract List<NYTimesNews> getListNYTimesNewsOfTheModel();               // Get list of news of the Model
    protected abstract void setListNYTimesNewsInTheModel(List<NYTimesNews> newsList);  // Set list of news in the Model

    // FOR TRACES
    private static final String TAG = BaseNewsFragment.class.getSimpleName();

    // View of the Fragment
    private View mNewsView;

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.fragment_news_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.fragment_news_swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fragment_news_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.fragment_news_coordinator_layout) CoordinatorLayout mCoordinatorLayout;

    // Declare Subscription
    protected Disposable mDisposable;

    // Declare list of news ( NewsList ) & Adapter
    protected List<NYTimesNews> mListNYTimesNews;
    protected NYTimesNewsAdapter mNYTimesNewsAdapter;

    // Position of the fragment in the tabLayout
    int mTabLayoutPosition;

    // Static variables for intent parameters
    public static final String BUNDLE_NEWS_URL = "BUNDLE_NEWS_URL";
    public static final String BUNDLE_TAB_LAYOUT_POSITION = "BUNDLE_POSITION";

    // Key for NYTimes Api access
    protected String api_key;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mNewsView = inflater.inflate(R.layout.fragment_news_base, container, false);

        // Telling ButterKnife to bind all views in layout
        ButterKnife.bind(this, mNewsView);
        
        // ProgressBar configuration
        mProgressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark),
                        PorterDuff.Mode.SRC_IN);

        // Get data from Bundle (created in method newInstance)
        mTabLayoutPosition = getArguments().getInt(BUNDLE_TAB_LAYOUT_POSITION, 4);

        // Get api_key
        api_key = getResources().getString(R.string.api_key);
        
        // If we have not yet interrogate the NYTimes database, we do it
        this.mListNYTimesNews = getListNYTimesNewsOfTheModel();
        if (mListNYTimesNews == null) {
            Log.d(TAG, "onCreateView: mListNYTimesNews = "+mListNYTimesNews);
            this.mListNYTimesNews = new ArrayList<>(); // Reset list
            if (this.testConnectivity()) {
                Log.d(TAG, "onCreateView: CONNECTION OK");
                mProgressBar.setVisibility(View.VISIBLE);  // Display ProgressBar
                this.executeHttpRequestWithRetrofit();     // Call the Stream of the New York Times
            }else{
                Log.d(TAG, "onCreateView: CONNEXION KOOOOOOOOO");
            }
        } else {
            Log.d(TAG, "onCreateView: mListNYTimesNews <> 0 : "
                    + getListNYTimesNewsOfTheModel().getClass().getSimpleName());
        }

        // Configure RecyclerView
        this.configureRecyclerView();

        // Configure the SwipeRefreshLayout
        this.configureSwipeRefreshLayout();

        // Calling the method that configuring click on RecyclerView
        this.configureOnClickRecyclerView();

        return mNewsView;
    }
    
    // Checking whether network is connected
    private boolean testConnectivity() {
        if (!isNetworkAvailable(getContext())) {
            Snackbar.make(getActivity().findViewById(R.id.activity_main_coordinator_layout),
                    "Not Connected",Snackbar.LENGTH_LONG).show();
            return false;
        }
        else return true;
    }

    // -----------------
    //     ACTIONS
    // -----------------
    //  Configure clickListener on Item of the RecyclerView
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_recycler_view_item)
        .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // If not connexion, not article view
                if (testConnectivity()) {
                    // If we have not already read
                    if (!mListNYTimesNews.get(position).isEverRead()) {
                        // Save in the model, the URL of the article in the list of articles already read
                        Model.getInstance().getSavedModel().getListUrlArticleRead()
                                .add(mListNYTimesNews.get(position).getNewsURL());
                        // Set at True everRead in the list of articles
                        mListNYTimesNews.get(position).setEverRead(true);
                        // Recharge RecyclerView Adapter
                        // for taking into account thr item everRead
                        mNYTimesNewsAdapter.notifyDataSetChanged();
                    }
                    //Launch WebView Activity
                    callWebViewActivity(mListNYTimesNews.get(position).getNewsURL(),
                            mTabLayoutPosition);
                }
            }
        });
    }
     // Launch WebViewActivity
     // Param : 1 _ Url to display
     //         2 _ Position of the Item in the RecyclerView
    protected void callWebViewActivity(String url, int position){
        Intent myIntent = new Intent(getActivity(), WebViewActivity.class);
        myIntent.putExtra(BUNDLE_NEWS_URL,url);
        myIntent.putExtra(BUNDLE_TAB_LAYOUT_POSITION,position);
        this.startActivity(myIntent);
    }

    // ---------------
    // CONFIGURATION
    // ---------------
    //Configure RECYCLER VIEW, ADAPTER, LAYOUT_MANAGER & glue it together
    private void configureRecyclerView(){
        // Add separator between items
        this.mRecyclerView.addItemDecoration(new UIUtilities.DividerItemDecoration(getActivity()));
        // Create adapter passing the list of users
        this.mNYTimesNewsAdapter = new NYTimesNewsAdapter(this.mTabLayoutPosition,
                this.mListNYTimesNews, Glide.with(this));
        // Attach the adapter to the recyclerView to populate items
        this.mRecyclerView.setAdapter(this.mNYTimesNewsAdapter);
        // Set layout manager to position the items
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    // Configure the SwipeRefreshLayout
    private void configureSwipeRefreshLayout(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (testConnectivity()) {
                    Log.d(TAG, "onCreateView: CONNECTION OK");
                    executeHttpRequestWithRetrofit();     // Call the Stream of the New York Times
                }else{
                    Log.d(TAG, "onCreateView: CONNEXION KOOOOOOOOO");
                    // Stop refreshing
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    // -------------------
    //     UPDATE UI
    // -------------------
    // Update UI with list of news
    protected void updateUIWithListOfNews(Object news) {

        // Stop refreshing
        mSwipeRefreshLayout.setRefreshing(false);
        
        // Hidden ProgressBar
        mProgressBar.setVisibility(View.GONE);

        // Empty the list of previous news
        mListNYTimesNews.clear();

        // Create list of the article to be display
        createListNYTimesNews(news);

        // Save the News in the Model
        setListNYTimesNewsInTheModel(mListNYTimesNews);

        // Recharge Adapter
        mNYTimesNewsAdapter.notifyDataSetChanged();
    }
    
    // Generate a toast Message if error during Downloading
    protected void updateUIWhenErrorHTTPRequest(){
        // Stop refreshing
        mSwipeRefreshLayout.setRefreshing(false);
        // Hidden ProgressBar
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Error during Downloading", Toast.LENGTH_LONG).show();
    }
    
    // -----------------
    //     ( OUT )
    // ----------------- 
    @Override
    public void onDestroy() {
        //Unsubscribe the stream when the fragment is destroyed so as not to create a memory leaks
        this.disposeWhenDestroy();
        super.onDestroy();
    }
    
    //  Unsubscribe the stream when the fragment is destroyed so as not to create a memory leaks
    private void disposeWhenDestroy(){
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }
}
