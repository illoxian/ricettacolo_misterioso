package com.pape.ricettacolomisterioso.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.models.ProductDao;
import com.pape.ricettacolomisterioso.repositories.ProductsRepository;
import com.pape.ricettacolomisterioso.repositories.ShoppingListRepository;

import java.util.List;

public class ProductProfileViewModel extends ViewModel {

    private final String TAG= "ProductProfileViewModel";

    private MutableLiveData<Product> product;
    private MutableLiveData<Long> insertId;
    private MutableLiveData<Item> findItem;
    private MutableLiveData<Integer> deleteId;
    private MutableLiveData<Integer> quantity;

    public MutableLiveData<Integer> getQuantity() {
        if (quantity==null) {
            quantity = new MutableLiveData<>();
        }
        return quantity;
    }

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

    public MutableLiveData<Item> getFindItem() {
        if (findItem == null) {
            findItem = new MutableLiveData<>();
        }
        return findItem;
    }

    public MutableLiveData<Integer> getDeleteId() {
        if (deleteId == null) {
            deleteId = new MutableLiveData<>();
        }
        return deleteId;
    }

    public void delete(Product product){
        ProductsRepository.getInstance().delete(product, getDeleteId());
    }

    public void minusQuantity(Product product) {
        this.product = new MutableLiveData<>(product);
        ProductsRepository.getInstance().minus(this.product);
    }

    public void plusQuantity(Product product) {
        this.product = new MutableLiveData<>(product);
        ProductsRepository.getInstance().plus(this.product);
        Log.d(TAG, ""+ this.product.getValue().getQuantity());
    }
    public void findItemById(Product product) {
        this.product = new MutableLiveData<>(product);
        ProductsRepository.getInstance().getProductById(this.product);

    }
    public void addItemToShoppingList(Item item){
        ShoppingListRepository.getInstance().addItem(item, getInsertId());
    }
    public void addItemToShoppingList(String itemName, int quantity){
        addItemToShoppingList(new Item(itemName, quantity, false));
    }
    public void addItemToShoppingList(String itemName){
        addItemToShoppingList(itemName, 1);
    }

    public void findItemInShoppingList(String itemName){
        ShoppingListRepository.getInstance().findItemFromName(itemName, getFindItem());
    }

    public void deleteItemFromShoppingList(String itemName){
        ShoppingListRepository.getInstance().delete(itemName, getDeleteId());
    }



}
