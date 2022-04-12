package com.diplom.smartstore.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class Login extends Fragment {

    Context context;
    EditText etEmail, etPassword;
    Button btnLogin, btnRegister;
    String email, password;
    TextView titleToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        titleToolbar = view.findViewById(R.id.toolBarTitle);

        etEmail = view.findViewById(R.id.EtEmail);
        etPassword = view.findViewById(R.id.EtPassword);
        btnLogin = view.findViewById(R.id.BtnLogin);
        btnRegister = view.findViewById(R.id.BtnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();


                ft.replace(R.id.content, new Register());
                ft.commit();
                titleToolbar.setText("Регистрация");
            }
        });

        return view;
    }

    private void checkLogin() {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()){
            alertFail("Данные введены не полностью");
        } else {
            sendLogin();
        }
    }

    private void sendLogin() {
        Toast.makeText(context, "Send", Toast.LENGTH_SHORT).show();
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        ft.replace(R.id.content, new Login());
        ft.commit();
        titleToolbar.setText("Профиль");
    }

    private void alertFail(String s) {
        new AlertDialog.Builder(context)
                .setMessage(s)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
