package com.example.finalprojectvegan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import com.example.finalprojectvegan.Model.UserVeganAllergyInfo;
import com.example.finalprojectvegan.Model.UserVeganTypeInfo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    private TextView Btn_Mypage_Setting, Btn_Mypage_BlockInfo, Btn_Mypage_help, Btn_Mypage_Info;
    private TextView userID, userVeganType, userAllergy;
    private Button Btn_EditAccount;
    private ImageView Iv_Mypage_profile;
    private String Id, VeganType, Allergy;
    String imagePath = "";
    File imageFile = null;
    Uri imageUri = null;
    int imageFrom;
    SimpleDateFormat imageDate = new SimpleDateFormat("yyyyMMdd_HHmmss");

//    ProgressDialog dialog;

    private Dialog dialog;

    private String USER_ID;
    private String USER_VEGAN_TYPE;
    private String USER_ALLERGY;
    private String USER_PROFILE_IMG;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

//        getFirebaseProfileImage(firebaseUser);

        Iv_Mypage_profile = findViewById(R.id.Iv_Mypage_Profile);
        userID = findViewById(R.id.Tv_Mypage_UserId);
        userVeganType = findViewById(R.id.Tv_Mypage_VeganType);
        userAllergy = findViewById(R.id.Tv_Mypage_Allergy);

        // MyPage 내 버튼
        Btn_Mypage_Setting = findViewById(R.id.Btn_Mypage_Setting);
        Btn_EditAccount = findViewById(R.id.Btn_EditAccount);
        Btn_Mypage_help = findViewById(R.id.Btn_Mypage_help);
        Btn_Mypage_Info = findViewById(R.id.Btn_Mypage_Info);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();

        Log.d("MYPAGE", "ONCREATE");

//        loadImage();

        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            if (firebaseUser != null) {
                                String Uid = firebaseUser.getUid();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("MYPAGE", "SUCCESS");

                                    if (documentSnapshot.getId().equals(Uid)) {
                                        USER_ID = documentSnapshot.getData().get("userId").toString();
                                        USER_VEGAN_TYPE = documentSnapshot.getData().get("userVeganType").toString();
                                        USER_ALLERGY = documentSnapshot.getData().get("userAllergy").toString().replaceAll("\\[|\\]", "").replaceAll(", ", ", ");
                                        USER_PROFILE_IMG = documentSnapshot.getData().get("userProfileImg").toString();
                                        Log.d("USER_ID", USER_ID);
                                        Log.d("USER_VEGAN_TYPE", USER_VEGAN_TYPE);
                                        Log.d("USER_ALLERGY", USER_ALLERGY);
                                        userID.setText(USER_ID);
                                        userVeganType.setText(USER_VEGAN_TYPE);
                                        userAllergy.setText(USER_ALLERGY);
                                        Glide.with(MypageActivity.this)
                                                .load(USER_PROFILE_IMG)
                                                .into(Iv_Mypage_profile);
                                    }
                                }
                            }
                        } else {
                            Log.d("MYPAGE", "ERROR", task.getException());
                        }
                    }
                });

        Id = userID.getText().toString();
        VeganType = userVeganType.getText().toString();
        Allergy = userAllergy.getText().toString();

        Btn_EditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MypageActivity.this, EditProfileActivity.class);
                intent.putExtra("userId", Id);
                intent.putExtra("userVeganType", VeganType);
                intent.putExtra("userAllergy", Allergy);
                startActivity(intent);
                Log.d("intent 넘겨주는 데이터", Id + VeganType + Allergy);
            }
        });

        Btn_Mypage_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MypageActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        Btn_Mypage_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notion.so/FAQ-01fca52faabf46f5826a13a11bf3c65f?pvs=4"));
                startActivity(intent);
            }
        });

        Btn_Mypage_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MypageActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

//        db.collection("posts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if (firebaseUser != null) {
//                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//
//                                    String publisher = documentSnapshot.getData().get("publisher").toString();
//
//                                    if (publisher.equals(firebaseUser.getUid())) {
//                                        DELETE_POST = documentSnapshot.getData().get("postId").toString();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                });


