package com.diplom.smartstore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.smartstore.R;
import com.diplom.smartstore.utils.LocalStorage;

public class Orders extends Fragment {

    TextView tvName, tvEmail, tvPhone, tvIIN;
    Button btnLogout;
    LocalStorage localStorage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);


        return view;
    }
}
