package com.oapm.perftest.bean;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class UserDataUtil {
    private static final Map<String, String> sUserDataMap = new HashMap();

    public static void clearUserData() {
    }

    public static Map<String, String> getUserDataMap() {
        return sUserDataMap;
    }

    public static void putUserData(String str, String str2) {
    }

    public static String removeUserData(String str) {
        return null;
    }
}
