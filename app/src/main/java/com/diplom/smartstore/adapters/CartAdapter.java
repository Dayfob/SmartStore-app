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
import com.diplom.smartstore.model.Cart;
import com.diplom.smartstore.utils.LoadImage;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private static final String TAG = "OnClick";
    Context context; // страница на которой все будет выведено
    Cart cart;
//    List<Product> products; // список всех категорий
    OnProductListener onProductListener;


    public CartAdapter(Context context, Cart cart, OnProductListener onProductListener) {
        this.context = context;
        this.cart = cart;
        this.onProductListener = onProductListener;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productsItems = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grid_item_product_flat, parent, false); // указывается дизайн
        return new CartAdapter.CartViewHolder(productsItems, onProductListener); // указываются элементы для работы
    }

    @Override
    public int getItemCount() {
        return cart.getProducts().size();
    }

    public static final class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productAmount;
        OnProductListener onProductListener;

        public CartViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
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
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        // нужно добавить асихронную загрузку фото:
        new LoadImage(holder.productImage).execute(cart.getProducts().get(position).getImgUrl());
        holder.productName.setText(cart.getProducts().get(position).getName());
        holder.productPrice.setText(cart.getProducts().get(position).getPrice() + "$");
        holder.productAmount.setText(cart.getProducts().get(position).getAmountCart() + " шт.");
    }

    // интерфейс для прослушивания нажатия на продукт
    public interface OnProductListener{
        void onProductClick(int position);
    }

}
