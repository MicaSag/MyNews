package com.android.sagot.mynews.Utils;

import android.util.Log;

import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.Doc;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.Models.NYTimesStreams.MostPopular.NYTimesMostPopular;
import com.android.sagot.mynews.Models.NYTimesStreams.MostPopular.ResultMostPopular;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.NYTimesTopStories;
import com.android.sagot.mynews.Models.NYTimesStreams.TopStories.Result;
import com.android.sagot.mynews.Utils.DateUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Create a List of News
 * */
public class NYTimesNewsList {

    private static final String TAG = NYTimesNewsList.class.getSimpleName();

    // Create List of Search Articles
    public static void createListArticleSearch(List<NYTimesNews> listNYTimesNews, NYTimesArticleSearch article) {

        //Here we recover only the elements of the query that interests us
        for (Doc docs : article.getResponse().getDocs()) {

            //  --> Create a news <--
            NYTimesNews news = new NYTimesNews();

            // -- Affected newsURL
            news.setNewsURL(docs.getWebUrl());

            // -- Affected imageURL
            // Test if an image is present
            if (docs.getMultimedia().size() != 0) {
                news.setImageURL("https://www.nytimes.com/" + docs.getMultimedia().get(0).getUrl());
            }

            // -- Affected section label ( section > subSection )
            String section = docs.getNewDesk();
            if (docs.getSectionName() != null) section = section + " > " + docs.getSectionName();
            news.setSection(section);

            // -- Affected date label ( SSAAMMJJ )
            news.setDate(DateUtilities.dateReformatSSAAMMJJ(docs.getPubDate()));

            // -- Affected Title
            news.setTitle(docs.getSnippet());

            // set the everRead at true if necessary
            for (String url : Model.getInstance().getSavedModel().getListUrlArticleRead())
                if (news.getNewsURL() == url) news.setEverRead(true);

            // Add news at Lit
            listNYTimesNews.add(news);
        }
        // Sort the newsList by createdDate in Descending
        Collections.sort(listNYTimesNews, new NYTimesNews());
        Collections.reverse(listNYTimesNews);
    }
    
    // Create List of Most Popular Articles 
    public static void createListArticleMostPopular(List<NYTimesNews> listNYTimesNews, NYTimesMostPopular article) {
        
        //Here we recover only the elements of the query that interests us
        for (ResultMostPopular results : article.getResults()){
            
            //  --> Create a news <--
            NYTimesNews news = new NYTimesNews();
            
            // -- Affected newsURL
            news.setNewsURL(results.getUrl());

            // -- Affected imageURL
            // Test if an image is present
            if (results.getMedia().size() != 0) {
                news.setImageURL(results.getMedia().get(0).getMediaMetadata().get(0).getUrl());
            }

            // -- Affected section label ( section > subSection )
            news.setSection(results.getSection());

            // -- Affected date label ( SSAAMMJJ )
            news.setDate(DateUtilities.dateReformatSSAAMMJJ(results.getPublishedDate()));

            // -- Affected Title
            news.setTitle(results.getTitle());

            // set the everRead at true if necessary
            for (String url : Model.getInstance().getSavedModel().getListUrlArticleRead())
                if (news.getNewsURL() == url) news.setEverRead(true);

            listNYTimesNews.add(news);
        }
        // Sort the newsList by createdDate in Descending
        Collections.sort(listNYTimesNews,new NYTimesNews());
        Collections.reverse(listNYTimesNews);
    }
    
    // Create List of Top Stories Articles 
    public static void createListArticleTopStories(List<NYTimesNews> listNYTimesNews, NYTimesTopStories article) {
        
        //Here we recover only the elements of the query that interests us
        for (Result results : article.getResults()){
            
            //  --> Create a news <--
            NYTimesNews news = new NYTimesNews();
            
            // -- Affected newsURL
            news.setNewsURL(results.getUrl());

            // -- Affected imageURL
            // Test if an image is present
            if (results.getMultimedia().size() != 0) {
                news.setImageURL(results.getMultimedia().get(0).getUrl());
            }

            // -- Affected section label ( section > subSection )
            String section = results.getSection();
            if (!results.getSubsection().equals("") ) section = section+" > "+results.getSubsection();
            news.setSection(results.getSection());

            // -- Affected date label ( SSAAMMJJ )
            news.setDate(DateUtilities.dateReformatSSAAMMJJ(results.getPublishedDate()));

            // -- Affected Title
            news.setTitle(results.getTitle());

            // set the everRead at true if necessary

            Log.d(TAG, "createListArticleTopStories: news.getNewsURL() ="+news.getNewsURL());
            List<String> listUrlSaved = Model.getInstance().getSavedModel().getListUrlArticleRead();
            for (String url : listUrlSaved) {

                Log.d(TAG, "createListArticleTopStories:URL ="+url);
                if (news.getNewsURL().equals(url)) {
                    Log.d(TAG, "createListArticleTopStories: URLs IDENTIQUE");
                    news.setEverRead(true);
                } else {
                    Log.d(TAG, "createListArticleTopStories: URLs <>");
                }
            }
            Log.d(TAG, "createListArticleTopStories:***********************************");

            listNYTimesNews.add(news);
        }
        // Sort the newsList by createdDate in Descending
        //Collections.sort(listNYTimesNews,new NYTimesNews());
        //Collections.reverse(listNYTimesNews);
    }
}
