package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuListViewHolder> {

    private static final String TAG = "MenuListAdapter";

    private List<Date> days;
    private LayoutInflater layoutInflater;

    public MenuListAdapter(Context context, List<Date> days) {
        this.layoutInflater = LayoutInflater.from(context);
        this.days = days;
    }

    @NonNull
    @Override
    public MenuListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.menu_row, parent, false);
        return new MenuListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuListViewHolder holder, int position) {
        ((MenuListViewHolder) holder).bind(days.get(position));
    }

    @Override
    public int getItemCount() {
        if(days != null)
            return days.size();
        else return 0;
    }

    public void setData(List<Date> data) {
        if (data != null) {
            this.days = data;
            notifyDataSetChanged();
        }
    }


    public static class MenuListViewHolder extends RecyclerView.ViewHolder {
        TextView item_name;

        public MenuListViewHolder(View view) {
            super(view);
            item_name = view.findViewById(R.id.menu_row_day);
        }

        void bind(Date day){
            SimpleDateFormat format = new SimpleDateFormat("EEEE\nd MMM", Locale.getDefault());
            item_name.setText(format.format(day));
        }

    }
}
