package com.example.kw_mk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentConsumerHome extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.consumer_main_home, container, false);

        GridView grid = rootView.findViewById(R.id.mainGrid);
        GridItemList gridAdapter = new GridItemList();

        gridAdapter.addItem(new GridItem("Item", "Tag", "test"));
        gridAdapter.addItem(new GridItem("Item", "Tag", "test"));
        gridAdapter.addItem(new GridItem("Item", "Tag", "test"));
        gridAdapter.addItem(new GridItem("Item", "Tag", "test"));
        gridAdapter.addItem(new GridItem("Item", "Tag", "test"));
        gridAdapter.addItem(new GridItem("Item", "Tag", "test"));
        gridAdapter.addItem(new GridItem("Item", "Tag", "test"));
        gridAdapter.addItem(new GridItem("Item", "Tag", "test"));
        gridAdapter.addItem(new GridItem("Item", "Tag", "test"));

        grid.setAdapter(gridAdapter);
        return rootView;
    }
}

class GridItem {
    String name;
    String phone;
    String category;

    public GridItem(String name, String phone, String category) {
        this.name = name;
        this.phone = phone;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        phoneText.setText(gridItem.getPhone());

        return convertView;
    }
}
