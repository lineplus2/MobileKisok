package com.example.kw_mk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.imageOptions;

public class FragmentSellerHome extends Fragment {

    TextView store_id, store_content, store_order_clear, store_order_now, store_order_reservation, store_order_cancel;
    TextView store_num, store_representative, store_businessnum, store_location, store_category;
    ImageView store_img;
    Switch store_switch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //if문
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seller_main_storemgmt, container, false);
        store_img = rootView.findViewById(R.id.store_img);
        store_id = rootView.findViewById(R.id.store_id);
        store_content = rootView.findViewById(R.id.store_content);
        store_order_clear = rootView.findViewById(R.id.store_order_clear);
        store_order_now = rootView.findViewById(R.id.store_order_now);
        store_order_reservation = rootView.findViewById(R.id.store_order_reservation);
        store_order_cancel = rootView.findViewById(R.id.store_order_cancel);
        store_num = rootView.findViewById(R.id.store_num);
        store_representative = rootView.findViewById(R.id.store_representative);
        store_location = rootView.findViewById(R.id.store_location);
        store_businessnum = rootView.findViewById(R.id.store_businessnum);
        store_category = rootView.findViewById(R.id.store_category);

        //db에서 매장정보 가져오기

        init();


        return rootView;
    }

    void init() {
        // DB 가져오기
        Glide.with(getActivity()).load(App.StoreUri).apply(imageOptions).into(store_img);
        final DocumentReference doc = db.collection("Store_Info").document(App.LoginUserEmail);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    store_id.setText((String) document.get("가게이름"));
                    store_content.setText((String) document.get("가게소개"));
                    store_num.setText((String) document.get("전화번호"));
                    store_representative.setText((String) document.get("사업자명"));
                    store_businessnum.setText((String) document.get("사업자번호"));
                    store_category.setText((String) document.get("카테고리"));
                } else {
                }
            }
        });

    }
}
