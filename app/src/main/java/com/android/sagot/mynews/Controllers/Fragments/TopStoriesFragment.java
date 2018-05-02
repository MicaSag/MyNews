package com.android.sagot.mynews.Controllers.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.sagot.mynews.Models.NYTimesStreams.NYTimesTopStories;
import com.android.sagot.mynews.Models.NYTimesStreams.Result;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopStoriesFragment extends Fragment {

    // View of the Fragment
    private View mTopStoriesView;

    // Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.fragment_top_stories_text) TextView mTextView;

    // Declare Subscription
    private Disposable disposable;


    public TopStoriesFragment() {
        // Required empty public constructor
    }

    // Method that will create a new instance of TopStoriesFragment, and add data to its bundle.
    public static TopStoriesFragment newInstance() {
        return(new TopStoriesFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mTopStoriesView = inflater.inflate(R.layout.fragment_top_stories, container, false);

        // Telling ButterKnife to bind all views in layout
        ButterKnife.bind(this, mTopStoriesView);

        // Call the Stream Top Stories of the New York Times
        this.executeHttpRequestWithRetrofit();

        return mTopStoriesView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Unsubscribe the stream when the fragment is destroyed so as not to create a memory leaks
        this.disposeWhenDestroy();
    }

    // -----------------
    // ACTIONS
    // -----------------


    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    // 1 - Execute our Stream
    private void executeHttpRequestWithRetrofit(){
        // Update UI
        this.updateUIWhenStartingHTTPRequest();
        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        this.disposable = NYTimesStreams.streamFetchTopStoriesFollowing("sports").subscribeWith(new DisposableObserver<NYTimesTopStories>() {
            @Override
            public void onNext(NYTimesTopStories topStories) {
                Log.e("TAG","On Next");
                // Update UI with list of users
                updateUIWithListOfUsers(topStories);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");
            }
        });
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUIWhenStartingHTTPRequest(){
        this.mTextView.setText("Downloading...");
    }

    private void updateUIWhenStopingHTTPRequest(String response){
        this.mTextView.setText(response);
    }

    private void updateUIWithListOfUsers(NYTimesTopStories topStories){
        StringBuilder stringBuilder = new StringBuilder();
        for (Result news : topStories.getResults()){

               stringBuilder.append("-"+news.getTitle()+"\n");
        }
        updateUIWhenStopingHTTPRequest(stringBuilder.toString());
    }
}
