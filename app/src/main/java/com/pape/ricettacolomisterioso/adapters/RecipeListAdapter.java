package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.utils.Functions;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
    private static int inflateRecipeItem = R.layout.recipe_list_item;
    private List<Recipe> recipes;
    private LayoutInflater layoutInflater;
    private OnItemInteractions onItemInteractions;
    public RecipeListAdapter(Context context, List<Recipe> recipes, OnItemInteractions onItemInteractions) {
        this.recipes = recipes;
        this.layoutInflater = LayoutInflater.from(context);
        this.onItemInteractions = onItemInteractions;
    }

    @Override
    public int getItemCount() {
        if (recipes != null)
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


    public interface OnItemInteractions {
        void onItemClick(Recipe recipe, View view);
    }

    public static class RecipeListViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "RecipeListViewHolder";

        TextView recipeTitle;
        TextView recipeCategory;
        ImageView recipeIcon;

        public RecipeListViewHolder(View view) {
            super(view);
            recipeTitle = view.findViewById(R.id.textView_recipe_list_item_name);
            recipeCategory = view.findViewById(R.id.textView_recipe_list_item_category);
            recipeIcon = view.findViewById(R.id.image_view_recipe_list_item_category);

        }

        void bind(Recipe recipe, OnItemInteractions onItemInteractions) {
            Log.d(TAG, recipe.toString());
            recipeTitle.setText(recipe.getTitle());
            recipeCategory.setText(Functions.getRecipeCategoryString(itemView.getContext(),
                    recipe.getCategoryId()));

            recipeIcon.setImageDrawable(itemView.getResources().getDrawable(
                    recipe.getCategoryIconId(itemView.getContext())));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemInteractions.onItemClick(recipe, v);
                }
            });
        }
    }
}
