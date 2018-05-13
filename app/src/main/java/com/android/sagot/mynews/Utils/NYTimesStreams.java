package com.android.sagot.mynews.Utils;

import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.Models.NYTimesStreams.MostPopular.NYTimesMostPopular;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.NYTimesTopStories;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NYTimesStreams {

    // NYTimes ArticleTopStories STREAM
    public static Observable<NYTimesTopStories> streamFetchTopStories(String section){
        NYTimesService topStoriesService = NYTimesService.retrofit.create(NYTimesService.class);
        return topStoriesService.getTopStories(section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    // NYTimes ArticleSearch STREAM
    public static Observable<NYTimesArticleSearch> streamFetchArticleSearch(Map<String,String> filters){
        NYTimesService articleSearchService = NYTimesService.retrofit.create(NYTimesService.class);
        return articleSearchService.getArticleSearch(filters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    // NYTimes ArticleMostPopular STREAM
    public static Observable<NYTimesMostPopular> streamFetchMostPopular(){
        NYTimesService mostPopularService = NYTimesService.retrofit.create(NYTimesService.class);
        return mostPopularService.getMostPopular()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
