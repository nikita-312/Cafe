package com.dineore.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dineore.app.R;
import com.dineore.app.model.CartModel;
import com.dineore.app.utils.TextviewBold;
import com.dineore.app.utils.TextviewRegular;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class LiveOrderAdapter extends RecyclerView.Adapter<LiveOrderAdapter.MenuViewHolder> {

    private Context context;
    private List<CartModel> cartModelsarray;

    public LiveOrderAdapter(List<CartModel> cartModelsarray) {
        this.cartModelsarray = cartModelsarray;

    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.row_item_live_order, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.tvrCartName.setText(cartModelsarray.get(position).getItem_name());
        holder.tvbCartPrice.setText(cartModelsarray.get(position).getPrice());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(context).load(cartModelsarray.get(position).getImages()).apply(options).into(holder.imageView1);
        holder.tvrQty.setText(cartModelsarray.get(position).getQty());
    }

    @Override
    public int getItemCount() {
        return cartModelsarray.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemll;
        RoundedImageView imageView1;
        TextviewRegular tvrCartName, tvrQty;
        TextviewBold tvbCartPrice;

        MenuViewHolder(View itemView) {
            super(itemView);
            itemll = itemView.findViewById(R.id.itemll);
            imageView1 = itemView.findViewById(R.id.imageView1);
            tvrCartName = itemView.findViewById(R.id.tvrCartName);
            tvbCartPrice = itemView.findViewById(R.id.tvbCartPrice);
            tvrQty = itemView.findViewById(R.id.tvrQty);
        }
    }
}
