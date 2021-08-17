package com.app.frimline.Common;

import android.content.Context;
import android.util.Log;

import com.app.frimline.models.Billing;
import com.app.frimline.models.BlogModel;
import com.app.frimline.models.CategoryRootFragments.CategoryRootModel;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.models.CategoryRootFragments.ReviewRootModel;
import com.app.frimline.models.HomeFragements.Attribute;
import com.app.frimline.models.HomeFragements.BannerModel;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.models.HomeFragements.SectionPositionModel;
import com.app.frimline.models.HomeFragements.Tags;
import com.app.frimline.models.HomeFragements.TradingStoriesModel;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.OrderModel;
import com.app.frimline.models.OrderedProductModel;
import com.app.frimline.models.ProfileModel;
import com.app.frimline.models.QAModel;
import com.app.frimline.models.SearchModel;

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

    public static ArrayList<ProductModel> commonProductParsing(JSONArray productArr) {
        ArrayList<ProductModel> poductList = new ArrayList<>();
        for (int i = 0; i < productArr.length(); i++) {
            try {
                JSONObject productObj = productArr.getJSONObject(i);
                ProductModel model = new ProductModel();
                model.setId(getString(productObj, "id"));
                model.setName(getString(productObj, "name"));
                model.setSlug(getString(productObj, "slug"));
                model.setRating(getString(productObj, "rating_count"));
                model.setDescription(getString(productObj, "description"));
                model.setShortDescription(getString(productObj, "short_description"));
                model.setPrice(HELPER.format.format(Integer.parseInt(getString(productObj, "price"))));
                model.setCalculatedAmount(HELPER.format.format(Integer.parseInt(getString(productObj, "price"))));
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


                Attribute attribute = new Attribute();
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
                poductList.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return poductList;
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
        ArrayList<ProductModel> topRattedModelArrayList = commonProductParsing(topRatedArr);
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.TOP_RATTED);
        homeModel.setLayoutIndex(getPosition(jsonObject, "Top Rated"));
        homeModel.setApiProductModel(topRattedModelArrayList);

        return homeModel;
    }

    public static HomeModel getProducts(JSONObject jsonObject) {
        JSONArray productArr = getJSONArray(jsonObject, "product");
        List<ProductModel> productModels = commonProductParsing(productArr);


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
    public static ArrayList<ProductModel> convertListToArray(List<ProductModel> array){
        return new ArrayList<>(array);
    }

    public static HomeModel getHotProducts(JSONObject jsonObject) {
        JSONArray topRatedArr = getJSONArray(jsonObject, "hot_product");
        ArrayList<ProductModel> topRattedModelArrayList = commonProductParsing(topRatedArr);
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
        ArrayList<ProductModel> topRattedModelArrayList = commonProductParsing(topRatedArr);
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_TOP_PRODUCT);
        homeModel.setApiProductModel(topRattedModelArrayList);

        return homeModel;
    }

    public static ArrayList<HomeModel> handleShopFragmentData(String res) {
        ArrayList<HomeModel> rootArrayList = new ArrayList<>();
        JSONObject jsonObject = createJsonObject(res);
        if (jsonObject != null) {
            int count=0;
            HomeModel topRatedModel = getTopRatedForShop(jsonObject);
            if (topRatedModel.getApiProductModel().size() != 0) {
                rootArrayList.add(topRatedModel);
                count++;
            }

            HomeModel categoryModel = getCategoryForShop(jsonObject);
            if (categoryModel.getCategoryArrayList().size() != 0)
                rootArrayList.add(categoryModel);

            HomeModel hotProductModel = getHotProducts(jsonObject);
            if (hotProductModel.getApiProductModel().size() != 0) {
                rootArrayList.add(hotProductModel);
                count++;
            }

            if (count == 0){
                rootArrayList.clear();
            }


        }
        return rootArrayList;
    }

    public static ReviewRootModel handleReviewList(String apiData) {
        ReviewRootModel model = new ReviewRootModel();

        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(apiData);
            if (jsonArr.length() != 0) {
                ArrayList<ReviewRootModel.Review> reviewsList = new ArrayList<>();
                for (int i = 0; i < jsonArr.length(); i++) {
                    ReviewRootModel.Review review = model.new Review();
                    review.setId(jsonArr.getJSONObject(i).getString("id"));
                    review.setReviewerEmail(jsonArr.getJSONObject(i).getString("reviewer_email"));
                    review.setReview(jsonArr.getJSONObject(i).getString("review"));
                    review.setRating(jsonArr.getJSONObject(i).getString("rating"));
                    review.setStatus(jsonArr.getJSONObject(i).getString("status"));
                    if (jsonArr.getJSONObject(i).get("reviewer_avatar_urls") instanceof JSONObject && jsonArr.getJSONObject(i).getJSONObject("reviewer_avatar_urls").has("96")) {
                        review.setUserAvatar(((JSONObject) jsonArr.getJSONObject(i).get("reviewer_avatar_urls")).getString("96"));
                    }

                    reviewsList.add(review);
                }
                model.setReviewsList(reviewsList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return model;

    }


    public static QAModel handleQAFragment(String apiData) {
        QAModel model = new QAModel();

        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(apiData);
            if (jsonArr.length() != 0) {
                ArrayList<QAModel> reviewsList = new ArrayList<>();
                for (int i = 0; i < jsonArr.length(); i++) {
                    QAModel review = new QAModel();
                    review.setId(jsonArr.getJSONObject(i).getString("id"));
                    if (jsonArr.getJSONObject(i).get("answer") instanceof JSONArray) {
                        if (jsonArr.getJSONObject(i).getJSONArray("answer").length() != 0) {
                            review.setAnswer(jsonArr.getJSONObject(i).getJSONArray("answer").getJSONObject(0).getString("content"));
                        }
                    }
                    review.setQuestion(jsonArr.getJSONObject(i).getString("content"));


                    reviewsList.add(review);
                }
                model.setBlogList(reviewsList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return model;

    }

    public static ArrayList<BlogModel> handleResponseBlogFragment(String res) {
        ArrayList<BlogModel> rootArrayList = new ArrayList<>();

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(res);
            for (int i = 0; i < jsonArray.length(); i++) {
                BlogModel model = new BlogModel();
                if (i % 2 == 0) {
                    model.setLayoutType(LAYOUT_TYPE.LAYOUT_RIGHT_BLOG);
                } else {
                    model.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
                }
                model.setId(jsonArray.getJSONObject(i).getString("id"));
                model.setDate(jsonArray.getJSONObject(i).getString("date"));
                model.setTitle(jsonArray.getJSONObject(i).getJSONObject("title").getString("rendered"));
                model.setShortContent(jsonArray.getJSONObject(i).getJSONObject("excerpt").getString("rendered"));
                model.setContent(jsonArray.getJSONObject(i).getJSONObject("content").getString("rendered"));
                rootArrayList.add(model);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootArrayList;

    }

    public static ArrayList<BlogModel> handleResponseRecentBlog(String res) {
        ArrayList<BlogModel> rootArrayList = new ArrayList<>();
        ArrayList<BlogModel> tempArray = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(res);
            for (int i = 0; i < jsonArray.length(); i++) {
                BlogModel model = new BlogModel();
                if (i % 2 == 0) {
                    model.setLayoutType(LAYOUT_TYPE.LAYOUT_RIGHT_BLOG);
                } else {
                    model.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
                }
                model.setId(jsonArray.getJSONObject(i).getString("id"));
                model.setDate(jsonArray.getJSONObject(i).getString("date"));
                //image link for blog
                model.setBlogImage(jsonArray.getJSONObject(i).getString("date"));

                model.setTitle(jsonArray.getJSONObject(i).getJSONObject("title").getString("rendered"));
                model.setShortContent(jsonArray.getJSONObject(i).getJSONObject("excerpt").getString("rendered"));
                model.setContent(jsonArray.getJSONObject(i).getJSONObject("content").getString("rendered"));
                rootArrayList.add(model);
            }

            tempArray = new ArrayList<>();
            ArrayList<ArrayList<BlogModel>> chunkArray = chunkBlog(rootArrayList, 2);

            for (int i = 0; i < chunkArray.size(); i++) {
                BlogModel model = new BlogModel();
                ArrayList<BlogModel> modelArrayList = chunkArray.get(i);
                if (modelArrayList.size() == 1) {
                    model.setLayoutType(LAYOUT_TYPE.LAYOUT_ONE_BLOG);
                } else {
                    model.setLayoutType(LAYOUT_TYPE.LAYOUT_TWO_BLOG);
                }
                model.setBlogList(modelArrayList);
                tempArray.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tempArray;

    }

    public static ArrayList<ArrayList<BlogModel>> chunkBlog(List<BlogModel> array, int chunkSize) {
        int numOfChunks = (int) Math.ceil((double) array.size() / chunkSize);
        ArrayList<ArrayList<BlogModel>> output = new ArrayList<>();

        final AtomicInteger counter = new AtomicInteger();

        for (BlogModel number : array) {
            if (counter.getAndIncrement() % chunkSize == 0) {
                output.add(new ArrayList<>());
            }
            output.get(output.size() - 1).add(number);
        }

        return output;
    }

    public static ProfileModel parseSignUpResponse(String response) {
        ProfileModel model = null;

        JSONObject jsonObj = createJsonObject(response);

        try {
            assert jsonObj != null;
            if (getString(jsonObj, "code").equalsIgnoreCase("200") && jsonObj.get("data") instanceof JSONObject) {
                model = new ProfileModel();
                model.setEmail(getString(getJSONObject(jsonObj, "data"), "user_email"));
                model.setDisplayName(getString(getJSONObject(jsonObj, "data"), "user_display_name"));
                model.setToken(getString(getJSONObject(jsonObj, "data"), "token"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return model;

    }

    public static ArrayList<OrderModel> parseOrderHistory(String response) {
        ArrayList<OrderModel> arrayList = new ArrayList<>();


        try {
            JSONArray array = new JSONArray(response);

            for (int i = 0; i < array.length(); i++) {
                OrderModel orderModel = new OrderModel();
                JSONObject object = array.getJSONObject(i);
                orderModel.setOrderId(getString(object, "id"));
                orderModel.setOrderKey(getString(object, "order_key"));
                orderModel.setStatus(getString(object, "status"));
                orderModel.setOrderDate(getString(object, "date_created"));
                orderModel.setDiscountTotal(getString(object, "discount_total"));
                orderModel.setDiscountTax(getString(object, "discount_tax"));
                orderModel.setShippingTotal(getString(object, "shipping_total"));
                orderModel.setShippingTax(getString(object, "shipping_tax"));
                orderModel.setCartTax(getString(object, "cart_tax"));
                orderModel.setTotal(getString(object, "total"));
                orderModel.setTotal_tax(getString(object, "total_tax"));
                orderModel.setCustomerNote(getString(object, "customer_note"));
                orderModel.setPaymentMethod(getString(object, "payment_method"));
                orderModel.setPaymentMethodTitle(getString(object, "payment_method_title"));
                orderModel.setTransactionId(getString(object, "transaction_id"));
                orderModel.setDatePaid(getString(object, "date_paid"));

                Billing billingAddress = new Billing();
                billingAddress.setFirstName(getString(getJSONObject(object, "billing"), "first_name"));
                billingAddress.setLastName(getString(getJSONObject(object, "billing"), "last_name"));
                billingAddress.setCompany(getString(getJSONObject(object, "billing"), "company"));
                billingAddress.setAddress1(getString(getJSONObject(object, "billing"), "address_1"));
                billingAddress.setAddress2(getString(getJSONObject(object, "billing"), "address_2"));
                billingAddress.setCity(getString(getJSONObject(object, "billing"), "city"));
                billingAddress.setState(getString(getJSONObject(object, "billing"), "state"));
                billingAddress.setPostCode(getString(getJSONObject(object, "billing"), "postcode"));
                billingAddress.setCountry(getString(getJSONObject(object, "billing"), "country"));
                billingAddress.setEmail(getString(getJSONObject(object, "billing"), "email"));
                billingAddress.setPhone(getString(getJSONObject(object, "billing"), "phone"));
                orderModel.setBillingAddress(billingAddress);


                Billing shippingAddress = new Billing();
                shippingAddress.setFirstName(getString(getJSONObject(object, "shipping"), "first_name"));
                shippingAddress.setLastName(getString(getJSONObject(object, "shipping"), "last_name"));
                shippingAddress.setCompany(getString(getJSONObject(object, "shipping"), "company"));
                shippingAddress.setAddress1(getString(getJSONObject(object, "shipping"), "address_1"));
                shippingAddress.setAddress2(getString(getJSONObject(object, "shipping"), "address_2"));
                shippingAddress.setCity(getString(getJSONObject(object, "shipping"), "city"));
                shippingAddress.setState(getString(getJSONObject(object, "shipping"), "state"));
                shippingAddress.setPostCode(getString(getJSONObject(object, "shipping"), "postcode"));
                shippingAddress.setCountry(getString(getJSONObject(object, "shipping"), "country"));
                orderModel.setShippingAddress(shippingAddress);


                ArrayList<OrderedProductModel> productList = new ArrayList<>();
                JSONArray productArr = getJSONArray(object, "line_items");
                for (int k = 0; k < productArr.length(); k++) {
                    JSONObject productObj = productArr.getJSONObject(k);
                    OrderedProductModel productModel = new OrderedProductModel();
                    productModel.setId(productObj.getString("id"));
                    productModel.setName(productObj.getString("name"));
                    productModel.setProductId(productObj.getString("product_id"));
                    productModel.setQty(productObj.getString("quantity"));
                    productModel.setSubTotal(productObj.getString("subtotal"));
                    productModel.setSubTotalTxt(productObj.getString("subtotal_tax"));
                    productModel.setTotal(productObj.getString("total"));
                    productModel.setTotalTax(productObj.getString("total_tax"));
                    productModel.setProductPrice(productObj.getString("price"));
                    productList.add(productModel);
                }
                orderModel.setProductsList(productList);
                arrayList.add(orderModel);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return arrayList;

    }


    public static ArrayList<SearchModel> parseSearch(String response) {
        ArrayList<SearchModel> searchListResult = null;

        JSONObject searchObj = createJsonObject(response);
        if (searchObj != null) {

            try {
                searchListResult = new ArrayList<>();
                ArrayList<ProductModel> arrayProduct = commonProductParsing(searchObj.getJSONArray("products"));
                int totalSize = arrayProduct.size();
                int firstPartSize = Integer.parseInt(String.valueOf(totalSize / 2));
                List<ProductModel> topProduct =  arrayProduct.subList(0, firstPartSize);

                ArrayList<ArrayList<ProductModel>> chunkArray = chunkArray(topProduct, 3);
                ArrayList<HomeModel> tempArray = new ArrayList<>();

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

                //-----------------------------------------------

                SearchModel topSearchModel = new SearchModel();
                topSearchModel.setLayoutType(LAYOUT_TYPE.LAYOUT_TOP_PRODUCT);
                List<ProductModel> HotProduct =   arrayProduct.subList(firstPartSize, arrayProduct.size());
                Log.e("HotProduct", String.valueOf(HotProduct.size()));
                topSearchModel.setTopProduct(tempArray);
                searchListResult.add(topSearchModel);

                //----------------------------------------
                JSONArray categoryArr = searchObj.getJSONArray("category");
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
                SearchModel categoryModel = new SearchModel();
                categoryModel.setLayoutType(LAYOUT_TYPE.LAYOUT_FILTER_CHIP);
                categoryModel.setCategoryList(categoryList);
                searchListResult.add(categoryModel);

                //------------------------------------------
                SearchModel hotSearchModel = new SearchModel();
                hotSearchModel.setLayoutType(LAYOUT_TYPE.LAYOUT_HOT_PRODUCT);
                hotSearchModel.setHotProduct(new ArrayList<>(HotProduct));
                searchListResult.add(hotSearchModel);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return searchListResult;
    }
}

