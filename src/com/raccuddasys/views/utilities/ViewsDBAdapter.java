package com.raccuddasys.views.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ViewsDBAdapter {
	public static final String KEY_TITLE = "title";
    public static final String KEY_LATITUDE = "image_latitude";
    public static final String KEY_LONGITUDE = "image_longitude";
    public static final String KEY_DESCRIPTION = "image_description";
    public static final String KEY_ROWID = "_id";
    
    private static final String TAG = "ViewsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_CREATE =
        "create table views (_id integer primary key autoincrement, "
                + "title text not null, image_latitude text not null, image_longitude text not null, "
                + "image_description text not null);";


    private static final String DATABASE_NAME = "views360";
    private static final String DATABASE_TABLE = "views";
    private static final int DATABASE_VERSION = 2;
    private final Context mCtx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS views");
            onCreate(db);
        }
    }
    
    public ViewsDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    
    public ViewsDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }
    /**
     * Insert a views using the parameters provided. If the view is
     * successfully inserted return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the name of image
     * @param latitude the image latitude obtained from sensors
     * @return rowId or -1 if failed
     */
    public long createView(String title, String latitude, String longitude, String description) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_LATITUDE, latitude);
        initialValues.put(KEY_LONGITUDE, longitude);
        initialValues.put(KEY_DESCRIPTION, description);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the view with the given rowId
     * 
     * @param rowId id of view to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteView(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all views in the database
     * 
     * @return Cursor over all views
     */
    public Cursor fetchAllViews() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_LATITUDE, KEY_LONGITUDE, KEY_DESCRIPTION}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the view that matches the given rowId
     * 
     * @param rowId id of view to retrieve
     * @return Cursor positioned to matching view, if found
     * @throws SQLException if view could not be found/retrieved
     */
    public Cursor fetchView(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_TITLE, KEY_LATITUDE, KEY_LONGITUDE, KEY_DESCRIPTION}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

}
