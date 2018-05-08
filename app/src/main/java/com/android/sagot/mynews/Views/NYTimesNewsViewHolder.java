package com.android.sagot.mynews.Views;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.R;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NYTimesNewsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_recycler_view_item_title) TextView mTitle;
    @BindView(R.id.fragment_recycler_view_item_section) TextView mSection;
    @BindView(R.id.fragment_recycler_view_item_date) TextView mDate;
    @BindView(R.id.fragment_recycler_view_item_image) ImageView mImageView;

    public NYTimesNewsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithNews(NYTimesNews NYTimesNews, RequestManager glide){


        this.mTitle.setText(NYTimesNews.getTitle());
        this.mDate.setText(NYTimesNews.getDate());
        this.mSection.setText(NYTimesNews.getSection());

        // Update ImageView
        glide.load(NYTimesNews.getImageURL()).into(mImageView);
    }
}
