package com.example.kw_mk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class FragmentConsumerMypage extends Fragment {

    TextView name, email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.consumer_main_mypage, container, false);

        name = rootView.findViewById(R.id.mypage_name);
        email = rootView.findViewById(R.id.mypage_email);

        name.setText(App.LoginUserName);
        email.setText(App.LoginUserEmail);

        return rootView;

    }
}
