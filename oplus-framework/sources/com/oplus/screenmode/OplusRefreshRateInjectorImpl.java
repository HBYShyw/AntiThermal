package com.oplus.screenmode;

import android.content.Context;
import android.os.IBinder;
import android.os.SystemProperties;
import android.os.Trace;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.view.RootViewSurfaceTaker;
import java.util.Arrays;

/* loaded from: classes.dex */
public class OplusRefreshRateInjectorImpl extends OplusRefreshRateInjector {
    private static final float FACTOR_FILTER = 0.6f;
    private static final int REFRESH_RATE_60 = 2;
    private int mOverrideRefreshRateId = 0;
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static ArraySet<String> sDisableOverrideViewList = null;

    OplusRefreshRateInjectorImpl() {
    }

    OplusRefreshRateInjectorImpl(Context context) {
        if (sDisableOverrideViewList == null) {
            sDisableOverrideViewList = getDisableOverrideViewList(context);
        }
    }

    private static ArraySet<String> parseOverrideViewList(String viewList) {
        try {
            if (!TextUtils.isEmpty(viewList)) {
                ArraySet<String> result = new ArraySet<>();
                String[] strs = viewList.split(",");
                result.addAll(Arrays.asList(strs));
                return result;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static ArraySet<String> getDisableOverrideViewList(Context context) {
        return null;
    }

    @Override // com.oplus.screenmode.OplusRefreshRateInjector
    public void setRefreshRateIfNeed(Context context, ViewGroup viewGroup, IBinder token) {
        int rateId = 0;
        Trace.traceBegin(8L, "setRefreshRateIfNeed");
        try {
            if (viewGroup instanceof RootViewSurfaceTaker) {
                rateId = ((RootViewSurfaceTaker) viewGroup).willYouTakeTheSurface() != null ? 2 : 0;
            }
            if (rateId == 0) {
                boolean isLandscape = context.getResources().getConfiguration().orientation == 2;
                int threshold = getAreaThreshold(viewGroup.getWidth(), viewGroup.getHeight(), isLandscape);
                rateId = getRefreshRateId(viewGroup, threshold);
            }
            if (this.mOverrideRefreshRateId != rateId) {
                OplusDisplayModeManager.getInstance().overrideWindowRefreshRate(token, rateId);
                this.mOverrideRefreshRateId = rateId;
            }
        } catch (Exception e) {
        } catch (Throwable th) {
            Trace.traceEnd(8L);
            throw th;
        }
        Trace.traceEnd(8L);
    }

    private int getRefreshRateId(ViewGroup viewGroup, int threshold) {
        int rateId;
        boolean needCheck = viewGroup.getVisibility() == 0 && viewGroup.getWidth() * viewGroup.getHeight() >= threshold;
        if (needCheck) {
            int rateId2 = getRefreshRateIdFromView(viewGroup, threshold);
            if (rateId2 != 0) {
                return rateId2;
            }
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof ViewGroup) {
                    rateId = getRefreshRateId((ViewGroup) child, threshold);
                } else {
                    rateId = getRefreshRateIdFromView(child, threshold);
                }
                if (rateId != 0) {
                    return rateId;
                }
            }
        }
        return 0;
    }

    private boolean surfaceViewAvailable(View view) {
        SurfaceView surfaceV;
        if ((view instanceof SurfaceView) && (surfaceV = (SurfaceView) view) != null && !surfaceV.isZOrderedOnTop()) {
            return true;
        }
        return false;
    }

    private int getRefreshRateIdFromView(View view, int threshold) {
        boolean useLowRate = view.getVisibility() == 0 && (surfaceViewAvailable(view) || (view instanceof TextureView)) && view.getWidth() * view.getHeight() >= threshold && !disableViewOverride(view.getClass().getSimpleName());
        if (useLowRate && DEBUG) {
            Log.v("RefreshRateInjector", view + " request low refresh rate");
        }
        return useLowRate ? 2 : 0;
    }

    private int getAreaThreshold(int windowW, int windowH, boolean isLandscape) {
        if (isLandscape) {
            int shortSide = Math.min(windowW, windowH);
            return shortSide * shortSide;
        }
        return (int) (windowW * windowH * 0.6f);
    }

    private boolean disableViewOverride(String key) {
        ArraySet<String> arraySet = sDisableOverrideViewList;
        return arraySet != null && (arraySet.contains("All") || sDisableOverrideViewList.contains(key));
    }

    public static void enterPSMode(boolean enter) {
        OplusDisplayModeManager.getInstance().enterPSMode(enter);
    }

    public static void enterPSModeOnRate(boolean enter, int rate) {
        OplusDisplayModeManager.getInstance().enterPSModeOnRate(enter, rate);
    }

    @Override // com.oplus.screenmode.OplusRefreshRateInjector
    public boolean setHighTemperatureStatus(int status, int rate) {
        return OplusDisplayModeManager.getInstance().setHighTemperatureStatus(status, rate);
    }
}
