package com.alghazal.mohammed.hussein.project6newapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private Context context;
    ArrayList<Article> articles;

    public ArticleAdapter(@NonNull Context context, ArrayList<Article> articles )
    {
        this.context= context;
        this.articles=articles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the {@link Article} object located at this position in the list
        final Article currentArticle = articles.get(position);

        holder.webTitle.setText(currentArticle.getWebTitle());

        String orginalDate = currentArticle.getPubDate();
        String[] parts = orginalDate.split("T");
        holder.date.setText(parts[0]);

        holder.sectionName.setText(currentArticle.getSectionsName());

        String author = currentArticle.getAuthor();

        if(author.equals(""))
        {
            holder.authorView.setText("");
            // authorView.setVisibility(View.GONE);
        }
        else
        {
            holder.authorView.setText("By: "+author);
            //Log.i(" Author part "+1+"  ",author);
        }

        // set setOnClickListener on Item to open in browser
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
//                Uri articleUri = Uri.parse(currentArticle.getUrl());
//
//                // Create a new intent to view the article URI
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
//
//                // Send the intent to launch a new activity
//                context.startActivity(websiteIntent);
                // show in WebView
                Intent intent = new Intent(context,WebViewer.class);
                intent.putExtra("URL",currentArticle.getUrl().toString());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    // use this method to clear the adapter
    public void clear() {
        int size = this.articles.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.articles.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    ///////////////////////////////////////////

    public String setDate(Date dateObject)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);
        return dateToDisplay;
    }

    public void update(List<Article> articles) {
            this.articles.clear();
            this.articles.addAll(articles);
            notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView webTitle;
        private TextView date;
        private TextView sectionName;
        private TextView authorView;
        private View parentView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.parentView=itemView;
            this.webTitle= itemView.findViewById(R.id.webTitle);
            this.date= itemView.findViewById(R.id.date);
            this.sectionName= itemView.findViewById(R.id.sectionName);
            this.authorView = itemView.findViewById(R.id.author);
        }
    }
}
