package com.example.kw_mk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentSellerStore extends Fragment {

    TextView store_id, store_content, store_order_clear, store_order_now, store_order_reservation, store_order_cancel;
    TextView store_num, store_representative, store_businessnum;
    Switch store_switch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //if문
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seller_main_storemgmt, container, false);
        //rootView = (ViewGroup) inflater.inflate(R.layout.seller_main_fail, container, false);

        store_id = rootView.findViewById(R.id.store_id);
        store_content = rootView.findViewById(R.id.store_content);
        store_order_clear = rootView.findViewById(R.id.store_order_clear);
        store_order_now = rootView.findViewById(R.id.store_order_now);
        store_order_reservation = rootView.findViewById(R.id.store_order_reservation);
        store_order_cancel = rootView.findViewById(R.id.store_order_cancel);
        store_num = rootView.findViewById(R.id.store_num);
        store_representative = rootView.findViewById(R.id.store_representative);




        return rootView;
    }
}
