package com.diplom.smartstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.model.App;
import com.diplom.smartstore.model.Category;
import com.diplom.smartstore.model.News;
import com.diplom.smartstore.model.Product;
import com.diplom.smartstore.model.Subcategory;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int TYPE_NEWS = 1;
    private static int TYPE_CATEGORIES = 2;
    private static int TYPE_PRODUCTS = 3;

    Context context; // страница на которой все будет выведено
    App app;
    List<News> newsList;
    List<Subcategory> subcategoryList;
    List<Product> productList;

    // конструктор
    public HomeAdapter(Context context, App app, List<News> newsList, List<Subcategory> subcategoryList, List<Product> productList) {
        this.context = context;
        this.app = app;
        this.newsList = newsList;
        this.subcategoryList = subcategoryList;
        this.productList = productList;
    }

    // какие данные будут вставлены, установка данных
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_NEWS) {
            NewsHomeViewHolder newsViewHolder = (NewsHomeViewHolder) holder;
            setNewsRecycler(newsViewHolder.newsRecycler, app.getNews());
        } else if (getItemViewType(position) == TYPE_CATEGORIES) {
            SubcategoriesHomeViewHolder subcategoriesViewHolder = (SubcategoriesHomeViewHolder) holder;
            setSubcategoriesRecycler(subcategoriesViewHolder.subcategoriesRecycler, app.getSubcategories());
        } else if (getItemViewType(position) == TYPE_PRODUCTS) {
            ProductsHomeViewHolder productsViewHolder = (ProductsHomeViewHolder) holder;
            setProductsRecycler(productsViewHolder.productRecycler, app.getProducts());
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // держатель вызывающий раздуватель
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == TYPE_NEWS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_news, parent, false);
            return new NewsHomeViewHolder(view);
        } else if (viewType == TYPE_CATEGORIES) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_categories, parent, false);
            return new SubcategoriesHomeViewHolder(view);
        } else { //(viewType == TYPE_PRODUCTS)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_product, parent, false);
            return new ProductsHomeViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return TYPE_NEWS;
        } else if (position == 3) {
            return TYPE_CATEGORIES;
        } else if (position == 5) {
            return TYPE_PRODUCTS;
        }
        return position;
    }

    // описываются элементы для работы
    public static final class NewsHomeViewHolder extends RecyclerView.ViewHolder {
        RecyclerView newsRecycler;

        public NewsHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            newsRecycler = itemView.findViewById(R.id.newsRecyclerView);
        }
    }

    // описываются элементы для работы
    public static final class SubcategoriesHomeViewHolder extends RecyclerView.ViewHolder {
        RecyclerView subcategoriesRecycler;

        public SubcategoriesHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            subcategoriesRecycler = itemView.findViewById(R.id.fastProductCategoryRecyclerView);
        }
    }

    // описываются элементы для работы
    public static final class ProductsHomeViewHolder extends RecyclerView.ViewHolder {
        RecyclerView productRecycler;

        public ProductsHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            productRecycler = itemView.findViewById(R.id.popularProductsGridView);
        }
    }

    // лист новостей
    private void setNewsRecycler(RecyclerView recyclerView, List<News> newsList) {
        NewsAdapter newsAdapter = new NewsAdapter(context, newsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(newsAdapter);
    }

    // лист категорий
    private void setSubcategoriesRecycler(RecyclerView recyclerView, List<Subcategory> subcategoryList) {
        SubcategoryAdapter subcategoryAdapter = new SubcategoryAdapter(context, subcategoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(subcategoryAdapter);
    }

    // лист продуктов
//    private void setProductsRecycler(RecyclerView recyclerView, List<Product> productsList) {
//        ProductAdapter productAdapter = new ProductAdapter(context, productsList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
//        recyclerView.setAdapter(productAdapter);
//    }
    private void setProductsRecycler(RecyclerView recyclerView, List<Product> productsList) {
        ProductAdapter productAdapter = new ProductAdapter(context, productsList);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(productAdapter);
    }
}
