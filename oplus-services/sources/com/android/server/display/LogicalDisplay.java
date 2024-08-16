package com.android.server.display;

import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.DisplayEventReceiver;
import android.view.DisplayInfo;
import android.view.SurfaceControl;
import com.android.server.display.mode.DisplayModeDirector;
import com.android.server.wm.utils.InsetUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Objects;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class LogicalDisplay {
    private static final int BLANK_LAYER_STACK = -1;
    static final float BRIGHTNESS_OFF_FLOAT = -1.0f;
    static final int DISPLAY_PHASE_DISABLED = -1;
    static final int DISPLAY_PHASE_ENABLED = 1;
    static final int DISPLAY_PHASE_ENABLED_DISPLAY_OFF = 10;
    static final int DISPLAY_PHASE_LAYOUT_TRANSITION = 0;
    private static final DisplayInfo EMPTY_DISPLAY_INFO = new DisplayInfo();
    private static final String TAG = "LogicalDisplay";
    private final DisplayInfo mBaseDisplayInfo;
    private DisplayModeDirector.DesiredDisplayModeSpecs mDesiredDisplayModeSpecs;
    private int mDevicePosition;
    private boolean mDirty;
    private int mDisplayGroupId;
    private String mDisplayGroupName;
    private final int mDisplayId;
    private int mDisplayOffsetX;
    private int mDisplayOffsetY;
    private final Point mDisplayPosition;
    private boolean mDisplayScalingDisabled;
    private DisplayEventReceiver.FrameRateOverride[] mFrameRateOverrides;
    private boolean mHasContent;
    private final DisplayInfoProxy mInfo;
    private boolean mIsEnabled;
    private boolean mIsInTransition;
    private final int mLayerStack;
    private SurfaceControl.RefreshRateRange mLayoutLimitedRefreshRate;
    private int mLeadDisplayId;
    private ILogicalDisplayExt mLogicalDisplayExt;
    private DisplayInfo mOverrideDisplayInfo;
    private ArraySet<Integer> mPendingFrameRateOverrideUids;
    private DisplayDevice mPrimaryDisplayDevice;
    private DisplayDeviceInfo mPrimaryDisplayDeviceInfo;
    private int mRequestedColorMode;
    private boolean mRequestedMinimalPostProcessing;
    private final Rect mTempDisplayRect;
    private final SparseArray<Float> mTempFrameRateOverride;
    private final Rect mTempLayerStackRect;
    private String mThermalBrightnessThrottlingDataId;
    private SparseArray<SurfaceControl.RefreshRateRange> mThermalRefreshRateThrottling;
    private int[] mUserDisabledHdrTypes;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface DisplayPhase {
    }

    public ILogicalDisplayExt getLogicalDisplayExt() {
        return this.mLogicalDisplayExt;
    }

    public LogicalDisplay(int i, int i2, DisplayDevice displayDevice) {
        DisplayInfo displayInfo = new DisplayInfo();
        this.mBaseDisplayInfo = displayInfo;
        this.mLeadDisplayId = -1;
        this.mDisplayGroupId = -1;
        this.mInfo = new DisplayInfoProxy(null);
        this.mUserDisabledHdrTypes = new int[0];
        this.mDesiredDisplayModeSpecs = new DisplayModeDirector.DesiredDisplayModeSpecs();
        this.mDisplayPosition = new Point();
        this.mTempLayerStackRect = new Rect();
        this.mTempDisplayRect = new Rect();
        this.mLogicalDisplayExt = (ILogicalDisplayExt) ExtLoader.type(ILogicalDisplayExt.class).base(this).create();
        this.mDevicePosition = -1;
        this.mDirty = false;
        this.mThermalRefreshRateThrottling = new SparseArray<>();
        this.mDisplayId = i;
        this.mLayerStack = i2;
        this.mPrimaryDisplayDevice = displayDevice;
        this.mPendingFrameRateOverrideUids = new ArraySet<>();
        this.mTempFrameRateOverride = new SparseArray<>();
        this.mIsEnabled = true;
        this.mIsInTransition = false;
        this.mThermalBrightnessThrottlingDataId = "default";
        displayInfo.thermalBrightnessThrottlingDataId = "default";
    }

    public void setDevicePositionLocked(int i) {
        if (this.mDevicePosition != i) {
            this.mDevicePosition = i;
            this.mDirty = true;
        }
    }

    public int getDevicePositionLocked() {
        return this.mDevicePosition;
    }

    public int getDisplayIdLocked() {
        return this.mDisplayId;
    }

    public DisplayDevice getPrimaryDisplayDeviceLocked() {
        return this.mPrimaryDisplayDevice;
    }

    public DisplayInfo getDisplayInfoLocked() {
        if (this.mInfo.get() == null) {
            DisplayInfo displayInfo = new DisplayInfo();
            displayInfo.copyFrom(this.mBaseDisplayInfo);
            DisplayInfo displayInfo2 = this.mOverrideDisplayInfo;
            if (displayInfo2 != null) {
                displayInfo.appWidth = displayInfo2.appWidth;
                displayInfo.appHeight = displayInfo2.appHeight;
                displayInfo.smallestNominalAppWidth = displayInfo2.smallestNominalAppWidth;
                displayInfo.smallestNominalAppHeight = displayInfo2.smallestNominalAppHeight;
                displayInfo.largestNominalAppWidth = displayInfo2.largestNominalAppWidth;
                displayInfo.largestNominalAppHeight = displayInfo2.largestNominalAppHeight;
                displayInfo.logicalWidth = displayInfo2.logicalWidth;
                displayInfo.logicalHeight = displayInfo2.logicalHeight;
                displayInfo.physicalXDpi = displayInfo2.physicalXDpi;
                displayInfo.physicalYDpi = displayInfo2.physicalYDpi;
                displayInfo.rotation = displayInfo2.rotation;
                displayInfo.displayCutout = displayInfo2.displayCutout;
                displayInfo.logicalDensityDpi = displayInfo2.logicalDensityDpi;
                displayInfo.roundedCorners = displayInfo2.roundedCorners;
                displayInfo.displayShape = displayInfo2.displayShape;
            }
            this.mInfo.set(displayInfo);
        }
        return this.mInfo.get();
    }

    public DisplayEventReceiver.FrameRateOverride[] getFrameRateOverrides() {
        return this.mFrameRateOverrides;
    }

    public ArraySet<Integer> getPendingFrameRateOverrideUids() {
        return this.mPendingFrameRateOverrideUids;
    }

    public void clearPendingFrameRateOverrideUids() {
        this.mPendingFrameRateOverrideUids = new ArraySet<>();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getNonOverrideDisplayInfoLocked(DisplayInfo displayInfo) {
        displayInfo.copyFrom(this.mBaseDisplayInfo);
    }

    public boolean setDisplayInfoOverrideFromWindowManagerLocked(DisplayInfo displayInfo) {
        if (displayInfo != null) {
            if (!TextUtils.equals(displayInfo.uniqueId, this.mBaseDisplayInfo.uniqueId)) {
                Slog.i(TAG, "OverrideDisplayInfo uniqueId not match baseDisplayInfo");
                return false;
            }
            DisplayInfo displayInfo2 = this.mOverrideDisplayInfo;
            if (displayInfo2 == null) {
                this.mOverrideDisplayInfo = new DisplayInfo(displayInfo);
                this.mInfo.set(null);
                return true;
            }
            if (!displayInfo2.equals(displayInfo)) {
                this.mOverrideDisplayInfo.copyFrom(displayInfo);
                this.mInfo.set(null);
                return true;
            }
        } else if (this.mOverrideDisplayInfo != null) {
            this.mOverrideDisplayInfo = null;
            this.mInfo.set(null);
            return true;
        }
        return false;
    }

    public boolean isValidLocked() {
        return this.mPrimaryDisplayDevice != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDirtyLocked() {
        return this.mDirty;
    }

    public void updateDisplayGroupIdLocked(int i) {
        if (i != this.mDisplayGroupId) {
            this.mDisplayGroupId = i;
            this.mDirty = true;
        }
    }

    public void updateLayoutLimitedRefreshRateLocked(SurfaceControl.RefreshRateRange refreshRateRange) {
        if (Objects.equals(refreshRateRange, this.mLayoutLimitedRefreshRate)) {
            return;
        }
        this.mLayoutLimitedRefreshRate = refreshRateRange;
        this.mDirty = true;
    }

    public void updateThermalRefreshRateThrottling(SparseArray<SurfaceControl.RefreshRateRange> sparseArray) {
        if (sparseArray == null) {
            sparseArray = new SparseArray<>();
        }
        if (this.mThermalRefreshRateThrottling.contentEquals(sparseArray)) {
            return;
        }
        this.mThermalRefreshRateThrottling = sparseArray;
        this.mDirty = true;
    }

    public void updateLocked(DisplayDeviceRepository displayDeviceRepository) {
        DisplayDevice displayDevice = this.mPrimaryDisplayDevice;
        if (displayDevice == null) {
            Slog.w(TAG, "updateLocked primaryDisplayDevice null");
            return;
        }
        if (!displayDeviceRepository.containsLocked(displayDevice)) {
            setPrimaryDisplayDeviceLocked(null);
            return;
        }
        DisplayDeviceInfo displayDeviceInfoLocked = this.mPrimaryDisplayDevice.getDisplayDeviceInfoLocked();
        if (!Objects.equals(this.mPrimaryDisplayDeviceInfo, displayDeviceInfoLocked) || this.mDirty) {
            DisplayInfo displayInfo = this.mBaseDisplayInfo;
            displayInfo.layerStack = this.mLayerStack;
            displayInfo.flags = 0;
            displayInfo.removeMode = 0;
            int i = displayDeviceInfoLocked.flags;
            if ((i & 8) != 0) {
                displayInfo.flags = 0 | 1;
            }
            if ((i & 4) != 0) {
                displayInfo.flags |= 2;
            }
            if ((i & 16) != 0) {
                displayInfo.flags |= 4;
                displayInfo.removeMode = 1;
            }
            if ((i & 1024) != 0) {
                displayInfo.removeMode = 1;
            }
            if ((i & 64) != 0) {
                displayInfo.flags |= 8;
            }
            if ((i & 256) != 0) {
                displayInfo.flags |= 16;
            }
            if ((i & 512) != 0) {
                displayInfo.flags |= 32;
            }
            if ((i & 4096) != 0) {
                displayInfo.flags |= 64;
            }
            if ((i & 8192) != 0) {
                displayInfo.flags |= 128;
            }
            if ((i & 16384) != 0) {
                displayInfo.flags |= 256;
            }
            if ((32768 & i) != 0) {
                displayInfo.flags |= 512;
            }
            if ((65536 & i) != 0) {
                displayInfo.flags |= 1024;
            }
            if ((i & 131072) != 0) {
                displayInfo.flags |= 2048;
            }
            this.mLogicalDisplayExt.setDisplayInfoFlags(displayDeviceInfoLocked, displayInfo, this.mDisplayId);
            if ((displayDeviceInfoLocked.flags & 524288) != 0) {
                this.mBaseDisplayInfo.flags |= 4096;
            }
            Rect maskingInsets = getMaskingInsets(displayDeviceInfoLocked);
            int i2 = (displayDeviceInfoLocked.width - maskingInsets.left) - maskingInsets.right;
            int i3 = (displayDeviceInfoLocked.height - maskingInsets.top) - maskingInsets.bottom;
            DisplayInfo displayInfo2 = this.mBaseDisplayInfo;
            displayInfo2.type = displayDeviceInfoLocked.type;
            displayInfo2.address = displayDeviceInfoLocked.address;
            displayInfo2.deviceProductInfo = displayDeviceInfoLocked.deviceProductInfo;
            displayInfo2.name = displayDeviceInfoLocked.name;
            displayInfo2.uniqueId = displayDeviceInfoLocked.uniqueId;
            displayInfo2.appWidth = i2;
            displayInfo2.appHeight = i3;
            displayInfo2.logicalWidth = i2;
            displayInfo2.logicalHeight = i3;
            displayInfo2.rotation = 0;
            displayInfo2.modeId = displayDeviceInfoLocked.modeId;
            displayInfo2.renderFrameRate = displayDeviceInfoLocked.renderFrameRate;
            displayInfo2.defaultModeId = displayDeviceInfoLocked.defaultModeId;
            Display.Mode[] modeArr = displayDeviceInfoLocked.supportedModes;
            displayInfo2.supportedModes = (Display.Mode[]) Arrays.copyOf(modeArr, modeArr.length);
            DisplayInfo displayInfo3 = this.mBaseDisplayInfo;
            displayInfo3.colorMode = displayDeviceInfoLocked.colorMode;
            int[] iArr = displayDeviceInfoLocked.supportedColorModes;
            displayInfo3.supportedColorModes = Arrays.copyOf(iArr, iArr.length);
            DisplayInfo displayInfo4 = this.mBaseDisplayInfo;
            displayInfo4.hdrCapabilities = displayDeviceInfoLocked.hdrCapabilities;
            displayInfo4.userDisabledHdrTypes = this.mUserDisabledHdrTypes;
            displayInfo4.minimalPostProcessingSupported = displayDeviceInfoLocked.allmSupported || displayDeviceInfoLocked.gameContentTypeSupported;
            displayInfo4.logicalDensityDpi = displayDeviceInfoLocked.densityDpi;
            displayInfo4.physicalXDpi = displayDeviceInfoLocked.xDpi;
            displayInfo4.physicalYDpi = displayDeviceInfoLocked.yDpi;
            displayInfo4.appVsyncOffsetNanos = displayDeviceInfoLocked.appVsyncOffsetNanos;
            displayInfo4.presentationDeadlineNanos = displayDeviceInfoLocked.presentationDeadlineNanos;
            displayInfo4.state = displayDeviceInfoLocked.state;
            displayInfo4.committedState = displayDeviceInfoLocked.committedState;
            displayInfo4.smallestNominalAppWidth = i2;
            displayInfo4.smallestNominalAppHeight = i3;
            displayInfo4.largestNominalAppWidth = i2;
            displayInfo4.largestNominalAppHeight = i3;
            displayInfo4.ownerUid = displayDeviceInfoLocked.ownerUid;
            displayInfo4.ownerPackageName = displayDeviceInfoLocked.ownerPackageName;
            displayInfo4.displayCutout = (displayDeviceInfoLocked.flags & 2048) != 0 ? null : displayDeviceInfoLocked.displayCutout;
            displayInfo4.displayId = this.mDisplayId;
            displayInfo4.displayGroupId = this.mDisplayGroupId;
            updateFrameRateOverrides(displayDeviceInfoLocked);
            DisplayInfo displayInfo5 = this.mBaseDisplayInfo;
            displayInfo5.brightnessMinimum = displayDeviceInfoLocked.brightnessMinimum;
            displayInfo5.brightnessMaximum = displayDeviceInfoLocked.brightnessMaximum;
            displayInfo5.brightnessDefault = displayDeviceInfoLocked.brightnessDefault;
            displayInfo5.hdrSdrRatio = displayDeviceInfoLocked.hdrSdrRatio;
            displayInfo5.backlightType = displayDeviceInfoLocked.backlightType;
            displayInfo5.roundedCorners = displayDeviceInfoLocked.roundedCorners;
            displayInfo5.installOrientation = displayDeviceInfoLocked.installOrientation;
            displayInfo5.displayShape = displayDeviceInfoLocked.displayShape;
            if (this.mDevicePosition == 1) {
                displayInfo5.flags = displayInfo5.flags | 8192 | 8;
                displayInfo5.removeMode = 1;
            }
            displayInfo5.layoutLimitedRefreshRate = this.mLayoutLimitedRefreshRate;
            displayInfo5.thermalRefreshRateThrottling = this.mThermalRefreshRateThrottling;
            displayInfo5.thermalBrightnessThrottlingDataId = this.mThermalBrightnessThrottlingDataId;
            this.mPrimaryDisplayDeviceInfo = displayDeviceInfoLocked;
            this.mInfo.set(null);
            this.mDirty = false;
        }
    }

    private void updateFrameRateOverrides(DisplayDeviceInfo displayDeviceInfo) {
        this.mTempFrameRateOverride.clear();
        DisplayEventReceiver.FrameRateOverride[] frameRateOverrideArr = this.mFrameRateOverrides;
        if (frameRateOverrideArr != null) {
            for (DisplayEventReceiver.FrameRateOverride frameRateOverride : frameRateOverrideArr) {
                this.mTempFrameRateOverride.put(frameRateOverride.uid, Float.valueOf(frameRateOverride.frameRateHz));
            }
        }
        DisplayEventReceiver.FrameRateOverride[] frameRateOverrideArr2 = displayDeviceInfo.frameRateOverrides;
        this.mFrameRateOverrides = frameRateOverrideArr2;
        if (frameRateOverrideArr2 != null) {
            for (DisplayEventReceiver.FrameRateOverride frameRateOverride2 : frameRateOverrideArr2) {
                float floatValue = this.mTempFrameRateOverride.get(frameRateOverride2.uid, Float.valueOf(0.0f)).floatValue();
                if (floatValue == 0.0f || frameRateOverride2.frameRateHz != floatValue) {
                    this.mTempFrameRateOverride.put(frameRateOverride2.uid, Float.valueOf(frameRateOverride2.frameRateHz));
                } else {
                    this.mTempFrameRateOverride.delete(frameRateOverride2.uid);
                }
            }
        }
        for (int i = 0; i < this.mTempFrameRateOverride.size(); i++) {
            this.mPendingFrameRateOverrideUids.add(Integer.valueOf(this.mTempFrameRateOverride.keyAt(i)));
        }
    }

    public Rect getInsets() {
        return getMaskingInsets(this.mPrimaryDisplayDeviceInfo);
    }

    private static Rect getMaskingInsets(DisplayDeviceInfo displayDeviceInfo) {
        DisplayCutout displayCutout;
        if (((displayDeviceInfo.flags & 2048) != 0) && (displayCutout = displayDeviceInfo.displayCutout) != null) {
            return displayCutout.getSafeInsets();
        }
        return new Rect();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Point getDisplayPosition() {
        return new Point(this.mDisplayPosition);
    }

    public void configureDisplayLocked(SurfaceControl.Transaction transaction, DisplayDevice displayDevice, boolean z) {
        int i;
        int i2;
        displayDevice.setLayerStackLocked(transaction, z ? -1 : this.mLayerStack, this.mDisplayId);
        displayDevice.setDisplayFlagsLocked(transaction, (!isEnabledLocked() || displayDevice.getDisplayDeviceInfoLocked().touch == 0) ? 0 : 1);
        if (displayDevice == this.mPrimaryDisplayDevice) {
            displayDevice.setDesiredDisplayModeSpecsLocked(this.mDesiredDisplayModeSpecs);
            displayDevice.setRequestedColorModeLocked(this.mRequestedColorMode);
        } else {
            displayDevice.setDesiredDisplayModeSpecsLocked(new DisplayModeDirector.DesiredDisplayModeSpecs());
            displayDevice.setRequestedColorModeLocked(0);
        }
        displayDevice.setAutoLowLatencyModeLocked(this.mRequestedMinimalPostProcessing);
        displayDevice.setGameContentTypeLocked(this.mRequestedMinimalPostProcessing);
        DisplayInfo displayInfoLocked = getDisplayInfoLocked();
        DisplayDeviceInfo displayDeviceInfoLocked = displayDevice.getDisplayDeviceInfoLocked();
        this.mTempLayerStackRect.set(0, 0, displayInfoLocked.logicalWidth, displayInfoLocked.logicalHeight);
        int i3 = (((displayDeviceInfoLocked.flags & 2) != 0 ? displayInfoLocked.rotation : 0) + displayDeviceInfoLocked.rotation) % 4;
        boolean z2 = i3 == 1 || i3 == 3;
        int i4 = z2 ? displayDeviceInfoLocked.height : displayDeviceInfoLocked.width;
        int i5 = z2 ? displayDeviceInfoLocked.width : displayDeviceInfoLocked.height;
        Rect maskingInsets = getMaskingInsets(displayDeviceInfoLocked);
        InsetUtils.rotateInsets(maskingInsets, i3);
        int i6 = i4 - (maskingInsets.left + maskingInsets.right);
        int i7 = i5 - (maskingInsets.top + maskingInsets.bottom);
        if ((displayInfoLocked.flags & 1073741824) != 0 || this.mDisplayScalingDisabled) {
            int i8 = displayInfoLocked.logicalWidth;
            i = displayInfoLocked.logicalHeight;
            i2 = i8;
        } else {
            int i9 = displayInfoLocked.logicalHeight;
            int i10 = i6 * i9;
            int i11 = displayInfoLocked.logicalWidth;
            if (i10 < i7 * i11) {
                i = (i9 * i6) / i11;
                i2 = i6;
            } else {
                i2 = (i11 * i7) / i9;
                i = i7;
            }
        }
        int i12 = (i7 - i) / 2;
        int i13 = (i6 - i2) / 2;
        this.mTempDisplayRect.set(i13, i12, i2 + i13, i + i12);
        this.mTempDisplayRect.offset(maskingInsets.left, maskingInsets.top);
        if (i3 == 0) {
            this.mTempDisplayRect.offset(this.mDisplayOffsetX, this.mDisplayOffsetY);
        } else if (i3 == 1) {
            this.mTempDisplayRect.offset(this.mDisplayOffsetY, -this.mDisplayOffsetX);
        } else if (i3 == 2) {
            this.mTempDisplayRect.offset(-this.mDisplayOffsetX, -this.mDisplayOffsetY);
        } else {
            this.mTempDisplayRect.offset(-this.mDisplayOffsetY, this.mDisplayOffsetX);
        }
        Point point = this.mDisplayPosition;
        Rect rect = this.mTempDisplayRect;
        point.set(rect.left, rect.top);
        displayDevice.setProjectionLocked(transaction, i3, this.mTempLayerStackRect, this.mTempDisplayRect);
    }

    public boolean hasContentLocked() {
        return this.mHasContent;
    }

    public void setHasContentLocked(boolean z) {
        this.mHasContent = z;
    }

    public void setDesiredDisplayModeSpecsLocked(DisplayModeDirector.DesiredDisplayModeSpecs desiredDisplayModeSpecs) {
        this.mDesiredDisplayModeSpecs = desiredDisplayModeSpecs;
    }

    public DisplayModeDirector.DesiredDisplayModeSpecs getDesiredDisplayModeSpecsLocked() {
        return this.mDesiredDisplayModeSpecs;
    }

    public void setRequestedColorModeLocked(int i) {
        this.mRequestedColorMode = i;
    }

    public boolean getRequestedMinimalPostProcessingLocked() {
        return this.mRequestedMinimalPostProcessing;
    }

    public void setRequestedMinimalPostProcessingLocked(boolean z) {
        this.mRequestedMinimalPostProcessing = z;
    }

    public int getRequestedColorModeLocked() {
        return this.mRequestedColorMode;
    }

    public int getDisplayOffsetXLocked() {
        return this.mDisplayOffsetX;
    }

    public int getDisplayOffsetYLocked() {
        return this.mDisplayOffsetY;
    }

    public void setDisplayOffsetsLocked(int i, int i2) {
        this.mDisplayOffsetX = i;
        this.mDisplayOffsetY = i2;
    }

    public boolean isDisplayScalingDisabled() {
        return this.mDisplayScalingDisabled;
    }

    public void setDisplayScalingDisabledLocked(boolean z) {
        this.mDisplayScalingDisabled = z;
    }

    public void setUserDisabledHdrTypes(int[] iArr) {
        if (this.mUserDisabledHdrTypes != iArr) {
            this.mUserDisabledHdrTypes = iArr;
            this.mBaseDisplayInfo.userDisabledHdrTypes = iArr;
            this.mInfo.set(null);
        }
    }

    public void swapDisplaysLocked(LogicalDisplay logicalDisplay) {
        setPrimaryDisplayDeviceLocked(logicalDisplay.setPrimaryDisplayDeviceLocked(this.mPrimaryDisplayDevice));
    }

    public DisplayDevice setPrimaryDisplayDeviceLocked(DisplayDevice displayDevice) {
        DisplayDevice displayDevice2 = this.mPrimaryDisplayDevice;
        this.mPrimaryDisplayDevice = displayDevice;
        this.mPrimaryDisplayDeviceInfo = null;
        this.mBaseDisplayInfo.copyFrom(EMPTY_DISPLAY_INFO);
        this.mOverrideDisplayInfo = null;
        this.mInfo.set(null);
        Slog.w(TAG, "setPrimaryDisplayDeviceLocked device changed:" + displayDevice2 + "->" + displayDevice);
        return displayDevice2;
    }

    public boolean isEnabledLocked() {
        return this.mIsEnabled;
    }

    public void setEnabledLocked(boolean z) {
        DisplayDevice displayDevice;
        Runnable requestDisplayStateLocked;
        this.mIsEnabled = z;
        if (z || (displayDevice = this.mPrimaryDisplayDevice) == null || (requestDisplayStateLocked = displayDevice.requestDisplayStateLocked(1, -1.0f, -1.0f)) == null) {
            return;
        }
        requestDisplayStateLocked.run();
    }

    public boolean isInTransitionLocked() {
        return this.mIsInTransition;
    }

    public void setIsInTransitionLocked(boolean z) {
        this.mIsInTransition = z;
    }

    public void setThermalBrightnessThrottlingDataIdLocked(String str) {
        if (Objects.equals(str, this.mThermalBrightnessThrottlingDataId)) {
            return;
        }
        this.mThermalBrightnessThrottlingDataId = str;
        this.mDirty = true;
    }

    public void setLeadDisplayLocked(int i) {
        int i2 = this.mDisplayId;
        if (i2 == this.mLeadDisplayId || i2 == i) {
            return;
        }
        this.mLeadDisplayId = i;
    }

    public int getLeadDisplayIdLocked() {
        return this.mLeadDisplayId;
    }

    public void setDisplayGroupNameLocked(String str) {
        this.mDisplayGroupName = str;
    }

    public String getDisplayGroupNameLocked() {
        return this.mDisplayGroupName;
    }

    public void dumpLocked(PrintWriter printWriter) {
        printWriter.println("mDisplayId=" + this.mDisplayId);
        printWriter.println("mIsEnabled=" + this.mIsEnabled);
        printWriter.println("mIsInTransition=" + this.mIsInTransition);
        printWriter.println("mLayerStack=" + this.mLayerStack);
        printWriter.println("mPosition=" + this.mDevicePosition);
        printWriter.println("mHasContent=" + this.mHasContent);
        printWriter.println("mDesiredDisplayModeSpecs={" + this.mDesiredDisplayModeSpecs + "}");
        StringBuilder sb = new StringBuilder();
        sb.append("mRequestedColorMode=");
        sb.append(this.mRequestedColorMode);
        printWriter.println(sb.toString());
        printWriter.println("mDisplayOffset=(" + this.mDisplayOffsetX + ", " + this.mDisplayOffsetY + ")");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mDisplayScalingDisabled=");
        sb2.append(this.mDisplayScalingDisabled);
        printWriter.println(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("mPrimaryDisplayDevice=");
        DisplayDevice displayDevice = this.mPrimaryDisplayDevice;
        sb3.append(displayDevice != null ? displayDevice.getNameLocked() : "null");
        printWriter.println(sb3.toString());
        printWriter.println("mBaseDisplayInfo=" + this.mBaseDisplayInfo);
        printWriter.println("mOverrideDisplayInfo=" + this.mOverrideDisplayInfo);
        printWriter.println("mRequestedMinimalPostProcessing=" + this.mRequestedMinimalPostProcessing);
        printWriter.println("mFrameRateOverrides=" + Arrays.toString(this.mFrameRateOverrides));
        printWriter.println("mPendingFrameRateOverrideUids=" + this.mPendingFrameRateOverrideUids);
        printWriter.println("mDisplayGroupName=" + this.mDisplayGroupName);
        printWriter.println("mThermalBrightnessThrottlingDataId=" + this.mThermalBrightnessThrottlingDataId);
        printWriter.println("mLeadDisplayId=" + this.mLeadDisplayId);
        printWriter.println("mLayoutLimitedRefreshRate=" + this.mLayoutLimitedRefreshRate);
        printWriter.println("mThermalRefreshRateThrottling=" + this.mThermalRefreshRateThrottling);
    }

    public String toStringMini() {
        return "id=" + this.mDisplayId + ",stack=" + this.mLayerStack + ",content=" + this.mHasContent + ",displayInfo{stack=" + this.mBaseDisplayInfo.layerStack + ",type=" + this.mBaseDisplayInfo.type + ",id=" + this.mBaseDisplayInfo.displayId + ",uId=" + this.mBaseDisplayInfo.uniqueId + ",state=" + Display.stateToString(this.mBaseDisplayInfo.state) + ",pkg=" + this.mBaseDisplayInfo.ownerPackageName + "}";
    }

    public String toString() {
        StringWriter stringWriter = new StringWriter();
        dumpLocked(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
