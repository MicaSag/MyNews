package com.android.sagot.mynews.Controllers.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.sagot.mynews.Controllers.Activities.ItemActivity;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.ItemClickSupport;
import com.android.sagot.mynews.Views.NYTimesNewsAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 *  BASE FRAGMENT
 */
public abstract class NewsFragment extends Fragment {

    // Force developer implement those methods
    protected abstract void executeHttpRequestWithRetrofit(int offset);
    protected abstract void updateUIWithListOfNews(Object news);

    // FOR TRACES
    private static final String TAG = NewsFragment.class.getSimpleName();

    // View of the Fragment
    private View mNewsView;

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.fragment_news_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.fragment_news_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    // Declare Subscription
    protected Disposable mDisposable;

    // Declare list of news ( NewsList ) & Adapter
    protected List<NYTimesNews> mListNYTimesNews;
    protected NYTimesNewsAdapter mNYTimesNewsAdapter;

    public static final String BUNDLE_NEWS_URL = "BUNDLE_NEWS_URL";

    public int offset;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mNewsView = inflater.inflate(R.layout.fragment_news, container, false);

        // Telling ButterKnife to bind all views in layout
        ButterKnife.bind(this, mNewsView);

        // Configure RecyclerView
        this.configureRecyclerView();

        // Configure the SwipeRefreshLayout
        this.configureSwipeRefreshLayout();

        // Calling the method that configuring click on RecyclerView
        this.configureOnClickRecyclerView();

        // Call the Stream Top Stories of the New York Times
        this.executeHttpRequestWithRetrofit(offset);
        Log.d(TAG, "onCreateView: offset = "+offset);

        return mNewsView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Unsubscribe the stream when the fragment is destroyed so as not to create a memory leaks
        this.disposeWhenDestroy();
    }

    // -----------------
    // ACTIONS
    // -----------------
    /**
     *  Configure clickListener on Item of the RecyclerView
     */
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_recycler_view_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // Get news from adapter
                        NYTimesNews news = mNYTimesNewsAdapter.getNews(position);
                        //Launch Item Activity
                        launchItemActivity(position);
                    }
                });
    }
    /**
     *  Launch ItemActivity
     *
     * @param position
     *              Position of the Item in the RecyclerView
     */
    private void launchItemActivity(int position){
        Intent myIntent = new Intent(getActivity(), ItemActivity.class);
        myIntent.putExtra(BUNDLE_NEWS_URL,mListNYTimesNews.get(position).getNewsURL());
        this.startActivity(myIntent);
    }

    // -----------------
    // CONFIGURATION
    // -----------------
    /**
     *  Configure RecyclerView, Adapter, LayoutManager & glue it together
     */
    private void configureRecyclerView(){
        // Reset list
        this.mListNYTimesNews = new ArrayList<>();
        // Create adapter passing the list of users
        this.mNYTimesNewsAdapter = new NYTimesNewsAdapter(this.mListNYTimesNews, Glide.with(this));
        // Attach the adapter to the recyclerview to populate items
        this.mRecyclerView.setAdapter(this.mNYTimesNewsAdapter);
        // Set layout manager to position the items
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    /**
     *  Configure the SwipeRefreshLayout
     */
    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHttpRequestWithRetrofit(0);
            }
        });
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------
    /**
     *  Unsubscribe the stream when the fragment is destroyed so as not to create a memory leaks
     */
    private void disposeWhenDestroy(){
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

    // -------------------
    //     UPDATE UI
    // -------------------
    /**
     *  Generate a toast Message if error during Downloading
     */
    protected void updateUIWhenErrorHTTPRequest(){
        Toast.makeText(getActivity(), "Error during Downloading", Toast.LENGTH_LONG).show();

        // Stop refreshing
        swipeRefreshLayout.setRefreshing(false);
    }
}
