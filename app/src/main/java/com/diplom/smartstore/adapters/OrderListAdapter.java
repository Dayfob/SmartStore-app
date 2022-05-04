package com.diplom.smartstore.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.model.Order;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.WOrderListViewHolder> {

    Context context; // страница на которой все будет выведено
    List<Order> orders; // список всех категорий
    OnOrderListener onOrderListener;
    FragmentActivity fragmentActivity;

    public OrderListAdapter(Context context, List<Order> orders, OnOrderListener onOrderListener, FragmentActivity fragmentActivity) {
        this.context = context;
        this.orders = orders;
        this.onOrderListener = onOrderListener;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public OrderListAdapter.WOrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productsItems = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grid_item_order, parent, false); // указывается дизайн
        return new OrderListAdapter.WOrderListViewHolder(productsItems, onOrderListener); // указываются элементы для работы
    }

    @Override
    public int getItemCount() {return orders.size();}

    public static final class WOrderListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView orderId, orderStatus, orderDelivery, orderDate, orderTotalPrice;
        OnOrderListener onOrderListener;

        public WOrderListViewHolder(@NonNull View itemView, OnOrderListener onOrderListener) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderDelivery = itemView.findViewById(R.id.orderDelivery);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderTotalPrice = itemView.findViewById(R.id.orderTotalPrice);
            this.onOrderListener = onOrderListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onOrderListener.onOrderClick(getAdapterPosition()); // call the onClick in the OnItemClickListener
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.WOrderListViewHolder holder, int position) {
        // нужно добавить асихронную загрузку фото:
//        ImageLoader.getInstance().displayImage(products.get(position).getImgUrl(), holder.productImage);
        holder.orderId.setText(orders.get(position).getId().toString());
        holder.orderStatus.setText(orders.get(position).getStatus());
        holder.orderDelivery.setText(orders.get(position).getDeliveryMethod());
        holder.orderDate.setText(orders.get(position).getCreatedAtDate());
        holder.orderTotalPrice.setText(orders.get(position).getTotalPrice() + " kzt");
    }

    // интерфейс для прослушивания нажатия на продукт
    public interface OnOrderListener{
        void onOrderClick(int position);
    }

}
