package com.cchaegog.chaegog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cchaegog.chaegog.Model.WriteReviewInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WriteReviewActivity extends AppCompatActivity {
    ImageView add_image, imageView1, imageView2, imageView3;
    Button Btn_uploadReview;
    EditText editText_review;
    TextView restaurant_name_review;
    String name, rating, token;
    RatingBar ratingBar;
    final int CAMERA = 100;
    final int GALLERY = 101;
    int imgFrom;
    SimpleDateFormat imageDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.getDefault());
    String imagePath = "";
    File imageFile = null;
    Uri imageUri = null;
    private Dialog dialog;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("review").document();

        add_image = findViewById(R.id.add_image);
        imageView1 = findViewById(R.id.review_imageView);
//        imageView2 = findViewById(R.id.image_review2);
//        imageView3 = findViewById(R.id.image_review3);
        Btn_uploadReview = findViewById(R.id.Btn_uploadReview);
        editText_review = findViewById(R.id.edit_review);
        restaurant_name_review = findViewById(R.id.restaurant_name_review);
//        recyclerView = findViewById(R.id.image_recyclerView);
        ratingBar = findViewById(R.id.ratingBar);

        Intent nameIntent = getIntent();
        name = nameIntent.getStringExtra("name");
        restaurant_name_review.setText(name);

        // 이미지 버튼 클릭시
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 팝업 창 띄우기
                dialog = new Dialog(WriteReviewActivity.this);
                dialog.setContentView(R.layout.dialog);
                dialog.show();

                // 팝업창 -> 카메라 선택시
                Button Btn_Camera = dialog.findViewById(R.id.Btn_Camera);
                Btn_Camera.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // 팝업창 내리기
                        dialog.dismiss();
                        // 권한설정
                        boolean hasCamPerm = false;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                        }
                        if (!hasCamPerm) {
                            ActivityCompat.requestPermissions(WriteReviewActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (intent.resolveActivity(getPackageManager()) != null) {
                            try {
                                imageFile = createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (imageFile != null) {
                                Uri imageUri = FileProvider.getUriForFile(getApplicationContext(),
                                        "kr.ac.duksung.projectvegan.fileprovider", imageFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, CAMERA);
                            }
                        }
                    }
                });

                // 팝업창 -> 갤러리 선택시
                Button Btn_Gallery = dialog.findViewById(R.id.Btn_Gallery);
                Btn_Gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 팝업창 내리기
                        dialog.dismiss();
                        // 권한 설정
                        boolean hasWritePerm = false;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                        }
                        if (!hasWritePerm) {  // 권한 없을 시  권한설정 요청
                            ActivityCompat.requestPermissions(WriteReviewActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                        intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setType("image/*");
//                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 다중이미지 선택
                        startActivityForResult(intent, GALLERY);
                    }
                });
            }
        });

        // 등록 버튼 누를시
        Btn_uploadReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String review = ((EditText) findViewById(R.id.edit_review)).getText().toString();
                Log.d("Review_onclick",review);

                if(review.equals("")) {
                    Toast.makeText(WriteReviewActivity.this, "리뷰를 작성해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if(imagePath.length() > 0 && imgFrom >= 100) {
                        uploader();
                    } else {
                        ReviewUpdateWithoutImage();
                    }

                    // 알림 test
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                token = documentSnapshot.getData().get("userToken").toString();
//
//                                if (documentSnapshot.getId().equals(firebaseUser.getUid())) {
//                                    Log.d("myToken!", token);
//                                    pushNotification = new PushNotification(new NotificationData("채곡채곡", "내가 쓴 글에 댓글이 달렸어요!"), token);
////                                    Call<NotificationAPI> call = (Call<NotificationAPI>) RetrofitInstance.getApi();
////                                    call.enqueue(new Callback<NotificationAPI>(){
////
////                                        @Override
////                                        public void onResponse(Call<NotificationAPI> call, Response<NotificationAPI> response) {
////                                            Log.d("SendNotification", "Response : " + response);
////                                        }
////
////                                        @Override
////                                        public void onFailure(Call<NotificationAPI> call, Throwable t) {
////                                        }
////                                    });
//                                    //sendNotification.SendNotification(pushNotification);
//                                }
//                            }
//
//                        } else {
//                            Log.d("error", "Error getting documents", task.getException());
//                        }
//                    }
//                });

                    Toast.makeText(WriteReviewActivity.this, "리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                imageUri = data.getData(); // 이미지 Uri 정보
                imagePath = data.getDataString(); // 이미지 위치 경로 정보
            }
            if(imagePath.length() > 0) {

                Glide.with(this)
                        .load(imageUri).override(500, 500)
                        .apply(new RequestOptions().transform(new CenterCrop(),
                                new RoundedCorners(10)))
                        .into(imageView1);
                imgFrom = requestCode;
                imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageView1.setImageResource(0);
                    }
                });
            }
        }


