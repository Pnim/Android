package com.example.voiceassistent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MessageModel {
    public String text;
    public String  dateTime;
    public int wasSend;

    public MessageModel(String text, String date, int wasSend){
        this.text = text;
        this.dateTime = date;
        this.wasSend = wasSend;
    }

    public MessageModel(Message message){
        this.text = message.text;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        this.dateTime = dateFormat.format(message.dateTime);
        this.wasSend = message.wasSend ? 1 : 0;
    }
}
