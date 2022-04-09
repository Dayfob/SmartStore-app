package com.diplom.smartstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.model.Category;
import com.diplom.smartstore.model.Subcategory;

import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder> {

    Context context; // страница на которой все будет выведено
    List<Category> categories; // список всех категорий

    // конструктор
    public CatalogAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
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
        SubcategoryAdapter subcategoryAdapter = new SubcategoryAdapter(context, subcategoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false ));
        recyclerView.setAdapter(subcategoryAdapter);
    }
}
