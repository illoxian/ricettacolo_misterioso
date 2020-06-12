package com.pape.ricettacolomisterioso.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.repositories.ShoppingListRepository;

import java.util.List;

public class ShoppingListViewModel extends ViewModel {
    private final String TAG= "ShoppingListViewModel";

    private MutableLiveData<List<Item>> items;
    private MutableLiveData<Long> insertId;
    private MutableLiveData<Integer> updateId;
    private MutableLiveData<Integer> deleteId;

    public ShoppingListViewModel() {
    }

    public MutableLiveData<List<Item>> getItems() {
        if (items == null) {
            items = new MutableLiveData<>();
        }
        return items;
    }

    public MutableLiveData<Long> getInsertId() {
        if (insertId == null) {
            insertId = new MutableLiveData<>();
        }
        return insertId;
    }

    public MutableLiveData<Integer> getUpdateId() {
        if (updateId == null) {
            updateId = new MutableLiveData<>();
        }
        return updateId;
    }

    public MutableLiveData<Integer> getDeleteId() {
        if (deleteId == null) {
            deleteId = new MutableLiveData<>();
        }
        return deleteId;
    }

    public void getAllItems(){
        ShoppingListRepository.getInstance().getItems(getItems());
    }

    public void addItem(Item item){
        ShoppingListRepository.getInstance().addItem(item, getInsertId());
    }

    public void updateIsSelected(Long itemId, boolean isSelected){
        ShoppingListRepository.getInstance().updateIsSelected(itemId, isSelected, getUpdateId());
    }

    public void delete(Item item){
        ShoppingListRepository.getInstance().delete(item.getItemName(), getDeleteId());
    }
}
