package com.diplom.smartstore.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.adapters.CartAdapter;
import com.diplom.smartstore.adapters.CatalogAdapter;
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

public class Catalog extends Fragment {

    RecyclerView catalogRecycler;
    CatalogAdapter catalogAdapter;
    Context context;
    View view;
    List<Subcategory> subcategoryList = new ArrayList<>();
    List<Category> categoryList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_catalog, container, false);

        // load data
        getCatalog();

        return view;
    }

    private void getCatalog() {
        String url = getString(R.string.api_server) + getString(R.string.getCategories);

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
                                            JSONObject category = response.getJSONObject(i); // категория
                                            JSONArray subcategories = category.getJSONArray("subcategories"); // подкатегории

                                            for (int j = 0; j < subcategories.length(); j++) {
                                                JSONObject subcategory = subcategories.getJSONObject(i); // подкатегория
                                                JSONArray subcategoryAttributes = subcategory.getJSONArray("attributes"); // список аттрибутов подкатегории

                                                List<Attribute> attributesSubcategory = new ArrayList<>();

                                                for (int p = 0; p < subcategoryAttributes.length(); p++) {
                                                    // добавляем аттрибут в массив аттрибутов подкатегории
                                                    attributesSubcategory.add(new Attribute(p, subcategoryAttributes.get(p).toString(), null));
                                                }

                                                Log.d("attributesSubcategory", "===:> " + subcategory.getInt("id"));

                                                // добавляем подктегорию в массив подктегорий категории
                                                subcategoryList.add(new Subcategory(
                                                        subcategory.getInt("id"),
                                                        subcategory.getString("name"),
                                                        subcategory.getString("slug"),
                                                        subcategory.getString("description"),
                                                        null,
                                                        attributesSubcategory));


                                            }
                                            // добавляем ктегорию в массив категорий
                                            categoryList.add(new Category(
                                                    category.getInt("id"),
                                                    category.getString("name"),
                                                    category.getString("slug"),
                                                    category.getString("description"),
                                                    subcategoryList));

                                        }

                                        // Add the following lines to create RecyclerView
                                        catalogRecycler = view.findViewById(R.id.categoriesRecyclerView);
                                        catalogRecycler.setHasFixedSize(true);
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                                                RecyclerView.VERTICAL, false);
                                        catalogRecycler.setLayoutManager(layoutManager);
                                        catalogRecycler.setAdapter(new CatalogAdapter(context, categoryList));

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
