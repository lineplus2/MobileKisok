package com.example.kw_mk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.imageOptions;
import static com.example.kw_mk.App.storageRef;

public class FragmentSellerMenu extends Fragment {


    FloatingActionButton floatingBtn;
    RecyclerView menuList;
    MenuInfoRecyclerAdapter adap;
    Context context;
    Handler handler;

    ArrayList<MenuInfoRecycler> menuListData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seller_main_menumgmt, container, false);

        floatingBtn = rootView.findViewById(R.id.floatingActionButton);
        menuList = rootView.findViewById(R.id.sellerMenuList);

        context = container.getContext();
        handler = new Handler();

        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellerMenuAddActivity.class);
                startActivity(intent);

            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    public void initData() {
        menuListData = new ArrayList<MenuInfoRecycler>();

        db.collection("Store_Info").document(App.LoginUserEmail).collection("Menu").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {


                                String name = document.get("메뉴이름").toString();
                                String price = document.get("메뉴가격").toString();

                                StorageReference str = storageRef.child("Store_Info").child(App.LoginUserEmail).child("Menu").child(name);

                                menuListData.add(new MenuInfoRecycler(name, price, str));
                                adap.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Error :::: ", "Error getting documents: ", task.getException());
                        }
                    }
                });

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        menuList.setLayoutManager(manager);
        adap = new MenuInfoRecyclerAdapter(menuListData, context);
        menuList.setAdapter(adap);

    }
}


class MenuInfoRecycler {
    String name;
    String price;
    StorageReference image;

    MenuInfoRecycler(String name, String price, StorageReference uri) {
        this.name = name;
        this.price = price;
        this.image = uri;
    }

    public void setImage(StorageReference image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public StorageReference getImage() {
        return image;
    }
}

class MenuInfoViewHolder extends RecyclerView.ViewHolder {
    TextView menuName;
    TextView menuPrice;
    ImageView menuImage;

    MenuInfoViewHolder(View itemView) {
        super(itemView);

        menuName = itemView.findViewById(R.id.menuItemName);
        menuPrice = itemView.findViewById(R.id.menuItemPrice);
        menuImage = itemView.findViewById(R.id.menuItemImage);

    }
}

class MenuInfoRecyclerAdapter extends RecyclerView.Adapter<MenuInfoViewHolder> {

    Context context;
    private ArrayList<MenuInfoRecycler> menuInfoRecyclerList = null;


    MenuInfoRecyclerAdapter(ArrayList<MenuInfoRecycler> dataList, Context context) {
        menuInfoRecyclerList = dataList;
        this.context = context;
    }


    @Override
    public MenuInfoViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.seller_main_menumgmt_item, parent, false);
        MenuInfoViewHolder menuviewHolder = new MenuInfoViewHolder(view);

        return menuviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuInfoViewHolder holder, int position) {
        holder.menuName.setText(menuInfoRecyclerList.get(position).name);
        holder.menuPrice.setText(menuInfoRecyclerList.get(position).price);
        if (menuInfoRecyclerList.get(position).image != null) {
            menuInfoRecyclerList.get(position).image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).apply(imageOptions).into(holder.menuImage);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return menuInfoRecyclerList.size();
    }
}


