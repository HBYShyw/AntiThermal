package com.android.server.wm;

import android.R;
import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.os.IBinder;
import android.os.Trace;
import android.util.RotationUtils;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.DisplayAddress;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.window.ScreenCapture;
import com.android.internal.policy.TransitionAnimation;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.display.DisplayControl;
import com.android.server.wm.LocalAnimationAdapter;
import com.android.server.wm.SimpleSurfaceAnimatable;
import com.android.server.wm.SurfaceAnimator;
import com.android.server.wm.utils.CoordinateTransforms;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ScreenRotationAnimation {
    private static final int CAPTIRE_LAYERS_FOR_SCREEN_ROTATION = -222;
    private static final String TAG = "WindowManager";
    private boolean mAnimRunning;
    private SurfaceControl mBackColorSurface;
    private final Context mContext;
    private int mCurRotation;
    private final DisplayContent mDisplayContent;
    private float mEndLuma;
    private SurfaceControl mEnterBlackFrameLayer;
    private BlackFrame mEnteringBlackFrame;
    private boolean mFinishAnimReady;
    private long mFinishAnimStartTime;
    private final int mOriginalHeight;
    private final int mOriginalRotation;
    private final int mOriginalWidth;
    private Animation mRotateAlphaAnimation;
    private Animation mRotateEnterAnimation;
    private Animation mRotateExitAnimation;
    private SurfaceControl[] mRoundedCornerOverlay;
    private SurfaceControl mScreenshotLayer;
    private final WindowManagerService mService;
    private float mStartLuma;
    private boolean mStarted;
    private SurfaceRotationAnimationController mSurfaceRotationAnimationController;
    private final float[] mTmpFloats = new float[9];
    private final Transformation mRotateExitTransformation = new Transformation();
    private final Transformation mRotateEnterTransformation = new Transformation();
    private final Matrix mSnapshotInitialMatrix = new Matrix();
    private ScreenRotationAnimationWrapper mSRAWrapper = new ScreenRotationAnimationWrapper();
    private IScreenRotationAnimationExt mSRAExt = (IScreenRotationAnimationExt) ExtLoader.type(IScreenRotationAnimationExt.class).base(this).create();
    private IScreenRotationAnimationSocExt mAnimationSocExt = (IScreenRotationAnimationSocExt) ExtLoader.type(IScreenRotationAnimationSocExt.class).base(this).create();

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:55:0x02f2  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0328  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x032c  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x030e  */
    /* JADX WARN: Type inference failed for: r8v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ScreenRotationAnimation(DisplayContent displayContent, int i) {
        int i2;
        ScreenCapture.ScreenshotHardwareBuffer screenshotHardwareBuffer;
        boolean z;
        WindowManagerService windowManagerService = displayContent.mWmService;
        this.mService = windowManagerService;
        this.mContext = windowManagerService.mContext;
        this.mDisplayContent = displayContent;
        Rect bounds = displayContent.getBounds();
        int width = bounds.width();
        int height = bounds.height();
        this.mSRAWrapper.getSocExtImpl().init();
        int displayInfo = displayContent.getDisplayInfo();
        int i3 = displayInfo.rotation;
        this.mOriginalRotation = i;
        int deltaRotation = RotationUtils.deltaRotation(i, i3);
        boolean z2 = deltaRotation == 1 || deltaRotation == 3;
        int i4 = z2 ? height : width;
        this.mOriginalWidth = i4;
        int i5 = z2 ? width : height;
        this.mOriginalHeight = i5;
        int i6 = displayInfo.logicalWidth;
        int i7 = displayInfo.logicalHeight;
        boolean z3 = (i6 > i4) == (i7 > i5) && !(i6 == i4 && i7 == i5);
        this.mSurfaceRotationAnimationController = new SurfaceRotationAnimationController();
        boolean hasSecureWindowOnScreen = displayContent.hasSecureWindowOnScreen();
        int displayId = displayContent.getDisplayId();
        SurfaceControl.Transaction transaction = windowManagerService.mTransactionFactory.get();
        try {
            if (z3) {
                try {
                    DisplayAddress.Physical physical = displayInfo.address;
                    if (!(physical instanceof DisplayAddress.Physical)) {
                        Slog.e(TAG, "Display does not have a physical address: " + displayId);
                        return;
                    }
                    i2 = displayId;
                    IBinder physicalDisplayToken = DisplayControl.getPhysicalDisplayToken(physical.getPhysicalDisplayId());
                    if (physicalDisplayToken == null) {
                        Slog.e(TAG, "Display token is null.");
                        return;
                    }
                    setSkipScreenshotForRoundedCornerOverlays(false, transaction);
                    this.mRoundedCornerOverlay = displayContent.findRoundedCornerOverlays();
                    screenshotHardwareBuffer = this.mSRAWrapper.getExtImpl().getScreenshotHardwareBuffer();
                    if (screenshotHardwareBuffer == null) {
                        z = hasSecureWindowOnScreen;
                        ScreenCapture.DisplayCaptureArgs.Builder captureSecureLayers = new ScreenCapture.DisplayCaptureArgs.Builder(physicalDisplayToken).setSourceCrop(new Rect(0, 0, width, height)).setAllowProtected(true).setCaptureSecureLayers(true);
                        displayInfo = i7;
                        screenshotHardwareBuffer = ScreenCapture.captureDisplay(captureSecureLayers.setUid(-222L).setHintForSeamlessTransition(true).build());
                    } else {
                        z = hasSecureWindowOnScreen;
                        displayInfo = i7;
                    }
                } catch (Surface.OutOfResourcesException e) {
                    e = e;
                    displayInfo = i7;
                    Slog.w(TAG, "Unable to allocate freeze surface", e);
                    if (this.mScreenshotLayer != null) {
                        displayContent.getPendingTransaction().setGeometry(this.mScreenshotLayer, new Rect(0, 0, this.mOriginalWidth, this.mOriginalHeight), new Rect(0, 0, i6, displayInfo), 0);
                    }
                    if (ProtoLogGroup.WM_SHOW_SURFACE_ALLOC.isLogToLogcat()) {
                    }
                    if (i != i3) {
                    }
                    transaction.apply();
                }
            } else {
                int i8 = i7;
                i2 = displayId;
                z = hasSecureWindowOnScreen;
                screenshotHardwareBuffer = this.mSRAWrapper.getExtImpl().getScreenshotHardwareBuffer();
                displayInfo = i8;
                if (screenshotHardwareBuffer == null) {
                    screenshotHardwareBuffer = ScreenCapture.captureLayers(new ScreenCapture.LayerCaptureArgs.Builder(displayContent.getSurfaceControl()).setCaptureSecureLayers(true).setAllowProtected(true).setSourceCrop(new Rect(0, 0, width, height)).setUid(-222L).setHintForSeamlessTransition(true).build());
                    displayInfo = i8;
                }
            }
        } catch (Surface.OutOfResourcesException e2) {
            e = e2;
        }
        if (screenshotHardwareBuffer == null) {
            Slog.w(TAG, "Unable to take screenshot of display " + i2);
            return;
        }
        boolean z4 = screenshotHardwareBuffer.containsSecureLayers() ? true : z;
        this.mBackColorSurface = displayContent.makeChildSurface(null).setName("BackColorSurface").setColorLayer().setCallsite("ScreenRotationAnimation").build();
        SurfaceControl build = displayContent.makeOverlay().setName("RotationLayer").setOpaque(true).setSecure(z4).setCallsite("ScreenRotationAnimation").setBLASTLayer().build();
        this.mScreenshotLayer = build;
        InputMonitor.setTrustedOverlayInputInfo(build, transaction, i2, "RotationLayer");
        this.mEnterBlackFrameLayer = displayContent.makeOverlay().setName("EnterBlackFrameLayer").setContainerLayer().setCallsite("ScreenRotationAnimation").build();
        HardwareBuffer hardwareBuffer = screenshotHardwareBuffer.getHardwareBuffer();
        Trace.traceBegin(32L, "ScreenRotationAnimation#getMedianBorderLuma");
        float luma = this.mSRAWrapper.getExtImpl().getLuma(true);
        this.mStartLuma = luma;
        if (luma == Float.MIN_VALUE) {
            this.mStartLuma = TransitionAnimation.getBorderLuma(hardwareBuffer, screenshotHardwareBuffer.getColorSpace());
        }
        this.mStartLuma = this.mSRAWrapper.getExtImpl().computStartLuma(this.mStartLuma);
        Trace.traceEnd(32L);
        this.mSRAWrapper.getExtImpl().setRotationLayer(this.mScreenshotLayer, displayContent);
        transaction.setLayer(this.mScreenshotLayer, 2010000);
        transaction.reparent(this.mBackColorSurface, displayContent.getSurfaceControl());
        transaction.setDimmingEnabled(this.mScreenshotLayer, !screenshotHardwareBuffer.containsHdrLayers());
        transaction.setLayer(this.mBackColorSurface, -1);
        SurfaceControl surfaceControl = this.mBackColorSurface;
        float f = this.mStartLuma;
        transaction.setColor(surfaceControl, new float[]{f, f, f});
        updateAnimationForFolding(transaction, false);
        transaction.setAlpha(this.mBackColorSurface, 1.0f);
        transaction.setBuffer(this.mScreenshotLayer, hardwareBuffer);
        transaction.setColorSpace(this.mScreenshotLayer, screenshotHardwareBuffer.getColorSpace());
        transaction.show(this.mScreenshotLayer);
        transaction.show(this.mBackColorSurface);
        hardwareBuffer.close();
        SurfaceControl[] surfaceControlArr = this.mRoundedCornerOverlay;
        if (surfaceControlArr != null) {
            for (SurfaceControl surfaceControl2 : surfaceControlArr) {
                if (surfaceControl2.isValid()) {
                    transaction.hide(surfaceControl2);
                }
            }
        }
        if (this.mScreenshotLayer != null && z3) {
            displayContent.getPendingTransaction().setGeometry(this.mScreenshotLayer, new Rect(0, 0, this.mOriginalWidth, this.mOriginalHeight), new Rect(0, 0, i6, displayInfo), 0);
        }
        if (ProtoLogGroup.WM_SHOW_SURFACE_ALLOC.isLogToLogcat()) {
            Slog.i(TAG, "  FREEZE " + this.mScreenshotLayer + ": CREATE");
        } else if (ProtoLogCache.WM_SHOW_SURFACE_ALLOC_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, 10608884, 0, (String) null, new Object[]{String.valueOf(this.mScreenshotLayer)});
        }
        if (i != i3) {
            setRotation(transaction, i3);
        } else {
            this.mCurRotation = i3;
            this.mSnapshotInitialMatrix.reset();
            setRotationTransform(transaction, this.mSnapshotInitialMatrix);
        }
        transaction.apply();
    }

    void setSkipScreenshotForRoundedCornerOverlays(final boolean z, final SurfaceControl.Transaction transaction) {
        this.mDisplayContent.forAllWindows(new Consumer() { // from class: com.android.server.wm.ScreenRotationAnimation$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScreenRotationAnimation.lambda$setSkipScreenshotForRoundedCornerOverlays$0(transaction, z, (WindowState) obj);
            }
        }, false);
        if (z) {
            return;
        }
        transaction.apply(true);
    }

    public static /* synthetic */ void lambda$setSkipScreenshotForRoundedCornerOverlays$0(SurfaceControl.Transaction transaction, boolean z, WindowState windowState) {
        if (windowState.mToken.mRoundedCornerOverlay && windowState.isVisible() && windowState.mWinAnimator.hasSurface()) {
            transaction.setSkipScreenshot(windowState.mWinAnimator.mSurfaceController.mSurfaceControl, z);
        }
    }

    public void updateAnimationForFolding(SurfaceControl.Transaction transaction, boolean z) {
        this.mSRAWrapper.getExtImpl().updateAnimationForFolding(transaction, this.mBackColorSurface, this.mScreenshotLayer, this.mDisplayContent, z);
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1133871366145L, this.mStarted);
        protoOutputStream.write(1133871366146L, this.mAnimRunning);
        protoOutputStream.end(start);
    }

    public boolean hasScreenshot() {
        return this.mScreenshotLayer != null;
    }

    private void setRotationTransform(SurfaceControl.Transaction transaction, Matrix matrix) {
        if (this.mScreenshotLayer == null) {
            return;
        }
        matrix.getValues(this.mTmpFloats);
        float[] fArr = this.mTmpFloats;
        transaction.setPosition(this.mScreenshotLayer, fArr[2], fArr[5]);
        SurfaceControl surfaceControl = this.mScreenshotLayer;
        float[] fArr2 = this.mTmpFloats;
        transaction.setMatrix(surfaceControl, fArr2[0], fArr2[3], fArr2[1], fArr2[4]);
        transaction.setAlpha(this.mScreenshotLayer, 1.0f);
        if (this.mSRAWrapper.getExtImpl().getDeviceFolding()) {
            transaction.setAlpha(this.mScreenshotLayer, 0.0f);
        }
        transaction.show(this.mScreenshotLayer);
    }

    public void printTo(String str, PrintWriter printWriter) {
        printWriter.print(str);
        printWriter.print("mSurface=");
        printWriter.print(this.mScreenshotLayer);
        printWriter.print(str);
        printWriter.print("mEnteringBlackFrame=");
        printWriter.println(this.mEnteringBlackFrame);
        BlackFrame blackFrame = this.mEnteringBlackFrame;
        if (blackFrame != null) {
            blackFrame.printTo(str + "  ", printWriter);
        }
        printWriter.print(str);
        printWriter.print("mCurRotation=");
        printWriter.print(this.mCurRotation);
        printWriter.print(" mOriginalRotation=");
        printWriter.println(this.mOriginalRotation);
        printWriter.print(str);
        printWriter.print("mOriginalWidth=");
        printWriter.print(this.mOriginalWidth);
        printWriter.print(" mOriginalHeight=");
        printWriter.println(this.mOriginalHeight);
        printWriter.print(str);
        printWriter.print("mStarted=");
        printWriter.print(this.mStarted);
        printWriter.print(" mAnimRunning=");
        printWriter.print(this.mAnimRunning);
        printWriter.print(" mFinishAnimReady=");
        printWriter.print(this.mFinishAnimReady);
        printWriter.print(" mFinishAnimStartTime=");
        printWriter.println(this.mFinishAnimStartTime);
        printWriter.print(str);
        printWriter.print("mRotateExitAnimation=");
        printWriter.print(this.mRotateExitAnimation);
        printWriter.print(" ");
        this.mRotateExitTransformation.printShortString(printWriter);
        printWriter.println();
        printWriter.print(str);
        printWriter.print("mRotateEnterAnimation=");
        printWriter.print(this.mRotateEnterAnimation);
        printWriter.print(" ");
        this.mRotateEnterTransformation.printShortString(printWriter);
        printWriter.println();
        printWriter.print(str);
        printWriter.print("mSnapshotInitialMatrix=");
        this.mSnapshotInitialMatrix.dump(printWriter);
        printWriter.println();
    }

    public void setRotation(SurfaceControl.Transaction transaction, int i) {
        this.mCurRotation = i;
        CoordinateTransforms.computeRotationMatrix(RotationUtils.deltaRotation(i, this.mOriginalRotation), this.mOriginalWidth, this.mOriginalHeight, this.mSnapshotInitialMatrix);
        setRotationTransform(transaction, this.mSnapshotInitialMatrix);
    }

    private boolean startAnimation(SurfaceControl.Transaction transaction, long j, float f, int i, int i2, int i3, int i4) {
        boolean z;
        if (this.mScreenshotLayer == null) {
            return false;
        }
        if (this.mStarted) {
            return true;
        }
        this.mStarted = true;
        int deltaRotation = RotationUtils.deltaRotation(this.mCurRotation, this.mOriginalRotation);
        if (i3 != 0 && i4 != 0 && !this.mSRAWrapper.getExtImpl().getDeviceFolding()) {
            this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, i3);
            this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, i4);
            this.mRotateAlphaAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.resolver_launch_anim);
            this.mSRAWrapper.getExtImpl().changeRotateAnimation(this.mRotateExitAnimation, this.mRotateEnterAnimation, this.mRotateAlphaAnimation, this.mContext);
            z = true;
        } else {
            if (!this.mSRAWrapper.getExtImpl().hookLoadAnimation(deltaRotation, this.mOriginalWidth, this.mOriginalHeight, i, i2)) {
                if (deltaRotation == 0) {
                    this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.recent_exit);
                    this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.push_down_out_no_alpha);
                } else if (deltaRotation == 1) {
                    this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.screen_rotate_180_exit);
                    this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.screen_rotate_180_enter);
                } else if (deltaRotation == 2) {
                    this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.recents_fade_out);
                    this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.recents_fade_in);
                } else if (deltaRotation == 3) {
                    this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.screen_rotate_0_exit);
                    this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.screen_rotate_0_enter);
                }
            }
            z = false;
        }
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ORIENTATION, -177040661, 0, (String) null, new Object[]{String.valueOf(z), String.valueOf(Surface.rotationToString(this.mCurRotation)), String.valueOf(Surface.rotationToString(this.mOriginalRotation))});
        }
        this.mRotateExitAnimation.initialize(i, i2, this.mOriginalWidth, this.mOriginalHeight);
        this.mRotateExitAnimation.restrictDuration(j);
        this.mRotateExitAnimation.scaleCurrentDuration(f);
        if (!this.mSRAWrapper.getExtImpl().enterAnimationinitialize(this.mRotateEnterAnimation, this.mOriginalWidth, this.mOriginalHeight, i, i2)) {
            this.mRotateEnterAnimation.initialize(i, i2, this.mOriginalWidth, this.mOriginalHeight);
        }
        this.mRotateEnterAnimation.restrictDuration(j);
        this.mRotateEnterAnimation.scaleCurrentDuration(f);
        this.mAnimRunning = false;
        this.mFinishAnimReady = false;
        this.mFinishAnimStartTime = -1L;
        if (z) {
            this.mRotateAlphaAnimation.restrictDuration(j);
            this.mRotateAlphaAnimation.scaleCurrentDuration(f);
        }
        if (z && this.mEnteringBlackFrame == null) {
            try {
                this.mEnteringBlackFrame = new BlackFrame(this.mService.mTransactionFactory, transaction, new Rect(-i, -i2, i * 2, i2 * 2), new Rect(0, 0, i, i2), 2010000, this.mDisplayContent, false, this.mEnterBlackFrameLayer);
            } catch (Surface.OutOfResourcesException e) {
                Slog.w(TAG, "Unable to allocate black surface", e);
            }
        }
        if (z) {
            this.mSurfaceRotationAnimationController.startCustomAnimation();
        } else {
            this.mSurfaceRotationAnimationController.startScreenRotationAnimation();
        }
        return true;
    }

    public boolean dismiss(SurfaceControl.Transaction transaction, long j, float f, int i, int i2, int i3, int i4) {
        if (this.mScreenshotLayer == null) {
            return false;
        }
        if (!this.mStarted) {
            float luma = this.mSRAWrapper.getExtImpl().getLuma(false);
            this.mEndLuma = luma;
            if (luma == Float.MIN_VALUE) {
                this.mEndLuma = TransitionAnimation.getBorderLuma(this.mDisplayContent.getWindowingLayer(), i, i2);
            }
            this.mSRAWrapper.getExtImpl().hookComputStartLumaForDismiss(this.mCurRotation, this.mOriginalRotation, this.mDisplayContent);
            startAnimation(transaction, j, f, i, i2, i3, i4);
            this.mSRAWrapper.getSocExtImpl().hookPerfLockAcquired();
        }
        if (!this.mStarted) {
            return false;
        }
        this.mFinishAnimReady = true;
        return true;
    }

    public void kill() {
        SurfaceRotationAnimationController surfaceRotationAnimationController = this.mSurfaceRotationAnimationController;
        if (surfaceRotationAnimationController != null) {
            surfaceRotationAnimationController.cancel();
            this.mSurfaceRotationAnimationController = null;
        }
        if (this.mScreenshotLayer != null) {
            if (!ProtoLogGroup.WM_SHOW_SURFACE_ALLOC.isLogToLogcat()) {
                Slog.i(TAG, "  FREEZE " + this.mScreenshotLayer + ": DESTROY");
            } else if (ProtoLogCache.WM_SHOW_SURFACE_ALLOC_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, 1089714158, 0, (String) null, new Object[]{String.valueOf(this.mScreenshotLayer)});
            }
            SurfaceControl.Transaction transaction = this.mService.mTransactionFactory.get();
            if (this.mScreenshotLayer.isValid()) {
                transaction.remove(this.mScreenshotLayer);
            }
            this.mScreenshotLayer = null;
            SurfaceControl surfaceControl = this.mEnterBlackFrameLayer;
            if (surfaceControl != null) {
                if (surfaceControl.isValid()) {
                    transaction.remove(this.mEnterBlackFrameLayer);
                }
                this.mEnterBlackFrameLayer = null;
            }
            SurfaceControl surfaceControl2 = this.mBackColorSurface;
            if (surfaceControl2 != null) {
                if (surfaceControl2.isValid()) {
                    transaction.remove(this.mBackColorSurface);
                }
                this.mBackColorSurface = null;
            }
            if (this.mRoundedCornerOverlay != null) {
                if (this.mDisplayContent.getRotationAnimation() == null || this.mDisplayContent.getRotationAnimation() == this) {
                    setSkipScreenshotForRoundedCornerOverlays(true, transaction);
                    for (SurfaceControl surfaceControl3 : this.mRoundedCornerOverlay) {
                        if (surfaceControl3.isValid()) {
                            transaction.show(surfaceControl3);
                        }
                    }
                }
                this.mRoundedCornerOverlay = null;
            }
            transaction.apply();
        }
        BlackFrame blackFrame = this.mEnteringBlackFrame;
        if (blackFrame != null) {
            blackFrame.kill();
            this.mEnteringBlackFrame = null;
        }
        Animation animation = this.mRotateExitAnimation;
        if (animation != null) {
            animation.cancel();
            this.mRotateExitAnimation = null;
        }
        Animation animation2 = this.mRotateEnterAnimation;
        if (animation2 != null) {
            animation2.cancel();
            this.mRotateEnterAnimation = null;
        }
        Animation animation3 = this.mRotateAlphaAnimation;
        if (animation3 != null) {
            animation3.cancel();
            this.mRotateAlphaAnimation = null;
        }
        this.mSRAWrapper.getSocExtImpl().hookPerfLockRelease();
    }

    public boolean isAnimating() {
        SurfaceRotationAnimationController surfaceRotationAnimationController = this.mSurfaceRotationAnimationController;
        return surfaceRotationAnimationController != null && surfaceRotationAnimationController.isAnimating();
    }

    public boolean isRotating() {
        return this.mCurRotation != this.mOriginalRotation;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class SurfaceRotationAnimationController {
        private SurfaceAnimator mDisplayAnimator;
        private SurfaceAnimator mEnterBlackFrameAnimator;
        private SurfaceAnimator mRotateScreenAnimator;
        private SurfaceAnimator mScreenshotRotationAnimator;

        SurfaceRotationAnimationController() {
        }

        void startCustomAnimation() {
            try {
                ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner.deferStartingAnimations();
                this.mRotateScreenAnimator = startScreenshotAlphaAnimation();
                this.mDisplayAnimator = startDisplayRotation();
                if (ScreenRotationAnimation.this.mEnteringBlackFrame != null) {
                    this.mEnterBlackFrameAnimator = startEnterBlackFrameAnimation();
                }
                ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().notifyScreenshotAnimationStart();
            } finally {
                ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner.continueStartingAnimations();
            }
        }

        void startScreenRotationAnimation() {
            try {
                ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner.deferStartingAnimations();
                this.mDisplayAnimator = startDisplayRotation();
                this.mScreenshotRotationAnimator = startScreenshotRotationAnimation();
                if (!ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().startScreenRotateBackColorAnimation(new float[]{ScreenRotationAnimation.this.mEndLuma, ScreenRotationAnimation.this.mEndLuma, ScreenRotationAnimation.this.mEndLuma}, ScreenRotationAnimation.this.mRotateEnterAnimation, ScreenRotationAnimation.this.mBackColorSurface, ScreenRotationAnimation.this.mDisplayContent)) {
                    startColorAnimation();
                }
            } finally {
                ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner.continueStartingAnimations();
            }
        }

        private SimpleSurfaceAnimatable.Builder initializeBuilder() {
            SimpleSurfaceAnimatable.Builder builder = new SimpleSurfaceAnimatable.Builder();
            final DisplayContent displayContent = ScreenRotationAnimation.this.mDisplayContent;
            Objects.requireNonNull(displayContent);
            SimpleSurfaceAnimatable.Builder syncTransactionSupplier = builder.setSyncTransactionSupplier(new Supplier() { // from class: com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final Object get() {
                    return DisplayContent.this.getSyncTransaction();
                }
            });
            final DisplayContent displayContent2 = ScreenRotationAnimation.this.mDisplayContent;
            Objects.requireNonNull(displayContent2);
            SimpleSurfaceAnimatable.Builder pendingTransactionSupplier = syncTransactionSupplier.setPendingTransactionSupplier(new Supplier() { // from class: com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda2
                @Override // java.util.function.Supplier
                public final Object get() {
                    return DisplayContent.this.getPendingTransaction();
                }
            });
            final DisplayContent displayContent3 = ScreenRotationAnimation.this.mDisplayContent;
            Objects.requireNonNull(displayContent3);
            SimpleSurfaceAnimatable.Builder commitTransactionRunnable = pendingTransactionSupplier.setCommitTransactionRunnable(new Runnable() { // from class: com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayContent.this.commitPendingTransaction();
                }
            });
            final DisplayContent displayContent4 = ScreenRotationAnimation.this.mDisplayContent;
            Objects.requireNonNull(displayContent4);
            return commitTransactionRunnable.setAnimationLeashSupplier(new Supplier() { // from class: com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda4
                @Override // java.util.function.Supplier
                public final Object get() {
                    return DisplayContent.this.makeOverlay();
                }
            });
        }

        private SurfaceAnimator startDisplayRotation() {
            SurfaceAnimator startAnimation = startAnimation(initializeBuilder().setAnimationLeashParent(ScreenRotationAnimation.this.mDisplayContent.getSurfaceControl()).setSurfaceControl(ScreenRotationAnimation.this.mDisplayContent.getWindowingLayer()).setParentSurfaceControl(ScreenRotationAnimation.this.mDisplayContent.getSurfaceControl()).setWidth(ScreenRotationAnimation.this.mDisplayContent.getSurfaceWidth()).setHeight(ScreenRotationAnimation.this.mDisplayContent.getSurfaceHeight()).build(), createWindowAnimationSpec(ScreenRotationAnimation.this.mRotateEnterAnimation), new ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda0(this));
            Rect bounds = ScreenRotationAnimation.this.mDisplayContent.getBounds();
            ScreenRotationAnimation.this.mDisplayContent.getPendingTransaction().setWindowCrop(startAnimation.mLeash, bounds.width(), bounds.height());
            return startAnimation;
        }

        private SurfaceAnimator startScreenshotAlphaAnimation() {
            return startAnimation(initializeBuilder().setSurfaceControl(ScreenRotationAnimation.this.mScreenshotLayer).setAnimationLeashParent(ScreenRotationAnimation.this.mDisplayContent.getOverlayLayer()).setWidth(ScreenRotationAnimation.this.mDisplayContent.getSurfaceWidth()).setHeight(ScreenRotationAnimation.this.mDisplayContent.getSurfaceHeight()).build(), createWindowAnimationSpec(ScreenRotationAnimation.this.mRotateAlphaAnimation), new ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda0(this));
        }

        private SurfaceAnimator startEnterBlackFrameAnimation() {
            return startAnimation(initializeBuilder().setSurfaceControl(ScreenRotationAnimation.this.mEnterBlackFrameLayer).setAnimationLeashParent(ScreenRotationAnimation.this.mDisplayContent.getOverlayLayer()).build(), createWindowAnimationSpec(ScreenRotationAnimation.this.mRotateEnterAnimation), new ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda0(this));
        }

        private SurfaceAnimator startScreenshotRotationAnimation() {
            return startAnimation(initializeBuilder().setSurfaceControl(ScreenRotationAnimation.this.mScreenshotLayer).setAnimationLeashParent(ScreenRotationAnimation.this.mDisplayContent.getOverlayLayer()).build(), createWindowAnimationSpec(ScreenRotationAnimation.this.mRotateExitAnimation), new ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda0(this));
        }

        private void startColorAnimation() {
            int integer = ScreenRotationAnimation.this.mContext.getResources().getInteger(R.integer.leanback_setup_translation_forward_in_content_delay);
            ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner.startAnimation(new LocalAnimationAdapter.AnimationSpec() { // from class: com.android.server.wm.ScreenRotationAnimation.SurfaceRotationAnimationController.1
                final /* synthetic */ int val$colorTransitionMs;
                final /* synthetic */ long val$duration;
                final /* synthetic */ int val$endColor;
                final /* synthetic */ float[] val$rgbTmpFloat;
                final /* synthetic */ int val$startColor;
                final /* synthetic */ ArgbEvaluator val$va;

                AnonymousClass1(long j, ArgbEvaluator argbEvaluator, int i, int i2, float[] fArr, int integer2) {
                    r2 = j;
                    r4 = argbEvaluator;
                    r5 = i;
                    r6 = i2;
                    r7 = fArr;
                    r8 = integer2;
                }

                @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
                public long getDuration() {
                    return r2;
                }

                @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
                public void apply(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, long j) {
                    Color valueOf = Color.valueOf(((Integer) r4.evaluate(getFraction((float) j), Integer.valueOf(r5), Integer.valueOf(r6))).intValue());
                    r7[0] = valueOf.red();
                    r7[1] = valueOf.green();
                    r7[2] = valueOf.blue();
                    if (surfaceControl.isValid()) {
                        transaction.setColor(surfaceControl, r7);
                    }
                }

                @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
                public void dump(PrintWriter printWriter, String str) {
                    printWriter.println(str + "startLuma=" + ScreenRotationAnimation.this.mStartLuma + " endLuma=" + ScreenRotationAnimation.this.mEndLuma + " durationMs=" + r8);
                }

                @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
                public void dumpDebugInner(ProtoOutputStream protoOutputStream) {
                    long start = protoOutputStream.start(1146756268036L);
                    protoOutputStream.write(1108101562369L, ScreenRotationAnimation.this.mStartLuma);
                    protoOutputStream.write(1108101562370L, ScreenRotationAnimation.this.mEndLuma);
                    protoOutputStream.write(1112396529667L, r8);
                    protoOutputStream.end(start);
                }
            }, ScreenRotationAnimation.this.mBackColorSurface, ScreenRotationAnimation.this.mDisplayContent.getPendingTransaction(), null);
        }

        /* renamed from: com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$1 */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public class AnonymousClass1 implements LocalAnimationAdapter.AnimationSpec {
            final /* synthetic */ int val$colorTransitionMs;
            final /* synthetic */ long val$duration;
            final /* synthetic */ int val$endColor;
            final /* synthetic */ float[] val$rgbTmpFloat;
            final /* synthetic */ int val$startColor;
            final /* synthetic */ ArgbEvaluator val$va;

            AnonymousClass1(long j, ArgbEvaluator argbEvaluator, int i, int i2, float[] fArr, int integer2) {
                r2 = j;
                r4 = argbEvaluator;
                r5 = i;
                r6 = i2;
                r7 = fArr;
                r8 = integer2;
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public long getDuration() {
                return r2;
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public void apply(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, long j) {
                Color valueOf = Color.valueOf(((Integer) r4.evaluate(getFraction((float) j), Integer.valueOf(r5), Integer.valueOf(r6))).intValue());
                r7[0] = valueOf.red();
                r7[1] = valueOf.green();
                r7[2] = valueOf.blue();
                if (surfaceControl.isValid()) {
                    transaction.setColor(surfaceControl, r7);
                }
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public void dump(PrintWriter printWriter, String str) {
                printWriter.println(str + "startLuma=" + ScreenRotationAnimation.this.mStartLuma + " endLuma=" + ScreenRotationAnimation.this.mEndLuma + " durationMs=" + r8);
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public void dumpDebugInner(ProtoOutputStream protoOutputStream) {
                long start = protoOutputStream.start(1146756268036L);
                protoOutputStream.write(1108101562369L, ScreenRotationAnimation.this.mStartLuma);
                protoOutputStream.write(1108101562370L, ScreenRotationAnimation.this.mEndLuma);
                protoOutputStream.write(1112396529667L, r8);
                protoOutputStream.end(start);
            }
        }

        private WindowAnimationSpec createWindowAnimationSpec(Animation animation) {
            return new WindowAnimationSpec(animation, new Point(0, 0), false, ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().getWindowCornerRadius());
        }

        private SurfaceAnimator startAnimation(SurfaceAnimator.Animatable animatable, LocalAnimationAdapter.AnimationSpec animationSpec, SurfaceAnimator.OnAnimationFinishedCallback onAnimationFinishedCallback) {
            SurfaceAnimator surfaceAnimator = new SurfaceAnimator(animatable, onAnimationFinishedCallback, ScreenRotationAnimation.this.mService);
            LocalAnimationAdapter localAnimationAdapter = new LocalAnimationAdapter(animationSpec, ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner);
            if (!ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().hookAdjustScreenshotInitialRotation(localAnimationAdapter, surfaceAnimator, ScreenRotationAnimation.this.mOriginalWidth, ScreenRotationAnimation.this.mOriginalHeight, false, ScreenRotationAnimation.this.mDisplayContent, ScreenRotationAnimation.this.mScreenshotLayer, ScreenRotationAnimation.this.mCurRotation)) {
                surfaceAnimator.startAnimation(ScreenRotationAnimation.this.mDisplayContent.getPendingTransaction(), localAnimationAdapter, false, 2);
            }
            return surfaceAnimator;
        }

        public void onAnimationEnd(int i, AnimationAdapter animationAdapter) {
            WindowManagerGlobalLock windowManagerGlobalLock = ScreenRotationAnimation.this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (isAnimating()) {
                        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                            long j = i;
                            SurfaceAnimator surfaceAnimator = this.mDisplayAnimator;
                            String valueOf = String.valueOf(surfaceAnimator != null ? Boolean.valueOf(surfaceAnimator.isAnimating()) : null);
                            SurfaceAnimator surfaceAnimator2 = this.mEnterBlackFrameAnimator;
                            String valueOf2 = String.valueOf(surfaceAnimator2 != null ? Boolean.valueOf(surfaceAnimator2.isAnimating()) : null);
                            SurfaceAnimator surfaceAnimator3 = this.mRotateScreenAnimator;
                            String valueOf3 = String.valueOf(surfaceAnimator3 != null ? Boolean.valueOf(surfaceAnimator3.isAnimating()) : null);
                            SurfaceAnimator surfaceAnimator4 = this.mScreenshotRotationAnimator;
                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1346895820, 1, (String) null, new Object[]{Long.valueOf(j), valueOf, valueOf2, valueOf3, String.valueOf(surfaceAnimator4 != null ? Boolean.valueOf(surfaceAnimator4.isAnimating()) : null)});
                        }
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (!ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                        Slog.d(ScreenRotationAnimation.TAG, "ScreenRotationAnimation onAnimationEnd");
                    } else if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ORIENTATION, 200829729, 0, (String) null, (Object[]) null);
                    }
                    this.mEnterBlackFrameAnimator = null;
                    this.mScreenshotRotationAnimator = null;
                    this.mRotateScreenAnimator = null;
                    ScreenRotationAnimation.this.mService.mAnimator.mBulkUpdateParams |= 1;
                    ScreenRotationAnimation rotationAnimation = ScreenRotationAnimation.this.mDisplayContent.getRotationAnimation();
                    ScreenRotationAnimation screenRotationAnimation = ScreenRotationAnimation.this;
                    if (rotationAnimation == screenRotationAnimation) {
                        screenRotationAnimation.mDisplayContent.setRotationAnimation(null);
                        if (ScreenRotationAnimation.this.mDisplayContent.mDisplayRotationCompatPolicy != null) {
                            ScreenRotationAnimation.this.mDisplayContent.mDisplayRotationCompatPolicy.onScreenRotationAnimationFinished();
                        }
                    } else {
                        screenRotationAnimation.kill();
                    }
                    ScreenRotationAnimation.this.mService.updateRotation(false, false);
                    ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().setFrozenByUserSwitching(false);
                    ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().adjustBlurBackgroundLayer();
                    ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().onScreenRotationAnimationEnd();
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        public void cancel() {
            SurfaceAnimator surfaceAnimator = this.mEnterBlackFrameAnimator;
            if (surfaceAnimator != null) {
                surfaceAnimator.cancelAnimation();
            }
            SurfaceAnimator surfaceAnimator2 = this.mScreenshotRotationAnimator;
            if (surfaceAnimator2 != null) {
                surfaceAnimator2.cancelAnimation();
            }
            SurfaceAnimator surfaceAnimator3 = this.mRotateScreenAnimator;
            if (surfaceAnimator3 != null) {
                surfaceAnimator3.cancelAnimation();
            }
            SurfaceAnimator surfaceAnimator4 = this.mDisplayAnimator;
            if (surfaceAnimator4 != null) {
                surfaceAnimator4.cancelAnimation();
            }
            if (ScreenRotationAnimation.this.mBackColorSurface != null) {
                ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner.onAnimationCancelled(ScreenRotationAnimation.this.mBackColorSurface);
            }
        }

        public boolean isAnimating() {
            SurfaceAnimator surfaceAnimator;
            SurfaceAnimator surfaceAnimator2;
            SurfaceAnimator surfaceAnimator3;
            SurfaceAnimator surfaceAnimator4 = this.mDisplayAnimator;
            return (surfaceAnimator4 != null && surfaceAnimator4.isAnimating()) || ((surfaceAnimator = this.mEnterBlackFrameAnimator) != null && surfaceAnimator.isAnimating()) || (((surfaceAnimator2 = this.mRotateScreenAnimator) != null && surfaceAnimator2.isAnimating()) || ((surfaceAnimator3 = this.mScreenshotRotationAnimator) != null && surfaceAnimator3.isAnimating()));
        }
    }

    public IScreenRotationAnimationWrapper getWrapper() {
        return this.mSRAWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class ScreenRotationAnimationWrapper implements IScreenRotationAnimationWrapper {
        /* synthetic */ ScreenRotationAnimationWrapper(ScreenRotationAnimation screenRotationAnimation, ScreenRotationAnimationWrapperIA screenRotationAnimationWrapperIA) {
            this();
        }

        private ScreenRotationAnimationWrapper() {
        }

        public IScreenRotationAnimationExt getExtImpl() {
            return ScreenRotationAnimation.this.mSRAExt;
        }

        public IScreenRotationAnimationSocExt getSocExtImpl() {
            return ScreenRotationAnimation.this.mAnimationSocExt;
        }

        @Override // com.android.server.wm.IScreenRotationAnimationWrapper
        public void setRotateExitAnimation(Animation animation) {
            ScreenRotationAnimation.this.mRotateExitAnimation = animation;
        }

        @Override // com.android.server.wm.IScreenRotationAnimationWrapper
        public void setRotateEnterAnimation(Animation animation) {
            ScreenRotationAnimation.this.mRotateEnterAnimation = animation;
        }

        @Override // com.android.server.wm.IScreenRotationAnimationWrapper
        public void setCurRotation(int i) {
            ScreenRotationAnimation.this.mCurRotation = i;
        }

        @Override // com.android.server.wm.IScreenRotationAnimationWrapper
        public void setEndLuma(float f) {
            ScreenRotationAnimation.this.mEndLuma = f;
        }
    }
}
