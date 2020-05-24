package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Item;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {
    private List<Item> items;
    private LayoutInflater layoutInflater;

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        TextView item_name;
        TextView item_counter;

        public ShoppingListViewHolder(View view) {
            super(view);
            item_name =;  // bind the view in the layoutxml
            item_counter =; //same as above

        }

    }

    public ShoppingListAdapter(Context context, List<Item> items) {
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        holder.item_name.setText(items.get(position).getItemName());
        holder.item_counter.setText(items.get(position).getItemCounter());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            } // click on itemView to edit the item by a popup
        });
    }

}
