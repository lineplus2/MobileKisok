package com.example.kw_mk;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

import static com.example.kw_mk.App.db;
import static com.example.kw_mk.App.payMenuListItem;
import static com.example.kw_mk.App.storeLocation;

public class ConsumerPayActivity extends AppCompatActivity {

    TextView storeName, storeNum, needs;

    RadioGroup rGroup;


    private int stuck = 10;

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

        BootpayAnalytics.init(ConsumerPayActivity.this, "60bd8a01d8c1bd00202bbe02");

        initData();

        getSupportActionBar().setTitle("????????????");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffffff")));


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BootUser bootUser = new BootUser().setPhone("010-3951-0826"); // !! ????????? ????????? ????????? ?????????
                BootExtra bootExtra = new BootExtra().setQuotas(new int[]{0, 2, 3});

                final int price = Integer.parseInt(totalAmount.getText().toString());

                HashMap<String, Object> mPay = new HashMap<>();
                // ????????????
                HashMap<String, Object> store = new HashMap<>();
                String payC = null;
                int id = rGroup.getCheckedRadioButtonId();

                RadioButton rb = findViewById(id);
                switch (rb.getText().toString()) {
                    case "?????????":
                        payC = "?????????";
                        break;
                    case "????????????":
                        payC = "????????????";
                        break;
                }


                // ????????? ???????????? ??????
                final DocumentReference docref = db.collection("Store_Info").document(email).collection("RealTimeOrder").document();
                store.put("????????????", needs.getText().toString());
                store.put("????????????", totalAmount.getText().toString());
                store.put("??????????????????", App.LoginUserEmail);
                store.put("????????????", FieldValue.serverTimestamp());
                store.put("???????????????", App.LoginUserName);

                db.collection("Store_Info").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        storeLocation.setLatitude(Double.parseDouble(document.get("??????").toString()));
                        storeLocation.setLongitude(Double.parseDouble(document.get("??????").toString()));
                    }
                });


                for (int i = 0; i < payMenuListItem.size(); i++) {
                    docref.collection("????????????").document(payMenuListItem.get(i).payName).set(payMenuListItem.get(i));
                    if (i != payMenuListItem.size() - 1) {
                        menu.append(payMenuListItem.get(i).payName + "          ?????? : " + payMenuListItem.get(i).amount);
                        menu.append("\n");
                    } else {
                        menu.append(payMenuListItem.get(i).payName + "          ?????? : " + payMenuListItem.get(i).amount);
                    }
                }

                // ????????? ??????????????? ??????
                mPay.put("????????????", FieldValue.serverTimestamp());
                mPay.put("?????????????????????", storeName.getText().toString());
                mPay.put("??????????????????", storeNum.getText().toString());
                mPay.put("????????????", totalAmount.getText().toString());
                mPay.put("????????????", needs.getText().toString());
                mPay.put("????????????????????????", email);
                mPay.put("????????????", menu.toString());

                if (payMenuListItem.size() != 0) {
                    store.put("????????????", payC);
                    docref.set(store);
                    payMenuListItem.clear();
                } else {
                    Toast.makeText(ConsumerPayActivity.this, "??????????????? ????????????.", Toast.LENGTH_SHORT).show();
                }

                db.collection("User_Info").document(App.LoginUserEmail).collection("OrderList").document().set(mPay);

                // ??????


                Bootpay.init(getFragmentManager())
                        .setApplicationId("60bd8a01d8c1bd00202bbe02") // ?????? ????????????(???????????????)??? application id ???(?????? ??? ??????)
                        .setPG(PG.INICIS) // ????????? PG ???
                        .setMethod(Method.CARD) // ????????????
                        .setContext(ConsumerPayActivity.this)
                        .setBootUser(bootUser)
                        .setBootExtra(bootExtra)
                        .setUX(UX.PG_DIALOG)
//                .setUserPhone("010-1234-5678") // ????????? ????????????
                        .setName("???????????????") // ????????? ?????????
                        .setOrderId("1234") // ?????? ???????????? (expire_month)
                        .setPrice(100) // ????????? ??????
                        .addItem("?????????", 1, "ITEM_CODE_MOUSE", 100) // ??????????????? ?????? ????????????, ????????? ?????? ??????
                        .addItem("?????????", 1, "ITEM_CODE_KEYBOARD", 200, "??????", "????????????", "????????????") // ??????????????? ?????? ????????????, ????????? ?????? ??????
                        .onConfirm(new ConfirmListener() { // ????????? ???????????? ?????? ?????? ???????????? ?????????, ?????? ???????????? ?????? ????????? ??????
                            @Override
                            public void onConfirm(@Nullable String message) {
                                Log.d("confirm", "?????? ??????");
                                if (0 < stuck)
                                    Bootpay.confirm(message); // ????????? ?????? ??????.
                                else {
                                    Log.d("confirm", message);
                                    Bootpay.removePaymentWindow(); // ????????? ?????? ????????? ???????????? ?????? ?????? ??????
                                }
                            }
                        })
                        .onDone(new DoneListener() { // ??????????????? ??????, ????????? ?????? ??? ????????? ????????? ????????? ???????????????
                            @Override
                            public void onDone(@Nullable String message) {
                                Log.d("done", message);
                            }
                        })
                        .onReady(new ReadyListener() { // ???????????? ?????? ??????????????? ???????????? ???????????? ???????????????.
                            @Override
                            public void onReady(@Nullable String message) {
                                Log.d("ready", message);
                            }
                        })
                        .onCancel(new CancelListener() { // ?????? ????????? ??????
                            @Override
                            public void onCancel(@Nullable String message) {

                                Log.d("cancel", message);
                            }
                        })
                        .onClose(
                                new CloseListener() { //???????????? ????????? ???????????? ??????
                                    @Override
                                    public void onClose(String message) {
                                        Log.d("close", "close");
                                    }
                                })
                        .request();
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
                storeName.setText(document.get("????????????").toString());
                storeNum.setText(document.get("????????????").toString());
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