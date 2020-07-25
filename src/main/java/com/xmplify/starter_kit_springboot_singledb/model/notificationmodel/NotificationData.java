package com.xmplify.starter_kit_springboot_singledb.model.notificationmodel;

public class NotificationData<T> {
    private String to; // "/topics/"+ topic name

    private T data;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
