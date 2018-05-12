package com.android.sagot.mynews.Utils;

import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.NYTimesTopStories;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NYTimesStreams {
    public static Observable<NYTimesTopStories> streamFetchTopStories(String section){
        NYTimesService topStoriesService = NYTimesService.retrofit.create(NYTimesService.class);
        return topStoriesService.getTopStories(section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<NYTimesArticleSearch> streamFetchArticleSearch(String section){
        NYTimesService articleSearchService = NYTimesService.retrofit.create(NYTimesService.class);
        return articleSearchService.getArticleSearch(section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<NYTimesArticleSearch> streamFetchArticleSearchSports(){
        NYTimesService articleSearchService = NYTimesService.retrofit.create(NYTimesService.class);
        return articleSearchService.getArticleSearchSports()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
