package com.android.sagot.mynews.Views;


import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.R;
import com.android.sagot.mynews.Utils.DateUtilities;
import com.bumptech.glide.RequestManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NYTimesNewsViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = NYTimesNewsViewHolder.class.getSimpleName();

    @BindView(R.id.fragment_recycler_view_item_card) CardView mCard;
    @BindView(R.id.fragment_recycler_view_item_title) TextView mTitle;
    @BindView(R.id.fragment_recycler_view_item_section) TextView mSection;
    @BindView(R.id.fragment_recycler_view_item_date) TextView mDate;
    @BindView(R.id.fragment_recycler_view_item_image) ImageView mImageView;

    public NYTimesNewsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithNews(int tabLayoutPosition, NYTimesNews NYTimesNews, RequestManager glide){

        // Put the data on the TextView
        this.mTitle.setText(NYTimesNews.getTitle());
        // Affected date label ( JJ/MM/AA )
        String newsDate = DateUtilities.dateReformat(NYTimesNews.getDate());
        this.mDate.setText(newsDate);
        // Affected section
        this.mSection.setText(NYTimesNews.getSection());
        // If the Article is ever read, the color background change
        int color = mCard.getResources()
                .obtainTypedArray(R.array.everRead_colors).getColor(tabLayoutPosition, 0);
        if (NYTimesNews.isEverRead()) {
            this.mSection.setTextColor(Color.BLUE);
            this.mDate.setTextColor(Color.BLUE);
            this.mTitle.setTextColor(Color.BLUE);
            this.mCard.setCardBackgroundColor(color);
        } else {
            // Obligation to put a color because otherwise the color is randomly positioned
            this.mSection.setTextColor(Color.BLACK);
            this.mDate.setTextColor(Color.BLACK);
            this.mTitle.setTextColor(Color.BLACK);
            this.mCard.setCardBackgroundColor(Color.WHITE);
        }

        // Put the image data on the ImageView
        glide.load(NYTimesNews.getImageURL()).into(mImageView);
    }
}
