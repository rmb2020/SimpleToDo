package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by NB1 on 8/22/2017.
 */

public class TasksDatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "tasksDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TASKS = "tasks";
    // private static final String TABLE_USERS = "users";

    // tasks Table Columns
    private static final String KEY_TASK_ID = "id";
    //private static final String KEY_POST_USER_ID_FK = "userId";
    private static final String KEY_TASK_TEXT = "text";

    private static TasksDatabaseHelper sInstance;

    public static synchronized TasksDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TasksDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private TasksDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS +
                "(" +
                KEY_TASK_ID + " INTEGER PRIMARY KEY," +
                // Define a primary key
                //KEY_POST_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS + "," + // Define a foreign key
                KEY_TASK_TEXT + " TEXT" +
                ")";


        db.execSQL(CREATE_TASKS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    // Insert a post into the database
    public void addPost(Post post) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_TASK_TEXT, post.text);

            db.insertOrThrow(TABLE_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add task to database");
        } finally {
            db.endTransaction();
        }
    }

    public long updateTask(int taskId, String existingTask, String task ) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_TEXT, task);

        return db.update(TABLE_TASKS, values, KEY_TASK_TEXT + "=?", new String[]{existingTask} );
    }

    public long delete(String deleteText){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_TASKS,KEY_TASK_TEXT + "=?",new String[]{deleteText});
    }


    // Get all posts in the database
   // public List<Post> Posts;
    public List<Post> getAllPosts() {
        // somehow List<Post> poats = new ArrayList didn't work. But declaration and alue assignment when separated worked.
        List<Post> posts;
        posts = new ArrayList<>();

        String POSTS_SELECT_QUERY = "SELECT * FROM " + TABLE_TASKS;
        //String POSTS_SELECT_QUERY = "SELECT " + KEY_TASK_TEXT + " FROM " + TABLE_TASKS;

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Post newPost = new Post();
                    newPost.text = cursor.getString(cursor.getColumnIndex(KEY_TASK_TEXT));
                    posts.add(newPost);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get tasks from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }


    // Delete all posts and users in the database
    public void deleteAllPostsAndUsers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_TASKS, null, null);
            // db.delete(TABLE_USERS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }
}

