//package com.example.finalprojectvegan;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//
//import com.example.finalprojectvegan.Adapter.UserAdapter;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserSearchFragment extends Fragment {
//
//    // 인스턴스 선언 fragment_user_search에 만들어 두었던 view / view에 넣을 Adapter(UserAdapter) / 유저 리스트 / 검색창
//    private RecyclerView recyclerView;
//    private UserAdapter userAdapter;
//    private List<User> mUsers;
//
//    EditText search_bar;
//
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private String mParam1;
//    private String mParam2;
//
//    public UserSearchFragment() {
//        // Required empty public constructor
//    }
//
//    public static UserSearchFragment newInstance(String param1, String param2) {
//        UserSearchFragment fragment = new UserSearchFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        // return으로 되어있던 inflater를 view 객체에 담는다.
//        View view = inflater.inflate(R.layout.fragment_user_search, container, false);
//
//        // recyclerView 리소스 파일과 연결
//        recyclerView = view.findViewById(R.id.recycler_view);
//        // 뷰 크기 고정
//        recyclerView.setHasFixedSize(true);
//        // 1차원 목록 정렬 (2차원 목록은 GridLayoutManager)
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // 검색창 연결
//        search_bar = view.findViewById(R.id.search_bar);
//
//        // 유저리스트 배열 생성
//        mUsers = new ArrayList<>();
//
//        // Adapter 생성
//        userAdapter = new UserAdapter(getContext(), mUsers);
//
//        // recyclerView에 Adapter 꽂기
//        recyclerView.setAdapter(userAdapter);
//
//        // 유저리스트 읽기
//        readUsers();
//
//        // 검색창에 입력 시 반응
//        search_bar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                searchUsers(s.toString().toLowerCase());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        return view;
//    }
//
//    // 유저리스트 읽기 메서드 -> view에 보여줄 기본 셋팅
//    private void readUsers(){
//
//        // Users reference 생성
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//
//        // 데이터 변경
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (search_bar.getText().toString().equals("")){
//                    // 검색창이 비어 있으면
//                    // 유저리스트 clear
//                    mUsers.clear();
//                    // firebase Users 리스트 최대값 반복 -> user(Model)에 담기, 유저리스트에 넣기
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        User user = dataSnapshot.getValue(User.class);
//                        mUsers.add(user);
//                    }
//                    // Adapter 갱신
//                    userAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    // 검색창 입력 반응 메서드
//    private void searchUsers (String s){
//        // Query 객체 생성 -> 유저 검색이기 때문에 "Users" path에서 orderByChild 사용
//        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
//                .startAt(s)
//                .endAt(s+"\uf8ff");
//
//        // query 입력값 변경시
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mUsers.clear();
//                // query 진행 후의 firebase Users 리스트 최대값 반복 -> user(Model)에 담고, 유저리스트에 넣기
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    User user = dataSnapshot.getValue(User.class);
//                    mUsers.add(user);
//                }
//
//                // Adapter 갱신
//                userAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//}
//
////package com.example.finalprojectvegan;
////
////import android.os.Bundle;
////
////import androidx.annotation.NonNull;
////import androidx.fragment.app.Fragment;
////import androidx.recyclerview.widget.LinearLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////
////import android.text.Editable;
////import android.text.TextWatcher;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.EditText;
////
////import com.example.finalprojectvegan.Adapter.UserAdapter;
////import com.example.finalprojectvegan.Model.User;
////import com.google.firebase.database.DataSnapshot;
////import com.google.firebase.database.DatabaseError;
////import com.google.firebase.database.DatabaseReference;
////import com.google.firebase.database.FirebaseDatabase;
////import com.google.firebase.database.Query;
////import com.google.firebase.database.ValueEventListener;
////
////import java.util.ArrayList;
////import java.util.List;
////
////public class UserSearchFragment extends Fragment {
////
////    // 인스턴스 선언 fragment_user_search에 만들어 두었던 view / view에 넣을 Adapter(UserAdapter) / 유저 리스트 / 검색창
////    private RecyclerView recyclerView;
////    private UserAdapter userAdapter;
////    private List<User> mUsers;
////
////    EditText search_bar;
////
////    private static final String ARG_PARAM1 = "param1";
////    private static final String ARG_PARAM2 = "param2";
////
////    private String mParam1;
////    private String mParam2;
////
////    public UserSearchFragment() {
////        // Required empty public constructor
////    }
////
////    public static UserSearchFragment newInstance(String param1, String param2) {
////        UserSearchFragment fragment = new UserSearchFragment();
////        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
////        fragment.setArguments(args);
////        return fragment;
////    }
////
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
////        }
////    }
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        // Inflate the layout for this fragment
////
////        // return으로 되어있던 inflater를 view 객체에 담는다.
////        View view = inflater.inflate(R.layout.fragment_user_search, container, false);
////
////        // recyclerView 리소스 파일과 연결
////        recyclerView = view.findViewById(R.id.recycler_view);
////        // 뷰 크기 고정
////        recyclerView.setHasFixedSize(true);
////        // 1차원 목록 정렬 (2차원 목록은 GridLayoutManager)
////        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
////
////        // 검색창 연결
////        search_bar = view.findViewById(R.id.search_bar);
////
////        // 유저리스트 배열 생성
////        mUsers = new ArrayList<>();
////
////        // Adapter 생성
////        userAdapter = new UserAdapter(getContext(), mUsers);
////
////        // recyclerView에 Adapter 꽂기
////        recyclerView.setAdapter(userAdapter);
////
////        // 유저리스트 읽기
////        readUsers();
////
////        // 검색창에 입력 시 반응
////        search_bar.addTextChangedListener(new TextWatcher() {
////            @Override
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////
////            }
////
////            @Override
////            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                searchUsers(s.toString().toLowerCase());
////            }
////
////            @Override
////            public void afterTextChanged(Editable s) {
////
////            }
////        });
////
////        return view;
////    }
////
////    // 유저리스트 읽기 메서드 -> view에 보여줄 기본 셋팅
////    private void readUsers(){
////
////        // Users reference 생성
////        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
////
////        // 데이터 변경
////        reference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                if (search_bar.getText().toString().equals("")){
////                    // 검색창이 비어 있으면
////                    // 유저리스트 clear
////                    mUsers.clear();
////                    // firebase Users 리스트 최대값 반복 -> user(Model)에 담기, 유저리스트에 넣기
////                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
////                        User user = dataSnapshot.getValue(User.class);
////                        mUsers.add(user);
////                    }
////                    // Adapter 갱신
////                    userAdapter.notifyDataSetChanged();
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
////    }
////
////    // 검색창 입력 반응 메서드
////    private void searchUsers (String s){
////        // Query 객체 생성 -> 유저 검색이기 때문에 "Users" path에서 orderByChild 사용
////        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
////                .startAt(s)
////                .endAt(s+"\uf8ff");
////
////        // query 입력값 변경시
////        query.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                mUsers.clear();
////                // query 진행 후의 firebase Users 리스트 최대값 반복 -> user(Model)에 담고, 유저리스트에 넣기
////                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
////                    User user = dataSnapshot.getValue(User.class);
////                    mUsers.add(user);
////                }
////
////                // Adapter 갱신
////                userAdapter.notifyDataSetChanged();
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
////    }
////}