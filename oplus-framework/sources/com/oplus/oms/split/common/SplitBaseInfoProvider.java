package com.oplus.oms.split.common;

import java.lang.reflect.Field;
import java.util.Map;

/* loaded from: classes.dex */
public class SplitBaseInfoProvider {
    private static final String CLASS_OMS_CONFIG = ".OmsConfig";
    private static final String CLASS_PREFIX = "com.oplus.ocs";
    private static final String COMPONENT_FEATURES = "COMPONENT_FEATURES";
    private static final String DEFAULT_SPLIT_INFO_VERSION = "DEFAULT_SPLIT_INFO_VERSION";
    private static final String DEFAULT_SPLIT_INFO_VERSION_VALUE = "unknown_1.0";
    private static final String DEFAULT_VALUE = "unknown";
    private static final String DYNAMIC_FEATURES = "DYNAMIC_FEATURES";
    private static final String OMS_ID = "OMS_ID";
    private static final String PROCESS_MAP = "sProcessMap";
    private static final String SPLIT_MAP = "sSplitMap";
    private static final String TAG = "SplitBaseInfoProvider";
    private static final String VERSION_NAME = "VERSION_NAME";

    private static Class<?> getOmsConfigClass() throws ClassNotFoundException {
        try {
            return ReflectUtil.getClass("com.oplus.ocs.OmsConfig");
        } catch (ClassNotFoundException e) {
            SplitLog.w(TAG, "Oms Warning: Can't find class com.oplus.ocs.OmsConfig.class!", new Object[0]);
            throw e;
        }
    }

    private SplitBaseInfoProvider() {
    }

    public static String getVersionName() {
        try {
            Field field = getOmsConfigClass().getField(VERSION_NAME);
            field.setAccessible(true);
            return (String) field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            SplitLog.e(TAG, "getVersionName error", new Object[0]);
            return "unknown";
        }
    }

    public static String getOmsId() {
        try {
            Field field = getOmsConfigClass().getField(OMS_ID);
            field.setAccessible(true);
            return (String) field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            SplitLog.e(TAG, "getOmsId error", new Object[0]);
            return "unknown";
        }
    }

    public static String getDefaultSplitInfoVersion() {
        try {
            Field field = getOmsConfigClass().getField(DEFAULT_SPLIT_INFO_VERSION);
            field.setAccessible(true);
            return (String) field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            SplitLog.e(TAG, "getDefaultSplitInfoVersion error", new Object[0]);
            return DEFAULT_SPLIT_INFO_VERSION_VALUE;
        }
    }

    public static String[] getDynamicFeatures() {
        try {
            Field field = getOmsConfigClass().getField(DYNAMIC_FEATURES);
            field.setAccessible(true);
            return (String[]) field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            SplitLog.e(TAG, "getDynamicFeatures error", new Object[0]);
            return null;
        }
    }

    public static String[] getComponentFeatures() {
        try {
            Field field = getOmsConfigClass().getField(COMPONENT_FEATURES);
            field.setAccessible(true);
            return (String[]) field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            SplitLog.e(TAG, "getDynamicFeatures error", new Object[0]);
            return null;
        }
    }

    public static Map<String, ProcessInfoData> getProcessMap() {
        try {
            Field field = getOmsConfigClass().getField(PROCESS_MAP);
            field.setAccessible(true);
            return (Map) field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            SplitLog.e(TAG, "getProcessMap error", new Object[0]);
            return null;
        }
    }

    public static Map<String, SplitInfoData> getSplitMap() {
        try {
            Field field = getOmsConfigClass().getField(SPLIT_MAP);
            field.setAccessible(true);
            return (Map) field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            SplitLog.e(TAG, "getSplitMap error", new Object[0]);
            return null;
        }
    }
}
