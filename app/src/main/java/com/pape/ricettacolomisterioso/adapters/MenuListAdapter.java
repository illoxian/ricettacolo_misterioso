package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.DailyMenu;
import com.pape.ricettacolomisterioso.models.DailyRecipe;
import com.pape.ricettacolomisterioso.utils.Functions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuListViewHolder> {

    private static final String TAG = "MenuListAdapter";

    public interface OnItemInteractions {
        void onRecipeClick(DailyMenu dailyMenu, int slot);
        void onRecipeLongClick(DailyMenu dailyMenu, int slot, int adapterPosition);
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
        View view = this.layoutInflater.inflate(R.layout.menu_row, parent, false);
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
        ConstraintLayout item_layout;

        public MenuListViewHolder(View view) {
            super(view);
            item_name = view.findViewById(R.id.menu_row_day);
            recipe_cards = new ArrayList<>();
            recipe_cards.add(view.findViewById(R.id.menu_card1));
            recipe_cards.add(view.findViewById(R.id.menu_card2));
            recipe_cards.add(view.findViewById(R.id.menu_card3));
            recipe_cards.add(view.findViewById(R.id.menu_card4));
            item_layout = view.findViewById(R.id.menu_row_layout);
        }

        void bind(DailyMenu dailyMenu, OnItemInteractions onItemInteractions){
            SimpleDateFormat format = new SimpleDateFormat("EEEE\nd MMM", Locale.getDefault());
            item_name.setText(format.format(dailyMenu.getDay()));

            long dailyMenuTime = dailyMenu.getDay().getTime();
            long today = Functions.ExcludeTime(Calendar.getInstance().getTime()).getTime();
            Log.d(TAG, "bind: DailyMenuTime: " + dailyMenuTime +
                             "Today: " + today);

            if(dailyMenuTime == today){
                item_name.setTextColor(Functions.getThemeColor(itemView.getContext(), R.attr.colorSecondary));
                item_name.setTypeface(null, Typeface.BOLD);
                Log.d(TAG, "bind: è oggi");
            }
            else{
                Log.d(TAG, "bind: NON è oggi");
                item_name.setTextColor(Functions.getThemeColor(itemView.getContext(), R.attr.colorOnSurface));
                item_name.setTypeface(null, Typeface.NORMAL);
            }

            for(int i=0; i<recipe_cards.size(); i++){
                CardView card = recipe_cards.get(i);
                TextView tv = (TextView) card.getChildAt(0);

                DailyRecipe dailyRecipe = dailyMenu.getRecipes().get(i);
                String recipeString;

                if(dailyRecipe == null)
                    recipeString = null;
                else
                    recipeString = dailyRecipe.getRecipeName();

                if(recipeString == null){
                    tv.setText(R.string.menu_add_recipe);
                    tv.setTextColor(Functions.getThemeColor(itemView.getContext(), R.attr.colorOnSurface));
                    card.setCardBackgroundColor(Functions.getThemeColor(itemView.getContext(), R.attr.colorSurface));
                }
                else{
                    tv.setText(recipeString);
                    tv.setTextColor(Functions.getThemeColor(itemView.getContext(), R.attr.colorOnPrimary));
                    card.setCardBackgroundColor(Functions.getThemeColor(itemView.getContext(), R.attr.colorPrimaryVariant));
                    if(dailyRecipe.getRecipeComplex() != null){
                        card.setClickable(true);
                        card.setFocusable(true);

                        int[] attrs = new int[]{R.attr.selectableItemBackground};
                        TypedArray typedArray = itemView.getContext().obtainStyledAttributes(attrs);
                        int selectableItemBackground = typedArray.getResourceId(0, 0);
                        typedArray.recycle();

                        card.setForeground(itemView.getContext().getDrawable(selectableItemBackground));
                    }
                }


                final int slot = i;
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemInteractions.onRecipeClick(dailyMenu, slot);
                    }
                });
                card.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemInteractions.onRecipeLongClick(dailyMenu, slot, getAdapterPosition());
                        return true;
                    }
                });
            }
        }

    }

    public void update(int adapterPosition, DailyMenu dailyMenu){
        dailyMenus.set(adapterPosition, dailyMenu);
        notifyItemChanged(adapterPosition);
    }
}
