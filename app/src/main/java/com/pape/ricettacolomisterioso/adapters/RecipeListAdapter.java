package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
    private List<Recipe> recipes;
    private LayoutInflater layoutInflater;

    public interface OnItemInteractions {
        void onItemClick(Recipe recipe, View view);
    }

    private OnItemInteractions onItemInteractions;
    private static int inflateRecipeItem = R.layout.recipe_list_item;


    public RecipeListAdapter(Context context, List<Recipe> recipes,  OnItemInteractions onItemInteractions) {
        this.recipes = recipes;
        this.layoutInflater = LayoutInflater.from(context);
        this.onItemInteractions = onItemInteractions;
    }

    @Override
    public int getItemCount() {
        if(recipes != null)
            return recipes.size();
        else return 0;
    }

    public void setData(List<Recipe> data) {
        if (data != null) {
            this.recipes = data;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        ((RecipeListViewHolder) holder).bind(recipes.get(position), this.onItemInteractions);
    }


    public static class RecipeListViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "RecipeListViewHolder";

        TextView textViewRecipeTitle;
        TextView textViewRecipeCategory;
        ImageView item_icon;
        public RecipeListViewHolder(View view) {
            super(view);
            textViewRecipeTitle = view.findViewById(R.id.textView_recipe_list_item_name);
            textViewRecipeCategory = view.findViewById(R.id.textView_recipe_list_item_category);
            item_icon = view.findViewById(R.id.image_view_recipe_list_item_category);

        }

        void bind(Recipe recipe, OnItemInteractions onItemInteractions) {
            Log.d(TAG, recipe.toString());
            textViewRecipeTitle.setText(recipe.getRecipe_name());
            textViewRecipeCategory.setText(recipe.getCategory());

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(item_icon.getContext());
            if(sharedPreferences.getBoolean("image_instead_of_icon", false) && recipe.getImageUrl() != null)
            {
                File f = new File(recipe.getImageUrl());
                Picasso.get().load(f).into(item_icon);
            }
            else {
                item_icon.setImageDrawable(itemView.getResources().getDrawable(
                        recipe.getCategoryIconId(itemView.getContext())));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemInteractions.onItemClick(recipe, v);
                }
            });
        }
    }


    public void removeProductAt(int position) {
        recipes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, recipes.size());
    }

    public void insertProductAt(Recipe recipe, int position) {
        recipes.add(position, recipe);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, recipes.size());
    }
}
