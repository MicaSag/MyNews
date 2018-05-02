package com.android.sagot.mynews.Utils;

import com.android.sagot.mynews.Models.NYTimesStreams.NYTimesTopStories;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NYTimesService {
    @GET("svc/{topstories}/v2/home.json?api-key=de9402ab67114b3c8f08f3d58562b310")
    Call<List<NYTimesTopStories>> getFollowing(@Path("topstories") String username);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.nytimes.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}


