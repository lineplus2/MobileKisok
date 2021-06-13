package com.example.kw_mk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.kw_mk.App.db;

public class FragmentSellerOrder extends Fragment {

    ArrayList<orderListItem> orderList, reserveList;
    orderListAdapter orderListAdapter, reserveListAdapter;

    Button btn_1, btn_2;

    RecyclerView orderMenu;

    DocumentReference orderStoref;

    public static int btnSet = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db.collection("Store_Info").document(App.LoginUserEmail).collection("RealTimeOrder").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                String named = "";

                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            named = dc.getDocument().get("주문자이름").toString();
                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:
                            break;
                    }
                }
                if (btnSet == 0) {
                    order_ListSet();
                }


            }
        });

        db.collection("Store_Info").document(App.LoginUserEmail).collection("Reserve").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                String namedd = "";

                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            namedd = dc.getDocument().get("주문자이름").toString();
                            alertShow(namedd);
                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:
                            break;
                    }
                }

                if (btnSet == 1) {
                    reserve_ListSet();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seller_main_order, container, false);

        orderMenu = rootView.findViewById(R.id.orderList);
        btn_1 = rootView.findViewById(R.id.order_btn);
        btn_2 = rootView.findViewById(R.id.order_btn2);

        orderStoref = db.collection("Store_Info").document(App.LoginUserEmail);
        order_ListSet();

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSet = 0;
                order_ListSet();
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSet = 1;
                reserve_ListSet();
            }
        });

        orderMenu.setHasFixedSize(true);


        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Query query = db.collection("Store_Info").document(App.LoginUserEmail).collection("RealTimeOrder");
        ListenerRegistration registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            }
        });

        Query query1 = db.collection("Store_Info").document(App.LoginUserEmail).collection("Reserve");
        ListenerRegistration registration1 = query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            }
        });

        registration.remove();
        registration1.remove();


    }

    void alertShow(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("AlertDialog");
        builder.setMessage(name + "이 근처에 왔습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }


    void reserve_ListSet() {
        reserveList = new ArrayList<>();

        orderStoref.collection("Reserve").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful() && task != null) {
                                                   for (final QueryDocumentSnapshot document : task.getResult()) {
                                                       final ArrayList<orderItem> itemList = new ArrayList<>();
                                                       String name = document.get("주문자이름").toString();
                                                       String id = document.getId();
                                                       DocumentReference co = orderStoref.collection("Reserve").document(id);
                                                       co.collection("주문목록").get()
                                                               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<QuerySnapshot> ta) {
                                                                       if (ta.isSuccessful()) {
                                                                           for (QueryDocumentSnapshot doc : ta.getResult()) {
                                                                               String itemName = doc.get("payName").toString();
                                                                               String itemAmount = doc.get("amount").toString();
                                                                               itemList.add(new orderItem(itemName, itemAmount));
                                                                           }
                                                                       } else {
                                                                           Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                                                       }
                                                                   }
                                                               });
                                                       reserveList.add(new orderListItem(name, id, itemList));
                                                       reserveListAdapter.notifyDataSetChanged();
                                                   }
                                               }
                                           }
                                       }
                );
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        orderMenu.setLayoutManager(manager);
        reserveListAdapter = new orderListAdapter(reserveList, getContext());
        orderMenu.setAdapter(reserveListAdapter);
    }

    void order_ListSet() {

        orderList = new ArrayList<>();

        orderStoref.collection("RealTimeOrder").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (final QueryDocumentSnapshot document : task.getResult()) {
                                                       final ArrayList<orderItem> itemList = new ArrayList<>();
                                                       String name = document.get("주문자이름").toString();
                                                       String id = document.getId();
                                                       DocumentReference co = orderStoref.collection("RealTimeOrder").document(id);
                                                       co.collection("주문목록").get()
                                                               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<QuerySnapshot> ta) {
                                                                       if (ta.isSuccessful()) {
                                                                           for (QueryDocumentSnapshot doc : ta.getResult()) {
                                                                               String itemName = doc.get("payName").toString();
                                                                               String itemAmount = doc.get("amount").toString();
                                                                               itemList.add(new orderItem(itemName, itemAmount));
                                                                           }
                                                                       } else {
                                                                           Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                                                       }
                                                                   }
                                                               });
                                                       orderList.add(new orderListItem(name, id, itemList));
                                                       orderListAdapter.notifyDataSetChanged();
                                                   }
                                               }
                                           }
                                       }
                );
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        orderMenu.setLayoutManager(manager);
        orderListAdapter = new orderListAdapter(orderList, getContext());
        orderMenu.setAdapter(orderListAdapter);
    }
}

