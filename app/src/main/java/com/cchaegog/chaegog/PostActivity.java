package com.cchaegog.chaegog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    // ***** sns구현 짱 김(즐겨찾기 명칭) 내용 구현 -> 오류 *******

    // 인스턴스 생성

    // Uri와 url 생성 -> 이미지 주소 접근
    Uri imageUri;
    String myUrl = "";

    // 파일 업로드를 위한 인스턴스 생성
    // UploadTask -> upload 기능을 위한 인스턴스 생성 / StorageReference -> Cloud storage 사용 목적 참조
    UploadTask uploadTask;
    StorageReference storageReference;

    // 리소스 파일과 연결될 변수들 생성
    ImageView close, image_added;
    TextView post;
    EditText description;

    InputImage image;

    private final int GALLERY_CODE = 10;

    URI uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // 리소스 파일과 각 변수들 연결
        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);

        // firebaseStorage 참조 생성
        storageReference = FirebaseStorage.getInstance().getReference("posts");

        // close 버튼 클릭시
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // main으로 넘긴다
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        // post 버튼 클릭시
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // uploadImage 메서드 실행
                uploadImage();
            }
        });

        image_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });

        // CropImage 액티비티 실행
//        CropImage.activity()
//                .setAspectRatio(1,1)
//                .start(PostActivity.this);
//        onSelectImageClick(image_added);

    }

//    public void onSelectImageClick(View view) {
//        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GALLEY_CODE) {
//            try {
//                Uri file = Uri.fromFile(new File(uri));
//                final StorageReference riverRef = storageReference.child("im ages/" + file.getLastPathSegment());
//                UploadTask uploadTask = riverRef.putFile(file);
//
//                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                        if (!task.isSuccessful()) {
//                            throw task.getException();
//                        }
//                        return riverRef.getDownloadUrl();
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(PostActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
//
//                            Uri downloadUrl = task.getResult();
//
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent);
//                        } else {
//
//                        }
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//
//    }
//    private String getRealPathFromUri (Uri uri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
//        Cursor cursor = cursorLoader.loadInBackground();
//
//        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String result = cursor.getString(columnIndex);
//        cursor.close();
//        return result;
//
//    }

    private void uploadImage() {

        // 업로드 실행 중 보여줄 dialog 생성
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        if (imageUri != null) {
            StorageReference filereference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = filereference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filereference.getDownloadUrl();
                }
            });

            uploadTask.addOnCompleteListener(new OnCompleteListener() {

                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()) {
                        Uri downloadUri = (Uri) task.getResult();
                        myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

                        String postid = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("postimage", myUrl);
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(postid).setValue(hashMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(PostActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "No ImageSelected!", Toast.LENGTH_SHORT).show();
        }
    }


    private String getFileExtension(Uri uri) {
        android.webkit.MimeTypeMap.getSingleton();
        String extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(uri));
        return extension;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            setImage(uri);
        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            imageUri = result.getUri();
//            image_added.setImageURI(imageUri);
//        } else {
//            Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(PostActivity.this, MainActivity.class));
//            finish();
//        }
    }

    private void setImage(Uri uri) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            image_added.setImageBitmap(bitmap);

            image = InputImage.fromBitmap(bitmap, 0);
            Log.e("setImage", "이미지 to 비트맵");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

    // ********************************


//    ImageView close, image_added;
//    TextView post;
//    EditText description;
//
//    private String[] permissionList = {Manifest.permission.READ_EXTERNAL_STORAGE};
//
//    private FirebaseStorage storage;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post);
//
//        // 리소스 파일과 각 변수들 연결
//        close = findViewById(R.id.close);
//        image_added = findViewById(R.id.image_added);
//        post = findViewById(R.id.post);
//        description = findViewById(R.id.description);
//
//        storage = FirebaseStorage.getInstance();
//    }
//
//    // 권한 체크 함수
//    public void checkPermission() {
//        // 현재 버전 6.0 미만이면 종료 -> 6.0 이후부터 권한 허락
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
//
//        // 각 권한 허용 여부를 확인
//        for (String permission : permissionList) {
//            int chk = checkCallingOrSelfPermission(permission);
//            // 거부 상태라면
//            if (chk == PackageManager.PERMISSION_DENIED) {
//                // 사용자에게 권한 허용여부를 확인하는 창 띄우기
//                requestPermissions(permissionList, 0);
//                break;
//            }
//        }
//    }