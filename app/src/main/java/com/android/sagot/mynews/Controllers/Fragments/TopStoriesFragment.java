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
import com.android.sagot.mynews.Models.NYTimesStreams.NYTimesTopStories;
import com.android.sagot.mynews.Models.NYTimesStreams.Result;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.DateUtilities;
import com.android.sagot.mynews.Utils.ItemClickSupport;
import com.android.sagot.mynews.Utils.NYTimesStreams;
import com.android.sagot.mynews.Views.NYTimesNewsAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 *  TopStoriesFragment
 */
public class TopStoriesFragment extends Fragment {

    private static final String TAG = TopStoriesFragment.class.getSimpleName();

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

    public static final String BUNDLE_NEWS_URL = "BUNDLE_NEWS_URL";

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

        // Calling the method that configuring click on RecyclerView
        this.configureOnClickRecyclerView();

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
                executeHttpRequestWithRetrofit();
            }
        });
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------
    /**
     *  Execute Stream " NYTimesStreams.streamFetchTopStories "
     */
    private void executeHttpRequestWithRetrofit(){

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        this.disposable = NYTimesStreams.streamFetchTopStories("home").subscribeWith(new DisposableObserver<NYTimesTopStories>() {
            @Override
            public void onNext(NYTimesTopStories topStories) {
                Log.e("TAG","On Next");
                // Update UI with list of TopStories news
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
    /**
     *  Unsubscribe the stream when the fragment is destroyed so as not to create a memory leaks
     */
    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    // -------------------
    //     UPDATE UI
    // -------------------
    /**
     *  Generate a toast Message if error during Downloading
     */
    private void updateUIWhenErrorHTTPRequest(){
        Toast.makeText(getActivity(), "Error during Downloading", Toast.LENGTH_LONG).show();
    }
    /**
     *  Update UI with list of TopStories news
     *
     * @param topStories
     *              list of news topStories of the NewYorkTimes
     */
    private void updateUIWithListOfNews(NYTimesTopStories topStories){

        // Stop refreshing and clear actual list of news
        swipeRefreshLayout.setRefreshing(false);
        // Empty the list of previous news
        mListNYTimesNews.clear();

        //Here we recover only the elements of the query that interests us
        String imageURL;
        String newsURL;
        for (Result news : topStories.getResults()){

            // Initialize blank URL
            imageURL = "";

            // Affected newsURL
            newsURL = news.getUrl();

            // Affected imageURL
            // Test if an image is present
            if (news.getMultimedia().size() != 0) {
                imageURL = news.getMultimedia().get(0).getUrl();
            }

            // Affected section label ( section > subSection )
            String section = news.getSection();
            if (!news.getSubsection().equals("") ) section = section+" > "+news.getSubsection();

            // Affected date label ( JJ/MM/AA )
            String newsDate = DateUtilities.dateReformat(news.getCreatedDate());
            mListNYTimesNews.add( new NYTimesNews(news.getTitle(),
                                                imageURL,
                                                newsURL,
                                                newsDate,
                                                section
                                                ));
        }
        // Sort the newsList by createdDate in Descending
        Collections.sort(mListNYTimesNews,new NYTimesNews());
        Collections.reverse(mListNYTimesNews);
        
        // Recharge Adapter
        mNYTimesNewsAdapter.notifyDataSetChanged();
    }
}
