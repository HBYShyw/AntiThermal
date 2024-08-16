package android.service.wallpaper;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/* loaded from: classes.dex */
public class OplusWallpaperLogUtils {
    private static final String TAG = "OplusWallpaperLogUtils";
    private static final Uri LOG_URI = Settings.System.getUriFor("log_switch_type");
    public static boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static boolean sLogSwitchContentObserverEnable = false;

    /* loaded from: classes.dex */
    public static class WallpaperLogSwitchObserver extends ContentObserver {
        public WallpaperLogSwitchObserver() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            OplusWallpaperLogUtils.updateLogState();
        }
    }

    public static void registerLogSwitchObserver(Context context, ContentObserver observer) {
        Log.d(TAG, "registerLogSwitchObserver mLogSwitchContentObserverEnable = " + sLogSwitchContentObserverEnable);
        if (context != null && !sLogSwitchContentObserverEnable) {
            Context appContext = context.getApplicationContext();
            appContext.getContentResolver().registerContentObserver(LOG_URI, false, observer);
            updateLogState();
            sLogSwitchContentObserverEnable = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateLogState() {
        boolean on = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        Log.d(TAG, "updateLogState on = " + on);
        DEBUG = on;
        WallpaperService.DEBUG = on;
        setDynamicalLogValue("android.app.WallpaperManager", "DEBUG", on);
    }

    private static void setDynamicalLogValue(String className, String fieldName, boolean on) {
        try {
            Field field = Class.forName(className).getDeclaredField(fieldName);
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                field.setBoolean(null, on);
            }
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "class not found");
        } catch (IllegalAccessException e2) {
            Log.i(TAG, "set value fail");
        } catch (NoSuchFieldException e3) {
            Log.i(TAG, "no such field");
        }
    }
}
