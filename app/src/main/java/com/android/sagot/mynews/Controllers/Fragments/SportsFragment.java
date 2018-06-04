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
 * Sports FRAGMENT
 */
public class SportsFragment extends BaseNewsFragment {

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

    // --------------
    //    ( IN )
    // --------------
    // BASE METHOD Implementation
    // Get the list of Business news saved in the Model
    // CALLED BY BASE METHOD 'onCreateView(...)'
    @Override
    protected List<NYTimesNews> getListNYTimesNewsOfTheModel() {
        return Model.getInstance().getListSportsNews();
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------
    //  Formatting Request for Stream " NYTimesStreams.streamFetchArticleSearch "
    protected  Map<String, String> formattingRequest() {

        // Create criteria Object
        Criteria criteria = new Criteria();
        // Set a Business search
        criteria.setSports(true);
        // Create a new request and put criteria
        NYTimesRequest request = new NYTimesRequest();
        request.createQuery(criteria);
        // FOR DEBUG : Display request
        request.displayQuery();

        return request.getQuery();
    }
    
    //  BASE METHOD Implementation
    //  Execute Stream " NYTimesStreams.streamFetchArticleSearch "
    // CALLED BY BASE METHOD 'updateUIWithListOfNews((...)'
    @Override
    protected void executeHttpRequestWithRetrofit() {

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        mDisposable = NYTimesStreams.streamFetchArticleSearch(api_key, this.formattingRequest())
                .subscribeWith(new DisposableObserver<NYTimesArticleSearch>() {
            @Override
            public void onNext(NYTimesArticleSearch articleSearch) {
                Log.d(TAG,"On Next");
                // CALL BASE METHOD : Update UI with list of news
                updateUIWithListOfNews(articleSearch);
            }
            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"On Error"+Log.getStackTraceString(e));
                // CALL BASE METHOD : Generate a toast Message if error during Downloading
                updateUIWhenErrorHTTPRequest();
            }
            @Override
            public void onComplete() { Log.d(TAG,"On Complete !!"); }
        });
    }

    // --------------
    //   UPDATE UI
    // --------------
    // BASE METHOD Implementation
    // Create list of news to display
    // CALLED BY BASE METHOD 'updateUIWithListOfNews((...
    @Override
    protected void createListNYTimesNews(Object news) {
        NYTimesNewsList.createListArticleSearch(mListNYTimesNews,(NYTimesArticleSearch)news);
    }

    // --------------
    //    ( OUT )
    // --------------
    // BASE METHOD Implementation
    // Save the list of Business in the Model
    // CALLED BY BASE METHOD 'updateUIWithListOfNews((...
    @Override
    protected void setListNYTimesNewsInTheModel(List<NYTimesNews> newsList) {
        Model.getInstance().setListSportsNews(newsList);
    }
}
