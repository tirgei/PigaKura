package com.masterclass.pigakura.pojo;

/**
 * Created by root on 7/15/17.
 */

public class Users {

    private String userId;
    private boolean hasVoted;

    public Users(){}

    public Users(String userId, boolean hasVoted){
        this.userId = userId;
        this.hasVoted = hasVoted;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}
