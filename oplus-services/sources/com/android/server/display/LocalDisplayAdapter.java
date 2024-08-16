package com.android.server.display;

import android.R;
import android.app.ActivityThread;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.sidekick.SidekickInternal;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.DisplayUtils;
import android.util.LongSparseArray;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayAddress;
import android.view.DisplayCutout;
import android.view.DisplayEventReceiver;
import android.view.DisplayShape;
import android.view.RoundedCorners;
import android.view.SurfaceControl;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.display.BrightnessSynchronizer;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.LocalServices;
import com.android.server.display.DisplayAdapter;
import com.android.server.display.DisplayManagerService;
import com.android.server.display.LocalDisplayAdapter;
import com.android.server.display.mode.DisplayModeDirector;
import com.android.server.lights.LightsManager;
import com.android.server.lights.LogicalLight;
import com.pixelworks.hardware.IrisFeatureHal;
import com.pixelworks.hardware.IrisHal;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class LocalDisplayAdapter extends DisplayAdapter {
    private static final String PROPERTY_EMULATOR_CIRCULAR = "ro.emulator.circular";
    private static final String TAG = "LocalDisplayAdapter";
    private static final String UNIQUE_ID_PREFIX = "local:";
    private static final float ZERO_NIT = 0.0f;
    private final int mBrightnessThreshold;
    private final LongSparseArray<LocalDisplayDevice> mDevices;
    private final int mHightBrightness;
    private final Injector mInjector;
    private int mIris514command;
    private int mIris514ext;
    private IrisHal mIrisHal;
    private final boolean mIsBootDisplayModeSupported;
    private LocalDisplayAdapterWrapper mLdaWrapper;
    private final int mLowBrightness;
    private Context mOverlayContext;
    private final SurfaceControlProxy mSurfaceControlProxy;
    private int mlast_level;
    private static final boolean PANIC_DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static boolean DEBUG = SystemProperties.getBoolean("dbg.dms.lda", false);
    private static final boolean MTK_DEBUG = "eng".equals(Build.TYPE);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface DisplayEventListener {
        void onFrameRateOverridesChanged(long j, long j2, DisplayEventReceiver.FrameRateOverride[] frameRateOverrideArr);

        void onHotplug(long j, long j2, boolean z);

        void onModeChanged(long j, long j2, int i, long j3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public enum IrisTable {
        LOWLEVEL,
        MEDIUMLEVEL,
        HIGHTLEVEL
    }

    static int getPowerModeForState(int i) {
        if (i == 1) {
            return 0;
        }
        if (i == 6) {
            return 4;
        }
        if (i != 3) {
            return i != 4 ? 2 : 3;
        }
        return 1;
    }

    public LocalDisplayAdapter(DisplayManagerService.SyncRoot syncRoot, Context context, Handler handler, DisplayAdapter.Listener listener) {
        this(syncRoot, context, handler, listener, new Injector());
    }

    @VisibleForTesting
    LocalDisplayAdapter(DisplayManagerService.SyncRoot syncRoot, Context context, Handler handler, DisplayAdapter.Listener listener, Injector injector) {
        super(syncRoot, context, handler, listener, TAG);
        this.mDevices = new LongSparseArray<>();
        this.mIrisHal = null;
        this.mIris514command = FrameworkStatsLog.DEVICE_WIDE_JOB_CONSTRAINT_CHANGED;
        this.mIris514ext = 3;
        this.mlast_level = -1;
        this.mLowBrightness = 2500;
        this.mHightBrightness = 4334;
        this.mBrightnessThreshold = 12;
        this.mLdaWrapper = new LocalDisplayAdapterWrapper();
        this.mInjector = injector;
        SurfaceControlProxy surfaceControlProxy = injector.getSurfaceControlProxy();
        this.mSurfaceControlProxy = surfaceControlProxy;
        this.mLdaWrapper.getExtImpl().init(context);
        this.mIsBootDisplayModeSupported = surfaceControlProxy.getBootDisplayModeSupport();
        if (getChipFeatue() != 0) {
            this.mIrisHal = new IrisHal();
        } else {
            Slog.e(TAG, "Failed to get Iris feature");
        }
    }

    @Override // com.android.server.display.DisplayAdapter
    public void registerLocked() {
        SurfaceControl.DisplayMode[] displayModeArr;
        SurfaceControl.DisplayMode[] displayModeArr2;
        super.registerLocked();
        this.mInjector.setDisplayEventListenerLocked(getHandler().getLooper(), new LocalDisplayEventListener());
        long[] physicalDisplayIds = this.mSurfaceControlProxy.getPhysicalDisplayIds();
        int i = 0;
        while (i < physicalDisplayIds.length) {
            int i2 = i + 1;
            for (int i3 = i2; i3 < physicalDisplayIds.length; i3++) {
                SurfaceControl.StaticDisplayInfo staticDisplayInfo = this.mSurfaceControlProxy.getStaticDisplayInfo(physicalDisplayIds[i]);
                SurfaceControl.StaticDisplayInfo staticDisplayInfo2 = this.mSurfaceControlProxy.getStaticDisplayInfo(physicalDisplayIds[i3]);
                if (staticDisplayInfo != null && staticDisplayInfo2 != null && staticDisplayInfo.isInternal && staticDisplayInfo2.isInternal) {
                    SurfaceControl.DynamicDisplayInfo dynamicDisplayInfo = this.mSurfaceControlProxy.getDynamicDisplayInfo(physicalDisplayIds[i]);
                    SurfaceControl.DynamicDisplayInfo dynamicDisplayInfo2 = this.mSurfaceControlProxy.getDynamicDisplayInfo(physicalDisplayIds[i3]);
                    if (dynamicDisplayInfo != null && dynamicDisplayInfo2 != null && (displayModeArr = dynamicDisplayInfo.supportedDisplayModes) != null && (displayModeArr2 = dynamicDisplayInfo2.supportedDisplayModes) != null && displayModeArr[0].width < displayModeArr2[0].width) {
                        Slog.e(TAG, "registerLocked: change ids[" + i + "]=" + physicalDisplayIds[i] + " w=" + dynamicDisplayInfo.supportedDisplayModes[0].width + " to ids[" + i3 + "]=" + physicalDisplayIds[i3] + " w=" + dynamicDisplayInfo2.supportedDisplayModes[0].width);
                        long j = physicalDisplayIds[i];
                        physicalDisplayIds[i] = physicalDisplayIds[i3];
                        physicalDisplayIds[i3] = j;
                    }
                }
            }
            i = i2;
        }
        for (long j2 : physicalDisplayIds) {
            tryConnectDisplayLocked(j2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryConnectDisplayLocked(long j) {
        IBinder physicalDisplayToken = this.mSurfaceControlProxy.getPhysicalDisplayToken(j);
        if (physicalDisplayToken != null) {
            SurfaceControl.StaticDisplayInfo staticDisplayInfo = this.mSurfaceControlProxy.getStaticDisplayInfo(j);
            if (staticDisplayInfo == null) {
                Slog.w(TAG, "No valid static info found for display device " + j);
                return;
            }
            SurfaceControl.DynamicDisplayInfo dynamicDisplayInfo = this.mSurfaceControlProxy.getDynamicDisplayInfo(j);
            if (dynamicDisplayInfo == null) {
                Slog.w(TAG, "No valid dynamic info found for display device " + j);
                return;
            }
            if (dynamicDisplayInfo.supportedDisplayModes == null) {
                Slog.w(TAG, "No valid modes found for display device " + j);
                return;
            }
            if (dynamicDisplayInfo.activeDisplayModeId < 0) {
                Slog.w(TAG, "No valid active mode found for display device " + j);
                return;
            }
            if (dynamicDisplayInfo.activeColorMode < 0) {
                Slog.w(TAG, "No valid active color mode for display device " + j);
                dynamicDisplayInfo.activeColorMode = -1;
            }
            SurfaceControl.DesiredDisplayModeSpecs desiredDisplayModeSpecs = this.mSurfaceControlProxy.getDesiredDisplayModeSpecs(physicalDisplayToken);
            LocalDisplayDevice localDisplayDevice = this.mDevices.get(j);
            Slog.d(TAG, "tryConnectDisplayLocked id=" + j + " staticInfo=" + staticDisplayInfo + " dynamicInfo=" + dynamicDisplayInfo);
            if (localDisplayDevice == null) {
                boolean z = this.mDevices.size() == 0;
                if (z) {
                    this.mLdaWrapper.getExtImpl().setPrimaryPhysicalDisplayId(j);
                }
                LocalDisplayDevice localDisplayDevice2 = new LocalDisplayDevice(physicalDisplayToken, j, staticDisplayInfo, dynamicDisplayInfo, desiredDisplayModeSpecs, z);
                this.mDevices.put(j, localDisplayDevice2);
                sendDisplayDeviceEventLocked(localDisplayDevice2, 1);
                return;
            }
            if (localDisplayDevice.updateDisplayPropertiesLocked(staticDisplayInfo, dynamicDisplayInfo, desiredDisplayModeSpecs)) {
                sendDisplayDeviceEventLocked(localDisplayDevice, 2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryDisconnectDisplayLocked(long j) {
        LocalDisplayDevice localDisplayDevice = this.mDevices.get(j);
        if (localDisplayDevice != null) {
            this.mDevices.remove(j);
            sendDisplayDeviceEventLocked(localDisplayDevice, 3);
        }
    }

    public int getChipFeatue() {
        return new IrisFeatureHal().getFeature();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class LocalDisplayDevice extends DisplayDevice {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private int mActiveColorMode;
        private int mActiveModeId;
        private float mActiveRenderFrameRate;
        private SurfaceControl.DisplayMode mActiveSfDisplayMode;
        private int mActiveSfDisplayModeAtStartId;
        private boolean mAllmRequested;
        private boolean mAllmSupported;
        private final BacklightAdapter mBacklightAdapter;
        private float mBrightnessState;
        private int mCommittedState;
        private int mCurrentBacklightType;
        private float mCurrentHdrSdrRatio;
        private int mDcThreshold;
        private int mDefaultModeGroup;
        private int mDefaultModeId;
        private final DisplayModeDirector.DesiredDisplayModeSpecs mDisplayModeSpecs;
        private boolean mDisplayModeSpecsInvalid;
        private DisplayEventReceiver.FrameRateOverride[] mFrameRateOverrides;
        private boolean mGameContentTypeRequested;
        private boolean mGameContentTypeSupported;
        private boolean mHavePendingChanges;
        private Display.HdrCapabilities mHdrCapabilities;
        private DisplayDeviceInfo mInfo;
        private final boolean mIsFirstDisplay;
        private final long mPhysicalDisplayId;
        private float mSdrBrightnessState;
        private SurfaceControl.DisplayMode[] mSfDisplayModes;
        private boolean mSidekickActive;
        private final SidekickInternal mSidekickInternal;
        private int mState;
        private SurfaceControl.StaticDisplayInfo mStaticDisplayInfo;
        private final ArrayList<Integer> mSupportedColorModes;
        private final SparseArray<DisplayModeRecord> mSupportedModes;
        private int mSystemPreferredModeId;
        private Display.Mode mUserPreferredMode;
        private int mUserPreferredModeId;

        @Override // com.android.server.display.DisplayDevice
        public boolean hasStableUniqueId() {
            return true;
        }

        LocalDisplayDevice(IBinder iBinder, long j, SurfaceControl.StaticDisplayInfo staticDisplayInfo, SurfaceControl.DynamicDisplayInfo dynamicDisplayInfo, SurfaceControl.DesiredDisplayModeSpecs desiredDisplayModeSpecs, boolean z) {
            super(LocalDisplayAdapter.this, iBinder, LocalDisplayAdapter.UNIQUE_ID_PREFIX + j, LocalDisplayAdapter.this.getContext());
            this.mSupportedModes = new SparseArray<>();
            this.mSupportedColorModes = new ArrayList<>();
            this.mDisplayModeSpecs = new DisplayModeDirector.DesiredDisplayModeSpecs();
            this.mState = 0;
            this.mCommittedState = 0;
            this.mBrightnessState = Float.NaN;
            this.mSdrBrightnessState = Float.NaN;
            this.mCurrentHdrSdrRatio = Float.NaN;
            this.mCurrentBacklightType = 0;
            this.mDefaultModeId = -1;
            this.mSystemPreferredModeId = -1;
            this.mUserPreferredModeId = -1;
            this.mActiveSfDisplayModeAtStartId = -1;
            this.mActiveModeId = -1;
            this.mDcThreshold = SystemProperties.getInt("ro.oplus.dc.brightness.threshold", 0);
            this.mFrameRateOverrides = new DisplayEventReceiver.FrameRateOverride[0];
            LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setStaticDisplayDensity(staticDisplayInfo, j);
            this.mPhysicalDisplayId = j;
            this.mIsFirstDisplay = z;
            updateDisplayPropertiesLocked(staticDisplayInfo, dynamicDisplayInfo, desiredDisplayModeSpecs);
            this.mSidekickInternal = (SidekickInternal) LocalServices.getService(SidekickInternal.class);
            this.mBacklightAdapter = new BacklightAdapter(iBinder, z, LocalDisplayAdapter.this.mSurfaceControlProxy);
            this.mActiveSfDisplayModeAtStartId = dynamicDisplayInfo.activeDisplayModeId;
        }

        @Override // com.android.server.display.DisplayDevice
        public Display.Mode getActiveDisplayModeAtStartLocked() {
            return findMode(findMatchingModeIdLocked(this.mActiveSfDisplayModeAtStartId));
        }

        public boolean updateDisplayPropertiesLocked(SurfaceControl.StaticDisplayInfo staticDisplayInfo, SurfaceControl.DynamicDisplayInfo dynamicDisplayInfo, SurfaceControl.DesiredDisplayModeSpecs desiredDisplayModeSpecs) {
            boolean updateStaticInfo = updateStaticInfo(staticDisplayInfo) | updateDisplayModesLocked(dynamicDisplayInfo.supportedDisplayModes, dynamicDisplayInfo.preferredBootDisplayMode, dynamicDisplayInfo.activeDisplayModeId, dynamicDisplayInfo.renderFrameRate, desiredDisplayModeSpecs) | updateColorModesLocked(dynamicDisplayInfo.supportedColorModes, dynamicDisplayInfo.activeColorMode) | updateHdrCapabilitiesLocked(dynamicDisplayInfo.hdrCapabilities) | updateAllmSupport(dynamicDisplayInfo.autoLowLatencyModeSupported) | updateGameContentTypeSupport(dynamicDisplayInfo.gameContentTypeSupported);
            if (updateStaticInfo) {
                this.mHavePendingChanges = true;
            }
            return updateStaticInfo;
        }

        /* JADX WARN: Code restructure failed: missing block: B:94:0x01b7, code lost:
        
            if (r16.mDisplayModeSpecs.appRequest.equals(r21.appRequestRanges) != false) goto L88;
         */
        /* JADX WARN: Removed duplicated region for block: B:101:0x01d4  */
        /* JADX WARN: Removed duplicated region for block: B:108:0x01df  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x0195  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean updateDisplayModesLocked(SurfaceControl.DisplayMode[] displayModeArr, int i, int i2, float f, SurfaceControl.DesiredDisplayModeSpecs desiredDisplayModeSpecs) {
            DisplayModeRecord displayModeRecord;
            DisplayModeRecord displayModeRecord2;
            boolean z;
            int i3;
            boolean z2;
            float f2;
            boolean z3;
            boolean z4;
            this.mSfDisplayModes = (SurfaceControl.DisplayMode[]) Arrays.copyOf(displayModeArr, displayModeArr.length);
            this.mActiveSfDisplayMode = getModeById(displayModeArr, i2);
            SurfaceControl.DisplayMode modeById = getModeById(displayModeArr, i);
            Slog.d(LocalDisplayAdapter.TAG, "updateDisplayModesLocked activeMode=" + this.mActiveSfDisplayMode);
            LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().updateDisplayModes(this.mIsFirstDisplay, this.mPhysicalDisplayId);
            ArrayList arrayList = new ArrayList();
            int i4 = 0;
            boolean z5 = false;
            while (i4 < displayModeArr.length) {
                SurfaceControl.DisplayMode displayMode = displayModeArr[i4];
                ArrayList arrayList2 = new ArrayList();
                int i5 = 0;
                while (i5 < displayModeArr.length) {
                    SurfaceControl.DisplayMode displayMode2 = displayModeArr[i5];
                    if (i5 != i4 && displayMode2.width == displayMode.width && displayMode2.height == displayMode.height && displayMode2.refreshRate != displayMode.refreshRate && displayMode2.group == displayMode.group) {
                        arrayList2.add(Float.valueOf(displayMode2.refreshRate));
                    }
                    i5++;
                }
                Collections.sort(arrayList2);
                Iterator it = arrayList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z4 = false;
                        break;
                    }
                    DisplayModeRecord displayModeRecord3 = (DisplayModeRecord) it.next();
                    if (displayModeRecord3.hasMatchingMode(displayMode) && refreshRatesEquals(arrayList2, displayModeRecord3.mMode.getAlternativeRefreshRates())) {
                        z4 = true;
                        break;
                    }
                }
                if (!z4) {
                    DisplayModeRecord findDisplayModeRecord = findDisplayModeRecord(displayMode, arrayList2);
                    if (findDisplayModeRecord == null) {
                        int size = arrayList2.size();
                        float[] fArr = new float[size];
                        for (int i6 = 0; i6 < size; i6++) {
                            fArr[i6] = arrayList2.get(i6).floatValue();
                        }
                        findDisplayModeRecord = new DisplayModeRecord(displayMode, fArr);
                        z5 = true;
                    }
                    arrayList.add(findDisplayModeRecord);
                }
                i4++;
            }
            Iterator it2 = arrayList.iterator();
            while (true) {
                displayModeRecord = null;
                if (!it2.hasNext()) {
                    displayModeRecord2 = null;
                    break;
                }
                displayModeRecord2 = (DisplayModeRecord) it2.next();
                if (displayModeRecord2.hasMatchingMode(this.mActiveSfDisplayMode)) {
                    break;
                }
            }
            if (i != -1 && modeById != null) {
                Iterator it3 = arrayList.iterator();
                while (true) {
                    if (!it3.hasNext()) {
                        break;
                    }
                    DisplayModeRecord displayModeRecord4 = (DisplayModeRecord) it3.next();
                    if (displayModeRecord4.hasMatchingMode(modeById)) {
                        displayModeRecord = displayModeRecord4;
                        break;
                    }
                }
                if (displayModeRecord != null) {
                    int modeId = displayModeRecord.mMode.getModeId();
                    if (LocalDisplayAdapter.this.mIsBootDisplayModeSupported && this.mSystemPreferredModeId != modeId) {
                        this.mSystemPreferredModeId = modeId;
                        z = true;
                        i3 = this.mActiveModeId;
                        if (i3 != -1 || i3 == displayModeRecord2.mMode.getModeId()) {
                            z2 = false;
                        } else {
                            Slog.d(LocalDisplayAdapter.TAG, "The active mode was changed from SurfaceFlinger or the display device to " + displayModeRecord2.mMode);
                            this.mActiveModeId = displayModeRecord2.mMode.getModeId();
                            LocalDisplayAdapter.this.sendTraversalRequestLocked();
                            z2 = true;
                        }
                        f2 = this.mActiveRenderFrameRate;
                        if (f2 > 0.0f || f2 == f) {
                            z3 = false;
                        } else {
                            Slog.d(LocalDisplayAdapter.TAG, "The render frame rate was changed from SurfaceFlinger or the display device to " + f);
                            this.mActiveRenderFrameRate = f;
                            LocalDisplayAdapter.this.sendTraversalRequestLocked();
                            z3 = true;
                        }
                        if (this.mDisplayModeSpecs.baseModeId != -1) {
                            int findMatchingModeIdLocked = findMatchingModeIdLocked(desiredDisplayModeSpecs.defaultMode);
                            if (findMatchingModeIdLocked != -1) {
                                DisplayModeDirector.DesiredDisplayModeSpecs desiredDisplayModeSpecs2 = this.mDisplayModeSpecs;
                                if (desiredDisplayModeSpecs2.baseModeId == findMatchingModeIdLocked) {
                                    if (desiredDisplayModeSpecs2.primary.equals(desiredDisplayModeSpecs.primaryRanges)) {
                                    }
                                }
                            }
                            this.mDisplayModeSpecsInvalid = true;
                            LocalDisplayAdapter.this.sendTraversalRequestLocked();
                        }
                        if (arrayList.size() == this.mSupportedModes.size() || z5) {
                            return z2 || z || z3;
                        }
                        this.mSupportedModes.clear();
                        Iterator it4 = arrayList.iterator();
                        while (it4.hasNext()) {
                            DisplayModeRecord displayModeRecord5 = (DisplayModeRecord) it4.next();
                            this.mSupportedModes.put(displayModeRecord5.mMode.getModeId(), displayModeRecord5);
                        }
                        int i7 = this.mDefaultModeId;
                        if (i7 == -1) {
                            this.mDefaultModeId = displayModeRecord2.mMode.getModeId();
                            this.mDefaultModeGroup = this.mActiveSfDisplayMode.group;
                            this.mActiveRenderFrameRate = f;
                        } else if (z5 && z2) {
                            Slog.d(LocalDisplayAdapter.TAG, "New display modes are added and the active mode has changed, use active mode as default mode.");
                            this.mDefaultModeId = displayModeRecord2.mMode.getModeId();
                            this.mDefaultModeGroup = this.mActiveSfDisplayMode.group;
                            this.mActiveRenderFrameRate = f;
                        } else if (findSfDisplayModeIdLocked(i7, this.mDefaultModeGroup) < 0) {
                            Slog.w(LocalDisplayAdapter.TAG, "Default display mode no longer available, using currently active mode as default.");
                            this.mDefaultModeId = displayModeRecord2.mMode.getModeId();
                            this.mDefaultModeGroup = this.mActiveSfDisplayMode.group;
                            this.mActiveRenderFrameRate = f;
                        }
                        if (this.mSupportedModes.indexOfKey(this.mDisplayModeSpecs.baseModeId) < 0) {
                            if (this.mDisplayModeSpecs.baseModeId != -1) {
                                Slog.w(LocalDisplayAdapter.TAG, "DisplayModeSpecs base mode no longer available, using currently active mode.");
                            }
                            this.mDisplayModeSpecs.baseModeId = displayModeRecord2.mMode.getModeId();
                            this.mDisplayModeSpecsInvalid = true;
                        }
                        Display.Mode mode = this.mUserPreferredMode;
                        if (mode != null) {
                            this.mUserPreferredModeId = findUserPreferredModeIdLocked(mode);
                        }
                        if (this.mSupportedModes.indexOfKey(this.mActiveModeId) < 0) {
                            if (this.mActiveModeId != -1) {
                                Slog.w(LocalDisplayAdapter.TAG, "Active display mode no longer available, reverting to default mode.");
                            }
                            this.mActiveModeId = getPreferredModeId();
                        }
                        LocalDisplayAdapter.this.sendTraversalRequestLocked();
                        return true;
                    }
                }
            }
            z = false;
            i3 = this.mActiveModeId;
            if (i3 != -1) {
            }
            z2 = false;
            f2 = this.mActiveRenderFrameRate;
            if (f2 > 0.0f) {
            }
            z3 = false;
            if (this.mDisplayModeSpecs.baseModeId != -1) {
            }
            if (arrayList.size() == this.mSupportedModes.size() || z5) {
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public DisplayDeviceConfig getDisplayDeviceConfig() {
            if (this.mDisplayDeviceConfig == null) {
                loadDisplayDeviceConfig();
            }
            return this.mDisplayDeviceConfig;
        }

        private int getPreferredModeId() {
            int i = this.mUserPreferredModeId;
            return i != -1 ? i : this.mDefaultModeId;
        }

        private int getLogicalDensity() {
            DensityMapping densityMapping = getDisplayDeviceConfig().getDensityMapping();
            if (densityMapping == null) {
                return (int) ((this.mStaticDisplayInfo.density * 160.0f) + 0.5d);
            }
            DisplayDeviceInfo displayDeviceInfo = this.mInfo;
            return densityMapping.getDensityForResolution(displayDeviceInfo.width, displayDeviceInfo.height);
        }

        private void loadDisplayDeviceConfig() {
            DisplayDeviceConfig create = DisplayDeviceConfig.create(LocalDisplayAdapter.this.getOverlayContext(), this.mPhysicalDisplayId, this.mIsFirstDisplay);
            this.mDisplayDeviceConfig = create;
            this.mBacklightAdapter.setForceSurfaceControl(create.hasQuirk(DisplayDeviceConfig.QUIRK_CAN_SET_BRIGHTNESS_VIA_HWC));
            this.mBacklightAdapter.setForceSurfaceControl(true);
        }

        private boolean updateStaticInfo(SurfaceControl.StaticDisplayInfo staticDisplayInfo) {
            if (Objects.equals(this.mStaticDisplayInfo, staticDisplayInfo)) {
                return false;
            }
            this.mStaticDisplayInfo = staticDisplayInfo;
            return true;
        }

        private boolean updateColorModesLocked(int[] iArr, int i) {
            if (iArr == null) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            boolean z = false;
            for (int i2 : iArr) {
                if (!this.mSupportedColorModes.contains(Integer.valueOf(i2))) {
                    z = true;
                }
                arrayList.add(Integer.valueOf(i2));
            }
            if (!(arrayList.size() != this.mSupportedColorModes.size() || z)) {
                return false;
            }
            this.mSupportedColorModes.clear();
            this.mSupportedColorModes.addAll(arrayList);
            Collections.sort(this.mSupportedColorModes);
            if (!this.mSupportedColorModes.contains(Integer.valueOf(this.mActiveColorMode))) {
                if (this.mActiveColorMode != 0) {
                    Slog.w(LocalDisplayAdapter.TAG, "Active color mode no longer available, reverting to default mode.");
                    this.mActiveColorMode = 0;
                } else if (!this.mSupportedColorModes.isEmpty()) {
                    Slog.e(LocalDisplayAdapter.TAG, "Default and active color mode is no longer available! Reverting to first available mode.");
                    this.mActiveColorMode = this.mSupportedColorModes.get(0).intValue();
                } else {
                    Slog.e(LocalDisplayAdapter.TAG, "No color modes available!");
                }
            }
            return true;
        }

        private boolean updateHdrCapabilitiesLocked(Display.HdrCapabilities hdrCapabilities) {
            if (Objects.equals(this.mHdrCapabilities, hdrCapabilities)) {
                return false;
            }
            this.mHdrCapabilities = hdrCapabilities;
            return true;
        }

        private boolean updateAllmSupport(boolean z) {
            if (this.mAllmSupported == z) {
                return false;
            }
            this.mAllmSupported = z;
            return true;
        }

        private boolean updateGameContentTypeSupport(boolean z) {
            if (this.mGameContentTypeSupported == z) {
                return false;
            }
            this.mGameContentTypeSupported = z;
            return true;
        }

        private SurfaceControl.DisplayMode getModeById(SurfaceControl.DisplayMode[] displayModeArr, int i) {
            for (SurfaceControl.DisplayMode displayMode : displayModeArr) {
                if (displayMode.id == i) {
                    return displayMode;
                }
            }
            Slog.e(LocalDisplayAdapter.TAG, "Can't find display mode with id " + i);
            return null;
        }

        private DisplayModeRecord findDisplayModeRecord(SurfaceControl.DisplayMode displayMode, List<Float> list) {
            for (int i = 0; i < this.mSupportedModes.size(); i++) {
                DisplayModeRecord valueAt = this.mSupportedModes.valueAt(i);
                if (valueAt.hasMatchingMode(displayMode) && refreshRatesEquals(list, valueAt.mMode.getAlternativeRefreshRates()) && LocalDisplayAdapter.this.hdrTypesEqual(displayMode.supportedHdrTypes, valueAt.mMode.getSupportedHdrTypes())) {
                    return valueAt;
                }
            }
            return null;
        }

        private boolean refreshRatesEquals(List<Float> list, float[] fArr) {
            if (list.size() != fArr.length) {
                return false;
            }
            for (int i = 0; i < list.size(); i++) {
                if (Float.floatToIntBits(list.get(i).floatValue()) != Float.floatToIntBits(fArr[i])) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.android.server.display.DisplayDevice
        public void applyPendingDisplayDeviceInfoChangesLocked() {
            if (this.mHavePendingChanges) {
                this.mInfo = null;
                this.mHavePendingChanges = false;
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public DisplayDeviceInfo getDisplayDeviceInfoLocked() {
            if (this.mInfo == null) {
                DisplayDeviceInfo displayDeviceInfo = new DisplayDeviceInfo();
                this.mInfo = displayDeviceInfo;
                SurfaceControl.DisplayMode displayMode = this.mActiveSfDisplayMode;
                displayDeviceInfo.width = displayMode.width;
                displayDeviceInfo.height = displayMode.height;
                displayDeviceInfo.modeId = this.mActiveModeId;
                displayDeviceInfo.renderFrameRate = this.mActiveRenderFrameRate;
                displayDeviceInfo.defaultModeId = getPreferredModeId();
                this.mInfo.supportedModes = getDisplayModes(this.mSupportedModes);
                DisplayDeviceInfo displayDeviceInfo2 = this.mInfo;
                displayDeviceInfo2.colorMode = this.mActiveColorMode;
                displayDeviceInfo2.allmSupported = this.mAllmSupported;
                displayDeviceInfo2.gameContentTypeSupported = this.mGameContentTypeSupported;
                displayDeviceInfo2.supportedColorModes = new int[this.mSupportedColorModes.size()];
                for (int i = 0; i < this.mSupportedColorModes.size(); i++) {
                    this.mInfo.supportedColorModes[i] = this.mSupportedColorModes.get(i).intValue();
                }
                DisplayDeviceInfo displayDeviceInfo3 = this.mInfo;
                displayDeviceInfo3.hdrCapabilities = this.mHdrCapabilities;
                SurfaceControl.DisplayMode displayMode2 = this.mActiveSfDisplayMode;
                displayDeviceInfo3.appVsyncOffsetNanos = displayMode2.appVsyncOffsetNanos;
                displayDeviceInfo3.presentationDeadlineNanos = displayMode2.presentationDeadlineNanos;
                displayDeviceInfo3.state = this.mState;
                displayDeviceInfo3.committedState = this.mCommittedState;
                displayDeviceInfo3.uniqueId = getUniqueId();
                DisplayAddress.Physical fromPhysicalDisplayId = DisplayAddress.fromPhysicalDisplayId(this.mPhysicalDisplayId);
                DisplayDeviceInfo displayDeviceInfo4 = this.mInfo;
                displayDeviceInfo4.address = fromPhysicalDisplayId;
                displayDeviceInfo4.densityDpi = getLogicalDensity();
                LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setDisplayInfoDpi(this.mInfo, this.mPhysicalDisplayId);
                DisplayDeviceInfo displayDeviceInfo5 = this.mInfo;
                SurfaceControl.DisplayMode displayMode3 = this.mActiveSfDisplayMode;
                displayDeviceInfo5.xDpi = displayMode3.xDpi;
                displayDeviceInfo5.yDpi = displayMode3.yDpi;
                SurfaceControl.StaticDisplayInfo staticDisplayInfo = this.mStaticDisplayInfo;
                displayDeviceInfo5.deviceProductInfo = staticDisplayInfo.deviceProductInfo;
                if (staticDisplayInfo.secure) {
                    displayDeviceInfo5.flags = 12;
                }
                Resources resources = LocalDisplayAdapter.this.getOverlayContext().getResources();
                DisplayAddress.Physical physical = this.mInfo.address;
                boolean z = physical != null && (physical.getPort() & 128) == 128;
                DisplayDeviceInfo displayDeviceInfo6 = this.mInfo;
                displayDeviceInfo6.flags |= 1;
                if (this.mIsFirstDisplay) {
                    if (resources.getBoolean(17891754) || (Build.IS_EMULATOR && SystemProperties.getBoolean(LocalDisplayAdapter.PROPERTY_EMULATOR_CIRCULAR, false))) {
                        this.mInfo.flags |= 256;
                    }
                } else if (z) {
                    displayDeviceInfo6.type = 1;
                    displayDeviceInfo6.touch = 1;
                    displayDeviceInfo6.name = LocalDisplayAdapter.this.getContext().getResources().getString(R.string.fcError);
                    this.mInfo.flags |= 2;
                    if (SystemProperties.getBoolean("vendor.display.builtin_presentation", false)) {
                        this.mInfo.flags |= 64;
                    }
                    if (!SystemProperties.getBoolean("vendor.display.builtin_mirroring", false)) {
                        this.mInfo.flags |= 128;
                    }
                    Slog.d(LocalDisplayAdapter.TAG, "densityDpi=" + this.mInfo.densityDpi + " smallWidth=" + SystemProperties.getInt("vendor.display.smallest_width", 360) + " width=" + this.mInfo.width + " activeMode=" + this.mActiveSfDisplayMode);
                } else {
                    if (!resources.getBoolean(17891745)) {
                        this.mInfo.flags |= 128;
                    }
                    if (isDisplayPrivate(fromPhysicalDisplayId)) {
                        this.mInfo.flags |= 16;
                    }
                }
                if (DisplayCutout.getMaskBuiltInDisplayCutout(resources, this.mInfo.uniqueId)) {
                    this.mInfo.flags |= 2048;
                }
                Display.Mode maximumResolutionDisplayMode = DisplayUtils.getMaximumResolutionDisplayMode(this.mInfo.supportedModes);
                int physicalWidth = maximumResolutionDisplayMode == null ? this.mInfo.width : maximumResolutionDisplayMode.getPhysicalWidth();
                int physicalHeight = maximumResolutionDisplayMode == null ? this.mInfo.height : maximumResolutionDisplayMode.getPhysicalHeight();
                DisplayDeviceInfo displayDeviceInfo7 = this.mInfo;
                int i2 = physicalWidth;
                int i3 = physicalHeight;
                displayDeviceInfo7.displayCutout = DisplayCutout.fromResourcesRectApproximation(resources, displayDeviceInfo7.uniqueId, i2, i3, displayDeviceInfo7.width, displayDeviceInfo7.height);
                DisplayDeviceInfo displayDeviceInfo8 = this.mInfo;
                displayDeviceInfo8.roundedCorners = RoundedCorners.fromResources(resources, displayDeviceInfo8.uniqueId, i2, i3, displayDeviceInfo8.width, displayDeviceInfo8.height);
                DisplayDeviceInfo displayDeviceInfo9 = this.mInfo;
                displayDeviceInfo9.installOrientation = this.mStaticDisplayInfo.installOrientation;
                displayDeviceInfo9.displayShape = DisplayShape.fromResources(resources, displayDeviceInfo9.uniqueId, i2, i3, displayDeviceInfo9.width, displayDeviceInfo9.height);
                this.mInfo.name = getDisplayDeviceConfig().getName();
                if (this.mStaticDisplayInfo.isInternal) {
                    DisplayDeviceInfo displayDeviceInfo10 = this.mInfo;
                    displayDeviceInfo10.type = 1;
                    displayDeviceInfo10.touch = 1;
                    displayDeviceInfo10.flags |= 2;
                    if (displayDeviceInfo10.name == null) {
                        displayDeviceInfo10.name = resources.getString(R.string.fcError);
                    }
                    int secondaryLcdDensity = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getSecondaryLcdDensity();
                    if (!this.mIsFirstDisplay && secondaryLcdDensity != 0) {
                        Slog.d(LocalDisplayAdapter.TAG, "secondary lcd density override " + this.mInfo.densityDpi + "->" + secondaryLcdDensity);
                        this.mInfo.densityDpi = secondaryLcdDensity;
                    }
                } else {
                    DisplayDeviceInfo displayDeviceInfo11 = this.mInfo;
                    displayDeviceInfo11.type = 2;
                    displayDeviceInfo11.touch = 2;
                    displayDeviceInfo11.flags |= 64;
                    if (displayDeviceInfo11.name == null) {
                        displayDeviceInfo11.name = LocalDisplayAdapter.this.getContext().getResources().getString(R.string.fileSizeSuffix);
                    }
                }
                DisplayDeviceInfo displayDeviceInfo12 = this.mInfo;
                displayDeviceInfo12.frameRateOverrides = this.mFrameRateOverrides;
                displayDeviceInfo12.flags |= 8192;
                displayDeviceInfo12.brightnessMinimum = 0.0f;
                displayDeviceInfo12.brightnessMaximum = 1.0f;
                displayDeviceInfo12.brightnessDefault = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getDefaultDisplayBrightness(this.mPhysicalDisplayId);
                DisplayDeviceInfo displayDeviceInfo13 = this.mInfo;
                displayDeviceInfo13.backlightType = this.mCurrentBacklightType;
                displayDeviceInfo13.hdrSdrRatio = this.mCurrentHdrSdrRatio;
            }
            return this.mInfo;
        }

        @Override // com.android.server.display.DisplayDevice
        public Runnable requestDisplayStateLocked(final int i, final float f, final float f2) {
            boolean z = this.mState != i;
            boolean z2 = (BrightnessSynchronizer.floatEquals(this.mBrightnessState, f) && BrightnessSynchronizer.floatEquals(this.mSdrBrightnessState, f2)) ? false : true;
            if (!z && !z2) {
                return null;
            }
            final long j = this.mPhysicalDisplayId;
            final IBinder displayTokenLocked = getDisplayTokenLocked();
            final int i2 = this.mState;
            if (z) {
                this.mState = i;
                IDisplayDeviceExt extImpl = getExtImpl();
                if (i == 2) {
                    extImpl.updatePowerModeChanged(true);
                }
                updateDeviceInfoLocked();
            }
            final boolean z3 = z2;
            return new Runnable() { // from class: com.android.server.display.LocalDisplayAdapter.LocalDisplayDevice.1
                @Override // java.lang.Runnable
                public void run() {
                    int i3;
                    int i4;
                    int i5;
                    int i6 = i2;
                    if (Display.isSuspendedState(i6) || i2 == 0) {
                        if (!Display.isSuspendedState(i)) {
                            setDisplayState(i);
                            i6 = i;
                        } else {
                            int i7 = i;
                            if (i7 == 4 || (i3 = i2) == 4) {
                                i6 = 3;
                                setDisplayState(3);
                            } else if (i7 == 6 || i3 == 6) {
                                i6 = 2;
                                setDisplayState(2);
                            } else if (i3 != 0) {
                                return;
                            }
                        }
                    }
                    boolean isLongTakeAodToOn = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().isLongTakeAodToOn(i6, i, LocalDisplayDevice.this.mPhysicalDisplayId);
                    if (isLongTakeAodToOn && (i5 = i) != i6) {
                        setDisplayState(i5);
                    }
                    if (z3) {
                        setDisplayBrightness(f, f2);
                        LocalDisplayDevice.this.mBrightnessState = f;
                        LocalDisplayDevice.this.mSdrBrightnessState = f2;
                    }
                    if (isLongTakeAodToOn || (i4 = i) == i6) {
                        return;
                    }
                    setDisplayState(i4);
                }

                private void setDisplayState(int i3) {
                    if (LocalDisplayAdapter.DEBUG || LocalDisplayAdapter.MTK_DEBUG) {
                        Slog.d(LocalDisplayAdapter.TAG, "setDisplayState(id=" + j + ", state=" + Display.stateToString(i3) + ")");
                    }
                    if (LocalDisplayDevice.this.mSidekickActive) {
                        Trace.traceBegin(131072L, "SidekickInternal#endDisplayControl");
                        try {
                            LocalDisplayDevice.this.mSidekickInternal.endDisplayControl();
                            Trace.traceEnd(131072L);
                            LocalDisplayDevice.this.mSidekickActive = false;
                        } finally {
                        }
                    }
                    int powerModeForState = LocalDisplayAdapter.getPowerModeForState(i3);
                    Trace.traceBegin(131072L, "setDisplayState(id=" + j + ", state=" + Display.stateToString(i3) + ")");
                    LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setSwitchingTrackerPowerEventLog(i3, true);
                    try {
                        LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().requestDisplayState(LocalDisplayDevice.this.mIsFirstDisplay, i3);
                        Slog.d(LocalDisplayAdapter.TAG, "setDisplayState(id=" + j + " isFirstDisplay=" + LocalDisplayDevice.this.mIsFirstDisplay + " state changed:" + Display.stateToString(i2) + "->" + Display.stateToString(i3) + ") t-Name=" + Thread.currentThread().getName());
                        LocalDisplayAdapter.this.mSurfaceControlProxy.setDisplayPowerMode(displayTokenLocked, powerModeForState);
                        LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setDisplayPowerModeFinished(LocalDisplayDevice.this.mIsFirstDisplay, i3);
                        Trace.traceCounter(131072L, "DisplayPowerMode", powerModeForState);
                        Trace.traceEnd(131072L);
                        LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setSwitchingTrackerPowerEventLog(i3, false);
                        setCommittedState(i3);
                        if (!Display.isSuspendedState(i3) || i3 == 1 || LocalDisplayDevice.this.mSidekickInternal == null || LocalDisplayDevice.this.mSidekickActive) {
                            return;
                        }
                        Trace.traceBegin(131072L, "SidekickInternal#startDisplayControl");
                        try {
                            LocalDisplayDevice localDisplayDevice = LocalDisplayDevice.this;
                            localDisplayDevice.mSidekickActive = localDisplayDevice.mSidekickInternal.startDisplayControl(i3);
                        } finally {
                        }
                    } catch (Throwable th) {
                        Trace.traceEnd(131072L);
                        LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setSwitchingTrackerPowerEventLog(i3, false);
                        throw th;
                    }
                }

                private void setCommittedState(int i3) {
                    synchronized (LocalDisplayAdapter.this.getSyncRoot()) {
                        LocalDisplayDevice.this.mCommittedState = i3;
                        LocalDisplayDevice.this.updateDeviceInfoLocked();
                    }
                }

                /* JADX WARN: Can't wrap try/catch for region: R(18:5|(1:7)|8|9|10|(1:12)|13|(7:(16:22|23|24|(4:26|27|(1:29)(1:59)|30)(2:61|(1:63)(1:64))|31|(1:33)|34|(8:41|42|43|44|45|(1:49)|51|52)|58|42|43|44|45|(2:47|49)|51|52)|(10:36|38|41|42|43|44|45|(0)|51|52)|44|45|(0)|51|52)|67|23|24|(0)(0)|31|(0)|34|58|42|43) */
                /* JADX WARN: Code restructure failed: missing block: B:65:0x03a4, code lost:
                
                    r0 = th;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:66:0x03a5, code lost:
                
                    r6 = 131072;
                 */
                /* JADX WARN: Removed duplicated region for block: B:26:0x0200 A[Catch: all -> 0x03a9, TRY_LEAVE, TryCatch #2 {all -> 0x03a9, blocks: (B:10:0x0087, B:12:0x00b9, B:13:0x00dd, B:15:0x0160, B:17:0x0166, B:19:0x016c, B:22:0x0175, B:23:0x01b0, B:26:0x0200, B:67:0x019c), top: B:9:0x0087 }] */
                /* JADX WARN: Removed duplicated region for block: B:33:0x02bb A[Catch: all -> 0x03a4, TryCatch #1 {all -> 0x03a4, blocks: (B:27:0x020c, B:29:0x0214, B:31:0x02b3, B:33:0x02bb, B:34:0x02cb, B:36:0x02d6, B:38:0x02dc, B:42:0x034e, B:58:0x02e8, B:59:0x022c, B:61:0x0251, B:63:0x0265, B:64:0x028a), top: B:24:0x01fe }] */
                /* JADX WARN: Removed duplicated region for block: B:47:0x0397  */
                /* JADX WARN: Removed duplicated region for block: B:61:0x0251 A[Catch: all -> 0x03a4, TryCatch #1 {all -> 0x03a4, blocks: (B:27:0x020c, B:29:0x0214, B:31:0x02b3, B:33:0x02bb, B:34:0x02cb, B:36:0x02d6, B:38:0x02dc, B:42:0x034e, B:58:0x02e8, B:59:0x022c, B:61:0x0251, B:63:0x0265, B:64:0x028a), top: B:24:0x01fe }] */
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                private void setDisplayBrightness(float f3, float f4) {
                    long j2;
                    float brightnessToNits;
                    float f5;
                    float maxBrightness;
                    float totalDisplayBrightness;
                    float aodBrightness;
                    float enhanceDolbyOriginNit;
                    float enhanceDolbyScale;
                    int edrType;
                    float f6;
                    float brightnessToNits2;
                    float f7;
                    boolean z4;
                    float f8;
                    float f9;
                    int i3;
                    float f10;
                    float f11;
                    float f12;
                    float f13;
                    float f14;
                    if (Float.isNaN(f3) || Float.isNaN(f4)) {
                        return;
                    }
                    float powerOnRealTimeBrightness = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getPowerOnRealTimeBrightness(LocalDisplayDevice.this.mIsFirstDisplay, f3);
                    float round = Math.round(f4);
                    if (LocalDisplayAdapter.DEBUG) {
                        Slog.d(LocalDisplayAdapter.TAG, "setDisplayBrightness(id=" + j + ", brightnessState=" + powerOnRealTimeBrightness + ", sdrBrightnessState=" + round + ")");
                    }
                    Trace.traceBegin(131072L, "setDisplayBrightness(id=" + j + ", brightnessState=" + powerOnRealTimeBrightness + ", sdrBrightnessState=" + round + ")");
                    try {
                        brightnessToNits = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().brightnessToNits(powerOnRealTimeBrightness);
                        float brightnessToNits3 = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().brightnessToNits(round);
                        if (LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().hasRemapDisable()) {
                            brightnessToNits = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().brightnessToNits(j, powerOnRealTimeBrightness);
                            brightnessToNits3 = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().brightnessToNits(j, round);
                        }
                        f5 = brightnessToNits3;
                        maxBrightness = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getMaxBrightness(j);
                        totalDisplayBrightness = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getTotalDisplayBrightness(j);
                        aodBrightness = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getAodBrightness();
                        enhanceDolbyOriginNit = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getEnhanceDolbyOriginNit(j, powerOnRealTimeBrightness, round);
                        enhanceDolbyScale = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getEnhanceDolbyScale(brightnessToNits, enhanceDolbyOriginNit, j);
                        edrType = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getEdrType();
                        LocalDisplayDevice.this.mCurrentBacklightType = edrType;
                        LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().setCurrentEdrEnhanceScale(enhanceDolbyScale);
                    } catch (Throwable th) {
                        th = th;
                        j2 = 131072;
                    }
                    try {
                        if (!BrightnessSynchronizer.floatEquals(powerOnRealTimeBrightness, maxBrightness) && !BrightnessSynchronizer.floatEquals(powerOnRealTimeBrightness, totalDisplayBrightness) && !BrightnessSynchronizer.floatEquals(powerOnRealTimeBrightness, aodBrightness) && !BrightnessSynchronizer.floatEquals(powerOnRealTimeBrightness, -1.0f)) {
                            round = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getBrightnessFromNit(j, f5);
                            brightnessToNits2 = brightnessToNits;
                            f6 = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getBrightnessFromNit(j, brightnessToNits);
                            float brightnessToBacklight = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().brightnessToBacklight(j, f6);
                            float brightnessToBacklight2 = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().brightnessToBacklight(j, round);
                            float brightnessToColor = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().brightnessToColor(j, f6);
                            boolean isAnimating = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().isAnimating(j);
                            if (LocalDisplayDevice.this.mDcThreshold != 0) {
                                if (!LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().needCaculateScale(j)) {
                                    LocalDisplayDevice.this.mBacklightAdapter.setBacklight(brightnessToBacklight, brightnessToNits2, brightnessToBacklight, brightnessToNits2, brightnessToColor, enhanceDolbyScale, enhanceDolbyOriginNit, edrType, isAnimating);
                                } else {
                                    LocalDisplayDevice.this.mBacklightAdapter.setBacklight(brightnessToBacklight2, f5, brightnessToBacklight, brightnessToNits2, brightnessToColor, enhanceDolbyScale, enhanceDolbyOriginNit, edrType, isAnimating);
                                }
                                f7 = round;
                                z4 = isAnimating;
                                f8 = f6;
                                f9 = brightnessToNits2;
                                i3 = edrType;
                                f10 = enhanceDolbyScale;
                                f11 = enhanceDolbyOriginNit;
                                f12 = f5;
                            } else if (!LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().needCaculateScale(j)) {
                                z4 = isAnimating;
                                i3 = edrType;
                                f10 = enhanceDolbyScale;
                                f11 = enhanceDolbyOriginNit;
                                f12 = f5;
                                f9 = brightnessToNits2;
                                f7 = round;
                                f8 = f6;
                                LocalDisplayDevice.this.mBacklightAdapter.setBacklight(brightnessToBacklight, brightnessToNits2, brightnessToBacklight, brightnessToNits2, brightnessToColor, enhanceDolbyScale, enhanceDolbyOriginNit, powerOnRealTimeBrightness, i3, z4);
                            } else {
                                f7 = round;
                                z4 = isAnimating;
                                f8 = f6;
                                f9 = brightnessToNits2;
                                i3 = edrType;
                                f10 = enhanceDolbyScale;
                                f11 = enhanceDolbyOriginNit;
                                f12 = f5;
                                LocalDisplayDevice.this.mBacklightAdapter.setBacklight(brightnessToBacklight2, f12, brightnessToBacklight, f9, brightnessToColor, f10, f11, powerOnRealTimeBrightness, i3, z4);
                            }
                            if (LocalDisplayDevice.this.mIsFirstDisplay) {
                                LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().updateDCLayerState((int) powerOnRealTimeBrightness);
                            }
                            LocalDisplayDevice.this.setIrisBrightness(powerOnRealTimeBrightness);
                            if (!LocalDisplayAdapter.DEBUG && !LocalDisplayAdapter.MTK_DEBUG && !LocalDisplayAdapter.PANIC_DEBUG) {
                                f14 = f12;
                                f13 = f9;
                                float f15 = f7;
                                LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().updateScreenBrightnessProvider(f8, (int) powerOnRealTimeBrightness, f13, j, LocalDisplayDevice.this.mIsFirstDisplay, (int) f15, i2, LocalDisplayDevice.this.mState);
                                j2 = 131072;
                                Trace.traceCounter(131072L, "ScreenBrightness", BrightnessSynchronizer.brightnessFloatToInt(powerOnRealTimeBrightness));
                                Trace.traceCounter(131072L, "SdrScreenBrightness", BrightnessSynchronizer.brightnessFloatToInt(f15));
                                if (f13 != 0.0f && f14 != 0.0f) {
                                    handleHdrSdrNitsChanged(f13, f14);
                                }
                                Trace.traceEnd(131072L);
                                return;
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append("brightness color = ");
                            sb.append(f8);
                            sb.append(", brightness nit = ");
                            f13 = f9;
                            sb.append(f13);
                            sb.append(", sdrNits = ");
                            f14 = f12;
                            sb.append(f14);
                            sb.append(" id=");
                            sb.append(j);
                            sb.append(", mDcThreshold=");
                            sb.append(LocalDisplayDevice.this.mDcThreshold);
                            sb.append(", dolbyOriginNit = ");
                            sb.append(f11);
                            sb.append(", scale = ");
                            sb.append(f10);
                            sb.append(", edrType = ");
                            sb.append(i3);
                            sb.append(", isAnimating = ");
                            sb.append(z4);
                            Slog.d(LocalDisplayAdapter.TAG, sb.toString());
                            float f152 = f7;
                            LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().updateScreenBrightnessProvider(f8, (int) powerOnRealTimeBrightness, f13, j, LocalDisplayDevice.this.mIsFirstDisplay, (int) f152, i2, LocalDisplayDevice.this.mState);
                            j2 = 131072;
                            Trace.traceCounter(131072L, "ScreenBrightness", BrightnessSynchronizer.brightnessFloatToInt(powerOnRealTimeBrightness));
                            Trace.traceCounter(131072L, "SdrScreenBrightness", BrightnessSynchronizer.brightnessFloatToInt(f152));
                            if (f13 != 0.0f) {
                                handleHdrSdrNitsChanged(f13, f14);
                            }
                            Trace.traceEnd(131072L);
                            return;
                        }
                        if (!LocalDisplayAdapter.DEBUG) {
                            f14 = f12;
                            f13 = f9;
                            float f1522 = f7;
                            LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().updateScreenBrightnessProvider(f8, (int) powerOnRealTimeBrightness, f13, j, LocalDisplayDevice.this.mIsFirstDisplay, (int) f1522, i2, LocalDisplayDevice.this.mState);
                            j2 = 131072;
                            Trace.traceCounter(131072L, "ScreenBrightness", BrightnessSynchronizer.brightnessFloatToInt(powerOnRealTimeBrightness));
                            Trace.traceCounter(131072L, "SdrScreenBrightness", BrightnessSynchronizer.brightnessFloatToInt(f1522));
                            if (f13 != 0.0f) {
                            }
                            Trace.traceEnd(131072L);
                            return;
                        }
                        Trace.traceCounter(131072L, "ScreenBrightness", BrightnessSynchronizer.brightnessFloatToInt(powerOnRealTimeBrightness));
                        Trace.traceCounter(131072L, "SdrScreenBrightness", BrightnessSynchronizer.brightnessFloatToInt(f1522));
                        if (f13 != 0.0f) {
                        }
                        Trace.traceEnd(131072L);
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        Trace.traceEnd(j2);
                        throw th;
                    }
                    f6 = powerOnRealTimeBrightness;
                    brightnessToNits2 = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().brightnessToNits(j, powerOnRealTimeBrightness);
                    float brightnessToBacklight3 = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().brightnessToBacklight(j, f6);
                    float brightnessToBacklight22 = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().brightnessToBacklight(j, round);
                    float brightnessToColor2 = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().brightnessToColor(j, f6);
                    boolean isAnimating2 = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().isAnimating(j);
                    if (LocalDisplayDevice.this.mDcThreshold != 0) {
                    }
                    if (LocalDisplayDevice.this.mIsFirstDisplay) {
                    }
                    LocalDisplayDevice.this.setIrisBrightness(powerOnRealTimeBrightness);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("brightness color = ");
                    sb2.append(f8);
                    sb2.append(", brightness nit = ");
                    f13 = f9;
                    sb2.append(f13);
                    sb2.append(", sdrNits = ");
                    f14 = f12;
                    sb2.append(f14);
                    sb2.append(" id=");
                    sb2.append(j);
                    sb2.append(", mDcThreshold=");
                    sb2.append(LocalDisplayDevice.this.mDcThreshold);
                    sb2.append(", dolbyOriginNit = ");
                    sb2.append(f11);
                    sb2.append(", scale = ");
                    sb2.append(f10);
                    sb2.append(", edrType = ");
                    sb2.append(i3);
                    sb2.append(", isAnimating = ");
                    sb2.append(z4);
                    Slog.d(LocalDisplayAdapter.TAG, sb2.toString());
                    float f15222 = f7;
                    LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().updateScreenBrightnessProvider(f8, (int) powerOnRealTimeBrightness, f13, j, LocalDisplayDevice.this.mIsFirstDisplay, (int) f15222, i2, LocalDisplayDevice.this.mState);
                    j2 = 131072;
                }

                private float brightnessToBacklight(float f3) {
                    if (BrightnessSynchronizer.floatEquals(f3, -1.0f)) {
                        return -1.0f;
                    }
                    return LocalDisplayDevice.this.getDisplayDeviceConfig().getBacklightFromBrightness(f3);
                }

                private float backlightToNits(float f3) {
                    return LocalDisplayDevice.this.getDisplayDeviceConfig().getNitsFromBacklight(f3);
                }

                void handleHdrSdrNitsChanged(float f3, float f4) {
                    float max = (f3 == -1.0f || f4 == -1.0f) ? Float.NaN : Math.max(1.0f, f3 / f4);
                    if (BrightnessSynchronizer.floatEquals(LocalDisplayDevice.this.mCurrentHdrSdrRatio, max)) {
                        return;
                    }
                    LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().notifyBacklightAnimFinished(max);
                    synchronized (LocalDisplayAdapter.this.getSyncRoot()) {
                        LocalDisplayDevice.this.mCurrentHdrSdrRatio = max;
                        LocalDisplayDevice.this.updateDeviceInfoLocked();
                    }
                }
            };
        }

        @Override // com.android.server.display.DisplayDevice
        public void setUserPreferredDisplayModeLocked(Display.Mode mode) {
            Display.Mode findMode;
            int i;
            int preferredModeId = getPreferredModeId();
            this.mUserPreferredMode = mode;
            if (mode == null && (i = this.mSystemPreferredModeId) != -1) {
                this.mDefaultModeId = i;
            }
            if (mode != null && ((mode.isRefreshRateSet() || mode.isResolutionSet()) && (findMode = findMode(mode.getPhysicalWidth(), mode.getPhysicalHeight(), mode.getRefreshRate())) != null)) {
                this.mUserPreferredMode = findMode;
            }
            this.mUserPreferredModeId = findUserPreferredModeIdLocked(this.mUserPreferredMode);
            if (preferredModeId == getPreferredModeId()) {
                return;
            }
            updateDeviceInfoLocked();
            if (LocalDisplayAdapter.this.mIsBootDisplayModeSupported) {
                if (this.mUserPreferredModeId == -1) {
                    LocalDisplayAdapter.this.mSurfaceControlProxy.clearBootDisplayMode(getDisplayTokenLocked());
                } else {
                    LocalDisplayAdapter.this.mSurfaceControlProxy.setBootDisplayMode(getDisplayTokenLocked(), findSfDisplayModeIdLocked(this.mUserPreferredMode.getModeId(), this.mDefaultModeGroup));
                }
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public Display.Mode getUserPreferredDisplayModeLocked() {
            return this.mUserPreferredMode;
        }

        @Override // com.android.server.display.DisplayDevice
        public Display.Mode getSystemPreferredDisplayModeLocked() {
            return findMode(this.mSystemPreferredModeId);
        }

        @Override // com.android.server.display.DisplayDevice
        public void setRequestedColorModeLocked(int i) {
            requestColorModeLocked(i);
        }

        @Override // com.android.server.display.DisplayDevice
        public void setDesiredDisplayModeSpecsLocked(DisplayModeDirector.DesiredDisplayModeSpecs desiredDisplayModeSpecs) {
            int i = desiredDisplayModeSpecs.baseModeId;
            if (i == 0) {
                return;
            }
            int findSfDisplayModeIdLocked = findSfDisplayModeIdLocked(i, this.mDefaultModeGroup);
            if (findSfDisplayModeIdLocked < 0) {
                Slog.w(LocalDisplayAdapter.TAG, "Ignoring request for invalid base mode id " + desiredDisplayModeSpecs.baseModeId);
                updateDeviceInfoLocked();
                return;
            }
            int findDisplayModeIdByPolicy = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().findDisplayModeIdByPolicy(this.mIsFirstDisplay, desiredDisplayModeSpecs.vrrPolicy, 0, findSfDisplayModeIdLocked);
            if (this.mDisplayModeSpecsInvalid || !desiredDisplayModeSpecs.equals(this.mDisplayModeSpecs)) {
                this.mDisplayModeSpecsInvalid = false;
                this.mDisplayModeSpecs.copyFrom(desiredDisplayModeSpecs);
                Handler oPlusRefreshRateHandler = LocalDisplayAdapter.this.mLdaWrapper.getExtImpl().getOPlusRefreshRateHandler(LocalDisplayAdapter.this.getHandler());
                TriConsumer triConsumer = new TriConsumer() { // from class: com.android.server.display.LocalDisplayAdapter$LocalDisplayDevice$$ExternalSyntheticLambda1
                    public final void accept(Object obj, Object obj2, Object obj3) {
                        ((LocalDisplayAdapter.LocalDisplayDevice) obj).setDesiredDisplayModeSpecsAsync((IBinder) obj2, (SurfaceControl.DesiredDisplayModeSpecs) obj3);
                    }
                };
                IBinder displayTokenLocked = getDisplayTokenLocked();
                DisplayModeDirector.DesiredDisplayModeSpecs desiredDisplayModeSpecs2 = this.mDisplayModeSpecs;
                oPlusRefreshRateHandler.sendMessage(PooledLambda.obtainMessage(triConsumer, this, displayTokenLocked, new SurfaceControl.DesiredDisplayModeSpecs(findDisplayModeIdByPolicy, desiredDisplayModeSpecs2.allowGroupSwitching, desiredDisplayModeSpecs2.primary, desiredDisplayModeSpecs2.appRequest)));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDesiredDisplayModeSpecsAsync(IBinder iBinder, SurfaceControl.DesiredDisplayModeSpecs desiredDisplayModeSpecs) {
            Slog.d(LocalDisplayAdapter.TAG, "setDesiredDisplayModeSpecsAsync: display unique id = " + getUniqueId() + ", display mode specs = " + desiredDisplayModeSpecs);
            LocalDisplayAdapter.this.mSurfaceControlProxy.setDesiredDisplayModeSpecs(iBinder, desiredDisplayModeSpecs);
        }

        @Override // com.android.server.display.DisplayDevice
        public void onOverlayChangedLocked() {
            updateDeviceInfoLocked();
        }

        public void onActiveDisplayModeChangedLocked(int i, float f) {
            if (updateActiveModeLocked(i, f)) {
                updateDeviceInfoLocked();
            }
        }

        public void onFrameRateOverridesChanged(DisplayEventReceiver.FrameRateOverride[] frameRateOverrideArr) {
            if (updateFrameRateOverridesLocked(frameRateOverrideArr)) {
                updateDeviceInfoLocked();
            }
        }

        public boolean updateActiveModeLocked(int i, float f) {
            if (this.mActiveSfDisplayMode.id == i && this.mActiveRenderFrameRate == f) {
                return false;
            }
            this.mActiveSfDisplayMode = getModeById(this.mSfDisplayModes, i);
            int findMatchingModeIdLocked = findMatchingModeIdLocked(i);
            this.mActiveModeId = findMatchingModeIdLocked;
            if (findMatchingModeIdLocked == -1) {
                Slog.w(LocalDisplayAdapter.TAG, "In unknown mode after setting allowed modes, activeModeId=" + i);
            }
            this.mActiveRenderFrameRate = f;
            return true;
        }

        public boolean updateFrameRateOverridesLocked(DisplayEventReceiver.FrameRateOverride[] frameRateOverrideArr) {
            if (Arrays.equals(frameRateOverrideArr, this.mFrameRateOverrides)) {
                return false;
            }
            this.mFrameRateOverrides = frameRateOverrideArr;
            return true;
        }

        public void requestColorModeLocked(int i) {
            if (this.mActiveColorMode == i) {
                return;
            }
            if (!this.mSupportedColorModes.contains(Integer.valueOf(i))) {
                Slog.w(LocalDisplayAdapter.TAG, "Unable to find color mode " + i + ", ignoring request.");
                return;
            }
            this.mActiveColorMode = i;
            LocalDisplayAdapter.this.getHandler().sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.display.LocalDisplayAdapter$LocalDisplayDevice$$ExternalSyntheticLambda0
                public final void accept(Object obj, Object obj2, Object obj3) {
                    ((LocalDisplayAdapter.LocalDisplayDevice) obj).requestColorModeAsync((IBinder) obj2, ((Integer) obj3).intValue());
                }
            }, this, getDisplayTokenLocked(), Integer.valueOf(i)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void requestColorModeAsync(IBinder iBinder, int i) {
            LocalDisplayAdapter.this.mSurfaceControlProxy.setActiveColorMode(iBinder, i);
            synchronized (LocalDisplayAdapter.this.getSyncRoot()) {
                updateDeviceInfoLocked();
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public void setAutoLowLatencyModeLocked(boolean z) {
            if (this.mAllmRequested == z) {
                return;
            }
            this.mAllmRequested = z;
            if (!this.mAllmSupported) {
                Slog.d(LocalDisplayAdapter.TAG, "Unable to set ALLM because the connected display does not support ALLM.");
            } else {
                LocalDisplayAdapter.this.mSurfaceControlProxy.setAutoLowLatencyMode(getDisplayTokenLocked(), z);
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public void setGameContentTypeLocked(boolean z) {
            if (this.mGameContentTypeRequested == z) {
                return;
            }
            this.mGameContentTypeRequested = z;
            LocalDisplayAdapter.this.mSurfaceControlProxy.setGameContentType(getDisplayTokenLocked(), z);
        }

        @Override // com.android.server.display.DisplayDevice
        public void dumpLocked(PrintWriter printWriter) {
            super.dumpLocked(printWriter);
            printWriter.println("mPhysicalDisplayId=" + this.mPhysicalDisplayId);
            printWriter.println("mDisplayModeSpecs={" + this.mDisplayModeSpecs + "}");
            StringBuilder sb = new StringBuilder();
            sb.append("mDisplayModeSpecsInvalid=");
            sb.append(this.mDisplayModeSpecsInvalid);
            printWriter.println(sb.toString());
            printWriter.println("mActiveModeId=" + this.mActiveModeId);
            printWriter.println("mActiveColorMode=" + this.mActiveColorMode);
            printWriter.println("mDefaultModeId=" + this.mDefaultModeId);
            printWriter.println("mUserPreferredModeId=" + this.mUserPreferredModeId);
            printWriter.println("mState=" + Display.stateToString(this.mState));
            printWriter.println("mCommittedState=" + Display.stateToString(this.mCommittedState));
            printWriter.println("mBrightnessState=" + this.mBrightnessState);
            printWriter.println("mBacklightAdapter=" + this.mBacklightAdapter);
            printWriter.println("mAllmSupported=" + this.mAllmSupported);
            printWriter.println("mAllmRequested=" + this.mAllmRequested);
            printWriter.println("mGameContentTypeSupported=" + this.mGameContentTypeSupported);
            printWriter.println("mGameContentTypeRequested=" + this.mGameContentTypeRequested);
            printWriter.println("mStaticDisplayInfo=" + this.mStaticDisplayInfo);
            printWriter.println("mSfDisplayModes=");
            for (SurfaceControl.DisplayMode displayMode : this.mSfDisplayModes) {
                printWriter.println("  " + displayMode);
            }
            printWriter.println("mActiveSfDisplayMode=" + this.mActiveSfDisplayMode);
            printWriter.println("mActiveRenderFrameRate=" + this.mActiveRenderFrameRate);
            printWriter.println("mSupportedModes=");
            for (int i = 0; i < this.mSupportedModes.size(); i++) {
                printWriter.println("  " + this.mSupportedModes.valueAt(i));
            }
            printWriter.println("mSupportedColorModes=" + this.mSupportedColorModes);
            printWriter.println("mDisplayDeviceConfig=" + this.mDisplayDeviceConfig);
        }

        private int findSfDisplayModeIdLocked(int i, int i2) {
            DisplayModeRecord displayModeRecord = this.mSupportedModes.get(i);
            if (displayModeRecord == null) {
                return -1;
            }
            int i3 = -1;
            for (SurfaceControl.DisplayMode displayMode : this.mSfDisplayModes) {
                if (displayModeRecord.hasMatchingMode(displayMode)) {
                    if (i3 == -1) {
                        i3 = displayMode.id;
                    }
                    if (displayMode.group == i2) {
                        return displayMode.id;
                    }
                }
            }
            return i3;
        }

        private Display.Mode findMode(int i) {
            for (int i2 = 0; i2 < this.mSupportedModes.size(); i2++) {
                Display.Mode mode = this.mSupportedModes.valueAt(i2).mMode;
                if (mode.getModeId() == i) {
                    return mode;
                }
            }
            return null;
        }

        private Display.Mode findMode(int i, int i2, float f) {
            for (int i3 = 0; i3 < this.mSupportedModes.size(); i3++) {
                Display.Mode mode = this.mSupportedModes.valueAt(i3).mMode;
                if (mode.matchesIfValid(i, i2, f)) {
                    return mode;
                }
            }
            return null;
        }

        private int findUserPreferredModeIdLocked(Display.Mode mode) {
            if (mode == null) {
                return -1;
            }
            for (int i = 0; i < this.mSupportedModes.size(); i++) {
                Display.Mode mode2 = this.mSupportedModes.valueAt(i).mMode;
                if (mode.matches(mode2.getPhysicalWidth(), mode2.getPhysicalHeight(), mode2.getRefreshRate())) {
                    return mode2.getModeId();
                }
            }
            return -1;
        }

        private int findMatchingModeIdLocked(int i) {
            SurfaceControl.DisplayMode modeById = getModeById(this.mSfDisplayModes, i);
            if (modeById == null) {
                Slog.e(LocalDisplayAdapter.TAG, "Invalid display mode ID " + i);
                return -1;
            }
            for (int i2 = 0; i2 < this.mSupportedModes.size(); i2++) {
                DisplayModeRecord valueAt = this.mSupportedModes.valueAt(i2);
                if (valueAt.hasMatchingMode(modeById)) {
                    return valueAt.mMode.getModeId();
                }
            }
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateDeviceInfoLocked() {
            this.mInfo = null;
            LocalDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 2);
        }

        private Display.Mode[] getDisplayModes(SparseArray<DisplayModeRecord> sparseArray) {
            int size = sparseArray.size();
            Display.Mode[] modeArr = new Display.Mode[size];
            for (int i = 0; i < size; i++) {
                modeArr[i] = sparseArray.valueAt(i).mMode;
            }
            return modeArr;
        }

        private boolean isDisplayPrivate(DisplayAddress.Physical physical) {
            int[] intArray;
            if (physical != null && (intArray = LocalDisplayAdapter.this.getOverlayContext().getResources().getIntArray(R.array.config_tether_wifi_regexs)) != null) {
                int port = physical.getPort();
                for (int i : intArray) {
                    if (i == port) {
                        return true;
                    }
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:11:0x0056  */
        /* JADX WARN: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void setIrisBrightness(float f) {
            boolean z;
            if (LocalDisplayAdapter.this.mIrisHal == null) {
                return;
            }
            int i = (int) f;
            IrisTable irisTable = IrisTable.LOWLEVEL;
            boolean z2 = true;
            if (LocalDisplayAdapter.this.mlast_level == -1) {
                LocalDisplayAdapter.this.mlast_level = i;
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                IrisTable irisBrightnessTable = getIrisBrightnessTable(i);
                if (irisBrightnessTable != getIrisBrightnessTable(LocalDisplayAdapter.this.mlast_level)) {
                    LocalDisplayAdapter.this.mlast_level = i;
                } else if ((irisBrightnessTable == IrisTable.LOWLEVEL || irisBrightnessTable == IrisTable.HIGHTLEVEL) && Math.abs(i - LocalDisplayAdapter.this.mlast_level) >= 12) {
                    LocalDisplayAdapter.this.mlast_level = i;
                }
                if (z2) {
                    return;
                }
                LocalDisplayAdapter.this.mIrisHal.irisConfigureSet(LocalDisplayAdapter.this.mIris514command, new int[]{i, LocalDisplayAdapter.this.mIris514ext});
                return;
            }
            z2 = z;
            if (z2) {
            }
        }

        private IrisTable getIrisBrightnessTable(int i) {
            IrisTable irisTable = IrisTable.LOWLEVEL;
            if (i < 2500) {
                return IrisTable.LOWLEVEL;
            }
            if (i > 2500 && i < 4334) {
                return IrisTable.MEDIUMLEVEL;
            }
            return IrisTable.HIGHTLEVEL;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hdrTypesEqual(int[] iArr, int[] iArr2) {
        int[] copyOf = Arrays.copyOf(iArr, iArr.length);
        Arrays.sort(copyOf);
        return Arrays.equals(copyOf, iArr2);
    }

    Context getOverlayContext() {
        if (this.mOverlayContext == null) {
            this.mOverlayContext = ActivityThread.currentActivityThread().getSystemUiContext();
        }
        return this.mOverlayContext;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class DisplayModeRecord {
        public final Display.Mode mMode;

        DisplayModeRecord(SurfaceControl.DisplayMode displayMode, float[] fArr) {
            this.mMode = DisplayAdapter.createMode(displayMode.width, displayMode.height, displayMode.refreshRate, fArr, displayMode.supportedHdrTypes);
        }

        public boolean hasMatchingMode(SurfaceControl.DisplayMode displayMode) {
            return this.mMode.getPhysicalWidth() == displayMode.width && this.mMode.getPhysicalHeight() == displayMode.height && Float.floatToIntBits(this.mMode.getRefreshRate()) == Float.floatToIntBits(displayMode.refreshRate);
        }

        public String toString() {
            return "DisplayModeRecord{mMode=" + this.mMode + "}";
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        private ProxyDisplayEventReceiver mReceiver;

        public void setDisplayEventListenerLocked(Looper looper, DisplayEventListener displayEventListener) {
            this.mReceiver = new ProxyDisplayEventReceiver(looper, displayEventListener);
        }

        public SurfaceControlProxy getSurfaceControlProxy() {
            return new SurfaceControlProxy();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ProxyDisplayEventReceiver extends DisplayEventReceiver {
        private final DisplayEventListener mListener;

        ProxyDisplayEventReceiver(Looper looper, DisplayEventListener displayEventListener) {
            super(looper, 0, 3);
            this.mListener = displayEventListener;
        }

        public void onHotplug(long j, long j2, boolean z) {
            this.mListener.onHotplug(j, j2, z);
        }

        public void onModeChanged(long j, long j2, int i, long j3) {
            this.mListener.onModeChanged(j, j2, i, j3);
        }

        public void onFrameRateOverridesChanged(long j, long j2, DisplayEventReceiver.FrameRateOverride[] frameRateOverrideArr) {
            this.mListener.onFrameRateOverridesChanged(j, j2, frameRateOverrideArr);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class LocalDisplayEventListener implements DisplayEventListener {
        private LocalDisplayEventListener() {
        }

        @Override // com.android.server.display.LocalDisplayAdapter.DisplayEventListener
        public void onHotplug(long j, long j2, boolean z) {
            if (LocalDisplayAdapter.DEBUG || LocalDisplayAdapter.PANIC_DEBUG) {
                Slog.d(LocalDisplayAdapter.TAG, "onHotplug connected=" + z + " id=" + j2);
            }
            synchronized (LocalDisplayAdapter.this.getSyncRoot()) {
                if (z) {
                    LocalDisplayAdapter.this.tryConnectDisplayLocked(j2);
                } else {
                    LocalDisplayAdapter.this.tryDisconnectDisplayLocked(j2);
                }
            }
        }

        @Override // com.android.server.display.LocalDisplayAdapter.DisplayEventListener
        public void onModeChanged(long j, long j2, int i, long j3) {
            if (LocalDisplayAdapter.DEBUG || LocalDisplayAdapter.PANIC_DEBUG) {
                Slog.d(LocalDisplayAdapter.TAG, "onModeChanged(timestampNanos=" + j + ", physicalDisplayId=" + j2 + ", modeId=" + i + ", renderPeriod=" + j3 + ")");
            }
            synchronized (LocalDisplayAdapter.this.getSyncRoot()) {
                LocalDisplayDevice localDisplayDevice = (LocalDisplayDevice) LocalDisplayAdapter.this.mDevices.get(j2);
                if (localDisplayDevice == null) {
                    if (LocalDisplayAdapter.DEBUG) {
                        Slog.d(LocalDisplayAdapter.TAG, "Received mode change for unhandled physical display: physicalDisplayId=" + j2);
                    }
                    return;
                }
                localDisplayDevice.onActiveDisplayModeChangedLocked(i, 1.0E9f / ((float) j3));
            }
        }

        @Override // com.android.server.display.LocalDisplayAdapter.DisplayEventListener
        public void onFrameRateOverridesChanged(long j, long j2, DisplayEventReceiver.FrameRateOverride[] frameRateOverrideArr) {
            if (LocalDisplayAdapter.DEBUG || LocalDisplayAdapter.PANIC_DEBUG) {
                Slog.d(LocalDisplayAdapter.TAG, "onFrameRateOverrideChanged(timestampNanos=" + j + ", physicalDisplayId=" + j2 + " overrides=" + Arrays.toString(frameRateOverrideArr) + ")");
            }
            synchronized (LocalDisplayAdapter.this.getSyncRoot()) {
                LocalDisplayDevice localDisplayDevice = (LocalDisplayDevice) LocalDisplayAdapter.this.mDevices.get(j2);
                if (localDisplayDevice == null) {
                    if (LocalDisplayAdapter.DEBUG) {
                        Slog.d(LocalDisplayAdapter.TAG, "Received frame rate override event for unhandled physical display: physicalDisplayId=" + j2);
                    }
                    return;
                }
                localDisplayDevice.onFrameRateOverridesChanged(frameRateOverrideArr);
            }
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class SurfaceControlProxy {
        public SurfaceControl.DynamicDisplayInfo getDynamicDisplayInfo(long j) {
            return SurfaceControl.getDynamicDisplayInfo(j);
        }

        public long[] getPhysicalDisplayIds() {
            return DisplayControl.getPhysicalDisplayIds();
        }

        public IBinder getPhysicalDisplayToken(long j) {
            return DisplayControl.getPhysicalDisplayToken(j);
        }

        public SurfaceControl.StaticDisplayInfo getStaticDisplayInfo(long j) {
            return SurfaceControl.getStaticDisplayInfo(j);
        }

        public SurfaceControl.DesiredDisplayModeSpecs getDesiredDisplayModeSpecs(IBinder iBinder) {
            return SurfaceControl.getDesiredDisplayModeSpecs(iBinder);
        }

        public boolean setDesiredDisplayModeSpecs(IBinder iBinder, SurfaceControl.DesiredDisplayModeSpecs desiredDisplayModeSpecs) {
            return SurfaceControl.setDesiredDisplayModeSpecs(iBinder, desiredDisplayModeSpecs);
        }

        public void setDisplayPowerMode(IBinder iBinder, int i) {
            SurfaceControl.setDisplayPowerMode(iBinder, i);
        }

        public boolean setActiveColorMode(IBinder iBinder, int i) {
            return SurfaceControl.setActiveColorMode(iBinder, i);
        }

        public boolean getBootDisplayModeSupport() {
            Trace.traceBegin(32L, "getBootDisplayModeSupport");
            try {
                return SurfaceControl.getBootDisplayModeSupport();
            } finally {
                Trace.traceEnd(32L);
            }
        }

        public void setBootDisplayMode(IBinder iBinder, int i) {
            SurfaceControl.setBootDisplayMode(iBinder, i);
        }

        public void clearBootDisplayMode(IBinder iBinder) {
            SurfaceControl.clearBootDisplayMode(iBinder);
        }

        public void setAutoLowLatencyMode(IBinder iBinder, boolean z) {
            SurfaceControl.setAutoLowLatencyMode(iBinder, z);
        }

        public void setGameContentType(IBinder iBinder, boolean z) {
            SurfaceControl.setGameContentType(iBinder, z);
        }

        public boolean getDisplayBrightnessSupport(IBinder iBinder) {
            return SurfaceControl.getDisplayBrightnessSupport(iBinder);
        }

        public boolean setDisplayBrightness(IBinder iBinder, float f) {
            return SurfaceControl.setDisplayBrightness(iBinder, f);
        }

        public boolean setDisplayBrightness(IBinder iBinder, float f, float f2, float f3, float f4) {
            return SurfaceControl.setDisplayBrightness(iBinder, f, f2, f3, f4);
        }

        public boolean setDisplayBrightness(IBinder iBinder, float f, float f2) {
            return SurfaceControl.setDisplayBrightness(iBinder, f, f2);
        }

        public boolean setDisplayBrightness(IBinder iBinder, float f, float f2, float f3, float f4, float f5, float f6, float f7, int i, boolean z) {
            return SurfaceControl.setDisplayBrightness(iBinder, f, f2, f3, f4, f5, f6, f7, i, z);
        }

        public boolean setDisplayBrightness(IBinder iBinder, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i, boolean z) {
            return SurfaceControl.setDisplayBrightness(iBinder, f, f2, f3, f4, f5, f6, f7, f8, i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class BacklightAdapter {
        private final LogicalLight mBacklight;
        private final IBinder mDisplayToken;
        private final SurfaceControlProxy mSurfaceControlProxy;
        private final boolean mUseSurfaceControlBrightness;
        private boolean mForceSurfaceControl = false;
        private int mDcThreshold = SystemProperties.getInt("ro.oplus.dc.brightness.threshold", 0);

        BacklightAdapter(IBinder iBinder, boolean z, SurfaceControlProxy surfaceControlProxy) {
            this.mDisplayToken = iBinder;
            this.mSurfaceControlProxy = surfaceControlProxy;
            this.mUseSurfaceControlBrightness = surfaceControlProxy.getDisplayBrightnessSupport(iBinder);
            if (z) {
                this.mBacklight = ((LightsManager) LocalServices.getService(LightsManager.class)).getLight(0);
            } else {
                this.mBacklight = null;
            }
        }

        void setBacklight(float f, float f2, float f3, float f4) {
            if (this.mUseSurfaceControlBrightness || this.mForceSurfaceControl) {
                if (BrightnessSynchronizer.floatEquals(f, Float.NaN)) {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, f3);
                    return;
                } else {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, f, f2, f3, f4);
                    return;
                }
            }
            LogicalLight logicalLight = this.mBacklight;
            if (logicalLight != null) {
                logicalLight.setBrightness(f3);
            }
        }

        void setForceSurfaceControl(boolean z) {
            this.mForceSurfaceControl = z;
        }

        void setBacklight(float f, float f2, float f3, float f4, float f5, float f6, float f7, int i, boolean z) {
            if (this.mUseSurfaceControlBrightness || this.mForceSurfaceControl) {
                if (BrightnessSynchronizer.floatEquals(f, Float.NaN)) {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, f3, f5);
                    return;
                } else if (this.mDcThreshold == 0) {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, f, f2, f3, f4, f5, f6, f7, i, z);
                    return;
                } else {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, f3, -1.0f, f3, f4, f5, f6, f7, i, z);
                    return;
                }
            }
            LogicalLight logicalLight = this.mBacklight;
            if (logicalLight != null) {
                logicalLight.setBrightness(f3);
            }
        }

        void setBacklight(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i, boolean z) {
            if (this.mUseSurfaceControlBrightness || this.mForceSurfaceControl) {
                if (BrightnessSynchronizer.floatEquals(f, Float.NaN)) {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, f3, f5, f6, f7);
                    return;
                } else {
                    this.mSurfaceControlProxy.setDisplayBrightness(this.mDisplayToken, f, f2, f3, f4, f5, f6, f7, f8, i, z);
                    return;
                }
            }
            LogicalLight logicalLight = this.mBacklight;
            if (logicalLight != null) {
                logicalLight.setBrightness(f3);
            }
        }

        public String toString() {
            return "BacklightAdapter [useSurfaceControl=" + this.mUseSurfaceControlBrightness + " (force_anyway? " + this.mForceSurfaceControl + "), backlight=" + this.mBacklight + "]";
        }
    }

    public ILocalDisplayAdapterWrapper getWrapper() {
        return this.mLdaWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class LocalDisplayAdapterWrapper implements ILocalDisplayAdapterWrapper {
        private ILocalDisplayAdapterExt mLdaExt;

        private LocalDisplayAdapterWrapper() {
            this.mLdaExt = (ILocalDisplayAdapterExt) ExtLoader.type(ILocalDisplayAdapterExt.class).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ILocalDisplayAdapterExt getExtImpl() {
            return this.mLdaExt;
        }

        @Override // com.android.server.display.ILocalDisplayAdapterWrapper
        public boolean getDebug() {
            return LocalDisplayAdapter.DEBUG;
        }
    }
}
