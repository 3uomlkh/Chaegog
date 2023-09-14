package com.example.finalprojectvegan;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AppCompatActivity {
    String user = "cchaegog@gmail.com"; // 보내는 계정의 id
    String password = "gqxg poqe mstf ekye"; // 보내는 계정의 pw

    String uidKey; // 신고당한 게시물 ID

    Intent intent = getIntent();

    GMailSender gMailSender = new GMailSender(user, password);
    String emailCode = gMailSender.getEmailCode();
    public void sendSecurityCode(Context context, String sendTo) {

//        uidKey = intent.getStringExtra("uidKey");

        Log.d("이메일 전송", "아주 성공적");
        try {
            gMailSender.sendMail("게시물 신고 보고서", "3회 이상 신고되었습니다.", sendTo);
            Toast.makeText(context, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {
            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}