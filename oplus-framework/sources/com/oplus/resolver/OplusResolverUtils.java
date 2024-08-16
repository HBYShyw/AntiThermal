package com.oplus.resolver;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import com.oplus.content.OplusFeatureConfigManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/* loaded from: classes.dex */
public class OplusResolverUtils {
    public static final String ARABIC_LAUGUAGE = "ar-AR";
    private static final int COLUMN_FIVE_COUNT = 5;
    private static final int COLUMN_FIVE_SCREEN_MIN_WIDTH = 444;
    private static final int COLUMN_FOUR_COUNT = 4;
    private static final int COLUMN_FOUR_SCREEN_MIN_WIDTH = 336;
    private static final int COLUMN_SIX_COUNT = 6;
    private static final int COLUMN_SIX_SCREEN_MIN_WIDTH = 528;
    private static final int COLUMN_THREE_COUNT = 3;
    private static final String FEATURE_FOLD = "oplus.hardware.type.fold";
    private static final String FEATURE_FOLD_REMAP_DISPLAY_DISABLED = "oplus.software.fold_remap_display_disabled";
    private static final String FEATURE_TABLET = "oplus.hardware.type.tablet";
    private static final int FOLLOW_SUIT_PANEL_WIDTH = 360;
    private static final int FOLLOW_SUIT_PANEL_WITH_PADDING_WIDTH = 408;
    public static final String HEBREW_LAUGUAGE = "iw-IL";
    private static final int LARGE_SCREEN_GRID_ALL_COLUMN_COUNT = 12;
    private static final int LARGE_SCREEN_PANEL_GRID_ALL_COLUMN_COUNT = 6;
    private static final int MIDDLE_SCREEN_GRID_ALL_COLUMN_COUNT = 8;
    private static final int MIDDLE_SCREEN_ONE_ROW_DOMESTIC_SUPPORT_MAX_HEIGHT = 628;
    private static final int MIDDLE_SCREEN_ONE_ROW_EXPORT_SUPPORT_MAX_HEIGHT = 704;
    private static final int MIDDLE_SCREEN_PANEL_GRID_COLUMN_COUNT = 6;
    private static final int MIDDLE_SCREEN_WIDTH = 840;
    public static final String PERSIAN_LANGUAGE = "fa-IR";
    private static final int ROW_ONE_COUNT = 1;
    private static final int ROW_TWO_COUNT = 2;
    private static final int SMALL_SCREEN_ONE_ROW_DOMESTIC_SUPPORT_MAX_HEIGHT = 600;
    private static final int SMALL_SCREEN_ONE_ROW_EXPORT_SUPPORT_MAX_HEIGHT = 680;
    private static final int SMALL_SCREEN_WIDTH = 600;
    private static final String TAG = "OplusResolverUtils";
    public static final String URDU_LANGUAGE = "ur-PK";
    public static final String UYGHUR_LAUGUAGE = "ug-CN";

    public static boolean isTablet() {
        return OplusFeatureConfigManager.getInstacne().hasFeature("oplus.hardware.type.tablet");
    }

    public static boolean isFoldScreen() {
        return OplusFeatureConfigManager.getInstance().hasFeature("oplus.hardware.type.fold");
    }

    public static int calculateRowCount(Configuration cfg) {
        int rowCount = 1;
        float screenHeight = cfg.screenHeightDp;
        float screenWidth = cfg.screenWidthDp;
        if (screenWidth < 600.0f) {
            if (screenHeight >= 628.0f) {
                rowCount = 2;
            }
        } else if (screenHeight >= 600.0f) {
            rowCount = 2;
        }
        Log.d(TAG, "calculateRowCount rowCount=" + rowCount + " screenHeight: " + screenHeight);
        return rowCount;
    }

    public static int calculateColumnCount(Activity activity, float panelWidth) {
        int columnSize;
        boolean asFollowerPanel = asFollowerPanel(activity);
        if (asFollowerPanel) {
            columnSize = 4;
        } else if (panelWidth < 336.0f) {
            columnSize = 3;
        } else if (panelWidth < 444.0f) {
            columnSize = 4;
        } else if (panelWidth < 528.0f) {
            columnSize = 5;
        } else {
            columnSize = 6;
        }
        Log.d(TAG, "calculateItemCount columnSize=" + columnSize + " asFollowerPanel: " + asFollowerPanel);
        return columnSize;
    }

