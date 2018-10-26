package com.example.anonymous.utils;

import com.example.anonymous.exception.ServerException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
    public static String encryptSHA256(String str) {
        String encryptString = "";

        MessageDigest digestMessage=null;
        try {
            digestMessage = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new ServerException("비밀 번호 암호화중 실패");
        }
        digestMessage.update(str.getBytes());
        byte byteData[] = digestMessage.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        encryptString = sb.toString();

        return encryptString;
    }
}
