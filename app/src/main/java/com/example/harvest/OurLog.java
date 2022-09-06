package com.example.harvest;

import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OurLog {
    private String documentID;
    private String userID;
    private String timeCreated;
    private String logName;
    public OurLog(){
        //firestore always needs a public empty constructor
    }

    public OurLog(String userID, String logName, String timeCreated){

        this.userID = userID;

        this.timeCreated = timeCreated;
        this.logName = logName;
    }
    @Exclude //prevents the document ID from being stored as a field in the log
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getLogName(){
        return logName;
    }
    public String getTimeCreated(){
        return timeCreated;
    }
}

