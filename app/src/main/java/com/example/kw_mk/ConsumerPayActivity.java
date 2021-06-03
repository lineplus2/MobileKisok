package com.example.kw_mk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.payMenuListItem;

public class ConsumerPayActivity extends AppCompatActivity {

    TextView storeName, storeNum, needs;

    RadioGroup rGroup;

    Button pay;
    public static TextView totalAmount;
    public static RecyclerView addMenuList;

    public static payMenuListAdapter payAdapter;

    String email;
    StringBuilder menu = new StringBuilder("");

    int sum = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_main_home_pay);

        pay = findViewById(R.id.payBtn);
        addMenuList = findViewById(R.id.menuList);
        totalAmount = findViewById(R.id.totalAmount);
        storeName = findViewById(R.id.payStoreName);
        storeNum = findViewById(R.id.payStoreNum);
        email = getIntent().getStringExtra("Email");
        needs = findViewById(R.id.needs);
        rGroup = findViewById(R.id.r_group);


        initData();


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> mPay = new HashMap<>();
                // 주문넣기
                HashMap<String, Object> store = new HashMap<>();
                String payC = null;
                int id = rGroup.getCheckedRadioButtonId();

                RadioButton rb = findViewById(id);
                switch (rb.getText().toString()) {
                    case "선결제":
                        payC = "선결제";
                        break;
                    case "방문결제":
                        payC = "방문결제";
                        break;
                }

                DocumentReference docref = db.collection("Store_Info").document(email).collection("RealTimeOrder").document();
                store.put("요청사항", needs.getText().toString());
                store.put("결제금액", totalAmount.getText().toString());
                store.put("주문자이메일", App.LoginUserEmail);
                store.put("주문시간", FieldValue.serverTimestamp());
                store.put("주문자이름", App.LoginUserName);

                for (int i = 0; i < payMenuListItem.size(); i++) {
                    docref.collection("주문목록").document(payMenuListItem.get(i).payName).set(payMenuListItem.get(i));
                    if(i != payMenuListItem.size()-1 ){
                        menu.append(payMenuListItem.get(i).payName +"          수량 : " + payMenuListItem.get(i).amount);
                        menu.append("\n");
                    } else {
                        menu.append(payMenuListItem.get(i).payName +"          수량 : " + payMenuListItem.get(i).amount);
                    }
                }


                if (payMenuListItem.size() != 0) {
                    store.put("결제방법", payC);
                    docref.set(store);
                    payMenuListItem.clear();
                    finish();
                } else {
                    Toast.makeText(ConsumerPayActivity.this, "주문목록이 없습니다.", Toast.LENGTH_SHORT).show();
                }

                // 자신의 주문목록에 넣기
                mPay.put("주문시간", FieldValue.serverTimestamp());
                mPay.put("주문한가게이름", storeName.getText().toString());
                mPay.put("가게전화번호", storeNum.getText().toString());
                mPay.put("결제금액", totalAmount.getText().toString());
                mPay.put("요청사항", needs.getText().toString());
                mPay.put("주문한가게이메일", email);
                mPay.put("주문목록", menu.toString());

                db.collection("User_Info").document(App.LoginUserEmail).collection("OrderList").document().set(mPay);
            }
        });
    }

    public void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(ConsumerPayActivity.this, LinearLayoutManager.VERTICAL, false);
        addMenuList.setLayoutManager(manager);
        payAdapter = new payMenuListAdapter(payMenuListItem, ConsumerPayActivity.this);
        addMenuList.setAdapter(payAdapter);

        for (int i = 0; i < payMenuListItem.size(); i++) {
            sum += Integer.parseInt(payMenuListItem.get(i).payPrice) * (Integer.parseInt(payMenuListItem.get(i).amount));
        }
        totalAmount.setText(String.valueOf(sum));

        db.collection("Store_Info").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                storeName.setText(document.get("가게이름").toString());
                storeNum.setText(document.get("전화번호").toString());
            }
        });
    }


}

class payMenuList {
    String payName;
    String payPrice;
    String amount;

    payMenuList(String payName, String payPrice, String amount) {
        this.payName = payName;
        this.payPrice = payPrice;
        this.amount = amount;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayName() {
        return payName;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public String getAmount() {
        return amount;
    }
}

class payMenuListViewHolder extends RecyclerView.ViewHolder {
    TextView payMenuName;
    TextView payMenuPrice;
    TextView payAmount;
    Button delBtn;

    payMenuListViewHolder(View itemView) {
        super(itemView);

        payMenuName = itemView.findViewById(R.id.payMenuName);
        payMenuPrice = itemView.findViewById(R.id.payMenuPrice);
        payAmount = itemView.findViewById(R.id.payAmount);
        delBtn = itemView.findViewById(R.id.menuDel);

    }
}

class payMenuListAdapter extends RecyclerView.Adapter<payMenuListViewHolder> {

    Context context;
    private ArrayList<payMenuList> payMenuListItem = null;
    int sum1 = 0;

    payMenuListAdapter(ArrayList<payMenuList> datalist, Context context) {
        this.payMenuListItem = datalist;
        this.context = context;
    }


    @NonNull
    @Override
    public payMenuListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.consumer_main_home_pay_item, parent, false);
        payMenuListViewHolder payMenuListViewHolder = new payMenuListViewHolder(view);

        return payMenuListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final payMenuListViewHolder holder, final int position) {
        holder.payMenuName.setText(payMenuListItem.get(position).payName);
        holder.payMenuPrice.setText(payMenuListItem.get(position).payPrice);
        holder.payAmount.setText(payMenuListItem.get(position).amount);

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payMenuListItem.remove(position);
                for (int i = 0; i < payMenuListItem.size(); i++) {
                    sum1 += Integer.parseInt(payMenuListItem.get(i).payPrice) * (Integer.parseInt(payMenuListItem.get(i).amount));
                }
                ConsumerPayActivity.totalAmount.setText(String.valueOf(sum1));
                if (payMenuListItem.size() == 0) {
                    ConsumerPayActivity.totalAmount.setText("0");
                }
                ConsumerPayActivity.payAdapter.notifyDataSetChanged();


            }
        });
    }

    @Override
    public int getItemCount() {
        return payMenuListItem.size();
    }
}