//package com.diplom.smartstore.fragments;
//
//import android.animation.ObjectAnimator;
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.Fragment;
//
//import com.diplom.smartstore.R;
//import com.diplom.smartstore.utils.Http;
//import com.stripe.android.EphemeralKey;
//import com.stripe.android.paymentsheet.PaymentSheet;
//import com.stripe.android.paymentsheet.PaymentSheetResult;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.Objects;
//
//import okhttp3.*;
//
//public class CreateOrder extends Fragment {
//
//    Context context;
//    TextView titleToolbar;
//    ConstraintLayout deliveryDataCL;
//    RadioGroup paymentGroup;
//    RadioButton rbDelivery, rbPickup, rbCard, rbCash;
//    EditText City, Address, AdditionalInfo;
//    String deliveryMethod = null, paymentMethod = null;
//    Button buttonContinue;
//    View view;
//    String PUBLISH_KEY = "pk_test_51KvcFqIwdOpAH80orn1UPBSKFJVPcMgeGyMwnFLHaHuLyKxj9eN7g9O4Uyvd98GxJ8EYvmS3E3GyHeIG6bvgwPhH00Pp4nzyHQ";
//    String SECRET_KEY = "sk_test_51KvcFqIwdOpAH80orZnTKwfhrww10bFcNdvW5QXe50PzHpBf7odkLcgJ41qBOvzHnSzOM612bkhWcCRKSBoRynFt00RK7x0ssO";
//    PaymentSheet paymentSheet;
//    String paymentIntentClientSecret;
//    String clientSecret;
//    String clientEphemeral;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_order_create, container, false);
//
//        titleToolbar = requireActivity().findViewById(R.id.toolBarTitle);
//        titleToolbar.setText("Order processing");
//        deliveryDataCL = view.findViewById(R.id.deliveryData);
//        paymentGroup = view.findViewById(R.id.RgPaymentMethod);
//
//        rbDelivery = view.findViewById(R.id.RbDeliveryToAddress);
//        rbPickup = view.findViewById(R.id.RbPickup);
//        rbCard = view.findViewById(R.id.RbCardPayment);
//        rbCash = view.findViewById(R.id.RbCashPayment);
//
//        rbDelivery.setOnClickListener(deliveryRbClickListener);
//        rbPickup.setOnClickListener(deliveryRbClickListener);
//        rbCard.setOnClickListener(paymentRbClickListener);
//        rbCash.setOnClickListener(paymentRbClickListener);
//
//        City = view.findViewById(R.id.EtCity);
//        Address = view.findViewById(R.id.EtStreetHouse);
//        AdditionalInfo = view.findViewById(R.id.EtAdditionalInfo);
//
//        buttonContinue = view.findViewById(R.id.BtnContinue);
//        buttonContinue.setEnabled(false);
//
//        paymentSheet = new PaymentSheet(requireActivity(), this::onPaymentSheetResult);
//        buttonContinue.setOnClickListener(v -> {
//            // действие при нажатии кнопки "оформить заказ"
//            if (deliveryMethod == null) {
//                Toast.makeText(getActivity(), "You did not selected a delivery method", Toast.LENGTH_SHORT).show();
//            } else if (paymentMethod == null) {
//                Toast.makeText(getActivity(), "You did not selected a payment method", Toast.LENGTH_SHORT).show();
//            } else {
//                if (City.getText().toString().equals("")) {
//                    Toast.makeText(getActivity(), "You did not enter a city", Toast.LENGTH_SHORT).show();
//                } else if (Address.getText().toString().equals("")) {
//                    Toast.makeText(getActivity(), "You did not enter a street and house number", Toast.LENGTH_SHORT).show();
//                }
//                String address = City.getText().toString() + ", " + Address.getText().toString();
//                String additionalInfo = AdditionalInfo.getText().toString();
//                Log.d("CreateOrder", " before");
//                sendOrderInfo(deliveryMethod, address, additionalInfo, paymentMethod);
//                Log.d("CreateOrder", " after");
//            }
//        });
//
////        fetchPaymentIntent();
//        return view;
//    }
//
//    @SuppressLint("NonConstantResourceId")
//    View.OnClickListener deliveryRbClickListener = v -> {
//        RadioButton rb = (RadioButton) v;
//        switch (rb.getId()) {
//            case R.id.RbDeliveryToAddress:
//                deliveryMethod = "Delivery to address";
//                deliveryDataCL.setVisibility(View.VISIBLE);
//                break;
//            case R.id.RbPickup:
//                deliveryMethod = "Pickup";
//                deliveryDataCL.setVisibility(View.GONE);
//                break;
//            default:
//                deliveryMethod = null;
//                break;
//        }
//    };
//
//    @SuppressLint("NonConstantResourceId")
//    View.OnClickListener paymentRbClickListener = v -> {
//        RadioButton rb = (RadioButton) v;
//        switch (rb.getId()) {
//            case R.id.RbCardPayment:
//                paymentMethod = "Card payment";
//                break;
//            case R.id.RbCashPayment:
//                paymentMethod = "Cash Payment upon receipt";
//                break;
//            default:
//                paymentMethod = null;
//                break;
//        }
//    };
//
//    private void sendOrderInfo(String deliveryMethod, String address, String additionalInfo, String paymentMethod) {
//        String url = requireActivity().getString(R.string.api_server) + requireActivity().getString(R.string.createOrder);
//
//        JSONObject params = new JSONObject();
//        try {
//            params.put("delivery_method", deliveryMethod);
//            params.put("payment_method", paymentMethod);
//            params.put("address", address);
//            if (!additionalInfo.equals("")) {
//                params.put("additional_information", additionalInfo);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String data = params.toString();
//
//        Thread request = new Thread() {
//            @Override
//            public void run() {
//                Http http = new Http(getActivity(), url);
//                http.setMethod("POST");
//                http.setToken(true);
//                http.setData(data);
//                http.send();
//                requireActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Integer code = http.getStatusCode();
//                        if (code == 201 || code == 200) {
//                            try {
//                                JSONObject response = new JSONObject(http.getResponse());
//                                clientSecret = response.getJSONObject("clientSecret").toString();
//                                clientEphemeral = response.getJSONObject("clientEphemeral").toString();
//                                JSONObject order = response.getJSONObject("order");
//                                String userId = response.getJSONObject("user_id").toString();
//
////                                PaymentSheet.Configuration configuration = new PaymentSheet.Configuration("Smart Store, Inc.");
////                                PaymentSheet.Configuration customerConfiguration = new PaymentSheet.CustomerConfiguration(userId, clientEphemeral);
////                                paymentSheet.presentWithPaymentIntent(
////                                        clientSecret,
////                                        new PaymentSheet.Configuration("Smart Store, Inc.",
////                                                new PaymentSheet.CustomerConfiguration(userId, clientEphemeral)));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        } else if (code == 422) {
//                            alertFail("Error 422");
//                        } else {
//                            alertFail("Error " + code);
//                        }
//
//
//                    }
//                });
//            }
//
//        };
//        request.start();
//    }
//
//    private void alertFail(String s) {
//        new AlertDialog.Builder(getActivity())
//                .setMessage(s)
//                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
//                .show();
//    }
//
//    private void alertSuccess(String s) {
//        new AlertDialog.Builder(getActivity())
//                .setMessage(s)
//                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
//    }
//
////    private void fetchPaymentIntent() {
//////        final String shoppingCartContent = "{\"items\": [ {\"id\":\"xl-tshirt\"}]}"; // что это?
//////
//////        final RequestBody requestBody = RequestBody.create(
//////                MediaType.toString("application/json; charset=utf-8"),
//////                shoppingCartContent
//////        );
//////
//////        Request request = new Request.Builder()
//////                .url(requireActivity().getString(R.string.api_server) + "create-payment-intent") // посмотреть что делать с адресом
//////                .post(requestBody)
//////                .build();
//////
//////        new OkHttpClient()
//////                .newCall(request)
//////                .enqueue(new Callback() {
//////                    @Override
//////                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
//////                        alertFail("Failed to load data. Error: " + e.toString());
//////                    }
//////
//////                    @Override
//////                    public void onResponse(
//////                            @NonNull Call call,
//////                            @NonNull Response response
//////                    ) throws IOException {
//////                        if (!response.isSuccessful()) {
//////                            alertFail("Failed to load page. Error: " + response.toString());
//////                        } else {
//////                            final JSONObject responseJson = parseResponse(response.body());
//////                            paymentIntentClientSecret = responseJson.optString("clientSecret");
//////                            requireActivity().runOnUiThread(() -> buttonContinue.setEnabled(true));
//////                            Log.i("CreateOrder", "Retrieved PaymentIntent");
//////                        }
//////                    }
//////                });
////    }
////
//////    private JSONObject parseResponse(ResponseBody responseBody) {
//////        if (responseBody != null) {
//////            try {
//////                return new JSONObject(responseBody.string());
//////            } catch (IOException | JSONException e) {
//////                Log.e(TAG, "Error parsing response", e);
//////            }
//////        }
//////
//////        return new JSONObject();
//////    }
//
//    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
//        // implemented in the next steps
//        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
//            alertSuccess("Payment complete!");
//        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
//            Log.i("CreateOrder", "Payment canceled!");
//        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
//            Throwable error = ((PaymentSheetResult.Failed) paymentSheetResult).getError();
//            alertFail("Payment failed: " + error.getLocalizedMessage());
//        }
//    }
//
//}
