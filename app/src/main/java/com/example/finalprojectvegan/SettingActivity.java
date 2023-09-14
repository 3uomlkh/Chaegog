package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SettingActivity extends AppCompatActivity {
    private int myInt;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SwitchCompat alert_switch;
    private TextView Btn_Mypage_Logout, Btn_Mypage_DeleteAccount;
    private String DELETE_POST;
    private Dialog dialog;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // 알림메세지 수신 동의
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        myInt = pref.getInt("MyPrefInt", 1);

        alert_switch = findViewById(R.id.alert_switch);
        if (myInt == 1) {
            alert_switch.setChecked(true);
        } else {
            alert_switch.setChecked(false);
        }
        alert_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    editor.putInt("MyPrefInt", 1);
                    editor.apply();
                } else {
                    editor.putInt("MyPrefInt", 0);
                    editor.apply();
                }
            }
        });

        // 로그아웃 버튼 클릭 시
        Btn_Mypage_Logout = findViewById(R.id.Btn_Mypage_Logout);
        Btn_Mypage_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(SettingActivity.this);
                dialog.setContentView(R.layout.dialog_logout);
                dialog.show();

                Button Btn_Logout = dialog.findViewById(R.id.Btn_Logout);
                Btn_Logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        firebaseAuth.signOut();
                        Intent intentLogout = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intentLogout);
                    }
                });

                Button Btn_Cancle = dialog.findViewById(R.id.Btn_Cancle);
                Btn_Cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        // 계정 삭제 버튼 클릭 시
        Btn_Mypage_DeleteAccount = findViewById(R.id.Btn_Mypage_DeleteAccount);
        Btn_Mypage_DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(SettingActivity.this);
                dialog.setContentView(R.layout.dialog_accoutdelete);
                dialog.show();

                Button Btn_DeleteAccount = dialog.findViewById(R.id.Btn_DeleteAccount);
                Btn_DeleteAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        Delete();

//                        firebaseUser.delete()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Log.d("AccountDelete", "User account deleted.");
//                                            firebaseAuth.signOut();
////                                            Intent intentDelete = new Intent(SettingActivity.this, LoginActivity.class);
////                                            startActivity(intentDelete);
//                                        } else {
//                                            Log.d("AccountDelete", "Failure");
//                                        }
//                                    }
//                                });

//                        db.collection("posts")
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            if (firebaseUser != null) {
//                                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//
//                                                    String publisher = documentSnapshot.getData().get("publisher").toString();
//
//                                                    if (publisher.equals(firebaseUser.getUid())) {
//                                                        DELETE_POST = documentSnapshot.getData().get("postId").toString();
//
//                                                        db.collection("posts").document(DELETE_POST)
//                                                                .delete()
//                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                    @Override
//                                                                    public void onSuccess(Void unused) {
//
//                                                                    }
//                                                                })
//                                                                .addOnFailureListener(new OnFailureListener() {
//                                                                    @Override
//                                                                    public void onFailure(@NonNull Exception e) {
//
//                                                                    }
//                                                                });
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                });
//
//
//                        db.collection("users").document(firebaseUser.getUid())
//                                .delete()
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//
//                                    }
//                                });
                    }
                });

                Button Btn_Cancle = dialog.findViewById(R.id.Btn_Cancle);
                Btn_Cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }
    public void Delete() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.signOut();
                            Log.d("계정삭제", "성공");
                            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d("계정삭제", "실패");
                        }
                    }
                });
    }
}