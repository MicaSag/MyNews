package com.android.sagot.mynews.Controllers.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.sagot.mynews.Controllers.Activities.ItemSearchActivity;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Utils.NYTimesNewsList;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.Utils.NYTimesRequest;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import java.util.List;
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

    // --------------
    //    ( IN )
    // --------------
    // Get the list of Search news saved in the Model
    @Override
    protected List<NYTimesNews> getListNYTimesNewsOfTheModel() {
        return Model.getInstance().getListSearchNews();
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
         request.createQuery(Model.getInstance().getDataModel().getSearchCriteria());
         request.addDateCriteriaToQuery(Model.getInstance().getDataModel().getSearchCriteria()
                                .getBeginDate(), "BeginDate");
         request.addDateCriteriaToQuery(Model.getInstance().getDataModel().getSearchCriteria()
                                .getEndDate(), "EndDate");
         // Display request
         request.displayQuery();

         return request.getQuery();
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
                // Update UI with list of TopStories news
                updateUIWithListOfNews(articleSearch);
                Log.d(TAG, "onNext: ");
            }
            @Override
            public void onError(Throwable e) {
                // Display a toast message
                updateUIWhenErrorHTTPRequest();
                Log.d(TAG, "onError: ");
            }
            @Override
            public void onComplete() { Log.d(TAG,"On Complete !!"); }
        });
    }

    // Create list of news to display
    @Override
    protected void createListNYTimesNews(Object news) {
        NYTimesNewsList.createListArticleSearch(mListNYTimesNews,(NYTimesArticleSearch)news);
    }
    
    // --------------
    //    ( OUT )
    // --------------
    // Save the list of Search in the Model
    @Override
    protected void setListNYTimesNewsInTheModel(List<NYTimesNews> newsList) {
        Model.getInstance().setListSearchNews(newsList);
    }
}
