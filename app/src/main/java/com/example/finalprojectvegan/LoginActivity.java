package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    // 변수 선언
    private EditText Et_Login_Email, Et_Login_Password;
    private Button Btn_Login, Btn_Register, Btn_LoginKakao;
    private TextView Btn_PasswordReset;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    DatabaseReference reference;

    ProgressDialog pd;

    // 뒤로가기 버튼 클릭시 앱 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Log.d("getKeyHash", "" + getKeyHash(LoginActivity.this));

        SharedPreferences sh = getSharedPreferences("temp", MODE_PRIVATE);



        // 변수 초기화
        Et_Login_Email = findViewById(R.id.Et_Login_Email);
        Et_Login_Password = findViewById(R.id.Et_Login_Password);
        Btn_Login = findViewById(R.id.Btn_Login);
        Btn_Register = findViewById(R.id.Btn_Register);
        Btn_PasswordReset = findViewById(R.id.Btn_PasswordReset);
        Btn_LoginKakao = findViewById(R.id.Btn_LoginKakao);

        firebaseAuth = FirebaseAuth.getInstance(); // 초기화
        firebaseUser = firebaseAuth.getCurrentUser();

        // 로그인 버튼 클릭시
        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 처리중 화면 띄우기
                ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Please wait..");
                pd.show();

                // 문자열에 담기
                String userEmail = Et_Login_Email.getText().toString();

                // 로그인 할 때 사용자의 이메일을 저장함(추후 닉네임 저장으로 변경)
//                SharedPreferences.Editor editor = sh.edit();
//                editor.putString("userEmail", userEmail);
//                editor.apply();

                String userPassword = Et_Login_Password.getText().toString();

                // 작성란 확인
                if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)){
                    Toast.makeText(LoginActivity.this, "모두 작성해 주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

//                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        pd.dismiss();

                                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
//                                        updateUI(user);

//                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
//                                                .child(firebaseAuth.getCurrentUser().getUid());
//
//                                        // 데이터베이스 정보 불러오기
//                                        reference.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                pd.dismiss();
//                                                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//                                                pd.dismiss();
//                                            }
//                                        });

                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                        // 아래 내용 필요한지 아닌지
//                                        if (task.getException() != null) {
//                                            Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                                        }
                                        Log.d("SIGNIN", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
//                                        updateUI(null);
                                    }
                                }
                            });
                }

//                    Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            boolean success = jsonObject.getBoolean("success");
//                            if (success) {
//                                String userID = jsonObject.getString("userID");
//                                String userPassword = jsonObject.getString("userPassword");
//                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.putExtra("userID", userID);
//                                intent.putExtra("userPassword", userPassword);
//                                startActivity(intent);
//                            } else {
//                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                LoginRequest loginRequest = new LoginRequest(userEmail, userPassword, responseListener);
//                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//                queue.add(loginRequest);

            }
        });

        // 회원가입 화면으로 전환
        Btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterStep1Activity.class);
                startActivity(intent);
            }
        });

        Btn_PasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PasswordResetActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

//        Btn_LoginKakao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
//                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
//                } else {
//                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
//                }
//            }
//        });
    }

//    public static String getKeyHash(final Context context) {
//        PackageManager pm = context.getPackageManager();
//        try {
//            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
//            if (packageInfo == null)
//                return null;
//
//            for (Signature signature : packageInfo.signatures) {
//                try {
//                    MessageDigest md = MessageDigest.getInstance("SHA");
//                    md.update(signature.toByteArray());
//                    return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
//        @Override
//        public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
//            updateKakaoLoginUi();
//            return null;
//        }
//    };
//
//    private void updateKakaoLoginUi() {
//        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
//            @Override
//            public Unit invoke(User user, Throwable throwable) {
//                if (user != null) {
//                    Log.d("Kakao", "ID : " + user.getId());
//                    Log.d("Kakao", "EMAIL : " + user.getKakaoAccount().getEmail());
//                    Log.d("Kakao", "ninkname : " + user.getKakaoAccount().getProfile().getNickname());
//                    Intent intent = new Intent(LoginActivity.this, RegisterStep2Activity.class);
////                    intent.putExtra("userID", user.getId());
////                    intent.putExtra("userEmail", user.getKakaoAccount().getEmail());
//                    startActivity(intent);
////                    register(user.getKakaoAccount().getEmail(), user.getKakaoAccount().getProfile().getNickname());
//                } else {
//
//                }
//                return null;
//            }
//        });
//    }
//
//    private void register (String userEmail, String userID) {
//        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
//                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            // Firebase 아이디 정보 변수에 담기.
//                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//                            String id = firebaseUser.getUid();
//
//                            // reference 초기화. child로 "Users"만들고, 그 안에 또 "userid"만든다..?
//                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
//
//                            // HashMap 에 유저정보 담는다.
//                            HashMap<String, Object> hashMap = new HashMap<>();
//                            hashMap.put("id", id);
//                            hashMap.put("userID", userID.toLowerCase());
//                            hashMap.put("userEmail", userEmail);
//                            hashMap.put("bio", "");
//                            hashMap.put("imageurl", "https://w7.pngwing.com/pngs/858/581/png-transparent-profile-icon-user-computer-icons-system-chinese-wind-title-column-miscellaneous-service-logo.png");
//
//                            // DB에 자료 담기
//                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//
//                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//                                        UserInfo userInfo = new UserInfo(userEmail, userID);
//
//                                        db.collection("user").document(firebaseUser.getUid()).set(userInfo)
//                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void unused) {
//                                                        Toast.makeText(getApplicationContext(), "회원정보 등록 성공", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        Toast.makeText(getApplicationContext(), "회원정보 등록 실패", Toast.LENGTH_SHORT).show();
//                                                        Log.d("tag", "Error writing document", e);
//                                                    }
//                                                });
//
//                                        // 처리중 화면 종료
//                                        pd.dismiss();
//                                        Intent intent = new Intent(LoginActivity.this, RegisterStep2Activity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                    }
//                                }
//                            });
//
//                        } else {
//
//                            // 유저입력 정보가 유효하지 않을경우
//                            pd.dismiss();
//                            Toast.makeText(LoginActivity.this, "You can't register with Kakao", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
}