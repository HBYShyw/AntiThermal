package android.service.wallpaper;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.provider.Settings;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class OplusWallpaperServiceHelper {
    private static final int DARK_MODE_STATE_DEFAULT = 0;
    private static final int DARK_MODE_STATE_OPEN = 1;
    private static final String DARK_WALLPAPER_SETTINGS_STATE = "oplus_customize_settings_dark_wallpaper";
    private static final int NIGHT_MODE = 32;
    private static final int NIGHT_MODE_FLAG = 48;
    private static final String TAG = "OplusWallpaperServiceHelper";
    private static boolean sNightMode;
    private final ArrayList<WallpaperService.Engine> mActiveEngines = new ArrayList<>();
    private DarkWallpaperSettingsChangeObserver mDarkWallpaperSettingsChangeObserver;
    private HandlerThread mHandlerThread;
    private boolean mIsRegisterDarkWallpaperContentObserver;

    public OplusWallpaperServiceHelper() {
        HandlerThread handlerThread = new HandlerThread("wallpaper-helper");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
    }

    /* loaded from: classes.dex */
    private class DarkWallpaperSettingsChangeObserver extends ContentObserver {
        public DarkWallpaperSettingsChangeObserver() {
            super(new Handler());
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(OplusWallpaperServiceHelper.TAG, "DarkWallpaperSettingsChangeObserver onChange selfChange = " + selfChange);
            OplusWallpaperServiceHelper.this.updateEngineSurface();
        }
    }

    public void registerSetingsContentObserver(Context context) {
        Log.d(TAG, "registerSetingsContentObserver mIsRegisterDarkWallpaperContentObserver = " + this.mIsRegisterDarkWallpaperContentObserver);
        if (context != null) {
            final Context appContext = context.getApplicationContext();
            this.mHandlerThread.getThreadHandler().post(new Runnable() { // from class: android.service.wallpaper.OplusWallpaperServiceHelper.1
                @Override // java.lang.Runnable
                public void run() {
                    OplusWallpaperServiceHelper.this.mDarkWallpaperSettingsChangeObserver = new DarkWallpaperSettingsChangeObserver();
                    appContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(OplusWallpaperServiceHelper.DARK_WALLPAPER_SETTINGS_STATE), true, OplusWallpaperServiceHelper.this.mDarkWallpaperSettingsChangeObserver);
                    OplusWallpaperServiceHelper.this.mIsRegisterDarkWallpaperContentObserver = true;
                }
            });
        }
    }

    public void unregisterSettingsContentObserver(Context context) {
        Log.d(TAG, "unregisterSettingsContentObserver mIsRegisterDarkWallpaperContentObserver = " + this.mIsRegisterDarkWallpaperContentObserver);
        if (context != null) {
            final Context appContext = context.getApplicationContext();
            this.mHandlerThread.getThreadHandler().post(new Runnable() { // from class: android.service.wallpaper.OplusWallpaperServiceHelper.2
                @Override // java.lang.Runnable
                public void run() {
                    if (OplusWallpaperServiceHelper.this.mIsRegisterDarkWallpaperContentObserver) {
                        appContext.getContentResolver().unregisterContentObserver(OplusWallpaperServiceHelper.this.mDarkWallpaperSettingsChangeObserver);
                        OplusWallpaperServiceHelper.this.mIsRegisterDarkWallpaperContentObserver = false;
                    }
                }
            });
        }
    }

    public void addEngine(WallpaperService.Engine engine) {
        Log.d(TAG, "addEngine engine= " + engine);
        this.mActiveEngines.add(engine);
    }

    public void removeEngine(WallpaperService.Engine engine) {
        Log.d(TAG, "removeEngine engine= " + engine);
        this.mActiveEngines.remove(engine);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEngineSurface() {
        Log.d(TAG, "updateEngineSurface mActiveEngine size is: " + this.mActiveEngines.size());
        for (int i = 0; i < this.mActiveEngines.size(); i++) {
            WallpaperService.Engine engine = this.mActiveEngines.get(i);
            Log.d(TAG, "updateEngineSurface engine: " + engine);
            engine.updateSurface(true, false, true);
        }
    }

    public static float getDarkModeWallpaperWindowAlpha(Context context) {
        Trace.traceBegin(8L, "OplusWallpaperServiceHelper.getDarkModeWallpaperWindowAlpha");
        boolean nightMode = isNightMode(context) && isDarkModeWallpaperSwitchOpened(context);
        Log.i(TAG, "getDarkModeWallpaperWindowAlpha. nightMode = " + nightMode);
        float alpha = 0.76f;
        if (!nightMode) {
            alpha = 0.98f;
        }
        Trace.traceEnd(8L);
        return alpha;
    }

    private static boolean isNightMode(Context context) {
        Configuration configuration;
        if (context != null) {
            Resources res = context.getResources();
            if (res != null && (configuration = context.getResources().getConfiguration()) != null) {
                int currentNightMode = configuration.uiMode & 48;
                Log.d(TAG, "isNightMode is " + (32 == currentNightMode));
                return 32 == currentNightMode;
            }
        }
        return false;
    }

    public static float getDarkModeAlpha(Context context, WallpaperService.Engine engine) {
        Trace.traceBegin(8L, "OplusWallpaperServiceHelper.getDarkModeAlpha");
        boolean nightMode = isNightModeByEngine(engine) && isDarkModeWallpaperSwitchOpened(context);
        Log.i(TAG, "getDarkModeAlpha. nightMode = " + nightMode);
        float alpha = 0.76f;
        if (!nightMode) {
            alpha = 0.98f;
        }
        Trace.traceEnd(8L);
        return alpha;
    }

    private static boolean isNightModeByEngine(WallpaperService.Engine engine) {
        Configuration configuration;
        if (engine == null || (configuration = engine.mMergedConfiguration.getMergedConfiguration()) == null) {
            return false;
        }
        int currentNightMode = configuration.uiMode & 48;
        Log.d(TAG, "isNightModeByEngine is " + (32 == currentNightMode));
        return 32 == currentNightMode;
    }

    private static boolean isDarkModeWallpaperSwitchOpened(Context context) {
        return getDarkWallpaperSwitchSettings(context);
    }

    private static boolean getDarkWallpaperSwitchSettings(Context context) {
        return context != null && Settings.Secure.getInt(context.getContentResolver(), DARK_WALLPAPER_SETTINGS_STATE, 0) == 1;
    }
}
