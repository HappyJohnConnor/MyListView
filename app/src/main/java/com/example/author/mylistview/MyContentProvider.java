package com.example.author.mylistview;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by author on 2017/08/25.
 */

public class MyContentProvider extends ContentProvider {
    private MyDBHelper mDBHelper;
    private static final String AUTHORITY = "com.example.author.mylistview.MyContentProvider";

    private static final int REMINDER_RECORD = 10;
    private static final int REMINDER_RECORD_ID = 20;
    private static final String BASE_PATH = "listview";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, REMINDER_RECORD);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", REMINDER_RECORD_ID);
    }

    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        mDBHelper = new MyDBHelper(getContext());
        mDatabase = mDBHelper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(MyDBHelper.TABLE_NAME);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case REMINDER_RECORD:
                break;
            case REMINDER_RECORD_ID:
                queryBuilder.appendWhere(MyDBHelper.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = mDBHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case REMINDER_RECORD:
                id = sqlDB.insert(MyDBHelper.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case REMINDER_RECORD_ID:
                return mDatabase.delete(MyDBHelper.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("invalid uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        count=mDatabase.update(MyDBHelper.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
