package com.android.sagot.mynews.Controllers.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.sagot.mynews.Controllers.Activities.MainActivity;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.NYTimesNewsList;
import com.android.sagot.mynews.Utils.NYTimesRequest;
import com.android.sagot.mynews.Utils.NYTimesStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    // Method that will create a new instance ofSearchFragment and add data to its bundle.
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
    // BASE METHOD Implementation
    // Get the list of Top Stories news saved in the Model
    // CALLED BY BASE METHOD 'onCreateView(...)'
    @Override
    protected List<NYTimesNews> getListNYTimesNewsOfTheModel() {
        return Model.getInstance().getListSearchNews();
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------
    // Formatting Request for Stream " NYTimesStreams.streamFetchArticleSearch "
     protected  Map<String, String> formattingRequest() {

         // Create a new request and put criteria
         NYTimesRequest request = new NYTimesRequest();
         request.createQuery(Model.getInstance().getSavedModel().getSearchCriteria());
         request.addDateCriteriaToQuery(Model.getInstance().getSavedModel().getSearchCriteria()
                                .getBeginDate(), "BeginDate");
         request.addDateCriteriaToQuery(Model.getInstance().getSavedModel().getSearchCriteria()
                                .getEndDate(), "EndDate");
         // Display request
         request.displayQuery();

         return request.getQuery();
     }
    
    //  BASE METHOD Implementation
    //  Execute Stream " NYTimesStreams.streamFetchArticleSearch "
    // CALLED BY BASE METHOD 'updateUIWithListOfNews(...)'
    @Override
    protected void executeHttpRequestWithRetrofit() {

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        mDisposable = NYTimesStreams.streamFetchArticleSearch(api_key,  this.formattingRequest())
                .subscribeWith(new DisposableObserver<NYTimesArticleSearch>() {
            @Override
            public void onNext(NYTimesArticleSearch articleSearch) {
                // CALL BASE METHOD : Update UI with list of news
                updateUIWithListOfNews(articleSearch);
                Log.d(TAG, "onNext: ");
            }
            @Override
            public void onError(Throwable e) {
                // CALL BASE METHOD : Generate a toast Message if error during Downloading
                updateUIWhenErrorHTTPRequest();
                Log.d(TAG, "onError: ");
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
    // CALLED BY BASE METHOD 'updateUIWithListOfNews((...)'
    @Override
    protected void createListNYTimesNews(Object news) {
        NYTimesNewsList.createListArticleSearch(mListNYTimesNews,(NYTimesArticleSearch)news);
    }
    // BASE METHOD Implementation
    // Return coordinatorLayout of the parent activity
    // CALLED BY BASE METHOD 'testConnectivity()'
    @Override
    protected View getCoordinatorLayoutActivity() {
        return getActivity().findViewById(R.id.fragment_news_coordinator_layout);
    }
    
    // --------------
    //    ( OUT )
    // --------------
    // BASE METHOD Implementation
    // Save the list of Business in the Model
    // CALLED BY BASE METHOD 'updateUIWithListOfNews((...)'
    @Override
    protected void setListNYTimesNewsInTheModel(List<NYTimesNews> newsList) {
        Model.getInstance().setListSearchNews(newsList);
    }
}
