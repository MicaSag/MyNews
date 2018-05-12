package com.android.sagot.mynews.Utils;

import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.Models.NYTimesStreams.MostPopular.NYTimesMostPopular;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.NYTimesTopStories;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NYTimesService {
    // Top Stories API
    @GET("svc/topstories/v2/{section}.json?api-key=de9402ab67114b3c8f08f3d58562b310")
    Observable<NYTimesTopStories> getTopStories(@Path("section") String section);

    // Article Search API
    @GET("svc/search/v2/articlesearch.json?api-key=de9402ab67114b3c8f08f3d58562b310")
    Observable<NYTimesArticleSearch> getArticleSearch(@Query("section") String section);

    // Most Popular API
    @GET("svc/mostpopular/v2/mostshared/all-sections/30.json?api-key=de9402ab67114b3c8f08f3d58562b310")
    Observable<NYTimesMostPopular> getMostPopular();

    // Use excludeFieldsWithoutExposeAnnotation() for ignore some fields
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.nytimes.com/")
            .addConverterFactory(GsonConverterFactory.create(
                    new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

}


