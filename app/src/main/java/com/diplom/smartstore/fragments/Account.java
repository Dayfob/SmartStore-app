package com.diplom.smartstore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import com.diplom.smartstore.R;

public class Account extends Fragment {

    TextView tvName, tvEmail, tvPhone;
    RelativeLayout rlUserData, rlUserOrders;
    Button btnLogout;
    TextView titleToolbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        titleToolbar = view.findViewById(R.id.toolBarTitle);

        tvName = view.findViewById(R.id.userNameTV);
        tvEmail = view.findViewById(R.id.userEmailTV);
        tvPhone = view.findViewById(R.id.userPhoneTV);

        rlUserData = view.findViewById(R.id.userMainInfoRL);
        rlUserOrders = view.findViewById(R.id.userOrderInfoRL);
        
        btnLogout = view.findViewById(R.id.BtnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        getUser();
        return view;
    }

    private void logout() {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        ft.replace(R.id.content, new Login());
        ft.commit();
        titleToolbar.setText("Логин");
    }

    private void getUser() {
        tvName.setText("");
        tvEmail.setText("");
        tvPhone.setText("");
    }
}
