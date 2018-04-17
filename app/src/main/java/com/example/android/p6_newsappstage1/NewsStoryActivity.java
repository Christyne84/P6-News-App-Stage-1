package com.example.android.p6_newsappstage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsStoryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsStory>>{

    public static final String LOG_TAG = NewsStoryActivity.class.getName();

    /**
     * Constant value for the news story loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_STORY_LOADER_ID = 1;

    /** Adapter for the list of news stories*/
    private NewsStoryAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.i(LOG_TAG, getString(R.string.news_story_activity_oncreate_log_message));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_story_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView newsStoryListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsStoryListView.setEmptyView(mEmptyStateTextView);

        // Create a new {@link ArrayAdapter} of news stories
        mAdapter = new NewsStoryAdapter(this, new ArrayList<NewsStory>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsStoryListView.setAdapter(mAdapter);

        newsStoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current news story that was clicked on
                NewsStory currentNewsStory = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsStoryUri = null;
                if (currentNewsStory != null) {
                    newsStoryUri = Uri.parse(currentNewsStory.getUrl());
                }

                // Create a new intent to view the news story URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsStoryUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            //Log.i(LOG_TAG, getString(R.string.newsStory_initLoader_log_message));
            loaderManager.initLoader(NEWS_STORY_LOADER_ID, null, this);
        } else {
            // Otherwise, display error. First, hide loading indicator so error message will be visible
            View loadingSpinner= findViewById(R.id.loading_spinner);
            loadingSpinner.setVisibility(View.GONE);

            // Update empty state with "no connection" error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<NewsStory>> onCreateLoader(int i, Bundle bundle) {
        //Log.i(LOG_TAG, getString(R.string.onCreateLoader_log_message));

        // Create a new loader for the given URL
        // Build URI reference for for news stories from The Guardian data set
        // http://content.guardianapis.com/search?show-tags=contributor&api-key=test
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("http")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("api-key", "test");
        return new NewsStoryLoader(this, uriBuilder.build().toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> newsStories) {
        //Log.i(LOG_TAG, getString(R.string.onLoadFinished_log_message));

        // Hide loading indicator because the data has been loaded
        View loadingSpinner = findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.GONE);

        // Set empty state text to display "No news stories found."
        mEmptyStateTextView.setText(R.string.no_news_stories);

       // Clear the adapter of previous news story data
        mAdapter.clear();

        // If there is a valid list of {@link NewsStory}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsStories != null && !newsStories.isEmpty()) {
            mAdapter.addAll(newsStories);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsStory>> loader) {
        //Log.i(LOG_TAG, getString(R.string.onLoaderReset_log_message));
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}