package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.viewmodels.ShoppingListViewModel;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {
    private List<Item> items;
    private LayoutInflater layoutInflater;
    private ShoppingListViewModel model;

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        CheckBox item_checkbox;
        TextView item_name;
        TextView item_quantity;
        ImageView item_image_delete;

        public ShoppingListViewHolder(View view) {
            super(view);
            item_name = view.findViewById(R.id.textView_shopping_list_item_name);
            item_quantity = view.findViewById(R.id.textView_shopping_list_item_quantity);
            item_checkbox = view.findViewById(R.id.checkBox_shopping_list_item);
            item_image_delete = view.findViewById(R.id.imageView_shopping_list_item_delete);
        }

    }

    public ShoppingListAdapter(Context context, List<Item> items) {
        this.items = items;
        model = ViewModelProviders.of((FragmentActivity) context).get(ShoppingListViewModel.class);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        Item item = items.get(position);
        holder.item_checkbox.setChecked(item.isSelected());
        holder.item_name.setText(item.getItemName());
        holder.item_quantity.setText(item.getQuantity()+"");
        holder.item_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.updateIsSelected(item.getId(), isChecked);
            }
        });
        holder.item_image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.delete(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
