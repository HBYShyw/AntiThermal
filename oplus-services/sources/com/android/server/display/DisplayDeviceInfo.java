package com.android.server.display;

import android.hardware.display.DeviceProductInfo;
import android.view.Display;
import android.view.DisplayAddress;
import android.view.DisplayCutout;
import android.view.DisplayEventReceiver;
import android.view.DisplayShape;
import android.view.RoundedCorners;
import com.android.internal.display.BrightnessSynchronizer;
import java.util.Arrays;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DisplayDeviceInfo {
    public static final int DIFF_COLOR_MODE = 4;
    public static final int DIFF_EVERYTHING = -1;
    public static final int DIFF_HDR_SDR_RATIO = 8;
    public static final int DIFF_OTHER = 2;
    public static final int DIFF_STATE = 1;
    public static final int FLAG_ALLOWED_TO_BE_DEFAULT_DISPLAY = 1;
    public static final int FLAG_ALWAYS_UNLOCKED = 32768;
    public static final int FLAG_CAN_SHOW_WITH_INSECURE_KEYGUARD = 512;
    public static final int FLAG_DESTROY_CONTENT_ON_REMOVAL = 1024;
    public static final int FLAG_DEVICE_DISPLAY_GROUP = 262144;
    public static final int FLAG_MASK_DISPLAY_CUTOUT = 2048;
    public static final int FLAG_NEVER_BLANK = 32;
    public static final int FLAG_OWN_CONTENT_ONLY = 128;
    public static final int FLAG_OWN_DISPLAY_GROUP = 16384;
    public static final int FLAG_OWN_FOCUS = 131072;
    public static final int FLAG_PRESENTATION = 64;
    public static final int FLAG_PRIVATE = 16;
    public static final int FLAG_ROTATES_WITH_CONTENT = 2;
    public static final int FLAG_ROUND = 256;
    public static final int FLAG_SECURE = 4;
    public static final int FLAG_SHOULD_SHOW_SYSTEM_DECORATIONS = 4096;
    public static final int FLAG_STEAL_TOP_FOCUS_DISABLED = 524288;
    public static final int FLAG_SUPPORTS_PROTECTED_BUFFERS = 8;
    public static final int FLAG_TOUCH_FEEDBACK_DISABLED = 65536;
    public static final int FLAG_TRUSTED = 8192;
    public static final int TOUCH_EXTERNAL = 2;
    public static final int TOUCH_INTERNAL = 1;
    public static final int TOUCH_NONE = 0;
    public static final int TOUCH_VIRTUAL = 3;
    public DisplayAddress address;
    public boolean allmSupported;
    public long appVsyncOffsetNanos;
    public float brightnessDefault;
    public float brightnessMaximum;
    public float brightnessMinimum;
    public int colorMode;
    public int defaultModeId;
    public int densityDpi;
    public DeviceProductInfo deviceProductInfo;
    public DisplayCutout displayCutout;
    public DisplayShape displayShape;
    public int flags;
    public boolean gameContentTypeSupported;
    public Display.HdrCapabilities hdrCapabilities;
    public int height;
    public int modeId;
    public String name;
    public String ownerPackageName;
    public int ownerUid;
    public long presentationDeadlineNanos;
    public float renderFrameRate;
    public RoundedCorners roundedCorners;
    public int touch;
    public int type;
    public String uniqueId;
    public int width;
    public float xDpi;
    public float yDpi;
    public Display.Mode[] supportedModes = Display.Mode.EMPTY_ARRAY;
    public int[] supportedColorModes = {0};
    public int rotation = 0;
    public int state = 2;
    public int committedState = 0;
    public DisplayEventReceiver.FrameRateOverride[] frameRateOverrides = new DisplayEventReceiver.FrameRateOverride[0];
    public float hdrSdrRatio = Float.NaN;
    public int backlightType = 0;
    public int installOrientation = 0;

    public int hashCode() {
        return 0;
    }

    public void setAssumedDensityForExternalDisplay(int i, int i2) {
        int min = (Math.min(i, i2) * 320) / 1080;
        this.densityDpi = min;
        this.xDpi = min;
        this.yDpi = min;
    }

    public boolean equals(Object obj) {
        return (obj instanceof DisplayDeviceInfo) && equals((DisplayDeviceInfo) obj);
    }

    public boolean equals(DisplayDeviceInfo displayDeviceInfo) {
        return displayDeviceInfo != null && diff(displayDeviceInfo) == 0;
    }

    public int diff(DisplayDeviceInfo displayDeviceInfo) {
        int i = (this.state == displayDeviceInfo.state && this.committedState == displayDeviceInfo.committedState) ? 0 : 1;
        if (this.colorMode != displayDeviceInfo.colorMode) {
            i |= 4;
        }
        if (!BrightnessSynchronizer.floatEquals(this.hdrSdrRatio, displayDeviceInfo.hdrSdrRatio)) {
            i |= 8;
        }
        return (Objects.equals(this.name, displayDeviceInfo.name) && Objects.equals(this.uniqueId, displayDeviceInfo.uniqueId) && this.width == displayDeviceInfo.width && this.height == displayDeviceInfo.height && this.modeId == displayDeviceInfo.modeId && this.renderFrameRate == displayDeviceInfo.renderFrameRate && this.defaultModeId == displayDeviceInfo.defaultModeId && Arrays.equals(this.supportedModes, displayDeviceInfo.supportedModes) && Arrays.equals(this.supportedColorModes, displayDeviceInfo.supportedColorModes) && Objects.equals(this.hdrCapabilities, displayDeviceInfo.hdrCapabilities) && this.allmSupported == displayDeviceInfo.allmSupported && this.gameContentTypeSupported == displayDeviceInfo.gameContentTypeSupported && this.densityDpi == displayDeviceInfo.densityDpi && this.xDpi == displayDeviceInfo.xDpi && this.yDpi == displayDeviceInfo.yDpi && this.appVsyncOffsetNanos == displayDeviceInfo.appVsyncOffsetNanos && this.presentationDeadlineNanos == displayDeviceInfo.presentationDeadlineNanos && this.flags == displayDeviceInfo.flags && Objects.equals(this.displayCutout, displayDeviceInfo.displayCutout) && this.touch == displayDeviceInfo.touch && this.rotation == displayDeviceInfo.rotation && this.type == displayDeviceInfo.type && Objects.equals(this.address, displayDeviceInfo.address) && Objects.equals(this.deviceProductInfo, displayDeviceInfo.deviceProductInfo) && this.ownerUid == displayDeviceInfo.ownerUid && Objects.equals(this.ownerPackageName, displayDeviceInfo.ownerPackageName) && Arrays.equals(this.frameRateOverrides, displayDeviceInfo.frameRateOverrides) && BrightnessSynchronizer.floatEquals(this.brightnessMinimum, displayDeviceInfo.brightnessMinimum) && BrightnessSynchronizer.floatEquals(this.brightnessMaximum, displayDeviceInfo.brightnessMaximum) && BrightnessSynchronizer.floatEquals(this.brightnessDefault, displayDeviceInfo.brightnessDefault) && Objects.equals(this.roundedCorners, displayDeviceInfo.roundedCorners) && this.installOrientation == displayDeviceInfo.installOrientation && Objects.equals(this.displayShape, displayDeviceInfo.displayShape)) ? i : i | 2;
    }

    public void copyFrom(DisplayDeviceInfo displayDeviceInfo) {
        this.name = displayDeviceInfo.name;
        this.uniqueId = displayDeviceInfo.uniqueId;
        this.width = displayDeviceInfo.width;
        this.height = displayDeviceInfo.height;
        this.modeId = displayDeviceInfo.modeId;
        this.renderFrameRate = displayDeviceInfo.renderFrameRate;
        this.defaultModeId = displayDeviceInfo.defaultModeId;
        this.supportedModes = displayDeviceInfo.supportedModes;
        this.colorMode = displayDeviceInfo.colorMode;
        this.supportedColorModes = displayDeviceInfo.supportedColorModes;
        this.hdrCapabilities = displayDeviceInfo.hdrCapabilities;
        this.allmSupported = displayDeviceInfo.allmSupported;
        this.gameContentTypeSupported = displayDeviceInfo.gameContentTypeSupported;
        this.densityDpi = displayDeviceInfo.densityDpi;
        this.xDpi = displayDeviceInfo.xDpi;
        this.yDpi = displayDeviceInfo.yDpi;
        this.appVsyncOffsetNanos = displayDeviceInfo.appVsyncOffsetNanos;
        this.presentationDeadlineNanos = displayDeviceInfo.presentationDeadlineNanos;
        this.flags = displayDeviceInfo.flags;
        this.displayCutout = displayDeviceInfo.displayCutout;
        this.touch = displayDeviceInfo.touch;
        this.rotation = displayDeviceInfo.rotation;
        this.type = displayDeviceInfo.type;
        this.address = displayDeviceInfo.address;
        this.deviceProductInfo = displayDeviceInfo.deviceProductInfo;
        this.state = displayDeviceInfo.state;
        this.committedState = displayDeviceInfo.committedState;
        this.ownerUid = displayDeviceInfo.ownerUid;
        this.ownerPackageName = displayDeviceInfo.ownerPackageName;
        this.frameRateOverrides = displayDeviceInfo.frameRateOverrides;
        this.brightnessMinimum = displayDeviceInfo.brightnessMinimum;
        this.brightnessMaximum = displayDeviceInfo.brightnessMaximum;
        this.brightnessDefault = displayDeviceInfo.brightnessDefault;
        this.hdrSdrRatio = displayDeviceInfo.hdrSdrRatio;
        this.backlightType = displayDeviceInfo.backlightType;
        this.roundedCorners = displayDeviceInfo.roundedCorners;
        this.installOrientation = displayDeviceInfo.installOrientation;
        this.displayShape = displayDeviceInfo.displayShape;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DisplayDeviceInfo{\"");
        sb.append(this.name);
        sb.append("\": uniqueId=\"");
        sb.append(this.uniqueId);
        sb.append("\", ");
        sb.append(this.width);
        sb.append(" x ");
        sb.append(this.height);
        sb.append(", modeId ");
        sb.append(this.modeId);
        sb.append(", renderFrameRate ");
        sb.append(this.renderFrameRate);
        sb.append(", defaultModeId ");
        sb.append(this.defaultModeId);
        sb.append(", supportedModes ");
        sb.append(Arrays.toString(this.supportedModes));
        sb.append(", colorMode ");
        sb.append(this.colorMode);
        sb.append(", supportedColorModes ");
        sb.append(Arrays.toString(this.supportedColorModes));
        sb.append(", hdrCapabilities ");
        sb.append(this.hdrCapabilities);
        sb.append(", allmSupported ");
        sb.append(this.allmSupported);
        sb.append(", gameContentTypeSupported ");
        sb.append(this.gameContentTypeSupported);
        sb.append(", density ");
        sb.append(this.densityDpi);
        sb.append(", ");
        sb.append(this.xDpi);
        sb.append(" x ");
        sb.append(this.yDpi);
        sb.append(" dpi");
        sb.append(", appVsyncOff ");
        sb.append(this.appVsyncOffsetNanos);
        sb.append(", presDeadline ");
        sb.append(this.presentationDeadlineNanos);
        if (this.displayCutout != null) {
            sb.append(", cutout ");
            sb.append(this.displayCutout);
        }
        sb.append(", touch ");
        sb.append(touchToString(this.touch));
        sb.append(", rotation ");
        sb.append(this.rotation);
        sb.append(", type ");
        sb.append(Display.typeToString(this.type));
        if (this.address != null) {
            sb.append(", address ");
            sb.append(this.address);
        }
        sb.append(", deviceProductInfo ");
        sb.append(this.deviceProductInfo);
        sb.append(", state ");
        sb.append(Display.stateToString(this.state));
        sb.append(", committedState ");
        sb.append(Display.stateToString(this.committedState));
        if (this.ownerUid != 0 || this.ownerPackageName != null) {
            sb.append(", owner ");
            sb.append(this.ownerPackageName);
            sb.append(" (uid ");
            sb.append(this.ownerUid);
            sb.append(")");
        }
        sb.append(", frameRateOverride ");
        for (DisplayEventReceiver.FrameRateOverride frameRateOverride : this.frameRateOverrides) {
            sb.append(frameRateOverride);
            sb.append(" ");
        }
        sb.append(", brightnessMinimum ");
        sb.append(this.brightnessMinimum);
        sb.append(", brightnessMaximum ");
        sb.append(this.brightnessMaximum);
        sb.append(", brightnessDefault ");
        sb.append(this.brightnessDefault);
        sb.append(", hdrSdrRatio ");
        sb.append(this.hdrSdrRatio);
        sb.append(", backlightType ");
        sb.append(this.backlightType);
        if (this.roundedCorners != null) {
            sb.append(", roundedCorners ");
            sb.append(this.roundedCorners);
        }
        sb.append(flagsToString(this.flags));
        sb.append(", installOrientation ");
        sb.append(this.installOrientation);
        if (this.displayShape != null) {
            sb.append(", displayShape ");
            sb.append(this.displayShape);
        }
        sb.append("}");
        return sb.toString();
    }

    private static String touchToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? Integer.toString(i) : "VIRTUAL" : "EXTERNAL" : "INTERNAL" : "NONE";
    }

    private static String flagsToString(int i) {
        StringBuilder sb = new StringBuilder();
        if ((i & 1) != 0) {
            sb.append(", FLAG_ALLOWED_TO_BE_DEFAULT_DISPLAY");
        }
        if ((i & 2) != 0) {
            sb.append(", FLAG_ROTATES_WITH_CONTENT");
        }
        if ((i & 4) != 0) {
            sb.append(", FLAG_SECURE");
        }
        if ((i & 8) != 0) {
            sb.append(", FLAG_SUPPORTS_PROTECTED_BUFFERS");
        }
        if ((i & 16) != 0) {
            sb.append(", FLAG_PRIVATE");
        }
        if ((i & 32) != 0) {
            sb.append(", FLAG_NEVER_BLANK");
        }
        if ((i & 64) != 0) {
            sb.append(", FLAG_PRESENTATION");
        }
        if ((i & 128) != 0) {
            sb.append(", FLAG_OWN_CONTENT_ONLY");
        }
        if ((i & 256) != 0) {
            sb.append(", FLAG_ROUND");
        }
        if ((i & 512) != 0) {
            sb.append(", FLAG_CAN_SHOW_WITH_INSECURE_KEYGUARD");
        }
        if ((i & 1024) != 0) {
            sb.append(", FLAG_DESTROY_CONTENT_ON_REMOVAL");
        }
        if ((i & 2048) != 0) {
            sb.append(", FLAG_MASK_DISPLAY_CUTOUT");
        }
        if ((i & 4096) != 0) {
            sb.append(", FLAG_SHOULD_SHOW_SYSTEM_DECORATIONS");
        }
        if ((i & 8192) != 0) {
            sb.append(", FLAG_TRUSTED");
        }
        if ((i & 16384) != 0) {
            sb.append(", FLAG_OWN_DISPLAY_GROUP");
        }
        if ((32768 & i) != 0) {
            sb.append(", FLAG_ALWAYS_UNLOCKED");
        }
        if ((65536 & i) != 0) {
            sb.append(", FLAG_TOUCH_FEEDBACK_DISABLED");
        }
        if ((131072 & i) != 0) {
            sb.append(", FLAG_OWN_FOCUS");
        }
        if ((i & 524288) != 0) {
            sb.append(", FLAG_STEAL_TOP_FOCUS_DISABLED");
        }
        return sb.toString();
    }
}
