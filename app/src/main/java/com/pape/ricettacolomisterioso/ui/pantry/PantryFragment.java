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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.adapters.ExpiringProductListAdapter;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.viewmodels.ExpiringProductListViewModel;
import com.pape.ricettacolomisterioso.viewmodels.PantryViewModel;
import com.pape.ricettacolomisterioso.databinding.FragmentPantryBinding;
import java.util.ArrayList;
import java.util.List;

import static com.pape.ricettacolomisterioso.ui.pantry.ProductProfileActivity.TAG;

public class PantryFragment extends Fragment {

    private PantryViewModel pantryViewModel;
    private FragmentPantryBinding binding;
    private ExpiringProductListViewModel model;
    private MutableLiveData<List<Product>> liveData;
    private int NEW_PRODUCT_ADDED = 0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPantryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.app_bar_add){
            Log.d("PantryFragment", "onOptionsItemSelected: Add");
            startNewProductActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.expiringProductPreviewRecyclerView.setLayoutManager(layoutManager);
        model =  new ViewModelProvider(this).get(ExpiringProductListViewModel.class);
        final Observer<List<Product>> observer = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> product) {
                ExpiringProductListAdapter mAdapter = new ExpiringProductListAdapter(getActivity(), product);
                binding.expiringProductPreviewRecyclerView.setAdapter(mAdapter);
                Log.d(TAG, product.toString());
            }
        };
        liveData = model.getMostExpiringProduct();
        liveData.observe(requireActivity(), observer);
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
