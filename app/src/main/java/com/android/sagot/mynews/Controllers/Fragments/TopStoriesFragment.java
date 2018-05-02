package com.android.sagot.mynews.Controllers.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.sagot.mynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
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

    // Method that will create a new instance of PageFragment, and add data to its bundle.
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
        this.streamShowString();

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

    @OnClick(R.id.fragment_top_stories_button)
    public void submit(View view) {
        this.streamShowString();
    }

    // ------------------------------
    //  Reactive X
    // ------------------------------

    // Create Observable
    private Observable<String> getObservable(){
        return Observable.just("Cool !");
    }

    // Create Subscriber
    private DisposableObserver<String> getSubscriber(){
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String item) {
                mTextView.setText("Observable emits : "+item);
            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
            }
        };
    }

    // Create Stream and execute it
    private void streamShowString(){
        this.disposable = this.getObservable()
                .subscribeWith(getSubscriber());
    }

    // Dispose subscription
    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }
}
