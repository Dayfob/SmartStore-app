package com.diplom.smartstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.model.Attribute;

import java.util.List;

public class ProductAttributesListAdapter extends RecyclerView.Adapter<ProductAttributesListAdapter.ProductAttributesListViewHolder> {

    Context context; // страница на которой все будет выведено
    List<Attribute> attributes; // список  аттрибутов

    public ProductAttributesListAdapter(Context context, List<Attribute> attributes) {
        this.context = context;
        this.attributes = attributes;
    }


    @NonNull
    @Override
    public ProductAttributesListAdapter.ProductAttributesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View attributesView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grid_item_product_attribute, parent, false); // указывается дизайн
        return new ProductAttributesListAdapter.ProductAttributesListViewHolder(attributesView); // указываются элементы для работы
    }


    @Override
    public int getItemCount() {return attributes.size();}

    public static final class ProductAttributesListViewHolder extends RecyclerView.ViewHolder {
        TextView attributeTitle, attributeValue;
        public ProductAttributesListViewHolder(@NonNull View itemView) {
            super(itemView);
            attributeTitle = itemView.findViewById(R.id.productAttributeTitle);
            attributeValue = itemView.findViewById(R.id.productAttributeAmount);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAttributesListAdapter.ProductAttributesListViewHolder holder, int position) {
        holder.attributeTitle.setText(attributes.get(position).getName()+":");
        holder.attributeValue.setText(attributes.get(position).getValue());
    }
}
