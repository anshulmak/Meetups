package com.maguresoftwares.meetups.Models;

public class user_like {

    private String user_id;
    private boolean check_like;

    public user_like(String user_id, boolean check_like) {

        this.user_id = user_id;
        this.check_like=check_like;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isCheck_like() {
        return check_like;
    }

    public void setCheck_like(boolean check_like) {
        this.check_like = check_like;
    }

    public user_like() {

    }

    @Override
    public String toString() {
        return "user_like{" +
                "user_id='" + user_id + '\'' +
                ", check_like=" + check_like +
                '}';
    }
}
