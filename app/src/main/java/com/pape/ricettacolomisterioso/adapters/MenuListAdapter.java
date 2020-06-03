package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.DailyMenu;
import com.pape.ricettacolomisterioso.utils.Functions;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuListViewHolder> {

    private static final String TAG = "MenuListAdapter";

    public interface OnItemInteractions {
        void onRecipeClick(String recipe, Date day, int slot);
    }

    private List<DailyMenu> dailyMenus;
    private LayoutInflater layoutInflater;
    private  OnItemInteractions onItemInteractions;

    public MenuListAdapter(Context context, List<DailyMenu> dailyMenus, OnItemInteractions onItemInteractions) {
        this.layoutInflater = LayoutInflater.from(context);
        this.dailyMenus = dailyMenus;
        this.onItemInteractions = onItemInteractions;
    }

    @NonNull
    @Override
    public MenuListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.menu_row2, parent, false);
        return new MenuListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuListViewHolder holder, int position) {
        ((MenuListViewHolder) holder).bind(dailyMenus.get(position), onItemInteractions);
    }

    @Override
    public int getItemCount() {
        if(dailyMenus != null)
            return dailyMenus.size();
        else return 0;
    }

    public void setData(List<DailyMenu> data) {
        if (data != null) {
            this.dailyMenus = data;
            notifyDataSetChanged();
        }
    }


    public static class MenuListViewHolder extends RecyclerView.ViewHolder {
        TextView item_name;
        private List<CardView> recipe_cards;

        public MenuListViewHolder(View view) {
            super(view);
            item_name = view.findViewById(R.id.menu_row_day);
            recipe_cards = new ArrayList<>();
            recipe_cards.add(view.findViewById(R.id.menu_card1));
            recipe_cards.add(view.findViewById(R.id.menu_card2));
            recipe_cards.add(view.findViewById(R.id.menu_card3));
            recipe_cards.add(view.findViewById(R.id.menu_card4));
        }

        void bind(DailyMenu dailyMenu, OnItemInteractions onItemInteractions){
            SimpleDateFormat format = new SimpleDateFormat("EEEE\nd MMM", Locale.getDefault());
            item_name.setText(format.format(dailyMenu.getDay()));

            for(int i=0; i<recipe_cards.size(); i++){
                CardView card = recipe_cards.get(i);
                TextView tv = (TextView) card.getChildAt(0);
                String recipe;

                if(i<dailyMenu.getRecipes().size())
                    recipe = dailyMenu.getRecipes().get(i);
                else
                    recipe=null;

                if(recipe == null){
                    tv.setText(R.string.menu_add_recipe);
                    card.setCardBackgroundColor(Functions.getThemeColor(itemView.getContext(), R.attr.colorSurface));
                }
                else{
                    tv.setText(recipe);
                    card.setCardBackgroundColor(itemView.getResources().getColor(R.color.yellow_500_light));
                }


                final int slot = i;
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemInteractions.onRecipeClick(recipe, dailyMenu.getDay(), slot);
                    }
                });
            }
        }

    }
}
