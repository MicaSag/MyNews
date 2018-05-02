package com.android.sagot.mynews.Utils;

import com.android.sagot.mynews.Models.NYTimesStreams.NYTimesTopStories;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.annotations.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NYTimesCalls {

    // Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable List<NYTimesTopStories> users);
        void onFailure();
    }

    // Public method to start fetching Top Stories
    public static void fetchUserFollowing(Callbacks callbacks, String username){

        // Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        // Get a Retrofit instance and the related endpoints
        NYTimesService gitHubService = NYTimesService.retrofit.create(NYTimesService.class);

        // Create the call on NYTimes Top Stories API
        Call<List<NYTimesTopStories>> call = gitHubService.getFollowing(username);
        // Start the call
        call.enqueue(new Callback<List<NYTimesTopStories>>() {

            @Override
            public void onResponse(Call<List<NYTimesTopStories>> call, Response<List<NYTimesTopStories>> response) {
                // Call the proper callback used in controller (TopStoriesFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<NYTimesTopStories>> call, Throwable t) {
                // Call the proper callback used in controller (TopStoriesFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
