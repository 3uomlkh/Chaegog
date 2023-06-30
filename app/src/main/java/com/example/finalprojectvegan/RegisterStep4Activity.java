package com.example.finalprojectvegan;

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

import com.example.finalprojectvegan.Model.UserVeganAllergyInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterStep4Activity extends AppCompatActivity {

    // 변수 선언
    private CheckBox Cb_memil, Cb_mil, Cb_daedu, Cb_hodu, Cb_peanut, Cb_peach, Cb_tomato, Cb_poultry, Cb_milk,
            Cb_shrimp, Cb_mackerel, Cb_mussel, Cb_abalone, Cb_oyster, Cb_shellfish, Cb_crab, Cb_squid, Cb_sulfurous;
    private Button Btn_RegisterFinish;
    private TextView Tv_SelectedAllergy;
    private int count = 0;
    private String userId, userEmail, userPw, userVeganReason, userVeganType, userAllergy;

    private ArrayList<String> Array_userAllergy;

    private FirebaseAuth firebaseAuth;

    // 뒤로가기 버튼 클릭시 앱 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step4);

        // 인증 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();

        // 변수 초기화
        Cb_memil = findViewById(R.id.Cb_memil);
        Cb_mil = findViewById(R.id.Cb_mil);
        Cb_daedu = findViewById(R.id.Cb_daedu);
        Cb_hodu = findViewById(R.id.Cb_hodu);
        Cb_peanut = findViewById(R.id.Cb_peanut);
        Cb_peach = findViewById(R.id.Cb_peach);
        Cb_tomato = findViewById(R.id.Cb_tomato);
        Cb_poultry = findViewById(R.id.Cb_poultry);
        Cb_milk = findViewById(R.id.Cb_milk);
        Cb_shrimp = findViewById(R.id.Cb_shrimp);
        Cb_mackerel = findViewById(R.id.Cb_mackerel);
        Cb_mussel = findViewById(R.id.Cb_mussel);
        Cb_abalone = findViewById(R.id.Cb_abalone);
        Cb_oyster = findViewById(R.id.Cb_oyster);
        Cb_shellfish = findViewById(R.id.Cb_shellfish);
        Cb_crab = findViewById(R.id.Cb_crab);
        Cb_squid = findViewById(R.id.Cb_squid);
        Cb_sulfurous = findViewById(R.id.Cb_sulfurous);

        // 클릭 이벤트 구현 -> 효과는 나중에 주기
        Cb_memil.setOnClickListener(CbClickListener);
        Cb_mil.setOnClickListener(CbClickListener);
        Cb_daedu.setOnClickListener(CbClickListener);
        Cb_hodu.setOnClickListener(CbClickListener);
        Cb_peanut.setOnClickListener(CbClickListener);
        Cb_peach.setOnClickListener(CbClickListener);
        Cb_tomato.setOnClickListener(CbClickListener);
        Cb_poultry.setOnClickListener(CbClickListener);
        Cb_milk.setOnClickListener(CbClickListener);
        Cb_shrimp.setOnClickListener(CbClickListener);
        Cb_mackerel.setOnClickListener(CbClickListener);
        Cb_mussel.setOnClickListener(CbClickListener);
        Cb_abalone.setOnClickListener(CbClickListener);
        Cb_oyster.setOnClickListener(CbClickListener);
        Cb_shellfish.setOnClickListener(CbClickListener);
        Cb_crab.setOnClickListener(CbClickListener);
        Cb_squid.setOnClickListener(CbClickListener);
        Cb_sulfurous.setOnClickListener(CbClickListener);

        Tv_SelectedAllergy = findViewById(R.id.Tv_SelectedAllergy);
        Array_userAllergy = new ArrayList<>();

        // 앞선 Activity에서 보낸 정보 받아서 변수 초기화
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userEmail = intent.getStringExtra("userEmail");
        userPw = intent.getStringExtra("userPw");
        userVeganReason = intent.getStringExtra("userVeganReason");
        userVeganType = intent.getStringExtra("userVeganType");

        // 회원가입 버튼 클릭시
        Btn_RegisterFinish = findViewById(R.id.Btn_RegisterFinish);
        Btn_RegisterFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 선택 안한경우 text 바꿔서 저장
                if (count == 0) {
                    Tv_SelectedAllergy.append(" 없음");
                    userAllergy = Tv_SelectedAllergy.getText().toString();
                    Array_userAllergy.clear();
                    Array_userAllergy.add("없음");
                } else {
                    userAllergy = Tv_SelectedAllergy.getText().toString();
                }
                Log.d("LAST REGISTER", "SUCCESS");
                // 함수 선언
                updateUserProfile(userId, userEmail, userPw, userVeganReason, userVeganType, Array_userAllergy);
                Intent intent3 = new Intent(RegisterStep4Activity.this, LoginActivity.class);
                startActivity(intent3);
            }
        });
    }

    // db에 회원정보 저장하는 함수
    // Firebase Firestore에 회원가입 단계의 모든 정보를 한번에 저장한다.
    private void updateUserProfile(String userId, String userEmail, String userPw, String userVeganReason, String userVeganType, ArrayList Array_userAllergy) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        UserProfile userProfile = new UserProfile(userId, userEmail, userPw, userVeganReason, userVeganType, Array_userAllergy);

        if (firebaseUser != null){
            db.collection("users").document(firebaseUser.getUid()).set(userProfile)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "회원정보 등록 성공", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "회원정보 등록 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // CheckBox 클릭 리스너 구현
    View.OnClickListener CbClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean checked = ((CheckBox) view).isChecked();

            switch (view.getId()) {
                case R.id.Cb_memil:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "메밀", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("메밀 ");
                        Array_userAllergy.add("메밀");
                        count++;
                    } else {
                        Array_userAllergy.remove("메밀");
                    }
                    break;
                case R.id.Cb_mil:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "밀", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("밀 ");
                        Array_userAllergy.add("밀");
                        count++;
                    } else {
                        Array_userAllergy.remove("밀");
                    }
                    break;
                case R.id.Cb_daedu:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "대두", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("대두 ");
                        Array_userAllergy.add("대두");
                        similarAllergy += "두부 두유 콩나물 된장 고추장 간장 콩기름 ";
                        count++;
                    } else {
                        Array_userAllergy.remove("대두");
                    }
                    break;
                case R.id.Cb_hodu:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "호두", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("호두 ");
                        Array_userAllergy.add("호두");
                        count++;
                    } else {
                        Array_userAllergy.remove("호두");
                    }
                    break;
                case R.id.Cb_peanut:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "땅콩", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("땅콩 ");
                        Array_userAllergy.add("땅콩");
                        similarAllergy += "아몬드 캐슈넛 피스타치오 피칸 호두 잣";
                        count++;
                    } else {
                        Array_userAllergy.remove("땅콩");
                    }
                    break;
                case R.id.Cb_peach:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "복숭아", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("복숭아 ");
                        Array_userAllergy.add("복숭아");
                        similarAllergy += "사과 자두 체리 배";
                        count++;
                    } else {
                        Array_userAllergy.remove("복숭아");
                    }
                    break;
                case R.id.Cb_tomato:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "토마토", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("토마토 ");
                        Array_userAllergy.add("토마토");
                        count++;
                    } else {
                        Array_userAllergy.remove("토마토");
                    }
                    break;
                case R.id.Cb_poultry:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "가금류", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("가금류 ");
                        Array_userAllergy.add("가금류");
                        similarAllergy += "계란 난백 난황 알부민 ";
                        count++;
                    } else {
                        Array_userAllergy.remove("가금류");
                    }
                    break;
                case R.id.Cb_milk:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "우유", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("우유 ");
                        Array_userAllergy.add("우유");
                        similarAllergy += "카제인 유청단백 분말유 분유 유크림 유당 유청 산양유 버터 크림 락토알부민 락토페린";
                        count++;
                    } else {
                        Array_userAllergy.remove("우유");
                    }
                    break;
                case R.id.Cb_shrimp:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "새우", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("새우 ");
                        Array_userAllergy.add("새우");
                        similarAllergy += "꽃게 바닷가재 ";
                        count++;
                    } else {
                        Array_userAllergy.remove("새우");
                    }
                    break;
                case R.id.Cb_mackerel:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "고등어", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("고등어 ");
                        Array_userAllergy.add("고등어");
                        count++;
                    } else {
                        Array_userAllergy.remove("고등어");
                    }
                    break;
                case R.id.Cb_mussel:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "홍합", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("홍합 ");
                        Array_userAllergy.add("홍합");
                        similarAllergy += "조개류 ";
                        count++;
                    } else {
                        Array_userAllergy.remove("홍합");
                    }
                    break;
                case R.id.Cb_abalone:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "전복", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("전복 ");
                        Array_userAllergy.add("전복");
                        similarAllergy += "조개류 ";
                        count++;
                    } else {
                        Array_userAllergy.remove("전복");
                    }
                    break;
                case R.id.Cb_oyster:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "굴", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("굴 ");
                        Array_userAllergy.add("굴");
                        similarAllergy += "조개류 ";
                        count++;
                    } else {
                        Array_userAllergy.remove("굴");
                    }
                    break;
                case R.id.Cb_shellfish:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "조개류", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("조개류 ");
                        Array_userAllergy.add("조개류");
                        count++;
                    } else {
                        Array_userAllergy.remove("조개류");
                    }
                    break;
                case R.id.Cb_crab:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "게", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("게 ");
                        Array_userAllergy.add("게");
                        count++;
                    } else {
                        Array_userAllergy.remove("게");
                    }
                    break;
                case R.id.Cb_squid:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "오징어", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("오징어 ");
                        Array_userAllergy.add("오징어");
                        count++;
                    } else {
                        Array_userAllergy.remove("오징어");
                    }
                    break;
                case R.id.Cb_sulfurous:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "아황산", Toast.LENGTH_SHORT).show();
                        Tv_SelectedAllergy.append("아황산 ");
                        Array_userAllergy.add("아황산");
                        count++;
                    } else {
                        Array_userAllergy.remove("아황산");
                    }
                    break;
            }
        }
    };

}