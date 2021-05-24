package com.example.kw_mk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentSellerStore extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //if문
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seller_main_storemgmt, container, false);
        //rootView = (ViewGroup) inflater.inflate(R.layout.seller_main_fail, container, false);


        return rootView;
    }
}
