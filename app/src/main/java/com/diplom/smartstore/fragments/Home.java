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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.adapters.HomeAdapter;
import com.diplom.smartstore.model.App;
import com.diplom.smartstore.model.Attribute;
import com.diplom.smartstore.model.Brand;
import com.diplom.smartstore.model.Category;
import com.diplom.smartstore.model.News;
import com.diplom.smartstore.model.Product;
import com.diplom.smartstore.model.Subcategory;
import com.diplom.smartstore.utils.Http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    RecyclerView homeRecycler;
    Context context;
    TextView titleToolbar;
    View view;

    private final List<Subcategory> subcategoryList = new ArrayList<>();
    private final List<News> newsList = new ArrayList<>();
    private final List<Product> productList = new ArrayList<>();
    private final List<Product> wishlistProductList = new ArrayList<>();
    private App app;
//    @Override
    //    public void onAttach(Context context) {
//        super.onAttach(context);
//        toolbarTitleCallback = (ToolbarTitle) context;
//        showBackButtonCallback = (ShowBackButton) context;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        titleToolbar = view.findViewById(R.id.toolBarTitle);

        // load data
        getHome();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void getHome() {
        String urlNews = getString(R.string.api_server) + getString(R.string.getAllNews);
        String urlSubcategories = getString(R.string.api_server) + getString(R.string.getAllSubcategories);
        String urlProducts = getString(R.string.api_server) + getString(R.string.getAllProducts);
        String urlWishlist = getString(R.string.api_server) + getString(R.string.getWishlist);

        // запрос для получения нвостей
        Thread request = new Thread() {
            @Override
            public void run() {
                if (isAdded()) {
                    Http httpNews = new Http(getActivity(), urlNews);//getActivity изза фрагмента вместо активити
                    httpNews.setToken(true);
                    httpNews.send();
                    Http httpSubcategories = new Http(getActivity(), urlSubcategories);//getActivity изза фрагмента вместо активити
                    httpSubcategories.setToken(true);
                    httpSubcategories.send();
                    Http httpProducts = new Http(getActivity(), urlProducts);//getActivity изза фрагмента вместо активити
                    httpProducts.setToken(true);
                    httpProducts.send();
                    Http httpWishlist = new Http(getActivity(), urlWishlist);//getActivity изза фрагмента вместо активити
                    httpWishlist.setToken(true);
                    httpWishlist.send();

                    if (isAdded()) {
                        //getActivity изза фрагмента вместо активити
                        requireActivity().runOnUiThread(() -> {
                            Integer codeResponseNews = httpNews.getStatusCode();
                            if (codeResponseNews == 200) {
                                try {
                                    // получаем JSON ответ
                                    JSONArray response = new JSONArray(httpNews.getResponse());
                                    // перебираем массив
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject news = response.getJSONObject(i); // новость
                                        // добавляем новость в массив нвостей
                                        newsList.add(new News(
                                                news.getInt("id"),
                                                news.getString("title"),
                                                news.getString("slug"),
                                                news.getString("text"),
                                                news.getString("image_url"),
                                                null));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                alertFail("Error " + codeResponseNews);
                            }

                            Integer codeResponseSubcategories = httpSubcategories.getStatusCode();
                            if (codeResponseSubcategories == 200) {
                                try {
                                    // получаем JSON ответ
                                    JSONArray response = new JSONArray(httpSubcategories.getResponse());
                                    // перебираем массив
                                    for (int j = 0; j < response.length(); j++) {
                                        JSONObject subcategory = response.getJSONObject(j); // подкатегория
                                        JSONArray subcategoryAttributes = subcategory.getJSONArray("attributes"); // список аттрибутов подкатегории

                                        List<Attribute> attributesSubcategory = new ArrayList<>();

                                        for (int p = 0; p < subcategoryAttributes.length(); p++) {
                                            // добавляем аттрибут в массив аттрибутов подкатегории
                                            attributesSubcategory.add(new Attribute(p, subcategoryAttributes.get(p).toString(), null));
                                        }

                                        // добавляем подктегорию в массив подктегорий категории
                                        subcategoryList.add(new Subcategory(
                                                subcategory.getInt("id"),
                                                subcategory.getString("name"),
                                                subcategory.getString("slug"),
                                                subcategory.getString("description"),
                                                subcategory.getString("image_url"), // image
                                                attributesSubcategory));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                alertFail("Error " + codeResponseSubcategories);
                            }

                            Integer code = httpWishlist.getStatusCode();
                            if (code == 200) {
                                try {
                                    // получаем JSON ответ
                                    JSONObject response = new JSONObject(httpWishlist.getResponse());

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
                                        wishlistProductList.add(new Product(product.getInt("id"),
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

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if (code == 401) {
                                alertFail("Please login");
                            } else {
                                alertFail("Error " + code);
                            }

                            Integer codeResponseProducts = httpProducts.getStatusCode();
                            if (codeResponseProducts == 200) {
                                try {
                                    // получаем JSON ответ
                                    JSONArray response = new JSONArray(httpProducts.getResponse());

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

                                        boolean inCart = false;

                                        for (Product wishlistProduct : wishlistProductList) {
                                            if (product.getInt("id") == wishlistProduct.getId()) {
                                                inCart = true;
                                            }
                                        }

                                        // добавляем товар в массив товаров
                                        productList.add(new Product(product.getInt("id"),
                                                product.getString("name"),
                                                product.getString("slug"),
                                                product.getString("image_url"),
                                                product.getString("description"),
                                                new Brand(productBrand.getInt("id"), productBrand.getString("name"),
                                                        productBrand.getString("slug"), productBrand.getString("description")),
                                                new Category(productCategory.getInt("id"), productCategory.getString("name"),
                                                        productCategory.getString("slug"), null, null),
                                                new Subcategory(productSubcategory.getInt("id"), productSubcategory.getString("name"),
                                                        productSubcategory.getString("slug"), productSubcategory.getString("description"),
                                                        null, attributesSubcategory), //image
                                                0,
                                                product.getInt("amount_left"),
                                                product.getInt("price"),
                                                attributesProduct,
                                                inCart));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                alertFail("Error " + codeResponseProducts);
                            }

                            app = new App(null, productList, null, subcategoryList, null, newsList);

                            // Add the following lines to create RecyclerView
                            homeRecycler = view.findViewById(R.id.homeModulesRecyclerView);
                            homeRecycler.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                                    RecyclerView.VERTICAL, false);
                            homeRecycler.setLayoutManager(layoutManager);
                            homeRecycler.setAdapter(new HomeAdapter(context, app, newsList, subcategoryList, productList, getActivity()));

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
