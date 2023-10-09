package com.cchaegog.chaegog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    // 변수 선언
    private static final String TAG = "Log";
    private EditText Et_Login_Email, Et_Login_Password;
    private Button Btn_Login, Btn_Register, Btn_LoginKakao;
    private TextView Btn_PasswordReset;
    private String userToken;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser, currentUser;
    private FirebaseFirestore db;
    private ArrayList<String> userIdList = new ArrayList();

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
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("users").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    userIdList.add(documentSnapshot.getId());
                                }
                                Log.d(TAG, userIdList + "입니다.");
                                if (userIdList.contains(currentUser.getUid())) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Log.d(TAG, currentUser.getUid());
                                    Delete(currentUser);
                                }
                            }
                        }
                    });
        } else{
            Log.d(TAG, "계정이 존재하지 않음");
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
        db = FirebaseFirestore.getInstance();

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
                if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(LoginActivity.this, "모두 작성해 주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    if (firebaseUser.isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, "인증된 계정입니다", Toast.LENGTH_SHORT).show();
                    }
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

    public void Delete(FirebaseUser user) {
        Log.d(TAG, user.getUid());
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.delete();
                            Log.d("계정삭제", "성공");
                        } else {
                            Log.d("계정삭제", "실패");
                        }
                    }
                });
    }
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