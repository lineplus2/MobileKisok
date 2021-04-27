package com.example.kw_mk;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragmentConsumerHome extends Fragment {
    GridItemList gridAdapter;
    private Context context;
    double a, b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.consumer_main_home, container, false);

        GridView grid = rootView.findViewById(R.id.mainGrid);
        gridAdapter = new GridItemList();

        context = container.getContext();

        App.gpsTracker = new GpsTracker(context);

        gridAdapter.addItem(new GridItem("Item1", "1", "test"));
        gridAdapter.addItem(new GridItem("Item2", "2", "test"));
        gridAdapter.addItem(new GridItem("Item3", "3", "test"));
        gridAdapter.addItem(new GridItem("Item4", "4", "test"));
        gridAdapter.addItem(new GridItem("Item5", "5", "test"));
        gridAdapter.addItem(new GridItem("item6", "6", "test"));
        gridAdapter.addItem(new GridItem("Item7", "7", "test"));
        gridAdapter.addItem(new GridItem("Item8", "8", "test"));
        gridAdapter.addItem(new GridItem("Item9", "9", "test"));

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
}

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
