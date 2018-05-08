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

    // FOR DATA
    private List<NYTimesNews> mListNews;

    // Declaring a Glide Object
    private RequestManager glide;

    // CONSTRUCTOR
    public NYTimesNewsAdapter(List<NYTimesNews> listNews, RequestManager glide) {
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

    // UPDATE VIEW HOLDER WITH A NYTIMESNEWS
    @Override
    public void onBindViewHolder(NYTimesNewsViewHolder viewHolder, int position) {
        viewHolder.updateWithNews(this.mListNews.get(position),this.glide);
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.mListNews.size();
    }
}