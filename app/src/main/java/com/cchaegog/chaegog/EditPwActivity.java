package com.cchaegog.chaegog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EditPwActivity extends AppCompatActivity {

    Button Btn_Change_Pw;
    TextView Tv_Current_Pw;
    EditText Et_Change_Pw;
    String ChangePw;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pw);

        Btn_Change_Pw = findViewById(R.id.Btn_Edit_Pw);
        Tv_Current_Pw = findViewById(R.id.Tv_CurrentPw);
        Et_Change_Pw = findViewById(R.id.Et_ChangePw);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("users")
                        .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                if (firebaseUser.getUid().equals(documentSnapshot.getId())) {
                                                    Tv_Current_Pw.setText(documentSnapshot.getData().get("userPw").toString());
                                                }

                                            }
                                        }
                                    }
                                });

        Btn_Change_Pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePw = Et_Change_Pw.getText().toString();
                // Et 양식 조건
                if (ChangePw.length() >= 6) {
                    updateUserPw(ChangePw);
                }
            }
        });
    }

    public void updateUserPw(String ChangePw) {
        firebaseUser.updatePassword(ChangePw)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
                        documentReference.update("userPw", ChangePw)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "비밀번호 변경 완료", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                    }
                });
    }
}