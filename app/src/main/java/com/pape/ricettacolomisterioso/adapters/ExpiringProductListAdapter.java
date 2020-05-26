package com.pape.ricettacolomisterioso.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.ui.pantry.ProductProfileActivity;
import com.pape.ricettacolomisterioso.utils.Functions;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpiringProductListAdapter extends RecyclerView.Adapter<ExpiringProductListAdapter.ExpiringProductListViewHolder> {
    private List<Product> products;
    private LayoutInflater layoutInflater;
    private Context context;
    private static String TAG = "ExpiringProductListAdapter";
    public static class ExpiringProductListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView product_name;
        TextView product_category;
        TextView product_days_remains;
        ProgressBar product_progress_bar;
        ImageView product_icon;
        public ExpiringProductListViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.text_view_expiring_product_list_item_name);
            product_category = view.findViewById(R.id.text_view_expiring_product_list_item_category);
            product_days_remains = view.findViewById(R.id.expiring_item_value_text_view);
            product_progress_bar = view.findViewById(R.id.progressBar_expiring_item);
            product_icon = view.findViewById(R.id.image_view_expiring_product_list_item_category);
        }
    }
    public ExpiringProductListAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ExpiringProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.expiring_product_list_item, parent, false);
        return new ExpiringProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpiringProductListViewHolder holder, int position) {
        Product product = products.get(position);

        holder.product_name.setText(product.getProduct_name());
        holder.product_category.setText(product.getCategory());
        Date expiring = product.getExpirationDate();
        Date purchase_date = product.getPurchaseDate();
        Date today = Calendar.getInstance().getTime();

        expiring = Functions.ExcludeTime(expiring);
        purchase_date = Functions.ExcludeTime(purchase_date);
        Date today_not_time = Functions.ExcludeTime(today);

        int daysRemaining = Functions.time_in_day_remain(expiring,today_not_time);
        if(daysRemaining > 0) {
            int progress = Functions.percentual_for_bar(purchase_date,expiring,today); //in progress bar Today is used with time to prevent progress bar fully empty
            String daysRemainingString = daysRemaining + " " + context.getString(R.string.remaining_day);
            holder.product_days_remains.setText(daysRemainingString);
            holder.product_progress_bar.setProgress(progress);
        } else if(daysRemaining == 0){
            holder.product_days_remains.setText(context.getString(R.string.product_expired_today));
            holder.product_progress_bar.setProgress(100);
        }else {
            String ExpiredFromXDays = context.getString(R.string.product_expired_from) + Math.abs(daysRemaining) + context.getString(R.string.days);
            holder.product_days_remains.setText(ExpiredFromXDays);
            holder.product_progress_bar.setProgress(100);
        }

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
}
