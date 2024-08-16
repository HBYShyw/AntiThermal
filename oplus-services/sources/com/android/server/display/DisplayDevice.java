package com.android.server.display;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayViewport;
import android.os.IBinder;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayAddress;
import android.view.Surface;
import android.view.SurfaceControl;
import com.android.server.display.mode.DisplayModeDirector;
import java.io.PrintWriter;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class DisplayDevice {
    private static final Display.Mode EMPTY_DISPLAY_MODE = new Display.Mode.Builder().build();
    private static final String TAG = "DisplayDevice";
    private final Context mContext;
    private Rect mCurrentDisplayRect;
    private Rect mCurrentLayerStackRect;
    private Surface mCurrentSurface;
    DisplayDeviceInfo mDebugLastLoggedDeviceInfo;
    private final DisplayAdapter mDisplayAdapter;
    private final IBinder mDisplayToken;
    private final String mUniqueId;
    private int mCurrentLayerStack = -2;
    private int mCurrentFlags = 0;
    private int mCurrentOrientation = -1;
    public IDisplayDeviceExt mDisplayDeviceExt = (IDisplayDeviceExt) ExtLoader.type(IDisplayDeviceExt.class).base(this).create();
    protected DisplayDeviceConfig mDisplayDeviceConfig = null;

    public void applyPendingDisplayDeviceInfoChangesLocked() {
    }

    public abstract DisplayDeviceInfo getDisplayDeviceInfoLocked();

    public int getDisplayIdToMirrorLocked() {
        return 0;
    }

    public abstract boolean hasStableUniqueId();

    public boolean isWindowManagerMirroringLocked() {
        return false;
    }

    public void onOverlayChangedLocked() {
    }

    public void performTraversalLocked(SurfaceControl.Transaction transaction) {
    }

    public Runnable requestDisplayStateLocked(int i, float f, float f2) {
        return null;
    }

    public void setAutoLowLatencyModeLocked(boolean z) {
    }

    public void setDesiredDisplayModeSpecsLocked(DisplayModeDirector.DesiredDisplayModeSpecs desiredDisplayModeSpecs) {
    }

    public void setGameContentTypeLocked(boolean z) {
    }

    public void setRequestedColorModeLocked(int i) {
    }

    public void setUserPreferredDisplayModeLocked(Display.Mode mode) {
    }

    public void setWindowManagerMirroringLocked(boolean z) {
    }

    public DisplayDevice(DisplayAdapter displayAdapter, IBinder iBinder, String str, Context context) {
        this.mDisplayAdapter = displayAdapter;
        this.mDisplayToken = iBinder;
        this.mUniqueId = str;
        this.mContext = context;
    }

    public final DisplayAdapter getAdapterLocked() {
        return this.mDisplayAdapter;
    }

    public DisplayDeviceConfig getDisplayDeviceConfig() {
        if (this.mDisplayDeviceConfig == null) {
            this.mDisplayDeviceConfig = loadDisplayDeviceConfig();
        }
        return this.mDisplayDeviceConfig;
    }

    public final IBinder getDisplayTokenLocked() {
        return this.mDisplayToken;
    }

    public Point getDisplaySurfaceDefaultSizeLocked() {
        DisplayDeviceInfo displayDeviceInfoLocked = getDisplayDeviceInfoLocked();
        int i = this.mCurrentOrientation;
        boolean z = i == 0 || i == 2;
        return new Point(z ? displayDeviceInfoLocked.width : displayDeviceInfoLocked.height, z ? displayDeviceInfoLocked.height : displayDeviceInfoLocked.width);
    }

    public final String getNameLocked() {
        return getDisplayDeviceInfoLocked().name;
    }

    public final String getUniqueId() {
        return this.mUniqueId;
    }

    public Display.Mode getUserPreferredDisplayModeLocked() {
        return EMPTY_DISPLAY_MODE;
    }

    public Display.Mode getSystemPreferredDisplayModeLocked() {
        return EMPTY_DISPLAY_MODE;
    }

    public Display.Mode getActiveDisplayModeAtStartLocked() {
        return EMPTY_DISPLAY_MODE;
    }

    public final void setLayerStackLocked(SurfaceControl.Transaction transaction, int i, int i2) {
        Slog.d(TAG, "setLayerStackLocked id=" + this.mUniqueId + " token=" + this.mDisplayToken + " stack=" + this.mCurrentLayerStack + "->" + i);
        if (this.mCurrentLayerStack != i) {
            this.mCurrentLayerStack = i;
            this.mDisplayDeviceExt.setLayerStack(i);
            transaction.setDisplayLayerStack(this.mDisplayToken, i);
            Slog.i(TAG, "[" + i2 + "] Layerstack set to " + i + " for " + this.mUniqueId);
        }
    }

    public final void setDisplayFlagsLocked(SurfaceControl.Transaction transaction, int i) {
        if (this.mCurrentFlags != i) {
            this.mCurrentFlags = i;
            transaction.setDisplayFlags(this.mDisplayToken, i);
        }
    }

    public void setProjectionLocked(SurfaceControl.Transaction transaction, int i, Rect rect, Rect rect2) {
        Rect rect3;
        Rect rect4;
        if (this.mCurrentOrientation == i && (rect3 = this.mCurrentLayerStackRect) != null && rect3.equals(rect) && (rect4 = this.mCurrentDisplayRect) != null && rect4.equals(rect2)) {
            return;
        }
        this.mCurrentOrientation = i;
        if (this.mCurrentLayerStackRect == null) {
            this.mCurrentLayerStackRect = new Rect();
        }
        this.mCurrentLayerStackRect.set(rect);
        if (this.mCurrentDisplayRect == null) {
            this.mCurrentDisplayRect = new Rect();
        }
        this.mCurrentDisplayRect.set(rect2);
        transaction.setDisplayProjection(this.mDisplayToken, i, rect, rect2);
    }

    public final void setSurfaceLocked(SurfaceControl.Transaction transaction, Surface surface) {
        if (!this.mDisplayDeviceExt.shouldSetDisplayDeviceSurface(this)) {
            Slog.d(TAG, "Skip setSurface for privacy.");
        } else if (this.mCurrentSurface != surface) {
            this.mCurrentSurface = surface;
            transaction.setDisplaySurface(this.mDisplayToken, surface);
            this.mDisplayDeviceExt.cacheSurfaceForDisplay(this, surface);
        }
    }

    public final void populateViewportLocked(DisplayViewport displayViewport) {
        displayViewport.orientation = this.mCurrentOrientation;
        Rect rect = this.mCurrentLayerStackRect;
        if (rect != null) {
            displayViewport.logicalFrame.set(rect);
        } else {
            displayViewport.logicalFrame.setEmpty();
        }
        Rect rect2 = this.mCurrentDisplayRect;
        if (rect2 != null) {
            displayViewport.physicalFrame.set(rect2);
        } else {
            displayViewport.physicalFrame.setEmpty();
        }
        int i = this.mCurrentOrientation;
        boolean z = true;
        if (i != 1 && i != 3) {
            z = false;
        }
        DisplayDeviceInfo displayDeviceInfoLocked = getDisplayDeviceInfoLocked();
        displayViewport.deviceWidth = z ? displayDeviceInfoLocked.height : displayDeviceInfoLocked.width;
        displayViewport.deviceHeight = z ? displayDeviceInfoLocked.width : displayDeviceInfoLocked.height;
        displayViewport.uniqueId = displayDeviceInfoLocked.uniqueId;
        DisplayAddress.Physical physical = displayDeviceInfoLocked.address;
        if (physical instanceof DisplayAddress.Physical) {
            displayViewport.physicalPort = Integer.valueOf(physical.getPort());
        } else {
            displayViewport.physicalPort = null;
        }
    }

    public void dumpLocked(PrintWriter printWriter) {
        printWriter.println("mAdapter=" + this.mDisplayAdapter.getName());
        printWriter.println("mUniqueId=" + this.mUniqueId);
        printWriter.println("mDisplayToken=" + this.mDisplayToken);
        printWriter.println("mCurrentLayerStack=" + this.mCurrentLayerStack);
        printWriter.println("mCurrentFlags=" + this.mCurrentFlags);
        printWriter.println("mCurrentOrientation=" + this.mCurrentOrientation);
        printWriter.println("mCurrentLayerStackRect=" + this.mCurrentLayerStackRect);
        printWriter.println("mCurrentDisplayRect=" + this.mCurrentDisplayRect);
        printWriter.println("mCurrentSurface=" + this.mCurrentSurface);
    }

    public String toString() {
        return "[id=" + this.mUniqueId + ",stack=" + this.mCurrentLayerStack + "]";
    }

    public IDisplayDeviceExt getExtImpl() {
        return this.mDisplayDeviceExt;
    }

    private DisplayDeviceConfig loadDisplayDeviceConfig() {
        return DisplayDeviceConfig.create(this.mContext, false);
    }
}
