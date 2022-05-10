package com.svs.farm_app.main.Survey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuestionsResponse {

    String question_id, questions_cat, questions_cat_id,  question_type,  question, comment_field_value,  question_status,questionAnswer;


    public QuestionsResponse(String question_id, String questions_cat_id, String questions_cat, String question_type, String question, String comment_field_value, String question_status) {
        this.question_id = question_id;
        this.questions_cat_id = questions_cat_id;
        this.questions_cat = questions_cat;
        this.question_type = question_type;
        this.question = question;
        this.comment_field_value = comment_field_value;
        this.question_status = question_status;
        this.questionAnswer = questionAnswer;
    }

    public String getQuestion_id() {
        return question_id;
    }
    public String questionAnswer() {
        return questionAnswer;
    }

    public String questions_cat() {
        return questions_cat;
    }

    public String getQuestions_cat_id() {
        return questions_cat_id;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public String getQuestion() {
        return question;
    }

    public String getComment_field_value() {
        return comment_field_value;
    }

    public String getQuestion_status() {
        return question_status;
    }


}
