package com.diplom.smartstore.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.diplom.smartstore.R;
import com.diplom.smartstore.utils.Http;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateOrderActivity extends AppCompatActivity {

    Context context = this;
    Toolbar toolbar;
    TextView titleToolbar;
    ConstraintLayout deliveryDataCL;
    RadioGroup paymentGroup;
    RadioButton rbDelivery, rbPickup, rbCard, rbCash;
    EditText City, Address, AdditionalInfo;
    String deliveryMethod = null, paymentMethod = null;
    Button buttonContinue;
    View view;
    String PUBLISH_KEY = "pk_test_51KvcFqIwdOpAH80orn1UPBSKFJVPcMgeGyMwnFLHaHuLyKxj9eN7g9O4Uyvd98GxJ8EYvmS3E3GyHeIG6bvgwPhH00Pp4nzyHQ";
    String SECRET_KEY = "sk_test_51KvcFqIwdOpAH80orZnTKwfhrww10bFcNdvW5QXe50PzHpBf7odkLcgJ41qBOvzHnSzOM612bkhWcCRKSBoRynFt00RK7x0ssO";
    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    String clientSecret;
    String clientEphemeral;
    String orderId;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PaymentConfiguration.init(
                getApplicationContext(),
                PUBLISH_KEY
        );
        setContentView(R.layout.activity_create_order);

        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Title
        titleToolbar = findViewById(R.id.toolBarTitle);
        titleToolbar.setText("Order processing");

        deliveryDataCL = findViewById(R.id.deliveryData);
        paymentGroup = findViewById(R.id.RgPaymentMethod);

        rbDelivery = findViewById(R.id.RbDeliveryToAddress);
        rbPickup = findViewById(R.id.RbPickup);
        rbCard = findViewById(R.id.RbCardPayment);
        rbCash = findViewById(R.id.RbCashPayment);

        rbDelivery.setOnClickListener(deliveryRbClickListener);
        rbPickup.setOnClickListener(deliveryRbClickListener);
        rbCard.setOnClickListener(paymentRbClickListener);
        rbCash.setOnClickListener(paymentRbClickListener);

        City = findViewById(R.id.EtCity);
        Address = findViewById(R.id.EtStreetHouse);
        AdditionalInfo = findViewById(R.id.EtAdditionalInfo);

        buttonContinue = findViewById(R.id.BtnContinue);
//        buttonContinue.setEnabled(false);

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        buttonContinue.setOnClickListener(v -> {
            // действие при нажатии кнопки "оформить заказ"
            if (deliveryMethod == null) {
                Toast.makeText(this, "You did not selected a delivery method", Toast.LENGTH_SHORT).show();
            } else if (deliveryMethod.equals("Delivery to address")) {
                if (City.getText().toString().equals("")) {
                    Toast.makeText(this, "You did not enter a city", Toast.LENGTH_SHORT).show();
                } else if (Address.getText().toString().equals("")) {
                    Toast.makeText(this, "You did not enter a street and house number", Toast.LENGTH_SHORT).show();
                } else {
                    String address = City.getText().toString() + ", " + Address.getText().toString();
                    String additionalInfo = AdditionalInfo.getText().toString();
                    if (paymentMethod == null) {
                        Toast.makeText(this, "You did not selected a payment method", Toast.LENGTH_SHORT).show();
                    } else {
                        sendOrderInfo(deliveryMethod, address, additionalInfo, paymentMethod);
                    }
                }
            } else if (deliveryMethod.equals("Pickup")) {
                String address = "";
                String additionalInfo = "";
                if (paymentMethod == null) {
                    Toast.makeText(this, "You did not selected a payment method", Toast.LENGTH_SHORT).show();
                } else {
                    sendOrderInfo(deliveryMethod, address, additionalInfo, paymentMethod);
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    View.OnClickListener deliveryRbClickListener = v -> {
        RadioButton rb = (RadioButton) v;
        switch (rb.getId()) {
            case R.id.RbDeliveryToAddress:
                deliveryMethod = "Delivery to address";
                deliveryDataCL.setVisibility(View.VISIBLE);
                break;
            case R.id.RbPickup:
                deliveryMethod = "Pickup";
                deliveryDataCL.setVisibility(View.GONE);
                break;
            default:
                deliveryMethod = null;
                break;
        }
    };

    @SuppressLint("NonConstantResourceId")
    View.OnClickListener paymentRbClickListener = v -> {
        RadioButton rb = (RadioButton) v;
        switch (rb.getId()) {
            case R.id.RbCardPayment:
                paymentMethod = "Card payment";
                break;
            case R.id.RbCashPayment:
                paymentMethod = "Cash Payment upon receipt";
                break;
            default:
                paymentMethod = null;
                break;
        }
    };

    private void sendOrderInfo(String deliveryMethod, String address, String additionalInfo, String paymentMethod) {
        String url = this.getString(R.string.api_server) + this.getString(R.string.createOrder);

        JSONObject params = new JSONObject();
        try {
            params.put("delivery_method", deliveryMethod);
            params.put("payment_method", paymentMethod);
            if (!address.equals("")) {
                params.put("address", address);
            }
            if (!additionalInfo.equals("")) {
                params.put("additional_information", additionalInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();

        Thread request = new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                Http http = new Http(context, url);
                http.setMethod("POST");
                http.setToken(true);
                http.setData(data);
                http.send();
                runOnUiThread(() -> {
                    Integer code = http.getStatusCode();
                    if (code == 201 || code == 200) {
                        try {
                            if (paymentMethod.equals("Card payment")){
                                JSONObject response = new JSONObject(http.getResponse());
                                JSONObject order = response.getJSONObject("order");
                                orderId = order.getString("id");
                                String clientId = response.getString("client_id");
                                clientSecret = response.getString("clientSecret");
                                JSONObject clientEphemeral = response.getJSONObject("clientEphemeral");
                                String ephemeralKey = clientEphemeral.getString("id");

                                paymentSheet.presentWithPaymentIntent(
                                        clientSecret,
                                        new PaymentSheet.Configuration("Smart Store, Inc.",
                                                new PaymentSheet.CustomerConfiguration(clientId, ephemeralKey)));
                            } else {
                                alertSuccessAndOut("Order created!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (code == 422) {
                        alertFail("Error 422");
                    } else {
                        alertFail("Error " + code);
                    }


                });
            }

        };
        request.start();
    }

    private void sendInvoice(){
        String url = this.getString(R.string.api_server) + this.getString(R.string.invoices);

        JSONObject params = new JSONObject();
        try {
            params.put("order_id", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();

        Thread request = new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                Http http = new Http(context, url);
                http.setMethod("POST");
                http.setToken(true);
                http.setData(data);
                http.send();
                runOnUiThread(() -> {
                    Integer code = http.getStatusCode();
                    if (code == 201 || code == 200) {
//                        alertSuccess("The Invoice will be sent by email");
                    } else if (code == 422) {
                        alertFail("Error 422");
                    } else {
                        alertFail("Error " + code);
                    }


                });
            }

        };
        request.start();
    }

    private void alertFail(String s) {
        new AlertDialog.Builder(this)
                .setMessage(s)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void alertSuccess(String s) {
        new AlertDialog.Builder(this)
                .setMessage(s)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        // implemented in the next steps
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            sendInvoice();
            alertSuccessAndOut("Payment complete!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.i("CreateOrder", "Payment canceled!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Throwable error = ((PaymentSheetResult.Failed) paymentSheetResult).getError();
            alertFail("Payment failed: " + error.getLocalizedMessage());
        }
    }

    private void alertSuccessAndOut(String s) {
        new AlertDialog.Builder(this)
                .setMessage(s)
                .setPositiveButton("OK", (dialog, id) -> {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }).show();
    }
}
