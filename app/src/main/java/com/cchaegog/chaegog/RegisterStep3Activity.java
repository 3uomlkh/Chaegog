package com.cchaegog.chaegog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterStep3Activity extends AppCompatActivity {

    private Button Btn_Register3To4;
    private RadioGroup Rg_veganType;
    private TextView Tv_SelectedVeganType;
    private String userId, userEmail, userPw, userVeganReason;

    FirebaseAuth firebaseAuth;

//    private ArrayList<VeganReasons> Array_userVeganReason;
//    ArrayList<String> Arr = new ArrayList<>();

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
                                Intent intent = new Intent(RegisterStep3Activity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Log.d("계정삭제", "실패");
                            }
                        }
                    });
        } else {
            Intent intent = new Intent(RegisterStep3Activity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step3);

//        Array_userVeganReason = new ArrayList<VeganReasons>();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userEmail = intent.getStringExtra("userEmail");
        userPw = intent.getStringExtra("userPw");
//        Arr = intent.getStringArrayExtra("veganReason");
        userVeganReason = intent.getStringExtra("userVeganReason");
//        ArrayList<VeganReasons> Array_userVeganReason = (ArrayList<VeganReasons>) intent.getSerializableExtra("userVeganReason");
        Log.d("비건이유 제발", userVeganReason + "입니다");

        Rg_veganType = findViewById(R.id.Rg_veganType);

        Tv_SelectedVeganType = findViewById(R.id.Tv_SelectedVeganType);

        Btn_Register3To4 = findViewById(R.id.Btn_Register3To4);
        Btn_Register3To4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userVeganType = Tv_SelectedVeganType.getText().toString();

                // 선택 안한경우
                if (userVeganType.equals("")) {
                    Toast.makeText(RegisterStep3Activity.this, "선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // 선택 한 경우 petExtra를 이용해 정보 보내기 -> 회원가입 마지막 단계에서 합치기 위함
                    Log.d("REGISTER3", "SUCCESS");
                    Intent intent3 = new Intent(RegisterStep3Activity.this, RegisterStep4Activity.class);
                    intent3.putExtra("userId", userId);
                    intent3.putExtra("userEmail", userEmail);
                    intent3.putExtra("userPw", userPw);
                    intent3.putExtra("userVeganReason", userVeganReason);
                    intent3.putExtra("userVeganType", userVeganType);
                    startActivity(intent3);
                }
            }
        });

        // RadioButton 리스너 작성
        Rg_veganType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.Radio_Vegan:
                        Tv_SelectedVeganType.setText("비건");
                        break;
                    case R.id.Radio_Lacto:
                        Tv_SelectedVeganType.setText("락토");
                        break;
                    case R.id.Radio_Ovo:
                        Tv_SelectedVeganType.setText("오보");
                        break;
                    case R.id.Radio_LactoOvo:
                        Tv_SelectedVeganType.setText("락토오보");
                        break;
                    case R.id.Radio_Pesco:
                        Tv_SelectedVeganType.setText("페스코");
                        break;
                    case R.id.Radio_Pollo:
                        Tv_SelectedVeganType.setText("폴로");
                        break;
                    case R.id.Radio_etc:
                        Tv_SelectedVeganType.setText("지향없음");
                        break;
                }
            }
        });
    }

}