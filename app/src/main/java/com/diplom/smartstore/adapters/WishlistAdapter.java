package com.diplom.smartstore.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.smartstore.R;
import com.diplom.smartstore.model.Product;
import com.diplom.smartstore.utils.Http;
import com.diplom.smartstore.utils.LoadImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    Context context; // страница на которой все будет выведено
    List<Product> products; // список всех категорий
    OnProductListener onProductListener;
    FragmentActivity fragmentActivity;

    public WishlistAdapter(Context context, List<Product> products, OnProductListener onProductListener, FragmentActivity fragmentActivity) {
        this.context = context;
        this.products = products;
        this.onProductListener = onProductListener;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public WishlistAdapter.WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productsItems = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.grid_item_product_flat, parent, false); // указывается дизайн
        return new WishlistAdapter.WishlistViewHolder(productsItems, onProductListener); // указываются элементы для работы
    }

    @Override
    public int getItemCount() {return products.size();}

    public static final class WishlistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView productImage;
        ImageView buttonLike;
        TextView productName;
        TextView productPrice;
        TextView productAmount;
        OnProductListener onProductListener;

        public WishlistViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImageFlat);
            buttonLike = itemView.findViewById(R.id.productHeartFlat);
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
    public void onBindViewHolder(@NonNull WishlistAdapter.WishlistViewHolder holder, int position) {
        // нужно добавить асихронную загрузку фото:
        new LoadImage(holder.productImage).execute(products.get(position).getImgUrl());
        holder.productName.setText(products.get(position).getName());
        holder.productPrice.setText(products.get(position).getPrice() + "$");
        holder.productAmount.setText("");

        if (products.get(position).getLiked()) {
            holder.buttonLike.setColorFilter(fragmentActivity.getResources().getColor(R.color.colorAccent));
        } else {
            holder.buttonLike.setColorFilter(fragmentActivity.getResources().getColor(R.color.colorSecondary));
        }

        holder.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (products.get(position).getLiked()) {
                    holder.buttonLike.setColorFilter(fragmentActivity.getResources().getColor(R.color.colorSecondary));
                    deleteFromFavourite(products.get(position).getId());
                    products.get(position).setLiked(false);
                } else {
                    holder.buttonLike.setColorFilter(fragmentActivity.getResources().getColor(R.color.colorAccent));
                    addToFavourite(products.get(position).getId());
                    products.get(position).setLiked(true);
                }
            }
        });
    }

    // интерфейс для прослушивания нажатия на продукт
    public interface OnProductListener{
        void onProductClick(int position);
    }

    private void deleteFromFavourite(Integer id) {
        String url = fragmentActivity.getString(R.string.api_server) + fragmentActivity.getString(R.string.deleteFromWishlist);

        JSONObject params = new JSONObject();
        try {
            params.put("item_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();

        Thread request = new Thread() {
            @Override
            public void run() {
                Http http = new Http(fragmentActivity, url);
                http.setMethod("POST");
                http.setToken(true);
                http.setData(data);
                http.send();
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if (code == 201 || code == 200) {
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertSuccess(msg);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (code == 422) {
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertFail(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            alertFail("Ошибка " + code);
                        }
                    }
                });
            }

        };
        request.start();
    }

    private void addToFavourite(Integer id) {
        String url = fragmentActivity.getString(R.string.api_server) + fragmentActivity.getString(R.string.addToWishlist);

        JSONObject params = new JSONObject();
        try {
            params.put("item_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();

        Thread request = new Thread() {
            @Override
            public void run() {
                Http http = new Http(fragmentActivity, url);
                http.setMethod("POST");
                http.setToken(true);
                http.setData(data);
                http.send();
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if (code == 201 || code == 200) {
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertSuccess(msg);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (code == 422) {
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertFail(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            alertFail("Ошибка " + code);
                        }
                    }
                });
            }

        };
        request.start();
    }

    private void alertFail(String s) {
        new AlertDialog.Builder(fragmentActivity)
                .setMessage(s)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void alertSuccess(String s) {
        new AlertDialog.Builder(fragmentActivity)
                .setMessage(s)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
