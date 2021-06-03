package com.example.kw_mk;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.myLocation;

public class FragmentSellerReview extends Fragment {

    ArrayList<ReviewItem> list;
    RecyclerView ReView;
    ReviewRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seller_main_review, container, false);

        ReView = rootView.findViewById(R.id.sellerReviewFragmentRecyclerView);

        initInfo();

        return rootView;
    }

    void initInfo() {
        list = new ArrayList<>();
        db.collection("Store_Info").document(App.LoginUserEmail).collection("Review")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("주문자명").toString();
                                String review = document.get("리뷰").toString();
                                String menu = document.get("주문목록").toString();

                                list.add(new ReviewItem(name, review, menu));

                                LinearLayoutManager manager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                ReView.setLayoutManager(manager2);
                                adapter = new ReviewRecyclerAdapter(list, getContext());
                                ReView.setAdapter(adapter);

                            }
                        }
                    }

                });
    }

}


class ReviewItem {
    String name;
    String review;
    String menu;

    ReviewItem(String name, String review, String menu) {
        this.name = name;
        this.review = review;
        this.menu = menu;
    }

}

class ReviewViewHolder extends RecyclerView.ViewHolder {

    TextView nameText;
    TextView reviewText;
    TextView menuText;

    ReviewViewHolder(View itemView) {
        super(itemView);

        nameText = itemView.findViewById(R.id.sellerReviewName);
        reviewText = itemView.findViewById(R.id.sellerReviewC);
        menuText = itemView.findViewById(R.id.sellerReviewMenuList);

    }
}


class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    Context context;
    private ArrayList<ReviewItem> ReviewRecyclerList = null;


    ReviewRecyclerAdapter(ArrayList<ReviewItem> dataList, Context context) {
        ReviewRecyclerList = dataList;
        this.context = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.seller_main_review_item, parent, false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);

        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewViewHolder holder, int position) {
        holder.nameText.setText(ReviewRecyclerList.get(position).name);
        holder.reviewText.setText(ReviewRecyclerList.get(position).review);
        holder.menuText.setText(ReviewRecyclerList.get(position).menu);
    }

    @Override
    public int getItemCount() {
        return ReviewRecyclerList.size();
    }
}
