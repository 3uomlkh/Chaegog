package com.cchaegog.chaegog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cchaegog.chaegog.Model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import kotlinx.coroutines.Delay;

public class RegisterStep1Activity extends AppCompatActivity {

    // 변수 선언
    private EditText Et_Register_Id, Et_Register_Email, Et_Register_Pw, Et_Register_PwCheck;
    private Button Btn_Register1To2;
    private static final String TAG = "Log";

    // Firebase 인증, 처리중 화면
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Dialog dialog, dialogSuccess, dialogMailVerify;
    ProgressDialog pd;
    Handler handler = new Handler();

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
        firebaseUser = firebaseAuth.getCurrentUser();

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
                            // 조건 모두 성립함 -> 회원가입 진행
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

                            pd.dismiss();

                            firebaseUser = firebaseAuth.getCurrentUser();
                            dialogMailVerify = new Dialog(RegisterStep1Activity.this);
                            dialogMailVerify.setContentView(R.layout.dialog_email_verify);
                            dialogMailVerify.show();

                            Button Btn_Verify = dialogMailVerify.findViewById(R.id.Btn_Verify);
                            Button Btn_Cancle = dialogMailVerify.findViewById(R.id.Btn_Cancle);

                            Btn_Verify.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    sendEmail(firebaseUser, userId, userEmail, userPw);
                                    dialogMailVerify.dismiss();
                                }
                            });

                            Btn_Cancle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogMailVerify.dismiss();
                                }
                            });
                        } else {

                            Log.d(TAG, "회원가입 실패");
                            // 유저입력 정보가 유효하지 않을경우
                            pd.dismiss();
                            Toast.makeText(RegisterStep1Activity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendEmail(FirebaseUser user, String userId, String userEmail, String userPw) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "인증메일 전송 성공");
                            Toast.makeText(RegisterStep1Activity.this, "메일을 전송하였습니다\n인증 성공시 회원가입이 계속됩니다", Toast.LENGTH_SHORT).show();

                            dialogSuccess = new Dialog(RegisterStep1Activity.this);
                            dialogSuccess.setContentView(R.layout.dialog_email_success);
                            dialogSuccess.show();

                            Button Btn_Success = dialogSuccess.findViewById(R.id.Btn_Success);
                            Button Btn_Cancle = dialogSuccess.findViewById(R.id.Btn_Cancle);

                            Btn_Success.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    pd = new ProgressDialog(RegisterStep1Activity.this);
                                    pd.setMessage("Please wait..");
                                    pd.show();

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            pd.dismiss();
                                            user.reload();
                                            verifyEmail(user, userId, userEmail, userPw);
                                        }
                                    }, 5000);
                                }
                            });

                            Btn_Cancle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogSuccess.dismiss();
                                    Log.d(TAG, "메일 인증 취소 클릭");
                                }
                            });

                        } else {
                            Log.d(TAG, "인증메일 전송 실패");
                            Toast.makeText(RegisterStep1Activity.this, "메일을 전송에 실패하였습니다.\n인증 재시도 해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void verifyEmail(FirebaseUser user, String userId, String userEmail, String userPw) {
//        user.reload();
//        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser.isEmailVerified()) {
            Log.d(TAG, "메일 인증 성공");

            Intent intent = new Intent(RegisterStep1Activity.this, RegisterStep2Activity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("userEmail", userEmail);
            intent.putExtra("userPw", userPw);
            startActivity(intent);
//            dialogSuccess.dismiss();
            Log.d(TAG, "메일 인증 성공 클릭");
        } else {
            dialog = new Dialog(RegisterStep1Activity.this);
            dialog.setContentView(R.layout.dialog_email);
            dialog.show();

            Button Btn_ReSend = dialog.findViewById(R.id.Btn_ReSend);
            Button Btn_Cancle = dialog.findViewById(R.id.Btn_Cancle);

            Btn_ReSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendEmail(user, userId, userEmail, userPw);
                    dialog.dismiss();
                    Log.d(TAG, "메일 재전송 클릭");
                }
            });

            Btn_Cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Log.d(TAG, "메일 인증 취소 클릭");
                }
            });

            Log.d(TAG, "메일 인증 실패");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        if (firebaseUser != null) {
//            firebaseUser.delete()
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                firebaseAuth.signOut();
//                                Log.d(TAG, "계정삭제 성공");
//                                Intent intent = new Intent(RegisterStep1Activity.this, LoginActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Log.d(TAG, "계정 삭제 실패");
//                            }
//                        }
//                    });
//        } else {
//            Intent intent = new Intent(RegisterStep1Activity.this, LoginActivity.class);
//            startActivity(intent);
//        }
    }
}
