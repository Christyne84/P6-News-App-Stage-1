package com.example.android.p6_newsappstage1;

/**
 * A {@link NewsStory} object contains information about a news story
 */
public class NewsStory {

    /** Title of the news story */
    private String mTitle;

    /** Section name of the news story */
    private String mSectionName;

    /** Date when the news story was published */
    private String mWebPublicationDate;

    /** Website URL of the news story */
    private String mUrl;

    /**
     * Create a new Info object.
     *
     * @param title is the title of the NewsStory
     * @param sectionName is the sectionName of the NewsStory
     * @param webPublicationDate is the date the NewsStory was published
     * @param url is the website URL to find more details about the NewsStory
     */
    public NewsStory(String title, String sectionName, String webPublicationDate, String url) {
        mTitle = title;
        mSectionName = sectionName;
        mWebPublicationDate = webPublicationDate;
        mUrl = url;
    }

    /** Return the title of the news story */
    public String getTitle() {
        return mTitle;
    }

    /** Return the section of the news story */
    public String getSectionName() {
        return mSectionName;
    }

    /** Return the publication date of the news story */
    public String getWebPublicationDate() {
        return mWebPublicationDate;
    }

    /** Returns the website URL where the news story can be found. */
    public String getUrl() {
        return mUrl;
    }
}
