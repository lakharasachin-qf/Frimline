package com.app.frimline.models;

import java.util.ArrayList;

public class QAModel {
    private String id;
    private String date="";
    private String question="";
    private String answer="";
    private ArrayList<QAModel> blogList=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ArrayList<QAModel> getBlogList() {
        return blogList;
    }

    public void setBlogList(ArrayList<QAModel> blogList) {
        this.blogList = blogList;
    }
}
