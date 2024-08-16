package com.oplus.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.util.Slog;
import android.util.SparseIntArray;
import com.oplus.notification.redpackage.RedPackageAssistRUSManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusNavigationBarUtil {
    private static final String ACTIVITY_COLOR = "activityColor";
    private static final String ACTIVITY_NAME = "activityName";
    private static final int ALPHA_BIT_NUM = 4;
    private static final int COLOR_ALPHA_OPAQUE = -16777216;
    private static final int COLOR_BIT_NUM = 6;
    private static final boolean DEBUG_OPLUS_SYSTEMBAR = false;
    private static final String DEFAULT_COLOR = "default_color";
    private static final int HEX_NUM = 16;
    private static final String IS_NEED_PALETTE = "is_need_palette";
    private static final int MAX_COUNT = 20;
    private static final String NAVBAR_BACKGROUND = "nav_bg";
    private static final String NAV_BG_COLOR = "bg_color";
    private static final String PKG = "pkg";
    private static final String PKG_VERSION = "pkg_version";
    private Context mContext;
    private static final String TAG = OplusNavigationBarUtil.class.getSimpleName();
    private static final String[] mDefaultAdaptationAppNames = new String[0];
    private static final int[] mDefaultAppColors = new int[0];
    private static final String[] mDefaultNotAdaptationActivityNames = new String[0];
    private static final int[] mDefaultNotAdaptationActivityColors = new int[0];
    private static final List<AdaptationAppInfo> mDefaultAdapationApps = new ArrayList();
    private static final List<AdaptationAppInfo> mStatusDefaultAdapationApps = new ArrayList();
    private static final List<AdaptationActivityInfo> mDefaultNotAdapationActivities = new ArrayList();
    private static final Object mObject = new Object();
    private static volatile OplusNavigationBarUtil sColorNavigationBarUtil = null;
    private boolean mHasInitialized = false;
    private boolean mReadNavData = false;
    private boolean mReadStatusData = false;
    private boolean mUseDefualtData = true;
    private int mUpdateNavCount = 0;
    private int mUpdateStaCount = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class AdaptationActivityInfo {
        String mActivityName;
        int mDefaultColor;

        AdaptationActivityInfo() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class AdaptationAppInfo {
        Map<String, String> mActivityColorList;
        SparseIntArray mColorArray;
        int mDefaultColor;
        boolean mIsNeedPalette;
        int[] mKeys;
        String mPkg;

        AdaptationAppInfo() {
        }
    }

    public static OplusNavigationBarUtil getInstance() {
        if (sColorNavigationBarUtil == null) {
            synchronized (OplusNavigationBarUtil.class) {
                if (sColorNavigationBarUtil == null) {
                    sColorNavigationBarUtil = new OplusNavigationBarUtil();
                }
            }
        }
        return sColorNavigationBarUtil;
    }

    private OplusNavigationBarUtil() {
    }

    public void init(Context context) {
        this.mContext = context;
        this.mHasInitialized = true;
        updateAppNavBarDefaultList();
        registerContentObserver();
        RedPackageAssistRUSManager.getInstance().init(this.mContext);
    }

    private void registerContentObserver() {
        if (this.mContext == null) {
            Log.w(TAG, "color navigation bar util isn't init.");
            return;
        }
        Uri uriNav = Uri.parse("content://com.android.systemui/navigationbar");
        this.mContext.getContentResolver().registerContentObserver(uriNav, true, new NavBarContentObserver());
        Uri uriStatus = Uri.parse("content://com.android.systemui/statusbar");
        this.mContext.getContentResolver().registerContentObserver(uriStatus, true, new StatusBarContentObserver());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAppNavBarDefaultList() {
        synchronized (mObject) {
            mDefaultAdapationApps.clear();
            mDefaultNotAdapationActivities.clear();
            int size = mDefaultAdaptationAppNames.length;
            for (int i = 0; i < size; i++) {
                addAdaptationApp(mDefaultAdaptationAppNames[i], mDefaultAppColors[i]);
            }
            int size2 = mDefaultNotAdaptationActivityNames.length;
            for (int i2 = 0; i2 < size2; i2++) {
                addNotAdaptationActivity(mDefaultNotAdaptationActivityNames[i2], mDefaultNotAdaptationActivityColors[i2]);
            }
            this.mUseDefualtData = true;
        }
    }

    private void addAdaptationApp(String pkg, int color) {
        addAdaptationApp(pkg, color, false);
    }

    private void addAdaptationApp(String pkg, int color, boolean palette) {
        addAdaptationApp(pkg, color, palette, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addAdaptationApp(String pkg, int color, boolean palette, Map activityColorList) {
        AdaptationAppInfo appInfo = new AdaptationAppInfo();
        appInfo.mPkg = pkg;
        appInfo.mDefaultColor = color;
        appInfo.mIsNeedPalette = palette;
        appInfo.mActivityColorList = activityColorList;
        mDefaultAdapationApps.add(appInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addStatusAdaptationApp(String pkg, int color, Map activityColorList) {
        AdaptationAppInfo appInfo = new AdaptationAppInfo();
        appInfo.mPkg = pkg;
        appInfo.mDefaultColor = color;
        appInfo.mActivityColorList = activityColorList;
        mStatusDefaultAdapationApps.add(appInfo);
    }

    private void addNotAdaptationActivity(String activityName, int color) {
        AdaptationActivityInfo activityInfo = new AdaptationActivityInfo();
        activityInfo.mActivityName = activityName;
        activityInfo.mDefaultColor = color;
        mDefaultNotAdapationActivities.add(activityInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNavBgColorListFromDB() {
        if (!this.mHasInitialized) {
            Log.w(TAG, "color navigation bar util isn't init.");
        } else {
            Thread thread = new Thread() { // from class: com.oplus.util.OplusNavigationBarUtil.1
                /* JADX WARN: Removed duplicated region for block: B:51:0x0116 A[ORIG_RETURN, RETURN] */
                @Override // java.lang.Thread, java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void run() {
                    Cursor cursor = null;
                    Map<String, String> map = null;
                    Uri uri = Uri.parse("content://com.android.systemui/navigationbar");
                    try {
                        try {
                            cursor = OplusNavigationBarUtil.this.mContext.getContentResolver().query(uri, null, null, null, null);
                        } catch (Exception e) {
                            OplusNavigationBarUtil.this.updateAppNavBarDefaultList();
                            Log.w(OplusNavigationBarUtil.TAG, "query error! list size " + OplusNavigationBarUtil.mDefaultAdapationApps.size() + " e:" + e);
                            if (0 == 0) {
                                return;
                            }
                        }
                        if (cursor != null && cursor.getCount() != 0) {
                            synchronized (OplusNavigationBarUtil.mObject) {
                                OplusNavigationBarUtil.mDefaultAdapationApps.clear();
                                while (true) {
                                    if (!cursor.moveToNext()) {
                                        break;
                                    }
                                    String pkg = cursor.getString(cursor.getColumnIndex("pkg"));
                                    String defColor = cursor.getString(cursor.getColumnIndex(OplusNavigationBarUtil.DEFAULT_COLOR));
                                    if (defColor == null || defColor.equals("")) {
                                        defColor = "0";
                                    }
                                    int defaultColor = OplusNavigationBarUtil.this.stringColorToIntColor(defColor);
                                    boolean palette = 1 == cursor.getInt(cursor.getColumnIndex(OplusNavigationBarUtil.IS_NEED_PALETTE));
                                    String activity = cursor.getString(cursor.getColumnIndex("activityName"));
                                    String activityColor = cursor.getString(cursor.getColumnIndex(OplusNavigationBarUtil.ACTIVITY_COLOR));
                                    if (activity != null && activityColor != null && !activity.equals("") && !activityColor.equals("")) {
                                        map = new HashMap<>();
                                        String[] actList = activity.split(",");
                                        String[] actcolorList = activityColor.split(",");
                                        for (int i = 0; actList.length > i && actcolorList.length > i; i++) {
                                            map.put(actList[i], actcolorList[i]);
                                        }
                                    }
                                    OplusNavigationBarUtil.this.addAdaptationApp(pkg, defaultColor, palette, map);
                                }
                                OplusNavigationBarUtil.this.mUseDefualtData = false;
                                OplusNavigationBarUtil.this.mReadNavData = true;
                            }
                            if (cursor == null) {
                                return;
                            }
                            cursor.close();
                        }
                        Log.w(OplusNavigationBarUtil.TAG, "cursor is null or count is 0.");
                        if (cursor == null) {
                        }
                        cursor.close();
                    } catch (Throwable th) {
                        if (0 != 0) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
            };
            thread.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStatusBgColorListFromDB() {
        if (!this.mHasInitialized) {
            Log.w(TAG, "color navigation bar util isn't init.");
        } else {
            Thread thread = new Thread() { // from class: com.oplus.util.OplusNavigationBarUtil.2
                /* JADX WARN: Removed duplicated region for block: B:43:0x00f0 A[ORIG_RETURN, RETURN] */
                @Override // java.lang.Thread, java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void run() {
                    Cursor cursor = null;
                    Map<String, String> map = null;
                    Uri uri = Uri.parse("content://com.android.systemui/statusbar");
                    try {
                        try {
                            cursor = OplusNavigationBarUtil.this.mContext.getContentResolver().query(uri, null, null, null, null);
                        } catch (Exception e) {
                            OplusNavigationBarUtil.mStatusDefaultAdapationApps.clear();
                            Log.w(OplusNavigationBarUtil.TAG, "updateStatusBgColorListFromDB query error:" + e);
                            if (0 == 0) {
                                return;
                            }
                        }
                        if (cursor != null && cursor.getCount() != 0) {
                            synchronized (OplusNavigationBarUtil.mObject) {
                                OplusNavigationBarUtil.mStatusDefaultAdapationApps.clear();
                                while (cursor.moveToNext()) {
                                    String pkg = cursor.getString(cursor.getColumnIndex("pkg"));
                                    String defColor = cursor.getString(cursor.getColumnIndex(OplusNavigationBarUtil.DEFAULT_COLOR));
                                    if (defColor == null || defColor.equals("")) {
                                        defColor = "0";
                                    }
                                    int defaultColor = OplusNavigationBarUtil.this.stringColorToIntColor(defColor);
                                    String activity = cursor.getString(cursor.getColumnIndex("activityName"));
                                    String activityColor = cursor.getString(cursor.getColumnIndex(OplusNavigationBarUtil.ACTIVITY_COLOR));
                                    if (!activity.equals("") && !activityColor.equals("")) {
                                        map = new HashMap<>();
                                        String[] actList = activity.split(",");
                                        String[] actcolorList = activityColor.split(",");
                                        for (int i = 0; actList.length > i && actcolorList.length > i; i++) {
                                            map.put(actList[i], actcolorList[i]);
                                        }
                                    }
                                    OplusNavigationBarUtil.this.addStatusAdaptationApp(pkg, defaultColor, map);
                                }
                                OplusNavigationBarUtil.this.mReadStatusData = true;
                            }
                            if (cursor == null) {
                                return;
                            }
                            cursor.close();
                        }
                        Log.w(OplusNavigationBarUtil.TAG, "updateStatusBgColorListFromDB cursor is null or count is 0.");
                        if (cursor == null) {
                        }
                        cursor.close();
                    } catch (Throwable th) {
                        if (0 != 0) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
            };
            thread.start();
        }
    }

    public boolean isHasInitialized() {
        return this.mHasInitialized;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int stringColorToIntColor(String color) {
        int length = color.length();
        if (length < 6) {
            Slog.e(TAG, "Color String Error! colorString:" + color);
            return 0;
        }
        String alpha = color.substring(0, length - 6);
        String colorString = color.substring(length - 6, length);
        if (alpha.equals("")) {
            alpha = "ff";
        }
        if (colorString.equals("")) {
            return 0;
        }
        return Integer.valueOf(colorString, 16).intValue() | (Integer.valueOf(alpha, 16).intValue() << 24);
    }

    /* loaded from: classes.dex */
    public class NavBarContentObserver extends ContentObserver {
        public NavBarContentObserver() {
            super(new Handler());
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            OplusNavigationBarUtil.this.updateNavBgColorListFromDB();
        }
    }

    /* loaded from: classes.dex */
    public class StatusBarContentObserver extends ContentObserver {
        public StatusBarContentObserver() {
            super(new Handler());
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            OplusNavigationBarUtil.this.updateStatusBgColorListFromDB();
        }
    }

    public boolean isActivityNeedPalette(String pkg, String activityName) {
        if (!this.mHasInitialized) {
            Log.w(TAG, "color navigation bar util isn't init.");
            return false;
        }
        if (!this.mReadNavData && this.mUpdateNavCount < 20) {
            updateNavBgColorListFromDB();
            this.mUpdateNavCount++;
            Slog.d(TAG, "isActivityNeedPalette mUpdateNavCount:" + this.mUpdateNavCount);
        }
        synchronized (mObject) {
            int size = mDefaultAdapationApps.size();
            for (int i = 0; i < size; i++) {
                AdaptationAppInfo appInfo = mDefaultAdapationApps.get(i);
                if (appInfo.mActivityColorList != null) {
                    for (Map.Entry<String, String> entry : appInfo.mActivityColorList.entrySet()) {
                        if (entry.getKey().equals(activityName)) {
                            return false;
                        }
                    }
                }
                if (appInfo.mPkg.equals(pkg)) {
                    return appInfo.mIsNeedPalette;
                }
            }
            return false;
        }
    }

    public int getNavBarColorFromAdaptation(String pkg, String activityName) {
        if (!this.mHasInitialized) {
            Log.w(TAG, "color navigation bar util isn't init.");
            return 0;
        }
        if (!this.mReadNavData && this.mUpdateNavCount < 20) {
            updateNavBgColorListFromDB();
            this.mUpdateNavCount++;
            Slog.d(TAG, "getNavBarOplusFromAdaptation mUpdateNavCount:" + this.mUpdateNavCount);
        }
        synchronized (mObject) {
            if (this.mUseDefualtData) {
                int size = mDefaultNotAdapationActivities.size();
                for (int i = 0; i < size; i++) {
                    AdaptationActivityInfo activityInfo = mDefaultNotAdapationActivities.get(i);
                    if (activityInfo.mActivityName.equals(activityName)) {
                        Slog.d(TAG, "the defualt activity:" + activityName + " color: " + Integer.toHexString(activityInfo.mDefaultColor));
                        return activityInfo.mDefaultColor;
                    }
                }
            }
            int size2 = mDefaultAdapationApps.size();
            for (int i2 = 0; i2 < size2; i2++) {
                AdaptationAppInfo appInfo = mDefaultAdapationApps.get(i2);
                if (appInfo.mPkg.equals(pkg)) {
                    if (appInfo.mActivityColorList != null) {
                        for (Map.Entry<String, String> entry : appInfo.mActivityColorList.entrySet()) {
                            if (entry.getValue() != null && !entry.getValue().equals("")) {
                                if (entry.getKey().equals(activityName)) {
                                    return Integer.valueOf(entry.getValue(), 16).intValue() | (-16777216);
                                }
                            }
                            return 0;
                        }
                    }
                    return appInfo.mDefaultColor;
                }
            }
            return 0;
        }
    }

    public int getStatusBarColorFromAdaptation(String pkg, String activityName) {
        if (!this.mHasInitialized) {
            Log.w(TAG, "color navigation bar util isn't init.");
            return 0;
        }
        if (!this.mReadStatusData && this.mUpdateStaCount < 20) {
            updateStatusBgColorListFromDB();
            this.mUpdateStaCount++;
        }
        synchronized (mObject) {
            int size = mStatusDefaultAdapationApps.size();
            for (int i = 0; i < size; i++) {
                AdaptationAppInfo appInfo = mStatusDefaultAdapationApps.get(i);
                if (appInfo.mPkg.equals(pkg)) {
                    if (appInfo.mActivityColorList != null && !appInfo.mActivityColorList.equals("")) {
                        for (Map.Entry<String, String> entry : appInfo.mActivityColorList.entrySet()) {
                            if (entry.getKey().equals(activityName)) {
                                return stringColorToIntColor(entry.getValue());
                            }
                        }
                    }
                    return appInfo.mDefaultColor;
                }
            }
            return 0;
        }
    }

    public int getImeBgColorFromAdaptation(String pkg) {
        return this.mContext.getColor(201719829);
    }

    public void setImePackageInGestureMode(boolean isImeInGestureMode) {
        Log.i(TAG, "setImePackageInGestureMode isImeInGestureMode:" + isImeInGestureMode);
    }

    public static String getVersion(Context context, String pkgName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(pkgName, 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            Log.e(TAG, "GetVersion failed! e:" + e);
            return null;
        }
    }

    public static boolean compareVersion(String versionA, String versionB) {
        Log.i(TAG, "A:" + versionA + " B:" + versionB);
        if (versionA == null || versionA.equals("") || versionB == null || versionB.equals("")) {
            return false;
        }
        if (versionA.equals(versionB)) {
            return true;
        }
        String[] arrayA = versionA.split("\\.");
        String[] arrayB = versionB.split("\\.");
        int length = arrayA.length < arrayB.length ? arrayA.length : arrayB.length;
        for (int i = 0; i < length; i++) {
            if (Integer.parseInt(arrayB[i]) > Integer.parseInt(arrayA[i])) {
                Log.i(TAG, "B:" + Integer.parseInt(arrayB[i]) + " > A:" + Integer.parseInt(arrayA[i]));
                return true;
            }
            if (Integer.parseInt(arrayB[i]) < Integer.parseInt(arrayA[i])) {
                Log.i(TAG, "B:" + Integer.parseInt(arrayB[i]) + " < A:" + Integer.parseInt(arrayA[i]));
                return false;
            }
        }
        return false;
    }
}
