package com.example.android.p6_newsappstage1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Loads a list of news stories by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsStoryLoader extends AsyncTaskLoader<List<NewsStory>> {
    /** Tag for log messages */
    private static final String LOG_TAG = NewsStoryLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link NewsStoryLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
     public NewsStoryLoader(Context context, String url) {
         super(context);
         mUrl = url;
     }

     @Override
     protected void onStartLoading(){
         //Log.i(LOG_TAG, "TEST: onStartLoading() called");
         forceLoad();
     }

    /**
     * This is on a background thread
     */
    @Override
    public List<NewsStory> loadInBackground(){
        //Log.i(LOG_TAG, "TEST: loadInBackground() called");
        if (mUrl == null){
            return null;
        }

        // Perform the network request, parse the response, and extract a list of newsStories.
        return QueryUtils.fetchNewsStoryData(mUrl);
    }
}
