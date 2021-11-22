package com.laika.IoT.util;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FCMUtil {
    // 공식 문서에 보면 토픽 지정 보내기, 여러 토큰에 한번에 보내기가 있다. 필요할 때마다 추가해 사용하자
    public void sendToOneToken(String targetToken, String title, String body) throws FirebaseMessagingException {
        // [START send_to_token]
        // This registration token comes from the client FCM SDKs.
        String registrationToken = targetToken;

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setToken(registrationToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
        // [END send_to_token]
    }
    public void sendTokens(List<String> tokens, String title, String body) throws FirebaseMessagingException {
        // [START send_to_token]
        // This registration token comes from the client FCM SDKs.

        // See documentation on defining a message payload.
        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
        // See the BatchResponse reference documentation
        // for the contents of response.
        System.out.println(response.getSuccessCount() + " messages were sent successfully");
    }
}
