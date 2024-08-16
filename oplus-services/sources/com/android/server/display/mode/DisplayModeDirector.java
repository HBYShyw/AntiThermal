package com.android.server.display.mode;

import android.R;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.BrightnessInfo;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManagerInternal;
import android.hardware.fingerprint.IUdfpsRefreshRateRequestCallback;
import android.net.Uri;
import android.os.Handler;
import android.os.IThermalEventListener;
import android.os.IThermalService;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.provider.DeviceConfig;
import android.provider.DeviceConfigInterface;
import android.provider.Settings;
import android.sysprop.SurfaceFlingerProperties;
import android.util.IndentingPrintWriter;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.SurfaceControl;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.display.BrightnessSynchronizer;
import com.android.internal.os.BackgroundThread;
import com.android.server.LocalServices;
import com.android.server.display.DisplayDeviceConfig;
import com.android.server.display.IDisplayModeDirectorExt;
import com.android.server.display.mode.DisplayModeDirector;
import com.android.server.display.mode.VotesStorage;
import com.android.server.display.utils.AmbientFilter;
import com.android.server.display.utils.AmbientFilterFactory;
import com.android.server.display.utils.SensorUtils;
import com.android.server.sensors.SensorManagerInternal;
import com.android.server.statusbar.StatusBarManagerInternal;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Callable;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DisplayModeDirector {
    private static final float FLOAT_TOLERANCE = 0.01f;
    private static final int MSG_DEFAULT_PEAK_REFRESH_RATE_CHANGED = 3;
    private static final int MSG_HIGH_BRIGHTNESS_THRESHOLDS_CHANGED = 6;
    private static final int MSG_LOW_BRIGHTNESS_THRESHOLDS_CHANGED = 2;
    private static final int MSG_REFRESH_RATE_IN_HBM_HDR_CHANGED = 8;
    private static final int MSG_REFRESH_RATE_IN_HBM_SUNLIGHT_CHANGED = 7;
    private static final int MSG_REFRESH_RATE_IN_HIGH_ZONE_CHANGED = 5;
    private static final int MSG_REFRESH_RATE_IN_LOW_ZONE_CHANGED = 4;
    private static final int MSG_REFRESH_RATE_RANGE_CHANGED = 1;
    private static final String TAG = "DisplayModeDirector";
    public static IDisplayModeDirectorExt mDmdExt;
    private boolean mAlwaysRespectAppRequest;
    private final AppRequestObserver mAppRequestObserver;
    private BrightnessObserver mBrightnessObserver;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private DisplayDeviceConfig mDefaultDisplayDeviceConfig;
    private SparseArray<Display.Mode> mDefaultModeByDisplay;
    private DesiredDisplayModeSpecsListener mDesiredDisplayModeSpecsListener;
    private final DeviceConfigInterface mDeviceConfig;
    private final DeviceConfigDisplaySettings mDeviceConfigDisplaySettings;
    private final DisplayObserver mDisplayObserver;
    private final DisplayModeDirectorHandler mHandler;
    private final HbmObserver mHbmObserver;
    private final Injector mInjector;
    private final Object mLock;
    private boolean mLoggingEnabled;
    private int mModeSwitchingType;
    private final SensorObserver mSensorObserver;
    private final SettingsObserver mSettingsObserver;
    private final SkinThermalStatusObserver mSkinThermalStatusObserver;
    private SparseArray<Display.Mode[]> mSupportedModesByDisplay;
    private final boolean mSupportsFrameRateOverride;
    private final UdfpsObserver mUdfpsObserver;
    private final VotesStorage mVotesStorage;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface DesiredDisplayModeSpecsListener {
        void onDesiredDisplayModeSpecsChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Injector {
        public static final Uri PEAK_REFRESH_RATE_URI = Settings.System.getUriFor("peak_refresh_rate");

        BrightnessInfo getBrightnessInfo(int i);

        DeviceConfigInterface getDeviceConfig();

        Display getDisplay(int i);

        boolean getDisplayInfo(int i, DisplayInfo displayInfo);

        Display[] getDisplays();

        boolean isDozeState(Display display);

        void registerDisplayListener(DisplayManager.DisplayListener displayListener, Handler handler);

        void registerDisplayListener(DisplayManager.DisplayListener displayListener, Handler handler, long j);

        void registerPeakRefreshRateObserver(ContentResolver contentResolver, ContentObserver contentObserver);

        boolean registerThermalServiceListener(IThermalEventListener iThermalEventListener);

        boolean supportsFrameRateOverride();
    }

    private boolean equalsWithinFloatTolerance(float f, float f2) {
        return f >= f2 - 0.01f && f <= f2 + 0.01f;
    }

    public DisplayModeDirector(Context context, Handler handler) {
        this(context, handler, new RealInjector(context));
    }

    public DisplayModeDirector(Context context, Handler handler, Injector injector) {
        this.mLock = new Object();
        this.mModeSwitchingType = 1;
        this.mContext = context;
        this.mHandler = new DisplayModeDirectorHandler(handler.getLooper());
        this.mInjector = injector;
        this.mSupportedModesByDisplay = new SparseArray<>();
        this.mDefaultModeByDisplay = new SparseArray<>();
        this.mAppRequestObserver = new AppRequestObserver();
        this.mDeviceConfig = injector.getDeviceConfig();
        DeviceConfigDisplaySettings deviceConfigDisplaySettings = new DeviceConfigDisplaySettings();
        this.mDeviceConfigDisplaySettings = deviceConfigDisplaySettings;
        this.mSettingsObserver = new SettingsObserver(context, handler);
        this.mBrightnessObserver = new BrightnessObserver(context, handler, injector);
        this.mDefaultDisplayDeviceConfig = null;
        this.mUdfpsObserver = new UdfpsObserver();
        VotesStorage votesStorage = new VotesStorage(new VotesStorage.Listener() { // from class: com.android.server.display.mode.DisplayModeDirector$$ExternalSyntheticLambda1
            @Override // com.android.server.display.mode.VotesStorage.Listener
            public final void onChanged() {
                DisplayModeDirector.this.lambda$start$0();
            }
        });
        this.mVotesStorage = votesStorage;
        this.mDisplayObserver = new DisplayObserver(context, handler, votesStorage);
        this.mSensorObserver = new SensorObserver(context, votesStorage, injector);
        this.mSkinThermalStatusObserver = new SkinThermalStatusObserver(injector, votesStorage);
        this.mHbmObserver = new HbmObserver(injector, votesStorage, BackgroundThread.getHandler(), deviceConfigDisplaySettings);
        this.mAlwaysRespectAppRequest = false;
        this.mSupportsFrameRateOverride = injector.supportsFrameRateOverride();
        mDmdExt = (IDisplayModeDirectorExt) ExtLoader.type(IDisplayModeDirectorExt.class).create();
    }

    public void start(SensorManager sensorManager) {
        this.mSettingsObserver.observe();
        this.mDisplayObserver.observe();
        this.mBrightnessObserver.observe(sensorManager);
        this.mSensorObserver.observe();
        this.mHbmObserver.observe();
        this.mSkinThermalStatusObserver.observe();
        synchronized (this.mLock) {
            lambda$start$0();
        }
        mDmdExt.registerResolutionChangeListener(new Runnable() { // from class: com.android.server.display.mode.DisplayModeDirector$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DisplayModeDirector.this.lambda$start$0();
            }
        });
    }

    public void onBootCompleted() {
        this.mUdfpsObserver.observe();
    }

    public void setLoggingEnabled(boolean z) {
        if (this.mLoggingEnabled == z) {
            return;
        }
        this.mLoggingEnabled = z;
        this.mBrightnessObserver.setLoggingEnabled(z);
        this.mSkinThermalStatusObserver.setLoggingEnabled(z);
        this.mVotesStorage.setLoggingEnabled(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class VoteSummary {
        public float appRequestBaseModeRefreshRate;
        public boolean disableRefreshRateSwitching;
        public int height;
        public float maxPhysicalRefreshRate;
        public float maxRenderFrameRate;
        public float minPhysicalRefreshRate;
        public float minRenderFrameRate;
        public int width;

        VoteSummary() {
            reset();
        }

        public void reset() {
            this.minPhysicalRefreshRate = 0.0f;
            this.maxPhysicalRefreshRate = Float.POSITIVE_INFINITY;
            this.minRenderFrameRate = 0.0f;
            this.maxRenderFrameRate = Float.POSITIVE_INFINITY;
            this.width = -1;
            this.height = -1;
            this.disableRefreshRateSwitching = false;
            this.appRequestBaseModeRefreshRate = 0.0f;
        }

        public String toString() {
            return "minPhysicalRefreshRate=" + this.minPhysicalRefreshRate + ", maxPhysicalRefreshRate=" + this.maxPhysicalRefreshRate + ", minRenderFrameRate=" + this.minRenderFrameRate + ", maxRenderFrameRate=" + this.maxRenderFrameRate + ", width=" + this.width + ", height=" + this.height + ", disableRefreshRateSwitching=" + this.disableRefreshRateSwitching + ", appRequestBaseModeRefreshRate=" + this.appRequestBaseModeRefreshRate;
        }
    }

    private void summarizeVotes(SparseArray<Vote> sparseArray, int i, int i2, VoteSummary voteSummary) {
        int i3;
        int i4;
        voteSummary.reset();
        while (i2 >= i) {
            Vote vote = sparseArray.get(i2);
            if (vote != null) {
                SurfaceControl.RefreshRateRanges refreshRateRanges = vote.refreshRateRanges;
                voteSummary.minPhysicalRefreshRate = Math.max(voteSummary.minPhysicalRefreshRate, Math.max(refreshRateRanges.physical.min, refreshRateRanges.render.min));
                voteSummary.maxPhysicalRefreshRate = Math.min(voteSummary.maxPhysicalRefreshRate, vote.refreshRateRanges.physical.max);
                SurfaceControl.RefreshRateRanges refreshRateRanges2 = vote.refreshRateRanges;
                float min = Math.min(refreshRateRanges2.render.max, refreshRateRanges2.physical.max);
                voteSummary.minRenderFrameRate = Math.max(voteSummary.minRenderFrameRate, vote.refreshRateRanges.render.min);
                voteSummary.maxRenderFrameRate = Math.min(voteSummary.maxRenderFrameRate, min);
                if (voteSummary.height == -1 && voteSummary.width == -1 && (i3 = vote.height) > 0 && (i4 = vote.width) > 0) {
                    voteSummary.width = i4;
                    voteSummary.height = i3;
                }
                if (!voteSummary.disableRefreshRateSwitching && vote.disableRefreshRateSwitching) {
                    voteSummary.disableRefreshRateSwitching = true;
                }
                if (voteSummary.appRequestBaseModeRefreshRate == 0.0f) {
                    float f = vote.appRequestBaseModeRefreshRate;
                    if (f > 0.0f) {
                        voteSummary.appRequestBaseModeRefreshRate = f;
                    }
                }
                if (this.mLoggingEnabled) {
                    Slog.w(TAG, "Vote summary for priority " + Vote.priorityToString(i2) + ": " + voteSummary);
                }
            }
            i2--;
        }
    }

    private Display.Mode selectBaseMode(VoteSummary voteSummary, ArrayList<Display.Mode> arrayList, Display.Mode mode) {
        float f = voteSummary.appRequestBaseModeRefreshRate;
        if (f <= 0.0f) {
            f = mode.getRefreshRate();
        }
        Iterator<Display.Mode> it = arrayList.iterator();
        while (it.hasNext()) {
            Display.Mode next = it.next();
            if (equalsWithinFloatTolerance(f, next.getRefreshRate())) {
                return next;
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList.get(0);
    }

    private void disableModeSwitching(VoteSummary voteSummary, float f) {
        voteSummary.maxPhysicalRefreshRate = f;
        voteSummary.minPhysicalRefreshRate = f;
        voteSummary.maxRenderFrameRate = Math.min(voteSummary.maxRenderFrameRate, f);
        if (this.mLoggingEnabled) {
            Slog.i(TAG, "Disabled mode switching on summary: " + voteSummary);
        }
    }

    private void disableRenderRateSwitching(VoteSummary voteSummary, float f) {
        voteSummary.minRenderFrameRate = voteSummary.maxRenderFrameRate;
        if (!isRenderRateAchievable(f, voteSummary)) {
            voteSummary.maxRenderFrameRate = f;
            voteSummary.minRenderFrameRate = f;
        }
        if (this.mLoggingEnabled) {
            Slog.i(TAG, "Disabled render rate switching on summary: " + voteSummary);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0280  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x026e A[Catch: all -> 0x02dc, TryCatch #0 {, blocks: (B:4:0x0003, B:8:0x001f, B:13:0x003b, B:15:0x0043, B:18:0x0055, B:19:0x0069, B:28:0x0073, B:30:0x0077, B:21:0x00ea, B:23:0x00ee, B:25:0x0158, B:64:0x0047, B:31:0x015c, B:33:0x0160, B:34:0x019b, B:36:0x01cf, B:37:0x020a, B:39:0x0210, B:40:0x0251, B:43:0x0253, B:50:0x0261, B:52:0x027b, B:55:0x0281, B:56:0x02b8, B:58:0x0265, B:60:0x026e, B:62:0x0278, B:67:0x02ba, B:68:0x02da), top: B:3:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public DesiredDisplayModeSpecs getDesiredDisplayModeSpecs(int i) {
        int i2;
        int i3;
        boolean z;
        synchronized (this.mLock) {
            SparseArray<Vote> votes = this.mVotesStorage.getVotes(i);
            Display.Mode[] modeArr = this.mSupportedModesByDisplay.get(i);
            Display.Mode mode = this.mDefaultModeByDisplay.get(i);
            if (modeArr != null && mode != null) {
                ArrayList<Display.Mode> arrayList = new ArrayList<>();
                arrayList.add(mode);
                VoteSummary voteSummary = new VoteSummary();
                if (this.mAlwaysRespectAppRequest) {
                    i2 = 6;
                    i3 = 4;
                } else {
                    i2 = 14;
                    i3 = 0;
                }
                while (true) {
                    if (i3 > i2) {
                        break;
                    }
                    summarizeVotes(votes, i3, i2, voteSummary);
                    if (voteSummary.height == -1 || voteSummary.width == -1) {
                        voteSummary.width = mode.getPhysicalWidth();
                        voteSummary.height = mode.getPhysicalHeight();
                    }
                    if (i == 0) {
                        voteSummary.width = mDmdExt.getWidth(voteSummary.width);
                        voteSummary.height = mDmdExt.getHeight(voteSummary.height);
                    }
                    arrayList = filterModes(modeArr, voteSummary);
                    if (!arrayList.isEmpty()) {
                        if (this.mLoggingEnabled) {
                            Slog.w(TAG, "Found available modes=" + arrayList + " with lowest priority considered " + Vote.priorityToString(i3) + " and constraints: width=" + voteSummary.width + ", height=" + voteSummary.height + ", minPhysicalRefreshRate=" + voteSummary.minPhysicalRefreshRate + ", maxPhysicalRefreshRate=" + voteSummary.maxPhysicalRefreshRate + ", minRenderFrameRate=" + voteSummary.minRenderFrameRate + ", maxRenderFrameRate=" + voteSummary.maxRenderFrameRate + ", disableRefreshRateSwitching=" + voteSummary.disableRefreshRateSwitching + ", appRequestBaseModeRefreshRate=" + voteSummary.appRequestBaseModeRefreshRate);
                        }
                    } else {
                        if (this.mLoggingEnabled) {
                            Slog.w(TAG, "Couldn't find available modes with lowest priority set to " + Vote.priorityToString(i3) + " and with the following constraints: width=" + voteSummary.width + ", height=" + voteSummary.height + ", minPhysicalRefreshRate=" + voteSummary.minPhysicalRefreshRate + ", maxPhysicalRefreshRate=" + voteSummary.maxPhysicalRefreshRate + ", minRenderFrameRate=" + voteSummary.minRenderFrameRate + ", maxRenderFrameRate=" + voteSummary.maxRenderFrameRate + ", disableRefreshRateSwitching=" + voteSummary.disableRefreshRateSwitching + ", appRequestBaseModeRefreshRate=" + voteSummary.appRequestBaseModeRefreshRate);
                        }
                        i3++;
                    }
                }
                if (this.mLoggingEnabled) {
                    Slog.i(TAG, "Primary physical range: [" + voteSummary.minPhysicalRefreshRate + " " + voteSummary.maxPhysicalRefreshRate + "] render frame rate range: [" + voteSummary.minRenderFrameRate + " " + voteSummary.maxRenderFrameRate + "]");
                }
                VoteSummary voteSummary2 = new VoteSummary();
                summarizeVotes(votes, 4, 14, voteSummary2);
                voteSummary2.minPhysicalRefreshRate = Math.min(voteSummary2.minPhysicalRefreshRate, voteSummary.minPhysicalRefreshRate);
                voteSummary2.maxPhysicalRefreshRate = Math.max(voteSummary2.maxPhysicalRefreshRate, voteSummary.maxPhysicalRefreshRate);
                voteSummary2.minRenderFrameRate = Math.min(voteSummary2.minRenderFrameRate, voteSummary.minRenderFrameRate);
                voteSummary2.maxRenderFrameRate = Math.max(voteSummary2.maxRenderFrameRate, voteSummary.maxRenderFrameRate);
                if (this.mLoggingEnabled) {
                    Slog.i(TAG, "App request range: [" + voteSummary2.minPhysicalRefreshRate + " " + voteSummary2.maxPhysicalRefreshRate + "] Frame rate range: [" + voteSummary2.minRenderFrameRate + " " + voteSummary2.maxRenderFrameRate + "]");
                }
                Display.Mode selectBaseMode = selectBaseMode(voteSummary, arrayList, mode);
                if (selectBaseMode == null) {
                    Slog.w(TAG, "Can't find a set of allowed modes which satisfies the votes. Falling back to the default mode. Display = " + i + ", votes = " + votes + ", supported modes = " + Arrays.toString(modeArr));
                    float refreshRate = mode.getRefreshRate();
                    SurfaceControl.RefreshRateRange refreshRateRange = new SurfaceControl.RefreshRateRange(refreshRate, refreshRate);
                    SurfaceControl.RefreshRateRanges refreshRateRanges = new SurfaceControl.RefreshRateRanges(refreshRateRange, refreshRateRange);
                    return new DesiredDisplayModeSpecs(mode.getModeId(), false, refreshRateRanges, refreshRateRanges);
                }
                int i4 = this.mModeSwitchingType;
                if (i4 != 0 && i4 != 3) {
                    z = false;
                    if (!z || voteSummary.disableRefreshRateSwitching) {
                        float refreshRate2 = selectBaseMode.getRefreshRate();
                        disableModeSwitching(voteSummary, refreshRate2);
                        if (z) {
                            disableModeSwitching(voteSummary2, refreshRate2);
                            disableRenderRateSwitching(voteSummary, refreshRate2);
                            if (this.mModeSwitchingType == 0) {
                                disableRenderRateSwitching(voteSummary2, refreshRate2);
                            }
                        }
                    }
                    return new DesiredDisplayModeSpecs(selectBaseMode.getModeId(), this.mModeSwitchingType == 2, new SurfaceControl.RefreshRateRanges(new SurfaceControl.RefreshRateRange(voteSummary.minPhysicalRefreshRate, voteSummary.maxPhysicalRefreshRate), new SurfaceControl.RefreshRateRange(voteSummary.minRenderFrameRate, voteSummary.maxRenderFrameRate)), new SurfaceControl.RefreshRateRanges(new SurfaceControl.RefreshRateRange(voteSummary2.minPhysicalRefreshRate, voteSummary2.maxPhysicalRefreshRate), new SurfaceControl.RefreshRateRange(voteSummary2.minRenderFrameRate, voteSummary2.maxRenderFrameRate)));
                }
                z = true;
                if (!z) {
                }
                float refreshRate22 = selectBaseMode.getRefreshRate();
                disableModeSwitching(voteSummary, refreshRate22);
                if (z) {
                }
                return new DesiredDisplayModeSpecs(selectBaseMode.getModeId(), this.mModeSwitchingType == 2, new SurfaceControl.RefreshRateRanges(new SurfaceControl.RefreshRateRange(voteSummary.minPhysicalRefreshRate, voteSummary.maxPhysicalRefreshRate), new SurfaceControl.RefreshRateRange(voteSummary.minRenderFrameRate, voteSummary.maxRenderFrameRate)), new SurfaceControl.RefreshRateRanges(new SurfaceControl.RefreshRateRange(voteSummary2.minPhysicalRefreshRate, voteSummary2.maxPhysicalRefreshRate), new SurfaceControl.RefreshRateRange(voteSummary2.minRenderFrameRate, voteSummary2.maxRenderFrameRate)));
            }
            Slog.e(TAG, "Asked about unknown display, returning empty display mode specs!(id=" + i + ")");
            return new DesiredDisplayModeSpecs();
        }
    }

    private boolean isRenderRateAchievable(float f, VoteSummary voteSummary) {
        return f / ((float) ((int) Math.ceil((double) ((f / voteSummary.maxRenderFrameRate) - 0.01f)))) >= voteSummary.minRenderFrameRate - 0.01f;
    }

    private ArrayList<Display.Mode> filterModes(Display.Mode[] modeArr, VoteSummary voteSummary) {
        if (voteSummary.minRenderFrameRate > voteSummary.maxRenderFrameRate + 0.01f) {
            if (this.mLoggingEnabled) {
                Slog.w(TAG, "Vote summary resulted in empty set (invalid frame rate range): minRenderFrameRate=" + voteSummary.minRenderFrameRate + ", maxRenderFrameRate=" + voteSummary.maxRenderFrameRate);
            }
            return new ArrayList<>();
        }
        ArrayList<Display.Mode> arrayList = new ArrayList<>();
        boolean z = voteSummary.appRequestBaseModeRefreshRate > 0.0f;
        for (Display.Mode mode : modeArr) {
            if (mode.getPhysicalWidth() != voteSummary.width || mode.getPhysicalHeight() != voteSummary.height) {
                if (this.mLoggingEnabled) {
                    Slog.w(TAG, "Discarding mode " + mode.getModeId() + ", wrong size: desiredWidth=" + voteSummary.width + ": desiredHeight=" + voteSummary.height + ": actualWidth=" + mode.getPhysicalWidth() + ": actualHeight=" + mode.getPhysicalHeight());
                }
            } else {
                float refreshRate = mode.getRefreshRate();
                if (refreshRate < voteSummary.minPhysicalRefreshRate - 0.01f || refreshRate > voteSummary.maxPhysicalRefreshRate + 0.01f) {
                    if (this.mLoggingEnabled) {
                        Slog.w(TAG, "Discarding mode " + mode.getModeId() + ", outside refresh rate bounds: minPhysicalRefreshRate=" + voteSummary.minPhysicalRefreshRate + ", maxPhysicalRefreshRate=" + voteSummary.maxPhysicalRefreshRate + ", modeRefreshRate=" + refreshRate);
                    }
                } else if (!this.mSupportsFrameRateOverride && (refreshRate < voteSummary.minRenderFrameRate - 0.01f || refreshRate > voteSummary.maxRenderFrameRate + 0.01f)) {
                    if (this.mLoggingEnabled) {
                        Slog.w(TAG, "Discarding mode " + mode.getModeId() + ", outside render rate bounds: minPhysicalRefreshRate=" + voteSummary.minPhysicalRefreshRate + ", maxPhysicalRefreshRate=" + voteSummary.maxPhysicalRefreshRate + ", modeRefreshRate=" + refreshRate);
                    }
                } else if (!isRenderRateAchievable(refreshRate, voteSummary)) {
                    if (this.mLoggingEnabled) {
                        Slog.w(TAG, "Discarding mode " + mode.getModeId() + ", outside frame rate bounds: minRenderFrameRate=" + voteSummary.minRenderFrameRate + ", maxRenderFrameRate=" + voteSummary.maxRenderFrameRate + ", modePhysicalRefreshRate=" + refreshRate);
                    }
                } else {
                    arrayList.add(mode);
                    if (equalsWithinFloatTolerance(mode.getRefreshRate(), voteSummary.appRequestBaseModeRefreshRate)) {
                        z = false;
                    }
                }
            }
        }
        return z ? new ArrayList<>() : arrayList;
    }

    public AppRequestObserver getAppRequestObserver() {
        return this.mAppRequestObserver;
    }

    public void setDesiredDisplayModeSpecsListener(DesiredDisplayModeSpecsListener desiredDisplayModeSpecsListener) {
        synchronized (this.mLock) {
            this.mDesiredDisplayModeSpecsListener = desiredDisplayModeSpecsListener;
        }
    }

    public void defaultDisplayDeviceUpdated(DisplayDeviceConfig displayDeviceConfig) {
        synchronized (this.mLock) {
            this.mDefaultDisplayDeviceConfig = displayDeviceConfig;
            this.mSettingsObserver.setRefreshRates(displayDeviceConfig, true);
            this.mBrightnessObserver.updateBlockingZoneThresholds(displayDeviceConfig, true);
            this.mBrightnessObserver.reloadLightSensor(displayDeviceConfig);
            this.mHbmObserver.setupHdrRefreshRates(displayDeviceConfig);
        }
    }

    public void setShouldAlwaysRespectAppRequestedMode(boolean z) {
        synchronized (this.mLock) {
            if (this.mAlwaysRespectAppRequest != z) {
                this.mAlwaysRespectAppRequest = z;
                lambda$start$0();
            }
        }
    }

    public boolean shouldAlwaysRespectAppRequestedMode() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mAlwaysRespectAppRequest;
        }
        return z;
    }

    public void setModeSwitchingType(int i) {
        synchronized (this.mLock) {
            if (i != this.mModeSwitchingType) {
                this.mModeSwitchingType = i;
                lambda$start$0();
            }
        }
    }

    public int getModeSwitchingType() {
        int i;
        synchronized (this.mLock) {
            i = this.mModeSwitchingType;
        }
        return i;
    }

    @VisibleForTesting
    Vote getVote(int i, int i2) {
        return this.mVotesStorage.getVotes(i).get(i2);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(TAG);
        synchronized (this.mLock) {
            printWriter.println("  mSupportedModesByDisplay:");
            for (int i = 0; i < this.mSupportedModesByDisplay.size(); i++) {
                printWriter.println("    " + this.mSupportedModesByDisplay.keyAt(i) + " -> " + Arrays.toString(this.mSupportedModesByDisplay.valueAt(i)));
            }
            printWriter.println("  mDefaultModeByDisplay:");
            for (int i2 = 0; i2 < this.mDefaultModeByDisplay.size(); i2++) {
                printWriter.println("    " + this.mDefaultModeByDisplay.keyAt(i2) + " -> " + this.mDefaultModeByDisplay.valueAt(i2));
            }
            printWriter.println("  mModeSwitchingType: " + switchingTypeToString(this.mModeSwitchingType));
            printWriter.println("  mAlwaysRespectAppRequest: " + this.mAlwaysRespectAppRequest);
            this.mSettingsObserver.dumpLocked(printWriter);
            this.mAppRequestObserver.dumpLocked(printWriter);
            this.mBrightnessObserver.dumpLocked(printWriter);
            this.mUdfpsObserver.dumpLocked(printWriter);
            this.mHbmObserver.dumpLocked(printWriter);
            this.mSkinThermalStatusObserver.dumpLocked(printWriter);
        }
        this.mVotesStorage.dump(printWriter);
        this.mSensorObserver.dump(printWriter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public float getMaxRefreshRateLocked(int i) {
        float f = 0.0f;
        for (Display.Mode mode : this.mSupportedModesByDisplay.get(i)) {
            if (mode.getRefreshRate() > f) {
                f = mode.getRefreshRate();
            }
        }
        return f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: notifyDesiredDisplayModeSpecsChangedLocked, reason: merged with bridge method [inline-methods] */
    public void lambda$start$0() {
        if (this.mDesiredDisplayModeSpecsListener == null || this.mHandler.hasMessages(1)) {
            return;
        }
        this.mHandler.obtainMessage(1, this.mDesiredDisplayModeSpecsListener).sendToTarget();
    }

    private static String switchingTypeToString(int i) {
        if (i == 0) {
            return "SWITCHING_TYPE_NONE";
        }
        if (i == 1) {
            return "SWITCHING_TYPE_WITHIN_GROUPS";
        }
        if (i == 2) {
            return "SWITCHING_TYPE_ACROSS_AND_WITHIN_GROUPS";
        }
        if (i == 3) {
            return "SWITCHING_TYPE_RENDER_FRAME_RATE_ONLY";
        }
        return "Unknown SwitchingType " + i;
    }

    @VisibleForTesting
    void injectSupportedModesByDisplay(SparseArray<Display.Mode[]> sparseArray) {
        this.mSupportedModesByDisplay = sparseArray;
    }

    @VisibleForTesting
    void injectDefaultModeByDisplay(SparseArray<Display.Mode> sparseArray) {
        this.mDefaultModeByDisplay = sparseArray;
    }

    @VisibleForTesting
    void injectVotesByDisplay(SparseArray<SparseArray<Vote>> sparseArray) {
        this.mVotesStorage.injectVotesByDisplay(sparseArray);
    }

    @VisibleForTesting
    void injectBrightnessObserver(BrightnessObserver brightnessObserver) {
        this.mBrightnessObserver = brightnessObserver;
    }

    @VisibleForTesting
    BrightnessObserver getBrightnessObserver() {
        return this.mBrightnessObserver;
    }

    @VisibleForTesting
    SettingsObserver getSettingsObserver() {
        return this.mSettingsObserver;
    }

    @VisibleForTesting
    UdfpsObserver getUdpfsObserver() {
        return this.mUdfpsObserver;
    }

    @VisibleForTesting
    HbmObserver getHbmObserver() {
        return this.mHbmObserver;
    }

    @VisibleForTesting
    DesiredDisplayModeSpecs getDesiredDisplayModeSpecsWithInjectedFpsSettings(float f, float f2, float f3) {
        DesiredDisplayModeSpecs desiredDisplayModeSpecs;
        synchronized (this.mLock) {
            this.mSettingsObserver.updateRefreshRateSettingLocked(f, f2, f3);
            desiredDisplayModeSpecs = getDesiredDisplayModeSpecs(0);
        }
        return desiredDisplayModeSpecs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DisplayModeDirectorHandler extends Handler {
        DisplayModeDirectorHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    ((DesiredDisplayModeSpecsListener) message.obj).onDesiredDisplayModeSpecsChanged();
                    return;
                case 2:
                    Pair pair = (Pair) message.obj;
                    DisplayModeDirector.this.mBrightnessObserver.onDeviceConfigLowBrightnessThresholdsChanged((int[]) pair.first, (int[]) pair.second);
                    return;
                case 3:
                    DisplayModeDirector.this.mSettingsObserver.onDeviceConfigDefaultPeakRefreshRateChanged((Float) message.obj);
                    return;
                case 4:
                    DisplayModeDirector.this.mBrightnessObserver.onDeviceConfigRefreshRateInLowZoneChanged(message.arg1);
                    return;
                case 5:
                    DisplayModeDirector.this.mBrightnessObserver.onDeviceConfigRefreshRateInHighZoneChanged(message.arg1);
                    return;
                case 6:
                    Pair pair2 = (Pair) message.obj;
                    DisplayModeDirector.this.mBrightnessObserver.onDeviceConfigHighBrightnessThresholdsChanged((int[]) pair2.first, (int[]) pair2.second);
                    return;
                case 7:
                    DisplayModeDirector.this.mHbmObserver.onDeviceConfigRefreshRateInHbmSunlightChanged(message.arg1);
                    return;
                case 8:
                    DisplayModeDirector.this.mHbmObserver.onDeviceConfigRefreshRateInHbmHdrChanged(message.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class DesiredDisplayModeSpecs {
        public boolean allowGroupSwitching;
        public final SurfaceControl.RefreshRateRanges appRequest;
        public int baseModeId;
        public final SurfaceControl.RefreshRateRanges primary;
        public int vrrPolicy;

        public DesiredDisplayModeSpecs() {
            this.primary = new SurfaceControl.RefreshRateRanges();
            this.appRequest = new SurfaceControl.RefreshRateRanges();
        }

        public DesiredDisplayModeSpecs(int i, boolean z, SurfaceControl.RefreshRateRanges refreshRateRanges, SurfaceControl.RefreshRateRanges refreshRateRanges2) {
            this.baseModeId = i;
            this.allowGroupSwitching = z;
            this.primary = refreshRateRanges;
            this.appRequest = refreshRateRanges2;
            this.vrrPolicy = DisplayModeDirector.mDmdExt.getVrrPolicy(refreshRateRanges.render.max);
        }

        public String toString() {
            return String.format("baseModeId=%d allowGroupSwitching=%b primary=%s appRequest=%s", Integer.valueOf(this.baseModeId), Boolean.valueOf(this.allowGroupSwitching), this.primary.toString(), this.appRequest.toString());
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof DesiredDisplayModeSpecs)) {
                return false;
            }
            DesiredDisplayModeSpecs desiredDisplayModeSpecs = (DesiredDisplayModeSpecs) obj;
            return this.baseModeId == desiredDisplayModeSpecs.baseModeId && this.allowGroupSwitching == desiredDisplayModeSpecs.allowGroupSwitching && this.primary.equals(desiredDisplayModeSpecs.primary) && this.appRequest.equals(desiredDisplayModeSpecs.appRequest) && this.vrrPolicy == desiredDisplayModeSpecs.vrrPolicy;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.baseModeId), Boolean.valueOf(this.allowGroupSwitching), this.primary, this.appRequest, Integer.valueOf(this.vrrPolicy));
        }

        public void copyFrom(DesiredDisplayModeSpecs desiredDisplayModeSpecs) {
            this.baseModeId = desiredDisplayModeSpecs.baseModeId;
            this.allowGroupSwitching = desiredDisplayModeSpecs.allowGroupSwitching;
            SurfaceControl.RefreshRateRanges refreshRateRanges = this.primary;
            SurfaceControl.RefreshRateRange refreshRateRange = refreshRateRanges.physical;
            SurfaceControl.RefreshRateRanges refreshRateRanges2 = desiredDisplayModeSpecs.primary;
            SurfaceControl.RefreshRateRange refreshRateRange2 = refreshRateRanges2.physical;
            refreshRateRange.min = refreshRateRange2.min;
            refreshRateRange.max = refreshRateRange2.max;
            SurfaceControl.RefreshRateRange refreshRateRange3 = refreshRateRanges.render;
            SurfaceControl.RefreshRateRange refreshRateRange4 = refreshRateRanges2.render;
            refreshRateRange3.min = refreshRateRange4.min;
            refreshRateRange3.max = refreshRateRange4.max;
            SurfaceControl.RefreshRateRanges refreshRateRanges3 = this.appRequest;
            SurfaceControl.RefreshRateRange refreshRateRange5 = refreshRateRanges3.physical;
            SurfaceControl.RefreshRateRanges refreshRateRanges4 = desiredDisplayModeSpecs.appRequest;
            SurfaceControl.RefreshRateRange refreshRateRange6 = refreshRateRanges4.physical;
            refreshRateRange5.min = refreshRateRange6.min;
            refreshRateRange5.max = refreshRateRange6.max;
            SurfaceControl.RefreshRateRange refreshRateRange7 = refreshRateRanges3.render;
            SurfaceControl.RefreshRateRange refreshRateRange8 = refreshRateRanges4.render;
            refreshRateRange7.min = refreshRateRange8.min;
            refreshRateRange7.max = refreshRateRange8.max;
            this.vrrPolicy = desiredDisplayModeSpecs.vrrPolicy;
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    final class SettingsObserver extends ContentObserver {
        private final Context mContext;
        private float mDefaultPeakRefreshRate;
        private float mDefaultRefreshRate;
        private final Uri mLowPowerModeSetting;
        private final Uri mMatchContentFrameRateSetting;
        private final Uri mMinRefreshRateSetting;
        private final Uri mPeakRefreshRateSetting;
        private final Uri mPolicyChanged;

        SettingsObserver(Context context, Handler handler) {
            super(handler);
            this.mPeakRefreshRateSetting = Settings.System.getUriFor("peak_refresh_rate");
            this.mMinRefreshRateSetting = Settings.System.getUriFor("min_refresh_rate");
            this.mLowPowerModeSetting = Settings.Global.getUriFor("low_power");
            this.mMatchContentFrameRateSetting = Settings.Secure.getUriFor("match_content_frame_rate");
            this.mPolicyChanged = Settings.System.getUriFor("adfr_policy_change");
            this.mContext = context;
            setRefreshRates(null, false);
        }

        public void setRefreshRates(DisplayDeviceConfig displayDeviceConfig, boolean z) {
            int defaultRefreshRate;
            setDefaultPeakRefreshRate(displayDeviceConfig, z);
            if (displayDeviceConfig == null) {
                defaultRefreshRate = this.mContext.getResources().getInteger(R.integer.config_drawLockTimeoutMillis);
            } else {
                defaultRefreshRate = displayDeviceConfig.getDefaultRefreshRate();
            }
            this.mDefaultRefreshRate = defaultRefreshRate;
        }

        public void observe() {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            DisplayModeDirector.this.mInjector.registerPeakRefreshRateObserver(contentResolver, this);
            contentResolver.registerContentObserver(this.mMinRefreshRateSetting, false, this, 0);
            contentResolver.registerContentObserver(this.mLowPowerModeSetting, false, this, 0);
            contentResolver.registerContentObserver(this.mMatchContentFrameRateSetting, false, this);
            if (DisplayModeDirector.mDmdExt.isAdfrEnabled()) {
                contentResolver.registerContentObserver(this.mPolicyChanged, false, this, 0);
            }
            Float defaultPeakRefreshRate = DisplayModeDirector.this.mDeviceConfigDisplaySettings.getDefaultPeakRefreshRate();
            if (defaultPeakRefreshRate != null) {
                this.mDefaultPeakRefreshRate = defaultPeakRefreshRate.floatValue();
            }
            synchronized (DisplayModeDirector.this.mLock) {
                updateRefreshRateSettingLocked();
                updateLowPowerModeSettingLocked();
                updateModeSwitchingTypeSettingLocked();
            }
        }

        public void setDefaultRefreshRate(float f) {
            synchronized (DisplayModeDirector.this.mLock) {
                this.mDefaultRefreshRate = f;
                updateRefreshRateSettingLocked();
            }
        }

        public void onDeviceConfigDefaultPeakRefreshRateChanged(Float f) {
            synchronized (DisplayModeDirector.this.mLock) {
                if (f == null) {
                    setDefaultPeakRefreshRate(DisplayModeDirector.this.mDefaultDisplayDeviceConfig, false);
                    updateRefreshRateSettingLocked();
                } else if (this.mDefaultPeakRefreshRate != f.floatValue()) {
                    this.mDefaultPeakRefreshRate = f.floatValue();
                    updateRefreshRateSettingLocked();
                }
            }
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri, int i) {
            synchronized (DisplayModeDirector.this.mLock) {
                if (!this.mPeakRefreshRateSetting.equals(uri) && !this.mMinRefreshRateSetting.equals(uri)) {
                    if (this.mLowPowerModeSetting.equals(uri)) {
                        updateLowPowerModeSettingLocked();
                    } else if (this.mMatchContentFrameRateSetting.equals(uri)) {
                        updateModeSwitchingTypeSettingLocked();
                    }
                    if (DisplayModeDirector.mDmdExt.isAdfrEnabled() && this.mPolicyChanged.equals(uri)) {
                        DisplayModeDirector.this.lambda$start$0();
                    }
                }
                updateRefreshRateSettingLocked();
                if (DisplayModeDirector.mDmdExt.isAdfrEnabled()) {
                    DisplayModeDirector.this.lambda$start$0();
                }
            }
        }

        @VisibleForTesting
        float getDefaultRefreshRate() {
            return this.mDefaultRefreshRate;
        }

        @VisibleForTesting
        float getDefaultPeakRefreshRate() {
            return this.mDefaultPeakRefreshRate;
        }

        /* JADX WARN: Removed duplicated region for block: B:4:0x0010  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void setDefaultPeakRefreshRate(DisplayDeviceConfig displayDeviceConfig, boolean z) {
            Float defaultPeakRefreshRate;
            int defaultPeakRefreshRate2;
            if (z) {
                try {
                    defaultPeakRefreshRate = DisplayModeDirector.this.mDeviceConfigDisplaySettings.getDefaultPeakRefreshRate();
                } catch (Exception unused) {
                }
                if (defaultPeakRefreshRate == null) {
                    if (displayDeviceConfig == null) {
                        defaultPeakRefreshRate2 = this.mContext.getResources().getInteger(R.integer.config_downloadDataDirSize);
                    } else {
                        defaultPeakRefreshRate2 = displayDeviceConfig.getDefaultPeakRefreshRate();
                    }
                    defaultPeakRefreshRate = Float.valueOf(defaultPeakRefreshRate2);
                }
                this.mDefaultPeakRefreshRate = defaultPeakRefreshRate.floatValue();
            }
            defaultPeakRefreshRate = null;
            if (defaultPeakRefreshRate == null) {
            }
            this.mDefaultPeakRefreshRate = defaultPeakRefreshRate.floatValue();
        }

        private void updateLowPowerModeSettingLocked() {
            boolean z = Settings.Global.getInt(this.mContext.getContentResolver(), "low_power", 0) != 0;
            if (z) {
                Slog.e(DisplayModeDirector.TAG, "DO NOT switch to 60hz when low power");
            }
            DisplayModeDirector.this.mVotesStorage.updateGlobalVote(10, null);
            DisplayModeDirector.this.mBrightnessObserver.onLowPowerModeEnabledLocked(z);
        }

        private void updateRefreshRateSettingLocked() {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            updateRefreshRateSettingLocked(Settings.System.getFloatForUser(contentResolver, "min_refresh_rate", 0.0f, contentResolver.getUserId()), Settings.System.getFloatForUser(contentResolver, "peak_refresh_rate", this.mDefaultPeakRefreshRate, contentResolver.getUserId()), this.mDefaultRefreshRate);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateRefreshRateSettingLocked(float f, float f2, float f3) {
            Vote forRenderFrameRates = f2 <= 0.0f ? null : Vote.forRenderFrameRates(0.0f, Math.max(f, f2));
            if (f < 0.0f) {
                f = 0.0f;
            }
            DisplayModeDirector.this.mVotesStorage.updateGlobalVote(7, forRenderFrameRates);
            DisplayModeDirector.this.mVotesStorage.updateGlobalVote(3, Vote.forRenderFrameRates(f, Float.POSITIVE_INFINITY));
            DisplayModeDirector.this.mVotesStorage.updateGlobalVote(0, f3 != 0.0f ? Vote.forRenderFrameRates(0.0f, f3) : null);
            if (f2 == 0.0f && f3 == 0.0f) {
                Slog.e(DisplayModeDirector.TAG, "Default and peak refresh rates are both 0. One of them should be set to a valid value.");
                f2 = f;
            } else if (f2 == 0.0f) {
                f2 = f3;
            } else if (f3 != 0.0f) {
                f2 = Math.min(f3, f2);
            }
            DisplayModeDirector.this.mBrightnessObserver.onRefreshRateSettingChangedLocked(f, f2);
        }

        private void updateModeSwitchingTypeSettingLocked() {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            int intForUser = Settings.Secure.getIntForUser(contentResolver, "match_content_frame_rate", DisplayModeDirector.this.mModeSwitchingType, contentResolver.getUserId());
            if (intForUser != DisplayModeDirector.this.mModeSwitchingType) {
                DisplayModeDirector.this.mModeSwitchingType = intForUser;
                DisplayModeDirector.this.lambda$start$0();
            }
        }

        public void dumpLocked(PrintWriter printWriter) {
            printWriter.println("  SettingsObserver");
            printWriter.println("    mDefaultRefreshRate: " + this.mDefaultRefreshRate);
            printWriter.println("    mDefaultPeakRefreshRate: " + this.mDefaultPeakRefreshRate);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class AppRequestObserver {
        private final SparseArray<Display.Mode> mAppRequestedModeByDisplay = new SparseArray<>();
        private final SparseArray<SurfaceControl.RefreshRateRange> mAppPreferredRefreshRateRangeByDisplay = new SparseArray<>();

        AppRequestObserver() {
        }

        public void setAppRequest(int i, int i2, float f, float f2) {
            synchronized (DisplayModeDirector.this.mLock) {
                setAppRequestedModeLocked(i, i2);
                setAppPreferredRefreshRateRangeLocked(i, f, f2);
            }
        }

        private void setAppRequestedModeLocked(int i, int i2) {
            Vote vote;
            Vote vote2;
            Display.Mode findModeByIdLocked = findModeByIdLocked(i, i2);
            if (Objects.equals(findModeByIdLocked, this.mAppRequestedModeByDisplay.get(i))) {
                return;
            }
            if (findModeByIdLocked != null) {
                this.mAppRequestedModeByDisplay.put(i, findModeByIdLocked);
                vote = Vote.forBaseModeRefreshRate(findModeByIdLocked.getRefreshRate());
                vote2 = Vote.forSize(findModeByIdLocked.getPhysicalWidth(), findModeByIdLocked.getPhysicalHeight());
            } else {
                this.mAppRequestedModeByDisplay.remove(i);
                vote = null;
                vote2 = null;
            }
            DisplayModeDirector.this.mVotesStorage.updateVote(i, 5, vote);
            DisplayModeDirector.this.mVotesStorage.updateVote(i, 6, vote2);
        }

        /* JADX WARN: Code restructure failed: missing block: B:21:0x0023, code lost:
        
            if (r1.max == 0.0f) goto L7;
         */
        /* JADX WARN: Removed duplicated region for block: B:10:0x0033  */
        /* JADX WARN: Removed duplicated region for block: B:8:0x0032 A[RETURN] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void setAppPreferredRefreshRateRangeLocked(int i, float f, float f2) {
            SurfaceControl.RefreshRateRange refreshRateRange;
            Vote vote = null;
            if (f > 0.0f || f2 > 0.0f) {
                if (f2 <= 0.0f) {
                    f2 = Float.POSITIVE_INFINITY;
                }
                refreshRateRange = new SurfaceControl.RefreshRateRange(f, f2);
                if (refreshRateRange.min == 0.0f) {
                }
                if (Objects.equals(refreshRateRange, this.mAppPreferredRefreshRateRangeByDisplay.get(i))) {
                    if (refreshRateRange != null) {
                        this.mAppPreferredRefreshRateRangeByDisplay.put(i, refreshRateRange);
                        vote = Vote.forRenderFrameRates(refreshRateRange.min, refreshRateRange.max);
                    } else {
                        this.mAppPreferredRefreshRateRangeByDisplay.remove(i);
                    }
                    DisplayModeDirector.this.mVotesStorage.updateVote(i, 4, vote);
                    return;
                }
                return;
            }
            refreshRateRange = null;
            if (Objects.equals(refreshRateRange, this.mAppPreferredRefreshRateRangeByDisplay.get(i))) {
            }
        }

        private Display.Mode findModeByIdLocked(int i, int i2) {
            Display.Mode[] modeArr = (Display.Mode[]) DisplayModeDirector.this.mSupportedModesByDisplay.get(i);
            if (modeArr == null) {
                return null;
            }
            for (Display.Mode mode : modeArr) {
                if (mode.getModeId() == i2) {
                    return mode;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dumpLocked(PrintWriter printWriter) {
            printWriter.println("  AppRequestObserver");
            printWriter.println("    mAppRequestedModeByDisplay:");
            for (int i = 0; i < this.mAppRequestedModeByDisplay.size(); i++) {
                printWriter.println("    " + this.mAppRequestedModeByDisplay.keyAt(i) + " -> " + this.mAppRequestedModeByDisplay.valueAt(i));
            }
            printWriter.println("    mAppPreferredRefreshRateRangeByDisplay:");
            for (int i2 = 0; i2 < this.mAppPreferredRefreshRateRangeByDisplay.size(); i2++) {
                printWriter.println("    " + this.mAppPreferredRefreshRateRangeByDisplay.keyAt(i2) + " -> " + this.mAppPreferredRefreshRateRangeByDisplay.valueAt(i2));
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class DisplayObserver implements DisplayManager.DisplayListener {
        private final Context mContext;
        private final Handler mHandler;
        private final VotesStorage mVotesStorage;

        DisplayObserver(Context context, Handler handler, VotesStorage votesStorage) {
            this.mContext = context;
            this.mHandler = handler;
            this.mVotesStorage = votesStorage;
        }

        public void observe() {
            DisplayModeDirector.this.mInjector.registerDisplayListener(this, this.mHandler);
            SparseArray sparseArray = new SparseArray();
            SparseArray sparseArray2 = new SparseArray();
            DisplayInfo displayInfo = new DisplayInfo();
            for (Display display : DisplayModeDirector.this.mInjector.getDisplays()) {
                int displayId = display.getDisplayId();
                display.getDisplayInfo(displayInfo);
                sparseArray.put(displayId, displayInfo.supportedModes);
                sparseArray2.put(displayId, displayInfo.getDefaultMode());
            }
            synchronized (DisplayModeDirector.this.mLock) {
                int size = sparseArray.size();
                for (int i = 0; i < size; i++) {
                    DisplayModeDirector.this.mSupportedModesByDisplay.put(sparseArray.keyAt(i), (Display.Mode[]) sparseArray.valueAt(i));
                    DisplayModeDirector.this.mDefaultModeByDisplay.put(sparseArray2.keyAt(i), (Display.Mode) sparseArray2.valueAt(i));
                }
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
            DisplayInfo displayInfo = getDisplayInfo(i);
            updateDisplayModes(i, displayInfo);
            updateLayoutLimitedFrameRate(i, displayInfo);
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i) {
            synchronized (DisplayModeDirector.this.mLock) {
                DisplayModeDirector.this.mSupportedModesByDisplay.remove(i);
                DisplayModeDirector.this.mDefaultModeByDisplay.remove(i);
            }
            updateLayoutLimitedFrameRate(i, null);
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
            DisplayInfo displayInfo = getDisplayInfo(i);
            updateDisplayModes(i, displayInfo);
            updateLayoutLimitedFrameRate(i, displayInfo);
        }

        private DisplayInfo getDisplayInfo(int i) {
            DisplayInfo displayInfo = new DisplayInfo();
            if (DisplayModeDirector.this.mInjector.getDisplayInfo(i, displayInfo)) {
                return displayInfo;
            }
            return null;
        }

        private void updateLayoutLimitedFrameRate(int i, DisplayInfo displayInfo) {
            SurfaceControl.RefreshRateRange refreshRateRange;
            this.mVotesStorage.updateVote(i, 9, (displayInfo == null || (refreshRateRange = displayInfo.layoutLimitedRefreshRate) == null) ? null : Vote.forPhysicalRefreshRates(refreshRateRange.min, refreshRateRange.max));
        }

        private void updateDisplayModes(int i, DisplayInfo displayInfo) {
            boolean z;
            if (displayInfo == null) {
                return;
            }
            synchronized (DisplayModeDirector.this.mLock) {
                boolean z2 = true;
                if (Arrays.equals((Object[]) DisplayModeDirector.this.mSupportedModesByDisplay.get(i), displayInfo.supportedModes)) {
                    z = false;
                } else {
                    DisplayModeDirector.this.mSupportedModesByDisplay.put(i, displayInfo.supportedModes);
                    z = true;
                }
                if (Objects.equals(DisplayModeDirector.this.mDefaultModeByDisplay.get(i), displayInfo.getDefaultMode())) {
                    z2 = z;
                } else {
                    DisplayModeDirector.this.mDefaultModeByDisplay.put(i, displayInfo.getDefaultMode());
                }
                if (z2) {
                    DisplayModeDirector.this.lambda$start$0();
                }
            }
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class BrightnessObserver implements DisplayManager.DisplayListener {
        private static final int LIGHT_SENSOR_RATE_MS = 250;
        private AmbientFilter mAmbientFilter;
        private final Context mContext;
        private final Handler mHandler;
        private int[] mHighAmbientBrightnessThresholds;
        private int[] mHighDisplayBrightnessThresholds;
        private final Injector mInjector;
        private Sensor mLightSensor;
        private String mLightSensorName;
        private String mLightSensorType;
        private boolean mLoggingEnabled;
        private int[] mLowAmbientBrightnessThresholds;
        private int[] mLowDisplayBrightnessThresholds;
        private int mRefreshRateInHighZone;
        private int mRefreshRateInLowZone;
        private Sensor mRegisteredLightSensor;
        private SensorManager mSensorManager;
        private boolean mShouldObserveAmbientHighChange;
        private boolean mShouldObserveAmbientLowChange;
        private boolean mShouldObserveDisplayHighChange;
        private boolean mShouldObserveDisplayLowChange;
        private final LightSensorEventListener mLightSensorListener = new LightSensorEventListener();
        private float mAmbientLux = -1.0f;
        private int mBrightness = -1;
        private int mDefaultDisplayState = 0;
        private boolean mRefreshRateChangeable = false;
        private boolean mLowPowerModeEnabled = false;

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i) {
        }

        BrightnessObserver(Context context, Handler handler, Injector injector) {
            this.mContext = context;
            this.mHandler = handler;
            this.mInjector = injector;
            updateBlockingZoneThresholds(null, false);
            this.mRefreshRateInHighZone = context.getResources().getInteger(R.integer.config_navBarInteractionMode);
        }

        public void updateBlockingZoneThresholds(DisplayDeviceConfig displayDeviceConfig, boolean z) {
            loadLowBrightnessThresholds(displayDeviceConfig, z);
            loadHighBrightnessThresholds(displayDeviceConfig, z);
        }

        @VisibleForTesting
        int[] getLowDisplayBrightnessThreshold() {
            return this.mLowDisplayBrightnessThresholds;
        }

        @VisibleForTesting
        int[] getLowAmbientBrightnessThreshold() {
            return this.mLowAmbientBrightnessThresholds;
        }

        @VisibleForTesting
        int[] getHighDisplayBrightnessThreshold() {
            return this.mHighDisplayBrightnessThresholds;
        }

        @VisibleForTesting
        int[] getHighAmbientBrightnessThreshold() {
            return this.mHighAmbientBrightnessThresholds;
        }

        @VisibleForTesting
        int getRefreshRateInHighZone() {
            return this.mRefreshRateInHighZone;
        }

        @VisibleForTesting
        int getRefreshRateInLowZone() {
            return this.mRefreshRateInLowZone;
        }

        private void loadLowBrightnessThresholds(final DisplayDeviceConfig displayDeviceConfig, boolean z) {
            loadRefreshRateInHighZone(displayDeviceConfig, z);
            loadRefreshRateInLowZone(displayDeviceConfig, z);
            this.mLowDisplayBrightnessThresholds = loadBrightnessThresholds(new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda12
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    int[] lambda$loadLowBrightnessThresholds$0;
                    lambda$loadLowBrightnessThresholds$0 = DisplayModeDirector.BrightnessObserver.this.lambda$loadLowBrightnessThresholds$0();
                    return lambda$loadLowBrightnessThresholds$0;
                }
            }, new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda13
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    int[] lowDisplayBrightnessThresholds;
                    lowDisplayBrightnessThresholds = DisplayDeviceConfig.this.getLowDisplayBrightnessThresholds();
                    return lowDisplayBrightnessThresholds;
                }
            }, R.array.config_defaultFirstUserRestrictions, displayDeviceConfig, z);
            int[] loadBrightnessThresholds = loadBrightnessThresholds(new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda14
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    int[] lambda$loadLowBrightnessThresholds$2;
                    lambda$loadLowBrightnessThresholds$2 = DisplayModeDirector.BrightnessObserver.this.lambda$loadLowBrightnessThresholds$2();
                    return lambda$loadLowBrightnessThresholds$2;
                }
            }, new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda15
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    int[] lowAmbientBrightnessThresholds;
                    lowAmbientBrightnessThresholds = DisplayDeviceConfig.this.getLowAmbientBrightnessThresholds();
                    return lowAmbientBrightnessThresholds;
                }
            }, R.array.config_autoBrightnessDisplayValuesNits, displayDeviceConfig, z);
            this.mLowAmbientBrightnessThresholds = loadBrightnessThresholds;
            if (this.mLowDisplayBrightnessThresholds.length == loadBrightnessThresholds.length) {
                return;
            }
            throw new RuntimeException("display low brightness threshold array and ambient brightness threshold array have different length: displayBrightnessThresholds=" + Arrays.toString(this.mLowDisplayBrightnessThresholds) + ", ambientBrightnessThresholds=" + Arrays.toString(this.mLowAmbientBrightnessThresholds));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int[] lambda$loadLowBrightnessThresholds$0() throws Exception {
            return DisplayModeDirector.this.mDeviceConfigDisplaySettings.getLowDisplayBrightnessThresholds();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int[] lambda$loadLowBrightnessThresholds$2() throws Exception {
            return DisplayModeDirector.this.mDeviceConfigDisplaySettings.getLowAmbientBrightnessThresholds();
        }

        private void loadRefreshRateInLowZone(DisplayDeviceConfig displayDeviceConfig, boolean z) {
            int defaultLowBlockingZoneRefreshRate;
            if (displayDeviceConfig == null) {
                defaultLowBlockingZoneRefreshRate = this.mContext.getResources().getInteger(R.integer.config_dreamsBatteryLevelMinimumWhenPowered);
            } else {
                defaultLowBlockingZoneRefreshRate = displayDeviceConfig.getDefaultLowBlockingZoneRefreshRate();
            }
            if (z) {
                try {
                    defaultLowBlockingZoneRefreshRate = DisplayModeDirector.this.mDeviceConfig.getInt("display_manager", "refresh_rate_in_zone", defaultLowBlockingZoneRefreshRate);
                } catch (Exception unused) {
                }
            }
            this.mRefreshRateInLowZone = defaultLowBlockingZoneRefreshRate;
        }

        private void loadRefreshRateInHighZone(DisplayDeviceConfig displayDeviceConfig, boolean z) {
            int defaultHighBlockingZoneRefreshRate;
            if (displayDeviceConfig == null) {
                defaultHighBlockingZoneRefreshRate = this.mContext.getResources().getInteger(R.integer.config_navBarInteractionMode);
            } else {
                defaultHighBlockingZoneRefreshRate = displayDeviceConfig.getDefaultHighBlockingZoneRefreshRate();
            }
            if (z) {
                try {
                    defaultHighBlockingZoneRefreshRate = DisplayModeDirector.this.mDeviceConfig.getInt("display_manager", "refresh_rate_in_high_zone", defaultHighBlockingZoneRefreshRate);
                } catch (Exception unused) {
                }
            }
            this.mRefreshRateInHighZone = defaultHighBlockingZoneRefreshRate;
        }

        private void loadHighBrightnessThresholds(final DisplayDeviceConfig displayDeviceConfig, boolean z) {
            this.mHighDisplayBrightnessThresholds = loadBrightnessThresholds(new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda4
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    int[] lambda$loadHighBrightnessThresholds$4;
                    lambda$loadHighBrightnessThresholds$4 = DisplayModeDirector.BrightnessObserver.this.lambda$loadHighBrightnessThresholds$4();
                    return lambda$loadHighBrightnessThresholds$4;
                }
            }, new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda5
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    int[] highDisplayBrightnessThresholds;
                    highDisplayBrightnessThresholds = DisplayDeviceConfig.this.getHighDisplayBrightnessThresholds();
                    return highDisplayBrightnessThresholds;
                }
            }, R.array.config_tether_dhcp_range, displayDeviceConfig, z);
            int[] loadBrightnessThresholds = loadBrightnessThresholds(new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda6
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    int[] lambda$loadHighBrightnessThresholds$6;
                    lambda$loadHighBrightnessThresholds$6 = DisplayModeDirector.BrightnessObserver.this.lambda$loadHighBrightnessThresholds$6();
                    return lambda$loadHighBrightnessThresholds$6;
                }
            }, new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda7
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    int[] highAmbientBrightnessThresholds;
                    highAmbientBrightnessThresholds = DisplayDeviceConfig.this.getHighAmbientBrightnessThresholds();
                    return highAmbientBrightnessThresholds;
                }
            }, R.array.config_tether_bluetooth_regexs, displayDeviceConfig, z);
            this.mHighAmbientBrightnessThresholds = loadBrightnessThresholds;
            if (this.mHighDisplayBrightnessThresholds.length == loadBrightnessThresholds.length) {
                return;
            }
            throw new RuntimeException("display high brightness threshold array and ambient brightness threshold array have different length: displayBrightnessThresholds=" + Arrays.toString(this.mHighDisplayBrightnessThresholds) + ", ambientBrightnessThresholds=" + Arrays.toString(this.mHighAmbientBrightnessThresholds));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int[] lambda$loadHighBrightnessThresholds$4() throws Exception {
            return DisplayModeDirector.this.mDeviceConfigDisplaySettings.getHighDisplayBrightnessThresholds();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int[] lambda$loadHighBrightnessThresholds$6() throws Exception {
            return DisplayModeDirector.this.mDeviceConfigDisplaySettings.getHighAmbientBrightnessThresholds();
        }

        /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:4:0x000c A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private int[] loadBrightnessThresholds(Callable<int[]> callable, Callable<int[]> callable2, int i, DisplayDeviceConfig displayDeviceConfig, boolean z) {
            int[] call;
            int[] call2;
            if (z) {
                try {
                    call = callable.call();
                } catch (Exception unused) {
                }
                if (call == null) {
                    return call;
                }
                try {
                    if (displayDeviceConfig == null) {
                        call2 = this.mContext.getResources().getIntArray(i);
                    } else {
                        call2 = callable2.call();
                    }
                    call = call2;
                    return call;
                } catch (Exception e) {
                    Slog.e(DisplayModeDirector.TAG, "Unexpectedly failed to load display brightness threshold");
                    e.printStackTrace();
                    return call;
                }
            }
            call = null;
            if (call == null) {
            }
        }

        @VisibleForTesting
        int[] getLowDisplayBrightnessThresholds() {
            return this.mLowDisplayBrightnessThresholds;
        }

        @VisibleForTesting
        int[] getLowAmbientBrightnessThresholds() {
            return this.mLowAmbientBrightnessThresholds;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void observe(SensorManager sensorManager) {
            this.mSensorManager = sensorManager;
            this.mBrightness = getBrightness(0);
            int[] lowDisplayBrightnessThresholds = DisplayModeDirector.this.mDeviceConfigDisplaySettings.getLowDisplayBrightnessThresholds();
            int[] lowAmbientBrightnessThresholds = DisplayModeDirector.this.mDeviceConfigDisplaySettings.getLowAmbientBrightnessThresholds();
            if (lowDisplayBrightnessThresholds != null && lowAmbientBrightnessThresholds != null && lowDisplayBrightnessThresholds.length == lowAmbientBrightnessThresholds.length) {
                this.mLowDisplayBrightnessThresholds = lowDisplayBrightnessThresholds;
                this.mLowAmbientBrightnessThresholds = lowAmbientBrightnessThresholds;
            }
            int[] highDisplayBrightnessThresholds = DisplayModeDirector.this.mDeviceConfigDisplaySettings.getHighDisplayBrightnessThresholds();
            int[] highAmbientBrightnessThresholds = DisplayModeDirector.this.mDeviceConfigDisplaySettings.getHighAmbientBrightnessThresholds();
            if (highDisplayBrightnessThresholds != null && highAmbientBrightnessThresholds != null && highDisplayBrightnessThresholds.length == highAmbientBrightnessThresholds.length) {
                this.mHighDisplayBrightnessThresholds = highDisplayBrightnessThresholds;
                this.mHighAmbientBrightnessThresholds = highAmbientBrightnessThresholds;
            }
            int refreshRateInLowZone = DisplayModeDirector.this.mDeviceConfigDisplaySettings.getRefreshRateInLowZone();
            if (refreshRateInLowZone != -1) {
                this.mRefreshRateInLowZone = refreshRateInLowZone;
            }
            int refreshRateInHighZone = DisplayModeDirector.this.mDeviceConfigDisplaySettings.getRefreshRateInHighZone();
            if (refreshRateInHighZone != -1) {
                this.mRefreshRateInHighZone = refreshRateInHighZone;
            }
            restartObserver();
            DisplayModeDirector.this.mDeviceConfigDisplaySettings.startListening();
            this.mInjector.registerDisplayListener(this, this.mHandler, 12L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLoggingEnabled(boolean z) {
            if (this.mLoggingEnabled == z) {
                return;
            }
            this.mLoggingEnabled = z;
            this.mLightSensorListener.setLoggingEnabled(z);
        }

        @VisibleForTesting
        public void onRefreshRateSettingChangedLocked(float f, float f2) {
            boolean z = f2 - f > 1.0f && f2 > 60.0f;
            if (this.mRefreshRateChangeable != z) {
                this.mRefreshRateChangeable = z;
                updateSensorStatus();
                if (z) {
                    return;
                }
                DisplayModeDirector.this.mVotesStorage.updateGlobalVote(1, null);
                DisplayModeDirector.this.mVotesStorage.updateGlobalVote(11, null);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onLowPowerModeEnabledLocked(boolean z) {
            if (this.mLowPowerModeEnabled != z) {
                this.mLowPowerModeEnabled = z;
                updateSensorStatus();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onDeviceConfigLowBrightnessThresholdsChanged(int[] iArr, int[] iArr2) {
            final DisplayDeviceConfig displayDeviceConfig;
            if (iArr != null && iArr2 != null && iArr.length == iArr2.length) {
                this.mLowDisplayBrightnessThresholds = iArr;
                this.mLowAmbientBrightnessThresholds = iArr2;
            } else {
                synchronized (DisplayModeDirector.this.mLock) {
                    displayDeviceConfig = DisplayModeDirector.this.mDefaultDisplayDeviceConfig;
                }
                this.mLowDisplayBrightnessThresholds = loadBrightnessThresholds(new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda8
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        int[] lambda$onDeviceConfigLowBrightnessThresholdsChanged$8;
                        lambda$onDeviceConfigLowBrightnessThresholdsChanged$8 = DisplayModeDirector.BrightnessObserver.this.lambda$onDeviceConfigLowBrightnessThresholdsChanged$8();
                        return lambda$onDeviceConfigLowBrightnessThresholdsChanged$8;
                    }
                }, new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda9
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        int[] lowDisplayBrightnessThresholds;
                        lowDisplayBrightnessThresholds = DisplayDeviceConfig.this.getLowDisplayBrightnessThresholds();
                        return lowDisplayBrightnessThresholds;
                    }
                }, R.array.config_defaultFirstUserRestrictions, displayDeviceConfig, false);
                this.mLowAmbientBrightnessThresholds = loadBrightnessThresholds(new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda10
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        int[] lambda$onDeviceConfigLowBrightnessThresholdsChanged$10;
                        lambda$onDeviceConfigLowBrightnessThresholdsChanged$10 = DisplayModeDirector.BrightnessObserver.this.lambda$onDeviceConfigLowBrightnessThresholdsChanged$10();
                        return lambda$onDeviceConfigLowBrightnessThresholdsChanged$10;
                    }
                }, new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda11
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        int[] lowAmbientBrightnessThresholds;
                        lowAmbientBrightnessThresholds = DisplayDeviceConfig.this.getLowAmbientBrightnessThresholds();
                        return lowAmbientBrightnessThresholds;
                    }
                }, R.array.config_autoBrightnessDisplayValuesNits, displayDeviceConfig, false);
            }
            restartObserver();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int[] lambda$onDeviceConfigLowBrightnessThresholdsChanged$8() throws Exception {
            return DisplayModeDirector.this.mDeviceConfigDisplaySettings.getLowDisplayBrightnessThresholds();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int[] lambda$onDeviceConfigLowBrightnessThresholdsChanged$10() throws Exception {
            return DisplayModeDirector.this.mDeviceConfigDisplaySettings.getLowAmbientBrightnessThresholds();
        }

        public void onDeviceConfigRefreshRateInLowZoneChanged(int i) {
            if (i == -1) {
                synchronized (DisplayModeDirector.this.mLock) {
                    loadRefreshRateInLowZone(DisplayModeDirector.this.mDefaultDisplayDeviceConfig, false);
                }
                restartObserver();
                return;
            }
            if (i != this.mRefreshRateInLowZone) {
                this.mRefreshRateInLowZone = i;
                restartObserver();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onDeviceConfigHighBrightnessThresholdsChanged(int[] iArr, int[] iArr2) {
            final DisplayDeviceConfig displayDeviceConfig;
            if (iArr != null && iArr2 != null && iArr.length == iArr2.length) {
                this.mHighDisplayBrightnessThresholds = iArr;
                this.mHighAmbientBrightnessThresholds = iArr2;
            } else {
                synchronized (DisplayModeDirector.this.mLock) {
                    displayDeviceConfig = DisplayModeDirector.this.mDefaultDisplayDeviceConfig;
                }
                this.mHighDisplayBrightnessThresholds = loadBrightnessThresholds(new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda0
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        int[] lambda$onDeviceConfigHighBrightnessThresholdsChanged$12;
                        lambda$onDeviceConfigHighBrightnessThresholdsChanged$12 = DisplayModeDirector.BrightnessObserver.this.lambda$onDeviceConfigHighBrightnessThresholdsChanged$12();
                        return lambda$onDeviceConfigHighBrightnessThresholdsChanged$12;
                    }
                }, new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda1
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        int[] highDisplayBrightnessThresholds;
                        highDisplayBrightnessThresholds = DisplayDeviceConfig.this.getHighDisplayBrightnessThresholds();
                        return highDisplayBrightnessThresholds;
                    }
                }, R.array.config_tether_dhcp_range, displayDeviceConfig, false);
                this.mHighAmbientBrightnessThresholds = loadBrightnessThresholds(new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda2
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        int[] lambda$onDeviceConfigHighBrightnessThresholdsChanged$14;
                        lambda$onDeviceConfigHighBrightnessThresholdsChanged$14 = DisplayModeDirector.BrightnessObserver.this.lambda$onDeviceConfigHighBrightnessThresholdsChanged$14();
                        return lambda$onDeviceConfigHighBrightnessThresholdsChanged$14;
                    }
                }, new Callable() { // from class: com.android.server.display.mode.DisplayModeDirector$BrightnessObserver$$ExternalSyntheticLambda3
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        int[] highAmbientBrightnessThresholds;
                        highAmbientBrightnessThresholds = DisplayDeviceConfig.this.getHighAmbientBrightnessThresholds();
                        return highAmbientBrightnessThresholds;
                    }
                }, R.array.config_tether_bluetooth_regexs, displayDeviceConfig, false);
            }
            restartObserver();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int[] lambda$onDeviceConfigHighBrightnessThresholdsChanged$12() throws Exception {
            return DisplayModeDirector.this.mDeviceConfigDisplaySettings.getHighDisplayBrightnessThresholds();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int[] lambda$onDeviceConfigHighBrightnessThresholdsChanged$14() throws Exception {
            return DisplayModeDirector.this.mDeviceConfigDisplaySettings.getHighAmbientBrightnessThresholds();
        }

        public void onDeviceConfigRefreshRateInHighZoneChanged(int i) {
            if (i == -1) {
                synchronized (DisplayModeDirector.this.mLock) {
                    loadRefreshRateInHighZone(DisplayModeDirector.this.mDefaultDisplayDeviceConfig, false);
                }
                restartObserver();
                return;
            }
            if (i != this.mRefreshRateInHighZone) {
                this.mRefreshRateInHighZone = i;
                restartObserver();
            }
        }

        void dumpLocked(PrintWriter printWriter) {
            printWriter.println("  BrightnessObserver");
            printWriter.println("    mAmbientLux: " + this.mAmbientLux);
            printWriter.println("    mBrightness: " + this.mBrightness);
            printWriter.println("    mDefaultDisplayState: " + this.mDefaultDisplayState);
            printWriter.println("    mLowPowerModeEnabled: " + this.mLowPowerModeEnabled);
            printWriter.println("    mRefreshRateChangeable: " + this.mRefreshRateChangeable);
            printWriter.println("    mShouldObserveDisplayLowChange: " + this.mShouldObserveDisplayLowChange);
            printWriter.println("    mShouldObserveAmbientLowChange: " + this.mShouldObserveAmbientLowChange);
            printWriter.println("    mRefreshRateInLowZone: " + this.mRefreshRateInLowZone);
            for (int i : this.mLowDisplayBrightnessThresholds) {
                printWriter.println("    mDisplayLowBrightnessThreshold: " + i);
            }
            for (int i2 : this.mLowAmbientBrightnessThresholds) {
                printWriter.println("    mAmbientLowBrightnessThreshold: " + i2);
            }
            printWriter.println("    mShouldObserveDisplayHighChange: " + this.mShouldObserveDisplayHighChange);
            printWriter.println("    mShouldObserveAmbientHighChange: " + this.mShouldObserveAmbientHighChange);
            printWriter.println("    mRefreshRateInHighZone: " + this.mRefreshRateInHighZone);
            int[] iArr = this.mHighDisplayBrightnessThresholds;
            int length = iArr.length;
            for (int i3 = 0; i3 < length; i3++) {
                printWriter.println("    mDisplayHighBrightnessThresholds: " + iArr[i3]);
            }
            for (int i4 : this.mHighAmbientBrightnessThresholds) {
                printWriter.println("    mAmbientHighBrightnessThresholds: " + i4);
            }
            printWriter.println("    mRegisteredLightSensor: " + this.mRegisteredLightSensor);
            printWriter.println("    mLightSensor: " + this.mLightSensor);
            printWriter.println("    mLightSensorName: " + this.mLightSensorName);
            printWriter.println("    mLightSensorType: " + this.mLightSensorType);
            this.mLightSensorListener.dumpLocked(printWriter);
            if (this.mAmbientFilter != null) {
                this.mAmbientFilter.dump(new IndentingPrintWriter(printWriter, "    "));
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
            if (i == 0) {
                updateDefaultDisplayState();
                int brightness = getBrightness(i);
                synchronized (DisplayModeDirector.this.mLock) {
                    if (brightness != this.mBrightness) {
                        this.mBrightness = brightness;
                        onBrightnessChangedLocked();
                    }
                }
            }
        }

        private void restartObserver() {
            if (this.mRefreshRateInLowZone > 0) {
                this.mShouldObserveDisplayLowChange = hasValidThreshold(this.mLowDisplayBrightnessThresholds);
                this.mShouldObserveAmbientLowChange = hasValidThreshold(this.mLowAmbientBrightnessThresholds);
            } else {
                this.mShouldObserveDisplayLowChange = false;
                this.mShouldObserveAmbientLowChange = false;
            }
            if (this.mRefreshRateInHighZone > 0) {
                this.mShouldObserveDisplayHighChange = hasValidThreshold(this.mHighDisplayBrightnessThresholds);
                this.mShouldObserveAmbientHighChange = hasValidThreshold(this.mHighAmbientBrightnessThresholds);
            } else {
                this.mShouldObserveDisplayHighChange = false;
                this.mShouldObserveAmbientHighChange = false;
            }
            if (this.mShouldObserveAmbientLowChange || this.mShouldObserveAmbientHighChange) {
                Sensor lightSensor = getLightSensor();
                if (lightSensor != null && lightSensor != this.mLightSensor) {
                    this.mAmbientFilter = AmbientFilterFactory.createBrightnessFilter(DisplayModeDirector.TAG, this.mContext.getResources());
                    this.mLightSensor = lightSensor;
                }
            } else {
                this.mAmbientFilter = null;
                this.mLightSensor = null;
            }
            updateSensorStatus();
            synchronized (DisplayModeDirector.this.mLock) {
                onBrightnessChangedLocked();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reloadLightSensor(DisplayDeviceConfig displayDeviceConfig) {
            reloadLightSensorData(displayDeviceConfig);
            restartObserver();
        }

        private void reloadLightSensorData(DisplayDeviceConfig displayDeviceConfig) {
            if (displayDeviceConfig != null && displayDeviceConfig.getAmbientLightSensor() != null) {
                this.mLightSensorType = displayDeviceConfig.getAmbientLightSensor().type;
                this.mLightSensorName = displayDeviceConfig.getAmbientLightSensor().name;
            } else if (this.mLightSensorName == null && this.mLightSensorType == null) {
                this.mLightSensorType = this.mContext.getResources().getString(R.string.config_usbResolverActivity);
                this.mLightSensorName = "";
            }
        }

        private Sensor getLightSensor() {
            return SensorUtils.findSensor(this.mSensorManager, this.mLightSensorType, this.mLightSensorName, 5);
        }

        private boolean hasValidThreshold(int[] iArr) {
            for (int i : iArr) {
                if (i >= 0) {
                    return true;
                }
            }
            return false;
        }

        private boolean isInsideLowZone(int i, float f) {
            int i2 = 0;
            while (true) {
                int[] iArr = this.mLowDisplayBrightnessThresholds;
                if (i2 >= iArr.length) {
                    return false;
                }
                int i3 = iArr[i2];
                int i4 = this.mLowAmbientBrightnessThresholds[i2];
                if (i3 < 0 || i4 < 0) {
                    if (i3 >= 0) {
                        if (i <= i3) {
                            return true;
                        }
                    } else if (i4 >= 0 && f <= i4) {
                        return true;
                    }
                } else if (i <= i3 && f <= i4) {
                    return true;
                }
                i2++;
            }
        }

        private boolean isInsideHighZone(int i, float f) {
            int i2 = 0;
            while (true) {
                int[] iArr = this.mHighDisplayBrightnessThresholds;
                if (i2 >= iArr.length) {
                    return false;
                }
                int i3 = iArr[i2];
                int i4 = this.mHighAmbientBrightnessThresholds[i2];
                if (i3 < 0 || i4 < 0) {
                    if (i3 >= 0) {
                        if (i >= i3) {
                            return true;
                        }
                    } else if (i4 >= 0 && f >= i4) {
                        return true;
                    }
                } else if (i >= i3 && f >= i4) {
                    return true;
                }
                i2++;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onBrightnessChangedLocked() {
            Vote vote;
            Vote vote2;
            if (this.mRefreshRateChangeable && this.mBrightness >= 0) {
                boolean z = false;
                if (hasValidLowZone() && isInsideLowZone(this.mBrightness, this.mAmbientLux)) {
                    int i = this.mRefreshRateInLowZone;
                    vote = Vote.forPhysicalRefreshRates(i, i);
                    vote2 = Vote.forDisableRefreshRateSwitching();
                } else {
                    vote = null;
                    vote2 = null;
                }
                if (hasValidHighZone() && isInsideHighZone(this.mBrightness, this.mAmbientLux)) {
                    z = true;
                }
                if (z) {
                    int i2 = this.mRefreshRateInHighZone;
                    vote = Vote.forPhysicalRefreshRates(i2, i2);
                    vote2 = Vote.forDisableRefreshRateSwitching();
                }
                if (this.mLoggingEnabled) {
                    Slog.d(DisplayModeDirector.TAG, "Display brightness " + this.mBrightness + ", ambient lux " + this.mAmbientLux + ", Vote " + vote);
                }
                DisplayModeDirector.this.mVotesStorage.updateGlobalVote(1, vote);
                DisplayModeDirector.this.mVotesStorage.updateGlobalVote(11, vote2);
            }
        }

        private boolean hasValidLowZone() {
            return this.mRefreshRateInLowZone > 0 && (this.mShouldObserveDisplayLowChange || this.mShouldObserveAmbientLowChange);
        }

        private boolean hasValidHighZone() {
            return this.mRefreshRateInHighZone > 0 && (this.mShouldObserveDisplayHighChange || this.mShouldObserveAmbientHighChange);
        }

        private void updateDefaultDisplayState() {
            Display display = this.mInjector.getDisplay(0);
            if (display == null) {
                return;
            }
            setDefaultDisplayState(display.getState());
        }

        @VisibleForTesting
        void setDefaultDisplayState(int i) {
            if (this.mLoggingEnabled) {
                Slog.d(DisplayModeDirector.TAG, "setDefaultDisplayState: mDefaultDisplayState = " + this.mDefaultDisplayState + ", state = " + i);
            }
            if (this.mDefaultDisplayState != i) {
                this.mDefaultDisplayState = i;
                updateSensorStatus();
            }
        }

        private void updateSensorStatus() {
            if (this.mSensorManager == null || this.mLightSensorListener == null) {
                return;
            }
            if (this.mLoggingEnabled) {
                Slog.d(DisplayModeDirector.TAG, "updateSensorStatus: mShouldObserveAmbientLowChange = " + this.mShouldObserveAmbientLowChange + ", mShouldObserveAmbientHighChange = " + this.mShouldObserveAmbientHighChange);
                Slog.d(DisplayModeDirector.TAG, "updateSensorStatus: mLowPowerModeEnabled = " + this.mLowPowerModeEnabled + ", mRefreshRateChangeable = " + this.mRefreshRateChangeable);
            }
            if ((this.mShouldObserveAmbientLowChange || this.mShouldObserveAmbientHighChange) && isDeviceActive() && !this.mLowPowerModeEnabled && this.mRefreshRateChangeable) {
                registerLightSensor();
            } else {
                unregisterSensorListener();
            }
        }

        private void registerLightSensor() {
            Sensor sensor = this.mRegisteredLightSensor;
            if (sensor == this.mLightSensor) {
                return;
            }
            if (sensor != null) {
                unregisterSensorListener();
            }
            this.mSensorManager.registerListener(this.mLightSensorListener, this.mLightSensor, 250000, this.mHandler);
            this.mRegisteredLightSensor = this.mLightSensor;
            if (this.mLoggingEnabled) {
                Slog.d(DisplayModeDirector.TAG, "updateSensorStatus: registerListener");
            }
        }

        private void unregisterSensorListener() {
            this.mLightSensorListener.removeCallbacks();
            this.mSensorManager.unregisterListener(this.mLightSensorListener);
            this.mRegisteredLightSensor = null;
            if (this.mLoggingEnabled) {
                Slog.d(DisplayModeDirector.TAG, "updateSensorStatus: unregisterListener");
            }
        }

        private boolean isDeviceActive() {
            return this.mDefaultDisplayState == 2;
        }

        private int getBrightness(int i) {
            BrightnessInfo brightnessInfo = this.mInjector.getBrightnessInfo(i);
            if (brightnessInfo != null) {
                return BrightnessSynchronizer.brightnessFloatToInt(brightnessInfo.adjustedBrightness);
            }
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public final class LightSensorEventListener implements SensorEventListener {
            private static final int INJECT_EVENTS_INTERVAL_MS = 250;
            private final Runnable mInjectSensorEventRunnable;
            private float mLastSensorData;
            private boolean mLoggingEnabled;
            private long mTimestamp;

            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            private LightSensorEventListener() {
                this.mInjectSensorEventRunnable = new Runnable() { // from class: com.android.server.display.mode.DisplayModeDirector.BrightnessObserver.LightSensorEventListener.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LightSensorEventListener.this.processSensorData(SystemClock.uptimeMillis());
                        LightSensorEventListener lightSensorEventListener = LightSensorEventListener.this;
                        if (!lightSensorEventListener.isDifferentZone(lightSensorEventListener.mLastSensorData, BrightnessObserver.this.mAmbientLux, BrightnessObserver.this.mLowAmbientBrightnessThresholds)) {
                            LightSensorEventListener lightSensorEventListener2 = LightSensorEventListener.this;
                            if (!lightSensorEventListener2.isDifferentZone(lightSensorEventListener2.mLastSensorData, BrightnessObserver.this.mAmbientLux, BrightnessObserver.this.mHighAmbientBrightnessThresholds)) {
                                return;
                            }
                        }
                        BrightnessObserver.this.mHandler.postDelayed(LightSensorEventListener.this.mInjectSensorEventRunnable, 250L);
                    }
                };
            }

            public void dumpLocked(PrintWriter printWriter) {
                printWriter.println("    mLastSensorData: " + this.mLastSensorData);
                printWriter.println("    mTimestamp: " + formatTimestamp(this.mTimestamp));
            }

            public void setLoggingEnabled(boolean z) {
                if (this.mLoggingEnabled == z) {
                    return;
                }
                this.mLoggingEnabled = z;
            }

            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(SensorEvent sensorEvent) {
                this.mLastSensorData = sensorEvent.values[0];
                if (this.mLoggingEnabled) {
                    Slog.d(DisplayModeDirector.TAG, "On sensor changed: " + this.mLastSensorData);
                }
                boolean isDifferentZone = isDifferentZone(this.mLastSensorData, BrightnessObserver.this.mAmbientLux, BrightnessObserver.this.mLowAmbientBrightnessThresholds);
                boolean isDifferentZone2 = isDifferentZone(this.mLastSensorData, BrightnessObserver.this.mAmbientLux, BrightnessObserver.this.mHighAmbientBrightnessThresholds);
                if (((isDifferentZone && this.mLastSensorData < BrightnessObserver.this.mAmbientLux) || (isDifferentZone2 && this.mLastSensorData > BrightnessObserver.this.mAmbientLux)) && BrightnessObserver.this.mAmbientFilter != null) {
                    BrightnessObserver.this.mAmbientFilter.clear();
                }
                long uptimeMillis = SystemClock.uptimeMillis();
                this.mTimestamp = System.currentTimeMillis();
                if (BrightnessObserver.this.mAmbientFilter != null) {
                    BrightnessObserver.this.mAmbientFilter.addValue(uptimeMillis, this.mLastSensorData);
                }
                BrightnessObserver.this.mHandler.removeCallbacks(this.mInjectSensorEventRunnable);
                processSensorData(uptimeMillis);
                if ((!isDifferentZone || this.mLastSensorData <= BrightnessObserver.this.mAmbientLux) && (!isDifferentZone2 || this.mLastSensorData >= BrightnessObserver.this.mAmbientLux)) {
                    return;
                }
                BrightnessObserver.this.mHandler.postDelayed(this.mInjectSensorEventRunnable, 250L);
            }

            public void removeCallbacks() {
                BrightnessObserver.this.mHandler.removeCallbacks(this.mInjectSensorEventRunnable);
            }

            private String formatTimestamp(long j) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(new Date(j));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void processSensorData(long j) {
                if (BrightnessObserver.this.mAmbientFilter != null) {
                    BrightnessObserver brightnessObserver = BrightnessObserver.this;
                    brightnessObserver.mAmbientLux = brightnessObserver.mAmbientFilter.getEstimate(j);
                } else {
                    BrightnessObserver.this.mAmbientLux = this.mLastSensorData;
                }
                synchronized (DisplayModeDirector.this.mLock) {
                    BrightnessObserver.this.onBrightnessChangedLocked();
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public boolean isDifferentZone(float f, float f2, int[] iArr) {
                for (float f3 : iArr) {
                    if (f <= f3 && f2 > f3) {
                        return true;
                    }
                    if (f > f3 && f2 <= f3) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class UdfpsObserver extends IUdfpsRefreshRateRequestCallback.Stub {
        private final SparseBooleanArray mAuthenticationPossible;
        private final SparseBooleanArray mUdfpsRefreshRateEnabled;

        private UdfpsObserver() {
            this.mUdfpsRefreshRateEnabled = new SparseBooleanArray();
            this.mAuthenticationPossible = new SparseBooleanArray();
        }

        public void observe() {
            StatusBarManagerInternal statusBarManagerInternal = (StatusBarManagerInternal) LocalServices.getService(StatusBarManagerInternal.class);
            if (statusBarManagerInternal == null || DisplayModeDirector.this.mContext.getResources().getBoolean(17891717)) {
                return;
            }
            statusBarManagerInternal.setUdfpsRefreshRateCallback(this);
        }

        public void onRequestEnabled(int i) {
            synchronized (DisplayModeDirector.this.mLock) {
                this.mUdfpsRefreshRateEnabled.put(i, true);
                updateVoteLocked(i, true, 14);
            }
        }

        public void onRequestDisabled(int i) {
            synchronized (DisplayModeDirector.this.mLock) {
                this.mUdfpsRefreshRateEnabled.put(i, false);
                updateVoteLocked(i, false, 14);
            }
        }

        public void onAuthenticationPossible(int i, boolean z) {
            synchronized (DisplayModeDirector.this.mLock) {
                this.mAuthenticationPossible.put(i, z);
                updateVoteLocked(i, z, 8);
            }
        }

        @GuardedBy({"mLock"})
        private void updateVoteLocked(int i, boolean z, int i2) {
            Vote vote;
            if (z) {
                float maxRefreshRateLocked = DisplayModeDirector.this.getMaxRefreshRateLocked(i);
                vote = Vote.forPhysicalRefreshRates(maxRefreshRateLocked, maxRefreshRateLocked);
            } else {
                vote = null;
            }
            DisplayModeDirector.this.mVotesStorage.updateVote(i, i2, vote);
        }

        void dumpLocked(PrintWriter printWriter) {
            printWriter.println("  UdfpsObserver");
            printWriter.println("    mUdfpsRefreshRateEnabled: ");
            for (int i = 0; i < this.mUdfpsRefreshRateEnabled.size(); i++) {
                printWriter.println("      Display " + this.mUdfpsRefreshRateEnabled.keyAt(i) + ": " + (this.mUdfpsRefreshRateEnabled.valueAt(i) ? "enabled" : "disabled"));
            }
            printWriter.println("    mAuthenticationPossible: ");
            for (int i2 = 0; i2 < this.mAuthenticationPossible.size(); i2++) {
                printWriter.println("      Display " + this.mAuthenticationPossible.keyAt(i2) + ": " + (this.mAuthenticationPossible.valueAt(i2) ? "possible" : "impossible"));
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    protected static final class SensorObserver implements SensorManagerInternal.ProximityActiveListener, DisplayManager.DisplayListener {
        private final Context mContext;
        private DisplayManager mDisplayManager;
        private DisplayManagerInternal mDisplayManagerInternal;
        private final Injector mInjector;
        private final VotesStorage mVotesStorage;
        private final String mProximitySensorName = null;
        private final String mProximitySensorType = "android.sensor.proximity";

        @GuardedBy({"mSensorObserverLock"})
        private final SparseBooleanArray mDozeStateByDisplay = new SparseBooleanArray();
        private final Object mSensorObserverLock = new Object();

        @GuardedBy({"mSensorObserverLock"})
        private boolean mIsProxActive = false;

        SensorObserver(Context context, VotesStorage votesStorage, Injector injector) {
            this.mContext = context;
            this.mVotesStorage = votesStorage;
            this.mInjector = injector;
        }

        public void onProximityActive(boolean z) {
            synchronized (this.mSensorObserverLock) {
                if (this.mIsProxActive != z) {
                    this.mIsProxActive = z;
                    recalculateVotesLocked();
                }
            }
        }

        public void observe() {
            this.mDisplayManager = (DisplayManager) this.mContext.getSystemService(DisplayManager.class);
            this.mDisplayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);
            ((SensorManagerInternal) LocalServices.getService(SensorManagerInternal.class)).addProximityActiveListener(BackgroundThread.getExecutor(), this);
            synchronized (this.mSensorObserverLock) {
                for (Display display : this.mInjector.getDisplays()) {
                    this.mDozeStateByDisplay.put(display.getDisplayId(), this.mInjector.isDozeState(display));
                }
            }
            this.mInjector.registerDisplayListener(this, BackgroundThread.getHandler(), 7L);
        }

        private void recalculateVotesLocked() {
            SurfaceControl.RefreshRateRange refreshRateForDisplayAndSensor;
            for (Display display : this.mInjector.getDisplays()) {
                int displayId = display.getDisplayId();
                this.mVotesStorage.updateVote(displayId, 13, (!this.mIsProxActive || this.mDozeStateByDisplay.get(displayId) || (refreshRateForDisplayAndSensor = this.mDisplayManagerInternal.getRefreshRateForDisplayAndSensor(displayId, this.mProximitySensorName, "android.sensor.proximity")) == null) ? null : Vote.forPhysicalRefreshRates(refreshRateForDisplayAndSensor.min, refreshRateForDisplayAndSensor.max));
            }
        }

        void dump(PrintWriter printWriter) {
            printWriter.println("  SensorObserver");
            printWriter.println("    mIsProxActive=" + this.mIsProxActive);
            printWriter.println("    mDozeStateByDisplay:");
            for (int i = 0; i < this.mDozeStateByDisplay.size(); i++) {
                printWriter.println("      " + this.mDozeStateByDisplay.keyAt(i) + " -> " + this.mDozeStateByDisplay.valueAt(i));
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
            Injector injector = this.mInjector;
            boolean isDozeState = injector.isDozeState(injector.getDisplay(i));
            synchronized (this.mSensorObserverLock) {
                this.mDozeStateByDisplay.put(i, isDozeState);
                recalculateVotesLocked();
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
            boolean z = this.mDozeStateByDisplay.get(i);
            synchronized (this.mSensorObserverLock) {
                SparseBooleanArray sparseBooleanArray = this.mDozeStateByDisplay;
                Injector injector = this.mInjector;
                sparseBooleanArray.put(i, injector.isDozeState(injector.getDisplay(i)));
                if (z != this.mDozeStateByDisplay.get(i)) {
                    recalculateVotesLocked();
                }
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i) {
            synchronized (this.mSensorObserverLock) {
                this.mDozeStateByDisplay.delete(i);
                recalculateVotesLocked();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class HbmObserver implements DisplayManager.DisplayListener {
        private final DeviceConfigDisplaySettings mDeviceConfigDisplaySettings;
        private DisplayManagerInternal mDisplayManagerInternal;
        private final Handler mHandler;
        private final Injector mInjector;
        private int mRefreshRateInHbmHdr;
        private int mRefreshRateInHbmSunlight;
        private final VotesStorage mVotesStorage;
        private final SparseIntArray mHbmMode = new SparseIntArray();
        private final SparseBooleanArray mHbmActive = new SparseBooleanArray();

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
        }

        HbmObserver(Injector injector, VotesStorage votesStorage, Handler handler, DeviceConfigDisplaySettings deviceConfigDisplaySettings) {
            this.mInjector = injector;
            this.mVotesStorage = votesStorage;
            this.mHandler = handler;
            this.mDeviceConfigDisplaySettings = deviceConfigDisplaySettings;
        }

        public void setupHdrRefreshRates(DisplayDeviceConfig displayDeviceConfig) {
            this.mRefreshRateInHbmHdr = this.mDeviceConfigDisplaySettings.getRefreshRateInHbmHdr(displayDeviceConfig);
            this.mRefreshRateInHbmSunlight = this.mDeviceConfigDisplaySettings.getRefreshRateInHbmSunlight(displayDeviceConfig);
        }

        public void observe() {
            synchronized (DisplayModeDirector.this.mLock) {
                setupHdrRefreshRates(DisplayModeDirector.this.mDefaultDisplayDeviceConfig);
            }
            this.mDisplayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);
            this.mInjector.registerDisplayListener(this, this.mHandler, 10L);
        }

        @VisibleForTesting
        int getRefreshRateInHbmSunlight() {
            return this.mRefreshRateInHbmSunlight;
        }

        @VisibleForTesting
        int getRefreshRateInHbmHdr() {
            return this.mRefreshRateInHbmHdr;
        }

        public void onDeviceConfigRefreshRateInHbmSunlightChanged(int i) {
            if (i != this.mRefreshRateInHbmSunlight) {
                this.mRefreshRateInHbmSunlight = i;
                onDeviceConfigRefreshRateInHbmChanged();
            }
        }

        public void onDeviceConfigRefreshRateInHbmHdrChanged(int i) {
            if (i != this.mRefreshRateInHbmHdr) {
                this.mRefreshRateInHbmHdr = i;
                onDeviceConfigRefreshRateInHbmChanged();
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i) {
            this.mVotesStorage.updateVote(i, 2, null);
            this.mHbmMode.delete(i);
            this.mHbmActive.delete(i);
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
            BrightnessInfo brightnessInfo = this.mInjector.getBrightnessInfo(i);
            if (brightnessInfo == null) {
                return;
            }
            int i2 = brightnessInfo.highBrightnessMode;
            boolean z = i2 != 0 && brightnessInfo.adjustedBrightness > brightnessInfo.highBrightnessTransitionPoint;
            if (i2 == this.mHbmMode.get(i) && z == this.mHbmActive.get(i)) {
                return;
            }
            this.mHbmMode.put(i, i2);
            this.mHbmActive.put(i, z);
            recalculateVotesForDisplay(i);
        }

        private void onDeviceConfigRefreshRateInHbmChanged() {
            int[] copyKeys = this.mHbmMode.copyKeys();
            if (copyKeys != null) {
                for (int i : copyKeys) {
                    recalculateVotesForDisplay(i);
                }
            }
        }

        private void recalculateVotesForDisplay(int i) {
            int i2;
            int i3 = 0;
            Vote vote = null;
            if (this.mHbmActive.get(i, false)) {
                int i4 = this.mHbmMode.get(i, 0);
                if (i4 == 1) {
                    int i5 = this.mRefreshRateInHbmSunlight;
                    if (i5 > 0) {
                        vote = Vote.forPhysicalRefreshRates(i5, i5);
                    } else {
                        List refreshRateLimitations = this.mDisplayManagerInternal.getRefreshRateLimitations(i);
                        while (true) {
                            if (refreshRateLimitations == null || i3 >= refreshRateLimitations.size()) {
                                break;
                            }
                            DisplayManagerInternal.RefreshRateLimitation refreshRateLimitation = (DisplayManagerInternal.RefreshRateLimitation) refreshRateLimitations.get(i3);
                            if (refreshRateLimitation.type == 1) {
                                SurfaceControl.RefreshRateRange refreshRateRange = refreshRateLimitation.range;
                                vote = Vote.forPhysicalRefreshRates(refreshRateRange.min, refreshRateRange.max);
                                break;
                            }
                            i3++;
                        }
                    }
                } else if (i4 == 2 && (i2 = this.mRefreshRateInHbmHdr) > 0) {
                    vote = Vote.forPhysicalRefreshRates(i2, i2);
                } else {
                    Slog.w(DisplayModeDirector.TAG, "Unexpected HBM mode " + i4 + " for display ID " + i);
                }
            }
            this.mVotesStorage.updateVote(i, 2, vote);
        }

        void dumpLocked(PrintWriter printWriter) {
            printWriter.println("   HbmObserver");
            printWriter.println("     mHbmMode: " + this.mHbmMode);
            printWriter.println("     mHbmActive: " + this.mHbmActive);
            printWriter.println("     mRefreshRateInHbmSunlight: " + this.mRefreshRateInHbmSunlight);
            printWriter.println("     mRefreshRateInHbmHdr: " + this.mRefreshRateInHbmHdr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class DeviceConfigDisplaySettings implements DeviceConfig.OnPropertiesChangedListener {
        private DeviceConfigDisplaySettings() {
        }

        public void startListening() {
            DisplayModeDirector.this.mDeviceConfig.addOnPropertiesChangedListener("display_manager", BackgroundThread.getExecutor(), this);
        }

        public int[] getLowDisplayBrightnessThresholds() {
            return getIntArrayProperty("peak_refresh_rate_brightness_thresholds");
        }

        public int[] getLowAmbientBrightnessThresholds() {
            return getIntArrayProperty("peak_refresh_rate_ambient_thresholds");
        }

        public int getRefreshRateInLowZone() {
            return DisplayModeDirector.this.mDeviceConfig.getInt("display_manager", "refresh_rate_in_zone", -1);
        }

        public int[] getHighDisplayBrightnessThresholds() {
            return getIntArrayProperty("fixed_refresh_rate_high_display_brightness_thresholds");
        }

        public int[] getHighAmbientBrightnessThresholds() {
            return getIntArrayProperty("fixed_refresh_rate_high_ambient_brightness_thresholds");
        }

        public int getRefreshRateInHighZone() {
            return DisplayModeDirector.this.mDeviceConfig.getInt("display_manager", "refresh_rate_in_high_zone", -1);
        }

        public int getRefreshRateInHbmHdr(DisplayDeviceConfig displayDeviceConfig) {
            int defaultRefreshRateInHbmHdr;
            if (displayDeviceConfig == null) {
                defaultRefreshRateInHbmHdr = DisplayModeDirector.this.mContext.getResources().getInteger(R.integer.config_dreamsBatteryLevelDrainCutoff);
            } else {
                defaultRefreshRateInHbmHdr = displayDeviceConfig.getDefaultRefreshRateInHbmHdr();
            }
            try {
                return DisplayModeDirector.this.mDeviceConfig.getInt("display_manager", "refresh_rate_in_hbm_hdr", defaultRefreshRateInHbmHdr);
            } catch (NullPointerException unused) {
                return defaultRefreshRateInHbmHdr;
            }
        }

        public int getRefreshRateInHbmSunlight(DisplayDeviceConfig displayDeviceConfig) {
            int defaultRefreshRateInHbmSunlight;
            if (displayDeviceConfig == null) {
                defaultRefreshRateInHbmSunlight = DisplayModeDirector.this.mContext.getResources().getInteger(R.integer.config_dreamsBatteryLevelMinimumWhenNotPowered);
            } else {
                defaultRefreshRateInHbmSunlight = displayDeviceConfig.getDefaultRefreshRateInHbmSunlight();
            }
            try {
                return DisplayModeDirector.this.mDeviceConfig.getInt("display_manager", "refresh_rate_in_hbm_sunlight", defaultRefreshRateInHbmSunlight);
            } catch (NullPointerException unused) {
                return defaultRefreshRateInHbmSunlight;
            }
        }

        public Float getDefaultPeakRefreshRate() {
            float f = DisplayModeDirector.this.mDeviceConfig.getFloat("display_manager", "peak_refresh_rate_default", -1.0f);
            if (f == -1.0f) {
                return null;
            }
            return Float.valueOf(f);
        }

        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            DisplayModeDirector.this.mHandler.obtainMessage(3, getDefaultPeakRefreshRate()).sendToTarget();
            int[] lowDisplayBrightnessThresholds = getLowDisplayBrightnessThresholds();
            int[] lowAmbientBrightnessThresholds = getLowAmbientBrightnessThresholds();
            int refreshRateInLowZone = getRefreshRateInLowZone();
            DisplayModeDirector.this.mHandler.obtainMessage(2, new Pair(lowDisplayBrightnessThresholds, lowAmbientBrightnessThresholds)).sendToTarget();
            DisplayModeDirector.this.mHandler.obtainMessage(4, refreshRateInLowZone, 0).sendToTarget();
            int[] highDisplayBrightnessThresholds = getHighDisplayBrightnessThresholds();
            int[] highAmbientBrightnessThresholds = getHighAmbientBrightnessThresholds();
            int refreshRateInHighZone = getRefreshRateInHighZone();
            DisplayModeDirector.this.mHandler.obtainMessage(6, new Pair(highDisplayBrightnessThresholds, highAmbientBrightnessThresholds)).sendToTarget();
            DisplayModeDirector.this.mHandler.obtainMessage(5, refreshRateInHighZone, 0).sendToTarget();
            synchronized (DisplayModeDirector.this.mLock) {
                DisplayModeDirector.this.mHandler.obtainMessage(7, getRefreshRateInHbmSunlight(DisplayModeDirector.this.mDefaultDisplayDeviceConfig), 0).sendToTarget();
                DisplayModeDirector.this.mHandler.obtainMessage(8, getRefreshRateInHbmHdr(DisplayModeDirector.this.mDefaultDisplayDeviceConfig), 0).sendToTarget();
            }
        }

        private int[] getIntArrayProperty(String str) {
            String string = DisplayModeDirector.this.mDeviceConfig.getString("display_manager", str, (String) null);
            if (string != null) {
                return parseIntArray(string);
            }
            return null;
        }

        private int[] parseIntArray(String str) {
            String[] split = str.split(",");
            int length = split.length;
            int[] iArr = new int[length];
            for (int i = 0; i < length; i++) {
                try {
                    iArr[i] = Integer.parseInt(split[i]);
                } catch (NumberFormatException e) {
                    Slog.e(DisplayModeDirector.TAG, "Incorrect format for array: '" + str + "'", e);
                    return null;
                }
            }
            return iArr;
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static class RealInjector implements Injector {
        private final Context mContext;
        private DisplayManager mDisplayManager;

        RealInjector(Context context) {
            this.mContext = context;
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public DeviceConfigInterface getDeviceConfig() {
            return DeviceConfigInterface.REAL;
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public void registerPeakRefreshRateObserver(ContentResolver contentResolver, ContentObserver contentObserver) {
            contentResolver.registerContentObserver(Injector.PEAK_REFRESH_RATE_URI, false, contentObserver, 0);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public void registerDisplayListener(DisplayManager.DisplayListener displayListener, Handler handler) {
            getDisplayManager().registerDisplayListener(displayListener, handler);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public void registerDisplayListener(DisplayManager.DisplayListener displayListener, Handler handler, long j) {
            getDisplayManager().registerDisplayListener(displayListener, handler, j);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public Display getDisplay(int i) {
            return getDisplayManager().getDisplay(i);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public Display[] getDisplays() {
            return getDisplayManager().getDisplays("android.hardware.display.category.ALL_INCLUDING_DISABLED");
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public boolean getDisplayInfo(int i, DisplayInfo displayInfo) {
            Display display = getDisplayManager().getDisplay(i);
            if (display == null) {
                return false;
            }
            return display.getDisplayInfo(displayInfo);
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public BrightnessInfo getBrightnessInfo(int i) {
            Display display = getDisplayManager().getDisplay(i);
            if (display != null) {
                return display.getBrightnessInfo();
            }
            return null;
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public boolean isDozeState(Display display) {
            if (display == null) {
                return false;
            }
            return Display.isDozeState(display.getState());
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public boolean registerThermalServiceListener(IThermalEventListener iThermalEventListener) {
            IThermalService thermalService = getThermalService();
            if (thermalService == null) {
                Slog.w(DisplayModeDirector.TAG, "Could not observe thermal status. Service not available");
                return false;
            }
            try {
                thermalService.registerThermalEventListenerWithType(iThermalEventListener, 3);
                return true;
            } catch (RemoteException e) {
                Slog.e(DisplayModeDirector.TAG, "Failed to register thermal status listener", e);
                return false;
            }
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.Injector
        public boolean supportsFrameRateOverride() {
            return SurfaceFlingerProperties.enable_frame_rate_override().orElse(Boolean.TRUE).booleanValue();
        }

        private DisplayManager getDisplayManager() {
            if (this.mDisplayManager == null) {
                this.mDisplayManager = (DisplayManager) this.mContext.getSystemService(DisplayManager.class);
            }
            return this.mDisplayManager;
        }

        private IThermalService getThermalService() {
            return IThermalService.Stub.asInterface(ServiceManager.getService("thermalservice"));
        }
    }
}
