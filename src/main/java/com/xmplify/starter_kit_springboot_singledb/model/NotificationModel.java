package com.xmplify.starter_kit_springboot_singledb.model;

public class NotificationModel {
    MessageBody message;

    public MessageBody getMessage() {
        return message;
    }

    public void setMessage(MessageBody message) {
        this.message = message;
    }

    public static class MessageBody {
        String topic;
        NotificationBody notification;

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public NotificationBody getNotification() {
            return notification;
        }

        public void setNotification(NotificationBody notification) {
            this.notification = notification;
        }
    }

    public static class NotificationBody {
        String title;
        String body;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
