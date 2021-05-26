package com.example.kw_mk;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.storageRef;
import static com.example.kw_mk.App.testLo;

public class FragmentConsumerHome extends Fragment {
    GridItemList gridAdapter;
    private Context context;

    HomeRecyclerAdapter adp;

    ArrayList<homeRecyclerView> ReList = new ArrayList<>();

    RecyclerView recyclerView2;

    final String TAG = " FragmentConsumerHome";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.consumer_main_home, container, false);

        GridView grid = rootView.findViewById(R.id.mainGrid);
        recyclerView2 = (RecyclerView) rootView.findViewById(R.id.reTest);


        initData();


        gridAdapter = new GridItemList();

        // GridView Setting
        gridAdapter.addItem(new GridItem("Item1", "1", "test"));
        gridAdapter.addItem(new GridItem("Item2", "2", "test"));
        gridAdapter.addItem(new GridItem("Item3", "3", "test"));
        gridAdapter.addItem(new GridItem("Item4", "4", "test"));
        gridAdapter.addItem(new GridItem("Item5", "5", "test"));
        gridAdapter.addItem(new GridItem("item6", "6", "test"));
        gridAdapter.addItem(new GridItem("Item7", "7", "test"));
        gridAdapter.addItem(new GridItem("Item8", "8", "test"));
        gridAdapter.addItem(new GridItem("Item9", "9", "test"));


        context = container.getContext();

        testLo = new Location("Point A");

        testLo.setLatitude(35.839323);
        testLo.setLongitude(128.565597);


        grid.setAdapter(gridAdapter);


        // 그리드뷰 버튼
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridItem item = (GridItem) gridAdapter.getItem(position);

                switch (item.getPosition()) {
                    case "1":
                        Toast.makeText(context, "버튼 1", Toast.LENGTH_LONG).show();

                        break;
                    case "2":
                        Toast.makeText(context, "버튼 2", Toast.LENGTH_LONG).show();
                        break;
                    case "3":
                        break;

                }
            }
        });
        return rootView;
    }

    public void initData() {
        String uridata;
        ReList = new ArrayList<homeRecyclerView>();


        db.collection("Store_Info").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {

                                StorageReference str = storageRef.child("Store_Info").child(document.get("사업자이메일").toString() + "/storeImage");

                                String StoreId = document.get("가게이름").toString();
                                String StoreUser = document.get("사업자이메일").toString();
                                ReList.add(new homeRecyclerView(StoreId, str, StoreUser));
                                adp.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        LinearLayoutManager manager2 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(manager2);

        adp = new HomeRecyclerAdapter(ReList, getContext());
        recyclerView2.setAdapter(adp);
    }
}

// 그리드뷰

class GridItem {
    String name;
    String position;
    String category;

    public GridItem(String name, String phone, String category) {
        this.name = name;
        this.position = phone;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String phone) {
        this.position = position;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}


class GridItemList extends BaseAdapter {
    ArrayList<GridItem> items = new ArrayList<GridItem>();
    Context context;

    public void addItem(GridItem item) {
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        GridItem gridItem = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
        }

        TextView nameText = convertView.findViewById(R.id.name_text);
        TextView phoneText = convertView.findViewById(R.id.phone_text);

        nameText.setText(gridItem.getName());
        phoneText.setText(gridItem.getPosition());

        return convertView;
    }
}


// 리싸이클러뷰

class homeRecyclerView {
    String storeName;
    StorageReference storeUri;
    String storeEmail;

    homeRecyclerView(String Name, StorageReference Image, String storeEmail) {

        this.storeName = Name;
        this.storeUri = Image;
        this.storeEmail = storeEmail;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setStoreUri(StorageReference storeUri) {
        this.storeUri = storeUri;
    }

    public void setStoreEmail(String storeEmail) {
        this.storeEmail = storeEmail;
    }

    public String getStoreName() {
        return storeName;
    }

    public StorageReference getStoreUri() {
        return storeUri;
    }

    public String getStoreEmail() {
        return storeEmail;
    }
}

class HomeViewHolder extends RecyclerView.ViewHolder {
    TextView storeName;
    ImageView storeImage;

    HomeViewHolder(View itemView) {
        super(itemView);

        storeName = itemView.findViewById(R.id.reStoreName);
        storeImage = itemView.findViewById(R.id.reStoreImage);

    }
}

class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    Context context;
    private ArrayList<homeRecyclerView> HomeRecyclerList = null;


    HomeRecyclerAdapter(ArrayList<homeRecyclerView> dataList, Context context) {
        HomeRecyclerList = dataList;
        this.context = context;
    }

//    public interface ItemClickListener() {
//        void onItemClick(int position);
//    }


    @Override
    public HomeViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.consumer_main_listitem, parent, false);
        HomeViewHolder viewHolder = new HomeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, int position) {

        final String email;
        holder.storeName.setText(HomeRecyclerList.get(position).getStoreName());
        HomeRecyclerList.get(position).getStoreUri().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.storeImage);
            }
        });
        email = HomeRecyclerList.get(position).getStoreEmail();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ConsumerMainStore.class);
                intent.putExtra("Email", email);
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return HomeRecyclerList.size();
    }
}