//        Iv_Mypage_profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                dialog = new Dialog(MypageActivity.this);
//                dialog.setContentView(R.layout.dialog);
//                dialog.show();
//
//                Button Btn_Camera = dialog.findViewById(R.id.Btn_Camera);
//                Btn_Camera.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
//                        if (!hasCamPerm) {
//                            ActivityCompat.requestPermissions(MypageActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                        }
//                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                        if (intent.resolveActivity(getPackageManager()) != null) {
//                            try {
//                                imageFile = createImageFile();
//                                uploader();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            if (imageFile != null) {
//                                imageUri = FileProvider.getUriForFile(getApplicationContext(),
//                                        "kr.ac.duksung.finalprojectvegan.fileprovider", imageFile);
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                                startActivityForResult(intent, CAMERA);
//                                uploader();
//                            }
//                        }
//                    }
//                });
//
//                Button Btn_Gallery = dialog.findViewById(R.id.Btn_Gallery);
//                Btn_Gallery.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//                        if (!hasWritePerm) {  // 권한 없을 시  권한설정 요청
//                            ActivityCompat.requestPermissions(MypageActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                        }
//                        intent = new Intent(Intent.ACTION_PICK);
//                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                        intent.setType("image/*");
//                        startActivityForResult(intent, GALLERY);
//
//                        uploader();
//                    }
//                });
//
//                Button Btn_Basic = dialog.findViewById(R.id.Btn_Basic);
//                Btn_Basic.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                        storageReference = firebaseStorage.getReference().child("users").child(firebaseUser.getUid()).child(imageFile.getName()); // reference에 경로 세팅
//                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                profileUpdate(uri);
//                            }
//                        });
//                    }
//                });

//                Intent intent = new Intent(getBaseContext(), PopupActivity.class);
//                intent.putExtra("type", PopupType.SELECT);
//                intent.putExtra("gravity", PopupGravity.CENTER);
//                intent.putExtra("title", "사진을 불러올 기능을 선택하세요");
//                intent.putExtra("buttonLeft", "카메라");
//                intent.putExtra("buttonCenter", "기본이미지");
//                intent.putExtra("buttonRight", "갤러리");
//                startActivityForResult(intent, 2);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////                        Log.d(TAG, "권한 설정 완료");
//                    } else {
////                        Log.d(TAG, "권한 설정 요청");
//                        ActivityCompat.requestPermissions(MypageActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                    }
//                }
            }
