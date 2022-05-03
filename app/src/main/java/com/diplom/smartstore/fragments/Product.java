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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.activities.MainActivity;
import com.diplom.smartstore.adapters.ProductAttributesListAdapter;
import com.diplom.smartstore.adapters.SubcategoryProductListAdapter;
import com.diplom.smartstore.model.Attribute;
import com.diplom.smartstore.model.Brand;
import com.diplom.smartstore.model.Category;
import com.diplom.smartstore.model.Subcategory;
import com.diplom.smartstore.utils.Http;
import com.diplom.smartstore.utils.LoadImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Product extends Fragment {


    TextView tvProductName, tvProductNumber, tvProductPrice, tvProductCartAmount, tvProductDescription;
    ImageView productImage, buttonLike;
    Button btnCartMinus, btnCartPlus, btnCartAdd;
    RecyclerView attributesRecycler;
    View view;
    TextView titleToolbar;
    ImageView backToolbar;
    int productId;
    int cartAmount = 1;
    private List<com.diplom.smartstore.model.Product> wishlistProductList = new ArrayList<>();
    com.diplom.smartstore.model.Product product;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_product, container, false);
        // отображается но не возвращает назад
//        titleToolbar = requireActivity().findViewById(R.id.toolBarTitle);
//        backToolbar = requireActivity().findViewById(R.id.backButton);
//        backToolbar.setVisibility(View.VISIBLE);
//        backToolbar.setOnClickListener(v -> {
//            requireActivity().onBackPressed();
//            requireActivity().onBackPressed();
//            getFragmentManager().popBackStack();
//        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productId = bundle.getInt("id", 1);
        }

        productImage = view.findViewById(R.id.productImage);
        buttonLike = view.findViewById(R.id.productHeart);
        tvProductName = view.findViewById(R.id.productName);
        tvProductNumber = view.findViewById(R.id.productNumber);
        tvProductPrice = view.findViewById(R.id.productPrice);
        tvProductCartAmount = view.findViewById(R.id.TVCartAmount);
        tvProductDescription = view.findViewById(R.id.TVproductDescription);
        btnCartMinus = view.findViewById(R.id.buttonCartMinus);
        btnCartPlus = view.findViewById(R.id.buttonCartPlus);
        btnCartAdd = view.findViewById(R.id.buttonCartAdd);


        tvProductCartAmount.setText(String.valueOf(cartAmount));
        btnCartMinus.setOnClickListener(v -> {
            if (cartAmount > 1) {
                cartAmount--;
                tvProductCartAmount.setText(String.valueOf(cartAmount));
            }
        });
        btnCartPlus.setOnClickListener(v -> {
            cartAmount++;
            tvProductCartAmount.setText(String.valueOf(cartAmount));
        });
        btnCartAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // прослшуивание нажатия добавить в корзину
                addToCart(productId, cartAmount);
            }
        });

        // load data
        getProduct();

        return view;
    }

    private void addToCart(Integer id, int cartAmount) {
        String url = getString(R.string.api_server) + getString(R.string.addToCart);

        JSONObject params = new JSONObject();
        try {
            params.put("item_id", id);
            params.put("item_amount", cartAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();

        Thread request = new Thread() {
            @Override
            public void run() {
                if (isAdded()) {// вроде проверят добвален ли фрагмент
                    Http http = new Http(getActivity(), url);//getActivity изза фрагмента вместо активити
                    http.setMethod("POST");
                    http.setToken(true);
                    http.setData(data);
                    http.send();

                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {//getActivity изза фрагмента вместо активити
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                Integer code = http.getStatusCode();
                                if (code == 201 || code == 200) {
                                    try {
                                        JSONObject response = new JSONObject(http.getResponse());
                                        String msg = response.getString("message");
                                        alertSuccess(msg);

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

    private void getProduct() {
        String url = getString(R.string.api_server) + getString(R.string.getOneProduct) + productId;
        String urlWishlist = getString(R.string.api_server) + getString(R.string.getWishlist);

        Thread request = new Thread() {
            @Override
            public void run() {
                if (isAdded()) {// вроде проверят добвален ли фрагмент
                    Http http = new Http(getActivity(), url);//getActivity изза фрагмента вместо активити
                    http.setToken(true);
                    http.send();

                    Http httpWishlist = new Http(getActivity(), urlWishlist);//getActivity изза фрагмента вместо активити
                    httpWishlist.setToken(true);
                    httpWishlist.send();

                    if (isAdded()) {
                        //getActivity изза фрагмента вместо активити
                        requireActivity().runOnUiThread(() -> {
                            Integer codeHttpWishlist = httpWishlist.getStatusCode();
                            if (codeHttpWishlist == 200) {
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
                                        wishlistProductList.add(new com.diplom.smartstore.model.Product(product.getInt("id"),
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
                            } else if (codeHttpWishlist == 401) {
                                alertFail("Пожалуйста авторизуйтесь");
                            } else {
                                alertFail("Ошибка " + codeHttpWishlist);
                            }

                            Integer code = http.getStatusCode();
                            if (code == 200) {
                                try {
                                    JSONObject response = new JSONObject(http.getResponse());

                                    JSONObject productBrand = response.getJSONObject("brand_id"); // бренд продукта (аттрибут объекта продукт)
                                    JSONObject productCategory = response.getJSONObject("category_id"); // категория продукта (аттрибут объекта продукт)
                                    JSONObject productSubcategory = response.getJSONObject("subcategory_id"); // подкатегория продукта (аттрибут объекта продукт)

                                    JSONArray productSubcategoryAttributes = productSubcategory.getJSONArray("attributes"); // список аттрибутов подкатегории
                                    JSONArray productAttributes = response.getJSONArray("attributes"); // список аттрибутов подкатегории

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

                                    boolean inWishlist = false;

                                    for (com.diplom.smartstore.model.Product wishlistProduct : wishlistProductList) {
                                        if (response.getInt("id") == wishlistProduct.getId()) {
                                            inWishlist = true;
                                            Log.d("test", wishlistProduct.getId() + " is true");
                                        }
                                    }

                                    product = new com.diplom.smartstore.model.Product(response.getInt("id"),
                                            response.getString("name"),
                                            response.getString("slug"),
                                            response.getString("image_url"),
                                            response.getString("description"),
                                            new Brand(productBrand.getInt("id"), productBrand.getString("name"),
                                                    productBrand.getString("slug"), productBrand.getString("description")),
                                            new Category(productCategory.getInt("id"), productCategory.getString("name"),
                                                    productCategory.getString("slug"), productCategory.getString("description"), null),
                                            new Subcategory(productSubcategory.getInt("id"), productSubcategory.getString("name"),
                                                    productSubcategory.getString("slug"), productSubcategory.getString("description"),
                                                    null, attributesSubcategory), // image
                                            0,
                                            response.getInt("amount_left"),
                                            response.getInt("price"),
                                            attributesProduct,
                                            inWishlist);

//                                        Log.d("test", product.getName());
                                    // добавление данных в поля
//                                        new LoadImage(productImage).execute(product.getImgUrl());
                                    ImageLoader.getInstance().displayImage(product.getImgUrl(), productImage);
                                    tvProductName.setText(product.getName());
                                    tvProductNumber.setText(attributesProduct.get(0).getValue());
                                    tvProductPrice.setText(Integer.toString(product.getPrice()));
                                    tvProductDescription.setText(product.getDescription());

                                    if (product.getLiked()) {
                                        buttonLike.setColorFilter(requireActivity().getResources().getColor(R.color.colorAccent));
                                    } else {
                                        buttonLike.setColorFilter(requireActivity().getResources().getColor(R.color.colorSecondary));
                                    }

                                    buttonLike.setOnClickListener(v -> {
                                        if (product.getLiked()){
                                            buttonLike.setColorFilter(requireActivity().getResources().getColor(R.color.colorSecondary));
                                            deleteFromFavourite(product.getId());
                                            product.setLiked(false);
                                        } else {
                                            buttonLike.setColorFilter(requireActivity().getResources().getColor(R.color.colorAccent));
                                            addToFavourite(product.getId());
                                            product.setLiked(true);
                                        }
                                    });

                                    // Add the following lines to create RecyclerView
                                    attributesRecycler = view.findViewById(R.id.productAttributes);
                                    attributesRecycler.setHasFixedSize(true);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                                            RecyclerView.VERTICAL, false);
                                    attributesRecycler.setLayoutManager(layoutManager);
                                    attributesRecycler.setAdapter(new ProductAttributesListAdapter(context, attributesProduct));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                alertFail("Ошибка " + code);
                            }
                        });
                    }
                }
            }
        };
        request.start();
    }

    private void deleteFromFavourite(Integer id) {
        String url = getString(R.string.api_server) + getString(R.string.deleteFromWishlist);

        JSONObject params = new JSONObject();
        try {
            params.put("item_id", id);
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
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertSuccess(msg);

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

        };
        request.start();
    }

    private void addToFavourite(Integer id) {
        String url = getString(R.string.api_server) + getString(R.string.addToWishlist);

        JSONObject params = new JSONObject();
        try {
            params.put("item_id", id);
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
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertSuccess(msg);

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
