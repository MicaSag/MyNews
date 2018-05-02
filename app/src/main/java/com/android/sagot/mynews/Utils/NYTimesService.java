package com.android.sagot.mynews.Utils;

import com.android.sagot.mynews.Models.NYTimesStreams.NYTimesTopStories;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NYTimesService {
    @GET("svc/topstories/v2/{section}.json?api-key=de9402ab67114b3c8f08f3d58562b310")
    Observable<NYTimesTopStories> getFollowing(@Path("section") String section);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.nytimes.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}


