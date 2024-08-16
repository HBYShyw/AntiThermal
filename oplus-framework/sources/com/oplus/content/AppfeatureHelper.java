package com.oplus.content;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.content.IOplusFeatureConfigManager;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

/* loaded from: classes.dex */
public class AppfeatureHelper {
    private static final String CFG_ARGS_SPLIT = ";";
    private static final String CFG_ARG_SPLIT = ":";
    static String TAG = "AppfeatureHelper";
    private static final String[] APP_TABLE_NAME_MAP = {"app_feature", "app_feature_first", "app_feature_second"};

    /* loaded from: classes.dex */
    public enum CACHE_MODE {
        CACHE_ONLY,
        CACHE_AND_DB
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Uri getUriFromFeatureMap(int featureID) {
        if (featureID < 0 || featureID >= IOplusFeatureConfigManager.FeatureID.INVALID.ordinal()) {
            throw new IllegalArgumentException("invlid feature id");
        }
        Uri uri = Uri.parse("content://com.oplus.customize.coreapp.configmanager.configprovider.AppFeatureProvider");
        String[] strArr = APP_TABLE_NAME_MAP;
        if (!TextUtils.isEmpty(strArr[featureID])) {
            return uri.buildUpon().appendPath(strArr[featureID]).build();
        }
        throw new IllegalArgumentException("getUriFromFeatureMap featureID is not support");
    }

    public static int getInt(ContentResolver resolver, IOplusFeatureConfigManager.FeatureID featureID, String featureName, int def) {
        int parseInt;
        String v = getStringForFeature(resolver, featureID.ordinal(), featureName, "int");
        if (v == null) {
            parseInt = def;
        } else {
            try {
                parseInt = Integer.parseInt(v);
            } catch (Exception e) {
                Log.i(TAG, "getInt error:" + e.toString());
                return def;
            }
        }
        int value = parseInt;
        return value;
    }

    public static int getInt(ContentResolver resolver, String featureName, int def) {
        return getInt(resolver, IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT, featureName, def);
    }

    public static long getLong(ContentResolver resolver, IOplusFeatureConfigManager.FeatureID featureID, String featureName, long def) {
        long parseLong;
        String v = getStringForFeature(resolver, featureID.ordinal(), featureName, "long");
        if (v == null) {
            parseLong = def;
        } else {
            try {
                parseLong = Long.parseLong(v);
            } catch (Exception e) {
                Log.i(TAG, "getLong error:" + e.toString());
                return def;
            }
        }
        long value = parseLong;
        return value;
    }

    public static long getLong(ContentResolver resolver, String featureName, long def) {
        return getLong(resolver, IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT, featureName, def);
    }

    public static float getFloat(ContentResolver resolver, IOplusFeatureConfigManager.FeatureID featureID, String featureName, float def) {
        float parseFloat;
        String v = getStringForFeature(resolver, featureID.ordinal(), featureName, "float");
        if (v == null) {
            parseFloat = def;
        } else {
            try {
                parseFloat = Float.parseFloat(v);
            } catch (Exception e) {
                Log.i(TAG, "getFloat error:" + e.toString());
                return def;
            }
        }
        float value = parseFloat;
        return value;
    }

    public static float getFloat(ContentResolver resolver, String featureName, float def) {
        return getFloat(resolver, IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT, featureName, def);
    }

    public static boolean getBoolean(ContentResolver resolver, IOplusFeatureConfigManager.FeatureID featureID, String featureName, boolean def) {
        boolean parseBoolean;
        String v = getStringForFeature(resolver, featureID.ordinal(), featureName, "boolean");
        if (v == null) {
            parseBoolean = def;
        } else {
            try {
                parseBoolean = Boolean.parseBoolean(v);
            } catch (Exception e) {
                Log.i(TAG, "getBoolean error:" + e.toString());
                return def;
            }
        }
        boolean value = parseBoolean;
        return value;
    }

    public static boolean getBoolean(ContentResolver resolver, String featureName, boolean def) {
        return getBoolean(resolver, IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT, featureName, def);
    }

    public static String getString(ContentResolver resolver, IOplusFeatureConfigManager.FeatureID featureID, String featureName, String def) {
        String v = getStringForFeature(resolver, featureID.ordinal(), featureName, "String");
        if (v == null) {
            return def;
        }
        return v;
    }

    public static String getString(ContentResolver resolver, String featureName, String def) {
        return getString(resolver, IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT, featureName, def);
    }

    public static List<String> getStringList(ContentResolver resolver, IOplusFeatureConfigManager.FeatureID featureID, String featureName) {
        return getStringListForFeature(resolver, featureID.ordinal(), featureName);
    }

    public static List<String> getStringList(ContentResolver resolver, String featureName) {
        return getStringList(resolver, IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT, featureName);
    }

