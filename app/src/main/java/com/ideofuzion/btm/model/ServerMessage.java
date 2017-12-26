package com.ideofuzion.btm.model;

//After response of server this class is in responce
public class ServerMessage {
    private int code;
    private String message;
    private String data;

    public ServerMessage() {
        message = "";
        data = "";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
