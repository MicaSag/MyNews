package com.android.sagot.mynews.Controllers.Fragments;


import android.os.Bundle;
import android.util.Log;

import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.MostPopular.NYTimesMostPopular;
import com.android.sagot.mynews.Models.NYTimesStreams.MostPopular.ResultMostPopular;
import com.android.sagot.mynews.Utils.DateUtilities;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import java.util.Collections;

import io.reactivex.observers.DisposableObserver;

/**
 * MostPopular FRAGMENT
 */
public class MostPopularFragment extends BaseNewsFragment {

    // For debug
    private static final String TAG = MostPopularFragment.class.getSimpleName();

    public MostPopularFragment() {
        // Required empty public constructor
    }

    // Method that will create a new instance of MostPopularFragment, and add data to its bundle.
    public static MostPopularFragment newInstance(int tabLayoutPosition) {

        // Create new fragment
        MostPopularFragment fragment = new MostPopularFragment();

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
     *  Execute Stream " NYTimesStreams.streamFetchMostPopularFragment "
     */
    @Override
    protected void executeHttpRequestWithRetrofit() {

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        mDisposable = NYTimesStreams.streamFetchMostPopular(api_key).subscribeWith(new DisposableObserver<NYTimesMostPopular>() {
            @Override
            public void onNext(NYTimesMostPopular mostPopular) {
                Log.e("TAG","On Next");
                // Update Model with list of TopStories news
                updateUIWithListOfNews(mostPopular);
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
     *  Update UI with list of MostPopular news
     *
     * @param news
     *              list of news MostPopular of the NewYorkTimes
     */
    protected void updateUIWithListOfNews(Object news) {

        // Stop refreshing
        swipeRefreshLayout.setRefreshing(false);

        // Empty the list of previous news
        mListNYTimesNews.clear();

        // Create list of the article to be display
        NYTimesNewsList.createListArticleMostPopular(mListNYTimesNews,(NYTimesMostPopular)news);

        // Save the News in the Model
        Model.getInstance().setListMostPopularNews(mListNYTimesNews);

        // Recharge Adapter
        mNYTimesNewsAdapter.notifyDataSetChanged();
    }
}
