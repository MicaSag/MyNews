package com.android.sagot.mynews.Controllers.Fragments;


import android.os.Bundle;
import android.util.Log;

import com.android.sagot.mynews.Models.Criteria;
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
 * Business FRAGMENT
 */
public class BusinessFragment extends BaseNewsFragment {

    // For debug
    private static final String TAG = BusinessFragment.class.getSimpleName();

    public BusinessFragment() {
        // Required empty public constructor
    }

    public static BusinessFragment newInstance(int tabLayoutPosition) {

        // Create new fragment
        BusinessFragment fragment = new BusinessFragment();

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
        return Model.getInstance().getListBusinessNews();
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------
    /**
     *  Formatting Request for Stream " NYTimesStreams.streamFetchArticleSearch "
     */
    protected  Map<String, String> formattingRequest() {

        // Create criteria Object
        Criteria criteria = new Criteria();
        // Set a Business search
        criteria.setBusiness(true);
        // Create a new request and put criteria
        NYTimesRequest request = new NYTimesRequest();
        request.createQuery(criteria);
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
        mDisposable = NYTimesStreams.streamFetchArticleSearch(api_key,this.formattingRequest())
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
    // Create list of news to display
    @Override
    protected void createListNYTimesNews(Object news) {
        NYTimesNewsList.createListArticleSearch(mListNYTimesNews,(NYTimesArticleSearch)news);
    }

    // --------------
    //    ( OUT )
    // --------------
    // Save the list of Business in the Model
    @Override
    protected void setListNYTimesNewsInTheModel(List<NYTimesNews> newsList) {
        Model.getInstance().setListBusinessNews(newsList);
    }
}
