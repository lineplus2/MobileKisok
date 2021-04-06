package com.example.kw_mk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentConsumerMypage extends Fragment {
    public class FragmentConsumerHome extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { // xml 생성 후 수정 필요
            ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.consumer_main_home, container, false);
            return rootView;

        }
    }
}
