package com.example.finalprojectvegan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
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
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalprojectvegan.CommentActivity;
import com.example.finalprojectvegan.Model.FeedFavorite;
import com.example.finalprojectvegan.Model.FeedInfo;
import com.example.finalprojectvegan.MypageActivity;
import com.example.finalprojectvegan.ProgressDialog;
import com.example.finalprojectvegan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomefeedAdapter extends RecyclerView.Adapter<HomefeedAdapter.ViewHolder>{

    private ArrayList<FeedInfo> FeedDataset;
    private Context context;
    private TextView Tv_HomeFeed_Title, Tv_HomeFeed_Content, Tv_HomeFeed_CreatedAt, Tv_HomeFeed_Publisher, Tv_HomeFeed_Favorite;
    private ImageView Iv_HomeFeed_Image, Iv_HomeFeed_Profile, Iv_HomeFeed_Favorite;
    private String FeedId, USER_ID, USER_PROFILE_IMG;
    private ArrayList<String> favoriteUserList;
    private ArrayList<String> favoriteCount;
    private ArrayList<String> favoriteUser;
    private ArrayList<FeedFavorite> FeedFavoritesDataset;
    private List<String> uidList;
    private String count;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public Button Btn_HomeFeedComment, Btn_HomeFeedEtc;
        public CheckBox Cb_HomeFeedFavorite;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;

            // 변수 초기화
            db = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            favoriteUserList = new ArrayList<>();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("posts");

            // 좋아요 버튼 클릭시
//            Cb_HomeFeedFavorite = view.findViewById(R.id.Cb_HomeFeedFavorite);
//            Cb_HomeFeedFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                    // 클릭된 view 파악
//                    int pos = getAbsoluteAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION) {
//
//                        // 클릭한 view 알아내고 (pos) 해당 view의 FeedId값을 가져온다.
//                        FeedId = FeedDataset.get(pos).getPostId();
//                        Log.d("DOCUMENTID_Send", FeedId);
//
//                    }
//                    if (checked) {
//
//                    }
//                    db.collection("posts").document(FeedId).collection("favorite")
//                            .get()
//                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                            String favoriteOnUser = documentSnapshot.getData().get("publisher").toString();
//                                            // 좋아요 컬렉션에 현재 사용자 Uid가 있으면 삭제
//                                            if (favoriteOnUser.equals(firebaseUser.getUid())) {
//                                                compoundButton.isChecked();
//                                                db.collection("posts").document(FeedId).collection("favorite").document(favoriteOnUser)
//                                                        .delete()
//                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void unused) {
//
//                                                            }
//                                                        })
//                                                        .addOnFailureListener(new OnFailureListener() {
//                                                            @Override
//                                                            public void onFailure(@NonNull Exception e) {
//
//                                                            }
//                                                        });
//                                                break;
//                                            } else {
//                                                // 없으면 추가
//                                                Map<String, Object> favorite = new HashMap<>();
//                                                favorite.put("userId", firebaseUser.getUid());
//                                                DocumentReference postDoc = db.collection("posts").document(FeedId);
//                                                DocumentReference favoriteDoc = postDoc.collection("favorite").document(firebaseUser.getUid());
//                                                favoriteDoc.set(favorite);
//                                                postDoc.update("favorite", FieldValue.increment(1));
//                                            }
//                                        }
//                                    }
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//
//                                }
//                            });
//                    if (checked) {
//                        Cb_HomeFeedFavorite.setEnabled(true);
//                        Log.d("CHECK", "눌림");
//
//                        favoriteUserList.add(firebaseUser.getUid());
//                        count = String.valueOf(favoriteUserList.size());
//
////                        FeedFavorite feedFavorite = new FeedFavorite(count, favoriteUserList);
////                        databaseReference.child(FeedId).child("favorite").setValue(feedFavorite);
////                        db.collection("posts").document(FeedId)
////                                .update("favorite", FieldValue.increment(1))
////                                .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                    @Override
////                                    public void onSuccess(Void unused) {
////                                        Log.d("좋아요", "성공");
////                                    }
////                                });
//                    } else {
//                        Log.d("CHECK", "해제");
//                        favoriteUserList.remove(firebaseUser.getUid());
//                        count = String.valueOf(favoriteUserList.size());
////                        db.collection("posts").document(FeedId)
////                                .update("favorite", FieldValue.increment(-1))
////                                .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                    @Override
////                                    public void onSuccess(Void unused) {
////                                        Log.d("좋아요", "성공");
////                                    }
////                                });
//                    }
//                }
//            });

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

    public HomefeedAdapter(Context context, ArrayList<FeedInfo> myDataset, ArrayList<FeedFavorite> myFavoriteDataset) {
        FeedDataset = myDataset;
        FeedFavoritesDataset = myFavoriteDataset;
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

        List<String> uidList = new ArrayList<String>() {

        };

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Iv_HomeFeed_Image = cardView.findViewById(R.id.Iv_HomeFeed_Image);
        Iv_HomeFeed_Profile = cardView.findViewById(R.id.Iv_HomeFeed_Profile);
        Iv_HomeFeed_Favorite = cardView.findViewById(R.id.Iv_HomeFeedFavorite);
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
        String postId = FeedDataset.get(position).getPostId();
//        if (FeedFavoritesDataset.size() != 0) {
//            String favoriteCount = Integer.toString(FeedFavoritesDataset.get(position).getFavoriteCount());
//        }
//        String favoriteCount = Integer.toString(FeedFavoritesDataset.get(position).getFavoriteCount());
//        if (position == 0) {
//            notifyItemChanged(position);
//        }


        if (FeedFavoritesDataset.size() != 0) {
            if (FeedFavoritesDataset.get(position).getFavoriteUser().containsKey(firebaseUser.getUid())) {
                Iv_HomeFeed_Favorite.setImageResource(R.drawable.favorite_off);
            } else {
                Iv_HomeFeed_Favorite.setImageResource(R.drawable.favorite_on);
            }
        } else {
            Log.d("error", "비어있음");
        }
//        if (FeedFavoritesDataset.get(position).getFavoriteUser().containsKey(firebaseUser.getUid())) {
//            Iv_HomeFeed_Favorite.setImageResource(R.drawable.favorite_off);
//        } else {
//            Iv_HomeFeed_Favorite.setImageResource(R.drawable.favorite_on);
//        }
        Iv_HomeFeed_Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onFavoriteClicked(firebaseDatabase.getReference().child("posts").child(postId).child(uidList.get(position)));
            }
        });

