package com.example.ecommerce;

public class MessageModel {
    private String Message;

    private String senderid;

    private String Time;

    public MessageModel() {

    }

    public MessageModel(String message, String senderid, String time) {
        Message = message;
        this.senderid = senderid;
        Time = time;
    }


    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }


}
