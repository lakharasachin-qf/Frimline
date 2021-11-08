package com.app.frimline.models.roomModels;


import androidx.room.TypeConverter;

import com.app.frimline.models.homeFragments.Attribute;
import com.google.gson.Gson;

import java.io.Serializable;

public class AttributeConverter implements Serializable {

    @TypeConverter  // note this annotation
    public String fromOptionValuesList(Attribute optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();

        return gson.toJson(optionValues, Attribute.class);
    }

    @TypeConverter // note this annotation
    public Attribute toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();

        return gson.fromJson(optionValuesString, Attribute.class);
    }

}