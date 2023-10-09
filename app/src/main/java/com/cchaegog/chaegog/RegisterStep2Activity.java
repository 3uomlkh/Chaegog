package com.cchaegog.chaegog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterStep2Activity extends AppCompatActivity {

    private Button Btn_Register2To3;
    private TextView Tv_SelectedVeganReason;
    private CheckBox Cb_Environment, Cb_Animal, Cb_Health, Cb_Religion, Cb_Etc;
    private String userId, userEmail, userPw, userVeganReason;

//    ArrayList<VeganReasons> Array_userVeganReason = new ArrayList<VeganReasons>();
//    ArrayList<String> arr = new ArrayList<>();
    //    private ArrayList<VeganReasons> Array_userVeganReason;
    private int countR = 0;

    private FirebaseAuth firebaseAuth;

    // 뒤로가기 버튼 클릭시 앱 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseAuth.signOut();
                                Log.d("계정삭제", "성공");
                                Intent intent = new Intent(RegisterStep2Activity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Log.d("계정삭제", "실패");
                            }
                        }
                    });
        } else {
            Intent intent = new Intent(RegisterStep2Activity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

//        arr = new ArrayList<>();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userEmail = intent.getStringExtra("userEmail");
        userPw = intent.getStringExtra("userPw");

        // 변수 초기화
        Cb_Environment = findViewById(R.id.Cb_Environment);
        Cb_Animal = findViewById(R.id.Cb_Animal);
        Cb_Health = findViewById(R.id.Cb_Health);
        Cb_Religion = findViewById(R.id.Cb_Religion);
        Cb_Etc = findViewById(R.id.Cb_Etc);
        Tv_SelectedVeganReason = findViewById(R.id.Tv_SelectedVeganReason);

        // FirebaseAuth 객체의 공유 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();

        // 클릭 이벤트 미리 주기 -> 이후에 Cb에 따라서 CbClickListener에서 효과 주기
        Cb_Environment.setOnClickListener(CbClickListener);
        Cb_Animal.setOnClickListener(CbClickListener);
        Cb_Health.setOnClickListener(CbClickListener);
        Cb_Religion.setOnClickListener(CbClickListener);
        Cb_Etc.setOnClickListener(CbClickListener);

        Btn_Register2To3 = findViewById(R.id.Btn_Register2To3);
        Btn_Register2To3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String userVeganReason = Tv_SelectedVeganReason.getText().toString();

                if (countR == 0) {
                    Tv_SelectedVeganReason.append("없음 ");
                    userVeganReason = Tv_SelectedVeganReason.getText().toString();
//                    Array_userVeganReason.clear();
//                    arr.clear();
//                    Array_userVeganReason.add(new VeganReasons("없음"));
//                    arr.add("없음");
//                    Array_userVeganReason.add(0, "없음");
                } else {
//                    userVeganReason = Tv_SelectedVeganReason.getText().toString();
                }
                Log.d("비건이유 문제발생?", Tv_SelectedVeganReason + "혹은" + userVeganReason);

                if (userVeganReason.equals("")) {
                    Toast.makeText(RegisterStep2Activity.this, "선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("REGISTER2", "SUCCESS");
                    Intent intent2 = new Intent(RegisterStep2Activity.this, RegisterStep3Activity.class);
                    intent2.putExtra("userId", userId);
                    intent2.putExtra("userEmail", userEmail);
                    intent2.putExtra("userPw", userPw);
                    intent2.putExtra("userVeganReason", userVeganReason);
//                    intent2.putExtra("veganReason", arr);
                    startActivity(intent2);
//                    Log.d("이유!!!!!!", Array_userVeganReason + "입니다");
//                    Log.d("이유2222!!!!!!", arr + "입니다");
                }

            }
        });
    }

    // 클릭 리스너
    View.OnClickListener CbClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean checked = ((CheckBox) view).isChecked();

            switch (view.getId()) {
                case R.id.Cb_Environment:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "환경", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append("환경 ");
                        userVeganReason = Tv_SelectedVeganReason.getText().toString();
//                        Array_userVeganReason.add(new VeganReasons("환경"));
//                        arr.add("환경");
                        countR++;
                    } else {
//                        Tv_SelectedVeganReason.setText("");
                        userVeganReason = Tv_SelectedVeganReason.getText().toString();
                        userVeganReason = userVeganReason.replace("환경 ", "");
//                        Array_userVeganReason.remove("환경");
//                        arr.remove("환경");
                    }
                    break;
                case R.id.Cb_Animal:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "동물권", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append("동물권 ");
                        userVeganReason = Tv_SelectedVeganReason.getText().toString();
//                        Array_userVeganReason.add(new VeganReasons("동물권"));
//                        arr.add("동물권");
                        countR++;
                    } else {
//                        Tv_SelectedVeganReason.setText("");

                        userVeganReason = Tv_SelectedVeganReason.getText().toString();
                        userVeganReason = userVeganReason.replace("동물권 ", "");
//                        Array_userVeganReason.remove("동물권");
//                        arr.remove("동물권");
                    }
                    break;
                case R.id.Cb_Health:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "건강", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append("건강 ");
                        userVeganReason = Tv_SelectedVeganReason.getText().toString();
//                        Array_userVeganReason.add(new VeganReasons("건강"));
//                        arr.add("건강");
                        countR++;
                    } else {
//                        Tv_SelectedVeganReason.setText("");
//                        Array_userVeganReason.remove("건강");
//                        arr.remove("건강");

                        userVeganReason = Tv_SelectedVeganReason.getText().toString();
                        userVeganReason = userVeganReason.replace("건강 ", "");
                    }
                    break;
                case R.id.Cb_Religion:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "종교", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append("종교 ");
                        userVeganReason = Tv_SelectedVeganReason.getText().toString();
//                        Array_userVeganReason.add(new VeganReasons("종교"));
//                        arr.add("종교");
                        countR++;
                    } else {
//                        Tv_SelectedVeganReason.setText("");
//                        Array_userVeganReason.remove("종교");
//                        arr.remove("종교");

                        userVeganReason = Tv_SelectedVeganReason.getText().toString();
                        userVeganReason = userVeganReason.replace("종교 ", "");
                    }
                    break;
                case R.id.Cb_Etc:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "기타", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append("기타 ");
                        userVeganReason = Tv_SelectedVeganReason.getText().toString();
//                        Array_userVeganReason.add(new VeganReasons("기타"));
//                        arr.add("기타");
                        countR++;
                    } else {
//                        Tv_SelectedVeganReason.setText("");
//                        Array_userVeganReason.remove("기타");
//                        arr.remove("기타");

                        userVeganReason = Tv_SelectedVeganReason.getText().toString();
                        userVeganReason = userVeganReason.replace("기타 ", "");
                    }
                    break;
                default:

            }
        }
    };

}