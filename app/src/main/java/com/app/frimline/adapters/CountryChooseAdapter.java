package com.app.frimline.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.common.PREF;
import com.app.frimline.R;
import com.app.frimline.intefaces.OnItemSelectListener;
import com.app.frimline.models.CountryModel;

import java.util.ArrayList;


public class CountryChooseAdapter extends RecyclerView.Adapter<CountryChooseAdapter.SelecBrandLIstHolder> {

    private ArrayList<CountryModel> arrayList;
    private final Activity act;
    private int checkedPosition = -1;
    private final int calledFlag;


    public CountryChooseAdapter(ArrayList<CountryModel> arrayList, Activity act, int calledFlag) {
        this.arrayList = arrayList;
        this.act = act;
        this.calledFlag = calledFlag;
    }

    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @NonNull
    @Override
    public CountryChooseAdapter.SelecBrandLIstHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_layout, parent, false);
        return new SelecBrandLIstHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelecBrandLIstHolder holder, int position) {
        CountryModel listModel = arrayList.get(position);

        holder.radioButton.setText(convertFirstUpper(listModel.getName()));
        holder.radioButton.setOnClickListener(v -> holder.itemView.performClick());
        holder.itemView.setOnClickListener(view -> {
            holder.radioButton.setChecked(true);
            // checkedPosition = position;
            ((OnItemSelectListener) act).onItemSelect(listModel, position, calledFlag);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SelecBrandLIstHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public SelecBrandLIstHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
            radioButton.setButtonTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<CountryModel> list) {
        arrayList = list;
        notifyDataSetChanged();
    }
}
