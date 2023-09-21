package com.example.finalprojectvegan.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalprojectvegan.CommentActivity;
import com.example.finalprojectvegan.EditFeedActivity;
import com.example.finalprojectvegan.Fcm.NotificationAPI;
import com.example.finalprojectvegan.Fcm.NotificationData;
import com.example.finalprojectvegan.Fcm.PushNotification;
import com.example.finalprojectvegan.Fcm.RetrofitInstance;
import com.example.finalprojectvegan.FragHomeFeed;
import com.example.finalprojectvegan.Model.FeedInfo;
import com.example.finalprojectvegan.Model.ReportInfo;
import com.example.finalprojectvegan.R;
import com.example.finalprojectvegan.SendMail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder>{
    private int myInt;
    private SharedPreferences pref;

//    private ArrayList<FeedInfo> FeedDataset = new ArrayList<>();
    private List<FeedInfo> feedInfoList = new ArrayList<>();
    private List<String> uidList = new ArrayList<>();
    private List<String> postId = new ArrayList<>();
    private Context context;

    private String FeedId, USER_ID, USER_PROFILE_IMG, post;
    private List<String> userIdList = new ArrayList<>();
    private String FeedPublisher, FeedTitle, FeedContent, FeedUri, blockUserID, FeedKey;
    private String postPublisher, token;
    private PushNotification pushNotification;

    private PopupMenu popupMenu;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private TextView feed_report;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Tv_HomeFeed_Title, Tv_HomeFeed_Content, Tv_HomeFeed_CreatedAt, Tv_HomeFeed_Publisher, Tv_HomeFeed_Favorite;
        private ImageView Iv_HomeFeed_Image, Iv_HomeFeed_Profile, Iv_HomeFeed_Favorite;
        public CardView cardView;
        public Button Btn_HomeFeedComment, Btn_HomeFeedEtc;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;

            // 변수 초기화

            db = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            // 피드에서 더보기 선택시
            Btn_HomeFeedEtc = view.findViewById(R.id.Btn_HomeFeedEtc);
            Btn_HomeFeedEtc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupMenu = new PopupMenu(context.getApplicationContext(), view);
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        // 피드 dataset으로부터 클릭된 view의 작성자(publisher)를 가져온다
                        FeedPublisher = feedInfoList.get(pos).getPublisher();
                        FeedId = feedInfoList.get(pos).getPostId();
                        FeedTitle = feedInfoList.get(pos).getTitle();
                        FeedContent = feedInfoList.get(pos).getContent();
                        FeedUri = feedInfoList.get(pos).getUri();
                        FeedKey = uidList.get(pos);
                        // 현재 사용자 Uid와 게시물 작성자가 같은 경우(즉, 로그인한 사용자의 게시물인 경우)
                        if (firebaseUser.getUid().equals(FeedPublisher)) {
                            // myfeed_menu.xml 메뉴 구현 -> 수정/삭제
                            popupMenu.getMenuInflater().inflate(R.menu.myfeed_menu, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    switch (menuItem.getItemId()) {
                                        // 게시물 수정 클릭시
                                        case R.id.feed_edit:
                                            // putExtra()를 통해 각각 게시물의 id, title, content를 EditFeedActivity로 보내준다.
                                            Log.d("EditInfo_Send", "Success");
                                            Intent intent = new Intent(context, EditFeedActivity.class);
                                            intent.putExtra("EditFeedId", FeedId);
                                            intent.putExtra("EditFeedTitle", FeedTitle);
                                            intent.putExtra("EditFeedContent", FeedContent);
                                            intent.putExtra("EditFeedUri", FeedUri);
                                            intent.putExtra("EditFeedKey", FeedKey);
                                            context.startActivity(intent);
                                            return true;

                                        // 게시물 삭제 클릭시
                                        case R.id.feed_delete:
                                            // Dialog를 통해서 진행여부를 확인받는다.
                                            AlertDialog.Builder DeleteDialogBuilder = new AlertDialog.Builder(context);
                                            DeleteDialogBuilder.setTitle("게시물 삭제");
                                            DeleteDialogBuilder.setMessage("정말 삭제하시겠습니까?");
                                            DeleteDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    // POSTS db에서 현재 FeedId에 해당하는 document를 삭제한다.

                                                    databaseReference.child("posts").child(uidList.get(pos)).removeValue();

                                                    db.collection("posts").document(FeedId)
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Log.d("Post Delete Success", "Success");
                                                                }
                                                            });
                                                    notifyItemRemoved(i);
                                                }
                                            });
                                            DeleteDialogBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                            DeleteDialogBuilder.show();
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            // 팝업 메뉴 보이기
                            popupMenu.show();
                        } else {
                            // 현재 사용자 작성 게시물이 아닌 경우 -> feed_menu.xml 표시 -> 신고/차단
                            popupMenu.getMenuInflater().inflate(R.menu.feed_menu, popupMenu.getMenu());

//                            MenuItem reportItem = popupMenu.getMenu().findItem(R.id.feed_report);
//                            MenuItem reportCancleItem = popupMenu.getMenu().findItem(R.id.feed_report_cancle);

                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    switch (menuItem.getItemId()) {
                                        // 게시물 신고 클릭시
                                        case R.id.feed_report:
                                            // Dialog를 통해 진행여부 확인.
                                            AlertDialog.Builder ReportDialogBuilder = new AlertDialog.Builder(context);
                                            ReportDialogBuilder.setTitle("게시물 신고");
                                            ReportDialogBuilder.setMessage("정말 신고하시겠습니까?");
                                            ReportDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if (uidList.size() != 0 ) {
                                                        onReportClicked(firebaseDatabase.getReference().child("posts").child(uidList.get(pos)), pos);
//                                                        feedInfoList.remove(pos);
//                                                        if (feedInfoList.get(pos).getReport() != null) {
//                                                            if (feedInfoList.get(pos).getReport().containsKey(firebaseUser.getUid())) {
//                                                                feedInfoList.remove(pos);
//                                                                notifyItemRemoved(pos);
//                                                            } else {
//
//                                                            }
//                                                        }
//                                                        else {
//
//                                                        }
//                                                        notifyItemRemoved(pos);
                                                    }
                                                }
                                            });
                                            ReportDialogBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            });
                                            ReportDialogBuilder.show();
                                            return true;

