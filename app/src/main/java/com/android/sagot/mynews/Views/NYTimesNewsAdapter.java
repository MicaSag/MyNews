package com.android.sagot.mynews.Views;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.R;
import com.bumptech.glide.RequestManager;

import java.util.List;

public class NYTimesNewsAdapter extends RecyclerView.Adapter<NYTimesNewsViewHolder> {

    // List of news
    private List<NYTimesNews> mListNews;

    // TabLayout position
    private int mTabLayoutPosition;

    // Declaring a Glide Object
    private RequestManager glide;

    // CONSTRUCTOR
    public NYTimesNewsAdapter(int tabLayoutPosition, List<NYTimesNews> listNews, RequestManager glide) {
        this.mTabLayoutPosition = tabLayoutPosition;
        this.mListNews = listNews;
        this.glide = glide;
    }

    @Override
    public NYTimesNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_recycler_view_item, parent, false);

        return new NYTimesNewsViewHolder(view);
    }

    // UPDATE VIEW HOLDER WITH A NY_TIMES_NEWS
    @Override
    public void onBindViewHolder(NYTimesNewsViewHolder viewHolder, int position) {
        viewHolder.updateWithNews(this.mTabLayoutPosition,this.mListNews.get(position),this.glide);
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.mListNews.size();
    }


}