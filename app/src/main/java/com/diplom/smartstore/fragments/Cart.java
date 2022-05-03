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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.adapters.CartAdapter;
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

public class Cart extends Fragment implements CartAdapter.OnProductListener {

    private static final String TAG = "onClick";
    CartAdapter cartAdapter;
    RecyclerView cartRecycler;
    Context context;
    TextView productsAmount;
    TextView productsPrice;
    TextView productsTotalPrice;
    Button buttonBuy;
    View view;
    List<Product> productList = new ArrayList<>();
    private List<Product> wishlistProductList = new ArrayList<>();
    Cart Cart = this;
    com.diplom.smartstore.model.Cart cart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);


        buttonBuy = view.findViewById(R.id.cartBuyButton);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // действие при нажатии кнопки "оформить заказ"
                Log.d(TAG, "оформить заказ: clicked " + v);
            }
        });

        getCartProducts();

        return view;
    }

    @Override
    public void onProductClick(int position) {
        Log.d(TAG, "onProductClick: clicked " + position);
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

    private void getCartProducts() {
        String url = getString(R.string.api_server) + getString(R.string.getCart);
        String urlWishlist = getString(R.string.api_server) + getString(R.string.getWishlist);

        Thread request = new Thread() {
            @Override
            public void run() {
                if (isAdded()) {
                    Http http = new Http(getActivity(), url);//getActivity изза фрагмента вместо активити
                    http.setToken(true);
                    http.send();
                    Http httpWishlist = new Http(getActivity(), urlWishlist);//getActivity изза фрагмента вместо активити
                    httpWishlist.setToken(true);
                    httpWishlist.send();
                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {//getActivity изза фрагмента вместо активити
                            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
                            @Override
                            public void run() {
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
                                } else if (codeHttpWishlist == 401){
                                    alertFail("Пожалуйста авторизуйтесь");
                                } else {
                                    alertFail("Ошибка " + codeHttpWishlist);
                                }


                                Integer code = http.getStatusCode();
                                if (code == 200) {
                                    try {
                                        // получаем JSON ответ
                                        JSONObject response = new JSONObject(http.getResponse());

                                        // выбираем из ответа JSON объект корзины
                                        JSONObject jsonobject = response.getJSONObject("cart");
                                        Integer totalPrice = jsonobject.getInt("total_price");

                                        // выбираем из ответа JSON массив продуктов
                                        JSONArray jsonarray = response.getJSONArray("cartProducts");

                                        productList.clear();
                                        // перебираем массив
                                        for (int i = 0; i < jsonarray.length(); i++) {
                                            JSONObject cartProduct = jsonarray.getJSONObject(i); // продукт корзины
                                            JSONObject product = cartProduct.getJSONObject("item_id"); // продукт
                                            int productAmount = cartProduct.getInt("item_amount"); // продукт


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

                                            for (Product wishlistProduct: wishlistProductList) {
                                                if (product.getInt("id") == wishlistProduct.getId()){
                                                    inCart = true;
                                                    Log.d("test", wishlistProduct.getId() + " is true");
                                                }
                                            }

                                            // добавляем товар в массив товаров корзины
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
                                                            null, attributesSubcategory),
                                                    productAmount,
                                                    product.getInt("amount_left"),
                                                    product.getInt("price"),
                                                    attributesProduct,
                                                    inCart));
                                        }

                                        if (productList.size() == 0) {
                                            buttonBuy.setClickable(false);
                                            buttonBuy.setBackgroundResource(R.drawable.bg_for_buy_btn_rounded_gray);
                                            buttonBuy.setTextColor(R.color.colorSecondary);
                                        }
//                                        else {
//                                            buttonBuy.setClickable(true);
//                                            buttonBuy.setBackgroundResource(R.drawable.bg_for_buy_btn_rounded);
//                                            buttonBuy.setTextColor(R.color.White);
//                                        }

                                        cart = new com.diplom.smartstore.model.Cart(productList);
                                        int productsAmountSum = 0;
                                        int productsPriceSum = 0;
                                        for (Product product : cart.getProducts()) {
                                            productsAmountSum += product.getAmountCart();
                                            productsPriceSum += product.getAmountCart() * product.getPrice();
                                        }


                                        // Add the following lines to create RecyclerView
                                        cartRecycler = view.findViewById(R.id.cartRecyclerView);
                                        cartRecycler.setHasFixedSize(true);
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                                                RecyclerView.VERTICAL, false);
                                        cartRecycler.setLayoutManager(layoutManager);
                                        CartAdapter cartAdapter = new CartAdapter(context, cart, Cart, getActivity(), buttonBuy);
                                        cartAdapter.setOnDataChangeListener(size -> {
                                            //do whatever here
                                            getUpdatedCart();
                                        });

                                        cartRecycler.setAdapter(cartAdapter);

                                        productsAmount = view.findViewById(R.id.cartProductAmountTextView);
                                        productsPrice = view.findViewById(R.id.cartProductPriceAmountTextView);
                                        productsTotalPrice = view.findViewById(R.id.cartProductTotalPriceAmountTextView);

                                        productsAmount.setText(productsAmountSum + " PCS.");
                                        productsPrice.setText(productsPriceSum + " tg.");
                                        productsTotalPrice.setText(totalPrice + " tg.");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (code == 401) {
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
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void alertSuccess(String s) {
        new AlertDialog.Builder(getActivity())
                .setMessage(s)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
    }

    private void getUpdatedCart() {
        int productsAmountSum = 0;
        int productsPriceSum = 0;
        for (Product product : cart.getProducts()) {
            productsAmountSum += product.getAmountCart();
            productsPriceSum += product.getAmountCart() * product.getPrice();
        }
        productsAmount.setText(productsAmountSum + " PCS.");
        productsPrice.setText(productsPriceSum + " tg.");
        productsTotalPrice.setText(productsPriceSum + " tg.");
    }
}
