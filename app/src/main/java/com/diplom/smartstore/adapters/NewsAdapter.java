package com.diplom.smartstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.model.News;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    public int getItemCount() {
        return newsList.size();
    }

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
    public interface OnNewsListener {
        void onNewsClick(int position);
    }

}
