package com.diplom.smartstore.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.adapters.CatalogAdapter;
import com.diplom.smartstore.model.Category;
import com.diplom.smartstore.model.Subcategory;

import java.util.ArrayList;
import java.util.List;

public class Catalog extends Fragment {

    RecyclerView catalogRecycler;
    CatalogAdapter catalogAdapter;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        // load data
        List<Subcategory> subcategoryList = new ArrayList<>();
        subcategoryList.add(new Subcategory(1, "Кухня", "Kuhnya", "Dess",
                null, null, null));
        subcategoryList.add(new Subcategory(2, "Кухня2", "Kuhnya2", "Dess2",
                null, null, null));
        subcategoryList.add(new Subcategory(3, "Кухня2", "Kuhnya2", "Dess2",
                null, null, null));
        subcategoryList.add(new Subcategory(4, "Кухня2", "Kuhnya2", "Dess2",
                null, null, null));
        subcategoryList.add(new Subcategory(5, "Кухня2", "Kuhnya2", "Dess2",
                null, null, null));
        subcategoryList.add(new Subcategory(6, "Кухня2", "Kuhnya2", "Dess2",
                null, null, null));
        subcategoryList.add(new Subcategory(7, "Кухня2", "Kuhnya2", "Dess2",
                null, null, null));
        subcategoryList.add(new Subcategory(8, "Кухня2", "Kuhnya2", "Dess2",
                null, null, null));


        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Дом", "Dom", "Каталог дом",
                subcategoryList, null));
        categoryList.add(new Category(2, "Сад", "Sad", "Каталог сад",
                subcategoryList, null));
        categoryList.add(new Category(3, "Сад2", "Sad2", "Каталог сад2",
                subcategoryList, null));
        categoryList.add(new Category(4, "Сад3", "Sad2", "Каталог сад2",
                subcategoryList, null));
        categoryList.add(new Category(5, "Сад4", "Sad2", "Каталог сад2",
                subcategoryList, null));


        // Add the following lines to create RecyclerView
        catalogRecycler = view.findViewById(R.id.categoriesRecyclerView);
        catalogRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                RecyclerView.VERTICAL, false);
        catalogRecycler.setLayoutManager(layoutManager);
        catalogRecycler.setAdapter(new CatalogAdapter(context, categoryList));

        return view;
    }

}
