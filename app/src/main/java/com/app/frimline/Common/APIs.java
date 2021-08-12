package com.app.frimline.Common;

public class APIs {
    public static final String BASE = "https://frimline.queryfinders.com";
    public static final String TODAY_TOMORROW = BASE + "/wp-json/wc/v3/about-us";
    public static final String THEME_COLOR = BASE + "/wp-json/wc/v3/theme-color";
    public static final String CATEGORY_HOME = BASE + "/wp-json/wc/v3/category-home/";
    public static final String SHOP = BASE + "/wp-json/wc/v3/shop";
    public static final String PRODUCT_REVIEW = BASE + "/wp-json/wc/v3/product-reviews/";
    public static final String PRODUCT_QA = BASE + "/wp-json/wc/v3/question_answer/9659";
    public static final String BLOGS = BASE + "/wp-json/wp/v2/posts";
    public static final String SIGN_UP = BASE + "/wp-json/wp/v2/user/register";
    public static final String SIGN_IN = BASE + "/wp-json/wp/v2/user/login";
    public static final String FORGOT_PASSWORD = BASE + "/wp-json/wp/v2/user/lostpassword";
    public static final String RECENT_BLOG = BASE + "/wp-json/wp/v2/posts/?per_page=3&orderby=modified&order=desc";
    public static final String PROFILE = BASE + "/wp-json/wp/v2/user/info";
    public static final String UPDATE_BILLING_ADDRESS = BASE + "/wp-json/wp/v2/user_billing/update";
    public static final String UPDATE_SHIPPING_ADDRESS = BASE + "/wp-json/wp/v2/user_shipping/update";
    public static final String ORDER_HISTORY = BASE + "/wp-json/wc/v3/order";


}
