package com.diplom.smartstore.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.model.Product;

import java.util.List;

public class ProductBaseAdapter extends BaseAdapter {


    Context context; // страница на которой все будет выведено
    List<Product> products; // список всех категорий
    LayoutInflater inflater;

    public ProductBaseAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.inflater = (LayoutInflater.from(context));
    }

//    @NonNull
//    @Override
//    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View productsItems = LayoutInflater.from(parent.getContext()).
//                inflate(R.layout.grid_item_product, parent, false); // указывается дизайн
//        return new ProductsViewHolder(productsItems); // указываются элементы для работы
//    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.grid_item_product, null); // надуть макет
        ImageView productImage = convertView.findViewById(R.id.productImage);
        TextView productName = convertView.findViewById(R.id.productName);
        TextView productPrice = convertView.findViewById(R.id.productPrice);

//        productImage.setImageBitmap(products.get(position).getImgUrl());
        productName.setText(products.get(position).getName());
        productPrice.setText(products.get(position).getPrice());
        return convertView;
    }

//    public static final class ProductsViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView productImage;
//        TextView productName;
//        TextView productPrice;
//
//        public ProductsViewHolder(@NonNull View itemView) {
//            super(itemView);
//            productImage = itemView.findViewById(R.id.productImage);
//            productName = itemView.findViewById(R.id.productName);
//            productPrice = itemView.findViewById(R.id.productPrice);
//        }
//    }

//    @Override
//    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
//        // нужно добавить асихронную загрузку фото:
////        holder.subcategoryImage.setImageBitmap(subcategories.get(position).getImageUrl());
////        holder.productImage.setImageBitmap();
//        holder.productName.setText(products.get(position).getName());
//        holder.productPrice.setText(products.get(position).getPrice());
//    }

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
