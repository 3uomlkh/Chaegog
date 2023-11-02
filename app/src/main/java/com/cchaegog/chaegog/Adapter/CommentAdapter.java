package com.cchaegog.chaegog.Adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cchaegog.chaegog.Model.WriteCommentInfo;
import com.cchaegog.chaegog.R;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<WriteCommentInfo> cDataset;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    public CommentAdapter(ArrayList<WriteCommentInfo> commentDataset) {
        cDataset = commentDataset;
        notifyDataSetChanged();;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        View view = holder.view;

        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("CommentAdapter Success", documentSnapshot.getId() + " ==> " + documentSnapshot.getData());
                                    Log.d("CommentAdapter Success", "FirebaseUser -> " + firebaseUser.getUid());
                                    // CommentDataset에 들어있는 Publisher 이름과 users documentSnapshot으로부터 받아온 getId 값 일치하면
                                    // CommentPublisher TV에 documentSnapshot으로부터 받아온 데이터 속 userId 값 배치하기
                                    if (documentSnapshot.getId().equals(cDataset.get(holder.getAbsoluteAdapterPosition()).getPublisher())) {
                                        TextView Tv_Comment_Publisher = view.findViewById(R.id.Tv_Comment_Publisher);
                                        ImageView Iv_Comment_Profile = view.findViewById(R.id.Iv_Comment_Profile);

                                        Tv_Comment_Publisher.setText(documentSnapshot.getData().get("userId").toString());
                                        // documentSnapshot의 getId 값이 현재 로그인한 유저의 Uid값과 동일하다면
                                        // 댓글 닉네임 색상 변경해주기
                                        if (documentSnapshot.getId().equals(firebaseUser.getUid())) {
                                            Tv_Comment_Publisher.setTextColor(Color.parseColor("#ff6f60"));;
                                        } else {
                                            Tv_Comment_Publisher.setTextColor(Color.parseColor("#000000"));
                                        }

                                        Glide.with(view)
                                                .load(documentSnapshot.getData().get("userProfileImg").toString())
                                                .into(Iv_Comment_Profile);
                                    }
                                }
                            }
                        }
                    }
                });

        // 댓글 내용 표시
        TextView Comment = view.findViewById(R.id.Tv_Comment);
        Comment.setText(cDataset.get(position).getComment());

        // 댓글 작성 시간 표시
        TextView CommentCreatedAt = view.findViewById(R.id.Tv_Comment_CreatedAt);
        CommentCreatedAt.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cDataset.get(position).getCreatedAt()));

    }

    @Override
    public int getItemCount() {
        return cDataset.size();
    }

}
