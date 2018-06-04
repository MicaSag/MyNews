package com.android.sagot.mynews.Controllers.Fragments;

import android.os.Bundle;
import android.util.Log;

import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.MostPopular.NYTimesMostPopular;
import com.android.sagot.mynews.Models.NYTimesStreams.MostPopular.ResultMostPopular;
import com.android.sagot.mynews.Utils.DateUtilities;
import com.android.sagot.mynews.Utils.NYTimesNewsList;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import java.util.Collections;
import java.util.List;

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
    
    // --------------
    //    ( IN )
    // --------------
    // BASE METHOD Implementation
    // Get the list of Most Popular news saved in the Model
    // CALLED BY BASE METHOD 'onCreateView(...)
    @Override
    protected List<NYTimesNews> getListNYTimesNewsOfTheModel() {
        return Model.getInstance().getListMostPopularNews();
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------
    //  BASE METHOD Implementation
    //  Execute Stream " NYTimesStreams.streamFetchMostPopular "
    // CALLED BY BASE METHOD 'updateUIWithListOfNews((...)'
    @Override
    protected void executeHttpRequestWithRetrofit() {

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        mDisposable = NYTimesStreams.streamFetchMostPopular(api_key).subscribeWith(new DisposableObserver<NYTimesMostPopular>() {
            @Override
            public void onNext(NYTimesMostPopular mostPopular) {
                Log.e("TAG","On Next");
                // CALL BASE METHOD : Update UI with list of news
                updateUIWithListOfNews(mostPopular);
            }
            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
                // CALL BASE METHOD : Generate a toast Message if error during Downloading
                updateUIWhenErrorHTTPRequest();
            }
            @Override
            public void onComplete() { Log.e("TAG","On Complete !!"); }
        });
    }
    
    // --------------
    //   UPDATE UI
    // --------------
    // BASE METHOD Implementation
    // Create list of news to display
    // CALLED BY BASE METHOD 'updateUIWithListOfNews(...)
    @Override
    protected void createListNYTimesNews(Object news) {
        NYTimesNewsList.createListArticleMostPopular(mListNYTimesNews,(NYTimesMostPopular)news);  
    }

    // --------------
    //    ( OUT )
    // --------------
    // BASE METHOD Implementation
    // Save the list of Business in the Model
    // CALLED BY BASE METHOD 'updateUIWithListOfNews(...)
    @Override
    protected void setListNYTimesNewsInTheModel(List<NYTimesNews> newsList) {
        Model.getInstance().setListMostPopularNews(newsList);
    }
}
