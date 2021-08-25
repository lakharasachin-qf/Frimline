package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemQAALayoutBinding;
import com.app.frimline.models.QAModel;

import java.util.ArrayList;

public class ProductQNAAdapter extends RecyclerView.Adapter<ProductQNAAdapter.ViewHolder> {
    private final ArrayList<QAModel> frameItems;
    Activity activity;

    private boolean isThemeColor = false;

    public ProductQNAAdapter(ArrayList<QAModel> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    public void setThemeColor(boolean themeColor) {
        isThemeColor = themeColor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQAALayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_q_a_a_layout, parent, false);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_q_a_a_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final QAModel model = frameItems.get(position);
        if (CONSTANT.API_MODE) {
            holder.binding.question.setText(model.getQuestion());
            holder.binding.answer.setText(model.getAnswer());
            if (model.getAnswer() == null || model.getAnswer().isEmpty()) {
                holder.binding.answer.setText("Not Answered Yet.");
            }
        }
    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemQAALayoutBinding binding;

        public ViewHolder(@NonNull ItemQAALayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            if (isThemeColor) {
                binding.qLogo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getThemeColor())));
            } else {
                binding.qLogo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(activity).getCategoryColor())));
            }

        }
    }


}
