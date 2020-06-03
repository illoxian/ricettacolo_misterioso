package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Item;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {

    private static final String TAG = "ShoppingListAdapter";

    public interface OnItemInteractions {
        void onItemClick(Item item);
        void onItemClickDelete(Item item, int position);
        void onItemCheckChanged(Item item, boolean check, TextView textViewName);
    }

    private List<Item> items;
    private LayoutInflater layoutInflater;
    private OnItemInteractions onItemInteractions;

    public ShoppingListAdapter(Context context, List<Item> items, OnItemInteractions onItemInteractions) {
        this.layoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.onItemInteractions = onItemInteractions;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        ((ShoppingListViewHolder) holder).bind(items.get(position), this.onItemInteractions);
    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.size();
        else return 0;
    }

    public void setData(List<Item> data) {
        if (data != null) {
            this.items = data;
            notifyDataSetChanged();
        }
    }


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

        void bind(Item item, OnItemInteractions onItemInteractions){

            item_checkbox.setChecked(item.isSelected());
            item_name.setText(item.getItemName());
            item_quantity.setText(item.getQuantity()+" "+itemView.getContext().getResources().getString(R.string.measure_unit_piece_abbreviation));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemInteractions.onItemClick(item);
                }
            });

            item_image_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemInteractions.onItemClickDelete(item, getAdapterPosition());
                }
            });

            item_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onItemInteractions.onItemCheckChanged(item, isChecked, item_name);
                }
            });
        }

    }



    public void removeItemAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    public void insertItemAt(Item item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, items.size());
    }

    public void insertItem(Item item) {
        insertItemAt(item, items.size());
    }

    public void updateItem(Item item, int position) {
        items.set(position, item);
        notifyItemChanged(position);
    }

    public void moveItem(int fromPosition, int toPosition){
        Item item = items.get(fromPosition);
        items.remove(fromPosition);
        items.add(toPosition, item);

        notifyItemMoved(fromPosition, toPosition);
    }

    public void moveItemToEnd(int fromPosition){
        moveItem(fromPosition, items.size()-1);
    }

    public void moveItemToTop(int fromPosition){
        moveItem(fromPosition, 0);
    }

    public void setItemStrikethrough(boolean isChecked, TextView textViewName) {
        if(isChecked){
            textViewName.setPaintFlags(textViewName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            textViewName.setPaintFlags(textViewName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
