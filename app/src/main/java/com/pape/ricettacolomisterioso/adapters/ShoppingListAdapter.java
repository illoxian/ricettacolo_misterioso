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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.viewmodels.ShoppingListViewModel;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {

    private static final String TAG = "ShoppingListAdapter";

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    private List<Item> items;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public ShoppingListAdapter(Context context, List<Item> items, OnItemClickListener onItemClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        ((ShoppingListViewHolder) holder).bind(items.get(position), this.onItemClickListener);
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

        void bind(Item item, OnItemClickListener onItemClickListener){

            item_checkbox.setChecked(item.isSelected());
            item_name.setText(item.getItemName());
            item_quantity.setText(item.getQuantity()+" "+itemView.getContext().getResources().getString(R.string.measure_unit_piece_abbreviation));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(item);
                }
            });

            /*item_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    model.updateIsSelected(item.getId(), isChecked);
                    if(isChecked){
                        holder.item_name.setPaintFlags(holder.item_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        //moveItemToEnd(holder.getAdapterPosition()); //move to the end
                    }
                    else{
                        holder.item_name.setPaintFlags(holder.item_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        //moveItemToTop(holder.getAdapterPosition()); //move to the top
                    }
                }
            });

            holder.item_image_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.delete(item);
                    removeItemAt(holder.getAdapterPosition());
                }
            });*/
        }

    }










    public void removeItemAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    public void insertItem(Item item) {
        int position = items.size();
        items.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, items.size());
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
}
