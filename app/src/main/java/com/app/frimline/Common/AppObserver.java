package com.app.frimline.Common;

import android.content.Context;

import com.app.frimline.models.DataTransferModel;

import java.util.Observable;

public class AppObserver extends Observable {
    private final Context context;
    private String userName;
    private String data;
    private DataTransferModel dataTransferModel;
    private int nStatusType;

    public AppObserver(Context context) {
        this.context = context;
    }

    public int getValue() {
        return nStatusType;
    }

    public void setValue(int nStatusTyp) {
        this.nStatusType = nStatusTyp;
        setChanged();
        notifyObservers(userName);
    }

    public String getData() {
        return data;
    }

    public DataTransferModel getModel() {
        return dataTransferModel;
    }

    public void setValue(int nStatusTyp, String data) {
        this.nStatusType = nStatusTyp;
        this.data = data;
        setChanged();
        notifyObservers(userName);
    }

    public void setValue(int nStatusTyp, DataTransferModel data) {
        this.nStatusType = nStatusTyp;
        this.dataTransferModel = data;
        setChanged();
        notifyObservers(userName);
    }
}
