package com.android.sagot.mynews.Utils;

import com.android.sagot.mynews.Models.NYTimesStreams.NYTimesTopStories;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NYTimesStreams {
    public static Observable<NYTimesTopStories> streamFetchTopStoriesFollowing(String section){
        NYTimesService topStoriesService = NYTimesService.retrofit.create(NYTimesService.class);
        return topStoriesService.getFollowing(section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
