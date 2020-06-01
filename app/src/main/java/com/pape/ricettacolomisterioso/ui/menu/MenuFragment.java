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
import com.pape.ricettacolomisterioso.viewmodels.MenuViewModel;

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

        mAdapter = new MenuListAdapter(getActivity(), model.getDays().getValue());
        binding.menuRecyclerview.setAdapter(mAdapter);

        model.getDays().observe(getViewLifecycleOwner(), new Observer<List<Date>>() {
            @Override
            public void onChanged(@Nullable List<Date> days) {
                Log.d(TAG, "onChanged: Items:" + days);
                binding.toolbarTitle.setText(model.getWeekRangeString());
                mAdapter.setData(model.getDays().getValue());
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
