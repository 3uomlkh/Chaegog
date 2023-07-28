package com.example.finalprojectvegan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalprojectvegan.Model.FeedInfo;
import com.example.finalprojectvegan.Model.WritePostInfo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
import java.util.Date;

public class WritePostActivity extends AppCompatActivity {

    private static final int GALLERY = 101;
    private static final int CAMERA = 100;
    int imageFrom;
    private String imagePath = "";
    private File imageFile = null;
    SimpleDateFormat imageDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
    Uri imageUri = null;

    private Button Btn_UploadPost;
    private ImageView Iv_Add_UploadPost;
    private EditText Et_Post_Title, Et_Post_Contents;
    private Dialog dialog;
    private Intent intent;

    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        Iv_Add_UploadPost = findViewById(R.id.Iv_Add_UploadPost);
        Btn_UploadPost = findViewById(R.id.Btn_UploadPost);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_writepost);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Btn_UploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagePath.length() > 0 && imageFrom >= 100) {
                    uploader();
                    Toast.makeText(WritePostActivity.this, "게시물이 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        Iv_Add_UploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(WritePostActivity.this);
                dialog.setContentView(R.layout.dialog_cam);
                dialog.show();

                Button Btn_Camera = dialog.findViewById(R.id.Btn_Camera);
                Btn_Camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                        if (!hasCamPerm) {
                            ActivityCompat.requestPermissions(WritePostActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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

                Button Btn_Gallery =dialog.findViewById(R.id.Btn_Gallery);
                Btn_Gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                        if (hasWritePerm) {
                            ActivityCompat.requestPermissions(WritePostActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                        intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY);
                    }
                });



//                intent = new Intent(getBaseContext(), PopupActivity.class);
//                intent.putExtra("type", PopupType.SELECT);
//                intent.putExtra("gravity", PopupGravity.CENTER);
//                intent.putExtra("title", "사진을 불러올 기능을 선택하세요");
//                intent.putExtra("buttonLeft", "카메라");
//                intent.putExtra("buttonRight", "갤러리");
//                startActivityForResult(intent, 2);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////                        Log.d(TAG, "권한 설정 완료");
//                    } else {
////                        Log.d(TAG, "권한 설정 요청");
//                        ActivityCompat.requestPermissions(WritePostActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                    }
//                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) { // 결과 있을 경우
            if (requestCode == GALLERY) { // 갤러리 선택한 경우
                imageUri = data.getData(); // 이미지 Uri 정보
                imagePath = data.getDataString(); // 이미지 위치 경로 정보
            } // 카메라 선택한 경우는 아래 createImageFile에서 imageFile 생성해주기 때문에 여기선 불필요
            if (imagePath.length() > 0) { // 저장한 파일 경로를 Glide 사용해 Iv에 세팅
                Glide.with(this)
                        .load(imagePath)
                        .into(Iv_Add_UploadPost);
                imageFrom = requestCode;
            }
        }
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
//                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
//
//                } else if (result == PopupResult.RIGHT) {
//                    // 작성 코드
//                    intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, GALLERY);
//                }
//            } else if (requestCode == GALLERY) {
//                imageUri = data.getData();
//                imagePath = data.getDataString();
//                if (imagePath.length() > 0) {
//                    Glide.with(this)
//                            .load(imagePath)
//                            .into(Iv_Add_UploadPost);
//                    imageFrom = requestCode;
//                }
//            }
//        }
//    }

    File createImageFile() throws IOException {
        String timeStamp = imageDate.format(new Date());
        String fileName = "POST_IMAGE_" + timeStamp; // 이미지 파일 명
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(fileName, ".jpg", storageDir); // 파일 생성
        imagePath = file.getAbsolutePath(); // 파일 절대경로 저장
        return file;
    }

    void uploader() {
        UploadTask uploadTask = null; // 파일 업로드하는 객체
        switch (imageFrom) {
            case GALLERY: // 갤러리에서 가져온 경우
                String timeStamp = imageDate.format(new Date());
                String imageFileName = "POST_IMAGE_" + timeStamp + "_.png"; // 새로운 파일명 생성 후
                storageReference = firebaseStorage.getReference().child("Posts").child(imageFileName); // reference에 경로 세팅
                uploadTask = storageReference.putFile(imageUri); // 업로드할 파일과 위치 설정
                break;

            case CAMERA: // 카메라에서 가져온 경우 createImage에서 생성한 이미지 우일명 이용
                storageReference = firebaseStorage.getReference().child("Posts").child(imageFile.getName()); // reference에 경로 세팅
                uploadTask = storageReference.putFile(Uri.fromFile(imageFile));
                break;
        }

        // 파일 업로드
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Upload", "success");
                downloadUri(); // 업로드 성공시 업로드한 파일 Uri 다운받기
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Upload", "Failure");
            }
        });
    }

    void downloadUri() {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("DownloadUri", "success");
                Log.d("DownloadUri", "uri = " + uri);
                postUpdate(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DownloadUri", "Failure");
            }
        });
    }

    private void postUpdate(Uri uri) {

        final String title = ((EditText) findViewById(R.id.Et_Post_Title)).getText().toString();
        final String content = ((EditText) findViewById(R.id.Et_Post_Content)).getText().toString();
        final Long favorite = Long.valueOf("0");

        if (title.length() > 0 && content.length() > 0) {

            DocumentReference documentReference = db.collection("posts").document();
            FeedInfo feedInfo = new FeedInfo(title, content, firebaseUser.getUid(), documentReference.getId(), uri.toString(), favorite, new Date());

            documentReference.set(feedInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } else {
            Toast.makeText(this, "게시글 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}