//package com.example.finalprojectvegan.Adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.FragmentActivity;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.finalprojectvegan.R;
//import com.example.finalprojectvegan.RoadingFragment;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
//
//    private Context mContext;
//    private List<User> mUsers;
//
//    private FirebaseUser firebaseUser;
//
//    public UserAdapter(Context mContext, List<User> mUsers) {
//        // 생성자 생성
//        this.mContext = mContext;
//        this.mUsers = mUsers;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
//        return new UserAdapter.ViewHolder(view);
//    }
//
//
//    // 데이터 바인딩하기
//    @Override
//    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
//
//        // firebaseUser에 최근유저 할당
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        // User(model) 객체 만들기 -> 목록에 보여질 user
//        // 객체화한 배역 mUsers에서 position으로 데이터 인덱스 값을 받아와 user에 담는다.
//        // RecyclerView의 경우 객체 사용을 최소화하여 재활용 하기 때문에 매 데이터마다 다시 바인딩이 필요하다.
//        final User user = mUsers.get(position);
//
//        // Viewholder로 사용자 정보 입력
//        // follow 버튼 보이도록 셋팅
//        holder.btn_follow.setVisibility(View.VISIBLE);
//        holder.userName.setText(user.getUserName());
//        holder.userEmail.setText(user.getUserEmail());
//        Glide.with(mContext).load(user.getImageurl()).into(holder.image_profile);
//        isFollowing(user.getId(), holder.btn_follow);
//
//        // 유저 본인이 목록에 있으면 버튼 안보이도록 셋팅
//        if (user.getId().equals(firebaseUser.getUid())) {
//            holder.btn_follow.setVisibility(View.GONE);
//        }
//
//        // View를 클릭했을 경우
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Editor를 사용해서 내부저장소에 사용자 정보를 담아 넘긴다. (클릭된 사용자의 정보)
////                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
////                editor.putString("profileid", user.getId());
////                editor.apply();
////
////                // FragmentActivity로 Context를 형 변환해서 FragMypage로 replace한다.
//                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,
//                        new RoadingFragment()).commit();
//
//
//            }
//        });
//
//        // follow 버튼 클릭시
//        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // 버튼의 text가 follow이면 Database에 setValue한다.
//                if (holder.btn_follow.getText().toString().equals("follow")) {
//                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
//                            .child("following").child(user.getId()).setValue(true);
//                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
//                            .child("followers").child(firebaseUser.getUid()).setValue(true);
//                } else {
//                    // 이미 following이면 Database에서 removeValuegksek.
//                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
//                            .child("following").child(user.getId()).removeValue();
//                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
//                            .child("followers").child(firebaseUser.getUid()).removeValue();
//                }
//            }
//        });
//    }
//
//    // 데이터의 크기를 return한다.
//    @Override
//    public int getItemCount() {
//        return mUsers.size();
//    }
//
//
//    // 각각의 변수들을 리소스 파일과 연결 시켜주기 위한 ViewHolder
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView userName;
//        public TextView userEmail;
//        public CircleImageView image_profile;
//        public Button btn_follow;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            userName = itemView.findViewById(R.id.username);
//            userEmail = itemView.findViewById(R.id.useremail);
//            image_profile = itemView.findViewById(R.id.image_profile);
//            btn_follow = itemView.findViewById(R.id.btn_follow);
//        }
//    }
//
//    // 버튼의 text를 변경하는 메서드
//    private void isFollowing(String userid, Button button) {
//        // reference 객체 생성
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
//                .child("Follow").child(firebaseUser.getUid()).child("following");
//        reference.addValueEventListener(new ValueEventListener() {
//
//            // reference 값이 변경될때
//            // -> 초기에는 database에 유저가 following 하는 다른 사용자가 없으므로 follow로 기본 설정.
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child(userid).exists()) {
//                    // holder에 표시된 user의 id가 존재하면 "following"으로 text 변경
//                    button.setText("following");
//                } else {
//                    // 존재하지 않으면 "follow"으로 text 변경
//                    button.setText("follow");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//}