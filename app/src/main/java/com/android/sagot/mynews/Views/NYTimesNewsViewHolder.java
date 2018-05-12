package com.android.sagot.mynews.Views;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.DateUtilities;
import com.bumptech.glide.RequestManager;

import java.util.Formatter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NYTimesNewsViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = NYTimesNewsViewHolder.class.getSimpleName();

    @BindView(R.id.fragment_recycler_view_item_title) TextView mTitle;
    @BindView(R.id.fragment_recycler_view_item_section) TextView mSection;
    @BindView(R.id.fragment_recycler_view_item_date) TextView mDate;
    @BindView(R.id.fragment_recycler_view_item_image) ImageView mImageView;

    public NYTimesNewsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithNews(NYTimesNews NYTimesNews, RequestManager glide){

        // Put the data on the TextView
        this.mTitle.setText(NYTimesNews.getTitle());
        // Affected date label ( JJ/MM/AA )
        String newsDate = DateUtilities.dateReformat(NYTimesNews.getDate());
        this.mDate.setText(newsDate);
        this.mSection.setText(NYTimesNews.getSection());

        // Put the image data on the ImageView
        glide.load(NYTimesNews.getImageURL()).into(mImageView);
    }
}
