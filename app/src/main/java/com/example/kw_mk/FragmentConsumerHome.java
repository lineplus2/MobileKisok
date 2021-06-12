package com.example.kw_mk;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.imageOptions;
import static com.example.kw_mk.App.myLocation;
import static com.example.kw_mk.App.storageRef;

public class FragmentConsumerHome extends Fragment {
    private Context context;

    HomeRecyclerAdapter adp;

    ArrayList<homeRecyclerView> ReList = new ArrayList<>();

    RecyclerView recyclerView2;


    final String TAG = " FragmentConsumerHome";

    ImageView category1, category2, category3, category4, category5, category6, category7, category8, category9;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.consumer_main_home, container, false);

        category1 = rootView.findViewById(R.id.category1);
        category2 = rootView.findViewById(R.id.category2);
        category3 = rootView.findViewById(R.id.category3);
        category4 = rootView.findViewById(R.id.category4);
        category5 = rootView.findViewById(R.id.category5);
        category6 = rootView.findViewById(R.id.category6);
        category7 = rootView.findViewById(R.id.category7);
        category8 = rootView.findViewById(R.id.category8);
        category9 = rootView.findViewById(R.id.category9);

        recyclerView2 = (RecyclerView) rootView.findViewById(R.id.reTest);

        initData();

        context = container.getContext();

        category1.setOnClickListener(categoryClick);
        category2.setOnClickListener(categoryClick);
        category3.setOnClickListener(categoryClick);
        category4.setOnClickListener(categoryClick);
        category5.setOnClickListener(categoryClick);
        category6.setOnClickListener(categoryClick);
        category7.setOnClickListener(categoryClick);
        category8.setOnClickListener(categoryClick);
        category9.setOnClickListener(categoryClick);

        return rootView;
    }

    View.OnClickListener categoryClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent categoryintent;
            switch (v.getId()) {
                case R.id.category1:
                    categoryintent = new Intent(getContext(), ConsumerCategory.class);
                    categoryintent.putExtra("category", "한식");
                    startActivity(categoryintent);
                    break;
                case R.id.category2:
                    categoryintent = new Intent(getContext(), ConsumerCategory.class);
                    categoryintent.putExtra("category", "일식");
                    startActivity(categoryintent);
                    break;
                case R.id.category3:
                    categoryintent = new Intent(getContext(), ConsumerCategory.class);
                    categoryintent.putExtra("category", "중식");
                    startActivity(categoryintent);
                    break;
                case R.id.category4:
                    categoryintent = new Intent(getContext(), ConsumerCategory.class);
                    categoryintent.putExtra("category", "양식");
                    startActivity(categoryintent);
                    break;
                case R.id.category5:
                    categoryintent = new Intent(getContext(), ConsumerCategory.class);
                    categoryintent.putExtra("category", "패스트푸드");
                    startActivity(categoryintent);
                    break;
                case R.id.category6:
                    categoryintent = new Intent(getContext(), ConsumerCategory.class);
                    categoryintent.putExtra("category", "분식");
                    startActivity(categoryintent);
                    break;
                case R.id.category7:
                    categoryintent = new Intent(getContext(), ConsumerCategory.class);
                    categoryintent.putExtra("category", "베이커리");
                    startActivity(categoryintent);
                    break;
                case R.id.category8:
                    categoryintent = new Intent(getContext(), ConsumerCategory.class);
                    categoryintent.putExtra("category", "찜탕");
                    startActivity(categoryintent);
                    break;
                case R.id.category9:
                    categoryintent = new Intent(getContext(), ConsumerCategory.class);
                    categoryintent.putExtra("category", "술집");
                    startActivity(categoryintent);
                    break;

            }
        }
    };


    public void initData() {
        ReList = new ArrayList<homeRecyclerView>();


        db.collection("Store_Info").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {

                                Location lo = null;
                                StorageReference str = storageRef.child("Store_Info").child(document.get("사업자이메일").toString() + "/storeImage");

                                String StoreId = document.get("가게이름").toString();
                                String StoreUser = document.get("사업자이메일").toString();
                                double la = Double.valueOf(document.get("위도").toString()).doubleValue();
                                double ln = Double.parseDouble(document.get("경도").toString());
                                lo = new Location("Point");
                                lo.setLatitude(la);
                                lo.setLongitude(ln);
                                adp.notifyDataSetChanged();

                                int loTo = (int) lo.distanceTo(myLocation);

                                if (loTo < 1000) {
                                    ReList.add(new homeRecyclerView(StoreId, str, StoreUser, lo));
                                }

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


// 리싸이클러뷰

class homeRecyclerView {
    String storeName;
    StorageReference storeUri;
    String storeEmail;
    Location storeL;

    homeRecyclerView(String Name, StorageReference Image, String storeEmail, Location storeLocation) {

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

class HomeViewHolder extends RecyclerView.ViewHolder {
    TextView storeName, storeDistance;
    ImageView storeImage;


    HomeViewHolder(View itemView) {
        super(itemView);

        storeName = itemView.findViewById(R.id.reStoreName);
        storeImage = itemView.findViewById(R.id.reStoreImage);
        storeDistance = itemView.findViewById(R.id.distance);


    }
}

class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    Context context;
    private ArrayList<homeRecyclerView> HomeRecyclerList = null;


    HomeRecyclerAdapter(ArrayList<homeRecyclerView> dataList, Context context) {
        HomeRecyclerList = dataList;
        this.context = context;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.consumer_main_listitem, parent, false);
        HomeViewHolder viewHolder = new HomeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, int position) {


        GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.imagerounding);


        final String email;
        holder.storeName.setText(HomeRecyclerList.get(position).getStoreName());
        HomeRecyclerList.get(position).getStoreUri().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).apply(imageOptions).into(holder.storeImage);
            }
        });
//        holder.storeImage.setBackground(drawable);
//        holder.storeImage.setClipToOutline(true);


        int loToInt = (int) HomeRecyclerList.get(position).getStoreL().distanceTo(myLocation);

        holder.storeDistance.setText(loToInt + "M");

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