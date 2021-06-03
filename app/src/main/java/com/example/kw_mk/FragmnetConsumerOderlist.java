package com.example.kw_mk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.kw_mk.App.db;

public class FragmnetConsumerOderlist extends Fragment {

    RecyclerView re;
    ArrayList<RecyclerItem> mList = new ArrayList<>();
    MyAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.consumer_main_record, container, false);

        Context context = container.getContext();
        re = (RecyclerView) rootView.findViewById(R.id.recyclerViewTest);

        InitializeData();

        return rootView;
    }

    public void InitializeData() {
        mList = new ArrayList<RecyclerItem>();

        db.collection("User_Info").document(App.LoginUserEmail).collection("OrderList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String name = document.get("주문한가게이름").toString();
                            String menu = document.get("주문목록").toString();
                            String price = document.get("결제금액").toString();
                            String needs = document.get("요청사항").toString();
                            String email = document.get("주문한가게이메일").toString();
                            String id = document.getId();

                            mList.add(new RecyclerItem(name, menu, id, price, needs, email));
                            mAdapter.notifyDataSetChanged();

                        }
                    }
                });

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        re.setLayoutManager(manager); // LayoutManager 등록
        mAdapter = new MyAdapter(mList);
        re.setAdapter(mAdapter);  // Adapter 등록

    }


}

class RecyclerItem {
    private String storeName;
    private String menuList;
    String price;
    String needs;
    private String Id;
    private String email;

    public RecyclerItem(String storeName, String menuList, String id, String price, String needs, String email) {
        this.storeName = storeName;
        this.menuList = menuList;
        this.price = price;
        this.Id = id;
        this.needs = needs;
        this.email = email;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setMenuList(String menuList) {
        this.menuList = menuList;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public String getMenuList() {
        return this.menuList;
    }

    public String getId() {
        return Id;
    }

    public String getPrice() {
        return price;
    }

    public String getNeeds() {
        return needs;
    }

    public String getEmail() {
        return email;
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    TextView storeName;
    TextView menuList;
    Button btn_reviewWrite;
    Button btn_details;

    ViewHolder(View itemView) {
        super(itemView);

        storeName = itemView.findViewById(R.id.record_storeName);
        menuList = itemView.findViewById(R.id.record_MenuList);
        btn_reviewWrite = (Button) itemView.findViewById(R.id.reviewWrite);
        btn_details = (Button) itemView.findViewById(R.id.details);

    }
}

class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

    Context context;
    private ArrayList<RecyclerItem> myDataList = null;

    MyAdapter(ArrayList<RecyclerItem> dataList) {
        myDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.consumer_main_record_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.storeName.setText(myDataList.get(position).getStoreName());
        viewHolder.menuList.setText(myDataList.get(position).getMenuList());
        viewHolder.btn_reviewWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, record_reviewActivity.class);
                intent.putExtra("storeName", myDataList.get(position).getStoreName());
                intent.putExtra("payPrice", myDataList.get(position).getPrice());
                intent.putExtra("email", myDataList.get(position).getEmail());
                intent.putExtra("menuList", myDataList.get(position).getMenuList());
                context.startActivity(intent);
            }
        });

        viewHolder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context acontext = v.getContext();
                Intent intent = new Intent(acontext, record_contentActivity.class);
                intent.putExtra("storeName", myDataList.get(position).getStoreName());
                intent.putExtra("payPrice", myDataList.get(position).getPrice());
                intent.putExtra("needs", myDataList.get(position).getNeeds());
                intent.putExtra("menuList", myDataList.get(position).getMenuList());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }
}
