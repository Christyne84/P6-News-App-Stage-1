package com.example.android.p6_newsappstage1;

import android.text.TextUtils;
import android.util.Log;

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
import java.util.List;

/**
 * Helper methods related to requesting and receiving news story data from The Guardian site.
 */
public final class QueryUtils {
    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String RESPONSE = "response";
    private static final String RESULTS = "results";
    private static final String WEB_TITLE = "webTitle";
    private static final String SECTION_NAME = "sectionName";
    private static final String WEB_PUBLICATION_DATE = "webPublicationDate";
    private static final String WEB_URL = "webUrl";
    private static final int READ_TIMEOUT = 10000;  /* milliseconds */
    private static final int CONNECT_TIMEOUT = 15000;  /* milliseconds */
    private static final String GET_REQUEST_METHOD = "GET";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian data set and return a list of {@link NewsStory} objects.
     */
    public static List<NewsStory> fetchNewsStoryData(String requestUrl) {
        //Log.i(LOG_TAG, "TEST: fetchNewsStoryData() called");

        //Create a delay in fetching the data, so the spinner can be tested, and
        // also the internet connection
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link NewsStory}s
        //return the list of {@link NewsStory}s
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
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
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(GET_REQUEST_METHOD);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news story JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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
     * Return a list of {@link NewsStory} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<NewsStory> extractFeatureFromJson(String newsStoryJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsStoryJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding newsStories to
        List<NewsStory> newsStories = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsStoryJSON);

            //Extract the JSONArray associated with the key "response" which represents a list
            //of JSONObject
            JSONObject newsStoryArray = baseJsonResponse.getJSONObject(RESPONSE);
            //Extract the JSONArray associated with the key "result" which represents a list
            //of JSONArray
            JSONArray resultsArray = newsStoryArray.getJSONArray(RESULTS);

            //For each news story in the newsStoryArray, create a {@link NewsStory} object
            for(int i = 0; i < resultsArray.length(); i++){
                JSONObject resultObj = resultsArray.getJSONObject(i);

                String title = resultObj.getString(WEB_TITLE);
                String sectionName = resultObj.getString(SECTION_NAME);
                String webPublicationDate = resultObj.getString(WEB_PUBLICATION_DATE);
                // Extract the value for the key called "webUrl"
                String url = resultObj.getString(WEB_URL);

                JSONArray tagsArray = resultObj.getJSONArray("tags");
                JSONObject firstTagObj = tagsArray.getJSONObject(0);

                String contributorName = firstTagObj.getString(WEB_TITLE);

                // Create a new {@link NewsStory} object with the title, sectionName, webPublicationDate,
                // and url from the JSON response.
                NewsStory newsStory = new NewsStory(title, sectionName, webPublicationDate, url, contributorName);
                newsStories.add(newsStory);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news story JSON results", e);
        }

        // Return the list of newsStories
        return newsStories;
    }
}
