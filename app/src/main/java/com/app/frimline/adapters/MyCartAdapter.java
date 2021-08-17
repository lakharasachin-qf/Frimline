package com.app.frimline.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cartcounter.CharOrder;
import com.app.cartcounter.strategy.Strategy;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.app.frimline.databinding.ItemMyCartLayoutBinding;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.models.roomModels.ProductEntity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Objects;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {
    private final ArrayList<ProductModel> frameItems;
    private CartRoomDatabase db;

    public interface setActionsListener {
        void onDeleteAction(int position, ProductModel model);
        void onCartUpdate(int position, ProductModel model);
    }

    private Activity activity;
    private setActionsListener actionsListener;

    public void setActionsListener(setActionsListener actionsListener) {
        this.actionsListener = actionsListener;
    }

    public MyCartAdapter(ArrayList<ProductModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
        db = CartRoomDatabase.getAppDatabase(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyCartLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_my_cart_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductModel model = frameItems.get(position);

        ((ViewHolder) holder).binding.actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionsListener.onDeleteAction(position, model);
            }
        });

        holder.binding.counter.setAnimationDuration(150L);
        holder.binding.counter.setTextFontFamily(Objects.requireNonNull(ResourcesCompat.getFont(activity, R.font.proxinova_semi_bold)));
        holder.binding.counter.setCharStrategy(Strategy.NormalAnimation());
        holder.binding.counter.addCharOrder(CharOrder.Number);
        holder.binding.counter.setAnimationInterpolator(new AccelerateDecelerateInterpolator());
        holder.binding.counter.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        if (CONSTANT.API_MODE) {
            HELPER.LOAD_HTML(holder.binding.productName, model.getName());
            HELPER.LOAD_HTML(holder.binding.price, activity.getString(R.string.Rs) + model.getCalculatedAmount());
            HELPER.LOAD_HTML(holder.binding.productQTY, "Quantity : " + model.getQty());
            HELPER.LOAD_HTML(holder.binding.dateDelivery, "Delivery By <b>4 Jun 2021</b>");
            Glide.with(activity).load(model.getProductImagesList().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_square_place_holder)
                    .error(R.drawable.ic_square_place_holder)
                    .into(holder.binding.image);

            holder.binding.counter.setText(model.getQty());
            holder.binding.incrementAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentCounter = Integer.parseInt(holder.binding.counter.getText().toString());
                    currentCounter++;
                    holder.binding.counter.setText(String.valueOf(currentCounter));
                    ProductEntity entity = db.productEntityDao().findProductByProductId(model.getId());
                    model.setQty(holder.binding.counter.getText().toString());
                    entity.setQty(holder.binding.counter.getText().toString());
                    entity.setCalculatedAmount(HELPER.incrementAction(model));
                    model.setCalculatedAmount(entity.getCalculatedAmount());
                    db.productEntityDao().updateSpecificProduct((entity));
                    HELPER.LOAD_HTML(holder.binding.price, activity.getString(R.string.Rs) + model.getCalculatedAmount());
                    actionsListener.onCartUpdate(position,model);
                    HELPER.LOAD_HTML(holder.binding.productQTY, "Quantity : " + model.getQty());
                }
            });

            holder.binding.decrementAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int currentCounter = Integer.parseInt(holder.binding.counter.getText().toString());
                    if (currentCounter > 1) {
                        currentCounter--;
                        holder.binding.counter.setText(String.valueOf(currentCounter));
                        ProductEntity entity = db.productEntityDao().findProductByProductId(model.getId());
                        model.setQty(holder.binding.counter.getText().toString());
                        entity.setQty(holder.binding.counter.getText().toString());
                        entity.setCalculatedAmount(HELPER.incrementAction(model));
                        model.setCalculatedAmount(entity.getCalculatedAmount());
                        db.productEntityDao().updateSpecificProduct((entity));
                        HELPER.LOAD_HTML(holder.binding.price, activity.getString(R.string.Rs) + model.getCalculatedAmount());
                        HELPER.LOAD_HTML(holder.binding.productQTY, "Quantity : " + model.getQty());
                        actionsListener.onCartUpdate(position,model);
                    } else {
                        Toast.makeText(activity, "At least one quantity required", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {

            holder.binding.counter.setText("0");
            holder.binding.incrementAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentCounter = Integer.parseInt(holder.binding.counter.getText().toString());
                    currentCounter++;
                    holder.binding.counter.setText(String.valueOf(currentCounter));

                }
            });
            holder.binding.decrementAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentCounter = Integer.parseInt(holder.binding.counter.getText().toString());
                    if (currentCounter > 1) {
                        currentCounter--;
                        holder.binding.counter.setText(String.valueOf(currentCounter));

                    } else {
                        Toast.makeText(activity, "At least one quantity required", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMyCartLayoutBinding binding;

        public ViewHolder(ItemMyCartLayoutBinding itemMyCartLayoutBinding) {
            super(itemMyCartLayoutBinding.getRoot());
            binding = itemMyCartLayoutBinding;


        }
    }






}
