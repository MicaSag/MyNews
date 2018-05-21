package com.android.sagot.mynews.Controllers.Fragments;


import android.os.Bundle;
import android.util.Log;

import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.NYTimesTopStories;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.Result;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.DateUtilities;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import java.util.Collections;

import io.reactivex.observers.DisposableObserver;

/**
 *  TopStories FRAGMENT
 */
public class TopStoriesFragment extends NewsFragment{

    // For debug
    private static final String TAG = TopStoriesFragment.class.getSimpleName();

    public TopStoriesFragment() {
        // Required empty public constructor
    }

    // Method that will create a new instance of TopStoriesFragment, and add data to its bundle.
    public static TopStoriesFragment newInstance(int tabLayoutPosition) {

        // Create new fragment
        TopStoriesFragment fragment = new TopStoriesFragment();

        // Create bundle and add it some data
        Bundle args = new Bundle();
        args.putInt(BUNDLE_TAB_LAYOUT_POSITION, tabLayoutPosition);
        fragment.setArguments(args);

        return fragment;
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------
    /**
     *  Execute Stream " NYTimesStreams.streamFetchTopStories "
     */
    @Override
    protected void executeHttpRequestWithRetrofit(){

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        String section = "home";                      // Section of the new to return
        this.mDisposable = NYTimesStreams.streamFetchTopStories(section, api_key).subscribeWith(new DisposableObserver<NYTimesTopStories>() {
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

    // -------------------
    //     UPDATE UI
    // -------------------
    /**
     *  Update UI with list of TopStories news
     *
     * @param news
     *              list of news topStories of the NewYorkTimes
     */
    protected void updateUIWithListOfNews(Object news){

        // Stop refreshing and clear actual list of news
        swipeRefreshLayout.setRefreshing(false);
        // Empty the list of previous news
        mListNYTimesNews.clear();


        // Cast Object news in NYTimesMostPopular
        NYTimesTopStories newsTopStories = (NYTimesTopStories)news;

        //Here we recover only the elements of the query that interests us
        String imageURL;
        String newsURL;
        for (Result results : newsTopStories.getResults()){

            // Initialize blank URL
            imageURL = "";

            // Affected newsURL
            newsURL = results.getUrl();

            // Affected imageURL
            // Test if an image is present
            if (results.getMultimedia().size() != 0) {
                imageURL = results.getMultimedia().get(0).getUrl();
            }

            // Affected section label ( section > subSection )
            String section = results.getSection();
            if (!results.getSubsection().equals("") ) section = section+" > "+results.getSubsection();

            // Affected date label ( SSAAMMJJ ) to sort out the list of news later
            String newsDate = DateUtilities.dateReformatSSAAMMJJ(results.getCreatedDate());

            // Affected Title
            String title = results.getTitle();

            mListNYTimesNews.add( new NYTimesNews(title,
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
