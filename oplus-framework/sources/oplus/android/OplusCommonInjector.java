package oplus.android;

import android.app.Activity;
import android.app.Application;
import android.app.IOplusCommonInjector;
import android.app.OplusThemeHelper;
import android.common.OplusFeatureCache;
import android.content.pm.PackageParser;
import android.content.res.Configuration;
import android.content.res.IOplusThemeManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import com.oplus.font.IOplusFontManager;
import com.oplus.internal.R;

/* loaded from: classes.dex */
public class OplusCommonInjector implements IOplusCommonInjector {
    private static volatile OplusCommonInjector sInstance = null;

    public static OplusCommonInjector getInstance() {
        if (sInstance == null) {
            synchronized (OplusCommonInjector.class) {
                if (sInstance == null) {
                    sInstance = new OplusCommonInjector();
                }
            }
        }
        return sInstance;
    }

    OplusCommonInjector() {
    }

    @Override // android.app.IOplusCommonInjector
    public void onCreateForActivity(Activity activity, Bundle savedInstanceState) {
    }

    @Override // android.app.IOplusCommonInjector
    public void onConfigurationChangedForApplication(Application application, Configuration newConfig) {
        ((IOplusFontManager) OplusFeatureCache.getOrCreate(IOplusFontManager.DEFAULT, new Object[0])).updateLanguageLocale(newConfig);
    }

    @Override // android.app.IOplusCommonInjector
    public void onCreateForApplication(Application application) {
        if (application != null) {
            ((IOplusFontManager) OplusFeatureCache.getOrCreate(IOplusFontManager.DEFAULT, new Object[0])).setCurrentAppName(application.getPackageName());
            ((IOplusFontManager) OplusFeatureCache.getOrCreate(IOplusFontManager.DEFAULT, new Object[0])).initVariationFontVariable(application);
        }
    }

    @Override // android.app.IOplusCommonInjector
    public void applyConfigurationToResourcesForResourcesManager(Configuration config, int changes) {
        OplusThemeHelper.getInstance().handleExtraConfigurationChanges(changes);
        ((IOplusFontManager) OplusFeatureCache.getOrCreate(IOplusFontManager.DEFAULT, new Object[0])).updateTypefaceInCurrProcess(config, changes);
        ((IOplusThemeManager) OplusFeatureCache.getOrCreate(IOplusThemeManager.DEFAULT, new Object[0])).updateExtraConfigForUxIcon(changes);
    }

    @Override // android.app.IOplusCommonInjector
    public void hookPreloadResources(Resources mResources, String tag) {
        long startTime = SystemClock.uptimeMillis();
        TypedArray ar = mResources.obtainTypedArray(201785378);
        int N = preloadDrawables(mResources, ar, tag);
        ar.recycle();
        Log.i(tag, "...preloaded " + N + " ROM drawable resources in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
        long startTime2 = SystemClock.uptimeMillis();
        TypedArray ar2 = mResources.obtainTypedArray(201785379);
        int N2 = preloadOplusStateLists(mResources, ar2, tag);
        ar2.recycle();
        Log.i(tag, "...preloaded " + N2 + " ROM color resources in " + (SystemClock.uptimeMillis() - startTime2) + "ms.");
    }

    @Override // android.app.IOplusCommonInjector
    public void hookActivityAliasTheme(PackageParser.Activity a, Resources res, XmlResourceParser parser, PackageParser.Activity target) {
        TypedArray sb = res.obtainAttributes(parser, R.styleable.AndroidManifestActivityAlias);
        a.info.theme = sb.getResourceId(0, target.info.theme);
        sb.recycle();
    }

    private int preloadDrawables(Resources mResources, TypedArray ar, String TAG) {
        int N = ar.length();
        for (int i = 0; i < N; i++) {
            int id = ar.getResourceId(i, 0);
            if (id != 0 && mResources.getDrawable(id, null) == null) {
                throw new IllegalArgumentException("Unable to find preloaded drawable resource #0x" + Integer.toHexString(id) + " (" + ar.getString(i) + ")");
            }
        }
        return N;
    }

    private int preloadOplusStateLists(Resources mResources, TypedArray ar, String TAG) {
        int N = ar.length();
        for (int i = 0; i < N; i++) {
            int id = ar.getResourceId(i, 0);
            if (id != 0 && mResources.getColorStateList(id, null) == null) {
                throw new IllegalArgumentException("Unable to find preloaded color resource #0x" + Integer.toHexString(id) + " (" + ar.getString(i) + ")");
            }
        }
        return N;
    }
}
