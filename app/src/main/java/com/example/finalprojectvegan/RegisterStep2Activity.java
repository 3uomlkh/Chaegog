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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.finalprojectvegan.Model.UserVeganCategoryInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterStep2Activity extends AppCompatActivity {

    private Button Btn_Register2To3;
    private TextView Tv_SelectedVeganReason;
    private CheckBox Cb_Environment, Cb_Animal, Cb_Health, Cb_Religion, Cb_Etc;
    private String userId, userEmail, userPw;

    private FirebaseAuth firebaseAuth;

    // 뒤로가기 버튼 클릭시 앱 종료
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(RegisterStep2Activity.this, LoginActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

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

                String userVeganReason = Tv_SelectedVeganReason.getText().toString();

                if (userVeganReason.equals("")) {
                    Toast.makeText(RegisterStep2Activity.this, "선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("REGISTER2", "SUCCESS");
                    Intent intent2 = new Intent(RegisterStep2Activity.this, RegisterStep3Activity.class);
                    intent2.putExtra("userId", userId);
                    intent2.putExtra("userEmail", userEmail);
                    intent2.putExtra("userPw", userPw);
                    intent2.putExtra("userVeganReason", userVeganReason);
                    startActivity(intent2);
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
                        Tv_SelectedVeganReason.append(" 환경");
                    } else {
                        Tv_SelectedVeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Animal:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "동물권", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append(" 동물권");
                    } else {
                        Tv_SelectedVeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Health:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "건강", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append(" 건강");
                    } else {
                        Tv_SelectedVeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Religion:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "종교", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append(" 종교");
                    } else {
                        Tv_SelectedVeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Etc:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "기타", Toast.LENGTH_SHORT).show();
                        Tv_SelectedVeganReason.append(" 기타");
                    } else {
                        Tv_SelectedVeganReason.setText("");
                    }
                    break;
                default:

            }
        }
    };

}