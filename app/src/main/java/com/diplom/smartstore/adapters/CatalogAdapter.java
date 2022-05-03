package com.diplom.smartstore.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.fragments.SubcategoryProductList;
import com.diplom.smartstore.model.Category;
import com.diplom.smartstore.model.Subcategory;

import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>
        implements SubcategoryAdapter.OnSubcategoryListener {

    private static final String TAG = "onClick";
    Context context; // страница на которой все будет выведено
    List<Category> categories; // список всех категорий
    FragmentActivity fragmentActivity;

    // конструктор
    public CatalogAdapter(Context context, List<Category> categories, FragmentActivity fragmentActivity) {
        this.context = context;
        this.categories = categories;
        this.fragmentActivity = fragmentActivity;
    }

    // держатель вызывающий раздуватель
    @NonNull
    @Override
    public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryItems = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grid_item_catalog, parent, false); // указывается дизайн
        return new CatalogViewHolder(categoryItems); // указываются элементы для работы
    }

    // какие данные будут вставлены, установка данных
    @Override
    public void onBindViewHolder(@NonNull CatalogViewHolder holder, int position) {
        holder.categoryTitle.setText(categories.get(position).getName());
        setSubcategoryRecycler(holder.subcategoryRecycler, categories.get(position).getSubcategories());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public void onSubcategoryClick(int position) {
        Log.d(TAG, "onSubcategoryClick: clicked " + position);
        //действия при нажатии на подкаталог
        // Prevent Reload Same Fragment
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SubcategoryProductList subcategoryList = new SubcategoryProductList();
        Bundle bundle = new Bundle();
        bundle.putInt("id", categories.get(position).getId());
        subcategoryList.setArguments(bundle);
        ft.replace(R.id.content, subcategoryList);
        ft.addToBackStack("subcategoryProducts");
        ft.commit();
    }

    // описываются элементы для работы
    public static final class CatalogViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTitle;
        RecyclerView subcategoryRecycler;

        public CatalogViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.parentCategory);
            subcategoryRecycler = itemView.findViewById(R.id.subcategoryRecyclerView);
        }
    }

    // лист подкатегорий
    private void setSubcategoryRecycler(RecyclerView recyclerView, List<Subcategory> subcategoryList){
        SubcategoryAdapter subcategoryAdapter = new SubcategoryAdapter(context, subcategoryList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false ));
        recyclerView.setAdapter(subcategoryAdapter);
    }
}
