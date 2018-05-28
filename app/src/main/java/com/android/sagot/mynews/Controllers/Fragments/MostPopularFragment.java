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

        // Cast Object news in NYTimesMostPopular
        NYTimesMostPopular newsMostPopular = (NYTimesMostPopular)news;

        //Here we recover only the elements of the query that interests us
        String imageURL;
        String newsURL;
        for (ResultMostPopular results : newsMostPopular.getResults()){

            // Initialize blank URL
            imageURL = "";

            // Affected newsURL
            newsURL = results.getUrl();
            Log.d(TAG, "updateUIWithListOfNews: newsURL = "+newsURL);

            // Affected imageURL
            // Test if an image is present
            if (results.getMedia().size() != 0) {
                imageURL = results.getMedia().get(0).getMediaMetadata().get(0).getUrl();
            }

            // Affected section label ( section > subSection )
            String section = results.getSection();
            Log.d(TAG, "updateUIWithListOfNews: section = "+section);

            // Affected date label ( SSAAMMJJ )  to sort out the list of news later
            String newsDate = DateUtilities.dateReformatSSAAMMJJ(results.getPublishedDate());

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

        // Save the News in the Model
        Model.getInstance().setListMostPopularNews(mListNYTimesNews);

        // Recharge Adapter
        mNYTimesNewsAdapter.notifyDataSetChanged();
    }
}
