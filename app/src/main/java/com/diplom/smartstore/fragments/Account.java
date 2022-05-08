package com.diplom.smartstore.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.diplom.smartstore.R;
import com.diplom.smartstore.utils.Http;
import com.diplom.smartstore.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Account extends Fragment {

    TextView tvName, tvEmail, tvPhone;
    RelativeLayout rlUserData, rlUserOrders;
    Button btnLogout;
    TextView titleToolbar;
    LocalStorage localStorage;


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        titleToolbar = requireActivity().findViewById(R.id.toolBarTitle);

        tvName = view.findViewById(R.id.userNameTV);
        tvEmail = view.findViewById(R.id.userEmailTV);
        tvPhone = view.findViewById(R.id.userPhoneTV);

        rlUserData = view.findViewById(R.id.userMainInfoRL);
        rlUserOrders = view.findViewById(R.id.userOrderInfoRL);

        btnLogout = view.findViewById(R.id.BtnLogout);

        localStorage = new LocalStorage(requireActivity());

        rlUserData.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            titleToolbar.setText("Personal data");

            ft.replace(R.id.content, new UserData());
            ft.addToBackStack("userData");
            ft.commit();
        });

        rlUserOrders.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            titleToolbar.setText("Order history");

            ft.replace(R.id.content, new Orders());
            ft.addToBackStack("orders");
            ft.commit();
        });

        btnLogout.setOnClickListener(v -> logout());

        getUser();
        return view;
    }

    private void logout() {
        String url = getString(R.string.api_server) + getString(R.string.logout);

        Thread request = new Thread() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (isAdded()) {
                    Http http = new Http(getActivity(), url);//getActivity изза фрагмента вместо активити
                    http.setToken(true);
                    http.setMethod("POST");
                    http.send();
                    if (isAdded()) {
                        //getActivity изза фрагмента вместо активити
                        requireActivity().runOnUiThread(() -> {
                            Integer code = http.getStatusCode();
                            if (code == 200) {
                                localStorage.setToken("");
                                FragmentManager fm = getParentFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();

                                titleToolbar.setText("Login");

                                ft.replace(R.id.content, new Login());
                                ft.commit();
                            } else {
                                alertFail("Error " + code);
                            }
                        });
                    }
                }
            }
        };
        request.start();
    }

    private void getUser() {
        String url = getString(R.string.api_server) + getString(R.string.userData);

        Thread request = new Thread() {
            @Override
            public void run() {
                if (isAdded()) {// вроде проверят добвален ли фрагмент
                    Http http = new Http(getActivity(), url);//getActivity изза фрагмента вместо активити
                    http.setToken(true);
                    http.send();
                    if (isAdded()) {
                        //getActivity изза фрагмента вместо активити
                        requireActivity().runOnUiThread(() -> {
                            Integer code = http.getStatusCode();
                            if (code == 200) {
                                try {
                                    JSONArray response = new JSONArray(http.getResponse());
                                    String name = null;
                                    String email = null;
                                    String phoneNumber = null;

                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject jsonobject = response.getJSONObject(i);
                                        name = jsonobject.getString("name");
                                        email = jsonobject.getString("email");
                                        phoneNumber = jsonobject.getString("phone_number");
                                    }

                                    tvName.setText(name);
                                    tvEmail.setText(email);
                                    tvPhone.setText(phoneNumber);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                alertFail("Error " + code);
                            }
                        });
                    }
                }
            }
        };
        request.start();
    }

    private void alertFail(String s) {
        new AlertDialog.Builder(getActivity())
                .setMessage(s)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void alertSuccess(String s) {
        new AlertDialog.Builder(getActivity())
                .setMessage(s)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
    }
}
