package com.app.frimline.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.R;
import com.app.frimline.intefaces.OnItemSelectListener;
import com.app.frimline.models.CountryModel;

import java.util.ArrayList;


public class CountryChooseAdapter extends RecyclerView.Adapter<CountryChooseAdapter.SelecBrandLIstHolder> {

    private ArrayList<CountryModel> arrayList;
    private Activity act;
    private int checkedPosition = -1;
    private int calledFlag;


    public CountryChooseAdapter(ArrayList<CountryModel> arrayList, Activity act, int calledFlag) {
        this.arrayList = arrayList;
        this.act = act;
        this.calledFlag = calledFlag;
    }


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
                notifyDataSetChanged();

                ((OnItemSelectListener) act).onItemSelect(listModel, position);

            }
        });
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class SelecBrandLIstHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public SelecBrandLIstHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }

    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
