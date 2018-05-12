package com.android.sagot.mynews.Controllers.Fragments;


import android.util.Log;

import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.Doc;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.Utils.DateUtilities;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import java.util.Collections;

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

    public static NewsFragment newInstance() {
        return (new SportsFragment());
    }


    @Override
    protected void executeHttpRequestWithRetrofit() {

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        mDisposable = NYTimesStreams.streamFetchArticleSearch("api-key=de9402ab67114b3c8f08f3d58562b310").subscribeWith(new DisposableObserver<NYTimesArticleSearch>() {
        //mDisposable = NYTimesStreams.streamFetchArticleSearchSports().subscribeWith(new DisposableObserver<NYTimesArticleSearch>() {
            @Override
            public void onNext(NYTimesArticleSearch articleSearch) {
                Log.e("TAG","On Next");
                // Update UI with list of TopStories news
                updateUIWithListOfNews(articleSearch);
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

            // Affected date label ( JJ/MM/AA )
            String newsDate = DateUtilities.dateReformat(docs.getPubDate());

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

        // Recharge Adapter
        mNYTimesNewsAdapter.notifyDataSetChanged();
    }
}
