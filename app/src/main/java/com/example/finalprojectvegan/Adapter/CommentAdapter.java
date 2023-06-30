package com.example.finalprojectvegan.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectvegan.Model.UserInfo;
import com.example.finalprojectvegan.Model.WriteCommentInfo;
import com.example.finalprojectvegan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Comment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

//    private Context cContext;
    private ArrayList<WriteCommentInfo> cDataset;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//    private FirebaseUser firebaseUser;

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.onBind(cDataset.get(position));

        View view = holder.view;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String uid = firebaseUser.getUid();
                            String user = cDataset.get(holder.getAbsoluteAdapterPosition()).getPublisher();
                            Log.d("Comment-UID", uid);
                            Log.d("Comment-user", user);

                            ArrayList<UserInfo> CommentUserList = new ArrayList<>();
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("success", documentSnapshot.getId() + "=> " + documentSnapshot.getData());


                                    TextView commentPublisherName = view.findViewById(R.id.Tv_Comment_Publisher);

                                    if (documentSnapshot.getId().equals(user)) {
                                        if (documentSnapshot.getId().equals(uid)) {
                                            CommentUserList.add(new UserInfo(
                                                    documentSnapshot.getData().get("userID").toString(),
                                                    documentSnapshot.getData().get("userEmail").toString(),
                                                    documentSnapshot.getData().get("userPassword").toString()
                                            ));
                                            Log.d("USERNAME", documentSnapshot.getData().get("userID").toString());
//                                            TextView commentPublisherName = view.findViewById(R.id.commentPublisherName);
                                            commentPublisherName.setText(documentSnapshot.getData().get("userID").toString());
                                        }
                                        commentPublisherName.setTextColor(Color.parseColor("#ff6f60"));
                                    }
//                                    CommentUserList.add(new UserInfo(
//                                            documentSnapshot.getData().get("userID").toString(),
//                                            documentSnapshot.getData().get("userEmail").toString(),
//                                            documentSnapshot.getData().get("userPassword").toString()
//                                    ));
//                                    TextView commentPublisherName = view.findViewById(R.id.commentPublisherName);
//                                    commentPublisherName.setText(documentSnapshot.getData().get("userID").toString());

//                                    if (documentSnapshot.getId().equals(user)) {
//                                        // 글씨 색상 변경 넣어주기 (사용자 본인 댓글 표시)
//                                        commentPublisherName.setText(documentSnapshot.getData().get("userID").toString());
//                                    }
                                }
                            }
                        }
                    }
                });

//        TextView commentPublisherName = view.findViewById(R.id.commentPublisherName);
//        commentPublisherName.setText(cDataset.get(position).getPublisher());

        TextView commentTextView = view.findViewById(R.id.Tv_Comment);
        commentTextView.setText(cDataset.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return cDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;

        public TextView commentTextView;
        public TextView commentPublisherName;
        public ImageView commentPublisherImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;

            commentPublisherImage = (ImageView) itemView.findViewById(R.id.Iv_Comment_Profile);
            commentPublisherName = (TextView) itemView.findViewById(R.id.Tv_Comment_Publisher);
            commentTextView = (TextView) itemView.findViewById(R.id.Tv_Comment);

        }
    }

    public CommentAdapter(ArrayList<WriteCommentInfo> commentDataset) {
//        this.cDataset = list;
        cDataset = commentDataset;
//        this.context = context;
        notifyDataSetChanged();;
    }
}
