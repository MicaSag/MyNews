package com.android.sagot.mynews.Utils;

import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.NYTimesTopStories;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NYTimesService {
    @GET("svc/topstories/v2/{section}.json?api-key=de9402ab67114b3c8f08f3d58562b310")
    Observable<NYTimesTopStories> getTopStories(@Path("section") String section);

    // Article Search API
    @GET("svc/search/v2/articlesearch.json?api-key=de9402ab67114b3c8f08f3d58562b310")
    //Observable<NYTimesArticleSearch> getArticleSearch();
    Observable<NYTimesArticleSearch> getArticleSearch(@Query("section") String section);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.nytimes.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}


