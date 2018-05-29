package com.android.sagot.mynews.Controllers.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.sagot.mynews.Controllers.Activities.ItemSearchActivity;
import com.android.sagot.mynews.Utils.NYTimesNewsList;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.Utils.NYTimesRequest;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * Search FRAGMENT
 */
public class SearchFragment extends BaseNewsFragment {

    // For debug
    private static final String TAG = SearchFragment.class.getSimpleName();

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(int tabLayoutPosition) {

        // Create new fragment
        SearchFragment fragment = new SearchFragment();

        // Create bundle and add it some data
        Bundle args = new Bundle();
        // Put tabLayoutPosition
        args.putInt(BUNDLE_TAB_LAYOUT_POSITION, tabLayoutPosition);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void launchItemActivity(int position) {
        Intent myIntent = new Intent(getActivity(), ItemSearchActivity.class);
        myIntent.putExtra(BUNDLE_NEWS_URL,mListNYTimesNews.get(position).getNewsURL());
        myIntent.putExtra(BUNDLE_TAB_LAYOUT_POSITION,mTabLayoutPosition);
        this.startActivity(myIntent);
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------
    /**
     *  Formatting Request for Stream " NYTimesStreams.streamFetchArticleSearch "
     */
     protected  Map<String, String> formattingRequest() {

         // Create a new request and put criteria
         NYTimesRequest request = new NYTimesRequest();
         request.createNYTimesRequest(Model.getInstance().getDataModel().getSearchCriteria());
         request.addDateCriteriaToRequest(Model.getInstance().getDataModel().getSearchCriteria()
                                .getBeginDate(), "BeginDate");
         request.addDateCriteriaToRequest(Model.getInstance().getDataModel().getSearchCriteria()
                                .getEndDate(), "EndDate");
         // Display request
         request.displayRequest();

         return request.getFilters();
     }
    /**
     *  Execute Stream " NYTimesStreams.streamFetchArticleSearch "
     */
    @Override
    protected void executeHttpRequestWithRetrofit() {

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        mDisposable = NYTimesStreams.streamFetchArticleSearch(api_key,  this.formattingRequest())
                .subscribeWith(new DisposableObserver<NYTimesArticleSearch>() {
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
     *  Update UI with list of Business news
     *
     * @param news
     *              list of news Business of the NewYorkTimes
     */
    protected void updateUIWithListOfNews(Object news) {

        // Stop refreshing
        swipeRefreshLayout.setRefreshing(false);

        // Empty the list of previous news
        mListNYTimesNews.clear();

        // Create list of the article to be display
        NYTimesNewsList newsList = new NYTimesNewsList();
        newsList.createListArticleSearch(mListNYTimesNews,(NYTimesArticleSearch)news);

        // Recharge Adapter
        mNYTimesNewsAdapter.notifyDataSetChanged();

        Log.d(TAG, "updateUIWithListOfNews: meta:hits = "+((NYTimesArticleSearch) news).getResponse().getMeta().getHits());
        Log.d(TAG, "updateUIWithListOfNews: meta:hits = "+((NYTimesArticleSearch) news).getResponse().getMeta().getOffset());
        Log.d(TAG, "updateUIWithListOfNews: meta:hits = "+((NYTimesArticleSearch) news).getResponse().getMeta().getTime());
    }
}
