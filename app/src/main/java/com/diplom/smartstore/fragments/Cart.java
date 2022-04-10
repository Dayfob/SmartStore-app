package com.diplom.smartstore.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.adapters.CartAdapter;
import com.diplom.smartstore.model.Product;

import java.util.ArrayList;
import java.util.List;

public class Cart extends Fragment {

    CartAdapter cartAdapter;
    RecyclerView cartRecycler;
    Context context;
    TextView productsAmount;
    TextView productsPrice;
    TextView productsTotalPrice;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // load data
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 1,2,
                100,null));
        productList.add(new Product(2, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 1, 2,
                100,null));
        productList.add(new Product(3, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 1, 2,
                100,null));
        productList.add(new Product(4, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 1, 2,
                100,null));
        productList.add(new Product(5, "Кухня", "Kuhnya", "Dess",
                null, null, null, null, 1, 2,
                100,null));

        com.diplom.smartstore.model.Cart cart = new com.diplom.smartstore.model.Cart(productList);

        int productsAmountSum = 0;
        int productsPriceSum = 0;
        int productsTotalPriceSum = 0;
        for (Product product : cart.getProducts()){
            productsAmountSum += product.getAmountCart();
            productsPriceSum += product.getAmountCart() * product.getPrice();
        }
        productsTotalPriceSum = productsPriceSum;

        // Add the following lines to create RecyclerView
        cartRecycler = view.findViewById(R.id.cartRecyclerView);
        cartRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                RecyclerView.VERTICAL, false);
        cartRecycler.setLayoutManager(layoutManager);
        cartRecycler.setAdapter(new CartAdapter(context, cart));

        productsAmount = view.findViewById(R.id.cartProductAmountTextView);
        productsPrice = view.findViewById(R.id.cartProductPriceAmountTextView);
        productsTotalPrice = view.findViewById(R.id.cartProductTotalPriceAmountTextView);

        productsAmount.setText(productsAmountSum + " шт.");
        productsPrice.setText(productsPriceSum + " тенге");
        productsTotalPrice.setText(productsTotalPriceSum + " тенге");
        return view;
    }
}
