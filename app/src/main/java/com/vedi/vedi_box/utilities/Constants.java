package com.vedi.vedi_box.utilities;

import java.util.HashMap;

public class Constants {
    public static String KEY_HOTLY = "";
    public static final String KEY_PREFERENCE_NAME = "VediBOXPreference";
    public static final String KEY_CART_STATUS = "cart_status";
    public static final String KEY_ORDER_STATUS = "order_status";
    public static final String KEY_ALERT_STATUS = "alert_status";
    public static final String KEY_ALERT_ONLINE = "internet_status";

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_MOBILE = "user_mobile";
    public static final String KEY_USER_FULL_NAME = "full_name";


    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_PRODUCT_SUB_CATEGORY = "product_sub_category";
    public static final String KEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRODUCT_IMAGE = "product_image";
    public static final String KEY_PRODUCT_RATE = "product_rate";
    public static final String KEY_PRODUCT_PIECES = "product_pieces";

    public static final String KEY_MINIMUM_PURCHASE = "minimum_purchase";
    public static final String KEY_DELIVERY_COST = "delivery_cost";

    public static final String KEY_SUB_CATEGORY_TYPE = "sub_category_type";
    public static final String KEY_CATEGORY_TYPE = "category_type";


    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String KEY_FCM_TOKEN = "fcm_token";
    public static final String SAVED_RECYCLER_POSITION = "recycler_position";
    public static final String KEY_CLASS_TYPE = "class_type";
    public static final String KEY_PRODUCT_STOCK = "product_stock";


    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.REMOTE_MSG_AUTHORIZATION, "Key=AAAAXF5-KdE:APA91bGUN2jE2kSPWAs9JBvnmybvwyVe4pZ016JxXJQrTneZCatBpVLnEeyY76bf6Ikmihn_xsthP-uNYYVTB2RsXSigX8hUYKM9hptWzhQDsRLX8-x5fsd9n9ETvXtcVLpQYmKGcUVT");
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;

    }

}
