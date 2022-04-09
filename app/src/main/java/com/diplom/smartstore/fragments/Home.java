package com.diplom.smartstore.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.adapters.CatalogAdapter;
import com.diplom.smartstore.adapters.HomeAdapter;
import com.diplom.smartstore.interfaces.ShowBackButton;
import com.diplom.smartstore.interfaces.ToolbarTitle;
import com.diplom.smartstore.model.App;
import com.diplom.smartstore.model.News;
import com.diplom.smartstore.model.Product;
import com.diplom.smartstore.model.Subcategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home extends Fragment {

    RecyclerView homeRecycler;
    HomeAdapter homeAdapter;
    Context context;

//    @Override
    //    public void onAttach(Context context) {
//        super.onAttach(context);
//        toolbarTitleCallback = (ToolbarTitle) context;
//        showBackButtonCallback = (ShowBackButton) context;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // load data
        List<Subcategory> subcategoryList = new ArrayList<>();
        subcategoryList.add(new Subcategory(1, "Кухня", "Kuhnya", "Dess",
                null, null, null));
        subcategoryList.add(new Subcategory(2, "Кухня2", "Kuhnya2", "Dess2",
                null, null, null));
        subcategoryList.add(new Subcategory(3, "Кухня", "Kuhnya", "Dess",
                null, null, null));
        subcategoryList.add(new Subcategory(4, "Кухня2", "Kuhnya2", "Dess2",
                null, null, null));
        subcategoryList.add(new Subcategory(5, "Кухня", "Kuhnya", "Dess",
                null, null, null));
        subcategoryList.add(new Subcategory(6, "Кухня2", "Kuhnya2", "Dess2",
                null, null, null));

        List<News> newsList = new ArrayList<>();
        newsList.add(new News(1, "Кухня", "Kuhnya", "Dess",
                null, null));
        newsList.add(new News(2, "Кухня", "Kuhnya", "Dess",
                null, null));
        newsList.add(new News(3, "Кухня", "Kuhnya", "Dess",
                null, null));
        newsList.add(new News(4, "Кухня", "Kuhnya", "Dess",
                null, null));
        newsList.add(new News(5, "Кухня", "Kuhnya", "Dess",
                null, null));

        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 2,
                100,null));
        productList.add(new Product(2, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 2,
                100,null));
        productList.add(new Product(3, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 2,
                100,null));
        productList.add(new Product(4, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 2,
                100,null));
        productList.add(new Product(5, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 2,
                100,null));

        App app = new App(null, productList, null, subcategoryList, null, newsList);


        homeRecycler = view.findViewById(R.id.homeListView);
        homeRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                RecyclerView.VERTICAL, false);
        homeRecycler.setLayoutManager(layoutManager);
        homeRecycler.setAdapter(new HomeAdapter(context, app, newsList, subcategoryList, productList));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
