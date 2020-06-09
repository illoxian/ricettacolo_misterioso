package com.pape.ricettacolomisterioso.ui.pantry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.adapters.ExpiringProductListAdapter;
import com.pape.ricettacolomisterioso.adapters.ProductListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentPantryBinding;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.viewmodels.PantryViewModel;

import java.util.ArrayList;
import java.util.List;

public class PantryFragment extends Fragment {

    private static String TAG = "PantryFragment";

    private PantryViewModel model;
    private ExpiringProductListAdapter expiringAdapter;
    private ProductListAdapter searchAdapter;

    private FragmentPantryBinding binding;

    private int NEW_PRODUCT_ADDED = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPantryBinding.inflate(getLayoutInflater());
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model =  new ViewModelProvider(this).get(PantryViewModel.class);

        getMostExpiringProducts();

        binding.expiringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(PantryFragmentDirections.showExpiringProductListAction());
            }
        });

        List<CardView> cardViews = new ArrayList<>();
        cardViews.add(binding.fruitsAndVegetablesCardView);
        cardViews.add(binding.meatCardView);
        cardViews.add(binding.fishCardView);
        cardViews.add(binding.pastaAndRiceCardView);
        cardViews.add(binding.milkAndCheeseCardView);
        cardViews.add(binding.iceCreamAndFrozenFoodCardView);
        cardViews.add(binding.breadAndPizzaCardView);
        cardViews.add(binding.breakfastAndSweetCardView);
        cardViews.add(binding.oilAndCondimentsCardView);
        cardViews.add(binding.waterAndDrinksCardView);
        cardViews.add(binding.seeAllCardView);
        for(CardView cardView: cardViews)
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //andare avanti aprendo fragmentProductList
                    int res = getCardViewStringRes(cardView);
                    PantryFragmentDirections.ShowFragmentProductListAction action = PantryFragmentDirections.showFragmentProductListAction(res);
                    Navigation.findNavController(view).navigate(action);

                }
            });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pantry_app_bar_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setIconified(true);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.categoryLayout.setVisibility(View.VISIBLE);
                binding.expiringCard.setVisibility(View.VISIBLE);
                binding.categoryProductTextview.setVisibility(View.VISIBLE);
                binding.pantryFragmentRecyclerView.setVisibility(View.GONE);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        onSearched(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        binding.categoryLayout.setVisibility(View.GONE);
                        binding.expiringCard.setVisibility(View.GONE);
                        binding.categoryProductTextview.setVisibility(View.GONE);
                        binding.pantryFragmentRecyclerView.setVisibility(View.VISIBLE);

                        onSearched(newText);
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.app_bar_add){
            Log.d(TAG, "onOptionsItemSelected: Add");
            startNewProductActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSearched(String newString){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.pantryFragmentRecyclerView.setLayoutManager(layoutManager);

        searchAdapter = new ProductListAdapter(getActivity(), model.getProducts().getValue(), new ProductListAdapter.OnItemInteractions() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(getContext(), ProductProfileActivity.class);
                Bundle productBundle = new Bundle();
                productBundle.putParcelable("product", product);
                intent.putExtra("product", productBundle);
                startActivity(intent);
            }

            @Override
            public void onItemClickAddToShoppingList(Product product) {
                model.addProductToShoppingList(product.getProduct_name());
                Snackbar snackbar = Snackbar.make(getView(), R.string.product_added_to_shopping_list, Snackbar.LENGTH_LONG);
                snackbar.setAnchorView(getActivity().findViewById(R.id.nav_view));
                snackbar.show();
            }
        });
        binding.pantryFragmentRecyclerView.setAdapter(searchAdapter);

        model.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                searchAdapter.setData(model.getProducts().getValue());
            }
        });

        model.getProductsSearched(newString);
    }

    private void startNewProductActivity(){
        Intent intent = new Intent(this.getActivity(), NewProductActivity.class);
        startActivityForResult(intent,NEW_PRODUCT_ADDED);
    }

    public int getCardViewStringRes(CardView cardView){
        int res = 0;
        switch (cardView.getId()){
            case R.id.fruits_and_vegetables_cardView:
                res =  R.string.pantry_categories_fruits_and_vegetables;
                break;
            case R.id.meat_cardView:
                res = R.string.pantry_categories_meat;
                break;
            case R.id.fish_cardView:
                res = R.string.pantry_categories_fish;
                break;
            case R.id.pasta_and_rice_cardView:
                res = R.string.pantry_categories_pasta_and_rice;
                break;
            case R.id.milk_and_cheese_cardView:
                res = R.string.pantry_categories_milk_and_cheese;
                break;
            case R.id.ice_cream_and_frozen_food_cardView:
                res = R.string.pantry_categories_ice_cream_and_frozen_food;
                break;
            case R.id.bread_and_pizza_cardView:
                res = R.string.pantry_categories_bread_and_pizza;
                break;
            case R.id.breakfast_and_sweet_cardView:
                res = R.string.pantry_categories_breakfast_and_sweets;
                break;
            case R.id.oil_and_condiments_cardView:
                res = R.string.pantry_categories_oil_and_condiments;
                break;
            case R.id.water_and_drinks_cardView:
                res = R.string.pantry_categories_water_and_drinks;
                break;
            case R.id.see_all_cardView:
                res = R.string.pantry_categories_see_all;
        }

        return res;

    }

    public void getMostExpiringProducts(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.expiringProductPreviewRecyclerView.setLayoutManager(layoutManager);

        expiringAdapter = new ExpiringProductListAdapter(getActivity(), model.getMostExpiringProducts().getValue(), new ExpiringProductListAdapter.OnItemInteractions() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(getActivity(), ProductProfileActivity.class);
                Bundle productBundle = new Bundle();
                productBundle.putParcelable("product", product);
                intent.putExtra("product", productBundle);
                startActivity(intent);
            }
        });
        binding.expiringProductPreviewRecyclerView.setAdapter(expiringAdapter);

        model.getMostExpiringProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                Log.d(TAG, "onChanged: Items:" + products);
                expiringAdapter.setData(model.getMostExpiringProducts().getValue());
            }
        });

        model.getAllMostExpiringProducts();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_PRODUCT_ADDED) {
            if(resultCode == Activity.RESULT_OK){
                long insertId = data.getLongExtra("insertId",-1);
                if(insertId>=0) {
                    Snackbar.make(getView(), R.string.new_product_toast_success, Snackbar.LENGTH_LONG).show();
                    getMostExpiringProducts();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
