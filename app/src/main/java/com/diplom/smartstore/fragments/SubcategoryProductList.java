package com.diplom.smartstore.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.adapters.SubcategoryProductListAdapter;
import com.diplom.smartstore.model.Attribute;
import com.diplom.smartstore.model.Brand;
import com.diplom.smartstore.model.Category;
import com.diplom.smartstore.model.Product;
import com.diplom.smartstore.model.Subcategory;
import com.diplom.smartstore.utils.Http;
import com.diplom.smartstore.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubcategoryProductList extends Fragment implements SubcategoryProductListAdapter.OnProductListener {

    private static final String TAG = "onClick";
    RecyclerView recyclerView;
    Context context;
    TextView titleToolbar;
    LocalStorage localStorage;
    List<Product> productList = new ArrayList<>();
    View view;
    private SubcategoryProductList subcategoryProductList;
    int SubcategoryId = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wishlist, container, false); // используется тот же лейаут что и у вишлиста
        titleToolbar = requireActivity().findViewById(R.id.toolBarTitle);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            SubcategoryId = bundle.getInt("id", 1);
        }


        // load data
        getProducts();

        return view;
    }

    @Override
    public void onProductClick(int position) {
        Log.d(TAG, "onProductClick: clicked " + position);
    }

    private void getProducts() {
        String url = getString(R.string.api_server) + getString(R.string.getSubcategoryProducts) + SubcategoryId;

        Log.d("test", "hereeeee " + url);
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
                                            JSONObject product = response.getJSONObject(i); // продукт

                                            JSONObject productBrand = product.getJSONObject("brand_id"); // бренд продукта (аттрибут объекта продукт)
                                            JSONObject productCategory = product.getJSONObject("category_id"); // категория продукта (аттрибут объекта продукт)
                                            JSONObject productSubcategory = product.getJSONObject("subcategory_id"); // подкатегория продукта (аттрибут объекта продукт)

                                            JSONArray productSubcategoryAttributes = productSubcategory.getJSONArray("attributes"); // список аттрибутов подкатегории
                                            JSONArray productAttributes = productSubcategory.getJSONArray("attributes"); // список аттрибутов подкатегории

                                            // перебираем список
                                            List<Attribute> attributesSubcategory = new ArrayList<>();
                                            List<Attribute> attributesProduct = new ArrayList<>();

                                            for (int j = 0; j < productSubcategoryAttributes.length(); j++) {
                                                // добавляем аттрибут в массив аттрибутов подкатегории
                                                Attribute productSubcategoryAttribute = new Attribute(j, productSubcategoryAttributes.get(j).toString(), null);
                                                attributesSubcategory.add(productSubcategoryAttribute);
                                                // добавляем аттрибут в массив аттрибутов товара
                                                Attribute productAttribute = new Attribute(j, productSubcategoryAttributes.get(j).toString(), productAttributes.get(j).toString());
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
                                                    0,
                                                    product.getInt("amount_left"),
                                                    product.getInt("price"),
                                                    attributesProduct));
                                        }

                                        // Add the following lines to create RecyclerView
                                        recyclerView = view.findViewById(R.id.wishlistRecyclerView);
                                        recyclerView.setHasFixedSize(true);
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                                                RecyclerView.VERTICAL, false);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setAdapter(new SubcategoryProductListAdapter(context, productList, subcategoryProductList));

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
}