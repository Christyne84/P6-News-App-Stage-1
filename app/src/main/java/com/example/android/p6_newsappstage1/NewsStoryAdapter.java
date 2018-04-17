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

    private class ViewHolder {
        public TextView titleView, sectionNameView, contributorNameView, dateView;
    }

    // Date formats used to build the date strings.
    private static SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    //input.setTimeZone(TimeZone.getTimeZone("UTC"));
    private static SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.US);

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

        ViewHolder holder;

        // Check if an existing list item view (called convertView) that is being reused,
        // otherwise inflate a new list item
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_story_list_item, parent, false);

            //This is the first time the function is called for this item.
            //Create and save the view holder as the "tag".
            holder = new ViewHolder();
            holder.titleView = listItemView.findViewById(R.id.title);
            holder.sectionNameView = listItemView.findViewById(R.id.section_name);
            holder.contributorNameView = listItemView.findViewById(R.id.contributor);
            holder.dateView = listItemView.findViewById(R.id.date);

            listItemView.setTag(holder);
        } else {
            //Get the already saved tag object.
            holder = (ViewHolder)listItemView.getTag();
        }

        // Get the {@link NewsStory} object located at this position in the list
        NewsStory currentNewsStory = getItem(position);

        // Get the title
        if (currentNewsStory != null) {
            // Display the title of the current news story in that TextView
            holder.titleView.setText(currentNewsStory.getTitle());
            //Display the section name of the current news story in that TextView
            holder.sectionNameView.setText(currentNewsStory.getSectionName());
            //Display the contributor name of the current news story in that TextView
            holder.contributorNameView.setText(currentNewsStory.getContributorName());

            //Parse the String which holds the date and time (original "2018-04-15T08:35:35Z" to
            //"2018-04-15" and "08:35:35", and from "08:35:35" to "08:35")
            String originalDate = currentNewsStory.getWebPublicationDate();
            try {
                Date d = inputDateFormat.parse(originalDate);
                String formattedDateTime = outputDateFormat.format(d);

                // Display the date of the current news story in that TextView
                holder.dateView.setText(formattedDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Return the whole news story list item layout (containing 3 TextViews and a View)
        // so that it can be shown in the ListView.
        return listItemView;
    }
}