//        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            //데이터 받기
//            if (requestCode == 2) {
//                PopupResult result = (PopupResult) data.getSerializableExtra("result");
//                if (result == PopupResult.LEFT) {
//                    // 카메라 선택한 경우
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (intent.resolveActivity(getPackageManager()) != null) {
//                        try {
//                            imageFile = createImageFile();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        if (imageFile != null) {
//                            imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.finalprojectvegan.fileprovider", imageFile);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                            startActivityForResult(intent, CAMERA);
//                        }
//                    }
//                    uploader();
//
//                } else if (result == PopupResult.RIGHT) {
//                    // 작성 코드
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, GALLERY);
//                } else if (result == PopupResult.CENTER) {
//                    Log.d("IMAGE_URL", USER_PROFILE_IMG);
//                    Glide.with(MypageActivity.this)
//                            .load(USER_PROFILE_IMG)
//                            .into(Iv_Mypage_profile);
//                }
//            } else if (requestCode == GALLERY) {
//                imageUri = data.getData();
//                imagePath = data.getDataString();
//                if (imagePath.length() > 0) {
//                    Glide.with(this)
//                            .load(imagePath)
//                            .into(Iv_Mypage_profile);
//                    imageFrom = requestCode;
//
//                    uploader();
//                }
//            }
//        }
//    }

    //////////////////////////////////////////
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) { // 결과 있을 경우
//            if (requestCode == GALLERY) { // 갤러리 선택한 경우
//                imageUri = data.getData(); // 이미지 Uri 정보
//                imagePath = data.getDataString(); // 이미지 위치 경로 정보
//            } // 카메라 선택한 경우는 아래 createImageFile에서 imageFile 생성해주기 때문에 여기선 불필요
//            if (imagePath.length() > 0) { // 저장한 파일 경로를 Glide 사용해 Iv에 세팅
//                Glide.with(this)
//                        .load(imagePath)
//                        .into(Iv_Mypage_profile);
//                imageFrom = requestCode;
//            }
//        }
//    }
//
//    File createImageFile() throws IOException {
//        String timeStamp = imageDate.format(new Date());
//        String fileName = "PROFILE_IMAGE_" + timeStamp; // 이미지 파일 명
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File file = File.createTempFile(fileName, ".jpg", storageDir); // 파일 생성
//        imagePath = file.getAbsolutePath(); // 파일 절대경로 저장
//        return file;
//    }
//
//    void uploader() {
//        UploadTask uploadTask = null; // 파일 업로드하는 객체
//        switch (imageFrom) {
//            case GALLERY: // 갤러리에서 가져온 경우
//                String timeStamp = imageDate.format(new Date());
//                String imageFileName = "PROFILE_IMAGE_" + timeStamp + "_.png"; // 새로운 파일명 생성 후
//                storageReference = firebaseStorage.getReference().child("users").child(firebaseUser.getUid()).child(imageFileName); // reference에 경로 세팅
//                uploadTask = storageReference.putFile(imageUri); // 업로드할 파일과 위치 설정
//                break;
//
//            case CAMERA: // 카메라에서 가져온 경우 createImage에서 생성한 이미지 우일명 이용
//                storageReference = firebaseStorage.getReference().child("users").child(firebaseUser.getUid()).child(imageFile.getName()); // reference에 경로 세팅
//                uploadTask = storageReference.putFile(Uri.fromFile(imageFile));
//                break;
//        }
//
//        // 파일 업로드
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Log.d("Upload", "success");
//                downloadUri(); // 업로드 성공시 업로드한 파일 Uri 다운받기
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("Upload", "Failure");
//            }
//        });
//    }
//
//    void downloadUri() {
//        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Log.d("DownloadUri", "success");
//                Log.d("DownloadUri", "uri = " + uri);
//                profileUpdate(uri);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("DownloadUri", "Failure");
//            }
//        });
//    }
//
//    private void profileUpdate(Uri uri) {
//
//        DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
//        documentReference
//                .update("userProfileImg", uri.toString())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d("MYPAGE_IMG_UPDATE", "success");
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("MYPAGE_IMG_UPDATE", "failure");
//                    }
//                });
//    }
    ///////////////////////////////

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            //데이터 받기
//            if (requestCode == 2) {
//                PopupResult result = (PopupResult) data.getSerializableExtra("result");
//                if (result == PopupResult.LEFT) {
//                    // 작성 코드
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    activityResultPicture.launch(intent);
//
//                } else if (result == PopupResult.RIGHT) {
//                    // 작성 코드
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, GALLERY);
//                }
//            }
//            else if(requestCode == GALLERY && resultCode == RESULT_OK) {
//                Uri uri = data.getData();
//                setImage(uri);
//            }
//        }
//        // 갤러리
////        else if(requestCode == GALLERY && resultCode == RESULT_OK) {
////            Uri uri = data.getData();
////            setImage(uri);
////        }
//    }
//
//    // 카메라
//    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        Bundle extras = result.getData().getExtras();
//                        Bitmap bitmap = (Bitmap) extras.get("data");
//
//                        byte[] bytes = bitmapToByteArray(bitmap);
//
//                        uploadImage(bytes);
//
//                        dialog = new ProgressDialog(MypageActivity.this); //프로그레스 대화상자 객체 생성
//                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //프로그레스 대화상자 스타일 원형으로 설정
//                        dialog.setMessage("이미지 저장 중입니다."); //프로그레스 대화상자 메시지 설정
//                        dialog.show(); //프로그레스 대화상자 띄우기
//
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable(){
//                            @Override
//                            public void run() {
//                                dialog.dismiss(); // 3초 시간지연 후 프로그레스 대화상자 닫기
//                                Toast.makeText(MypageActivity.this, "이미지 저장.", Toast.LENGTH_LONG).show();
//
//                                loadImage();
//                            }
//                        }, 5000);
//                    }
//                }
//            }
//    );
//
//    public byte[] bitmapToByteArray( Bitmap bitmap ) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
//        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
//        byte[] byteArray = stream.toByteArray() ;
//        return byteArray ;
//    }
//
//    public void uploadImage(byte[] bytes) {
//
//        firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference storageReference = firebaseStorage.getReference();
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        final StorageReference ProfileImageReference = storageReference.child("users/" + firebaseUser.getUid() + "/profileImage.jpg");
//
//        UploadTask uploadTask = ProfileImageReference.putBytes(bytes);
//        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    Log.d("실패", "실패");
//                    throw task.getException();
//                }
//                return ProfileImageReference.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                    Log.d("성공", "성공" + downloadUri);
//                } else {
//                    Log.d("실패2", "실패2");
//                }
//            }
//        });
//    }
//
//    public void loadImage() {
//        Iv_Mypage_profile = findViewById(R.id.Iv_Mypage_Profile);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//
//        firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference storageReference = firebaseStorage.getReference();
//        StorageReference pathReference = storageReference.child("users");
//        if (pathReference == null) {
//            Toast.makeText(this, "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
//        } else {
//            StorageReference submitProfile = storageReference.child("users/" + firebaseUser.getUid() + "/profileImage.jpg");
//            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with(MypageActivity.this)
//                            .load(uri)
//                            .centerCrop()
//                            .override(300)
//                            .into(Iv_Mypage_profile);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//        }
//    }
//
//    private void setImage(Uri uri) {
//        try{
//            InputStream in = getContentResolver().openInputStream(uri);
//            Bitmap bitmap = BitmapFactory.decodeStream(in);
//            Log.e("setImage", "이미지 to 비트맵");
//
//            byte[] bytes = bitmapToByteArray(bitmap);
//
//            uploadImage(bytes);
//
//            dialog = new ProgressDialog(MypageActivity.this); //프로그레스 대화상자 객체 생성
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //프로그레스 대화상자 스타일 원형으로 설정
//            dialog.setMessage("제출 중입니다."); //프로그레스 대화상자 메시지 설정
//            dialog.show(); //프로그레스 대화상자 띄우기
//
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable(){
//                @Override
//                public void run() {
//                    dialog.dismiss(); // 3초 시간지연 후 프로그레스 대화상자 닫기
//                    Toast.makeText(MypageActivity.this, "이미지 저장.", Toast.LENGTH_LONG).show();
//                    loadImage();
//                }
//            }, 5000);
//
//        } catch (FileNotFoundException e){
//            e.printStackTrace();
//        }
//    }
//
//    private void getFirebaseProfileImage(FirebaseUser id) {
//        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profileImage");
//        if (!file.isDirectory()) {
//            file.mkdir();
//        }
//        loadImage();
//    }

    ////////////////////////

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
//    private static final int CAMERA = 100;
//    private static final int GALLERY = 101;
//
//    private TextView Btn_Logout, Btn_DeleteAccount, Btn_Info, Btn_BlockInfo, Btn_setting, Btn_help;
//    private TextView userID, userVeganType, userAllergy;
//    private ImageView Iv_Mypage_Profile;
//    private Button Btn_EditAccount;
//
//    private String imagePath = "";
//    private File imageFile = null;
//    Uri imageUri = null;
//    int imageFrom;
//    SimpleDateFormat imageDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
//
//    private FirebaseAuth firebaseAuth;
//    private FirebaseUser firebaseUser;
//    private FirebaseStorage firebaseStorage;
//    private StorageReference storageReference;
//    private FirebaseFirestore db;
//
//    ProgressDialog dialog;
//
//    String USER_ID;
//    String USER_VEGAN_TYPE;
//    String USER_ALLERGY;
//
//    InputImage image;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mypage);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//        String user = firebaseUser.toString();
//
//        getFirebaseProfileImage(firebaseUser);
//
//        Iv_Mypage_Profile = findViewById(R.id.Iv_Mypage_Profile);
//        userID = findViewById(R.id.Tv_Mypage_UserId);
//        userVeganType = findViewById(R.id.Tv_Mypage_VeganType);
//        userAllergy = findViewById(R.id.Tv_Mypage_Allergy);
//
//        Btn_EditAccount = findViewById(R.id.Btn_EditAccount);
//        Btn_Logout = findViewById(R.id.Btn_Mypage_Logout);
//        Btn_help = findViewById(R.id.Btn_Mypage_help);
//        Btn_Info = findViewById(R.id.Btn_Mypage_Info);
//        Btn_setting = findViewById(R.id.Btn_Mypage_Setting);
//        Btn_BlockInfo = findViewById(R.id.Btn_Mypage_BlockInfo);
//        Btn_DeleteAccount = findViewById(R.id.Btn_Mypage_DeleteAccount);
//
//        Btn_EditAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MypageActivity.this, RoadingActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        Btn_Logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseAuth.signOut();
//                Intent intent = new Intent(MypageActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        Iv_Mypage_Profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(getBaseContext(), PopupActivity.class);
//                intent.putExtra("type", PopupType.SELECT);
//                intent.putExtra("gravity", PopupGravity.CENTER);
//                intent.putExtra("title", "사진을 불러올 기능을 선택하세요");
//                intent.putExtra("buttonLeft", "카메라");
//                intent.putExtra("buttonRight", "갤러리");
//                startActivityForResult(intent, 2);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                        Log.d("Image Permission", "권한 설정 완료");
//                    } else {
//                        Log.d("Image Permission", "권한 설정 요청");
//                        ActivityCompat.requestPermissions(MypageActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                    }
//                }
//
//            }
//        });
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            //데이터 받기
//            if (requestCode == 2) {
//                PopupResult result = (PopupResult) data.getSerializableExtra("result");
//                if (result == PopupResult.LEFT) {
//                    // 카메라 선택
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    activityResultPicture.launch(intent);
//
////                    if (intent.resolveActivity(getPackageManager()) != null) {
////                        try {
//////                            imageFile = createImageFile();
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
////                        if (imageFile != null) {
////                            imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.finalprojectvegan.fileprovider", imageFile);
////                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
////                            startActivityForResult(intent, CAMERA);
////                        }
////                    }
//
//                } else if (result == PopupResult.RIGHT) {
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, GALLERY);
//                }
//            }
//        }
//        // 갤러리
//        else if(requestCode == GALLERY && resultCode == RESULT_OK) {
//            imageUri = data.getData();
//            setImage(imageUri);
////            imageUri = data.getData();
////            imagePath = data.getDataString();
////            if(imagePath.length() > 0) {
////                Glide.with(this)
////                        .load(imagePath)
////                        .into(Iv_Mypage_Profile);
////                imageFrom = requestCode;
////            }
//        }
//
////        if (imagePath.length() > 0 && imageFrom >= 100) {
//////            uploader();
////            Toast.makeText(MypageActivity.this, "게시물이 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
////            finish();
////        }
//    }
//
////    File createImageFile() throws IOException {
////        String timeStamp = imageDate.format(new Date());
////        String fileName = "PROFILE IMAGE_" + timeStamp;
////        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
////        File file = File.createTempFile(fileName, ".jpg", storageDir);
////        imagePath = file.getAbsolutePath();
////        return file;
////    }
////
////    void uploader() {
////        UploadTask uploadTask = null; // 파일 업로드하는 객체
////        switch (imageFrom) {
////            case GALLERY: // 갤러리에서 가져온 경우
////                String timeStamp = imageDate.format(new Date());
////                String imageFileName = "PROFILE IMAGE_" + timeStamp + "_.png"; // 새로운 파일명 생성 후
////                storageReference = firebaseStorage.getReference().child("userProfile").child(imageFileName); // reference에 경로 세팅
////                uploadTask = storageReference.putFile(imageUri); // 업로드할 파일과 위치 설정
////                break;
////
////            case CAMERA: // 카메라에서 가져온 경우 createImage에서 생성한 이미지 우일명 이용
////                storageReference = firebaseStorage.getReference().child("userProfile").child(imageFile.getName()); // reference에 경로 세팅
////                uploadTask = storageReference.putFile(Uri.fromFile(imageFile));
////                break;
////        }
////
////        // 파일 업로드
////        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
////            @Override
////            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                Log.d("Upload", "success");
////                downloadUri(); // 업로드 성공시 업로드한 파일 Uri 다운받기
////            }
////        }).addOnFailureListener(new OnFailureListener() {
////            @Override
////            public void onFailure(@NonNull Exception e) {
////                Log.d("Upload", "Failure");
////            }
////        });
////    }
////
////    void downloadUri() {
////        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////            @Override
////            public void onSuccess(Uri uri) {
////                Log.d("DownloadUri", "success");
////                Log.d("DownloadUri", "uri = " + uri);
////                profileUpdate(uri);
////            }
////        }).addOnFailureListener(new OnFailureListener() {
////            @Override
////            public void onFailure(@NonNull Exception e) {
////                Log.d("DownloadUri", "Failure");
////            }
////        });
////    }
////
////    private void profileUpdate(Uri uri) {
////
//////        db.collection("users").document(firebaseUser.getUid())
//////                .update("userImage", uri)
//////                .addOnSuccessListener(new OnSuccessListener<Void>() {
//////                    @Override
//////                    public void onSuccess(Void unused) {
//////                        Log.d("profile update", "success");
//////                    }
//////                })
//////                .addOnFailureListener(new OnFailureListener() {
//////                    @Override
//////                    public void onFailure(@NonNull Exception e) {
//////                        Log.d("profile update", "failure");
//////                    }
//////                });
////    }
//
//    // 카메라
//    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        Bundle extras = result.getData().getExtras();
//                        Bitmap bitmap = (Bitmap) extras.get("data");
////                        imageView_profile.setImageBitmap(bitmap);
//
////                        image = InputImage.fromBitmap(bitmap, 0);
//
//                        byte[] bytes = bitmapToByteArray(bitmap);
//
//                        uploadImage(bytes);
//
//                        dialog = new ProgressDialog(MypageActivity.this); //프로그레스 대화상자 객체 생성
//                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //프로그레스 대화상자 스타일 원형으로 설정
//                        dialog.setMessage("제출 중입니다."); //프로그레스 대화상자 메시지 설정
//                        dialog.show(); //프로그레스 대화상자 띄우기
//
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable(){
//                            @Override
//                            public void run() {
//                                dialog.dismiss(); // 3초 시간지연 후 프로그레스 대화상자 닫기
//                                Toast.makeText(MypageActivity.this, "이미지 저장.", Toast.LENGTH_LONG).show();
//
//                                loadImage();
//                            }
//                        }, 5000);
//                    }
//                }
//            }
//    );
//
//    public byte[] bitmapToByteArray( Bitmap bitmap ) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
//        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
//        byte[] byteArray = stream.toByteArray() ;
//        return byteArray ;
//    }
//
//    public void uploadImage(byte[] bytes) {
//        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference storageReference = firebaseStorage.getReference();
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        final StorageReference mountainImageReference = storageReference.child("users/" + firebaseUser.getUid() + "/profileImage.jpg");
//
//        UploadTask uploadTask = mountainImageReference.putBytes(bytes);
//        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    Log.d("실패", "실패");
//                    throw task.getException();
//                }
//                return mountainImageReference.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                    Log.d("성공", "성공" + downloadUri);
//                } else {
//                    Log.d("실패2", "실패2");
//                }
//            }
//        });
//    }
//
//    public void loadImage() {
//        Iv_Mypage_Profile = (ImageView) findViewById(R.id.imageView_profile);
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference storageReference = firebaseStorage.getReference();
//        StorageReference pathReference = storageReference.child("users");
//        if (pathReference == null) {
//            Toast.makeText(this, "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
//        } else {
//            StorageReference submitProfile = storageReference.child("users/" + firebaseUser.getUid() + "/profileImage.jpg");
//            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with(MypageActivity.this).load(uri).centerCrop().override(300).into(Iv_Mypage_Profile);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//        }
//    }
//
//    private void setImage(Uri uri) {
//        try{
//            InputStream in = getContentResolver().openInputStream(uri);
//            Bitmap bitmap = BitmapFactory.decodeStream(in);
////            imageView_profile.setImageBitmap(bitmap);
//
////            image = InputImage.fromBitmap(bitmap, 0);
//            Log.e("setImage", "이미지 to 비트맵");
//
//            byte[] bytes = bitmapToByteArray(bitmap);
//
//            uploadImage(bytes);
//
//            dialog = new ProgressDialog(MypageActivity.this); //프로그레스 대화상자 객체 생성
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //프로그레스 대화상자 스타일 원형으로 설정
//            dialog.setMessage("제출 중입니다."); //프로그레스 대화상자 메시지 설정
//            dialog.show(); //프로그레스 대화상자 띄우기
//
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable(){
//                @Override
//                public void run() {
//                    dialog.dismiss(); // 3초 시간지연 후 프로그레스 대화상자 닫기
//                    Toast.makeText(MypageActivity.this, "이미지 저장.", Toast.LENGTH_LONG).show();
//                    loadImage();
//                }
//            }, 5000);
//
//        } catch (FileNotFoundException e){
//            e.printStackTrace();
//        }
//    }
//
//    private void getFirebaseProfileImage(FirebaseUser id) {
//        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profileImage");
//        if (!file.isDirectory()) {
//            file.mkdir();
//        }
//        loadImage();
//    }
//
////    private void downloadImg(String id) {
////        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
////        StorageReference storageReference = firebaseStorage.getReference();
////        storageReference.child("profileImage/" + "profile" + id + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////            @Override
////            public void onSuccess(Uri uri) {
////                Glide.with(MypageActivity.this).load(uri).into(imageView_profile);
////                dialogwithUri = uri;
////            }
////        }).addOnFailureListener(new OnFailureListener() {
////            @Override
////            public void onFailure(@NonNull Exception e) {
////
////            }
////        });
////    }