class orderListItem {
    String orderByName;
    String id;
    ArrayList<orderItem> item;

    orderListItem(String name, String id, ArrayList<orderItem> item) {
        this.orderByName = name;
        this.id = id;
        this.item = item;
    }

    public void setOrderByName(String orderByName) {
        this.orderByName = orderByName;
    }

    public String getOrderByName() {
        return orderByName;
    }

    public String getId() {
        return id;
    }

    public ArrayList<orderItem> getItem() {
        return item;
    }
}

class orderListViewHolder extends RecyclerView.ViewHolder {
    TextView orderName;
    RecyclerView orderItemList;
    Button btn;


    orderListViewHolder(View itemView) {
        super(itemView);

        orderName = itemView.findViewById(R.id.orderByName);
        orderItemList = itemView.findViewById(R.id.orderByMenu);
        btn = itemView.findViewById(R.id.orderCompliteBtn);

    }
}


class orderListAdapter extends RecyclerView.Adapter<orderListViewHolder> {

    Context context;
    private ArrayList<orderListItem> orderListItem = null;
    orderItemAdapter ItemAdapter;

    ArrayList<orderItem> orItem;

    orderListAdapter(ArrayList<orderListItem> datal, Context context) {
        this.orderListItem = datal;
        this.context = context;
    }


    @NonNull
    @Override
    public orderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.seller_main_order_item, parent, false);
        orderListViewHolder orderListViewHolder = new orderListViewHolder(view);

        return orderListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final orderListViewHolder holder, final int position) {

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FragmentSellerOrder.btnSet == 0) {
                    db.collection("Store_Info").document(App.LoginUserEmail).collection("RealTimeOrder").document(orderListItem.get(position).getId()).collection("주문목록")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (final QueryDocumentSnapshot document : task.getResult()) {
                                            document.getReference().delete();
                                        }
                                    }

                                }
                            });
                    db.collection("Store_Info").document(App.LoginUserEmail).collection("RealTimeOrder").document(orderListItem.get(position).getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    orderListItem.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                } else if (FragmentSellerOrder.btnSet == 1) {
                    db.collection("Store_Info").document(App.LoginUserEmail).collection("Reserve").document(orderListItem.get(position).getId()).collection("주문목록")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (final QueryDocumentSnapshot document : task.getResult()) {
                                            document.getReference().delete();
                                        }
                                    }
                                }
                            });

                    db.collection("Store_Info").document(App.LoginUserEmail).collection("Reserve").document(orderListItem.get(position).getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    orderListItem.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }

            }
        });


        LinearLayoutManager mana = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.orderItemList.setLayoutManager(mana);
        ItemAdapter = new orderItemAdapter(orderListItem.get(position).item, context);
        holder.orderItemList.setAdapter(ItemAdapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 1000);

        holder.orderName.setText(orderListItem.get(position).orderByName);
    }


    @Override
    public int getItemCount() {
        return orderListItem.size();
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class orderItem {
    String name;
    String amount;

    orderItem(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

}


class orderItemViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView amount;

    orderItemViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.orderListMenu);
        amount = itemView.findViewById(R.id.orderListAmount);

    }
}


class orderItemAdapter extends RecyclerView.Adapter<orderItemViewHolder> {

    Context context;
    private ArrayList<orderItem> orderItem = null;

    orderItemAdapter(ArrayList<orderItem> data, Context context) {
        this.orderItem = data;
        this.context = context;
    }


    @NonNull
    @Override
    public orderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.seller_main_order_itemmenu, parent, false);
        orderItemViewHolder orderItemViewHolder = new orderItemViewHolder(view);

        return orderItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final orderItemViewHolder holder, final int position) {

        holder.name.setText(orderItem.get(position).getName());
        holder.amount.setText(orderItem.get(position).getAmount());

    }


    @Override
    public int getItemCount() {
        return orderItem.size();
    }
}