package com.app.frimline.intefaces;

import com.app.frimline.models.CountryModel;
import com.app.frimline.models.StateModel;

public interface OnItemSelectListener {
    void  onItemSelect(CountryModel model,int postion,int flag);
    void  onItemSelect(StateModel model, int postion, int flag);
}
