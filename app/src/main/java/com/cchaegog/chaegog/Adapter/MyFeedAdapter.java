package com.cchaegog.chaegog.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cchaegog.chaegog.CommentActivity;
import com.cchaegog.chaegog.EditFeedActivity;
import com.cchaegog.chaegog.Model.FeedInfo;
import com.cchaegog.chaegog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.ViewHolder>{

    private Context context;
//    private ArrayList<FeedInfo> MyFeedDataset;
    private List<FeedInfo> feedInfoList = new ArrayList<>();
    private List<String> uidList = new ArrayList<>();
    private String MyFeedId, Uid, User;
    private String FeedId, FeedPublisher, FeedTitle, FeedContent, FeedUri, blockUserID, FeedKey;
    private Button Btn_MyFeedComment, Btn_MyFeedEtc;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;

    private PopupMenu popupMenu;

    private ImageView Iv_MyFeed_Item, Iv_MyFeed_Profile;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            Btn_MyFeedComment = view.findViewById(R.id.Btn_MyFeedComment);
            Btn_MyFeedComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 클릭된 view 파악
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
//                        MyFeedId = MyFeedDataset.get(pos).getPostId();
                        MyFeedId = feedInfoList.get(pos).getPostId();
                        Log.d("MyFeedDOCUMENTID_Send", MyFeedId);
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("POSTSDocumentId", MyFeedId);
                        context.startActivity(intent);
                    }
                }
            });

            Btn_MyFeedEtc = view.findViewById(R.id.Btn_MyFeedEtc);
            Btn_MyFeedEtc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupMenu = new PopupMenu(context.getApplicationContext(), view);
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        FeedId = feedInfoList.get(pos).getPostId();
                        FeedTitle = feedInfoList.get(pos).getTitle();
                        FeedContent = feedInfoList.get(pos).getContent();
                        FeedUri = feedInfoList.get(pos).getUri();
                        FeedKey = uidList.get(pos);

                        popupMenu.getMenuInflater().inflate(R.menu.myfeed_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
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
                        popupMenu.show();
                    }
                }
            });
        }
    }

    public MyFeedAdapter(Context context, List<FeedInfo> feedInfoList, List<String> uidList) {
        this.feedInfoList = feedInfoList;
        this.uidList = uidList;
        this.context = context;
    }
//    public MyFeedAdapter(Context context, ArrayList<FeedInfo> myDataset) {
//        MyFeedDataset = myDataset;
//        this.context = context;
//    }

    @NonNull
    @Override
    public MyFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.myfeed_item, parent, false);
//        ViewHolder viewHolder = new ViewHolder(cardView);
//        Iv_MyFeed_Item = cardView.findViewById(R.id.mypage_item_imageView);
//
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        return viewHolder;
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardView cardView = holder.cardView;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Iv_MyFeed_Item = cardView.findViewById(R.id.Iv_MyFeed_item_Image);
        Iv_MyFeed_Profile = cardView.findViewById(R.id.Iv_MyFeed_Profile);


        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (firebaseUser != null) {

                                Uid = firebaseUser.getUid();
                                User = feedInfoList.get(holder.getAbsoluteAdapterPosition()).getPublisher();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("MYFEEDADAPTER SUCCESS", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                                    // document ID가 피드 작성자와 같고, 현재 로그인한 유저와도 같은 경우
                                    if (documentSnapshot.getId().equals(User)) {
                                        if ( documentSnapshot.getId().equals(Uid)) {

                                            cardView.setVisibility(View.VISIBLE);
                                            ViewGroup.LayoutParams params = cardView.getLayoutParams();
                                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                            cardView.setLayoutParams(params);

                                            TextView titleTextView = cardView.findViewById(R.id.Tv_MyFeed_item_Title);
                                            titleTextView.setText(feedInfoList.get(holder.getAdapterPosition()).getTitle());

                                            TextView createdAtTextView = cardView.findViewById(R.id.Tv_MyFeed_item_CreatedAt);
                                            createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(feedInfoList.get(holder.getAdapterPosition()).getCreatedAt()));

                                            TextView contentsTextView =cardView.findViewById(R.id.Tv_MyFeed_item_Contents);
                                            contentsTextView.setText(feedInfoList.get(holder.getAdapterPosition()).getContent());

                                            String url = feedInfoList.get(holder.getAdapterPosition()).getUri();

//                                            Double favorite = MyFeedDataset.get(holder.getAbsoluteAdapterPosition()).getFavorite();

                                            Glide.with(cardView)
                                                    .load(url)
                                                    .into(Iv_MyFeed_Item);

                                        } else {
                                            cardView.setVisibility(View.GONE);
                                            ViewGroup.LayoutParams params = cardView.getLayoutParams();
                                            params.height = 0;
                                            params.width = 0;
                                            cardView.setLayoutParams(params);
                                        }
                                    }
                                }
                            }

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return feedInfoList.size();
    }
}
