package com.diplom.smartstore.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.adapters.WishlistAdapter;
import com.diplom.smartstore.model.Attribute;
import com.diplom.smartstore.model.Brand;
import com.diplom.smartstore.model.Category;
import com.diplom.smartstore.model.Product;
import com.diplom.smartstore.model.Subcategory;
import com.diplom.smartstore.utils.Http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WishList extends Fragment implements WishlistAdapter.OnProductListener {

    RecyclerView wishlistRecycler;
    Context context;
    TextView titleToolbar;
    List<Product> productList = new ArrayList<>();
    View view;
    WishList WishList = this;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        titleToolbar = requireActivity().findViewById(R.id.toolBarTitle);

        // load data
        getWishlistProducts();

        return view;
    }

    @Override
    public void onProductClick(int position) {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        com.diplom.smartstore.fragments.Product productFragment = new com.diplom.smartstore.fragments.Product();
        Bundle bundle = new Bundle();
        bundle.putInt("id", productList.get(position).getId());
        productFragment.setArguments(bundle);
        ft.replace(R.id.content, productFragment);
        ft.addToBackStack("product");
        ft.commit();
    }

    private void getWishlistProducts() {
        String url = getString(R.string.api_server) + getString(R.string.getWishlist);

        Thread request = new Thread() {
            @Override
            public void run() {
                if (isAdded()) {
                    Http http = new Http(getActivity(), url);//getActivity изза фрагмента вместо активити
                    http.setToken(true);
                    http.send();
                    if (isAdded()) {
                        //getActivity изза фрагмента вместо активити
                        requireActivity().runOnUiThread(() -> {
                            Integer code = http.getStatusCode();
                            if (code == 200) {
                                try {
                                    // получаем JSON ответ
                                    JSONObject response = new JSONObject(http.getResponse());

                                    // выбираем из ответа JSON массив продуктов
                                    JSONArray jsonarray = response.getJSONArray("wishlistProducts");

                                    // перебираем массив
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        JSONObject wishlistProduct = jsonarray.getJSONObject(i); // продукт листа желаний
                                        JSONObject product = wishlistProduct.getJSONObject("item_id"); // продукт

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
                                                attributesProduct,
                                                product.getBoolean("liked")));
                                    }

                                    // Add the following lines to create RecyclerView
                                    wishlistRecycler = view.findViewById(R.id.wishlistRecyclerView);
                                    wishlistRecycler.setHasFixedSize(true);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                                            RecyclerView.VERTICAL, false);
                                    wishlistRecycler.setLayoutManager(layoutManager);
                                    wishlistRecycler.setAdapter(new WishlistAdapter(context, productList, WishList, getActivity()));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if (code == 401){
                                alertFail("Please login");
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
