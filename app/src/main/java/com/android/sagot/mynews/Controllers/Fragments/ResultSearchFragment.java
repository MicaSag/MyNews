package com.android.sagot.mynews.Controllers.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.sagot.mynews.Controllers.Activities.ItemActivity;
import com.android.sagot.mynews.Controllers.Activities.SearchActivity;
import com.android.sagot.mynews.Controllers.Activities.SearchItemActivity;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.Doc;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.Models.SearchCriteria;
import com.android.sagot.mynews.Utils.DateUtilities;
import com.android.sagot.mynews.Utils.NYTimesStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * Search FRAGMENT
 */
public class ResultSearchFragment extends NewsFragment {

    // For debug
    private static final String TAG = ResultSearchFragment.class.getSimpleName();

    // Object containing the criteria of search
    private SearchCriteria mSearchCriteria;

    public ResultSearchFragment() {
        // Required empty public constructor
    }

    public static ResultSearchFragment newInstance(int tabLayoutPosition, String searchCriteria) {

        // Create new fragment
        ResultSearchFragment fragment = new ResultSearchFragment();

        // Create bundle and add it some data
        Bundle args = new Bundle();
        // Put searchCriteria in bundle
        args.putString(SearchActivity.BUNDLE_SEARCH_CRITERIA, searchCriteria);
        // Put tabLayoutPosition
        args.putInt(BUNDLE_TAB_LAYOUT_POSITION, tabLayoutPosition);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void launchItemActivity(int position) {
        Intent myIntent = new Intent(getActivity(), SearchItemActivity.class);
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
        // Get data from Bundle (created in method newInstance)
        // and restoring the searchCriteria with a Gson Object
        final Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
        mSearchCriteria = gson.fromJson(getArguments()
                .getString(SearchActivity.BUNDLE_SEARCH_CRITERIA),SearchCriteria.class);

        Map<String, String> filters = new HashMap<>(); // Filters following conditions

         // Query criteria
         filters.put("q", mSearchCriteria.getSearchQueryTerm());
         Log.d(TAG, "formattingRequest: getSearchQueryTerm = "+mSearchCriteria.getSearchQueryTerm());

         // Sections criteria
        String sections = "news_desk:(";
        if(mSearchCriteria.isArts()) sections+=" \"Arts\"";
        if(mSearchCriteria.isBusiness()) sections+=" \"Business\"";
        if(mSearchCriteria.isEntrepreneurs()) sections+=" \"Entrepreneurs\"";
        if(mSearchCriteria.isPolitics()) sections+=" \"Politics\"";
        if(mSearchCriteria.isSports()) sections+=" \"Sports\"";
        if(mSearchCriteria.isTravel()) sections+=" \"Travel\"";
        sections +=")";
        Log.d(TAG, "executeHttpRequestWithRetrofit: sections = "+sections);
        filters.put("fq", sections);

        //  Dates criteria
        if (mSearchCriteria.getBeginDate() != null) filters.put("begin_date", mSearchCriteria.getBeginDate());
        if (mSearchCriteria.getEndDate() != null) filters.put("end_date", mSearchCriteria.getEndDate());

        return filters;
     }
    /**
     *  Execute Stream " NYTimesStreams.streamFetchArticleSearch "
     */
    @Override
    protected void executeHttpRequestWithRetrofit() {

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        mDisposable = NYTimesStreams.streamFetchArticleSearch(api_key,  this.formattingRequest()).subscribeWith(new DisposableObserver<NYTimesArticleSearch>() {
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

        Log.d(TAG, "updateUIWithListOfNews: meta:hits = "+((NYTimesArticleSearch) news).getResponse().getMeta().getHits());
        Log.d(TAG, "updateUIWithListOfNews: meta:hits = "+((NYTimesArticleSearch) news).getResponse().getMeta().getOffset());
        Log.d(TAG, "updateUIWithListOfNews: meta:hits = "+((NYTimesArticleSearch) news).getResponse().getMeta().getTime());

        // Recharge Adapter
        mNYTimesNewsAdapter.notifyDataSetChanged();
    }
}
