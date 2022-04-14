package com.diplom.smartstore.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.diplom.smartstore.R;
import com.diplom.smartstore.utils.Http;
import com.diplom.smartstore.utils.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Register extends Fragment {

    Context context;
    EditText etName, etEmail, etPassword, etConfirmation;
    Button btnRegister;
    String name, email, password, confirmation;
    TextView titleToolbar;
    LocalStorage localStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        titleToolbar = getActivity().findViewById(R.id.toolBarTitle);
//        localStorage = new LocalStorage(getActivity());
        localStorage = new LocalStorage(requireActivity());

        etName = view.findViewById(R.id.EtName);
        etEmail = view.findViewById(R.id.EtEmail);
        etPassword = view.findViewById(R.id.EtPassword);
        etConfirmation = view.findViewById(R.id.EtConfirmation);
        btnRegister = view.findViewById(R.id.BtnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRegister();
            }
        });
        return view;
    }

    private void checkRegister() {
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        confirmation = etConfirmation.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmation.isEmpty()) {
            alertFail("Данные введены не полностью");
        } else if (!password.equals(confirmation)) {
            alertFail("Пароли не совподают");
        } else {
            sendRegister();
        }
    }

    private void sendRegister() {
        JSONObject params = new JSONObject();
        try {
            params.put("name", name);
            params.put("email", email);
            params.put("password", password);
            params.put("device_name", android.os.Build.MODEL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = getString(R.string.api_server) + getString(R.string.register);

        Thread request = new Thread() {

            @Override
            public void run() {
                if (isAdded()) {
                    Http http = new Http(getActivity(), url);
                    http.setMethod("POST");
                    http.setData(data);
                    http.send();
                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Integer code = http.getStatusCode();
                                if (code == 201 || code == 200) {
                                    try {
                                        JSONObject response = new JSONObject(http.getResponse());
                                        String token = response.getString("token");
                                        localStorage.setToken(token);
                                        Log.d("token", "send: " + localStorage.getToken());

                                        FragmentManager fm = getParentFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();

                                        titleToolbar.setText("Профиль");
                                        ft.replace(R.id.content, new Account());
                                        ft.commit();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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

    private void alertFail(String s) {
        new AlertDialog.Builder(getActivity())
                .setMessage(s)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
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
