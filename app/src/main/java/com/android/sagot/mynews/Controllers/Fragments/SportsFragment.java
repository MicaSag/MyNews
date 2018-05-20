package com.android.sagot.mynews.Controllers.Fragments;


import android.os.Bundle;
import android.util.Log;

import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.Doc;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.DateUtilities;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * Sports FRAGMENT
 */
public class SportsFragment extends NewsFragment {

    // For debug
    private static final String TAG = SportsFragment.class.getSimpleName();

    public SportsFragment() {
        // Required empty public constructor
    }

    public static SportsFragment newInstance(int tabLayoutPosition) {

        // Create new fragment
        SportsFragment fragment = new SportsFragment();

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
     *  Execute Stream " NYTimesStreams.streamFetchArticleSearch "
     */
    @Override
    protected void executeHttpRequestWithRetrofit(int offset) {

        Map<String, String> filters = new HashMap<>(); // Filters following conditions
        filters.put("page", String.valueOf(offset));
        filters.put("fq", "news_desk:(\"Sports\")");
        filters.put("begin_date", "20180501");
        filters.put("end_date", "20180513");

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        mDisposable = NYTimesStreams.streamFetchArticleSearch(api_key, filters).subscribeWith(new DisposableObserver<NYTimesArticleSearch>() {
            @Override
            public void onNext(NYTimesArticleSearch articleSearch) {
                Log.d(TAG,"On Next");
                // Update UI with list of TopStories news
                updateUIWithListOfNews(articleSearch);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"On Error"+Log.getStackTraceString(e));
                updateUIWhenErrorHTTPRequest();
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"On Complete !!");
            }
        });
    }

    // -------------------
    //     UPDATE UI
    // -------------------
    /**
     *  Update UI with list of Sports news
     *
     * @param news
     *              list of news Sports of the NewYorkTimes
     */
    @Override
    protected void updateUIWithListOfNews(Object news) {
        // Stop refreshing and clear actual list of news
        swipeRefreshLayout.setRefreshing(false);
        // Empty the list of previous news

        mListNYTimesNews.clear();
        NYTimesArticleSearch newsArticleSearch = (NYTimesArticleSearch)news;

        //Here we recover only the elements of the query that interests us
        String imageURL;
        String newsURL;
        for (Doc docs : newsArticleSearch.getResponse().getDocs()){

            // Initialize blank URL
            imageURL = "";

            // Affected newsURL
            newsURL = docs.getWebUrl();
            Log.d(TAG, "updateUIWithListOfNews: newsURL = "+newsURL);

            // Affected imageURL
            // Test if an image is present
            if (docs.getMultimedia().size() != 0) {
                imageURL = "https://www.nytimes.com/"+docs.getMultimedia().get(0).getUrl();
            }

            // Affected section label ( section > subSection )
            String section = docs.getNewDesk();
            if (docs.getSectionName() != null ) section = section+" > "+docs.getSectionName();
            Log.d(TAG, "updateUIWithListOfNews: section = "+section);

            // Affected date label ( SSAAMMJJ )
            String newsDate = DateUtilities.dateReformatSSAAMMJJ(docs.getPubDate());

            // Affected Title
            String title = docs.getSnippet();

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

        // Save Offset of the Page
        Log.d(TAG, "updateUIWithListOfNews: offset file = "+newsArticleSearch.getResponse().getMeta().getOffset());

        offset +=1;

        // Recharge Adapter
        mNYTimesNewsAdapter.notifyDataSetChanged();
    }
}
