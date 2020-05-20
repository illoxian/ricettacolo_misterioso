package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Product;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    private List<Product> products;
    private LayoutInflater layoutInflater;
    public static class ProductListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView product_name;
        TextView product_category;
        public ProductListViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.text_view_product_list_item_name);
            product_category = view.findViewById(R.id.text_view_product_list_item_category);
        }
    }

    public ProductListAdapter(Context context, List<Product> products) {
        this.products = products;
        layoutInflater = LayoutInflater.from(context);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.product_list_item, parent, false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        holder.product_name.setText(products.get(position).getProduct_name());
        holder.product_category.setText(products.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
