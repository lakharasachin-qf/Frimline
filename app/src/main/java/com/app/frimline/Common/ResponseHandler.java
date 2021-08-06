package com.app.frimline.Common;

import android.content.Context;

import com.app.frimline.models.CategoryRootFragments.CategoryRootModel;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.models.HomeFragements.BannerModel;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.models.HomeFragements.SectionPositionModel;
import com.app.frimline.models.HomeFragements.Tags;
import com.app.frimline.models.HomeFragements.TradingStoriesModel;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

            ArrayList<String> bannerList = new ArrayList<>();
            JSONArray bannerArr = getJSONArray(jsonObject, "banner");
            if (bannerArr.length() != 0) {
                for (int i = 0; i < bannerArr.length(); i++) {

                    try {
                        bannerList.add(bannerArr.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            categoryRootModel.setBannerList(bannerList);

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


    public static HomeModel getBannerList(JSONObject jsonObject) {


        ArrayList<BannerModel> categoryList = new ArrayList<>();

        BannerModel singleModel = new BannerModel();
        singleModel.setUrl(getString(jsonObject, "banner_1"));
        categoryList.add(singleModel);

        singleModel = new BannerModel();
        singleModel.setUrl(getString(jsonObject, "banner_2"));
        categoryList.add(singleModel);


        HomeModel categoryModel = new HomeModel();
        categoryModel.setLayoutType(LAYOUT_TYPE.BANNER);
        categoryModel.setLayoutIndex(0);
        //categoryModel.setLayoutIndex(getPosition(jsonObject, "Offers"));
        categoryModel.setBannerList(categoryList);
        return categoryModel;


    }

    public static HomeModel getTopRated(JSONObject jsonObject) {
        JSONArray topRatedArr = getJSONArray(jsonObject, "top_rated");
        ArrayList<ProductModel> topRattedModelArrayList = new ArrayList<>();
        for (int i = 0; i < topRatedArr.length(); i++) {
            try {
                JSONObject productObj = topRatedArr.getJSONObject(i);
                ProductModel model = new ProductModel();
                model.setId(getString(productObj, "id"));
                model.setName(getString(productObj, "name"));
                model.setSlug(getString(productObj, "slug"));
                model.setDescription(getString(productObj, "description"));
                model.setShortDescription(getString(productObj, "short_description"));
                model.setPrice(getString(productObj, "price"));
                model.setRegularPrice(getString(productObj, "regular_price"));
                model.setPriceHtml(getString(productObj, "price_html"));
                model.setCategoryId(productObj.getJSONArray("categories").getJSONObject(0).getString("id"));
                model.setCategoryName(productObj.getJSONArray("categories").getJSONObject(0).getString("name"));
                model.setStockStatus(getString(productObj, "stock_status"));
                ArrayList<Tags> tagsArrayList = new ArrayList<>();
                for (int k = 0; k < productObj.getJSONArray("tags").length(); k++) {
                    Tags tags = new Tags();
                    tags.setTag(productObj.getJSONArray("tags").getJSONObject(k).getString("name"));
                    tags.setId(productObj.getJSONArray("tags").getJSONObject(k).getString("id"));
                    tagsArrayList.add(tags);
                }
                ArrayList<String> productImages = new ArrayList<>();
                for (int k = 0; k < productObj.getJSONArray("images").length(); k++) {
                    productImages.add(productObj.getJSONArray("images").getJSONObject(k).getString("src"));
                }
                model.setProductImagesList(productImages);
                model.setTagsModel(tagsArrayList);


                ProductModel.Attribute attribute = model.new Attribute();
                JSONArray metaDataArra = getJSONArray(productObj, "meta_data");
                for (int md = 0; md < metaDataArra.length(); md++) {

                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("how_touse")) {
                        attribute.setHowToUse(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("Ingredents_content")) {
                        attribute.setIngredients(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("description")) {
                        attribute.setDescription(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("additional_information")) {
                        attribute.setDimWeight(metaDataArra.getJSONObject(md).getJSONObject("value").getString("weight"));
                        if (metaDataArra.getJSONObject(md).getJSONObject("value").get("dimensions") instanceof JSONObject) {
                            attribute.setDimLength(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("length"));
                            attribute.setDimWidth(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("width"));
                            attribute.setDimHeight(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("height"));
                        }
                    }
                }
                JSONArray attributesArr = getJSONArray(productObj, "attributes");
                if (attributesArr.length() != 0 && attributesArr.getJSONObject(0).get("options") instanceof JSONArray && attributesArr.getJSONObject(0).getJSONArray("options").length() != 0) {
                    attribute.setSize(attributesArr.getJSONObject(0).getJSONArray("options").getString(0));
                }
                model.setAttribute(attribute);
                topRattedModelArrayList.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.TOP_RATTED);
        homeModel.setLayoutIndex(getPosition(jsonObject, "Top Rated"));
        homeModel.setApiProductModel(topRattedModelArrayList);

        return homeModel;
    }

    public static HomeModel getProducts(JSONObject jsonObject) {
        JSONArray productArr = getJSONArray(jsonObject, "product");
        List<ProductModel> productModels = new ArrayList<>();
        for (int i = 0; i < productArr.length(); i++) {
            try {
                JSONObject productObj = productArr.getJSONObject(i);
                ProductModel model = new ProductModel();
                model.setId(getString(productObj, "id"));
                model.setName(getString(productObj, "name"));
                model.setSlug(getString(productObj, "slug"));
                model.setDescription(getString(productObj, "description"));
                model.setShortDescription(getString(productObj, "short_description"));
                model.setPrice(getString(productObj, "price"));
                model.setRegularPrice(getString(productObj, "regular_price"));
                model.setPriceHtml(getString(productObj, "price_html"));
                model.setCategoryId(productObj.getJSONArray("categories").getJSONObject(0).getString("id"));
                model.setCategoryName(productObj.getJSONArray("categories").getJSONObject(0).getString("name"));
                model.setStockStatus(getString(productObj, "stock_status"));
                ArrayList<Tags> tagsArrayList = new ArrayList<>();
                for (int k = 0; k < productObj.getJSONArray("tags").length(); k++) {
                    Tags tags = new Tags();
                    tags.setTag(productObj.getJSONArray("tags").getJSONObject(k).getString("name"));
                    tags.setId(productObj.getJSONArray("tags").getJSONObject(k).getString("id"));
                    tagsArrayList.add(tags);
                }
                ArrayList<String> productImages = new ArrayList<>();
                for (int k = 0; k < productObj.getJSONArray("images").length(); k++) {
                    productImages.add(productObj.getJSONArray("images").getJSONObject(k).getString("src"));
                }
                model.setProductImagesList(productImages);
                model.setTagsModel(tagsArrayList);
                ProductModel.Attribute attribute = model.new Attribute();
                JSONArray metaDataArra = getJSONArray(productObj, "meta_data");
                for (int md = 0; md < metaDataArra.length(); md++) {

                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("how_touse")) {
                        attribute.setHowToUse(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("Ingredents_content")) {
                        attribute.setIngredients(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("description")) {
                        attribute.setDescription(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("additional_information")) {
                        attribute.setDimWeight(metaDataArra.getJSONObject(md).getJSONObject("value").getString("weight"));
                        if (metaDataArra.getJSONObject(md).getJSONObject("value").get("dimensions") instanceof JSONObject) {
                            attribute.setDimLength(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("length"));
                            attribute.setDimWidth(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("width"));
                            attribute.setDimHeight(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("height"));
                        }
                    }
                }
                JSONArray attributesArr = getJSONArray(productObj, "attributes");
                if (attributesArr.length() != 0 && attributesArr.getJSONObject(0).get("options") instanceof JSONArray && attributesArr.getJSONObject(0).getJSONArray("options").length() != 0) {
                    attribute.setSize(attributesArr.getJSONObject(0).getJSONArray("options").getString(0));
                }


                model.setAttribute(attribute);

                productModels.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        ArrayList<HomeModel> tempArray = new ArrayList<>();
        ArrayList<ArrayList<ProductModel>> chunkArray = chunkArray(productModels, 3);

        for (int i = 0; i < chunkArray.size(); i++) {
            HomeModel model = new HomeModel();
            ArrayList<ProductModel> modelArrayList = chunkArray.get(i);
            model.setApiProductModel(modelArrayList);
            if (modelArrayList.size() == 3)
                model.setLayoutType(LAYOUT_TYPE.LAYOUT_THREE_PRODUCT);
            else if (modelArrayList.size() == 2)
                model.setLayoutType(LAYOUT_TYPE.LAYOUT_TWO_PRODUCT);
            else if (modelArrayList.size() == 1)
                model.setLayoutType(LAYOUT_TYPE.LAYOUT_ONE_PRODUCT);
            tempArray.add(model);

        }
        HomeModel productHomeModel = new HomeModel();
        productHomeModel.setLayoutType(LAYOUT_TYPE.CATEGORY_PRODUCT);
        productHomeModel.setLayoutIndex(getPosition(jsonObject, "Product"));
        productHomeModel.setCategoryProduct(tempArray);
        return productHomeModel;
    }

    public static HomeModel getCategory(JSONObject jsonObject) {
        JSONArray categoryArr = getJSONArray(jsonObject, "category");
        ArrayList<CategorySingleModel> categoryList = new ArrayList<>();
        for (int i = 0; i < categoryArr.length(); i++) {
            CategorySingleModel singleModel = new CategorySingleModel();
            try {
                singleModel.setCategoryId(getString(categoryArr.getJSONObject(i), "id"));
                singleModel.setCategoryName(getString(categoryArr.getJSONObject(i), "name"));
                singleModel.setSlug(getString(categoryArr.getJSONObject(i), "slug"));
                singleModel.setParents(getString(categoryArr.getJSONObject(i), "parent"));
                singleModel.setDescriptions(getString(categoryArr.getJSONObject(i), "description"));
                singleModel.setDisplay(getString(categoryArr.getJSONObject(i), "display"));
                singleModel.setImage(getString(getJSONObject(categoryArr.getJSONObject(i), "image"), "src"));
                singleModel.setMenuOrder(getString(categoryArr.getJSONObject(i), "menu_order"));
                singleModel.setCount(getString(categoryArr.getJSONObject(i), "count"));
                singleModel.setCatColor(getString(categoryArr.getJSONObject(i), "cat_color"));
                singleModel.setDetailImage(getString(categoryArr.getJSONObject(i), "details_image"));
                singleModel.setLongDescription(getString(categoryArr.getJSONObject(i), "long_description"));
                categoryList.add(singleModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        HomeModel categoryModel = new HomeModel();
        categoryModel.setLayoutType(LAYOUT_TYPE.CATEGORY);
        categoryModel.setLayoutIndex(getPosition(jsonObject, "Category"));
        categoryModel.setCategoryArrayList(categoryList);
        return categoryModel;
    }

    public static HomeModel getTradingStories(JSONObject jsonObject) {
        JSONArray categoryArr = getJSONArray(jsonObject, "tranding_stories");
        ArrayList<TradingStoriesModel> categoryList = new ArrayList<>();
        for (int i = 0; i < categoryArr.length(); i++) {
            TradingStoriesModel singleModel = new TradingStoriesModel();
            try {
                singleModel.setUrl(categoryArr.getString(i));
                categoryList.add(singleModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        HomeModel categoryModel = new HomeModel();
        categoryModel.setLayoutType(LAYOUT_TYPE.OFFERS);
        categoryModel.setLayoutIndex(getPosition(jsonObject, "Tranding Story"));
        categoryModel.setTradingStoriesList(categoryList);
        return categoryModel;
    }

    public static ArrayList<HomeModel> handleResponseCategoryHomeFragments(String res) {
        ArrayList<HomeModel> rootArrayList = new ArrayList<>();

        JSONObject jsonObject = createJsonObject(res);
        if (jsonObject != null) {
            rootArrayList.add(getBannerList(jsonObject));

            rootArrayList.add(getTopRated(jsonObject));

            rootArrayList.add(getProducts(jsonObject));

            rootArrayList.add(getCategory(jsonObject));

            rootArrayList.add(getTradingStories(jsonObject));

            Collections.sort(rootArrayList, new Comparator<HomeModel>() {
                @Override
                public int compare(HomeModel lhs, HomeModel rhs) {
                    return lhs.getLayoutIndex() - rhs.getLayoutIndex();
                }
            });

            JSONArray sectionArr = getJSONArray(jsonObject, "section_position");
            ArrayList<SectionPositionModel> positionModelsList = new ArrayList<>();
            for (int i = 0; i < sectionArr.length(); i++) {
                SectionPositionModel model = new SectionPositionModel();
                try {
                    model.setName(sectionArr.getJSONObject(i).getString("name"));
                    model.setPosition(sectionArr.getJSONObject(i).getString("order_no"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                positionModelsList.add(model);
            }


        }
        return rootArrayList;

    }

    public static int getPosition(JSONObject jsonObject, String key) {
        JSONArray sectionArr = getJSONArray(jsonObject, "section_position");
        int pos = 0;
        for (int i = 0; i < sectionArr.length(); i++) {
            try {
                if (sectionArr.getJSONObject(i).getString("name").equalsIgnoreCase(key)) {
                    pos = Integer.parseInt(sectionArr.getJSONObject(i).getString("order_no"));
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return pos;
    }

    public static ArrayList<ArrayList<ProductModel>> chunkArray(List<ProductModel> array, int chunkSize) {
        int numOfChunks = (int) Math.ceil((double) array.size() / chunkSize);
        ArrayList<ArrayList<ProductModel>> output = new ArrayList<>();

        final AtomicInteger counter = new AtomicInteger();

        for (ProductModel number : array) {
            if (counter.getAndIncrement() % chunkSize == 0) {
                output.add(new ArrayList<>());
            }
            output.get(output.size() - 1).add(number);
        }

        return output;
    }


    public static HomeModel getHotProducts(JSONObject jsonObject) {
        JSONArray topRatedArr = getJSONArray(jsonObject, "hot_product");
        ArrayList<ProductModel> topRattedModelArrayList = new ArrayList<>();
        for (int i = 0; i < topRatedArr.length(); i++) {
            try {
                JSONObject productObj = topRatedArr.getJSONObject(i);
                ProductModel model = new ProductModel();
                model.setId(getString(productObj, "id"));
                model.setName(getString(productObj, "name"));
                model.setSlug(getString(productObj, "slug"));
                model.setDescription(getString(productObj, "description"));
                model.setShortDescription(getString(productObj, "short_description"));
                model.setPrice(getString(productObj, "price"));
                model.setRegularPrice(getString(productObj, "regular_price"));
                model.setPriceHtml(getString(productObj, "price_html"));
                model.setCategoryId(productObj.getJSONArray("categories").getJSONObject(0).getString("id"));
                model.setCategoryName(productObj.getJSONArray("categories").getJSONObject(0).getString("name"));
                model.setStockStatus(getString(productObj, "stock_status"));
                ArrayList<Tags> tagsArrayList = new ArrayList<>();
                for (int k = 0; k < productObj.getJSONArray("tags").length(); k++) {
                    Tags tags = new Tags();
                    tags.setTag(productObj.getJSONArray("tags").getJSONObject(k).getString("name"));
                    tags.setId(productObj.getJSONArray("tags").getJSONObject(k).getString("id"));
                    tagsArrayList.add(tags);
                }
                ArrayList<String> productImages = new ArrayList<>();
                for (int k = 0; k < productObj.getJSONArray("images").length(); k++) {
                    productImages.add(productObj.getJSONArray("images").getJSONObject(k).getString("src"));
                }
                model.setProductImagesList(productImages);
                model.setTagsModel(tagsArrayList);


                ProductModel.Attribute attribute = model.new Attribute();
                JSONArray metaDataArra = getJSONArray(productObj, "meta_data");
                for (int md = 0; md < metaDataArra.length(); md++) {

                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("how_touse")) {
                        attribute.setHowToUse(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("Ingredents_content")) {
                        attribute.setIngredients(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("description")) {
                        attribute.setDescription(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("additional_information")) {
                        attribute.setDimWeight(metaDataArra.getJSONObject(md).getJSONObject("value").getString("weight"));
                        if (metaDataArra.getJSONObject(md).getJSONObject("value").get("dimensions") instanceof JSONObject) {
                            attribute.setDimLength(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("length"));
                            attribute.setDimWidth(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("width"));
                            attribute.setDimHeight(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("height"));
                        }
                    }
                }
                JSONArray attributesArr = getJSONArray(productObj, "attributes");
                if (attributesArr.length() != 0 && attributesArr.getJSONObject(0).get("options") instanceof JSONArray && attributesArr.getJSONObject(0).getJSONArray("options").length() != 0) {
                    attribute.setSize(attributesArr.getJSONObject(0).getJSONArray("options").getString(0));
                }
                model.setAttribute(attribute);
                topRattedModelArrayList.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_HOT_PRODUCT);
        homeModel.setApiProductModel(topRattedModelArrayList);

        return homeModel;
    }
    public static HomeModel getCategoryForShop(JSONObject jsonObject) {
        JSONArray categoryArr = getJSONArray(jsonObject, "category");
        ArrayList<CategorySingleModel> categoryList = new ArrayList<>();
        for (int i = 0; i < categoryArr.length(); i++) {
            CategorySingleModel singleModel = new CategorySingleModel();
            try {
                singleModel.setCategoryId(getString(categoryArr.getJSONObject(i), "id"));
                singleModel.setCategoryName(getString(categoryArr.getJSONObject(i), "name"));
                singleModel.setSlug(getString(categoryArr.getJSONObject(i), "slug"));
                singleModel.setParents(getString(categoryArr.getJSONObject(i), "parent"));
                singleModel.setDescriptions(getString(categoryArr.getJSONObject(i), "description"));
                singleModel.setDisplay(getString(categoryArr.getJSONObject(i), "display"));
                singleModel.setImage(getString(getJSONObject(categoryArr.getJSONObject(i), "image"), "src"));
                singleModel.setMenuOrder(getString(categoryArr.getJSONObject(i), "menu_order"));
                singleModel.setCount(getString(categoryArr.getJSONObject(i), "count"));
                singleModel.setCatColor(getString(categoryArr.getJSONObject(i), "cat_color"));
                singleModel.setDetailImage(getString(categoryArr.getJSONObject(i), "details_image"));
                singleModel.setLongDescription(getString(categoryArr.getJSONObject(i), "long_description"));
                categoryList.add(singleModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        HomeModel categoryModel = new HomeModel();
        categoryModel.setLayoutType(LAYOUT_TYPE.LAYOUT_FILTER_CHIP);
        categoryModel.setCategoryArrayList(categoryList);
        return categoryModel;
    }
    public static HomeModel getTopRatedForShop(JSONObject jsonObject) {
        JSONArray topRatedArr = getJSONArray(jsonObject, "top_rated");
        ArrayList<ProductModel> topRattedModelArrayList = new ArrayList<>();
        for (int i = 0; i < topRatedArr.length(); i++) {
            try {
                JSONObject productObj = topRatedArr.getJSONObject(i);
                ProductModel model = new ProductModel();
                model.setId(getString(productObj, "id"));
                model.setName(getString(productObj, "name"));
                model.setSlug(getString(productObj, "slug"));
                model.setDescription(getString(productObj, "description"));
                model.setShortDescription(getString(productObj, "short_description"));
                model.setPrice(getString(productObj, "price"));
                model.setRegularPrice(getString(productObj, "regular_price"));
                model.setPriceHtml(getString(productObj, "price_html"));
                model.setCategoryId(productObj.getJSONArray("categories").getJSONObject(0).getString("id"));
                model.setCategoryName(productObj.getJSONArray("categories").getJSONObject(0).getString("name"));
                model.setStockStatus(getString(productObj, "stock_status"));
                ArrayList<Tags> tagsArrayList = new ArrayList<>();
                for (int k = 0; k < productObj.getJSONArray("tags").length(); k++) {
                    Tags tags = new Tags();
                    tags.setTag(productObj.getJSONArray("tags").getJSONObject(k).getString("name"));
                    tags.setId(productObj.getJSONArray("tags").getJSONObject(k).getString("id"));
                    tagsArrayList.add(tags);
                }
                ArrayList<String> productImages = new ArrayList<>();
                for (int k = 0; k < productObj.getJSONArray("images").length(); k++) {
                    productImages.add(productObj.getJSONArray("images").getJSONObject(k).getString("src"));
                }
                model.setProductImagesList(productImages);
                model.setTagsModel(tagsArrayList);


                ProductModel.Attribute attribute = model.new Attribute();
                JSONArray metaDataArra = getJSONArray(productObj, "meta_data");
                for (int md = 0; md < metaDataArra.length(); md++) {

                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("how_touse")) {
                        attribute.setHowToUse(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("Ingredents_content")) {
                        attribute.setIngredients(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("description")) {
                        attribute.setDescription(metaDataArra.getJSONObject(md).getString("value"));
                    }
                    if (metaDataArra.getJSONObject(md).getString("key").equalsIgnoreCase("additional_information")) {
                        attribute.setDimWeight(metaDataArra.getJSONObject(md).getJSONObject("value").getString("weight"));
                        if (metaDataArra.getJSONObject(md).getJSONObject("value").get("dimensions") instanceof JSONObject) {
                            attribute.setDimLength(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("length"));
                            attribute.setDimWidth(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("width"));
                            attribute.setDimHeight(metaDataArra.getJSONObject(md).getJSONObject("value").getJSONObject("dimensions").getString("height"));
                        }
                    }
                }
                JSONArray attributesArr = getJSONArray(productObj, "attributes");
                if (attributesArr.length() != 0 && attributesArr.getJSONObject(0).get("options") instanceof JSONArray && attributesArr.getJSONObject(0).getJSONArray("options").length() != 0) {
                    attribute.setSize(attributesArr.getJSONObject(0).getJSONArray("options").getString(0));
                }
                model.setAttribute(attribute);
                topRattedModelArrayList.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_TOP_PRODUCT);
        homeModel.setApiProductModel(topRattedModelArrayList);

        return homeModel;
    }

    public static ArrayList<HomeModel> handleShopFragmentData(String res) {
        ArrayList<HomeModel> rootArrayList = new ArrayList<>();
        JSONObject jsonObject = createJsonObject(res);
        if (jsonObject != null) {
            rootArrayList.add(getTopRatedForShop(jsonObject));
            rootArrayList.add(getCategoryForShop(jsonObject));
            rootArrayList.add(getHotProducts(jsonObject));
        }
        return rootArrayList;
    }
}

