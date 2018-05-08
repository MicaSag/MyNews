package com.android.sagot.mynews.Controllers.Fragments;


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

import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.NYTimesTopStories;
import com.android.sagot.mynews.Models.NYTimesStreams.Result;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.NYTimesStreams;
import com.android.sagot.mynews.Views.NYTimesNewsAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * TopStoriesFragment
 */
public class TopStoriesFragment extends Fragment {

    // View of the Fragment
    private View mTopStoriesView;

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.fragment_top_stories_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.fragment_top_stories_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    // Declare Subscription
    private Disposable disposable;

    // Declare list of news ( NYTimesNews ) & Adapter
    private List<NYTimesNews> mListNYTimesNews;
    private NYTimesNewsAdapter mNYTimesNewsAdapter;

    public TopStoriesFragment() {
        // Required empty public constructor
    }

    // Method that will create a new instance of TopStoriesFragment, and add data to its bundle.
    public static TopStoriesFragment newInstance() {
        return(new TopStoriesFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mTopStoriesView = inflater.inflate(R.layout.fragment_top_stories, container, false);

        // Telling ButterKnife to bind all views in layout
        ButterKnife.bind(this, mTopStoriesView);

        // Configure RecyclerView
        this.configureRecyclerView();

        // Configure the SwipeRefreshLayout
        this.configureSwipeRefreshLayout();

        // Call the Stream Top Stories of the New York Times
        this.executeHttpRequestWithRetrofit();

        return mTopStoriesView;
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

    // -----------------
    // CONFIGURATION
    // -----------------

    // 3 - Configure RecyclerView, Adapter, LayoutManager & glue it together
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

    // Configure the SwipeRefreshLayout
    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHttpRequestWithRetrofit();
            }
        });
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    // 1 - Execute our Stream
    private void executeHttpRequestWithRetrofit(){
        // Update UI
        this.updateUIWhenStartingHTTPRequest();
        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        this.disposable = NYTimesStreams.streamFetchTopStoriesFollowing("home").subscribeWith(new DisposableObserver<NYTimesTopStories>() {
            @Override
            public void onNext(NYTimesTopStories topStories) {
                Log.e("TAG","On Next");
                // Update UI with list of users
                updateUIWithListOfNews(topStories);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
                updateUIWhenErrorHTTPRequest();
            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");
            }
        });
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUIWhenStartingHTTPRequest(){
        //Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_LONG).show();
    }

    private void updateUIWhenErrorHTTPRequest(){
        Toast.makeText(getActivity(), "Error during Downloading", Toast.LENGTH_LONG).show();
    }

    private void updateUIWithListOfNews(NYTimesTopStories topStories){

        // Stop refreshing and clear actual list of news
        swipeRefreshLayout.setRefreshing(false);
        // Empty the list of previous news
        mListNYTimesNews.clear();

        //Here we recover only the elements of the query that interests us
        String imageURL;
        for (Result news : topStories.getResults()){
            imageURL = "";
            // Test if an image is present
            if (news.getMultimedia().size() != 0) {
                imageURL = news.getMultimedia().get(0).getUrl();
            }
            mListNYTimesNews.add(new NYTimesNews(news.getTitle(),imageURL));
        }
        // Recharge Adapter
        mNYTimesNewsAdapter.notifyDataSetChanged();
    }
}
