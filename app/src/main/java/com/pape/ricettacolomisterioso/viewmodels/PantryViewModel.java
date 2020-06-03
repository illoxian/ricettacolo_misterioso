package com.pape.ricettacolomisterioso.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.repositories.ProductsRepository;
import com.pape.ricettacolomisterioso.repositories.ShoppingListRepository;

import java.util.List;

public class PantryViewModel extends ViewModel {

    private MutableLiveData<List<Product>> mostExpiringProducts;
    private MutableLiveData<List<Product>> products;
    private MutableLiveData<Long> insertId;

    public MutableLiveData<List<Product>> getMostExpiringProducts(){
        if (mostExpiringProducts == null) {
            mostExpiringProducts = new MutableLiveData<>();
        }

        return mostExpiringProducts;
    }

    public MutableLiveData<List<Product>> getProducts() {
        if (products == null) {
            products = new MutableLiveData<>();
        }
        return products;
    }

    public MutableLiveData<List<Product>> getAllMostExpiringProducts(){
        ProductsRepository.getInstance().getMostExpiringProducts(mostExpiringProducts);
        return mostExpiringProducts;
    }

    public MutableLiveData<List<Product>> getAllProductsOrderByExpirationDate(){
        ProductsRepository.getInstance().getAllProductsOrderByExpirationDate(mostExpiringProducts);
        return mostExpiringProducts;
    }

    public MutableLiveData<List<Product>> getProductsSearched(String product_name){
        ProductsRepository.getInstance().getProductSearched(products, product_name);
        return products;
    }

    public MutableLiveData<Long> getInsertId() {
        if (insertId == null) {
            insertId = new MutableLiveData<>();
        }
        return insertId;
    }

    public void addProductToShoppingList(Item item){
        ShoppingListRepository.getInstance().addItem(item, getInsertId());
    }
    public void addProductToShoppingList(String itemName, int quantity){
        addProductToShoppingList(new Item(itemName, quantity, false));
    }
    public void addProductToShoppingList(String itemName){
        addProductToShoppingList(itemName, 1);
    }
}