//                                        case R.id.feed_report_cancle:
//                                            // Dialog를 통해 진행여부 확인.
//                                            AlertDialog.Builder ReportCancleDialogBuilder = new AlertDialog.Builder(context);
//                                            ReportCancleDialogBuilder.setTitle("게시물 신고 취소");
//                                            ReportCancleDialogBuilder.setMessage("정말 신고 취소하시겠습니까?");
//                                            ReportCancleDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    if (uidList.size() != 0 ) {
//                                                        onReportClicked(firebaseDatabase.getReference().child("posts").child(uidList.get(pos)), pos);
//                                                    }
//                                                }
//                                            });
//                                            ReportCancleDialogBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                }
//                                            });
//                                            ReportCancleDialogBuilder.show();
//                                            return true;

                                        // 게시물 차단 클릭시
                                        case R.id.feed_block:

                                            Block(FeedPublisher);

                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            popupMenu.show();
                        }
                    }
                }
            });

            Iv_HomeFeed_Image = cardView.findViewById(R.id.Iv_HomeFeed_Image);
            Iv_HomeFeed_Profile = cardView.findViewById(R.id.Iv_HomeFeed_Profile);
            Iv_HomeFeed_Favorite = cardView.findViewById(R.id.Iv_HomeFeedFavorite);
            Tv_HomeFeed_Title = cardView.findViewById(R.id.Tv_HomeFeed_Title);
            Tv_HomeFeed_Content = cardView.findViewById(R.id.Tv_HomeFeed_Content);
            Tv_HomeFeed_CreatedAt = cardView.findViewById(R.id.Tv_HomeFeed_CreatedAt);
            Tv_HomeFeed_Publisher = cardView.findViewById(R.id.Tv_HomeFeed_Publisher);
            Tv_HomeFeed_Favorite = cardView.findViewById(R.id.Tv_HomeFeed_Favorite);

            // 피드에서 댓글 아이콘 클릭시
            Btn_HomeFeedComment = view.findViewById(R.id.Btn_HomeFeedComment);
            Btn_HomeFeedComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 클릭된 view 파악
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        // 클릭한 view 알아내고 (pos) 해당 view의 FeedId값을 가져온다.
                        FeedId = feedInfoList.get(pos).getPostId();
                        Log.d("DOCUMENTID_Send", FeedId);
                        // CommentActivity로 FeedId 값 전달 -> 받아서 클릭한 Feed에 해당하는 댓글들만 보여준다.
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("POSTSDocumentId", FeedId);
                        context.startActivity(intent);
                    }
                }
            });
        }

        void onBind(FeedInfo data) {

        // posts RecyclerView에서 게시글 작성자 닉네임과 프로필 이미지를 표시하기 위해 추가로 users DB에서 데이터 가져오기
            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (firebaseUser != null) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                    Log.d("HomeFeedSuccess", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                        if (documentSnapshot.getId().equals(data.getPublisher())) {
//                                            if (data.getReport().containsKey(firebaseUser.getUid())) {
//
//                                            } else {
                                                userIdList.add(documentSnapshot.getData().get("userId").toString());
                                                postPublisher = documentSnapshot.getId();
                                                USER_ID = documentSnapshot.getData().get("userId").toString();
                                                USER_PROFILE_IMG = documentSnapshot.getData().get("userProfileImg").toString();
                                                token = documentSnapshot.getData().get("userToken").toString();
//                                            }
                                        }
                                    }
                                }
                            Tv_HomeFeed_Publisher.setText(USER_ID);
                            Glide.with(cardView)
                                    .load(USER_PROFILE_IMG)
                                    .skipMemoryCache(false)
                                    .into(Iv_HomeFeed_Profile);

                            } else {
                                Log.d("ERROR", "HOMEFEED_USER DATA GET", task.getException());
                            }
                        }
                    });

                Tv_HomeFeed_Title.setText(data.getTitle());
                Tv_HomeFeed_Content.setText(data.getContent());
                Tv_HomeFeed_CreatedAt.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(data.getCreatedAt()));
                Tv_HomeFeed_Favorite.setText(String.valueOf(data.getFavoriteCount()));
                String url = data.getUri();
                Glide.with(cardView)
                        .load(url)
                        .override(800, 800)
                        .apply(new RequestOptions().transform(new CenterCrop(),
                                new RoundedCorners(10)))
                        .into(Iv_HomeFeed_Image);
        }
    }

     // 이전에 사용하던 내용 -> 좋아요 방식 변경하며 제거됨
    public HomeFeedAdapter(Context context, List<FeedInfo> feedInfoList, List<String> uidList, int myInt) {
        this.feedInfoList = feedInfoList;
        this.uidList = uidList;
        this.context = context;
        this.myInt = myInt;
    }

    @NonNull
    @Override
    public HomeFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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
    public void onBindViewHolder(@NonNull HomeFeedAdapter.ViewHolder holder, int position) {

//        CardView cardView = holder.cardView;

        holder.onBind(feedInfoList.get(position));
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (feedInfoList.get(position).getFavorites() != null) {
            if (feedInfoList.get(position).getFavorites().containsKey(firebaseUser.getUid())) {
                holder.Iv_HomeFeed_Favorite.setImageResource(R.drawable.thumb_up_on);
            } else {
                holder.Iv_HomeFeed_Favorite.setImageResource(R.drawable.thumb_up_off);
            }
        } else {
            holder.Iv_HomeFeed_Favorite.setImageResource(R.drawable.thumb_up_off);
        }

        holder.Iv_HomeFeed_Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("좋아요", "눌림");
                if (uidList.size() != 0 ) {
                    Log.d("좋아요", "성공?");
                    onFavoriteClicked(firebaseDatabase.getReference().child("posts").child(uidList.get(holder.getAbsoluteAdapterPosition())));
                }
            }
        });

    }

    private void sendCommentToFCM() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String currentUser = documentSnapshot.getId();
                                if(firebaseUser.getUid().equals(currentUser)) {
                                    String currentUserId = documentSnapshot.getData().get("userId").toString();
                                    NotificationData data = new NotificationData("채곡채곡", currentUserId + "님이 좋아요를 눌렀습니다.");
                                    pushNotification = new PushNotification(data, token);
                                    SendNotification(pushNotification);
                                }
                            }

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

    }

    public void SendNotification(PushNotification pushNotification) {

        NotificationAPI api = RetrofitInstance.getClient().create(NotificationAPI.class);
        retrofit2.Call<ResponseBody> responseBodyCall = api.sendNotification(pushNotification);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("SendNotification","성공");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("SendNotification","실패");
            }
        });

    }

    // 좋아요 클릭시
    private void onFavoriteClicked(DatabaseReference feedRef) {

        feedRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                FeedInfo feedInfo = currentData.getValue(FeedInfo.class);
                if (feedInfo == null) {
                    return Transaction.success(currentData);
                }
                if (feedInfo.getFavorites().containsKey(firebaseUser.getUid())) {
                    // Unstar the post and remove self from stars
                    feedInfo.setFavoriteCount(feedInfo.getFavoriteCount() - 1);
                    feedInfo.getFavorites().remove(firebaseUser.getUid());
                } else {
                    // Star the post and add self to stars
                    feedInfo.setFavoriteCount(feedInfo.getFavoriteCount() + 1);
                    feedInfo.getFavorites().put(firebaseUser.getUid(), true);
                    if(myInt == 1 && !postPublisher.equals(firebaseUser.getUid())) { // 알림수신동의가 되어있다면 and 내 게시물이 아니라면 푸시알림 전송
                        sendCommentToFCM();
                    }
                }
                // Set value and report transaction success
                currentData.setValue(feedInfo);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot currentData) {
                // Transaction completed
                Log.d("좋아요", "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    private void onReportClicked(DatabaseReference feedRef, int i) {
        feedRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                FeedInfo feedInfo = currentData.getValue(FeedInfo.class);
                ReportInfo reportInfo = new ReportInfo(feedInfo.getPostId(), null);
                if (feedInfo == null) {
                    return Transaction.success(currentData);
                }
                if (feedInfo.getReport().containsKey(firebaseUser.getUid())) {
//                    feedInfoList.remove()
                    feedInfo.setReportCount(feedInfo.getReportCount() - 1);
                    feedInfo.getReport().remove(firebaseUser.getUid());
                } else {
                    feedInfo.setReportCount(feedInfo.getReportCount() + 1);
                    feedInfo.getReport().put(firebaseUser.getUid(), true);
                    firebaseDatabase.getReference().child("report").child(firebaseUser.getUid()).child(feedInfo.getPostId()).setValue(reportInfo);
                }
                currentData.setValue(feedInfo);
                return Transaction.success(currentData);

            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return feedInfoList.size();
    }

    // 차단 버튼 클릭시 실행 함수
    public void Block(String BlockUserID) {

//        BlockInfo blockInfo = new BlockInfo(BlockUserID);
//        String blockUserID;

        db.collection("BLOCKS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if (documentSnapshot.getId().equals(firebaseUser.getUid())) {
                                    blockUserID = documentSnapshot.getData().get("BlockUserID").toString();
                                }
                            }
                        }
                    }
                });
        if (blockUserID.equals(BlockUserID)) {
            AlertDialog.Builder AlreadyBlockDialogBuilder = new AlertDialog.Builder(context);
            AlreadyBlockDialogBuilder.setTitle("사용자 차단");
            AlreadyBlockDialogBuilder.setMessage("이미 차단되어있습니다\n차단을 해제하시겠습니까?");
            AlreadyBlockDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlreadyBlockDialogBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlreadyBlockDialogBuilder.show();
        } else {

        }
    }
}
