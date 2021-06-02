package com.example.kw_mk;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.storageRef;

public class FragmentSellerOrder extends Fragment {

    ArrayList<orderListItem> orderList;
    orderListAdapter orderListAdapter;

    ArrayList<HashMap<String, String>> data = new ArrayList<>();
    RecyclerView orderMenu;

    DocumentReference orderStoref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seller_main_order, container, false);

        data = new ArrayList<>();
        orderMenu = rootView.findViewById(R.id.orderList);

        orderStoref = db.collection("Store_Info").document(App.LoginUserEmail);

        orderMenu.setHasFixedSize(true);

        init_List();

        return rootView;
    }

    void init_List() {
        orderList = new ArrayList<>();


        orderStoref.collection("RealTimeOrder").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (final QueryDocumentSnapshot document : task.getResult()) {
                                                       String name = document.get("주문자이름").toString();
                                                       String id = document.getId();


                                                       orderList.add(new orderListItem(name, id));
                                                       orderListAdapter.notifyDataSetChanged();
                                                   }
                                               }
                                           }
                                       }
                );

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        orderMenu.setLayoutManager(manager);
        orderListAdapter = new orderListAdapter(orderList, getContext());
        orderMenu.setAdapter(orderListAdapter);

    }
}

class orderListItem {
    String orderByName;
    String id;

    orderListItem(String name, String id) {
        this.orderByName = name;
        this.id = id;
    }

    public void setOrderByName(String orderByName) {
        this.orderByName = orderByName;
    }

    public String getOrderByName() {
        return orderByName;
    }
}

class orderListViewHolder extends RecyclerView.ViewHolder {
    TextView orderName;
    ListView orderMenuList;
    Button btn;


    orderListViewHolder(View itemView) {
        super(itemView);

        orderName = itemView.findViewById(R.id.orderByName);
        orderMenuList = itemView.findViewById(R.id.orderByMenu);
        btn = itemView.findViewById(R.id.orderCompliteBtn);

    }
}


class orderListAdapter extends RecyclerView.Adapter<orderListViewHolder> {

    Context context;
    private ArrayList<orderListItem> orderListItem = null;

    orderListAdapter(ArrayList<orderListItem> datalist, Context context) {
        this.orderListItem = datalist;
        this.context = context;
    }


    @NonNull
    @Override
    public orderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.seller_main_order_item, parent, false);
        orderListViewHolder orderListViewHolder = new orderListViewHolder(view);

        return orderListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final orderListViewHolder holder, final int position) {

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holder.orderName.setText(orderListItem.get(position).orderByName);
    }


    @Override
    public int getItemCount() {
        return orderListItem.size();
    }
}