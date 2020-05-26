package com.pape.ricettacolomisterioso.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.repositories.ShoppingListRepository;

public class ProductProfileViewModel extends ViewModel {

    private final String TAG= "ProductProfileViewModel";

    private MutableLiveData<Product> product;
    private MutableLiveData<Long> insertId;

    public MutableLiveData<Product> getProduct(){
        if (product == null) {
            product = new MutableLiveData<>();
        }
        return product;
    }

    public MutableLiveData<Long> getInsertId() {
        if (insertId == null) {
            insertId = new MutableLiveData<>();
        }
        return insertId;
    }

    public void addItemToShoppingList(Item item){
        ShoppingListRepository.getInstance().addItem(item, getInsertId());
    }
    public void addItemToShoppingList(String itemName, int quantity){
        addItemToShoppingList(new Item(itemName, quantity, false));
    }
    public void addItemToShoppingList(String itemName){
        addItemToShoppingList(new Item(itemName, 1, false));
    }



}
