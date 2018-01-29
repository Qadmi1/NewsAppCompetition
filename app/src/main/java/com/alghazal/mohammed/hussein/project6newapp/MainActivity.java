package com.alghazal.mohammed.hussein.project6newapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alghazal.mohammed.hussein.project6newapp.Data.NewsDbHelper;
import com.alghazal.mohammed.hussein.project6newapp.Data.NewsContract.NewsEntry;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>> {

    private NewsDbHelper mDbHelper;
    ArrayList<Article> dbArticles;

    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search";
            //"http://content.guardianapis.com/search?order-by=newest&show-references=author&q=science%20AND%20tech%20AND%20game&api-key=test&show-tags=contributor";
            //"http://content.guardianapis.com/search?order-by=newest&q=science%20AND%20tech%20AND%20game&api-key=test";

    private static final int Article_LOADER_ID = 1;

    /** Adapter for the list of Article */
    private ArticleAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private ProgressBar progressBar;
    RecyclerView articleRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this, IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();
        mDbHelper = new NewsDbHelper(this);

        // Find a reference to the {@link ListView} in the layout
        articleRecyclerView =  findViewById(R.id.list);

        // Create a new adapter that takes an empty list of Article as input
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        mEmptyStateTextView =  findViewById(R.id.empty_view);
        //articleRecyclerView.setEmptyView(mEmptyStateTextView);
//
        progressBar= findViewById(R.id.progressBar);

//        // Set the adapter on the {@link ListView}
//        // so the list can be populated in the user interface
        articleRecyclerView.setAdapter(mAdapter);
        articleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        ///    check if there is a data base from last time
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                NewsEntry._ID,
                NewsEntry.COLUMN_NEWS_SECTION,
                NewsEntry.COLUMN_NEWS_DATE,
                NewsEntry.COLUMN_NEWS_TITLE,
                NewsEntry.COLUMN_NEWS_WRITER,
                NewsEntry.COLUMN_NEWS_URL
                };

        // Perform a query on the pets table
        Cursor cursor = db.query(
                NewsEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(Article_LOADER_ID, null, this);

            //here
        }
        else if(cursor.getCount()>0)
        {
            try {
                // Figure out the index of each column
                int idColumnIndex = cursor.getColumnIndex(NewsEntry._ID);
                int sectionColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_SECTION);
                int dateColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_DATE);
                int titleColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_TITLE);
                int writerColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_WRITER);
                int urlColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_NEWS_URL);

                dbArticles = new ArrayList<>();
                // Iterate through all the returned rows in the cursor
                while (cursor.moveToNext()) {
                    // Use that index to extract the String or Int value of the word
                    // at the current row the cursor is on.
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentSection = cursor.getString(sectionColumnIndex);
                    String currentDate = cursor.getString(dateColumnIndex);
                    String currentTitle = cursor.getString(titleColumnIndex);
                    String currentWriter = cursor.getString(writerColumnIndex);
                    String currentUrl = cursor.getString(urlColumnIndex);

                    // put all in a list
                    dbArticles.add(new Article(currentTitle, currentSection, currentUrl, currentDate, currentWriter));

                    Log.i("current Title ", currentID+"  "+currentTitle);
                }
            } finally {
                // Always close the cursor when you're done reading from it. This releases all its
                // resources and makes it invalid.
                progressBar.setVisibility(View.GONE);
                mAdapter.update(dbArticles);
                cursor.close();
            }
        }
        else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            progressBar.setVisibility(View.GONE);
            articleRecyclerView.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String section = sharedPrefs.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default));


        uriBuilder.appendQueryParameter(getString(R.string.query_key), getString(R.string.query_search_vlue));


        if(!section.equals(getString(R.string.settings_section_default)))
        {
            uriBuilder.appendQueryParameter(getString(R.string.settings_section_key), section);

        }

        uriBuilder.appendQueryParameter(getString(R.string.show_tags_key), getString(R.string.show_tags_vlue));
        uriBuilder.appendQueryParameter(getString(R.string.order_by_key), getString(R.string.order_by_vlue));
        uriBuilder.appendQueryParameter(getString(R.string.api_key_key), getString(R.string.api_key_vlue));


        // Create a new loader for the given URL
        Log.i("uriBuilder",uriBuilder.toString());
        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        progressBar.setVisibility(View.GONE);
        //Hide  articleRecyclerView to show the text
        articleRecyclerView.setVisibility(View.GONE);
        // Set empty state text to display "No articles found."
        mEmptyStateTextView.setText(R.string.no_Article);

        // Clear the adapter of previous article data
        mAdapter.clear();

        // If there is a valid list of {@link article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            mAdapter.update(articles);

            // we need to empty the table
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            int rows  =db.delete(NewsEntry.TABLE_NAME,null,null);
            // check the deleted
            //Toast.makeText(this,"deleted rows: "+ rows,Toast.LENGTH_SHORT).show();

            // here add to database
            int size = articles.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    Article currentArticle = articles.get(i);

                    // Create a ContentValues object where column names are the keys,
                    ContentValues values = new ContentValues();
                    values.put(NewsEntry.COLUMN_NEWS_SECTION, currentArticle.getSectionsName());
                    values.put(NewsEntry.COLUMN_NEWS_DATE, currentArticle.getPubDate());
                    values.put(NewsEntry.COLUMN_NEWS_TITLE, currentArticle.getWebTitle());
                    values.put(NewsEntry.COLUMN_NEWS_WRITER, currentArticle.getAuthor());
                    values.put(NewsEntry.COLUMN_NEWS_URL, currentArticle.getUrl());

                    long newRowId = db.insert(NewsEntry.TABLE_NAME, null, values);

                }
            }

            articleRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    //             menu
    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
