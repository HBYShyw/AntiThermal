package com.oplus.oms.split.splitload.extension;

import com.oplus.oms.split.common.ReflectUtil;
import com.oplus.oms.split.common.SplitLog;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
final class ComponentInfoManager {
    private static final String ACTIVITIES_SUFFIX = "_ACTIVITIES";
    private static final String APPLICATION_SUFFIX = "_APPLICATION";
    private static final String CLASS_COMPONENTINFO = "com.oplus.oms.split.core.extension.ComponentInfo";
    private static final String RECEIVERS_SUFFIX = "_RECEIVERS";
    private static final String SERVICES_SUFFIX = "_SERVICES";
    private static final String TAG = "ComponentInfoManager";

    private static Class<?> getComponentInfoClass() throws ClassNotFoundException {
        return ReflectUtil.getClass(CLASS_COMPONENTINFO);
    }

    private ComponentInfoManager() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getSplitApplication(String splitName) {
        String fieldName = splitName + APPLICATION_SUFFIX;
        try {
            Field field = getComponentInfoClass().getField(fieldName);
            field.setAccessible(true);
            return (String) field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            SplitLog.e(TAG, "getSplitApplication error", new Object[0]);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] getSplitActivities(String splitName) {
        String fieldName = splitName + ACTIVITIES_SUFFIX;
        try {
            Field field = getComponentInfoClass().getField(fieldName);
            field.setAccessible(true);
            String result = (String) field.get(null);
            if (result != null) {
                return result.split(",");
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            SplitLog.e(TAG, "getSplitActivities error", new Object[0]);
        }
        return new String[0];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] getSplitServices(String splitName) {
        String fieldName = splitName + SERVICES_SUFFIX;
        try {
            Field field = getComponentInfoClass().getField(fieldName);
            field.setAccessible(true);
            String result = (String) field.get(null);
            if (result != null) {
                return result.split(",");
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            SplitLog.e(TAG, "getSplitServices error", new Object[0]);
        }
        return new String[0];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] getSplitReceivers(String splitName) {
        String fieldName = splitName + RECEIVERS_SUFFIX;
        try {
            Field field = getComponentInfoClass().getField(fieldName);
            field.setAccessible(true);
            String result = (String) field.get(null);
            if (result != null) {
                return result.split(",");
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            SplitLog.e(TAG, "getSplitReceivers error", new Object[0]);
        }
        return new String[0];
    }
}
