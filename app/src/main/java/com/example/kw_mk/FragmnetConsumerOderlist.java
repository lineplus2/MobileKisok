package com.example.kw_mk;

import android.content.Context;
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.consumer_main_home_pay, container, false);

        this.InitializeData();
        Context context = container.getContext();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewTest);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new MyAdapter(mList));  // Adapter 등록


        return rootView;
    }

    public void InitializeData() {
        mList = new ArrayList<>();

        mList.add(new RecyclerItem("제목01", "내용01"));
        mList.add(new RecyclerItem("제목02", "내용02"));
        mList.add(new RecyclerItem("제목03", "내용03"));
        mList.add(new RecyclerItem("제목04", "내용04"));
        mList.add(new RecyclerItem("제목05", "내용05"));
        mList.add(new RecyclerItem("제목06", "내용06"));

    }


}

class RecyclerItem {
    private String titleStr;
    private String descStr;

    public RecyclerItem(String title, String desc) {
        this.titleStr = title;
        this.descStr = desc;
    }

    public void setTitle(String title) {
        titleStr = title;
    }

    public void setDesc(String desc) {
        descStr = desc;
    }

    public String getTitle() {
        return this.titleStr;
    }

    public String getDesc() {
        return this.descStr;
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView grade;
    Button btn_delete;

    ViewHolder(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.reTitle);
        grade = itemView.findViewById(R.id.reDesc);
        btn_delete = (Button) itemView.findViewById(R.id.btn_delete);

    }
}

class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<RecyclerItem> myDataList = null;

    MyAdapter(ArrayList<RecyclerItem> dataList) {
        myDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        viewHolder.title.setText(myDataList.get(position).getTitle());
        viewHolder.grade.setText(myDataList.get(position).getDesc());

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                myDataList.remove(pos);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }
}
