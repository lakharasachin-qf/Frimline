package com.app.frimline.Common;

public class APIs {
    public static final String BASE = "https://frimline.queryfinders.com";
    public static final String TODAY_TOMORROW = BASE + "/wp-json/wc/v3/about-us";
    public static final String THEME_COLOR = BASE + "/wp-json/wc/v3/theme-color";
    public static final String CATEGORY_HOME = BASE + "/wp-json/wc/v3/category-home/";
    public static final String SHOP = BASE + "/wp-json/wc/v3/shop";
    public static final String PRODUCT_REVIEW = BASE + "/wp-json/wc/v3/product-reviews/";
    public static final String PRODUCT_QA = BASE + "/wp-json/wc/v3/question_answer/";
    public static final String BLOGS = BASE + "/wp-json/wp/v2/posts";
    public static final String SIGN_UP = BASE + "/wp-json/wp/v2/user/register";
    public static final String SIGN_IN = BASE + "/wp-json/wp/v2/user/login";
    public static final String FORGOT_PASSWORD = BASE + "/wp-json/wp/v2/user/lostpassword";
    public static final String RECENT_BLOG = BASE + "/wp-json/wp/v2/posts/?per_page=3&orderby=modified&order=desc";
    public static final String PROFILE = BASE + "/wp-json/wp/v2/user/info";
    public static final String UPDATE_BILLING_ADDRESS = BASE + "/wp-json/wp/v2/user_billing/update";
    public static final String UPDATE_SHIPPING_ADDRESS = BASE + "/wp-json/wp/v2/user_shipping/update";
    public static final String ORDER_HISTORY = BASE + "/wp-json/wc/v3/order";
    public static final String SEARCH = BASE + "/wp-json/wc/v3/products_list";
    public static final String ADD_REVIEW = BASE + "/wp-json/wc/v3/product/addreview";
    public static final String ADD_QUESTION = BASE + "/wp-json/wc/v3/product/addquestion";
    public static final String UPDATE_PROFILE = BASE + "/wp-json/wp/v2/user/update";
    public static final String SIGN_IN_MOBILE = BASE + "/wp-json/wp/v2/user/mobilelogin";
    public static final String OTP_VERIFICATION = BASE + "/wp-json/wp/v2/user/validate/otp";
    public static final String PRODUCT_DETAILS = BASE + "/wp-json/wc/v3/product-details/";
    public static final String VALIDATE_CODE = BASE + "/wp-json/wc/v3/coupons/validate";
    public static final String CREATE_ORDER = BASE + "/wp-json/wc/v3/validate_order/create";
    public static final String COMPLETE_ORDER = BASE + "/wp-json/wc/v3/order/create";
    public static final String VERIFY_POSTCODE = BASE + "/wp-json/wc/v3/validate/pincode";
    public static final String GET_COUNTRY_STATE = BASE + "/wp-json/wc/v3/country/list";
    public static final String COD_CHARGES = BASE + "/wp-json/wc/v3/cod/charge";
    public static final String POP_OFFER = BASE + "/wp-json/wp/v2/get/popup_offer";
    public static final String WISHLIST = BASE + "/wp-json/wc/v3/wishlist/list";
    public static final String ADD_WISHLIST = BASE + "/wp-json/wc/v3/wishlist/add";
    public static final String REMOVE_WISHLIST = BASE + "/wp-json/wc/v3/wishlist/remove";
    public static final String SUBSCRIBE_NOTIFICATION = BASE + "/wp-json/pd/fcm/subscribe?user_email=sunnypatel4773@gmail.com&device_token=12345852&subscribed=notification &api_secret_key=KUbPbwoKYw)(AHg(93o!RRw%";


}
