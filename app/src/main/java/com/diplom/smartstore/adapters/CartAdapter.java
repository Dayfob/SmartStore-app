package com.diplom.smartstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.model.Cart;
import com.diplom.smartstore.model.Category;
import com.diplom.smartstore.model.Product;
import com.diplom.smartstore.model.Subcategory;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context; // страница на которой все будет выведено
    Cart cart;
//    List<Product> products; // список всех категорий


    public CartAdapter(Context context, Cart cart) {
        this.context = context;
        this.cart = cart;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productsItems = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grid_item_product_flat, parent, false); // указывается дизайн
        return new CartAdapter.CartViewHolder(productsItems); // указываются элементы для работы
    }

    @Override
    public int getItemCount() {
        return cart.getProducts().size();
    }

    public static final class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productAmount;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImageFlat);
            productName = itemView.findViewById(R.id.productNameFlat);
            productPrice = itemView.findViewById(R.id.productPriceFlat);
            productAmount = itemView.findViewById(R.id.productAmountFlat);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        // нужно добавить асихронную загрузку фото:
//        holder.subcategoryImage.setImageBitmap(subcategories.get(position).getImageUrl());
//        holder.productImage.setImageBitmap();
        holder.productName.setText(cart.getProducts().get(position).getName());
        holder.productPrice.setText(cart.getProducts().get(position).getPrice() + "$");
        holder.productAmount.setText(cart.getProducts().get(position).getAmountCart() + " шт.");
    }

//    public class FetchImage extends Thread {
//
//        Context Context;
//        String url;
//        Bitmap bitmap;
//        ProgressDialog progressDialog;
//        Handler mainHandler = new Handler();
//
//        public FetchImage(String url){
//            this.url = url;
//        }
//
//        @Override
//        public void run() {
//            mainHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    progressDialog = new ProgressDialog(Context);
//                    progressDialog.setMessage("Loading");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//                }
//            });
//
//            InputStream inputStream = null;
//            try {
//                inputStream = new URL(url).openStream();
//                bitmap = BitmapFactory.decodeStream(inputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            mainHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (progressDialog.isShowing()){
//                        progressDialog.dismiss();
//                        // тут нужно присваивать картинку к ImageView, но так нельзя
//                    }
//                }
//            });
//        }
//    }
}