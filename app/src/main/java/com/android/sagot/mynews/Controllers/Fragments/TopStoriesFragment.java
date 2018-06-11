package com.android.sagot.mynews.Controllers.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.NYTimesTopStories;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.NYTimesNewsList;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import java.util.List;

import io.reactivex.observers.DisposableObserver;
/**
 *  TopStories FRAGMENT
 */
public class TopStoriesFragment extends BaseNewsFragment {

    // For debug
    private static final String TAG = TopStoriesFragment.class.getSimpleName();

    public TopStoriesFragment() {
        // Required empty public constructor
    }

    // Method that will create a new instance of TopStoriesFragment, and add data to its bundle.
    public static TopStoriesFragment newInstance(int tabLayoutPosition) {
        // Create new fragment
        TopStoriesFragment fragment = new TopStoriesFragment();
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
    // Get the list of Top Stories news saved in the Model
    // CALLED BY BASE METHOD 'onCreateView(...)'
    @Override
    protected List<NYTimesNews> getListNYTimesNewsOfTheModel() {
        return Model.getInstance().getListTopStoriesNews();
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------
    //  BASE METHOD Implementation
    //  Execute Stream " NYTimesStreams.streamFetchTopStories "
    // CALLED BY BASE METHOD 'updateUIWithListOfNews((...)'
    @Override
    protected void executeHttpRequestWithRetrofit(){

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        String section = "home";                      // Section of the new to return
        this.mDisposable = NYTimesStreams.streamFetchTopStories(section, api_key)
                .subscribeWith(new DisposableObserver<NYTimesTopStories>() {
            @Override
            public void onNext(NYTimesTopStories topStories) {
                Log.e("TAG","On Next");
                // CALL BASE METHOD : Update UI with list of news
                updateUIWithListOfNews(topStories);
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
        NYTimesNewsList.createListArticleTopStories(mListNYTimesNews,(NYTimesTopStories)news);  
    }

    // BASE METHOD Implementation
    // Return coordinatorLayout of the parent activity
    // CALLED BY BASE METHOD 'testConnectivity()'
    @Override
    protected View getCoordinatorLayoutActivity() {
        return getActivity().findViewById(R.id.activity_main_coordinator_layout);
    }

    // --------------
    //    ( OUT )
    // --------------
    // BASE METHOD Implementation
    // Save the list of Business in the Model
    // CALLED BY BASE METHOD 'updateUIWithListOfNews(...)
    @Override
    protected void setListNYTimesNewsInTheModel(List<NYTimesNews> newsList) {
        Model.getInstance().setListTopStoriesNews(newsList);
    }
}
