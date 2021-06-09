package com.example.kw_mk;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.imageOptions;
import static com.example.kw_mk.App.myLocation;
import static com.example.kw_mk.App.storageRef;

public class ConsumerCategory extends AppCompatActivity {

    RecyclerView categoryStoreList;

    categoryRecyclerAdapter cateAdap;

    ArrayList<categoryRecyclerView> categoryList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_category);

        categoryStoreList = findViewById(R.id.categoryList);

        initData();

        getSupportActionBar().setTitle("카테고리 별 매장");


    }

    void initData() {

        String category = getIntent().getStringExtra("category");

        db.collection("Store_Info").whereEqualTo("카테고리", category).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {

                                Location lo = null;
                                StorageReference stora = storageRef.child("Store_Info").child(document.get("사업자이메일").toString() + "/storeImage");

                                String name = document.get("가게이름").toString();
                                String StoreUser = document.get("사업자이메일").toString();
                                double la = Double.valueOf(document.get("위도").toString()).doubleValue();
                                double ln = Double.parseDouble(document.get("경도").toString());
                                lo = new Location("Point");
                                lo.setLatitude(la);
                                lo.setLongitude(ln);

                                categoryList.add(new categoryRecyclerView(name, stora, StoreUser, lo));
                                cateAdap.notifyDataSetChanged();

                            }
                        } else {
                        }
                    }
                });

        LinearLayoutManager manager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        categoryStoreList.setLayoutManager(manager2);
        cateAdap = new categoryRecyclerAdapter(categoryList, this);
        categoryStoreList.setAdapter(cateAdap);

    }
}


class categoryRecyclerView {
    String storeName;
    StorageReference storeUri;
    String storeEmail;
    Location storeL;

    categoryRecyclerView(String Name, StorageReference Image, String storeEmail, Location storeLocation) {

        this.storeName = Name;
        this.storeUri = Image;
        this.storeEmail = storeEmail;
        this.storeL = storeLocation;
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

    public void setStoreL(Location storeL) {
        this.storeL = storeL;
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

    public Location getStoreL() {
        return storeL;
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView storeName, storeDistance;
    ImageView storeImage;

    CategoryViewHolder(View itemView) {
        super(itemView);

        storeName = itemView.findViewById(R.id.reStoreName);
        storeImage = itemView.findViewById(R.id.reStoreImage);
        storeDistance = itemView.findViewById(R.id.distance);


    }
}

class categoryRecyclerAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;
    private ArrayList<categoryRecyclerView> categoryRecyclerList = null;


    categoryRecyclerAdapter(ArrayList<categoryRecyclerView> dataList, Context context) {
        categoryRecyclerList = dataList;
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.consumer_main_listitem, parent, false);
        CategoryViewHolder viewHolder = new CategoryViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position) {

        final String email;
        holder.storeName.setText(categoryRecyclerList.get(position).getStoreName());
        categoryRecyclerList.get(position).getStoreUri().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).apply(imageOptions).into(holder.storeImage);
            }
        });

        int loToInt = (int) categoryRecyclerList.get(position).getStoreL().distanceTo(myLocation);

        holder.storeDistance.setText(loToInt + "M");

        email = categoryRecyclerList.get(position).getStoreEmail();

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
        return categoryRecyclerList.size();
    }
}
