package com.example.lab_4_balls;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import com.example.lab_4_balls.Models.UserScore;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "UserScore.db";
    public static int version = 1;

    private static final String SQL_CREATE_USERSCORES =
            "create table " + UserScores.TABLE_NAME + " (" +
                    UserScores._ID + " integer primary key, " +
                    UserScores.COLUMN_NAME_USERNAME + " TEXT not null unique, " +
                    UserScores.COLUMN_NAME_SCORE + " integer)";

    private static final String SQL_GET_TOP_SCORES =
            "SELECT * FROM " + UserScores.TABLE_NAME +
                    " ORDER BY " + UserScores.COLUMN_NAME_SCORE + " DESC " +
                    " LIMIT %o";

    private static final String SQL_SELECT_LIKE =
            "SELECT * FROM " + UserScores.TABLE_NAME +
                    " WHERE " + UserScores.COLUMN_NAME_USERNAME + " LIKE \"%s%%\"" +
                    " LIMIT %o";

    private static final String SQL_UPDATE_SCORE =
            "UPDATE " + UserScores.TABLE_NAME +
                    " SET " + UserScores.COLUMN_NAME_SCORE + " = %o " +
                    " WHERE " + UserScores.COLUMN_NAME_USERNAME + " == \"%s\"";

    private static final String SQL_SELECT_BY_USERNAME =
            "SELECT * FROM " + UserScores.TABLE_NAME +
                    " WHERE " + UserScores.COLUMN_NAME_USERNAME + " == \"%s\"";

    private static final String SQL_GET_ABS_SCORE_POS =
            "SELECT COUNT(CASE WHEN " +
                    UserScores.COLUMN_NAME_SCORE + " > %o THEN 1 END)+1 AS " +
                    UserScores.COUNT_POS + " FROM " +
                    UserScores.TABLE_NAME;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERSCORES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean has(String username) {
        boolean res = false;
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format(SQL_SELECT_BY_USERNAME, username);
        Cursor cursor = db.rawQuery(query, null);

        res = cursor.moveToFirst();
        cursor.close();
        db.close();

        return res;
    }

    public int scoreByUserName(UserScore userScore) {
        int res = -1;
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format(SQL_SELECT_BY_USERNAME, userScore.username);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndex(UserScores.COLUMN_NAME_SCORE);
            res = cursor.getInt(column_index);
        }
        cursor.close();
        db.close();

        return res;
    }

    public int getAbsScorePos(int score) {
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format(SQL_GET_ABS_SCORE_POS, score);

        Cursor cursor = db.rawQuery(query, null);

        int pos = -1;
        int id = 0;
        if (cursor.moveToFirst()) {
            int column_index_pos = cursor.getColumnIndex(UserScores.COUNT_POS);
            pos = cursor.getInt(column_index_pos);
        }

        cursor.close();
        db.close();

        return pos;
    }

    public ArrayList<UserScore> getlikeUsername(String text, int limit) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<UserScore> res = new ArrayList<>();

        String query = String.format(SQL_SELECT_LIKE, text, limit);

        Cursor cursor = db.rawQuery(query, null);
        int id = 0;
        if (cursor.moveToFirst()) {
            do {
                int column_index_username = cursor.getColumnIndex(UserScores.COLUMN_NAME_USERNAME);
                int column_index_score = cursor.getColumnIndex(UserScores.COLUMN_NAME_SCORE);

                String username = cursor.getString(column_index_username);
                int score = cursor.getInt(column_index_score);

                res.add(new UserScore(getAbsScorePos(score), username, score));
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return res;
    }

    public ArrayList<UserScore> getTopN(int n) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<UserScore> res = new ArrayList<>();

        String query = String.format(SQL_GET_TOP_SCORES, n);

        Cursor cursor = db.rawQuery(query, null);

        int id = 0;
        int lastScore = -1;
        if (cursor.moveToFirst()) {
            do {
                int column_index_username = cursor.getColumnIndex(UserScores.COLUMN_NAME_USERNAME);
                int column_index_score = cursor.getColumnIndex(UserScores.COLUMN_NAME_SCORE);

                String username = cursor.getString(column_index_username);
                int score = cursor.getInt(column_index_score);

                if (lastScore == score) {
                    id--;
                }

                UserScore userScore = new UserScore(++id, username, score);

                lastScore = score;
                res.add(userScore);

            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return res;
    }

    public void updateScores(UserScore userScore) {
        SQLiteDatabase db = getWritableDatabase();

        String query = String.format(SQL_UPDATE_SCORE, userScore.score, userScore.username);
        db.execSQL(query);

        db.close();
    }

    public boolean add(UserScore userScore) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(UserScores.COLUMN_NAME_USERNAME, userScore.username);
        cv.put(UserScores.COLUMN_NAME_SCORE, userScore.score);

        long insert = db.insert(UserScores.TABLE_NAME, null, cv);

        db.close();

        return (insert != -1);
    }

    public static class UserScores implements BaseColumns {
        public static final String TABLE_NAME = "user_scores";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_SCORE = "score";
        public static final String COUNT_POS = "pos";
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}