//        if (FeedId == postId) {
//            firebaseDatabase = FirebaseDatabase.getInstance();
//            databaseReference = firebaseDatabase.getReference("posts").child(FeedId).child("favorite").child("count");
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
////                    Log.d("좋아요", dataSnapshot.getValue().toString());
////                }
////                Log.d("좋아요", snapshot.getValue(String.class));
//                    String favoriteCount = snapshot.getValue(String.class);
//                    Log.d("좋아요", favoriteCount);
//                    Tv_HomeFeed_Favorite.setText(favoriteCount);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }

//        if (FeedId == postId) {
//
//            firebaseDatabase = FirebaseDatabase.getInstance();
//            databaseReference = firebaseDatabase.getReference("posts");
//
//            favoriteCount = new ArrayList<>();
//            favoriteUser = new ArrayList<>();
//
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                    Long favoriteCount = snapshot.child(FeedId).child("favorite").child("count").getValue(Long.class);
//                    Log.d("좋아요 수", favoriteCount.toString());
//                    Tv_HomeFeed_Favorite.setText(favoriteCount.toString());
//
////                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//////                    Log.d("favorite", dataSnapshot.child("favoriteUserList").getValue().toString());
////                        favoriteCount.add((String) dataSnapshot.child("count").getValue());
////                        favoriteUser.add((String) dataSnapshot.child("favoriteUserList").getValue());
//////                    Tv_HomeFeed_Favorite.setText(favoriteCount.get(position));
////                    }
////                    Tv_HomeFeed_Favorite.setText(favoriteCount.get(position));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }



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

        if (FeedFavoritesDataset.size() != 0) {
            String favoriteCount = Integer.toString(FeedFavoritesDataset.get(position).getFavoriteCount());
            Log.d("좋아요 수", favoriteCount);
            Tv_HomeFeed_Favorite.setText(favoriteCount);

        }

        Tv_HomeFeed_Title.setText(Title);
        Tv_HomeFeed_Content.setText(Content);
        Tv_HomeFeed_CreatedAt.setText(CreatedAt);
//        Tv_HomeFeed_Favorite.setText(favoriteCount);

        Log.d("url", url);

        Glide.with(cardView)
                .load(url)
                .override(800, 800)
                .apply(new RequestOptions().transform(new CenterCrop(),
                        new RoundedCorners(10)))
                .into(Iv_HomeFeed_Image);

    }

    private void onFavoriteClicked(DatabaseReference favoriteRef) {
        favoriteRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                FeedFavorite feedFavorite = currentData.getValue(FeedFavorite.class);
                String uidKey = currentData.getKey();
                uidList.add(uidKey);
                if (feedFavorite == null) {
                    return Transaction.success(currentData);
                }
                if (feedFavorite.getFavoriteUser().containsKey(firebaseUser.getUid())) {
                    feedFavorite.setFavoriteCount(feedFavorite.getFavoriteCount() - 1);
                    feedFavorite.getFavoriteUser().remove(firebaseUser.getUid());
                } else {
                    feedFavorite.setFavoriteCount(feedFavorite.getFavoriteCount() + 1);
                    feedFavorite.getFavoriteUser().put(firebaseUser.getUid(), true);
                }
                currentData.setValue(feedFavorite);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return FeedDataset.size();
    }


//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }


}
