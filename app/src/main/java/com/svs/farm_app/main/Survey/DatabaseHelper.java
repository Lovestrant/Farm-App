package com.svs.farm_app.main.Survey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "Survey.db", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE survey_responses(id INTEGER  primary key autoincrement, survey_key TEXT,user_id TEXT,fid TEXT,company_id TEXT,season_id TEXT, survey_date TEXT,question_id TEXT,feedback TEXT)");
        }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE if exists survey_responses");

    }


    public Boolean addAnswerResponse(String survey_key, String user_id, String fid, String company_id, String season_id, String survey_date, String question_id, String feedback) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
       // values.put("id", "");
        values.put("survey_key", survey_key);
        values.put("user_id", user_id);
        values.put("fid", fid);
        values.put("company_id", company_id);
        values.put("season_id", season_id);
        values.put("survey_date", survey_date);
        values.put("question_id", question_id);
        values.put("feedback", feedback);

        long result = db.insert("survey_responses", null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor CheckSurveyData(int question_id) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor=DB.rawQuery("Select * from survey_responses where question_id =?",new String[]{String.valueOf(question_id)});
        return cursor;
    }

    public Cursor CheckSurveyDataSingle(String user_id,String question_id) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor=DB.rawQuery("Select * from survey_responses where question_id =? and user_id = ?",new String[]{question_id, user_id});
        return cursor;
    }

    public Boolean deleteSurveyData(int id){
        SQLiteDatabase DB=this.getWritableDatabase();

        Cursor cursor=DB.rawQuery("Select * from survey_responses where id=?", new String[]{String.valueOf(id)});
        if(cursor.getCount()>0) {
            long result = DB.delete("survey_responses", "id=?", new String[]{String.valueOf(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }
}
