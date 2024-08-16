package com.oplus.deepthinker.sdk.app;

import android.net.Uri;
import android.os.Bundle;

/* loaded from: classes.dex */
public interface IProviderFeature extends ICommonFeature {
    public static final String APP_SUPERVISOR_QUERY_BLACK_GUARD_ACTIVITY = "query_black_guard_activity";
    public static final String COMMON_ARG1 = "common_arg1";
    public static final String COMMON_ARG2 = "common_arg2";
    public static final String COMMON_RESULT = "common_result";
    public static final String COMMON_TYPE1 = "common_type1";
    public static final String FEATURE_ABILITY_APP_SUPERVISOR = "ability_app_supervisor";
    public static final String FEATURE_ABILITY_CHARGE_PREDICTION = "ability_charge_prediction";
    public static final String FEATURE_ABILITY_EVENT_ASSOCIATION = "ability_event_association";
    public static final String FEATURE_ABILITY_INOUTDOOR = "ability_in_outdoor";
    public static final String FEATURE_ABILITY_USERPROFILE = "ability_userprofile";
    public static final String FEATURE_APP_IMPORTANCE = "ability_app_importance";
    public static final String FEATURE_APP_IMPORTANCE_GET_RESULT = "ability_app_importance_1";
    public static final String FEATURE_APP_IMPORTANCE_GET_RESULT_LOCKED = "ability_app_importance_2";
    public static final String FEATURE_APP_IMPORTANCE_PARAM_SERIALIZABLE_MAP = "ability_app_importance_param_0";
    public static final String USERPROFILE_QUERY = "query_user_profile";
    public static final String USERPROFILE_QUERY_RESULT = "query_result_user_profile";
    public static final String USERPROFILE_SPECIFIC_QUERY = "query_specific_profile";
    public static final Uri FEATURE_PROVIDER = Uri.parse("content://com.oplus.deepthinker.provider.feature");
    public static final Uri WIFI_LABEL_URI = Uri.parse("content://com.oplus.deepthinker.provider.feature/profile/wifi_label");
    public static final Uri HOME_LABEL_URI = Uri.parse("content://com.oplus.deepthinker.provider.feature/profile/home_label");
    public static final Uri COMPANY_LABEL_URI = Uri.parse("content://com.oplus.deepthinker.provider.feature/profile/company_label");
    public static final Uri COMMUTE_LABEL_URI = Uri.parse("content://com.oplus.deepthinker.provider.feature/profile/commute_label");

    default Bundle call(String str, Bundle bundle) {
        return null;
    }

    default String getTag() {
        return "";
    }
}
