package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.pape.ricettacolomisterioso.utils.Functions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    private static final String TAG = "ProductListAdapter";

    public interface OnItemInteractions {
        void onItemClick(Product product);
        void onItemClickAddToShoppingList(Product product);
    }

    private List<Product> products;
    private LayoutInflater layoutInflater;
    private OnItemInteractions onItemInteractions;

    public ProductListAdapter(Context context, List<Product> products,  OnItemInteractions onItemInteractions) {
        this.layoutInflater = LayoutInflater.from(context);
        this.products = products;
        this.onItemInteractions = onItemInteractions;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.product_list_item, parent, false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        ((ProductListViewHolder) holder).bind(products.get(position), this.onItemInteractions);
    }

    @Override
    public int getItemCount() {
        if(products != null)
            return products.size();
        else return 0;
    }

    public void setData(List<Product> data) {
        if (data != null) {
            this.products = data;
            notifyDataSetChanged();
        }
    }


    public static class ProductListViewHolder extends RecyclerView.ViewHolder {
        TextView product_name;
        TextView product_category;
        ImageView product_icon;
        ImageView addToShoppingListIcon;

        public ProductListViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.text_view_product_list_item_name);
            product_category = view.findViewById(R.id.text_view_product_list_item_category);
            product_icon = view.findViewById(R.id.image_view_product_list_item_category);
            addToShoppingListIcon = view.findViewById(R.id.image_view_product_list_item_add_shopping_list);
        }

        void bind(Product product, OnItemInteractions onItemInteractions) {

            product_name.setText(product.getProduct_name());
            product_category.setText(Functions.getProductCategoryString(itemView.getContext(), product.getCategory()));

            SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(product_icon.getContext());
            if(sharedPreferences.getBoolean("image_instead_of_icon", false) && product.getImageUrl() != null)
            {
                Picasso.get().load(product.getImageUrl()).into(product_icon);
            }
            else {
                product_icon.setImageDrawable(itemView.getResources().getDrawable(
                        product.getCategoryIconId(itemView.getContext())));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemInteractions.onItemClick(product);
                }
            });

            addToShoppingListIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemInteractions.onItemClickAddToShoppingList(product);
                }
            });

        }
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

    public void insertProduct(Product product) {
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
