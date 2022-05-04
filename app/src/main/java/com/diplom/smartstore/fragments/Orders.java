package com.diplom.smartstore.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.adapters.OrderListAdapter;
import com.diplom.smartstore.adapters.WishlistAdapter;
import com.diplom.smartstore.model.Attribute;
import com.diplom.smartstore.model.Brand;
import com.diplom.smartstore.model.Category;
import com.diplom.smartstore.model.Order;
import com.diplom.smartstore.model.Product;
import com.diplom.smartstore.model.Subcategory;
import com.diplom.smartstore.model.User;
import com.diplom.smartstore.utils.Http;
import com.diplom.smartstore.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Orders extends Fragment implements OrderListAdapter.OnOrderListener {


    RecyclerView orderListRecycler;
    LocalStorage localStorage;
    TextView titleToolbar;
    List<Order> orderList = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    Orders orders = this;
    Context context;
    View view;


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_orders_list, container, false);
        titleToolbar = requireActivity().findViewById(R.id.toolBarTitle);
        titleToolbar.setText("Orders");

        getOrders();

        return view;
    }

    private void getOrders() {
        String url = getString(R.string.api_server) + getString(R.string.getOrders);

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
                                        // получаем JSON ответ
                                        JSONArray response = new JSONArray(http.getResponse());

                                        // перебираем массив
                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject order = response.getJSONObject(i); // заказ + список продуктов
                                            JSONObject orderInfo = order.getJSONObject("order"); // информация заказа
                                            JSONObject user = orderInfo.getJSONObject("user");

                                            JSONArray orderProducts = order.getJSONArray("orderProducts");
                                            Log.d("test", " " + orderProducts);
                                            for (int j = 0; j < orderProducts.length(); j++) {
                                                JSONObject orderProduct = orderProducts.getJSONObject(j); // продукт заказа
                                                JSONObject product = orderProduct.getJSONObject("item_id"); // продукт заказа
                                                JSONObject productBrand = product.getJSONObject("brand_id"); // бренд продукта (аттрибут объекта продукт)
                                                JSONObject productCategory = product.getJSONObject("category_id"); // категория продукта (аттрибут объекта продукт)
                                                JSONObject productSubcategory = product.getJSONObject("subcategory_id"); // подкатегория продукта (аттрибут объекта продукт)


                                                JSONArray productSubcategoryAttributes = productSubcategory.getJSONArray("attributes"); // список аттрибутов подкатегории
                                                JSONArray productAttributes = productSubcategory.getJSONArray("attributes"); // список аттрибутов подкатегории

                                                // перебираем список
                                                List<Attribute> attributesSubcategory = new ArrayList<>();
                                                List<Attribute> attributesProduct = new ArrayList<>();

                                                for (int p = 0; p < productSubcategoryAttributes.length(); p++) {
                                                    // добавляем аттрибут в массив аттрибутов подкатегории
                                                    Attribute productSubcategoryAttribute = new Attribute(p, productSubcategoryAttributes.get(p).toString(), null);
                                                    attributesSubcategory.add(productSubcategoryAttribute);
                                                    // добавляем аттрибут в массив аттрибутов товара
                                                    Attribute productAttribute = new Attribute(p, productSubcategoryAttributes.get(p).toString(), productAttributes.get(p).toString());
                                                    attributesProduct.add(productAttribute);
                                                }

                                                // добавляем товар в массив товаров списка желаний
                                                productList.add(new Product(product.getInt("id"),
                                                        product.getString("name"),
                                                        product.getString("slug"),
                                                        product.getString("image_url"),
                                                        product.getString("description"),
                                                        new Brand(productBrand.getInt("id"), productBrand.getString("name"),
                                                                productBrand.getString("slug"), productBrand.getString("description")),
                                                        new Category(productCategory.getInt("id"), productCategory.getString("name"),
                                                                productCategory.getString("slug"), productCategory.getString("description"), null),
                                                        new Subcategory(productSubcategory.getInt("id"), productSubcategory.getString("name"),
                                                                productSubcategory.getString("slug"), productSubcategory.getString("description"),
                                                                null, attributesSubcategory), // image
                                                        orderProduct.getInt("item_amount"),
                                                        product.getInt("amount_left"),
                                                        product.getInt("price"),
                                                        attributesProduct,
                                                        null));
                                            }

                                            orderList.add(new Order(orderInfo.getInt("id"),
                                                    orderInfo.getString("status"),
                                                    new User(user.getInt("id"),
                                                            user.getString("iin"),
                                                            user.getString("name"),
                                                            user.getString("phone_number"),
                                                            user.getString("email"),
                                                            null,
                                                            null
                                                    ),
                                                    orderInfo.getInt("total_price"),
                                                    orderInfo.getBoolean("is_sent"),
                                                    orderInfo.getBoolean("is_paid"),
                                                    orderInfo.getString("payment_method"),
                                                    orderInfo.getString("delivery_method"),
                                                    orderInfo.getString("address"),
                                                    orderInfo.getString("additional_information"),
                                                    orderInfo.getInt("delivery_price"),
                                                    orderInfo.getString("created_at").substring(0,10),
                                                    productList
                                            ));
                                        }

                                        // Add the following lines to create RecyclerView
                                        orderListRecycler = view.findViewById(R.id.orderListRecyclerView);
                                        orderListRecycler.setHasFixedSize(true);
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                                                RecyclerView.VERTICAL, false);
                                        orderListRecycler.setLayoutManager(layoutManager);
                                        orderListRecycler.setAdapter(new OrderListAdapter(context, orderList, orders, getActivity()));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (code == 401){
                                    alertFail("Пожалуйста авторизуйтесь");
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

    @Override
    public void onOrderClick(int position) {

    }
}
