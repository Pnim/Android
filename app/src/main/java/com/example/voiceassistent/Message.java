package com.example.voiceassistent;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    public String text;
    public Date dateTime;
    public Boolean wasSend;

    public Message() { }

    public Message(String text, Date dateTime, Boolean wasSend) {
        this.text = text;
        this.dateTime = dateTime;
        this.wasSend = wasSend;
    }

    public Message(MessageModel entity) throws ParseException {

        text = entity.text;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        dateTime = dateFormat.parse(entity.dateTime);
        wasSend = ToBool(entity.wasSend);
    }

    private boolean ToBool(int value) {
        return value == 1 ? true : false;
    }
}
