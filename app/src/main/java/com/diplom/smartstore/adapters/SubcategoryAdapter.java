package com.diplom.smartstore.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.model.Subcategory;
import com.diplom.smartstore.utils.LoadImage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.SubcategoryViewHolder> {


    Context context; // страница на которой все будет выведено
    List<Subcategory> subcategories; // список всех категорий
    OnSubcategoryListener onSubcategoryListener;

    public SubcategoryAdapter(Context context, List<Subcategory> subcategories, OnSubcategoryListener onSubcategoryListener) {
        this.context = context;
        this.subcategories = subcategories;
        this.onSubcategoryListener = onSubcategoryListener;
    }

    @NonNull
    @Override
    public SubcategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View subcategoryItems = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grid_item_categories, parent, false); // указывается дизайн
        return new SubcategoryAdapter.SubcategoryViewHolder(subcategoryItems, onSubcategoryListener); // указываются элементы для работы
    }

    @Override
    public void onBindViewHolder(@NonNull SubcategoryViewHolder holder, int position) {
        // нужно добавить асихронную загрузку фото:
        new LoadImage(holder.subcategoryImage).execute(subcategories.get(position).getImageUrl());
        holder.subcategoryName.setText(subcategories.get(position).getName());

    }

    @Override
    public int getItemCount() {return subcategories.size();}

    public static final class SubcategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView subcategoryImage;
        TextView subcategoryName;
        OnSubcategoryListener onSubcategoryListener;

        public SubcategoryViewHolder(@NonNull View itemView, OnSubcategoryListener onSubcategoryListener) {
            super(itemView);
            subcategoryImage = itemView.findViewById(R.id.subcategoryImage);
            subcategoryName = itemView.findViewById(R.id.subcategoryName);
            this.onSubcategoryListener = onSubcategoryListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSubcategoryListener.onSubcategoryClick(getAdapterPosition());
        }
    }

    // интерфейс для прослушивания нажатия на новость
    public interface OnSubcategoryListener{
        void onSubcategoryClick(int position);
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
