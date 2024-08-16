package com.oplus.deepthinker.userprofile;

import android.net.Uri;

/* loaded from: classes.dex */
public class UserProfileConstants {
    public static final String COLUMN_CONFIDENCE = "confidence";
    public static final String COLUMN_DATA_CYCLE = "data_cycle";
    public static final String COLUMN_DETAIL = "detail";
    public static final String COLUMN_EXPIRATION_TIME = "expiration_time";
    public static final String COLUMN_LABEL_ID = "label_id";
    public static final String COLUMN_LABEL_RESULT = "label_result";
    public static final String COLUMN_PKG_NAME = "pkg_name";
    public static final int DEFAULT_LONG_TERM_DATA_CYCLE = 30;
    public static final int DEFAULT_SHORT_TERM_DATA_CYCLE = 1;
    public static final String FEATURE_ABILITY_USERPROFILE = "ability_userprofile";
    public static final String FEATURE_APP_IMPORTANCE = "ability_app_importance";
    public static final String FEATURE_APP_IMPORTANCE_GET_RESULT = "ability_app_importance_1";
    public static final String FEATURE_APP_IMPORTANCE_GET_RESULT_LOCKED = "ability_app_importance_2";
    public static final String FEATURE_APP_IMPORTANCE_PARAM_SERIALIZABLE_MAP = "ability_app_importance_param_0";
    public static final Uri FEATURE_PROVIDER = Uri.parse("content://com.oplus.deepthinker.provider.feature");
    public static final String KEY_DATA_CYCLE = "data_cycle";
    public static final String KEY_LABEL_ID = "label_id";
    public static final String KEY_LABEL_TYPE = "label_type";
    public static final int TYPE_APP_LABEL = 1;
    public static final int TYPE_USER_LABEL = 0;
    public static final String USERPROFILE_QUERY = "query_user_profile";
    public static final String USERPROFILE_QUERY_RESULT = "query_result_user_profile";

    /* loaded from: classes.dex */
    public enum LabelId {
        NOTIFICATION_IMPORTANCE(2),
        APP_ACTIVATION(3),
        TOP_APPS(4),
        MORNING_APPS(5),
        NOON_APPS(6),
        EVENING_APPS(7),
        SLEEP_APPS(8),
        APP_ACTIVE_TIME_PERIOD(9),
        TOP_POWER_COMSUMPTION_APPS(10),
        BATTERY_USAGE(11),
        POWER_ACCESSIBILITY(12),
        ACTIVE_TIME_PERIOD(13),
        INACTIVE_TIME_PERIOD(14),
        TRAFFIC_USAGE(15),
        WIFI_DEPENDENCY(16),
        TRAFFIC_SENSITIVE(17),
        SLEEP_HABIT(18),
        SLEEP_CHARGE(19),
        SLEEP_ADEQUACY(20),
        WAKE_APPS(21),
        APP_INACTIVE_TIME_PERIOD(22),
        CHARGING_LOCATION(23),
        WIFI_LOCATION(24),
        BATTERY_CHARGE_HABIT(25),
        BATTERY_DISCHARGE_HABIT(26),
        WIFI_SETTING_HABIT(27),
        BT_SETTING_HABIT(28),
        MUTE_SETTING_HABIT(29),
        APP_JUMP_RELATION(30),
        APP_EXPLORE(31),
        RESIDENCE(32),
        APP_WIFI_DEPENDENCY(33),
        APP_MOBILE_TRAFFIC_SENSITIVE(34),
        APP_WEEKDAY_ACTIVE_TIME_PERIOD(35),
        APP_WEEKEND_ACTIVE_TIME_PERIOD(36),
        APP_WEEKDAY_INACTIVE_TIME_PERIOD(37),
        APP_WEEKEND_INACTIVE_TIME_PERIOD(38),
        APP_NOTIFICATION(39),
        APP_LONG_TIME_USE(40),
        APP_PREFERENCE(41),
        APP_BG_VECTOR(42);

        private int value;

        public final int getValue() {
            return this.value;
        }

        public final void setValue(int v) {
            this.value = v;
        }

        LabelId(int value) {
            this.value = value;
        }
    }
}
