package com.codepath.nytimessearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ComplexArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Article> items;
    private final int ARTICLE = 0, ARTICLE_TEXT = 1;

    private static OnItemClickListener listener;

    public ComplexArticlesAdapter(Context context, List<Article> items) {
        this.context = context;
        this.items = items;
    }

    private Context getContext() { return context; }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case ARTICLE:
                View v1 = inflater.inflate(R.layout.item_article, parent, false);
                viewHolder = new ArticleViewHolder(v1);
                v1.setOnClickListener((v) -> listener.onItemClick(v, viewHolder.getLayoutPosition()));
                break;
            case ARTICLE_TEXT:
                View v2 = inflater.inflate(R.layout.item_article_text, parent, false);
                viewHolder = new ArticleTextViewHolder(v2);
                v2.setOnClickListener((v) -> listener.onItemClick(v, viewHolder.getLayoutPosition()));
                break;
            default:
                View v0 = inflater.inflate(R.layout.item_article_text, parent, false);
                viewHolder = new ArticleTextViewHolder(v0);
                v0.setOnClickListener((v) -> listener.onItemClick(v, viewHolder.getLayoutPosition()));
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ARTICLE:
                ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
                configureArticleViewHolder(articleViewHolder, position);
                break;
            case ARTICLE_TEXT:
                ArticleTextViewHolder articleTextViewHolder = (ArticleTextViewHolder) holder;
                configureArticleTextViewHolder(articleTextViewHolder, position);
                break;
            default:
                break;
        }
    }

    private void configureArticleViewHolder(ArticleViewHolder viewHolder, int position) {
        Article article = items.get(position);
        viewHolder.getHeadlineTextView().setText(article.getHeadline());
        Picasso.with(getContext())
                .load(article.getPhotoURL())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.getPhotoImageView());
    }

    private void configureArticleTextViewHolder(ArticleTextViewHolder viewHolder, int position) {
        Article article = items.get(position);
        viewHolder.getHeadlineTextView().setText(article.getHeadline());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Article article = items.get(position);
        if (article.getPhotoURL() != null) {
            return ARTICLE;
        } else if (article.getPhotoURL() == null){
            return ARTICLE_TEXT;
        }
        return -1;
    }


    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
