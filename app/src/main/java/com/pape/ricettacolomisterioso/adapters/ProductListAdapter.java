package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.ui.pantry.ProductProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    private List<Product> products;
    private LayoutInflater layoutInflater;
    public static class ProductListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView product_name;
        TextView product_category;
        ImageView product_icon;
        public ProductListViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.text_view_product_list_item_name);
            product_category = view.findViewById(R.id.text_view_product_list_item_category);
            product_icon = view.findViewById(R.id.image_view_product_list_item_category);
        }
    }

    public ProductListAdapter(Context context, List<Product> products) {
        this.products = products;
        if(context != null)
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
        Product product = products.get(position);

        holder.product_name.setText(product.getProduct_name());
        holder.product_category.setText(product.getCategory());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(holder.product_icon.getContext());
        if(sharedPreferences.getBoolean("image_instead_of_icon", false) && product.getImageUrl() != null)
        {
            Picasso.get().load(product.getImageUrl()).into(holder.product_icon);
        }
        else {
            holder.product_icon.setImageDrawable(holder.itemView.getResources().getDrawable(
                    products.get(position).getCategoryIconId(holder.itemView.getContext())));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductProfileActivity.class);
                Bundle product = new Bundle();
                product.putParcelable("product", products.get(position));
                intent.putExtra("product", product);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public void removeProductAt(int position) {
        products.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, products.size());
    }

    public void insertProductAt(Product product, int position) {
        products.add(position, product);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, products.size());
    }

    public void insertItem(Product product) {
        insertProductAt(product, products.size());
    }

    public void updateProduct(Product product, int position) {
        products.set(position, product);
        notifyItemChanged(position);
    }

    public void moveProduct(int fromPosition, int toPosition){
        Product product = products.get(fromPosition);
        products.remove(fromPosition);
        products.add(toPosition, product);

        notifyItemMoved(fromPosition, toPosition);
    }
}
