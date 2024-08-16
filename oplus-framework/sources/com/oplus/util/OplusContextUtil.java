package com.oplus.util;

import android.app.Activity;
import android.common.OplusFeatureCache;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.os.UserHandle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import com.oplus.internal.R;
import com.oplus.theme.IOplusThemeStyle;
import com.oplus.theme.OplusThemeStyle;

/* loaded from: classes.dex */
public class OplusContextUtil {
    private static final String METADATA_STYLE_VALUE = "true";
    private static final String TAG = "OplusContextUtil";
    private static boolean sIsOplusStyleActivity;
    private static boolean sIsOplusStyleApplication;
    private static boolean sIsOplusStyleApplicationAssigned;
    private static int sLastActivityHash;
    private final boolean mIsOplusOSStyle;
    private final boolean mIsOplusStyle;

    public OplusContextUtil(Context context) {
        boolean isOplusStyle = isOplusStyle(context);
        this.mIsOplusStyle = isOplusStyle;
        this.mIsOplusOSStyle = isOplusOSStyle(context, isOplusStyle);
    }

    public boolean isOplusStyle() {
        return this.mIsOplusStyle;
    }

    public boolean isOplusOSStyle() {
        return this.mIsOplusOSStyle;
    }

    public static boolean isOplusStyle(Context context) {
        if (context == null) {
            return false;
        }
        TypedArray a = context.getTheme().obtainStyledAttributes(R.styleable.OplusTheme);
        boolean isOplusTheme = a.getBoolean(0, false);
        a.recycle();
        return isOplusTheme;
    }

    public static boolean isOplusAlertDialogBuilderStyle(Context context) {
        if (context == null) {
            return false;
        }
        TypedArray a = context.getTheme().obtainStyledAttributes(R.styleable.OplusTheme);
        boolean isOplusAlertDialogBuilderStyle = a.getBoolean(1, false);
        a.recycle();
        return isOplusAlertDialogBuilderStyle;
    }

    public static Context getOplusThemeContext(Context context) {
        return isOplusStyle(context) ? context : new ContextThemeWrapper(context, ((IOplusThemeStyle) OplusFeatureCache.getOrCreate(IOplusThemeStyle.DEFAULT, new Object[0])).getSystemThemeStyle(OplusThemeStyle.DEFAULT_SYSTEM_THEME));
    }

    public static int getResId(Context context, int id) {
        TypedValue value = new TypedValue();
        context.getResources().getValue(id, value, true);
        return value.resourceId;
    }

    public static boolean isOplusOSStyle(Context context, boolean isOplusStyle) {
        Context ctx;
        if (isOplusStyle) {
            return true;
        }
        if (context == null) {
            return false;
        }
        boolean isOplusOSStyle = isOplusStyleInApplication(context);
        if (isOplusOSStyle) {
            return true;
        }
        while ((context instanceof ContextWrapper) && !(context instanceof Activity) && context != (ctx = ((ContextWrapper) context).getBaseContext())) {
            try {
                context = ctx;
            } catch (Exception e) {
                OplusLog.e(TAG, e.toString());
            }
        }
        if (context instanceof Activity) {
            return isOplusStyleInActivity(context);
        }
        return isOplusOSStyle;
    }

    private static boolean isOplusStyleInActivity(Context context) {
        try {
        } catch (Exception e) {
            OplusLog.e(TAG, e.toString());
        }
        if (sLastActivityHash == context.hashCode()) {
            return sIsOplusStyleActivity;
        }
        ActivityInfo info = context.getPackageManager().getActivityInfo(((Activity) context).getComponentName(), 128);
        if (info.metaData != null && (METADATA_STYLE_VALUE.equals(info.metaData.getString(((IOplusThemeStyle) OplusFeatureCache.getOrCreate(IOplusThemeStyle.DEFAULT, new Object[0])).getMetaDataStyleTitle(true))) || METADATA_STYLE_VALUE.equals(info.metaData.getString(((IOplusThemeStyle) OplusFeatureCache.getOrCreate(IOplusThemeStyle.DEFAULT, new Object[0])).getMetaDataStyleTitle(false))))) {
            sIsOplusStyleActivity = true;
            sLastActivityHash = context.hashCode();
            return true;
        }
        sIsOplusStyleActivity = false;
        sLastActivityHash = context.hashCode();
        return false;
    }

    private static boolean isOplusStyleInApplication(Context context) {
        try {
        } catch (AbstractMethodError e) {
            OplusLog.e(TAG, "AbstraceMethod not implemented by App : " + e);
        } catch (Exception e2) {
            OplusLog.e(TAG, e2.toString());
        }
        if (sIsOplusStyleApplicationAssigned) {
            return sIsOplusStyleApplication;
        }
        ApplicationInfo appInfo = context.getPackageManager().getApplicationInfoAsUser(context.getPackageName(), 128, UserHandle.getCallingUserId());
        if (appInfo != null && appInfo.metaData != null && (METADATA_STYLE_VALUE.equals(appInfo.metaData.getString(((IOplusThemeStyle) OplusFeatureCache.getOrCreate(IOplusThemeStyle.DEFAULT, new Object[0])).getMetaDataStyleTitle(true))) || METADATA_STYLE_VALUE.equals(appInfo.metaData.getString(((IOplusThemeStyle) OplusFeatureCache.getOrCreate(IOplusThemeStyle.DEFAULT, new Object[0])).getMetaDataStyleTitle(false))))) {
            sIsOplusStyleApplication = true;
            sIsOplusStyleApplicationAssigned = true;
            return true;
        }
        sIsOplusStyleApplication = false;
        sIsOplusStyleApplicationAssigned = true;
        return false;
    }

    public static boolean isOplusOSStyle(Context context) {
        return isOplusOSStyle(context, isOplusStyle(context));
    }

    public static TypedArray getWindowStyle(Context context) {
        return context.obtainStyledAttributes(android.R.styleable.Window);
    }

    public static Activity getActivityContext(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            ContextWrapper wrapper = (ContextWrapper) context;
            Context base = wrapper.getBaseContext();
            if (base != context) {
                context = base;
            } else {
                return null;
            }
        }
        return null;
    }

    public static String getActivityContextName(Context context) {
        Activity activity = getActivityContext(context);
        if (activity == null) {
            return null;
        }
        return activity.getClass().getName();
    }

    public static int getAttrColor(Context context, int attr) {
        int[] colorAttr = {attr};
        TypedArray a = context.getTheme().obtainStyledAttributes(colorAttr);
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }
}
