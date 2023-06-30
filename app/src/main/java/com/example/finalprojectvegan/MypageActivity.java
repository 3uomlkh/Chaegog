package com.example.finalprojectvegan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalprojectvegan.Model.UserInfo;
import com.example.finalprojectvegan.Model.UserVeganAllergyInfo;
import com.example.finalprojectvegan.Model.UserVeganTypeInfo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.lakue.lakuepopupactivity.PopupActivity;
import com.lakue.lakuepopupactivity.PopupGravity;
import com.lakue.lakuepopupactivity.PopupResult;
import com.lakue.lakuepopupactivity.PopupType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MypageActivity extends AppCompatActivity {

    private static final int CAMERA = 100;
    private static final int GALLERY = 101;

    private TextView Btn_Logout, Btn_DeleteAccount, Btn_Info, Btn_BlockInfo, Btn_setting, Btn_help;
    private TextView userID, userVeganType, userAllergy;
    private ImageView Iv_Mypage_Profile;
    private Button Btn_EditAccount;

    private String imagePath = "";
    private File imageFile = null;
    private Uri imageUri = null;
    private int imageFrom;
    SimpleDateFormat imageDate = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    ProgressDialog dialog;

    String USER_ID;
    String USER_VEGAN_TYPE;
    String USER_ALLERGY;

    InputImage image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String user = firebaseUser.toString();

        getFirebaseProfileImage(firebaseUser);

        Iv_Mypage_Profile = findViewById(R.id.Iv_Mypage_Profile);
        userID = findViewById(R.id.Tv_Mypage_UserId);
        userVeganType = findViewById(R.id.Tv_Mypage_VeganType);
        userAllergy = findViewById(R.id.Tv_Mypage_Allergy);

        Btn_EditAccount = findViewById(R.id.Btn_EditAccount);
        Btn_Logout = findViewById(R.id.Btn_Mypage_Logout);
        Btn_help = findViewById(R.id.Btn_Mypage_help);
        Btn_Info = findViewById(R.id.Btn_Mypage_Info);
        Btn_setting = findViewById(R.id.Btn_Mypage_Setting);
        Btn_BlockInfo = findViewById(R.id.Btn_Mypage_BlockInfo);
        Btn_DeleteAccount = findViewById(R.id.Btn_Mypage_DeleteAccount);

        Btn_EditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MypageActivity.this, RoadingActivity.class);
                startActivity(intent);
            }
        });

        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<UserInfo> postUserList = new ArrayList<>();

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {

                                String uid = firebaseUser.getUid();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                    postUserList.add(new UserInfo(
                                            documentSnapshot.getData().get("userID").toString(),
                                            documentSnapshot.getData().get("userEmail").toString(),
                                            documentSnapshot.getData().get("userPassword").toString()));

                                    if (documentSnapshot.getId().equals(uid)) {
                                        USER_ID = documentSnapshot.getData().get("userID").toString();
                                        userID.setText(USER_ID);
                                    }
                                }
                            }
                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

        db.collection("userVeganType")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<UserVeganTypeInfo> postUserList = new ArrayList<>();

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {

                                String uid = firebaseUser.getUid();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                    postUserList.add(new UserVeganTypeInfo(
                                            documentSnapshot.getData().get("veganType").toString()));

                                    if (documentSnapshot.getId().equals(uid)) {
                                        USER_VEGAN_TYPE = documentSnapshot.getData().get("veganType").toString();
                                        userVeganType.setText(USER_VEGAN_TYPE);
                                    }
                                }
                            }
                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

        db.collection("userVeganAllergy")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<UserVeganAllergyInfo> postUserList = new ArrayList<>();

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {

                                String uid = firebaseUser.getUid();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                    postUserList.add(new UserVeganAllergyInfo(
                                            documentSnapshot.getData().get("userAllergy").toString()));

                                    if (documentSnapshot.getId().equals(uid)) {
                                        USER_ALLERGY = documentSnapshot.getData().get("userAllergy").toString();
                                        userAllergy.setText(USER_ALLERGY);
                                    }
                                }
                            }
                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

        Btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(MypageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Iv_Mypage_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), PopupActivity.class);
                intent.putExtra("type", PopupType.SELECT);
                intent.putExtra("gravity", PopupGravity.CENTER);
                intent.putExtra("title", "사진을 불러올 기능을 선택하세요");
                intent.putExtra("buttonLeft", "카메라");
                intent.putExtra("buttonRight", "갤러리");
                startActivityForResult(intent, 2);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Image Permission", "권한 설정 완료");
                    } else {
                        Log.d("Image Permission", "권한 설정 요청");
                        ActivityCompat.requestPermissions(MypageActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //데이터 받기
            if (requestCode == 2) {
                PopupResult result = (PopupResult) data.getSerializableExtra("result");
                if (result == PopupResult.LEFT) {
                    // 카메라 선택
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

                } else if (result == PopupResult.RIGHT) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY);
                }
            }
        }
        // 갤러리
        else if(requestCode == GALLERY && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imagePath = data.getDataString();
            if(imagePath.length() > 0) {
                Glide.with(this)
                        .load(imagePath)
                        .into(Iv_Mypage_Profile);
                imageFrom = requestCode;
            }
        }
    }

    File createImageFile() throws IOException {
        String timeStamp = imageDate.format(new Date());
        String fileName = "PROFILE IMAGE_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(fileName, ".jpg", storageDir);
        imagePath = file.getAbsolutePath();
        return file;
    }

    // 카메라
    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");
