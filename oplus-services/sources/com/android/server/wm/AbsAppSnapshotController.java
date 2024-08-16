package com.android.server.wm;

import android.R;
import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.hardware.HardwareBuffer;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Pair;
import android.util.Slog;
import android.view.InsetsState;
import android.view.SurfaceControl;
import android.view.ThreadedRenderer;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.window.ScreenCapture;
import android.window.SnapshotDrawerUtils;
import android.window.TaskSnapshot;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.ColorUtils;
import com.android.server.wm.SnapshotCache;
import com.android.server.wm.Transition;
import com.android.server.wm.WindowContainer;
import com.android.server.wm.utils.InsetUtils;
import java.io.PrintWriter;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public abstract class AbsAppSnapshotController<TYPE extends WindowContainer, CACHE extends SnapshotCache<TYPE>> {
    private static final boolean CROP_TASKSNAPSHOT_ENABLE_VALUE = SystemProperties.getBoolean("persist.sys.crop_task_snapshot_enable", false);

    @VisibleForTesting
    static final int SNAPSHOT_MODE_APP_THEME = 1;

    @VisibleForTesting
    static final int SNAPSHOT_MODE_NONE = 2;

    @VisibleForTesting
    static final int SNAPSHOT_MODE_REAL = 0;
    static final String TAG = "WindowManager";
    protected CACHE mCache;
    protected final boolean mIsRunningOnIoT;
    protected final boolean mIsRunningOnTv;
    protected final WindowManagerService mService;
    private boolean mSnapshotEnabled;
    private final Rect mTmpRect = new Rect();
    public IAbsAppSnapshotControllerExt mAbsAppSnapConExt = (IAbsAppSnapshotControllerExt) ExtLoader.type(IAbsAppSnapshotControllerExt.class).base(this).create();
    protected final float mHighResSnapshotScale = initSnapshotScale();

    protected abstract ActivityRecord findAppTokenForSnapshot(TYPE type);

    abstract ActivityManager.TaskDescription getTaskDescription(TYPE type);

    abstract ActivityRecord getTopActivity(TYPE type);

    abstract ActivityRecord getTopFullscreenActivity(TYPE type);

    protected abstract boolean use16BitFormat();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbsAppSnapshotController(WindowManagerService windowManagerService) {
        this.mService = windowManagerService;
        this.mIsRunningOnTv = windowManagerService.mContext.getPackageManager().hasSystemFeature("android.software.leanback");
        this.mIsRunningOnIoT = windowManagerService.mContext.getPackageManager().hasSystemFeature("android.hardware.type.embedded");
    }

    protected float initSnapshotScale() {
        return Math.max(Math.min(this.mService.mContext.getResources().getFloat(R.dimen.config_screenBrightnessSettingForVrMinimumFloat), 1.0f), 0.1f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initialize(CACHE cache) {
        this.mCache = cache;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSnapshotEnabled(boolean z) {
        this.mSnapshotEnabled = z;
    }

    public boolean getSnapshotEnabled() {
        return this.mSnapshotEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldDisableSnapshots() {
        return this.mIsRunningOnTv || this.mIsRunningOnIoT || !this.mSnapshotEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public TaskSnapshot captureSnapshot(TYPE type, boolean z) {
        if (z) {
            return snapshot(type);
        }
        int snapshotMode = getSnapshotMode(type);
        Slog.w(TAG, "captureTaskSnapshot mSnapshotMode= " + snapshotMode);
        if (snapshotMode == 0) {
            return snapshot(type);
        }
        if (snapshotMode != 1) {
            return snapshotMode != 2 ? null : null;
        }
        return drawAppThemeSnapshot(type);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final TaskSnapshot recordSnapshotInner(TYPE type, boolean z) {
        if (shouldDisableSnapshots()) {
            return null;
        }
        TaskSnapshot captureSnapshot = captureSnapshot(type, z && type.isActivityTypeHome());
        if (captureSnapshot == null) {
            return null;
        }
        HardwareBuffer hardwareBuffer = captureSnapshot.getHardwareBuffer();
        if (hardwareBuffer.getWidth() == 0 || hardwareBuffer.getHeight() == 0) {
            hardwareBuffer.close();
            Slog.e(TAG, "Invalid snapshot dimensions " + hardwareBuffer.getWidth() + "x" + hardwareBuffer.getHeight());
            return null;
        }
        this.mCache.putSnapshot(type, captureSnapshot);
        return captureSnapshot;
    }

    @VisibleForTesting
    int getSnapshotMode(TYPE type) {
        ActivityRecord topActivity = getTopActivity(type);
        if (type.isActivityTypeStandardOrUndefined() || type.isActivityTypeAssistant() || this.mAbsAppSnapConExt.isActivityTypeMultiSearch(type.getActivityType())) {
            return (topActivity == null || !topActivity.shouldUseAppThemeSnapshot()) ? 0 : 1;
        }
        return 2;
    }

    TaskSnapshot snapshot(TYPE type) {
        return snapshot(type, 0);
    }

    TaskSnapshot snapshot(TYPE type, int i) {
        TaskSnapshot.Builder builder = new TaskSnapshot.Builder();
        if (!prepareTaskSnapshot(type, i, builder)) {
            return null;
        }
        ScreenCapture.ScreenshotHardwareBuffer createSnapshot = createSnapshot(type, builder);
        if (createSnapshot == null) {
            Slog.w(TAG, "snapshotTask screenshotBuffer is null");
            return null;
        }
        if (type instanceof Task) {
            this.mAbsAppSnapConExt.snapshotTask((Task) type, createSnapshot);
        }
        builder.setCaptureTime(SystemClock.elapsedRealtimeNanos());
        builder.setSnapshot(createSnapshot.getHardwareBuffer());
        builder.setColorSpace(createSnapshot.getColorSpace());
        return builder.build();
    }

    ScreenCapture.ScreenshotHardwareBuffer createSnapshot(TYPE type, TaskSnapshot.Builder builder) {
        Point point = new Point();
        Trace.traceBegin(32L, "createSnapshot");
        ScreenCapture.ScreenshotHardwareBuffer createSnapshot = createSnapshot(type, this.mHighResSnapshotScale, builder.getPixelFormat(), point, builder);
        Trace.traceEnd(32L);
        builder.setTaskSize(point);
        return createSnapshot;
    }

    ScreenCapture.ScreenshotHardwareBuffer createSnapshot(TYPE type, float f, int i, Point point, TaskSnapshot.Builder builder) {
        SurfaceControl[] surfaceControlArr;
        TaskSnapshot.Builder builder2;
        ScreenCapture.ScreenshotHardwareBuffer captureLayersExcluding;
        Transition.ChangeInfo changeInfo;
        if (type.getSurfaceControl() == null) {
            Slog.w(TAG, "Failed to take screenshot. No surface control for " + type);
            return null;
        }
        this.mTmpRect.setEmpty();
        if (type.mTransitionController.inFinishingTransition(type) && (changeInfo = type.mTransitionController.mFinishingTransition.mChanges.get(type)) != null) {
            this.mTmpRect.set(changeInfo.mAbsoluteBounds);
        }
        if (this.mTmpRect.isEmpty()) {
            type.getBounds(this.mTmpRect);
        }
        boolean z = false;
        this.mTmpRect.offsetTo(0, 0);
        WindowState windowState = type.getDisplayContent().mInputMethodWindow;
        boolean z2 = (windowState == null || windowState.getSurfaceControl() == null || type.getDisplayContent().shouldImeAttachedToApp()) ? false : true;
        WindowState navigationBar = type.getDisplayContent().getDisplayPolicy().getNavigationBar();
        boolean z3 = (navigationBar == null || navigationBar.getSurfaceControl() == null || !navigationBar.getSurfaceControl().isValid()) ? false : true;
        if (z2 && z3) {
            surfaceControlArr = new SurfaceControl[]{windowState.getSurfaceControl(), navigationBar.getSurfaceControl()};
        } else if (z2 || z3) {
            SurfaceControl[] surfaceControlArr2 = new SurfaceControl[1];
            surfaceControlArr2[0] = z2 ? windowState.getSurfaceControl() : navigationBar.getSurfaceControl();
            surfaceControlArr = surfaceControlArr2;
        } else {
            surfaceControlArr = new SurfaceControl[0];
        }
        if (z2 || windowState == null || !windowState.isVisible()) {
            builder2 = builder;
        } else {
            builder2 = builder;
            z = true;
        }
        builder2.setHasImeSurface(z);
        if (type instanceof Task) {
            captureLayersExcluding = this.mAbsAppSnapConExt.createTaskSnapshot((Task) type, findAppTokenForSnapshot(type), this.mTmpRect, f, i, surfaceControlArr);
        } else {
            captureLayersExcluding = ScreenCapture.captureLayersExcluding(type.getSurfaceControl(), this.mTmpRect, f, i, surfaceControlArr);
        }
        if (point != null) {
            point.x = this.mTmpRect.width();
            point.y = this.mTmpRect.height();
        }
        if (!isInvalidHardwareBuffer(captureLayersExcluding == null ? null : captureLayersExcluding.getHardwareBuffer())) {
            return captureLayersExcluding;
        }
        Slog.d(TAG, "createTaskSnapshot isInvalidHardwareBuffer for " + type);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isInvalidHardwareBuffer(HardwareBuffer hardwareBuffer) {
        return hardwareBuffer == null || hardwareBuffer.isClosed() || hardwareBuffer.getWidth() <= 1 || hardwareBuffer.getHeight() <= 1;
    }

    @VisibleForTesting
    boolean prepareTaskSnapshot(TYPE type, int i, TaskSnapshot.Builder builder) {
        Pair<ActivityRecord, WindowState> checkIfReadyToSnapshot = checkIfReadyToSnapshot(type);
        boolean z = false;
        if (checkIfReadyToSnapshot == null) {
            return false;
        }
        ActivityRecord activityRecord = (ActivityRecord) checkIfReadyToSnapshot.first;
        WindowState windowState = (WindowState) checkIfReadyToSnapshot.second;
        Rect systemBarInsets = getSystemBarInsets(windowState.getFrame(), windowState.getInsetsStateWithVisibilityOverride());
        Rect letterboxInsets = activityRecord.getLetterboxInsets();
        InsetUtils.addInsets(systemBarInsets, letterboxInsets);
        this.mAbsAppSnapConExt.prepareTaskSnapshot(systemBarInsets, activityRecord, windowState);
        builder.setIsRealSnapshot(true);
        builder.setId(System.currentTimeMillis());
        builder.setContentInsets(systemBarInsets);
        builder.setLetterboxInsets(letterboxInsets);
        boolean z2 = windowState.getAttrs().format != -1;
        boolean hasWallpaper = windowState.hasWallpaper();
        if (i == 0) {
            i = (CROP_TASKSNAPSHOT_ENABLE_VALUE || (use16BitFormat() && activityRecord.fillsParent() && (!z2 || !hasWallpaper))) ? 4 : 1;
        }
        if (PixelFormat.formatHasAlpha(i) && (!activityRecord.fillsParent() || z2)) {
            z = true;
        }
        builder.setTopActivityComponent(activityRecord.mActivityComponent);
        builder.setPixelFormat(i);
        builder.setIsTranslucent(z);
        builder.setOrientation(activityRecord.getTask().getConfiguration().orientation);
        if (activityRecord.getTask().getDisplayContent() != null) {
            builder.setRotation(activityRecord.getTask().getDisplayContent().getRotation());
        }
        builder.setWindowingMode(type.getWindowingMode());
        builder.setAppearance(getAppearance(type));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Pair<ActivityRecord, WindowState> checkIfReadyToSnapshot(TYPE type) {
        if (!this.mService.mPolicy.isScreenOn()) {
            Slog.i(TAG, "Attempted to take screenshot while display was off.");
            if (!this.mAbsAppSnapConExt.isSecondScreenOn(this.mService.mPolicy) && (type instanceof Task) && !this.mAbsAppSnapConExt.snapshotForScreenOffActPreload((Task) type)) {
                return null;
            }
        }
        ActivityRecord findAppTokenForSnapshot = findAppTokenForSnapshot(type);
        if (findAppTokenForSnapshot == null) {
            Slog.w(TAG, "Failed to take screenshot. No visible windows for " + type);
            return null;
        }
        WindowState findMainWindow = findAppTokenForSnapshot.findMainWindow();
        if (findMainWindow == null) {
            Slog.w(TAG, "Failed to take screenshot. No main window for " + type);
            return null;
        }
        if (findAppTokenForSnapshot.hasFixedRotationTransform()) {
            Slog.i(TAG, "Skip taking screenshot. App has fixed rotation " + findAppTokenForSnapshot);
            return null;
        }
        return new Pair<>(findAppTokenForSnapshot, findMainWindow);
    }

    private TaskSnapshot drawAppThemeSnapshot(TYPE type) {
        WindowState findMainWindow;
        ActivityRecord topActivity = getTopActivity(type);
        if (topActivity == null || (findMainWindow = topActivity.findMainWindow()) == null) {
            return null;
        }
        ActivityManager.TaskDescription taskDescription = getTaskDescription(type);
        int alphaComponent = ColorUtils.setAlphaComponent(taskDescription.getBackgroundColor(), 255);
        if (type instanceof Task) {
            alphaComponent = this.mAbsAppSnapConExt.drawAppThemeSnapshot(alphaComponent, (Task) type);
        }
        int i = alphaComponent;
        WindowManager.LayoutParams attrs = findMainWindow.getAttrs();
        Rect bounds = type.getBounds();
        Rect systemBarInsets = getSystemBarInsets(findMainWindow.getFrame(), findMainWindow.getInsetsStateWithVisibilityOverride());
        SnapshotDrawerUtils.SystemBarBackgroundPainter systemBarBackgroundPainter = new SnapshotDrawerUtils.SystemBarBackgroundPainter(attrs.flags, attrs.privateFlags, attrs.insetsFlags.appearance, taskDescription, this.mHighResSnapshotScale, findMainWindow.getRequestedVisibleTypes());
        int width = bounds.width();
        int height = bounds.height();
        float f = this.mHighResSnapshotScale;
        int i2 = (int) (width * f);
        int i3 = (int) (height * f);
        RenderNode create = RenderNode.create("SnapshotController", null);
        create.setLeftTopRightBottom(0, 0, i2, i3);
        create.setClipToBounds(false);
        RecordingCanvas start = create.start(i2, i3);
        start.drawColor(i);
        systemBarBackgroundPainter.setInsets(systemBarInsets);
        systemBarBackgroundPainter.drawDecors(start, (Rect) null);
        create.end(start);
        Bitmap createHardwareBitmap = ThreadedRenderer.createHardwareBitmap(create, i2, i3);
        if (createHardwareBitmap == null) {
            Slog.d(TAG, "drawAppThemeSnapshot, hwBitmap is null");
            return null;
        }
        Rect rect = new Rect(systemBarInsets);
        Rect letterboxInsets = topActivity.getLetterboxInsets();
        InsetUtils.addInsets(rect, letterboxInsets);
        return new TaskSnapshot(System.currentTimeMillis(), SystemClock.elapsedRealtimeNanos(), topActivity.mActivityComponent, createHardwareBitmap.getHardwareBuffer(), createHardwareBitmap.getColorSpace(), findMainWindow.getConfiguration().orientation, findMainWindow.getWindowConfiguration().getRotation(), new Point(width, height), rect, letterboxInsets, false, false, type.getWindowingMode(), getAppearance(type), false, false);
    }

    static Rect getSystemBarInsets(Rect rect, InsetsState insetsState) {
        return insetsState.calculateInsets(rect, WindowInsets.Type.systemBars(), false).toRect();
    }

    private int getAppearance(TYPE type) {
        ActivityRecord topFullscreenActivity = getTopFullscreenActivity(type);
        WindowState topFullscreenOpaqueWindow = topFullscreenActivity != null ? topFullscreenActivity.getTopFullscreenOpaqueWindow() : null;
        if (topFullscreenOpaqueWindow != null) {
            return topFullscreenOpaqueWindow.mAttrs.insetsFlags.appearance;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAppRemoved(ActivityRecord activityRecord) {
        this.mCache.onAppRemoved(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAppDied(ActivityRecord activityRecord) {
        this.mCache.onAppDied(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAnimatingByRecents(Task task) {
        return task.isAnimatingByRecents();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "mHighResSnapshotScale=" + this.mHighResSnapshotScale);
        printWriter.println(str + "mSnapshotEnabled=" + this.mSnapshotEnabled);
        this.mCache.dump(printWriter, str);
    }
}
