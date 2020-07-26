package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.notificationmodel.NotificationData;
import com.xmplify.starter_kit_springboot_singledb.repository.AccountRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/pushNotification")
public class PushNotificationController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AdminRepository adminRepository;


    @GetMapping("/")
    public String sendPushNotification() {

        String topicName = "news";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "key=AAAAYPwXgBo:APA91bFnzXAIZvFJNoJvwFZyehB0qt0aOXRlmYmA00GsnCvAvnKT3gj3s3QowP-gtsOHbj4Pcpu9YcLFplIsRrfFcL75UJ2Q3FgTvJbcxu3fam-4APtcZ0bEDMJFGKqMdbCyGSBgg5m2");

        NotificationData<String> notificationModel = new NotificationData<>();
        notificationModel.setTo("/topics/" + topicName);

        HttpEntity<NotificationData> request = new HttpEntity<>(notificationModel, headers);

        RestTemplate restTemplate = new RestTemplate();
        String value = restTemplate.exchange("https://fcm.googleapis.com/fcm/send", HttpMethod.POST, request, String.class).getBody();

        System.out.println("PushNotificationController :sendPushNotification : " + value);
        return value;

    }

}
