package com.diplom.smartstore.fragments;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.diplom.smartstore.R;
import com.diplom.smartstore.utils.Http;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateOrder extends Fragment {

    Context context;
    TextView titleToolbar;
    ConstraintLayout deliveryDataCL;
    RadioGroup paymentGroup;
    RadioButton rbDelivery, rbPickup, rbCard, rbCash;
    EditText City, Address, AdditionalInfo;
    String deliveryMethod = null, paymentMethod = null;
    Button buttonContinue;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_create, container, false);
        titleToolbar = requireActivity().findViewById(R.id.toolBarTitle);
        titleToolbar.setText("Order processing");
        deliveryDataCL = view.findViewById(R.id.deliveryData);
        paymentGroup = view.findViewById(R.id.RgPaymentMethod);

        rbDelivery = view.findViewById(R.id.RbDeliveryToAddress);
        rbPickup = view.findViewById(R.id.RbPickup);
        rbCard = view.findViewById(R.id.RbCardPayment);
        rbCash = view.findViewById(R.id.RbCashPayment);

        rbDelivery.setOnClickListener(deliveryRbClickListener);
        rbPickup.setOnClickListener(deliveryRbClickListener);
        rbCard.setOnClickListener(paymentRbClickListener);
        rbCash.setOnClickListener(paymentRbClickListener);

        City = view.findViewById(R.id.EtCity);
        Address = view.findViewById(R.id.EtStreetHouse);
        AdditionalInfo = view.findViewById(R.id.EtAdditionalInfo);

        buttonContinue = view.findViewById(R.id.BtnContinue);
        buttonContinue.setOnClickListener(v -> {
            // действие при нажатии кнопки "оформить заказ"
            if (deliveryMethod == null){
                Toast.makeText(getActivity(), "You did not selected a delivery method", Toast.LENGTH_SHORT).show();
            } else if (paymentMethod == null){
                Toast.makeText(getActivity(), "You did not selected a payment method", Toast.LENGTH_SHORT).show();
            } else {
                if (City.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "You did not enter a city", Toast.LENGTH_SHORT).show();
                } else if (Address.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "You did not enter a street and house number", Toast.LENGTH_SHORT).show();
                }
                String address = City.getText().toString() + ", " + Address.getText().toString();
                String additionalInfo = AdditionalInfo.getText().toString();
                sendOrderInfo(deliveryMethod, address, additionalInfo, paymentMethod);
            }





        });
        return view;
    }

    View.OnClickListener deliveryRbClickListener = v -> {
        RadioButton rb = (RadioButton)v;
        switch (rb.getId()) {
            case R.id.RbDeliveryToAddress: deliveryMethod = "Delivery to address";
                deliveryDataCL.setVisibility(View.VISIBLE);
//                ObjectAnimator animationDown = ObjectAnimator.ofFloat(paymentGroup, "translationY", -100f);
//                animationDown.setDuration(2000);
//                animationDown.start();
                break;
            case R.id.RbPickup: deliveryMethod = "Pickup";
//                deliveryDataCL.setVisibility(View.INVISIBLE);
                deliveryDataCL.setVisibility(View.GONE);
//                ObjectAnimator animationUp = ObjectAnimator.ofFloat(paymentGroup, "translationY", 100f);
//                animationUp.setDuration(2000);
//                animationUp.start();
                break;
            default: deliveryMethod = null;
                break;
        }
    };

    View.OnClickListener paymentRbClickListener = v -> {
        RadioButton rb = (RadioButton)v;
        switch (rb.getId()) {
            case R.id.RbCardPayment: paymentMethod = "Card payment";
                break;
            case R.id.RbCashPayment: paymentMethod = "Cash Payment upon receipt";
                break;
            default: paymentMethod = null;
                break;
        }
    };

    private void sendOrderInfo(String deliveryMethod, String address, String additionalInfo, String paymentMethod) {
        String url = requireActivity().getString(R.string.api_server) + requireActivity().getString(R.string.createOrder);

        JSONObject params = new JSONObject();
        try {
            params.put("delivery_method", deliveryMethod);
            params.put("payment_method", paymentMethod);
            params.put("address", address);
            if (!additionalInfo.equals("")) {params.put("additional_information", additionalInfo);}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();

        Thread request = new Thread() {
            @Override
            public void run() {
                Http http = new Http(getActivity(), url);
                http.setMethod("POST");
                http.setToken(true);
                http.setData(data);
                http.send();
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if (code == 201 || code == 200) {

                        } else if (code == 422) {
                            alertFail("Error 422");
                        } else {
                            alertFail("Error " + code);
                        }
                    }
                });
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
