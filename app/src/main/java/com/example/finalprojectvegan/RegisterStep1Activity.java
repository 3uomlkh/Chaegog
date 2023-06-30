package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.finalprojectvegan.Model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterStep1Activity extends AppCompatActivity {

    // 변수 선언
    private EditText Et_Register_Id, Et_Register_Email, Et_Register_Pw, Et_Register_PwCheck;
    private Button Btn_Register1To2;

    // Firebase 인증, 처리중 화면
    private FirebaseAuth firebaseAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step1);

        // 변수 초기화
        Et_Register_Id = findViewById(R.id.Et_Register_Id);
        Et_Register_Email = findViewById(R.id.Et_Register_Email);
        Et_Register_Pw = findViewById(R.id.Et_Register_Pw);
        Et_Register_PwCheck = findViewById(R.id.Et_Register_PwCheck);

        firebaseAuth = FirebaseAuth.getInstance(); // 초기화

        Btn_Register1To2 = findViewById(R.id.Btn_Register1To2);
        Btn_Register1To2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 처리중 화면 띄우기
                pd = new ProgressDialog(RegisterStep1Activity.this);
                pd.setMessage("Please wait..");
                pd.show();

                // Et에 현재 입력되어 있는 값을 get(가져오기)해준다.
                String userId = Et_Register_Id.getText().toString();
                String userPw = Et_Register_Pw.getText().toString();
                String userPwCheck = Et_Register_PwCheck.getText().toString();
                String userEmail = Et_Register_Email.getText().toString();

                // Et 양식 조건
                if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPw) || TextUtils.isEmpty(userPwCheck)){
                    Toast.makeText(RegisterStep1Activity.this, "모든 양식을 채워주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    if (userPw.length() < 6){
                        Toast.makeText(RegisterStep1Activity.this, "비밀번호는 최소 6자리 이상으로 해주세요!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (userPw.equals(userPwCheck)) {
                            register(userId, userEmail, userPw);
                        } else {
                            Toast.makeText(RegisterStep1Activity.this, "비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                // 닷홈 서버와 php 통신하기
//                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            boolean success = jsonObject.getBoolean("success");
//                            if (success) {
////                                Toast.makeText(getApplicationContext(), "회원 정보 1단계 등록 완료", Toast.LENGTH_SHORT).show();
////                                Intent intent = new Intent(RegisterStep1Activity.this, RegisterStep2Activity.class);
////                                startActivity(intent);
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), "회원 정보 1단계 등록 실패", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                };

//                RegisterStep1Request registerStep1Request = new RegisterStep1Request(userID, userEmail, userPassword, responseListener);
//                RequestQueue queue = Volley.newRequestQueue(RegisterStep1Activity.this);
//                queue.add(registerStep1Request);

            }
        });

    }

    // 회원가입 정보 등록
    private void register (String userId, String userEmail, String userPw) {

        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPw)
                .addOnCompleteListener(RegisterStep1Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("REGISTER1", "SUCCESS");

                            // 처리중 화면 종료
                            pd.dismiss();
                            Intent intent = new Intent(RegisterStep1Activity.this, RegisterStep2Activity.class);
                            intent.putExtra("userId", userId);
                            intent.putExtra("userEmail", userEmail);
                            intent.putExtra("userPw", userPw);
                            startActivity(intent);

                        } else {

                            Log.d("REGISTER1", "FAILURE");

                            // 유저입력 정보가 유효하지 않을경우
                            pd.dismiss();
                            Toast.makeText(RegisterStep1Activity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
