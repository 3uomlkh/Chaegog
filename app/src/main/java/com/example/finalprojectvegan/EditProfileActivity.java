package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {

    final int CAMERA = 100;
    final int GALLERY = 101;

    private Button Btn_EditAccount;
    private ImageView Iv_Edit_Mypage_Profile;
    private EditText Et_Edit_Mypate_UserId;
    private TextView Tv_Edit_VeganReason, Tv_Edit_VeganType, Tv_Edit_Allergy;
    private int count = 0;

    private ArrayList<String> Array_Edit_userAllergy;

    private CheckBox Cb_Environment, Cb_Animal, Cb_Health, Cb_Religion, Cb_Etc;
    private RadioGroup Rg_veganType1, Rg_veganType2;
    private CheckBox Cb_memil, Cb_mil, Cb_daedu, Cb_hodu, Cb_peanut, Cb_peach, Cb_tomato, Cb_poultry, Cb_milk,
            Cb_shrimp, Cb_mackerel, Cb_mussel, Cb_abalone, Cb_oyster, Cb_shellfish, Cb_crab, Cb_squid, Cb_sulfurous;
    private String userAllergy;

    private Dialog dialog;
    private Intent intent;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference, storageReferenceBasic;

    String imagePath = "";
    File imageFile = null;
    Uri imageUri = null;
    int imageFrom;
    SimpleDateFormat imageDate = new SimpleDateFormat("yyyyMMdd_HHmmss");

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Btn_EditAccount = findViewById(R.id.Btn_EditAccount);
        Iv_Edit_Mypage_Profile = findViewById(R.id.Iv_Edit_Mypage_Profile);
        Et_Edit_Mypate_UserId = findViewById(R.id.Et_Edit_Mypage_UserId);

        Tv_Edit_VeganReason = findViewById(R.id.Tv_Edit_VeganReason);
        Tv_Edit_VeganType = findViewById(R.id.Tv_Edit_VeganType);
        Tv_Edit_Allergy = findViewById(R.id.Tv_Edit_Allergy);

        Array_Edit_userAllergy = new ArrayList<>();

        Rg_veganType1 = findViewById(R.id.Rg_veganType1);
        Rg_veganType2 = findViewById(R.id.Rg_veganType2);

        Cb_Environment = findViewById(R.id.Cb_Environment);
        Cb_Animal = findViewById(R.id.Cb_Animal);
        Cb_Health = findViewById(R.id.Cb_Health);
        Cb_Religion = findViewById(R.id.Cb_Religion);
        Cb_Etc = findViewById(R.id.Cb_Etc);

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

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();

        loadProfile();

        Btn_EditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagePath.length() > 0 && imageFrom >= 100) {
                    upload();
                }
                updateUserId(Et_Edit_Mypate_UserId.getText().toString());
                updateVeganReason(Tv_Edit_VeganReason.getText().toString());
                updateVeganType(Tv_Edit_VeganType.getText().toString());
                if (count == 0) {
                    Tv_Edit_Allergy.append(" 없음");
                    userAllergy = Tv_Edit_Allergy.getText().toString();
                    Array_Edit_userAllergy.clear();
                    Array_Edit_userAllergy.add("없음");
                } else {
                    userAllergy = Tv_Edit_Allergy.getText().toString();
                }
                updateAllergy(Array_Edit_userAllergy);

                Toast.makeText(EditProfileActivity.this, "수정 성공", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(EditProfileActivity.this, MypageActivity.class));

            }
        });

        Iv_Edit_Mypage_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(EditProfileActivity.this);
                dialog.setContentView(R.layout.dialog);
                dialog.show();

                Button Btn_Camera = dialog.findViewById(R.id.Btn_Camera);
                Btn_Camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                        if (!hasCamPerm) {
                            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            try {
                                imageFile = createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (imageFile != null) {
                                imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.finalprojectvegan.fileprovider", imageFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, CAMERA);
                            }
                        }

                    }
                });

                Button Btn_Gallery = dialog.findViewById(R.id.Btn_Gallery);
                Btn_Gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                        if (!hasWritePerm) {  // 권한 없을 시  권한설정 요청
                            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }

                        intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY);
                    }
                });

                Button Btn_Basic = dialog.findViewById(R.id.Btn_Basic);
                Btn_Basic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        storageReferenceBasic = firebaseStorage.getReference().child("users").child(firebaseUser.getUid()).child("basic");
                        storageReferenceBasic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("BASIC PROFILE LOAD", "success");
                                updateProfile(uri);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("BASIC PROFILE LOAD", "failure");
                            }
                        });
                    }
                });
            }
        });

        // 클릭 이벤트 미리 주기 -> 이후에 Cb에 따라서 CbClickListener에서 효과 주기
        Cb_Environment.setOnClickListener(CbClickListener);
        Cb_Animal.setOnClickListener(CbClickListener);
        Cb_Health.setOnClickListener(CbClickListener);
        Cb_Religion.setOnClickListener(CbClickListener);
        Cb_Etc.setOnClickListener(CbClickListener);

        // 클릭 이벤트 구현 -> 효과는 나중에 주기
        Cb_memil.setOnClickListener(CbClickListenerAllergy);
        Cb_mil.setOnClickListener(CbClickListenerAllergy);
        Cb_daedu.setOnClickListener(CbClickListenerAllergy);
        Cb_hodu.setOnClickListener(CbClickListenerAllergy);
        Cb_peanut.setOnClickListener(CbClickListenerAllergy);
        Cb_peach.setOnClickListener(CbClickListenerAllergy);
        Cb_tomato.setOnClickListener(CbClickListenerAllergy);
        Cb_poultry.setOnClickListener(CbClickListenerAllergy);
        Cb_milk.setOnClickListener(CbClickListenerAllergy);
        Cb_shrimp.setOnClickListener(CbClickListenerAllergy);
        Cb_mackerel.setOnClickListener(CbClickListenerAllergy);
        Cb_mussel.setOnClickListener(CbClickListenerAllergy);
        Cb_abalone.setOnClickListener(CbClickListenerAllergy);
        Cb_oyster.setOnClickListener(CbClickListenerAllergy);
        Cb_shellfish.setOnClickListener(CbClickListenerAllergy);
        Cb_crab.setOnClickListener(CbClickListenerAllergy);
        Cb_squid.setOnClickListener(CbClickListenerAllergy);
        Cb_sulfurous.setOnClickListener(CbClickListenerAllergy);

        // RadioButton 리스너 작성
        Rg_veganType1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.Radio_Vegan:
                        Tv_Edit_VeganType.setText("비건");
                        break;
                    case R.id.Radio_Lacto:
                        Tv_Edit_VeganType.setText("락토");
                        break;
                    case R.id.Radio_Ovo:
                        Tv_Edit_VeganType.setText("오보");
                        break;
                    case R.id.Radio_LactoOvo:
                        Tv_Edit_VeganType.setText("락토오보");
                        break;
                    case R.id.Radio_Pollo:
                        Tv_Edit_VeganType.setText("폴로");
                        break;
                };
            }
        });
        Rg_veganType2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.Radio_Pesco:
                        Tv_Edit_VeganType.setText("페스코");
                        break;
                    case R.id.Radio_etc:
                        Tv_Edit_VeganType.setText("지향없음");
                        break;
                };
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                imageUri = data.getData();
                imagePath = data.getDataString();
            }
            if (imagePath.length() > 0) {
                Glide.with(EditProfileActivity.this)
                        .load(imagePath)
                        .into(Iv_Edit_Mypage_Profile);
                imageFrom = requestCode;
            }
        }
    }

    File createImageFile() throws IOException {
        String timeStamp = imageDate.format(new Date());
        String fileName = "PROFILE_IMAGE_" + timeStamp;
        File storeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(fileName, ".jpg", storeDir);
        imagePath = file.getAbsolutePath();
        return file;
    }

    // storage에 선택한 사진 업로드하는 함수
    void upload() {
        UploadTask uploadTask = null; // 파일 업로드하는 객체
        switch (imageFrom) {
            case GALLERY:
                String timeStamp = imageDate.format(new Date());
                String imageFileName = "PROFILE_IMAGE_" + timeStamp + "_.png";
                storageReference = firebaseStorage.getReference().child("users").child(firebaseUser.getUid()).child(imageFileName);
                uploadTask = storageReference.putFile(imageUri);
                break;
            case CAMERA:
                storageReference = firebaseStorage.getReference().child("users").child(firebaseUser.getUid()).child(imageFile.getName());
                uploadTask = storageReference.putFile(Uri.fromFile(imageFile));
                break;
        }

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("UPLOAD PROFILE", "success");
                downloadUri();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("UPLOAD PROFILE", "failure");
            }
        });
    }

    // storage에 업로드한 이미지 URI 다운받기
    void downloadUri() {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("DOWNLOAD PROFILE URI", "success");
                Log.d("DOWNLOAD PROFILE URI", uri.toString());
                updateProfile(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DOWNLOAD PROFILE URI", "failure");
            }
        });
    }

    // 위 함수에서 다운받은 URI를 사용자DB(users collection)에 업데이트 시킨다.
    // -> 초기 업데이트 이전에는 기본 이미지로 설정되어있다.
    void updateProfile(Uri uri) {
        DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
        documentReference.update("userProfileImg", uri.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("UPDATE PROFILE IMG", "success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("UPDATE PROFILE IMG", "failure");
                    }
                });
    }

    void updateUserId(String userId) {
        DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
        documentReference.update("userId", userId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("UPDATE USERID", "success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("UPDATE USERID", "failure");
                    }
                });
    }

    void updateVeganReason(String veganReason) {
        DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
        documentReference.update("userVeganReason", veganReason)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("UPDATE VEGANREASON", "success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("UPDATE VEGANREASON", "failure");
                    }
                });
    }

    void updateVeganType(String veganType) {
        DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
        documentReference.update("userVeganType", veganType)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("UPDATE VEGANTYPE", "success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("UPDATE VEGANTYPE", "failure");
                    }
                });
    }

    void updateAllergy(ArrayList Array_Edit_userAllergy) {
        DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
        documentReference.update("userAllergy", Array_Edit_userAllergy)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("UPDATE ALLERGY", "success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("UPDATE ALLERGY", "success");
                    }
                });
    }

    // users DB에 저장된 프로필 이미지를 가져와 수정 창에서 확인 가능하도록 한다.
    void loadProfile() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (firebaseUser != null) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("MYPAGE EDIT LOAD", "success");

                                    if (documentSnapshot.getId().equals(firebaseUser.getUid())) {
                                        Et_Edit_Mypate_UserId.setText(documentSnapshot.getData().get("userId").toString());
                                        Glide.with(EditProfileActivity.this)
                                                .load(documentSnapshot.getData().get("userProfileImg").toString())
                                                .into(Iv_Edit_Mypage_Profile);
                                    }
                                }
                            }
                        } else {
                            Log.d("MYPAGE EDIT LOAD", "failure");
                        }
                    }
                });
    }

    // 클릭 리스너 구현 -> Cb 경우에 따라 결과값 받기
    View.OnClickListener CbClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            boolean checked = ((CheckBox) view).isChecked();

            switch (view.getId()) {
                case R.id.Cb_Environment:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "환경", Toast.LENGTH_SHORT).show();
                        Tv_Edit_VeganReason.append(" 환경");
                    } else {
                        Tv_Edit_VeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Animal:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "동물권", Toast.LENGTH_SHORT).show();
                        Tv_Edit_VeganReason.append(" 동물권");
                    } else {
                        Tv_Edit_VeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Health:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "건강", Toast.LENGTH_SHORT).show();
                        Tv_Edit_VeganReason.append(" 건강");
                    } else {
                        Tv_Edit_VeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Religion:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "종교", Toast.LENGTH_SHORT).show();
                        Tv_Edit_VeganReason.append(" 종교");
                    } else {
                        Tv_Edit_VeganReason.setText("");
                    }
                    break;
                case R.id.Cb_Etc:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "기타", Toast.LENGTH_SHORT).show();
                        Tv_Edit_VeganReason.append(" 기타");
                    } else {
                        Tv_Edit_VeganReason.setText("");
                    }
                    break;
                default:
            }

        }
    };

    // CheckBox 클릭 리스너 구현
    View.OnClickListener CbClickListenerAllergy = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean checked = ((CheckBox) view).isChecked();

            switch (view.getId()) {
                case R.id.Cb_memil:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "메밀", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("메밀 ");
                        Array_Edit_userAllergy.add("메밀");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("메밀");
                    }
                    break;
                case R.id.Cb_mil:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "밀", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("밀 ");
                        Array_Edit_userAllergy.add("밀");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("밀");
                    }
                    break;
                case R.id.Cb_daedu:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "대두", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("대두 ");
                        Array_Edit_userAllergy.add("대두");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("대두");
                    }
                    break;
                case R.id.Cb_hodu:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "호두", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("호두 ");
                        Array_Edit_userAllergy.add("호두");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("호두");
                    }
                    break;
                case R.id.Cb_peanut:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "땅콩", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("땅콩 ");
                        Array_Edit_userAllergy.add("땅콩");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("땅콩");
                    }
                    break;
                case R.id.Cb_peach:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "복숭아", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("복숭아 ");
                        Array_Edit_userAllergy.add("복숭아");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("복숭아");
                    }
                    break;
                case R.id.Cb_tomato:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "토마토", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("토마토 ");
                        Array_Edit_userAllergy.add("토마토");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("토마토");
                    }
                    break;
                case R.id.Cb_poultry:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "가금류", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("가금류 ");
                        Array_Edit_userAllergy.add("가금류");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("가금류");
                    }
                    break;
                case R.id.Cb_milk:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "우유", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("우유 ");
                        Array_Edit_userAllergy.add("우유");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("우유");
                    }
                    break;
                case R.id.Cb_shrimp:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "새우", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("새우 ");
                        Array_Edit_userAllergy.add("새우");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("새우");
                    }
                    break;
                case R.id.Cb_mackerel:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "고등어", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("고등어 ");
                        Array_Edit_userAllergy.add("고등어");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("고등어");
                    }
                    break;
                case R.id.Cb_mussel:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "홍합", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("홍합 ");
                        Array_Edit_userAllergy.add("홍합");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("홍합");
                    }
                    break;
                case R.id.Cb_abalone:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "전복", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("전복 ");
                        Array_Edit_userAllergy.add("전복");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("전복");
                    }
                    break;
                case R.id.Cb_oyster:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "굴", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("굴 ");
                        Array_Edit_userAllergy.add("굴");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("굴");
                    }
                    break;
                case R.id.Cb_shellfish:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "조개류", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("조개류 ");
                        Array_Edit_userAllergy.add("조개류");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("조개류");
                    }
                    break;
                case R.id.Cb_crab:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "게", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("게 ");
                        Array_Edit_userAllergy.add("게");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("게");
                    }
                    break;
                case R.id.Cb_squid:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "오징어", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("오징어 ");
                        Array_Edit_userAllergy.add("오징어");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("오징어");
                    }
                    break;
                case R.id.Cb_sulfurous:
                    if (checked) {
                        //Toast.makeText(getApplicationContext(), "아황산", Toast.LENGTH_SHORT).show();
                        Tv_Edit_Allergy.append("아황산 ");
                        Array_Edit_userAllergy.add("아황산");
                        count++;
                    } else {
                        Array_Edit_userAllergy.remove("아황산");
                    }
                    break;
            }
        }
    };

}