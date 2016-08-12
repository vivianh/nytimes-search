package com.codepath.nytimessearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvHeadline) TextView headlineTextView;
    @BindView(R.id.ivPhoto) ImageView photoImageView;

    public ArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getHeadlineTextView() {
        return headlineTextView;
    }

    public ImageView getPhotoImageView() {
        return photoImageView;
    }
}