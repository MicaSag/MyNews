package com.android.sagot.mynews.Controllers.Fragments;


import android.os.Bundle;
import android.util.Log;

import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.NYTimesTopStories;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.Result;
import com.android.sagot.mynews.Utils.DateUtilities;
import com.android.sagot.mynews.Utils.NYTimesNewsList;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import java.util.Collections;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 *  TopStories FRAGMENT
 */
public class TopStoriesFragment extends BaseNewsFragment {

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
    
    // --------------
    //    ( IN )
    // --------------
    // Get the list of TopStories news saved in the Model
    @Override
    protected List<NYTimesNews> getListNYTimesNewsOfTheModel() {
        return Model.getInstance().getListTopStoriesNews();
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
    // Create list of news to display
    @Override
    protected void createListNYTimesNews(Object news) {
        NYTimesNewsList.createListArticleTopStories(mListNYTimesNews,(NYTimesTopStories)news);  
    }
    
    // --------------
    //    ( OUT )
    // --------------
    // Save the list of TopStories in the Model
    @Override
    protected void setListNYTimesNewsInTheModel(List<NYTimesNews> newsList) {
        Model.getInstance().setListTopStoriesNews(newsList);
    }
}
