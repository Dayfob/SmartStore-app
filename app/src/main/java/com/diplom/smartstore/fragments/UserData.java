package com.diplom.smartstore.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

public class UserData extends Fragment {

    TextView etName, etEmail, etPhone, etIIN;
    Button btnUpdate;
    LocalStorage localStorage;
    String name, email, phone, IIN;
    TextView titleToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_data, container, false);
        titleToolbar = getActivity().findViewById(R.id.toolBarTitle);
        localStorage = new LocalStorage(requireActivity());

        etName = view.findViewById(R.id.EtName);
        etEmail = view.findViewById(R.id.EtEmail);
        etPhone = view.findViewById(R.id.EtPhone);

        etIIN = view.findViewById(R.id.EtIIN);

        getUser();

        btnUpdate = view.findViewById(R.id.BtnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                phone = etPhone.getText().toString();
                IIN = etIIN.getText().toString();
                Log.d("http", "onClick: "+name+" "+email+" "+phone+" "+IIN);

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || IIN.isEmpty()) {
                    alertFail("Данные введены не полностью");
                } else {
                    updateUserData();
                }
            }
        });
        return view;
    }

    private void updateUserData() { // еще не готово, надо исправить

        JSONObject params = new JSONObject();
        try {
            params.put("name", name);
            params.put("email", email);
            params.put("phone", phone);
            params.put("iin", IIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = getString(R.string.api_server) + getString(R.string.updateUserData);

        Thread request = new Thread() {

            @Override
            public void run() {
                if (isAdded()) {
                    Http http = new Http(getActivity(), url);
                    http.setToken(true);
                    http.setMethod("POST");
                    http.setData(data);
                    Log.d("http", "===:> "+http);
                    http.send();
                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Integer code = http.getStatusCode();
                                if (code == 201 || code == 200) {
                                    try {
                                        JSONObject response = new JSONObject(http.getResponse());
                                        String msg = response.getString("message");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    FragmentManager fm = getParentFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();

                                    titleToolbar.setText("Профиль");
                                    ft.replace(R.id.content, new Account());
                                    ft.commit();

                                } else if (code == 422) {
                                    try {
                                        JSONObject response = new JSONObject(http.getResponse());
                                        String msg = response.getString("message");
                                        alertFail(msg);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    alertFail("Ошибка " + code);
                                }
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
                if (isAdded()) {
                    Http http = new Http(getActivity(), url);//getActivity изза фрагмента вместо активити
                    http.setToken(true);
                    http.send();
                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {//getActivity изза фрагмента вместо активити
                            @Override
                            public void run() {
                                Integer code = http.getStatusCode();
                                if (code == 200) {
                                    try {
                                        JSONArray response = new JSONArray(http.getResponse());
                                        Log.d("user", "run: " + response);
                                        String name = null;
                                        String email = null;
                                        String phoneNumber = null;
                                        String IIN = null;

                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject jsonobject = response.getJSONObject(i);
                                            name = jsonobject.getString("name");
                                            email = jsonobject.getString("email");
                                            phoneNumber = jsonobject.getString("phone_number");
                                            IIN = jsonobject.getString("iin");
                                        }

                                        if (name != null) {
                                            etName.setText(name);
                                        }
                                        if (email != null) {
                                            etEmail.setText(email);
                                        }
                                        if (phoneNumber != null) {
                                            etPhone.setText(phoneNumber);
                                        }
                                        if (IIN != null) {
                                            etIIN.setText(IIN);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    alertFail("Ошибка " + code);
                                }
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void alertSuccess(String s) {
        new AlertDialog.Builder(getActivity())
                .setMessage(s)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
