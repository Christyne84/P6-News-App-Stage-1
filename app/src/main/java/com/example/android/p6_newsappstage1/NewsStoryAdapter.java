package com.example.android.p6_newsappstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsStoryAdapter extends ArrayAdapter<NewsStory> {

    /**
     * Create a new {@link NewsStoryAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param newsStories is the list of {@link NewsStory}s to be displayed.
     *
     */
    public NewsStoryAdapter(Context context, List<NewsStory> newsStories) {
        super(context, 0, newsStories);
    }

    /**
     * {@link NewsStoryAdapter} is an {@link ArrayAdapter} that can provide the layout for each list item
     * based on a data source, which is a list of {@link NewsStory} objects.
     */

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if an existing list item view (called convertView) that is being reused,
        // otherwise inflate a new list item
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_story_list_item, parent, false);
        }

        // Get the {@link NewsStory} object located at this position in the list
        NewsStory currentNewsStory = getItem(position);

        // Find the TextView with view ID title
        TextView titleView = listItemView.findViewById(R.id.title);
        // Get the title
        String title = null;
        if (currentNewsStory != null) {
            title = currentNewsStory.getTitle();
        }
        // Display the title of the current news story in that TextView
        titleView.setText(title);

        // Get the section name string from the NewsStory object
        String sectionName = null;
        if (currentNewsStory != null) {
            sectionName = currentNewsStory.getSectionName();
        }
        // Find the TextView in the news_story_list_item.xml layout with the ID location_offset.
        TextView sectionNameView = listItemView.findViewById(R.id.section_name);
        //Display the section name of the current news story in that TextView
        sectionNameView.setText(sectionName);

        // Get the contributor's name string from the NewsStory object
        String contributorName = null;
        if (currentNewsStory != null) {
            contributorName = currentNewsStory.getContributorName();
        }
        // Find the TextView in the news_story_list_item.xml layout with the ID location_offset.
        TextView contributorNameView = listItemView.findViewById(R.id.contributor);
        //Display the section name of the current news story in that TextView
        contributorNameView.setText(contributorName);


        //Parse the String which holds the date and time (original "2018-04-15T08:35:35Z" to
        //"2018-04-15" and "08:35:35", and from "08:35:35" to "08:35")
        String originalDate = null;

        if (currentNewsStory != null) {
            originalDate = currentNewsStory.getWebPublicationDate();
        }

        String formattedDateTime = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        //input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.US);

        Date d;
        try {
            d = input.parse(originalDate);
            formattedDateTime = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Find the TextView in the news_story_list_item.xml layout with the ID date.
        TextView dateView = listItemView.findViewById(R.id.date);
        // Display the date of the current news story in that TextView
        dateView.setText(formattedDateTime);

        // Return the whole news story list item layout (containing 3 TextViews and a View)
        // so that it can be shown in the ListView.
        return listItemView;
    }
}
