package com.alghazal.mohammed.hussein.project6newapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.alghazal.mohammed.hussein.project6newapp.Data.NewsDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving article data from guardianapis
 */
public final class QueryUtils {


    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static ArrayList<Article> fetchArticleeData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        ArrayList<Article> articles = new ArrayList<>();
        // Extract relevant fields from the JSON response and create an {@link Event} object
        articles = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return articles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Artivle JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link ArrayList<Article>} object by parsing out information
     * about the first Article from the input ArticleJSON string.
     */
    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing the given JSON response.
     */
    private static ArrayList<Article> extractFeatureFromJson(String articleJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding articles to
        ArrayList<Article> articles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(articleJSON);

            JSONObject response = baseJsonResponse.getJSONObject("response");


            JSONArray articleArray = response.getJSONArray("results");

            // For each article in the ArticleArray, create an {@link Article} object
            for (int i = 0; i < articleArray.length(); i++) {

                // Get a single article at position i within the list of articles
                JSONObject currentArticle = articleArray.getJSONObject(i);

                String webTitle = currentArticle.getString("webTitle");

                String sectionName = currentArticle.getString("sectionName");

                String webUrl = currentArticle.getString("webUrl");

                String webPublicationDate = currentArticle.getString("webPublicationDate");

                //get the author
                JSONArray tags = currentArticle.getJSONArray("tags");

                String authorName="";
                if(tags.length()!=0)
                {
                    JSONObject authorInfo = tags.getJSONObject(0);
                    authorName = authorInfo.getString("webTitle");
                }

                // new Article(String webTitle, String sectionsName, String url, String pubDate)
                Article article = new Article(webTitle, sectionName, webUrl, webPublicationDate, authorName);


                //SHOW SAMPLE :)
               // Log.i("Loging New Article: "+i," webTitle "+webTitle+" authorName "+authorName+
                     // " webPublicationDate "+webPublicationDate);

                // Add the new {@link article} to the list of articles.
                articles.add(article);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Article JSON results", e);
        }

        // Return the list of articles
        return articles;
    }
}