    public static boolean isFeatureSupport(ContentResolver resolver, IOplusFeatureConfigManager.FeatureID featureID, String featureName) {
        boolean isFeatureSupport = false;
        Cursor cursor = getAppFeatureCursor(resolver, featureID.ordinal(), featureName);
        if (cursor != null && cursor.getCount() > 0) {
            isFeatureSupport = true;
        }
        if (cursor != null) {
            cursor.close();
        }
        return isFeatureSupport;
    }

    public static boolean isFeatureSupport(ContentResolver resolver, String featureName) {
        return isFeatureSupport(resolver, IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT, featureName);
    }

    public static void enableAppFeatureCache(ContentResolver resolver, IOplusFeatureConfigManager.FeatureID featureID, List<String> featureList, CACHE_MODE cacheMode) {
        AppFeatureCache.getInstance().enableAppFeatureCache(resolver, featureID.ordinal(), featureList, cacheMode);
    }

    public static void enableAppFeatureCache(ContentResolver resolver, List<String> featureList, CACHE_MODE cacheMode) {
        enableAppFeatureCache(resolver, IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT, featureList, cacheMode);
    }

    public static void registerContentObserver(ContentResolver resolver, boolean notifyForDescendants, ContentObserver observer) {
        registerContentObserver(resolver, IOplusFeatureConfigManager.FeatureID.STATIC_COMPONENT, notifyForDescendants, observer);
    }

    public static void registerContentObserver(ContentResolver resolver, IOplusFeatureConfigManager.FeatureID featureID, boolean notifyForDescendants, ContentObserver observer) {
        Uri uri = getUriFromFeatureMap(featureID.ordinal());
        if (resolver != null && observer != null) {
            resolver.registerContentObserver(uri, notifyForDescendants, observer);
        }
    }

    public static void unRegisterContentObserver(ContentResolver resolver, ContentObserver observer) {
        if (resolver != null && observer != null) {
            resolver.unregisterContentObserver(observer);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0017, code lost:
    
        r2 = r1.getString(r1.getColumnIndexOrThrow("parameters"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0021, code lost:
    
        if (r2 == null) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0027, code lost:
    
        if (r2.isEmpty() != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0029, code lost:
    
        r3 = r2.split(com.oplus.content.AppfeatureHelper.CFG_ARGS_SPLIT);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0030, code lost:
    
        if (r3.length <= 0) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0032, code lost:
    
        r4 = r3.length;
        r6 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0035, code lost:
    
        if (r6 >= r4) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0037, code lost:
    
        r7 = r3[r6];
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0039, code lost:
    
        if (r7 == null) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x003f, code lost:
    
        if (r7.isEmpty() != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0041, code lost:
    
        r8 = r7.indexOf(com.oplus.content.AppfeatureHelper.CFG_ARG_SPLIT);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0047, code lost:
    
        if (r8 <= 0) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0049, code lost:
    
        r9 = r7.substring(0, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0051, code lost:
    
        if (r9.equals(r14) == false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0053, code lost:
    
        r0 = r7.substring(r8 + 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x005d, code lost:
    
        if (r0 == null) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x005a, code lost:
    
        r6 = r6 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0064, code lost:
    
        if (r1.moveToNext() != false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0015, code lost:
    
        if (r1.moveToFirst() != false) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static String getStringForFeature(ContentResolver resolver, int featureID, String featureName, String type) {
        String value = null;
        if (resolver != null && !TextUtils.isEmpty(featureName) && type != null) {
            Cursor cursor = getAppFeatureCursor(resolver, featureID, featureName);
            if (cursor != null) {
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return value;
    }

    private static List<String> getStringListForFeature(ContentResolver resolver, int featureID, String featureName) {
        List<String> list = new ArrayList<>();
        if (resolver != null && !TextUtils.isEmpty(featureName)) {
            Cursor cursor = getAppFeatureCursor(resolver, featureID, featureName);
            if (cursor != null && cursor.moveToFirst()) {
                String jsonStr = cursor.getString(cursor.getColumnIndexOrThrow("lists"));
                try {
                    list = jsonString2List(jsonStr);
                } catch (Exception e) {
                    Log.e(TAG, "getStringListForFeature error: " + e.toString());
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private static Cursor getAppFeatureCursor(ContentResolver resolver, int featureID, String featureName) {
        Cursor cursor = AppFeatureCache.getInstance().getCursorFromCache(featureID, featureName);
        if (cursor == null && AppFeatureCache.getCachedMode(featureID) != CACHE_MODE.CACHE_ONLY) {
            Uri uri = getUriFromFeatureMap(featureID);
            return resolver.query(uri, null, "featurename=?", new String[]{featureName}, null);
        }
        return cursor;
    }

    private static List<String> jsonString2List(String jsonString) throws Exception {
        List<String> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            String value = jsonArray.getString(i);
            list.add(value);
        }
        return list;
    }
}
