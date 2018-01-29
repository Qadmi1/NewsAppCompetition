package com.alghazal.mohammed.hussein.project6newapp;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    private String url;

    public ArticleLoader(Context context, String url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (url == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Article> articles = QueryUtils.fetchArticleeData(url);
        return articles;
    }
}
