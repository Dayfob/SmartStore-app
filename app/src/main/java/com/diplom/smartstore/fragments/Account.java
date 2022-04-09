package com.diplom.smartstore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import com.diplom.smartstore.R;

public class Account extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.listview, container, false);

//        // load products
//        DB_Handler db_handler = new DB_Handler(getActivity());
//        List<Category> categoryList = db_handler.getCategoryList();
//
//        // fill listview with data
//        ListView listView= view.findViewById(R.id.listview);
//        listView.setAdapter(new CategoryListAdapter(getActivity(), categoryList));

        return view;
    }
}
