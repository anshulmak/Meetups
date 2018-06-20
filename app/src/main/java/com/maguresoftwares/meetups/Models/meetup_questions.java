package com.maguresoftwares.meetups.Models;

import java.util.List;

public class meetup_questions {

    private String question;
    private String user_id;
    private String timestamp;
    private String name;
    private List<user_like> user_likes;
    private String number_Of_likes;
    private String question_id;

    public meetup_questions(String question, String user_id, String timestamp, String name , String number_Of_likes,List<user_like> user_likes,String question_id) {
        this.question = question;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.name = name;
        this.user_likes = user_likes;
        this.number_Of_likes=number_Of_likes;
        this.question_id=question_id;
    }

    public meetup_questions() {

    }

    public List<user_like> getUser_likes() {
        return user_likes;
    }

    public void setUser_likes(List<user_like> user_likes) {
        this.user_likes = user_likes;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNumber_Of_likes() {
        return number_Of_likes;
    }

    public void setNumber_Of_likes(String number_Of_likes) {
        this.number_Of_likes = number_Of_likes;
    }

    @Override
    public String toString() {
        return "meetup_questions{" +
                "question='" + question + '\'' +
                ", user_id='" + user_id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", name='" + name + '\'' +
                ", user_likes=" + user_likes +
                ", number_Of_likes='" + number_Of_likes + '\'' +
                ", question_id='" + question_id + '\'' +
                '}';
    }
}

