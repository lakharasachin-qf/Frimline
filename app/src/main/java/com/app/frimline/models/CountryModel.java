package com.app.frimline.models;

import java.util.ArrayList;

public class CountryModel {
    String name;
    private ArrayList<StateModel> models;

    public ArrayList<StateModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<StateModel> models) {
        this.models = models;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
