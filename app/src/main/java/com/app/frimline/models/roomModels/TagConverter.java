package com.app.frimline.models.roomModels;


import androidx.room.TypeConverter;

import com.app.frimline.models.homeFragments.Tags;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TagConverter implements Serializable {

    @TypeConverter // note this annotation
    public String fromOptionValuesList(ArrayList<Tags> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Tags>>() {
        }.getType();
        return gson.toJson(optionValues, type);
    }

    @TypeConverter // note this annotation
    public ArrayList<Tags> toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Tags>>() {
        }.getType();
        return gson.fromJson(optionValuesString, type);
    }

}