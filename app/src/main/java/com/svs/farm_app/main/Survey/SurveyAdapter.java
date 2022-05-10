package com.svs.farm_app.main.Survey;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.authentication.utils.VolleySingleton;
import com.svs.farm_app.R;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.myViewHolder>{
    ArrayList<QuestionsResponse> dataHolder;
    EditText questionAnswer;



    public SurveyAdapter(ArrayList<QuestionsResponse> dataHolder) {
        this.dataHolder = dataHolder;

    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_layout,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.questionId.setText(dataHolder.get(position).getQuestion_id());
        holder.questionId.setVisibility(View.GONE);
        holder.question.setText(dataHolder.get(position).getQuestion());


        //Object in Model
        QuestionsResponse s = dataHolder.get(position);
        int question_id = Integer.parseInt(s.getQuestion_id());
        int user_id = Integer.parseInt(Preferences.USER_ID);
        DatabaseHelper db = new DatabaseHelper(holder.itemView.getContext());

        Cursor checkData = db.CheckSurveyDataSingle(String.valueOf(user_id),String.valueOf(question_id));
        if (checkData.getCount() > 0) {
            int i;
            for (i = 0; i < checkData.getCount(); i++) {
                if (checkData.moveToNext()) {

                    int userId = checkData.getInt(2);
                    int questionId = checkData.getInt(7);
                    String TheFeedback = checkData.getString(8);

                    holder.submitBtn.setBackgroundColor(Color.parseColor("#660000"));
                    holder.submitBtn.setText("RESUBMIT TO SERVER");

                    holder.questionId.setText("Answered: "+ TheFeedback);
                    holder.questionId.setVisibility(View.VISIBLE);
                    holder.questionId.setTextColor(Color.parseColor("#660000"));
                    holder.questionAnswer.setVisibility(View.GONE);

                }
            }


        }

       // set onclick listener to the submit btn
        holder.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Date
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                //Survey Key, random number
                String SurveyKey = String.valueOf((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);

                //Other values of the date

                int user_id = Integer.parseInt(Preferences.USER_ID);
                int company_id = Integer.parseInt(Preferences.COMPANY_ID);
                String survey_date = dtf.format(now);

                String feedback = holder.questionAnswer.getText().toString();

                int question_id = Integer.parseInt(s.getQuestion_id());
                String questions_cat_id = s.getQuestions_cat_id();
                String questions_cat = s.questions_cat;
                String question_type = s.question_type;
                String question = s.getQuestion();
                String comment_field_value = s.getComment_field_value();
                String question_status = s.getQuestion_status();

                if(holder.submitBtn.getText().equals("RESUBMIT TO SERVER")) {
                    Survey.UploadDataToServer(holder,v.getContext(), question_id);

                }else {

                    if(holder.questionAnswer.getText().toString().equals("")) {
                        holder.questionAnswer.setError("Answer Required");
                    }else {

                        //Submit data to API
                        SubmitSurveyAnswersToTheApi(holder,v.getContext(), SurveyKey,user_id,question_id,company_id,company_id,survey_date,question_id, feedback);

                    }

                }
            }
        });


    }

    public void SubmitSurveyAnswersToTheApi(myViewHolder holder,Context context, String surveyKey, int user_id, int question_id, int company_id, int company_id1, String survey_date, int question_id1, String feedback) {
        //Data in Json to be sent to the API POST Endpoint
        // Map<String, String> map = new HashMap<>();

        JSONObject requestObject =new JSONObject();
        try{

            requestObject.put("surveys",getJsonObject(surveyKey,user_id,question_id,company_id,company_id1,survey_date,question_id,feedback));

        }catch(JSONException e){

        }

        //Molding the response from JSon

        Preferences.savePrefenceSettings(context);


        // creating a new variable for our request queue
        RequestQueue mRequestQueue = VolleySingleton.getInstance(context).getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest (Request.Method.POST, Config.SURVEY_RESPONSE,requestObject, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {

                    JSONObject  respObj = new JSONObject(response.toString());

                    // below are the strings which we
                    // extract from our json object.
                    String message = respObj.getString("message");

                    if(message.equals("Surveys saved successfully")) {

                        Toast.makeText(context, "Data post Success", Toast.LENGTH_SHORT).show();

                        holder.submitBtn.setVisibility(View.GONE);
                        holder.questionId.setText("Answered Already");
                        holder.questionId.setVisibility(View.VISIBLE);
                        holder.questionId.setTextColor(Color.parseColor("#008000"));
                        holder.questionAnswer.setVisibility(View.GONE);


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
                    Boolean checkStatus = db.addAnswerResponse(surveyKey, String.valueOf(user_id), String.valueOf(question_id), String.valueOf(company_id), String.valueOf(company_id1), survey_date, String.valueOf(question_id), feedback);

                    if( checkStatus == true) {
                        Toast.makeText(context, "Data saved to local DB, You will upload when internet is back", Toast.LENGTH_SHORT).show();
                    }

                    holder.submitBtn.setBackgroundColor(Color.parseColor("#660000"));
                    holder.submitBtn.setText("RESUBMIT TO SERVER");

                }


            }

        });

        mRequestQueue.add(request);



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

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView questionId,question;
        Button submitBtn; EditText questionAnswer;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            questionId = (TextView)itemView.findViewById(R.id.questionId);
            question = (TextView)itemView.findViewById(R.id.question);
            questionAnswer = (EditText) itemView.findViewById(R.id.questionAnswer);
            submitBtn = (Button)itemView.findViewById(R.id.SubmitBtn);


        }
    }

}




