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
import com.diplom.smartstore.model.News;
import com.diplom.smartstore.model.Subcategory;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {


    Context context; // страница на которой все будет выведено
    List<News> newsList; // список всех категорий

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newsItems = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grid_item_news, parent, false); // указывается дизайн
        return new NewsAdapter.NewsViewHolder(newsItems); // указываются элементы для работы
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        // нужно добавить асихронную загрузку фото:
//        holder.subcategoryImage.setImageBitmap(subcategories.get(position).getImageUrl());
//        holder.newsImage.setImageBitmap();
    }

    @Override
    public int getItemCount() {return newsList.size();}

    public static final class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView newsImage;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsImageView);
        }
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
