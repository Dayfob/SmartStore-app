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

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    Context context; // страница на которой все будет выведено
    List<Product> products; // список всех категорий

    public WishlistAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public WishlistAdapter.WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productsItems = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grid_item_product_flat, parent, false); // указывается дизайн
        return new WishlistAdapter.WishlistViewHolder(productsItems); // указываются элементы для работы
    }

    @Override
    public int getItemCount() {return products.size();}

    public static final class WishlistViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productAmount;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImageFlat);
            productName = itemView.findViewById(R.id.productNameFlat);
            productPrice = itemView.findViewById(R.id.productPriceFlat);
            productAmount = itemView.findViewById(R.id.productAmountFlat);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.WishlistViewHolder holder, int position) {
        // нужно добавить асихронную загрузку фото:
//        holder.subcategoryImage.setImageBitmap(subcategories.get(position).getImageUrl());
//        holder.productImage.setImageBitmap();
        holder.productName.setText(products.get(position).getName());
        holder.productPrice.setText(products.get(position).getPrice() + "$");
        holder.productAmount.setText("");
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