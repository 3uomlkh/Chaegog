package com.example.finalprojectvegan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalprojectvegan.CommentActivity;
import com.example.finalprojectvegan.Model.FeedInfo;
import com.example.finalprojectvegan.MypageActivity;
import com.example.finalprojectvegan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomefeedAdapter extends RecyclerView.Adapter<HomefeedAdapter.ViewHolder>{

    private ArrayList<FeedInfo> FeedDataset;
    private Context context;
    private TextView Tv_HomeFeed_Title, Tv_HomeFeed_Content, Tv_HomeFeed_CreatedAt, Tv_HomeFeed_Publisher, Tv_HomeFeed_Favorite;
    private ImageView Iv_HomeFeed_Image, Iv_HomeFeed_Profile;
    private String FeedId, USER_ID, USER_PROFILE_IMG;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public Button Btn_HomeFeedComment, Btn_HomeFeedEtc;
        public CheckBox Cb_HomeFeedFavorite;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;

            // 변수 초기화
            db = FirebaseFirestore.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            // 좋아요 버튼 클릭시
            Cb_HomeFeedFavorite = view.findViewById(R.id.Cb_HomeFeedFavorite);
            Cb_HomeFeedFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    // 클릭된 view 파악
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        // 클릭한 view 알아내고 (pos) 해당 view의 FeedId값을 가져온다.
                        FeedId = FeedDataset.get(pos).getPostId();
                        Log.d("DOCUMENTID_Send", FeedId);
                    }
                    if (checked) {

                    }
                    db.collection("posts").document(FeedId).collection("favorite")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            String favoriteOnUser = documentSnapshot.getData().get("publisher").toString();
                                            // 좋아요 컬렉션에 현재 사용자 Uid가 있으면 삭제
                                            if (favoriteOnUser.equals(firebaseUser.getUid())) {
                                                compoundButton.isChecked();
                                                db.collection("posts").document(FeedId).collection("favorite").document(favoriteOnUser)
                                                        .delete()
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
                                                break;
                                            } else {
                                                // 없으면 추가
                                                Map<String, Object> favorite = new HashMap<>();
                                                favorite.put("userId", firebaseUser.getUid());
                                                DocumentReference postDoc = db.collection("posts").document(FeedId);
                                                DocumentReference favoriteDoc = postDoc.collection("favorite").document(firebaseUser.getUid());
                                                favoriteDoc.set(favorite);
                                                postDoc.update("favorite", FieldValue.increment(1));
                                            }
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
//                    if (checked) {
//                        db.collection("posts").document(FeedId).collection("favorite")
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                                if (documentSnapshot.getData().get("publisher").equals(firebaseUser.getUid())) {
//
//                                                }
//                                            }
//                                        }
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//
//                                    }
//                                });
//                        Cb_HomeFeedFavorite.setEnabled(true);
//                        Log.d("CHECK", "눌림");
//
//                        db.collection("posts").document(FeedId)
//                                .update("favorite", FieldValue.increment(1))
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Log.d("좋아요", "성공");
//                                    }
//                                });
//                    } else {
//                        Log.d("CHECK", "해제");
//                        db.collection("posts").document(FeedId)
//                                .update("favorite", FieldValue.increment(-1))
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Log.d("좋아요", "성공");
//                                    }
//                                });
//                    }
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

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Iv_HomeFeed_Image = cardView.findViewById(R.id.Iv_HomeFeed_Image);
        Iv_HomeFeed_Profile = cardView.findViewById(R.id.Iv_HomeFeed_Profile);
        Tv_HomeFeed_Title = cardView.findViewById(R.id.Tv_HomeFeed_Title);
        Tv_HomeFeed_Content = cardView.findViewById(R.id.Tv_HomeFeed_Content);
        Tv_HomeFeed_CreatedAt = cardView.findViewById(R.id.Tv_HomeFeed_CreatedAt);
        Tv_HomeFeed_Publisher = cardView.findViewById(R.id.Tv_HomeFeed_Publisher);
        Tv_HomeFeed_Favorite = cardView.findViewById(R.id.Tv_HomeFeed_Favorite);

        // RecyclerView에 표시할 posts 내용들 Dataset에서 가져와서 넣기
        String user = FeedDataset.get(position).getPublisher();
        String Title = FeedDataset.get(position).getTitle();
        String Content = FeedDataset.get(position).getContent();
        String CreatedAt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(FeedDataset.get(position).getCreatedAt());
        String url = FeedDataset.get(position).getUri();
        String favorite = FeedDataset.get(position).getFavorite().toString();
//        Double favorite = FeedDataset.get(position).getFavorite();
//        String Stfavorite = "0";

//        db.collection("posts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                String Stfavorite = documentSnapshot.getData().get("favorite").toString();
//                                Tv_HomeFeed_Favorite.setText(Stfavorite);
//                            }
//                        }
//                    }
//                });

//        db.collection("posts")
//                        .get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                                String Stfavorite = documentSnapshot.getData().get("favorite").toString();
//                                                Log.d("좋아요 수", Stfavorite);
//                                                Tv_HomeFeed_Favorite.setText(Stfavorite);
//                                            }
//                                        }
//                                    }
//                                });

        Tv_HomeFeed_Title.setText(Title);
        Tv_HomeFeed_Content.setText(Content);
        Tv_HomeFeed_CreatedAt.setText(CreatedAt);
        Tv_HomeFeed_Favorite.setText(favorite);

        Glide.with(cardView)
                .load(url)
                .override(800, 800)
                .apply(new RequestOptions().transform(new CenterCrop(),
                        new RoundedCorners(10)))
                .into(Iv_HomeFeed_Image);

//        USER_ID = "닉네임";
//        USER_PROFILE_IMG = "https://firebasestorage.googleapis.com/v0/b/finalprojectvegan.appspot.com/o/users%2Fbasic%2Fbasic_tomato.png?alt=media&token=5b496daf-cf05-4f84-98a8-bb9506caf206";
//        Tv_HomeFeed_Publisher.setText(USER_ID);
//        Glide.with(context)
//                .load(USER_PROFILE_IMG)
//                .into(Iv_HomeFeed_Profile);

        // posts RecyclerView에서 게시글 작성자 닉네임과 프로필 이미지를 표시하기 위해 추가로 users DB에서 데이터 가져오기
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (firebaseUser != null) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                    Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                                    if (documentSnapshot.getId().equals(user)) {

                                        USER_ID = documentSnapshot.getData().get("userId").toString();
                                        USER_PROFILE_IMG = documentSnapshot.getData().get("userProfileImg").toString();

                                        // 여기서 로그 찍어보면 다 나오는데
                                        Log.d("USER_ID", USER_ID);
                                        Log.d("USER_PROFILE_IMG", USER_PROFILE_IMG);

                                        // 여기서 출력하면 첫번째 뷰만 안나옵니다..
                                        Tv_HomeFeed_Publisher.setText(USER_ID);
                                        Glide.with(context)
                                                .load(USER_PROFILE_IMG)
                                                .skipMemoryCache(false)
                                                .into(Iv_HomeFeed_Profile);

                                    }
                                }
                            }

                        } else {
                            Log.d("ERROR", "HOMEFEED_USER DATA GET", task.getException());
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return FeedDataset.size();
    }

}
