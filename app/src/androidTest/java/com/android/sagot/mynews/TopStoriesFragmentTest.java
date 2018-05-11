package com.android.sagot.mynews;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.NYTimesTopStories;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class TopStoriesFragmentTest {

    @Test
    public void fetchNewsTopStoriesTest() throws Exception {
        // Get the stream
        Observable<NYTimesTopStories> observableNews = NYTimesStreams.streamFetchTopStories("home");
        // Create a new TestObserver
        TestObserver<NYTimesTopStories> testObserver = new TestObserver<>();
        // Launch observable
        observableNews.subscribeWith(testObserver)
                .assertNoErrors()       // Check if no errors
                .assertNoTimeout()      // Check if no Timeout
                .awaitTerminalEvent();  // Await the stream terminated before continue

        // Get list of news fetched
        NYTimesTopStories newsFetched = testObserver.values().get(0);
        Log.d("JUNIT_TEST", "fetchNewsTest:newsFetched.getNumResults() = "+newsFetched.getNumResults());
        // Verify if status: "OK"
        assertThat("The status of the Stream was read correctly", newsFetched.getStatus().equals("OK"));
        // Verify if section: "home"
        assertThat("The section of the Stream was read correctly", newsFetched.getSection().equals("home"));


    }
}
