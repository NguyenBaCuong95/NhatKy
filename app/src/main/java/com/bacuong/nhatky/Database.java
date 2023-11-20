package com.bacuong.nhatky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

//    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Diary_Dream";

    private static final String TABLE = "Diary";
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_CONTENT = "Content";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String create_table = "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_DATE + " TEXT," +
                COLUMN_TITLE + " TEXT," + COLUMN_CONTENT + " TEXT)";
        database.execSQL(create_table);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(database);
    }

    public void addDiary(Diary diary) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, diary.getDate());
        values.put(COLUMN_TITLE, diary.getTitle());
        values.put(COLUMN_CONTENT, diary.getContent());
        database.insert(TABLE, null, values);
        database.close();
    }

    public void updateDiary(Diary diary) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, diary.getDate());
        values.put(COLUMN_TITLE, diary.getTitle());
        values.put(COLUMN_CONTENT, diary.getContent());
        database.update(TABLE, values, COLUMN_ID + "=?", new String[] {String.valueOf(diary.getId())});
        database.close();
    }

    public void deleteDiary(int id) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE, COLUMN_ID + "=?", new String[] { String.valueOf(id)});
        database.close();
    }

    public Diary getDiary(int id) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE, null, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
//        if(cursor != null)
//            cursor.moveToFirst();
        Diary diary = new Diary(cursor.getInt(0), cursor.getString(1),
                                cursor.getString(2), cursor.getString(3));
        cursor.close();
        return diary;
    }

    public ArrayList<Diary> getAllDiary() {
        ArrayList<Diary> diaryList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Diary diary = new Diary(cursor.getInt(0), cursor.getString(1),
                                    cursor.getString(2), cursor.getString(3));
                diaryList.add(diary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return diaryList;
    }

    public int getDiaryCount() {
        String query = "SELECT * FROM " + TABLE;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
