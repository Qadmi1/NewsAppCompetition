package com.alghazal.mohammed.hussein.project6newapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.alghazal.mohammed.hussein.project6newapp.Data.NewsContract.NewsEntry;


public class NewsDbHelper extends SQLiteOpenHelper {
    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "news.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link NewsDbHelper}.
     *
     * @param context of the app
     */
    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the table
        String SQL_CREATE_ARTICLE_TABLE = "CREATE TABLE " + NewsEntry.TABLE_NAME + " ("
                + NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NewsEntry.COLUMN_NEWS_SECTION + " TEXT NOT NULL, "
                + NewsEntry.COLUMN_NEWS_DATE + " TEXT NOT NULL, "
                + NewsEntry.COLUMN_NEWS_WRITER + " TEXT, "
                + NewsEntry.COLUMN_NEWS_TITLE + " TEXT NOT NULL, "
                + NewsEntry.COLUMN_NEWS_URL + " TEXT);";




        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ARTICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}