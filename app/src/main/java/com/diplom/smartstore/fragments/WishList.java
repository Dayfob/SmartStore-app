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
import com.diplom.smartstore.adapters.CartAdapter;
import com.diplom.smartstore.adapters.WishlistAdapter;
import com.diplom.smartstore.model.Product;

import java.util.ArrayList;
import java.util.List;

public class WishList extends Fragment {

    CartAdapter wishlistAdapter;
    RecyclerView wishlistRecycler;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        // load data
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 0, 2,
                100,null));
        productList.add(new Product(2, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 0, 2,
                100,null));
        productList.add(new Product(3, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 0, 2,
                100,null));
        productList.add(new Product(4, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 0, 2,
                100,null));
        productList.add(new Product(5, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 0, 2,
                100,null));

        // Add the following lines to create RecyclerView
        wishlistRecycler = view.findViewById(R.id.wishlistRecyclerView);
        wishlistRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                RecyclerView.VERTICAL, false);
        wishlistRecycler.setLayoutManager(layoutManager);
        wishlistRecycler.setAdapter(new WishlistAdapter(context, productList));

        return view;
    }

}
