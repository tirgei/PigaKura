package com.masterclass.pigakura.commoners;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.masterclass.pigakura.pojo.AboutCandidate;

import java.util.List;

/**
 * Created by tirgei on 7/4/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static int DB_VERSION =1;
    private static final String DB_NAME = "piga_kura";
    private static final String PROFILE_TABLE = "profile";

    private static final String KEY_ID = "id";
    private static final String CANDIDATE_NAME = "candidate_name";
    private static final String RUNNING_MATE = "running_mate";
    private static final String PARTY_NAME = "party_name";
    private static final String ABOUT = "about";
    private static final String MANIFESTO = "manifesto";
    private Context context;

    public DatabaseHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + PROFILE_TABLE + " ("
                + KEY_ID + " INTEGER, " + CANDIDATE_NAME + " TEXT NOT NULL, "
                + RUNNING_MATE + " TEXT NOT NULL, " + PARTY_NAME + " TEXT NOT NULL, "
                + ABOUT + " TEXT NOT NULL, " + MANIFESTO + " TEXT NOT NULL )";

        db.execSQL(CREATE_PROFILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
        onCreate(db);
    }

    public void addProfile(AboutCandidate candidate){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, candidate.getCandidateId());
        values.put(CANDIDATE_NAME, candidate.getCandidateName());
        values.put(RUNNING_MATE, candidate.getRunningMate());
        values.put(PARTY_NAME, candidate.getPartyName());
        values.put(ABOUT, candidate.getAboutCandidate());
        values.put(MANIFESTO, candidate.getAboutManifesto());

        db.insert(PROFILE_TABLE, null, values);
        //Toast.makeText(context, "Added successfully!", Toast.LENGTH_LONG).show();
        db.close();
    }

    public Cursor getCandidate(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(PROFILE_TABLE, new String[]{KEY_ID, CANDIDATE_NAME, RUNNING_MATE, PARTY_NAME, ABOUT, MANIFESTO}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        /*if(cursor != null)
            cursor.moveToFirst();*/

        //AboutCandidate candidate = new AboutCandidate(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return cursor;
    }

    public int updateProfile(AboutCandidate candidate){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CANDIDATE_NAME, candidate.getCandidateName());
        values.put(RUNNING_MATE, candidate.getRunningMate());
        values.put(PARTY_NAME, candidate.getPartyName());
        values.put(ABOUT, candidate.getAboutCandidate());
        values.put(MANIFESTO, candidate.getAboutManifesto());

        return db.update(PROFILE_TABLE, values, KEY_ID + "=?", new String[]{String.valueOf(candidate.getCandidateId())});
    }

    public void deleteProfile(AboutCandidate candidate){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PROFILE_TABLE, KEY_ID + "=?", new String[]{String.valueOf(KEY_ID)});

        db.close();
    }
}
