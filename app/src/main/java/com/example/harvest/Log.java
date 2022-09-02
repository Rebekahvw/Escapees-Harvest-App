package com.example.harvest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Log {

    private String userID;
    private String createTime;
    private String logName;

    public Log(){

    }

    public Log(String userID, String logName){

        this.userID = userID;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.createTime = formatter.format(date);
        this.logName = logName;
    }
}
