package com.pape.ricettacolomisterioso.ui.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pape.ricettacolomisterioso.adapters.MenuListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentMenuBinding;
import com.pape.ricettacolomisterioso.models.DailyMenu;
import com.pape.ricettacolomisterioso.viewmodels.MenuViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuFragment extends Fragment {

    private static final String TAG = "ShoppingListFragment";

    private MenuViewModel model;
    private MenuListAdapter mAdapter;

    private FragmentMenuBinding binding;



    public MenuFragment() {
    }

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //model = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ShoppingListViewModel.class);
        model =  new ViewModelProvider(this).get(MenuViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.menuRecyclerview.setLayoutManager(layoutManager);

        mAdapter = new MenuListAdapter(getActivity(), model.getDailyMenus().getValue(), new MenuListAdapter.OnItemInteractions() {
            @Override
            public void onRecipeClick(String recipe, Date day, int slot) {
                if(recipe==null){
                    Log.d(TAG, "onRecipeClick: Aggiungi una ricetta");
                    List<String> list = new ArrayList<>();
                    list.add("aaaa");
                    model.insert(new DailyMenu(day, list));
                    model.ChangeWeek(0);
                }
                else{
                    Log.d(TAG, "onRecipeClick: "+ recipe);
                }
            }
        });
        binding.menuRecyclerview.setAdapter(mAdapter);

        model.getDailyMenus().observe(getViewLifecycleOwner(), new Observer<List<DailyMenu>>() {
            @Override
            public void onChanged(@Nullable List<DailyMenu> dailyMenus) {
                Log.d(TAG, "onChanged: Items:" + dailyMenus);
                binding.toolbarTitle.setText(model.getWeekRangeString());
                mAdapter.setData(model.getDailyMenus().getValue());
            }
        });

        binding.imageViewWeekPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.ChangeWeek(-1);
            }
        });

        binding.imageViewWeekNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.ChangeWeek(1);
            }
        });

        model.ChangeWeek(0);
    }
}
