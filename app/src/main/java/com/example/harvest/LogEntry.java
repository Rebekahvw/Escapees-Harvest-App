package com.example.harvest;

import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEntry {
    private String documentID;
    private String userID;
    private String timeCreated;
    private String produceType;
    private float weight;

    public LogEntry(){
        //firestore always needs a public empty constructor
    }

    public LogEntry(String userID, String produceType, float weight, String timeCreated){

        this.userID = userID;
        this.produceType = produceType;
        this.weight=weight;
        this.timeCreated = timeCreated;
    }
    @Exclude //prevents the document ID from being stored as a field in the log
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getTimeCreated(){
        return timeCreated;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProduceType() {
        return produceType;
    }

    public float getWeight() {
        return weight;
    }
}

