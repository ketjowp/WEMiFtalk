package com.example.wemiftalk.Chat;

import java.util.ArrayList;

public class MessageObject {

    String messageId,
            SenderId,
            message;

    ArrayList<String> mediaUrlList;

    public MessageObject(String messageId,String SenderId,String message,ArrayList<String> mediaUrlList){
        this.messageId=messageId;
        this.SenderId=SenderId;
        this.message=message;
        this.mediaUrlList=mediaUrlList;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public ArrayList<String> getMediaUrlList() {
        return mediaUrlList;
    }
}
