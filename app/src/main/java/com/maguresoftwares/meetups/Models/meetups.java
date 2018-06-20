package com.maguresoftwares.meetups.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.security.Key;
import java.util.List;

public class meetups implements Parcelable {

    private String meetup_title;
    private String description;
    private String location;
    private String host_name;
    private String host_occupation;
    private String creator_id;
    private String key;
    private String meetup_id;
    private List<meetup_questions> meetup_question;
    private String date;
    private String meetup_rating;
    private String number_of_ratings;
    private String time_from;
    private String time_till;

    public meetups(String meetup_title, String description, String location, String host_name, String host_occupation, String creator_id, String key, String meetup_id, List<meetup_questions> meetup_question, String date, String meetup_rating, String number_of_ratings, String time_from, String time_till) {
        this.meetup_title = meetup_title;
        this.description = description;
        this.location = location;
        this.host_name = host_name;
        this.host_occupation = host_occupation;
        this.creator_id = creator_id;
        this.key = key;
        this.meetup_id = meetup_id;
        this.meetup_question = meetup_question;
        this.date = date;
        this.meetup_rating = meetup_rating;
        this.number_of_ratings = number_of_ratings;
        this.time_from = time_from;
        this.time_till = time_till;
    }


    public meetups() {

    }

    protected meetups(Parcel in) {
        meetup_title = in.readString();
        description = in.readString();
        location = in.readString();
        host_name = in.readString();
        host_occupation = in.readString();
        creator_id = in.readString();
        key = in.readString();
        meetup_id = in.readString();
        date = in.readString();
        time_from = in.readString();
        time_till = in.readString();
        meetup_rating = in.readString();
        number_of_ratings = in.readString();
    }

    public static final Creator<meetups> CREATOR = new Creator<meetups>() {
        @Override
        public meetups createFromParcel(Parcel in) {
            return new meetups(in);
        }

        @Override
        public meetups[] newArray(int size) {
            return new meetups[size];
        }
    };

    public String getMeetup_rating() {
        return meetup_rating;
    }

    public void setMeetup_rating(String meetup_rating) {
        this.meetup_rating = meetup_rating;
    }

    public String getNumber_of_ratings() {
        return number_of_ratings;
    }

    public void setNumber_of_ratings(String number_of_ratings) {
        this.number_of_ratings = number_of_ratings;
    }

    public List<meetup_questions> getMeetup_question() {
        return meetup_question;
    }

    public void setMeetup_question(List<meetup_questions> meetup_question) {
        this.meetup_question= meetup_question;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHost_name() {
        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public String getHost_occupation() {
        return host_occupation;
    }

    public void setHost_occupation(String host_occupation) {
        this.host_occupation = host_occupation;
    }

    public String getMeetup_id() {
        return meetup_id;
    }

    public void setMeetup_id(String chatroom_id) {
        this.meetup_id = chatroom_id;
    }

    public String getMeetup_title() {
        return meetup_title;
    }

    public void setMeetup_title(String meetup_title) {
        this.meetup_title = meetup_title;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime_from() {
        return time_from;
    }

    public void setTime_from(String time_from) {
        this.time_from = time_from;
    }

    public String getTime_till() {
        return time_till;
    }

    public void setTime_till(String time_till) {
        this.time_till = time_till;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "meetups{" +
                "meetup_title='" + meetup_title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", host_name='" + host_name + '\'' +
                ", host_occupation='" + host_occupation + '\'' +
                ", creator_id='" + creator_id + '\'' +
                ", key='" + key + '\'' +
                ", meetup_id='" + meetup_id + '\'' +
                ", meetup_question=" + meetup_question +
                ", date='" + date + '\'' +
                ", meetup_rating='" + meetup_rating + '\'' +
                ", number_of_ratings='" + number_of_ratings + '\'' +
                ", time_from='" + time_from + '\'' +
                ", time_till='" + time_till + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(meetup_title);
        parcel.writeString(description);
        parcel.writeString(location);
        parcel.writeString(host_name);
        parcel.writeString(host_occupation);
        parcel.writeString(creator_id);
        parcel.writeString(key);
        parcel.writeString(meetup_id);
        parcel.writeString(date);
        parcel.writeString(time_from);
        parcel.writeString(time_till);
        parcel.writeString(meetup_rating);
        parcel.writeString(number_of_ratings);
    }
}

