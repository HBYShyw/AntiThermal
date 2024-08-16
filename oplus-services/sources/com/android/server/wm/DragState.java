package com.android.server.wm;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.input.InputManagerGlobal;
import android.os.Binder;
import android.os.IBinder;
import android.os.InputConstants;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Slog;
import android.view.Display;
import android.view.DragEvent;
import android.view.InputApplicationHandle;
import android.view.InputChannel;
import android.view.InputWindowHandle;
import android.view.OplusBaseLayoutParams;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.view.IDragAndDropPermissions;
import com.android.server.LocalServices;
import com.android.server.pm.UserManagerInternal;
import com.android.server.wm.DragState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DragState {
    private static final String ANIMATED_PROPERTY_ALPHA = "alpha";
    private static final String ANIMATED_PROPERTY_SCALE = "scale";
    private static final String ANIMATED_PROPERTY_X = "x";
    private static final String ANIMATED_PROPERTY_Y = "y";
    private static final int DRAG_FLAGS_URI_ACCESS = 3;
    private static final int DRAG_FLAGS_URI_PERMISSIONS = 195;
    private static final long MAX_ANIMATION_DURATION_MS = 375;
    private static final long MIN_ANIMATION_DURATION_MS = 195;
    ValueAnimator mAnimator;
    boolean mCrossProfileCopyAllowed;
    float mCurrentX;
    float mCurrentY;
    ClipData mData;
    ClipDescription mDataDescription;
    DisplayContent mDisplayContent;
    final DragDropController mDragDropController;
    boolean mDragInProgress;
    boolean mDragResult;
    int mFlags;
    InputInterceptor mInputInterceptor;
    SurfaceControl mInputSurface;
    private boolean mIsClosing;
    IBinder mLocalWin;
    float mOriginalAlpha;
    float mOriginalX;
    float mOriginalY;
    int mPid;
    boolean mRelinquishDragSurfaceToDropTarget;
    final WindowManagerService mService;
    int mSourceUserId;
    SurfaceControl mSurfaceControl;
    float mThumbOffsetX;
    float mThumbOffsetY;
    IBinder mToken;
    int mTouchSource;
    final SurfaceControl.Transaction mTransaction;
    int mUid;
    public int mWindowFlag;
    float mAnimatedScale = 1.0f;
    volatile boolean mAnimationCompleted = false;
    private final Interpolator mCubicEaseOutInterpolator = new DecelerateInterpolator(1.5f);
    private final Point mDisplaySize = new Point();
    private final Rect mTmpClipRect = new Rect();
    private DragStateWrapper mDragStateWrapper = new DragStateWrapper();
    ArrayList<WindowState> mNotifiedWindows = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public DragState(WindowManagerService windowManagerService, DragDropController dragDropController, IBinder iBinder, SurfaceControl surfaceControl, int i, IBinder iBinder2) {
        this.mService = windowManagerService;
        this.mDragDropController = dragDropController;
        this.mToken = iBinder;
        this.mSurfaceControl = surfaceControl;
        this.mFlags = i;
        this.mLocalWin = iBinder2;
        this.mTransaction = windowManagerService.mTransactionFactory.get();
        WindowManager.LayoutParams attrs = windowManagerService.mWindowMap.get(this.mLocalWin).getAttrs();
        if (attrs != null && OplusBaseLayoutParams.class.isInstance(attrs)) {
            this.mWindowFlag = ((OplusBaseLayoutParams) attrs).oplusFlags;
        }
        if (WindowManagerDebugConfig.DEBUG_DRAG) {
            Slog.d("WindowManager", "get oplusFlags from LayoutParams, mWindowFlag = " + this.mWindowFlag);
        }
    }

    public Point getDisplaySize() {
        return this.mDisplaySize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isClosing() {
        return this.mIsClosing;
    }

    private CompletableFuture<Void> showInputSurface() {
        if (this.mInputSurface == null) {
            this.mInputSurface = this.mService.makeSurfaceBuilder(this.mDisplayContent.getSession()).setContainerLayer().setName("Drag and Drop Input Consumer").setCallsite("DragState.showInputSurface").setParent(this.mDisplayContent.getOverlayLayer()).build();
        }
        InputWindowHandle inputWindowHandle = getInputWindowHandle();
        if (inputWindowHandle == null) {
            Slog.w("WindowManager", "Drag is in progress but there is no drag window handle.");
            return CompletableFuture.completedFuture(null);
        }
        Rect rect = this.mTmpClipRect;
        Point point = this.mDisplaySize;
        rect.set(0, 0, point.x, point.y);
        this.mTransaction.show(this.mInputSurface).setInputWindowInfo(this.mInputSurface, inputWindowHandle).setLayer(this.mInputSurface, Integer.MAX_VALUE).setCrop(this.mInputSurface, this.mTmpClipRect);
        final CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        this.mTransaction.addWindowInfosReportedListener(new Runnable() { // from class: com.android.server.wm.DragState$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                completableFuture.complete(null);
            }
        }).apply();
        return completableFuture;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeLocked() {
        float f;
        float f2;
        SurfaceControl surfaceControl;
        this.mIsClosing = true;
        if (this.mInputInterceptor != null) {
            if (WindowManagerDebugConfig.DEBUG_DRAG) {
                Slog.d("WindowManager", "unregistering drag input channel");
            }
            this.mDragDropController.sendHandlerMessage(1, this.mInputInterceptor);
            this.mInputInterceptor = null;
        }
        if (this.mDragInProgress) {
            if (WindowManagerDebugConfig.DEBUG_DRAG) {
                Slog.d("WindowManager", "broadcasting DRAG_ENDED");
            }
            Iterator<WindowState> it = this.mNotifiedWindows.iterator();
            while (it.hasNext()) {
                WindowState next = it.next();
                if (this.mDragResult || next.mSession.mPid != this.mPid) {
                    f = 0.0f;
                    f2 = 0.0f;
                    surfaceControl = null;
                } else {
                    float translateToWindowX = next.translateToWindowX(this.mCurrentX);
                    float translateToWindowY = next.translateToWindowY(this.mCurrentY);
                    if (relinquishDragSurfaceToDragSource()) {
                        f = translateToWindowX;
                        f2 = translateToWindowY;
                        surfaceControl = this.mSurfaceControl;
                    } else {
                        surfaceControl = null;
                        f = translateToWindowX;
                        f2 = translateToWindowY;
                    }
                }
                DragEvent obtain = DragEvent.obtain(4, f, f2, this.mThumbOffsetX, this.mThumbOffsetY, null, null, null, surfaceControl, null, this.mDragResult);
                try {
                    next.mClient.dispatchDragEvent(obtain);
                } catch (RemoteException unused) {
                    Slog.w("WindowManager", "Unable to drag-end window " + next);
                }
                if (WindowManagerService.MY_PID != next.mSession.mPid) {
                    obtain.recycle();
                }
            }
            this.mNotifiedWindows.clear();
            this.mDragInProgress = false;
        }
        if (isFromSource(8194)) {
            this.mService.restorePointerIconLocked(this.mDisplayContent, this.mCurrentX, this.mCurrentY);
            this.mTouchSource = 0;
        }
        SurfaceControl surfaceControl2 = this.mInputSurface;
        if (surfaceControl2 != null) {
            this.mTransaction.remove(surfaceControl2).apply();
            this.mInputSurface = null;
        }
        this.mDragDropController.mDragDropControllerExt.notifyDnDSplitScreenCloseIfNeed();
        if (this.mSurfaceControl != null) {
            if (!this.mRelinquishDragSurfaceToDropTarget && !relinquishDragSurfaceToDragSource()) {
                this.mTransaction.reparent(this.mSurfaceControl, null).apply();
            } else {
                this.mDragDropController.sendTimeoutMessage(3, this.mSurfaceControl, 5000L);
            }
            this.mSurfaceControl = null;
        }
        if (this.mAnimator != null && !this.mAnimationCompleted) {
            Slog.wtf("WindowManager", "Unexpectedly destroying mSurfaceControl while animation is running");
        }
        this.mFlags = 0;
        this.mLocalWin = null;
        this.mToken = null;
        this.mData = null;
        this.mThumbOffsetY = 0.0f;
        this.mThumbOffsetX = 0.0f;
        this.mNotifiedWindows = null;
        this.mDragDropController.onDragStateClosedLocked(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean reportDropWindowLock(IBinder iBinder, float f, float f2) {
        ClipData clipData;
        if (this.mAnimator != null || this.mIsClosing) {
            return false;
        }
        WindowState windowState = this.mService.mInputToWindowMap.get(iBinder);
        if (!isWindowNotified(windowState)) {
            this.mDragResult = false;
            endDragLocked();
            this.mDragDropController.mDragDropControllerExt.postCancelDragAndDrop();
            if (WindowManagerDebugConfig.DEBUG_DRAG) {
                Slog.d("WindowManager", "Drop outside a valid window " + windowState);
            }
            return false;
        }
        if (WindowManagerDebugConfig.DEBUG_DRAG) {
            Slog.d("WindowManager", "sending DROP to " + windowState);
        }
        int userId = UserHandle.getUserId(windowState.getOwningUid());
        int i = this.mFlags;
        DragAndDropPermissionsHandler dragAndDropPermissionsHandler = ((i & 256) == 0 || (i & 3) == 0 || this.mData == null) ? null : new DragAndDropPermissionsHandler(this.mService.mGlobalLock, this.mData, this.mUid, windowState.getOwningPackage(), this.mFlags & 195, this.mSourceUserId, userId);
        int i2 = this.mSourceUserId;
        if (i2 != userId && (clipData = this.mData) != null) {
            clipData.fixUris(i2);
        }
        IBinder asBinder = windowState.mClient.asBinder();
        this.mDragDropController.mDragDropControllerExt.adjustYForZoomWinIfNeed(windowState, f, f2);
        DragEvent obtainDragEvent = obtainDragEvent(3, f, f2, this.mData, targetInterceptsGlobalDrag(windowState), dragAndDropPermissionsHandler);
        this.mDragDropController.mDragDropControllerExt.notifyDnDSplitScreenDrop(f, f2);
        try {
            try {
                windowState.mClient.dispatchDragEvent(obtainDragEvent);
                this.mDragDropController.sendTimeoutMessage(0, asBinder, 5000L);
                if (WindowManagerService.MY_PID != windowState.mSession.mPid) {
                    obtainDragEvent.recycle();
                }
                this.mToken = asBinder;
                return true;
            } catch (RemoteException unused) {
                Slog.w("WindowManager", "can't send drop notification to win " + windowState);
                endDragLocked();
                if (WindowManagerService.MY_PID != windowState.mSession.mPid) {
                    obtainDragEvent.recycle();
                }
                return false;
            }
        } catch (Throwable th) {
            if (WindowManagerService.MY_PID != windowState.mSession.mPid) {
                obtainDragEvent.recycle();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class InputInterceptor {
        InputChannel mClientChannel;
        InputApplicationHandle mDragApplicationHandle = new InputApplicationHandle(new Binder(), "drag", InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS);
        InputWindowHandle mDragWindowHandle;
        DragInputEventReceiver mInputEventReceiver;

        InputInterceptor(Display display) {
            this.mClientChannel = DragState.this.mService.mInputManager.createInputChannel("drag");
            this.mInputEventReceiver = new DragInputEventReceiver(this.mClientChannel, DragState.this.mService.mH.getLooper(), DragState.this.mDragDropController);
            InputWindowHandle inputWindowHandle = new InputWindowHandle(this.mDragApplicationHandle, display.getDisplayId());
            this.mDragWindowHandle = inputWindowHandle;
            inputWindowHandle.name = "drag";
            inputWindowHandle.token = this.mClientChannel.getToken();
            InputWindowHandle inputWindowHandle2 = this.mDragWindowHandle;
            inputWindowHandle2.layoutParamsType = 2016;
            inputWindowHandle2.dispatchingTimeoutMillis = InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
            inputWindowHandle2.ownerPid = WindowManagerService.MY_PID;
            inputWindowHandle2.ownerUid = WindowManagerService.MY_UID;
            inputWindowHandle2.scaleFactor = 1.0f;
            inputWindowHandle2.inputConfig = 256;
            inputWindowHandle2.touchableRegion.setEmpty();
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ORIENTATION, -694710814, 0, (String) null, (Object[]) null);
            }
            DragState.this.mService.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.DragState$InputInterceptor$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DragState.InputInterceptor.lambda$new$0((DisplayContent) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$new$0(DisplayContent displayContent) {
            displayContent.getDisplayRotation().pause();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void tearDown() {
            DragState.this.mService.mInputManager.removeInputChannel(this.mClientChannel.getToken());
            this.mInputEventReceiver.dispose();
            this.mInputEventReceiver = null;
            this.mClientChannel.dispose();
            this.mClientChannel = null;
            this.mDragWindowHandle = null;
            this.mDragApplicationHandle = null;
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ORIENTATION, 269576220, 0, (String) null, (Object[]) null);
            }
            DragState.this.mService.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.DragState$InputInterceptor$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DragState.InputInterceptor.lambda$tearDown$1((DisplayContent) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$tearDown$1(DisplayContent displayContent) {
            displayContent.getDisplayRotation().resume();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputChannel getInputChannel() {
        InputInterceptor inputInterceptor = this.mInputInterceptor;
        if (inputInterceptor == null) {
            return null;
        }
        return inputInterceptor.mClientChannel;
    }

    InputWindowHandle getInputWindowHandle() {
        InputInterceptor inputInterceptor = this.mInputInterceptor;
        if (inputInterceptor == null) {
            return null;
        }
        return inputInterceptor.mDragWindowHandle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompletableFuture<Void> register(Display display) {
        display.getRealSize(this.mDisplaySize);
        if (WindowManagerDebugConfig.DEBUG_DRAG) {
            Slog.d("WindowManager", "registering drag input channel");
        }
        if (this.mInputInterceptor != null) {
            Slog.e("WindowManager", "Duplicate register of drag input channel");
            return CompletableFuture.completedFuture(null);
        }
        this.mInputInterceptor = new InputInterceptor(display);
        return showInputSurface();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void broadcastDragStartedLocked(final float f, final float f2) {
        this.mCurrentX = f;
        this.mOriginalX = f;
        this.mCurrentY = f2;
        this.mOriginalY = f2;
        ClipData clipData = this.mData;
        this.mDataDescription = clipData != null ? clipData.getDescription() : null;
        this.mNotifiedWindows.clear();
        this.mDragInProgress = true;
        this.mDragDropController.mDragDropControllerExt.notifyDnDSplitScreenStartIfNeed(this);
        this.mSourceUserId = UserHandle.getUserId(this.mUid);
        this.mCrossProfileCopyAllowed = true ^ ((UserManagerInternal) LocalServices.getService(UserManagerInternal.class)).getUserRestriction(this.mSourceUserId, "no_cross_profile_copy_paste");
        if (WindowManagerDebugConfig.DEBUG_DRAG) {
            Slog.d("WindowManager", "broadcasting DRAG_STARTED at (" + f + ", " + f2 + ")");
        }
        final boolean containsApplicationExtras = containsApplicationExtras(this.mDataDescription);
        this.mService.mRoot.forAllWindows(new Consumer() { // from class: com.android.server.wm.DragState$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DragState.this.lambda$broadcastDragStartedLocked$1(f, f2, containsApplicationExtras, (WindowState) obj);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: sendDragStartedLocked, reason: merged with bridge method [inline-methods] */
    public void lambda$broadcastDragStartedLocked$1(WindowState windowState, float f, float f2, boolean z) {
        boolean targetInterceptsGlobalDrag = targetInterceptsGlobalDrag(windowState);
        if (this.mDragInProgress && isValidDropTarget(windowState, z, targetInterceptsGlobalDrag)) {
            DragEvent obtainDragEvent = obtainDragEvent(1, windowState.translateToWindowX(f), windowState.translateToWindowY(f2), targetInterceptsGlobalDrag ? this.mData.copyForTransferWithActivityInfo() : null, targetInterceptsGlobalDrag, null);
            try {
                try {
                    windowState.mClient.dispatchDragEvent(obtainDragEvent);
                    this.mNotifiedWindows.add(windowState);
                    if (WindowManagerService.MY_PID == windowState.mSession.mPid) {
                        return;
                    }
                } catch (RemoteException unused) {
                    Slog.w("WindowManager", "Unable to drag-start window " + windowState);
                    if (WindowManagerService.MY_PID == windowState.mSession.mPid) {
                        return;
                    }
                }
                obtainDragEvent.recycle();
            } catch (Throwable th) {
                if (WindowManagerService.MY_PID != windowState.mSession.mPid) {
                    obtainDragEvent.recycle();
                }
                throw th;
            }
        }
    }

    private boolean containsApplicationExtras(ClipDescription clipDescription) {
        if (clipDescription == null) {
            return false;
        }
        return clipDescription.hasMimeType("application/vnd.android.activity") || clipDescription.hasMimeType("application/vnd.android.shortcut") || clipDescription.hasMimeType("application/vnd.android.task");
    }

    private boolean isValidDropTarget(WindowState windowState, boolean z, boolean z2) {
        if (windowState == null) {
            return false;
        }
        boolean z3 = this.mLocalWin == windowState.mClient.asBinder();
        if ((!z3 && !z2 && z) || !windowState.isPotentialDragTarget(z2)) {
            return false;
        }
        if (((this.mFlags & 256) == 0 || !targetWindowSupportsGlobalDrag(windowState)) && !z3) {
            return false;
        }
        return z2 || this.mCrossProfileCopyAllowed || this.mSourceUserId == UserHandle.getUserId(windowState.getOwningUid());
    }

    private boolean targetWindowSupportsGlobalDrag(WindowState windowState) {
        ActivityRecord activityRecord = windowState.mActivityRecord;
        return activityRecord == null || activityRecord.mTargetSdk >= 24;
    }

    public boolean targetInterceptsGlobalDrag(WindowState windowState) {
        return (windowState.mAttrs.privateFlags & ChargerErrorCode.ERR_FILE_FAILURE_ACCESS) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendDragStartedIfNeededLocked(WindowState windowState) {
        if (!this.mDragInProgress || isWindowNotified(windowState)) {
            return;
        }
        if (WindowManagerDebugConfig.DEBUG_DRAG) {
            Slog.d("WindowManager", "need to send DRAG_STARTED to new window " + windowState);
        }
        lambda$broadcastDragStartedLocked$1(windowState, this.mCurrentX, this.mCurrentY, containsApplicationExtras(this.mDataDescription));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isWindowNotified(WindowState windowState) {
        Iterator<WindowState> it = this.mNotifiedWindows.iterator();
        while (it.hasNext()) {
            if (it.next() == windowState) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endDragLocked() {
        if (this.mAnimator != null) {
            return;
        }
        ValueAnimator createCustormAnimatorIfNeed = this.mDragDropController.mDragDropControllerExt.createCustormAnimatorIfNeed(ChargerErrorCode.ERR_FILE_FAILURE_ACCESS, this);
        this.mAnimator = createCustormAnimatorIfNeed;
        if (createCustormAnimatorIfNeed != null) {
            return;
        }
        if (!this.mDragResult && !isAccessibilityDragDrop() && !relinquishDragSurfaceToDragSource()) {
            if ((this.mWindowFlag & 2048) != 0) {
                this.mAnimator = this.mDragDropController.mDragDropControllerExt.createReturnAnimationIfNeed(this);
                return;
            } else {
                this.mAnimator = createReturnAnimationLocked();
                return;
            }
        }
        closeLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelDragLocked(boolean z) {
        if (this.mAnimator != null) {
            return;
        }
        if (!this.mDragInProgress || z || isAccessibilityDragDrop()) {
            closeLocked();
            return;
        }
        ValueAnimator createCustormAnimatorIfNeed = this.mDragDropController.mDragDropControllerExt.createCustormAnimatorIfNeed(1073741824, this);
        this.mAnimator = createCustormAnimatorIfNeed;
        if (createCustormAnimatorIfNeed != null) {
            return;
        }
        this.mAnimator = createCancelAnimationLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateDragSurfaceLocked(boolean z, float f, float f2) {
        if (this.mAnimator != null) {
            return;
        }
        this.mCurrentX = f;
        this.mCurrentY = f2;
        if (z) {
            if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
                Slog.i("WindowManager", ">>> OPEN TRANSACTION notifyMoveLocked");
            }
            this.mTransaction.setPosition(this.mSurfaceControl, f - this.mThumbOffsetX, f2 - this.mThumbOffsetY).apply();
            this.mDragDropController.mDragDropControllerExt.notifyDnDSplitScreenLocation(f, f2);
            this.mDragDropController.mDragDropControllerExt.handleZoomDrag(f, f2);
            if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, 342460966, 20, (String) null, new Object[]{String.valueOf(this.mSurfaceControl), Long.valueOf((int) (f - this.mThumbOffsetX)), Long.valueOf((int) (f2 - this.mThumbOffsetY))});
            }
        }
    }

    boolean isInProgress() {
        return this.mDragInProgress;
    }

    private DragEvent obtainDragEvent(int i, float f, float f2, ClipData clipData, boolean z, IDragAndDropPermissions iDragAndDropPermissions) {
        return DragEvent.obtain(i, f, f2, this.mThumbOffsetX, this.mThumbOffsetY, null, this.mDataDescription, clipData, z ? this.mSurfaceControl : null, iDragAndDropPermissions, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ValueAnimator createReturnAnimationLocked() {
        float f = this.mCurrentX;
        float f2 = this.mThumbOffsetX;
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_X, f - f2, this.mOriginalX - f2);
        float f3 = this.mCurrentY;
        float f4 = this.mThumbOffsetY;
        PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_Y, f3 - f4, this.mOriginalY - f4);
        float f5 = this.mAnimatedScale;
        PropertyValuesHolder ofFloat3 = PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_SCALE, f5, f5);
        float f6 = this.mOriginalAlpha;
        final ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(ofFloat, ofFloat2, ofFloat3, PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_ALPHA, f6, f6 / 2.0f));
        float f7 = this.mOriginalX - this.mCurrentX;
        float f8 = this.mOriginalY - this.mCurrentY;
        double sqrt = Math.sqrt((f7 * f7) + (f8 * f8));
        Point point = this.mDisplaySize;
        int i = point.x;
        int i2 = point.y;
        long sqrt2 = ((long) ((sqrt / Math.sqrt((i * i) + (i2 * i2))) * 180.0d)) + MIN_ANIMATION_DURATION_MS;
        AnimationListener animationListener = new AnimationListener();
        ofPropertyValuesHolder.setDuration(sqrt2);
        ofPropertyValuesHolder.setInterpolator(this.mCubicEaseOutInterpolator);
        ofPropertyValuesHolder.addListener(animationListener);
        ofPropertyValuesHolder.addUpdateListener(animationListener);
        this.mService.mAnimationHandler.post(new Runnable() { // from class: com.android.server.wm.DragState$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ofPropertyValuesHolder.start();
            }
        });
        return ofPropertyValuesHolder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ValueAnimator createCancelAnimationLocked() {
        float f = this.mCurrentX;
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_X, f - this.mThumbOffsetX, f);
        float f2 = this.mCurrentY;
        final ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(ofFloat, PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_Y, f2 - this.mThumbOffsetY, f2), PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_SCALE, this.mAnimatedScale, 0.0f), PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_ALPHA, this.mOriginalAlpha, 0.0f));
        AnimationListener animationListener = new AnimationListener();
        ofPropertyValuesHolder.setDuration(MIN_ANIMATION_DURATION_MS);
        ofPropertyValuesHolder.setInterpolator(this.mCubicEaseOutInterpolator);
        ofPropertyValuesHolder.addListener(animationListener);
        ofPropertyValuesHolder.addUpdateListener(animationListener);
        this.mService.mAnimationHandler.post(new Runnable() { // from class: com.android.server.wm.DragState$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ofPropertyValuesHolder.start();
            }
        });
        return ofPropertyValuesHolder;
    }

    private boolean isFromSource(int i) {
        return (this.mTouchSource & i) == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overridePointerIconLocked(int i) {
        this.mTouchSource = i;
        if (isFromSource(8194)) {
            InputManagerGlobal.getInstance().setPointerIconType(1021);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class AnimationListener implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        private AnimationListener() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SurfaceControl.Transaction transaction = DragState.this.mService.mTransactionFactory.get();
            try {
                SurfaceControl surfaceControl = DragState.this.mSurfaceControl;
                if (surfaceControl != null && surfaceControl.isValid()) {
                    transaction.setPosition(DragState.this.mSurfaceControl, ((Float) valueAnimator.getAnimatedValue(DragState.ANIMATED_PROPERTY_X)).floatValue(), ((Float) valueAnimator.getAnimatedValue(DragState.ANIMATED_PROPERTY_Y)).floatValue());
                    transaction.setAlpha(DragState.this.mSurfaceControl, ((Float) valueAnimator.getAnimatedValue(DragState.ANIMATED_PROPERTY_ALPHA)).floatValue());
                    transaction.setMatrix(DragState.this.mSurfaceControl, ((Float) valueAnimator.getAnimatedValue(DragState.ANIMATED_PROPERTY_SCALE)).floatValue(), 0.0f, 0.0f, ((Float) valueAnimator.getAnimatedValue(DragState.ANIMATED_PROPERTY_SCALE)).floatValue());
                    transaction.apply();
                }
                if (transaction != null) {
                    transaction.close();
                }
            } catch (Throwable th) {
                if (transaction != null) {
                    try {
                        transaction.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            DragState.this.mAnimationCompleted = true;
            DragState.this.mDragDropController.sendHandlerMessage(2, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAccessibilityDragDrop() {
        return (this.mFlags & 1024) != 0;
    }

    private boolean relinquishDragSurfaceToDragSource() {
        return (this.mFlags & 2048) != 0;
    }

    public IDragStateWrapper getWrapper() {
        return this.mDragStateWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class DragStateWrapper implements IDragStateWrapper {
        private DragStateWrapper() {
        }

        @Override // com.android.server.wm.IDragStateWrapper
        public ValueAnimator createCancelAnimationLocked() {
            return DragState.this.createCancelAnimationLocked();
        }

        @Override // com.android.server.wm.IDragStateWrapper
        public ValueAnimator createReturnAnimationLocked() {
            return DragState.this.createReturnAnimationLocked();
        }
    }
}
