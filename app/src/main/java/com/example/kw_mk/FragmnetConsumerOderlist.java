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

import java.util.ArrayList;

public class FragmnetConsumerOderlist extends Fragment {

    RecyclerView re = null;
    ArrayList<RecyclerItem> mList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.consumer_main_record, container, false);

        this.InitializeData();
        Context context = container.getContext();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewTest);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new MyAdapter(mList));  // Adapter 등록


        return rootView;
    }

    public void InitializeData() {
        mList = new ArrayList<RecyclerItem>();

        mList.add(new RecyclerItem("제목01", "내용01"));
        mList.add(new RecyclerItem("제목02", "내용02"));
        mList.add(new RecyclerItem("제목03", "내용03"));
        mList.add(new RecyclerItem("제목04", "내용04"));
        mList.add(new RecyclerItem("제목05", "내용05"));
        mList.add(new RecyclerItem("제목06", "내용06"));

    }


}

class RecyclerItem {
    private String storeName;
    private String menuList;

    public RecyclerItem(String storeName, String menuList) {
        this.storeName = storeName;
        this.menuList = menuList;
    }

    public void setStoreName(String storeName) { this.storeName = storeName; }

    public void setMenuList(String menuList) {
        this.menuList = menuList;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public String getMenuList() {
        return this.menuList;
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
                context.startActivity(intent);
            }
        });

        viewHolder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context acontext = v.getContext();
                Intent intent = new Intent(acontext, TestActivity.class);
                context.startActivity(intent);
//                myDataList.remove(position);
//                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }
}
