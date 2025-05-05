package com.example.kindremind_mobileappproject.ui.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    //singleton pattern
    private static DBAdapter instance;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private final Context context;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_DEEDID = "deedId";
    public static final String KEY_CUSTOM = "custom";
    public static final String KEY_DATE = "date";
    public static final String KEY_NOTE = "note";
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "deed_database";
    private static final String DATABASE_TABLE = "completed_deeds";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table completed_deeds (_id integer primary key autoincrement, "
            + "deedId integer, custom integer, date text, "
            + " note text);";

    // Constructor
    private DBAdapter(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }
    public static synchronized DBAdapter getInstance(Context context) {
        if (instance == null) {
            instance = new DBAdapter(context);
        }
        return instance;
    }

    // To create and upgrade a database in an Android application SQLiteOpenHelper subclass is usually created
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // onCreate() is only called by the framework, if the database does not exist
            Log.d("Create", "Creating the database");

            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // onUpgrade() is only called by the framework, if one changes the database version number

            // Sends a Warn log message
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            // Method to execute an SQL statement directly
            db.execSQL("DROP TABLE IF EXISTS books");
            onCreate(db);
        }
    }

    // Opens the database
    public void open() throws SQLException {
        // Create and/or open a database that will be used for reading and writing
        db = DBHelper.getWritableDatabase();
    }

    // Closes the database
    public void close() {
        // Closes the database
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // Checks if the database is open
    public boolean isOpen() {
        return db != null && db.isOpen();
    }

    // Insert a completed deed into the database
    public long insertCompletedDeed(int deedId, int custom, String date, String note) {
        // The class ContentValues allows to define key/values. The "key" represents the
        // table column identifier and the "value" represents the content for the table
        // record in this column. ContentValues can be used for inserts and updates of database entries.
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DEEDID, deedId);
        initialValues.put(KEY_CUSTOM, custom);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_NOTE, note);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Retrieves a completed deed by it's ID
    public Cursor getCompletedDeed(int deedId) throws SQLException {

        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
                KEY_ROWID,KEY_DEEDID, KEY_CUSTOM, KEY_DATE, KEY_NOTE}, KEY_DEEDID + " = " + deedId, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    // Retrieves all the completed deeds
    public Cursor getAllCompletedDeeds() {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
                KEY_ROWID,KEY_DEEDID, KEY_CUSTOM, KEY_DATE, KEY_NOTE}, null, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // Deletes all rows from the completed_deeds table
    public void deleteDeeds() {
        db.delete(DATABASE_TABLE, null, null);
    }

}