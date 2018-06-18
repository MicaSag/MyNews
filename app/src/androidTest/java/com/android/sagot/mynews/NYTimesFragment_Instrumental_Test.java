package com.android.sagot.mynews;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.Models.NYTimesStreams.MostPopular.NYTimesMostPopular;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.NYTimesTopStories;
import com.android.sagot.mynews.Utils.NYTimesStreams;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class NYTimesFragment_Instrumental_Test {

    @Test
    public void fetchNewsTopStories_I_Test() throws Exception {
        // Get the stream
        Observable<NYTimesTopStories> observableNews = NYTimesStreams.streamFetchTopStories("home","de9402ab67114b3c8f08f3d58562b310");
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
        // Verify if Results Exist
        assertThat("Results exist in the Stream request", !newsFetched.getResults().equals(null));
    }

    @Test
    public void fetchNewsMostPopular_I_Test() throws Exception {
        // Get the stream
        Observable<NYTimesMostPopular> observableNews = NYTimesStreams.streamFetchMostPopular("de9402ab67114b3c8f08f3d58562b310");
        // Create a new TestObserver
        TestObserver<NYTimesMostPopular> testObserver = new TestObserver<>();
        // Launch observable
        observableNews.subscribeWith(testObserver)
                .assertNoErrors()       // Check if no errors
                .assertNoTimeout()      // Check if no Timeout
                .awaitTerminalEvent();  // Await the stream terminated before continue

        // Get list of news fetched
        NYTimesMostPopular newsFetched = testObserver.values().get(0);
        Log.d("JUNIT_TEST", "fetchNewsTest:newsFetched.getNumResults() = "+newsFetched.getNumResults());
        // Verify if status: "OK"
        assertThat("The status of the Stream was read correctly", newsFetched.getStatus().equals("OK"));
        // Verify if Results Exist
        assertThat("Results exist in the Stream request", !newsFetched.getResults().equals(null));
    }

    @Test
    public void fetchNewsArticleSearch_I_Test() throws Exception {
        // Criteria of the query
        Map<String, String> query =  new HashMap<>();
        query.put("q", "Obama");
        query.put("fq", "news_desk:( Sports , Business , Politics");
        // Get the stream
        Observable<NYTimesArticleSearch> observableNews =
                NYTimesStreams.streamFetchArticleSearch("de9402ab67114b3c8f08f3d58562b310", query);
        // Create a new TestObserver
        TestObserver<NYTimesArticleSearch> testObserver = new TestObserver<>();
        // Launch observable
        observableNews.subscribeWith(testObserver)
                .assertNoErrors()       // Check if no errors
                .assertNoTimeout()      // Check if no Timeout
                .awaitTerminalEvent();  // Await the stream terminated before continue

        // Get list of news fetched
        NYTimesArticleSearch newsFetched = testObserver.values().get(0);
        Log.d("JUNIT_TEST", "fetchNewsTest:newsFetched.getNumResults() = "+newsFetched.getResponse());
        // Verify if status: "OK"
        assertThat("The status of the Stream was read correctly", newsFetched.getStatus().equals("OK"));
        // Verify if Results Exist
        assertThat("Results exist in the Stream request", !newsFetched.getResponse().equals(null));
    }
}
