package com.example.recorder.model;

import com.example.recorder.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: gongming
 * Date: 2/23/14
 * Time: 11:25 上午
 * Email:gongmingqm10@foxmail.com
 */
public class Message {

    private String content;
    private String sender;
    private Date time;

    public Message(String content, String sender) {
        this.content = content;
        this.sender = sender;
        this.time = Calendar.getInstance().getTime();
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String getTime() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return df.format(time);
    }

    public boolean isSender() {
        return Constants.MESSAGE_SENDER_ME.equals(sender);
    }


}
