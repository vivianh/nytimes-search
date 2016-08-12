package com.codepath.nytimessearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleTextViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvHeadline) TextView headlineTextView;

    public ArticleTextViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getHeadlineTextView() {
        return headlineTextView;
    }
}