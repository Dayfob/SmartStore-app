package com.diplom.smartstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.model.Product;
import com.diplom.smartstore.utils.LoadImage;

import java.util.List;

public class SubcategoryProductListAdapter extends RecyclerView.Adapter<SubcategoryProductListAdapter.SubcategoryProductListViewHolder> {

    Context context; // страница на которой все будет выведено
    List<Product> products; // список всех категорий
    OnProductListener onProductListener;

    public SubcategoryProductListAdapter(Context context, List<Product> products, OnProductListener onProductListener) {
        this.context = context;
        this.products = products;
        this.onProductListener = onProductListener;
    }

    @NonNull
    @Override
    public SubcategoryProductListAdapter.SubcategoryProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productsItems = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grid_item_product_flat, parent, false); // указывается дизайн
        return new SubcategoryProductListAdapter.SubcategoryProductListViewHolder(productsItems, onProductListener); // указываются элементы для работы
    }

    @Override
    public int getItemCount() {return products.size();}

    public static final class SubcategoryProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productAmount;
        OnProductListener onProductListener;

        public SubcategoryProductListViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImageFlat);
            productName = itemView.findViewById(R.id.productNameFlat);
            productPrice = itemView.findViewById(R.id.productPriceFlat);
            productAmount = itemView.findViewById(R.id.productAmountFlat);
            this.onProductListener = onProductListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProductListener.onProductClick(getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SubcategoryProductListAdapter.SubcategoryProductListViewHolder holder, int position) {
        // нужно добавить асихронную загрузку фото:
        new LoadImage(holder.productImage).execute(products.get(position).getImageUrl());
        holder.productName.setText(products.get(position).getName());
        holder.productPrice.setText(products.get(position).getPrice() + "$");
        holder.productAmount.setText("");
    }

    // интерфейс для прослушивания нажатия на продукт
    public interface OnProductListener{
        void onProductClick(int position);
    }

}
