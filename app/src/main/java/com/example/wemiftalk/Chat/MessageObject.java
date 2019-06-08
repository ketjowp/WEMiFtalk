package com.example.wemiftalk.Chat;

public class MessageObject {

    String messageId,
            SenderId,
            message;

    public MessageObject(String messageId,String SenderId,String message){
        this.messageId=messageId;
        this.SenderId=SenderId;
        this.message=message;
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
}
