package com.app.frimline.adapters;

import android.app.Activity;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.R;
import com.app.frimline.databinding.ItemMyCartLayoutBinding;
import com.app.frimline.models.OutCategoryModel;

import java.util.ArrayList;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {
    private final ArrayList<OutCategoryModel> frameItems;

    public interface setActionsListener {
        void onDeleteAction(int position, OutCategoryModel model);
    }

    private Activity activity;
    private setActionsListener actionsListener;

    public void setActionsListener(setActionsListener actionsListener) {
        this.actionsListener = actionsListener;
    }

    public MyCartAdapter(ArrayList<OutCategoryModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyCartLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_my_cart_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OutCategoryModel model = frameItems.get(position);
        ((ViewHolder) holder).binding.actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    actionsListener.onDeleteAction(position,model);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ((ViewHolder) holder).binding.dateDelivery.setText(Html.fromHtml("Delivery By <b>4 Jun 2021</b>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            ((ViewHolder) holder).binding.dateDelivery.setText(Html.fromHtml("Delivery By <b>4 Jun 2021</b>"));
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

//            ImageView imageView = itemView.findViewById(R.id.actionAddCart);
//            imageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
//            View underLine = itemView.findViewById(R.id.underLine);
//            underLine.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));

        }
    }


}