//        if (resultCode == RESULT_OK) {
//            if (requestCode == GALLERY) {
//                ClipData clipData = data.getClipData();
//                for (int i = 0; i < clipData.getItemCount(); i++) {
//                    Uri imageUri = clipData.getItemAt(i).getUri();
//                    String imagePath = clipData.getItemAt(i).toString();
//
//                    uriList.add(imageUri); // 이미지 Uri 정보 리스트
//                    imagePathList.add(imagePath); // 이미지 위치 경로 정보 리스트
//
//                    Log.d("UriList1", "uriList : " + uriList);
//                    adapter = new MultiImageAdapter(uriList, WriteReviewActivity.this);
//                    recyclerView.setAdapter(adapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(WriteReviewActivity.this, LinearLayoutManager.HORIZONTAL, false));
//
////                    if (imagePath.length() > 0) {
////                        viewPager2 = findViewById(R.id.review_viewpager);
////
////                        viewPager2.setOffscreenPageLimit(1);
////                        viewPager2.setAdapter(new ImageSliderAdapter(uriList, this));
////
////                        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
////                            @Override
////                            public void onPageSelected(int position) {
////                                super.onPageSelected(position);
////                            }
////                        });
////                    }
//                }
//            }
//        }
    }

    // 카메라랑 연결되는 이미지 파일 생성
    File createImageFile() throws IOException {
        String timeStamp = imageDate.format(new Date());
        String fileName = "IMAGE_" + timeStamp; // 이미지 파일 명
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(fileName, ".jpg", storageDir); // 파일 생성
        imagePath = file.getAbsolutePath(); // 파일 절대경로 저장
        return file;
    }

    void uploader() { // 등록 버튼 클릭 -> 이미지를 storage로 업로드
        UploadTask uploadTask = null;
        switch (imgFrom) {
            case GALLERY:
                String timeStamp = imageDate.format(new Date());
                String imageFileName = "IMAGE_" + timeStamp + "_.png";

                storageReference = firebaseStorage.getReference().child("review")
                        .child(firebaseUser.getUid()).child(imageFileName); // reference에 경로 세팅
//                storageReference = firebaseStorage.getReference().child("review")
//                        .child(firebaseUser.getUid()).child(documentReference.getId()).child(imageFileName);
                uploadTask  = storageReference.putFile(imageUri);
                Log.d("review_uploader", "docId : " + documentReference.getId());

                break;

            case CAMERA:
                storageReference = firebaseStorage.getReference().child("review").child(imageFile.getName()); // reference에 경로 세팅
                uploadTask = storageReference.putFile(Uri.fromFile(imageFile));
                break;
        }

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

    // 지정한 경로 (reference)에 대한 uri 다운
    void downloadUri() {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("DownloadUri", "success");
                Log.d("DownloadUri", "uri = " + uri);
                ReviewUpdate(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DownloadUri", "Failure");
            }
        });
    }

    private void ReviewUpdate(Uri uri) {
        final String review = ((EditText) findViewById(R.id.edit_review)).getText().toString();
        if(ratingBar.getRating() == 0) {
            rating = "5.0";
        } else {
            rating = ratingBar.getRating() + "";
        }
        WriteReviewInfo reviewInfo = new WriteReviewInfo(documentReference.getId(), rating, name, review, firebaseUser.getUid(), uri.toString(), new Date());
        uploader(reviewInfo);

    }

    private void ReviewUpdateWithoutImage() {
        final String review = ((EditText) findViewById(R.id.edit_review)).getText().toString();
        if(ratingBar.getRating() == 0) {
            rating = "5.0";
        } else {
            rating = ratingBar.getRating() + "";
        }
        WriteReviewInfo reviewInfo = new WriteReviewInfo(documentReference.getId(), rating, name, review, firebaseUser.getUid(), new Date());
        uploader(reviewInfo);

    }

    private void uploader(WriteReviewInfo writeReviewInfo) {
        documentReference.set(writeReviewInfo)
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
    }
}