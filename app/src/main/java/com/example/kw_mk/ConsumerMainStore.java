package com.example.kw_mk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.imageOptions;
import static com.example.kw_mk.App.storageRef;

public class ConsumerMainStore extends AppCompatActivity {

    TextView StoreName, StoreNum, StoreContent, StoreOwner, StoreAddress, StoreBusinessNum;
    RecyclerView menuList;
    FloatingActionButton fltbtn;

    ArrayList<MenuListItem> menuListItem;

    menuListAdapter adapter;

    public static String email;
    public static ArrayList addList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_main_store);

        StoreName = findViewById(R.id.Store_Info_Name);
        StoreNum = findViewById(R.id.Store_Info_Num);
        StoreContent = findViewById(R.id.Store_Info_Content);
        StoreOwner = findViewById(R.id.Store_Info_Owner);
        StoreAddress = findViewById(R.id.Store_Info_Address);
        StoreBusinessNum = findViewById(R.id.store_Info_businessNum);
        fltbtn = findViewById(R.id.floatingActionButton3);

        menuList = findViewById(R.id.storeMenu);

        getSupportActionBar().setTitle("매장정보");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffffff")));

        init_Info();

        fltbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsumerMainStore.this, ConsumerPayActivity.class);
                intent.putExtra("Email", email);
                startActivity(intent);

            }
        });
    }

    void init_Info() {
        email = getIntent().getStringExtra("Email");
        menuListItem = new ArrayList<MenuListItem>();

        db.collection("Store_Info").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                StoreName.setText(document.get("가게이름").toString());
                StoreNum.setText(document.get("전화번호").toString());
                StoreContent.setText(document.get("가게소개").toString());
                StoreOwner.setText(document.get("사업자명").toString());
                StoreAddress.setText(document.get("주소").toString() + "  " + document.get("상세주소"));
                StoreBusinessNum.setText(document.get("사업자번호").toString());
            }
        });

        db.collection("Store_Info").document(email).collection("Menu").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {


                                String name = document.get("메뉴이름").toString();
                                String price = document.get("메뉴가격").toString();

                                StorageReference str = storageRef.child("Store_Info").child(email).child("Menu").child(name);

                                menuListItem.add(new MenuListItem(name, price, str));
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Error :::: ", "Error getting documents: ", task.getException());
                        }
                    }
                });

        LinearLayoutManager manager = new LinearLayoutManager(ConsumerMainStore.this, LinearLayoutManager.VERTICAL, false);
        menuList.setLayoutManager(manager);
        adapter = new menuListAdapter(menuListItem, ConsumerMainStore.this);
        menuList.setAdapter(adapter);


    }
}

class MenuListItem {
    String name;
    String price;
    StorageReference img;

    MenuListItem(String name, String price, StorageReference img) {
        this.name = name;
        this.price = price;
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImg(StorageReference img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public StorageReference getImg() {
        return img;
    }
}

class MenuListViewHolder extends RecyclerView.ViewHolder {
    TextView menuName;
    TextView menuPrice;
    ImageView menuImage;

    MenuListViewHolder(View itemView) {
        super(itemView);

        menuName = itemView.findViewById(R.id.menuListName);
        menuPrice = itemView.findViewById(R.id.menuListPrice);
        menuImage = itemView.findViewById(R.id.menuListImg);

    }
}

class menuListAdapter extends RecyclerView.Adapter<MenuListViewHolder> {

    Context context;
    private ArrayList<MenuListItem> menuListItem = null;

    menuListAdapter(ArrayList<MenuListItem> datalist, Context context) {
        this.menuListItem = datalist;
        this.context = context;
    }


    @NonNull
    @Override
    public MenuListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.consumer_main_store_menulist, parent, false);
        MenuListViewHolder menuListViewHolder = new MenuListViewHolder(view);

        return menuListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuListViewHolder holder, final int position) {
        holder.menuName.setText(menuListItem.get(position).name);
        holder.menuPrice.setText(menuListItem.get(position).price);
        if (menuListItem.get(position).img != null) {
            menuListItem.get(position).img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).apply(imageOptions).into(holder.menuImage);
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShoppingCartActivity.class);
                intent.putExtra("email", ConsumerMainStore.email);
                intent.putExtra("menuName", menuListItem.get(position).name);
                intent.putExtra("menuPrice", menuListItem.get(position).price);

                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return menuListItem.size();
    }
}
