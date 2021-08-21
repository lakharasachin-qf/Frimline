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

import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.models.ListModel;
import com.google.gson.Gson;

import java.util.ArrayList;


public class SortingAdapter extends RecyclerView.Adapter<SortingAdapter.SelecBrandLIstHolder> {

    private int selectedPosition = -1;
    private ArrayList<ListModel> arrayList;
    private Activity act;
    private setOnCheckedRadioListener radioListener;

    public void setRadioListener(setOnCheckedRadioListener radioListener) {
        this.radioListener = radioListener;
    }

    public interface setOnCheckedRadioListener {
        void onOptionSelect(ListModel listModel, int position);
    }

    private int checkedPosition = -1;
    private int calledFlag;


    public SortingAdapter(ArrayList<ListModel> arrayList, Activity act, int calledFlag) {
        this.arrayList = arrayList;
        this.act = act;
        this.calledFlag = calledFlag;
    }


    @Override
    public SortingAdapter.SelecBrandLIstHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_radion_button_layout, parent, false);
        return new SelecBrandLIstHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelecBrandLIstHolder holder, int position) {
        ListModel listModel = arrayList.get(position);

//        holder.radioButton.setText(listModel.getName());
//        holder.radioButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.itemView.performClick();
//            }
//        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onClick(View view) {
//                holder.radioButton.setChecked(true);
//                selectedPosition = holder.getBindingAdapterPosition();
//                Toast.makeText(act, selectedPosition + "", Toast.LENGTH_SHORT).show();
//                FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.APPLY_SORT_SELECTION, new Gson().toJson(listModel));
//                notifyDataSetChanged();
//
//            }
//        });

        holder.bind(arrayList.get(position));
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

        void bind(final ListModel listModel) {
            if (checkedPosition == -1) {
                radioButton.setChecked(false);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    radioButton.setChecked(true);
                } else {
                    radioButton.setChecked(false);
                }
            }
            radioButton.setText(listModel.getName());

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioButton.setChecked(true);
                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                        FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.APPLY_SORT_SELECTION, new Gson().toJson(getSelected()));
                    }
                }
            });
        }
    }

    public ListModel getSelected() {
        if (checkedPosition != -1) {
            return arrayList.get(checkedPosition);
        }
        return null;
    }

    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
