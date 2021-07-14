package com.app.frimline.Common;

import android.content.Context;

import java.util.Observable;

public class AppObserver extends Observable {
    private final Context context;
    private String userName;
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


}
