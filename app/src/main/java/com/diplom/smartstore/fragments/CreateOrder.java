package com.diplom.smartstore.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.adapters.CartAdapter;
import com.diplom.smartstore.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateOrder extends Fragment {

    Context context;
    TextView titleToolbar;
    RadioButton rbDelivery, rbPickup, rbCard, rbCash;
    EditText City, Address, AdditionalInfo;
    Button buttonContinue;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_create, container, false);
        titleToolbar = requireActivity().findViewById(R.id.toolBarTitle);
        titleToolbar.setText("Order processing");

        rbDelivery = view.findViewById(R.id.RbDeliveryToAddress);
        rbPickup = view.findViewById(R.id.RbPickup);
        rbCard = view.findViewById(R.id.RbCartPayment);
        rbCash = view.findViewById(R.id.RbCashPayment);

        City = view.findViewById(R.id.EtCity);
        Address = view.findViewById(R.id.EtStreetHouse);
        AdditionalInfo = view.findViewById(R.id.EtAdditionalInfo);

        buttonContinue = view.findViewById(R.id.BtnContinue);
        buttonContinue.setOnClickListener(v -> {
            // действие при нажатии кнопки "оформить заказ"
            Log.d("TAG", "оформить заказ: clicked " + v);

        });



        return view;
    }

}
