package com.android.server.wm;

import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Debug;
import android.os.SystemProperties;
import android.util.Slog;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import android.view.InsetsSource;
import android.view.InsetsSourceControl;
import android.view.SurfaceControl;
import android.view.WindowInsets;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.function.TriFunction;
import com.android.server.wm.SurfaceAnimator;
import java.io.PrintWriter;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class InsetsSourceProvider {
    static boolean DEBUG_PANIC = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String TAG = "InsetsSourceProvider";
    private ControlAdapter mAdapter;
    private boolean mClientVisible;
    private InsetsSourceControl mControl;
    private InsetsControlTarget mControlTarget;
    private final boolean mControllable;
    protected final DisplayContent mDisplayContent;
    private final InsetsSourceControl mFakeControl;
    private InsetsControlTarget mFakeControlTarget;
    private TriFunction<DisplayFrames, WindowContainer, Rect, Integer> mFrameProvider;
    private boolean mIsLeashReadyForDispatching;
    private SparseArray<TriFunction<DisplayFrames, WindowContainer, Rect, Integer>> mOverrideFrameProviders;
    private InsetsControlTarget mPendingControlTarget;
    private boolean mSeamlessRotating;
    private boolean mServerVisible;
    protected final InsetsSource mSource;
    private final InsetsStateController mStateController;
    protected WindowContainer mWindowContainer;
    private final Rect mTmpRect = new Rect();
    private final SparseArray<Rect> mOverrideFrames = new SparseArray<>();
    private final Rect mSourceFrame = new Rect();
    private final Rect mLastSourceFrame = new Rect();
    private Insets mInsetsHint = Insets.NONE;
    private final Consumer<SurfaceControl.Transaction> mSetLeashPositionConsumer = new Consumer() { // from class: com.android.server.wm.InsetsSourceProvider$$ExternalSyntheticLambda0
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            InsetsSourceProvider.this.lambda$new$0((SurfaceControl.Transaction) obj);
        }
    };
    private boolean mCropToProvidingInsets = false;
    public IInsetsSourceProviderExt mInsetsSourceProviderExt = (IInsetsSourceProviderExt) ExtLoader.type(IInsetsSourceProviderExt.class).base(this).create();

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(SurfaceControl.Transaction transaction) {
        SurfaceControl leash;
        InsetsSourceControl insetsSourceControl = this.mControl;
        if (insetsSourceControl == null || (leash = insetsSourceControl.getLeash()) == null || !leash.isValid()) {
            return;
        }
        Point surfacePosition = this.mControl.getSurfacePosition();
        if (DEBUG_PANIC && this.mControl.getType() == WindowInsets.Type.navigationBars()) {
            Slog.d(TAG, "set leash position, the mControl: " + this.mControl.toString());
        }
        transaction.setPosition(leash, surfacePosition.x, surfacePosition.y);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsSourceProvider(InsetsSource insetsSource, InsetsStateController insetsStateController, DisplayContent displayContent) {
        this.mClientVisible = (WindowInsets.Type.defaultVisible() & insetsSource.getType()) != 0;
        this.mSource = insetsSource;
        this.mDisplayContent = displayContent;
        this.mStateController = insetsStateController;
        this.mFakeControl = new InsetsSourceControl(insetsSource.getId(), insetsSource.getType(), (SurfaceControl) null, false, new Point(), Insets.NONE);
        this.mControllable = (insetsSource.getType() & InsetsPolicy.CONTROLLABLE_TYPES) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsSource getSource() {
        return this.mSource;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isControllable() {
        return this.mControllable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowContainer(WindowContainer windowContainer, TriFunction<DisplayFrames, WindowContainer, Rect, Integer> triFunction, SparseArray<TriFunction<DisplayFrames, WindowContainer, Rect, Integer>> sparseArray) {
        if (DEBUG_PANIC && this.mSource.getType() == WindowInsets.Type.navigationBars()) {
            Slog.d(TAG, "InsetsSource setWin: " + windowContainer + ", type: " + WindowInsets.Type.toString(this.mSource.getType()) + ", mWindowContainer: " + this.mWindowContainer + ", mPendingControlTarget: " + this.mPendingControlTarget + ", caller: " + Debug.getCallers(5));
        }
        WindowContainer windowContainer2 = this.mWindowContainer;
        if (windowContainer2 != null) {
            if (this.mControllable) {
                windowContainer2.setControllableInsetProvider(null);
            }
            this.mWindowContainer.cancelAnimation();
            this.mWindowContainer.getInsetsSourceProviders().remove(this.mSource.getId());
            this.mSeamlessRotating = false;
        }
        if (ProtoLogCache.WM_DEBUG_WINDOW_INSETS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, -1483435730, 0, (String) null, new Object[]{String.valueOf(windowContainer), String.valueOf(WindowInsets.Type.toString(this.mSource.getType()))});
        }
        this.mWindowContainer = windowContainer;
        this.mFrameProvider = triFunction;
        this.mOverrideFrames.clear();
        this.mOverrideFrameProviders = sparseArray;
        if (windowContainer == null) {
            setServerVisible(false);
            this.mSource.setVisibleFrame((Rect) null);
            this.mSource.setInsetsRoundedCornerFrame(false);
            this.mSourceFrame.setEmpty();
            return;
        }
        this.mWindowContainer.getInsetsSourceProviders().put(this.mSource.getId(), this);
        if (this.mControllable) {
            this.mWindowContainer.setControllableInsetProvider(this);
            InsetsControlTarget insetsControlTarget = this.mPendingControlTarget;
            if (insetsControlTarget != null) {
                updateControlForTarget(insetsControlTarget, true);
                this.mPendingControlTarget = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateSourceFrame(Rect rect) {
        Rect rect2;
        WindowContainer windowContainer = this.mWindowContainer;
        if (windowContainer == null) {
            return;
        }
        WindowState asWindowState = windowContainer.asWindowState();
        if (asWindowState == null) {
            if (this.mServerVisible) {
                this.mTmpRect.set(this.mWindowContainer.getBounds());
                TriFunction<DisplayFrames, WindowContainer, Rect, Integer> triFunction = this.mFrameProvider;
                if (triFunction != null) {
                    triFunction.apply(this.mWindowContainer.getDisplayContent().mDisplayFrames, this.mWindowContainer, this.mTmpRect);
                }
            } else {
                this.mTmpRect.setEmpty();
            }
            this.mSource.setFrame(this.mTmpRect);
            this.mSource.setVisibleFrame((Rect) null);
            return;
        }
        this.mSourceFrame.set(rect);
        TriFunction<DisplayFrames, WindowContainer, Rect, Integer> triFunction2 = this.mFrameProvider;
        if (triFunction2 != null) {
            this.mSource.setFlags(((Integer) triFunction2.apply(this.mWindowContainer.getDisplayContent().mDisplayFrames, this.mWindowContainer, this.mSourceFrame)).intValue());
        }
        updateSourceFrameForServerVisibility();
        SparseArray<TriFunction<DisplayFrames, WindowContainer, Rect, Integer>> sparseArray = this.mOverrideFrameProviders;
        if (sparseArray != null) {
            for (int size = sparseArray.size() - 1; size >= 0; size--) {
                int keyAt = this.mOverrideFrameProviders.keyAt(size);
                if (this.mOverrideFrames.contains(keyAt)) {
                    rect2 = this.mOverrideFrames.get(keyAt);
                    rect2.set(rect);
                } else {
                    rect2 = new Rect(rect);
                }
                if (this.mOverrideFrameProviders.get(keyAt) != null) {
                    this.mOverrideFrameProviders.get(keyAt).apply(this.mWindowContainer.getDisplayContent().mDisplayFrames, this.mWindowContainer, rect2);
                }
                this.mOverrideFrames.put(keyAt, rect2);
            }
        }
        Rect rect3 = asWindowState.mGivenVisibleInsets;
        if (rect3.left != 0 || rect3.top != 0 || rect3.right != 0 || rect3.bottom != 0) {
            this.mTmpRect.set(rect);
            this.mTmpRect.inset(asWindowState.mGivenVisibleInsets);
            this.mSource.setVisibleFrame(this.mTmpRect);
            return;
        }
        this.mSource.setVisibleFrame((Rect) null);
    }

    private void updateSourceFrameForServerVisibility() {
        if (this.mServerVisible) {
            this.mSource.setFrame(this.mSourceFrame);
        } else {
            this.mSource.setFrame(0, 0, 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsSource createSimulatedSource(DisplayFrames displayFrames, Rect rect) {
        InsetsSource insetsSource = new InsetsSource(this.mSource);
        this.mTmpRect.set(rect);
        TriFunction<DisplayFrames, WindowContainer, Rect, Integer> triFunction = this.mFrameProvider;
        if (triFunction != null) {
            triFunction.apply(displayFrames, this.mWindowContainer, this.mTmpRect);
        }
        insetsSource.setFrame(this.mTmpRect);
        insetsSource.setVisibleFrame((Rect) null);
        return insetsSource;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPostLayout() {
        boolean isVisibleRequested;
        AsyncRotationController asyncRotationController;
        SurfaceControl.Transaction drawTransaction;
        WindowContainer windowContainer = this.mWindowContainer;
        if (windowContainer == null) {
            return;
        }
        WindowState asWindowState = windowContainer.asWindowState();
        boolean z = false;
        boolean z2 = true;
        if (asWindowState != null) {
            isVisibleRequested = asWindowState.wouldBeVisibleIfPolicyIgnored() && asWindowState.isVisibleByPolicy();
        } else {
            isVisibleRequested = this.mWindowContainer.isVisibleRequested();
        }
        setServerVisible(isVisibleRequested);
        if (this.mControl != null) {
            Point windowFrameSurfacePosition = getWindowFrameSurfacePosition();
            if (this.mControl.setSurfacePosition(windowFrameSurfacePosition.x, windowFrameSurfacePosition.y) && this.mControlTarget != null) {
                if (this.mControl.getType() == WindowInsets.Type.navigationBars() && this.mDisplayContent.getAsyncRotationController() != null && asWindowState != null && this.mDisplayContent.getAsyncRotationController().isTargetToken(asWindowState.mToken)) {
                    z = true;
                }
                if (asWindowState != null && asWindowState.getWindowFrames().didFrameSizeChange() && asWindowState.mWinAnimator.getShown() && (this.mWindowContainer.okToDisplay() || z)) {
                    asWindowState.applyWithNextDraw(this.mSetLeashPositionConsumer);
                } else {
                    SurfaceControl.Transaction syncTransaction = this.mWindowContainer.getSyncTransaction();
                    if (asWindowState != null && (asyncRotationController = this.mDisplayContent.getAsyncRotationController()) != null && (drawTransaction = asyncRotationController.getDrawTransaction(asWindowState.mToken)) != null) {
                        syncTransaction = drawTransaction;
                    }
                    this.mSetLeashPositionConsumer.accept(syncTransaction);
                }
                z = true;
            }
            if (this.mServerVisible && !this.mLastSourceFrame.equals(this.mSource.getFrame())) {
                Insets calculateInsets = this.mSource.calculateInsets(this.mWindowContainer.getBounds(), true);
                if (calculateInsets.equals(this.mControl.getInsetsHint())) {
                    z2 = z;
                } else {
                    this.mControl.setInsetsHint(calculateInsets);
                    this.mInsetsHint = calculateInsets;
                }
                this.mLastSourceFrame.set(this.mSource.getFrame());
                z = z2;
            }
            if (z) {
                this.mStateController.notifyControlChanged(this.mControlTarget);
            }
        }
    }

    private Point getWindowFrameSurfacePosition() {
        AsyncRotationController asyncRotationController;
        WindowState asWindowState = this.mWindowContainer.asWindowState();
        if (asWindowState != null && this.mControl != null && (asyncRotationController = this.mDisplayContent.getAsyncRotationController()) != null && asyncRotationController.shouldFreezeInsetsPosition(asWindowState)) {
            return this.mControl.getSurfacePosition();
        }
        Rect frame = asWindowState != null ? asWindowState.getFrame() : this.mWindowContainer.getBounds();
        Point point = new Point();
        this.mWindowContainer.transformFrameToSurfacePosition(frame.left, frame.top, point);
        return point;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateFakeControlTarget(InsetsControlTarget insetsControlTarget) {
        if (insetsControlTarget == this.mFakeControlTarget) {
            return;
        }
        this.mFakeControlTarget = insetsControlTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCropToProvidingInsetsBounds(SurfaceControl.Transaction transaction) {
        this.mCropToProvidingInsets = true;
        WindowContainer windowContainer = this.mWindowContainer;
        if (windowContainer == null || !windowContainer.mSurfaceAnimator.hasLeash()) {
            return;
        }
        transaction.setWindowCrop(this.mWindowContainer.mSurfaceAnimator.mLeash, getProvidingInsetsBoundsCropRect());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeCropToProvidingInsetsBounds(SurfaceControl.Transaction transaction) {
        this.mCropToProvidingInsets = false;
        WindowContainer windowContainer = this.mWindowContainer;
        if (windowContainer == null || !windowContainer.mSurfaceAnimator.hasLeash()) {
            return;
        }
        transaction.setWindowCrop(this.mWindowContainer.mSurfaceAnimator.mLeash, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rect getProvidingInsetsBoundsCropRect() {
        Rect bounds;
        if (this.mWindowContainer.asWindowState() != null) {
            bounds = this.mWindowContainer.asWindowState().getFrame();
        } else {
            bounds = this.mWindowContainer.getBounds();
        }
        Rect frame = getSource().getFrame();
        int i = frame.left;
        int i2 = bounds.left;
        int i3 = frame.top;
        int i4 = bounds.top;
        return new Rect(i - i2, i3 - i4, frame.right - i2, frame.bottom - i4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateControlForTarget(InsetsControlTarget insetsControlTarget, boolean z) {
        SurfaceControl.Transaction pendingTransaction;
        InsetsControlTarget insetsControlTarget2;
        if (this.mSeamlessRotating) {
            return;
        }
        WindowContainer windowContainer = this.mWindowContainer;
        if (windowContainer != null && windowContainer.getSurfaceControl() == null) {
            setWindowContainer(null, null, null);
        }
        WindowContainer windowContainer2 = this.mWindowContainer;
        if (windowContainer2 == null) {
            this.mPendingControlTarget = insetsControlTarget;
            return;
        }
        if (insetsControlTarget != this.mControlTarget || z) {
            if (insetsControlTarget == null) {
                windowContainer2.cancelAnimation();
                setClientVisible((WindowInsets.Type.defaultVisible() & this.mSource.getType()) != 0);
                return;
            }
            Point windowFrameSurfacePosition = getWindowFrameSurfacePosition();
            this.mAdapter = new ControlAdapter(windowFrameSurfacePosition);
            if (this.mSource.getType() == WindowInsets.Type.ime()) {
                setClientVisible(insetsControlTarget.isRequestedVisible(WindowInsets.Type.ime()));
            }
            if (this.mInsetsSourceProviderExt.hasFlexibleActivityInfo(insetsControlTarget.getWindow())) {
                pendingTransaction = this.mWindowContainer.getSyncTransaction();
            } else {
                pendingTransaction = this.mWindowContainer.getPendingTransaction();
            }
            this.mWindowContainer.startAnimation(pendingTransaction, this.mAdapter, !this.mClientVisible, 32);
            this.mIsLeashReadyForDispatching = false;
            SurfaceControl surfaceControl = this.mAdapter.mCapturedLeash;
            this.mControlTarget = insetsControlTarget;
            if (getSource().getType() == WindowInsets.Type.ime() && this.mInsetsSourceProviderExt.getInputShowStatus() && ((insetsControlTarget2 = this.mControlTarget) == this.mDisplayContent.mRemoteInsetsControlTarget || insetsControlTarget2.isRequestedVisible(WindowInsets.Type.ime()))) {
                setClientVisible(true);
                if (surfaceControl != null) {
                    pendingTransaction.setAlpha(surfaceControl, 1.0f);
                    pendingTransaction.show(surfaceControl);
                }
            }
            this.mInsetsSourceProviderExt.showImeLeashInCarDisplayIfNeed(this, this.mControlTarget, pendingTransaction, surfaceControl);
            updateVisibility();
            this.mControl = new InsetsSourceControl(this.mSource.getId(), this.mSource.getType(), surfaceControl, this.mClientVisible, windowFrameSurfacePosition, this.mInsetsHint);
            if (DEBUG_PANIC || ProtoLogGroup.WM_DEBUG_IME.isLogToLogcat()) {
                InsetsSourceControl insetsSourceControl = this.mControl;
                Slog.d(TAG, String.format("InsetsSource type %s Control %s for target %s", WindowInsets.Type.toString(insetsSourceControl != null ? insetsSourceControl.getType() : -1), this.mControl, this.mControlTarget) + ", surfacePosition: " + windowFrameSurfacePosition + ", caller: " + Debug.getCallers(8));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startSeamlessRotation() {
        if (this.mSeamlessRotating) {
            return;
        }
        this.mSeamlessRotating = true;
        this.mWindowContainer.cancelAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishSeamlessRotation() {
        this.mSeamlessRotating = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateClientVisibility(InsetsControlTarget insetsControlTarget) {
        boolean isRequestedVisible = insetsControlTarget.isRequestedVisible(this.mSource.getType());
        if (insetsControlTarget != this.mControlTarget || isRequestedVisible == this.mClientVisible) {
            return false;
        }
        setClientVisible(isRequestedVisible);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSurfaceTransactionApplied() {
        this.mIsLeashReadyForDispatching = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setClientVisible(boolean z) {
        if (this.mClientVisible == z) {
            return;
        }
        this.mClientVisible = z;
        updateVisibility();
        this.mDisplayContent.setLayoutNeeded();
        this.mDisplayContent.mWmService.mWindowPlacerLocked.requestTraversal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void setServerVisible(boolean z) {
        this.mServerVisible = z;
        updateSourceFrameForServerVisibility();
        updateVisibility();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateVisibility() {
        this.mSource.setVisible(this.mServerVisible && this.mClientVisible);
        if (ProtoLogCache.WM_DEBUG_WINDOW_INSETS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, 2070726247, 0, (String) null, new Object[]{String.valueOf(WindowInsets.Type.toString(this.mSource.getType())), String.valueOf(this.mServerVisible), String.valueOf(this.mClientVisible)});
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsSourceControl getControl(InsetsControlTarget insetsControlTarget) {
        if (insetsControlTarget == this.mControlTarget) {
            if (!this.mIsLeashReadyForDispatching && this.mControl != null) {
                return new InsetsSourceControl(this.mControl.getId(), this.mControl.getType(), (SurfaceControl) null, this.mControl.isInitiallyVisible(), this.mControl.getSurfacePosition(), this.mControl.getInsetsHint());
            }
            return this.mControl;
        }
        if (insetsControlTarget == this.mFakeControlTarget) {
            return this.mFakeControl;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsControlTarget getControlTarget() {
        return this.mControlTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsControlTarget getFakeControlTarget() {
        return this.mFakeControlTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isClientVisible() {
        return this.mClientVisible;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean overridesFrame(int i) {
        return this.mOverrideFrames.contains(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getOverriddenFrame(int i) {
        return this.mOverrideFrames.get(i);
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + getClass().getSimpleName());
        String str2 = str + "  ";
        printWriter.print(str2 + "mSource=");
        this.mSource.dump("", printWriter);
        printWriter.print(str2 + "mSourceFrame=");
        printWriter.println(this.mSourceFrame);
        if (this.mOverrideFrames.size() > 0) {
            printWriter.print(str2 + "mOverrideFrames=");
            printWriter.println(this.mOverrideFrames);
        }
        if (this.mControl != null) {
            printWriter.print(str2 + "mControl=");
            this.mControl.dump("", printWriter);
        }
        printWriter.print(str2);
        printWriter.print("mIsLeashReadyForDispatching=");
        printWriter.print(this.mIsLeashReadyForDispatching);
        printWriter.println();
        if (this.mWindowContainer != null) {
            printWriter.print(str2 + "mWindowContainer=");
            printWriter.println(this.mWindowContainer);
        }
        if (this.mAdapter != null) {
            printWriter.print(str2 + "mAdapter=");
            this.mAdapter.dump(printWriter, "");
        }
        if (this.mControlTarget != null) {
            printWriter.print(str2 + "mControlTarget=");
            printWriter.println(this.mControlTarget);
        }
        if (this.mPendingControlTarget != null) {
            printWriter.print(str2 + "mPendingControlTarget=");
            printWriter.println(this.mPendingControlTarget);
        }
        if (this.mFakeControlTarget != null) {
            printWriter.print(str2 + "mFakeControlTarget=");
            printWriter.println(this.mFakeControlTarget);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j, int i) {
        long start = protoOutputStream.start(j);
        this.mSource.dumpDebug(protoOutputStream, 1146756268033L);
        this.mTmpRect.dumpDebug(protoOutputStream, 1146756268034L);
        this.mFakeControl.dumpDebug(protoOutputStream, 1146756268035L);
        InsetsSourceControl insetsSourceControl = this.mControl;
        if (insetsSourceControl != null) {
            insetsSourceControl.dumpDebug(protoOutputStream, 1146756268036L);
        }
        InsetsControlTarget insetsControlTarget = this.mControlTarget;
        if (insetsControlTarget != null && insetsControlTarget.getWindow() != null) {
            this.mControlTarget.getWindow().dumpDebug(protoOutputStream, 1146756268037L, i);
        }
        InsetsControlTarget insetsControlTarget2 = this.mPendingControlTarget;
        if (insetsControlTarget2 != null && insetsControlTarget2.getWindow() != null) {
            this.mPendingControlTarget.getWindow().dumpDebug(protoOutputStream, 1146756268038L, i);
        }
        InsetsControlTarget insetsControlTarget3 = this.mFakeControlTarget;
        if (insetsControlTarget3 != null && insetsControlTarget3.getWindow() != null) {
            this.mFakeControlTarget.getWindow().dumpDebug(protoOutputStream, 1146756268039L, i);
        }
        ControlAdapter controlAdapter = this.mAdapter;
        if (controlAdapter != null && controlAdapter.mCapturedLeash != null) {
            this.mAdapter.mCapturedLeash.dumpDebug(protoOutputStream, 1146756268040L);
        }
        protoOutputStream.write(1133871366154L, this.mIsLeashReadyForDispatching);
        protoOutputStream.write(1133871366155L, this.mClientVisible);
        protoOutputStream.write(1133871366156L, this.mServerVisible);
        protoOutputStream.write(1133871366157L, this.mSeamlessRotating);
        protoOutputStream.write(1133871366159L, this.mControllable);
        protoOutputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class ControlAdapter implements AnimationAdapter {
        private SurfaceControl mCapturedLeash;
        private final Point mSurfacePosition;

        @Override // com.android.server.wm.AnimationAdapter
        public void dumpDebug(ProtoOutputStream protoOutputStream) {
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getDurationHint() {
            return 0L;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public boolean getShowWallpaper() {
            return false;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getStatusBarTransitionsStartTime() {
            return 0L;
        }

        ControlAdapter(Point point) {
            this.mSurfacePosition = point;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void startAnimation(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, int i, SurfaceAnimator.OnAnimationFinishedCallback onAnimationFinishedCallback) {
            if (InsetsSourceProvider.this.mSource.getType() == WindowInsets.Type.ime()) {
                transaction.setAlpha(surfaceControl, 1.0f);
                transaction.hide(surfaceControl);
            }
            if (ProtoLogCache.WM_DEBUG_WINDOW_INSETS_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, -1185473319, 0, (String) null, new Object[]{String.valueOf(InsetsSourceProvider.this.mSource), String.valueOf(InsetsSourceProvider.this.mControlTarget)});
            }
            if (InsetsSourceProvider.DEBUG_PANIC && InsetsSourceProvider.this.mSource.getType() == WindowInsets.Type.navigationBars()) {
                Slog.d(InsetsSourceProvider.TAG, "startAnimation, source: " + InsetsSourceProvider.this.mSource.toString() + ", mControlTarget: " + InsetsSourceProvider.this.mControlTarget + ", mSurfacePosition: " + this.mSurfacePosition);
            }
            this.mCapturedLeash = surfaceControl;
            Point point = this.mSurfacePosition;
            transaction.setPosition(surfaceControl, point.x, point.y);
            if (InsetsSourceProvider.this.mCropToProvidingInsets) {
                transaction.setWindowCrop(this.mCapturedLeash, InsetsSourceProvider.this.getProvidingInsetsBoundsCropRect());
            }
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void onAnimationCancelled(SurfaceControl surfaceControl) {
            if (InsetsSourceProvider.this.mAdapter == this) {
                InsetsSourceProvider.this.mStateController.notifyControlRevoked(InsetsSourceProvider.this.mControlTarget, InsetsSourceProvider.this);
                InsetsSourceProvider.this.mControl = null;
                InsetsSourceProvider.this.mControlTarget = null;
                InsetsSourceProvider.this.mAdapter = null;
                InsetsSourceProvider.this.setClientVisible((WindowInsets.Type.defaultVisible() & InsetsSourceProvider.this.mSource.getType()) != 0);
                if (ProtoLogCache.WM_DEBUG_WINDOW_INSETS_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, -1394745488, 0, (String) null, new Object[]{String.valueOf(InsetsSourceProvider.this.mSource), String.valueOf(InsetsSourceProvider.this.mControlTarget)});
                }
            }
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dump(PrintWriter printWriter, String str) {
            printWriter.print(str + "ControlAdapter mCapturedLeash=");
            printWriter.print(this.mCapturedLeash);
            printWriter.println();
        }
    }
}
