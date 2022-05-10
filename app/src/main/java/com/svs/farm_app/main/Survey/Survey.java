package com.svs.farm_app.main.Survey;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.authentication.utils.VolleySingleton;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.svs.farm_app.R;
import com.svs.farm_app.main.LoginActivity;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Survey extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<QuestionsResponse> dataHolder = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private Survey mContext;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        recyclerView = findViewById(R.id.SurveyRecyclerView);

        //Volley code to retrieve data from APi

        mRequestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        fetchQuestionsFromApi();


    }

    private void fetchQuestionsFromApi() {
       // String Url = "http://95.217.63.201/farm_app/index.php/api/surveys_questions";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Config.SURVEY_QUESTIONS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String question_id = jsonObject.getString("question_id");
                        String questions_cat_id = jsonObject.getString("questions_cat_id");
                        String questions_cat = jsonObject.getString("questions_cat");
                        String question_type = jsonObject.getString("question_type");
                        String question = jsonObject.getString("question");
                        String comment_field_value = jsonObject.getString("comment_field_value");
                        String question_status = jsonObject.getString("question_status");

                       // if(question_status == "open") {

                            QuestionsResponse questionsResponse = new QuestionsResponse(question_id,questions_cat_id,questions_cat,question_type,question,comment_field_value,question_status);
                            dataHolder.add(questionsResponse);
                      //  }




                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        llm.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(llm);
                        SurveyAdapter adapter = new SurveyAdapter(dataHolder);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("X-API-KEY", Config.API_KEY);
                return params;
            }
        };

        mRequestQueue.add(jsonArrayRequest);

    }


    public static void SubmitSurveyAnswersToApi(Context context, String survey_key, int user_id, int fid, int company_id, int season_id, String survey_date, int question_id, String feedback) {

        //Data in Json to be sent to the API POST Endpoint
       // Map<String, String> map = new HashMap<>();

        JSONObject requestObject =new JSONObject();
        try{

            requestObject.put("surveys",getJsonObject(survey_key,user_id,fid,company_id,season_id,survey_date,question_id,feedback));

        }catch(JSONException e){

        }

        //Molding the response from JSon

        Preferences.savePrefenceSettings(context);


        // creating a new variable for our request queue
        RequestQueue mRequestQueue = VolleySingleton.getInstance(context).getRequestQueue();

        JsonObjectRequest  request = new JsonObjectRequest (Request.Method.POST, Config.SURVEY_RESPONSE,requestObject, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                    try {

                        JSONObject  respObj = new JSONObject(response.toString());

                        // below are the strings which we
                        // extract from our json object.
                        String message = respObj.getString("message");

                        if(message.equals("Surveys saved successfully")) {

                            Toast.makeText(context, "Data post Success", Toast.LENGTH_SHORT).show();

//                            Arrays.fill(status, Boolean.TRUE);



                        Preferences.ReturnStatus= "true";
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Failed: "+ error,Toast.LENGTH_SHORT).show();

                 DatabaseHelper db = new DatabaseHelper(context);


                Cursor checkData = db.CheckSurveyData(question_id);
                if(checkData.getCount() >0) {
                    //Do nothing
                }else{

                    //Insert Data to Local DB when After failing to be uploaded to server
                    Boolean checkStatus = db.addAnswerResponse(survey_key, String.valueOf(user_id), String.valueOf(fid), String.valueOf(company_id), String.valueOf(season_id), survey_date, String.valueOf(question_id), feedback);

                    if( checkStatus == true) {
                        Toast.makeText(context, "Data saved to local DB, You will upload when internet is back", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to save to local DB", Toast.LENGTH_SHORT).show();
                    }

                }


            }

        });

        mRequestQueue.add(request);



    }

    private static Object getFeedBack(int question_id, String feedback) {

        JSONArray arrayFeedback=new JSONArray();

        JSONObject Feedback = new JSONObject();

        try {
            Feedback.put("question_id", question_id);
            Feedback.put("feedback", feedback);
            Feedback.put("comment", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        arrayFeedback.put(Feedback);

        return arrayFeedback;
    }

    private static Object getJsonObject(String survey_key, int user_id, int fid, int company_id, int season_id, String survey_date,int question_id, String feedback) {

        JSONArray Survey=new JSONArray();

        try {

            JSONObject SurveyObject = new JSONObject();
            SurveyObject.put("survey_key", survey_key);
            SurveyObject.put("user_id", user_id);
            SurveyObject.put("fid", fid);
            SurveyObject.put("company_id", company_id);
            SurveyObject.put("season_id", season_id);
            SurveyObject.put("survey_date", survey_date);

           // JSONObject FeedbackObject = new JSONObject();
            SurveyObject.put("feedback", getFeedBack(question_id,feedback));


            Survey.put(SurveyObject);





        }catch (JSONException e) {
            e.printStackTrace();
        }
        return Survey;
    }


    public static void UploadDataToServer(SurveyAdapter.myViewHolder holder, Context context, int question_id) {

        DatabaseHelper db = new DatabaseHelper(context);
        Cursor checkData = db.CheckSurveyData(question_id);
        if (checkData.getCount() > 0) {
            int i;
            for (i = 0; i < checkData.getCount(); i++) {
                if (checkData.moveToNext()) {
                    int id = checkData.getInt(0);
                    String survey_key = checkData.getString(1);
                    int user_id = checkData.getInt(2);
                    int fid = checkData.getInt(3);
                    int company_id = checkData.getInt(4);
                    int season_id = Integer.parseInt(checkData.getString(5));
                    String survey_date = checkData.getString(6);
                    String feedback = checkData.getString(8);


                    SubmitSurveyAnswersToApi(context,survey_key,user_id,fid,company_id,season_id,survey_date,question_id,feedback);

                    Boolean result = db.deleteSurveyData(id);
                    if(result == true) {
                        Toast.makeText(context, "Dropped from Local DB", Toast.LENGTH_SHORT).show();
                        holder.submitBtn.setVisibility(View.GONE);
                        holder.questionId.setText("Answered Already");
                        holder.questionId.setVisibility(View.VISIBLE);
                        holder.questionId.setTextColor(Color.parseColor("#008000"));
                        holder.questionAnswer.setVisibility(View.GONE);
                    }
                }
            }


        }
    }

}