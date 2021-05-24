package com.example.kw_mk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import static com.example.kw_mk.LoginActivity.pref;

public class FragmentConsumerMypage extends Fragment {


    public static Context conContext;

    Button myInfoModify, logout;
    TextView myName, myEmail;

    public static ImageView profileImage;

    DocumentReference docRef;
    StorageReference stoRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.consumer_main_mypage, container, false);

        final FirebaseUser user = App.mAuth.getCurrentUser();

        conContext = container.getContext();
        docRef = App.db.collection("User_Info").document(user.getEmail());

        logout = rootView.findViewById(R.id.logout_btn);
        myInfoModify = rootView.findViewById(R.id.myInfoModify);
        myName = rootView.findViewById(R.id.mypage_name);
        myEmail = rootView.findViewById(R.id.mypage_email);
        profileImage = rootView.findViewById(R.id.myProfileImage);

        if (user != null) {
            String email = user.getEmail();
            myEmail.setText(email);
            myName.setText(App.LoginUserName);
        } else {
            Toast.makeText(getActivity(), "유저 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        Glide.with(getActivity()).load(App.LoginUserUri).into(profileImage);

        myInfoModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MypageModifyActivity.class);
                startActivity(intent);
            }
        });

        // 로그아웃 버튼
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return rootView;

    }

    // 로그아웃
    public void logout() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ID", "");
        editor.putString("PWD", "");

        editor.commit();
        getActivity().finish();
    }
}