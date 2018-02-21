package com.masterclass.pigakura.pojo;

/**
 * Created by tirgei on 6/23/17.
 */

public class ChatForumMessages {
    private String message;
    private String timeStamp;
    private String key;

    public ChatForumMessages(){

    }

    public ChatForumMessages(String message, String time, String key){
        this.message = message;
        this.timeStamp = time;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
