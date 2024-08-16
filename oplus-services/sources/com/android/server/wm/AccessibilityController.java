package com.android.server.wm;

import android.R;
import android.accessibilityservice.AccessibilityTrace;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManagerInternal;
import android.graphics.BLASTBufferQueue;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.ArraySet;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.util.proto.ProtoOutputStream;
import android.view.Display;
import android.view.MagnificationSpec;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.ViewConfiguration;
import android.view.WindowInfo;
import android.view.animation.DecelerateInterpolator;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.TraceBuffer;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.LocalServices;
import com.android.server.wm.AccessibilityController;
import com.android.server.wm.AccessibilityWindowsPopulator;
import com.android.server.wm.WindowManagerInternal;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class AccessibilityController {
    private static final int OPLUS_MIRAGE_TV_DISPLAY_ID = 2020;
    private static final String TAG = "AccessibilityController";
    private final AccessibilityControllerInternalImpl mAccessibilityTracing;
    private final AccessibilityWindowsPopulator mAccessibilityWindowsPopulator;
    private final WindowManagerService mService;
    private static final Object STATIC_LOCK = new Object();
    private static final Rect EMPTY_RECT = new Rect();
    private static final float[] sTempFloats = new float[9];
    private final SparseArray<DisplayMagnifier> mDisplayMagnifiers = new SparseArray<>();
    private final SparseArray<WindowsForAccessibilityObserver> mWindowsForAccessibilityObserver = new SparseArray<>();
    private SparseArray<IBinder> mFocusedWindow = new SparseArray<>();
    private int mFocusedDisplay = -1;
    private final SparseBooleanArray mIsImeVisibleArray = new SparseBooleanArray();
    private boolean mAllObserversInitialized = true;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AccessibilityControllerInternalImpl getAccessibilityControllerInternal(WindowManagerService windowManagerService) {
        return AccessibilityControllerInternalImpl.getInstance(windowManagerService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccessibilityController(WindowManagerService windowManagerService) {
        this.mService = windowManagerService;
        this.mAccessibilityTracing = getAccessibilityControllerInternal(windowManagerService);
        this.mAccessibilityWindowsPopulator = new AccessibilityWindowsPopulator(windowManagerService, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setMagnificationCallbacks(int i, WindowManagerInternal.MagnificationCallbacks magnificationCallbacks) {
        Display display;
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".setMagnificationCallbacks", 2048L, "displayId=" + i + "; callbacks={" + magnificationCallbacks + "}");
        }
        if (magnificationCallbacks != null) {
            if (this.mDisplayMagnifiers.get(i) != null) {
                throw new IllegalStateException("Magnification callbacks already set!");
            }
            DisplayContent displayContent = this.mService.mRoot.getDisplayContent(i);
            if (displayContent == null || (display = displayContent.getDisplay()) == null || display.getType() == 4) {
                return false;
            }
            DisplayMagnifier displayMagnifier = new DisplayMagnifier(this.mService, displayContent, display, magnificationCallbacks);
            displayMagnifier.notifyImeWindowVisibilityChanged(this.mIsImeVisibleArray.get(i, false));
            this.mDisplayMagnifiers.put(i, displayMagnifier);
            return true;
        }
        DisplayMagnifier displayMagnifier2 = this.mDisplayMagnifiers.get(i);
        if (displayMagnifier2 == null) {
            throw new IllegalStateException("Magnification callbacks already cleared!");
        }
        displayMagnifier2.destroy();
        this.mDisplayMagnifiers.remove(i);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowsForAccessibilityCallback(int i, WindowManagerInternal.WindowsForAccessibilityCallback windowsForAccessibilityCallback) {
        if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".setWindowsForAccessibilityCallback", 1024L, "displayId=" + i + "; callback={" + windowsForAccessibilityCallback + "}");
        }
        if (i == 2020) {
            Slog.d("WindowManager", "Skip setWindowsForAccessibilityCallback for mirage tv display");
            return;
        }
        if (windowsForAccessibilityCallback != null) {
            if (this.mWindowsForAccessibilityObserver.get(i) != null) {
                String str = "Windows for accessibility callback of display " + i + " already set!";
                Slog.e(TAG, str);
                if (Build.IS_DEBUGGABLE) {
                    throw new IllegalStateException(str);
                }
                this.mWindowsForAccessibilityObserver.remove(i);
            }
            this.mAccessibilityWindowsPopulator.setWindowsNotification(true);
            WindowsForAccessibilityObserver windowsForAccessibilityObserver = new WindowsForAccessibilityObserver(this.mService, i, windowsForAccessibilityCallback, this.mAccessibilityWindowsPopulator);
            this.mWindowsForAccessibilityObserver.put(i, windowsForAccessibilityObserver);
            this.mAllObserversInitialized &= windowsForAccessibilityObserver.mInitialized;
            return;
        }
        if (this.mWindowsForAccessibilityObserver.get(i) == null) {
            String str2 = "Windows for accessibility callback of display " + i + " already cleared!";
            Slog.e(TAG, str2);
            if (Build.IS_DEBUGGABLE) {
                throw new IllegalStateException(str2);
            }
        }
        this.mWindowsForAccessibilityObserver.remove(i);
        if (this.mWindowsForAccessibilityObserver.size() <= 0) {
            this.mAccessibilityWindowsPopulator.setWindowsNotification(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void performComputeChangedWindowsNot(int i, boolean z) {
        WindowsForAccessibilityObserver windowsForAccessibilityObserver;
        if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".performComputeChangedWindowsNot", 1024L, "displayId=" + i + "; forceSend=" + z);
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                windowsForAccessibilityObserver = this.mWindowsForAccessibilityObserver.get(i);
                if (windowsForAccessibilityObserver == null) {
                    windowsForAccessibilityObserver = null;
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        if (windowsForAccessibilityObserver != null) {
            windowsForAccessibilityObserver.performComputeChangedWindows(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMagnificationSpec(int i, MagnificationSpec magnificationSpec) {
        if (this.mAccessibilityTracing.isTracingEnabled(3072L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".setMagnificationSpec", 3072L, "displayId=" + i + "; spec={" + magnificationSpec + "}");
        }
        this.mAccessibilityWindowsPopulator.setMagnificationSpec(i, magnificationSpec);
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(i);
        if (displayMagnifier != null) {
            displayMagnifier.setMagnificationSpec(magnificationSpec);
        }
        WindowsForAccessibilityObserver windowsForAccessibilityObserver = this.mWindowsForAccessibilityObserver.get(i);
        if (windowsForAccessibilityObserver != null) {
            windowsForAccessibilityObserver.scheduleComputeChangedWindows();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getMagnificationRegion(int i, Region region) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".getMagnificationRegion", 2048L, "displayId=" + i + "; outMagnificationRegion={" + region + "}");
        }
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(i);
        if (displayMagnifier != null) {
            displayMagnifier.getMagnificationRegion(region);
        }
    }

    @VisibleForTesting
    Surface forceShowMagnifierWindow(int i) {
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(i);
        if (displayMagnifier == null) {
            return null;
        }
        displayMagnifier.mMagnifedViewport.mWindow.setAlpha(255);
        return displayMagnifier.mMagnifedViewport.mWindow.mSurface;
    }

    void onWindowLayersChanged(int i) {
        if (this.mAccessibilityTracing.isTracingEnabled(3072L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onWindowLayersChanged", 3072L, "displayId=" + i);
        }
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(i);
        if (displayMagnifier != null) {
            displayMagnifier.onWindowLayersChanged();
        }
        WindowsForAccessibilityObserver windowsForAccessibilityObserver = this.mWindowsForAccessibilityObserver.get(i);
        if (windowsForAccessibilityObserver != null) {
            windowsForAccessibilityObserver.scheduleComputeChangedWindows();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDisplaySizeChanged(DisplayContent displayContent) {
        if (this.mAccessibilityTracing.isTracingEnabled(3072L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onRotationChanged", 3072L, "displayContent={" + displayContent + "}");
        }
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(displayContent.getDisplayId());
        if (displayMagnifier != null) {
            displayMagnifier.onDisplaySizeChanged(displayContent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAppWindowTransition(int i, int i2) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onAppWindowTransition", 2048L, "displayId=" + i + "; transition=" + i2);
        }
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(i);
        if (displayMagnifier != null) {
            displayMagnifier.onAppWindowTransition(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWMTransition(int i, int i2) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onAppWindowTransition", 2048L, "displayId=" + i + "; type=" + i2);
        }
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(i);
        if (displayMagnifier != null) {
            displayMagnifier.onWMTransition(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWindowTransition(WindowState windowState, int i) {
        if (this.mAccessibilityTracing.isTracingEnabled(3072L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onWindowTransition", 3072L, "windowState={" + windowState + "}; transition=" + i);
        }
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(windowState.getDisplayId());
        if (displayMagnifier != null) {
            displayMagnifier.onWindowTransition(windowState, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWindowFocusChangedNot(int i) {
        WindowsForAccessibilityObserver windowsForAccessibilityObserver;
        if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onWindowFocusChangedNot", 1024L, "displayId=" + i);
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                windowsForAccessibilityObserver = this.mWindowsForAccessibilityObserver.get(i);
                if (windowsForAccessibilityObserver == null) {
                    windowsForAccessibilityObserver = null;
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        if (windowsForAccessibilityObserver != null) {
            windowsForAccessibilityObserver.performComputeChangedWindows(false);
        }
        sendCallbackToUninitializedObserversIfNeeded();
    }

    private void sendCallbackToUninitializedObserversIfNeeded() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mAllObserversInitialized) {
                    return;
                }
                if (this.mService.mRoot.getTopFocusedDisplayContent().mCurrentFocus == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                ArrayList arrayList = new ArrayList();
                for (int size = this.mWindowsForAccessibilityObserver.size() - 1; size >= 0; size--) {
                    WindowsForAccessibilityObserver valueAt = this.mWindowsForAccessibilityObserver.valueAt(size);
                    if (!valueAt.mInitialized) {
                        arrayList.add(valueAt);
                    }
                }
                this.mAllObserversInitialized = true;
                WindowManagerService.resetPriorityAfterLockedSection();
                boolean z = true;
                for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                    WindowsForAccessibilityObserver windowsForAccessibilityObserver = (WindowsForAccessibilityObserver) arrayList.get(size2);
                    windowsForAccessibilityObserver.performComputeChangedWindows(true);
                    z &= windowsForAccessibilityObserver.mInitialized;
                }
                WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock2) {
                    try {
                        this.mAllObserversInitialized &= z;
                    } finally {
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            } finally {
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSomeWindowResizedOrMoved(int... iArr) {
        onSomeWindowResizedOrMovedWithCallingUid(Binder.getCallingUid(), iArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSomeWindowResizedOrMovedWithCallingUid(int i, int... iArr) {
        if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".onSomeWindowResizedOrMoved", 1024L, "displayIds={" + Arrays.toString(iArr) + "}", "".getBytes(), i);
        }
        for (int i2 : iArr) {
            WindowsForAccessibilityObserver windowsForAccessibilityObserver = this.mWindowsForAccessibilityObserver.get(i2);
            if (windowsForAccessibilityObserver != null) {
                windowsForAccessibilityObserver.scheduleComputeChangedWindows();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void drawMagnifiedRegionBorderIfNeeded(int i) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".drawMagnifiedRegionBorderIfNeeded", 2048L, "displayId=" + i);
        }
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(i);
        if (displayMagnifier != null) {
            displayMagnifier.drawMagnifiedRegionBorderIfNeeded();
        }
    }

    public Pair<Matrix, MagnificationSpec> getWindowTransformationMatrixAndMagnificationSpec(IBinder iBinder) {
        Pair<Matrix, MagnificationSpec> pair;
        MagnificationSpec magnificationSpecForWindow;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Matrix matrix = new Matrix();
                MagnificationSpec magnificationSpec = new MagnificationSpec();
                WindowState windowState = this.mService.mWindowMap.get(iBinder);
                if (windowState != null) {
                    windowState.getTransformationMatrix(new float[9], matrix);
                    if (hasCallbacks() && (magnificationSpecForWindow = getMagnificationSpecForWindow(windowState)) != null && !magnificationSpecForWindow.isNop()) {
                        magnificationSpec.setTo(magnificationSpecForWindow);
                    }
                }
                pair = new Pair<>(matrix, magnificationSpec);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return pair;
    }

    MagnificationSpec getMagnificationSpecForWindow(WindowState windowState) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".getMagnificationSpecForWindow", 2048L, "windowState={" + windowState + "}");
        }
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(windowState.getDisplayId());
        if (displayMagnifier != null) {
            return displayMagnifier.getMagnificationSpecForWindow(windowState);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasCallbacks() {
        if (this.mAccessibilityTracing.isTracingEnabled(3072L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".hasCallbacks", 3072L);
        }
        return this.mDisplayMagnifiers.size() > 0 || this.mWindowsForAccessibilityObserver.size() > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForceShowMagnifiableBounds(int i, boolean z) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".setForceShowMagnifiableBounds", 2048L, "displayId=" + i + "; show=" + z);
        }
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(i);
        if (displayMagnifier != null) {
            displayMagnifier.setForceShowMagnifiableBounds(z);
            displayMagnifier.showMagnificationBoundsIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateImeVisibilityIfNeeded(int i, boolean z) {
        if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
            this.mAccessibilityTracing.logTrace(TAG + ".updateImeVisibilityIfNeeded", 2048L, "displayId=" + i + ";shown=" + z);
        }
        if (this.mIsImeVisibleArray.get(i, false) == z) {
            return;
        }
        this.mIsImeVisibleArray.put(i, z);
        DisplayMagnifier displayMagnifier = this.mDisplayMagnifiers.get(i);
        if (displayMagnifier != null) {
            displayMagnifier.notifyImeWindowVisibilityChanged(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void populateTransformationMatrix(WindowState windowState, Matrix matrix) {
        windowState.getTransformationMatrix(sTempFloats, matrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(final PrintWriter printWriter, final String str) {
        DumpUtils.dumpSparseArray(printWriter, str, this.mDisplayMagnifiers, "magnification display", new DumpUtils.KeyDumper() { // from class: com.android.server.wm.AccessibilityController$$ExternalSyntheticLambda0
            public final void dump(int i, int i2) {
                AccessibilityController.lambda$dump$0(printWriter, str, i, i2);
            }
        }, new DumpUtils.ValueDumper() { // from class: com.android.server.wm.AccessibilityController$$ExternalSyntheticLambda1
            public final void dump(Object obj) {
                ((AccessibilityController.DisplayMagnifier) obj).dump(printWriter, "");
            }
        });
        DumpUtils.dumpSparseArrayValues(printWriter, str, this.mWindowsForAccessibilityObserver, "windows for accessibility observer");
        this.mAccessibilityWindowsPopulator.dump(printWriter, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$0(PrintWriter printWriter, String str, int i, int i2) {
        printWriter.printf("%sDisplay #%d:", str + "  ", Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onFocusChanged(InputTarget inputTarget, InputTarget inputTarget2) {
        if (inputTarget != null) {
            this.mFocusedWindow.remove(inputTarget.getDisplayId());
        }
        if (inputTarget2 != null) {
            this.mFocusedWindow.put(inputTarget2.getDisplayId(), inputTarget2.getIWindow().asBinder());
        }
    }

    public void onDisplayRemoved(int i) {
        this.mIsImeVisibleArray.delete(i);
        this.mFocusedWindow.remove(i);
    }

    public void setFocusedDisplay(int i) {
        this.mFocusedDisplay = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder getFocusedWindowToken() {
        return this.mFocusedWindow.get(this.mFocusedDisplay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class DisplayMagnifier {
        private static final boolean DEBUG_DISPLAY_SIZE = false;
        private static final boolean DEBUG_LAYERS = false;
        private static final boolean DEBUG_RECTANGLE_REQUESTED = false;
        private static final boolean DEBUG_VIEWPORT_WINDOW = false;
        private static final boolean DEBUG_WINDOW_TRANSITIONS = false;
        private static final String LOG_TAG = "WindowManager";
        private final AccessibilityControllerInternalImpl mAccessibilityTracing;
        private final WindowManagerInternal.MagnificationCallbacks mCallbacks;
        private final Display mDisplay;
        private final DisplayContent mDisplayContent;
        private final Context mDisplayContext;
        private final Handler mHandler;
        private final long mLongAnimationDuration;
        private final WindowManagerService mService;
        private final Rect mTempRect1 = new Rect();
        private final Rect mTempRect2 = new Rect();
        private final Region mTempRegion1 = new Region();
        private final Region mTempRegion2 = new Region();
        private final Region mTempRegion3 = new Region();
        private final Region mTempRegion4 = new Region();
        private boolean mForceShowMagnifiableBounds = false;
        private final MagnifiedViewport mMagnifedViewport = new MagnifiedViewport();

        DisplayMagnifier(WindowManagerService windowManagerService, DisplayContent displayContent, Display display, WindowManagerInternal.MagnificationCallbacks magnificationCallbacks) {
            this.mDisplayContext = windowManagerService.mContext.createDisplayContext(display);
            this.mService = windowManagerService;
            this.mCallbacks = magnificationCallbacks;
            this.mDisplayContent = displayContent;
            this.mDisplay = display;
            this.mHandler = new MyHandler(windowManagerService.mH.getLooper());
            AccessibilityControllerInternalImpl accessibilityControllerInternal = AccessibilityController.getAccessibilityControllerInternal(windowManagerService);
            this.mAccessibilityTracing = accessibilityControllerInternal;
            this.mLongAnimationDuration = r0.getResources().getInteger(R.integer.config_longAnimTime);
            if (accessibilityControllerInternal.isTracingEnabled(2048L)) {
                accessibilityControllerInternal.logTrace("WindowManager.DisplayMagnifier.constructor", 2048L, "windowManagerService={" + windowManagerService + "}; displayContent={" + displayContent + "}; display={" + display + "}; callbacks={" + magnificationCallbacks + "}");
            }
        }

        void setMagnificationSpec(MagnificationSpec magnificationSpec) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.setMagnificationSpec", 2048L, "spec={" + magnificationSpec + "}");
            }
            this.mMagnifedViewport.updateMagnificationSpec(magnificationSpec);
            this.mMagnifedViewport.recomputeBounds();
            this.mService.applyMagnificationSpecLocked(this.mDisplay.getDisplayId(), magnificationSpec);
            this.mService.scheduleAnimationLocked();
        }

        void setForceShowMagnifiableBounds(boolean z) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.setForceShowMagnifiableBounds", 2048L, "show=" + z);
            }
            this.mForceShowMagnifiableBounds = z;
            this.mMagnifedViewport.setMagnifiedRegionBorderShown(z, true);
        }

        boolean isForceShowingMagnifiableBounds() {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.isForceShowingMagnifiableBounds", 2048L);
            }
            return this.mForceShowMagnifiableBounds;
        }

        void onWindowLayersChanged() {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.onWindowLayersChanged", 2048L);
            }
            this.mMagnifedViewport.recomputeBounds();
            this.mService.scheduleAnimationLocked();
        }

        void onDisplaySizeChanged(DisplayContent displayContent) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.onDisplaySizeChanged", 2048L, "displayContent={" + displayContent + "}");
            }
            this.mMagnifedViewport.onDisplaySizeChanged();
            this.mHandler.sendEmptyMessage(4);
        }

        void onAppWindowTransition(int i, int i2) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.onAppWindowTransition", 2048L, "displayId=" + i + "; transition=" + i2);
            }
            if (isForceShowingMagnifiableBounds()) {
                if (i2 != 6 && i2 != 8 && i2 != 10 && i2 != 28) {
                    switch (i2) {
                        case 12:
                        case 13:
                        case 14:
                            break;
                        default:
                            return;
                    }
                }
                this.mHandler.sendEmptyMessage(3);
            }
        }

        void onWMTransition(int i, int i2) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.onWMTransition", 2048L, "displayId=" + i + "; type=" + i2);
            }
            if (isForceShowingMagnifiableBounds()) {
                if (i2 == 1 || i2 == 2 || i2 == 3 || i2 == 4) {
                    this.mHandler.sendEmptyMessage(3);
                }
            }
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:25:0x0056. Please report as an issue. */
        /* JADX WARN: Failed to find 'out' block for switch in B:26:0x0059. Please report as an issue. */
        /* JADX WARN: Removed duplicated region for block: B:29:0x0060 A[FALL_THROUGH] */
        /* JADX WARN: Removed duplicated region for block: B:31:0x0079  */
        /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        void onWindowTransition(WindowState windowState, int i) {
            Rect rect;
            Rect rect2;
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.onWindowTransition", 2048L, "windowState={" + windowState + "}; transition=" + i);
            }
            boolean isForceShowingMagnifiableBounds = isForceShowingMagnifiableBounds();
            int i2 = windowState.mAttrs.type;
            if ((i != 1 && i != 3) || !isForceShowingMagnifiableBounds) {
                return;
            }
            if (i2 != 2 && i2 != 4 && i2 != 1005 && i2 != 2020 && i2 != 2024 && i2 != 2035 && i2 != 2038) {
                switch (i2) {
                    default:
                        switch (i2) {
                            default:
                                switch (i2) {
                                    case 2005:
                                    case 2006:
                                    case 2007:
                                    case 2008:
                                    case 2009:
                                    case 2010:
                                        break;
                                    default:
                                        return;
                                }
                            case 2001:
                            case 2002:
                            case 2003:
                                rect = this.mTempRect2;
                                this.mMagnifedViewport.getMagnifiedFrameInContentCoords(rect);
                                rect2 = this.mTempRect1;
                                windowState.getTouchableRegion(this.mTempRegion1);
                                this.mTempRegion1.getBounds(rect2);
                                if (rect.intersect(rect2)) {
                                    this.mCallbacks.onRectangleOnScreenRequested(rect2.left, rect2.top, rect2.right, rect2.bottom);
                                    return;
                                }
                                return;
                        }
                    case 1000:
                    case 1001:
                    case 1002:
                    case 1003:
                        break;
                }
            }
            rect = this.mTempRect2;
            this.mMagnifedViewport.getMagnifiedFrameInContentCoords(rect);
            rect2 = this.mTempRect1;
            windowState.getTouchableRegion(this.mTempRegion1);
            this.mTempRegion1.getBounds(rect2);
            if (rect.intersect(rect2)) {
            }
        }

        void notifyImeWindowVisibilityChanged(boolean z) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.notifyImeWindowVisibilityChanged", 2048L, "shown=" + z);
            }
            this.mHandler.obtainMessage(6, z ? 1 : 0, 0).sendToTarget();
        }

        MagnificationSpec getMagnificationSpecForWindow(WindowState windowState) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.getMagnificationSpecForWindow", 2048L, "windowState={" + windowState + "}");
            }
            MagnificationSpec magnificationSpec = this.mMagnifedViewport.getMagnificationSpec();
            if (magnificationSpec == null || magnificationSpec.isNop() || windowState.shouldMagnify()) {
                return magnificationSpec;
            }
            return null;
        }

        void getMagnificationRegion(Region region) {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.getMagnificationRegion", 2048L, "outMagnificationRegion={" + region + "}");
            }
            this.mMagnifedViewport.recomputeBounds();
            this.mMagnifedViewport.getMagnificationRegion(region);
        }

        void destroy() {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.destroy", 2048L);
            }
            this.mMagnifedViewport.destroyWindow();
        }

        void showMagnificationBoundsIfNeeded() {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.showMagnificationBoundsIfNeeded", 2048L);
            }
            this.mHandler.obtainMessage(5).sendToTarget();
        }

        void drawMagnifiedRegionBorderIfNeeded() {
            if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.drawMagnifiedRegionBorderIfNeeded", 2048L);
            }
            this.mMagnifedViewport.drawWindowIfNeeded();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void dump(PrintWriter printWriter, String str) {
            this.mMagnifedViewport.dump(printWriter, str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public final class MagnifiedViewport {
            private final float mBorderWidth;
            private final Path mCircularPath;
            private final int mDrawBorderInset;
            private boolean mFullRedrawNeeded;
            private final int mHalfBorderWidth;
            private final Region mMagnificationRegion;
            private final MagnificationSpec mMagnificationSpec;
            private final Region mOldMagnificationRegion;
            private final Point mScreenSize;
            private int mTempLayer;
            private final Matrix mTempMatrix;
            private final ViewportWindow mWindow;
            private final SparseArray<WindowState> mTempWindowStates = new SparseArray<>();
            private final RectF mTempRectF = new RectF();

            private boolean isExcludedWindowType(int i) {
                return i == 2027 || i == 2039;
            }

            MagnifiedViewport() {
                Point point = new Point();
                this.mScreenSize = point;
                this.mTempMatrix = new Matrix();
                this.mMagnificationRegion = new Region();
                this.mOldMagnificationRegion = new Region();
                this.mMagnificationSpec = new MagnificationSpec();
                this.mTempLayer = 0;
                float dimension = DisplayMagnifier.this.mDisplayContext.getResources().getDimension(R.dimen.action_bar_default_height);
                this.mBorderWidth = dimension;
                this.mHalfBorderWidth = (int) Math.ceil(dimension / 2.0f);
                this.mDrawBorderInset = ((int) dimension) / 2;
                this.mWindow = new ViewportWindow(DisplayMagnifier.this.mDisplayContext);
                if (DisplayMagnifier.this.mDisplayContext.getResources().getConfiguration().isScreenRound()) {
                    Path path = new Path();
                    this.mCircularPath = path;
                    getDisplaySizeLocked(point);
                    float f = point.x / 2;
                    path.addCircle(f, f, f, Path.Direction.CW);
                } else {
                    this.mCircularPath = null;
                }
                recomputeBounds();
            }

            void getMagnificationRegion(Region region) {
                region.set(this.mMagnificationRegion);
            }

            void updateMagnificationSpec(MagnificationSpec magnificationSpec) {
                if (magnificationSpec != null) {
                    this.mMagnificationSpec.initialize(magnificationSpec.scale, magnificationSpec.offsetX, magnificationSpec.offsetY);
                } else {
                    this.mMagnificationSpec.clear();
                }
                if (DisplayMagnifier.this.mHandler.hasMessages(5)) {
                    return;
                }
                setMagnifiedRegionBorderShown(DisplayMagnifier.this.isForceShowingMagnifiableBounds(), true);
            }

            void recomputeBounds() {
                getDisplaySizeLocked(this.mScreenSize);
                Point point = this.mScreenSize;
                int i = point.x;
                int i2 = point.y;
                this.mMagnificationRegion.set(0, 0, 0, 0);
                Region region = DisplayMagnifier.this.mTempRegion1;
                region.set(0, 0, i, i2);
                Path path = this.mCircularPath;
                if (path != null) {
                    region.setPath(path, region);
                }
                Region region2 = DisplayMagnifier.this.mTempRegion4;
                region2.set(0, 0, 0, 0);
                SparseArray<WindowState> sparseArray = this.mTempWindowStates;
                sparseArray.clear();
                populateWindowsOnScreen(sparseArray);
                for (int size = sparseArray.size() - 1; size >= 0; size--) {
                    WindowState valueAt = sparseArray.valueAt(size);
                    if (!isExcludedWindowType(valueAt.mAttrs.type)) {
                        int i3 = valueAt.mAttrs.privateFlags;
                        if ((2097152 & i3) == 0 && (i3 & 1048576) == 0) {
                            Matrix matrix = this.mTempMatrix;
                            AccessibilityController.populateTransformationMatrix(valueAt, matrix);
                            Region region3 = DisplayMagnifier.this.mTempRegion3;
                            valueAt.getTouchableRegion(region3);
                            Rect rect = DisplayMagnifier.this.mTempRect1;
                            region3.getBounds(rect);
                            RectF rectF = this.mTempRectF;
                            rectF.set(rect);
                            rectF.offset(-valueAt.getFrame().left, -valueAt.getFrame().top);
                            matrix.mapRect(rectF);
                            Region region4 = DisplayMagnifier.this.mTempRegion2;
                            region4.set((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                            Region region5 = DisplayMagnifier.this.mTempRegion3;
                            region5.set(this.mMagnificationRegion);
                            region5.op(region2, Region.Op.UNION);
                            region4.op(region5, Region.Op.DIFFERENCE);
                            if (valueAt.shouldMagnify()) {
                                this.mMagnificationRegion.op(region4, Region.Op.UNION);
                                this.mMagnificationRegion.op(region, Region.Op.INTERSECT);
                            } else {
                                region2.op(region4, Region.Op.UNION);
                                region.op(region4, Region.Op.DIFFERENCE);
                            }
                            if (AccessibilityController.isUntouchableNavigationBar(valueAt, DisplayMagnifier.this.mTempRegion3)) {
                                Rect systemBarInsetsFrame = AccessibilityController.getSystemBarInsetsFrame(valueAt);
                                region2.op(systemBarInsetsFrame, Region.Op.UNION);
                                region.op(systemBarInsetsFrame, Region.Op.DIFFERENCE);
                            }
                            if (valueAt.areAppWindowBoundsLetterboxed()) {
                                Region letterboxBounds = getLetterboxBounds(valueAt);
                                region2.op(letterboxBounds, Region.Op.UNION);
                                region.op(letterboxBounds, Region.Op.DIFFERENCE);
                            }
                            Region region6 = DisplayMagnifier.this.mTempRegion2;
                            region6.set(this.mMagnificationRegion);
                            region6.op(region2, Region.Op.UNION);
                            region6.op(0, 0, i, i2, Region.Op.INTERSECT);
                            if (region6.isRect()) {
                                Rect rect2 = DisplayMagnifier.this.mTempRect1;
                                region6.getBounds(rect2);
                                if (rect2.width() == i && rect2.height() == i2) {
                                    break;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
                sparseArray.clear();
                Region region7 = this.mMagnificationRegion;
                int i4 = this.mDrawBorderInset;
                region7.op(i4, i4, i - i4, i2 - i4, Region.Op.INTERSECT);
                if (!this.mOldMagnificationRegion.equals(this.mMagnificationRegion)) {
                    this.mWindow.setBounds(this.mMagnificationRegion);
                    Rect rect3 = DisplayMagnifier.this.mTempRect1;
                    if (this.mFullRedrawNeeded) {
                        this.mFullRedrawNeeded = false;
                        int i5 = this.mDrawBorderInset;
                        rect3.set(i5, i5, i - i5, i2 - i5);
                        this.mWindow.invalidate(rect3);
                    } else {
                        Region region8 = DisplayMagnifier.this.mTempRegion3;
                        region8.set(this.mMagnificationRegion);
                        region8.op(this.mOldMagnificationRegion, Region.Op.XOR);
                        region8.getBounds(rect3);
                        this.mWindow.invalidate(rect3);
                    }
                    this.mOldMagnificationRegion.set(this.mMagnificationRegion);
                    SomeArgs obtain = SomeArgs.obtain();
                    obtain.arg1 = Region.obtain(this.mMagnificationRegion);
                    DisplayMagnifier.this.mHandler.obtainMessage(1, obtain).sendToTarget();
                }
            }

            private Region getLetterboxBounds(WindowState windowState) {
                ActivityRecord activityRecord = windowState.mActivityRecord;
                if (activityRecord == null) {
                    return new Region();
                }
                Rect bounds = windowState.getBounds();
                Rect letterboxInsets = activityRecord.getLetterboxInsets();
                Rect copyOrNull = Rect.copyOrNull(bounds);
                copyOrNull.inset(Insets.subtract(Insets.NONE, Insets.of(letterboxInsets)));
                Region region = new Region();
                region.set(copyOrNull);
                region.op(bounds, Region.Op.DIFFERENCE);
                return region;
            }

            void onDisplaySizeChanged() {
                if (DisplayMagnifier.this.isForceShowingMagnifiableBounds()) {
                    setMagnifiedRegionBorderShown(false, false);
                    DisplayMagnifier.this.mHandler.sendMessageDelayed(DisplayMagnifier.this.mHandler.obtainMessage(5), ((float) DisplayMagnifier.this.mLongAnimationDuration) * DisplayMagnifier.this.mService.getWindowAnimationScaleLocked());
                }
                recomputeBounds();
                this.mWindow.updateSize();
            }

            void setMagnifiedRegionBorderShown(boolean z, boolean z2) {
                if (this.mWindow.setShown(z, z2)) {
                    this.mFullRedrawNeeded = true;
                    this.mOldMagnificationRegion.set(0, 0, 0, 0);
                }
            }

            void getMagnifiedFrameInContentCoords(Rect rect) {
                MagnificationSpec magnificationSpec = this.mMagnificationSpec;
                this.mMagnificationRegion.getBounds(rect);
                rect.offset((int) (-magnificationSpec.offsetX), (int) (-magnificationSpec.offsetY));
                rect.scale(1.0f / magnificationSpec.scale);
            }

            boolean isMagnifying() {
                return this.mMagnificationSpec.scale > 1.0f;
            }

            MagnificationSpec getMagnificationSpec() {
                return this.mMagnificationSpec;
            }

            void drawWindowIfNeeded() {
                recomputeBounds();
                this.mWindow.postDrawIfNeeded();
            }

            void destroyWindow() {
                this.mWindow.releaseSurface();
            }

            private void populateWindowsOnScreen(final SparseArray<WindowState> sparseArray) {
                this.mTempLayer = 0;
                DisplayMagnifier.this.mDisplayContent.forAllWindows(new Consumer() { // from class: com.android.server.wm.AccessibilityController$DisplayMagnifier$MagnifiedViewport$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        AccessibilityController.DisplayMagnifier.MagnifiedViewport.this.lambda$populateWindowsOnScreen$0(sparseArray, (WindowState) obj);
                    }
                }, false);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$populateWindowsOnScreen$0(SparseArray sparseArray, WindowState windowState) {
                if (windowState.isOnScreen() && windowState.isVisible() && windowState.mAttrs.alpha != 0.0f) {
                    int i = this.mTempLayer + 1;
                    this.mTempLayer = i;
                    sparseArray.put(i, windowState);
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void getDisplaySizeLocked(Point point) {
                Rect bounds = DisplayMagnifier.this.mDisplayContent.getConfiguration().windowConfiguration.getBounds();
                point.set(bounds.width(), bounds.height());
            }

            void dump(PrintWriter printWriter, String str) {
                this.mWindow.dump(printWriter, str);
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
            public final class ViewportWindow implements Runnable {
                private static final String SURFACE_TITLE = "Magnification Overlay";
                private int mAlpha;
                private final AnimationController mAnimationController;
                private final BLASTBufferQueue mBlastBufferQueue;
                private volatile boolean mInvalidated;
                private boolean mLastSurfaceShown;
                private boolean mShown;
                private final Surface mSurface;
                private final SurfaceControl mSurfaceControl;
                private final SurfaceControl.Transaction mTransaction;
                private final Region mBounds = new Region();
                private final Rect mDirtyRect = new Rect();
                private final Paint mPaint = new Paint();

                ViewportWindow(Context context) {
                    SurfaceControl surfaceControl;
                    try {
                        surfaceControl = DisplayMagnifier.this.mDisplayContent.makeOverlay().setName(SURFACE_TITLE).setBLASTLayer().setFormat(-3).setCallsite("ViewportWindow").build();
                    } catch (Surface.OutOfResourcesException unused) {
                        surfaceControl = null;
                    }
                    this.mSurfaceControl = surfaceControl;
                    DisplayMagnifier.this.mDisplay.getRealSize(MagnifiedViewport.this.mScreenSize);
                    BLASTBufferQueue bLASTBufferQueue = new BLASTBufferQueue(SURFACE_TITLE, surfaceControl, MagnifiedViewport.this.mScreenSize.x, MagnifiedViewport.this.mScreenSize.y, 1);
                    this.mBlastBufferQueue = bLASTBufferQueue;
                    SurfaceControl.Transaction transaction = DisplayMagnifier.this.mService.mTransactionFactory.get();
                    transaction.setLayer(surfaceControl, DisplayMagnifier.this.mService.mPolicy.getWindowLayerFromTypeLw(2027) * 10000).setPosition(surfaceControl, 0.0f, 0.0f);
                    InputMonitor.setTrustedOverlayInputInfo(surfaceControl, transaction, DisplayMagnifier.this.mDisplayContent.getDisplayId(), SURFACE_TITLE);
                    transaction.apply();
                    this.mTransaction = transaction;
                    this.mSurface = bLASTBufferQueue.createSurface();
                    this.mAnimationController = new AnimationController(context, DisplayMagnifier.this.mService.mH.getLooper());
                    TypedValue typedValue = new TypedValue();
                    context.getTheme().resolveAttribute(R.attr.colorActivatedHighlight, typedValue, true);
                    int color = context.getColor(typedValue.resourceId);
                    this.mPaint.setStyle(Paint.Style.STROKE);
                    this.mPaint.setStrokeWidth(MagnifiedViewport.this.mBorderWidth);
                    this.mPaint.setColor(color);
                    this.mInvalidated = true;
                }

                boolean setShown(boolean z, boolean z2) {
                    WindowManagerGlobalLock windowManagerGlobalLock = DisplayMagnifier.this.mService.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            if (this.mShown == z) {
                                WindowManagerService.resetPriorityAfterLockedSection();
                                return false;
                            }
                            this.mShown = z;
                            this.mAnimationController.onFrameShownStateChanged(z, z2);
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return true;
                        } catch (Throwable th) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                }

                int getAlpha() {
                    int i;
                    WindowManagerGlobalLock windowManagerGlobalLock = DisplayMagnifier.this.mService.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            i = this.mAlpha;
                        } catch (Throwable th) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return i;
                }

                void setAlpha(int i) {
                    WindowManagerGlobalLock windowManagerGlobalLock = DisplayMagnifier.this.mService.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            if (this.mAlpha == i) {
                                WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            this.mAlpha = i;
                            invalidate(null);
                            WindowManagerService.resetPriorityAfterLockedSection();
                        } catch (Throwable th) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                }

                void setBounds(Region region) {
                    WindowManagerGlobalLock windowManagerGlobalLock = DisplayMagnifier.this.mService.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            if (this.mBounds.equals(region)) {
                                WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            this.mBounds.set(region);
                            invalidate(this.mDirtyRect);
                            WindowManagerService.resetPriorityAfterLockedSection();
                        } catch (Throwable th) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                }

                void updateSize() {
                    WindowManagerGlobalLock windowManagerGlobalLock = DisplayMagnifier.this.mService.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            MagnifiedViewport magnifiedViewport = MagnifiedViewport.this;
                            magnifiedViewport.getDisplaySizeLocked(magnifiedViewport.mScreenSize);
                            this.mBlastBufferQueue.update(this.mSurfaceControl, MagnifiedViewport.this.mScreenSize.x, MagnifiedViewport.this.mScreenSize.y, 1);
                            invalidate(this.mDirtyRect);
                        } catch (Throwable th) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                }

                void invalidate(Rect rect) {
                    if (rect != null) {
                        this.mDirtyRect.set(rect);
                    } else {
                        this.mDirtyRect.setEmpty();
                    }
                    this.mInvalidated = true;
                    DisplayMagnifier.this.mService.scheduleAnimationLocked();
                }

                void postDrawIfNeeded() {
                    if (this.mInvalidated) {
                        DisplayMagnifier.this.mService.mAnimationHandler.post(this);
                    }
                }

                @Override // java.lang.Runnable
                public void run() {
                    drawOrRemoveIfNeeded();
                }

                private void drawOrRemoveIfNeeded() {
                    Region region;
                    Rect rect;
                    boolean z;
                    WindowManagerGlobalLock windowManagerGlobalLock = DisplayMagnifier.this.mService.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            if (this.mBlastBufferQueue.mNativeObject == 0) {
                                if (this.mSurface.isValid()) {
                                    this.mTransaction.remove(this.mSurfaceControl).apply();
                                    this.mSurface.release();
                                }
                                WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            if (!this.mInvalidated) {
                                WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            this.mInvalidated = false;
                            int i = this.mAlpha;
                            Canvas canvas = null;
                            if (i > 0) {
                                region = new Region(this.mBounds);
                                if (this.mDirtyRect.isEmpty()) {
                                    this.mBounds.getBounds(this.mDirtyRect);
                                }
                                this.mDirtyRect.inset(-MagnifiedViewport.this.mHalfBorderWidth, -MagnifiedViewport.this.mHalfBorderWidth);
                                rect = new Rect(this.mDirtyRect);
                            } else {
                                region = null;
                                rect = null;
                            }
                            WindowManagerService.resetPriorityAfterLockedSection();
                            if (i > 0) {
                                try {
                                    canvas = this.mSurface.lockCanvas(rect);
                                } catch (Surface.OutOfResourcesException | IllegalArgumentException unused) {
                                }
                                if (canvas == null) {
                                    return;
                                }
                                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                                this.mPaint.setAlpha(i);
                                canvas.drawPath(region.getBoundaryPath(), this.mPaint);
                                this.mSurface.unlockCanvasAndPost(canvas);
                                z = true;
                            } else {
                                z = false;
                            }
                            if (z && !this.mLastSurfaceShown) {
                                this.mTransaction.show(this.mSurfaceControl).apply();
                                this.mLastSurfaceShown = true;
                            } else {
                                if (z || !this.mLastSurfaceShown) {
                                    return;
                                }
                                this.mTransaction.hide(this.mSurfaceControl).apply();
                                this.mLastSurfaceShown = false;
                            }
                        } catch (Throwable th) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                }

                @GuardedBy({"mService.mGlobalLock"})
                void releaseSurface() {
                    this.mBlastBufferQueue.destroy();
                    DisplayMagnifier.this.mService.mAnimationHandler.post(this);
                }

                void dump(PrintWriter printWriter, String str) {
                    printWriter.println(str + " mBounds= " + this.mBounds + " mDirtyRect= " + this.mDirtyRect + " mWidth= " + MagnifiedViewport.this.mScreenSize.x + " mHeight= " + MagnifiedViewport.this.mScreenSize.y);
                }

                /* JADX INFO: Access modifiers changed from: private */
                /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
                public final class AnimationController extends Handler {
                    private static final int MAX_ALPHA = 255;
                    private static final int MIN_ALPHA = 0;
                    private static final int MSG_FRAME_SHOWN_STATE_CHANGED = 1;
                    private static final String PROPERTY_NAME_ALPHA = "alpha";
                    private final ValueAnimator mShowHideFrameAnimator;

                    AnimationController(Context context, Looper looper) {
                        super(looper);
                        ObjectAnimator ofInt = ObjectAnimator.ofInt(ViewportWindow.this, PROPERTY_NAME_ALPHA, 0, 255);
                        this.mShowHideFrameAnimator = ofInt;
                        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(2.5f);
                        long integer = context.getResources().getInteger(R.integer.config_longAnimTime);
                        ofInt.setInterpolator(decelerateInterpolator);
                        ofInt.setDuration(integer);
                    }

                    void onFrameShownStateChanged(boolean z, boolean z2) {
                        obtainMessage(1, z ? 1 : 0, z2 ? 1 : 0).sendToTarget();
                    }

                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        if (message.what != 1) {
                            return;
                        }
                        boolean z = message.arg1 == 1;
                        if (message.arg2 == 1) {
                            if (this.mShowHideFrameAnimator.isRunning()) {
                                this.mShowHideFrameAnimator.reverse();
                                return;
                            } else if (z) {
                                this.mShowHideFrameAnimator.start();
                                return;
                            } else {
                                this.mShowHideFrameAnimator.reverse();
                                return;
                            }
                        }
                        this.mShowHideFrameAnimator.cancel();
                        if (z) {
                            ViewportWindow.this.setAlpha(255);
                        } else {
                            ViewportWindow.this.setAlpha(0);
                        }
                    }
                }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private class MyHandler extends Handler {
            public static final int MESSAGE_NOTIFY_DISPLAY_SIZE_CHANGED = 4;
            public static final int MESSAGE_NOTIFY_IME_WINDOW_VISIBILITY_CHANGED = 6;
            public static final int MESSAGE_NOTIFY_MAGNIFICATION_REGION_CHANGED = 1;
            public static final int MESSAGE_NOTIFY_USER_CONTEXT_CHANGED = 3;
            public static final int MESSAGE_SHOW_MAGNIFIED_REGION_BOUNDS_IF_NEEDED = 5;

            MyHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 1) {
                    Region region = (Region) ((SomeArgs) message.obj).arg1;
                    DisplayMagnifier.this.mCallbacks.onMagnificationRegionChanged(region);
                    region.recycle();
                    return;
                }
                if (i == 3) {
                    DisplayMagnifier.this.mCallbacks.onUserContextChanged();
                    return;
                }
                if (i == 4) {
                    DisplayMagnifier.this.mCallbacks.onDisplaySizeChanged();
                    return;
                }
                if (i != 5) {
                    if (i != 6) {
                        return;
                    }
                    DisplayMagnifier.this.mCallbacks.onImeWindowVisibilityChanged(message.arg1 == 1);
                    return;
                }
                WindowManagerGlobalLock windowManagerGlobalLock = DisplayMagnifier.this.mService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        if (DisplayMagnifier.this.isForceShowingMagnifiableBounds()) {
                            DisplayMagnifier.this.mMagnifedViewport.setMagnifiedRegionBorderShown(true, true);
                            DisplayMagnifier.this.mService.scheduleAnimationLocked();
                        }
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        }
    }

    static boolean isUntouchableNavigationBar(WindowState windowState, Region region) {
        if (windowState.mAttrs.type != 2019) {
            return false;
        }
        windowState.getTouchableRegion(region);
        return region.isEmpty();
    }

    static Rect getSystemBarInsetsFrame(WindowState windowState) {
        if (windowState == null) {
            return EMPTY_RECT;
        }
        InsetsSourceProvider controllableInsetProvider = windowState.getControllableInsetProvider();
        return controllableInsetProvider != null ? controllableInsetProvider.getSource().getFrame() : EMPTY_RECT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class WindowsForAccessibilityObserver {
        private static final boolean DEBUG = false;
        private static final String LOG_TAG = "WindowManager";
        private final AccessibilityWindowsPopulator mA11yWindowsPopulator;
        private final AccessibilityControllerInternalImpl mAccessibilityTracing;
        private final WindowManagerInternal.WindowsForAccessibilityCallback mCallback;
        private final int mDisplayId;
        private final Handler mHandler;
        private boolean mInitialized;
        private final WindowManagerService mService;
        private final List<AccessibilityWindowsPopulator.AccessibilityWindow> mTempA11yWindows = new ArrayList();
        private final Set<IBinder> mTempBinderSet = new ArraySet();
        private final Point mTempPoint = new Point();
        private final Region mTempRegion = new Region();
        private final Region mTempRegion1 = new Region();
        private final Region mTempRegion2 = new Region();
        private final long mRecurringAccessibilityEventsIntervalMillis = ViewConfiguration.getSendRecurringAccessibilityEventsInterval();

        private static boolean isReportedWindowType(int i) {
            return (i == 2013 || i == 2021 || i == 2026 || i == 2016 || i == 2022 || i == 2018 || i == 2027 || i == 1004 || i == 2015 || i == 2030) ? false : true;
        }

        WindowsForAccessibilityObserver(WindowManagerService windowManagerService, int i, WindowManagerInternal.WindowsForAccessibilityCallback windowsForAccessibilityCallback, AccessibilityWindowsPopulator accessibilityWindowsPopulator) {
            this.mService = windowManagerService;
            this.mCallback = windowsForAccessibilityCallback;
            this.mDisplayId = i;
            this.mHandler = new MyHandler(windowManagerService.mH.getLooper());
            this.mAccessibilityTracing = AccessibilityController.getAccessibilityControllerInternal(windowManagerService);
            this.mA11yWindowsPopulator = accessibilityWindowsPopulator;
            computeChangedWindows(true);
        }

        void performComputeChangedWindows(boolean z) {
            if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.performComputeChangedWindows", 1024L, "forceSend=" + z);
            }
            this.mHandler.removeMessages(1);
            computeChangedWindows(z);
        }

        void scheduleComputeChangedWindows() {
            if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.scheduleComputeChangedWindows", 1024L);
            }
            if (this.mHandler.hasMessages(1)) {
                return;
            }
            this.mHandler.sendEmptyMessageDelayed(1, this.mRecurringAccessibilityEventsIntervalMillis);
        }

        void computeChangedWindows(boolean z) {
            WindowState topFocusWindow;
            if (this.mAccessibilityTracing.isTracingEnabled(1024L)) {
                this.mAccessibilityTracing.logTrace("WindowManager.computeChangedWindows", 1024L, "forceSend=" + z);
            }
            ArrayList arrayList = new ArrayList();
            WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    RecentsAnimationController recentsAnimationController = this.mService.getRecentsAnimationController();
                    if (recentsAnimationController != null) {
                        topFocusWindow = recentsAnimationController.getTargetAppMainWindow();
                    } else {
                        topFocusWindow = getTopFocusWindow();
                    }
                    if (topFocusWindow == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    DisplayContent displayContent = this.mService.mRoot.getDisplayContent(this.mDisplayId);
                    if (displayContent == null) {
                        Slog.w(LOG_TAG, "display content is null, should be created later");
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    displayContent.getDisplay().getRealSize(this.mTempPoint);
                    Point point = this.mTempPoint;
                    int i = point.x;
                    int i2 = point.y;
                    Region region = this.mTempRegion;
                    region.set(0, 0, i, i2);
                    List<AccessibilityWindowsPopulator.AccessibilityWindow> list = this.mTempA11yWindows;
                    this.mA11yWindowsPopulator.populateVisibleWindowsOnScreenLocked(this.mDisplayId, list);
                    Set<IBinder> set = this.mTempBinderSet;
                    set.clear();
                    int size = list.size();
                    boolean z2 = false;
                    for (int i3 = 0; i3 < size; i3++) {
                        AccessibilityWindowsPopulator.AccessibilityWindow accessibilityWindow = list.get(i3);
                        Region region2 = new Region();
                        accessibilityWindow.getTouchableRegionInWindow(region2);
                        if (windowMattersToAccessibility(accessibilityWindow, region2, region)) {
                            addPopulatedWindowInfo(accessibilityWindow, region2, arrayList, set);
                            if (windowMattersToUnaccountedSpaceComputation(accessibilityWindow)) {
                                updateUnaccountedSpace(accessibilityWindow, region);
                            }
                            z2 |= accessibilityWindow.isFocused();
                        } else if (accessibilityWindow.isUntouchableNavigationBar()) {
                            region.op(AccessibilityController.getSystemBarInsetsFrame(this.mService.mWindowMap.get(accessibilityWindow.getWindowInfo().token)), region, Region.Op.REVERSE_DIFFERENCE);
                        }
                        if (region.isEmpty() && z2) {
                            break;
                        }
                    }
                    int size2 = arrayList.size();
                    for (int i4 = 0; i4 < size2; i4++) {
                        WindowInfo windowInfo = (WindowInfo) arrayList.get(i4);
                        if (!set.contains(windowInfo.parentToken)) {
                            windowInfo.parentToken = null;
                        }
                        List list2 = windowInfo.childTokens;
                        if (list2 != null) {
                            for (int size3 = list2.size() - 1; size3 >= 0; size3--) {
                                if (!set.contains(windowInfo.childTokens.get(size3))) {
                                    windowInfo.childTokens.remove(size3);
                                }
                            }
                        }
                    }
                    list.clear();
                    set.clear();
                    int displayId = this.mService.mRoot.getTopFocusedDisplayContent().getDisplayId();
                    IBinder asBinder = topFocusWindow.mClient.asBinder();
                    WindowManagerService.resetPriorityAfterLockedSection();
                    this.mCallback.onWindowsForAccessibilityChanged(z, displayId, asBinder, arrayList);
                    clearAndRecycleWindows(arrayList);
                    this.mInitialized = true;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        private boolean windowMattersToUnaccountedSpaceComputation(AccessibilityWindowsPopulator.AccessibilityWindow accessibilityWindow) {
            return (accessibilityWindow.isTouchable() || accessibilityWindow.getType() == 2034 || !accessibilityWindow.isTrustedOverlay()) && accessibilityWindow.getType() != 2032;
        }

        private boolean windowMattersToAccessibility(AccessibilityWindowsPopulator.AccessibilityWindow accessibilityWindow, Region region, Region region2) {
            if (accessibilityWindow.ignoreRecentsAnimationForAccessibility()) {
                return false;
            }
            if (accessibilityWindow.isFocused()) {
                return true;
            }
            WindowState windowState = this.mService.mWindowMap.get(accessibilityWindow.getWindowInfo().token);
            if (windowState == null || !windowState.getWrapper().getExtImpl().shouldAddSettingsWindowToA11y(windowState)) {
                return (accessibilityWindow.isTouchable() || accessibilityWindow.getType() == 2034 || accessibilityWindow.isPIPMenu()) && !region2.quickReject(region) && isReportedWindowType(accessibilityWindow.getType());
            }
            return true;
        }

        private void updateUnaccountedSpace(AccessibilityWindowsPopulator.AccessibilityWindow accessibilityWindow, Region region) {
            if (accessibilityWindow.getType() != 2032) {
                Region region2 = this.mTempRegion2;
                accessibilityWindow.getTouchableRegionInScreen(region2);
                region.op(region2, region, Region.Op.REVERSE_DIFFERENCE);
            }
        }

        private static void addPopulatedWindowInfo(AccessibilityWindowsPopulator.AccessibilityWindow accessibilityWindow, Region region, List<WindowInfo> list, Set<IBinder> set) {
            WindowInfo windowInfo = accessibilityWindow.getWindowInfo();
            if (windowInfo.token == null) {
                return;
            }
            windowInfo.regionInScreen.set(region);
            windowInfo.layer = set.size();
            list.add(windowInfo);
            set.add(windowInfo.token);
        }

        private static void clearAndRecycleWindows(List<WindowInfo> list) {
            for (int size = list.size() - 1; size >= 0; size--) {
                list.remove(size).recycle();
            }
        }

        private WindowState getTopFocusWindow() {
            return this.mService.mRoot.getTopFocusedDisplayContent().mCurrentFocus;
        }

        public String toString() {
            return "WindowsForAccessibilityObserver{mDisplayId=" + this.mDisplayId + ", mInitialized=" + this.mInitialized + '}';
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private class MyHandler extends Handler {
            public static final int MESSAGE_COMPUTE_CHANGED_WINDOWS = 1;

            public MyHandler(Looper looper) {
                super(looper, null, false);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    return;
                }
                WindowsForAccessibilityObserver.this.computeChangedWindows(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class AccessibilityControllerInternalImpl implements WindowManagerInternal.AccessibilityControllerInternal {
        private static AccessibilityControllerInternalImpl sInstance;
        private UiChangesForAccessibilityCallbacksDispatcher mCallbacksDispatcher;
        private volatile long mEnabledTracingFlags = 0;
        private final Looper mLooper;
        private final AccessibilityTracing mTracing;

        static AccessibilityControllerInternalImpl getInstance(WindowManagerService windowManagerService) {
            AccessibilityControllerInternalImpl accessibilityControllerInternalImpl;
            synchronized (AccessibilityController.STATIC_LOCK) {
                if (sInstance == null) {
                    sInstance = new AccessibilityControllerInternalImpl(windowManagerService);
                }
                accessibilityControllerInternalImpl = sInstance;
            }
            return accessibilityControllerInternalImpl;
        }

        private AccessibilityControllerInternalImpl(WindowManagerService windowManagerService) {
            this.mLooper = windowManagerService.mH.getLooper();
            this.mTracing = AccessibilityTracing.getInstance(windowManagerService);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public void startTrace(long j) {
            this.mEnabledTracingFlags = j;
            this.mTracing.startTrace();
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public void stopTrace() {
            this.mTracing.stopTrace();
            this.mEnabledTracingFlags = 0L;
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public boolean isAccessibilityTracingEnabled() {
            return this.mTracing.isEnabled();
        }

        boolean isTracingEnabled(long j) {
            return (j & this.mEnabledTracingFlags) != 0;
        }

        void logTrace(String str, long j) {
            logTrace(str, j, "");
        }

        void logTrace(String str, long j, String str2) {
            logTrace(str, j, str2, "".getBytes(), Binder.getCallingUid());
        }

        void logTrace(String str, long j, String str2, byte[] bArr, int i) {
            this.mTracing.logState(str, j, str2, bArr, i, new HashSet(Arrays.asList("logTrace")));
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public void logTrace(String str, long j, String str2, byte[] bArr, int i, StackTraceElement[] stackTraceElementArr, Set<String> set) {
            this.mTracing.logState(str, j, str2, bArr, i, stackTraceElementArr, set);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public void logTrace(String str, long j, String str2, byte[] bArr, int i, StackTraceElement[] stackTraceElementArr, long j2, int i2, long j3, Set<String> set) {
            this.mTracing.logState(str, j, str2, bArr, i, stackTraceElementArr, j2, i2, j3, set);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal
        public void setUiChangesForAccessibilityCallbacks(WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks uiChangesForAccessibilityCallbacks) {
            if (isTracingEnabled(2048L)) {
                logTrace(AccessibilityController.TAG + ".setAccessibilityWindowManagerCallbacks", 2048L, "callbacks={" + uiChangesForAccessibilityCallbacks + "}");
            }
            if (uiChangesForAccessibilityCallbacks != null) {
                if (this.mCallbacksDispatcher != null) {
                    throw new IllegalStateException("Accessibility window manager callback already set!");
                }
                this.mCallbacksDispatcher = new UiChangesForAccessibilityCallbacksDispatcher(this, this.mLooper, uiChangesForAccessibilityCallbacks);
            } else {
                if (this.mCallbacksDispatcher == null) {
                    throw new IllegalStateException("Accessibility window manager callback already cleared!");
                }
                this.mCallbacksDispatcher = null;
            }
        }

        public boolean hasWindowManagerEventDispatcher() {
            if (isTracingEnabled(3072L)) {
                logTrace(AccessibilityController.TAG + ".hasCallbacks", 3072L);
            }
            return this.mCallbacksDispatcher != null;
        }

        public void onRectangleOnScreenRequested(int i, Rect rect) {
            if (isTracingEnabled(2048L)) {
                logTrace(AccessibilityController.TAG + ".onRectangleOnScreenRequested", 2048L, "rectangle={" + rect + "}");
            }
            UiChangesForAccessibilityCallbacksDispatcher uiChangesForAccessibilityCallbacksDispatcher = this.mCallbacksDispatcher;
            if (uiChangesForAccessibilityCallbacksDispatcher != null) {
                uiChangesForAccessibilityCallbacksDispatcher.onRectangleOnScreenRequested(i, rect);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public static final class UiChangesForAccessibilityCallbacksDispatcher {
            private static final boolean DEBUG_RECTANGLE_REQUESTED = false;
            private static final String LOG_TAG = "WindowManager";
            private final AccessibilityControllerInternalImpl mAccessibilityTracing;
            private final WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks mCallbacks;
            private final Handler mHandler;

            UiChangesForAccessibilityCallbacksDispatcher(AccessibilityControllerInternalImpl accessibilityControllerInternalImpl, Looper looper, WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks uiChangesForAccessibilityCallbacks) {
                this.mAccessibilityTracing = accessibilityControllerInternalImpl;
                this.mCallbacks = uiChangesForAccessibilityCallbacks;
                this.mHandler = new Handler(looper);
            }

            void onRectangleOnScreenRequested(int i, Rect rect) {
                if (this.mAccessibilityTracing.isTracingEnabled(2048L)) {
                    this.mAccessibilityTracing.logTrace("WindowManager.onRectangleOnScreenRequested", 2048L, "rectangle={" + rect + "}");
                }
                final WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks uiChangesForAccessibilityCallbacks = this.mCallbacks;
                Objects.requireNonNull(uiChangesForAccessibilityCallbacks);
                this.mHandler.sendMessage(PooledLambda.obtainMessage(new QuintConsumer() { // from class: com.android.server.wm.AccessibilityController$AccessibilityControllerInternalImpl$UiChangesForAccessibilityCallbacksDispatcher$$ExternalSyntheticLambda0
                    public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                        WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks.this.onRectangleOnScreenRequested(((Integer) obj).intValue(), ((Integer) obj2).intValue(), ((Integer) obj3).intValue(), ((Integer) obj4).intValue(), ((Integer) obj5).intValue());
                    }
                }, Integer.valueOf(i), Integer.valueOf(rect.left), Integer.valueOf(rect.top), Integer.valueOf(rect.right), Integer.valueOf(rect.bottom)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class AccessibilityTracing {
        private static final int BUFFER_CAPACITY = 12582912;
        private static final int CPU_STATS_COUNT = 5;
        private static final long MAGIC_NUMBER_VALUE = 4846245196254490945L;
        private static final String TAG = "AccessibilityTracing";
        private static final String TRACE_FILENAME = "/data/misc/a11ytrace/a11y_trace.winscope";
        private static AccessibilityTracing sInstance;
        private volatile boolean mEnabled;
        private final LogHandler mHandler;
        private final WindowManagerService mService;
        private final Object mLock = new Object();
        private final File mTraceFile = new File(TRACE_FILENAME);
        private final TraceBuffer mBuffer = new TraceBuffer(BUFFER_CAPACITY);

        static AccessibilityTracing getInstance(WindowManagerService windowManagerService) {
            AccessibilityTracing accessibilityTracing;
            synchronized (AccessibilityController.STATIC_LOCK) {
                if (sInstance == null) {
                    sInstance = new AccessibilityTracing(windowManagerService);
                }
                accessibilityTracing = sInstance;
            }
            return accessibilityTracing;
        }

        AccessibilityTracing(WindowManagerService windowManagerService) {
            this.mService = windowManagerService;
            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            this.mHandler = new LogHandler(handlerThread.getLooper());
        }

        void startTrace() {
            if (Build.IS_USER) {
                Slog.e(TAG, "Error: Tracing is not supported on user builds.");
                return;
            }
            synchronized (this.mLock) {
                this.mEnabled = true;
                this.mBuffer.resetBuffer();
            }
        }

        void stopTrace() {
            if (Build.IS_USER) {
                Slog.e(TAG, "Error: Tracing is not supported on user builds.");
                return;
            }
            synchronized (this.mLock) {
                this.mEnabled = false;
                if (this.mEnabled) {
                    Slog.e(TAG, "Error: tracing enabled while waiting for flush.");
                } else {
                    writeTraceToFile();
                }
            }
        }

        boolean isEnabled() {
            return this.mEnabled;
        }

        void logState(String str, long j) {
            if (this.mEnabled) {
                logState(str, j, "");
            }
        }

        void logState(String str, long j, String str2) {
            if (this.mEnabled) {
                logState(str, j, str2, "".getBytes());
            }
        }

        void logState(String str, long j, String str2, byte[] bArr) {
            if (this.mEnabled) {
                logState(str, j, str2, bArr, Binder.getCallingUid(), new HashSet(Arrays.asList("logState")));
            }
        }

        void logState(String str, long j, String str2, byte[] bArr, int i, Set<String> set) {
            if (this.mEnabled) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                set.add("logState");
                logState(str, j, str2, bArr, i, stackTrace, set);
            }
        }

        void logState(String str, long j, String str2, byte[] bArr, int i, StackTraceElement[] stackTraceElementArr, Set<String> set) {
            if (this.mEnabled) {
                log(str, j, str2, bArr, i, stackTraceElementArr, SystemClock.elapsedRealtimeNanos(), Process.myPid() + ":" + Application.getProcessName(), Thread.currentThread().getId() + ":" + Thread.currentThread().getName(), set);
            }
        }

        void logState(String str, long j, String str2, byte[] bArr, int i, StackTraceElement[] stackTraceElementArr, long j2, int i2, long j3, Set<String> set) {
            if (this.mEnabled) {
                log(str, j, str2, bArr, i, stackTraceElementArr, j2, String.valueOf(i2), String.valueOf(j3), set);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x0032, code lost:
        
            if (r0 < r6.length) goto L18;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x0034, code lost:
        
            r0 = r0 + 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x0037, code lost:
        
            if (r0 >= r6.length) goto L40;
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x0039, code lost:
        
            r2 = r7.iterator();
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0041, code lost:
        
            if (r2.hasNext() == false) goto L42;
         */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x0053, code lost:
        
            if (r6[r0].toString().contains(r2.next()) == false) goto L45;
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x0055, code lost:
        
            r1 = r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:29:0x0056, code lost:
        
            if (r1 == r0) goto L43;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x005a, code lost:
        
            r1 = r1 + 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x005d, code lost:
        
            if (r1 >= r6.length) goto L46;
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x005f, code lost:
        
            r5.append(r6[r1].toString());
            r5.append("\n");
         */
        /* JADX WARN: Code restructure failed: missing block: B:41:0x0072, code lost:
        
            return r5.toString();
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public String toStackTraceString(StackTraceElement[] stackTraceElementArr, Set<String> set) {
            if (stackTraceElementArr != null) {
                StringBuilder sb = new StringBuilder();
                int i = 0;
                int i2 = -1;
                while (i < stackTraceElementArr.length) {
                    Iterator<String> it = set.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        if (stackTraceElementArr[i].toString().contains(it.next())) {
                            i2 = i;
                            break;
                        }
                    }
                    if (i2 >= 0) {
                        break;
                    }
                    i++;
                }
            } else {
                return "";
            }
        }

        private void log(String str, long j, String str2, byte[] bArr, int i, StackTraceElement[] stackTraceElementArr, long j2, String str3, String str4, Set<String> set) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argl1 = j2;
            obtain.argl2 = j;
            obtain.arg1 = str;
            obtain.arg2 = str3;
            obtain.arg3 = str4;
            obtain.arg4 = set;
            obtain.arg5 = str2;
            obtain.arg6 = stackTraceElementArr;
            obtain.arg7 = bArr;
            this.mHandler.obtainMessage(1, i, 0, obtain).sendToTarget();
        }

        void writeTraceToFile() {
            this.mHandler.sendEmptyMessage(2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public class LogHandler extends Handler {
            public static final int MESSAGE_LOG_TRACE_ENTRY = 1;
            public static final int MESSAGE_WRITE_FILE = 2;

            LogHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i != 1) {
                    if (i != 2) {
                        return;
                    }
                    synchronized (AccessibilityTracing.this.mLock) {
                        AccessibilityTracing.this.writeTraceToFileInternal();
                    }
                    return;
                }
                SomeArgs someArgs = (SomeArgs) message.obj;
                try {
                    ProtoOutputStream protoOutputStream = new ProtoOutputStream();
                    PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
                    long start = protoOutputStream.start(2246267895810L);
                    long j = someArgs.argl1;
                    long time = new Date().getTime() - ((SystemClock.elapsedRealtimeNanos() - j) / 1000000);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
                    protoOutputStream.write(1125281431553L, j);
                    protoOutputStream.write(1138166333442L, simpleDateFormat.format(Long.valueOf(time)).toString());
                    Iterator it = AccessibilityTrace.getNamesOfLoggingTypes(someArgs.argl2).iterator();
                    while (it.hasNext()) {
                        protoOutputStream.write(2237677961219L, (String) it.next());
                    }
                    protoOutputStream.write(1138166333446L, (String) someArgs.arg1);
                    protoOutputStream.write(1138166333444L, (String) someArgs.arg2);
                    protoOutputStream.write(1138166333445L, (String) someArgs.arg3);
                    protoOutputStream.write(1138166333447L, packageManagerInternal.getNameForUid(message.arg1));
                    protoOutputStream.write(1138166333448L, (String) someArgs.arg5);
                    protoOutputStream.write(1138166333449L, AccessibilityTracing.this.toStackTraceString((StackTraceElement[]) someArgs.arg6, (Set) someArgs.arg4));
                    protoOutputStream.write(1146756268042L, (byte[]) someArgs.arg7);
                    long start2 = protoOutputStream.start(1146756268043L);
                    WindowManagerGlobalLock windowManagerGlobalLock = AccessibilityTracing.this.mService.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            AccessibilityTracing.this.mService.dumpDebugLocked(protoOutputStream, 0);
                        } catch (Throwable th) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    protoOutputStream.end(start2);
                    protoOutputStream.write(1138166333452L, AccessibilityTracing.this.printCpuStats(j));
                    protoOutputStream.end(start);
                    synchronized (AccessibilityTracing.this.mLock) {
                        AccessibilityTracing.this.mBuffer.add(protoOutputStream);
                    }
                } catch (Exception e) {
                    Slog.e(AccessibilityTracing.TAG, "Exception while tracing state", e);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeTraceToFileInternal() {
            try {
                ProtoOutputStream protoOutputStream = new ProtoOutputStream();
                protoOutputStream.write(1125281431553L, MAGIC_NUMBER_VALUE);
                protoOutputStream.write(1125281431555L, TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis()) - SystemClock.elapsedRealtimeNanos());
                this.mBuffer.writeTraceToFile(this.mTraceFile, protoOutputStream);
            } catch (IOException e) {
                Slog.e(TAG, "Unable to write buffer to file", e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String printCpuStats(long j) {
            Pair appProfileStatsForDebugging = this.mService.mAmInternal.getAppProfileStatsForDebugging(j, 5);
            return ((String) appProfileStatsForDebugging.first) + ((String) appProfileStatsForDebugging.second);
        }
    }
}
