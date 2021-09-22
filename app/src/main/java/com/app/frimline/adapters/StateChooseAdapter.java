package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.intefaces.OnItemSelectListener;
import com.app.frimline.models.CountryModel;
import com.app.frimline.models.StateModel;

import java.util.ArrayList;


public class StateChooseAdapter extends RecyclerView.Adapter<StateChooseAdapter.SelecBrandLIstHolder> {

    private ArrayList<StateModel> arrayList;
    private final Activity act;
    private int checkedPosition = -1;
    private final int calledFlag;


    public StateChooseAdapter(ArrayList<StateModel> arrayList, Activity act, int calledFlag) {
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

    @Override
    public StateChooseAdapter.SelecBrandLIstHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_layout, parent, false);
        return new SelecBrandLIstHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelecBrandLIstHolder holder, int position) {
        StateModel listModel = arrayList.get(position);

        holder.radioButton.setText(convertFirstUpper(listModel.getStateName()));
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.performClick();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.radioButton.setChecked(true);
                checkedPosition = position;
                ((OnItemSelectListener) act).onItemSelect(listModel, position, calledFlag);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class SelecBrandLIstHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public SelecBrandLIstHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
            radioButton.setButtonTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        }
    }
    public void updateList(ArrayList<StateModel> list){
        arrayList = list;
        notifyDataSetChanged();
    }
}
