package com.example.finalprojectvegan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalprojectvegan.CommentActivity;
import com.example.finalprojectvegan.Model.FeedInfo;
import com.example.finalprojectvegan.R;
import com.example.finalprojectvegan.Model.UserInfo;
import com.example.finalprojectvegan.Model.WritePostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HomefeedAdapter extends RecyclerView.Adapter<HomefeedAdapter.ViewHolder>{

    private ArrayList<FeedInfo> FeedDataset;
    private Context context;
    private ImageView Iv_HomeFeed_Image, Iv_HomeFeed_Profile;
    private String FeedId;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public Button Btn_HomeFeedComment, Btn_HomeFeedEtc;
        public CheckBox Cb_HomeFeedFavorite;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;

            db = FirebaseFirestore.getInstance();

            Cb_HomeFeedFavorite = view.findViewById(R.id.Cb_HomeFeedFavorite);
            Cb_HomeFeedFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (checked) {
                        Cb_HomeFeedFavorite.setEnabled(true);

                        Log.d("CHECK", "눌림");
                    } else {
                        Log.d("CHECK", "해제");
                    }
                }
            });

            // 피드에서 댓글 아이콘 클릭시
            Btn_HomeFeedComment = view.findViewById(R.id.Btn_HomeFeedComment);
            Btn_HomeFeedComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 클릭된 view 파악
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        // 클릭한 view 알아내고 (pos) 해당 view의 FeedId값을 가져온다.
                        FeedId = FeedDataset.get(pos).getPostId();
                        Log.d("DOCUMENTID_Send", FeedId);
                        // CommentActivity로 FeedId 값 전달 -> 받아서 클릭한 Feed에 해당하는 댓글들만 보여준다.
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("POSTSDocumentId", FeedId);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public HomefeedAdapter(Context context, ArrayList<FeedInfo> myDataset) {
        FeedDataset = myDataset;
        this.context = context;
    }

    @NonNull
    @Override
    public HomefeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.homefeed_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(cardView);

        Iv_HomeFeed_Image = cardView.findViewById(R.id.Iv_HomeFeed_Image);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardView cardView = holder.cardView;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<UserInfo> postUserList = new ArrayList<>();

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {

                                String user = FeedDataset.get(holder.getAbsoluteAdapterPosition()).getPublisher();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());

//                                    TextView publisherTextView = cardView.findViewById(R.id.Tv_HomeFeed_Publisher);
                                    ImageView Iv_Profile = cardView.findViewById(R.id.Iv_HomeFeed_Profile);
//        publisherTextView.setText(mDataset.get(position).getPublisher());
//                                    String user = mDataset.get(holder.getAdapterPosition()).getPublisher();
//                                publisherTextView.setText(documentSnapshot.getData().get("userID").toString());
                                    if (documentSnapshot.getId().equals(user)) {
                                        TextView publisherTextView = cardView.findViewById(R.id.Tv_HomeFeed_Publisher);
                                        publisherTextView.setText(documentSnapshot.getData().get("userId").toString());
//                                        loadImage(uid);
                                    }
//        if (user == FirebaseAuth.getInstance().getCurrentUser().toString()) {
//            publisherTextView.setText(user);
//            Log.d("user", user);
//        }
                                }
                            }

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

//        CardView cardView = holder.cardView;
//
//        TextView publisherTextView = cardView.findViewById(R.id.homefeed_item_publisher);
////        publisherTextView.setText(mDataset.get(position).getPublisher());
//        String user = mDataset.get(position).getPublisher();
////        if (user == FirebaseAuth.getInstance().getCurrentUser().toString()) {
////            publisherTextView.setText(user);
////            Log.d("user", user);
////        }

//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        imageView_profile = cardView.findViewById(R.id.imageView_profile);
//
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.getReference();
//        StorageReference pathReference = storageReference.child("users");
//
//        if (pathReference == null) {
//            Toast.makeText(context, "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "저장소에 사진이 있습니다.", Toast.LENGTH_SHORT).show();
//            StorageReference submitProfile = storageReference.child("users/" + firebaseUser.getUid() + "/profileImage.jpg");
//            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
////                    Glide.with(cardView).load(uri).into(imageView_profile);
//                    Glide.with(context).load(uri).into(im)
////                    Toast.makeText(context, "사진 출력", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//        }

        TextView titleTextView = cardView.findViewById(R.id.Tv_HomeFeed_Title);
        titleTextView.setText(FeedDataset.get(position).getTitle());

        TextView createdAtTextView = cardView.findViewById(R.id.Tv_HomeFeed_CreatedAt);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(FeedDataset.get(position).getCreatedAt()));

        TextView contentsTextView = cardView.findViewById(R.id.Tv_HomeFeed_Content);
        contentsTextView.setText(FeedDataset.get(position).getContent());

        String url = FeedDataset.get(position).getUri();
//        Glide.with(holder.cardView)
//                .load(url)
//                .into(holder.homefeed_item_imageView);

//        TextView imagePathTextView = cardView.findViewById(R.id.homefeed_item_imagePath);
//        imagePathTextView.setText(mDataset.get(position).getImagePath());
        Glide.with(cardView)
                .load(url)
                .override(800, 800)
                .apply(new RequestOptions().transform(new CenterCrop(),
                        new RoundedCorners(10)))
                .into(Iv_HomeFeed_Image);
//        Log.d("url", "url : " + imagePathTextView);
//        loadImage();


    }

//    public void loadImage(String a) {
////        imageView_profile = (ImageView) findViewById(R.id.imageView_profile);
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        a = firebaseUser.getUid();
//
//        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference storageReference = firebaseStorage.getReference();
//        StorageReference pathReference = storageReference.child("users");
//        if (pathReference == null) {
//            Toast.makeText(context, "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
//        } else {
//            StorageReference submitProfile = storageReference.child("users/" + a + "/profileImage.jpg");
//            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with(context).load(uri).centerCrop().override(300).into(imageView_profile);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//        }
//    }

    @Override
    public int getItemCount() {
        return FeedDataset.size();
    }

//    public void loadImage() {
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference storageReference = firebaseStorage.getReference();
//        StorageReference pathReference = storageReference.child("posts");
//        if (pathReference == null) {
//
//        } else {
//            StorageReference submitProfile = storageReference.child("posts/" + firebaseUser.getUid() + "/postImage" + System.currentTimeMillis() + ".jpg");
//            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with(context).load(uri).into(homefeed_item_imageView);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//        }
//    }

}
