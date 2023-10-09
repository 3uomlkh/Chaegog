//package com.example.finalprojectvegan;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.finalprojectvegan.Model.UserInfo;
//import com.example.finalprojectvegan.Model.WritePostInfo;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.lang.reflect.Array;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Locale;
//
//public class MyfeedAdapter extends RecyclerView.Adapter<MyfeedAdapter.ViewHolder>{
//
//    private ArrayList<WritePostInfo> mDataset;
//    private Context context;
////    private ArrayList<String> arrayList;
////
//    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
////
//    ImageView myfeed_item_imageView;
////    ImageView imageView_profile2;
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        public CardView cardView;
//
//        public ViewHolder(CardView view) {
//            super(view);
//            cardView = view;
//        }
//    }
//
//    public MyfeedAdapter(Context context, ArrayList<WritePostInfo> myDataset) {
//        mDataset = myDataset;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public MyfeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        Context context = parent.getContext();
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        LayoutInflater inflater =
//        CardView cardView = (CardView)inflater.inflate(R.layout.myfeed_item, parent, false);
//        myfeed_item_imageView = cardView.findViewById(R.id.mypage_item_imageView);
//
//        ViewHolder viewHolder = new ViewHolder(cardView);
//
//        return viewHolder;
//
////        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_item, parent, false);
////        ViewHolder viewHolder = new ViewHolder(cardView);
////        myfeed_item_imageView = cardView.findViewById(R.id.mypage_item_imageView);
////
////        cardView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////            }
////        });
//
////        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        CardView cardView = holder.cardView;
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("user")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            if (firebaseUser != null) {
//
//                                String uid = firebaseUser.getUid();
//                                String user = mDataset.get(holder.getAdapterPosition()).getPublisher();
//
//                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                    Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
//
////                                    TextView publisherTextView = cardView.findViewById(R.id.myfeed_item_publisher);
//                                    ImageView imageView_profile2 = cardView.findViewById(R.id.imageView_profile2);
//
//                                    if (documentSnapshot.getId().equals(user)) {
//                                        if (documentSnapshot.getId().equals(uid)) {
//                                            ArrayList<UserInfo> postUserList = new ArrayList<>();
//
//                                            postUserList.add(new UserInfo(
//                                                    documentSnapshot.getData().get("userID").toString(),
//                                                    documentSnapshot.getData().get("userEmail").toString(),
//                                                    documentSnapshot.getData().get("userPassword").toString()));
//
//                                            cardView.setVisibility(View.VISIBLE);
//                                            ViewGroup.LayoutParams params = cardView.getLayoutParams();
//                                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                                            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                                            cardView.setLayoutParams(params);
//
////                                            publisherTextView.setText(documentSnapshot.getData().get("userID").toString());
//
//                                            TextView titleTextView = cardView.findViewById(R.id.myfeed_item_title);
//                                            titleTextView.setText(mDataset.get(holder.getAdapterPosition()).getTitle());
//
//                                            TextView createdAtTextView = cardView.findViewById(R.id.myfeed_item_createdAt);
//                                            createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(holder.getAdapterPosition()).getCreatedAt()));
//
//                                            TextView contentsTextView = cardView.findViewById(R.id.myfeed_item_contents);
//                                            contentsTextView.setText(mDataset.get(holder.getAdapterPosition()).getContents());
//
//                                            String url = mDataset.get(holder.getAdapterPosition()).getImagePath();
//
//                                            Glide.with(cardView).load(url).override(800, 800).into(myfeed_item_imageView);
//
////                                        loadImage(uid);
//                                        } else {
//                                            cardView.setVisibility(View.GONE);
//                                            ViewGroup.LayoutParams params = cardView.getLayoutParams();
//                                            params.height = 0;
//                                            params.width = 0;
//                                            cardView.setLayoutParams(params);
//                                        }
//                                    }
//                                }
//                            }
//
//                        } else {
//                            Log.d("error", "Error getting documents", task.getException());
//                        }
//                    }
//                });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDataset.size();
//    }
//}
