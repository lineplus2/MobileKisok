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

        getSupportActionBar().setTitle("장바구니");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffffff")));


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BootUser bootUser = new BootUser().setPhone("010-3951-0826"); // !! 자신의 핸드폰 번호로 바꾸기
                BootExtra bootExtra = new BootExtra().setQuotas(new int[]{0, 2, 3});

                final int price = Integer.parseInt(totalAmount.getText().toString());

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


                // 가게로 주문정보 넣기
                final DocumentReference docref = db.collection("Store_Info").document(email).collection("RealTimeOrder").document();
                store.put("요청사항", needs.getText().toString());
                store.put("결제금액", totalAmount.getText().toString());
                store.put("주문자이메일", App.LoginUserEmail);
                store.put("주문시간", FieldValue.serverTimestamp());
                store.put("주문자이름", App.LoginUserName);

                db.collection("Store_Info").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        storeLocation.setLatitude(Double.parseDouble(document.get("위도").toString()));
                        storeLocation.setLongitude(Double.parseDouble(document.get("경도").toString()));
                    }
                });


                for (int i = 0; i < payMenuListItem.size(); i++) {
                    docref.collection("주문목록").document(payMenuListItem.get(i).payName).set(payMenuListItem.get(i));
                    if (i != payMenuListItem.size() - 1) {
                        menu.append(payMenuListItem.get(i).payName + "          수량 : " + payMenuListItem.get(i).amount);
                        menu.append("\n");
                    } else {
                        menu.append(payMenuListItem.get(i).payName + "          수량 : " + payMenuListItem.get(i).amount);
                    }
                }

                // 자신의 주문목록에 넣기
                mPay.put("주문시간", FieldValue.serverTimestamp());
                mPay.put("주문한가게이름", storeName.getText().toString());
                mPay.put("가게전화번호", storeNum.getText().toString());
                mPay.put("결제금액", totalAmount.getText().toString());
                mPay.put("요청사항", needs.getText().toString());
                mPay.put("주문한가게이메일", email);
                mPay.put("주문목록", menu.toString());

                if (payMenuListItem.size() != 0) {
                    store.put("결제방법", payC);
                    docref.set(store);
                    payMenuListItem.clear();
                } else {
                    Toast.makeText(ConsumerPayActivity.this, "주문목록이 없습니다.", Toast.LENGTH_SHORT).show();
                }

                db.collection("User_Info").document(App.LoginUserEmail).collection("OrderList").document().set(mPay);

                // 결제


                Bootpay.init(getFragmentManager())
                        .setApplicationId("60bd8a01d8c1bd00202bbe02") // 해당 프로젝트(안드로이드)의 application id 값(위의 값 복붙)
                        .setPG(PG.INICIS) // 결제할 PG 사
                        .setMethod(Method.CARD) // 결제수단
                        .setContext(ConsumerPayActivity.this)
                        .setBootUser(bootUser)
                        .setBootExtra(bootExtra)
                        .setUX(UX.PG_DIALOG)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                        .setName("결제상품명") // 결제할 상품명
                        .setOrderId("1234") // 결제 고유번호 (expire_month)
                        .setPrice(100) // 결제할 금액
                        .addItem("마우스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                        .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                        .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                            @Override
                            public void onConfirm(@Nullable String message) {
                                Log.d("confirm", "컴펌 실행");
                                if (0 < stuck)
                                    Bootpay.confirm(message); // 재고가 있을 경우.
                                else {
                                    Log.d("confirm", message);
                                    Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                                }
                            }
                        })
                        .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                            @Override
                            public void onDone(@Nullable String message) {
                                Log.d("done", message);
                            }
                        })
                        .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                            @Override
                            public void onReady(@Nullable String message) {
                                Log.d("ready", message);
                            }
                        })
                        .onCancel(new CancelListener() { // 결제 취소시 호출
                            @Override
                            public void onCancel(@Nullable String message) {

                                Log.d("cancel", message);
                            }
                        })
                        .onClose(
                                new CloseListener() { //결제창이 닫힐때 실행되는 부분
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