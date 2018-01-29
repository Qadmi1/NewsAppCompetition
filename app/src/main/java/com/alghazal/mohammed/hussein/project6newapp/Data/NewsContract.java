package com.alghazal.mohammed.hussein.project6newapp.Data;

import android.provider.BaseColumns;

public class NewsContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private NewsContract() {}


    public class NewsEntry implements BaseColumns {
        /**
         * Name of the table
         */
        public static final String TABLE_NAME = "news";
        /**
         * Name of the columns
         */
        public final static String _ID = BaseColumns._ID;
        public static final String COLUMN_NEWS_SECTION = "section";
        public static final String COLUMN_NEWS_DATE = "date";
        public static final String COLUMN_NEWS_WRITER = "writer";
        public static final String COLUMN_NEWS_TITLE = "title";
        public static final String COLUMN_NEWS_URL = "url";

    }
}