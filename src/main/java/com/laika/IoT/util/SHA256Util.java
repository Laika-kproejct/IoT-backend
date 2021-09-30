package com.laika.IoT.util;


import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SHA256Util {
    public static String getEncrypt(String source, String salt) {
        return getEncrypt(source, salt.getBytes());
    }

    public static String getEncrypt(String source, byte[] salt) {

        String result = ""; // 결과를 담을 객체

        byte[] a = source.getBytes(); // 입력받은 비밀번호를 byte 배열로 변환한다
        byte[] bytes = new byte[a.length + salt.length]; // 바이트 배열의 길이는 비밀번호 길이 + salt 길이

        System.arraycopy(a, 0, bytes, 0, a.length); // bytes 에 입력받은 비밀번호를 추가한다
        System.arraycopy(salt, 0, bytes, a.length, salt.length); // a를 입력한 다음 index부터 salt를 추가한다

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // SHA-256 해시기법 인코딩을 하기 위한 인스턴스 			생성
            md.update(bytes); // 인코딩

            byte[] byteData = md.digest(); // 인코딩한 값을 담는 byte 배열

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) { // SHA-256 인코딩 결과값을 담은 byte 배열을 int 값으로 변환하여 			저장
                sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
            }

            result = sb.toString(); // 최종 결과값을 result에 반환
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String generateSalt() {
        Random random = new Random(); // 랜덤 클래스 생성

        byte[] salt = new byte[8]; // 길이가 8인 byte 배열 생성
        random.nextBytes(salt); // byte 배열에 랜덤값 생성

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < salt.length; i++) {
            // byte 값을 Hex(16진수) 값으로 바꾸기.
            sb.append(String.format("%02x",salt[i]));
        }

        return sb.toString();
    }

}
