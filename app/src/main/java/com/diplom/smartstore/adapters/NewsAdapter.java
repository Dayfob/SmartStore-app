package com.diplom.smartstore.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.model.News;
import com.diplom.smartstore.utils.LoadImage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {


    Context context; // страница на которой все будет выведено
    List<News> newsList; // список всех категорий
    OnNewsListener onNewsListener;

    public NewsAdapter(Context context, List<News> newsList, OnNewsListener onNewsListener) {
        this.context = context;
        this.newsList = newsList;
        this.onNewsListener = onNewsListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newsItems = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grid_item_news, parent, false); // указывается дизайн
        return new NewsAdapter.NewsViewHolder(newsItems, onNewsListener); // указываются элементы для работы
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        // нужно добавить асихронную загрузку фото:
//        new LoadImage(holder.newsImage).execute(newsList.get(position).getImageUrl());
        ImageLoader.getInstance().displayImage(newsList.get(position).getImageUrl(), holder.newsImage);
    }

    @Override
    public int getItemCount() {return newsList.size();}

    public static final class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView newsImage;
        OnNewsListener onNewsListener;

        public NewsViewHolder(@NonNull View itemView, OnNewsListener onNewsListener) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsImageView);
            this.onNewsListener = onNewsListener;
            itemView.setOnClickListener(this);
        }

        // описание действий при нажатии на новость
        @Override
        public void onClick(View v) {
            onNewsListener.onNewsClick(getAdapterPosition());
        }
    }

    // интерфейс для прослушивания нажатия на новость
    public interface OnNewsListener{
        void onNewsClick(int position);
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
