package com.android.server.wm;

import android.R;
import android.app.AppOpsManager;
import android.app.admin.DevicePolicyCache;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.GraphicsProtos;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Binder;
import android.os.Debug;
import android.os.IBinder;
import android.os.InputConstants;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.WorkSource;
import android.provider.Settings;
import android.util.ArraySet;
import android.util.MergedConfiguration;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.view.DisplayInfo;
import android.view.IWindow;
import android.view.IWindowFocusObserver;
import android.view.IWindowId;
import android.view.InputChannel;
import android.view.InputWindowHandle;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewRootImpl;
import android.view.WindowInfo;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.inputmethod.ImeTracker;
import android.window.ClientWindowFrames;
import android.window.OnBackInvokedCallbackInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.KeyInterceptionInfo;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.ToBooleanFunction;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.wm.BLASTSyncEngine;
import com.android.server.wm.DisplayPolicy;
import com.android.server.wm.LocalAnimationAdapter;
import com.android.server.wm.RefreshRatePolicy;
import dalvik.annotation.optimization.NeverCompile;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowState extends WindowContainer<WindowState> implements WindowManagerPolicy.WindowState, InputTarget {
    static final int BLAST_TIMEOUT_DURATION = 5000;
    static final int EXCLUSION_LEFT = 0;
    static final int EXCLUSION_RIGHT = 1;
    static final int EXIT_ANIMATING_TYPES = 25;
    static final int LEGACY_POLICY_VISIBILITY = 1;
    static final int MINIMUM_VISIBLE_HEIGHT_IN_DP = 32;
    static final int MINIMUM_VISIBLE_WIDTH_IN_DP = 48;
    private static final int POLICY_VISIBILITY_ALL = 3;
    static final int RESIZE_HANDLE_WIDTH_IN_DP = 30;
    static final String TAG = "WindowManager";
    private static final int VISIBLE_FOR_USER = 2;
    final InsetsState mAboveInsetsState;
    ActivityRecord mActivityRecord;
    boolean mAnimatingExit;
    boolean mAppFreezing;
    final int mAppOp;
    private boolean mAppOpVisibility;
    final WindowManager.LayoutParams mAttrs;
    final int mBaseLayer;
    final IWindow mClient;
    private final ClientWindowFrames mClientWindowFrames;
    float mCompatScale;
    final Context mContext;
    final DeathRecipient mDeathRecipient;
    boolean mDestroying;
    int mDisableFlags;
    private boolean mDragResizing;
    private boolean mDragResizingChangeReported;
    private final List<DrawHandler> mDrawHandlers;
    private PowerManager.WakeLock mDrawLock;
    private boolean mDrawnStateEvaluated;
    private final List<Rect> mExclusionRects;
    private RemoteCallbackList<IWindowFocusObserver> mFocusCallbacks;
    private boolean mForceHideNonSystemOverlayWindow;
    final boolean mForceSeamlesslyRotate;
    int mFrameRateSelectionPriority;
    RefreshRatePolicy.FrameRateVote mFrameRateVote;
    private InsetsState mFrozenInsetsState;
    final Rect mGivenContentInsets;
    boolean mGivenInsetsPending;
    final Region mGivenTouchableRegion;
    final Rect mGivenVisibleInsets;
    float mGlobalScale;
    float mHScale;
    boolean mHasSurface;
    boolean mHaveFrame;
    boolean mHidden;
    private boolean mHiddenWhileSuspended;
    boolean mInRelayout;
    InputChannel mInputChannel;
    IBinder mInputChannelToken;
    final InputWindowHandleWrapper mInputWindowHandle;
    float mInvGlobalScale;
    private boolean mIsChildWindow;
    private boolean mIsDimming;
    private final boolean mIsFloatingLayer;
    final boolean mIsImWindow;
    final boolean mIsWallpaper;
    private final List<Rect> mKeepClearAreas;
    private KeyInterceptionInfo mKeyInterceptionInfo;
    private boolean mLastConfigReportedToClient;
    private final long[] mLastExclusionLogUptimeMillis;
    int mLastFreezeDuration;
    private final int[] mLastGrantedExclusionHeight;
    float mLastHScale;
    private final MergedConfiguration mLastReportedConfiguration;
    private final int[] mLastRequestedExclusionHeight;
    private int mLastRequestedHeight;
    private int mLastRequestedWidth;
    private boolean mLastShownChangedReported;
    final Rect mLastSurfaceInsets;
    private CharSequence mLastTitle;
    float mLastVScale;
    int mLastVisibleLayoutRotation;
    int mLayer;
    final boolean mLayoutAttached;
    boolean mLayoutNeeded;
    int mLayoutSeq;
    boolean mLegacyPolicyVisibilityAfterAnim;
    SparseArray<InsetsSource> mMergedLocalInsetsSources;
    private boolean mMovedByResize;
    boolean mObscured;
    private OnBackInvokedCallbackInfo mOnBackInvokedCallbackInfo;
    private long mOrientationChangeRedrawRequestTime;
    private boolean mOrientationChangeTimedOut;
    private boolean mOrientationChanging;
    final float mOverrideScale;
    final boolean mOwnerCanAddInternalSystemWindow;
    final int mOwnerUid;
    SeamlessRotator mPendingSeamlessRotate;
    boolean mPermanentlyHidden;
    final WindowManagerPolicy mPolicy;
    public int mPolicyVisibility;
    private PowerManagerWrapper mPowerManagerWrapper;
    int mPrepareSyncSeqId;
    private boolean mRedrawForSyncReported;
    boolean mRelayoutCalled;
    int mRelayoutSeq;
    boolean mRemoveOnExit;
    boolean mRemoved;
    int mRequestedHeight;
    private int mRequestedVisibleTypes;
    int mRequestedWidth;
    boolean mResizedWhileGone;
    private final Consumer<SurfaceControl.Transaction> mSeamlessRotationFinishedConsumer;
    boolean mSeamlesslyRotated;
    final Session mSession;
    private final Consumer<SurfaceControl.Transaction> mSetSurfacePositionConsumer;
    boolean mShouldScaleWallpaper;
    final int mShowUserId;
    StartingData mStartingData;
    private String mStringNameCache;
    final int mSubLayer;
    boolean mSurfacePlacementNeeded;
    private final Point mSurfacePosition;
    private int mSurfaceTranslationY;
    int mSyncSeqId;
    private final Region mTapExcludeRegion;
    private final Configuration mTempConfiguration;
    final Matrix mTmpMatrix;
    final float[] mTmpMatrixArray;
    private final Point mTmpPoint;
    private final Rect mTmpRect;
    private final Region mTmpRegion;
    private final SurfaceControl.Transaction mTmpTransaction;
    WindowToken mToken;
    int mTouchableInsets;
    private final List<Rect> mUnrestrictedKeepClearAreas;
    float mVScale;
    int mViewVisibility;
    int mWallpaperDisplayOffsetX;
    int mWallpaperDisplayOffsetY;
    float mWallpaperScale;
    float mWallpaperX;
    float mWallpaperXStep;
    float mWallpaperY;
    float mWallpaperYStep;
    float mWallpaperZoomOut;
    private boolean mWasExiting;
    final WindowStateAnimator mWinAnimator;
    private final WindowFrames mWindowFrames;
    final WindowId mWindowId;
    boolean mWindowRemovalAllowed;
    private IWindowStateExt mWindowStateExt;
    private WindowStateWrapper mWindowStateWrapper;
    IWindowManagerServiceExt mWmsExt;
    private final WindowProcessController mWpcForDisplayAreaConfigChanges;
    int mXOffset;
    int mYOffset;
    static boolean DEBUG_PANIC = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final StringBuilder sTmpSB = new StringBuilder();
    private static final Comparator<WindowState> sWindowSubLayerComparator = new Comparator<WindowState>() { // from class: com.android.server.wm.WindowState.1
        @Override // java.util.Comparator
        public int compare(WindowState windowState, WindowState windowState2) {
            int i = windowState.mSubLayer;
            int i2 = windowState2.mSubLayer;
            if (i >= i2) {
                return (i != i2 || i2 >= 0) ? 1 : -1;
            }
            return -1;
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface PowerManagerWrapper {
        boolean isInteractive();

        void wakeUp(long j, int i, String str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public WindowState asWindowState() {
        return this;
    }

    @Override // com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268040L;
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public WindowState getWindow() {
        return this;
    }

    @Override // com.android.server.wm.InputTarget
    public WindowState getWindowState() {
        return this;
    }

    @Override // com.android.server.wm.InputTarget
    public void handleTapOutsideFocusOutsideSelf() {
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ void commitPendingTransaction() {
        super.commitPendingTransaction();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ int compareTo(WindowContainer windowContainer) {
        return super.compareTo(windowContainer);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl getAnimationLeash() {
        return super.getAnimationLeash();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public /* bridge */ /* synthetic */ SurfaceControl getFreezeSnapshotTarget() {
        return super.getFreezeSnapshotTarget();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ SparseArray getInsetsSourceProviders() {
        return super.getInsetsSourceProviders();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl getParentSurfaceControl() {
        return super.getParentSurfaceControl();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl.Transaction getPendingTransaction() {
        return super.getPendingTransaction();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl getSurfaceControl() {
        return super.getSurfaceControl();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ int getSurfaceHeight() {
        return super.getSurfaceHeight();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ int getSurfaceWidth() {
        return super.getSurfaceWidth();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl.Transaction getSyncTransaction() {
        return super.getSyncTransaction();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ IWindowContainerWrapper getWCWrapper() {
        return super.getWCWrapper();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ boolean hasInsetsSourceProvider() {
        return super.hasInsetsSourceProvider();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ SurfaceControl.Builder makeAnimationLeash() {
        return super.makeAnimationLeash();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ void onAnimationLeashLost(SurfaceControl.Transaction transaction) {
        super.onAnimationLeashLost(transaction);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public /* bridge */ /* synthetic */ void onRequestedOverrideConfigurationChanged(Configuration configuration) {
        super.onRequestedOverrideConfigurationChanged(configuration);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public /* bridge */ /* synthetic */ void onUnfrozen() {
        super.onUnfrozen();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class DrawHandler {
        Consumer<SurfaceControl.Transaction> mConsumer;
        int mSeqId;

        DrawHandler(int i, Consumer<SurfaceControl.Transaction> consumer) {
            this.mSeqId = i;
            this.mConsumer = consumer;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(SurfaceControl.Transaction transaction) {
        finishSeamlessRotation(transaction);
        updateSurfacePosition(transaction);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(SurfaceControl.Transaction transaction) {
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl == null || !surfaceControl.isValid() || this.mSurfaceAnimator.hasLeash()) {
            return;
        }
        SurfaceControl surfaceControl2 = this.mSurfaceControl;
        Point point = this.mSurfacePosition;
        transaction.setPosition(surfaceControl2, point.x, point.y);
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public boolean isRequestedVisible(int i) {
        return (this.mRequestedVisibleTypes & i) != 0;
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public int getRequestedVisibleTypes() {
        return this.mRequestedVisibleTypes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRequestedVisibleTypes(int i) {
        if (this.mRequestedVisibleTypes != i) {
            this.mRequestedVisibleTypes = i;
        }
    }

    @VisibleForTesting
    void setRequestedVisibleTypes(int i, int i2) {
        setRequestedVisibleTypes((i & i2) | (this.mRequestedVisibleTypes & (~i2)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void freezeInsetsState() {
        if (this.mFrozenInsetsState == null) {
            this.mFrozenInsetsState = new InsetsState(getInsetsState(), true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearFrozenInsetsState() {
        this.mFrozenInsetsState = null;
    }

    InsetsState getFrozenInsetsState() {
        return this.mFrozenInsetsState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isReadyToDispatchInsetsState() {
        return (shouldCheckTokenVisibleRequested() ? isVisibleRequested() : isVisible()) && this.mFrozenInsetsState == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void seamlesslyRotateIfAllowed(SurfaceControl.Transaction transaction, int i, int i2, boolean z) {
        if (isVisibleNow()) {
            if ((!this.mIsWallpaper || this.mWindowStateExt.wallpaperSeamlesslyRotate(this)) && !this.mToken.hasFixedRotationTransform()) {
                Task task = getTask();
                if (task == null || !task.inPinnedWindowingMode()) {
                    SeamlessRotator seamlessRotator = this.mPendingSeamlessRotate;
                    if (seamlessRotator != null) {
                        i = seamlessRotator.getOldRotation();
                    }
                    InsetsSourceProvider insetsSourceProvider = this.mControllableInsetProvider;
                    if (insetsSourceProvider == null || insetsSourceProvider.getSource().getType() != WindowInsets.Type.ime()) {
                        if ((this.mForceSeamlesslyRotate || z) && !this.mWindowStateExt.blockSeamlesslyRotateForFingerPrintWindow(this)) {
                            InsetsSourceProvider insetsSourceProvider2 = this.mControllableInsetProvider;
                            if (insetsSourceProvider2 != null) {
                                insetsSourceProvider2.startSeamlessRotation();
                            }
                            this.mPendingSeamlessRotate = new SeamlessRotator(i, i2, getDisplayInfo(), false);
                            Point point = this.mLastSurfacePosition;
                            Point point2 = this.mSurfacePosition;
                            point.set(point2.x, point2.y);
                            this.mPendingSeamlessRotate.unrotate(transaction, this);
                            getDisplayContent().getDisplayRotation().markForSeamlessRotation(this, true);
                            applyWithNextDraw(this.mSeamlessRotationFinishedConsumer);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelSeamlessRotation() {
        finishSeamlessRotation(getPendingTransaction());
    }

    void finishSeamlessRotation(SurfaceControl.Transaction transaction) {
        SeamlessRotator seamlessRotator = this.mPendingSeamlessRotate;
        if (seamlessRotator == null) {
            return;
        }
        seamlessRotator.finish(transaction, this);
        this.mPendingSeamlessRotate = null;
        getDisplayContent().getDisplayRotation().markForSeamlessRotation(this, false);
        InsetsSourceProvider insetsSourceProvider = this.mControllableInsetProvider;
        if (insetsSourceProvider != null) {
            insetsSourceProvider.finishSeamlessRotation();
        }
        if (this.mLastHScale == 1.0f && this.mLastVScale == 1.0f) {
            return;
        }
        this.mLastHScale = 1.0f;
        this.mLastVScale = 1.0f;
        Slog.d(TAG, "finishSeamlessRotation force update mLastHScale this=" + this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Rect> getSystemGestureExclusion() {
        return this.mExclusionRects;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setSystemGestureExclusion(List<Rect> list) {
        if (this.mExclusionRects.equals(list)) {
            return false;
        }
        this.mExclusionRects.clear();
        this.mExclusionRects.addAll(list);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isImplicitlyExcludingAllSystemGestures() {
        ActivityRecord activityRecord;
        return (this.mAttrs.insetsFlags.behavior == 2 && !isRequestedVisible(WindowInsets.Type.navigationBars())) && this.mWmService.mConstants.mSystemGestureExcludedByPreQStickyImmersive && (activityRecord = this.mActivityRecord) != null && activityRecord.mTargetSdk < 29;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastExclusionHeights(int i, int i2, int i3) {
        if ((this.mLastGrantedExclusionHeight[i] == i3 && this.mLastRequestedExclusionHeight[i] == i2) ? false : true) {
            if (this.mLastShownChangedReported) {
                logExclusionRestrictions(i);
            }
            this.mLastGrantedExclusionHeight[i] = i3;
            this.mLastRequestedExclusionHeight[i] = i2;
        }
    }

    void getKeepClearAreas(Collection<Rect> collection, Collection<Rect> collection2) {
        getKeepClearAreas(collection, collection2, new Matrix(), new float[9]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getKeepClearAreas(Collection<Rect> collection, Collection<Rect> collection2, Matrix matrix, float[] fArr) {
        collection.addAll(getRectsInScreenSpace(this.mKeepClearAreas, matrix, fArr));
        collection2.addAll(getRectsInScreenSpace(this.mUnrestrictedKeepClearAreas, matrix, fArr));
    }

    List<Rect> getRectsInScreenSpace(List<Rect> list, Matrix matrix, float[] fArr) {
        getTransformationMatrix(fArr, matrix);
        ArrayList arrayList = new ArrayList();
        RectF rectF = new RectF();
        Iterator<Rect> it = list.iterator();
        while (it.hasNext()) {
            rectF.set(it.next());
            matrix.mapRect(rectF);
            Rect rect = new Rect();
            rectF.roundOut(rect);
            arrayList.add(rect);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setKeepClearAreas(List<Rect> list, List<Rect> list2) {
        boolean z = !this.mKeepClearAreas.equals(list);
        boolean z2 = !this.mUnrestrictedKeepClearAreas.equals(list2);
        if (!z && !z2) {
            return false;
        }
        if (z) {
            this.mKeepClearAreas.clear();
            this.mKeepClearAreas.addAll(list);
        }
        if (z2) {
            this.mUnrestrictedKeepClearAreas.clear();
            this.mUnrestrictedKeepClearAreas.addAll(list2);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnBackInvokedCallbackInfo(OnBackInvokedCallbackInfo onBackInvokedCallbackInfo) {
        if (ProtoLogCache.WM_DEBUG_BACK_PREVIEW_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, -228813488, 0, "%s: Setting back callback %s", new Object[]{String.valueOf(this), String.valueOf(onBackInvokedCallbackInfo)});
        }
        this.mOnBackInvokedCallbackInfo = onBackInvokedCallbackInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OnBackInvokedCallbackInfo getOnBackInvokedCallbackInfo() {
        return this.mOnBackInvokedCallbackInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState(final WindowManagerService windowManagerService, Session session, IWindow iWindow, WindowToken windowToken, WindowState windowState, int i, WindowManager.LayoutParams layoutParams, int i2, int i3, int i4, boolean z) {
        this(windowManagerService, session, iWindow, windowToken, windowState, i, layoutParams, i2, i3, i4, z, new PowerManagerWrapper() { // from class: com.android.server.wm.WindowState.2
            @Override // com.android.server.wm.WindowState.PowerManagerWrapper
            public void wakeUp(long j, int i5, String str) {
                WindowManagerService.this.mPowerManager.wakeUp(j, i5, str);
            }

            @Override // com.android.server.wm.WindowState.PowerManagerWrapper
            public boolean isInteractive() {
                return WindowManagerService.this.mPowerManager.isInteractive();
            }
        });
    }

    WindowState(WindowManagerService windowManagerService, Session session, IWindow iWindow, WindowToken windowToken, WindowState windowState, int i, WindowManager.LayoutParams layoutParams, int i2, int i3, int i4, boolean z, PowerManagerWrapper powerManagerWrapper) {
        super(windowManagerService);
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        this.mAttrs = layoutParams2;
        this.mPolicyVisibility = 3;
        boolean z2 = true;
        this.mLegacyPolicyVisibilityAfterAnim = true;
        this.mAppOpVisibility = true;
        this.mHidden = true;
        this.mDragResizingChangeReported = true;
        this.mRedrawForSyncReported = true;
        this.mSyncSeqId = 0;
        this.mPrepareSyncSeqId = 0;
        this.mRelayoutSeq = -1;
        this.mLayoutSeq = -1;
        this.mLastReportedConfiguration = new MergedConfiguration();
        this.mTempConfiguration = new Configuration();
        this.mGivenContentInsets = new Rect();
        this.mGivenVisibleInsets = new Rect();
        this.mGivenTouchableRegion = new Region();
        this.mTouchableInsets = 0;
        this.mGlobalScale = 1.0f;
        this.mInvGlobalScale = 1.0f;
        this.mCompatScale = 1.0f;
        this.mHScale = 1.0f;
        this.mVScale = 1.0f;
        this.mLastHScale = 1.0f;
        this.mLastVScale = 1.0f;
        this.mXOffset = 0;
        this.mYOffset = 0;
        this.mWallpaperScale = 1.0f;
        this.mTmpMatrix = new Matrix();
        this.mTmpMatrixArray = new float[9];
        this.mWindowFrames = new WindowFrames();
        this.mClientWindowFrames = new ClientWindowFrames();
        this.mExclusionRects = new ArrayList();
        this.mKeepClearAreas = new ArrayList();
        this.mUnrestrictedKeepClearAreas = new ArrayList();
        this.mLastRequestedExclusionHeight = new int[]{0, 0};
        this.mLastGrantedExclusionHeight = new int[]{0, 0};
        this.mLastExclusionLogUptimeMillis = new long[]{0, 0};
        this.mWallpaperX = -1.0f;
        this.mWallpaperY = -1.0f;
        this.mWallpaperZoomOut = -1.0f;
        this.mWallpaperXStep = -1.0f;
        this.mWallpaperYStep = -1.0f;
        this.mWallpaperDisplayOffsetX = ChargerErrorCode.ERR_FILE_FAILURE_ACCESS;
        this.mWallpaperDisplayOffsetY = ChargerErrorCode.ERR_FILE_FAILURE_ACCESS;
        this.mLastVisibleLayoutRotation = -1;
        this.mHasSurface = false;
        this.mTmpRect = new Rect();
        this.mTmpPoint = new Point();
        this.mTmpRegion = new Region();
        this.mResizedWhileGone = false;
        this.mSeamlesslyRotated = false;
        this.mAboveInsetsState = new InsetsState();
        this.mMergedLocalInsetsSources = null;
        Rect rect = new Rect();
        this.mLastSurfaceInsets = rect;
        this.mSurfacePosition = new Point();
        this.mTapExcludeRegion = new Region();
        this.mIsDimming = false;
        this.mRequestedVisibleTypes = WindowInsets.Type.defaultVisible();
        this.mFrameRateSelectionPriority = -1;
        this.mFrameRateVote = new RefreshRatePolicy.FrameRateVote();
        this.mDrawHandlers = new ArrayList();
        this.mSeamlessRotationFinishedConsumer = new Consumer() { // from class: com.android.server.wm.WindowState$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WindowState.this.lambda$new$0((SurfaceControl.Transaction) obj);
            }
        };
        this.mSetSurfacePositionConsumer = new Consumer() { // from class: com.android.server.wm.WindowState$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WindowState.this.lambda$new$1((SurfaceControl.Transaction) obj);
            }
        };
        this.mWmsExt = (IWindowManagerServiceExt) ExtLoader.type(IWindowManagerServiceExt.class).create();
        this.mWindowStateWrapper = new WindowStateWrapper();
        this.mWindowStateExt = (IWindowStateExt) ExtLoader.type(IWindowStateExt.class).base(this).create();
        this.mTmpTransaction = windowManagerService.mTransactionFactory.get();
        this.mSession = session;
        this.mClient = iWindow;
        this.mAppOp = i;
        this.mToken = windowToken;
        this.mActivityRecord = windowToken.asActivityRecord();
        this.mOwnerUid = i3;
        this.mShowUserId = i4;
        this.mOwnerCanAddInternalSystemWindow = z;
        this.mWindowId = new WindowId();
        layoutParams2.copyFrom(layoutParams);
        rect.set(layoutParams2.surfaceInsets);
        this.mViewVisibility = i2;
        WindowManagerService windowManagerService2 = this.mWmService;
        WindowManagerPolicy windowManagerPolicy = windowManagerService2.mPolicy;
        this.mPolicy = windowManagerPolicy;
        this.mContext = windowManagerService2.mContext;
        DeathRecipient deathRecipient = new DeathRecipient();
        this.mPowerManagerWrapper = powerManagerWrapper;
        if (!ActivityTaskManagerService.LTW_DISABLE) {
            this.mForceSeamlesslyRotate = windowToken.mRoundedCornerOverlay || layoutParams2.type == 2099;
        } else {
            this.mForceSeamlesslyRotate = windowToken.mRoundedCornerOverlay;
        }
        ActivityRecord activityRecord = this.mActivityRecord;
        InputWindowHandleWrapper inputWindowHandleWrapper = new InputWindowHandleWrapper(new InputWindowHandle(activityRecord != null ? activityRecord.getInputApplicationHandle(false) : null, getDisplayId()));
        this.mInputWindowHandle = inputWindowHandleWrapper;
        inputWindowHandleWrapper.setFocusable(false);
        inputWindowHandleWrapper.setOwnerPid(session.mPid);
        inputWindowHandleWrapper.setOwnerUid(session.mUid);
        inputWindowHandleWrapper.setName(getName());
        inputWindowHandleWrapper.setPackageName(layoutParams2.packageName);
        inputWindowHandleWrapper.setLayoutParamsType(layoutParams2.type);
        inputWindowHandleWrapper.setTrustedOverlay(shouldWindowHandleBeTrusted(session));
        if (WindowManagerDebugConfig.DEBUG) {
            Slog.v(TAG, "Window " + this + " client=" + iWindow.asBinder() + " token=" + windowToken + " (" + layoutParams2.token + ") params=" + layoutParams);
        }
        try {
            iWindow.asBinder().linkToDeath(deathRecipient, 0);
            this.mDeathRecipient = deathRecipient;
            int i5 = layoutParams2.type;
            if (i5 >= 1000 && i5 <= 1999) {
                this.mBaseLayer = (windowManagerPolicy.getWindowLayerLw(windowState) * 10000) + 1000;
                this.mSubLayer = windowManagerPolicy.getSubWindowLayerFromTypeLw(layoutParams.type);
                this.mIsChildWindow = true;
                this.mLayoutAttached = layoutParams2.type != 1003;
                int i6 = windowState.mAttrs.type;
                this.mIsImWindow = i6 == 2011 || i6 == 2012;
                this.mIsWallpaper = i6 == 2013;
            } else {
                this.mBaseLayer = (windowManagerPolicy.getWindowLayerLw(this) * 10000) + 1000;
                this.mSubLayer = 0;
                this.mIsChildWindow = false;
                this.mLayoutAttached = false;
                int i7 = layoutParams2.type;
                this.mIsImWindow = i7 == 2011 || i7 == 2012;
                this.mIsWallpaper = i7 == 2013;
            }
            if (!this.mIsImWindow && !this.mIsWallpaper) {
                z2 = false;
            }
            this.mIsFloatingLayer = z2;
            ActivityRecord activityRecord2 = this.mActivityRecord;
            if (activityRecord2 != null && activityRecord2.mShowForAllUsers) {
                layoutParams2.flags |= 524288;
            }
            WindowStateAnimator windowStateAnimator = new WindowStateAnimator(this);
            this.mWinAnimator = windowStateAnimator;
            windowStateAnimator.mAlpha = layoutParams.alpha;
            this.mRequestedWidth = -1;
            this.mRequestedHeight = -1;
            this.mLastRequestedWidth = -1;
            this.mLastRequestedHeight = -1;
            this.mLayer = 0;
            this.mOverrideScale = this.mWmService.mAtmService.mCompatModePackages.getCompatScale(layoutParams2.packageName, session.mUid);
            updateGlobalScale();
            this.mWindowStateExt.onWindowStateCreated(this);
            if (this.mIsChildWindow) {
                if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -916108501, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(windowState)});
                }
                windowState.addChild(this, sWindowSubLayerComparator);
            }
            int i8 = session.mPid;
            this.mWpcForDisplayAreaConfigChanges = (i8 == WindowManagerService.MY_PID || i8 < 0) ? null : windowManagerService.mAtmService.getProcessController(i8, session.mUid);
        } catch (RemoteException unused) {
            this.mDeathRecipient = null;
            this.mIsChildWindow = false;
            this.mLayoutAttached = false;
            this.mIsImWindow = false;
            this.mIsWallpaper = false;
            this.mIsFloatingLayer = false;
            this.mBaseLayer = 0;
            this.mSubLayer = 0;
            this.mWinAnimator = null;
            this.mWpcForDisplayAreaConfigChanges = null;
            this.mOverrideScale = 1.0f;
        }
    }

    boolean shouldWindowHandleBeTrusted(Session session) {
        if (!InputMonitor.isTrustedOverlay(this.mAttrs.type)) {
            WindowManager.LayoutParams layoutParams = this.mAttrs;
            if (((layoutParams.privateFlags & 536870912) == 0 || !session.mCanAddInternalSystemWindow) && !this.mWindowStateExt.isOplusTrustedWindow(layoutParams) && ((this.mAttrs.privateFlags & 8) == 0 || !session.mCanCreateSystemApplicationOverlay)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getTouchOcclusionMode() {
        return (WindowManager.LayoutParams.isSystemAlertWindowType(this.mAttrs.type) || isAnimating(3, -1) || inTransition()) ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void attach() {
        this.mWindowStateExt.attach(this);
        if (WindowManagerDebugConfig.DEBUG) {
            Slog.v(TAG, "Attaching " + this + " token=" + this.mToken);
        }
        this.mSession.windowAddedLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateGlobalScale() {
        if (hasCompatScale()) {
            float compatScale = (this.mOverrideScale == 1.0f || this.mToken.hasSizeCompatBounds()) ? this.mToken.getCompatScale() : 1.0f;
            this.mCompatScale = compatScale;
            float f = compatScale * this.mOverrideScale;
            this.mGlobalScale = f;
            WindowToken windowToken = this.mToken;
            if (windowToken instanceof ActivityRecord) {
                this.mGlobalScale = f * ((ActivityRecord) windowToken).getWrapper().getExtImpl().getCompatScaleInOplusCompatMode();
            }
            this.mInvGlobalScale = 1.0f / this.mGlobalScale;
            return;
        }
        this.mCompatScale = 1.0f;
        this.mInvGlobalScale = 1.0f;
        this.mGlobalScale = 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getCompatScaleForClient() {
        if (this.mToken.hasSizeCompatBounds()) {
            return 1.0f;
        }
        return this.mCompatScale;
    }

    boolean hasCompatScale() {
        WindowManager.LayoutParams layoutParams = this.mAttrs;
        if ((layoutParams.privateFlags & 128) != 0) {
            return true;
        }
        if (layoutParams.type == 3) {
            return false;
        }
        WindowToken windowToken = this.mToken;
        if (windowToken != null && (windowToken instanceof ActivityRecord) && ((ActivityRecord) windowToken).getWrapper().getExtImpl().getCompatScaleInOplusCompatMode() != 1.0f) {
            return true;
        }
        ActivityRecord activityRecord = this.mActivityRecord;
        return (activityRecord != null && activityRecord.hasSizeCompatBounds()) || this.mOverrideScale != 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getDrawnStateEvaluated() {
        return this.mDrawnStateEvaluated;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDrawnStateEvaluated(boolean z) {
        this.mDrawnStateEvaluated = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onParentChanged(ConfigurationContainer configurationContainer, ConfigurationContainer configurationContainer2) {
        super.onParentChanged(configurationContainer, configurationContainer2);
        setDrawnStateEvaluated(false);
        getDisplayContent().reapplyMagnificationSpec();
        this.mWindowStateExt.createCompactDimmer(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getOwningUid() {
        return this.mOwnerUid;
    }

    public String getOwningPackage() {
        return this.mAttrs.packageName;
    }

    public boolean canAddInternalSystemWindow() {
        return this.mOwnerCanAddInternalSystemWindow;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean skipLayout() {
        ActivityRecord activityRecord = this.mActivityRecord;
        return activityRecord != null && activityRecord.mWaitForEnteringPinnedMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFrames(ClientWindowFrames clientWindowFrames, int i, int i2) {
        int i3;
        int i4;
        WindowFrames windowFrames = this.mWindowFrames;
        this.mTmpRect.set(windowFrames.mParentFrame);
        windowFrames.mDisplayFrame.set(clientWindowFrames.displayFrame);
        windowFrames.mParentFrame.set(clientWindowFrames.parentFrame);
        windowFrames.mFrame.set(clientWindowFrames.frame);
        windowFrames.mCompatFrame.set(windowFrames.mFrame);
        float f = this.mInvGlobalScale;
        if (f != 1.0f) {
            windowFrames.mCompatFrame.scale(f);
        }
        windowFrames.setParentFrameWasClippedByDisplayCutout(clientWindowFrames.isParentFrameClippedByDisplayCutout);
        windowFrames.mRelFrame.set(windowFrames.mFrame);
        WindowContainer parent = getParent();
        if (this.mIsChildWindow) {
            Rect rect = ((WindowState) parent).mWindowFrames.mFrame;
            i4 = rect.left;
            i3 = rect.top;
        } else if (parent != null) {
            Rect bounds = parent.getBounds();
            i4 = bounds.left;
            i3 = bounds.top;
        } else {
            i3 = 0;
            i4 = 0;
        }
        Rect rect2 = windowFrames.mRelFrame;
        Rect rect3 = windowFrames.mFrame;
        rect2.offsetTo(rect3.left - i4, rect3.top - i3);
        if (this.mWindowStateExt.supportTransWindowAnim(this, windowFrames)) {
            this.mMovedByResize = true;
        }
        if (i != this.mLastRequestedWidth || i2 != this.mLastRequestedHeight || !this.mTmpRect.equals(windowFrames.mParentFrame)) {
            this.mLastRequestedWidth = i;
            this.mLastRequestedHeight = i2;
            windowFrames.setContentChanged(true);
        }
        if (this.mAttrs.type == 2034 && !windowFrames.mFrame.equals(windowFrames.mLastFrame)) {
            this.mMovedByResize = true;
        }
        if (this.mIsWallpaper) {
            Rect rect4 = windowFrames.mLastFrame;
            Rect rect5 = windowFrames.mFrame;
            if (rect4.width() != rect5.width() || rect4.height() != rect5.height() || this.mWindowStateExt.forceUpdateWallpaperOffset(this)) {
                this.mDisplayContent.mWallpaperController.updateWallpaperOffset(this, false);
            }
        }
        updateSourceFrame(windowFrames.mFrame);
        ActivityRecord activityRecord = this.mActivityRecord;
        if (activityRecord != null && !this.mIsChildWindow) {
            activityRecord.layoutLetterbox(this);
        }
        this.mSurfacePlacementNeeded = true;
        this.mHaveFrame = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateSourceFrame(Rect rect) {
        if (hasInsetsSourceProvider() && !this.mGivenInsetsPending) {
            SparseArray insetsSourceProviders = getInsetsSourceProviders();
            for (int size = insetsSourceProviders.size() - 1; size >= 0; size--) {
                ((InsetsSourceProvider) insetsSourceProviders.valueAt(size)).updateSourceFrame(rect);
            }
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public Rect getBounds() {
        WindowToken windowToken = this.mToken;
        if ((windowToken instanceof ActivityRecord) && ((ActivityRecord) windowToken).getWrapper().getExtImpl().hasSizeCompatBoundsInOplusCompatMode()) {
            return this.mToken.getBounds();
        }
        if (!getWrapper().getExtImpl().layoutFullscreenInEmbedding() || getTask() == null) {
            return this.mToken.hasSizeCompatBounds() ? this.mToken.getBounds() : super.getBounds();
        }
        return getTask().getBounds();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getFrame() {
        return this.mWindowFrames.mFrame;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getRelativeFrame() {
        return this.mWindowFrames.mRelFrame;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getDisplayFrame() {
        return this.mWindowFrames.mDisplayFrame;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getParentFrame() {
        return this.mWindowFrames.mParentFrame;
    }

    public WindowManager.LayoutParams getAttrs() {
        return this.mAttrs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDisableFlags() {
        return this.mDisableFlags;
    }

    public int getBaseType() {
        return getTopParentWindow().mAttrs.type;
    }

    boolean setReportResizeHints() {
        return this.mWindowFrames.setReportResizeHints();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateResizingWindowIfNeeded() {
        StartingData startingData;
        boolean hasInsetsChanged = this.mWindowFrames.hasInsetsChanged();
        if ((this.mHasSurface && getDisplayContent().mLayoutSeq == this.mLayoutSeq && !isGoneForLayout()) || hasInsetsChanged) {
            WindowStateAnimator windowStateAnimator = this.mWinAnimator;
            boolean reportResizeHints = setReportResizeHints();
            boolean z = (this.mInRelayout || isLastConfigReportedToClient()) ? false : true;
            if (WindowManagerDebugConfig.DEBUG_CONFIGURATION && z) {
                Slog.v(TAG, "Win " + this + " config changed: " + getConfiguration());
            }
            boolean z2 = ViewRootImpl.LOCAL_LAYOUT && this.mLayoutAttached && getParentWindow().frameChanged();
            if (WindowManagerDebugConfig.DEBUG) {
                Slog.v(TAG, "Resizing " + this + ": configChanged=" + z + " last=" + this.mWindowFrames.mLastFrame + " frame=" + this.mWindowFrames.mFrame);
            }
            if (reportResizeHints || z || hasInsetsChanged || shouldSendRedrawForSync() || z2) {
                if (DEBUG_PANIC && !ProtoLogGroup.WM_DEBUG_RESIZE.isLogToLogcat()) {
                    Slog.v(TAG, "Resize reasons for w = " + this + "getInsetsChangedInfo() = " + this.mWindowFrames.getInsetsChangedInfo() + "configChanged = " + z + "didFrameInsetsChange = " + reportResizeHints + "insetsChanged = " + hasInsetsChanged + "attachedFrameChanged = " + z2 + "shouldSendRedrawForSync() = " + shouldSendRedrawForSync());
                }
                if (ProtoLogCache.WM_DEBUG_RESIZE_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_RESIZE, 686185515, 240, (String) null, new Object[]{String.valueOf(this), String.valueOf(this.mWindowFrames.getInsetsChangedInfo()), Boolean.valueOf(z), Boolean.valueOf(reportResizeHints)});
                }
                if (hasInsetsChanged) {
                    this.mWindowFrames.setInsetsChanged(false);
                    WindowManagerService windowManagerService = this.mWmService;
                    int i = windowManagerService.mWindowsInsetsChanged - 1;
                    windowManagerService.mWindowsInsetsChanged = i;
                    if (i == 0) {
                        windowManagerService.mH.removeMessages(66);
                    }
                }
                onResizeHandled();
                this.mWmService.makeWindowFreezingScreenIfNeededLocked(this);
                if (((z && !this.mWindowStateExt.isWallpaperAndInTransition()) || getOrientationChanging()) && isVisibleRequested()) {
                    windowStateAnimator.mDrawState = 1;
                    ActivityRecord activityRecord = this.mActivityRecord;
                    if (activityRecord != null) {
                        activityRecord.clearAllDrawn();
                        if (this.mAttrs.type == 3 && (startingData = this.mActivityRecord.mStartingData) != null) {
                            startingData.mIsDisplayed = false;
                        }
                    }
                }
                if (this.mWmService.mResizingWindows.contains(this)) {
                    return;
                }
                if (ProtoLogCache.WM_DEBUG_RESIZE_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_RESIZE, 685047360, 0, (String) null, new Object[]{String.valueOf(this)});
                }
                this.mWmService.mResizingWindows.add(this);
                return;
            }
            if (getOrientationChanging() && isDrawn()) {
                if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1329340614, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(windowStateAnimator.mSurfaceController)});
                }
                setOrientationChanging(false);
                long elapsedRealtime = SystemClock.elapsedRealtime();
                WindowManagerService windowManagerService2 = this.mWmService;
                this.mLastFreezeDuration = (int) (elapsedRealtime - windowManagerService2.mDisplayFreezeTime);
                this.mWindowStateExt.updateOrientationChangeIfNeeded(this, this.mActivityRecord, windowManagerService2);
            }
        }
    }

    private boolean frameChanged() {
        WindowFrames windowFrames = this.mWindowFrames;
        return !windowFrames.mFrame.equals(windowFrames.mLastFrame);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getOrientationChanging() {
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            return false;
        }
        return ((!this.mOrientationChanging && (!isVisible() || getConfiguration().orientation == getLastReportedConfiguration().orientation)) || this.mSeamlesslyRotated || this.mOrientationChangeTimedOut) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOrientationChanging(boolean z) {
        this.mOrientationChangeTimedOut = false;
        if (this.mOrientationChanging == z) {
            return;
        }
        this.mOrientationChanging = z;
        if (z) {
            this.mLastFreezeDuration = 0;
            if (this.mWmService.mRoot.mOrientationChangeComplete && this.mDisplayContent.shouldSyncRotationChange(this)) {
                this.mWmService.mRoot.mOrientationChangeComplete = false;
                return;
            }
            return;
        }
        this.mDisplayContent.finishAsyncRotation(this.mToken);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void orientationChangeTimedOut() {
        this.mOrientationChangeTimedOut = true;
    }

    @Override // com.android.server.wm.WindowContainer
    public DisplayContent getDisplayContent() {
        return this.mToken.getDisplayContent();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void onDisplayChanged(DisplayContent displayContent) {
        DisplayContent displayContent2;
        if (displayContent != null && (displayContent2 = this.mDisplayContent) != null && displayContent != displayContent2 && displayContent2.getImeInputTarget() == this) {
            this.mWindowStateExt.onDisplayImeChanged(displayContent, this.mDisplayContent, this);
            displayContent.updateImeInputAndControlTarget(getImeInputTarget());
            this.mDisplayContent.setImeInputTarget(null);
        }
        this.mWindowStateExt.onDisplayChanged(displayContent, this.mDisplayContent, this);
        super.onDisplayChanged(displayContent);
        if (displayContent != null && this.mInputWindowHandle.getDisplayId() != displayContent.getDisplayId()) {
            this.mLayoutSeq = displayContent.mLayoutSeq - 1;
            this.mInputWindowHandle.setDisplayId(displayContent.getDisplayId());
        }
        this.mWindowStateExt.onDisplayChangedEnd(this.mInputWindowHandle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayFrames getDisplayFrames(DisplayFrames displayFrames) {
        DisplayFrames fixedRotationTransformDisplayFrames = this.mToken.getFixedRotationTransformDisplayFrames();
        return fixedRotationTransformDisplayFrames != null ? fixedRotationTransformDisplayFrames : displayFrames;
    }

    DisplayInfo getDisplayInfo() {
        DisplayInfo fixedRotationTransformDisplayInfo = this.mToken.getFixedRotationTransformDisplayInfo();
        return fixedRotationTransformDisplayInfo != null ? fixedRotationTransformDisplayInfo : getDisplayContent().getDisplayInfo();
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public Rect getMaxBounds() {
        Rect fixedRotationTransformMaxBounds = this.mToken.getFixedRotationTransformMaxBounds();
        return fixedRotationTransformMaxBounds != null ? fixedRotationTransformMaxBounds : super.getMaxBounds();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsState getInsetsState() {
        return getInsetsState(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsState getInsetsState(boolean z) {
        InsetsState fixedRotationTransformInsetsState = this.mToken.getFixedRotationTransformInsetsState();
        InsetsPolicy insetsPolicy = getDisplayContent().getInsetsPolicy();
        if (fixedRotationTransformInsetsState != null) {
            return this.mWindowStateExt.hookGetInsetsState(insetsPolicy.adjustInsetsForWindow(this, fixedRotationTransformInsetsState), z);
        }
        InsetsState insetsState = this.mFrozenInsetsState;
        if (insetsState == null) {
            insetsState = getMergedInsetsState();
        }
        return this.mWindowStateExt.hookGetInsetsState(insetsPolicy.adjustInsetsForWindow(this, insetsPolicy.enforceInsetsPolicyForTarget(this.mAttrs, getWindowingMode(), isAlwaysOnTop(), insetsState), z), z);
    }

    private InsetsState getMergedInsetsState() {
        InsetsState insetsState;
        if (this.mAttrs.receiveInsetsIgnoringZOrder) {
            insetsState = getDisplayContent().getInsetsStateController().getRawInsetsState();
        } else {
            insetsState = this.mAboveInsetsState;
        }
        if (this.mMergedLocalInsetsSources == null) {
            return insetsState;
        }
        InsetsState insetsState2 = new InsetsState(insetsState);
        for (int i = 0; i < this.mMergedLocalInsetsSources.size(); i++) {
            insetsState2.addSource(this.mMergedLocalInsetsSources.valueAt(i));
        }
        return insetsState2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsState getCompatInsetsState() {
        InsetsState insetsState = getInsetsState();
        if (this.mInvGlobalScale != 1.0f) {
            InsetsState insetsState2 = new InsetsState(insetsState, true);
            insetsState2.scale(this.mInvGlobalScale);
            insetsState = insetsState2;
        }
        return this.mWindowStateExt.hookGetCompatInsetsState(insetsState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsState getInsetsStateWithVisibilityOverride() {
        InsetsState insetsState = new InsetsState(getInsetsState(), true);
        for (int sourceSize = insetsState.sourceSize() - 1; sourceSize >= 0; sourceSize--) {
            InsetsSource sourceAt = insetsState.sourceAt(sourceSize);
            boolean isRequestedVisible = isRequestedVisible(sourceAt.getType());
            if (sourceAt.isVisible() != isRequestedVisible) {
                sourceAt.setVisible(isRequestedVisible);
            }
        }
        return insetsState;
    }

    @Override // com.android.server.wm.InputTarget
    public int getDisplayId() {
        DisplayContent displayContent = getDisplayContent();
        if (displayContent == null) {
            return -1;
        }
        return displayContent.getDisplayId();
    }

    @Override // com.android.server.wm.InputTarget
    public IWindow getIWindow() {
        return this.mClient;
    }

    @Override // com.android.server.wm.InputTarget
    public int getPid() {
        return this.mSession.mPid;
    }

    @Override // com.android.server.wm.InputTarget
    public int getUid() {
        return this.mSession.mUid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getTask() {
        ActivityRecord activityRecord = this.mActivityRecord;
        if (activityRecord != null) {
            return activityRecord.getTask();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskFragment getTaskFragment() {
        ActivityRecord activityRecord = this.mActivityRecord;
        if (activityRecord != null) {
            return activityRecord.getTaskFragment();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getRootTask() {
        Task task = getTask();
        if (task != null) {
            return task.getRootTask();
        }
        DisplayContent displayContent = getDisplayContent();
        if (this.mAttrs.type < 2000 || displayContent == null) {
            return null;
        }
        return displayContent.getDefaultTaskDisplayArea().getRootHomeTask();
    }

    private void cutRect(Rect rect, Rect rect2) {
        int i;
        if (rect2.isEmpty()) {
            return;
        }
        int i2 = rect2.top;
        int i3 = rect.bottom;
        if (i2 < i3 && rect2.bottom > rect.top) {
            int i4 = rect2.right;
            int i5 = rect.right;
            if (i4 >= i5 && (i = rect2.left) >= rect.left) {
                rect.right = i;
            } else if (rect2.left <= rect.left && i4 <= i5) {
                rect.left = i4;
            }
        }
        if (rect2.left >= rect.right || rect2.right <= rect.left) {
            return;
        }
        int i6 = rect2.bottom;
        if (i6 >= i3 && i2 >= rect.top) {
            rect.bottom = i2;
        } else {
            if (i2 > rect.top || i6 > i3) {
                return;
            }
            rect.top = i6;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void getVisibleBounds(Rect rect) {
        Task task = getTask();
        boolean z = false;
        boolean z2 = task != null && task.cropWindowsToRootTaskBounds();
        rect.setEmpty();
        this.mTmpRect.setEmpty();
        if (z2) {
            Task rootTask = task.getRootTask();
            if (rootTask != null) {
                rootTask.getDimBounds(this.mTmpRect);
            }
            rect.set(this.mWindowFrames.mFrame);
            InsetsState insetsStateWithVisibilityOverride = getInsetsStateWithVisibilityOverride();
            int i = this.mAttrs.type;
            int windowingMode = getWindowingMode();
            WindowManager.LayoutParams layoutParams = this.mAttrs;
            rect.inset(insetsStateWithVisibilityOverride.calculateVisibleInsets(rect, i, windowingMode, layoutParams.softInputMode, layoutParams.flags));
            if (z) {
                return;
            }
            rect.intersect(this.mTmpRect);
            return;
        }
        z = z2;
        rect.set(this.mWindowFrames.mFrame);
        InsetsState insetsStateWithVisibilityOverride2 = getInsetsStateWithVisibilityOverride();
        int i2 = this.mAttrs.type;
        int windowingMode2 = getWindowingMode();
        WindowManager.LayoutParams layoutParams2 = this.mAttrs;
        rect.inset(insetsStateWithVisibilityOverride2.calculateVisibleInsets(rect, i2, windowingMode2, layoutParams2.softInputMode, layoutParams2.flags));
        if (z) {
        }
    }

    public long getInputDispatchingTimeoutMillis() {
        ActivityRecord activityRecord = this.mActivityRecord;
        if (activityRecord != null) {
            return activityRecord.mInputDispatchingTimeoutMillis;
        }
        return InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasAppShownWindows() {
        ActivityRecord activityRecord = this.mActivityRecord;
        return activityRecord != null && (activityRecord.firstWindowDrawn || activityRecord.isStartingWindowDisplayed());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean hasContentToDisplay() {
        if (!this.mAppFreezing && isDrawn()) {
            if (this.mViewVisibility == 0) {
                return true;
            }
            if (isAnimating(3) && !getDisplayContent().mAppTransition.isTransitionSet()) {
                return true;
            }
        }
        return super.hasContentToDisplay();
    }

    private boolean isVisibleByPolicyOrInsets() {
        InsetsSourceProvider insetsSourceProvider;
        return isVisibleByPolicy() && ((insetsSourceProvider = this.mControllableInsetProvider) == null || insetsSourceProvider.isClientVisible());
    }

    @Override // com.android.server.wm.WindowContainer
    public boolean isVisible() {
        return wouldBeVisibleIfPolicyIgnored() && isVisibleByPolicyOrInsets();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean isVisibleRequested() {
        boolean z = wouldBeVisibleRequestedIfPolicyIgnored() && isVisibleByPolicyOrInsets();
        return (z && shouldCheckTokenVisibleRequested()) ? this.mToken.isVisibleRequested() : z;
    }

    boolean shouldCheckTokenVisibleRequested() {
        return (this.mActivityRecord == null && this.mToken.asWallpaperToken() == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isVisibleByPolicy() {
        return (this.mPolicyVisibility & 3) == 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean providesDisplayDecorInsets() {
        if (this.mInsetsSourceProviders == null || !getWrapper().getExtImpl().providesDisplayDecorInsetsForTaskbar(this)) {
            return false;
        }
        for (int size = this.mInsetsSourceProviders.size() - 1; size >= 0; size--) {
            if ((this.mInsetsSourceProviders.valueAt(size).getSource().getType() & DisplayPolicy.DecorInsets.CONFIG_TYPES) != 0) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearPolicyVisibilityFlag(int i) {
        this.mPolicyVisibility &= ~i;
        this.mWmService.scheduleAnimationLocked();
        if (WindowManagerDebugConfig.DEBUG_LAYOUT || WindowManagerDebugConfig.DEBUG) {
            Slog.i(TAG, "clearPolicyVisibilityFlag =  " + Integer.toHexString(i) + " this = " + this + " from stack callers=" + Debug.getCallers(5));
        }
    }

    void setPolicyVisibilityFlag(int i) {
        this.mPolicyVisibility |= i;
        this.mWmService.scheduleAnimationLocked();
        if (WindowManagerDebugConfig.DEBUG_LAYOUT || WindowManagerDebugConfig.DEBUG) {
            Slog.i(TAG, "setPolicyVisibilityFlag = " + Integer.toHexString(i) + " this = " + this + " from stack callers=" + Debug.getCallers(5));
        }
    }

    private boolean isLegacyPolicyVisibility() {
        return (this.mPolicyVisibility & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean wouldBeVisibleIfPolicyIgnored() {
        if (!this.mHasSurface || isParentWindowHidden() || this.mAnimatingExit || this.mDestroying) {
            return false;
        }
        return !(this.mToken.asWallpaperToken() != null) || this.mToken.isVisible();
    }

    private boolean wouldBeVisibleRequestedIfPolicyIgnored() {
        WindowState parentWindow = getParentWindow();
        if (((parentWindow == null || parentWindow.isVisibleRequested()) ? false : true) || this.mAnimatingExit || this.mDestroying) {
            return false;
        }
        return !(this.mToken.asWallpaperToken() != null) || this.mToken.isVisibleRequested();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isVisibleNow() {
        return (this.mToken.isVisible() || this.mAttrs.type == 3) && isVisible();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPotentialDragTarget(boolean z) {
        return ((!z && !isVisibleNow()) || this.mRemoved || this.mInputChannel == null || this.mInputWindowHandle == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isVisibleRequestedOrAdding() {
        ActivityRecord activityRecord = this.mActivityRecord;
        return (this.mHasSurface || (!this.mRelayoutCalled && this.mViewVisibility == 0)) && isVisibleByPolicy() && !isParentWindowHidden() && !((activityRecord != null && !activityRecord.isVisibleRequested()) || this.mAnimatingExit || this.mDestroying);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isOnScreen() {
        if (!this.mHasSurface || this.mDestroying || !isVisibleByPolicy() || !this.mWindowStateExt.canShowInLockDeviceMode(this.mAttrs.type)) {
            return false;
        }
        ActivityRecord activityRecord = this.mActivityRecord;
        if (activityRecord != null) {
            return (!isParentWindowHidden() && activityRecord.isVisible()) || isAnimationRunningSelfOrParent();
        }
        WallpaperWindowToken asWallpaperToken = this.mToken.asWallpaperToken();
        return asWallpaperToken != null ? !isParentWindowHidden() && asWallpaperToken.isVisible() : !isParentWindowHidden() || isAnimating(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDreamWindow() {
        ActivityRecord activityRecord = this.mActivityRecord;
        return activityRecord != null && activityRecord.getActivityType() == 5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSecureLocked() {
        if ((this.mAttrs.flags & 8192) != 0) {
            return true;
        }
        return !DevicePolicyCache.getInstance().isScreenCaptureAllowed(this.mShowUserId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean mightAffectAllDrawn() {
        int i = this.mWinAnimator.mAttrType;
        return ((!isOnScreen() && !(i == 1 || i == 4)) || this.mAnimatingExit || this.mDestroying) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInteresting() {
        RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
        ActivityRecord activityRecord = this.mActivityRecord;
        return activityRecord != null && !(activityRecord.isFreezingScreen() && this.mAppFreezing && !this.mWindowStateExt.getDeviceFolding()) && this.mViewVisibility == 0 && (recentsAnimationController == null || recentsAnimationController.isInterestingForAllDrawn(this));
    }

    boolean isReadyForDisplay() {
        if (this.mToken.waitingToShow && getDisplayContent().mAppTransition.isTransitionSet()) {
            return false;
        }
        if (this.mWindowStateExt.isNotReadyForDisplayDuringFixedRotation(this, getDisplayContent(), this.mWindowFrames.mFrame)) {
            if (WindowManagerDebugConfig.DEBUG_ANIM) {
                Slog.d(TAG, "Block isReadyForDisplay of " + this);
            }
            return false;
        }
        boolean z = !isParentWindowHidden() && this.mViewVisibility == 0 && (this.mToken.isVisible() || this.mToken.mChildren.isEmpty());
        if (this.mHasSurface && isVisibleByPolicy() && !this.mDestroying) {
            return z || isAnimating(3);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFullyTransparent() {
        return this.mAttrs.alpha == 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canAffectSystemUiFlags() {
        if (isFullyTransparent()) {
            return false;
        }
        ActivityRecord activityRecord = this.mActivityRecord;
        if (activityRecord == null) {
            return this.mWinAnimator.getShown() && !(this.mAnimatingExit || this.mDestroying);
        }
        if (activityRecord.canAffectSystemUiFlags()) {
            return (this.mAttrs.type == 3 && (this.mStartingData instanceof SnapshotStartingData)) ? false : true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDisplayed() {
        ActivityRecord activityRecord = this.mActivityRecord;
        return isDrawn() && isVisibleByPolicy() && ((!isParentWindowHidden() && (activityRecord == null || activityRecord.isVisibleRequested())) || isAnimationRunningSelfOrParent());
    }

    public boolean isAnimatingLw() {
        return isAnimating(3);
    }

    public boolean isGoneForLayout() {
        ActivityRecord activityRecord = this.mActivityRecord;
        return this.mViewVisibility == 8 || !this.mRelayoutCalled || (activityRecord == null && !(wouldBeVisibleIfPolicyIgnored() && isVisibleByPolicy())) || (!(activityRecord == null || activityRecord.isVisibleRequested()) || isParentWindowGoneForLayout() || ((this.mAnimatingExit && !isAnimatingLw()) || this.mDestroying));
    }

    public boolean isDrawFinishedLw() {
        int i;
        return this.mHasSurface && !this.mDestroying && ((i = this.mWinAnimator.mDrawState) == 2 || i == 3 || i == 4);
    }

    public boolean isDrawn() {
        int i;
        return this.mHasSurface && !this.mDestroying && ((i = this.mWinAnimator.mDrawState) == 3 || i == 4);
    }

    private boolean isOpaqueDrawn() {
        boolean z = this.mToken.asWallpaperToken() != null;
        return ((!z && this.mAttrs.format == -1) || (z && this.mToken.isVisible())) && isDrawn() && !isAnimating(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestDrawIfNeeded(List<WindowState> list) {
        if (isVisible()) {
            ActivityRecord activityRecord = this.mActivityRecord;
            if (activityRecord != null) {
                if (!activityRecord.isVisibleRequested() || this.mActivityRecord.allDrawn || this.mWindowStateExt.checkIfHasDrawn(this)) {
                    return;
                }
                if (this.mAttrs.type == 3) {
                    if (isDrawn()) {
                        return;
                    }
                } else if (this.mActivityRecord.mStartingWindow != null) {
                    return;
                }
            } else if (!this.mPolicy.isKeyguardHostWindow(this.mAttrs)) {
                return;
            }
            WindowStateAnimator windowStateAnimator = this.mWinAnimator;
            windowStateAnimator.printWindowState(windowStateAnimator.mDrawState, 1, this, "requestDrawIfNeeded");
            this.mWinAnimator.mDrawState = 1;
            forceReportingResized();
            if (list.contains(this) || this.mWindowStateExt.isInSkipWaitingForDrawn(this)) {
                return;
            }
            list.add(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void onMovedByResize() {
        if (ProtoLogCache.WM_DEBUG_RESIZE_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_RESIZE, 1635462459, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        this.mMovedByResize = true;
        super.onMovedByResize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAppVisibilityChanged(boolean z, boolean z2) {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            ((WindowState) this.mChildren.get(size)).onAppVisibilityChanged(z, z2);
        }
        boolean isVisibleNow = isVisibleNow();
        if (this.mAttrs.type != 3) {
            if (z != isVisibleNow) {
                if (!z2 && isVisibleNow) {
                    AccessibilityController accessibilityController = this.mWmService.mAccessibilityController;
                    this.mWinAnimator.applyAnimationLocked(2, false);
                    if (accessibilityController.hasCallbacks()) {
                        accessibilityController.onWindowTransition(this, 2);
                    }
                }
                setDisplayLayoutNeeded();
                return;
            }
            return;
        }
        if (!z && isVisibleNow && this.mActivityRecord.isAnimating(3)) {
            if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ANIM, -1471518109, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            this.mAnimatingExit = true;
            this.mRemoveOnExit = true;
            this.mWindowRemovalAllowed = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onSetAppExiting(boolean z) {
        DisplayContent displayContent = getDisplayContent();
        boolean z2 = false;
        if (!z) {
            this.mPermanentlyHidden = true;
            hide(false, false);
        }
        if (isVisibleNow() && z) {
            this.mWinAnimator.applyAnimationLocked(2, false);
            if (this.mWmService.mAccessibilityController.hasCallbacks()) {
                this.mWmService.mAccessibilityController.onWindowTransition(this, 2);
            }
            if (displayContent != null) {
                displayContent.setLayoutNeeded();
            }
            z2 = true;
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            z2 |= ((WindowState) this.mChildren.get(size)).onSetAppExiting(z);
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void onResize() {
        ArrayList<WindowState> arrayList = this.mWmService.mResizingWindows;
        if (this.mHasSurface && !isGoneForLayout() && !arrayList.contains(this)) {
            if (ProtoLogCache.WM_DEBUG_RESIZE_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_RESIZE, 417311568, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            arrayList.add(this);
            this.mWindowStateExt.needResetDrawStateOnResize(this, this.mWindowFrames.mFrame);
        }
        if (isGoneForLayout()) {
            this.mResizedWhileGone = true;
        }
        super.onResize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleWindowMovedIfNeeded() {
        if (hasMoved()) {
            Rect rect = this.mWindowFrames.mFrame;
            int i = rect.left;
            int i2 = rect.top;
            if (canPlayMoveAnimation() && !this.mWindowStateExt.isNoMoveAnimationOnFlexibleWindow()) {
                startMoveAnimation(i, i2);
            }
            if (this.mWmService.mAccessibilityController.hasCallbacks()) {
                this.mWmService.mAccessibilityController.onSomeWindowResizedOrMoved(getDisplayId());
            }
            try {
                this.mClient.moved(i, i2);
            } catch (RemoteException unused) {
            }
            this.mMovedByResize = false;
        }
    }

    private boolean canPlayMoveAnimation() {
        boolean hasMovementAnimations;
        if (getTask() == null) {
            hasMovementAnimations = getWindowConfiguration().hasMovementAnimations();
        } else {
            hasMovementAnimations = getTask().getWindowConfiguration().hasMovementAnimations();
        }
        return this.mToken.okToAnimate() && (this.mAttrs.privateFlags & 64) == 0 && !isDragResizing() && hasMovementAnimations && !this.mWinAnimator.mLastHidden && !this.mSeamlesslyRotated;
    }

    private boolean hasMoved() {
        if (this.mHasSurface && ((this.mWindowFrames.hasContentChanged() || this.mMovedByResize) && !this.mAnimatingExit)) {
            WindowFrames windowFrames = this.mWindowFrames;
            Rect rect = windowFrames.mRelFrame;
            int i = rect.top;
            Rect rect2 = windowFrames.mLastRelFrame;
            if ((i != rect2.top || rect.left != rect2.left) && ((!this.mIsChildWindow || !getParentWindow().hasMoved()) && !this.mTransitionController.isCollecting())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isObscuringDisplay() {
        Task task = getTask();
        return (task == null || task.fillsParent()) && isOpaqueDrawn() && fillsDisplay();
    }

    boolean fillsDisplay() {
        DisplayInfo displayInfo = getDisplayInfo();
        Rect rect = this.mWindowFrames.mFrame;
        return rect.left <= 0 && rect.top <= 0 && rect.right >= displayInfo.appWidth && rect.bottom >= displayInfo.appHeight;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean matchesDisplayAreaBounds() {
        Rect fixedRotationTransformDisplayBounds = this.mToken.getFixedRotationTransformDisplayBounds();
        if (fixedRotationTransformDisplayBounds != null) {
            return fixedRotationTransformDisplayBounds.equals(getBounds());
        }
        DisplayArea displayArea = getDisplayArea();
        if (displayArea == null) {
            return getDisplayContent().getBounds().equals(getBounds());
        }
        return displayArea.getBounds().equals(getBounds());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLastConfigReportedToClient() {
        return this.mLastConfigReportedToClient;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(Configuration configuration) {
        Configuration configuration2 = super.getConfiguration();
        this.mTempConfiguration.setTo(configuration2);
        super.onConfigurationChanged(configuration);
        int diff = configuration2.diff(this.mTempConfiguration);
        if (diff != 0) {
            this.mLastConfigReportedToClient = false;
        }
        if ((getDisplayContent().getImeInputTarget() == this || isImeLayeringTarget()) && (diff & 536870912) != 0) {
            this.mDisplayContent.updateImeControlTarget(isImeLayeringTarget());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void removeImmediately() {
        this.mWindowStateExt.removeImmediately(this);
        if (this.mRemoved) {
            if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 2018454757, 0, (String) null, new Object[]{String.valueOf(this)});
                return;
            }
            return;
        }
        this.mRemoved = true;
        this.mWinAnimator.destroySurfaceLocked(getSyncTransaction());
        if (!this.mDrawHandlers.isEmpty()) {
            this.mWmService.mH.removeMessages(64, this);
        }
        super.removeImmediately();
        this.mWindowStateExt.cancelFadeAnimationIfNeed(this);
        if (isImeOverlayLayeringTarget()) {
            this.mWmService.dispatchImeTargetOverlayVisibilityChanged(this.mClient.asBinder(), this.mAttrs.type, false, true);
        }
        DisplayContent displayContent = getDisplayContent();
        if (isImeLayeringTarget()) {
            displayContent.removeImeSurfaceByTarget(this);
            displayContent.setImeLayeringTarget(null);
            displayContent.computeImeTarget(true);
        }
        if (displayContent.getImeInputTarget() == this && !inRelaunchingActivity()) {
            this.mWmService.dispatchImeInputTargetVisibilityChanged(this.mClient.asBinder(), false, true);
            displayContent.updateImeInputAndControlTarget(null);
        }
        if (WindowManagerService.excludeWindowTypeFromTapOutTask(this.mAttrs.type)) {
            displayContent.mTapExcludedWindows.remove(this);
        }
        displayContent.mTapExcludeProvidingWindows.remove(this);
        displayContent.getDisplayPolicy().removeWindowLw(this);
        disposeInputChannel();
        this.mOnBackInvokedCallbackInfo = null;
        this.mSession.windowRemovedLocked();
        try {
            this.mClient.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
        } catch (RuntimeException unused) {
        }
        this.mWindowStateExt.cancelSplashScreenAnimation(this);
        this.mWmService.postWindowRemoveCleanupLocked(this);
        this.mWindowStateExt.setSimultaneousDisplayState(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void removeIfPossible() {
        int i;
        int i2;
        int i3;
        boolean z;
        ActivityRecord activityRecord;
        this.mWindowRemovalAllowed = true;
        if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1504168072, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(Debug.getCallers(5))});
        }
        int i4 = this.mAttrs.type;
        boolean z2 = i4 == 3;
        if (z2) {
            if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -986746907, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            ActivityRecord activityRecord2 = this.mActivityRecord;
            if (activityRecord2 != null) {
                activityRecord2.forAllWindows(new ToBooleanFunction() { // from class: com.android.server.wm.WindowState$$ExternalSyntheticLambda2
                    public final boolean apply(Object obj) {
                        boolean lambda$removeIfPossible$2;
                        lambda$removeIfPossible$2 = WindowState.lambda$removeIfPossible$2((WindowState) obj);
                        return lambda$removeIfPossible$2;
                    }
                }, true);
            }
        } else if (i4 == 1 && isSelfAnimating(0, 128)) {
            cancelAnimation();
        }
        if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS, -1047945589, 1, (String) null, new Object[]{Long.valueOf(System.identityHashCode(this.mClient.asBinder())), String.valueOf(this.mWinAnimator.mSurfaceController), String.valueOf(Debug.getCallers(5))});
        }
        DisplayContent displayContent = getDisplayContent();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            disposeInputChannel();
            this.mOnBackInvokedCallbackInfo = null;
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                String valueOf = String.valueOf(this);
                String valueOf2 = String.valueOf(this.mWinAnimator.mSurfaceController);
                boolean z3 = this.mAnimatingExit;
                boolean z4 = this.mRemoveOnExit;
                boolean z5 = this.mHasSurface;
                boolean shown = this.mWinAnimator.getShown();
                boolean isAnimating = isAnimating(3);
                ActivityRecord activityRecord3 = this.mActivityRecord;
                i3 = 2;
                i2 = 4;
                i = 5;
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -778347463, 262128, (String) null, new Object[]{valueOf, valueOf2, Boolean.valueOf(z3), Boolean.valueOf(z4), Boolean.valueOf(z5), Boolean.valueOf(shown), Boolean.valueOf(isAnimating), Boolean.valueOf(activityRecord3 != null && activityRecord3.isAnimating(3)), Boolean.valueOf(this.mWmService.mDisplayFrozen), String.valueOf(Debug.getCallers(6))});
            } else {
                i = 5;
                i2 = 4;
                i3 = 2;
            }
            if (this.mHasSurface && this.mToken.okToAnimate()) {
                z = isVisible();
                boolean z6 = (displayContent.inTransition() || inRelaunchingActivity()) ? false : true;
                if (z && isDisplayed()) {
                    if (!z2) {
                        i = i3;
                    }
                    if (z6 && this.mWinAnimator.applyAnimationLocked(i, false)) {
                        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ANIM, -91393839, 0, (String) null, new Object[]{String.valueOf(this)});
                        }
                        this.mAnimatingExit = true;
                        setDisplayLayoutNeeded();
                        this.mWmService.requestTraversal();
                    }
                    if (this.mWmService.mAccessibilityController.hasCallbacks()) {
                        this.mWmService.mAccessibilityController.onWindowTransition(this, i);
                    }
                }
                boolean z7 = z6 && (this.mAnimatingExit || isAnimationRunningSelfOrParent());
                boolean z8 = z2 && (activityRecord = this.mActivityRecord) != null && activityRecord.isLastWindow(this);
                if (this.mWinAnimator.getShown() && !z8 && z7) {
                    this.mAnimatingExit = true;
                    if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -1103716954, 0, (String) null, new Object[]{String.valueOf(this)});
                    }
                    if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ANIM, 975275467, 0, (String) null, new Object[]{String.valueOf(this)});
                    }
                    setupWindowForRemoveOnExit();
                    ActivityRecord activityRecord4 = this.mActivityRecord;
                    if (activityRecord4 != null) {
                        activityRecord4.updateReportedVisibilityLocked();
                    }
                    return;
                }
            } else {
                z = false;
            }
            boolean providesDisplayDecorInsets = providesDisplayDecorInsets();
            removeImmediately();
            boolean z9 = z && displayContent.updateOrientation();
            if (providesDisplayDecorInsets) {
                z9 |= displayContent.getDisplayPolicy().updateDecorInsetsInfo();
            }
            if (z9) {
                displayContent.sendNewConfiguration();
            }
            this.mWmService.updateFocusedWindowLocked(isFocused() ? i2 : 0, true);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeIfPossible$2(WindowState windowState) {
        if (!windowState.isSelfAnimating(0, 128)) {
            return false;
        }
        windowState.cancelAnimation();
        return true;
    }

    private void setupWindowForRemoveOnExit() {
        this.mRemoveOnExit = true;
        setDisplayLayoutNeeded();
        getDisplayContent().getDisplayPolicy().removeWindowLw(this);
        boolean updateFocusedWindowLocked = this.mWmService.updateFocusedWindowLocked(3, false);
        this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
        if (updateFocusedWindowLocked) {
            getDisplayContent().getInputMonitor().updateInputWindowsLw(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHasSurface(boolean z) {
        this.mHasSurface = z;
        this.mWindowStateExt.updateWindowState(this, this.mSession, this.mWinAnimator, this.mAttrs.type, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canBeImeTarget() {
        ActivityRecord activityRecord;
        ActivityRecord activityRecord2;
        ActivityRecord activityRecord3;
        int i;
        if (this.mIsImWindow || inPinnedWindowingMode() || this.mAttrs.type == 2036 || this.mWindowStateExt.isMinimizedPocketStudio()) {
            return false;
        }
        ActivityRecord activityRecord4 = this.mActivityRecord;
        if (!(activityRecord4 == null || activityRecord4.windowsAreFocusable())) {
            return false;
        }
        Task rootTask = getRootTask();
        if (rootTask != null && !rootTask.isFocusable()) {
            return false;
        }
        WindowManager.LayoutParams layoutParams = this.mAttrs;
        if (layoutParams.type != 3 && (i = layoutParams.flags & 131080) != 0 && i != 131080) {
            return false;
        }
        if (rootTask != null && (activityRecord3 = this.mActivityRecord) != null && this.mTransitionController.isTransientLaunch(activityRecord3)) {
            return false;
        }
        if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
            StringBuilder sb = new StringBuilder();
            sb.append("isVisibleRequestedOrAdding ");
            sb.append(this);
            sb.append(": ");
            sb.append(isVisibleRequestedOrAdding());
            sb.append(" isVisible: ");
            sb.append(isVisible() && (activityRecord2 = this.mActivityRecord) != null && activityRecord2.isVisible());
            Slog.i(TAG, sb.toString());
            if (!isVisibleRequestedOrAdding()) {
                Slog.i(TAG, "  mSurfaceController=" + this.mWinAnimator.mSurfaceController + " relayoutCalled=" + this.mRelayoutCalled + " viewVis=" + this.mViewVisibility + " policyVis=" + isVisibleByPolicy() + " policyVisAfterAnim=" + this.mLegacyPolicyVisibilityAfterAnim + " parentHidden=" + isParentWindowHidden() + " exiting=" + this.mAnimatingExit + " destroying=" + this.mDestroying);
                if (this.mActivityRecord != null) {
                    Slog.i(TAG, "  mActivityRecord.visibleRequested=" + this.mActivityRecord.isVisibleRequested());
                }
            }
        }
        return isVisibleRequestedOrAdding() || (isVisible() && (activityRecord = this.mActivityRecord) != null && activityRecord.isVisible());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void openInputChannel(InputChannel inputChannel) {
        if (this.mInputChannel != null) {
            throw new IllegalStateException("Window already has an input channel.");
        }
        InputChannel createInputChannel = this.mWmService.mInputManager.createInputChannel(getName());
        this.mInputChannel = createInputChannel;
        IBinder token = createInputChannel.getToken();
        this.mInputChannelToken = token;
        this.mInputWindowHandle.setToken(token);
        this.mWmService.mInputToWindowMap.put(this.mInputChannelToken, this);
        this.mInputChannel.copyTo(inputChannel);
    }

    public boolean transferTouch() {
        return this.mWmService.mInputManager.transferTouch(this.mInputChannelToken, getDisplayId());
    }

    void disposeInputChannel() {
        IBinder iBinder = this.mInputChannelToken;
        if (iBinder != null) {
            this.mWmService.mInputManager.removeInputChannel(iBinder);
            this.mWmService.mKeyInterceptionInfoForToken.remove(this.mInputChannelToken);
            this.mWmService.mInputToWindowMap.remove(this.mInputChannelToken);
            this.mInputChannelToken = null;
        }
        InputChannel inputChannel = this.mInputChannel;
        if (inputChannel != null) {
            inputChannel.dispose();
            this.mInputChannel = null;
        }
        this.mInputWindowHandle.setToken(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDisplayLayoutNeeded() {
        DisplayContent displayContent = getDisplayContent();
        if (displayContent != null) {
            displayContent.setLayoutNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void switchUser(int i) {
        super.switchUser(i);
        if (showToCurrentUser()) {
            setPolicyVisibilityFlag(2);
            return;
        }
        if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.w(TAG, "user changing, hiding " + this + ", attrs=" + this.mAttrs.type + ", belonging to " + this.mOwnerUid);
        }
        clearPolicyVisibilityFlag(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getSurfaceTouchableRegion(Region region, WindowManager.LayoutParams layoutParams) {
        boolean isModal = layoutParams.isModal();
        if (isModal) {
            if (this.mActivityRecord != null) {
                updateRegionForModalActivityWindow(region);
                this.mWindowStateExt.resizeTouchRegionForSpecial(this.mActivityRecord, this.mWindowFrames, region, this);
                this.mWindowStateExt.resizeTouchableRegionForBracketMode(region, this.mActivityRecord, this);
            } else {
                getDisplayContent().getBounds(this.mTmpRect);
                int width = this.mTmpRect.width();
                int height = this.mTmpRect.height();
                region.set(-width, -height, width + width, height + height);
            }
            subtractTouchExcludeRegionIfNeeded(region);
        } else {
            getTouchableRegion(region);
        }
        this.mWindowStateExt.resizeTouchableRegionInOplusCompatMode(this, region);
        this.mWindowStateExt.resizeTouchableRegionForBracketPanelWindow(region, this);
        Rect rect = this.mWindowFrames.mFrame;
        if ((rect.left != 0 || rect.top != 0) && !this.mWindowStateExt.translateTouchableRegionInOplusCompatMode(this, region)) {
            region.translate(-rect.left, -rect.top);
        }
        if (isModal && this.mTouchableInsets == 3) {
            this.mTmpRegion.set(0, 0, rect.right, rect.bottom);
            this.mTmpRegion.op(this.mGivenTouchableRegion, Region.Op.DIFFERENCE);
            region.op(this.mTmpRegion, Region.Op.DIFFERENCE);
        }
        float f = this.mInvGlobalScale;
        if (f != 1.0f) {
            region.scale(f);
        }
    }

    private void adjustRegionInFreefromWindowMode(Rect rect) {
        if (inFreeformWindowingMode() || this.mWindowStateExt.checkIfWindowingModeZoom(getWindowingMode())) {
            int i = -WindowManagerService.dipToPixel(30, getDisplayContent().getDisplayMetrics());
            rect.inset(i, i);
        }
    }

    private void updateRegionForModalActivityWindow(Region region) {
        this.mActivityRecord.getLetterboxInnerBounds(this.mTmpRect);
        if (this.mTmpRect.isEmpty()) {
            Rect fixedRotationTransformDisplayBounds = this.mActivityRecord.getFixedRotationTransformDisplayBounds();
            if (fixedRotationTransformDisplayBounds != null) {
                this.mTmpRect.set(fixedRotationTransformDisplayBounds);
            } else {
                TaskFragment taskFragment = getTaskFragment();
                if (taskFragment != null) {
                    Task asTask = taskFragment.asTask();
                    if (asTask != null) {
                        asTask.getDimBounds(this.mTmpRect);
                    } else {
                        this.mTmpRect.set(taskFragment.getBounds());
                    }
                } else if (getRootTask() != null) {
                    getRootTask().getDimBounds(this.mTmpRect);
                }
            }
        }
        adjustRegionInFreefromWindowMode(this.mTmpRect);
        getWrapper().getExtImpl().adjustTouchableRegionInActivityEmbedding(this, this.mTmpRect);
        region.set(this.mTmpRect);
        cropRegionToRootTaskBoundsIfNeeded(region);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkPolicyVisibilityChange() {
        if (isLegacyPolicyVisibility() != this.mLegacyPolicyVisibilityAfterAnim) {
            if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(TAG, "Policy visibility changing after anim in " + this.mWinAnimator + ": " + this.mLegacyPolicyVisibilityAfterAnim);
            }
            if (this.mLegacyPolicyVisibilityAfterAnim) {
                setPolicyVisibilityFlag(1);
            } else {
                clearPolicyVisibilityFlag(1);
            }
            if (isVisibleByPolicy()) {
                return;
            }
            this.mWinAnimator.hide(SurfaceControl.getGlobalTransaction(), "checkPolicyVisibilityChange");
            if (isFocused()) {
                if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 693423992, 0, (String) null, (Object[]) null);
                }
                this.mWmService.mFocusMayChange = true;
            }
            setDisplayLayoutNeeded();
            this.mWmService.enableScreenIfNeededLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRequestedSize(int i, int i2) {
        if (this.mRequestedWidth == i && this.mRequestedHeight == i2) {
            return;
        }
        this.mLayoutNeeded = true;
        this.mRequestedWidth = i;
        this.mRequestedHeight = i2;
    }

    void prepareWindowToDisplayDuringRelayout(boolean z) {
        ActivityRecord activityRecord;
        if ((this.mAttrs.flags & 2097152) != 0 || ((activityRecord = this.mActivityRecord) != null && activityRecord.canTurnScreenOn())) {
            WindowManagerService windowManagerService = this.mWmService;
            boolean z2 = windowManagerService.mAllowTheaterModeWakeFromLayout || Settings.Global.getInt(windowManagerService.mContext.getContentResolver(), "theater_mode_on", 0) == 0;
            ActivityRecord activityRecord2 = this.mActivityRecord;
            boolean z3 = activityRecord2 == null || activityRecord2.currentLaunchCanTurnScreenOn();
            if (z2 && z3 && (this.mWmService.mAtmService.isDreaming() || !this.mPowerManagerWrapper.isInteractive())) {
                if (WindowManagerDebugConfig.DEBUG_VISIBILITY || WindowManagerDebugConfig.DEBUG_POWER) {
                    Slog.v(TAG, "Relayout window turning screen on: " + this);
                }
                this.mWindowStateExt.wakeupInPrepareWindowToDisplayDuringRelayout(this.mAttrs.getTitle() != null ? this.mAttrs.getTitle().toString() : null);
            } else {
                this.mWindowStateExt.setSimultaneousDisplayState(true);
            }
            ActivityRecord activityRecord3 = this.mActivityRecord;
            if (activityRecord3 != null) {
                activityRecord3.setCurrentLaunchCanTurnScreenOn(false);
            }
        }
        if (z) {
            if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(TAG, "Already visible and does not turn on screen, skip preparing: " + this);
                return;
            }
            return;
        }
        if ((this.mAttrs.softInputMode & 240) == 16) {
            this.mLayoutNeeded = true;
        }
        if (isDrawn() && this.mToken.okToAnimate()) {
            this.mWinAnimator.applyEnterAnimationLocked();
        }
    }

    private Configuration getProcessGlobalConfiguration() {
        WindowState parentWindow = getParentWindow();
        return this.mWmService.mAtmService.getGlobalConfigurationForPid((parentWindow != null ? parentWindow.mSession : this.mSession).mPid);
    }

    private Configuration getLastReportedConfiguration() {
        return this.mLastReportedConfiguration.getMergedConfiguration();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void adjustStartingWindowFlags() {
        ActivityRecord activityRecord;
        WindowState windowState;
        WindowManager.LayoutParams layoutParams = this.mAttrs;
        if (layoutParams.type != 1 || (activityRecord = this.mActivityRecord) == null || (windowState = activityRecord.mStartingWindow) == null) {
            return;
        }
        WindowManager.LayoutParams layoutParams2 = windowState.mAttrs;
        layoutParams2.flags = (layoutParams.flags & 4718593) | (layoutParams2.flags & (-4718594));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowScale(int i, int i2) {
        WindowManager.LayoutParams layoutParams = this.mAttrs;
        if ((layoutParams.flags & 16384) != 0) {
            int i3 = layoutParams.width;
            this.mHScale = i3 != i ? i3 / i : 1.0f;
            int i4 = layoutParams.height;
            this.mVScale = i4 != i2 ? i4 / i2 : 1.0f;
            return;
        }
        this.mVScale = 1.0f;
        this.mHScale = 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class DeathRecipient implements IBinder.DeathRecipient {
        private DeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            try {
                WindowManagerGlobalLock windowManagerGlobalLock = WindowState.this.mWmService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        WindowState windowState = WindowState.this;
                        WindowState windowForClientLocked = windowState.mWmService.windowForClientLocked(windowState.mSession, windowState.mClient, false);
                        Slog.i(WindowState.TAG, "WIN DEATH: " + windowForClientLocked);
                        if (windowForClientLocked != null) {
                            ActivityRecord activityRecord = windowForClientLocked.mActivityRecord;
                            if (activityRecord != null && activityRecord.findMainWindow() == windowForClientLocked) {
                                WindowState.this.mWmService.mSnapshotController.onAppDied(windowForClientLocked.mActivityRecord);
                            }
                            windowForClientLocked.removeIfPossible();
                        } else if (WindowState.this.mHasSurface) {
                            Slog.e(WindowState.TAG, "!!! LEAK !!! Window removed but surface still valid.");
                            WindowState.this.removeIfPossible();
                        }
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (IllegalArgumentException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canReceiveKeys() {
        return canReceiveKeys(false);
    }

    public String canReceiveKeysReason(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("fromTouch= ");
        sb.append(z);
        sb.append(" isVisibleRequestedOrAdding=");
        sb.append(isVisibleRequestedOrAdding());
        sb.append(" mViewVisibility=");
        sb.append(this.mViewVisibility);
        sb.append(" mRemoveOnExit=");
        sb.append(this.mRemoveOnExit);
        sb.append(" flags=");
        sb.append(this.mAttrs.flags);
        sb.append(" appWindowsAreFocusable=");
        ActivityRecord activityRecord = this.mActivityRecord;
        boolean z2 = false;
        sb.append(activityRecord == null || activityRecord.windowsAreFocusable(z));
        sb.append(" canReceiveTouchInput=");
        sb.append(canReceiveTouchInput());
        sb.append(" displayIsOnTop=");
        sb.append(getDisplayContent().isOnTop());
        sb.append(" displayIsTrusted=");
        sb.append(getDisplayContent().isTrusted());
        sb.append(" transitShouldKeepFocus=");
        ActivityRecord activityRecord2 = this.mActivityRecord;
        if (activityRecord2 != null && this.mTransitionController.shouldKeepFocus(activityRecord2)) {
            z2 = true;
        }
        sb.append(z2);
        return sb.toString();
    }

    public boolean canReceiveKeys(boolean z) {
        ActivityRecord activityRecord;
        ActivityRecord activityRecord2;
        ActivityRecord activityRecord3 = this.mActivityRecord;
        if (activityRecord3 != null && this.mTransitionController.shouldKeepFocus(activityRecord3)) {
            return true;
        }
        if (!(isVisibleRequestedOrAdding() && this.mViewVisibility == 0 && !this.mRemoveOnExit && (this.mAttrs.flags & 8) == 0 && ((activityRecord = this.mActivityRecord) == null || activityRecord.windowsAreFocusable(z)) && ((activityRecord2 = this.mActivityRecord) == null || activityRecord2.getTask() == null || !this.mActivityRecord.getTask().getRootTask().shouldIgnoreInput()))) {
            return false;
        }
        if (!this.mWindowStateExt.hookCanReceiveKeys(this.mAttrs.type, this.mWinAnimator)) {
            return z || getDisplayContent().isOnTop() || getDisplayContent().isTrusted() || getDisplayContent().getWrapper().getNonStaticExtImpl().isPuttDisplay() || this.mWindowStateExt.isMirageDisplay(getDisplayId());
        }
        Slog.v(TAG, "canReceiveKeys: false if in lock device mode and win =" + this + "not show");
        return false;
    }

    public boolean canShowWhenLocked() {
        ActivityRecord activityRecord = this.mActivityRecord;
        if (activityRecord != null) {
            return activityRecord.canShowWhenLocked();
        }
        return (this.mAttrs.flags & 524288) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canReceiveTouchInput() {
        ActivityRecord activityRecord = this.mActivityRecord;
        if (activityRecord == null || activityRecord.getTask() == null || this.mTransitionController.shouldKeepFocus(this.mActivityRecord)) {
            return true;
        }
        return !this.mActivityRecord.getTask().getRootTask().shouldIgnoreInput() && this.mActivityRecord.isVisibleRequested();
    }

    @Deprecated
    public boolean hasDrawn() {
        return this.mWinAnimator.mDrawState == 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean show(boolean z, boolean z2) {
        if ((isLegacyPolicyVisibility() && this.mLegacyPolicyVisibilityAfterAnim) || !showToCurrentUser() || !this.mAppOpVisibility || this.mPermanentlyHidden || this.mHiddenWhileSuspended || this.mForceHideNonSystemOverlayWindow) {
            return false;
        }
        if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(TAG, "Policy visibility true: " + this);
        }
        if (z) {
            if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(TAG, "doAnimation: mPolicyVisibility=" + isLegacyPolicyVisibility() + " animating=" + isAnimating(3));
            }
            if (!this.mToken.okToAnimate() || (isLegacyPolicyVisibility() && !isAnimating(3))) {
                z = false;
            }
        }
        setPolicyVisibilityFlag(1);
        this.mLegacyPolicyVisibilityAfterAnim = true;
        if (z) {
            this.mWinAnimator.applyAnimationLocked(1, true);
        }
        if (z2) {
            this.mWmService.scheduleAnimationLocked();
        }
        if ((this.mAttrs.flags & 8) == 0) {
            this.mWmService.updateFocusedWindowLocked(0, false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hide(boolean z, boolean z2) {
        if (z && !this.mToken.okToAnimate()) {
            z = false;
        }
        if (!(z ? this.mLegacyPolicyVisibilityAfterAnim : isLegacyPolicyVisibility())) {
            return false;
        }
        if (z && !this.mWinAnimator.applyAnimationLocked(2, false)) {
            z = false;
        }
        this.mLegacyPolicyVisibilityAfterAnim = false;
        boolean isFocused = isFocused();
        if (!z) {
            if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(TAG, "Policy visibility false: " + this);
            }
            clearPolicyVisibilityFlag(1);
            this.mWmService.enableScreenIfNeededLocked();
            if (isFocused) {
                if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 1288731814, 0, (String) null, (Object[]) null);
                }
                this.mWmService.mFocusMayChange = true;
            }
        }
        if (z2) {
            this.mWmService.scheduleAnimationLocked();
        }
        if (isFocused) {
            this.mWmService.updateFocusedWindowLocked(0, false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForceHideNonSystemOverlayWindowIfNeeded(boolean z) {
        if (this.mSession.mCanAddInternalSystemWindow) {
            return;
        }
        if (WindowManager.LayoutParams.isSystemAlertWindowType(this.mAttrs.type) || this.mAttrs.type == 2005) {
            WindowManager.LayoutParams layoutParams = this.mAttrs;
            if ((layoutParams.type == 2038 && layoutParams.isSystemApplicationOverlay() && this.mSession.mCanCreateSystemApplicationOverlay) || this.mWindowStateExt.canOverlayWindows() || this.mForceHideNonSystemOverlayWindow == z) {
                return;
            }
            this.mForceHideNonSystemOverlayWindow = z;
            if (z) {
                hide(true, true);
            } else {
                show(true, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHiddenWhileSuspended(boolean z) {
        if (this.mOwnerCanAddInternalSystemWindow) {
            return;
        }
        if ((WindowManager.LayoutParams.isSystemAlertWindowType(this.mAttrs.type) || this.mAttrs.type == 2005) && this.mHiddenWhileSuspended != z) {
            this.mHiddenWhileSuspended = z;
            if (z) {
                hide(true, true);
            } else {
                show(true, true);
            }
        }
    }

    private void setAppOpVisibilityLw(boolean z) {
        if (!this.mWindowStateExt.canSetAppOpVisibilityLw(getOwningPackage(), getOwningUid())) {
            z = false;
        }
        if (this.mAppOpVisibility != z) {
            this.mAppOpVisibility = z;
            if (z) {
                show(true, true);
            } else {
                hide(true, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initAppOpsState() {
        if (this.mAppOp == -1 || !this.mAppOpVisibility) {
            return;
        }
        if (!this.mWindowStateExt.canInitAppOpVisibilityLw(getOwningPackage(), getOwningUid(), this.mSession.mPid)) {
            setAppOpVisibilityLw(false);
            return;
        }
        int startOpNoThrow = this.mWmService.mAppOps.startOpNoThrow(this.mAppOp, getOwningUid(), getOwningPackage(), true, null, "init-default-visibility");
        if (startOpNoThrow == 0 || startOpNoThrow == 3) {
            return;
        }
        setAppOpVisibilityLw(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetAppOpsState() {
        int i = this.mAppOp;
        if (i == -1 || !this.mAppOpVisibility) {
            return;
        }
        this.mWmService.mAppOps.finishOp(i, getOwningUid(), getOwningPackage(), (String) null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAppOpsState() {
        if (this.mAppOp == -1) {
            return;
        }
        int owningUid = getOwningUid();
        String owningPackage = getOwningPackage();
        if (this.mAppOpVisibility) {
            int checkOpNoThrow = this.mWmService.mAppOps.checkOpNoThrow(this.mAppOp, owningUid, owningPackage);
            if (checkOpNoThrow == 0 || checkOpNoThrow == 3) {
                return;
            }
            this.mWmService.mAppOps.finishOp(this.mAppOp, owningUid, owningPackage, (String) null);
            setAppOpVisibilityLw(false);
            return;
        }
        int startOpNoThrow = this.mWmService.mAppOps.startOpNoThrow(this.mAppOp, owningUid, owningPackage, true, null, "attempt-to-be-visible");
        if (startOpNoThrow == 0 || startOpNoThrow == 3) {
            setAppOpVisibilityLw(true);
        }
    }

    public void hidePermanentlyLw() {
        if (this.mPermanentlyHidden) {
            return;
        }
        this.mPermanentlyHidden = true;
        hide(true, true);
    }

    public void pokeDrawLockLw(long j) {
        if (isVisibleRequestedOrAdding()) {
            if (this.mDrawLock == null) {
                CharSequence windowTag = getWindowTag();
                PowerManager.WakeLock newWakeLock = this.mWmService.mPowerManager.newWakeLock(128, "Window:" + ((Object) windowTag));
                this.mDrawLock = newWakeLock;
                newWakeLock.setReferenceCounted(false);
                this.mDrawLock.setWorkSource(new WorkSource(this.mOwnerUid, this.mAttrs.packageName));
            }
            if (WindowManagerDebugConfig.DEBUG_POWER) {
                Slog.d(TAG, "pokeDrawLock: poking draw lock on behalf of visible window owned by " + this.mAttrs.packageName);
            }
            this.mDrawLock.acquire(j);
            return;
        }
        if (WindowManagerDebugConfig.DEBUG_POWER) {
            Slog.d(TAG, "pokeDrawLock: suppressed draw lock request for invisible window owned by " + this.mAttrs.packageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAlive() {
        return this.mClient.asBinder().isBinderAlive();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void sendAppVisibilityToClients() {
        super.sendAppVisibilityToClients();
        boolean isClientVisible = this.mToken.isClientVisible();
        if (this.mAttrs.type != 3 || isClientVisible) {
            try {
                if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                    Slog.v(TAG, "Setting visibility of " + this + ": " + isClientVisible);
                }
                this.mClient.dispatchAppVisibility(isClientVisible);
            } catch (RemoteException e) {
                Slog.w(TAG, "Exception thrown during dispatchAppVisibility " + this, e);
                int threadGroupLeader = Process.getThreadGroupLeader(this.mSession.mPid);
                int i = this.mSession.mPid;
                if (threadGroupLeader != i) {
                    Slog.w(TAG, this.mSession.mPid + " is reused by others, skip killProcess");
                    return;
                }
                Process.killProcess(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onStartFreezingScreen() {
        if (this.mWindowStateExt.shouldSkipFreezingWhenFolding(this)) {
            return;
        }
        this.mAppFreezing = true;
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            ((WindowState) this.mChildren.get(size)).onStartFreezingScreen();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onStopFreezingScreen() {
        boolean z = false;
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            z |= ((WindowState) this.mChildren.get(size)).onStopFreezingScreen();
        }
        if (!this.mAppFreezing) {
            return z;
        }
        this.mAppFreezing = false;
        if (this.mHasSurface && !getOrientationChanging() && this.mWmService.mWindowsFreezingScreen != 2) {
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1747461042, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            setOrientationChanging(true);
        }
        this.mLastFreezeDuration = 0;
        setDisplayLayoutNeeded();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean destroySurface(boolean z, boolean z2) {
        ArrayList arrayList = new ArrayList(this.mChildren);
        boolean z3 = false;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            z3 |= ((WindowState) arrayList.get(size)).destroySurface(z, z2);
        }
        if ((z2 || this.mWindowRemovalAllowed || z) && this.mDestroying) {
            if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
                ProtoLogImpl.e(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1577579529, 252, (String) null, new Object[]{String.valueOf(this), Boolean.valueOf(z2), Boolean.valueOf(this.mWindowRemovalAllowed), Boolean.valueOf(this.mRemoveOnExit)});
            }
            if (!z || this.mRemoveOnExit) {
                destroySurfaceUnchecked();
            }
            if (this.mRemoveOnExit) {
                removeImmediately();
            }
            if (z) {
                requestUpdateWallpaperIfNeeded();
            }
            this.mDestroying = false;
            if (!getDisplayContent().mAppTransition.isTransitionSet() || !getDisplayContent().mOpeningApps.contains(this.mActivityRecord)) {
                return true;
            }
            this.mWmService.mWindowPlacerLocked.requestTraversal();
            return true;
        }
        return z3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroySurfaceUnchecked() {
        this.mWinAnimator.destroySurfaceLocked(this.mTmpTransaction);
        this.mTmpTransaction.apply();
        this.mAnimatingExit = false;
        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ANIM, -2052051397, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        this.mWindowStateExt.setHideByKeyguardExitAnim(false);
        if (useBLASTSync()) {
            immediatelyNotifyBlastSync();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSurfaceShownChanged(boolean z) {
        if (this.mLastShownChangedReported == z) {
            return;
        }
        this.mLastShownChangedReported = z;
        if (z) {
            initExclusionRestrictions();
        } else {
            logExclusionRestrictions(0);
            logExclusionRestrictions(1);
            getDisplayContent().removeImeSurfaceByTarget(this);
        }
        int i = this.mAttrs.type;
        if (i >= 2000 && i != 2005 && i != 2030 && ((i != 2037 || !isOnVirtualDisplay()) && (this.mAttrs.type != 2037 || !this.mWindowStateExt.inRemapViceDisplay(this)))) {
            this.mWmService.mAtmService.mActiveUids.onNonAppSurfaceVisibilityChanged(this.mOwnerUid, z);
        }
        this.mWindowStateExt.onNonAppSurfaceVisibilityChanged(z);
    }

    private boolean isOnVirtualDisplay() {
        return getDisplayContent().mDisplay.getType() == 5;
    }

    private void logExclusionRestrictions(int i) {
        if (!DisplayContent.logsGestureExclusionRestrictions(this) || SystemClock.uptimeMillis() < this.mLastExclusionLogUptimeMillis[i] + this.mWmService.mConstants.mSystemGestureExclusionLogDebounceTimeoutMillis) {
            return;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        long[] jArr = this.mLastExclusionLogUptimeMillis;
        long j = uptimeMillis - jArr[i];
        jArr[i] = uptimeMillis;
        int i2 = this.mLastRequestedExclusionHeight[i];
        FrameworkStatsLog.write(223, this.mAttrs.packageName, i2, i2 - this.mLastGrantedExclusionHeight[i], i + 1, getConfiguration().orientation == 2, false, (int) j);
    }

    private void initExclusionRestrictions() {
        long uptimeMillis = SystemClock.uptimeMillis();
        long[] jArr = this.mLastExclusionLogUptimeMillis;
        jArr[0] = uptimeMillis;
        jArr[1] = uptimeMillis;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:15:0x001f. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:16:0x0022. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:17:0x0025. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:18:0x0028. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0033 A[FALL_THROUGH] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    boolean showForAllUsers() {
        WindowManager.LayoutParams layoutParams = this.mAttrs;
        int i = layoutParams.type;
        if (i != 3 && i != 2024 && i != 2030 && i != 2034 && i != 2037 && i != 2026 && i != 2027) {
            switch (i) {
                default:
                    switch (i) {
                        default:
                            switch (i) {
                                default:
                                    switch (i) {
                                        default:
                                            if ((layoutParams.privateFlags & 16) == 0) {
                                                return false;
                                            }
                                        case 2039:
                                        case 2040:
                                        case 2041:
                                            return this.mOwnerCanAddInternalSystemWindow;
                                    }
                                case 2017:
                                case 2018:
                                case 2019:
                                case IMirageWindowManagerExt.DISPLAY_ID /* 2020 */:
                                case 2021:
                                case 2022:
                                    break;
                            }
                        case 2007:
                        case 2008:
                        case 2009:
                            break;
                    }
                case 2000:
                case 2001:
                case 2002:
                    break;
            }
        }
        return this.mOwnerCanAddInternalSystemWindow;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean showToCurrentUser() {
        ActivityRecord activityRecord;
        WindowState topParentWindow = getTopParentWindow();
        return (topParentWindow.mAttrs.type < 2000 && (activityRecord = topParentWindow.mActivityRecord) != null && activityRecord.mShowForAllUsers && topParentWindow.getFrame().left <= topParentWindow.getDisplayFrame().left && topParentWindow.getFrame().top <= topParentWindow.getDisplayFrame().top && topParentWindow.getFrame().right >= topParentWindow.getDisplayFrame().right && topParentWindow.getFrame().bottom >= topParentWindow.getDisplayFrame().bottom) || topParentWindow.showForAllUsers() || this.mWmService.isUserVisible(topParentWindow.mShowUserId);
    }

    private static void applyInsets(Region region, Rect rect, Rect rect2) {
        region.set(rect.left + rect2.left, rect.top + rect2.top, rect.right - rect2.right, rect.bottom - rect2.bottom);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getTouchableRegion(Region region) {
        Rect rect = this.mWindowFrames.mFrame;
        int i = this.mTouchableInsets;
        if (i == 1) {
            applyInsets(region, rect, this.mGivenContentInsets);
        } else if (i == 2) {
            applyInsets(region, rect, this.mGivenVisibleInsets);
        } else if (i != 3) {
            region.set(rect);
        } else {
            region.set(this.mGivenTouchableRegion);
            int i2 = rect.left;
            if (i2 != 0 || rect.top != 0) {
                region.translate(i2, rect.top);
            }
        }
        cropRegionToRootTaskBoundsIfNeeded(region);
        subtractTouchExcludeRegionIfNeeded(region);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getEffectiveTouchableRegion(Region region) {
        DisplayContent displayContent = getDisplayContent();
        if (this.mAttrs.isModal() && displayContent != null) {
            region.set(displayContent.getBounds());
            cropRegionToRootTaskBoundsIfNeeded(region);
            subtractTouchExcludeRegionIfNeeded(region);
            return;
        }
        getTouchableRegion(region);
    }

    private void cropRegionToRootTaskBoundsIfNeeded(Region region) {
        Task rootTask;
        Task task = getTask();
        if (task == null || !task.cropWindowsToRootTaskBounds() || this.mWindowStateExt.isCompactScaledWindowingMode(this) || (rootTask = task.getRootTask()) == null || rootTask.mCreatedByOrganizer) {
            return;
        }
        rootTask.getDimBounds(this.mTmpRect);
        adjustRegionInFreefromWindowMode(this.mTmpRect);
        region.op(this.mTmpRect, Region.Op.INTERSECT);
    }

    private void subtractTouchExcludeRegionIfNeeded(Region region) {
        if (this.mTapExcludeRegion.isEmpty()) {
            return;
        }
        Region obtain = Region.obtain();
        getTapExcludeRegion(obtain);
        if (!obtain.isEmpty()) {
            region.op(obtain, Region.Op.DIFFERENCE);
        }
        obtain.recycle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportFocusChangedSerialized(boolean z) {
        RemoteCallbackList<IWindowFocusObserver> remoteCallbackList = this.mFocusCallbacks;
        if (remoteCallbackList != null) {
            int beginBroadcast = remoteCallbackList.beginBroadcast();
            for (int i = 0; i < beginBroadcast; i++) {
                IWindowFocusObserver broadcastItem = this.mFocusCallbacks.getBroadcastItem(i);
                if (z) {
                    try {
                        broadcastItem.focusGained(this.mWindowId.asBinder());
                    } catch (RemoteException unused) {
                    }
                } else {
                    broadcastItem.focusLost(this.mWindowId.asBinder());
                }
            }
            this.mFocusCallbacks.finishBroadcast();
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public Configuration getConfiguration() {
        if (!registeredForDisplayAreaConfigChanges()) {
            return super.getConfiguration();
        }
        this.mTempConfiguration.setTo(getProcessGlobalConfiguration());
        this.mTempConfiguration.updateFrom(getMergedOverrideConfiguration());
        return this.mTempConfiguration;
    }

    private boolean registeredForDisplayAreaConfigChanges() {
        WindowProcessController windowProcessController;
        WindowState parentWindow = getParentWindow();
        if (parentWindow != null) {
            windowProcessController = parentWindow.mWpcForDisplayAreaConfigChanges;
        } else {
            windowProcessController = this.mWpcForDisplayAreaConfigChanges;
        }
        return windowProcessController != null && windowProcessController.registeredForDisplayAreaConfigChanges();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowProcessController getProcess() {
        return this.mWpcForDisplayAreaConfigChanges;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fillClientWindowFramesAndConfiguration(ClientWindowFrames clientWindowFrames, MergedConfiguration mergedConfiguration, boolean z, boolean z2) {
        ActivityRecord activityRecord;
        clientWindowFrames.frame.set(this.mWindowFrames.mCompatFrame);
        clientWindowFrames.displayFrame.set(this.mWindowFrames.mDisplayFrame);
        float f = this.mInvGlobalScale;
        if (f != 1.0f) {
            clientWindowFrames.displayFrame.scale(f);
        }
        if (this.mLayoutAttached) {
            if (clientWindowFrames.attachedFrame == null) {
                clientWindowFrames.attachedFrame = new Rect();
            }
            clientWindowFrames.attachedFrame.set(getParentWindow().getFrame());
            float f2 = this.mInvGlobalScale;
            if (f2 != 1.0f) {
                clientWindowFrames.attachedFrame.scale(f2);
            }
        }
        clientWindowFrames.compatScale = getCompatScaleForClient();
        if (z || (z2 && ((activityRecord = this.mActivityRecord) == null || activityRecord.isVisibleRequested()))) {
            mergedConfiguration.setConfiguration(getProcessGlobalConfiguration(), getMergedOverrideConfiguration());
            MergedConfiguration mergedConfiguration2 = this.mLastReportedConfiguration;
            if (mergedConfiguration != mergedConfiguration2) {
                mergedConfiguration2.setTo(mergedConfiguration);
            }
        } else {
            mergedConfiguration.setTo(this.mLastReportedConfiguration);
        }
        this.mLastConfigReportedToClient = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportResized() {
        if (inRelaunchingActivity()) {
            return;
        }
        if ((!shouldCheckTokenVisibleRequested() || this.mToken.isVisibleRequested()) && !this.mWindowStateExt.shouldSkipResizeStartingWindow(this.mAttrs)) {
            if (Trace.isTagEnabled(32L)) {
                Trace.traceBegin(32L, "wm.reportResized_" + ((Object) getWindowTag()));
            }
            if (ProtoLogCache.WM_DEBUG_RESIZE_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_RESIZE, -1824578273, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(this.mWindowFrames.mCompatFrame)});
            }
            boolean z = this.mWinAnimator.mDrawState == 1;
            if (z && ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1130868271, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            this.mDragResizingChangeReported = true;
            this.mWindowFrames.clearReportResizeHints();
            updateLastFrames();
            int rotation = this.mLastReportedConfiguration.getMergedConfiguration().windowConfiguration.getRotation();
            fillClientWindowFramesAndConfiguration(this.mClientWindowFrames, this.mLastReportedConfiguration, true, false);
            boolean shouldSendRedrawForSync = shouldSendRedrawForSync();
            boolean z2 = shouldSendRedrawForSync && shouldSyncWithBuffers();
            boolean z3 = shouldSendRedrawForSync || z;
            boolean isDragResizeChanged = isDragResizeChanged();
            boolean z4 = shouldSendRedrawForSync || isDragResizeChanged || this.mWindowStateExt.getDeviceFolding();
            DisplayContent displayContent = getDisplayContent();
            boolean areSystemBarsForcedConsumedLw = displayContent.getDisplayPolicy().areSystemBarsForcedConsumedLw();
            int displayId = displayContent.getDisplayId();
            if (isDragResizeChanged) {
                setDragResizing();
            }
            boolean isDragResizing = isDragResizing();
            markRedrawForSyncReported();
            try {
                this.mWindowStateExt.setWindowRelayoutFlag(false);
                this.mWindowStateExt.setLastFinishDrawDp(-1);
                this.mClient.resized(this.mClientWindowFrames, z3, this.mLastReportedConfiguration, getCompatInsetsState(), z4, areSystemBarsForcedConsumedLw, displayId, z2 ? this.mSyncSeqId : -1, isDragResizing);
                if (z && rotation >= 0 && rotation != this.mLastReportedConfiguration.getMergedConfiguration().windowConfiguration.getRotation()) {
                    this.mOrientationChangeRedrawRequestTime = SystemClock.elapsedRealtime();
                    if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -567946587, 0, (String) null, new Object[]{String.valueOf(this)});
                    }
                }
                if (this.mWmService.mAccessibilityController.hasCallbacks()) {
                    this.mWmService.mAccessibilityController.onSomeWindowResizedOrMoved(displayId);
                }
            } catch (RemoteException e) {
                setOrientationChanging(false);
                this.mLastFreezeDuration = (int) (SystemClock.elapsedRealtime() - this.mWmService.mDisplayFreezeTime);
                Slog.w(TAG, "Failed to report 'resized' to " + this + " due to " + e);
            }
            Trace.traceEnd(32L);
        }
    }

    boolean inRelaunchingActivity() {
        ActivityRecord activityRecord = this.mActivityRecord;
        return activityRecord != null && activityRecord.isRelaunching();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isClientLocal() {
        return this.mClient instanceof IWindow.Stub;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyInsetsChanged() {
        if (ProtoLogCache.WM_DEBUG_WINDOW_INSETS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, 1047505501, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        this.mWindowFrames.setInsetsChanged(true);
        WindowManagerService windowManagerService = this.mWmService;
        windowManagerService.mWindowsInsetsChanged++;
        windowManagerService.mH.removeMessages(66);
        this.mWmService.mH.sendEmptyMessage(66);
        WindowContainer parent = getParent();
        if (parent != null) {
            parent.updateOverlayInsetsState(this);
        }
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public void notifyInsetsControlChanged() {
        if (ProtoLogCache.WM_DEBUG_WINDOW_INSETS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, 1030898920, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        if (this.mRemoved) {
            return;
        }
        try {
            this.mClient.insetsControlChanged(getCompatInsetsState(), getDisplayContent().getInsetsStateController().getControlsForDispatch(this));
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to deliver inset control state change to w=" + this, e);
        }
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public void showInsets(int i, boolean z, ImeTracker.Token token) {
        try {
            ImeTracker.forLogging().onProgress(token, 21);
            this.mClient.showInsets(i, z, token);
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to deliver showInsets", e);
            ImeTracker.forLogging().onFailed(token, 21);
        }
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public void hideInsets(int i, boolean z, ImeTracker.Token token) {
        try {
            ImeTracker.forLogging().onProgress(token, 22);
            this.mClient.hideInsets(i, z, token);
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to deliver hideInsets", e);
            ImeTracker.forLogging().onFailed(token, 22);
        }
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public boolean canShowTransient() {
        return (this.mAttrs.insetsFlags.behavior & 2) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canBeHiddenByKeyguard() {
        int i;
        return (this.mActivityRecord != null || this.mWindowStateExt.isOnMirageDisplay(this) || (i = this.mAttrs.type) == 2000 || i == 2013 || i == 2019 || i == 2040 || this.mPolicy.getWindowLayerLw(this) >= this.mPolicy.getWindowLayerFromTypeLw(2040)) ? false : true;
    }

    private int getRootTaskId() {
        Task rootTask = getRootTask();
        if (rootTask == null) {
            return -1;
        }
        return rootTask.mTaskId;
    }

    public void registerFocusObserver(IWindowFocusObserver iWindowFocusObserver) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mFocusCallbacks == null) {
                    this.mFocusCallbacks = new RemoteCallbackList<>();
                }
                this.mFocusCallbacks.register(iWindowFocusObserver);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void unregisterFocusObserver(IWindowFocusObserver iWindowFocusObserver) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                RemoteCallbackList<IWindowFocusObserver> remoteCallbackList = this.mFocusCallbacks;
                if (remoteCallbackList != null) {
                    remoteCallbackList.unregister(iWindowFocusObserver);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFocused() {
        return getDisplayContent().mCurrentFocus == this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean areAppWindowBoundsLetterboxed() {
        ActivityRecord activityRecord = this.mActivityRecord;
        return activityRecord != null && (activityRecord.areBoundsLetterboxed() || isLetterboxedForDisplayCutout());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLetterboxedForDisplayCutout() {
        if (this.mActivityRecord == null || !this.mWindowFrames.parentFrameWasClippedByDisplayCutout()) {
            return false;
        }
        WindowManager.LayoutParams layoutParams = this.mAttrs;
        if (layoutParams.layoutInDisplayCutoutMode == 3 || !layoutParams.isFullscreen() || this.mWindowStateExt.checkIfWindowingModeZoom(getWindowingMode())) {
            return false;
        }
        return !frameCoversEntireAppTokenBounds();
    }

    private boolean frameCoversEntireAppTokenBounds() {
        this.mTmpRect.set(this.mActivityRecord.getBounds());
        this.mTmpRect.intersectUnchecked(this.mWindowFrames.mFrame);
        return this.mActivityRecord.getBounds().equals(this.mTmpRect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFullyTransparentBarAllowed(Rect rect) {
        ActivityRecord activityRecord = this.mActivityRecord;
        return activityRecord == null || activityRecord.isFullyTransparentBarAllowed(rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDragResizeChanged() {
        return this.mDragResizing != computeDragResizing();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void setWaitingForDrawnIfResizingChanged() {
        if (isDragResizeChanged()) {
            this.mWmService.mRoot.mWaitingForDrawn.add(this);
        }
        super.setWaitingForDrawnIfResizingChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void resetDragResizingChangeReported() {
        this.mDragResizingChangeReported = false;
        super.resetDragResizingChangeReported();
    }

    private boolean computeDragResizing() {
        Task task = getTask();
        if (task == null) {
            return false;
        }
        if ((!inFreeformWindowingMode() && !task.getRootTask().mCreatedByOrganizer) || task.getActivityType() == 2) {
            return false;
        }
        WindowManager.LayoutParams layoutParams = this.mAttrs;
        return layoutParams.width == -1 && layoutParams.height == -1 && task.isDragResizing();
    }

    void setDragResizing() {
        boolean computeDragResizing = computeDragResizing();
        if (computeDragResizing == this.mDragResizing) {
            return;
        }
        this.mDragResizing = computeDragResizing;
    }

    boolean isDragResizing() {
        return this.mDragResizing;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j, int i) {
        boolean isVisible = isVisible();
        if (i != 2 || isVisible) {
            long start = protoOutputStream.start(j);
            super.dumpDebug(protoOutputStream, 1146756268033L, i);
            protoOutputStream.write(1120986464259L, getDisplayId());
            protoOutputStream.write(1120986464260L, getRootTaskId());
            this.mAttrs.dumpDebug(protoOutputStream, 1146756268037L);
            this.mGivenContentInsets.dumpDebug(protoOutputStream, 1146756268038L);
            this.mWindowFrames.dumpDebug(protoOutputStream, 1146756268073L);
            this.mAttrs.surfaceInsets.dumpDebug(protoOutputStream, 1146756268044L);
            GraphicsProtos.dumpPointProto(this.mSurfacePosition, protoOutputStream, 1146756268048L);
            this.mWinAnimator.dumpDebug(protoOutputStream, 1146756268045L);
            protoOutputStream.write(1133871366158L, this.mAnimatingExit);
            protoOutputStream.write(1120986464274L, this.mRequestedWidth);
            protoOutputStream.write(1120986464275L, this.mRequestedHeight);
            protoOutputStream.write(1120986464276L, this.mViewVisibility);
            protoOutputStream.write(1133871366166L, this.mHasSurface);
            protoOutputStream.write(1133871366167L, isReadyForDisplay());
            protoOutputStream.write(1133871366178L, this.mRemoveOnExit);
            protoOutputStream.write(1133871366179L, this.mDestroying);
            protoOutputStream.write(1133871366180L, this.mRemoved);
            protoOutputStream.write(1133871366181L, isOnScreen());
            protoOutputStream.write(1133871366182L, isVisible);
            protoOutputStream.write(1133871366183L, this.mPendingSeamlessRotate != null);
            protoOutputStream.write(1133871366186L, this.mForceSeamlesslyRotate);
            protoOutputStream.write(1133871366187L, hasCompatScale());
            protoOutputStream.write(1108101562412L, this.mGlobalScale);
            Iterator<Rect> it = this.mKeepClearAreas.iterator();
            while (it.hasNext()) {
                it.next().dumpDebug(protoOutputStream, 2246267895853L);
            }
            Iterator<Rect> it2 = this.mUnrestrictedKeepClearAreas.iterator();
            while (it2.hasNext()) {
                it2.next().dumpDebug(protoOutputStream, 2246267895854L);
            }
            if (this.mMergedLocalInsetsSources != null) {
                for (int i2 = 0; i2 < this.mMergedLocalInsetsSources.size(); i2++) {
                    this.mMergedLocalInsetsSources.valueAt(i2).dumpDebug(protoOutputStream, 2246267895855L);
                }
            }
            protoOutputStream.end(start);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    public void writeIdentifierToProto(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1120986464257L, System.identityHashCode(this));
        protoOutputStream.write(1120986464258L, this.mShowUserId);
        CharSequence windowTag = getWindowTag();
        if (windowTag != null) {
            protoOutputStream.write(1138166333443L, windowTag.toString());
        }
        protoOutputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    @NeverCompile
    public void dump(PrintWriter printWriter, String str, boolean z) {
        printWriter.print(str + "mDisplayId=" + getDisplayId());
        if (getRootTask() != null) {
            printWriter.print(" rootTaskId=" + getRootTaskId());
        }
        printWriter.println(" mSession=" + this.mSession + " mClient=" + this.mClient.asBinder());
        printWriter.println(str + "mOwnerUid=" + this.mOwnerUid + " showForAllUsers=" + showForAllUsers() + " package=" + this.mAttrs.packageName + " appop=" + AppOpsManager.opToName(this.mAppOp));
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("mAttrs=");
        sb.append(this.mAttrs.toString(str));
        printWriter.println(sb.toString());
        printWriter.println(str + "Requested w=" + this.mRequestedWidth + " h=" + this.mRequestedHeight + " mLayoutSeq=" + this.mLayoutSeq);
        if (this.mRequestedWidth != this.mLastRequestedWidth || this.mRequestedHeight != this.mLastRequestedHeight) {
            printWriter.println(str + "LastRequested w=" + this.mLastRequestedWidth + " h=" + this.mLastRequestedHeight);
        }
        if (this.mIsChildWindow || this.mLayoutAttached) {
            printWriter.println(str + "mParentWindow=" + getParentWindow() + " mLayoutAttached=" + this.mLayoutAttached);
        }
        if (this.mIsImWindow || this.mIsWallpaper || this.mIsFloatingLayer) {
            printWriter.println(str + "mIsImWindow=" + this.mIsImWindow + " mIsWallpaper=" + this.mIsWallpaper + " mIsFloatingLayer=" + this.mIsFloatingLayer);
        }
        if (z) {
            printWriter.print(str);
            printWriter.print("mBaseLayer=");
            printWriter.print(this.mBaseLayer);
            printWriter.print(" mSubLayer=");
            printWriter.print(this.mSubLayer);
        }
        if (z) {
            printWriter.println(str + "mToken=" + this.mToken);
            if (this.mActivityRecord != null) {
                printWriter.println(str + "mActivityRecord=" + this.mActivityRecord);
                printWriter.print(str + "drawnStateEvaluated=" + getDrawnStateEvaluated());
                printWriter.println(str + "mightAffectAllDrawn=" + mightAffectAllDrawn());
            }
            printWriter.println(str + "mViewVisibility=0x" + Integer.toHexString(this.mViewVisibility) + " mHaveFrame=" + this.mHaveFrame + " mObscured=" + this.mObscured);
            if (this.mDisableFlags != 0) {
                printWriter.println(str + "mDisableFlags=" + ViewDebug.flagsToString(View.class, "mSystemUiVisibility", this.mDisableFlags));
            }
        }
        if (!isVisibleByPolicy() || !this.mLegacyPolicyVisibilityAfterAnim || !this.mAppOpVisibility || isParentWindowHidden() || this.mPermanentlyHidden || this.mForceHideNonSystemOverlayWindow || this.mHiddenWhileSuspended) {
            printWriter.println(str + "mPolicyVisibility=" + isVisibleByPolicy() + " mLegacyPolicyVisibilityAfterAnim=" + this.mLegacyPolicyVisibilityAfterAnim + " mAppOpVisibility=" + this.mAppOpVisibility + " parentHidden=" + isParentWindowHidden() + " mPermanentlyHidden=" + this.mPermanentlyHidden + " mHiddenWhileSuspended=" + this.mHiddenWhileSuspended + " mForceHideNonSystemOverlayWindow=" + this.mForceHideNonSystemOverlayWindow);
        }
        if (!this.mRelayoutCalled || this.mLayoutNeeded) {
            printWriter.println(str + "mRelayoutCalled=" + this.mRelayoutCalled + " mLayoutNeeded=" + this.mLayoutNeeded);
        }
        if (z) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("mGivenContentInsets=");
            Rect rect = this.mGivenContentInsets;
            StringBuilder sb3 = sTmpSB;
            sb2.append(rect.toShortString(sb3));
            sb2.append(" mGivenVisibleInsets=");
            sb2.append(this.mGivenVisibleInsets.toShortString(sb3));
            printWriter.println(sb2.toString());
            if (this.mTouchableInsets != 0 || this.mGivenInsetsPending) {
                printWriter.println(str + "mTouchableInsets=" + this.mTouchableInsets + " mGivenInsetsPending=" + this.mGivenInsetsPending);
                Region region = new Region();
                getTouchableRegion(region);
                printWriter.println(str + "touchable region=" + region);
            }
            printWriter.println(str + "mFullConfiguration=" + getConfiguration());
            printWriter.println(str + "mLastReportedConfiguration=" + getLastReportedConfiguration());
        }
        printWriter.println(str + "mHasSurface=" + this.mHasSurface + " isReadyForDisplay()=" + isReadyForDisplay() + " mWindowRemovalAllowed=" + this.mWindowRemovalAllowed);
        if (this.mInvGlobalScale != 1.0f) {
            printWriter.println(str + "mCompatFrame=" + this.mWindowFrames.mCompatFrame.toShortString(sTmpSB));
        }
        if (z) {
            this.mWindowFrames.dump(printWriter, str);
            printWriter.println(str + " surface=" + this.mAttrs.surfaceInsets.toShortString(sTmpSB));
        }
        super.dump(printWriter, str, z);
        printWriter.println(str + this.mWinAnimator + ":");
        this.mWinAnimator.dump(printWriter, str + "  ", z);
        if (this.mAnimatingExit || this.mRemoveOnExit || this.mDestroying || this.mRemoved) {
            printWriter.println(str + "mAnimatingExit=" + this.mAnimatingExit + " mRemoveOnExit=" + this.mRemoveOnExit + " mDestroying=" + this.mDestroying + " mRemoved=" + this.mRemoved);
        }
        if (getOrientationChanging() || this.mAppFreezing) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append("mOrientationChanging=");
            sb4.append(this.mOrientationChanging);
            sb4.append(" configOrientationChanging=");
            sb4.append(getLastReportedConfiguration().orientation != getConfiguration().orientation);
            sb4.append(" mAppFreezing=");
            sb4.append(this.mAppFreezing);
            printWriter.println(sb4.toString());
        }
        if (this.mLastFreezeDuration != 0) {
            printWriter.print(str + "mLastFreezeDuration=");
            TimeUtils.formatDuration((long) this.mLastFreezeDuration, printWriter);
            printWriter.println();
        }
        printWriter.print(str + "mForceSeamlesslyRotate=" + this.mForceSeamlesslyRotate + " seamlesslyRotate: pending=");
        SeamlessRotator seamlessRotator = this.mPendingSeamlessRotate;
        if (seamlessRotator != null) {
            seamlessRotator.dump(printWriter);
        } else {
            printWriter.print("null");
        }
        if (this.mXOffset != 0 || this.mYOffset != 0) {
            printWriter.println(str + "mXOffset=" + this.mXOffset + " mYOffset=" + this.mYOffset);
        }
        if (this.mHScale != 1.0f || this.mVScale != 1.0f) {
            printWriter.println(str + "mHScale=" + this.mHScale + " mVScale=" + this.mVScale);
        }
        if (this.mWallpaperX != -1.0f || this.mWallpaperY != -1.0f) {
            printWriter.println(str + "mWallpaperX=" + this.mWallpaperX + " mWallpaperY=" + this.mWallpaperY);
        }
        printWriter.println(str + "mXOffset=" + this.mXOffset + " mYOffset=" + this.mYOffset);
        StringBuilder sb5 = new StringBuilder();
        sb5.append(str);
        sb5.append("mSeamlesslyRotated=");
        sb5.append(this.mSeamlesslyRotated);
        printWriter.println(sb5.toString());
        printWriter.println(str + "mSyncSeqId=" + this.mSyncSeqId);
        printWriter.println(str + "mSyncState=" + this.mSyncState);
        if (this.mWallpaperXStep != -1.0f || this.mWallpaperYStep != -1.0f) {
            printWriter.println(str + "mWallpaperXStep=" + this.mWallpaperXStep + " mWallpaperYStep=" + this.mWallpaperYStep);
        }
        if (this.mWallpaperZoomOut != -1.0f) {
            printWriter.println(str + "mWallpaperZoomOut=" + this.mWallpaperZoomOut);
        }
        if (this.mWallpaperDisplayOffsetX != Integer.MIN_VALUE || this.mWallpaperDisplayOffsetY != Integer.MIN_VALUE) {
            printWriter.println(str + "mWallpaperDisplayOffsetX=" + this.mWallpaperDisplayOffsetX + " mWallpaperDisplayOffsetY=" + this.mWallpaperDisplayOffsetY);
        }
        if (this.mDrawLock != null) {
            printWriter.println(str + "mDrawLock=" + this.mDrawLock);
        }
        if (isDragResizing()) {
            printWriter.println(str + "isDragResizing=" + isDragResizing());
        }
        if (computeDragResizing()) {
            printWriter.println(str + "computeDragResizing=" + computeDragResizing());
        }
        printWriter.println(str + "isOnScreen=" + isOnScreen());
        printWriter.println(str + "isVisible=" + isVisible());
        printWriter.println(str + "keepClearAreas: restricted=" + this.mKeepClearAreas + ", unrestricted=" + this.mUnrestrictedKeepClearAreas);
        if (z && this.mRequestedVisibleTypes != WindowInsets.Type.defaultVisible()) {
            printWriter.println(str + "Requested non-default-visibility types: " + WindowInsets.Type.toString(this.mRequestedVisibleTypes ^ WindowInsets.Type.defaultVisible()));
        }
        printWriter.println(str + "mPrepareSyncSeqId=" + this.mPrepareSyncSeqId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.ConfigurationContainer
    public String getName() {
        return Integer.toHexString(System.identityHashCode(this)) + " " + ((Object) getWindowTag());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence getWindowTag() {
        CharSequence title = this.mAttrs.getTitle();
        return (title == null || title.length() <= 0) ? this.mAttrs.packageName : title;
    }

    public String toString() {
        CharSequence windowTag = getWindowTag();
        if (this.mStringNameCache == null || this.mLastTitle != windowTag || this.mWasExiting != this.mAnimatingExit) {
            this.mLastTitle = windowTag;
            this.mWasExiting = this.mAnimatingExit;
            StringBuilder sb = new StringBuilder();
            sb.append("Window{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" u");
            sb.append(this.mShowUserId);
            sb.append(" ");
            sb.append((Object) this.mLastTitle);
            sb.append(this.mAnimatingExit ? " EXITING}" : "}");
            this.mStringNameCache = sb.toString();
        }
        return this.mStringNameCache;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isChildWindow() {
        return this.mIsChildWindow;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hideNonSystemOverlayWindowsWhenVisible() {
        return (this.mAttrs.privateFlags & 524288) != 0 && this.mSession.mCanHideNonSystemOverlayWindows;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState getParentWindow() {
        if (this.mIsChildWindow) {
            return (WindowState) super.getParent();
        }
        return null;
    }

    WindowState getTopParentWindow() {
        WindowState windowState;
        loop0: while (true) {
            windowState = this;
            while (this != null && this.mIsChildWindow) {
                this = this.getParentWindow();
                if (this != null) {
                    break;
                }
            }
        }
        return windowState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isParentWindowHidden() {
        WindowState parentWindow = getParentWindow();
        return parentWindow != null && parentWindow.mHidden;
    }

    private boolean isParentWindowGoneForLayout() {
        WindowState parentWindow = getParentWindow();
        return parentWindow != null && parentWindow.isGoneForLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestUpdateWallpaperIfNeeded() {
        DisplayContent displayContent = getDisplayContent();
        if (displayContent != null && ((this.mIsWallpaper && !this.mLastConfigReportedToClient) || hasWallpaper())) {
            displayContent.pendingLayoutChanges |= 4;
            displayContent.setLayoutNeeded();
            this.mWmService.mWindowPlacerLocked.requestTraversal();
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            ((WindowState) this.mChildren.get(size)).requestUpdateWallpaperIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float translateToWindowX(float f) {
        float f2 = f - this.mWindowFrames.mFrame.left;
        return this.mGlobalScale != 1.0f ? f2 * this.mInvGlobalScale : f2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float translateToWindowY(float f) {
        float f2 = f - this.mWindowFrames.mFrame.top;
        return this.mGlobalScale != 1.0f ? f2 * this.mInvGlobalScale : f2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRotationAnimationHint() {
        ActivityRecord activityRecord = this.mActivityRecord;
        if (activityRecord != null) {
            return activityRecord.mRotationAnimationHint;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean commitFinishDrawing(SurfaceControl.Transaction transaction) {
        boolean commitFinishDrawingLocked = this.mWinAnimator.commitFinishDrawingLocked();
        if (commitFinishDrawingLocked) {
            this.mWinAnimator.prepareSurfaceLocked(transaction);
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            commitFinishDrawingLocked |= ((WindowState) this.mChildren.get(size)).commitFinishDrawing(transaction);
        }
        return commitFinishDrawingLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean performShowLocked() {
        ActivityRecord activityRecord;
        ActivityRecord activityRecord2;
        if (!showToCurrentUser()) {
            Slog.w(TAG, "hiding " + this + ", belonging to " + this.mOwnerUid);
            clearPolicyVisibilityFlag(2);
            return false;
        }
        logPerformShow("performShow on ");
        int i = this.mWinAnimator.mDrawState;
        if ((i == 4 || i == 3) && this.mActivityRecord != null) {
            if (this.mAttrs.type != 3 && !this.mWindowStateExt.shouldDeferCallOnFirstWindowDrawn(this)) {
                this.mActivityRecord.onFirstWindowDrawn(this);
            } else if (this.mAttrs.type == 3) {
                this.mActivityRecord.onStartingWindowDrawn();
            } else {
                Slog.i(TAG, "defer call onFirstWindowDrawn until mainwindow drawn:" + this);
            }
        }
        if (this.mWinAnimator.mDrawState != 3 || !isReadyForDisplay()) {
            return false;
        }
        if (this.mAttrs.type == 3 && this.mToken.waitingToShow && getDisplayContent().mAppTransition.isTransitionSet() && (activityRecord2 = this.mActivityRecord) != null && activityRecord2.isEmbedded()) {
            return false;
        }
        if (this.mAttrs.type == 3 && this.mToken.waitingToShow && getDisplayContent().mAppTransition.isTransitionSet() && (activityRecord = this.mActivityRecord) != null && activityRecord.isEmbedded()) {
            return false;
        }
        logPerformShow("Showing ");
        this.mWmService.enableScreenIfNeededLocked();
        this.mWinAnimator.applyEnterAnimationLocked();
        this.mWinAnimator.mLastAlpha = -1.0f;
        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ANIM, -1288007399, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        this.mWindowStateWrapper.getExtImpl().cancelFlexibleAppInnerScreenAnimationIfNeed(this);
        this.mWindowStateExt.onWindowStateHasDrawn(this);
        WindowStateAnimator windowStateAnimator = this.mWinAnimator;
        windowStateAnimator.printWindowState(windowStateAnimator.mDrawState, 4, this, "performShowLocked");
        this.mWindowStateWrapper.getExtImpl().performShowLocked(this);
        this.mWinAnimator.mDrawState = 4;
        this.mWmService.scheduleAnimationLocked();
        if (this.mHidden) {
            this.mHidden = false;
            DisplayContent displayContent = getDisplayContent();
            for (int size = this.mChildren.size() - 1; size >= 0; size--) {
                WindowState windowState = (WindowState) this.mChildren.get(size);
                if (windowState.mWinAnimator.mSurfaceController != null) {
                    windowState.performShowLocked();
                    if (displayContent != null) {
                        displayContent.setLayoutNeeded();
                    }
                }
            }
        }
        if (this.mAttrs.type == 3) {
            this.mWindowStateExt.removeStartingBackColorLayerIfNeed(this);
        }
        return true;
    }

    private void logPerformShow(String str) {
        if (WindowManagerDebugConfig.DEBUG_VISIBILITY || (WindowManagerDebugConfig.DEBUG_STARTING_WINDOW_VERBOSE && this.mAttrs.type == 3)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(this);
            sb.append(": mDrawState=");
            sb.append(this.mWinAnimator.drawStateToString());
            sb.append(" readyForDisplay=");
            sb.append(isReadyForDisplay());
            sb.append(" starting=");
            sb.append(this.mAttrs.type == 3);
            sb.append(" during animation: policyVis=");
            sb.append(isVisibleByPolicy());
            sb.append(" parentHidden=");
            sb.append(isParentWindowHidden());
            sb.append(" tok.visibleRequested=");
            ActivityRecord activityRecord = this.mActivityRecord;
            sb.append(activityRecord != null && activityRecord.isVisibleRequested());
            sb.append(" tok.visible=");
            ActivityRecord activityRecord2 = this.mActivityRecord;
            sb.append(activityRecord2 != null && activityRecord2.isVisible());
            sb.append(" animating=");
            sb.append(isAnimating(3));
            sb.append(" tok animating=");
            ActivityRecord activityRecord3 = this.mActivityRecord;
            sb.append(activityRecord3 != null && activityRecord3.isAnimating(3));
            sb.append(" waitingToShow = ");
            sb.append(this.mToken.waitingToShow);
            sb.append(" isTransitionSet = ");
            sb.append(getDisplayContent().mAppTransition.isTransitionSet());
            sb.append(" Callers=");
            sb.append(Debug.getCallers(4));
            Slog.v(TAG, sb.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowInfo getWindowInfo() {
        WindowInfo obtain = WindowInfo.obtain();
        obtain.displayId = getDisplayId();
        obtain.type = this.mAttrs.type;
        obtain.layer = this.mLayer;
        obtain.token = this.mClient.asBinder();
        ActivityRecord activityRecord = this.mActivityRecord;
        if (activityRecord != null) {
            obtain.activityToken = activityRecord.token;
        }
        obtain.accessibilityIdOfAnchor = this.mAttrs.accessibilityIdOfAnchor;
        obtain.focused = isFocused();
        Task task = getTask();
        obtain.inPictureInPicture = task != null && task.inPinnedWindowingMode();
        obtain.taskId = task == null ? -1 : task.mTaskId;
        obtain.hasFlagWatchOutsideTouch = (this.mAttrs.flags & 262144) != 0;
        if (this.mIsChildWindow) {
            obtain.parentToken = getParentWindow().mClient.asBinder();
        }
        int size = this.mChildren.size();
        if (size > 0) {
            if (obtain.childTokens == null) {
                obtain.childTokens = new ArrayList(size);
            }
            for (int i = 0; i < size; i++) {
                obtain.childTokens.add(((WindowState) this.mChildren.get(i)).mClient.asBinder());
            }
        }
        return obtain;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean forAllWindows(ToBooleanFunction<WindowState> toBooleanFunction, boolean z) {
        if (this.mChildren.isEmpty()) {
            return applyInOrderWithImeWindows(toBooleanFunction, z);
        }
        if (z) {
            return forAllWindowTopToBottom(toBooleanFunction);
        }
        return forAllWindowBottomToTop(toBooleanFunction);
    }

    private boolean forAllWindowBottomToTop(ToBooleanFunction<WindowState> toBooleanFunction) {
        int size = this.mChildren.size();
        WindowState windowState = (WindowState) this.mChildren.get(0);
        int i = 0;
        while (i < size && windowState.mSubLayer < 0) {
            if (windowState.applyInOrderWithImeWindows(toBooleanFunction, false)) {
                return true;
            }
            i++;
            if (i >= size) {
                break;
            }
            windowState = (WindowState) this.mChildren.get(i);
        }
        if (applyInOrderWithImeWindows(toBooleanFunction, false)) {
            return true;
        }
        while (i < size) {
            if (windowState.applyInOrderWithImeWindows(toBooleanFunction, false)) {
                return true;
            }
            i++;
            if (i >= size) {
                break;
            }
            windowState = (WindowState) this.mChildren.get(i);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void updateAboveInsetsState(final InsetsState insetsState, SparseArray<InsetsSource> sparseArray, final ArraySet<WindowState> arraySet) {
        final SparseArray createMergedSparseArray = WindowContainer.createMergedSparseArray(sparseArray, this.mLocalInsetsSources);
        forAllWindows(new Consumer() { // from class: com.android.server.wm.WindowState$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WindowState.lambda$updateAboveInsetsState$3(insetsState, arraySet, createMergedSparseArray, (WindowState) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateAboveInsetsState$3(InsetsState insetsState, ArraySet arraySet, SparseArray sparseArray, WindowState windowState) {
        if (!windowState.mAboveInsetsState.equals(insetsState)) {
            windowState.mAboveInsetsState.set(insetsState);
            arraySet.add(windowState);
        }
        if (!sparseArray.contentEquals(windowState.mMergedLocalInsetsSources)) {
            windowState.mMergedLocalInsetsSources = sparseArray;
            arraySet.add(windowState);
        }
        SparseArray<InsetsSourceProvider> sparseArray2 = windowState.mInsetsSourceProviders;
        if (sparseArray2 != null) {
            for (int size = sparseArray2.size() - 1; size >= 0; size--) {
                insetsState.addSource(sparseArray2.valueAt(size).getSource());
            }
        }
    }

    private boolean forAllWindowTopToBottom(ToBooleanFunction<WindowState> toBooleanFunction) {
        int size = this.mChildren.size() - 1;
        WindowState windowState = (WindowState) this.mChildren.get(size);
        while (size >= 0 && windowState.mSubLayer >= 0) {
            if (windowState.applyInOrderWithImeWindows(toBooleanFunction, true)) {
                return true;
            }
            size--;
            if (size < 0) {
                break;
            }
            windowState = (WindowState) this.mChildren.get(size);
        }
        if (applyInOrderWithImeWindows(toBooleanFunction, true)) {
            return true;
        }
        while (size >= 0) {
            if (windowState.applyInOrderWithImeWindows(toBooleanFunction, true)) {
                return true;
            }
            size--;
            if (size < 0) {
                return false;
            }
            windowState = (WindowState) this.mChildren.get(size);
        }
        return false;
    }

    private boolean applyImeWindowsIfNeeded(ToBooleanFunction<WindowState> toBooleanFunction, boolean z) {
        if (!isImeLayeringTarget()) {
            return false;
        }
        WindowState imeInputTarget = getImeInputTarget();
        if (imeInputTarget == null || imeInputTarget.isDrawn() || imeInputTarget.isVisibleRequested()) {
            return this.mDisplayContent.forAllImeWindows(toBooleanFunction, z);
        }
        return false;
    }

    private boolean applyInOrderWithImeWindows(ToBooleanFunction<WindowState> toBooleanFunction, boolean z) {
        return z ? applyImeWindowsIfNeeded(toBooleanFunction, z) || toBooleanFunction.apply(this) : toBooleanFunction.apply(this) || applyImeWindowsIfNeeded(toBooleanFunction, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public WindowState getWindow(Predicate<WindowState> predicate) {
        if (this.mChildren.isEmpty()) {
            if (predicate.test(this)) {
                return this;
            }
            return null;
        }
        int size = this.mChildren.size() - 1;
        WindowState windowState = (WindowState) this.mChildren.get(size);
        while (size >= 0 && windowState.mSubLayer >= 0) {
            if (predicate.test(windowState)) {
                return windowState;
            }
            size--;
            if (size < 0) {
                break;
            }
            windowState = (WindowState) this.mChildren.get(size);
        }
        if (predicate.test(this)) {
            return this;
        }
        while (size >= 0) {
            if (predicate.test(windowState)) {
                return windowState;
            }
            size--;
            if (size < 0) {
                break;
            }
            windowState = (WindowState) this.mChildren.get(size);
        }
        return null;
    }

    @VisibleForTesting
    boolean isSelfOrAncestorWindowAnimatingExit() {
        while (!this.mAnimatingExit) {
            this = this.getParentWindow();
            if (this == null) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAnimationRunningSelfOrParent() {
        return inTransitionSelfOrParent() || isAnimating(0, 16);
    }

    private boolean shouldFinishAnimatingExit() {
        if (inTransition()) {
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -1145384901, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            return false;
        }
        if (!this.mDisplayContent.okToAnimate()) {
            return true;
        }
        if (isAnimationRunningSelfOrParent()) {
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -743856570, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            return false;
        }
        if (!this.mDisplayContent.mWallpaperController.isWallpaperTarget(this)) {
            return true;
        }
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -208825711, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanupAnimatingExitWindow() {
        if (this.mAnimatingExit && shouldFinishAnimatingExit()) {
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 1087494661, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            onExitAnimationDone();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onExitAnimationDone() {
        if (ProtoLogImpl.isEnabled(ProtoLogGroup.WM_DEBUG_ANIM)) {
            AnimationAdapter animation = this.mSurfaceAnimator.getAnimation();
            StringWriter stringWriter = new StringWriter();
            if (animation != null) {
                animation.dump(new PrintWriter(stringWriter), "");
            }
            if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ANIM, 1164325516, 252, (String) null, new Object[]{String.valueOf(this), Boolean.valueOf(this.mAnimatingExit), Boolean.valueOf(this.mRemoveOnExit), Boolean.valueOf(isAnimating()), String.valueOf(stringWriter)});
            }
        }
        if (!this.mChildren.isEmpty()) {
            ArrayList arrayList = new ArrayList(this.mChildren);
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                ((WindowState) arrayList.get(size)).onExitAnimationDone();
            }
        }
        WindowStateAnimator windowStateAnimator = this.mWinAnimator;
        if (windowStateAnimator.mEnteringAnimation) {
            windowStateAnimator.mEnteringAnimation = false;
            this.mWmService.requestTraversal();
            if (this.mActivityRecord == null) {
                try {
                    this.mClient.dispatchWindowShown();
                } catch (RemoteException unused) {
                }
            }
        }
        if (isAnimating()) {
            return;
        }
        this.mWindowStateExt.putSnapshotWhenStartingWindowExit(this.mAttrs.type, this.mRemoveOnExit, this);
        if (this.mWindowStateExt.getHideByKeyguardExitAnim()) {
            this.mWinAnimator.hide(getPendingTransaction(), "hideByKeyguardExitAnim");
        }
        if (this.mWindowStateExt.hideForUnFolded(this)) {
            this.mWinAnimator.hide(getPendingTransaction(), "hide ScreenRelayWindow for unfolded");
        }
        if (isSelfOrAncestorWindowAnimatingExit()) {
            if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1051545910, 12, (String) null, new Object[]{String.valueOf(this), Boolean.valueOf(this.mRemoveOnExit)});
            }
            this.mDestroying = true;
            boolean hasSurface = this.mWinAnimator.hasSurface();
            this.mWinAnimator.hide(getPendingTransaction(), "onExitAnimationDone");
            ActivityRecord activityRecord = this.mActivityRecord;
            if (activityRecord != null) {
                if (this.mAttrs.type == 1) {
                    activityRecord.destroySurfaces();
                } else {
                    destroySurface(false, activityRecord.mAppStopped);
                }
            } else if (hasSurface) {
                this.mWmService.mDestroySurface.add(this);
            }
            this.mAnimatingExit = false;
            this.mWindowStateExt.setHideByKeyguardExitAnim(false);
            if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ANIM, 283489582, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            getDisplayContent().mWallpaperController.hideWallpapers(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean handleCompleteDeferredRemoval() {
        if (this.mRemoveOnExit && !isSelfAnimating(0, 16)) {
            this.mRemoveOnExit = false;
            removeImmediately();
        }
        return super.handleCompleteDeferredRemoval();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean clearAnimatingFlags() {
        boolean z;
        boolean z2 = false;
        if (!this.mRemoveOnExit) {
            if (this.mAnimatingExit) {
                this.mAnimatingExit = false;
                this.mWindowStateExt.setHideByKeyguardExitAnim(false);
                if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ANIM, -1209252064, 0, (String) null, new Object[]{String.valueOf(this)});
                }
                z = true;
            } else {
                z = false;
            }
            if (this.mDestroying) {
                this.mDestroying = false;
                this.mWmService.mDestroySurface.remove(this);
                this.mWmsExt.getDestroySavedSurface().remove(this);
                z2 = true;
            } else {
                z2 = z;
            }
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            z2 |= ((WindowState) this.mChildren.get(size)).clearAnimatingFlags();
        }
        return z2;
    }

    public boolean isRtl() {
        return getConfiguration().getLayoutDirection() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateReportedVisibility(UpdateReportedVisibilityResults updateReportedVisibilityResults) {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            ((WindowState) this.mChildren.get(size)).updateReportedVisibility(updateReportedVisibilityResults);
        }
        if (this.mAppFreezing || this.mViewVisibility != 0 || this.mAttrs.type == 3 || this.mDestroying) {
            return;
        }
        if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(TAG, "Win " + this + ": isDrawn=" + isDrawn() + ", animating=" + isAnimating(3));
            if (!isDrawn()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Not displayed: s=");
                sb.append(this.mWinAnimator.mSurfaceController);
                sb.append(" pv=");
                sb.append(isVisibleByPolicy());
                sb.append(" mDrawState=");
                sb.append(this.mWinAnimator.mDrawState);
                sb.append(" ph=");
                sb.append(isParentWindowHidden());
                sb.append(" th=");
                ActivityRecord activityRecord = this.mActivityRecord;
                sb.append(activityRecord != null && activityRecord.isVisibleRequested());
                sb.append(" a=");
                sb.append(isAnimating(3));
                Slog.v(TAG, sb.toString());
            }
        }
        updateReportedVisibilityResults.numInteresting++;
        if (isDrawn()) {
            updateReportedVisibilityResults.numDrawn++;
            if (!isAnimating(3)) {
                updateReportedVisibilityResults.numVisible++;
            }
            updateReportedVisibilityResults.nowGone = false;
            return;
        }
        if (isAnimating(3)) {
            updateReportedVisibilityResults.nowGone = false;
        }
    }

    boolean surfaceInsetsChanging() {
        return !this.mLastSurfaceInsets.equals(this.mAttrs.surfaceInsets);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int relayoutVisibleWindow(int i) {
        boolean isVisible = isVisible();
        int i2 = i | ((isVisible && isDrawn()) ? 0 : 1);
        if (this.mAnimatingExit) {
            Slog.d(TAG, "relayoutVisibleWindow: " + this + " mAnimatingExit=true, mRemoveOnExit=" + this.mRemoveOnExit + ", mDestroying=" + this.mDestroying);
            if (isAnimating()) {
                cancelAnimation();
            }
            this.mAnimatingExit = false;
            this.mWindowStateExt.setHideByKeyguardExitAnim(false);
            if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ANIM, -1933723759, 0, (String) null, new Object[]{String.valueOf(this)});
            }
        }
        if (this.mDestroying) {
            this.mDestroying = false;
            this.mWmService.mDestroySurface.remove(this);
            this.mWmsExt.getDestroySavedSurface().remove(this);
        }
        if (!isVisible) {
            this.mWinAnimator.mEnterAnimationPending = true;
        }
        this.mLastVisibleLayoutRotation = getDisplayContent().getRotation();
        this.mWinAnimator.mEnteringAnimation = true;
        Trace.traceBegin(32L, "prepareToDisplay");
        try {
            prepareWindowToDisplayDuringRelayout(isVisible);
            return i2;
        } finally {
            Trace.traceEnd(32L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLaidOut() {
        return this.mLayoutSeq != -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateLastFrames() {
        WindowFrames windowFrames = this.mWindowFrames;
        windowFrames.mLastFrame.set(windowFrames.mFrame);
        WindowFrames windowFrames2 = this.mWindowFrames;
        windowFrames2.mLastRelFrame.set(windowFrames2.mRelFrame);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onResizeHandled() {
        this.mWindowFrames.onResizeHandled();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public boolean isSelfAnimating(int i, int i2) {
        if (this.mControllableInsetProvider != null) {
            return false;
        }
        return super.isSelfAnimating(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startAnimation(Animation animation) {
        if (this.mControllableInsetProvider != null) {
            return;
        }
        DisplayInfo displayInfo = getDisplayInfo();
        animation.initialize(this.mWindowFrames.mFrame.width(), this.mWindowFrames.mFrame.height(), displayInfo.appWidth, displayInfo.appHeight);
        animation.restrictDuration(10000L);
        animation.scaleCurrentDuration(this.mWmService.getWindowAnimationScaleLocked());
        if (this.mWindowStateExt.startAnimationWithRoundedCorners(this, animation, this.mSurfacePosition, this.mWindowFrames.mFrame)) {
            return;
        }
        startAnimation(getPendingTransaction(), new LocalAnimationAdapter(new WindowAnimationSpec(animation, this.mSurfacePosition, false, 0.0f), this.mWmService.mSurfaceAnimationRunner));
        commitPendingTransaction();
    }

    private void startMoveAnimation(int i, int i2) {
        if (this.mControllableInsetProvider != null) {
            return;
        }
        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ANIM, -347866078, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        Point point = new Point();
        Point point2 = new Point();
        Rect rect = this.mWindowFrames.mLastFrame;
        transformFrameToSurfacePosition(rect.left, rect.top, point);
        transformFrameToSurfacePosition(i, i2, point2);
        LocalAnimationAdapter localAnimationAdapter = new LocalAnimationAdapter(new MoveAnimationSpec(point.x, point.y, point2.x, point2.y), this.mWmService.mSurfaceAnimationRunner);
        if (this.mWindowStateExt.shouldBlockWindowMoveAnimation(this)) {
            return;
        }
        startAnimation(getPendingTransaction(), localAnimationAdapter);
    }

    private void startAnimation(SurfaceControl.Transaction transaction, AnimationAdapter animationAdapter) {
        startAnimation(transaction, animationAdapter, this.mWinAnimator.mLastHidden, 16);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public void onAnimationFinished(int i, AnimationAdapter animationAdapter) {
        super.onAnimationFinished(i, animationAdapter);
        this.mWinAnimator.onAnimationFinished();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getTransformationMatrix(float[] fArr, Matrix matrix) {
        float f = this.mGlobalScale;
        fArr[0] = f;
        fArr[3] = 0.0f;
        fArr[1] = 0.0f;
        fArr[4] = f;
        transformSurfaceInsetsPosition(this.mTmpPoint, this.mAttrs.surfaceInsets);
        Point point = this.mSurfacePosition;
        int i = point.x;
        Point point2 = this.mTmpPoint;
        int i2 = i + point2.x;
        int i3 = point.y + point2.y;
        WindowContainer parent = getParent();
        if (isChildWindow()) {
            WindowState parentWindow = getParentWindow();
            Rect rect = parentWindow.mWindowFrames.mFrame;
            int i4 = rect.left;
            Rect rect2 = parentWindow.mAttrs.surfaceInsets;
            i2 += i4 - rect2.left;
            i3 += rect.top - rect2.top;
        } else if (parent != null) {
            Rect bounds = parent.getBounds();
            i2 += bounds.left;
            i3 += bounds.top;
        }
        fArr[2] = i2;
        fArr[5] = i3;
        fArr[6] = 0.0f;
        fArr[7] = 0.0f;
        fArr[8] = 1.0f;
        matrix.setValues(fArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class UpdateReportedVisibilityResults {
        boolean nowGone = true;
        int numDrawn;
        int numInteresting;
        int numVisible;

        /* JADX INFO: Access modifiers changed from: package-private */
        public void reset() {
            this.numInteresting = 0;
            this.numVisible = 0;
            this.numDrawn = 0;
            this.nowGone = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class WindowId extends IWindowId.Stub {
        private final WeakReference<WindowState> mOuter;

        private WindowId(WindowState windowState) {
            this.mOuter = new WeakReference<>(windowState);
        }

        public void registerFocusObserver(IWindowFocusObserver iWindowFocusObserver) {
            WindowState windowState = this.mOuter.get();
            if (windowState != null) {
                windowState.registerFocusObserver(iWindowFocusObserver);
            }
        }

        public void unregisterFocusObserver(IWindowFocusObserver iWindowFocusObserver) {
            WindowState windowState = this.mOuter.get();
            if (windowState != null) {
                windowState.unregisterFocusObserver(iWindowFocusObserver);
            }
        }

        public boolean isFocused() {
            boolean isFocused;
            WindowState windowState = this.mOuter.get();
            if (windowState == null) {
                return false;
            }
            WindowManagerGlobalLock windowManagerGlobalLock = windowState.mWmService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    isFocused = windowState.isFocused();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return isFocused;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean shouldMagnify() {
        WindowManager.LayoutParams layoutParams = this.mAttrs;
        int i = layoutParams.type;
        return (i == 2039 || i == 2011 || i == 2012 || i == 2027 || i == 2019 || i == 2024 || (layoutParams.privateFlags & 4194304) != 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public SurfaceSession getSession() {
        SurfaceSession surfaceSession = this.mSession.mSurfaceSession;
        return surfaceSession != null ? surfaceSession : getParent().getSession();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean needsZBoost() {
        ActivityRecord activityRecord;
        InsetsControlTarget imeTarget = getDisplayContent().getImeTarget(0);
        if (!this.mIsImWindow || imeTarget == null || (activityRecord = imeTarget.getWindow().mActivityRecord) == null) {
            return false;
        }
        return activityRecord.needsZBoost();
    }

    private boolean isStartingWindowAssociatedToTask() {
        StartingData startingData = this.mStartingData;
        return (startingData == null || startingData.mAssociatedTask == null) ? false : true;
    }

    private void applyDims() {
        if (((this.mAttrs.flags & 2) != 0 || shouldDrawBlurBehind()) && this.mToken.isVisibleRequested() && isVisibleNow() && !this.mHidden && this.mTransitionController.canApplyDim(getTask())) {
            this.mIsDimming = true;
            float f = 0.0f;
            if (!this.mWindowStateExt.excludeDimmerForComponent(getActivityRecord())) {
                WindowManager.LayoutParams layoutParams = this.mAttrs;
                if ((layoutParams.flags & 2) != 0) {
                    f = layoutParams.dimAmount;
                }
            }
            int blurBehindRadius = shouldDrawBlurBehind() ? this.mAttrs.getBlurBehindRadius() : 0;
            Dimmer dimmer = getDimmer();
            if (dimmer != null) {
                dimmer.dimBelow(this, f, blurBehindRadius);
            }
        }
    }

    private boolean shouldDrawBlurBehind() {
        return (this.mAttrs.flags & 4) != 0 && this.mWmService.mBlurController.getBlurEnabled();
    }

    @VisibleForTesting
    void updateFrameRateSelectionPriorityIfNeeded() {
        RefreshRatePolicy refreshRatePolicy = getDisplayContent().getDisplayPolicy().getRefreshRatePolicy();
        int calculatePriority = refreshRatePolicy.calculatePriority(this);
        if (this.mFrameRateSelectionPriority != calculatePriority) {
            this.mFrameRateSelectionPriority = calculatePriority;
            getPendingTransaction().setFrameRateSelectionPriority(this.mSurfaceControl, this.mFrameRateSelectionPriority);
        }
        if (refreshRatePolicy.updateFrameRateVote(this)) {
            SurfaceControl.Transaction pendingTransaction = getPendingTransaction();
            SurfaceControl surfaceControl = this.mSurfaceControl;
            RefreshRatePolicy.FrameRateVote frameRateVote = this.mFrameRateVote;
            pendingTransaction.setFrameRate(surfaceControl, frameRateVote.mRefreshRate, frameRateVote.mCompatibility, 1);
        }
    }

    private void updateScaleIfNeeded() {
        if (isVisibleRequested() || (this.mIsWallpaper && this.mToken.isVisible())) {
            this.mWindowStateExt.expandFingerPrintDimLayerSurface(this, this.mWmService.mDisplayFrozen);
            float f = this.mGlobalScale;
            WindowState parentWindow = getParentWindow();
            if (parentWindow != null) {
                f *= parentWindow.mInvGlobalScale;
            }
            float f2 = this.mHScale * f;
            float f3 = this.mWallpaperScale;
            float f4 = f2 * f3;
            float f5 = this.mVScale * f * f3;
            if (this.mLastHScale == f4 && this.mLastVScale == f5) {
                return;
            }
            getSyncTransaction().setMatrix(this.mSurfaceControl, f4, 0.0f, 0.0f, f5);
            this.mLastHScale = f4;
            this.mLastVScale = f5;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void prepareSurfaces() {
        this.mIsDimming = false;
        if (this.mHasSurface) {
            if (this.mWindowStateExt.prepareSurfaces(this)) {
                this.mIsDimming = this.mWindowStateExt.toUpdateCompactDimmer(this);
            } else {
                this.mIsDimming = false;
                applyDims();
            }
            updateSurfacePositionNonOrganized();
            updateFrameRateSelectionPriorityIfNeeded();
            updateScaleIfNeeded();
            this.mWinAnimator.prepareSurfaceLocked(getSyncTransaction());
        }
        super.prepareSurfaces();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    @VisibleForTesting
    public void updateSurfacePosition(SurfaceControl.Transaction transaction) {
        if (this.mSurfaceControl == null) {
            return;
        }
        if ((this.mWmService.mWindowPlacerLocked.isLayoutDeferred() || isGoneForLayout()) && !this.mSurfacePlacementNeeded) {
            return;
        }
        boolean z = false;
        this.mSurfacePlacementNeeded = false;
        if (this.mWindowStateExt.isIgnoreImeTargetBottomOverlapFlexibleTask(this)) {
            transformFrameToSurfacePosition(getBounds().left, getBounds().top, this.mSurfacePosition);
        } else {
            Rect rect = this.mWindowFrames.mFrame;
            transformFrameToSurfacePosition(rect.left, rect.top, this.mSurfacePosition);
        }
        if (this.mWallpaperScale != 1.0f) {
            Rect parentFrame = getParentFrame();
            Matrix matrix = this.mTmpMatrix;
            matrix.setTranslate(this.mXOffset, this.mYOffset);
            float f = this.mWallpaperScale;
            matrix.postScale(f, f, parentFrame.exactCenterX(), parentFrame.exactCenterY());
            matrix.getValues(this.mTmpMatrixArray);
            this.mSurfacePosition.offset(Math.round(this.mTmpMatrixArray[2]), Math.round(this.mTmpMatrixArray[5]));
        } else {
            this.mSurfacePosition.offset(this.mXOffset, this.mYOffset);
        }
        if (this.mSurfaceAnimator.hasLeash() || this.mPendingSeamlessRotate != null || this.mLastSurfacePosition.equals(this.mSurfacePosition)) {
            return;
        }
        boolean z2 = this.mWindowFrames.isFrameSizeChangeReported() && !this.mWindowStateExt.shouldUpdateWinPos(this.mWindowFrames);
        boolean surfaceInsetsChanging = surfaceInsetsChanging();
        boolean z3 = z2 || surfaceInsetsChanging;
        Point point = this.mLastSurfacePosition;
        Point point2 = this.mSurfacePosition;
        point.set(point2.x, point2.y);
        if (surfaceInsetsChanging) {
            this.mLastSurfaceInsets.set(this.mAttrs.surfaceInsets);
        }
        boolean z4 = z3 && this.mWinAnimator.getShown() && !canPlayMoveAnimation() && okToDisplay() && this.mSyncState == 0;
        ActivityRecord activityRecord = getActivityRecord();
        if (activityRecord != null && activityRecord.areBoundsLetterboxed() && activityRecord.mLetterboxUiController.getIsRelaunchingAfterRequestedOrientationChanged()) {
            z = true;
        }
        if (z4 || z) {
            applyWithNextDraw(this.mSetSurfacePositionConsumer);
        } else {
            this.mSetSurfacePositionConsumer.accept(transaction);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void transformFrameToSurfacePosition(int i, int i2, Point point) {
        Rect bounds;
        point.set(i, i2);
        WindowContainer parent = getParent();
        if (isChildWindow()) {
            WindowState parentWindow = getParentWindow();
            Rect rect = parentWindow.mWindowFrames.mFrame;
            point.offset(-rect.left, -rect.top);
            float f = this.mInvGlobalScale;
            if (f != 1.0f) {
                point.x = (int) ((point.x * f) + 0.5f);
                point.y = (int) ((point.y * f) + 0.5f);
            }
            transformSurfaceInsetsPosition(this.mTmpPoint, parentWindow.mAttrs.surfaceInsets);
            Point point2 = this.mTmpPoint;
            point.offset(point2.x, point2.y);
        } else if (parent != null) {
            if (isStartingWindowAssociatedToTask()) {
                bounds = this.mStartingData.mAssociatedTask.getBounds();
            } else {
                bounds = parent.getBounds();
            }
            this.mWindowStateExt.changeStartingWindowParentBounds(this, bounds);
            point.offset(-bounds.left, -bounds.top);
        }
        transformSurfaceInsetsPosition(this.mTmpPoint, this.mAttrs.surfaceInsets);
        Point point3 = this.mTmpPoint;
        point.offset(-point3.x, -point3.y);
        point.y += this.mSurfaceTranslationY;
    }

    private void transformSurfaceInsetsPosition(Point point, Rect rect) {
        float f = this.mGlobalScale;
        if (f == 1.0f || this.mIsChildWindow) {
            point.x = rect.left;
            point.y = rect.top;
        } else {
            point.x = (int) ((rect.left * f) + 0.5f);
            point.y = (int) ((rect.top * f) + 0.5f);
        }
    }

    boolean needsRelativeLayeringToIme() {
        WindowState imeLayeringTarget;
        if (this.mDisplayContent.shouldImeAttachedToApp() || !getDisplayContent().getImeContainer().isVisible()) {
            return false;
        }
        if (isChildWindow()) {
            if (getParentWindow().isImeLayeringTarget()) {
                return true;
            }
        } else if (this.mActivityRecord != null) {
            WindowState imeLayeringTarget2 = getImeLayeringTarget();
            return (imeLayeringTarget2 == null || imeLayeringTarget2 == this || imeLayeringTarget2.mToken != this.mToken || this.mAttrs.type == 3 || getParent() == null || imeLayeringTarget2.compareTo((WindowContainer) this) > 0) ? false : true;
        }
        return (this.mAttrs.flags & 131080) == 131072 && isTrustedOverlay() && canAddInternalSystemWindow() && this.mWindowStateExt.shouldRelativeLayerInSplitScreenMode(this) && (imeLayeringTarget = getImeLayeringTarget()) != null && imeLayeringTarget != this && imeLayeringTarget.compareTo((WindowContainer) this) <= 0;
    }

    @Override // com.android.server.wm.InputTarget
    public InsetsControlTarget getImeControlTarget() {
        return getDisplayContent().getImeHostOrFallback(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void assignLayer(SurfaceControl.Transaction transaction, int i) {
        if (this.mStartingData != null) {
            transaction.setLayer(this.mSurfaceControl, Integer.MAX_VALUE);
        } else if (needsRelativeLayeringToIme()) {
            getDisplayContent().assignRelativeLayerForImeTargetChild(getDisplayContent().getPendingTransaction(), this);
        } else {
            super.assignLayer(transaction, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDimming() {
        return this.mIsDimming;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public void reparentSurfaceControl(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        if (isStartingWindowAssociatedToTask()) {
            return;
        }
        super.reparentSurfaceControl(transaction, surfaceControl);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public SurfaceControl getAnimationLeashParent() {
        if (isStartingWindowAssociatedToTask()) {
            return this.mStartingData.mAssociatedTask.mSurfaceControl;
        }
        return super.getAnimationLeashParent();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public void onAnimationLeashCreated(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        super.onAnimationLeashCreated(transaction, surfaceControl);
        if (isStartingWindowAssociatedToTask()) {
            transaction.setLayer(surfaceControl, Integer.MAX_VALUE);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    public void assignChildLayers(SurfaceControl.Transaction transaction) {
        int i = 2;
        for (int i2 = 0; i2 < this.mChildren.size(); i2++) {
            WindowState windowState = (WindowState) this.mChildren.get(i2);
            int i3 = windowState.mAttrs.type;
            if (i3 == 1001) {
                if (this.mWinAnimator.hasSurface()) {
                    windowState.assignRelativeLayer(transaction, this.mWinAnimator.mSurfaceController.mSurfaceControl, -2);
                } else {
                    windowState.assignLayer(transaction, -2);
                }
            } else if (i3 == 1004) {
                if (this.mWinAnimator.hasSurface()) {
                    windowState.assignRelativeLayer(transaction, this.mWinAnimator.mSurfaceController.mSurfaceControl, -1);
                } else {
                    windowState.assignLayer(transaction, -1);
                }
            } else {
                windowState.assignLayer(transaction, i);
            }
            windowState.assignChildLayers(transaction);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateTapExcludeRegion(Region region) {
        DisplayContent displayContent = getDisplayContent();
        if (displayContent == null) {
            throw new IllegalStateException("Trying to update window not attached to any display.");
        }
        if (region == null || region.isEmpty()) {
            this.mTapExcludeRegion.setEmpty();
            displayContent.mTapExcludeProvidingWindows.remove(this);
        } else {
            this.mTapExcludeRegion.set(region);
            displayContent.mTapExcludeProvidingWindows.add(this);
        }
        displayContent.updateTouchExcludeRegion();
        displayContent.getInputMonitor().updateInputWindowsLw(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getTapExcludeRegion(Region region) {
        this.mTmpRect.set(this.mWindowFrames.mFrame);
        this.mTmpRect.offsetTo(0, 0);
        region.set(this.mTapExcludeRegion);
        region.op(this.mTmpRect, Region.Op.INTERSECT);
        Rect rect = this.mWindowFrames.mFrame;
        region.translate(rect.left, rect.top);
    }

    boolean hasTapExcludeRegion() {
        return !this.mTapExcludeRegion.isEmpty();
    }

    boolean isImeLayeringTarget() {
        return getDisplayContent().getImeTarget(0) == this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isImeOverlayLayeringTarget() {
        return isImeLayeringTarget() && (this.mAttrs.flags & 131080) != 0;
    }

    WindowState getImeLayeringTarget() {
        InsetsControlTarget imeTarget = getDisplayContent().getImeTarget(0);
        if (imeTarget != null) {
            return imeTarget.getWindow();
        }
        return null;
    }

    WindowState getImeInputTarget() {
        InputTarget imeInputTarget = this.mDisplayContent.getImeInputTarget();
        if (imeInputTarget != null) {
            return imeInputTarget.getWindowState();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forceReportingResized() {
        this.mWindowFrames.forceReportingResized();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowFrames getWindowFrames() {
        return this.mWindowFrames;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetContentChanged() {
        this.mWindowFrames.setContentChanged(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public final class MoveAnimationSpec implements LocalAnimationAdapter.AnimationSpec {
        private final long mDuration;
        private Point mFrom;
        private Interpolator mInterpolator;
        private Point mTo;

        private MoveAnimationSpec(int i, int i2, int i3, int i4) {
            this.mFrom = new Point();
            this.mTo = new Point();
            Animation loadAnimation = AnimationUtils.loadAnimation(WindowState.this.mContext, R.anim.wallpaper_open_exit);
            this.mDuration = ((float) loadAnimation.computeDurationHint()) * WindowState.this.mWmService.getWindowAnimationScaleLocked();
            this.mInterpolator = loadAnimation.getInterpolator();
            this.mFrom.set(i, i2);
            this.mTo.set(i3, i4);
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public long getDuration() {
            return this.mDuration;
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void apply(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, long j) {
            float interpolation = this.mInterpolator.getInterpolation(getFraction((float) j));
            Point point = this.mFrom;
            int i = point.x;
            Point point2 = this.mTo;
            transaction.setPosition(surfaceControl, i + ((point2.x - i) * interpolation), point.y + ((point2.y - r7) * interpolation));
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void dump(PrintWriter printWriter, String str) {
            printWriter.println(str + "from=" + this.mFrom + " to=" + this.mTo + " duration=" + this.mDuration);
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void dumpDebugInner(ProtoOutputStream protoOutputStream) {
            long start = protoOutputStream.start(1146756268034L);
            GraphicsProtos.dumpPointProto(this.mFrom, protoOutputStream, 1146756268033L);
            GraphicsProtos.dumpPointProto(this.mTo, protoOutputStream, 1146756268034L);
            protoOutputStream.write(1112396529667L, this.mDuration);
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KeyInterceptionInfo getKeyInterceptionInfo() {
        KeyInterceptionInfo keyInterceptionInfo = this.mKeyInterceptionInfo;
        if (keyInterceptionInfo == null || keyInterceptionInfo.layoutParamsPrivateFlags != getAttrs().privateFlags || this.mKeyInterceptionInfo.layoutParamsType != getAttrs().type || this.mKeyInterceptionInfo.windowTitle != getWindowTag()) {
            this.mKeyInterceptionInfo = new KeyInterceptionInfo(getAttrs().type, getAttrs().privateFlags, getWindowTag().toString());
        }
        return this.mKeyInterceptionInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void getAnimationFrames(Rect rect, Rect rect2, Rect rect3, Rect rect4) {
        if (inFreeformWindowingMode()) {
            rect.set(getFrame());
        } else if (areAppWindowBoundsLetterboxed() || this.mToken.isFixedRotationTransforming()) {
            rect.set(getTask().getBounds());
        } else {
            rect.set(getParentFrame());
        }
        rect4.set(getAttrs().surfaceInsets);
        InsetsState insetsStateWithVisibilityOverride = getInsetsStateWithVisibilityOverride();
        rect2.set(insetsStateWithVisibilityOverride.calculateInsets(rect, WindowInsets.Type.systemBars(), false).toRect());
        rect3.set(insetsStateWithVisibilityOverride.calculateInsets(rect, WindowInsets.Type.systemBars(), true).toRect());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setViewVisibility(int i) {
        this.mViewVisibility = i;
    }

    SurfaceControl getClientViewRootSurface() {
        return this.mWinAnimator.getSurfaceControl();
    }

    private void dropBufferFrom(SurfaceControl.Transaction transaction) {
        SurfaceControl clientViewRootSurface = getClientViewRootSurface();
        if (clientViewRootSurface == null) {
            return;
        }
        transaction.unsetBuffer(clientViewRootSurface);
    }

    @Override // com.android.server.wm.WindowContainer
    protected boolean shouldUpdateSyncOnReparent() {
        return (this.mSyncState == 0 || this.mLastConfigReportedToClient) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean prepareSync() {
        int i;
        if (!this.mDrawHandlers.isEmpty()) {
            Slog.w(TAG, "prepareSync with mDrawHandlers, " + this + ", " + Debug.getCallers(8));
        }
        if (!super.prepareSync() || this.mIsWallpaper) {
            return false;
        }
        if (this.mActivityRecord != null && this.mViewVisibility != 0 && (i = this.mWinAnimator.mAttrType) != 1 && i != 3) {
            return false;
        }
        this.mSyncState = 1;
        if (this.mPrepareSyncSeqId > 0) {
            if (ProtoLogCache.WM_DEBUG_SYNC_ENGINE_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -417730399, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            dropBufferFrom(this.mSyncTransaction);
        }
        this.mSyncSeqId++;
        if (getSyncMethod() == 1) {
            this.mPrepareSyncSeqId = this.mSyncSeqId;
            requestRedrawForSync();
        } else if (this.mHasSurface && this.mWinAnimator.mDrawState != 1) {
            requestRedrawForSync();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean isSyncFinished(BLASTSyncEngine.SyncGroup syncGroup) {
        if (!isVisibleRequested() || isFullyTransparent()) {
            return true;
        }
        if (this.mWindowStateExt.isSyncFinished(this, this.mSyncState, this.mWmsExt, this.mWmService.mDestroySurface)) {
            Slog.i(TAG, "window surface saved sync finished");
            return true;
        }
        if (this.mSyncState == 1 && this.mWinAnimator.mDrawState == 4 && !this.mRedrawForSyncReported && !this.mWmService.mResizingWindows.contains(this)) {
            onSyncFinishedDrawing();
        }
        return super.isSyncFinished(syncGroup);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void finishSync(SurfaceControl.Transaction transaction, BLASTSyncEngine.SyncGroup syncGroup, boolean z) {
        BLASTSyncEngine.SyncGroup syncGroup2 = getSyncGroup();
        if (syncGroup2 == null || syncGroup == syncGroup2) {
            this.mPrepareSyncSeqId = 0;
            if (z) {
                dropBufferFrom(this.mSyncTransaction);
            }
            super.finishSync(transaction, syncGroup, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean finishDrawing(SurfaceControl.Transaction transaction, int i) {
        boolean z;
        boolean z2;
        SurfaceControl.Transaction transaction2 = transaction;
        if (this.mOrientationChangeRedrawRequestTime > 0) {
            Slog.i(TAG, "finishDrawing of orientation change: " + this + " " + (SystemClock.elapsedRealtime() - this.mOrientationChangeRedrawRequestTime) + "ms");
            this.mOrientationChangeRedrawRequestTime = 0L;
        } else {
            ActivityRecord activityRecord = this.mActivityRecord;
            if (activityRecord != null && activityRecord.mRelaunchStartTime != 0 && activityRecord.findMainWindow(false) == this) {
                Slog.i(TAG, "finishDrawing of relaunch: " + this + " " + (SystemClock.elapsedRealtime() - this.mActivityRecord.mRelaunchStartTime) + "ms");
                this.mActivityRecord.finishOrAbortReplacingWindow();
            }
        }
        if (this.mActivityRecord != null && this.mAttrs.type == 3) {
            this.mWmService.mAtmService.mTaskSupervisor.getActivityMetricsLogger().notifyStartingWindowDrawn(this.mActivityRecord);
        }
        int i2 = this.mPrepareSyncSeqId;
        boolean z3 = i2 > 0;
        boolean z4 = z3 && i2 > i;
        SurfaceControl.Transaction transaction3 = null;
        if (z4 && transaction2 != null) {
            if (ProtoLogCache.WM_DEBUG_SYNC_ENGINE_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -4263657, 5, (String) null, new Object[]{Long.valueOf(i), Long.valueOf(i2), String.valueOf(this)});
            }
            dropBufferFrom(transaction);
        }
        boolean executeDrawHandlers = executeDrawHandlers(transaction, i);
        if (this.mWindowStateExt.finishDrawingApplyPostDraw(this, transaction2)) {
            transaction2 = null;
        }
        AsyncRotationController asyncRotationController = this.mDisplayContent.getAsyncRotationController();
        if (asyncRotationController == null || !asyncRotationController.handleFinishDrawing(this, transaction2)) {
            if (z3) {
                if (z4) {
                    z2 = false;
                } else {
                    z2 = onSyncFinishedDrawing();
                    Slog.d(TAG, "not syncStillPending, layoutNeeded:" + z2);
                }
                if (transaction2 != null) {
                    Slog.d(TAG, "postDrawTransaction merge to syncTransaction:" + z2);
                    this.mSyncTransaction.merge(transaction2);
                    z = false;
                }
            } else if (useBLASTSync()) {
                z2 = onSyncFinishedDrawing();
                Slog.i(TAG, "finishDrawing skipLayout:false " + this + " " + Debug.getCallers(3));
            } else {
                transaction3 = transaction2;
                z = false;
                z2 = false;
            }
            transaction3 = transaction2;
            z = false;
        } else {
            Slog.d(TAG, "asyncRotationController handleFinishDrawing");
            z2 = false;
            z = true;
        }
        boolean finishDrawingLocked = z2 | this.mWinAnimator.finishDrawingLocked(transaction3);
        if (this.mWindowStateExt.finishDrawing(z)) {
            return false;
        }
        return executeDrawHandlers || finishDrawingLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void immediatelyNotifyBlastSync() {
        finishDrawing(null, Integer.MAX_VALUE);
        this.mWmService.mH.removeMessages(64, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean fillsParent() {
        return this.mAttrs.type == 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean showWallpaper() {
        if (!isVisibleRequested() || inMultiWindowMode()) {
            return false;
        }
        return hasWallpaper();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasWallpaper() {
        return (this.mAttrs.flags & 1048576) != 0 || hasWallpaperForLetterboxBackground();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasWallpaperForLetterboxBackground() {
        ActivityRecord activityRecord = this.mActivityRecord;
        return activityRecord != null && activityRecord.hasWallpaperBackgroundForLetterbox();
    }

    private boolean shouldSendRedrawForSync() {
        if (this.mRedrawForSyncReported) {
            return false;
        }
        if (!this.mInRelayout || (this.mPrepareSyncSeqId <= 0 && !(this.mViewVisibility == 0 && this.mWinAnimator.mDrawState == 1))) {
            return useBLASTSync();
        }
        return false;
    }

    int getSyncMethod() {
        BLASTSyncEngine.SyncGroup syncGroup = getSyncGroup();
        if (syncGroup == null) {
            return 0;
        }
        int i = this.mSyncMethodOverride;
        return i != -1 ? i : syncGroup.mSyncMethod;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldSyncWithBuffers() {
        return !this.mDrawHandlers.isEmpty() || getSyncMethod() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestRedrawForSync() {
        this.mRedrawForSyncReported = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean useBLASTSync() {
        return super.useBLASTSync() || this.mDrawHandlers.size() != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyWithNextDraw(Consumer<SurfaceControl.Transaction> consumer) {
        if (this.mSyncState != 0) {
            Slog.w(TAG, "applyWithNextDraw with mSyncState=" + this.mSyncState + ", " + this + ", " + Debug.getCallers(8));
        }
        int i = this.mSyncSeqId + 1;
        this.mSyncSeqId = i;
        this.mDrawHandlers.add(new DrawHandler(i, consumer));
        requestRedrawForSync();
        this.mWmService.mH.sendNewMessageDelayed(64, this, 5000L);
    }

    boolean executeDrawHandlers(SurfaceControl.Transaction transaction, int i) {
        boolean z;
        if (transaction == null) {
            transaction = this.mTmpTransaction;
            z = true;
        } else {
            z = false;
        }
        ArrayList arrayList = new ArrayList();
        boolean z2 = false;
        for (int i2 = 0; i2 < this.mDrawHandlers.size(); i2++) {
            DrawHandler drawHandler = this.mDrawHandlers.get(i2);
            if (drawHandler.mSeqId <= i) {
                drawHandler.mConsumer.accept(transaction);
                arrayList.add(drawHandler);
                z2 = true;
            }
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            this.mDrawHandlers.remove((DrawHandler) arrayList.get(i3));
        }
        if (z2) {
            this.mWmService.mH.removeMessages(64, this);
        }
        if (z) {
            transaction.apply();
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSurfaceTranslationY(int i) {
        this.mSurfaceTranslationY = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public int getWindowType() {
        return this.mAttrs.type;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void markRedrawForSyncReported() {
        this.mRedrawForSyncReported = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setWallpaperOffset(int i, int i2, float f) {
        if (this.mXOffset == i && this.mYOffset == i2 && Float.compare(this.mWallpaperScale, f) == 0) {
            return false;
        }
        this.mXOffset = i;
        this.mYOffset = i2;
        this.mWallpaperScale = f;
        scheduleAnimation();
        return true;
    }

    boolean isTrustedOverlay() {
        return this.mInputWindowHandle.isTrustedOverlay();
    }

    @Override // com.android.server.wm.InputTarget
    public boolean receiveFocusFromTapOutside() {
        return canReceiveKeys(true);
    }

    @Override // com.android.server.wm.InputTarget
    public void handleTapOutsideFocusInsideSelf() {
        this.mWmService.moveDisplayToTopInternal(getDisplayId());
        this.mWmService.handleTaskFocusChange(getTask(), this.mActivityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearClientTouchableRegion() {
        this.mTouchableInsets = 0;
        this.mGivenTouchableRegion.setEmpty();
    }

    @Override // com.android.server.wm.InputTarget
    public boolean shouldControlIme() {
        return (inMultiWindowMode() || this.mWindowStateExt.isNotFullScreenCompactWindow(this)) ? false : true;
    }

    @Override // com.android.server.wm.InputTarget
    public boolean canScreenshotIme() {
        return !isSecureLocked();
    }

    @Override // com.android.server.wm.InputTarget
    public ActivityRecord getActivityRecord() {
        return this.mActivityRecord;
    }

    @Override // com.android.server.wm.InputTarget
    public boolean isInputMethodClientFocus(int i, int i2) {
        return getDisplayContent().isInputMethodClientFocus(i, i2);
    }

    @Override // com.android.server.wm.InputTarget
    public void dumpProto(ProtoOutputStream protoOutputStream, long j, int i) {
        dumpDebug(protoOutputStream, j, i);
    }

    public boolean cancelAndRedraw() {
        return this.mPrepareSyncSeqId > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public Dimmer getDimmer() {
        Task task;
        if (getWrapper().getExtImpl().layoutFullscreenInEmbedding() && (task = getTask()) != null) {
            return task.getDimmer();
        }
        return super.getDimmer();
    }

    public IWindowStateWrapper getWrapper() {
        return this.mWindowStateWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class WindowStateWrapper implements IWindowStateWrapper {
        private WindowStateWrapper() {
        }

        @Override // com.android.server.wm.IWindowStateWrapper
        public IWindowStateExt getExtImpl() {
            return WindowState.this.mWindowStateExt;
        }

        @Override // com.android.server.wm.IWindowStateWrapper
        public boolean getAppOpVisibility() {
            return WindowState.this.mAppOpVisibility;
        }
    }
}
