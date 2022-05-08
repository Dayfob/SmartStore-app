package com.diplom.smartstore.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class Login extends Fragment {

    EditText etEmail, etPassword;
    Button btnLogin, btnRegister;
    String email, password;
    TextView titleToolbar;
    LocalStorage localStorage;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        titleToolbar = requireActivity().findViewById(R.id.toolBarTitle);
//        localStorage = new LocalStorage(getActivity());
        localStorage = new LocalStorage(requireActivity());


        etEmail = view.findViewById(R.id.EtEmail);
        etPassword = view.findViewById(R.id.EtPassword);
        btnLogin = view.findViewById(R.id.BtnLogin);
        btnRegister = view.findViewById(R.id.BtnRegister);

        btnLogin.setOnClickListener(v -> checkLogin());

        btnRegister.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            titleToolbar.setText("Registration");
            ft.replace(R.id.content, new Register());
            ft.addToBackStack("registration");
            ft.commit();
        });

        return view;
    }

    private void checkLogin() {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            alertFail("Данные введены не полностью");
        } else {
            sendLogin();
        }
    }

    private void sendLogin() {
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
            params.put("device_name", android.os.Build.MODEL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String url = getString(R.string.api_server) + getString(R.string.login);

        Thread request = new Thread() {

            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (isAdded()) {
                    Http http = new Http(getActivity(), url);//getActivity изза фрагмента вместо активити
                    http.setMethod("POST");
                    http.setData(data);
                    http.send();
                    if (isAdded()) {
                        //getActivity изза фрагмента вместо активити
                        requireActivity().runOnUiThread(() -> {
                            Integer code = http.getStatusCode();
                            if (code == 200) {
                                try {
                                    JSONObject response = new JSONObject(http.getResponse());
                                    String token = response.getString("token");
                                    localStorage.setToken(token);

                                    FragmentManager fm = getParentFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();

                                    titleToolbar.setText("Profile");
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
                            } else if (code == 401) {
                                try {
                                    JSONObject response = new JSONObject(http.getResponse());
                                    String msg = response.getString("message");
                                    alertFail(msg);
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