//                        imageView_profile.setImageBitmap(bitmap);

//                        image = InputImage.fromBitmap(bitmap, 0);

                        byte[] bytes = bitmapToByteArray(bitmap);

                        uploadImage(bytes);

                        dialog = new ProgressDialog(MypageActivity.this); //프로그레스 대화상자 객체 생성
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //프로그레스 대화상자 스타일 원형으로 설정
                        dialog.setMessage("제출 중입니다."); //프로그레스 대화상자 메시지 설정
                        dialog.show(); //프로그레스 대화상자 띄우기

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                dialog.dismiss(); // 3초 시간지연 후 프로그레스 대화상자 닫기
                                Toast.makeText(MypageActivity.this, "이미지 저장.", Toast.LENGTH_LONG).show();

                                loadImage();
                            }
                        }, 5000);
                    }
                }
            }
    );

    public byte[] bitmapToByteArray( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }

    public void uploadImage(byte[] bytes) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final StorageReference mountainImageReference = storageReference.child("users/" + firebaseUser.getUid() + "/profileImage.jpg");

        UploadTask uploadTask = mountainImageReference.putBytes(bytes);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d("실패", "실패");
                    throw task.getException();
                }
                return mountainImageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d("성공", "성공" + downloadUri);
                } else {
                    Log.d("실패2", "실패2");
                }
            }
        });
    }

    public void loadImage() {
        Iv_Mypage_Profile = (ImageView) findViewById(R.id.imageView_profile);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference pathReference = storageReference.child("users");
        if (pathReference == null) {
            Toast.makeText(this, "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference submitProfile = storageReference.child("users/" + firebaseUser.getUid() + "/profileImage.jpg");
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(MypageActivity.this).load(uri).centerCrop().override(300).into(Iv_Mypage_Profile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private void setImage(Uri uri) {
        try{
            InputStream in = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
//            imageView_profile.setImageBitmap(bitmap);

//            image = InputImage.fromBitmap(bitmap, 0);
            Log.e("setImage", "이미지 to 비트맵");

            byte[] bytes = bitmapToByteArray(bitmap);

            uploadImage(bytes);

            dialog = new ProgressDialog(MypageActivity.this); //프로그레스 대화상자 객체 생성
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //프로그레스 대화상자 스타일 원형으로 설정
            dialog.setMessage("제출 중입니다."); //프로그레스 대화상자 메시지 설정
            dialog.show(); //프로그레스 대화상자 띄우기

            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    dialog.dismiss(); // 3초 시간지연 후 프로그레스 대화상자 닫기
                    Toast.makeText(MypageActivity.this, "이미지 저장.", Toast.LENGTH_LONG).show();
                    loadImage();
                }
            }, 5000);

        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void getFirebaseProfileImage(FirebaseUser id) {
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profileImage");
        if (!file.isDirectory()) {
            file.mkdir();
        }
        loadImage();
    }

//    private void downloadImg(String id) {
//        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference storageReference = firebaseStorage.getReference();
//        storageReference.child("profileImage/" + "profile" + id + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(MypageActivity.this).load(uri).into(imageView_profile);
//                dialogwithUri = uri;
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//    }

}