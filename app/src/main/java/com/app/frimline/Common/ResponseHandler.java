package com.app.frimline.Common;

import android.content.Context;
import android.util.Log;

import com.app.frimline.models.CategoryRootFragments.CategoryRootModel;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResponseHandler {
    Context context;


    public ResponseHandler(Context context) {
        this.context = context;
    }

    public static ArrayList<String> getListFromJSon(JSONArray food_types) {
        ArrayList<String> strings = new ArrayList<>();

        if (food_types != null) {
            for (int i = 0; i < food_types.length(); i++) {
                try {
                    strings.add(food_types.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return strings;
        } else {

            return null;
        }

    }

    public static boolean isSuccess(String strResponse, JSONObject jsonResponse) {
        if (strResponse != null) {
            JSONObject jsonObject = createJsonObject(strResponse);
            if (jsonObject != null) {
                return getBool(jsonObject, "status");
            }
        } else if (jsonResponse != null) {
            return getBool(jsonResponse, "status");
        }
        return false;
    }

    public static JSONObject createJsonObject(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getString(strKey) : "";
        } catch (JSONException e) {
            return "";
        }
    }

    public static int getInt(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getInt(strKey) : 0;
        } catch (JSONException e) {
            return 0;
        }
    }

    public static float getFloat(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? (float) jObj.getDouble(strKey) : 0;
        } catch (JSONException e) {
            return 0;
        }
    }

    public static boolean getBool(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) && jObj.getBoolean(strKey);
        } catch (JSONException e) {
            return false;
        }
    }

    public static JSONObject getJSONObject(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getJSONObject(strKey) : new JSONObject();
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public static JSONArray getJSONArray(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getJSONArray(strKey) : new JSONArray();
        } catch (JSONException e) {
            return new JSONArray();
        }
    }


    public static CategoryRootModel handleResponseCategoryRootFragment(String res) {
        CategoryRootModel categoryRootModel = null;

        JSONObject jsonObject = createJsonObject(res);
        if (jsonObject != null) {
            categoryRootModel = new CategoryRootModel();
            categoryRootModel.setTitle(getString(jsonObject, "title"));
            categoryRootModel.setMessages(getString(jsonObject, "mobile_description"));
            categoryRootModel.setThemeColor(getString(jsonObject, "theme_color"));

            ArrayList<CategorySingleModel> dataList = new ArrayList<>();
            JSONArray dataArr = getJSONArray(jsonObject, "category");
            if (dataArr.length() != 0) {
                for (int i = 0; i < dataArr.length(); i++) {
                    CategorySingleModel singleModel = new CategorySingleModel();
                    try {
                        singleModel.setCategoryId(getString(dataArr.getJSONObject(i), "id"));
                        singleModel.setCategoryName(getString(dataArr.getJSONObject(i), "name"));
                        singleModel.setSlug(getString(dataArr.getJSONObject(i), "slug"));
                        singleModel.setParents(getString(dataArr.getJSONObject(i), "parent"));
                        singleModel.setDescriptions(getString(dataArr.getJSONObject(i), "description"));
                        singleModel.setDisplay(getString(dataArr.getJSONObject(i), "display"));
                        singleModel.setImage(getString(getJSONObject(dataArr.getJSONObject(i), "image"), "src"));
                        singleModel.setMenuOrder(getString(dataArr.getJSONObject(i), "menu_order"));
                        singleModel.setCount(getString(dataArr.getJSONObject(i), "count"));
                        singleModel.setCatColor(getString(dataArr.getJSONObject(i), "cat_color"));
                        singleModel.setDetailImage(getString(dataArr.getJSONObject(i), "details_image"));
                        singleModel.setLongDescription(getString(dataArr.getJSONObject(i), "long_description"));
                        dataList.add(singleModel);
                        dataList.add(singleModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            categoryRootModel.setCategoryList(dataList);


        }
        return categoryRootModel;

    }


}

