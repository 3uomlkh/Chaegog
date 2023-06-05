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

    String userAllergy, similarAllergy;
    Button Btn_RegisterFinish;
    TextView textView_select_allergy;
    CheckBox checkbox_memil, checkbox_mil, checkbox_daedu, checkbox_hodu, checkbox_peanut, checkbox_peach, checkbox_tomato, checkbox_poultry, checkbox_milk, checkbox_shrimp,
            checkbox_mackerel, checkbox_mussel, checkbox_abalone, checkbox_oyster, checkbox_shellfish, checkbox_crab, checkbox_squid, checkbox_sulfurous;
    int count = 0;

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

        textView_select_allergy = findViewById(R.id.textView_select_allergy);

        checkbox_memil = findViewById(R.id.checkbox_memil);
        checkbox_mil = findViewById(R.id.checkbox_mil);
        checkbox_daedu = findViewById(R.id.checkbox_daedu);
        checkbox_hodu = findViewById(R.id.checkbox_hodu);
        checkbox_peanut = findViewById(R.id.checkbox_peanut);
        checkbox_peach = findViewById(R.id.checkbox_peach);
        checkbox_tomato = findViewById(R.id.checkbox_tomato);
        checkbox_poultry = findViewById(R.id.checkbox_poultry);
        checkbox_milk = findViewById(R.id.checkbox_milk);
        checkbox_shrimp = findViewById(R.id.checkbox_shrimp);
        checkbox_mackerel = findViewById(R.id.checkbox_mackerel);
        checkbox_mussel = findViewById(R.id.checkbox_mussel);
        checkbox_abalone = findViewById(R.id.checkbox_abalone);
        checkbox_oyster = findViewById(R.id.checkbox_oyster);
        checkbox_shellfish = findViewById(R.id.checkbox_shellfish);
        checkbox_crab = findViewById(R.id.checkbox_crab);
        checkbox_squid = findViewById(R.id.checkbox_squid);
        checkbox_sulfurous = findViewById(R.id.checkbox_sulfurous);

        checkbox_memil.setOnClickListener(checkboxClickListener);
        checkbox_mil.setOnClickListener(checkboxClickListener);
        checkbox_daedu.setOnClickListener(checkboxClickListener);
        checkbox_hodu.setOnClickListener(checkboxClickListener);
        checkbox_peanut.setOnClickListener(checkboxClickListener);
        checkbox_peach.setOnClickListener(checkboxClickListener);
        checkbox_tomato.setOnClickListener(checkboxClickListener);
        checkbox_poultry.setOnClickListener(checkboxClickListener);
        checkbox_milk.setOnClickListener(checkboxClickListener);
        checkbox_shrimp.setOnClickListener(checkboxClickListener);
        checkbox_mackerel.setOnClickListener(checkboxClickListener);
        checkbox_mussel.setOnClickListener(checkboxClickListener);
        checkbox_abalone.setOnClickListener(checkboxClickListener);
        checkbox_oyster.setOnClickListener(checkboxClickListener);
        checkbox_shellfish.setOnClickListener(checkboxClickListener);
        checkbox_crab.setOnClickListener(checkboxClickListener);
        checkbox_squid.setOnClickListener(checkboxClickListener);
        checkbox_sulfurous.setOnClickListener(checkboxClickListener);

        Btn_RegisterFinish = findViewById(R.id.Btn_RegisterFinish);
        Btn_RegisterFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textView_select_allergy == null) {
                    Toast.makeText(getApplicationContext(), "선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (count == 0) {
                        Toast.makeText(getApplicationContext(), "없음", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append(" 없음");
                    }

                    profileUpload();
                }

//                if (count == 0) {
//                    Toast.makeText(getApplicationContext(), "없음", Toast.LENGTH_SHORT).show();
//                    textView_select_allergy.append(" 없음");
//                }
//
//                profileUpload();
//
//                Intent intent = new Intent(RegisterStep4Activity.this, LoginActivity.class);
//                startActivity(intent);
            }
        });
    }

    View.OnClickListener checkboxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean checked = ((CheckBox) view).isChecked();

            switch (view.getId()) {
                case R.id.checkbox_memil:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "메밀", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("메밀 ");

                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_mil:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "밀", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("밀 ");

                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_daedu:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "대두", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("대두 ");
                        similarAllergy += "두부 두유 콩나물 된장 고추장 간장 콩기름 ";
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_hodu:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "호두", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("호두 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_peanut:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "땅콩", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("땅콩 ");
                        similarAllergy += "아몬드 캐슈넛 피스타치오 피칸 호두 잣";
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_peach:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "복숭아", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("복숭아 ");
                        similarAllergy += "사과 자두 체리 배";
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_tomato:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "토마토", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("토마토 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_poultry:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "가금류", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("가금류 ");
                        similarAllergy += "계란 난백 난황 알부민 ";
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_milk:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "우유", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("우유 ");
                        similarAllergy += "카제인 유청단백 분말유 분유 유크림 유당 유청 산양유 버터 크림 락토알부민 락토페린";
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_shrimp:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "새우", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("새우 ");
                        similarAllergy += "꽃게 바닷가재 ";
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_mackerel:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "고등어", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("고등어 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_mussel:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "홍합", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("홍합 ");
                        similarAllergy += "조개류 ";
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_abalone:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "전복", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("전복 ");
                        similarAllergy += "조개류 ";
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_oyster:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "굴", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("굴 ");
                        similarAllergy += "조개류 ";
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_shellfish:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "조개류", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("조개류 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_crab:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "게", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("게 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_squid:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "오징어", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("오징어 ");
                        count++;
                    } else {

                    }
                    break;
                case R.id.checkbox_sulfurous:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "아황산", Toast.LENGTH_SHORT).show();
                        textView_select_allergy.append("아황산 ");
                        count++;
                    } else {

                    }
                    break;
            }
        }
    };

    private void profileUpload() {
        userAllergy = ((TextView) findViewById(R.id.textView_select_allergy)).getText().toString();

        if (userAllergy.length() > 0) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            UserVeganAllergyInfo UserVeganAllergyInfo = new UserVeganAllergyInfo(userAllergy);
            if (firebaseUser != null) {
                db.collection("userVeganAllergy").document(firebaseUser.getUid()).set(UserVeganAllergyInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "회원정보 등록 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterStep4Activity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "회원정보 등록 실패", Toast.LENGTH_SHORT).show();
                                Log.d("tag", "Error writing document", e);
                            }
                        });
            }
        }
    }
}