    public static int calculateResponsiveUIPanelWidth(Activity activity) {
        float panelWidth;
        float screenWidth = activity.getResources().getConfiguration().screenWidthDp;
        float screenHeight = activity.getResources().getConfiguration().screenHeightDp;
        boolean asFollowerPanel = asFollowerPanel(activity);
        if (asFollowerPanel) {
            panelWidth = 360.0f;
        } else if (screenWidth < 600.0f) {
            panelWidth = screenWidth;
        } else if (screenWidth < 840.0f) {
            panelWidth = (screenWidth / 8.0f) * 6.0f;
        } else {
            panelWidth = (screenWidth / 12.0f) * 6.0f;
            if (panelWidth < 444.0f) {
                panelWidth = 444.0f;
            }
        }
        Log.d(TAG, " calculateResponsiveUIPanelWidth screenWidth=" + screenWidth + ",screenHeight=" + screenHeight + ",panelWidth=" + panelWidth);
        return (int) panelWidth;
    }

    public static boolean calculateNotSmallScreen(Context context, Configuration cfg) {
        float screenWidth = cfg.screenWidthDp;
        return screenWidth >= 600.0f;
    }

    public static boolean isLargeScreen(Configuration cfg) {
        return cfg.screenWidthDp >= MIDDLE_SCREEN_WIDTH;
    }

    public static boolean asFollowerPanel(Activity activity) {
        return false;
    }

    public static boolean isRTLLanguage(Context context) {
        String languageCode = context.getString(201589177);
        return languageCode.equals(ARABIC_LAUGUAGE) || languageCode.equals(UYGHUR_LAUGUAGE) || languageCode.equals(HEBREW_LAUGUAGE) || languageCode.equals(PERSIAN_LANGUAGE) || languageCode.equals(URDU_LANGUAGE);
    }

    public static <T> T invokeMethod(Object obj, String str, Object... objArr) {
        if (obj != null && str != null) {
            return (T) invokeMethod(obj, obj.getClass(), str, null, objArr);
        }
        Log.e(TAG, "invokeMethod obj is null");
        return null;
    }

    public static <T> T invokeMethod(Object obj, String str, List<Class> list, Object... objArr) {
        if (obj == null || str == null) {
            Log.e(TAG, "invokeMethod obj is null");
            return null;
        }
        return (T) invokeMethod(obj, obj.getClass(), str, list, objArr);
    }

    public static <T> T getFiledValue(Object obj, String str) {
        if (obj == null || str == null) {
            Log.e(TAG, "getFiledValue obj is null");
            return null;
        }
        return (T) getFieldValue(obj, obj.getClass(), str);
    }

    private static <T> T invokeMethod(Object obj, Class cls, String str, List<Class> list, Object... objArr) {
        if (objArr != null) {
            try {
                if (objArr.length != 0) {
                    Class<?>[] clsArr = new Class[objArr.length];
                    for (int i = 0; i < objArr.length; i++) {
                        if (list != null && list.size() > i) {
                            clsArr[i] = list.get(i);
                        } else {
                            clsArr[i] = objArr[i].getClass();
                        }
                    }
                    Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
                    declaredMethod.setAccessible(true);
                    return (T) declaredMethod.invoke(obj, objArr);
                }
            } catch (NoSuchMethodException e) {
                if (cls.getSuperclass() != null) {
                    return (T) invokeMethod(obj, cls.getSuperclass(), str, list, objArr);
                }
                return null;
            } catch (Exception e2) {
                Log.e(TAG, "invokeMethod error:" + e2, e2);
                return null;
            }
        }
        Method declaredMethod2 = cls.getDeclaredMethod(str, new Class[0]);
        declaredMethod2.setAccessible(true);
        return (T) declaredMethod2.invoke(obj, new Object[0]);
    }

    private static <T> T getFieldValue(Object obj, Class cls, String str) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return (T) declaredField.get(obj);
        } catch (NoSuchFieldException e) {
            if (cls.getSuperclass() != null) {
                return (T) getFieldValue(obj, cls.getSuperclass(), str);
            }
            return null;
        } catch (Exception e2) {
            Log.e(TAG, "getFieldValue error:" + e2, e2);
            return null;
        }
    }
}
