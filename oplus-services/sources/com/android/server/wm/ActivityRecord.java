package com.android.server.wm;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ICompatCameraControlCallback;
import android.app.IScreenCaptureObserver;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.ResultInfo;
import android.app.TaskInfo;
import android.app.WindowConfiguration;
import android.app.admin.DevicePolicyManager;
import android.app.assist.ActivityId;
import android.app.servertransaction.ActivityConfigurationChangeItem;
import android.app.servertransaction.ActivityLifecycleItem;
import android.app.servertransaction.ActivityRelaunchItem;
import android.app.servertransaction.ActivityResultItem;
import android.app.servertransaction.ClientTransaction;
import android.app.servertransaction.ClientTransactionItem;
import android.app.servertransaction.DestroyActivityItem;
import android.app.servertransaction.MoveToDisplayItem;
import android.app.servertransaction.NewIntentItem;
import android.app.servertransaction.PauseActivityItem;
import android.app.servertransaction.ResumeActivityItem;
import android.app.servertransaction.StartActivityItem;
import android.app.servertransaction.StopActivityItem;
import android.app.servertransaction.TopResumedActivityChangeItem;
import android.app.servertransaction.TransferSplashScreenViewStateItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.LocusId;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConstrainDisplayApisConfig;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.gui.DropInputMode;
import android.hardware.HardwareBuffer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.ITheiaManagerExt;
import android.os.InputConstants;
import android.os.LocaleList;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.service.voice.IVoiceInteractionSession;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.Log;
import android.util.MergedConfiguration;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.view.AppTransitionAnimationSpec;
import android.view.DisplayInfo;
import android.view.IAppTransitionAnimationSpecsFuture;
import android.view.InputApplicationHandle;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationDefinition;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.window.ITaskFragmentOrganizer;
import android.window.RemoteTransition;
import android.window.SizeConfigurationBuckets;
import android.window.SplashScreenView;
import android.window.TaskSnapshot;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.ResolverActivity;
import com.android.internal.content.ReferrerIntent;
import com.android.internal.os.TimeoutRecord;
import com.android.internal.os.TransferPipe;
import com.android.internal.policy.AttributeCache;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.ToBooleanFunction;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.LocalServices;
import com.android.server.am.AppTimeTracker;
import com.android.server.am.PendingIntentRecord;
import com.android.server.contentcapture.ContentCaptureManagerInternal;
import com.android.server.display.IMirageDisplayManagerExt;
import com.android.server.display.color.ColorDisplayService;
import com.android.server.uri.NeededUriGrants;
import com.android.server.uri.UriPermissionOwner;
import com.android.server.wm.ActivityMetricsLogger;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.BLASTSyncEngine;
import com.android.server.wm.DisplayPolicy;
import com.android.server.wm.RemoteAnimationController;
import com.android.server.wm.StartingSurfaceController;
import com.android.server.wm.TransitionController;
import com.android.server.wm.WindowManagerService;
import com.android.server.wm.WindowState;
import com.android.server.wm.utils.InsetUtils;
import com.android.server.zenmode.IZenModeManagerExt;
import dalvik.annotation.optimization.NeverCompile;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class ActivityRecord extends WindowToken implements WindowManagerService.AppFreezeListener {
    static final String ACTIVITY_ICON_SUFFIX = "_activity_icon_";
    private static final float ASPECT_RATIO_ROUNDING_TOLERANCE = 0.005f;
    private static final String ATTR_COMPONENTSPECIFIED = "component_specified";
    private static final String ATTR_ID = "id";
    private static final String ATTR_LAUNCHEDFROMFEATURE = "launched_from_feature";
    private static final String ATTR_LAUNCHEDFROMPACKAGE = "launched_from_package";
    private static final String ATTR_LAUNCHEDFROMUID = "launched_from_uid";
    private static final String ATTR_RESOLVEDTYPE = "resolved_type";
    private static final String ATTR_USERID = "user_id";
    private static final int DESTROY_TIMEOUT = 10000;
    static final int FINISH_RESULT_CANCELLED = 0;
    static final int FINISH_RESULT_REMOVED = 2;
    static final int FINISH_RESULT_REQUESTED = 1;
    static final int INVALID_PID = -1;
    static final int LAUNCH_SOURCE_TYPE_APPLICATION = 4;
    static final int LAUNCH_SOURCE_TYPE_HOME = 2;
    static final int LAUNCH_SOURCE_TYPE_SYSTEM = 1;
    static final int LAUNCH_SOURCE_TYPE_SYSTEMUI = 3;
    private static final int LAUNCH_TICK = 500;
    private static final int MAX_STOPPING_TO_FORCE = 3;
    private static final int PAUSE_TIMEOUT = 500;
    private static final int SPLASH_SCREEN_BEHAVIOR_DEFAULT = 0;
    private static final int SPLASH_SCREEN_BEHAVIOR_ICON_PREFERRED = 1;
    static final int STARTING_WINDOW_TYPE_NONE = 0;
    static final int STARTING_WINDOW_TYPE_SNAPSHOT = 1;
    static final int STARTING_WINDOW_TYPE_SPLASH_SCREEN = 2;
    private static final int STOP_TIMEOUT = 11000;
    private static final String TAG_INTENT = "intent";
    private static final String TAG_PERSISTABLEBUNDLE = "persistable_bundle";
    static final int TRANSFER_SPLASH_SCREEN_ATTACH_TO_CLIENT = 2;
    static final int TRANSFER_SPLASH_SCREEN_COPYING = 1;
    static final int TRANSFER_SPLASH_SCREEN_FINISH = 3;
    static final int TRANSFER_SPLASH_SCREEN_IDLE = 0;
    private static final int TRANSFER_SPLASH_SCREEN_TIMEOUT = 2000;
    private static ConstrainDisplayApisConfig sConstrainDisplayApisConfig;
    boolean allDrawn;
    public WindowProcessController app;
    AppTimeTracker appTimeTracker;
    final Binder assistToken;
    private final boolean componentSpecified;
    int configChangeFlags;
    private long createTime;
    boolean deferRelaunchUntilPaused;
    boolean delayedResume;
    boolean finishing;
    boolean firstWindowDrawn;
    boolean forceNewConfig;
    boolean frozenBeforeDestroy;
    boolean hasBeenLaunched;
    private int icon;
    boolean idle;
    boolean immersive;
    volatile boolean inHistory;
    public final ActivityInfo info;
    public final Intent intent;
    private boolean keysPaused;
    private int labelRes;
    long lastLaunchTime;
    long lastVisibleTime;
    int launchCount;
    boolean launchFailed;
    int launchMode;
    long launchTickTime;
    long launchTimeStartOppo;
    final String launchedFromFeatureId;
    final String launchedFromPackage;
    final int launchedFromPid;
    final int launchedFromUid;
    int lockTaskLaunchMode;
    final ComponentName mActivityComponent;
    private IActivityRecordExt mActivityRecordExt;
    final ActivityRecordInputSink mActivityRecordInputSink;
    public IActivityRecordSocExt mActivityRecordSocExt;
    private final AddStartingWindow mAddStartingWindow;
    private boolean mAllowCrossUidActivitySwitchFromBelow;
    int mAllowedTouchUid;
    AnimatingActivityRegistry mAnimatingActivityRegistry;
    private final boolean mAppActivityEmbeddingSplitsEnabled;
    boolean mAppStopped;
    final ActivityTaskManagerService mAtmService;
    boolean mAutoEnteringPip;
    private boolean mCameraCompatControlClickedByUser;
    private final boolean mCameraCompatControlEnabled;
    private int mCameraCompatControlState;
    private RemoteCallbackList<IScreenCaptureObserver> mCaptureCallbacks;
    boolean mClientVisibilityDeferred;
    private final ColorDisplayService.ColorTransformController mColorTransformController;
    private ICompatCameraControlCallback mCompatCameraControlCallback;
    private CompatDisplayInsets mCompatDisplayInsets;
    private int mConfigurationSeq;
    private boolean mCurrentLaunchCanTurnScreenOn;
    private CustomAppTransition mCustomCloseTransition;
    private CustomAppTransition mCustomOpenTransition;
    boolean mDeferAnimationFinish;
    private boolean mDeferHidingClient;
    private final Runnable mDestroyTimeoutRunnable;
    boolean mDismissKeyguard;
    boolean mEnableRecentsScreenshot;
    boolean mEnteringAnimation;
    Drawable mEnterpriseThumbnailDrawable;
    private boolean mForceSendResultForMediaProjection;
    private boolean mFreezingScreen;
    boolean mHandleExitSplashScreen;

    @VisibleForTesting
    int mHandoverLaunchDisplayId;

    @VisibleForTesting
    TaskDisplayArea mHandoverTaskDisplayArea;
    private Boolean mHasDeskResources;
    final boolean mHasSceneTransition;
    private boolean mHaveState;
    private Bundle mIcicle;
    boolean mImeInsetsFrozenUntilStartInput;
    private boolean mInSizeCompatModeForBounds;
    private boolean mInheritShownWhenLocked;
    private InputApplicationHandle mInputApplicationHandle;
    long mInputDispatchingTimeoutMillis;
    private boolean mIsAspectRatioApplied;
    private boolean mIsEligibleForFixedOrientationLetterbox;
    boolean mIsExiting;
    private boolean mIsInputDroppedForAnimation;
    private boolean mLastAllDrawn;
    boolean mLastAllReadyAtSync;
    private AppSaturationInfo mLastAppSaturationInfo;
    private boolean mLastContainsDismissKeyguardWindow;
    private boolean mLastContainsShowWhenLockedWindow;
    private boolean mLastContainsTurnScreenOnWindow;
    private boolean mLastDeferHidingClient;

    @DropInputMode
    private int mLastDropInputMode;
    boolean mLastImeShown;
    Intent mLastNewIntent;
    private Task mLastParentBeforePip;
    private MergedConfiguration mLastReportedConfiguration;
    private int mLastReportedDisplayId;
    boolean mLastReportedMultiWindowMode;
    boolean mLastReportedPictureInPictureMode;
    private boolean mLastSurfaceShowing;
    ITaskFragmentOrganizer mLastTaskFragmentOrganizerBeforePip;
    private long mLastTransactionSequence;
    IBinder mLaunchCookie;
    private ActivityRecord mLaunchIntoPipHostActivity;
    WindowContainerToken mLaunchRootTask;
    private final int mLaunchSourceType;
    private final Runnable mLaunchTickRunnable;
    private boolean mLaunchedFromBubble;
    private Rect mLetterboxBoundsForFixedOrientationAndAspectRatio;
    final LetterboxUiController mLetterboxUiController;
    private LocusId mLocusId;
    private boolean mNeedsLetterboxedAnimation;
    private int mNumDrawnWindows;
    private int mNumInterestingWindows;
    private boolean mOccludesParent;
    boolean mOverrideTaskTransition;
    boolean mPauseSchedulePendingForPip;
    private final Runnable mPauseTimeoutRunnable;
    private ActivityOptions mPendingOptions;
    private int mPendingRelaunchCount;
    RemoteAnimationAdapter mPendingRemoteAnimation;
    private RemoteTransition mPendingRemoteTransition;
    private PersistableBundle mPersistentState;
    int mRelaunchReason;
    long mRelaunchStartTime;
    private RemoteAnimationDefinition mRemoteAnimationDefinition;
    private boolean mRemovingFromDisplay;
    private boolean mReportedDrawn;
    private final WindowState.UpdateReportedVisibilityResults mReportedVisibilityResults;
    boolean mRequestForceTransition;
    IBinder mRequestedLaunchingTaskFragmentToken;
    final RootWindowContainer mRootWindowContainer;
    int mRotationAnimationHint;

    @GuardedBy({"this"})
    ActivityServiceConnectionsHolder mServiceConnectionsHolder;
    boolean mShareIdentity;
    final boolean mShowForAllUsers;
    private boolean mShowWhenLocked;
    private Rect mSizeCompatBounds;
    private float mSizeCompatScale;
    private SizeConfigurationBuckets mSizeConfigurations;
    boolean mSplashScreenStyleSolidColor;
    StartingData mStartingData;
    StartingSurfaceController.StartingSurface mStartingSurface;
    WindowState mStartingWindow;
    private State mState;
    private final Runnable mStopTimeoutRunnable;
    final boolean mStyleFillsParent;
    int mTargetSdk;
    private boolean mTaskOverlay;
    final ActivityTaskSupervisor mTaskSupervisor;
    private ITheiaManagerExt mTheiaManagerExt;
    private final Rect mTmpBounds;
    private final Configuration mTmpConfig;
    private final Runnable mTransferSplashScreenTimeoutRunnable;

    @TransferSplashScreenState
    int mTransferringSplashScreenState;
    int mTransitionChangeFlags;
    private boolean mTurnScreenOn;
    final int mUserId;
    private boolean mVisible;
    volatile boolean mVisibleForServiceConnection;
    private boolean mVisibleSetFromTransferredStartingWindow;
    boolean mVoiceInteraction;
    boolean mWaitForEnteringPinnedMode;
    private boolean mWillCloseOrEnterPip;
    private IActivityRecordWrapper mWrapper;
    public IZenModeManagerExt mZenModeManagerExt;
    ArrayList<ReferrerIntent> newIntents;

    @VisibleForTesting
    boolean noDisplay;
    private CharSequence nonLocalizedLabel;
    boolean nowVisible;
    public final String packageName;
    long pauseTime;
    HashSet<WeakReference<PendingIntentRecord>> pendingResults;
    boolean pendingVoiceInteractionStart;
    PictureInPictureParams pictureInPictureArgs;
    boolean preserveWindowOnDeferredRelaunch;
    final String processName;
    boolean reportedVisible;
    final int requestCode;
    ComponentName requestedVrComponent;
    final String resolvedType;
    ActivityRecord resultTo;
    final String resultWho;
    ArrayList<ResultInfo> results;
    ActivityOptions returningOptions;
    final boolean rootVoiceInteraction;
    final Binder shareableActivityToken;
    final String shortComponentName;
    boolean shouldDockBigOverlays;
    boolean startingMoved;
    final boolean stateNotNeeded;
    boolean supportsEnterPipOnTaskSwitch;
    private Task task;
    final String taskAffinity;
    ActivityManager.TaskDescription taskDescription;
    private int theme;
    long topResumedStateLossTime;
    UriPermissionOwner uriPermissions;
    boolean visibleIgnoringKeyguard;
    IVoiceInteractionSession voiceSession;
    private static final String TAG = "ActivityTaskManager";
    private static final String TAG_ADD_REMOVE = TAG + ActivityTaskManagerDebugConfig.POSTFIX_ADD_REMOVE;
    private static final String TAG_APP = TAG + ActivityTaskManagerDebugConfig.POSTFIX_APP;
    private static final String TAG_CONFIGURATION = TAG + ActivityTaskManagerDebugConfig.POSTFIX_CONFIGURATION;
    private static final String TAG_CONTAINERS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_CONTAINERS;
    private static final String TAG_FOCUS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_FOCUS;
    private static final String TAG_PAUSE = TAG + ActivityTaskManagerDebugConfig.POSTFIX_PAUSE;
    private static final String TAG_RESULTS = TAG + ActivityTaskManagerDebugConfig.POSTFIX_RESULTS;
    private static final String TAG_SAVED_STATE = TAG + ActivityTaskManagerDebugConfig.POSTFIX_SAVED_STATE;
    private static final String TAG_STATES = TAG + ActivityTaskManagerDebugConfig.POSTFIX_STATES;
    private static final String TAG_SWITCH = TAG + ActivityTaskManagerDebugConfig.POSTFIX_SWITCH;
    private static final String TAG_TRANSITION = TAG + ActivityTaskManagerDebugConfig.POSTFIX_TRANSITION;
    private static final String TAG_USER_LEAVING = TAG + ActivityTaskManagerDebugConfig.POSTFIX_USER_LEAVING;
    private static final String TAG_VISIBILITY = TAG + ActivityTaskManagerDebugConfig.POSTFIX_VISIBILITY;
    private static boolean DEBUG_PANIC = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface FinishRequest {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface LaunchSourceType {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface SplashScreenBehavior {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public enum State {
        INITIALIZING,
        STARTED,
        RESUMED,
        PAUSING,
        PAUSED,
        STOPPING,
        STOPPED,
        FINISHING,
        DESTROYING,
        DESTROYED,
        RESTARTING_PROCESS
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface TransferSplashScreenState {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getCenterOffset(int i, int i2) {
        return (int) (((i - i2) + 1) * 0.5f);
    }

    private static boolean hasResizeChange(int i) {
        return (i & 3456) != 0;
    }

    private static boolean isResizeOnlyChange(int i) {
        return (i & (-3457)) == 0;
    }

    @Configuration.Orientation
    public static int reverseConfigurationOrientation(@Configuration.Orientation int i) {
        if (i == 1) {
            return 2;
        }
        if (i != 2) {
            return i;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public ActivityRecord asActivityRecord() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean canCreateRemoteAnimationTarget() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean canCustomizeAppTransition() {
        return true;
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268038L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean hasActivity() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public boolean onChildVisibleRequestedChanged(WindowContainer windowContainer) {
        return false;
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    void resetSurfacePositionForAnimationLeash(SurfaceControl.Transaction transaction) {
    }

    @Override // com.android.server.wm.WindowContainer
    boolean showSurfaceOnCreation() {
        return false;
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

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ DisplayContent getDisplayContent() {
        return super.getDisplayContent();
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
    public /* bridge */ /* synthetic */ void onAnimationLeashCreated(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        super.onAnimationLeashCreated(transaction, surfaceControl);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public /* bridge */ /* synthetic */ void onRequestedOverrideConfigurationChanged(Configuration configuration) {
        super.onRequestedOverrideConfigurationChanged(configuration);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public /* bridge */ /* synthetic */ void onUnfrozen() {
        super.onUnfrozen();
    }

    private void updateEnterpriseThumbnailDrawable(final Context context) {
        this.mEnterpriseThumbnailDrawable = ((DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)).getResources().getDrawable("WORK_PROFILE_ICON", "OUTLINE", "PROFILE_SWITCH_ANIMATION", new Supplier() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda33
            @Override // java.util.function.Supplier
            public final Object get() {
                Drawable drawable;
                drawable = context.getDrawable(R.drawable.ic_doc_generic);
                return drawable;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(final float[] fArr, final float[] fArr2) {
        this.mWmService.mH.post(new Runnable() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                ActivityRecord.this.lambda$new$1(fArr, fArr2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(float[] fArr, float[] fArr2) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mLastAppSaturationInfo == null) {
                    this.mLastAppSaturationInfo = new AppSaturationInfo();
                }
                this.mLastAppSaturationInfo.setSaturation(fArr, fArr2);
                updateColorTransform();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    @NeverCompile
    public void dump(PrintWriter printWriter, String str, boolean z) {
        ApplicationInfo applicationInfo;
        long uptimeMillis = SystemClock.uptimeMillis();
        printWriter.print(str);
        printWriter.print("packageName=");
        printWriter.print(this.packageName);
        printWriter.print(" processName=");
        printWriter.println(this.processName);
        printWriter.print(str);
        printWriter.print("launchedFromUid=");
        printWriter.print(this.launchedFromUid);
        printWriter.print(" launchedFromPackage=");
        printWriter.print(this.launchedFromPackage);
        printWriter.print(" launchedFromFeature=");
        printWriter.print(this.launchedFromFeatureId);
        printWriter.print(" userId=");
        printWriter.println(this.mUserId);
        printWriter.print(str);
        printWriter.print("app=");
        printWriter.println(this.app);
        printWriter.print(str);
        printWriter.println(this.intent.toInsecureString());
        printWriter.print(str);
        printWriter.print("rootOfTask=");
        printWriter.print(isRootOfTask());
        printWriter.print(" task=");
        printWriter.println(this.task);
        printWriter.print(str);
        printWriter.print("taskAffinity=");
        printWriter.println(this.taskAffinity);
        printWriter.print(str);
        printWriter.print("mActivityComponent=");
        printWriter.println(this.mActivityComponent.flattenToShortString());
        ActivityInfo activityInfo = this.info;
        if (activityInfo != null && (applicationInfo = activityInfo.applicationInfo) != null) {
            printWriter.print(str);
            printWriter.print("baseDir=");
            printWriter.println(applicationInfo.sourceDir);
            if (!Objects.equals(applicationInfo.sourceDir, applicationInfo.publicSourceDir)) {
                printWriter.print(str);
                printWriter.print("resDir=");
                printWriter.println(applicationInfo.publicSourceDir);
            }
            printWriter.print(str);
            printWriter.print("dataDir=");
            printWriter.println(applicationInfo.dataDir);
            if (applicationInfo.splitSourceDirs != null) {
                printWriter.print(str);
                printWriter.print("splitDir=");
                printWriter.println(Arrays.toString(applicationInfo.splitSourceDirs));
            }
        }
        printWriter.print(str);
        printWriter.print("stateNotNeeded=");
        printWriter.print(this.stateNotNeeded);
        printWriter.print(" componentSpecified=");
        printWriter.print(this.componentSpecified);
        printWriter.print(" mActivityType=");
        printWriter.println(WindowConfiguration.activityTypeToString(getActivityType()));
        if (this.rootVoiceInteraction) {
            printWriter.print(str);
            printWriter.print("rootVoiceInteraction=");
            printWriter.println(this.rootVoiceInteraction);
        }
        printWriter.print(str);
        printWriter.print("compat=");
        printWriter.print(this.mAtmService.compatibilityInfoForPackageLocked(this.info.applicationInfo));
        printWriter.print(" labelRes=0x");
        printWriter.print(Integer.toHexString(this.labelRes));
        printWriter.print(" icon=0x");
        printWriter.print(Integer.toHexString(this.icon));
        printWriter.print(" theme=0x");
        printWriter.println(Integer.toHexString(this.theme));
        printWriter.println(str + "mLastReportedConfigurations:");
        this.mLastReportedConfiguration.dump(printWriter, str + "  ");
        printWriter.print(str);
        printWriter.print("CurrentConfiguration=");
        printWriter.println(getConfiguration());
        if (!getRequestedOverrideConfiguration().equals(Configuration.EMPTY)) {
            printWriter.println(str + "RequestedOverrideConfiguration=" + getRequestedOverrideConfiguration());
        }
        if (!getResolvedOverrideConfiguration().equals(getRequestedOverrideConfiguration())) {
            printWriter.println(str + "ResolvedOverrideConfiguration=" + getResolvedOverrideConfiguration());
        }
        if (!matchParentBounds()) {
            printWriter.println(str + "bounds=" + getBounds());
        }
        if (this.resultTo != null || this.resultWho != null) {
            printWriter.print(str);
            printWriter.print("resultTo=");
            printWriter.print(this.resultTo);
            printWriter.print(" resultWho=");
            printWriter.print(this.resultWho);
            printWriter.print(" resultCode=");
            printWriter.println(this.requestCode);
        }
        ActivityManager.TaskDescription taskDescription = this.taskDescription;
        if (taskDescription != null && (taskDescription.getIconFilename() != null || this.taskDescription.getLabel() != null || this.taskDescription.getPrimaryColor() != 0)) {
            printWriter.print(str);
            printWriter.print("taskDescription:");
            printWriter.print(" label=\"");
            printWriter.print(this.taskDescription.getLabel());
            printWriter.print("\"");
            printWriter.print(" icon=");
            printWriter.print(this.taskDescription.getInMemoryIcon() != null ? this.taskDescription.getInMemoryIcon().getByteCount() + " bytes" : "null");
            printWriter.print(" iconResource=");
            printWriter.print(this.taskDescription.getIconResourcePackage());
            printWriter.print("/");
            printWriter.print(this.taskDescription.getIconResource());
            printWriter.print(" iconFilename=");
            printWriter.print(this.taskDescription.getIconFilename());
            printWriter.print(" primaryColor=");
            printWriter.println(Integer.toHexString(this.taskDescription.getPrimaryColor()));
            printWriter.print(str);
            printWriter.print("  backgroundColor=");
            printWriter.print(Integer.toHexString(this.taskDescription.getBackgroundColor()));
            printWriter.print(" statusBarColor=");
            printWriter.print(Integer.toHexString(this.taskDescription.getStatusBarColor()));
            printWriter.print(" navigationBarColor=");
            printWriter.println(Integer.toHexString(this.taskDescription.getNavigationBarColor()));
            printWriter.print(str);
            printWriter.print(" backgroundColorFloating=");
            printWriter.println(Integer.toHexString(this.taskDescription.getBackgroundColorFloating()));
        }
        if (this.results != null) {
            printWriter.print(str);
            printWriter.print("results=");
            printWriter.println(this.results);
        }
        HashSet<WeakReference<PendingIntentRecord>> hashSet = this.pendingResults;
        if (hashSet != null && hashSet.size() > 0) {
            printWriter.print(str);
            printWriter.println("Pending Results:");
            Iterator<WeakReference<PendingIntentRecord>> it = this.pendingResults.iterator();
            while (it.hasNext()) {
                WeakReference<PendingIntentRecord> next = it.next();
                PendingIntentRecord pendingIntentRecord = next != null ? next.get() : null;
                printWriter.print(str);
                printWriter.print("  - ");
                if (pendingIntentRecord == null) {
                    printWriter.println("null");
                } else {
                    printWriter.println(pendingIntentRecord);
                    pendingIntentRecord.dump(printWriter, str + "    ");
                }
            }
        }
        ArrayList<ReferrerIntent> arrayList = this.newIntents;
        if (arrayList != null && arrayList.size() > 0) {
            printWriter.print(str);
            printWriter.println("Pending New Intents:");
            for (int i = 0; i < this.newIntents.size(); i++) {
                Intent intent = this.newIntents.get(i);
                printWriter.print(str);
                printWriter.print("  - ");
                if (intent == null) {
                    printWriter.println("null");
                } else {
                    printWriter.println(intent.toShortString(false, true, false, false));
                }
            }
        }
        if (this.mPendingOptions != null) {
            printWriter.print(str);
            printWriter.print("pendingOptions=");
            printWriter.println(this.mPendingOptions);
        }
        if (this.mPendingRemoteAnimation != null) {
            printWriter.print(str);
            printWriter.print("pendingRemoteAnimationCallingPid=");
            printWriter.println(this.mPendingRemoteAnimation.getCallingPid());
        }
        if (this.mPendingRemoteTransition != null) {
            printWriter.print(str + " pendingRemoteTransition=" + this.mPendingRemoteTransition.getRemoteTransition());
        }
        AppTimeTracker appTimeTracker = this.appTimeTracker;
        if (appTimeTracker != null) {
            appTimeTracker.dumpWithHeader(printWriter, str, false);
        }
        UriPermissionOwner uriPermissionOwner = this.uriPermissions;
        if (uriPermissionOwner != null) {
            uriPermissionOwner.dump(printWriter, str);
        }
        printWriter.print(str);
        printWriter.print("launchFailed=");
        printWriter.print(this.launchFailed);
        printWriter.print(" launchCount=");
        printWriter.print(this.launchCount);
        printWriter.print(" lastLaunchTime=");
        long j = this.lastLaunchTime;
        if (j == 0) {
            printWriter.print("0");
        } else {
            TimeUtils.formatDuration(j, uptimeMillis, printWriter);
        }
        printWriter.println();
        if (this.mLaunchCookie != null) {
            printWriter.print(str);
            printWriter.print("launchCookie=");
            printWriter.println(this.mLaunchCookie);
        }
        if (this.mLaunchRootTask != null) {
            printWriter.print(str);
            printWriter.print("mLaunchRootTask=");
            printWriter.println(this.mLaunchRootTask);
        }
        printWriter.print(str);
        printWriter.print("mHaveState=");
        printWriter.print(this.mHaveState);
        printWriter.print(" mIcicle=");
        printWriter.println(this.mIcicle);
        printWriter.print(str);
        printWriter.print("state=");
        printWriter.print(this.mState);
        printWriter.print(" delayedResume=");
        printWriter.print(this.delayedResume);
        printWriter.print(" finishing=");
        printWriter.println(this.finishing);
        printWriter.print(str);
        printWriter.print("keysPaused=");
        printWriter.print(this.keysPaused);
        printWriter.print(" inHistory=");
        printWriter.print(this.inHistory);
        printWriter.print(" idle=");
        printWriter.println(this.idle);
        printWriter.print(str);
        printWriter.print("occludesParent=");
        printWriter.print(occludesParent());
        printWriter.print(" noDisplay=");
        printWriter.print(this.noDisplay);
        printWriter.print(" immersive=");
        printWriter.print(this.immersive);
        printWriter.print(" launchMode=");
        printWriter.println(this.launchMode);
        printWriter.print(str);
        printWriter.print("frozenBeforeDestroy=");
        printWriter.print(this.frozenBeforeDestroy);
        printWriter.print(" forceNewConfig=");
        printWriter.println(this.forceNewConfig);
        printWriter.print(str);
        printWriter.print("mActivityType=");
        printWriter.println(WindowConfiguration.activityTypeToString(getActivityType()));
        printWriter.print(str);
        printWriter.print("mImeInsetsFrozenUntilStartInput=");
        printWriter.println(this.mImeInsetsFrozenUntilStartInput);
        if (this.requestedVrComponent != null) {
            printWriter.print(str);
            printWriter.print("requestedVrComponent=");
            printWriter.println(this.requestedVrComponent);
        }
        super.dump(printWriter, str, z);
        if (this.mVoiceInteraction) {
            printWriter.println(str + "mVoiceInteraction=true");
        }
        printWriter.print(str);
        printWriter.print("providesOrientation=");
        printWriter.println(providesOrientation());
        printWriter.print(str);
        printWriter.print("mOccludesParent=");
        printWriter.println(this.mOccludesParent);
        printWriter.print(str);
        printWriter.print("overrideOrientation=");
        printWriter.println(ActivityInfo.screenOrientationToString(getOverrideOrientation()));
        printWriter.print(str);
        printWriter.print("requestedOrientation=");
        printWriter.println(ActivityInfo.screenOrientationToString(super.getOverrideOrientation()));
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("mVisibleRequested=");
        sb.append(this.mVisibleRequested);
        sb.append(" mVisible=");
        sb.append(this.mVisible);
        sb.append(" mClientVisible=");
        sb.append(isClientVisible());
        sb.append(this.mDeferHidingClient ? " mDeferHidingClient=" + this.mDeferHidingClient : "");
        sb.append(" reportedDrawn=");
        sb.append(this.mReportedDrawn);
        sb.append(" reportedVisible=");
        sb.append(this.reportedVisible);
        printWriter.println(sb.toString());
        if (this.paused) {
            printWriter.print(str);
            printWriter.print("paused=");
            printWriter.println(this.paused);
        }
        if (this.mAppStopped) {
            printWriter.print(str);
            printWriter.print("mAppStopped=");
            printWriter.println(this.mAppStopped);
        }
        if (this.mNumInterestingWindows != 0 || this.mNumDrawnWindows != 0 || this.allDrawn || this.mLastAllDrawn) {
            printWriter.print(str);
            printWriter.print("mNumInterestingWindows=");
            printWriter.print(this.mNumInterestingWindows);
            printWriter.print(" mNumDrawnWindows=");
            printWriter.print(this.mNumDrawnWindows);
            printWriter.print(" allDrawn=");
            printWriter.print(this.allDrawn);
            printWriter.print(" lastAllDrawn=");
            printWriter.print(this.mLastAllDrawn);
            printWriter.println(")");
        }
        if (this.mStartingData != null || this.firstWindowDrawn || this.mIsExiting) {
            printWriter.print(str);
            printWriter.print("startingData=");
            printWriter.print(this.mStartingData);
            printWriter.print(" firstWindowDrawn=");
            printWriter.print(this.firstWindowDrawn);
            printWriter.print(" mIsExiting=");
            printWriter.println(this.mIsExiting);
        }
        if (this.mStartingWindow != null || this.mStartingData != null || this.mStartingSurface != null || this.startingMoved || this.mVisibleSetFromTransferredStartingWindow) {
            printWriter.print(str);
            printWriter.print("startingWindow=");
            printWriter.print(this.mStartingWindow);
            printWriter.print(" startingSurface=");
            printWriter.print(this.mStartingSurface);
            printWriter.print(" startingDisplayed=");
            printWriter.print(isStartingWindowDisplayed());
            printWriter.print(" startingMoved=");
            printWriter.print(this.startingMoved);
            printWriter.println(" mVisibleSetFromTransferredStartingWindow=" + this.mVisibleSetFromTransferredStartingWindow);
        }
        if (this.mPendingRelaunchCount != 0) {
            printWriter.print(str);
            printWriter.print("mPendingRelaunchCount=");
            printWriter.println(this.mPendingRelaunchCount);
        }
        if (this.mSizeCompatScale != 1.0f || this.mSizeCompatBounds != null) {
            printWriter.println(str + "mSizeCompatScale=" + this.mSizeCompatScale + " mSizeCompatBounds=" + this.mSizeCompatBounds);
        }
        if (this.mRemovingFromDisplay) {
            printWriter.println(str + "mRemovingFromDisplay=" + this.mRemovingFromDisplay);
        }
        if (this.lastVisibleTime != 0 || this.nowVisible) {
            printWriter.print(str);
            printWriter.print("nowVisible=");
            printWriter.print(this.nowVisible);
            printWriter.print(" lastVisibleTime=");
            long j2 = this.lastVisibleTime;
            if (j2 == 0) {
                printWriter.print("0");
            } else {
                TimeUtils.formatDuration(j2, uptimeMillis, printWriter);
            }
            printWriter.println();
        }
        if (this.mDeferHidingClient) {
            printWriter.println(str + "mDeferHidingClient=" + this.mDeferHidingClient);
        }
        if (this.deferRelaunchUntilPaused || this.configChangeFlags != 0) {
            printWriter.print(str);
            printWriter.print("deferRelaunchUntilPaused=");
            printWriter.print(this.deferRelaunchUntilPaused);
            printWriter.print(" configChangeFlags=");
            printWriter.println(Integer.toHexString(this.configChangeFlags));
        }
        if (this.mServiceConnectionsHolder != null) {
            printWriter.print(str);
            printWriter.print("connections=");
            printWriter.println(this.mServiceConnectionsHolder);
        }
        if (this.info != null) {
            printWriter.println(str + "resizeMode=" + ActivityInfo.resizeModeToString(this.info.resizeMode));
            printWriter.println(str + "mLastReportedMultiWindowMode=" + this.mLastReportedMultiWindowMode + " mLastReportedPictureInPictureMode=" + this.mLastReportedPictureInPictureMode);
            if (this.info.supportsPictureInPicture()) {
                printWriter.println(str + "supportsPictureInPicture=" + this.info.supportsPictureInPicture());
                printWriter.println(str + "supportsEnterPipOnTaskSwitch: " + this.supportsEnterPipOnTaskSwitch);
            }
            if (getMaxAspectRatio() != 0.0f) {
                printWriter.println(str + "maxAspectRatio=" + getMaxAspectRatio());
            }
            float minAspectRatio = getMinAspectRatio();
            if (minAspectRatio != 0.0f) {
                printWriter.println(str + "minAspectRatio=" + minAspectRatio);
            }
            if (minAspectRatio != this.info.getManifestMinAspectRatio()) {
                printWriter.println(str + "manifestMinAspectRatio=" + this.info.getManifestMinAspectRatio());
            }
            printWriter.println(str + "supportsSizeChanges=" + ActivityInfo.sizeChangesSupportModeToString(supportsSizeChanges()));
            if (this.info.configChanges != 0) {
                printWriter.println(str + "configChanges=0x" + Integer.toHexString(this.info.configChanges));
            }
            printWriter.println(str + "neverSandboxDisplayApis=" + this.info.neverSandboxDisplayApis(sConstrainDisplayApisConfig));
            printWriter.println(str + "alwaysSandboxDisplayApis=" + this.info.alwaysSandboxDisplayApis(sConstrainDisplayApisConfig));
        }
        if (this.mLastParentBeforePip != null) {
            printWriter.println(str + "lastParentTaskIdBeforePip=" + this.mLastParentBeforePip.mTaskId);
        }
        if (this.mLaunchIntoPipHostActivity != null) {
            printWriter.println(str + "launchIntoPipHostActivity=" + this.mLaunchIntoPipHostActivity);
        }
        this.mLetterboxUiController.dump(printWriter, str);
        printWriter.println(str + "mRootLockActivity=" + this.mActivityRecordExt.getRootLockActivity());
        printWriter.println(str + "mShowWhenLocked=" + this.mShowWhenLocked);
        printWriter.println(str + "mLaunchedFromMultiSearch=" + this.mActivityRecordExt.getLaunchedFromMultiSearch());
        Object flexibleActivityInfo = this.mActivityRecordExt.getFlexibleActivityInfo();
        if (flexibleActivityInfo != null) {
            printWriter.println(str + "flexibleActivityInfo=" + flexibleActivityInfo);
        }
        printWriter.println(str + "mCameraCompatControlState=" + TaskInfo.cameraCompatControlStateToString(this.mCameraCompatControlState));
        printWriter.println(str + "mCameraCompatControlEnabled=" + this.mCameraCompatControlEnabled);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean dumpActivity(FileDescriptor fileDescriptor, PrintWriter printWriter, int i, ActivityRecord activityRecord, String str, String str2, boolean z, boolean z2, boolean z3, String str3, boolean z4, Runnable runnable, Task task) {
        if (str3 != null && !str3.equals(activityRecord.packageName)) {
            return false;
        }
        boolean z5 = !z2 && (z || !activityRecord.isInHistory());
        if (z4) {
            printWriter.println("");
        }
        if (runnable != null) {
            runnable.run();
        }
        String str4 = str + "  ";
        String[] strArr = new String[0];
        if (task != activityRecord.getTask()) {
            Task task2 = activityRecord.getTask();
            printWriter.print(str);
            printWriter.print(z5 ? "* " : "  ");
            printWriter.println(task2);
            if (z5) {
                task2.dump(printWriter, str + "  ");
            } else if (z && task2.intent != null) {
                printWriter.print(str);
                printWriter.print("  ");
                printWriter.println(task2.intent.toInsecureString());
            }
        }
        printWriter.print(str);
        printWriter.print(z5 ? "* " : "    ");
        printWriter.print(str2);
        printWriter.print(" #");
        printWriter.print(i);
        printWriter.print(": ");
        printWriter.println(activityRecord);
        if (z5) {
            activityRecord.dump(printWriter, str4, true);
        } else if (z) {
            printWriter.print(str4);
            printWriter.println(activityRecord.intent.toInsecureString());
            if (activityRecord.app != null) {
                printWriter.print(str4);
                printWriter.println(activityRecord.app);
            }
        }
        if (z3 && activityRecord.attachedToProcess()) {
            printWriter.flush();
            try {
                TransferPipe transferPipe = new TransferPipe();
                try {
                    activityRecord.app.getThread().dumpActivity(transferPipe.getWriteFd(), activityRecord.token, str4, strArr);
                    transferPipe.go(fileDescriptor, 2000L);
                    transferPipe.kill();
                } catch (Throwable th) {
                    transferPipe.kill();
                    throw th;
                }
            } catch (RemoteException unused) {
                printWriter.println(str4 + "Got a RemoteException while dumping the activity");
            } catch (IOException e) {
                printWriter.println(str4 + "Failure while dumping the activity: " + e);
            }
        }
        return true;
    }

    void setSavedState(Bundle bundle) {
        this.mIcicle = bundle;
        this.mHaveState = bundle != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle getSavedState() {
        return this.mIcicle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasSavedState() {
        return this.mHaveState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PersistableBundle getPersistentSavedState() {
        return this.mPersistentState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateApplicationInfo(ApplicationInfo applicationInfo) {
        this.info.applicationInfo = applicationInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSizeConfigurations(SizeConfigurationBuckets sizeConfigurationBuckets) {
        this.mSizeConfigurations = sizeConfigurationBuckets;
    }

    private void scheduleActivityMovedToDisplay(int i, Configuration configuration) {
        if (!attachedToProcess()) {
            if (ProtoLogCache.WM_DEBUG_SWITCH_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_SWITCH, -1495062622, 4, (String) null, new Object[]{String.valueOf(this), Long.valueOf(i)});
            }
        } else {
            try {
                if (ProtoLogCache.WM_DEBUG_SWITCH_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_SWITCH, 374506950, 4, (String) null, new Object[]{String.valueOf(this), Long.valueOf(i), String.valueOf(configuration)});
                }
                this.mAtmService.getLifecycleManager().scheduleTransaction(this.app.getThread(), this.token, (ClientTransactionItem) MoveToDisplayItem.obtain(i, configuration));
            } catch (RemoteException unused) {
            }
        }
    }

    private void scheduleConfigurationChanged(Configuration configuration) {
        if (!attachedToProcess()) {
            if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_CONFIGURATION, 1040675582, 0, (String) null, new Object[]{String.valueOf(this)});
                return;
            }
            return;
        }
        try {
            if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, 969323241, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(configuration)});
            }
            this.mAtmService.getLifecycleManager().scheduleTransaction(this.app.getThread(), this.token, (ClientTransactionItem) ActivityConfigurationChangeItem.obtain(configuration));
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean scheduleTopResumedActivityChanged(boolean z) {
        if (!attachedToProcess()) {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_STATES, -1193946201, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            return false;
        }
        if (z) {
            this.app.addToPendingTop();
        }
        try {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -1305966693, 12, (String) null, new Object[]{String.valueOf(this), Boolean.valueOf(z)});
            }
            this.mActivityRecordExt.topResumedActivityChanged(this, z, this.app);
            this.mActivityRecordExt.hookSetBinderUxFlag(true, this);
            this.mAtmService.getLifecycleManager().scheduleTransaction(this.app.getThread(), this.token, (ClientTransactionItem) TopResumedActivityChangeItem.obtain(z));
            this.mActivityRecordExt.hookSetBinderUxFlag(false, this);
            if (z) {
                this.mAtmService.mSocExt.onActivityStateChanged(this, z);
            }
            return true;
        } catch (RemoteException e) {
            this.mActivityRecordExt.hookSetBinderUxFlag(false, this);
            Slog.w(TAG, "Failed to send top-resumed=" + z + " to " + this, e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateMultiWindowMode() {
        boolean inMultiWindowMode;
        Task task = this.task;
        if (task == null || task.getRootTask() == null || !attachedToProcess() || (inMultiWindowMode = inMultiWindowMode()) == this.mLastReportedMultiWindowMode) {
            return;
        }
        if (!inMultiWindowMode && this.mLastReportedPictureInPictureMode) {
            updatePictureInPictureMode(null, false);
        } else {
            this.mLastReportedMultiWindowMode = inMultiWindowMode;
            ensureActivityConfiguration(0, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updatePictureInPictureMode(Rect rect, boolean z) {
        Task task = this.task;
        if (task == null || task.getRootTask() == null || !attachedToProcess()) {
            return;
        }
        boolean z2 = inPinnedWindowingMode() && rect != null;
        if (z2 != this.mLastReportedPictureInPictureMode || z) {
            this.mLastReportedPictureInPictureMode = z2;
            this.mLastReportedMultiWindowMode = z2;
            ensureActivityConfiguration(0, true, true);
            if (z2 && findMainWindow() == null) {
                EventLog.writeEvent(1397638484, "265293293", -1, "");
                removeImmediately();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getTask() {
        return this.task;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskFragment getTaskFragment() {
        WindowContainer parent = getParent();
        if (parent != null) {
            return parent.asTaskFragment();
        }
        return null;
    }

    private boolean shouldStartChangeTransition(TaskFragment taskFragment, TaskFragment taskFragment2) {
        return (taskFragment == null || taskFragment2 == null || !canStartChangeTransition() || !taskFragment.isOrganizedTaskFragment() || taskFragment.getBounds().equals(taskFragment2.getBounds())) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean canStartChangeTransition() {
        Task task = getTask();
        return (task == null || task.isDragResizing() || !super.canStartChangeTransition()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onParentChanged(ConfigurationContainer configurationContainer, ConfigurationContainer configurationContainer2) {
        TaskFragment taskFragment = (TaskFragment) configurationContainer2;
        TaskFragment taskFragment2 = (TaskFragment) configurationContainer;
        Task task = taskFragment != null ? taskFragment.getTask() : null;
        Task task2 = taskFragment2 != null ? taskFragment2.getTask() : null;
        this.task = task2;
        if (shouldStartChangeTransition(taskFragment2, taskFragment)) {
            if (this.mTransitionController.isShellTransitionsEnabled()) {
                initializeChangeTransition(getBounds());
            } else {
                taskFragment2.initializeChangeTransition(getBounds(), getSurfaceControl());
            }
        }
        if (this.mActivityRecordExt.getRootLockActivity() && task2 != null && task2.getWrapper().getExtImpl() != null) {
            task2.getWrapper().getExtImpl().setRootLockDeviceTask(true);
        }
        if (this.mActivityRecordExt.getLaunchedFromMultiSearch() && task2 != null) {
            task2.getWrapper().getExtImpl().setLaunchedFromMultiSearch(true);
        }
        this.mActivityRecordExt.onActivityRecordParentChanged(taskFragment, taskFragment2, this);
        super.onParentChanged(taskFragment2, taskFragment);
        if (isPersistable()) {
            if (task != null) {
                this.mAtmService.notifyTaskPersisterLocked(task, false);
            }
            if (task2 != null) {
                this.mAtmService.notifyTaskPersisterLocked(task2, false);
            }
        }
        if (taskFragment == null && taskFragment2 != null) {
            this.mVoiceInteraction = task2.voiceSession != null;
            task2.updateOverrideConfigurationFromLaunchBounds();
            this.mLastReportedMultiWindowMode = inMultiWindowMode();
            this.mLastReportedPictureInPictureMode = inPinnedWindowingMode();
        }
        if (this.task == null && getDisplayContent() != null) {
            getDisplayContent().mClosingApps.remove(this);
        }
        Task rootTask = getRootTask();
        updateAnimatingActivityRegistry();
        Task task3 = this.task;
        if (task3 == this.mLastParentBeforePip && task3 != null) {
            this.mAtmService.mWindowOrganizerController.mTaskFragmentOrganizerController.onActivityReparentedToTask(this);
            clearLastParentBeforePip();
        }
        if (this.task == this.mActivityRecordExt.getLastParentBeforeSplitScreen() && this.task != null) {
            this.mActivityRecordExt.clearLastParentBeforeSplitScreen();
        }
        if (task != null) {
            task.getWrapper().getExtImpl().notifyChildActivityRecordRemoved(this);
        }
        if (task2 != null) {
            task2.getWrapper().getExtImpl().notifyChildActivityRecordAdded(this);
        }
        this.mActivityRecordExt.onActivityRecordParentChangedAfter(taskFragment, taskFragment2, this);
        updateColorTransform();
        if (taskFragment != null) {
            taskFragment.cleanUpActivityReferences(this);
            this.mRequestedLaunchingTaskFragmentToken = null;
        }
        if (taskFragment2 != null) {
            if (isState(State.RESUMED)) {
                taskFragment2.setResumedActivity(this, "onParentChanged");
            }
            this.mLetterboxUiController.updateInheritedLetterbox();
        }
        if (rootTask != null && rootTask.topRunningActivity() == this && this.firstWindowDrawn) {
            rootTask.setHasBeenVisible(true);
        }
        updateUntrustedEmbeddingInputProtection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void setSurfaceControl(SurfaceControl surfaceControl) {
        super.setSurfaceControl(surfaceControl);
        if (surfaceControl != null) {
            this.mLastDropInputMode = 0;
            updateUntrustedEmbeddingInputProtection();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDropInputForAnimation(boolean z) {
        if (this.mIsInputDroppedForAnimation == z) {
            return;
        }
        this.mIsInputDroppedForAnimation = z;
        updateUntrustedEmbeddingInputProtection();
    }

    private void updateUntrustedEmbeddingInputProtection() {
        if (getSurfaceControl() == null) {
            return;
        }
        if (this.mIsInputDroppedForAnimation) {
            setDropInputMode(1);
        } else if (isEmbeddedInUntrustedMode()) {
            setDropInputMode(2);
        } else {
            setDropInputMode(0);
        }
    }

    @VisibleForTesting
    void setDropInputMode(@DropInputMode int i) {
        if (this.mLastDropInputMode != i) {
            this.mLastDropInputMode = i;
            this.mWmService.mTransactionFactory.get().setDropInputMode(getSurfaceControl(), i).apply();
        }
    }

    private boolean isEmbeddedInUntrustedMode() {
        if (getOrganizedTaskFragment() == null) {
            return false;
        }
        return !r0.isAllowedToEmbedActivityInTrustedMode(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAnimatingActivityRegistry() {
        Task rootTask = getRootTask();
        AnimatingActivityRegistry animatingActivityRegistry = rootTask != null ? rootTask.getAnimatingActivityRegistry() : null;
        AnimatingActivityRegistry animatingActivityRegistry2 = this.mAnimatingActivityRegistry;
        if (animatingActivityRegistry2 != null && animatingActivityRegistry2 != animatingActivityRegistry) {
            animatingActivityRegistry2.notifyFinished(this);
        }
        this.mAnimatingActivityRegistry = animatingActivityRegistry;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastParentBeforePip(ActivityRecord activityRecord) {
        Task task;
        TaskFragment organizedTaskFragment;
        if (activityRecord == null) {
            task = getTask();
        } else {
            task = activityRecord.getTask();
        }
        this.mLastParentBeforePip = task;
        task.mChildPipActivity = this;
        this.mLaunchIntoPipHostActivity = activityRecord;
        if (activityRecord == null) {
            organizedTaskFragment = getOrganizedTaskFragment();
        } else {
            organizedTaskFragment = activityRecord.getOrganizedTaskFragment();
        }
        this.mLastTaskFragmentOrganizerBeforePip = organizedTaskFragment != null ? organizedTaskFragment.getTaskFragmentOrganizer() : null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearLastParentBeforePip() {
        Task task = this.mLastParentBeforePip;
        if (task != null) {
            task.mChildPipActivity = null;
            this.mLastParentBeforePip = null;
        }
        this.mLaunchIntoPipHostActivity = null;
        this.mLastTaskFragmentOrganizerBeforePip = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getLastParentBeforePip() {
        return this.mLastParentBeforePip;
    }

    ActivityRecord getLaunchIntoPipHostActivity() {
        return this.mLaunchIntoPipHostActivity;
    }

    private void updateColorTransform() {
        if (this.mSurfaceControl == null || this.mLastAppSaturationInfo == null) {
            return;
        }
        SurfaceControl.Transaction pendingTransaction = getPendingTransaction();
        SurfaceControl surfaceControl = this.mSurfaceControl;
        AppSaturationInfo appSaturationInfo = this.mLastAppSaturationInfo;
        pendingTransaction.setColorTransform(surfaceControl, appSaturationInfo.mMatrix, appSaturationInfo.mTranslation);
        this.mWmService.scheduleAnimationLocked();
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    void onDisplayChanged(DisplayContent displayContent) {
        DisplayContent displayContent2 = this.mDisplayContent;
        super.onDisplayChanged(displayContent);
        DisplayContent displayContent3 = this.mDisplayContent;
        if (displayContent2 == displayContent3) {
            return;
        }
        displayContent3.onRunningActivityChanged();
        if (displayContent2 == null) {
            return;
        }
        displayContent2.onRunningActivityChanged();
        this.mTransitionController.collect(this);
        if (displayContent2.mOpeningApps.remove(this)) {
            if (DEBUG_PANIC) {
                Slog.d(TAG, "onDisplayChanged, adding " + this + " to mOpeningApps, callers=" + Debug.getCallers(5));
            }
            this.mDisplayContent.mOpeningApps.add(this);
            this.mDisplayContent.transferAppTransitionFrom(displayContent2);
            this.mDisplayContent.executeAppTransition();
        }
        displayContent2.mClosingApps.remove(this);
        displayContent2.getDisplayPolicy().removeRelaunchingApp(this);
        if (displayContent2.mFocusedApp == this) {
            displayContent2.setFocusedApp(null);
            if (displayContent.getTopMostActivity() == this) {
                displayContent.setFocusedApp(this);
            }
        }
        this.mLetterboxUiController.onMovedToDisplay(this.mDisplayContent.getDisplayId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void layoutLetterbox(WindowState windowState) {
        this.mLetterboxUiController.layoutLetterbox(windowState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasWallpaperBackgroundForLetterbox() {
        return this.mLetterboxUiController.hasWallpaperBackgroundForLetterbox();
    }

    void updateLetterboxSurface(WindowState windowState, SurfaceControl.Transaction transaction) {
        this.mLetterboxUiController.updateLetterboxSurface(windowState, transaction);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateLetterboxSurface(WindowState windowState) {
        this.mLetterboxUiController.updateLetterboxSurface(windowState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getLetterboxInsets() {
        return this.mLetterboxUiController.getLetterboxInsets();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getLetterboxInnerBounds(Rect rect) {
        this.mLetterboxUiController.getLetterboxInnerBounds(rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateCameraCompatState(boolean z, boolean z2, ICompatCameraControlCallback iCompatCameraControlCallback) {
        if (isCameraCompatControlEnabled()) {
            if (this.mCameraCompatControlClickedByUser && (z || this.mCameraCompatControlState == 3)) {
                return;
            }
            this.mCompatCameraControlCallback = iCompatCameraControlCallback;
            int i = !z ? 0 : z2 ? 2 : 1;
            if (setCameraCompatControlState(i)) {
                this.mTaskSupervisor.getActivityMetricsLogger().logCameraCompatControlAppearedEventReported(i, this.info.applicationInfo.uid);
                if (i == 0) {
                    this.mCameraCompatControlClickedByUser = false;
                    this.mCompatCameraControlCallback = null;
                }
                getTask().dispatchTaskInfoChangedIfNeeded(true);
                getDisplayContent().setLayoutNeeded();
                this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateCameraCompatStateFromUser(int i) {
        if (isCameraCompatControlEnabled()) {
            if (i == 0) {
                Slog.w(TAG, "Unexpected hidden state in updateCameraCompatState");
                return;
            }
            boolean cameraCompatControlState = setCameraCompatControlState(i);
            this.mCameraCompatControlClickedByUser = true;
            if (cameraCompatControlState) {
                this.mTaskSupervisor.getActivityMetricsLogger().logCameraCompatControlClickedEventReported(i, this.info.applicationInfo.uid);
                if (i == 3) {
                    this.mCompatCameraControlCallback = null;
                    return;
                }
                ICompatCameraControlCallback iCompatCameraControlCallback = this.mCompatCameraControlCallback;
                if (iCompatCameraControlCallback == null) {
                    Slog.w(TAG, "Callback for a camera compat control is null");
                    return;
                }
                try {
                    if (i == 2) {
                        iCompatCameraControlCallback.applyCameraCompatTreatment();
                    } else {
                        iCompatCameraControlCallback.revertCameraCompatTreatment();
                    }
                } catch (RemoteException e) {
                    Slog.e(TAG, "Unable to apply or revert camera compat treatment", e);
                }
            }
        }
    }

    private boolean setCameraCompatControlState(int i) {
        if (!isCameraCompatControlEnabled() || this.mCameraCompatControlState == i) {
            return false;
        }
        this.mCameraCompatControlState = i;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCameraCompatControlState() {
        return this.mCameraCompatControlState;
    }

    @VisibleForTesting
    boolean isCameraCompatControlEnabled() {
        return this.mCameraCompatControlEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFullyTransparentBarAllowed(Rect rect) {
        return this.mLetterboxUiController.isFullyTransparentBarAllowed(rect);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Token extends Binder {
        WeakReference<ActivityRecord> mActivityRef;

        private Token() {
        }

        public String toString() {
            return "Token{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.mActivityRef.get() + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ActivityRecord forToken(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        try {
            return ((Token) iBinder).mActivityRef.get();
        } catch (ClassCastException e) {
            Slog.w(TAG, "Bad activity token: " + iBinder, e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ActivityRecord forTokenLocked(IBinder iBinder) {
        ActivityRecord forToken = forToken(iBinder);
        if (forToken == null || forToken.getRootTask() == null) {
            return null;
        }
        return forToken;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isResolverActivity(String str) {
        return ResolverActivity.class.getName().equals(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isResolverOrDelegateActivity() {
        return isResolverActivity(this.mActivityComponent.getClassName()) || Objects.equals(this.mActivityComponent, this.mAtmService.mTaskSupervisor.getSystemChooserActivity());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isResolverOrChildActivity() {
        if (!"android".equals(this.packageName)) {
            return false;
        }
        try {
            return ResolverActivity.class.isAssignableFrom(Object.class.getClassLoader().loadClass(this.mActivityComponent.getClassName()));
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }

    private ActivityRecord(ActivityTaskManagerService activityTaskManagerService, WindowProcessController windowProcessController, int i, int i2, String str, String str2, Intent intent, String str3, ActivityInfo activityInfo, Configuration configuration, ActivityRecord activityRecord, String str4, int i3, boolean z, boolean z2, ActivityTaskSupervisor activityTaskSupervisor, ActivityOptions activityOptions, ActivityRecord activityRecord2, PersistableBundle persistableBundle, ActivityManager.TaskDescription taskDescription, long j) {
        super(activityTaskManagerService.mWindowManager, new Token(), 2, true, null, false);
        boolean z3;
        boolean z4;
        boolean z5;
        int i4;
        String str5;
        int i5;
        this.mHandoverLaunchDisplayId = -1;
        this.mActivityRecordSocExt = (IActivityRecordSocExt) ExtLoader.type(IActivityRecordSocExt.class).base(this).create();
        this.createTime = System.currentTimeMillis();
        this.mHaveState = true;
        this.pictureInPictureArgs = new PictureInPictureParams.Builder().build();
        this.mSplashScreenStyleSolidColor = false;
        this.mPauseSchedulePendingForPip = false;
        this.mAutoEnteringPip = false;
        this.mTaskOverlay = false;
        this.mRelaunchReason = 0;
        this.mForceSendResultForMediaProjection = false;
        this.mRemovingFromDisplay = false;
        this.mReportedVisibilityResults = new WindowState.UpdateReportedVisibilityResults();
        this.mCurrentLaunchCanTurnScreenOn = true;
        this.mInputDispatchingTimeoutMillis = InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        this.mLastTransactionSequence = Long.MIN_VALUE;
        this.mLastAllReadyAtSync = false;
        this.mSizeCompatScale = 1.0f;
        this.mInSizeCompatModeForBounds = false;
        this.mIsAspectRatioApplied = false;
        this.mCameraCompatControlState = 0;
        this.mEnableRecentsScreenshot = true;
        this.mLastDropInputMode = 0;
        this.mTransferringSplashScreenState = 0;
        this.mRotationAnimationHint = -1;
        ColorDisplayService.ColorTransformController colorTransformController = new ColorDisplayService.ColorTransformController() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda31
            public final void applyAppSaturation(float[] fArr, float[] fArr2) {
                ActivityRecord.this.lambda$new$2(fArr, fArr2);
            }
        };
        this.mColorTransformController = colorTransformController;
        this.mTmpConfig = new Configuration();
        this.mTmpBounds = new Rect();
        this.assistToken = new Binder();
        this.shareableActivityToken = new Binder();
        this.mZenModeManagerExt = (IZenModeManagerExt) ExtLoader.type(IZenModeManagerExt.class).create();
        this.mTheiaManagerExt = (ITheiaManagerExt) ExtLoader.type(ITheiaManagerExt.class).create();
        this.mPauseTimeoutRunnable = new Runnable() { // from class: com.android.server.wm.ActivityRecord.1
            @Override // java.lang.Runnable
            public void run() {
                Slog.w(ActivityRecord.TAG, "Activity pause timeout for " + ActivityRecord.this);
                ActivityRecord.this.mTheiaManagerExt.sendEvent(2L, SystemClock.uptimeMillis(), ActivityRecord.this.getPid(), ActivityRecord.this.getUid(), 4099L, ActivityRecord.this.packageName);
                WindowManagerGlobalLock windowManagerGlobalLock = ActivityRecord.this.mAtmService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        if (!ActivityRecord.this.hasProcess()) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        ActivityRecord activityRecord3 = ActivityRecord.this;
                        activityRecord3.mAtmService.logAppTooSlow(activityRecord3.app, activityRecord3.pauseTime, "pausing " + ActivityRecord.this);
                        ActivityRecord.this.activityPaused(true);
                        WindowManagerService.resetPriorityAfterLockedSection();
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            }
        };
        this.mLaunchTickRunnable = new Runnable() { // from class: com.android.server.wm.ActivityRecord.2
            @Override // java.lang.Runnable
            public void run() {
                WindowManagerGlobalLock windowManagerGlobalLock = ActivityRecord.this.mAtmService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        if (ActivityRecord.this.continueLaunchTicking()) {
                            ActivityRecord activityRecord3 = ActivityRecord.this;
                            activityRecord3.mAtmService.logAppTooSlow(activityRecord3.app, activityRecord3.launchTickTime, "launching " + ActivityRecord.this);
                        }
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        };
        this.mDestroyTimeoutRunnable = new Runnable() { // from class: com.android.server.wm.ActivityRecord.3
            @Override // java.lang.Runnable
            public void run() {
                WindowManagerGlobalLock windowManagerGlobalLock = ActivityRecord.this.mAtmService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        Slog.w(ActivityRecord.TAG, "Activity destroy timeout for " + ActivityRecord.this);
                        ActivityRecord.this.destroyed("destroyTimeout");
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        };
        this.mStopTimeoutRunnable = new Runnable() { // from class: com.android.server.wm.ActivityRecord.4
            @Override // java.lang.Runnable
            public void run() {
                WindowManagerGlobalLock windowManagerGlobalLock = ActivityRecord.this.mAtmService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        Slog.w(ActivityRecord.TAG, "Activity stop timeout for " + ActivityRecord.this);
                        if (ActivityRecord.this.isInHistory()) {
                            ActivityRecord.this.activityStopped(null, null, null);
                        }
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        };
        this.mAddStartingWindow = new AddStartingWindow();
        this.mTransferSplashScreenTimeoutRunnable = new Runnable() { // from class: com.android.server.wm.ActivityRecord.5
            @Override // java.lang.Runnable
            public void run() {
                WindowManagerGlobalLock windowManagerGlobalLock = ActivityRecord.this.mAtmService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        Slog.w(ActivityRecord.TAG, "Activity transferring splash screen timeout for " + ActivityRecord.this + " state " + ActivityRecord.this.mTransferringSplashScreenState);
                        if (ActivityRecord.this.isTransferringSplashScreen()) {
                            ActivityRecord activityRecord3 = ActivityRecord.this;
                            activityRecord3.mTransferringSplashScreenState = 3;
                            activityRecord3.removeStartingWindow();
                        }
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        };
        this.mWrapper = new ActivityRecordWrapper();
        this.mActivityRecordExt = (IActivityRecordExt) ExtLoader.type(IActivityRecordExt.class).base(this).create();
        this.mAtmService = activityTaskManagerService;
        ((Token) this.token).mActivityRef = new WeakReference<>(this);
        this.info = activityInfo;
        int userId = UserHandle.getUserId(activityInfo.applicationInfo.uid);
        this.mUserId = userId;
        String str6 = activityInfo.applicationInfo.packageName;
        this.packageName = str6;
        this.intent = intent;
        String str7 = activityInfo.targetActivity;
        if (str7 == null || (str7.equals(intent.getComponent().getClassName()) && ((i5 = activityInfo.launchMode) == 0 || i5 == 1))) {
            this.mActivityComponent = intent.getComponent();
        } else {
            this.mActivityComponent = new ComponentName(activityInfo.packageName, activityInfo.targetActivity);
        }
        this.mLetterboxUiController = new LetterboxUiController(this.mWmService, this);
        this.mCameraCompatControlEnabled = this.mWmService.mContext.getResources().getBoolean(17891720);
        this.mTargetSdk = activityInfo.applicationInfo.targetSdkVersion;
        this.mShowForAllUsers = (activityInfo.flags & 1024) != 0;
        setOrientation(this.mActivityRecordExt.onActivityRecordOrientationInit(this, activityInfo.screenOrientation));
        this.mRotationAnimationHint = activityInfo.rotationAnimation;
        int i6 = activityInfo.flags;
        this.mShowWhenLocked = (i6 & 8388608) != 0;
        this.mInheritShownWhenLocked = (activityInfo.privateFlags & 1) != 0;
        this.mTurnScreenOn = (i6 & 16777216) != 0;
        int themeResource = activityInfo.getThemeResource();
        themeResource = themeResource == 0 ? activityInfo.applicationInfo.targetSdkVersion < 11 ? R.style.Theme : R.style.Theme.Holo : themeResource;
        AttributeCache.Entry entry = AttributeCache.instance().get(str6, themeResource, com.android.internal.R.styleable.Window, userId);
        if (entry != null) {
            boolean z6 = !ActivityInfo.isTranslucentOrFloating(entry.array) || entry.array.getBoolean(14, false);
            this.mOccludesParent = z6;
            this.mStyleFillsParent = z6;
            this.noDisplay = entry.array.getBoolean(10, false);
            this.mActivityRecordExt.updateCompactFullScreenWindow(this, entry, themeResource);
        } else {
            this.mOccludesParent = true;
            this.mStyleFillsParent = true;
            this.noDisplay = false;
        }
        if (activityOptions != null) {
            this.mLaunchTaskBehind = activityOptions.getLaunchTaskBehind();
            int rotationAnimationHint = activityOptions.getRotationAnimationHint();
            if (rotationAnimationHint >= 0) {
                this.mRotationAnimationHint = rotationAnimationHint;
            }
            if (activityOptions.getLaunchIntoPipParams() != null) {
                this.pictureInPictureArgs = activityOptions.getLaunchIntoPipParams();
                if (activityRecord2 != null) {
                    adjustPictureInPictureParamsIfNeeded(activityRecord2.getBounds());
                }
            }
            this.mOverrideTaskTransition = activityOptions.getOverrideTaskTransition();
            this.mDismissKeyguard = activityOptions.getDismissKeyguard();
            this.mShareIdentity = activityOptions.isShareIdentityEnabled();
        }
        ((ColorDisplayService.ColorDisplayServiceInternal) LocalServices.getService(ColorDisplayService.ColorDisplayServiceInternal.class)).attachColorTransformController(str6, userId, new WeakReference(colorTransformController));
        this.mRootWindowContainer = activityTaskManagerService.mRootWindowContainer;
        this.launchedFromPid = i;
        this.launchedFromUid = i2;
        this.launchedFromPackage = str;
        this.launchedFromFeatureId = str2;
        this.mLaunchSourceType = determineLaunchSourceType(i2, windowProcessController);
        this.shortComponentName = intent.getComponent().flattenToShortString();
        this.resolvedType = str3;
        this.componentSpecified = z;
        this.rootVoiceInteraction = z2;
        this.mLastReportedConfiguration = new MergedConfiguration(configuration);
        this.resultTo = activityRecord;
        this.resultWho = str4;
        this.requestCode = i3;
        State state = State.INITIALIZING;
        setState(state, "ActivityRecord ctor");
        callServiceTrackeronActivityStatechange(state, true);
        this.launchFailed = false;
        this.delayedResume = false;
        this.finishing = false;
        this.deferRelaunchUntilPaused = false;
        this.keysPaused = false;
        this.inHistory = false;
        this.nowVisible = false;
        super.setClientVisible(true);
        this.idle = false;
        this.hasBeenLaunched = false;
        this.mActivityRecordSocExt.initSoc();
        this.mTaskSupervisor = activityTaskSupervisor;
        String computeTaskAffinity = computeTaskAffinity(activityInfo.taskAffinity, activityInfo.applicationInfo.uid, this.launchMode, this.mActivityComponent);
        activityInfo.taskAffinity = computeTaskAffinity;
        this.taskAffinity = computeTaskAffinity;
        String num = Integer.toString(activityInfo.applicationInfo.uid);
        ActivityInfo.WindowLayout windowLayout = activityInfo.windowLayout;
        if (windowLayout != null && (str5 = windowLayout.windowLayoutAffinity) != null && !str5.startsWith(num)) {
            activityInfo.windowLayout.windowLayoutAffinity = num + ":" + activityInfo.windowLayout.windowLayoutAffinity;
        }
        if (sConstrainDisplayApisConfig == null) {
            sConstrainDisplayApisConfig = new ConstrainDisplayApisConfig();
        }
        this.stateNotNeeded = (activityInfo.flags & 16) != 0;
        CharSequence charSequence = activityInfo.nonLocalizedLabel;
        this.nonLocalizedLabel = charSequence;
        int i7 = activityInfo.labelRes;
        this.labelRes = i7;
        if (charSequence == null && i7 == 0) {
            ApplicationInfo applicationInfo = activityInfo.applicationInfo;
            this.nonLocalizedLabel = applicationInfo.nonLocalizedLabel;
            this.labelRes = applicationInfo.labelRes;
        }
        this.icon = activityInfo.getIconResource();
        this.theme = activityInfo.getThemeResource();
        int i8 = activityInfo.flags;
        if ((i8 & 1) != 0 && windowProcessController != null && ((i4 = activityInfo.applicationInfo.uid) == 1000 || i4 == windowProcessController.mInfo.uid)) {
            this.processName = windowProcessController.mName;
        } else {
            this.processName = activityInfo.processName;
        }
        if ((i8 & 32) != 0) {
            intent.addFlags(8388608);
        }
        this.launchMode = activityInfo.launchMode;
        setActivityType(z, i2, intent, activityOptions, activityRecord2);
        this.immersive = (activityInfo.flags & 2048) != 0;
        String str8 = activityInfo.requestedVrComponent;
        this.requestedVrComponent = str8 == null ? null : ComponentName.unflattenFromString(str8);
        this.lockTaskLaunchMode = getLockTaskLaunchMode(activityInfo, activityOptions);
        if (activityOptions != null) {
            setOptions(activityOptions);
            this.mHasSceneTransition = activityOptions.getAnimationType() == 5 && activityOptions.getResultReceiver() != null;
            PendingIntent usageTimeReport = activityOptions.getUsageTimeReport();
            if (usageTimeReport != null) {
                this.appTimeTracker = new AppTimeTracker(usageTimeReport);
            }
            WindowContainerToken launchTaskDisplayArea = activityOptions.getLaunchTaskDisplayArea();
            this.mHandoverTaskDisplayArea = launchTaskDisplayArea != null ? (TaskDisplayArea) WindowContainer.fromBinder(launchTaskDisplayArea.asBinder()) : null;
            this.mHandoverLaunchDisplayId = activityOptions.getLaunchDisplayId();
            this.mLaunchCookie = activityOptions.getLaunchCookie();
            this.mLaunchRootTask = activityOptions.getLaunchRootTask();
            this.mActivityRecordExt.setLaunchedFromMultiSearch(activityOptions.getLaunchedFromMultiSearch());
        } else {
            this.mHasSceneTransition = false;
        }
        this.mPersistentState = persistableBundle;
        this.taskDescription = taskDescription;
        this.shouldDockBigOverlays = this.mWmService.mContext.getResources().getBoolean(17891626);
        if (j > 0) {
            this.createTime = j;
        }
        activityTaskManagerService.mPackageConfigPersister.updateConfigIfNeeded(this, userId, str6);
        this.mActivityRecordInputSink = new ActivityRecordInputSink(this, activityRecord2);
        updateEnterpriseThumbnailDrawable(activityTaskManagerService.getUiContext());
        if (activityInfo.lockTaskLaunchMode == 3 && str6.equals(this.mActivityRecordExt.getRootLockPkgName())) {
            z3 = true;
            this.mActivityRecordExt.setRootLockActivity(true);
        } else {
            z3 = true;
        }
        this.mActivityRecordExt.onActivityRecordCreated(this);
        try {
        } catch (PackageManager.NameNotFoundException unused) {
            z4 = false;
        }
        if (WindowManager.hasWindowExtensionsEnabled()) {
            if (activityTaskManagerService.mContext.getPackageManager().getProperty("android.window.PROPERTY_ACTIVITY_EMBEDDING_SPLITS_ENABLED", str6).getBoolean()) {
                z5 = z3;
                z4 = z5;
                this.mAppActivityEmbeddingSplitsEnabled = z4;
            }
        }
        z5 = false;
        z4 = z5;
        this.mAppActivityEmbeddingSplitsEnabled = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String computeTaskAffinity(String str, int i, int i2, ComponentName componentName) {
        String num = Integer.toString(i);
        if (str == null || str.startsWith(num)) {
            return str;
        }
        String str2 = num + ":" + str;
        if (i2 != 3 || componentName == null) {
            return str2;
        }
        return str2 + ":" + componentName.hashCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getLockTaskLaunchMode(ActivityInfo activityInfo, ActivityOptions activityOptions) {
        int i = activityInfo.lockTaskLaunchMode;
        if (!activityInfo.applicationInfo.isPrivilegedApp() && (i == 2 || i == 1)) {
            i = 0;
        }
        if (activityOptions != null && activityOptions.getLockTaskMode() && i == 0) {
            return 3;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputApplicationHandle getInputApplicationHandle(boolean z) {
        if (this.mInputApplicationHandle == null) {
            this.mInputApplicationHandle = new InputApplicationHandle(this.token, toString(), this.mInputDispatchingTimeoutMillis);
        } else if (z) {
            String activityRecord = toString();
            long j = this.mInputDispatchingTimeoutMillis;
            InputApplicationHandle inputApplicationHandle = this.mInputApplicationHandle;
            if (j != inputApplicationHandle.dispatchingTimeoutMillis || !activityRecord.equals(inputApplicationHandle.name)) {
                this.mInputApplicationHandle = new InputApplicationHandle(this.token, activityRecord, this.mInputDispatchingTimeoutMillis);
            }
        }
        return this.mInputApplicationHandle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setProcess(WindowProcessController windowProcessController) {
        this.app = windowProcessController;
        windowProcessController.getWrapper().getExtImpl().updateWaitActivityToAttach(false);
        Task task = this.task;
        if ((task != null ? task.getRootActivity() : null) == this) {
            this.task.setRootProcess(windowProcessController);
        }
        windowProcessController.addActivityIfNeeded(this);
        this.mInputDispatchingTimeoutMillis = ActivityTaskManagerService.getInputDispatchingTimeoutMillisLocked(this);
        TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null) {
            taskFragment.sendTaskFragmentInfoChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasProcess() {
        return this.app != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean attachedToProcess() {
        return hasProcess() && this.app.hasThread();
    }

    private int evaluateStartingWindowTheme(ActivityRecord activityRecord, String str, int i, int i2) {
        if (validateStartingWindowTheme(activityRecord, str, i)) {
            return (i2 == 0 || !validateStartingWindowTheme(activityRecord, str, i2)) ? i : i2;
        }
        return 0;
    }

    private boolean launchedFromSystemSurface() {
        int i = this.mLaunchSourceType;
        return i == 1 || i == 2 || i == 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLaunchSourceType(@LaunchSourceType int i) {
        return this.mLaunchSourceType == i;
    }

    private int determineLaunchSourceType(int i, WindowProcessController windowProcessController) {
        if (i == 1000 || i == 0) {
            return 1;
        }
        if (windowProcessController == null) {
            return 4;
        }
        if (windowProcessController.isHomeProcess()) {
            return 2;
        }
        return this.mAtmService.getSysUiServiceComponentLocked().getPackageName().equals(windowProcessController.mInfo.packageName) ? 3 : 4;
    }

    private boolean validateStartingWindowTheme(ActivityRecord activityRecord, String str, int i) {
        AttributeCache.Entry entry;
        if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -1782453012, 1, (String) null, new Object[]{Long.valueOf(i)});
        }
        if (i == 0 || (entry = AttributeCache.instance().get(str, i, com.android.internal.R.styleable.Window, this.mWmService.mCurrentUserId)) == null) {
            return false;
        }
        boolean z = entry.array.getBoolean(5, false);
        boolean z2 = entry.array.getBoolean(4, false);
        boolean z3 = entry.array.getBoolean(14, false);
        boolean z4 = entry.array.getBoolean(12, false);
        Slog.d(TAG, "validateStartingWindowTheme: " + String.format("Translucent=%s, Floating=%s, ShowWallpaper=%s, Disable=%s", Boolean.valueOf(z), Boolean.valueOf(z2), Boolean.valueOf(z3), Boolean.valueOf(z4)));
        if ((z || z2) && !this.mActivityRecordExt.hasSplashWindowFlag()) {
            this.mActivityRecordSocExt.setTranslucentWindowLaunch(true);
            return false;
        }
        if (z3 && getDisplayContent().mWallpaperController.getWallpaperTarget() != null && !this.mActivityRecordExt.hasSplashWindowFlag()) {
            return false;
        }
        if (!z4) {
            return true;
        }
        if ((launchedFromSystemSurface() && !this.mActivityRecordExt.notIgnoreWindowDisableStarting(this)) || this.mActivityRecordExt.hasSplashWindowFlag()) {
            return true;
        }
        if (activityRecord != null && activityRecord.getActivityType() == 1 && activityRecord.mTransferringSplashScreenState == 0) {
            if (activityRecord.mStartingData != null) {
                return true;
            }
            if (activityRecord.mStartingWindow != null && activityRecord.mStartingSurface != null) {
                return true;
            }
        }
        return false;
    }

    @VisibleForTesting
    boolean addStartingWindow(String str, int i, ActivityRecord activityRecord, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7) {
        if (!okToDisplay()) {
            Slog.w(TAG, "addStartingWindow fail due to not okToDisplay");
            return false;
        }
        if (this.mStartingData != null) {
            return false;
        }
        WindowState findMainWindow = findMainWindow();
        if (findMainWindow != null && findMainWindow.mWinAnimator.getShown()) {
            if (DEBUG_PANIC) {
                Slog.d(TAG, "addStartingWindow, mainWin=" + findMainWindow + ", shown=" + findMainWindow.mWinAnimator.getShown() + ", drawn=" + findMainWindow.isDrawn());
            }
            if (findMainWindow.isDrawn() || this.mActivityRecordExt.isWindowSurfaceSaved(findMainWindow)) {
                Slog.w(TAG, "App already has a visible window, why would you want a starting window?");
                return false;
            }
        }
        if (this.task == null) {
            Slog.w(TAG, "addStartingWindow fail due to task is null");
            return false;
        }
        if (!this.mActivityRecordExt.allowUseSnapshot(this, z, z2, z3, z5)) {
            return false;
        }
        TaskSnapshotController taskSnapshotController = this.mWmService.mTaskSnapshotController;
        Task task = this.task;
        TaskSnapshot snapshot = taskSnapshotController.getSnapshot(task.mTaskId, task.mUserId, false, false);
        if (snapshot != null && this.mActivityRecordExt.isZoomMode(snapshot.getWindowingMode())) {
            Slog.v(TAG, "last windowmode is zoom: " + this.task);
            return false;
        }
        int startingWindowType = this.mActivityRecordExt.getStartingWindowType(0, 2, 1);
        if (startingWindowType == -1) {
            startingWindowType = getStartingWindowType(z, z2, z3, z4, z5, z7, snapshot);
        }
        int handleStartingWindowForCompactWindow = this.mActivityRecordExt.handleStartingWindowForCompactWindow(this, snapshot, startingWindowType);
        int makeStartingWindowTypeParameter = StartingSurfaceController.makeStartingWindowTypeParameter(z, z2, z3, z4, z5, z6, handleStartingWindowForCompactWindow == 2 && this.mWmService.mStartingSurfaceController.isExceptionApp(this.packageName, this.mTargetSdk, new Supplier() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda7
            @Override // java.util.function.Supplier
            public final Object get() {
                ApplicationInfo lambda$addStartingWindow$3;
                lambda$addStartingWindow$3 = ActivityRecord.this.lambda$addStartingWindow$3();
                return lambda$addStartingWindow$3;
            }
        }), z7, handleStartingWindowForCompactWindow, this.packageName, this.mUserId);
        if (handleStartingWindowForCompactWindow == 1) {
            if (isActivityTypeHome()) {
                this.mWmService.mTaskSnapshotController.removeSnapshotCache(this.task.mTaskId);
                if ((this.mDisplayContent.mAppTransition.getTransitFlags() & 2) == 0) {
                    return false;
                }
            }
            return createSnapshot(snapshot, makeStartingWindowTypeParameter);
        }
        if (i == 0 && this.theme != 0) {
            Slog.d(TAG, "skip addStartingWindow due to resolvedTheme: " + i + ", theme: " + this.theme);
            return false;
        }
        if (activityRecord != null && transferStartingWindow(activityRecord)) {
            return true;
        }
        if (handleStartingWindowForCompactWindow != 2) {
            return false;
        }
        if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 2018852077, 0, (String) null, (Object[]) null);
        }
        this.mStartingData = new SplashScreenStartingData(this.mWmService, i, makeStartingWindowTypeParameter);
        scheduleAddStartingWindow();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ApplicationInfo lambda$addStartingWindow$3() {
        ActivityInfo resolveActivityInfo = this.intent.resolveActivityInfo(this.mAtmService.mContext.getPackageManager(), 128);
        if (resolveActivityInfo != null) {
            return resolveActivityInfo.applicationInfo;
        }
        return null;
    }

    private boolean createSnapshot(TaskSnapshot taskSnapshot, int i) {
        if (taskSnapshot == null) {
            return false;
        }
        if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -1340540100, 0, (String) null, (Object[]) null);
        }
        this.mStartingData = new SnapshotStartingData(this.mWmService, taskSnapshot, i);
        if (this.task.forAllLeafTaskFragments(new ActivityRecord$$ExternalSyntheticLambda34()) || this.mActivityRecordExt.shouldAssociateStartingDataWithTask()) {
            associateStartingDataWithTask();
        }
        scheduleAddStartingWindow();
        return true;
    }

    void scheduleAddStartingWindow() {
        this.mAddStartingWindow.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class AddStartingWindow implements Runnable {
        private AddStartingWindow() {
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x008a  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x00f8  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            StartingSurfaceController.StartingSurface startingSurface;
            WindowManagerGlobalLock windowManagerGlobalLock = ActivityRecord.this.mWmService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    ActivityRecord activityRecord = ActivityRecord.this;
                    StartingData startingData = activityRecord.mStartingData;
                    if (startingData == null) {
                        if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 1836214582, 0, (String) null, new Object[]{String.valueOf(activityRecord)});
                        }
                        return;
                    }
                    if (activityRecord.mStartingSurface != null) {
                        Slog.v("WindowManager", ActivityRecord.this + "already has a starting surface!!!");
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 108170907, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(startingData)});
                    }
                    boolean z = true;
                    try {
                        startingSurface = startingData.createStartingSurface(ActivityRecord.this);
                        if (startingSurface != null) {
                            try {
                                ActivityRecord.this.mActivityRecordExt.updateStartingRecords(ActivityRecord.this, true);
                            } catch (Exception e) {
                                e = e;
                                Slog.w(ActivityRecord.TAG, "Exception when adding starting window", e);
                                if (startingSurface == null) {
                                }
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        startingSurface = null;
                    }
                    if (startingSurface == null) {
                        WindowManagerGlobalLock windowManagerGlobalLock2 = ActivityRecord.this.mWmService.mGlobalLock;
                        WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock2) {
                            try {
                                ActivityRecord activityRecord2 = ActivityRecord.this;
                                if (activityRecord2.mStartingData == null) {
                                    if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
                                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 1822843721, 0, (String) null, new Object[]{String.valueOf(activityRecord2), String.valueOf(ActivityRecord.this.mStartingData)});
                                    }
                                    ActivityRecord activityRecord3 = ActivityRecord.this;
                                    activityRecord3.mStartingWindow = null;
                                    activityRecord3.mStartingData = null;
                                } else {
                                    activityRecord2.mStartingSurface = startingSurface;
                                    z = false;
                                }
                                if (!z && ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
                                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -1292329638, 0, (String) null, new Object[]{String.valueOf(ActivityRecord.this), String.valueOf(ActivityRecord.this.mStartingWindow), String.valueOf(ActivityRecord.this.mStartingSurface)});
                                }
                            } finally {
                            }
                        }
                        WindowManagerService.resetPriorityAfterLockedSection();
                        if (z) {
                            startingSurface.remove(false);
                            return;
                        }
                        return;
                    }
                    if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 1254403969, 0, (String) null, new Object[]{String.valueOf(ActivityRecord.this)});
                    }
                } finally {
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            }
        }
    }

    private int getStartingWindowType(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, TaskSnapshot taskSnapshot) {
        Intent intent;
        ActivityRecord activity;
        Slog.d(TAG, "getStartingWindowType: " + String.format("newTask=%b, taskSwitch=%b, processRunning=%b, allowTaskSnapshot=%b, activityCreated=%b, activityAllDrawn=%b ,snapshot=%s", Boolean.valueOf(z), Boolean.valueOf(z2), Boolean.valueOf(z3), Boolean.valueOf(z4), Boolean.valueOf(z5), Boolean.valueOf(z6), taskSnapshot) + ", record:" + this);
        if (!z && z2 && z3 && !z5 && (intent = this.task.intent) != null && this.mActivityComponent.equals(intent.getComponent()) && (activity = this.task.getActivity(new Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((ActivityRecord) obj).attachedToProcess();
            }
        })) != null) {
            return (activity.isSnapshotCompatible(taskSnapshot) && this.mActivityRecordExt.getFoldDeviceFixRotationFromTask(this, this.mDisplayContent.getDisplayRotation().rotationForOrientation(getOverrideOrientation(), this.mDisplayContent.getRotation())) == taskSnapshot.getRotation()) ? 1 : 0;
        }
        boolean isActivityTypeHome = isActivityTypeHome();
        if ((z || !z3 || (z2 && !z5)) && !isActivityTypeHome) {
            return 2;
        }
        if (z2) {
            if (this.mActivityRecordExt.isZoomMode(getWindowingMode()) && !isActivityTypeHome && this.mActivityRecordExt.isZoomSplashExceptionList(this.packageName)) {
                return 2;
            }
            if (z4) {
                if (isSnapshotCompatible(taskSnapshot)) {
                    return 1;
                }
                if (!isActivityTypeHome) {
                    return 2;
                }
            }
            return (z6 || isActivityTypeHome || this.mActivityRecordExt.clearStartingWindowWhenSnapshotDiffOrientation(this)) ? 0 : 2;
        }
        return 0;
    }

    @VisibleForTesting
    boolean isSnapshotCompatible(TaskSnapshot taskSnapshot) {
        if (taskSnapshot == null) {
            return false;
        }
        if (!taskSnapshot.getTopActivityComponent().equals(this.mActivityComponent)) {
            Slog.w(TAG, "isSnapshotCompatible, Obsoleted snapshot");
            return false;
        }
        int rotationForActivityInDifferentOrientation = this.mDisplayContent.rotationForActivityInDifferentOrientation(this);
        int rotation = this.task.getWindowConfiguration().getRotation();
        int i = rotationForActivityInDifferentOrientation != -1 ? rotationForActivityInDifferentOrientation : rotation;
        if (taskSnapshot.getRotation() != i && !this.mActivityRecordExt.ignoreSnapShotRotation(taskSnapshot.getRotation(), this.task)) {
            Slog.w(TAG, "isSnapshotCompatible, wrong snapshot rotation:" + taskSnapshot.getRotation() + ",targetRotation:" + i + ",rotation:" + rotationForActivityInDifferentOrientation + ",currentRotation:" + rotation);
            return false;
        }
        Rect bounds = this.task.getBounds();
        int width = bounds.width();
        int height = bounds.height();
        Point taskSize = taskSnapshot.getTaskSize();
        if (Math.abs(rotation - i) % 2 == 1) {
            width = height;
            height = width;
        }
        Slog.d(TAG, "isSnapshotCompatible: " + String.format("w=%d, h=%d, taskSize=%s", Integer.valueOf(width), Integer.valueOf(height), taskSize));
        return Math.abs((((float) taskSize.x) / ((float) Math.max(taskSize.y, 1))) - (((float) width) / ((float) Math.max(height, 1)))) <= 0.01f;
    }

    void setCustomizeSplashScreenExitAnimation(boolean z) {
        if (this.mHandleExitSplashScreen == z) {
            return;
        }
        this.mHandleExitSplashScreen = z;
    }

    private void scheduleTransferSplashScreenTimeout() {
        this.mAtmService.mH.postDelayed(this.mTransferSplashScreenTimeoutRunnable, 2000L);
    }

    private void removeTransferSplashScreenTimeout() {
        this.mAtmService.mH.removeCallbacks(this.mTransferSplashScreenTimeoutRunnable);
    }

    private boolean transferSplashScreenIfNeeded() {
        if (this.finishing || !this.mHandleExitSplashScreen || this.mStartingSurface == null || this.mStartingWindow == null || this.mTransferringSplashScreenState == 3) {
            return false;
        }
        if (isTransferringSplashScreen()) {
            return true;
        }
        requestCopySplashScreen();
        return isTransferringSplashScreen();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTransferringSplashScreen() {
        int i = this.mTransferringSplashScreenState;
        return i == 2 || i == 1;
    }

    private void requestCopySplashScreen() {
        this.mTransferringSplashScreenState = 1;
        if (!this.mAtmService.mTaskOrganizerController.copySplashScreenView(getTask())) {
            this.mTransferringSplashScreenState = 3;
            removeStartingWindow();
        }
        scheduleTransferSplashScreenTimeout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCopySplashScreenFinish(SplashScreenView.SplashScreenViewParcelable splashScreenViewParcelable) {
        WindowState windowState;
        removeTransferSplashScreenTimeout();
        if (splashScreenViewParcelable == null || this.mTransferringSplashScreenState != 1 || (windowState = this.mStartingWindow) == null || windowState.mRemoved || this.finishing) {
            if (splashScreenViewParcelable != null) {
                splashScreenViewParcelable.clearIfNeeded();
            }
            this.mTransferringSplashScreenState = 3;
            removeStartingWindow();
            return;
        }
        SurfaceControl applyStartingWindowAnimation = TaskOrganizerController.applyStartingWindowAnimation(windowState);
        try {
            this.mTransferringSplashScreenState = 2;
            this.mAtmService.getLifecycleManager().scheduleTransaction(this.app.getThread(), this.token, (ClientTransactionItem) TransferSplashScreenViewStateItem.obtain(splashScreenViewParcelable, applyStartingWindowAnimation));
            scheduleTransferSplashScreenTimeout();
        } catch (Exception unused) {
            Slog.w(TAG, "onCopySplashScreenComplete fail: " + this);
            this.mStartingWindow.cancelAnimation();
            splashScreenViewParcelable.clearIfNeeded();
            this.mTransferringSplashScreenState = 3;
        }
    }

    private void onSplashScreenAttachComplete() {
        removeTransferSplashScreenTimeout();
        WindowState windowState = this.mStartingWindow;
        if (windowState != null) {
            windowState.cancelAnimation();
            this.mStartingWindow.hide(false, false);
        }
        this.mTransferringSplashScreenState = 3;
        removeStartingWindowAnimation(false);
    }

    void cleanUpSplashScreen() {
        if (!this.mHandleExitSplashScreen || this.startingMoved) {
            return;
        }
        int i = this.mTransferringSplashScreenState;
        if (i == 3 || i == 0) {
            if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -1003678883, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            this.mAtmService.mTaskOrganizerController.onAppSplashScreenViewRemoved(getTask());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isStartingWindowDisplayed() {
        StartingData startingData = this.mStartingData;
        if (startingData == null) {
            Task task = this.task;
            startingData = task != null ? task.mSharedStartingData : null;
        }
        return startingData != null && startingData.mIsDisplayed;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void attachStartingWindow(WindowState windowState) {
        windowState.mStartingData = this.mStartingData;
        this.mStartingWindow = windowState;
        Slog.d(TAG, "attachStartingWindow: mStartingData=" + windowState.mStartingData + ",mStartingWindow=" + this.mStartingWindow);
        StartingData startingData = this.mStartingData;
        if (startingData != null) {
            if (startingData.mAssociatedTask != null) {
                attachStartingSurfaceToAssociatedTask();
            } else if (isEmbedded()) {
                associateStartingWindowWithTaskIfNeeded();
            }
        }
    }

    private void attachStartingSurfaceToAssociatedTask() {
        Task task;
        StartingData startingData = this.mStartingData;
        if (startingData == null || (task = startingData.mAssociatedTask) == null) {
            return;
        }
        WindowState windowState = this.mStartingWindow;
        if (windowState.mSurfaceControl == null) {
            return;
        }
        WindowContainer.overrideConfigurationPropagation(windowState, task);
        getSyncTransaction().reparent(this.mStartingWindow.mSurfaceControl, this.mStartingData.mAssociatedTask.mSurfaceControl);
    }

    private void associateStartingDataWithTask() {
        TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null && taskFragment.isEmbedded()) {
            TaskFragment organizedTaskFragment = taskFragment.getOrganizedTaskFragment();
            TaskFragment adjacentTaskFragment = organizedTaskFragment != null ? organizedTaskFragment.getAdjacentTaskFragment() : null;
            ActivityRecord activityRecord = adjacentTaskFragment != null ? adjacentTaskFragment.topRunningActivity() : null;
            if (activityRecord != null && activityRecord.isVisible() && activityRecord.firstWindowDrawn) {
                return;
            }
        }
        StartingData startingData = this.mStartingData;
        Task task = this.task;
        startingData.mAssociatedTask = task;
        task.mSharedStartingData = startingData;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void associateStartingWindowWithTaskIfNeeded() {
        StartingData startingData;
        if (this.mStartingWindow == null || (startingData = this.mStartingData) == null || startingData.mAssociatedTask != null) {
            return;
        }
        associateStartingDataWithTask();
        attachStartingSurfaceToAssociatedTask();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeStartingWindow() {
        boolean isEligibleForLetterboxEducation = isEligibleForLetterboxEducation();
        if (transferSplashScreenIfNeeded()) {
            Slog.d(TAG, "cancel removeStartingWindow due to transferSplashScreenIfNeeded");
            return;
        }
        removeStartingWindowAnimation(true);
        Task task = getTask();
        if (isEligibleForLetterboxEducation == isEligibleForLetterboxEducation() || task == null) {
            return;
        }
        task.dispatchTaskInfoChangedIfNeeded(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void waitForSyncTransactionCommit(ArraySet<WindowContainer> arraySet) {
        super.waitForSyncTransactionCommit(arraySet);
        StartingData startingData = this.mStartingData;
        if (startingData != null) {
            startingData.mWaitForSyncTransactionCommit = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void onSyncTransactionCommitted(SurfaceControl.Transaction transaction) {
        super.onSyncTransactionCommitted(transaction);
        StartingData startingData = this.mStartingData;
        if (startingData == null) {
            return;
        }
        startingData.mWaitForSyncTransactionCommit = false;
        if (startingData.mRemoveAfterTransaction) {
            startingData.mRemoveAfterTransaction = false;
            removeStartingWindowAnimation(startingData.mPrepareRemoveAnimation);
        }
    }

    void removeStartingWindowAnimation(boolean z) {
        this.mTransferringSplashScreenState = 0;
        Task task = this.task;
        if (task != null) {
            task.mSharedStartingData = null;
        }
        WindowState windowState = this.mStartingWindow;
        if (windowState == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Clearing startingData for token=");
            sb.append(this.mStartingData != null ? this : "null");
            Slog.d(TAG, sb.toString());
            if (this.mStartingData == null || this.mActivityRecordExt.shouldSkipRemoveStartingWindow(findMainWindow(false), this)) {
                return;
            }
            if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -2127842445, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            this.mStartingData = null;
            this.mStartingSurface = null;
            return;
        }
        this.mActivityRecordExt.setSnapshotStarting(this.mStartingData instanceof SnapshotStartingData);
        StartingData startingData = this.mStartingData;
        if (startingData != null) {
            if (startingData.mWaitForSyncTransactionCommit || this.mTransitionController.inCollectingTransition(windowState) || this.mActivityRecordExt.shouldSkipRemoveStartingWindow(findMainWindow(false), this)) {
                Slog.d(TAG, "cannot removestartingWindow syncTransactionCommit:" + this.mStartingData.mWaitForSyncTransactionCommit + " ,inCollectingTransition:" + this.mTransitionController.inCollectingTransition(windowState));
                StartingData startingData2 = this.mStartingData;
                startingData2.mRemoveAfterTransaction = true;
                startingData2.mPrepareRemoveAnimation = z;
                return;
            }
            boolean z2 = z && this.mStartingData.needRevealAnimation() && this.mStartingWindow.isVisibleByPolicy();
            if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -1237827119, 48, (String) null, new Object[]{String.valueOf(this), String.valueOf(this.mStartingWindow), Boolean.valueOf(z2), String.valueOf(Debug.getCallers(5))});
            }
            StartingSurfaceController.StartingSurface startingSurface = this.mStartingSurface;
            this.mStartingData = null;
            this.mStartingSurface = null;
            this.mStartingWindow = null;
            this.mTransitionChangeFlags &= -9;
            if (startingSurface == null) {
                this.mActivityRecordExt.updateStartingRecords(this, false);
                Slog.d(TAG, "startingWindow was set butstartingSurface==null, couldn't remove");
                return;
            }
            Slog.d(TAG, "removeStartingWindowAnimation starting " + String.format("%s startingWindow=%s ,startingView=%s", this, this.mStartingWindow, this.mStartingSurface) + ",Callers=" + Debug.getCallers(5));
            IActivityRecordExt iActivityRecordExt = this.mActivityRecordExt;
            if (iActivityRecordExt.interceptRemoveStartingWindow(this, this.mWmService.mAnimationHandler, startingSurface, iActivityRecordExt.isSnapshotStarting())) {
                return;
            }
            startingSurface.remove(z2);
            this.mActivityRecordExt.updateStartingRecords(this, false);
            return;
        }
        this.mActivityRecordExt.updateStartingRecords(this, false);
        Slog.d(TAG, "Tried to remove starting window but startingWindow was null: " + this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reparent(TaskFragment taskFragment, int i, String str) {
        if (getParent() == null) {
            Slog.w(TAG, "reparent: Attempted to reparent non-existing app token: " + this.token);
            return;
        }
        if (getTaskFragment() == taskFragment) {
            throw new IllegalArgumentException(str + ": task fragment =" + taskFragment + " is already the parent of r=" + this);
        }
        if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 573582981, 20, (String) null, new Object[]{String.valueOf(this), Long.valueOf(this.task.mTaskId), Long.valueOf(i)});
        }
        this.mActivityRecordExt.handleActivityReparent(this, this.task);
        reparent(taskFragment, i);
    }

    private boolean isHomeIntent(Intent intent) {
        return "android.intent.action.MAIN".equals(intent.getAction()) && (intent.hasCategory("android.intent.category.HOME") || intent.hasCategory("android.intent.category.SECONDARY_HOME")) && intent.getCategories().size() == 1 && intent.getData() == null && intent.getType() == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isMainIntent(Intent intent) {
        return "android.intent.action.MAIN".equals(intent.getAction()) && intent.hasCategory("android.intent.category.LAUNCHER") && intent.getCategories().size() == 1 && intent.getData() == null && intent.getType() == null;
    }

    @VisibleForTesting
    boolean canLaunchHomeActivity(int i, ActivityRecord activityRecord) {
        if (i == 1000 || i == 0) {
            return true;
        }
        RecentTasks recentTasks = this.mTaskSupervisor.mService.getRecentTasks();
        if (recentTasks == null || !recentTasks.isCallerRecents(i)) {
            return activityRecord != null && activityRecord.isResolverOrDelegateActivity();
        }
        return true;
    }

    private boolean canLaunchAssistActivity(String str) {
        ComponentName componentName = this.mAtmService.mActiveVoiceInteractionServiceComponent;
        if (componentName != null) {
            return componentName.getPackageName().equals(str);
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0067, code lost:
    
        if (android.service.dreams.DreamActivity.class.getName() == r2.info.name) goto L34;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setActivityType(boolean z, int i, Intent intent, ActivityOptions activityOptions, ActivityRecord activityRecord) {
        int i2 = 4;
        if ((!z || canLaunchHomeActivity(i, activityRecord)) && isHomeIntent(intent) && !isResolverOrDelegateActivity()) {
            ActivityInfo activityInfo = this.info;
            int i3 = activityInfo.resizeMode;
            if (i3 == 4 || i3 == 1) {
                activityInfo.resizeMode = 0;
            }
            i2 = 2;
        } else if (this.mAtmService.getRecentTasks().isRecentsComponent(this.mActivityComponent, this.info.applicationInfo.uid)) {
            i2 = 3;
        } else if (activityOptions == null || activityOptions.getLaunchActivityType() != 4 || !canLaunchAssistActivity(this.launchedFromPackage)) {
            if (activityOptions != null) {
                i2 = 5;
                if (activityOptions.getLaunchActivityType() == 5) {
                    if (this.mAtmService.canLaunchDreamActivity(this.launchedFromPackage)) {
                    }
                }
            }
            i2 = 0;
        }
        setActivityType(this.mActivityRecordExt.toMultiSearchActivityTypeIfNeed(this.info, this.mAtmService.getPackageManager(), i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTaskToAffiliateWith(Task task) {
        int i = this.launchMode;
        if (i == 3 || i == 2) {
            return;
        }
        this.task.setTaskToAffiliateWith(task);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getRootTask() {
        Task task = this.task;
        if (task != null) {
            return task.getRootTask();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRootTaskId() {
        Task task = this.task;
        if (task != null) {
            return task.getRootTaskId();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getOrganizedTask() {
        Task task = this.task;
        if (task != null) {
            return task.getOrganizedTask();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskFragment getOrganizedTaskFragment() {
        TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null) {
            return taskFragment.getOrganizedTaskFragment();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean isEmbedded() {
        TaskFragment taskFragment = getTaskFragment();
        return taskFragment != null && taskFragment.isEmbedded();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public TaskDisplayArea getDisplayArea() {
        return (TaskDisplayArea) super.getDisplayArea();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean providesOrientation() {
        return this.mStyleFillsParent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean fillsParent() {
        return occludesParent(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean occludesParent() {
        return occludesParent(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean occludesParent(boolean z) {
        if (z || !this.finishing) {
            return this.mActivityRecordExt.adjustOccludesParent(this.mOccludesParent || showWallpaper());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setOccludesParent(boolean z) {
        boolean z2 = z != this.mOccludesParent;
        this.mOccludesParent = z;
        setMainWindowOpaque(z);
        this.mWmService.mWindowPlacerLocked.requestTraversal();
        if (z2 && this.task != null && !z) {
            getRootTask().convertActivityToTranslucent(this);
        }
        if (z2 || !z) {
            this.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMainWindowOpaque(boolean z) {
        WindowState findMainWindow = findMainWindow();
        if (findMainWindow == null) {
            return;
        }
        findMainWindow.mWinAnimator.setOpaqueLocked(z & (!PixelFormat.formatHasAlpha(findMainWindow.getAttrs().format)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void takeFromHistory() {
        if (this.inHistory) {
            this.inHistory = false;
            if (this.task != null && !this.finishing) {
                this.task = null;
            }
            abortAndClearOptionsAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInHistory() {
        return this.inHistory;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInRootTaskLocked() {
        Task rootTask = getRootTask();
        return (rootTask == null || rootTask.isInTask(this) == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPersistable() {
        Intent intent;
        int i = this.info.persistableMode;
        return (i == 0 || i == 2) && ((intent = this.intent) == null || (intent.getFlags() & 8388608) == 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean isFocusable() {
        return super.isFocusable() && (canReceiveKeys() || isAlwaysFocusable());
    }

    boolean canReceiveKeys() {
        Task task;
        return getWindowConfiguration().canReceiveKeys() && ((task = this.task) == null || task.getWindowConfiguration().canReceiveKeys());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isResizeable() {
        return isResizeable(true);
    }

    boolean isResizeable(boolean z) {
        return this.mAtmService.mForceResizableActivities || ActivityInfo.isResizeableMode(this.info.resizeMode) || (this.info.supportsPictureInPicture() && z) || this.mActivityRecordExt.isResizeableForMultiSearch(this.task) || isEmbedded();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canForceResizeNonResizable(int i) {
        boolean supportsMultiWindow;
        int i2;
        if (i == 2 && this.info.supportsPictureInPicture()) {
            return false;
        }
        Task task = this.task;
        if (task != null) {
            supportsMultiWindow = task.supportsMultiWindow() || supportsMultiWindow();
        } else {
            supportsMultiWindow = supportsMultiWindow();
        }
        return ((WindowConfiguration.inMultiWindowMode(i) && supportsMultiWindow && !this.mAtmService.mForceResizableActivities) || (i2 = this.info.resizeMode) == 2 || i2 == 1) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean supportsPictureInPicture() {
        return this.mAtmService.mSupportsPictureInPicture && isActivityTypeStandardOrUndefined() && this.info.supportsPictureInPicture();
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean supportsSplitScreenWindowingMode() {
        return supportsSplitScreenWindowingModeInDisplayArea(getDisplayArea());
    }

    boolean supportsSplitScreenWindowingModeInDisplayArea(TaskDisplayArea taskDisplayArea) {
        return super.supportsSplitScreenWindowingMode() && this.mAtmService.mSupportsSplitScreenMultiWindow && this.mActivityRecordExt.supportsSplitScreenByVendorPolicy(this, supportsMultiWindowInDisplayArea(taskDisplayArea));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean supportsFreeform() {
        return supportsFreeformInDisplayArea(getDisplayArea());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean supportsFreeformInDisplayArea(TaskDisplayArea taskDisplayArea) {
        return this.mAtmService.mSupportsFreeformWindowManagement && supportsMultiWindowInDisplayArea(taskDisplayArea);
    }

    boolean supportsMultiWindow() {
        return supportsMultiWindowInDisplayArea(getDisplayArea());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean supportsMultiWindowInDisplayArea(TaskDisplayArea taskDisplayArea) {
        if (isActivityTypeHome() || !this.mAtmService.mSupportsMultiWindow || taskDisplayArea == null) {
            return false;
        }
        if (!isResizeable() && !taskDisplayArea.supportsNonResizableMultiWindow()) {
            return false;
        }
        ActivityInfo activityInfo = this.info;
        ActivityInfo.WindowLayout windowLayout = activityInfo.windowLayout;
        return windowLayout == null || taskDisplayArea.supportsActivityMinWidthHeightMultiWindow(windowLayout.minWidth, windowLayout.minHeight, activityInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canBeLaunchedOnDisplay(int i) {
        return this.mAtmService.mTaskSupervisor.canPlaceEntityOnDisplay(i, this.launchedFromPid, this.launchedFromUid, this.info);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkEnterPictureInPictureState(String str, boolean z) {
        if (!supportsPictureInPicture() || !checkEnterPictureInPictureAppOpsState() || this.mAtmService.shouldDisableNonVrUiLocked()) {
            return false;
        }
        DisplayContent displayContent = this.mDisplayContent;
        if (displayContent != null && !displayContent.mDwpcHelper.isEnteringPipAllowed(getUid())) {
            Slog.w(TAG, "Display " + this.mDisplayContent.getDisplayId() + " doesn't support enter picture-in-picture mode. caller = " + str);
            return false;
        }
        boolean z2 = this.mAtmService.getLockTaskModeState() != 0;
        TaskDisplayArea displayArea = getDisplayArea();
        boolean z3 = displayArea != null && displayArea.hasPinnedTask();
        boolean z4 = (isKeyguardLocked() || z2) ? false : true;
        if (z && z3) {
            return false;
        }
        int i = AnonymousClass6.$SwitchMap$com$android$server$wm$ActivityRecord$State[this.mState.ordinal()];
        if (i != 1) {
            return (i == 2 || i == 3) ? z4 && !z3 && this.supportsEnterPipOnTaskSwitch : i == 4 && this.supportsEnterPipOnTaskSwitch && z4 && !z3;
        }
        if (z2) {
            return false;
        }
        return this.supportsEnterPipOnTaskSwitch || !z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.wm.ActivityRecord$6, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$com$android$server$wm$ActivityRecord$State;

        static {
            int[] iArr = new int[State.values().length];
            $SwitchMap$com$android$server$wm$ActivityRecord$State = iArr;
            try {
                iArr[State.RESUMED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$server$wm$ActivityRecord$State[State.PAUSING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$server$wm$ActivityRecord$State[State.PAUSED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$server$wm$ActivityRecord$State[State.STOPPING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$server$wm$ActivityRecord$State[State.STARTED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$server$wm$ActivityRecord$State[State.STOPPED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$server$wm$ActivityRecord$State[State.DESTROYED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$server$wm$ActivityRecord$State[State.DESTROYING.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$server$wm$ActivityRecord$State[State.INITIALIZING.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWillCloseOrEnterPip(boolean z) {
        this.mWillCloseOrEnterPip = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean willCloseOrEnterPip() {
        return this.mWillCloseOrEnterPip;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkEnterPictureInPictureAppOpsState() {
        return this.mZenModeManagerExt.canEnterPictureInPicture(this.packageName, this.info.applicationInfo.uid) && this.mAtmService.getAppOpsManager().checkOpNoThrow(67, this.info.applicationInfo.uid, this.packageName) == 0;
    }

    private boolean isAlwaysFocusable() {
        return (this.info.flags & 262144) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean windowsAreFocusable() {
        return windowsAreFocusable(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean windowsAreFocusable(boolean z) {
        if (!z && this.mTargetSdk < 29) {
            ActivityRecord activityRecord = this.mWmService.mRoot.mTopFocusedAppByProcess.get(Integer.valueOf(getPid()));
            if (activityRecord != null && activityRecord != this) {
                return false;
            }
        }
        return (canReceiveKeys() || isAlwaysFocusable()) && isAttached();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean moveFocusableActivityToTop(String str) {
        if (!isFocusable()) {
            if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_FOCUS, 240271590, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            return false;
        }
        Task rootTask = getRootTask();
        if (rootTask == null) {
            Slog.w(TAG, "moveFocusableActivityToTop: invalid root task: activity=" + this + " task=" + this.task);
            return false;
        }
        DisplayContent displayContent = this.mDisplayContent;
        ActivityRecord activityRecord = displayContent.mFocusedApp;
        if (activityRecord != null && activityRecord.task == this.task) {
            if (this.task == displayContent.getTask(new Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda13
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$moveFocusableActivityToTop$4;
                    lambda$moveFocusableActivityToTop$4 = ActivityRecord.lambda$moveFocusableActivityToTop$4((Task) obj);
                    return lambda$moveFocusableActivityToTop$4;
                }
            }, true)) {
                if (activityRecord == this) {
                    if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_FOCUS, 385237117, 0, (String) null, new Object[]{String.valueOf(this)});
                    }
                } else {
                    if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_FOCUS, 1239439010, 0, (String) null, new Object[]{String.valueOf(this)});
                    }
                    this.mDisplayContent.setFocusedApp(this);
                    this.mAtmService.mWindowManager.updateFocusedWindowLocked(0, true);
                }
                return !isState(State.RESUMED);
            }
        }
        if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_FOCUS, -50336993, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        rootTask.moveToFront(str, this.task);
        if (this.mRootWindowContainer.getTopResumedActivity() == this) {
            this.mAtmService.setLastResumedActivityUncheckLocked(this, str);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$moveFocusableActivityToTop$4(Task task) {
        return task.isLeafTask() && task.isFocusable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishIfSubActivity(ActivityRecord activityRecord, String str, int i) {
        if (this.resultTo == activityRecord && this.requestCode == i && Objects.equals(this.resultWho, str)) {
            finishIfPossible("request-sub", false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean finishIfSameAffinity(ActivityRecord activityRecord) {
        if (!Objects.equals(activityRecord.taskAffinity, this.taskAffinity)) {
            return true;
        }
        activityRecord.finishIfPossible("request-affinity", true);
        return false;
    }

    private void finishActivityResults(final int i, final Intent intent, final NeededUriGrants neededUriGrants) {
        if (this.resultTo != null) {
            if (ActivityTaskManagerDebugConfig.DEBUG_RESULTS) {
                Slog.v(TAG_RESULTS, "Adding result to " + this.resultTo + " who=" + this.resultWho + " req=" + this.requestCode + " res=" + i + " data=" + intent);
            }
            int i2 = this.resultTo.mUserId;
            int i3 = this.mUserId;
            if (i2 != i3 && intent != null) {
                intent.prepareToLeaveUser(i3);
            }
            if (this.info.applicationInfo.uid > 0) {
                this.mAtmService.mUgmInternal.grantUriPermissionUncheckedFromIntent(neededUriGrants, this.resultTo.getUriPermissionsLocked());
            }
            if (this.mForceSendResultForMediaProjection || this.resultTo.isState(State.RESUMED)) {
                final ActivityRecord activityRecord = this.resultTo;
                this.mAtmService.mH.post(new Runnable() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda17
                    @Override // java.lang.Runnable
                    public final void run() {
                        ActivityRecord.this.lambda$finishActivityResults$5(activityRecord, i, intent, neededUriGrants);
                    }
                });
            } else {
                this.resultTo.addResultLocked(this, this.resultWho, this.requestCode, i, intent);
            }
            this.resultTo = null;
        } else if (ActivityTaskManagerDebugConfig.DEBUG_RESULTS) {
            Slog.v(TAG_RESULTS, "No result destination from " + this);
        }
        this.results = null;
        this.pendingResults = null;
        this.newIntents = null;
        setSavedState(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishActivityResults$5(ActivityRecord activityRecord, int i, Intent intent, NeededUriGrants neededUriGrants) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                activityRecord.sendResult(getUid(), this.resultWho, this.requestCode, i, intent, neededUriGrants, this.mForceSendResultForMediaProjection);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @FinishRequest
    public int finishIfPossible(String str, boolean z) {
        return finishIfPossible(0, null, null, str, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @FinishRequest
    public int finishIfPossible(int i, Intent intent, NeededUriGrants neededUriGrants, String str, boolean z) {
        Transition requestCloseTransitionIfNeeded;
        ActivityRecord activityRecord;
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 1047769218, 4, (String) null, new Object[]{String.valueOf(this), Long.valueOf(i), String.valueOf(intent), String.valueOf(str)});
        }
        if (this.finishing) {
            Slog.w(TAG, "Duplicate finish request for r=" + this);
            return 0;
        }
        if (!isInRootTaskLocked()) {
            Slog.w(TAG, "Finish request when not in root task for r=" + this);
            return 0;
        }
        Task rootTask = getRootTask();
        State state = State.RESUMED;
        int i2 = 1;
        boolean z2 = (isState(state) || rootTask.getTopResumedActivity() == null) && rootTask.isFocusedRootTaskOnDisplay() && !this.task.isClearingToReuseTask();
        boolean z3 = z2 && this.mRootWindowContainer.isTopDisplayFocusedRootTask(rootTask);
        this.mAtmService.deferWindowLayout();
        try {
            this.mTaskSupervisor.mNoHistoryActivities.remove(this);
            makeFinishingLocked();
            if (!isState(state)) {
                this.mActivityRecordExt.onActivityFinish(this, str);
            }
            Task task = getTask();
            EventLogTags.writeWmFinishActivity(this.mUserId, System.identityHashCode(this), task.mTaskId, this.shortComponentName, str);
            this.mActivityRecordExt.collectAppRequestFinishAr(this, str);
            this.mActivityRecordExt.activityPreloadAbort(this, "finishActivity");
            ActivityRecord activityAbove = task.getActivityAbove(this);
            if (activityAbove != null && (this.intent.getFlags() & 524288) != 0) {
                activityAbove.intent.addFlags(524288);
            }
            pauseKeyDispatchingLocked();
            if (z2 && task.topRunningActivity(true) == null) {
                this.mActivityRecordExt.notifySysActivityHotLaunch(ActivityRecord.class, this.mActivityComponent);
                task.adjustFocusToNextFocusableTask("finish-top", false, z3);
            }
            finishActivityResults(i, intent, neededUriGrants);
            boolean z4 = task.getTopNonFinishingActivity() == null && !task.isClearingToReuseTask();
            if (this.mActivityRecordExt.shouldSkipTransition(str)) {
                requestCloseTransitionIfNeeded = null;
            } else {
                requestCloseTransitionIfNeeded = this.mTransitionController.requestCloseTransitionIfNeeded(z4 ? task : this);
            }
            this.mActivityRecordExt.finishIfPossible(this, str, z4);
            if (isState(state)) {
                if (z4) {
                    this.mActivityRecordExt.startCompactMask(getTask());
                    this.mAtmService.getTaskChangeNotificationController().notifyTaskRemovalStarted(task.getTaskInfo());
                }
                if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY || ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
                    Slog.v(TAG_TRANSITION, "Prepare close transition: finishing " + this);
                }
                if (!this.mActivityRecordExt.skipPrepareAppTransitionForMirageIfNeed(task, this.mDisplayContent.getDisplayId(), str)) {
                    this.mDisplayContent.prepareAppTransition(2);
                }
                this.mDisplayContent.getWrapper().getExtImpl().setAnimationThreadUx(true, false, 1);
                setVisibility(false);
                if (this.mLastImeShown && this.mTransitionController.isShellTransitionsEnabled() && (activityRecord = task.topRunningActivity()) != null) {
                    activityRecord.mLastImeShown = true;
                }
                if (getTaskFragment().getPausingActivity() == null) {
                    if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -1003060523, 0, (String) null, new Object[]{String.valueOf(this)});
                    }
                    if (ActivityTaskManagerDebugConfig.DEBUG_USER_LEAVING) {
                        Slog.v(TAG_USER_LEAVING, "finish() => pause with userLeaving=false");
                    }
                    getTaskFragment().startPausing(false, false, null, "finish");
                    this.mAtmService.mSocExt.onBeforeActivitySwitch(getTask(), this);
                }
                this.mActivityRecordExt.onActivityFinish(this, str);
                if (z4) {
                    this.mAtmService.getLockTaskController().clearLockedTask(task);
                    if (z2 && this.mActivityRecordExt.assignLayersIfNeed(this)) {
                        this.mNeedsZBoost = true;
                        this.mDisplayContent.assignWindowLayers(false);
                    }
                }
            } else {
                if (!isState(State.PAUSING)) {
                    boolean z5 = this.mVisibleRequested;
                    if (z5 && !this.mActivityRecordExt.shouldBlockPrepareActivityHideTransitionAnimation(this, z5)) {
                        if (this.mTransitionController.isShellTransitionsEnabled()) {
                            setVisibility(false);
                            if (requestCloseTransitionIfNeeded != null) {
                                requestCloseTransitionIfNeeded.setReady(this.mDisplayContent, true);
                            }
                        } else {
                            prepareActivityHideTransitionAnimation();
                        }
                    }
                    boolean z6 = completeFinishing("finishIfPossible") == null;
                    if (z && isState(State.STOPPING)) {
                        this.mAtmService.updateOomAdj();
                    }
                    if (task.onlyHasTaskOverlayActivities(false)) {
                        task.forAllActivities(new Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda19
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ((ActivityRecord) obj).prepareActivityHideTransitionAnimationIfOvarlay();
                            }
                        });
                    }
                    this.mActivityRecordExt.finishActivity(this, str, false);
                    if (z6) {
                        i2 = 2;
                    }
                    return i2;
                }
                if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 1023413388, 0, (String) null, new Object[]{String.valueOf(this)});
                }
                if (!this.allDrawn && getDisplayContent() != null) {
                    Slog.d(TAG, "finishIfPossible mOpeningApps remove r=" + this);
                    getDisplayContent().mOpeningApps.remove(this);
                }
            }
            this.mActivityRecordExt.finishActivity(this, str, true);
            return i2;
        } finally {
            this.mAtmService.continueWindowLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForceSendResultForMediaProjection() {
        this.mForceSendResultForMediaProjection = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareActivityHideTransitionAnimationIfOvarlay() {
        if (this.mTaskOverlay) {
            prepareActivityHideTransitionAnimation();
        }
    }

    private void prepareActivityHideTransitionAnimation() {
        DisplayContent displayContent = this.mDisplayContent;
        displayContent.prepareAppTransition(2);
        setVisibility(false);
        displayContent.executeAppTransition();
    }

    ActivityRecord completeFinishing(String str) {
        return completeFinishing(true, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0055, code lost:
    
        r8 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:87:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0139  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ActivityRecord completeFinishing(boolean z, String str) {
        boolean z2;
        boolean z3;
        if (!this.finishing || isState(State.RESUMED)) {
            throw new IllegalArgumentException("Activity must be finishing and not resumed to complete, r=" + this + ", finishing=" + this.finishing + ", state=" + this.mState);
        }
        if (isState(State.PAUSING)) {
            return this;
        }
        boolean z4 = false;
        boolean z5 = this.mVisibleRequested || isState(State.PAUSED, State.STARTED);
        if (z && z5 && !this.task.isClearingToReuseTask()) {
            boolean z6 = occludesParent(true) ? false : false;
            if (z6) {
                this.mDisplayContent.ensureActivitiesVisible(null, 0, false, true);
            }
        }
        ActivityRecord activityRecord = getDisplayArea() == null ? null : getDisplayArea().topRunningActivity(true);
        TaskFragment taskFragment = getTaskFragment();
        if (activityRecord != null && taskFragment != null && taskFragment.isEmbedded()) {
            TaskFragment organizedTaskFragment = taskFragment.getOrganizedTaskFragment();
            TaskFragment adjacentTaskFragment = organizedTaskFragment != null ? organizedTaskFragment.getAdjacentTaskFragment() : null;
            if (adjacentTaskFragment != null && activityRecord.isDescendantOf(adjacentTaskFragment) && organizedTaskFragment.topRunningActivity() == null) {
                z2 = organizedTaskFragment.isDelayLastActivityRemoval();
                if (!z2 && this.mActivityRecordExt.shouldDelayRemovalInCompleteFinishing(activityRecord)) {
                    z2 = true;
                }
                z3 = activityRecord == null && !(activityRecord.nowVisible && activityRecord.isVisibleRequested());
                if (z3 && this.mDisplayContent.isSleeping() && activityRecord == activityRecord.getTaskFragment().mLastPausedActivity) {
                    activityRecord.getTaskFragment().clearLastPausedActivity();
                }
                if (!z5) {
                    if (z3 || z2 || (activityRecord != null && this.mActivityRecordExt.isZoomMode(activityRecord.getWindowingMode()) && !this.mActivityRecordExt.isZoomMode(getWindowingMode()) && z5 && this.mOccludesParent)) {
                        addToStopping(false, false, "completeFinishing");
                        State state = State.STOPPING;
                        callServiceTrackeronActivityStatechange(state, true);
                        setState(state, "completeFinishing");
                    } else if (!addToFinishingAndWaitForIdle()) {
                        z4 = destroyIfPossible(str);
                    }
                } else {
                    addToFinishingAndWaitForIdle();
                    z4 = destroyIfPossible(str);
                }
                if (z4) {
                    return this;
                }
                return null;
            }
        }
        z2 = false;
        if (!z2) {
            z2 = true;
        }
        if (activityRecord == null) {
        }
        if (z3) {
            activityRecord.getTaskFragment().clearLastPausedActivity();
        }
        if (!z5) {
        }
        if (z4) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean destroyIfPossible(String str) {
        State state = State.FINISHING;
        callServiceTrackeronActivityStatechange(state, true);
        setState(state, "destroyIfPossible");
        this.mTaskSupervisor.mStoppingActivities.remove(this);
        Task rootTask = getRootTask();
        TaskDisplayArea displayArea = getDisplayArea();
        if (displayArea == null) {
            Slog.e(TAG_STATES, "getDisplayArea is null");
            return false;
        }
        ActivityRecord activityRecord = displayArea.topRunningActivity();
        if (activityRecord == null && rootTask.isFocusedRootTaskOnDisplay() && displayArea.getOrCreateRootHomeTask() != null) {
            addToFinishingAndWaitForIdle();
            return false;
        }
        makeFinishingLocked();
        boolean destroyImmediately = destroyImmediately("finish-imm:" + str);
        if (activityRecord == null) {
            this.mRootWindowContainer.ensureVisibilityAndConfig(activityRecord, getDisplayId(), false, true);
        }
        if (destroyImmediately) {
            this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        }
        if (ProtoLogCache.WM_DEBUG_CONTAINERS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_CONTAINERS, -401282500, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(destroyImmediately)});
        }
        return destroyImmediately;
    }

    @VisibleForTesting
    boolean addToFinishingAndWaitForIdle() {
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 1610646518, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        State state = State.FINISHING;
        callServiceTrackeronActivityStatechange(state, true);
        setState(state, "addToFinishingAndWaitForIdle");
        if (!this.mTaskSupervisor.mFinishingActivities.contains(this)) {
            this.mTaskSupervisor.mFinishingActivities.add(this);
        }
        resumeKeyDispatchingLocked();
        return this.mRootWindowContainer.resumeFocusedTasksTopActivities();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean destroyImmediately(String str) {
        boolean z;
        if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH || ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
            String str2 = TAG_SWITCH;
            StringBuilder sb = new StringBuilder();
            sb.append("Removing activity from ");
            sb.append(str);
            sb.append(": token=");
            sb.append(this);
            sb.append(", app=");
            sb.append(hasProcess() ? this.app.mName : "(null)");
            Slog.v(str2, sb.toString());
        }
        State state = State.DESTROYING;
        State state2 = State.DESTROYED;
        if (isState(state, state2)) {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -21399771, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(str)});
            }
            return false;
        }
        EventLogTags.writeWmDestroyActivity(this.mUserId, System.identityHashCode(this), this.task.mTaskId, this.shortComponentName, str);
        cleanUp(false, false);
        boolean z2 = true;
        if (hasProcess()) {
            this.app.removeActivity(this, true);
            if (!this.app.hasActivities()) {
                this.mAtmService.clearHeavyWeightProcessIfEquals(this.app);
            }
            try {
                if (isState(State.FINISHING)) {
                    this.mAtmService.mSocExt.onActivityStateChanged(this, false);
                }
                if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
                    Slog.i(TAG_SWITCH, "Destroying: " + this);
                }
                this.mAtmService.getLifecycleManager().scheduleTransaction(this.app.getThread(), this.token, (ActivityLifecycleItem) DestroyActivityItem.obtain(this.finishing, this.configChangeFlags));
            } catch (Exception unused) {
                if (this.finishing) {
                    removeFromHistory(str + " exceptionInScheduleDestroy");
                    z = true;
                }
            }
            z = false;
            boolean z3 = z;
            this.nowVisible = false;
            if (this.finishing && !z) {
                if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -1432963966, 0, (String) null, new Object[]{String.valueOf(this)});
                }
                State state3 = State.DESTROYING;
                callServiceTrackeronActivityStatechange(state3, true);
                setState(state3, "destroyActivityLocked. finishing and not skipping destroy");
                this.mAtmService.mH.postDelayed(this.mDestroyTimeoutRunnable, 10000L);
            } else {
                if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 726205185, 0, (String) null, new Object[]{String.valueOf(this)});
                }
                State state4 = State.DESTROYED;
                callServiceTrackeronActivityStatechange(state4, true);
                setState(state4, "destroyActivityLocked. not finishing or skipping destroy");
                if (ActivityTaskManagerDebugConfig.DEBUG_APP) {
                    Slog.v(TAG_APP, "Clearing app during destroy for activity " + this);
                }
                detachFromProcess();
            }
            z2 = z3;
        } else if (this.finishing) {
            removeFromHistory(str + " hadNoApp");
        } else {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -729530161, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            callServiceTrackeronActivityStatechange(state2, true);
            setState(state2, "destroyActivityLocked. not finishing and had no app");
            z2 = false;
        }
        this.configChangeFlags = 0;
        return z2;
    }

    boolean safelyDestroy(String str) {
        if (!isDestroyable()) {
            return false;
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            Task task = getTask();
            Slog.v(TAG_SWITCH, "Safely destroying " + this + " in state " + getState() + " resumed=" + task.getTopResumedActivity() + " pausing=" + task.getTopPausingActivity() + " for reason " + str);
        }
        return destroyImmediately(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeFromHistory(String str) {
        finishActivityResults(0, null, null);
        makeFinishingLocked();
        if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 350168164, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(str), String.valueOf(Debug.getCallers(5))});
        }
        takeFromHistory();
        removeTimeouts();
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 579298675, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        State state = State.DESTROYED;
        callServiceTrackeronActivityStatechange(state, true);
        setState(state, "removeFromHistory");
        if (ActivityTaskManagerDebugConfig.DEBUG_APP) {
            Slog.v(TAG_APP, "Clearing app during remove for activity " + this);
        }
        detachFromProcess();
        resumeKeyDispatchingLocked();
        this.mDisplayContent.removeAppToken(this.token);
        cleanUpActivityServices();
        removeUriPermissionsLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void detachFromProcess() {
        WindowProcessController windowProcessController = this.app;
        if (windowProcessController != null) {
            windowProcessController.removeActivity(this, false);
        }
        this.app = null;
        this.mInputDispatchingTimeoutMillis = InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeFinishingLocked() {
        Task task;
        ActivityRecord activity;
        if (this.finishing) {
            return;
        }
        this.finishing = true;
        if (this.mVisible) {
            this.mActivityRecordExt.removeUnVisibleWindow(getUid(), this.packageName);
        }
        if (this.mLaunchCookie != null && this.mState != State.RESUMED && (task = this.task) != null && !task.mInRemoveTask && !task.isClearingToReuseTask() && (activity = this.task.getActivity(new Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda16
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$makeFinishingLocked$7;
                lambda$makeFinishingLocked$7 = ActivityRecord.this.lambda$makeFinishingLocked$7((ActivityRecord) obj);
                return lambda$makeFinishingLocked$7;
            }
        }, this, false, false)) != null) {
            activity.mLaunchCookie = this.mLaunchCookie;
            this.mLaunchCookie = null;
        }
        TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null) {
            Task task2 = taskFragment.getTask();
            if (task2 != null && task2.isClearingToReuseTask() && taskFragment.getTopNonFinishingActivity() == null) {
                taskFragment.mClearedTaskForReuse = true;
            }
            taskFragment.sendTaskFragmentInfoChanged();
        }
        if (this.mAppStopped) {
            abortAndClearOptionsAnimation();
        }
        if (getDisplayContent() != null) {
            getDisplayContent().mUnknownAppVisibilityController.appRemovedOrHidden(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$makeFinishingLocked$7(ActivityRecord activityRecord) {
        return activityRecord.mLaunchCookie == null && !activityRecord.finishing && activityRecord.isUid(getUid());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFinishing() {
        return this.finishing;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroyed(String str) {
        removeDestroyTimeout();
        if (this.mActivityRecordExt.ignoreTimeOutForNonFinishing(this, str)) {
            return;
        }
        if (ProtoLogCache.WM_DEBUG_CONTAINERS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_CONTAINERS, -1598452494, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        if (!isState(State.DESTROYING, State.DESTROYED, State.RESTARTING_PROCESS) && !this.mActivityRecordExt.ignoreTimeOut(this, str)) {
            throw new IllegalStateException("Reported destroyed for activity that is not destroying: r=" + this);
        }
        this.mTaskSupervisor.killTaskProcessesOnDestroyedIfNeeded(this.task);
        if (isInRootTaskLocked()) {
            cleanUp(true, false);
            removeFromHistory(str);
        }
        this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        this.mActivityRecordExt.onActivityDestroyed(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanUp(boolean z, boolean z2) {
        HashSet<WeakReference<PendingIntentRecord>> hashSet;
        if (getTaskFragment() != null) {
            getTaskFragment().cleanUpActivityReferences(this);
        }
        clearLastParentBeforePip();
        this.mActivityRecordExt.clearLastParentBeforeSplitScreen();
        cleanUpSplashScreen();
        this.deferRelaunchUntilPaused = false;
        this.frozenBeforeDestroy = false;
        if (z2) {
            State state = State.DESTROYED;
            callServiceTrackeronActivityStatechange(state, true);
            setState(state, "cleanUp");
            if (ActivityTaskManagerDebugConfig.DEBUG_APP) {
                Slog.v(TAG_APP, "Clearing app during cleanUp for activity " + this);
            }
            detachFromProcess();
        }
        this.mTaskSupervisor.cleanupActivity(this);
        if (this.finishing && (hashSet = this.pendingResults) != null) {
            Iterator<WeakReference<PendingIntentRecord>> it = hashSet.iterator();
            while (it.hasNext()) {
                PendingIntentRecord pendingIntentRecord = it.next().get();
                if (pendingIntentRecord != null) {
                    this.mAtmService.mPendingIntentController.cancelIntentSender(pendingIntentRecord, false);
                }
            }
            this.pendingResults = null;
        }
        if (z) {
            cleanUpActivityServices();
        }
        removeTimeouts();
        clearRelaunching();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRelaunching() {
        return this.mPendingRelaunchCount > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void startRelaunching() {
        if (this.mPendingRelaunchCount == 0) {
            this.mRelaunchStartTime = SystemClock.elapsedRealtime();
            if (this.mVisibleRequested) {
                this.mDisplayContent.getDisplayPolicy().addRelaunchingApp(this);
            }
        }
        clearAllDrawn();
        this.mPendingRelaunchCount++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishRelaunching() {
        this.mLetterboxUiController.setRelaunchingAfterRequestedOrientationChanged(false);
        this.mTaskSupervisor.getActivityMetricsLogger().notifyActivityRelaunched(this);
        int i = this.mPendingRelaunchCount;
        if (i > 0) {
            int i2 = i - 1;
            this.mPendingRelaunchCount = i2;
            if (i2 == 0 && !isClientVisible()) {
                finishOrAbortReplacingWindow();
            }
        } else {
            checkKeyguardFlagsChanged();
        }
        Task rootTask = getRootTask();
        if (rootTask == null || !rootTask.shouldSleepOrShutDownActivities()) {
            return;
        }
        rootTask.ensureActivitiesVisible(null, 0, false);
    }

    void clearRelaunching() {
        if (this.mPendingRelaunchCount == 0) {
            return;
        }
        this.mPendingRelaunchCount = 0;
        finishOrAbortReplacingWindow();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishOrAbortReplacingWindow() {
        this.mRelaunchStartTime = 0L;
        this.mDisplayContent.getDisplayPolicy().removeRelaunchingApp(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityServiceConnectionsHolder getOrCreateServiceConnectionsHolder() {
        ActivityServiceConnectionsHolder activityServiceConnectionsHolder;
        synchronized (this) {
            if (this.mServiceConnectionsHolder == null) {
                this.mServiceConnectionsHolder = new ActivityServiceConnectionsHolder(this);
            }
            activityServiceConnectionsHolder = this.mServiceConnectionsHolder;
        }
        return activityServiceConnectionsHolder;
    }

    private void cleanUpActivityServices() {
        synchronized (this) {
            ActivityServiceConnectionsHolder activityServiceConnectionsHolder = this.mServiceConnectionsHolder;
            if (activityServiceConnectionsHolder == null) {
                return;
            }
            activityServiceConnectionsHolder.disconnectActivityFromServices();
            this.mServiceConnectionsHolder = null;
        }
    }

    private void updateVisibleForServiceConnection() {
        State state;
        this.mVisibleForServiceConnection = this.mVisibleRequested || (state = this.mState) == State.RESUMED || state == State.PAUSING;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0013, code lost:
    
        if (r0 != 2) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0039, code lost:
    
        if (isState(com.android.server.wm.ActivityRecord.State.RESTARTING_PROCESS) == false) goto L4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0054, code lost:
    
        if (r10.lastLaunchTime > (android.os.SystemClock.uptimeMillis() - 60000)) goto L4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x001c, code lost:
    
        if (r10.finishing != false) goto L13;
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:5:0x0059  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void handleAppDied() {
        boolean z;
        Task task;
        ActivityRecord activityRecord;
        Task task2;
        WindowProcessController windowProcessController;
        if (!Process.isSdkSandboxUid(getUid())) {
            int i = this.mRelaunchReason;
            z = false;
            if (i != 1) {
            }
            if (this.launchCount < 3) {
            }
            if (!this.mHaveState) {
                if (!this.mActivityRecordExt.isFontPageKilled(getTask(), this)) {
                    if (!this.stateNotNeeded) {
                    }
                }
            }
            if (!this.finishing) {
                if (!this.mVisibleRequested) {
                    if (this.launchCount > 2) {
                    }
                }
                if (!z) {
                    if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
                        ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1789603530, 204, (String) null, new Object[]{String.valueOf(this), Boolean.valueOf(this.mHaveState), String.valueOf(this.stateNotNeeded), Boolean.valueOf(this.finishing), String.valueOf(this.mState), String.valueOf(Debug.getCallers(5))});
                    }
                    if (!this.finishing || ((windowProcessController = this.app) != null && windowProcessController.isRemoved())) {
                        Slog.w(TAG, "Force removing " + this + ": app died, no saved state");
                        int i2 = this.mUserId;
                        int identityHashCode = System.identityHashCode(this);
                        Task task3 = this.task;
                        EventLogTags.writeWmFinishActivity(i2, identityHashCode, task3 != null ? task3.mTaskId : -1, this.shortComponentName, "proc died without state saved");
                    }
                } else if (ActivityTaskManagerDebugConfig.DEBUG_APP) {
                    Slog.v(TAG_APP, "Keeping entry during removeHistory for activity " + this);
                }
                this.mTransitionController.requestCloseTransitionIfNeeded((z || (task2 = this.task) == null || task2.getChildCount() != 1) ? this : this.task);
                this.mActivityRecordExt.activityPreloadAbort(this, "appDied");
                this.mTaskSupervisor.killTaskProcessesOnDestroyedIfNeeded(this.task);
                cleanUp(true, true);
                if (z) {
                    return;
                }
                if (this.mStartingData != null && this.mVisible && (task = this.task) != null && (activityRecord = task.topRunningActivity()) != null && !activityRecord.mVisible && activityRecord.shouldBeVisible()) {
                    activityRecord.transferStartingWindow(this);
                }
                removeFromHistory("appDied");
                return;
            }
        }
        z = true;
        if (!z) {
        }
        this.mTransitionController.requestCloseTransitionIfNeeded((z || (task2 = this.task) == null || task2.getChildCount() != 1) ? this : this.task);
        this.mActivityRecordExt.activityPreloadAbort(this, "appDied");
        this.mTaskSupervisor.killTaskProcessesOnDestroyedIfNeeded(this.task);
        cleanUp(true, true);
        if (z) {
        }
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    void removeImmediately() {
        if (this.mState != State.DESTROYED) {
            Slog.w(TAG, "Force remove immediately " + this + " state=" + this.mState);
            destroyImmediately("removeImmediately");
            destroyed("removeImmediately");
        } else {
            onRemovedFromDisplay();
        }
        this.mActivityRecordInputSink.releaseSurfaceControl();
        this.mActivityRecordExt.removeImmediately();
        super.removeImmediately();
    }

    @Override // com.android.server.wm.WindowContainer
    void removeIfPossible() {
        this.mIsExiting = false;
        removeAllWindowsIfPossible();
        removeImmediately();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean handleCompleteDeferredRemoval() {
        if (this.mIsExiting) {
            removeIfPossible();
        }
        return super.handleCompleteDeferredRemoval();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x00db, code lost:
    
        if (r11.mTransitionController.inTransition() != false) goto L40;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onRemovedFromDisplay() {
        Task task;
        if (this.mRemovingFromDisplay) {
            return;
        }
        this.mRemovingFromDisplay = true;
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -1352076759, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        getDisplayContent().mOpeningApps.remove(this);
        getDisplayContent().mUnknownAppVisibilityController.appRemovedOrHidden(this);
        this.mWmService.mSnapshotController.onAppRemoved(this);
        this.mTaskSupervisor.getActivityMetricsLogger().notifyActivityRemoved(this);
        this.mTaskSupervisor.mStoppingActivities.remove(this);
        this.mLetterboxUiController.destroy();
        this.waitingToShow = false;
        boolean z = isAnimating(7) && ((getParent() == null || !(getParent() instanceof TaskFragment) ? isAnimating(5) || inTransition() : getParent().isAnimating(5) || getParent().inTransition()) || (task = this.task) == null || !task.isVisible());
        if (getDisplayContent().mClosingApps.contains(this)) {
            if (this.app == null && this.mActivityRecordExt.isMirageWindowDisplayId(getDisplayContent().getDisplayId())) {
                z = false;
            }
            z = true;
        } else {
            if (!z && getDisplayContent().mAppTransition.isTransitionSet()) {
                getDisplayContent().mClosingApps.add(this);
            }
            z = true;
        }
        if (!z) {
            commitVisibility(false, true);
        } else {
            setVisibleRequested(false);
        }
        this.mTransitionController.collect(this);
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 1653210583, 204, (String) null, new Object[]{String.valueOf(this), Boolean.valueOf(z), String.valueOf(getAnimation()), Boolean.valueOf(isAnimating(3, 1))});
        }
        Object[] objArr = new Object[5];
        objArr[0] = this;
        objArr[1] = Boolean.valueOf(z);
        objArr[2] = getParent();
        objArr[3] = Boolean.valueOf(this.mTransitionController.inTransition() || inTransition());
        objArr[4] = Debug.getCallers(4);
        Slog.d(TAG, String.format("removeAppToken: %s delayed=%b parent=%s animating=%b Callers=%s", objArr));
        if (this.mStartingData != null) {
            removeStartingWindow();
        }
        if (isAnimating(3, 1) || inTransition()) {
            getDisplayContent().mNoAnimationNotifyOnTransitionFinished.add(this.token);
        }
        if (z && !isEmpty()) {
            if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -2109936758, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            this.mIsExiting = true;
        } else {
            cancelAnimation();
            removeIfPossible();
        }
        stopFreezingScreen(true, true);
        DisplayContent displayContent = getDisplayContent();
        if (displayContent.mFocusedApp == this) {
            if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -771177730, 4, (String) null, new Object[]{String.valueOf(this), Long.valueOf(displayContent.getDisplayId())});
            }
            displayContent.setFocusedApp(null);
            this.mWmService.updateFocusedWindowLocked(0, true);
        }
        if (!z) {
            updateReportedVisibilityLocked();
        }
        this.mDisplayContent.mPinnedTaskController.onActivityHidden(this.mActivityComponent);
        this.mDisplayContent.onRunningActivityChanged();
        this.mWmService.mEmbeddedWindowController.onActivityRemoved(this);
        this.mRemovingFromDisplay = false;
    }

    @Override // com.android.server.wm.WindowToken
    protected boolean isFirstChildWindowGreaterThanSecond(WindowState windowState, WindowState windowState2) {
        int i = windowState.mAttrs.type;
        int i2 = windowState2.mAttrs.type;
        if (i == 1 && i2 != 1) {
            return false;
        }
        if (i == 1 || i2 != 1) {
            return (i == 3 && i2 != 3) || i == 3 || i2 != 3;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasStartingWindow() {
        if (this.mStartingData != null) {
            return true;
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            if (getChildAt(size).mAttrs.type == 3) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLastWindow(WindowState windowState) {
        return this.mChildren.size() == 1 && this.mChildren.get(0) == windowState;
    }

    @Override // com.android.server.wm.WindowToken
    void addWindow(WindowState windowState) {
        super.addWindow(windowState);
        checkKeyguardFlagsChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void removeChild(WindowState windowState) {
        if (this.mChildren.contains(windowState)) {
            super.removeChild((ActivityRecord) windowState);
            checkKeyguardFlagsChanged();
            updateLetterboxSurface(windowState);
        }
    }

    void setAppLayoutChanges(int i, String str) {
        if (this.mChildren.isEmpty()) {
            return;
        }
        DisplayContent displayContent = getDisplayContent();
        int i2 = i | displayContent.pendingLayoutChanges;
        displayContent.pendingLayoutChanges = i2;
        if (WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
            this.mWmService.mWindowPlacerLocked.debugLayoutRepeats(str, i2);
        }
    }

    private boolean transferStartingWindow(ActivityRecord activityRecord) {
        WindowState windowState = activityRecord.mStartingWindow;
        if (windowState != null && activityRecord.mStartingSurface != null) {
            if (windowState.getParent() == null) {
                return false;
            }
            if (this.mStartingSurface != null || this.mStartingData != null) {
                Slog.v("WindowManager", "transferStartingWindow, fromToken already add a starting window.");
                removeStartingWindow();
            }
            if (activityRecord.mVisible) {
                this.mDisplayContent.mSkipAppTransitionAnimation = true;
            }
            Slog.d(TAG, "Moving existing StartingWindow " + windowState + " from " + activityRecord + " to " + this);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (activityRecord.hasFixedRotationTransform() && !this.mDisplayContent.handleTopActivityLaunchingInDifferentOrientation(this, false) && this.mDisplayContent.getDisplayRotation() != null && isActivityTypeStandardOrUndefined()) {
                    int rotation = this.mDisplayContent.getRotation();
                    int rotationForOrientation = this.mDisplayContent.getDisplayRotation().rotationForOrientation(getRequestedOrientation(), rotation);
                    if (WindowManagerDebugConfig.DEBUG_ANIM) {
                        Slog.d(TAG, "Try set fixed rotation to  " + this + " from " + rotation + " to " + rotationForOrientation + ", while fromActivity has finished FixedRotation and is waiting for remote rotation.");
                    }
                    if (rotation == rotationForOrientation) {
                        this.mDisplayContent.setFixedRotationLaunchingApp(this, rotationForOrientation);
                    }
                }
                this.mStartingData = activityRecord.mStartingData;
                this.mStartingSurface = activityRecord.mStartingSurface;
                this.mStartingWindow = windowState;
                this.reportedVisible = activityRecord.reportedVisible;
                if ((activityRecord.mStartingData instanceof SnapshotStartingData) && isInTransition()) {
                    activityRecord.mStartingData.mWaitForSyncTransactionCommit = false;
                }
                activityRecord.mStartingData = null;
                activityRecord.mStartingSurface = null;
                activityRecord.mStartingWindow = null;
                activityRecord.startingMoved = true;
                windowState.mToken = this;
                windowState.mActivityRecord = this;
                Slog.d(TAG, "Removing StartingWindow " + windowState + " from " + activityRecord);
                this.mTransitionController.collect(windowState);
                windowState.reparent(this, Integer.MAX_VALUE);
                windowState.clearFrozenInsetsState();
                if (activityRecord.allDrawn) {
                    this.allDrawn = true;
                }
                if (activityRecord.firstWindowDrawn) {
                    this.firstWindowDrawn = true;
                }
                if (activityRecord.isVisible()) {
                    setVisible(true);
                    setVisibleRequested(true);
                    this.mVisibleSetFromTransferredStartingWindow = true;
                }
                setClientVisible(activityRecord.isClientVisible());
                if (activityRecord.isAnimating() && !this.mActivityRecordExt.shouldBlockTransferAnimation(activityRecord, this.mAnimatingActivityRegistry)) {
                    if (activityRecord.mDeferAnimationFinish) {
                        Slog.e(TAG, "transferAnimation from=" + activityRecord + " which anim is deferfinished, should not transfer to " + this);
                    }
                    transferAnimation(activityRecord);
                    this.mTransitionChangeFlags |= 8;
                    AnimatingActivityRegistry animatingActivityRegistry = this.mAnimatingActivityRegistry;
                    if (animatingActivityRegistry != null) {
                        animatingActivityRegistry.notifyStarting(this);
                    }
                } else if (this.mTransitionController.getTransitionPlayer() != null) {
                    this.mTransitionChangeFlags |= 8;
                }
                activityRecord.postWindowRemoveStartingWindowCleanup();
                activityRecord.mVisibleSetFromTransferredStartingWindow = false;
                this.mWmService.updateFocusedWindowLocked(3, true);
                getDisplayContent().setLayoutNeeded();
                this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
                return true;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        if (activityRecord.mStartingData == null) {
            return false;
        }
        Slog.d(TAG, "Moving pending StartingWindow from " + activityRecord + " to " + this);
        this.mStartingData = activityRecord.mStartingData;
        activityRecord.mStartingData = null;
        activityRecord.startingMoved = true;
        this.mActivityRecordExt.transferPreloadedInfoIfNeed(activityRecord, this);
        scheduleAddStartingWindow();
        return true;
    }

    void transferStartingWindowFromHiddenAboveTokenIfNeeded() {
        WindowState findMainWindow = findMainWindow(false);
        if (findMainWindow == null || !findMainWindow.mWinAnimator.getShown()) {
            this.task.forAllActivities(new Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda8
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$transferStartingWindowFromHiddenAboveTokenIfNeeded$8;
                    lambda$transferStartingWindowFromHiddenAboveTokenIfNeeded$8 = ActivityRecord.this.lambda$transferStartingWindowFromHiddenAboveTokenIfNeeded$8((ActivityRecord) obj);
                    return lambda$transferStartingWindowFromHiddenAboveTokenIfNeeded$8;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$transferStartingWindowFromHiddenAboveTokenIfNeeded$8(ActivityRecord activityRecord) {
        if (activityRecord == this) {
            return true;
        }
        return !activityRecord.isVisibleRequested() && transferStartingWindow(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyguardLocked() {
        DisplayContent displayContent = this.mDisplayContent;
        return displayContent != null ? displayContent.isKeyguardLocked() : this.mRootWindowContainer.getDefaultDisplay().isKeyguardLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkKeyguardFlagsChanged() {
        boolean containsDismissKeyguardWindow = containsDismissKeyguardWindow();
        boolean containsShowWhenLockedWindow = containsShowWhenLockedWindow();
        if (containsDismissKeyguardWindow != this.mLastContainsDismissKeyguardWindow || containsShowWhenLockedWindow != this.mLastContainsShowWhenLockedWindow) {
            this.mDisplayContent.notifyKeyguardFlagsChanged();
        }
        this.mLastContainsDismissKeyguardWindow = containsDismissKeyguardWindow;
        this.mLastContainsShowWhenLockedWindow = containsShowWhenLockedWindow;
        this.mLastContainsTurnScreenOnWindow = containsTurnScreenOnWindow();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean containsDismissKeyguardWindow() {
        if (isRelaunching()) {
            return this.mLastContainsDismissKeyguardWindow;
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            if ((((WindowState) this.mChildren.get(size)).mAttrs.flags & 4194304) != 0) {
                return true;
            }
        }
        return false;
    }

    boolean containsShowWhenLockedWindow() {
        if (isRelaunching()) {
            return this.mLastContainsShowWhenLockedWindow;
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            if ((((WindowState) this.mChildren.get(size)).mAttrs.flags & 524288) != 0) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShowWhenLocked(boolean z) {
        Slog.d(TAG, "setShowWhenLocked  from:" + this.mShowWhenLocked + " to " + z + "," + this);
        this.mShowWhenLocked = z;
        this.mAtmService.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInheritShowWhenLocked(boolean z) {
        this.mInheritShownWhenLocked = z;
        this.mAtmService.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
    }

    private static boolean canShowWhenLocked(ActivityRecord activityRecord) {
        ActivityRecord activityBelow;
        if (activityRecord == null || activityRecord.getTaskFragment() == null) {
            return false;
        }
        if (!activityRecord.inPinnedWindowingMode() && (activityRecord.mShowWhenLocked || activityRecord.containsShowWhenLockedWindow())) {
            return true;
        }
        if (!activityRecord.mInheritShownWhenLocked || (activityBelow = activityRecord.getTaskFragment().getActivityBelow(activityRecord)) == null || activityBelow.inPinnedWindowingMode()) {
            return false;
        }
        return activityBelow.mShowWhenLocked || activityBelow.containsShowWhenLockedWindow();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canShowWhenLocked() {
        TaskFragment taskFragment = getTaskFragment();
        if (taskFragment == null || taskFragment.getAdjacentTaskFragment() == null || !taskFragment.isEmbedded()) {
            return canShowWhenLocked(this);
        }
        return canShowWhenLocked(this) && canShowWhenLocked(taskFragment.getAdjacentTaskFragment().getTopNonFinishingActivity());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canShowWindows() {
        boolean z;
        boolean isAnimating;
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            z = this.mSyncState != 1;
        } else {
            z = this.allDrawn;
        }
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            isAnimating = this.mTransitionController.inPlayingTransition(this) && !this.mActivityRecordExt.ignoreChangePlayingTransition(hasNonDefaultColorWindow());
        } else {
            isAnimating = isAnimating(2, 1);
        }
        if (z) {
            return (isAnimating && hasNonDefaultColorWindow() && !this.mActivityRecordExt.ignoreOplusAppPlayingTransition(this)) ? false : true;
        }
        return false;
    }

    private boolean hasNonDefaultColorWindow() {
        return forAllWindows(new ToBooleanFunction() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda29
            public final boolean apply(Object obj) {
                boolean lambda$hasNonDefaultColorWindow$9;
                lambda$hasNonDefaultColorWindow$9 = ActivityRecord.lambda$hasNonDefaultColorWindow$9((WindowState) obj);
                return lambda$hasNonDefaultColorWindow$9;
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$hasNonDefaultColorWindow$9(WindowState windowState) {
        return windowState.mAttrs.getColorMode() != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean forAllActivities(Predicate<ActivityRecord> predicate, boolean z) {
        return predicate.test(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void forAllActivities(Consumer<ActivityRecord> consumer, boolean z) {
        consumer.accept(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public ActivityRecord getActivity(Predicate<ActivityRecord> predicate, boolean z, ActivityRecord activityRecord) {
        if (predicate.test(this)) {
            return this;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logStartActivity(int i, Task task) {
        Uri data = this.intent.getData();
        EventLog.writeEvent(i, Integer.valueOf(this.mUserId), Integer.valueOf(System.identityHashCode(this)), Integer.valueOf(task.mTaskId), this.shortComponentName, this.intent.getAction(), this.intent.getType(), data != null ? data.toSafeString() : null, Integer.valueOf(this.intent.getFlags()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UriPermissionOwner getUriPermissionsLocked() {
        if (this.uriPermissions == null) {
            this.uriPermissions = new UriPermissionOwner(this.mAtmService.mUgmInternal, this);
        }
        return this.uriPermissions;
    }

    void addResultLocked(ActivityRecord activityRecord, String str, int i, int i2, Intent intent) {
        ActivityResult activityResult = new ActivityResult(activityRecord, str, i, i2, intent);
        if (this.results == null) {
            this.results = new ArrayList<>();
        }
        this.results.add(activityResult);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0031 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void removeResultsLocked(ActivityRecord activityRecord, String str, int i) {
        ArrayList<ResultInfo> arrayList = this.results;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                ActivityResult activityResult = (ActivityResult) this.results.get(size);
                if (activityResult.mFrom == activityRecord) {
                    String str2 = ((ResultInfo) activityResult).mResultWho;
                    if (str2 != null) {
                        if (!str2.equals(str)) {
                        }
                        if (((ResultInfo) activityResult).mRequestCode != i) {
                        }
                    } else {
                        if (str != null) {
                        }
                        if (((ResultInfo) activityResult).mRequestCode != i) {
                            this.results.remove(size);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendResult(int i, String str, int i2, int i3, Intent intent, NeededUriGrants neededUriGrants) {
        sendResult(i, str, i2, i3, intent, neededUriGrants, false);
    }

    void sendResult(int i, String str, int i2, int i3, Intent intent, NeededUriGrants neededUriGrants, boolean z) {
        if (i > 0) {
            this.mAtmService.mUgmInternal.grantUriPermissionUncheckedFromIntent(neededUriGrants, getUriPermissionsLocked());
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_RESULTS) {
            Slog.v(TAG, "Send activity result to " + this + " : who=" + str + " req=" + i2 + " res=" + i3 + " data=" + intent + " forceSendForMediaProjection=" + z);
        }
        if (isState(State.RESUMED) && attachedToProcess()) {
            try {
                ArrayList arrayList = new ArrayList();
                arrayList.add(new ResultInfo(str, i2, i3, intent));
                this.mAtmService.getLifecycleManager().scheduleTransaction(this.app.getThread(), this.token, (ClientTransactionItem) ActivityResultItem.obtain(arrayList));
                return;
            } catch (Exception e) {
                Slog.w(TAG, "Exception thrown sending result to " + this, e);
            }
        }
        if (z && attachedToProcess() && isState(State.STARTED, State.PAUSING, State.PAUSED, State.STOPPING, State.STOPPED)) {
            ClientTransaction obtain = ClientTransaction.obtain(this.app.getThread(), this.token);
            obtain.addCallback(ActivityResultItem.obtain(List.of(new ResultInfo(str, i2, i3, intent))));
            ActivityLifecycleItem lifecycleItemForCurrentStateForResult = getLifecycleItemForCurrentStateForResult();
            if (lifecycleItemForCurrentStateForResult != null) {
                obtain.setLifecycleStateRequest(lifecycleItemForCurrentStateForResult);
            } else {
                Slog.w(TAG, "Unable to get the lifecycle item for state " + this.mState + " so couldn't immediately send result");
            }
            try {
                this.mAtmService.getLifecycleManager().scheduleTransaction(obtain);
                return;
            } catch (RemoteException e2) {
                Slog.w(TAG, "Exception thrown sending result to " + this, e2);
                return;
            }
        }
        addResultLocked(null, str, i2, i3, intent);
    }

    private ActivityLifecycleItem getLifecycleItemForCurrentStateForResult() {
        int i = AnonymousClass6.$SwitchMap$com$android$server$wm$ActivityRecord$State[this.mState.ordinal()];
        if (i == 2 || i == 3) {
            return PauseActivityItem.obtain();
        }
        if (i != 4) {
            if (i == 5) {
                return StartActivityItem.obtain((ActivityOptions) null);
            }
            if (i != 6) {
                return null;
            }
        }
        return StopActivityItem.obtain(this.configChangeFlags);
    }

    private void addNewIntentLocked(ReferrerIntent referrerIntent) {
        if (this.newIntents == null) {
            this.newIntents = new ArrayList<>();
        }
        this.newIntents.add(referrerIntent);
    }

    final boolean isSleeping() {
        Task rootTask = getRootTask();
        return rootTask != null ? rootTask.shouldSleepActivities() : this.mAtmService.isSleepingLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void deliverNewIntentLocked(int i, Intent intent, NeededUriGrants neededUriGrants, String str) {
        this.mAtmService.mUgmInternal.grantUriPermissionUncheckedFromIntent(neededUriGrants, getUriPermissionsLocked());
        ReferrerIntent referrerIntent = new ReferrerIntent(intent, getFilteredReferrer(str));
        boolean z = false;
        boolean z2 = isTopRunningActivity() && isSleeping();
        State state = this.mState;
        State state2 = State.RESUMED;
        if ((state == state2 || state == State.PAUSED || z2) && attachedToProcess()) {
            try {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(referrerIntent);
                this.mAtmService.getLifecycleManager().scheduleTransaction(this.app.getThread(), this.token, (ClientTransactionItem) NewIntentItem.obtain(arrayList, this.mState == state2));
                try {
                    this.mActivityRecordExt.setLastIntentReceived(intent);
                } catch (RemoteException e) {
                    e = e;
                    Slog.w(TAG, "Exception thrown sending new intent to " + this, e);
                    if (z) {
                    }
                } catch (NullPointerException e2) {
                    e = e2;
                    Slog.w(TAG, "Exception thrown sending new intent to " + this, e);
                    if (z) {
                    }
                }
            } catch (RemoteException e3) {
                e = e3;
                z = true;
            } catch (NullPointerException e4) {
                e = e4;
                z = true;
            }
        } else {
            z = true;
        }
        if (z) {
            addNewIntentLocked(referrerIntent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateOptionsLocked(ActivityOptions activityOptions) {
        if (activityOptions != null) {
            if (ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
                Slog.i(TAG, "Update options for " + this);
            }
            ActivityOptions activityOptions2 = this.mPendingOptions;
            if (activityOptions2 != null) {
                activityOptions2.abort();
            }
            setOptions(activityOptions);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getLaunchedFromBubble() {
        return this.mLaunchedFromBubble;
    }

    private void setOptions(ActivityOptions activityOptions) {
        this.mLaunchedFromBubble = activityOptions.getLaunchedFromBubble();
        this.mPendingOptions = activityOptions;
        this.mActivityRecordExt.setLaunchDisplayId(activityOptions.getLaunchDisplayId());
        if (activityOptions.getAnimationType() == 13) {
            this.mPendingRemoteAnimation = activityOptions.getRemoteAnimationAdapter();
        }
        this.mPendingRemoteTransition = activityOptions.getRemoteTransition();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyOptionsAnimation() {
        if (ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
            Slog.i(TAG, "Applying options for " + this);
        }
        RemoteAnimationAdapter remoteAnimationAdapter = this.mPendingRemoteAnimation;
        if (remoteAnimationAdapter != null) {
            this.mDisplayContent.mAppTransition.overridePendingAppTransitionRemote(remoteAnimationAdapter);
            this.mTransitionController.setStatusBarTransitionDelay(remoteAnimationAdapter.getStatusBarTransitionDelay());
        } else {
            ActivityOptions activityOptions = this.mPendingOptions;
            if (activityOptions == null) {
                return;
            }
            if (activityOptions.getAnimationType() == 5) {
                this.mTransitionController.setOverrideAnimation(TransitionInfo.AnimationOptions.makeSceneTransitionAnimOptions(), null, null);
                return;
            }
            applyOptionsAnimation(this.mPendingOptions, this.intent);
        }
        clearOptionsAnimationForSiblings();
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0214  */
    /* JADX WARN: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void applyOptionsAnimation(ActivityOptions activityOptions, Intent intent) {
        IRemoteCallback iRemoteCallback;
        IRemoteCallback iRemoteCallback2;
        TransitionInfo.AnimationOptions makeScaleUpAnimOptions;
        int animationType = activityOptions.getAnimationType();
        DisplayContent displayContent = getDisplayContent();
        TransitionInfo.AnimationOptions animationOptions = null;
        if (animationType != -1 && animationType != 0) {
            if (animationType != 1) {
                if (animationType == 2) {
                    displayContent.mAppTransition.overridePendingAppTransitionScaleUp(activityOptions.getStartX(), activityOptions.getStartY(), activityOptions.getWidth(), activityOptions.getHeight());
                    makeScaleUpAnimOptions = TransitionInfo.AnimationOptions.makeScaleUpAnimOptions(activityOptions.getStartX(), activityOptions.getStartY(), activityOptions.getWidth(), activityOptions.getHeight());
                    if (intent.getSourceBounds() == null) {
                        intent.setSourceBounds(new Rect(activityOptions.getStartX(), activityOptions.getStartY(), activityOptions.getStartX() + activityOptions.getWidth(), activityOptions.getStartY() + activityOptions.getHeight()));
                    }
                } else if (animationType == 3 || animationType == 4) {
                    boolean z = animationType == 3;
                    HardwareBuffer thumbnail = activityOptions.getThumbnail();
                    displayContent.mAppTransition.overridePendingAppTransitionThumb(thumbnail, activityOptions.getStartX(), activityOptions.getStartY(), activityOptions.getAnimationStartedListener(), z);
                    TransitionInfo.AnimationOptions makeThumbnailAnimOptions = TransitionInfo.AnimationOptions.makeThumbnailAnimOptions(thumbnail, activityOptions.getStartX(), activityOptions.getStartY(), z);
                    IRemoteCallback animationStartedListener = activityOptions.getAnimationStartedListener();
                    if (intent.getSourceBounds() == null && thumbnail != null) {
                        intent.setSourceBounds(new Rect(activityOptions.getStartX(), activityOptions.getStartY(), activityOptions.getStartX() + thumbnail.getWidth(), activityOptions.getStartY() + thumbnail.getHeight()));
                    }
                    iRemoteCallback = animationStartedListener;
                    iRemoteCallback2 = null;
                    animationOptions = makeThumbnailAnimOptions;
                } else if (animationType == 8 || animationType == 9) {
                    AppTransitionAnimationSpec[] animSpecs = activityOptions.getAnimSpecs();
                    IAppTransitionAnimationSpecsFuture specsFuture = activityOptions.getSpecsFuture();
                    if (specsFuture != null) {
                        displayContent.mAppTransition.overridePendingAppTransitionMultiThumbFuture(specsFuture, activityOptions.getAnimationStartedListener(), animationType == 8);
                    } else if (animationType == 9 && animSpecs != null) {
                        displayContent.mAppTransition.overridePendingAppTransitionMultiThumb(animSpecs, activityOptions.getAnimationStartedListener(), activityOptions.getAnimationFinishedListener(), false);
                    } else {
                        displayContent.mAppTransition.overridePendingAppTransitionAspectScaledThumb(activityOptions.getThumbnail(), activityOptions.getStartX(), activityOptions.getStartY(), activityOptions.getWidth(), activityOptions.getHeight(), activityOptions.getAnimationStartedListener(), animationType == 8);
                        if (intent.getSourceBounds() == null) {
                            intent.setSourceBounds(new Rect(activityOptions.getStartX(), activityOptions.getStartY(), activityOptions.getStartX() + activityOptions.getWidth(), activityOptions.getStartY() + activityOptions.getHeight()));
                        }
                    }
                } else if (animationType == 11) {
                    displayContent.mAppTransition.overridePendingAppTransitionClipReveal(activityOptions.getStartX(), activityOptions.getStartY(), activityOptions.getWidth(), activityOptions.getHeight());
                    makeScaleUpAnimOptions = TransitionInfo.AnimationOptions.makeClipRevealAnimOptions(activityOptions.getStartX(), activityOptions.getStartY(), activityOptions.getWidth(), activityOptions.getHeight());
                    if (intent.getSourceBounds() == null) {
                        intent.setSourceBounds(new Rect(activityOptions.getStartX(), activityOptions.getStartY(), activityOptions.getStartX() + activityOptions.getWidth(), activityOptions.getStartY() + activityOptions.getHeight()));
                    }
                } else if (animationType == 12) {
                    displayContent.mAppTransition.overridePendingAppTransitionStartCrossProfileApps();
                    iRemoteCallback2 = null;
                    animationOptions = TransitionInfo.AnimationOptions.makeCrossProfileAnimOptions();
                    iRemoteCallback = null;
                } else {
                    Slog.e("WindowManager", "applyOptionsLocked: Unknown animationType=" + animationType);
                }
                iRemoteCallback = null;
                animationOptions = makeScaleUpAnimOptions;
                iRemoteCallback2 = iRemoteCallback;
            } else {
                displayContent.mAppTransition.overridePendingAppTransition(activityOptions.getPackageName(), activityOptions.getCustomEnterResId(), activityOptions.getCustomExitResId(), activityOptions.getCustomBackgroundColor(), activityOptions.getAnimationStartedListener(), activityOptions.getAnimationFinishedListener(), activityOptions.getOverrideTaskTransition());
                animationOptions = TransitionInfo.AnimationOptions.makeCustomAnimOptions(activityOptions.getPackageName(), activityOptions.getCustomEnterResId(), activityOptions.getCustomExitResId(), activityOptions.getCustomBackgroundColor(), activityOptions.getOverrideTaskTransition());
                iRemoteCallback = activityOptions.getAnimationStartedListener();
                iRemoteCallback2 = activityOptions.getAnimationFinishedListener();
            }
            if (animationOptions == null) {
                this.mTransitionController.setOverrideAnimation(animationOptions, iRemoteCallback, iRemoteCallback2);
                return;
            }
            return;
        }
        iRemoteCallback = null;
        iRemoteCallback2 = iRemoteCallback;
        if (animationOptions == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearAllDrawn() {
        this.allDrawn = false;
        this.mLastAllDrawn = false;
    }

    private boolean allDrawnStatesConsidered() {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            WindowState windowState = (WindowState) this.mChildren.get(size);
            if (windowState.mightAffectAllDrawn() && !windowState.getDrawnStateEvaluated()) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAllDrawn() {
        int i;
        if (this.allDrawn || (i = this.mNumInterestingWindows) <= 0 || !allDrawnStatesConsidered() || this.mNumDrawnWindows < i || isRelaunching()) {
            return;
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(TAG, "allDrawn: " + this + " interesting=" + i + " drawn=" + this.mNumDrawnWindows);
        }
        this.allDrawn = true;
        DisplayContent displayContent = this.mDisplayContent;
        if (displayContent != null) {
            displayContent.mUnknownAppVisibilityController.appRemovedOrHidden(this);
        }
        this.mActivityRecordExt.updateAllDrawnActivity(this);
        DisplayContent displayContent2 = this.mDisplayContent;
        if (displayContent2 != null) {
            displayContent2.setLayoutNeeded();
        }
        this.mWmService.mH.obtainMessage(32, this).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void abortAndClearOptionsAnimation() {
        ActivityOptions activityOptions = this.mPendingOptions;
        if (activityOptions != null) {
            activityOptions.abort();
        }
        clearOptionsAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearOptionsAnimation() {
        this.mPendingOptions = null;
        this.mPendingRemoteAnimation = null;
        this.mPendingRemoteTransition = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearOptionsAnimationForSiblings() {
        Task task = this.task;
        if (task == null) {
            clearOptionsAnimation();
        } else {
            task.forAllActivities(new Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda24
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((ActivityRecord) obj).clearOptionsAnimation();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityOptions getOptions() {
        return this.mPendingOptions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityOptions takeOptions() {
        if (ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
            Slog.i(TAG, "Taking options for " + this + " callers=" + Debug.getCallers(6));
        }
        ActivityOptions activityOptions = this.mPendingOptions;
        if (activityOptions == null) {
            return null;
        }
        this.mPendingOptions = null;
        activityOptions.setRemoteTransition(null);
        activityOptions.setRemoteAnimationAdapter(null);
        return activityOptions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteTransition takeRemoteTransition() {
        RemoteTransition remoteTransition = this.mPendingRemoteTransition;
        this.mPendingRemoteTransition = null;
        return remoteTransition;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allowMoveToFront() {
        ActivityOptions activityOptions = this.mPendingOptions;
        return activityOptions == null || !activityOptions.getAvoidMoveToFront();
    }

    void removeUriPermissionsLocked() {
        UriPermissionOwner uriPermissionOwner = this.uriPermissions;
        if (uriPermissionOwner != null) {
            uriPermissionOwner.removeUriPermissions();
            this.uriPermissions = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pauseKeyDispatchingLocked() {
        if (this.keysPaused) {
            return;
        }
        this.keysPaused = true;
        if (getDisplayContent() != null) {
            getDisplayContent().getInputMonitor().pauseDispatchingLw(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resumeKeyDispatchingLocked() {
        if (this.keysPaused) {
            this.keysPaused = false;
            if (getDisplayContent() != null) {
                getDisplayContent().getInputMonitor().resumeDispatchingLw(this);
            }
        }
    }

    private void updateTaskDescription(CharSequence charSequence) {
        this.task.lastDescription = charSequence;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeferHidingClient(boolean z) {
        if (this.mDeferHidingClient == z) {
            return;
        }
        this.mDeferHidingClient = z;
        if (z || this.mVisibleRequested) {
            return;
        }
        setVisibility(false);
    }

    boolean getDeferHidingClient() {
        return this.mDeferHidingClient;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canAffectSystemUiFlags() {
        Task task = this.task;
        return task != null && task.canAffectSystemUiFlags() && isVisible() && !inPinnedWindowingMode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean isVisible() {
        return this.mVisible;
    }

    void setVisible(boolean z) {
        if (z != this.mVisible) {
            this.mVisible = z;
            WindowProcessController windowProcessController = this.app;
            if (windowProcessController != null) {
                this.mTaskSupervisor.onProcessActivityStateChanged(windowProcessController, false);
            }
            scheduleAnimation();
            if (this.mVisible) {
                this.mActivityRecordExt.addVisibleWindow(getUid(), this.packageName, this.info.applicationInfo);
            } else {
                this.mActivityRecordExt.removeUnVisibleWindow(getUid(), this.packageName);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean setVisibleRequested(boolean z) {
        WindowState windowState;
        boolean z2 = false;
        if (!super.setVisibleRequested(z)) {
            return false;
        }
        setInsetsFrozen(!z);
        updateVisibleForServiceConnection();
        WindowProcessController windowProcessController = this.app;
        if (windowProcessController != null) {
            this.mTaskSupervisor.onProcessActivityStateChanged(windowProcessController, false);
        }
        logAppCompatState();
        if (!z) {
            InputTarget imeInputTarget = this.mDisplayContent.getImeInputTarget();
            if (imeInputTarget != null && imeInputTarget.getWindowState() != null && imeInputTarget.getWindowState().mActivityRecord == this && (windowState = this.mDisplayContent.mInputMethodWindow) != null && windowState.isVisible()) {
                z2 = true;
            }
            this.mLastImeShown = z2;
            finishOrAbortReplacingWindow();
        }
        this.mActivityRecordExt.setVisibleRequested(z, this.mTransitionController);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setVisibility(boolean z) {
        if (getParent() == null) {
            Slog.w("WindowManager", "Attempted to set visibility of non-existing app token: " + this.token);
            return;
        }
        if (z == this.mVisibleRequested && z == this.mVisible && z == isClientVisible() && this.mTransitionController.isShellTransitionsEnabled()) {
            return;
        }
        if (z) {
            this.mDeferHidingClient = false;
        }
        setVisibility(z, this.mDeferHidingClient);
        this.mAtmService.addWindowLayoutReasons(2);
        this.mTaskSupervisor.getActivityMetricsLogger().notifyVisibilityChanged(this);
        this.mTaskSupervisor.mAppVisibilitiesChangedSinceLastPause = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x01a0  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x01c6  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0119  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setVisibility(boolean z, boolean z2) {
        boolean z3;
        boolean z4;
        boolean z5;
        AppTransition appTransition = getDisplayContent().mAppTransition;
        if (!z && !this.mVisibleRequested) {
            if (z2 || !this.mLastDeferHidingClient) {
                return;
            }
            this.mLastDeferHidingClient = z2;
            setClientVisible(false);
            return;
        }
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -374767836, 972, (String) null, new Object[]{String.valueOf(this.token), Boolean.valueOf(z), String.valueOf(appTransition), Boolean.valueOf(isVisible()), Boolean.valueOf(this.mVisibleRequested), String.valueOf(Debug.getCallers(6))});
        }
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            z3 = this.mTransitionController.isCollecting();
            if (z3) {
                this.mTransitionController.collect(this);
                z4 = false;
            } else {
                z4 = this.mTransitionController.inFinishingTransition(this);
                z5 = this.mTransitionController.inPlayingTransition(this);
                if (!z4 && !this.mDisplayContent.isSleeping()) {
                    Slog.e(TAG, "setVisibility=" + z + " while transition is not collecting or finishing " + this + " caller=" + Debug.getCallers(8));
                    if (z) {
                        SurfaceControl.Transaction syncTransaction = getSyncTransaction();
                        getWrapper().getExtImpl().showInTransition(syncTransaction, this.mWmService, this.mSurfaceControl);
                        for (WindowContainer parent = getParent(); parent != null && parent != this.mDisplayContent; parent = parent.getParent()) {
                            SurfaceControl surfaceControl = parent.mSurfaceControl;
                            if (surfaceControl != null) {
                                syncTransaction.show(surfaceControl);
                            }
                        }
                    }
                }
                onChildVisibilityRequested(z);
                DisplayContent displayContent = getDisplayContent();
                displayContent.mOpeningApps.remove(this);
                displayContent.mClosingApps.remove(this);
                this.waitingToShow = false;
                setVisibleRequested(z);
                this.mLastDeferHidingClient = z2;
                if (z) {
                    if (this.finishing || isState(State.STOPPED)) {
                        displayContent.mUnknownAppVisibilityController.appRemovedOrHidden(this);
                    }
                    if (this.startingMoved && !this.firstWindowDrawn && hasChild()) {
                        setClientVisible(false);
                    }
                } else {
                    if (!appTransition.isTransitionSet() && appTransition.isReady()) {
                        if (DEBUG_PANIC) {
                            Slog.d(TAG, "setAppVisibility, adding " + this + " to mOpeningApps, isTransitionSet()=" + appTransition.isTransitionSet() + ", isReady()=" + appTransition.isReady());
                        }
                        displayContent.mOpeningApps.add(this);
                    }
                    this.startingMoved = false;
                    if (!isVisible() || this.mAppStopped) {
                        clearAllDrawn();
                        if (!isVisible()) {
                            this.waitingToShow = true;
                            if (!isClientVisible()) {
                                forAllWindows(new Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda1
                                    @Override // java.util.function.Consumer
                                    public final void accept(Object obj) {
                                        ActivityRecord.this.lambda$setVisibility$10((WindowState) obj);
                                    }
                                }, true);
                            }
                        }
                    }
                    setClientVisible(true);
                    requestUpdateWallpaperIfNeeded();
                    if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1224184681, 0, (String) null, new Object[]{String.valueOf(this)});
                    }
                    this.mAppStopped = false;
                    transferStartingWindowFromHiddenAboveTokenIfNeeded();
                }
                if (!z3) {
                    if (!z && this.mTransitionController.inPlayingTransition(this)) {
                        this.mTransitionChangeFlags |= 32768;
                    }
                    Slog.d(TAG, String.format("setVisibility(%s, visible=%b) defer commitVisibility for transition collecting", this.token, Boolean.valueOf(z)));
                    return;
                }
                if (z4) {
                    this.mTransitionController.mValidateCommitVis.add(this);
                    Slog.d(TAG, String.format("setVisibility(%s, visible=%b) defer commitVisibility for inFinishingTransition", this.token, Boolean.valueOf(z)));
                    return;
                }
                if (z5) {
                    Slog.d(TAG, "setVisibility defer change visibility for:" + this + " visible:" + z);
                    this.mTransitionController.mStateValidators.add(new Runnable() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            ActivityRecord.this.lambda$setVisibility$11();
                        }
                    });
                }
                if (deferCommitVisibilityChange(z)) {
                    return;
                }
                commitVisibility(z, true);
                updateReportedVisibilityLocked();
                return;
            }
        } else {
            z3 = false;
            z4 = false;
        }
        z5 = z4;
        onChildVisibilityRequested(z);
        DisplayContent displayContent2 = getDisplayContent();
        displayContent2.mOpeningApps.remove(this);
        displayContent2.mClosingApps.remove(this);
        this.waitingToShow = false;
        setVisibleRequested(z);
        this.mLastDeferHidingClient = z2;
        if (z) {
        }
        if (!z3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setVisibility$10(WindowState windowState) {
        if (windowState.mWinAnimator.mDrawState != 4 || this.mActivityRecordExt.isWindowSurfaceSaved(windowState)) {
            return;
        }
        windowState.mWinAnimator.resetDrawState();
        windowState.forceReportingResized();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setVisibility$11() {
        Slog.d(TAG, "setVisibility after transition for:" + this);
        if (!isVisibleRequested() || this.mSurfaceControl == null || this.mDisplayContent == null) {
            return;
        }
        getSyncTransaction().show(this.mSurfaceControl);
        this.mLastSurfaceShowing = true;
        for (WindowContainer parent = getParent(); parent != null && parent != this.mDisplayContent; parent = parent.getParent()) {
            if (parent.mSurfaceControl != null) {
                parent.getSyncTransaction().show(parent.mSurfaceControl);
                Task asTask = parent.asTask();
                if (asTask != null) {
                    asTask.mLastSurfaceShowing = true;
                }
            }
        }
        scheduleAnimation();
    }

    private boolean deferCommitVisibilityChange(boolean z) {
        WindowState findFocusedWindow;
        ActivityRecord activityRecord;
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            return false;
        }
        if (!this.mDisplayContent.mAppTransition.isTransitionSet() && (isActivityTypeHome() || !isAnimating(2, 8))) {
            return false;
        }
        if (this.mWaitForEnteringPinnedMode && this.mVisible == z) {
            return false;
        }
        if (!okToAnimate(true, canTurnScreenOn() || this.mTaskSupervisor.getKeyguardController().isKeyguardGoingAway(this.mDisplayContent.mDisplayId)) || this.mActivityRecordExt.shouldSkipAppTransition(this)) {
            return false;
        }
        if (z) {
            if (DEBUG_PANIC) {
                Slog.d(TAG, "setAppVisibility, adding " + this + " to mOpeningApps, visible=" + z);
            }
            this.mDisplayContent.mOpeningApps.add(this);
            this.mEnteringAnimation = true;
        } else if (this.mVisible) {
            this.mDisplayContent.mClosingApps.add(this);
            this.mEnteringAnimation = false;
        }
        if ((this.mDisplayContent.mAppTransition.getTransitFlags() & 32) != 0 && (findFocusedWindow = this.mDisplayContent.findFocusedWindow()) != null && (activityRecord = findFocusedWindow.mActivityRecord) != null) {
            if (DEBUG_PANIC) {
                Slog.d(TAG, "setAppVisibility, TransitFlags=TRANSIT_FLAG_OPEN_BEHIND，  adding " + activityRecord + " to mOpeningApps");
            }
            this.mDisplayContent.mOpeningApps.add(activityRecord);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean applyAnimation(WindowManager.LayoutParams layoutParams, int i, boolean z, boolean z2, ArrayList<WindowContainer> arrayList) {
        if ((this.mTransitionChangeFlags & 8) != 0) {
            return false;
        }
        this.mRequestForceTransition = false;
        return super.applyAnimation(layoutParams, i, z, z2, arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void commitVisibility(boolean z, boolean z2, boolean z3) {
        this.mActivityRecordExt.resetUseTransferredAnimIfRequired(this.mVisibleSetFromTransferredStartingWindow, z);
        this.mVisibleSetFromTransferredStartingWindow = false;
        if (z == isVisible()) {
            return;
        }
        int size = this.mChildren.size();
        boolean isAnimating = WindowManagerService.sEnableShellTransitions ? z : isAnimating(2, 1);
        for (int i = 0; i < size; i++) {
            ((WindowState) this.mChildren.get(i)).onAppVisibilityChanged(z, isAnimating);
        }
        setVisible(z);
        setVisibleRequested(z);
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -636553602, 1020, (String) null, new Object[]{String.valueOf(this), Boolean.valueOf(isVisible()), Boolean.valueOf(this.mVisibleRequested), Boolean.valueOf(isInTransition()), Boolean.valueOf(isAnimating), String.valueOf(Debug.getCallers(5))});
        }
        if (!isAnimating(2) && z2 && !z && !inPinnedWindowingMode()) {
            this.mWmService.mSnapshotController.notifyAppVisibilityChanged(this, z);
        }
        this.mActivityRecordExt.notifyActivityRecordVisible(this, z);
        if (!z) {
            stopFreezingScreen(true, true);
        } else {
            WindowState windowState = this.mStartingWindow;
            if (windowState != null && !windowState.isDrawn() && ((this.firstWindowDrawn || this.allDrawn) && this.mActivityRecordExt.shouldClearStartingPolicyVisibility(this))) {
                this.mStartingWindow.clearPolicyVisibilityFlag(1);
                this.mStartingWindow.mLegacyPolicyVisibilityAfterAnim = false;
            }
            final WindowManagerService windowManagerService = this.mWmService;
            Objects.requireNonNull(windowManagerService);
            forAllWindows(new Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda25
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    WindowManagerService.this.makeWindowFreezingScreenIfNeededLocked((WindowState) obj);
                }
            }, true);
        }
        for (Task organizedTask = getOrganizedTask(); organizedTask != null; organizedTask = organizedTask.getParent().asTask()) {
            organizedTask.dispatchTaskInfoChangedIfNeeded(false);
        }
        DisplayContent displayContent = getDisplayContent();
        displayContent.getInputMonitor().setUpdateInputWindowsNeededLw();
        if (z2) {
            this.mWmService.updateFocusedWindowLocked(3, false);
            this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
        }
        displayContent.getInputMonitor().updateInputWindowsLw(false);
        this.mTransitionChangeFlags = 0;
        postApplyAnimation(z, z3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void commitVisibility(boolean z, boolean z2) {
        commitVisibility(z, z2, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setNeedsLetterboxedAnimation(boolean z) {
        this.mNeedsLetterboxedAnimation = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNeedsLetterboxedAnimation() {
        return this.mNeedsLetterboxedAnimation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInLetterboxAnimation() {
        return this.mNeedsLetterboxedAnimation && isAnimating();
    }

    private void postApplyAnimation(boolean z, boolean z2) {
        WindowState wallpaperTarget;
        boolean isShellTransitionsEnabled = this.mTransitionController.isShellTransitionsEnabled();
        boolean z3 = !isShellTransitionsEnabled && isAnimating(6, 25);
        if (!z3 && !isShellTransitionsEnabled) {
            onAnimationFinished(1, null);
            if (z) {
                this.mEnteringAnimation = true;
                this.mWmService.mActivityManagerAppTransitionNotifier.onAppTransitionFinishedLocked(this.token);
            }
        }
        if (z || (this.mState != State.RESUMED && (isShellTransitionsEnabled || !isAnimating(2, 9)))) {
            setClientVisible(z);
        }
        DisplayContent displayContent = getDisplayContent();
        if (!z) {
            this.mImeInsetsFrozenUntilStartInput = true;
            if (isShellTransitionsEnabled && (wallpaperTarget = displayContent.mWallpaperController.getWallpaperTarget()) != null && wallpaperTarget.mActivityRecord == this) {
                displayContent.mWallpaperController.hideWallpapers(wallpaperTarget);
            }
        }
        if (!displayContent.mClosingApps.contains(this) && !displayContent.mOpeningApps.contains(this) && !z2) {
            this.mWmService.mSnapshotController.notifyAppVisibilityChanged(this, z);
        }
        if (isShellTransitionsEnabled || isVisible() || z3 || displayContent.mAppTransition.isTransitionSet()) {
            return;
        }
        SurfaceControl.openTransaction();
        try {
            forAllWindows(new Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda28
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ActivityRecord.lambda$postApplyAnimation$12((WindowState) obj);
                }
            }, true);
        } finally {
            SurfaceControl.closeTransaction();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$postApplyAnimation$12(WindowState windowState) {
        windowState.mWinAnimator.hide(SurfaceControl.getGlobalTransaction(), "immediately hidden");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void commitFinishDrawing(SurfaceControl.Transaction transaction) {
        boolean z = false;
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            z |= ((WindowState) this.mChildren.get(size)).commitFinishDrawing(transaction);
        }
        if (z) {
            requestUpdateWallpaperIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldApplyAnimation(boolean z) {
        if (this.mActivityRecordExt.shouldApplyAnimation(this, z)) {
            return isVisible() != z || this.mRequestForceTransition || (!isVisible() && this.mIsExiting);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRecentsScreenshotEnabled(boolean z) {
        this.mEnableRecentsScreenshot = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldUseAppThemeSnapshot() {
        return !this.mEnableRecentsScreenshot || forAllWindows(new ToBooleanFunction() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda27
            public final boolean apply(Object obj) {
                boolean lambda$shouldUseAppThemeSnapshot$13;
                lambda$shouldUseAppThemeSnapshot$13 = ActivityRecord.this.lambda$shouldUseAppThemeSnapshot$13((WindowState) obj);
                return lambda$shouldUseAppThemeSnapshot$13;
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$shouldUseAppThemeSnapshot$13(WindowState windowState) {
        return this.mActivityRecordExt.shouldUseAppThemeSnapshot(windowState, windowState.isSecureLocked());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCurrentLaunchCanTurnScreenOn(boolean z) {
        this.mCurrentLaunchCanTurnScreenOn = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean currentLaunchCanTurnScreenOn() {
        return this.mCurrentLaunchCanTurnScreenOn;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00c2, code lost:
    
        if (r11 != 8) goto L67;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setState(State state, String str) {
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 1316533291, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(getState()), String.valueOf(state), String.valueOf(str)});
        }
        if (state == this.mState && !this.mActivityRecordExt.updateActvityState(this)) {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -926231510, 0, (String) null, new Object[]{String.valueOf(state)});
            }
            this.mActivityRecordExt.updateActivityStateChanged(this, getTaskFragment(), state, str);
            return;
        }
        State state2 = this.mState;
        this.mState = state;
        this.mActivityRecordExt.updateActvityResumeTimeStamp(this);
        callServiceTrackeronActivityStatechange(state, false);
        if (getTaskFragment() != null) {
            getTaskFragment().onActivityStateChanged(this, state, str);
        }
        if (state == State.STOPPING && !isSleeping() && getParent() == null) {
            Slog.w("WindowManager", "Attempted to notify stopping on non-existing app token: " + this.token);
            return;
        }
        updateVisibleForServiceConnection();
        WindowProcessController windowProcessController = this.app;
        if (windowProcessController != null) {
            this.mTaskSupervisor.onProcessActivityStateChanged(windowProcessController, false);
        }
        this.mActivityRecordExt.hookLifecyclePause(str, this.shortComponentName, state.toString());
        int i = AnonymousClass6.$SwitchMap$com$android$server$wm$ActivityRecord$State[state.ordinal()];
        if (i == 1) {
            this.mAtmService.updateBatteryStats(this, true);
            this.mAtmService.updateActivityUsageStats(this, 1);
        } else {
            if (i == 3) {
                this.mAtmService.updateBatteryStats(this, false);
                this.mAtmService.updateActivityUsageStats(this, 2);
                this.mActivityRecordExt.notifyActivityPaused(this.task, this);
            } else if (i != 5) {
                if (i == 6) {
                    this.mAtmService.updateActivityUsageStats(this, 23);
                } else {
                    if (i == 7) {
                        if (this.app != null && (this.mVisible || this.mVisibleRequested)) {
                            this.mAtmService.updateBatteryStats(this, false);
                        }
                        this.mAtmService.updateActivityUsageStats(this, 24);
                    }
                    WindowProcessController windowProcessController2 = this.app;
                    if (windowProcessController2 != null && !windowProcessController2.hasActivities()) {
                        this.app.updateProcessInfo(true, false, true, false);
                    }
                }
            }
            this.mActivityRecordExt.setStateForVisible(state2, state, getUid(), this.packageName, this.info.applicationInfo, Integer.toHexString(System.identityHashCode(this)));
        }
        WindowProcessController windowProcessController3 = this.app;
        if (windowProcessController3 != null) {
            windowProcessController3.updateProcessInfo(false, true, true, true);
        }
        ContentCaptureManagerInternal contentCaptureManagerInternal = (ContentCaptureManagerInternal) LocalServices.getService(ContentCaptureManagerInternal.class);
        if (contentCaptureManagerInternal != null) {
            contentCaptureManagerInternal.notifyActivityEvent(this.mUserId, this.mActivityComponent, DESTROY_TIMEOUT, new ActivityId(getTask() != null ? getTask().mTaskId : -1, this.shareableActivityToken));
        }
        this.mActivityRecordExt.setStateForVisible(state2, state, getUid(), this.packageName, this.info.applicationInfo, Integer.toHexString(System.identityHashCode(this)));
    }

    void callServiceTrackeronActivityStatechange(State state, boolean z) {
        this.mAtmService.mTaskSupervisor.notifyServiceTracker(state, z, this, this.createTime);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public State getState() {
        return this.mState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isState(State state) {
        return state == this.mState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isState(State state, State state2) {
        State state3 = this.mState;
        return state == state3 || state2 == state3;
    }

    boolean isState(State state, State state2, State state3) {
        State state4 = this.mState;
        return state == state4 || state2 == state4 || state3 == state4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isState(State state, State state2, State state3, State state4) {
        State state5 = this.mState;
        return state == state5 || state2 == state5 || state3 == state5 || state4 == state5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isState(State state, State state2, State state3, State state4, State state5) {
        State state6 = this.mState;
        return state == state6 || state2 == state6 || state3 == state6 || state4 == state6 || state5 == state6;
    }

    boolean isState(State state, State state2, State state3, State state4, State state5, State state6) {
        State state7 = this.mState;
        return state == state7 || state2 == state7 || state3 == state7 || state4 == state7 || state5 == state7 || state6 == state7;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroySurfaces() {
        destroySurfaces(false);
    }

    private void destroySurfaces(boolean z) {
        ArrayList arrayList = new ArrayList(this.mChildren);
        boolean z2 = false;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            WindowState windowState = (WindowState) arrayList.get(size);
            if (!this.mActivityRecordExt.handleDestroySurfaces(this.packageName, windowState.mAttrs.type)) {
                z2 |= windowState.destroySurface(z, this.mAppStopped);
            }
        }
        if (z2) {
            getDisplayContent().assignWindowLayers(true);
            updateLetterboxSurface(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyAppResumed() {
        if (getParent() == null) {
            Slog.w("WindowManager", "Attempted to notify resumed of non-existing app token: " + this.token);
            return;
        }
        boolean z = this.mAppStopped;
        if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1364498663, 3, (String) null, new Object[]{Boolean.valueOf(z), String.valueOf(this)});
        }
        this.mAppStopped = false;
        if (this.mAtmService.getActivityStartController().isInExecution()) {
            setCurrentLaunchCanTurnScreenOn(true);
        }
        if (z) {
            return;
        }
        destroySurfaces(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyUnknownVisibilityLaunchedForKeyguardTransition() {
        if (this.noDisplay || !isKeyguardLocked()) {
            return;
        }
        this.mDisplayContent.mUnknownAppVisibilityController.notifyLaunched(this);
    }

    private boolean shouldBeVisible(boolean z, boolean z2) {
        if (this.mActivityRecordExt.shouldBeVisible(z, getDisplayId())) {
            return true;
        }
        updateVisibilityIgnoringKeyguard(z);
        if (z2) {
            return this.visibleIgnoringKeyguard;
        }
        return shouldBeVisibleUnchecked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldBeVisibleUnchecked() {
        Task rootTask = getRootTask();
        if (isActivityTypeHome() && ((IMirageWindowManagerExt) ExtLoader.type(IMirageWindowManagerExt.class).create()).shouldForceLauncherVisible()) {
            return true;
        }
        if (this.mActivityRecordExt.shouldMakeHomeActivityVisibleOnSecondary(this, this.mTaskSupervisor.getKeyguardController())) {
            Slog.d(TAG, "activity: " + this + " shouldBeVisibleUnchecked");
            return true;
        }
        if (rootTask == null || !this.visibleIgnoringKeyguard) {
            return false;
        }
        if ((inPinnedWindowingMode() && rootTask.isForceHidden()) || hasOverlayOverUntrustedModeEmbedded()) {
            return false;
        }
        if (this.mActivityRecordExt.shouldBeVisible(!this.visibleIgnoringKeyguard, getDisplayId())) {
            return true;
        }
        if (this.mActivityRecordExt.isForceHidden()) {
            return false;
        }
        if (this.mDisplayContent.isSleeping()) {
            return canTurnScreenOn();
        }
        if (this.mActivityRecordExt.skipCheckKeyguardVisibility()) {
            return true;
        }
        return this.mTaskSupervisor.getKeyguardController().checkKeyguardVisibility(this);
    }

    boolean hasOverlayOverUntrustedModeEmbedded() {
        return (!isEmbeddedInUntrustedMode() || getTask() == null || getTask().getActivity(new Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$hasOverlayOverUntrustedModeEmbedded$14;
                lambda$hasOverlayOverUntrustedModeEmbedded$14 = ActivityRecord.this.lambda$hasOverlayOverUntrustedModeEmbedded$14((ActivityRecord) obj);
                return lambda$hasOverlayOverUntrustedModeEmbedded$14;
            }
        }, this, false, false) == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$hasOverlayOverUntrustedModeEmbedded$14(ActivityRecord activityRecord) {
        return (activityRecord.finishing || activityRecord.getUid() == getUid()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateVisibilityIgnoringKeyguard(boolean z) {
        this.visibleIgnoringKeyguard = (!z || this.mLaunchTaskBehind) && showToCurrentUser();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldBeVisible() {
        Task task = getTask();
        if (task == null) {
            return false;
        }
        return shouldBeVisible((task.shouldBeVisible(null) && task.getOccludingActivityAbove(this) == null) ? false : true, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeVisibleIfNeeded(ActivityRecord activityRecord, boolean z) {
        getRootTask();
        if ((this.mState == State.RESUMED && this.mVisibleRequested) || this == activityRecord) {
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.d(TAG_VISIBILITY, "Not making visible, r=" + this + " state=" + this.mState + " starting=" + activityRecord);
                return;
            }
            return;
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(TAG_VISIBILITY, "Making visible and scheduling visibility: " + this);
        }
        Task rootTask = getRootTask();
        try {
            if (rootTask.mTranslucentActivityWaiting != null) {
                updateOptionsLocked(this.returningOptions);
                rootTask.mUndrawnActivitiesBelowTopTranslucent.add(this);
            }
            setVisibility(true);
            this.app.postPendingUiCleanMsg(true);
            if (z) {
                this.mClientVisibilityDeferred = false;
                makeActiveIfNeeded(activityRecord);
            } else {
                this.mClientVisibilityDeferred = true;
            }
            this.mTaskSupervisor.mStoppingActivities.remove(this);
        } catch (Exception e) {
            Slog.w(TAG, "Exception thrown making visible: " + this.intent.getComponent(), e);
        }
        handleAlreadyVisible();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeInvisible() {
        if (!this.mVisibleRequested) {
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(TAG_VISIBILITY, "Already invisible: " + this);
                return;
            }
            return;
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(TAG_VISIBILITY, "Making invisible: " + this + ", state=" + getState());
        }
        try {
            boolean checkEnterPictureInPictureState = checkEnterPictureInPictureState("makeInvisible", true);
            boolean z = checkEnterPictureInPictureState && !isState(State.STARTED, State.STOPPING, State.STOPPED, State.PAUSED);
            setDeferHidingClient(z);
            setVisibility(false);
            this.mActivityRecordExt.clearAccessControlPassPackages(this.task, this.packageName, this.mUserId);
            int i = AnonymousClass6.$SwitchMap$com$android$server$wm$ActivityRecord$State[getState().ordinal()];
            if (i != 9) {
                switch (i) {
                    case 1:
                        if (z && this.mActivityRecordExt.isMirageWindowDisplayId(getDisplayContent().getDisplayId())) {
                            getTaskFragment().startPausing(false, null, "makeInvisible");
                            return;
                        }
                        break;
                    case 2:
                    case 3:
                    case 5:
                        break;
                    case 4:
                    case 6:
                        this.supportsEnterPipOnTaskSwitch = false;
                        return;
                    default:
                        return;
                }
            }
            addToStopping(true, checkEnterPictureInPictureState, "makeInvisible");
        } catch (Exception e) {
            Slog.w(TAG, "Exception thrown making hidden: " + this.intent.getComponent(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean makeActiveIfNeeded(ActivityRecord activityRecord) {
        if (shouldResumeActivity(activityRecord)) {
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(TAG_VISIBILITY, "Resume visible activity, " + this);
            }
            return getRootTask().resumeTopActivityUncheckedLocked(activityRecord, null);
        }
        if (shouldPauseActivity(activityRecord)) {
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(TAG_VISIBILITY, "Pause visible activity, " + this);
            }
            State state = State.PAUSING;
            callServiceTrackeronActivityStatechange(state, true);
            setState(state, "makeActiveIfNeeded");
            this.mActivityRecordExt.makeActiveIfNeeded(getUid(), this.packageName, this.info.applicationInfo);
            this.mActivityRecordExt.hookSetBinderUxFlag(true, this);
            EventLogTags.writeWmPauseActivity(this.mUserId, System.identityHashCode(this), this.shortComponentName, "userLeaving=false", "make-active");
            try {
                Trace.traceBegin(32L, "cmz.mtk.makeActiveIfNeeded.activityPaused");
                this.mAtmService.mSocExt.onActivityStateChanged(this, false);
                Trace.traceEnd(32L);
                this.mAtmService.getLifecycleManager().scheduleTransaction(this.app.getThread(), this.token, (ActivityLifecycleItem) PauseActivityItem.obtain(this.finishing, false, this.configChangeFlags, false, this.mAutoEnteringPip));
            } catch (Exception e) {
                Slog.w(TAG, "Exception thrown sending pause: " + this.intent.getComponent(), e);
            }
            this.mActivityRecordExt.hookSetBinderUxFlag(false, this);
        } else if (shouldStartActivity()) {
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(TAG_VISIBILITY, "Start visible activity, " + this);
            }
            State changeStartActiveStateIfNeed = getWrapper().getExtImpl().changeStartActiveStateIfNeed(State.STARTED);
            callServiceTrackeronActivityStatechange(changeStartActiveStateIfNeed, true);
            setState(changeStartActiveStateIfNeed, "makeActiveIfNeeded");
            this.mActivityRecordExt.makeActiveIfNeeded(getUid(), this.packageName, this.info.applicationInfo);
            this.mActivityRecordSocExt.acquireActivityBoost(this.packageName, this.app, this.info, this.mAtmService, this.processName);
            try {
                this.mAtmService.getLifecycleManager().scheduleTransaction(this.app.getThread(), this.token, (ActivityLifecycleItem) StartActivityItem.obtain(takeOptions()));
            } catch (Exception e2) {
                Slog.w(TAG, "Exception thrown sending start: " + this.intent.getComponent(), e2);
                this.mActivityRecordSocExt.releaseActivityBoost();
            }
            this.mTaskSupervisor.mStoppingActivities.remove(this);
        }
        return false;
    }

    @VisibleForTesting
    boolean shouldPauseActivity(ActivityRecord activityRecord) {
        return shouldMakeActive(activityRecord) && !isFocusable() && !isState(State.PAUSING, State.PAUSED) && this.results == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean shouldResumeActivity(ActivityRecord activityRecord) {
        return shouldBeResumed(activityRecord) && !isState(State.RESUMED);
    }

    private boolean shouldBeResumed(ActivityRecord activityRecord) {
        return shouldMakeActive(activityRecord) && isFocusable() && getTaskFragment().getVisibility(activityRecord) == 0 && canResumeByCompat();
    }

    private boolean shouldStartActivity() {
        return this.mVisibleRequested && (isState(State.STOPPED) || isState(State.STOPPING));
    }

    @VisibleForTesting
    boolean shouldMakeActive(ActivityRecord activityRecord) {
        if (!isState(State.STARTED, State.RESUMED, State.PAUSED, State.STOPPED, State.STOPPING) || getRootTask().mTranslucentActivityWaiting != null || this == activityRecord || !this.mTaskSupervisor.readyToResume() || this.mLaunchTaskBehind) {
            return false;
        }
        if (this.task.hasChild(this)) {
            return getWrapper().getExtImpl().inOplusCompactWindowMode(true) ? getTask().topRunningActivity() == this : getTaskFragment().topRunningActivity() == this;
        }
        throw new IllegalStateException("Activity not found in its task");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleAlreadyVisible() {
        stopFreezingScreenLocked(false);
        try {
            if (this.returningOptions != null) {
                this.app.getThread().scheduleOnNewActivityOptions(this.token, this.returningOptions.toBundle());
            }
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void activityResumedLocked(IBinder iBinder, boolean z) {
        ActivityRecord forTokenLocked = forTokenLocked(iBinder);
        if (forTokenLocked == null) {
            return;
        }
        if (DEBUG_PANIC) {
            Slog.d(TAG, "activityResumedLocked, token=" + iBinder + ", handleSplashScreenExit=" + z + ", r =" + forTokenLocked + ", callers=" + Debug.getCallers(5));
        }
        forTokenLocked.setCustomizeSplashScreenExitAnimation(z);
        forTokenLocked.setSavedState(null);
        forTokenLocked.mDisplayContent.handleActivitySizeCompatModeIfNeeded(forTokenLocked);
        forTokenLocked.mDisplayContent.mUnknownAppVisibilityController.notifyAppResumedFinished(forTokenLocked);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void activityRefreshedLocked(IBinder iBinder) {
        DisplayRotationCompatPolicy displayRotationCompatPolicy;
        ActivityRecord forTokenLocked = forTokenLocked(iBinder);
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_STATES, -1480918485, 0, (String) null, new Object[]{String.valueOf(forTokenLocked)});
        }
        if (forTokenLocked == null || (displayRotationCompatPolicy = forTokenLocked.mDisplayContent.mDisplayRotationCompatPolicy) == null) {
            return;
        }
        displayRotationCompatPolicy.lambda$onActivityConfigurationChanging$0(forTokenLocked);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void splashScreenAttachedLocked(IBinder iBinder) {
        ActivityRecord forTokenLocked = forTokenLocked(iBinder);
        if (forTokenLocked == null) {
            Slog.w(TAG, "splashScreenTransferredLocked cannot find activity");
        } else {
            forTokenLocked.onSplashScreenAttachComplete();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void completeResumeLocked() {
        boolean z = this.mVisibleRequested;
        setVisibility(true);
        if (!z) {
            this.mTaskSupervisor.mAppVisibilitiesChangedSinceLastPause = true;
        }
        this.idle = false;
        this.results = null;
        ArrayList<ReferrerIntent> arrayList = this.newIntents;
        if (arrayList != null && arrayList.size() > 0) {
            ArrayList<ReferrerIntent> arrayList2 = this.newIntents;
            Intent intent = arrayList2.get(arrayList2.size() - 1);
            this.mLastNewIntent = intent;
            this.mActivityRecordExt.setLastIntentReceived(intent);
        }
        this.newIntents = null;
        if (isActivityTypeHome()) {
            this.mTaskSupervisor.updateHomeProcess(this.task.getBottomMostActivity().app);
        }
        if (this.nowVisible) {
            this.mTaskSupervisor.stopWaitingForActivityVisible(this);
        }
        this.mTaskSupervisor.scheduleIdleTimeout(this);
        this.mTaskSupervisor.reportResumedActivityLocked(this);
        resumeKeyDispatchingLocked();
        Task rootTask = getRootTask();
        this.mTaskSupervisor.mNoAnimActivities.clear();
        this.returningOptions = null;
        if (canTurnScreenOn()) {
            if ("com.google.android.dialer".equals(this.packageName)) {
                this.mTaskSupervisor.wakeUp("turnScreenOnFlag:googledialer");
            } else {
                this.mTaskSupervisor.wakeUp("turnScreenOnFlag");
            }
        } else {
            rootTask.checkReadyForSleep();
        }
        this.mAtmService.mH.post(new Runnable() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                ActivityRecord.this.lambda$completeResumeLocked$15();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$completeResumeLocked$15() {
        this.mActivityRecordExt.updateAllTopApps();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void activityPaused(boolean z) {
        if (DEBUG_PANIC) {
            Slog.d(TAG, "activityPaused, token=" + this.token + ", timeout=" + z + ", callers=" + Debug.getCallers(5));
        }
        TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null) {
            removePauseTimeout();
            ActivityRecord pausingActivity = taskFragment.getPausingActivity();
            if (pausingActivity == this) {
                if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 397382873, 0, (String) null, new Object[]{String.valueOf(this), z ? "(due to timeout)" : " (pause complete)"});
                }
                this.mAtmService.deferWindowLayout();
                try {
                    taskFragment.completePause(true, null);
                    this.mAtmService.continueWindowLayout();
                    this.mDisplayContent.handleActivitySizeCompatModeIfNeeded(this);
                    return;
                } catch (Throwable th) {
                    this.mAtmService.continueWindowLayout();
                    throw th;
                }
            }
            EventLogTags.writeWmFailedToPause(this.mUserId, System.identityHashCode(this), this.shortComponentName, pausingActivity != null ? pausingActivity.shortComponentName : "(none)");
            if (isState(State.PAUSING)) {
                State state = State.PAUSED;
                callServiceTrackeronActivityStatechange(state, true);
                setState(state, "activityPausedLocked");
                if (this.finishing) {
                    if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -937498525, 0, (String) null, new Object[]{String.valueOf(this)});
                    }
                    completeFinishing("activityPausedLocked");
                }
            }
        }
        this.mDisplayContent.handleActivitySizeCompatModeIfNeeded(this);
        this.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void schedulePauseTimeout() {
        this.pauseTime = SystemClock.uptimeMillis();
        this.mAtmService.mH.postDelayed(this.mPauseTimeoutRunnable, 500L);
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -705939410, 0, (String) null, (Object[]) null);
        }
    }

    private void removePauseTimeout() {
        this.mAtmService.mH.removeCallbacks(this.mPauseTimeoutRunnable);
    }

    private void removeDestroyTimeout() {
        this.mAtmService.mH.removeCallbacks(this.mDestroyTimeoutRunnable);
    }

    private void removeStopTimeout() {
        this.mAtmService.mH.removeCallbacks(this.mStopTimeoutRunnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeTimeouts() {
        this.mTaskSupervisor.removeIdleTimeoutForActivity(this);
        removePauseTimeout();
        removeStopTimeout();
        removeDestroyTimeout();
        finishLaunchTickingLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopIfPossible() {
        if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            Slog.d(TAG_SWITCH, "Stopping: " + this);
        }
        this.mActivityRecordSocExt.setLaunching(false);
        Task rootTask = getRootTask();
        if (isNoHistory() && !this.finishing) {
            if (!rootTask.shouldSleepActivities()) {
                if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_STATES, -1136139407, 0, (String) null, new Object[]{String.valueOf(this)});
                }
                if (finishIfPossible("stop-no-history", false) != 0) {
                    resumeKeyDispatchingLocked();
                    return;
                }
            } else if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_STATES, 485170982, 0, (String) null, new Object[]{String.valueOf(this)});
            }
        }
        if (attachedToProcess()) {
            resumeKeyDispatchingLocked();
            try {
                if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 189628502, 0, (String) null, new Object[]{String.valueOf(this)});
                }
                State state = State.STOPPING;
                callServiceTrackeronActivityStatechange(state, true);
                if (isState(State.RESUMED, State.PAUSED)) {
                    this.mAtmService.mSocExt.onActivityStateChanged(this, false);
                }
                setState(state, "stopIfPossible");
                if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                    Slog.v(TAG_VISIBILITY, "Stopping:" + this);
                }
                EventLogTags.writeWmStopActivity(this.mUserId, System.identityHashCode(this), this.shortComponentName);
                this.mAtmService.getLifecycleManager().scheduleTransaction(this.app.getThread(), this.token, (ActivityLifecycleItem) StopActivityItem.obtain(this.configChangeFlags));
                this.mAtmService.mH.postDelayed(this.mStopTimeoutRunnable, 11000L);
            } catch (Exception e) {
                Slog.w(TAG, "Exception thrown during pause", e);
                this.mAppStopped = true;
                if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 306524472, 0, (String) null, new Object[]{String.valueOf(this)});
                }
                State state2 = State.STOPPED;
                callServiceTrackeronActivityStatechange(state2, true);
                setState(state2, "stopIfPossible");
                if (this.deferRelaunchUntilPaused) {
                    destroyImmediately("stop-except");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void activityStopped(Bundle bundle, PersistableBundle persistableBundle, CharSequence charSequence) {
        removeStopTimeout();
        State state = this.mState;
        boolean z = state == State.STOPPING;
        if (!z && state != State.RESTARTING_PROCESS) {
            Slog.i(TAG, "Activity reported stop, but no longer stopping: " + this + " " + this.mState);
            return;
        }
        if (persistableBundle != null) {
            this.mPersistentState = persistableBundle;
            this.mAtmService.notifyTaskPersisterLocked(this.task, false);
        }
        if (bundle != null) {
            setSavedState(bundle);
            this.launchCount = 0;
            updateTaskDescription(charSequence);
        }
        if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_STATES, -172326720, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(this.mIcicle)});
        }
        if (z) {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -1305791032, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            setState(State.STOPPED, "activityStopped");
        }
        this.mAppStopped = true;
        this.firstWindowDrawn = false;
        Task task = this.task;
        if (task.mLastRecentsAnimationTransaction != null) {
            task.clearLastRecentsAnimationTransaction(true);
        }
        this.mDisplayContent.mPinnedTaskController.onActivityHidden(this.mActivityComponent);
        this.mDisplayContent.mUnknownAppVisibilityController.appRemovedOrHidden(this);
        if (isClientVisible()) {
            setClientVisible(false);
        }
        if (!this.mActivityRecordExt.shouldWindowSurfaceSaved(findMainWindow(false), this.mDisplayContent)) {
            destroySurfaces();
        }
        removeStartingWindow();
        if (this.finishing) {
            abortAndClearOptionsAnimation();
        } else if (this.deferRelaunchUntilPaused) {
            destroyImmediately("stop-config");
            this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        } else {
            this.mAtmService.updatePreviousProcess(this);
        }
        this.mTaskSupervisor.checkReadyForSleepLocked(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addToStopping(boolean z, boolean z2, String str) {
        if (!this.mTaskSupervisor.mStoppingActivities.contains(this)) {
            EventLogTags.writeWmAddToStopping(this.mUserId, System.identityHashCode(this), this.shortComponentName, str);
            this.mTaskSupervisor.mStoppingActivities.add(this);
        }
        Task rootTask = getRootTask();
        boolean z3 = true;
        if (this.mTaskSupervisor.mStoppingActivities.size() <= 3 && (!isRootOfTask() || rootTask.getChildCount() > 1)) {
            z3 = false;
        }
        if (z || z3) {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, 1126328412, 15, (String) null, new Object[]{Boolean.valueOf(z3), Boolean.valueOf(!z2)});
            }
            if (!z2) {
                this.mTaskSupervisor.scheduleIdle();
                return;
            } else {
                this.mTaskSupervisor.scheduleIdleTimeout(this);
                return;
            }
        }
        rootTask.checkReadyForSleep();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startLaunchTickingLocked() {
        if (!Build.IS_USER && this.launchTickTime == 0) {
            this.launchTickTime = SystemClock.uptimeMillis();
            continueLaunchTicking();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean continueLaunchTicking() {
        Task rootTask;
        if (this.launchTickTime == 0 || (rootTask = getRootTask()) == null) {
            return false;
        }
        rootTask.removeLaunchTickMessages();
        this.mAtmService.mH.postDelayed(this.mLaunchTickRunnable, 500L);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeLaunchTickRunnable() {
        this.mAtmService.mH.removeCallbacks(this.mLaunchTickRunnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishLaunchTickingLocked() {
        this.launchTickTime = 0L;
        Task rootTask = getRootTask();
        if (rootTask == null) {
            return;
        }
        rootTask.removeLaunchTickMessages();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean mayFreezeScreenLocked() {
        return mayFreezeScreenLocked(this.app);
    }

    private boolean mayFreezeScreenLocked(WindowProcessController windowProcessController) {
        return (!hasProcess() || windowProcessController.isCrashing() || windowProcessController.isNotResponding()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startFreezingScreenLocked(int i) {
        startFreezingScreenLocked(this.app, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startFreezingScreenLocked(WindowProcessController windowProcessController, int i) {
        if (mayFreezeScreenLocked(windowProcessController)) {
            if (getParent() == null) {
                Slog.w("WindowManager", "Attempted to freeze screen with non-existing app token: " + this.token);
                return;
            }
            if (((-536870913) & i) == 0 && okToDisplay()) {
                if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1836306327, 0, (String) null, new Object[]{String.valueOf(this.token)});
                    return;
                }
                return;
            }
            startFreezingScreen();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startFreezingScreen() {
        startFreezingScreen(-1);
    }

    void startFreezingScreen(int i) {
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            return;
        }
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1746778201, 252, (String) null, new Object[]{String.valueOf(this.token), Boolean.valueOf(isVisible()), Boolean.valueOf(this.mFreezingScreen), Boolean.valueOf(this.mVisibleRequested), String.valueOf(new RuntimeException().fillInStackTrace())});
        }
        if (this.mVisibleRequested) {
            boolean z = i != -1;
            if (!this.mFreezingScreen) {
                this.mFreezingScreen = true;
                this.mWmService.registerAppFreezeListener(this);
                WindowManagerService windowManagerService = this.mWmService;
                int i2 = windowManagerService.mAppsFreezingScreen + 1;
                windowManagerService.mAppsFreezingScreen = i2;
                if (i2 == 1) {
                    if (z) {
                        this.mDisplayContent.getDisplayRotation().cancelSeamlessRotation();
                    }
                    this.mWmService.startFreezingDisplay(0, 0, this.mDisplayContent, i);
                    this.mWmService.mH.removeMessages(17);
                    this.mWmService.mH.sendEmptyMessageDelayed(17, 2000L);
                }
            }
            if (z) {
                return;
            }
            int size = this.mChildren.size();
            for (int i3 = 0; i3 < size; i3++) {
                ((WindowState) this.mChildren.get(i3)).onStartFreezingScreen();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFreezingScreen() {
        return this.mFreezingScreen;
    }

    @Override // com.android.server.wm.WindowManagerService.AppFreezeListener
    public void onAppFreezeTimeout() {
        Slog.w("WindowManager", "Force clearing freeze: " + this);
        stopFreezingScreen(true, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopFreezingScreenLocked(boolean z) {
        if (z || this.frozenBeforeDestroy) {
            this.frozenBeforeDestroy = false;
            if (getParent() == null) {
                return;
            }
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 466506262, 60, (String) null, new Object[]{String.valueOf(this.token), Boolean.valueOf(isVisible()), Boolean.valueOf(isFreezingScreen())});
            }
            stopFreezingScreen(true, z);
        }
    }

    void stopFreezingScreen(boolean z, boolean z2) {
        if (this.mFreezingScreen) {
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 539077569, 12, (String) null, new Object[]{String.valueOf(this), Boolean.valueOf(z2)});
            }
            int size = this.mChildren.size();
            boolean z3 = false;
            for (int i = 0; i < size; i++) {
                z3 |= ((WindowState) this.mChildren.get(i)).onStopFreezingScreen();
            }
            if (z2 || z3) {
                if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -251259736, 0, (String) null, new Object[]{String.valueOf(this)});
                }
                this.mFreezingScreen = false;
                this.mWmService.unregisterAppFreezeListener(this);
                r8.mAppsFreezingScreen--;
                this.mWmService.mLastFinishedFreezeSource = this;
            }
            if (z) {
                if (z3) {
                    this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
                }
                this.mWmService.stopFreezingDisplayLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onFirstWindowDrawn(WindowState windowState) {
        this.firstWindowDrawn = true;
        this.mSplashScreenStyleSolidColor = true;
        if (this.mStartingWindow != null) {
            Slog.d(TAG, "Finish StartingWindow: " + windowState.mToken + "first real window is shown");
            windowState.cancelAnimation();
        }
        Task task = this.task;
        if (task.mSharedStartingData == null) {
            task = null;
        }
        if (task == null) {
            removeStartingWindow();
        } else if (task.getActivity(new Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda35
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onFirstWindowDrawn$16;
                lambda$onFirstWindowDrawn$16 = ActivityRecord.this.lambda$onFirstWindowDrawn$16((ActivityRecord) obj);
                return lambda$onFirstWindowDrawn$16;
            }
        }) == null) {
            ActivityRecord activityRecord = task.topActivityContainsStartingWindow();
            if (activityRecord != null) {
                activityRecord.removeStartingWindow();
            } else {
                Slog.d(TAG, "Don't removeStartingWindow due to ActivityRecord is null!");
            }
        } else {
            Slog.d(TAG, "Don't removeStartingWindow due to visible windows!");
        }
        updateReportedVisibilityLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onFirstWindowDrawn$16(ActivityRecord activityRecord) {
        if (!activityRecord.isVisibleRequested() || this == activityRecord) {
            return false;
        }
        WindowState findMainWindow = activityRecord.findMainWindow(false);
        return findMainWindow == null || findMainWindow.mWinAnimator.mDrawState < 4;
    }

    private boolean setTaskHasBeenVisible() {
        if (this.task.getHasBeenVisible()) {
            return false;
        }
        if (inTransition() && this.mActivityRecordExt.shouldDeferTaskAppear(this.task)) {
            this.task.setDeferTaskAppear(true);
        }
        this.task.setHasBeenVisible(true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onStartingWindowDrawn() {
        boolean z;
        if (this.task != null) {
            this.mSplashScreenStyleSolidColor = true;
            z = !setTaskHasBeenVisible();
        } else {
            z = false;
        }
        boolean isZoomMode = this.mActivityRecordExt.isZoomMode(getWindowingMode());
        Slog.d(TAG, "onStartingWindowDrawn: " + String.format("wasTaskVisible=%b, mStartingData=%s, finishing=%b,mLaunchedFromBubble=%b, mVisibleRequested=%b, mDisplayContent=%s, isZoomMode=%b", Boolean.valueOf(z), this.mStartingData, Boolean.valueOf(this.finishing), Boolean.valueOf(this.mLaunchedFromBubble), Boolean.valueOf(this.mVisibleRequested), this.mDisplayContent, Boolean.valueOf(isZoomMode)));
        boolean shouldSplashDislay = this.mActivityRecordExt.shouldSplashDislay(z, isVisible(), isStartingWindowDisplayed(), this.mStartingData);
        if ((z && !shouldSplashDislay) || this.mStartingData == null || this.finishing || this.mLaunchedFromBubble || !this.mVisibleRequested || this.mDisplayContent.mAppTransition.isReady() || this.mDisplayContent.mAppTransition.isRunning() || isZoomMode || !this.mDisplayContent.isNextTransitionForward()) {
            return;
        }
        this.mStartingData.mIsTransitionForward = true;
        if (this != this.mDisplayContent.getLastOrientationSource()) {
            this.mDisplayContent.updateOrientation();
        }
        this.mDisplayContent.executeAppTransition();
    }

    private void onWindowsDrawn() {
        this.mActivityRecordSocExt.hookOnWindowsDrawn();
        ActivityMetricsLogger.TransitionInfoSnapshot notifyWindowsDrawn = this.mTaskSupervisor.getActivityMetricsLogger().notifyWindowsDrawn(this);
        boolean z = notifyWindowsDrawn != null;
        int i = z ? notifyWindowsDrawn.windowsDrawnDelayMs : -1;
        int launchState = z ? notifyWindowsDrawn.getLaunchState() : 0;
        if (z || (getDisplayArea() != null && this == getDisplayArea().topRunningActivity())) {
            this.mTaskSupervisor.reportActivityLaunched(false, this, i, launchState);
        }
        finishLaunchTickingLocked();
        logLaunchTime();
        if (this.task != null) {
            setTaskHasBeenVisible();
        }
        this.mLaunchRootTask = null;
        Task task = this.task;
        if (task != null && task.getWrapper().getExtImpl().getLaunchedFromMultiSearch()) {
            this.task.dispatchTaskInfoChangedIfNeeded(false);
        }
        this.mActivityRecordExt.onWindowsDrawn(this);
    }

    void onWindowsVisible() {
        if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v("WindowManager", "Reporting visible in " + this.token);
        }
        this.mTaskSupervisor.stopWaitingForActivityVisible(this);
        this.mActivityRecordExt.onWindowsVisible(this);
        if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            Log.v(TAG_SWITCH, "windowsVisibleLocked(): " + this);
        }
        if (this.nowVisible) {
            return;
        }
        this.nowVisible = true;
        this.mActivityRecordSocExt.setLaunching(false);
        this.lastVisibleTime = SystemClock.uptimeMillis();
        this.mAtmService.scheduleAppGcsLocked();
        this.mTaskSupervisor.scheduleProcessStoppingAndFinishingActivitiesIfNeeded();
        if (this.mImeInsetsFrozenUntilStartInput && getWindow(new Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda20
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onWindowsVisible$17;
                lambda$onWindowsVisible$17 = ActivityRecord.lambda$onWindowsVisible$17((WindowState) obj);
                return lambda$onWindowsVisible$17;
            }
        }) == null) {
            this.mImeInsetsFrozenUntilStartInput = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onWindowsVisible$17(WindowState windowState) {
        return WindowManager.LayoutParams.mayUseInputMethod(windowState.mAttrs.flags);
    }

    void onWindowsGone() {
        if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v("WindowManager", "Reporting gone in " + this.token);
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            Log.v(TAG_SWITCH, "windowsGone(): " + this);
        }
        this.nowVisible = false;
        this.mActivityRecordSocExt.setLaunching(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void checkAppWindowsReadyToShow() {
        boolean z = this.allDrawn;
        if (z == this.mLastAllDrawn) {
            return;
        }
        this.mLastAllDrawn = z;
        if (z) {
            if (this.mFreezingScreen) {
                showAllWindowsLocked();
                stopFreezingScreen(false, true);
                if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ORIENTATION, 806891543, 20, (String) null, new Object[]{String.valueOf(this), Long.valueOf(this.mNumInterestingWindows), Long.valueOf(this.mNumDrawnWindows)});
                }
                setAppLayoutChanges(4, "checkAppWindowsReadyToShow: freezingScreen");
                return;
            }
            setAppLayoutChanges(8, "checkAppWindowsReadyToShow");
            if (getDisplayContent().mOpeningApps.contains(this) || !canShowWindows()) {
                return;
            }
            showAllWindowsLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showAllWindowsLocked() {
        forAllWindows(new Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityRecord.lambda$showAllWindowsLocked$18((WindowState) obj);
            }
        }, false);
        this.mActivityRecordExt.onShowAllWindowsOfActivity(getTask());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showAllWindowsLocked$18(WindowState windowState) {
        if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(TAG, "performing show on: " + windowState);
        }
        windowState.performShowLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateReportedVisibilityLocked() {
        if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(TAG, "Update reported visibility: " + this);
        }
        int size = this.mChildren.size();
        this.mReportedVisibilityResults.reset();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            ((WindowState) this.mChildren.get(i)).updateReportedVisibility(this.mReportedVisibilityResults);
        }
        WindowState.UpdateReportedVisibilityResults updateReportedVisibilityResults = this.mReportedVisibilityResults;
        int i2 = updateReportedVisibilityResults.numInteresting;
        int i3 = updateReportedVisibilityResults.numVisible;
        int i4 = updateReportedVisibilityResults.numDrawn;
        boolean z2 = updateReportedVisibilityResults.nowGone;
        boolean z3 = i2 > 0 && i4 >= i2;
        if (i2 > 0 && i3 >= i2 && isVisible()) {
            z = true;
        }
        if (!z2) {
            if (!z3) {
                z3 = this.mReportedDrawn;
            }
            if (!z) {
                z = this.reportedVisible;
            }
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(TAG, "VIS " + this + ": interesting=" + i2 + " visible=" + i3);
        }
        if (z3 != this.mReportedDrawn) {
            if (z3) {
                onWindowsDrawn();
            }
            this.mReportedDrawn = z3;
        }
        if (z != this.reportedVisible) {
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.v(TAG, "Visibility changed in " + this + ": vis=" + z);
            }
            this.reportedVisible = z;
            if (z) {
                onWindowsVisible();
            } else {
                onWindowsGone();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isReportedDrawn() {
        return this.mReportedDrawn;
    }

    @Override // com.android.server.wm.WindowToken
    void setClientVisible(boolean z) {
        if (z || !this.mDeferHidingClient) {
            super.setClientVisible(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateDrawnWindowStates(WindowState windowState) {
        windowState.setDrawnStateEvaluated(true);
        if (WindowManagerDebugConfig.DEBUG_STARTING_WINDOW_VERBOSE && windowState == this.mStartingWindow) {
            Slog.d(TAG, "updateWindows: starting " + windowState + " isOnScreen=" + windowState.isOnScreen() + " allDrawn=" + this.allDrawn + " freezingScreen=" + this.mFreezingScreen);
        }
        if (this.allDrawn && !this.mFreezingScreen) {
            return false;
        }
        long j = this.mLastTransactionSequence;
        int i = this.mWmService.mTransactionSequence;
        if (j != i) {
            this.mLastTransactionSequence = i;
            this.mNumDrawnWindows = 0;
            this.mNumInterestingWindows = findMainWindow(false) != null ? 1 : 0;
        }
        WindowStateAnimator windowStateAnimator = windowState.mWinAnimator;
        if (!this.allDrawn && windowState.mightAffectAllDrawn()) {
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY || ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                boolean isAnimating = isAnimating(3, 1);
                Slog.v(TAG, "Eval win " + windowState + ": isDrawn=" + windowState.isDrawn() + ", isAnimationSet=" + isAnimating);
                if (!windowState.isDrawn()) {
                    Slog.v(TAG, "Not displayed: s=" + windowStateAnimator.mSurfaceController + " pv=" + windowState.isVisibleByPolicy() + " mDrawState=" + windowStateAnimator.drawStateToString() + " ph=" + windowState.isParentWindowHidden() + " th=" + this.mVisibleRequested + " a=" + isAnimating);
                }
            }
            if (windowState != this.mStartingWindow) {
                if (windowState.isInteresting() && windowState.mAttrs.type != 3) {
                    if (findMainWindow(false) != windowState) {
                        this.mNumInterestingWindows++;
                    }
                    if (windowState.isDrawn()) {
                        this.mNumDrawnWindows++;
                        if (!ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY && !ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                            return true;
                        }
                        Slog.v(TAG, "tokenMayBeDrawn: " + this + " w=" + windowState + " numInteresting=" + this.mNumInterestingWindows + " freezingScreen=" + this.mFreezingScreen + " mAppFreezing=" + windowState.mAppFreezing);
                        return true;
                    }
                }
            } else if (this.mStartingData != null && windowState.isDrawn()) {
                this.mStartingData.mIsDisplayed = true;
            }
        }
        return false;
    }

    public boolean inputDispatchingTimedOut(TimeoutRecord timeoutRecord, int i) {
        ActivityRecord waitingHistoryRecordLocked;
        WindowProcessController windowProcessController;
        boolean z;
        try {
            Trace.traceBegin(64L, "ActivityRecord#inputDispatchingTimedOut()");
            timeoutRecord.mLatencyTracker.waitingOnGlobalLockStarted();
            WindowManagerGlobalLock windowManagerGlobalLock = this.mAtmService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    timeoutRecord.mLatencyTracker.waitingOnGlobalLockEnded();
                    waitingHistoryRecordLocked = getWaitingHistoryRecordLocked();
                    windowProcessController = this.app;
                    z = hasProcess() && (this.app.getPid() == i || i == -1);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            if (z) {
                return this.mAtmService.mAmInternal.inputDispatchingTimedOut(windowProcessController.mOwner, waitingHistoryRecordLocked.shortComponentName, waitingHistoryRecordLocked.info.applicationInfo, this.shortComponentName, this.app, false, timeoutRecord);
            }
            return this.mAtmService.mAmInternal.inputDispatchingTimedOut(i, false, timeoutRecord) <= 0;
        } finally {
            Trace.traceEnd(64L);
        }
    }

    private ActivityRecord getWaitingHistoryRecordLocked() {
        Task topDisplayFocusedRootTask;
        if (!this.mAppStopped || (topDisplayFocusedRootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask()) == null) {
            return this;
        }
        ActivityRecord topResumedActivity = topDisplayFocusedRootTask.getTopResumedActivity();
        if (topResumedActivity == null) {
            topResumedActivity = topDisplayFocusedRootTask.getTopPausingActivity();
        }
        return topResumedActivity != null ? topResumedActivity : this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canBeTopRunning() {
        return !this.finishing && showToCurrentUser();
    }

    public boolean isInterestingToUserLocked() {
        State state;
        return this.mVisibleRequested || this.nowVisible || (state = this.mState) == State.PAUSING || state == State.RESUMED;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getTaskForActivityLocked(IBinder iBinder, boolean z) {
        ActivityRecord forTokenLocked = forTokenLocked(iBinder);
        if (forTokenLocked == null || forTokenLocked.getParent() == null) {
            return -1;
        }
        return getTaskForActivityLocked(forTokenLocked, z);
    }

    static int getTaskForActivityLocked(ActivityRecord activityRecord, boolean z) {
        Task task = activityRecord.task;
        if (!z || activityRecord.compareTo((WindowContainer) task.getRootActivity(false, true)) <= 0 || activityRecord.getWrapper().getExtImpl().isCompactRoot(activityRecord)) {
            return task.mTaskId;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ActivityRecord isInRootTaskLocked(IBinder iBinder) {
        ActivityRecord forTokenLocked = forTokenLocked(iBinder);
        if (forTokenLocked != null) {
            return forTokenLocked.getRootTask().isInTask(forTokenLocked);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Task getRootTask(IBinder iBinder) {
        ActivityRecord isInRootTaskLocked = isInRootTaskLocked(iBinder);
        if (isInRootTaskLocked != null) {
            return isInRootTaskLocked.getRootTask();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ActivityRecord isInAnyTask(IBinder iBinder) {
        ActivityRecord forTokenLocked = forTokenLocked(iBinder);
        if (forTokenLocked == null || !forTokenLocked.isAttached()) {
            return null;
        }
        return forTokenLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDisplayId() {
        DisplayContent displayContent;
        Task task = this.task;
        if (task == null || (displayContent = task.mDisplayContent) == null) {
            return -1;
        }
        return displayContent.mDisplayId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean isDestroyable() {
        return (this.finishing || !hasProcess() || isState(State.RESUMED) || getRootTask() == null || this == getTaskFragment().getPausingActivity() || !this.mHaveState || !this.mAppStopped || this.mVisibleRequested) ? false : true;
    }

    private static String createImageFilename(long j, int i) {
        return String.valueOf(i) + ACTIVITY_ICON_SUFFIX + j + ".png";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTaskDescription(ActivityManager.TaskDescription taskDescription) {
        Bitmap icon;
        if (taskDescription.getIconFilename() == null && (icon = taskDescription.getIcon()) != null) {
            String absolutePath = new File(TaskPersister.getUserImagesDir(this.task.mUserId), createImageFilename(this.createTime, this.task.mTaskId)).getAbsolutePath();
            this.mAtmService.getRecentTasks().saveImage(icon, absolutePath);
            taskDescription.setIconFilename(absolutePath);
        }
        this.taskDescription = taskDescription;
        getTask().updateTaskDescription();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLocusId(LocusId locusId) {
        if (Objects.equals(locusId, this.mLocusId)) {
            return;
        }
        this.mLocusId = locusId;
        if (getTask() != null) {
            getTask().dispatchTaskInfoChangedIfNeeded(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocusId getLocusId() {
        return this.mLocusId;
    }

    public void reportScreenCaptured() {
        RemoteCallbackList<IScreenCaptureObserver> remoteCallbackList = this.mCaptureCallbacks;
        if (remoteCallbackList != null) {
            int beginBroadcast = remoteCallbackList.beginBroadcast();
            for (int i = 0; i < beginBroadcast; i++) {
                try {
                    this.mCaptureCallbacks.getBroadcastItem(i).onScreenCaptured();
                } catch (RemoteException unused) {
                }
            }
            this.mCaptureCallbacks.finishBroadcast();
        }
    }

    public void registerCaptureObserver(IScreenCaptureObserver iScreenCaptureObserver) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mCaptureCallbacks == null) {
                    this.mCaptureCallbacks = new RemoteCallbackList<>();
                }
                this.mCaptureCallbacks.register(iScreenCaptureObserver);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void unregisterCaptureObserver(IScreenCaptureObserver iScreenCaptureObserver) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                RemoteCallbackList<IScreenCaptureObserver> remoteCallbackList = this.mCaptureCallbacks;
                if (remoteCallbackList != null) {
                    remoteCallbackList.unregister(iScreenCaptureObserver);
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRegisteredForScreenCaptureCallback() {
        RemoteCallbackList<IScreenCaptureObserver> remoteCallbackList = this.mCaptureCallbacks;
        return remoteCallbackList != null && remoteCallbackList.getRegisteredCallbackCount() > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setVoiceSessionLocked(IVoiceInteractionSession iVoiceInteractionSession) {
        this.voiceSession = iVoiceInteractionSession;
        this.pendingVoiceInteractionStart = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearVoiceSessionLocked() {
        this.voiceSession = null;
        this.pendingVoiceInteractionStart = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showStartingWindow(boolean z) {
        showStartingWindow(null, false, z, false, null);
    }

    private ActivityRecord searchCandidateLaunchingActivity() {
        ActivityRecord activityBelow = this.task.getActivityBelow(this);
        if (activityBelow == null) {
            activityBelow = this.task.getParent().getActivityBelow(this);
        }
        if (activityBelow == null || activityBelow.isActivityTypeHome()) {
            return null;
        }
        WindowProcessController windowProcessController = this.app;
        if (windowProcessController == null) {
            windowProcessController = (WindowProcessController) this.mAtmService.mProcessNames.get(this.processName, this.info.applicationInfo.uid);
        }
        WindowProcessController windowProcessController2 = activityBelow.app;
        if (windowProcessController2 == null) {
            windowProcessController2 = (WindowProcessController) this.mAtmService.mProcessNames.get(activityBelow.processName, activityBelow.info.applicationInfo.uid);
        }
        if (windowProcessController2 == windowProcessController || this.mActivityComponent.getPackageName().equals(activityBelow.mActivityComponent.getPackageName())) {
            return activityBelow;
        }
        return null;
    }

    private boolean isIconStylePreferred(int i) {
        AttributeCache.Entry entry;
        return i != 0 && (entry = AttributeCache.instance().get(this.packageName, i, com.android.internal.R.styleable.Window, this.mWmService.mCurrentUserId)) != null && entry.array.hasValue(61) && entry.array.getInt(61, 0) == 1;
    }

    private boolean shouldUseSolidColorSplashScreen(ActivityRecord activityRecord, boolean z, ActivityOptions activityOptions, int i) {
        int i2;
        Task task;
        if (activityRecord == null && !z && (task = this.task) != null && task.getActivityAbove(this) != null) {
            return true;
        }
        if (activityOptions != null) {
            int splashScreenStyle = activityOptions.getSplashScreenStyle();
            if (splashScreenStyle == 0) {
                return true;
            }
            if (splashScreenStyle == 1 || isIconStylePreferred(i) || (i2 = this.mLaunchSourceType) == 2 || this.launchedFromUid == 2000) {
                return false;
            }
            if (i2 == 3) {
                return true;
            }
        } else if (isIconStylePreferred(i)) {
            return false;
        }
        if (this.mActivityRecordExt.isInRestoring(this)) {
            Slog.d(TAG, "shouldUseSolidColorSplashScreen: use icon style for restore case");
            return false;
        }
        if (activityRecord == null) {
            activityRecord = searchCandidateLaunchingActivity();
        }
        if (activityRecord != null && !activityRecord.isActivityTypeHome()) {
            return activityRecord.mSplashScreenStyleSolidColor;
        }
        if (!(this.mActivityRecordExt.isZoomMode(getWindowingMode()) && this.mActivityRecordExt.isZoomSplashExceptionList(this.packageName)) && !z) {
            return true;
        }
        int i3 = this.mLaunchSourceType;
        return (i3 == 1 || i3 == 2 || this.launchedFromUid == 2000) ? false : true;
    }

    private int getSplashscreenTheme(ActivityOptions activityOptions) {
        String splashScreenThemeResName = activityOptions != null ? activityOptions.getSplashScreenThemeResName() : null;
        if (splashScreenThemeResName == null || splashScreenThemeResName.isEmpty()) {
            try {
                splashScreenThemeResName = this.mAtmService.getPackageManager().getSplashScreenTheme(this.packageName, this.mUserId);
            } catch (RemoteException unused) {
            }
        }
        if (splashScreenThemeResName == null || splashScreenThemeResName.isEmpty()) {
            return 0;
        }
        try {
            return this.mAtmService.mContext.createPackageContext(this.packageName, 0).getResources().getIdentifier(splashScreenThemeResName, null, null);
        } catch (PackageManager.NameNotFoundException | Resources.NotFoundException unused2) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showStartingWindow(ActivityRecord activityRecord, boolean z, boolean z2, boolean z3, ActivityRecord activityRecord2) {
        showStartingWindow(activityRecord, z, z2, isProcessRunning(), z3, activityRecord2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showStartingWindow(ActivityRecord activityRecord, boolean z, boolean z2, boolean z3, boolean z4, ActivityRecord activityRecord2, ActivityOptions activityOptions) {
        if (this.mTaskOverlay) {
            Slog.d(TAG, "skip showStartingWindow due to mTaskOverlay");
            return;
        }
        ActivityOptions activityOptions2 = activityOptions != null ? activityOptions : this.mPendingOptions;
        if (activityOptions2 != null && activityOptions2.getAnimationType() == 5) {
            Slog.d(TAG, "skip showStartingWindow when using shared element transition");
            return;
        }
        if (this.mActivityRecordExt.isBackgroundPuttTask(this)) {
            Slog.d(TAG, "skip showStartingWindow at One Putt scene");
            return;
        }
        this.mActivityRecordExt.reviseWindowFlagsForStarting(this, z, z2, isProcessRunning(), false, this.mState);
        int splashscreenTheme = z4 ? getSplashscreenTheme(activityOptions2) : 0;
        IActivityRecordExt iActivityRecordExt = this.mActivityRecordExt;
        Task task = this.task;
        int i = this.theme;
        int updateOrSaveResolvedThemeIfNeeded = iActivityRecordExt.updateOrSaveResolvedThemeIfNeeded(task, this, z, z2, activityRecord2, activityOptions2, z3, z4, i, splashscreenTheme, evaluateStartingWindowTheme(activityRecord, this.packageName, i, splashscreenTheme));
        this.mSplashScreenStyleSolidColor = shouldUseSolidColorSplashScreen(activityRecord2, z4, activityOptions2, updateOrSaveResolvedThemeIfNeeded);
        boolean z5 = this.mState.ordinal() >= State.STARTED.ordinal() && this.mState.ordinal() <= State.STOPPED.ordinal();
        boolean addStartingWindow = addStartingWindow(this.packageName, updateOrSaveResolvedThemeIfNeeded, activityRecord, z || (!z && !z5 && this.task.getActivity(new Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$showStartingWindow$19;
                lambda$showStartingWindow$19 = ActivityRecord.this.lambda$showStartingWindow$19((ActivityRecord) obj);
                return lambda$showStartingWindow$19;
            }
        }) == null), z2, z3, allowTaskSnapshot(), z5, this.mSplashScreenStyleSolidColor, this.allDrawn);
        if (WindowManagerDebugConfig.DEBUG_STARTING_WINDOW_VERBOSE && addStartingWindow) {
            Slog.d(TAG, "Scheduled starting window for " + this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$showStartingWindow$19(ActivityRecord activityRecord) {
        return (activityRecord.finishing || activityRecord == this) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelInitializing() {
        if (this.mStartingData != null) {
            if (ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                Slog.w(TAG_VISIBILITY, "Found orphaned starting window " + this);
            }
            removeStartingWindowAnimation(false);
        }
        if (this.mDisplayContent.mUnknownAppVisibilityController.allResolved()) {
            return;
        }
        this.mDisplayContent.mUnknownAppVisibilityController.appRemovedOrHidden(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postWindowRemoveStartingWindowCleanup() {
        if (this.mChildren.size() == 0 && this.mVisibleSetFromTransferredStartingWindow) {
            setVisible(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestUpdateWallpaperIfNeeded() {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            ((WindowState) this.mChildren.get(size)).requestUpdateWallpaperIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState getTopFullscreenOpaqueWindow() {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            WindowState windowState = (WindowState) this.mChildren.get(size);
            if (windowState != null && windowState.mAttrs.isFullscreen() && !windowState.isFullyTransparent()) {
                return windowState;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState findMainWindow() {
        return findMainWindow(true);
    }

    public WindowState findMainWindow(boolean z) {
        WindowState windowState = null;
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            WindowState windowState2 = (WindowState) this.mChildren.get(size);
            int i = windowState2.mAttrs.type;
            if (i == 1 || (z && i == 3)) {
                if (!windowState2.mAnimatingExit) {
                    return windowState2;
                }
                windowState = windowState2;
            }
        }
        return windowState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean needsZBoost() {
        return this.mNeedsZBoost || super.needsZBoost();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public SurfaceControl getAnimationLeashParent() {
        if (inPinnedWindowingMode()) {
            return getRootTask().getSurfaceControl();
        }
        return super.getAnimationLeashParent();
    }

    @VisibleForTesting
    boolean shouldAnimate() {
        Task task = this.task;
        return task == null || task.shouldAnimate();
    }

    public int isAppInfoGame() {
        ApplicationInfo applicationInfo = this.info.applicationInfo;
        if (applicationInfo != null) {
            return (applicationInfo.category == 0 || (applicationInfo.flags & 33554432) == 33554432) ? 1 : 0;
        }
        return 0;
    }

    private SurfaceControl createAnimationBoundsLayer(SurfaceControl.Transaction transaction) {
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 1720229827, 0, (String) null, (Object[]) null);
        }
        SurfaceControl.Builder callsite = makeAnimationLeash().setParent(getAnimationLeashParent()).setName(getSurfaceControl() + " - animation-bounds").setCallsite("ActivityRecord.createAnimationBoundsLayer");
        if (this.mNeedsLetterboxedAnimation) {
            callsite.setEffectLayer();
        }
        SurfaceControl build = callsite.build();
        transaction.show(build);
        return build;
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public boolean shouldDeferAnimationFinish(Runnable runnable) {
        AnimatingActivityRegistry animatingActivityRegistry = this.mAnimatingActivityRegistry;
        return animatingActivityRegistry != null && animatingActivityRegistry.notifyAboutToFinish(this, runnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean isWaitingForTransitionStart() {
        DisplayContent displayContent = getDisplayContent();
        return displayContent != null && displayContent.mAppTransition.isTransitionSet() && (displayContent.mOpeningApps.contains(this) || displayContent.mClosingApps.contains(this) || displayContent.mChangingContainers.contains(this));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTransitionForward() {
        StartingData startingData = this.mStartingData;
        return (startingData != null && startingData.mIsTransitionForward) || this.mDisplayContent.isNextTransitionForward();
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public void onLeashAnimationStarting(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        AnimatingActivityRegistry animatingActivityRegistry = this.mAnimatingActivityRegistry;
        if (animatingActivityRegistry != null) {
            animatingActivityRegistry.notifyStarting(this);
        }
        if (this.mNeedsLetterboxedAnimation) {
            updateLetterboxSurface(findMainWindow(), transaction);
            this.mNeedsAnimationBoundsLayer = true;
        }
        if (this.mNeedsAnimationBoundsLayer) {
            ((WindowContainer) this).mTmpRect.setEmpty();
            if (getDisplayContent().mAppTransitionController.isTransitWithinTask(getTransit(), this.task)) {
                this.task.getBounds(((WindowContainer) this).mTmpRect);
            } else {
                Task rootTask = getRootTask();
                if (rootTask == null) {
                    return;
                } else {
                    rootTask.getBounds(((WindowContainer) this).mTmpRect);
                }
            }
            if (this.mAnimationBoundsLayer != null) {
                if (WindowManagerDebugConfig.DEBUG_ANIM) {
                    Slog.d(TAG, "onLeashAnimationStarting ==> if already has an mAnimationBoundsLayer before creating a new one, we should remove the previous one this =:" + this);
                }
                transaction.remove(this.mAnimationBoundsLayer);
                this.mAnimationBoundsLayer = null;
            }
            SurfaceControl createAnimationBoundsLayer = createAnimationBoundsLayer(transaction);
            this.mAnimationBoundsLayer = createAnimationBoundsLayer;
            transaction.setLayer(createAnimationBoundsLayer, getLastLayer());
            if (this.mNeedsLetterboxedAnimation) {
                int roundedCornersRadius = this.mLetterboxUiController.getRoundedCornersRadius(findMainWindow());
                Rect rect = new Rect();
                getLetterboxInnerBounds(rect);
                transaction.setCornerRadius(this.mAnimationBoundsLayer, roundedCornersRadius).setCrop(this.mAnimationBoundsLayer, rect);
            }
            this.mActivityRecordExt.setAnimationLayer(getLastLayer());
            transaction.reparent(surfaceControl, this.mAnimationBoundsLayer);
        }
        this.mActivityRecordExt.onLeashAnimationStarting(transaction, surfaceControl);
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    void prepareSurfaces() {
        boolean z = isVisible() || isAnimating(2, 9);
        boolean inTransition = inTransition();
        if (WindowConfiguration.sExtImpl.isWindowingZoomMode(getWindowingMode()) || (!z && this.mTransitionController.mTransitionControllerExt.isRemoteAnimationPlaying(inTransition))) {
            z = z || inTransition;
        }
        if (this.mSurfaceControl != null) {
            if (z && !this.mLastSurfaceShowing) {
                getSyncTransaction().show(this.mSurfaceControl);
            } else if (!z && this.mLastSurfaceShowing) {
                getSyncTransaction().hide(this.mSurfaceControl);
            }
            this.mActivityRecordExt.hookPrepareSurfaces(z);
            if (z && this.mSyncState == 0 && !this.mActivityRecordExt.isSettingTaskFragment(getTaskFragment())) {
                this.mActivityRecordInputSink.applyChangesToSurfaceIfChanged(getPendingTransaction());
            }
        }
        WindowContainerThumbnail windowContainerThumbnail = this.mThumbnail;
        if (windowContainerThumbnail != null) {
            windowContainerThumbnail.setShowing(getPendingTransaction(), z);
        }
        this.mLastSurfaceShowing = z;
        super.prepareSurfaces();
    }

    boolean isSurfaceShowing() {
        return this.mLastSurfaceShowing;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void attachThumbnailAnimation() {
        if (isAnimating(2, 1)) {
            HardwareBuffer appTransitionThumbnailHeader = getDisplayContent().mAppTransition.getAppTransitionThumbnailHeader(this.task);
            if (appTransitionThumbnailHeader == null) {
                if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 1528528509, 0, (String) null, new Object[]{String.valueOf(this.task)});
                    return;
                }
                return;
            }
            clearThumbnail();
            SurfaceControl.Transaction pendingTransaction = getAnimatingContainer().getPendingTransaction();
            WindowContainerThumbnail windowContainerThumbnail = new WindowContainerThumbnail(pendingTransaction, getAnimatingContainer(), appTransitionThumbnailHeader);
            this.mThumbnail = windowContainerThumbnail;
            windowContainerThumbnail.startAnimation(pendingTransaction, loadThumbnailAnimation(appTransitionThumbnailHeader));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void attachCrossProfileAppsThumbnailAnimation() {
        Drawable drawable;
        if (isAnimating(2, 1)) {
            clearThumbnail();
            WindowState findMainWindow = findMainWindow();
            if (findMainWindow == null) {
                return;
            }
            Rect relativeFrame = findMainWindow.getRelativeFrame();
            if (this.task.mUserId == this.mWmService.mCurrentUserId) {
                drawable = this.mAtmService.getUiContext().getDrawable(R.drawable.ic_bluetooth_share_icon);
            } else {
                drawable = this.mEnterpriseThumbnailDrawable;
            }
            HardwareBuffer createCrossProfileAppsThumbnail = getDisplayContent().mAppTransition.createCrossProfileAppsThumbnail(drawable, relativeFrame);
            if (createCrossProfileAppsThumbnail == null) {
                return;
            }
            SurfaceControl.Transaction pendingTransaction = getPendingTransaction();
            this.mThumbnail = new WindowContainerThumbnail(pendingTransaction, getTask(), createCrossProfileAppsThumbnail);
            this.mThumbnail.startAnimation(pendingTransaction, getDisplayContent().mAppTransition.createCrossProfileAppsThumbnailAnimationLocked(relativeFrame), new Point(relativeFrame.left, relativeFrame.top));
        }
    }

    private Animation loadThumbnailAnimation(HardwareBuffer hardwareBuffer) {
        Rect rect;
        Rect rect2;
        DisplayInfo displayInfo = this.mDisplayContent.getDisplayInfo();
        WindowState findMainWindow = findMainWindow();
        if (findMainWindow != null) {
            Rect rect3 = findMainWindow.getInsetsStateWithVisibilityOverride().calculateInsets(findMainWindow.getFrame(), WindowInsets.Type.systemBars(), false).toRect();
            Rect rect4 = new Rect(findMainWindow.getFrame());
            rect4.inset(rect3);
            rect = rect3;
            rect2 = rect4;
        } else {
            rect = null;
            rect2 = new Rect(0, 0, displayInfo.appWidth, displayInfo.appHeight);
        }
        return getDisplayContent().mAppTransition.createThumbnailAspectScaleAnimationLocked(rect2, rect, hardwareBuffer, this.task, this.mDisplayContent.getConfiguration().orientation);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public void onAnimationLeashLost(SurfaceControl.Transaction transaction) {
        super.onAnimationLeashLost(transaction);
        SurfaceControl surfaceControl = this.mAnimationBoundsLayer;
        if (surfaceControl != null) {
            transaction.remove(surfaceControl);
            this.mAnimationBoundsLayer = null;
        }
        this.mNeedsAnimationBoundsLayer = false;
        if (this.mNeedsLetterboxedAnimation) {
            this.mNeedsLetterboxedAnimation = false;
            updateLetterboxSurface(findMainWindow(), transaction);
        }
        AnimatingActivityRegistry animatingActivityRegistry = this.mAnimatingActivityRegistry;
        if (animatingActivityRegistry != null) {
            animatingActivityRegistry.notifyFinished(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public void onAnimationFinished(int i, AnimationAdapter animationAdapter) {
        WindowState window;
        super.onAnimationFinished(i, animationAdapter);
        if (i == 128) {
            return;
        }
        this.mActivityRecordExt.onCompactWindowAnimationFinished(this);
        Trace.traceBegin(32L, "AR#onAnimationFinished");
        this.mTransit = -1;
        this.mTransitFlags = 0;
        setAppLayoutChanges(12, "ActivityRecord");
        clearThumbnail();
        setClientVisible(isVisible() || this.mVisibleRequested);
        getDisplayContent().computeImeTargetIfNeeded(this);
        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ANIM, 2010476671, 1020, (String) null, new Object[]{String.valueOf(this), Boolean.valueOf(this.reportedVisible), Boolean.valueOf(okToDisplay()), Boolean.valueOf(okToAnimate()), Boolean.valueOf(isStartingWindowDisplayed())});
        }
        WindowContainerThumbnail windowContainerThumbnail = this.mThumbnail;
        if (windowContainerThumbnail != null) {
            windowContainerThumbnail.destroy();
            this.mThumbnail = null;
        }
        this.mActivityRecordExt.onAnimationFinished(this);
        new ArrayList(this.mChildren).forEach(new Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((WindowState) obj).onExitAnimationDone();
            }
        });
        Task task = this.task;
        if (task != null && this.startingMoved && (window = task.getWindow(new Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onAnimationFinished$20;
                lambda$onAnimationFinished$20 = ActivityRecord.lambda$onAnimationFinished$20((WindowState) obj);
                return lambda$onAnimationFinished$20;
            }
        })) != null && window.mAnimatingExit && !window.isSelfAnimating(0, 16)) {
            window.onExitAnimationDone();
        }
        getDisplayContent().mAppTransition.notifyAppTransitionFinishedLocked(this.token);
        scheduleAnimation();
        this.mTaskSupervisor.scheduleProcessStoppingAndFinishingActivitiesIfNeeded();
        Trace.traceEnd(32L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onAnimationFinished$20(WindowState windowState) {
        return windowState.mAttrs.type == 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearAnimatingFlags() {
        boolean z = false;
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            z |= ((WindowState) this.mChildren.get(size)).clearAnimatingFlags();
        }
        if (z) {
            requestUpdateWallpaperIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void cancelAnimation() {
        super.cancelAnimation();
        clearThumbnail();
    }

    private void clearThumbnail() {
        WindowContainerThumbnail windowContainerThumbnail = this.mThumbnail;
        if (windowContainerThumbnail == null) {
            return;
        }
        windowContainerThumbnail.destroy();
        this.mThumbnail = null;
    }

    public int getTransit() {
        return this.mTransit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerRemoteAnimations(RemoteAnimationDefinition remoteAnimationDefinition) {
        this.mRemoteAnimationDefinition = remoteAnimationDefinition;
        if (remoteAnimationDefinition != null) {
            remoteAnimationDefinition.linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda0
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    ActivityRecord.this.unregisterRemoteAnimations();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterRemoteAnimations() {
        this.mRemoteAnimationDefinition = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public RemoteAnimationDefinition getRemoteAnimationDefinition() {
        return this.mActivityRecordExt.getRemoteAnimationDefinition(this.mRemoteAnimationDefinition);
    }

    @Override // com.android.server.wm.WindowToken
    void applyFixedRotationTransform(DisplayInfo displayInfo, DisplayFrames displayFrames, Configuration configuration) {
        super.applyFixedRotationTransform(displayInfo, displayFrames, configuration);
        ensureActivityConfiguration(0, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    @Configuration.Orientation
    public int getRequestedConfigurationOrientation(boolean z) {
        return getRequestedConfigurationOrientation(z, getOverrideOrientation());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    @Configuration.Orientation
    public int getRequestedConfigurationOrientation(boolean z, int i) {
        ActivityRecord activity;
        if (this.mLetterboxUiController.hasInheritedOrientation()) {
            RootDisplayArea rootDisplayArea = getRootDisplayArea();
            if (z && rootDisplayArea != null && rootDisplayArea.isOrientationDifferentFromDisplay()) {
                return reverseConfigurationOrientation(this.mLetterboxUiController.getInheritedOrientation());
            }
            return this.mLetterboxUiController.getInheritedOrientation();
        }
        Task task = this.task;
        if (task != null && i == 3 && (activity = task.getActivity(new Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean canDefineOrientationForActivitiesAbove;
                canDefineOrientationForActivitiesAbove = ((ActivityRecord) obj).canDefineOrientationForActivitiesAbove();
                return canDefineOrientationForActivitiesAbove;
            }
        }, this, false, true)) != null) {
            return activity.getRequestedConfigurationOrientation(z);
        }
        return super.getRequestedConfigurationOrientation(z, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canDefineOrientationForActivitiesAbove() {
        int overrideOrientation;
        return (this.finishing || (overrideOrientation = getOverrideOrientation()) == -2 || overrideOrientation == 3) ? false : true;
    }

    @Override // com.android.server.wm.WindowToken
    void onCancelFixedRotationTransform(int i) {
        if (this != this.mDisplayContent.getLastOrientationSource()) {
            return;
        }
        int requestedConfigurationOrientation = getRequestedConfigurationOrientation();
        if (requestedConfigurationOrientation == 0 || requestedConfigurationOrientation == this.mDisplayContent.getConfiguration().orientation) {
            this.mDisplayContent.mPinnedTaskController.onCancelFixedRotationTransform();
            if (!this.mActivityRecordExt.shouldExitFixedRotation(this.mDisplayContent, this)) {
                startFreezingScreen(i);
            }
            ensureActivityConfiguration(0, false);
            if (this.mTransitionController.isCollecting(this)) {
                this.task.resetSurfaceControlTransforms();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRequestedOrientation(int i) {
        if (this.mActivityRecordExt.blockActivityRecordRequestOrientation(this, i)) {
            return;
        }
        this.mActivityRecordExt.setOrientation(this, i, getOverrideOrientation());
        if (this.mLetterboxUiController.shouldIgnoreRequestedOrientation(i)) {
            return;
        }
        if (getRequestedConfigurationOrientation(false, i) != getRequestedConfigurationOrientation(false)) {
            clearSizeCompatModeAttributes();
        }
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1104347731, 0, (String) null, new Object[]{String.valueOf(ActivityInfo.screenOrientationToString(i)), String.valueOf(this)});
        }
        setOrientation(i, this);
        if (!getMergedOverrideConfiguration().equals(this.mLastReportedConfiguration.getMergedConfiguration())) {
            ensureActivityConfiguration(0, false, false, true);
            if (this.mTransitionController.inPlayingTransition(this)) {
                this.mTransitionController.mValidateActivityCompat.add(this);
            }
        }
        this.mAtmService.getTaskChangeNotificationController().notifyActivityRequestedOrientationChanged(this.task.mTaskId, i);
        this.mDisplayContent.getDisplayRotation().onSetRequestedOrientation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportDescendantOrientationChangeIfNeeded() {
        if (onDescendantOrientationChanged(this)) {
            this.task.dispatchTaskInfoChangedIfNeeded(true);
        }
    }

    @VisibleForTesting
    boolean shouldIgnoreOrientationRequests() {
        return this.mAppActivityEmbeddingSplitsEnabled && ActivityInfo.isFixedOrientationPortrait(getOverrideOrientation()) && !this.task.inMultiWindowMode() && getTask().getConfiguration().smallestScreenWidthDp >= 600;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public int getOrientation(int i) {
        if (getWrapper().getExtImpl().shouldIgnoreOrientationRequests(this) || shouldIgnoreOrientationRequests()) {
            return -2;
        }
        if (i == 3) {
            return getOverrideOrientation();
        }
        if (getDisplayContent() == null || getDisplayContent().mClosingApps.contains(this) || !(isVisibleRequested() || getDisplayContent().mOpeningApps.contains(this))) {
            return -2;
        }
        return getOverrideOrientation();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public int getOverrideOrientation() {
        return this.mLetterboxUiController.overrideOrientationIfNeeded(super.getOverrideOrientation());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRequestedOrientation() {
        return super.getOverrideOrientation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastReportedGlobalConfiguration(Configuration configuration) {
        this.forceNewConfig |= this.mActivityRecordExt.forceRelaunchWhenActivityIdle(configuration);
        this.mLastReportedConfiguration.setGlobalConfiguration(configuration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastReportedConfiguration(MergedConfiguration mergedConfiguration) {
        setLastReportedConfiguration(mergedConfiguration.getGlobalConfiguration(), mergedConfiguration.getOverrideConfiguration());
    }

    private void setLastReportedConfiguration(Configuration configuration, Configuration configuration2) {
        this.mLastReportedConfiguration.setConfiguration(configuration, configuration2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompatDisplayInsets getCompatDisplayInsets() {
        if (this.mLetterboxUiController.hasInheritedLetterboxBehavior()) {
            return this.mLetterboxUiController.getInheritedCompatDisplayInsets();
        }
        return this.mCompatDisplayInsets;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasCompatDisplayInsetsWithoutInheritance() {
        return this.mCompatDisplayInsets != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean inSizeCompatMode() {
        WindowContainer parent;
        if (this.mInSizeCompatModeForBounds) {
            return true;
        }
        if (getCompatDisplayInsets() == null || !shouldCreateCompatDisplayInsets() || isFixedRotationTransforming() || getConfiguration().windowConfiguration.getAppBounds() == null || (parent = getParent()) == null) {
            return false;
        }
        return (this.mSizeCompatBounds == null || !this.mActivityRecordExt.isCompactWindowingMode(getWindowingMode())) && parent.getConfiguration().densityDpi != getConfiguration().densityDpi;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldCreateCompatDisplayInsets() {
        int supportsSizeChanges = supportsSizeChanges();
        if (supportsSizeChanges == 1) {
            return true;
        }
        if (supportsSizeChanges == 2 || supportsSizeChanges == 3) {
            return false;
        }
        if (getRootTask() != null && getRootTask().getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
            return false;
        }
        if (inMultiWindowMode() || getWindowConfiguration().hasWindowDecorCaption()) {
            Task task = this.task;
            ActivityRecord rootActivity = task != null ? task.getRootActivity() : null;
            if (rootActivity != null && rootActivity != this && !rootActivity.shouldCreateCompatDisplayInsets()) {
                return false;
            }
        }
        return !isResizeable() && (this.info.isFixedOrientation() || hasFixedAspectRatio()) && !((this.mActivityRecordExt.isZoomMode(getWindowingMode()) && !this.mActivityRecordExt.getMaxBoundsForZoomWindow()) || this.mActivityRecordExt.isCompactWindowingMode(getWindowingMode()) || this.mActivityRecordExt.inOplusCompatMode() || !this.mActivityRecordExt.shouldCreateCompatDisplayInsetsForSquare(this) || this.mActivityRecordExt.shouldCreateCompatDisplayInsetsForMirageWindow(getDisplayId()) || !isActivityTypeStandardOrUndefined() || this.mActivityRecordExt.isSupprotBracketMode(this) || this.mActivityRecordExt.isActivityPreloadDisplay(getDisplayId(), this.mActivityRecordExt.getLastReportedDisplay()));
    }

    private int supportsSizeChanges() {
        if (this.mLetterboxUiController.shouldOverrideForceNonResizeApp()) {
            return 1;
        }
        if (this.info.supportsSizeChanges) {
            return 2;
        }
        return this.mLetterboxUiController.shouldOverrideForceResizeApp() ? 3 : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowToken
    public boolean hasSizeCompatBounds() {
        return this.mSizeCompatBounds != null;
    }

    private void updateCompatDisplayInsets() {
        if (getCompatDisplayInsets() == null && shouldCreateCompatDisplayInsets()) {
            Configuration requestedOverrideConfiguration = getRequestedOverrideConfiguration();
            Configuration configuration = getConfiguration();
            requestedOverrideConfiguration.colorMode = configuration.colorMode;
            requestedOverrideConfiguration.densityDpi = configuration.densityDpi;
            requestedOverrideConfiguration.smallestScreenWidthDp = configuration.smallestScreenWidthDp;
            if (ActivityInfo.isFixedOrientation(getOverrideOrientation())) {
                requestedOverrideConfiguration.windowConfiguration.setRotation(configuration.windowConfiguration.getRotation());
            }
            this.mCompatDisplayInsets = new CompatDisplayInsets(this.mDisplayContent, this, this.mLetterboxBoundsForFixedOrientationAndAspectRatio);
        }
    }

    private void clearSizeCompatModeAttributes() {
        this.mInSizeCompatModeForBounds = false;
        this.mSizeCompatScale = 1.0f;
        this.mSizeCompatBounds = null;
        this.mCompatDisplayInsets = null;
        this.mLetterboxUiController.clearInheritedCompatDisplayInsets();
    }

    @VisibleForTesting
    void clearSizeCompatMode() {
        float f = this.mSizeCompatScale;
        clearSizeCompatModeAttributes();
        if (this.mSizeCompatScale != f) {
            forAllWindows((Consumer<WindowState>) new ActivityRecord$$ExternalSyntheticLambda12(), false);
        }
        int activityType = getActivityType();
        Configuration requestedOverrideConfiguration = getRequestedOverrideConfiguration();
        requestedOverrideConfiguration.unset();
        requestedOverrideConfiguration.windowConfiguration.setActivityType(activityType);
        onRequestedOverrideConfigurationChanged(requestedOverrideConfiguration);
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean matchParentBounds() {
        WindowContainer parent;
        Rect resolvedOverrideBounds = getResolvedOverrideBounds();
        return resolvedOverrideBounds.isEmpty() || (parent = getParent()) == null || parent.getBounds().equals(resolvedOverrideBounds);
    }

    @Override // com.android.server.wm.WindowToken
    float getCompatScale() {
        if (this.mActivityRecordExt.hasSizeCompatBoundsInOplusCompatMode()) {
            return 1.0f;
        }
        return hasSizeCompatBounds() ? this.mSizeCompatScale : super.getCompatScale();
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.ConfigurationContainer
    void resolveOverrideConfiguration(Configuration configuration) {
        Configuration requestedOverrideConfiguration = getRequestedOverrideConfiguration();
        int i = requestedOverrideConfiguration.assetsSeq;
        if (i != 0 && configuration.assetsSeq > i) {
            requestedOverrideConfiguration.assetsSeq = 0;
        }
        super.resolveOverrideConfiguration(configuration);
        Configuration resolvedOverrideConfiguration = getResolvedOverrideConfiguration();
        applyLocaleOverrideIfNeeded(resolvedOverrideConfiguration);
        if (isFixedRotationTransforming()) {
            this.mTmpConfig.setTo(configuration);
            this.mTmpConfig.updateFrom(resolvedOverrideConfiguration);
            configuration = this.mTmpConfig;
        }
        this.mIsAspectRatioApplied = false;
        this.mIsEligibleForFixedOrientationLetterbox = false;
        this.mLetterboxBoundsForFixedOrientationAndAspectRatio = null;
        int windowingMode = configuration.windowConfiguration.getWindowingMode();
        boolean z = windowingMode == 6 || windowingMode == 1 || (!this.mWaitForEnteringPinnedMode && windowingMode == 2 && resolvedOverrideConfiguration.windowConfiguration.getWindowingMode() == 1);
        if (z && !((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).isMirageCarMode(getDisplayId()) && !this.mActivityRecordExt.inOplusCompatMode()) {
            resolveFixedOrientationConfiguration(configuration);
        }
        if (this.mCompatDisplayInsets != null && this.mActivityRecordExt.shouldClearSizeCompatMode(configuration)) {
            clearSizeCompatModeIfNeeded();
        }
        CompatDisplayInsets compatDisplayInsets = getCompatDisplayInsets();
        if (compatDisplayInsets != null) {
            resolveSizeCompatModeConfiguration(configuration, compatDisplayInsets);
        } else if (inMultiWindowMode() && !z) {
            resolvedOverrideConfiguration.orientation = 0;
            if (!matchParentBounds()) {
                getTaskFragment().computeConfigResourceOverrides(resolvedOverrideConfiguration, configuration);
            }
        } else if (!isLetterboxedForFixedOrientationAndAspectRatio() && (!((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).isMirageCarMode(getDisplayId()) || this.mActivityRecordExt.inOplusCompatMode())) {
            resolveAspectRatioRestriction(configuration);
        }
        this.mActivityRecordExt.applyOplusCompatAspectRatioIfNeed(resolvedOverrideConfiguration, configuration);
        if (z || compatDisplayInsets != null || (!inMultiWindowMode() && !this.mActivityRecordExt.isCompactWindowingMode(getWindowingMode()))) {
            updateResolvedBoundsPosition(configuration);
        }
        DisplayContent displayContent = this.mDisplayContent;
        boolean z2 = displayContent != null && displayContent.getIgnoreOrientationRequest();
        if (compatDisplayInsets == null && (this.mLetterboxBoundsForFixedOrientationAndAspectRatio != null || (z2 && this.mIsAspectRatioApplied))) {
            resolvedOverrideConfiguration.smallestScreenWidthDp = Math.min(resolvedOverrideConfiguration.screenWidthDp, resolvedOverrideConfiguration.screenHeightDp);
        }
        int i2 = this.mConfigurationSeq + 1;
        this.mConfigurationSeq = i2;
        this.mConfigurationSeq = Math.max(i2, 1);
        getResolvedOverrideConfiguration().seq = this.mConfigurationSeq;
        this.mActivityRecordExt.setMaxBoundsForZoomWindow(true);
        if (providesMaxBounds()) {
            this.mActivityRecordExt.setMaxBoundsForZoomWindow(false);
            this.mTmpBounds.set(resolvedOverrideConfiguration.windowConfiguration.getBounds());
            if (this.mTmpBounds.isEmpty()) {
                this.mTmpBounds.set(configuration.windowConfiguration.getBounds());
            }
            if (WindowManagerDebugConfig.DEBUG_CONFIGURATION && ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_CONFIGURATION, -108977760, 0, (String) null, new Object[]{String.valueOf(getUid()), String.valueOf(this.mTmpBounds), String.valueOf(this.info.neverSandboxDisplayApis(sConstrainDisplayApisConfig)), String.valueOf(this.info.alwaysSandboxDisplayApis(sConstrainDisplayApisConfig)), String.valueOf(!matchParentBounds()), String.valueOf(compatDisplayInsets != null), String.valueOf(shouldCreateCompatDisplayInsets())});
            }
            resolvedOverrideConfiguration.windowConfiguration.setMaxBounds(this.mTmpBounds);
        } else {
            this.mActivityRecordExt.setMaxBoundsForZoomWindow(false);
        }
        logAppCompatState();
        this.mActivityRecordExt.adjustAppCutoutInCompactWindow(this, configuration.windowConfiguration.getAppBounds(), resolvedOverrideConfiguration);
        this.mActivityRecordExt.resolveAppOrientationIfNeed(this, resolvedOverrideConfiguration, getOverrideOrientation(), configuration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Configuration.Orientation
    public int getOrientationForReachability() {
        if (this.mLetterboxUiController.hasInheritedLetterboxBehavior()) {
            return this.mLetterboxUiController.getInheritedOrientation();
        }
        return getRequestedConfigurationOrientation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean areBoundsLetterboxed() {
        return getAppCompatState(true) != 2;
    }

    private void logAppCompatState() {
        this.mTaskSupervisor.getActivityMetricsLogger().logAppCompatState(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAppCompatState() {
        return getAppCompatState(false);
    }

    private int getAppCompatState(boolean z) {
        if (!z && !this.mVisibleRequested) {
            return 1;
        }
        if (this.mLetterboxUiController.hasInheritedLetterboxBehavior()) {
            return this.mLetterboxUiController.getInheritedAppCompatState();
        }
        if (this.mInSizeCompatModeForBounds) {
            return 3;
        }
        if (isLetterboxedForFixedOrientationAndAspectRatio()) {
            return 4;
        }
        return this.mIsAspectRatioApplied ? 5 : 2;
    }

    private void updateResolvedBoundsPosition(Configuration configuration) {
        int i;
        Configuration resolvedOverrideConfiguration = getResolvedOverrideConfiguration();
        Rect bounds = resolvedOverrideConfiguration.windowConfiguration.getBounds();
        if (bounds.isEmpty()) {
            return;
        }
        Rect rect = this.mSizeCompatBounds;
        if (rect == null) {
            rect = bounds;
        }
        Rect appBounds = configuration.windowConfiguration.getAppBounds();
        Rect bounds2 = configuration.windowConfiguration.getBounds();
        if (bounds.isEmpty()) {
            return;
        }
        if (bounds2.width() != rect.width() || this.mActivityRecordExt.inOplusCompatMode() || this.mActivityRecordExt.inOplusActivityCompatMode()) {
            float width = rect.width();
            int max = (((float) bounds2.width()) == width || width > appBounds.width()) ? 0 : Math.max(0, (((int) Math.ceil((r4 - width) * this.mLetterboxUiController.getHorizontalPositionMultiplier(configuration))) - rect.left) + appBounds.left);
            float height = appBounds.height();
            float height2 = bounds2.height();
            float height3 = rect.height();
            if (height2 == height3 || height3 > height) {
                i = 0;
            } else {
                float verticalPositionMultiplier = this.mLetterboxUiController.getVerticalPositionMultiplier(configuration);
                if (this.mDisplayContent.getDisplayPolicy().isImmersiveMode()) {
                    height = height2;
                }
                i = Math.max(0, (((int) Math.ceil((height - height3) * verticalPositionMultiplier)) - rect.top) + appBounds.top);
            }
            Rect rect2 = this.mSizeCompatBounds;
            if (rect2 != null) {
                rect2.offset(max, i);
                Rect rect3 = this.mSizeCompatBounds;
                offsetBounds(resolvedOverrideConfiguration, rect3.left - bounds.left, rect3.top - bounds.top);
            } else {
                int[] iArr = {max};
                this.mActivityRecordExt.calculateOplusCompatBoundsOffset(iArr, this, configuration, this.mActivityComponent, bounds2, bounds, getRequestedConfigurationOrientation());
                this.mActivityRecordExt.setAppBoundsIfNeed(this, resolvedOverrideConfiguration);
                this.mActivityRecordExt.calculateFlexibleOffset(iArr);
                offsetBounds(resolvedOverrideConfiguration, iArr[0], iArr[1]);
            }
            if (resolvedOverrideConfiguration.windowConfiguration.getAppBounds().top == appBounds.top) {
                resolvedOverrideConfiguration.windowConfiguration.getBounds().top = bounds2.top;
                Rect rect4 = this.mSizeCompatBounds;
                if (rect4 != null) {
                    rect4.top = bounds2.top;
                }
            }
            getTaskFragment().computeConfigResourceOverrides(resolvedOverrideConfiguration, configuration);
            float f = this.mSizeCompatScale;
            if (f != 1.0f) {
                int i2 = bounds.left;
                int i3 = bounds.top;
                offsetBounds(resolvedOverrideConfiguration, ((int) ((i2 / f) + 0.5f)) - i2, ((int) ((i3 / f) + 0.5f)) - i3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getScreenResolvedBounds() {
        Rect bounds = getResolvedOverrideConfiguration().windowConfiguration.getBounds();
        Rect rect = this.mSizeCompatBounds;
        return rect != null ? rect : bounds;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recomputeConfiguration() {
        if (this.mLetterboxUiController.applyOnOpaqueActivityBelow(new Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda30
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ActivityRecord) obj).recomputeConfiguration();
            }
        })) {
            return;
        }
        onRequestedOverrideConfigurationChanged(getRequestedOverrideConfiguration());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInTransition() {
        return inTransitionSelfOrParent();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDisplaySleepingAndSwapping() {
        for (int size = this.mDisplayContent.mAllSleepTokens.size() - 1; size >= 0; size--) {
            if (this.mDisplayContent.mAllSleepTokens.get(size).isDisplaySwapping()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLetterboxedForFixedOrientationAndAspectRatio() {
        return this.mLetterboxBoundsForFixedOrientationAndAspectRatio != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAspectRatioApplied() {
        return this.mIsAspectRatioApplied;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEligibleForLetterboxEducation() {
        return this.mWmService.mLetterboxConfiguration.getIsEducationEnabled() && this.mIsEligibleForFixedOrientationLetterbox && getWindowingMode() == 1 && getRequestedConfigurationOrientation() == 1 && this.mStartingWindow == null;
    }

    private boolean orientationRespectedWithInsets(Rect rect, Rect rect2) {
        int requestedConfigurationOrientation;
        DisplayInfo displayInfo;
        rect2.setEmpty();
        boolean z = true;
        if (this.mDisplayContent == null || (requestedConfigurationOrientation = getRequestedConfigurationOrientation()) == 0) {
            return true;
        }
        int i = rect.height() >= rect.width() ? 1 : 2;
        if (isFixedRotationTransforming()) {
            displayInfo = getFixedRotationTransformDisplayInfo();
        } else {
            displayInfo = this.mDisplayContent.getDisplayInfo();
        }
        getTask().calculateInsetFrames(this.mTmpBounds, rect2, rect, displayInfo);
        int i2 = rect2.height() >= rect2.width() ? 1 : 2;
        if (i != i2 && i2 != requestedConfigurationOrientation) {
            z = false;
        }
        if (z) {
            rect2.setEmpty();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean handlesOrientationChangeFromDescendant(int i) {
        if (shouldIgnoreOrientationRequests()) {
            return false;
        }
        return super.handlesOrientationChangeFromDescendant(i);
    }

    private void resolveFixedOrientationConfiguration(Configuration configuration) {
        boolean isResizeable;
        Rect bounds = configuration.windowConfiguration.getBounds();
        Rect rect = new Rect();
        boolean orientationRespectedWithInsets = orientationRespectedWithInsets(bounds, rect);
        if (handlesOrientationChangeFromDescendant(getOverrideOrientation()) && this.mActivityRecordExt.ignoreOrientationRespectedWithInsets(this, orientationRespectedWithInsets)) {
            return;
        }
        TaskFragment organizedTaskFragment = getOrganizedTaskFragment();
        if (organizedTaskFragment == null || organizedTaskFragment.fillsParent()) {
            Task task = this.task;
            boolean z = false;
            if (task != null) {
                isResizeable = task.isResizeable() || isResizeable();
            } else {
                isResizeable = isResizeable();
            }
            if (WindowConfiguration.inMultiWindowMode(configuration.windowConfiguration.getWindowingMode()) && isResizeable) {
                return;
            }
            Rect bounds2 = getResolvedOverrideConfiguration().windowConfiguration.getBounds();
            int i = configuration.orientation;
            int requestedConfigurationOrientation = getRequestedConfigurationOrientation();
            if (requestedConfigurationOrientation != 0 && requestedConfigurationOrientation != i) {
                z = true;
            }
            this.mIsEligibleForFixedOrientationLetterbox = z;
            if (z || !(requestedConfigurationOrientation == 0 || orientationRespectedWithInsets)) {
                CompatDisplayInsets compatDisplayInsets = getCompatDisplayInsets();
                if (compatDisplayInsets == null || compatDisplayInsets.mIsInFixedOrientationLetterbox) {
                    if (orientationRespectedWithInsets) {
                        rect = configuration.windowConfiguration.getAppBounds();
                    }
                    Rect rect2 = new Rect();
                    Rect rect3 = new Rect();
                    if (requestedConfigurationOrientation == 2) {
                        int min = Math.min((rect.top + bounds.width()) - 1, rect.bottom);
                        rect2.set(bounds.left, rect.top, bounds.right, min);
                        rect3.set(rect.left, rect.top, rect.right, min);
                    } else {
                        int min2 = Math.min(rect.left + bounds.height(), rect.right);
                        rect2.set(rect.left, bounds.top, min2, bounds.bottom);
                        rect3.set(rect.left, rect.top, min2, rect.bottom);
                    }
                    Rect rect4 = new Rect(bounds2);
                    bounds2.set(rect2);
                    float fixedOrientationLetterboxAspectRatio = this.mLetterboxUiController.getFixedOrientationLetterboxAspectRatio(configuration);
                    if (isDefaultMultiWindowLetterboxAspectRatioDesired(configuration)) {
                        fixedOrientationLetterboxAspectRatio = 1.01f;
                    } else if (fixedOrientationLetterboxAspectRatio <= 1.0f) {
                        fixedOrientationLetterboxAspectRatio = computeAspectRatio(bounds);
                    }
                    this.mIsAspectRatioApplied = applyAspectRatio(bounds2, rect3, rect2, fixedOrientationLetterboxAspectRatio);
                    if (compatDisplayInsets != null) {
                        compatDisplayInsets.getBoundsByRotation(this.mTmpBounds, configuration.windowConfiguration.getRotation());
                        if (bounds2.width() != this.mTmpBounds.width() || bounds2.height() != this.mTmpBounds.height()) {
                            bounds2.set(rect4);
                            return;
                        }
                    }
                    getTaskFragment().computeConfigResourceOverrides(getResolvedOverrideConfiguration(), configuration, compatDisplayInsets);
                    this.mLetterboxBoundsForFixedOrientationAndAspectRatio = new Rect(bounds2);
                }
            }
        }
    }

    private boolean isDefaultMultiWindowLetterboxAspectRatioDesired(Configuration configuration) {
        return (this.mDisplayContent == null || !WindowConfiguration.inMultiWindowMode(configuration.windowConfiguration.getWindowingMode()) || this.mDisplayContent.getIgnoreOrientationRequest()) ? false : true;
    }

    private void resolveAspectRatioRestriction(Configuration configuration) {
        Configuration resolvedOverrideConfiguration = getResolvedOverrideConfiguration();
        Rect appBounds = configuration.windowConfiguration.getAppBounds();
        Rect bounds = configuration.windowConfiguration.getBounds();
        Rect bounds2 = resolvedOverrideConfiguration.windowConfiguration.getBounds();
        this.mTmpBounds.setEmpty();
        this.mIsAspectRatioApplied = applyAspectRatio(this.mTmpBounds, appBounds, bounds);
        this.mActivityRecordExt.adjustBracketMode(configuration, appBounds, bounds, this.mTmpBounds, this);
        this.mActivityRecordExt.resolveFlexibleActivityConfig(configuration, this.mTmpBounds);
        if (!this.mTmpBounds.isEmpty()) {
            bounds2.set(this.mTmpBounds);
        }
        if (!bounds2.isEmpty() && !bounds2.equals(bounds)) {
            getTaskFragment().computeConfigResourceOverrides(resolvedOverrideConfiguration, configuration, getFixedRotationTransformDisplayInfo(), null, this);
        }
        this.mActivityRecordExt.resolveAppOrientationIfNeed(this, resolvedOverrideConfiguration, getOverrideOrientation(), configuration);
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01ab  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01ba  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01c1  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01ce  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01bd  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0185  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void resolveSizeCompatModeConfiguration(Configuration configuration, CompatDisplayInsets compatDisplayInsets) {
        Task task;
        Rect bounds;
        Rect appBounds;
        int i;
        Rect appBounds2;
        float f;
        int i2;
        int i3;
        int i4;
        Rect rect;
        Configuration resolvedOverrideConfiguration = getResolvedOverrideConfiguration();
        if (this.mActivityRecordExt.isMirageWindowDisplayId(getDisplayId()) || ((task = this.task) != null && task.getWrapper().getExtImpl().getLaunchedFromMultiSearch())) {
            float f2 = this.mSizeCompatScale;
            this.mInSizeCompatModeForBounds = false;
            resolvedOverrideConfiguration.unset();
            this.mSizeCompatBounds = null;
            this.mSizeCompatScale = 1.0f;
            this.mCompatDisplayInsets = null;
            if (1.0f != f2) {
                forAllWindows((Consumer<WindowState>) new ActivityRecord$$ExternalSyntheticLambda12(), false);
                return;
            }
            return;
        }
        if (this.mActivityRecordExt.isZoomMode(configuration.windowConfiguration.getWindowingMode()) || ((getRootTask() != null && getRootTask().getWrapper().getExtImpl().isPuttTask()) || (getRootTask() != null && getRootTask().getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])))) {
            float f3 = this.mSizeCompatScale;
            this.mInSizeCompatModeForBounds = false;
            this.mSizeCompatScale = 1.0f;
            this.mSizeCompatBounds = null;
            this.mCompatDisplayInsets = null;
            if (1.0f != f3) {
                forAllWindows((Consumer<WindowState>) new ActivityRecord$$ExternalSyntheticLambda12(), false);
                return;
            }
            return;
        }
        Rect bounds2 = resolvedOverrideConfiguration.windowConfiguration.getBounds();
        if (isLetterboxedForFixedOrientationAndAspectRatio()) {
            bounds = new Rect(bounds2);
        } else {
            bounds = configuration.windowConfiguration.getBounds();
        }
        Rect rect2 = bounds;
        if (isLetterboxedForFixedOrientationAndAspectRatio()) {
            appBounds = new Rect(getResolvedOverrideConfiguration().windowConfiguration.getAppBounds());
        } else {
            appBounds = configuration.windowConfiguration.getAppBounds();
        }
        Rect rect3 = appBounds;
        int requestedConfigurationOrientation = getRequestedConfigurationOrientation();
        boolean z = (requestedConfigurationOrientation == 0 || ((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).isMirageCarMode(getDisplayId()) || this.mDisplayContent.getIgnoreOrientationRequest() || this.mActivityRecordExt.shouldReviseScreenOrientationForApp(this)) ? false : true;
        if (!z && (requestedConfigurationOrientation = compatDisplayInsets.mOriginalRequestedOrientation) == 0) {
            requestedConfigurationOrientation = configuration.orientation;
        }
        int i5 = requestedConfigurationOrientation;
        int rotation = configuration.windowConfiguration.getRotation();
        DisplayContent displayContent = this.mDisplayContent;
        boolean z2 = displayContent == null || displayContent.getDisplayRotation().isFixedToUserRotation();
        if (!z2 && !compatDisplayInsets.mIsFloating) {
            resolvedOverrideConfiguration.windowConfiguration.setRotation(rotation);
        } else {
            int rotation2 = resolvedOverrideConfiguration.windowConfiguration.getRotation();
            if (rotation2 != -1) {
                i = rotation2;
                Rect rect4 = new Rect();
                Rect rect5 = this.mTmpBounds;
                compatDisplayInsets.getContainerBounds(rect4, rect5, i, i5, z, z2);
                bounds2.set(rect5);
                if (!compatDisplayInsets.mIsFloating) {
                    this.mIsAspectRatioApplied = applyAspectRatio(bounds2, rect4, rect5);
                }
                getTaskFragment().computeConfigResourceOverrides(resolvedOverrideConfiguration, configuration, compatDisplayInsets);
                resolvedOverrideConfiguration.screenLayout = WindowContainer.computeScreenLayout(getConfiguration().screenLayout, resolvedOverrideConfiguration.screenWidthDp, resolvedOverrideConfiguration.screenHeightDp);
                if (resolvedOverrideConfiguration.screenWidthDp == resolvedOverrideConfiguration.screenHeightDp) {
                    resolvedOverrideConfiguration.orientation = configuration.orientation;
                }
                appBounds2 = resolvedOverrideConfiguration.windowConfiguration.getAppBounds();
                f = this.mSizeCompatScale;
                updateSizeCompatScale(appBounds2, rect3);
                i2 = rect3.top - rect2.top;
                boolean z3 = i2 != appBounds2.top - bounds2.top;
                if (this.mSizeCompatScale == 1.0f || z3) {
                    if (this.mSizeCompatBounds == null) {
                        this.mSizeCompatBounds = new Rect();
                    }
                    this.mSizeCompatBounds.set(appBounds2);
                    this.mSizeCompatBounds.offsetTo(0, 0);
                    this.mSizeCompatBounds.scale(this.mSizeCompatScale);
                    this.mSizeCompatBounds.bottom += i2;
                } else {
                    this.mSizeCompatBounds = null;
                }
                if (this.mSizeCompatScale != f) {
                    forAllWindows((Consumer<WindowState>) new ActivityRecord$$ExternalSyntheticLambda12(), false);
                }
                boolean equals = bounds2.equals(rect5);
                i3 = !equals ? rect2.left : rect3.left;
                i4 = !equals ? rect2.top : rect3.top;
                if (i3 == 0 || i4 != 0) {
                    rect = this.mSizeCompatBounds;
                    if (rect != null) {
                        rect.offset(i3, i4);
                    }
                    int i6 = i3 - bounds2.left;
                    int i7 = i4 - bounds2.top;
                    if (!this.mActivityRecordExt.shouldSizeCompatVerticalCenter()) {
                        offsetBounds(resolvedOverrideConfiguration, i6, i7);
                    } else {
                        offsetBounds(resolvedOverrideConfiguration, i6, 0);
                    }
                }
                this.mInSizeCompatModeForBounds = isInSizeCompatModeForBounds(appBounds2, rect3);
            }
        }
        i = rotation;
        Rect rect42 = new Rect();
        Rect rect52 = this.mTmpBounds;
        compatDisplayInsets.getContainerBounds(rect42, rect52, i, i5, z, z2);
        bounds2.set(rect52);
        if (!compatDisplayInsets.mIsFloating) {
        }
        getTaskFragment().computeConfigResourceOverrides(resolvedOverrideConfiguration, configuration, compatDisplayInsets);
        resolvedOverrideConfiguration.screenLayout = WindowContainer.computeScreenLayout(getConfiguration().screenLayout, resolvedOverrideConfiguration.screenWidthDp, resolvedOverrideConfiguration.screenHeightDp);
        if (resolvedOverrideConfiguration.screenWidthDp == resolvedOverrideConfiguration.screenHeightDp) {
        }
        appBounds2 = resolvedOverrideConfiguration.windowConfiguration.getAppBounds();
        f = this.mSizeCompatScale;
        updateSizeCompatScale(appBounds2, rect3);
        i2 = rect3.top - rect2.top;
        if (i2 != appBounds2.top - bounds2.top) {
        }
        if (this.mSizeCompatScale == 1.0f) {
        }
        if (this.mSizeCompatBounds == null) {
        }
        this.mSizeCompatBounds.set(appBounds2);
        this.mSizeCompatBounds.offsetTo(0, 0);
        this.mSizeCompatBounds.scale(this.mSizeCompatScale);
        this.mSizeCompatBounds.bottom += i2;
        if (this.mSizeCompatScale != f) {
        }
        boolean equals2 = bounds2.equals(rect52);
        if (!equals2) {
        }
        if (!equals2) {
        }
        if (i3 == 0) {
        }
        rect = this.mSizeCompatBounds;
        if (rect != null) {
        }
        int i62 = i3 - bounds2.left;
        int i72 = i4 - bounds2.top;
        if (!this.mActivityRecordExt.shouldSizeCompatVerticalCenter()) {
        }
        this.mInSizeCompatModeForBounds = isInSizeCompatModeForBounds(appBounds2, rect3);
    }

    void updateSizeCompatScale(final Rect rect, final Rect rect2) {
        this.mSizeCompatScale = ((Float) this.mLetterboxUiController.findOpaqueNotFinishingActivityBelow().map(new Function() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda36
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Float lambda$updateSizeCompatScale$22;
                lambda$updateSizeCompatScale$22 = ActivityRecord.lambda$updateSizeCompatScale$22((ActivityRecord) obj);
                return lambda$updateSizeCompatScale$22;
            }
        }).orElseGet(new Supplier() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda37
            @Override // java.util.function.Supplier
            public final Object get() {
                Float lambda$updateSizeCompatScale$23;
                lambda$updateSizeCompatScale$23 = ActivityRecord.lambda$updateSizeCompatScale$23(rect, rect2);
                return lambda$updateSizeCompatScale$23;
            }
        })).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Float lambda$updateSizeCompatScale$22(ActivityRecord activityRecord) {
        return Float.valueOf(activityRecord.mSizeCompatScale);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Float lambda$updateSizeCompatScale$23(Rect rect, Rect rect2) {
        int width = rect.width();
        int height = rect.height();
        int width2 = rect2.width();
        int height2 = rect2.height();
        return Float.valueOf((width > width2 || height > height2) ? Math.min(width2 / width, height2 / height) : 1.0f);
    }

    private boolean isInSizeCompatModeForBounds(Rect rect, Rect rect2) {
        if (this.mLetterboxUiController.hasInheritedLetterboxBehavior()) {
            return false;
        }
        int width = rect.width();
        int height = rect.height();
        int width2 = rect2.width();
        int height2 = rect2.height();
        if (width2 == width && height2 == height) {
            return false;
        }
        if ((width2 <= width || height2 <= height) && width2 >= width && height2 >= height) {
            float maxAspectRatio = getMaxAspectRatio();
            if (maxAspectRatio > 0.0f && (Math.max(width, height) + 0.5f) / Math.min(width, height) >= maxAspectRatio) {
                return false;
            }
            float minAspectRatio = getMinAspectRatio();
            if (minAspectRatio > 0.0f && (Math.max(width2, height2) + 0.5f) / Math.min(width2, height2) <= minAspectRatio) {
                return false;
            }
        }
        return true;
    }

    private static void offsetBounds(Configuration configuration, int i, int i2) {
        WindowConfiguration windowConfiguration;
        if (configuration == null || (windowConfiguration = configuration.windowConfiguration) == null) {
            return;
        }
        if (windowConfiguration.getBounds() != null) {
            configuration.windowConfiguration.getBounds().offset(i, i2);
        }
        if (configuration.windowConfiguration.getAppBounds() != null) {
            configuration.windowConfiguration.getAppBounds().offset(i, i2);
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public Rect getBounds() {
        final Rect bounds = super.getBounds();
        return (Rect) this.mLetterboxUiController.findOpaqueNotFinishingActivityBelow().map(new Function() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda14
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((ActivityRecord) obj).getBounds();
            }
        }).orElseGet(new Supplier() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda15
            @Override // java.util.function.Supplier
            public final Object get() {
                Rect lambda$getBounds$24;
                lambda$getBounds$24 = ActivityRecord.this.lambda$getBounds$24(bounds);
                return lambda$getBounds$24;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Rect lambda$getBounds$24(Rect rect) {
        if (this.mSizeCompatBounds == null) {
            return this.mActivityRecordExt.hasSizeCompatBoundsInOplusCompatMode() ? this.mActivityRecordExt.getSizeCompatBoundsInOplusCompatMode() : rect;
        }
        if (this.mActivityRecordExt.isZoomMode(getWindowingMode())) {
            return super.getBounds();
        }
        if (this.mActivityRecordExt.isCompactWindowingMode(getWindowingMode())) {
            return super.getBounds();
        }
        return this.mSizeCompatBounds;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean providesMaxBounds() {
        Task task;
        if (getUid() == 1000) {
            return false;
        }
        DisplayContent displayContent = this.mDisplayContent;
        if ((displayContent == null || displayContent.sandboxDisplayApis()) && !this.info.neverSandboxDisplayApis(sConstrainDisplayApisConfig)) {
            return this.info.alwaysSandboxDisplayApis(sConstrainDisplayApisConfig) || getCompatDisplayInsets() != null || shouldCreateCompatDisplayInsets() || this.mActivityRecordExt.getLaunchedFromMultiSearch() || ((task = this.task) != null && task.getWrapper().getExtImpl().getLaunchedFromMultiSearch()) || this.mActivityRecordExt.inOplusCompatMode() || this.mActivityRecordExt.inOplusActivityCompatMode();
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    @VisibleForTesting
    Rect getAnimationBounds(int i) {
        TaskFragment taskFragment = getTaskFragment();
        return taskFragment != null ? taskFragment.getBounds() : getBounds();
    }

    @Override // com.android.server.wm.WindowContainer
    void getAnimationPosition(Point point) {
        point.set(0, 0);
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(Configuration configuration) {
        Task task;
        Task task2;
        int hookRotationForPIPIfNeeded;
        int requestedOverrideWindowingMode;
        this.mActivityRecordExt.onPreActivityRecordConfigurationChanged(configuration);
        if (this.mActivityRecordExt.inOplusCompatMode()) {
            clearSizeCompatModeIfNeeded();
        }
        if (this.mTransitionController.isShellTransitionsEnabled() && isVisible() && isVisibleRequested()) {
            if (getRequestedOverrideWindowingMode() == 0) {
                requestedOverrideWindowingMode = configuration.windowConfiguration.getWindowingMode();
            } else {
                requestedOverrideWindowingMode = getRequestedOverrideWindowingMode();
            }
            if (getWindowingMode() != requestedOverrideWindowingMode) {
                this.mTransitionController.collect(this);
            }
        }
        if (getCompatDisplayInsets() != null) {
            Configuration requestedOverrideConfiguration = getRequestedOverrideConfiguration();
            boolean z = requestedOverrideConfiguration.windowConfiguration.getRotation() != -1;
            int requestedConfigurationOrientation = getRequestedConfigurationOrientation();
            if (requestedConfigurationOrientation != 0 && requestedConfigurationOrientation != getConfiguration().orientation && requestedConfigurationOrientation == getParent().getConfiguration().orientation && requestedOverrideConfiguration.windowConfiguration.getRotation() != getParent().getWindowConfiguration().getRotation()) {
                requestedOverrideConfiguration.windowConfiguration.setRotation(getParent().getWindowConfiguration().getRotation());
                onRequestedOverrideConfigurationChanged(requestedOverrideConfiguration);
                return;
            } else if (z && requestedConfigurationOrientation == 0 && requestedOverrideConfiguration.windowConfiguration.getRotation() != -1) {
                requestedOverrideConfiguration.windowConfiguration.setRotation(-1);
                onRequestedOverrideConfigurationChanged(requestedOverrideConfiguration);
                return;
            }
        }
        boolean inPinnedWindowingMode = inPinnedWindowingMode();
        DisplayContent displayContent = this.mDisplayContent;
        int activityType = getActivityType();
        if (inPinnedWindowingMode && attachedToProcess() && displayContent != null) {
            try {
                this.app.pauseConfigurationDispatch();
                super.onConfigurationChanged(configuration);
                if (this.mVisibleRequested && !inMultiWindowMode() && (hookRotationForPIPIfNeeded = this.mActivityRecordExt.hookRotationForPIPIfNeeded(displayContent.rotationForActivityInDifferentOrientation(this), displayContent, this)) != -1) {
                    this.app.resumeConfigurationDispatch();
                    displayContent.setFixedRotationLaunchingApp(this, hookRotationForPIPIfNeeded);
                }
            } finally {
                if (this.app.resumeConfigurationDispatch()) {
                    WindowProcessController windowProcessController = this.app;
                    windowProcessController.dispatchConfiguration(windowProcessController.getConfiguration());
                }
            }
        } else {
            super.onConfigurationChanged(configuration);
        }
        if (activityType != 0 && activityType != getActivityType()) {
            String str = "Can't change activity type once set: " + this + " activityType=" + WindowConfiguration.activityTypeToString(getActivityType()) + ", was " + WindowConfiguration.activityTypeToString(activityType);
            if (Build.IS_DEBUGGABLE) {
                throw new IllegalStateException(str);
            }
            Slog.w(TAG, str);
        }
        if (getMergedOverrideConfiguration().seq != getResolvedOverrideConfiguration().seq) {
            onMergedOverrideConfigurationChanged();
        }
        if (!inPinnedWindowingMode && inPinnedWindowingMode() && (task2 = this.task) != null) {
            this.mWaitForEnteringPinnedMode = false;
            this.mTaskSupervisor.scheduleUpdatePictureInPictureModeIfNeeded(task2, task2.getBounds());
        }
        if (this.mWaitForEnteringPinnedMode && !inPinnedWindowingMode && !inPinnedWindowingMode() && (task = this.task) != null && !task.inPinnedWindowingMode()) {
            this.mWaitForEnteringPinnedMode = false;
            Slog.d(TAG, "force reset mWaitForEnteringPinnedMode=" + this.mWaitForEnteringPinnedMode + ", this=" + this + ",task=" + this.task);
        }
        if (displayContent == null) {
            return;
        }
        if (this.mVisibleRequested) {
            displayContent.handleActivitySizeCompatModeIfNeeded(this);
            return;
        }
        if (getCompatDisplayInsets() == null || this.visibleIgnoringKeyguard) {
            return;
        }
        WindowProcessController windowProcessController2 = this.app;
        if (windowProcessController2 == null || !windowProcessController2.hasVisibleActivities()) {
            int currentOverrideConfigurationChanges = displayContent.getCurrentOverrideConfigurationChanges();
            if ((hasResizeChange(currentOverrideConfigurationChanges) && (currentOverrideConfigurationChanges & 536872064) != 536872064) || (currentOverrideConfigurationChanges & 4096) != 0) {
                restartProcessIfVisible();
            }
        }
    }

    private boolean applyAspectRatio(Rect rect, Rect rect2, Rect rect3) {
        return applyAspectRatio(rect, rect2, rect3, 0.0f);
    }

    private boolean applyAspectRatio(Rect rect, Rect rect2, Rect rect3, float f) {
        float f2;
        float f3;
        int i;
        int i2;
        if (this.mActivityRecordExt.dontApplyAspectRatio(this)) {
            return false;
        }
        float fixedAspectRatioForActivity = this.mActivityRecordExt.getFixedAspectRatioForActivity(this, true);
        if (fixedAspectRatioForActivity <= 0.0f) {
            fixedAspectRatioForActivity = this.mActivityRecordExt.getMaxAspectRatio(this.info, rect2);
        }
        Task rootTask = getRootTask();
        float fixedAspectRatioForActivity2 = this.mActivityRecordExt.getFixedAspectRatioForActivity(this, false);
        if (fixedAspectRatioForActivity2 <= 0.0f) {
            fixedAspectRatioForActivity2 = getMinAspectRatio();
        }
        TaskFragment organizedTaskFragment = getOrganizedTaskFragment();
        Task task = this.task;
        if (task == null || rootTask == null || ((fixedAspectRatioForActivity < 1.0f && fixedAspectRatioForActivity2 < 1.0f && f < 1.0f) || ((task.getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0]) && !this.mActivityRecordExt.inOplusActivityCompatMode()) || isInVrUiMode(getConfiguration()) || !(organizedTaskFragment == null || organizedTaskFragment.fillsParent())))) {
            return false;
        }
        int width = rect2.width();
        int height = rect2.height();
        float computeAspectRatio = computeAspectRatio(rect2);
        boolean z = this.mActivityRecordExt.inOplusCompatMode() || this.mActivityRecordExt.inOplusActivityCompatMode();
        if (z) {
            width = rect3.width();
            height = rect3.height();
            computeAspectRatio = computeAspectRatio(rect3);
        }
        if (f < 1.0f) {
            f = computeAspectRatio;
        }
        float f4 = z ? 0.0f : f;
        if (fixedAspectRatioForActivity < 1.0f || f4 <= fixedAspectRatioForActivity) {
            fixedAspectRatioForActivity = (fixedAspectRatioForActivity2 < 1.0f || f4 >= fixedAspectRatioForActivity2) ? f4 : fixedAspectRatioForActivity2;
        }
        if (computeAspectRatio - fixedAspectRatioForActivity <= ASPECT_RATIO_ROUNDING_TOLERANCE || z) {
            if (fixedAspectRatioForActivity - computeAspectRatio > ASPECT_RATIO_ROUNDING_TOLERANCE || z) {
                int requestedConfigurationOrientation = getRequestedConfigurationOrientation();
                if (this.mActivityRecordExt.adjustActivityWidth(this, requestedConfigurationOrientation == 1 || (requestedConfigurationOrientation != 2 && width < height))) {
                    f3 = height / fixedAspectRatioForActivity;
                    i = (int) (f3 + 0.5f);
                    i2 = height;
                } else {
                    f2 = width / fixedAspectRatioForActivity;
                    i2 = (int) (f2 + 0.5f);
                    i = width;
                }
            } else {
                i = width;
                i2 = height;
            }
        } else if (width < height) {
            f2 = width * fixedAspectRatioForActivity;
            i2 = (int) (f2 + 0.5f);
            i = width;
        } else {
            f3 = height * fixedAspectRatioForActivity;
            i = (int) (f3 + 0.5f);
            i2 = height;
        }
        if (width <= i && height <= i2) {
            return false;
        }
        if (z) {
            int i3 = rect3.left;
            int i4 = rect3.top;
            rect.set(i3, i4, i + i3, i2 + i4);
        } else {
            int i5 = rect2.left;
            int i6 = i + i5;
            if (i6 >= rect2.right) {
                i6 = rect3.right;
                i5 = rect3.left;
            }
            int i7 = rect2.top;
            int i8 = i2 + i7;
            if (i8 >= rect2.bottom) {
                i8 = rect3.bottom;
                i7 = rect3.top;
            }
            rect.set(i5, i7, i6, i8);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getMinAspectRatio() {
        if (this.mLetterboxUiController.hasInheritedLetterboxBehavior()) {
            return this.mLetterboxUiController.getInheritedMinAspectRatio();
        }
        ActivityInfo activityInfo = this.info;
        if (activityInfo.applicationInfo == null) {
            return activityInfo.getMinAspectRatio();
        }
        if (!this.mLetterboxUiController.shouldOverrideMinAspectRatio()) {
            return this.info.getMinAspectRatio();
        }
        if (this.info.isChangeEnabled(203647190L) && !ActivityInfo.isFixedOrientationPortrait(getOverrideOrientation())) {
            return this.info.getMinAspectRatio();
        }
        if (this.info.isChangeEnabled(218959984L) && isParentFullscreenPortrait()) {
            return this.info.getMinAspectRatio();
        }
        if (this.info.isChangeEnabled(208648326L)) {
            return Math.max(this.mLetterboxUiController.getSplitScreenAspectRatio(), this.info.getMinAspectRatio());
        }
        if (this.info.isChangeEnabled(180326787L)) {
            return Math.max(1.7777778f, this.info.getMinAspectRatio());
        }
        if (this.info.isChangeEnabled(180326845L)) {
            return Math.max(1.5f, this.info.getMinAspectRatio());
        }
        return this.info.getMinAspectRatio();
    }

    private boolean isParentFullscreenPortrait() {
        WindowContainer parent = getParent();
        return parent != null && parent.getConfiguration().orientation == 1 && parent.getWindowConfiguration().getWindowingMode() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getMaxAspectRatio() {
        if (this.mLetterboxUiController.hasInheritedLetterboxBehavior()) {
            return this.mLetterboxUiController.getInheritedMaxAspectRatio();
        }
        return this.info.getMaxAspectRatio();
    }

    private boolean hasFixedAspectRatio() {
        return (getMaxAspectRatio() == 0.0f && getMinAspectRatio() == 0.0f) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float computeAspectRatio(Rect rect) {
        int width = rect.width();
        int height = rect.height();
        if (width == 0 || height == 0) {
            return 0.0f;
        }
        return Math.max(width, height) / Math.min(width, height);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldUpdateConfigForDisplayChanged() {
        return this.mLastReportedDisplayId != getDisplayId();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean ensureActivityConfiguration(int i, boolean z) {
        return ensureActivityConfiguration(i, z, false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean ensureActivityConfiguration(int i, boolean z, boolean z2) {
        return ensureActivityConfiguration(i, z, z2, false);
    }

    boolean ensureActivityConfiguration(int i, boolean z, boolean z2, boolean z3) {
        State state;
        Task rootTask = getRootTask();
        if (rootTask == null) {
            return true;
        }
        if (rootTask.mConfigWillChange) {
            if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, -804217032, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            return true;
        }
        if (this.finishing) {
            if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, -846078709, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            stopFreezingScreenLocked(false);
            return true;
        }
        if (isState(State.DESTROYED)) {
            if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, 1105210816, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            return true;
        }
        if (!z2 && ((state = this.mState) == State.STOPPING || state == State.STOPPED || !shouldBeVisible())) {
            if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, 1635062046, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            return true;
        }
        if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, -1791031393, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        int displayId = getDisplayId();
        int i2 = this.mLastReportedDisplayId;
        boolean z4 = i2 != displayId;
        if (z4) {
            this.mActivityRecordExt.activityPreloadHandleDisplayChanged(getProcessGlobalConfiguration(), this.mLastReportedDisplayId);
            this.mActivityRecordExt.setLastReportedDisplay(getDisplayContent());
            this.mLastReportedDisplayId = displayId;
        }
        if (this.mVisibleRequested) {
            updateCompatDisplayInsets();
        }
        this.mTmpConfig.setTo(this.mLastReportedConfiguration.getMergedConfiguration());
        if (getConfiguration().equals(this.mTmpConfig) && !this.forceNewConfig && !z4) {
            if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, -1115019498, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            return true;
        }
        int configurationChanges = getConfigurationChanges(this.mTmpConfig);
        if (z4) {
            configurationChanges = this.mActivityRecordExt.needChangeDiff(configurationChanges, i2, displayId);
        }
        int i3 = configurationChanges;
        Configuration mergedOverrideConfiguration = getMergedOverrideConfiguration();
        this.mActivityRecordExt.reviseMergedOverrideConfiguration(mergedOverrideConfiguration, displayId);
        this.forceNewConfig = this.mActivityRecordExt.shouldForceRelaunch(i3, this.mLastReportedConfiguration, mergedOverrideConfiguration, this.mTmpConfig, z4) | this.forceNewConfig;
        setLastReportedConfiguration(getProcessGlobalConfiguration(), mergedOverrideConfiguration);
        this.mActivityRecordExt.adjustLastReportedConfiguration(getProcessGlobalConfiguration(), mergedOverrideConfiguration, i3, this, this.mLastReportedConfiguration);
        if (this.mState == State.INITIALIZING) {
            if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, -235225312, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            return true;
        }
        if (i3 == 0 && !this.forceNewConfig) {
            if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, -743431900, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            if (z4) {
                scheduleActivityMovedToDisplay(displayId, mergedOverrideConfiguration);
            } else {
                scheduleConfigurationChanged(mergedOverrideConfiguration);
            }
            notifyDisplayCompatPolicyAboutConfigurationChange(this.mLastReportedConfiguration.getMergedConfiguration(), this.mTmpConfig);
            return true;
        }
        if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, -929676529, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(Configuration.configurationDiffToString(i3))});
        }
        if (!attachedToProcess()) {
            if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, 1679569477, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            stopFreezingScreenLocked(false);
            this.forceNewConfig = false;
            return true;
        }
        if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, 1995093920, 0, (String) null, new Object[]{String.valueOf(this.info.name), String.valueOf(Integer.toHexString(i3)), String.valueOf(Integer.toHexString(this.info.getRealConfigChanged())), String.valueOf(this.mLastReportedConfiguration)});
        }
        Slog.d(TAG, "Checking to restart " + this + ": changed=0x = " + Integer.toHexString(i3) + "; handles=0x" + Integer.toHexString(this.info.getRealConfigChanged()) + "; \n mLastReportedConfiguration = " + this.mLastReportedConfiguration + "; \n mFullConfig = " + getConfiguration());
        boolean z5 = !ActivityTaskManagerService.LTW_DISABLE ? !this.mAtmService.getWrapper().getExtImpl().getRemoteTaskManager().shouldIgnoreRelaunch(z4, i2, displayId, i3) : true;
        if ((shouldRelaunchLocked(i3, this.mTmpConfig) && z5) || this.forceNewConfig) {
            this.configChangeFlags |= i3;
            startFreezingScreenLocked(i);
            this.forceNewConfig = false;
            boolean adjustPreserveWindowWhenRelaunch = this.mActivityRecordExt.adjustPreserveWindowWhenRelaunch(z & (isResizeOnlyChange(i3) && !this.mFreezingScreen), i3, mergedOverrideConfiguration);
            if (hasResizeChange((~this.info.getRealConfigChanged()) & i3)) {
                this.mRelaunchReason = this.task.isDragResizing() ? 2 : 1;
            } else {
                this.mRelaunchReason = 0;
            }
            if (z3) {
                this.mLetterboxUiController.setRelaunchingAfterRequestedOrientationChanged(true);
            }
            if (this.mState == State.PAUSING) {
                if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, -90559682, 0, (String) null, new Object[]{String.valueOf(this)});
                }
                this.deferRelaunchUntilPaused = true;
                this.preserveWindowOnDeferredRelaunch = adjustPreserveWindowWhenRelaunch;
                return true;
            }
            if (ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, 736692676, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            if (!this.mVisibleRequested && ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STATES, -1558137010, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(Debug.getCallers(4))});
            }
            relaunchActivityLocked(adjustPreserveWindowWhenRelaunch);
            return false;
        }
        if (z4) {
            scheduleActivityMovedToDisplay(displayId, mergedOverrideConfiguration);
        } else {
            scheduleConfigurationChanged(mergedOverrideConfiguration);
        }
        notifyDisplayCompatPolicyAboutConfigurationChange(this.mLastReportedConfiguration.getMergedConfiguration(), this.mTmpConfig);
        stopFreezingScreenLocked(false);
        return true;
    }

    private void notifyDisplayCompatPolicyAboutConfigurationChange(Configuration configuration, Configuration configuration2) {
        if (this.mDisplayContent.mDisplayRotationCompatPolicy == null || !shouldBeResumed(null)) {
            return;
        }
        this.mDisplayContent.mDisplayRotationCompatPolicy.onActivityConfigurationChanging(this, configuration, configuration2);
    }

    private Configuration getProcessGlobalConfiguration() {
        WindowProcessController windowProcessController = this.app;
        return windowProcessController != null ? windowProcessController.getConfiguration() : this.mAtmService.getGlobalConfiguration();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldRelaunchLocked(int i, Configuration configuration) {
        int realConfigChanged = this.info.getRealConfigChanged();
        boolean onlyVrUiModeChanged = onlyVrUiModeChanged(i, configuration);
        if (this.info.applicationInfo.targetSdkVersion < 26 && this.requestedVrComponent != null && onlyVrUiModeChanged) {
            realConfigChanged |= 512;
        }
        if (this.mWmService.mSkipActivityRelaunchWhenDocking && onlyDeskInUiModeChanged(configuration) && !hasDeskResources()) {
            realConfigChanged |= 512;
        }
        return this.mActivityRecordExt.hookShouldRelaunchLocked(i, realConfigChanged, configuration);
    }

    private boolean onlyVrUiModeChanged(int i, Configuration configuration) {
        return i == 512 && isInVrUiMode(getConfiguration()) != isInVrUiMode(configuration);
    }

    private boolean onlyDeskInUiModeChanged(Configuration configuration) {
        Configuration configuration2 = getConfiguration();
        return (isInDeskUiMode(configuration2) != isInDeskUiMode(configuration)) && !((configuration2.uiMode & (-16)) != (configuration.uiMode & (-16)));
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0031, code lost:
    
        r4.mHasDeskResources = java.lang.Boolean.TRUE;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    boolean hasDeskResources() {
        Boolean bool = this.mHasDeskResources;
        if (bool != null) {
            return bool.booleanValue();
        }
        this.mHasDeskResources = Boolean.FALSE;
        try {
            int i = 0;
            Configuration[] sizeAndUiModeConfigurations = this.mAtmService.mContext.createPackageContextAsUser(this.packageName, 0, UserHandle.of(this.mUserId)).getResources().getSizeAndUiModeConfigurations();
            int length = sizeAndUiModeConfigurations.length;
            while (true) {
                if (i >= length) {
                    break;
                }
                if (isInDeskUiMode(sizeAndUiModeConfigurations[i])) {
                    break;
                }
                i++;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Slog.w(TAG, "Exception thrown during checking for desk resources " + this, e);
        }
        return this.mHasDeskResources.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getConfigurationChanges(Configuration configuration) {
        int filterDiff = SizeConfigurationBuckets.filterDiff(configuration.diff(getConfiguration()), configuration, getConfiguration(), this.mSizeConfigurations);
        if ((536870912 & filterDiff) != 0) {
            filterDiff &= -536870913;
        }
        return this.mActivityRecordExt.calculateNewChanges(filterDiff, configuration, this.mSizeConfigurations);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void relaunchActivityLocked(boolean z) {
        ArrayList<ResultInfo> arrayList;
        ArrayList<ReferrerIntent> arrayList2;
        ResumeActivityItem obtain;
        WindowState windowState;
        if (this.mAtmService.mSuppressResizeConfigChanges && z) {
            this.configChangeFlags = 0;
            return;
        }
        if (!z) {
            InputTarget imeInputTarget = this.mDisplayContent.getImeInputTarget();
            this.mLastImeShown = (imeInputTarget == null || imeInputTarget.getWindowState() == null || imeInputTarget.getWindowState().mActivityRecord != this || (windowState = this.mDisplayContent.mInputMethodWindow) == null || !windowState.isVisible()) ? false : true;
        }
        Task rootTask = getRootTask();
        if (rootTask != null && rootTask.mTranslucentActivityWaiting == this) {
            rootTask.checkTranslucentActivityWaiting(null);
        }
        boolean shouldBeResumed = shouldBeResumed(null);
        if (shouldBeResumed) {
            arrayList = this.results;
            arrayList2 = this.newIntents;
        } else {
            arrayList = null;
            arrayList2 = null;
        }
        if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            Slog.v(TAG_SWITCH, "Relaunching: " + this + " with results=" + arrayList + " newIntents=" + arrayList2 + " andResume=" + shouldBeResumed + " preserveWindow=" + z);
        }
        if (shouldBeResumed) {
            EventLogTags.writeWmRelaunchResumeActivity(this.mUserId, System.identityHashCode(this), this.task.mTaskId, this.shortComponentName, Integer.toHexString(this.configChangeFlags));
        } else {
            EventLogTags.writeWmRelaunchActivity(this.mUserId, System.identityHashCode(this), this.task.mTaskId, this.shortComponentName, Integer.toHexString(this.configChangeFlags));
        }
        startFreezingScreenLocked(0);
        try {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_STATES, -1016578046, 0, (String) null, new Object[]{shouldBeResumed ? "RESUMED" : "PAUSED", String.valueOf(this), String.valueOf(Debug.getCallers(6))});
            }
            this.forceNewConfig = false;
            startRelaunching();
            ActivityRelaunchItem obtain2 = ActivityRelaunchItem.obtain(arrayList, arrayList2, this.configChangeFlags, new MergedConfiguration(getProcessGlobalConfiguration(), getMergedOverrideConfiguration()), z);
            if (shouldBeResumed) {
                obtain = ResumeActivityItem.obtain(isTransitionForward(), shouldSendCompatFakeFocus());
            } else {
                obtain = PauseActivityItem.obtain();
            }
            ClientTransaction obtain3 = ClientTransaction.obtain(this.app.getThread(), this.token);
            obtain3.addCallback(obtain2);
            obtain3.setLifecycleStateRequest(obtain);
            this.mActivityRecordExt.addColorModeOnResume(obtain3, shouldBeResumed, this.packageName);
            this.mAtmService.getLifecycleManager().scheduleTransaction(obtain3);
        } catch (RemoteException e) {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_STATES, -262984451, 0, (String) null, new Object[]{String.valueOf(e)});
            }
        }
        if (shouldBeResumed) {
            if (ProtoLogCache.WM_DEBUG_STATES_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_STATES, 1270792394, 0, (String) null, new Object[]{String.valueOf(this)});
            }
            this.results = null;
            this.newIntents = null;
            this.mAtmService.getAppWarningsLocked().onResumeActivity(this);
            this.mAtmService.mSocExt.onAfterActivityResumed(this);
        } else {
            removePauseTimeout();
            State state = State.PAUSED;
            callServiceTrackeronActivityStatechange(state, true);
            setState(state, "relaunchActivityLocked");
        }
        this.mTaskSupervisor.mStoppingActivities.remove(this);
        this.configChangeFlags = 0;
        this.deferRelaunchUntilPaused = false;
        this.preserveWindowOnDeferredRelaunch = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restartProcessIfVisible() {
        if (this.finishing) {
            return;
        }
        Slog.i(TAG, "Request to restart process of " + this);
        clearSizeCompatMode();
        if (attachedToProcess()) {
            State state = State.RESTARTING_PROCESS;
            callServiceTrackeronActivityStatechange(state, true);
            setState(state, "restartActivityProcess");
            if (!this.mVisibleRequested || this.mHaveState) {
                this.mAtmService.mH.post(new Runnable() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda21
                    @Override // java.lang.Runnable
                    public final void run() {
                        ActivityRecord.this.lambda$restartProcessIfVisible$25();
                    }
                });
            } else if (this.mTransitionController.isShellTransitionsEnabled()) {
                final Transition transition = new Transition(5, 0, this.mTransitionController, this.mWmService.mSyncEngine);
                this.mTransitionController.startCollectOrQueue(transition, new TransitionController.OnStartCollect() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda22
                    @Override // com.android.server.wm.TransitionController.OnStartCollect
                    public final void onCollectStarted(boolean z) {
                        ActivityRecord.this.lambda$restartProcessIfVisible$26(transition, z);
                    }
                });
            } else {
                startFreezingScreen();
                scheduleStopForRestartProcess();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restartProcessIfVisible$25() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mAtmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (hasProcess() && this.app.getReportedProcState() > 6) {
                    WindowProcessController windowProcessController = this.app;
                    WindowManagerService.resetPriorityAfterLockedSection();
                    this.mAtmService.mAmInternal.killProcess(windowProcessController.mName, windowProcessController.mUid, "resetConfig");
                    return;
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restartProcessIfVisible$26(Transition transition, boolean z) {
        if (this.mState != State.RESTARTING_PROCESS || !attachedToProcess()) {
            transition.abort();
            return;
        }
        setVisibleRequested(false);
        transition.collect(this);
        this.mTransitionController.requestStartTransition(transition, this.task, null, null);
        scheduleStopForRestartProcess();
    }

    private void scheduleStopForRestartProcess() {
        try {
            this.mAtmService.getLifecycleManager().scheduleTransaction(this.app.getThread(), this.token, (ActivityLifecycleItem) StopActivityItem.obtain(0));
        } catch (RemoteException e) {
            Slog.w(TAG, "Exception thrown during restart " + this, e);
        }
        this.mTaskSupervisor.scheduleRestartTimeout(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isProcessRunning() {
        WindowProcessController windowProcessController = this.app;
        if (windowProcessController == null) {
            windowProcessController = (WindowProcessController) this.mAtmService.mProcessNames.get(this.processName, this.info.applicationInfo.uid);
        }
        return windowProcessController != null && windowProcessController.hasThread();
    }

    private boolean allowTaskSnapshot() {
        if (this.mActivityRecordExt.isZoomMode(getWindowingMode())) {
            if (ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
                Slog.v(TAG_SWITCH, "Don't allow Snapshot for starting window in zoom mode, this=" + this);
            }
            return false;
        }
        ArrayList<ReferrerIntent> arrayList = this.newIntents;
        if (arrayList == null) {
            return true;
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            Intent intent = this.newIntents.get(size);
            if (intent != null && !isMainIntent(intent)) {
                Intent intent2 = this.mLastNewIntent;
                if (!(intent2 != null ? intent2.filterEquals(intent) : this.intent.filterEquals(intent)) || intent.getExtras() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNoHistory() {
        return ((this.intent.getFlags() & 1073741824) == 0 && (this.info.flags & 128) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveToXml(TypedXmlSerializer typedXmlSerializer) throws IOException, XmlPullParserException {
        typedXmlSerializer.attributeLong((String) null, ATTR_ID, this.createTime);
        typedXmlSerializer.attributeInt((String) null, ATTR_LAUNCHEDFROMUID, this.launchedFromUid);
        String str = this.launchedFromPackage;
        if (str != null) {
            typedXmlSerializer.attribute((String) null, ATTR_LAUNCHEDFROMPACKAGE, str);
        }
        String str2 = this.launchedFromFeatureId;
        if (str2 != null) {
            typedXmlSerializer.attribute((String) null, ATTR_LAUNCHEDFROMFEATURE, str2);
        }
        String str3 = this.resolvedType;
        if (str3 != null) {
            typedXmlSerializer.attribute((String) null, ATTR_RESOLVEDTYPE, str3);
        }
        typedXmlSerializer.attributeBoolean((String) null, ATTR_COMPONENTSPECIFIED, this.componentSpecified);
        typedXmlSerializer.attributeInt((String) null, ATTR_USERID, this.mUserId);
        ActivityManager.TaskDescription taskDescription = this.taskDescription;
        if (taskDescription != null) {
            taskDescription.saveToXml(typedXmlSerializer);
        }
        typedXmlSerializer.startTag((String) null, TAG_INTENT);
        this.intent.saveToXml(typedXmlSerializer);
        typedXmlSerializer.endTag((String) null, TAG_INTENT);
        if (!isPersistable() || this.mPersistentState == null) {
            return;
        }
        typedXmlSerializer.startTag((String) null, TAG_PERSISTABLEBUNDLE);
        this.mPersistentState.saveToXml(typedXmlSerializer);
        typedXmlSerializer.endTag((String) null, TAG_PERSISTABLEBUNDLE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ActivityRecord restoreFromXml(TypedXmlPullParser typedXmlPullParser, ActivityTaskSupervisor activityTaskSupervisor) throws IOException, XmlPullParserException {
        Intent intent = null;
        int attributeInt = typedXmlPullParser.getAttributeInt((String) null, ATTR_LAUNCHEDFROMUID, 0);
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_LAUNCHEDFROMPACKAGE);
        String attributeValue2 = typedXmlPullParser.getAttributeValue((String) null, ATTR_LAUNCHEDFROMFEATURE);
        String attributeValue3 = typedXmlPullParser.getAttributeValue((String) null, ATTR_RESOLVEDTYPE);
        boolean attributeBoolean = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_COMPONENTSPECIFIED, false);
        int attributeInt2 = typedXmlPullParser.getAttributeInt((String) null, ATTR_USERID, 0);
        long attributeLong = typedXmlPullParser.getAttributeLong((String) null, ATTR_ID, -1L);
        int depth = typedXmlPullParser.getDepth();
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription();
        taskDescription.restoreFromXml(typedXmlPullParser);
        PersistableBundle persistableBundle = null;
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1 || (next == 3 && typedXmlPullParser.getDepth() < depth)) {
                break;
            }
            if (next == 2) {
                String name = typedXmlPullParser.getName();
                if (TAG_INTENT.equals(name)) {
                    intent = Intent.restoreFromXml(typedXmlPullParser);
                } else if (TAG_PERSISTABLEBUNDLE.equals(name)) {
                    persistableBundle = PersistableBundle.restoreFromXml(typedXmlPullParser);
                } else {
                    Slog.w(TAG, "restoreActivity: unexpected name=" + name);
                    XmlUtils.skipCurrentTag(typedXmlPullParser);
                }
            }
        }
        if (intent == null) {
            throw new XmlPullParserException("restoreActivity error intent=" + intent);
        }
        ActivityTaskManagerService activityTaskManagerService = activityTaskSupervisor.mService;
        PersistableBundle persistableBundle2 = persistableBundle;
        ActivityInfo resolveActivity = activityTaskSupervisor.resolveActivity(intent, attributeValue3, 0, null, attributeInt2, Binder.getCallingUid(), 0);
        if (resolveActivity == null) {
            throw new XmlPullParserException("restoreActivity resolver error. Intent=" + intent + " resolvedType=" + attributeValue3);
        }
        return new Builder(activityTaskManagerService).setLaunchedFromUid(attributeInt).setLaunchedFromPackage(attributeValue).setLaunchedFromFeature(attributeValue2).setIntent(intent).setResolvedType(attributeValue3).setActivityInfo(resolveActivity).setComponentSpecified(attributeBoolean).setPersistentState(persistableBundle2).setTaskDescription(taskDescription).setCreateTime(attributeLong).build();
    }

    private static boolean isInVrUiMode(Configuration configuration) {
        return (configuration.uiMode & 15) == 7;
    }

    private static boolean isInDeskUiMode(Configuration configuration) {
        return (configuration.uiMode & 15) == 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getProcessName() {
        return this.info.applicationInfo.processName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getUid() {
        return this.info.applicationInfo.uid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUid(int i) {
        return this.info.applicationInfo.uid == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPid() {
        WindowProcessController windowProcessController = this.app;
        if (windowProcessController != null) {
            return windowProcessController.getPid();
        }
        return 0;
    }

    int getLaunchedFromPid() {
        return this.launchedFromPid;
    }

    int getLaunchedFromUid() {
        return this.launchedFromUid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getFilteredReferrer(String str) {
        if (str == null) {
            return null;
        }
        if (str.equals(this.packageName) || !this.mWmService.mPmInternal.filterAppAccess(str, this.info.applicationInfo.uid, this.mUserId)) {
            return str;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canTurnScreenOn() {
        if (getTurnScreenOnFlag()) {
            return this.mCurrentLaunchCanTurnScreenOn && getRootTask() != null && this.mTaskSupervisor.getKeyguardController().checkKeyguardVisibility(this);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTurnScreenOn(boolean z) {
        this.mActivityRecordExt.resolveScreenOnFlag(this, z);
        this.mTurnScreenOn = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAllowCrossUidActivitySwitchFromBelow(boolean z) {
        this.mAllowCrossUidActivitySwitchFromBelow = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Pair<Boolean, Boolean> allowCrossUidActivitySwitchFromBelow(int i) {
        int i2 = this.info.applicationInfo.uid;
        if (i == i2) {
            Boolean bool = Boolean.TRUE;
            return new Pair<>(bool, bool);
        }
        if (this.mAllowCrossUidActivitySwitchFromBelow) {
            Boolean bool2 = Boolean.TRUE;
            return new Pair<>(bool2, bool2);
        }
        return new Pair<>(Boolean.valueOf(!(ActivitySecurityModelFeatureFlags.shouldRestrictActivitySwitch(i2) && ActivitySecurityModelFeatureFlags.shouldRestrictActivitySwitch(i))), Boolean.FALSE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getTurnScreenOnFlag() {
        return this.mTurnScreenOn || containsTurnScreenOnWindow();
    }

    private boolean containsTurnScreenOnWindow() {
        if (isRelaunching()) {
            return this.mLastContainsTurnScreenOnWindow;
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            if ((((WindowState) this.mChildren.get(size)).mAttrs.flags & 2097152) != 0) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canResumeByCompat() {
        WindowProcessController windowProcessController = this.app;
        return windowProcessController == null || windowProcessController.updateTopResumingActivityInProcessIfNeeded(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTopRunningActivity() {
        return this.mRootWindowContainer.topRunningActivity() == this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFocusedActivityOnDisplay() {
        return this.mDisplayContent.forAllTaskDisplayAreas(new Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$isFocusedActivityOnDisplay$27;
                lambda$isFocusedActivityOnDisplay$27 = ActivityRecord.this.lambda$isFocusedActivityOnDisplay$27((TaskDisplayArea) obj);
                return lambda$isFocusedActivityOnDisplay$27;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$isFocusedActivityOnDisplay$27(TaskDisplayArea taskDisplayArea) {
        return taskDisplayArea.getFocusedActivity() == this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRootOfTask() {
        Task task = this.task;
        return task != null && this == task.getRootActivity(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTaskOverlay(boolean z) {
        this.mTaskOverlay = z;
        setAlwaysOnTop(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTaskOverlay() {
        return this.mTaskOverlay;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean isAlwaysOnTop() {
        return this.mTaskOverlay || super.isAlwaysOnTop();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean showToCurrentUser() {
        return this.mShowForAllUsers || this.mWmService.isUserVisible(this.mUserId);
    }

    @Override // com.android.server.wm.WindowToken
    public String toString() {
        if (this.stringName != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.stringName);
            sb.append(" t");
            Task task = this.task;
            sb.append(task == null ? -1 : task.mTaskId);
            sb.append(this.finishing ? " f}" : "");
            sb.append(this.mIsExiting ? " isExiting" : "");
            sb.append("}");
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder(128);
        sb2.append("ActivityRecord{");
        sb2.append(Integer.toHexString(System.identityHashCode(this)));
        sb2.append(" u");
        sb2.append(this.mUserId);
        sb2.append(' ');
        sb2.append(this.intent.getComponent().flattenToShortString());
        String sb3 = sb2.toString();
        this.stringName = sb3;
        return sb3;
    }

    void dumpDebug(ProtoOutputStream protoOutputStream, int i) {
        writeNameToProto(protoOutputStream, 1138166333441L);
        super.dumpDebug(protoOutputStream, 1146756268034L, i);
        protoOutputStream.write(1133871366147L, this.mLastSurfaceShowing);
        protoOutputStream.write(1133871366148L, isWaitingForTransitionStart());
        protoOutputStream.write(1133871366149L, isAnimating(7, 17));
        WindowContainerThumbnail windowContainerThumbnail = this.mThumbnail;
        if (windowContainerThumbnail != null) {
            windowContainerThumbnail.dumpDebug(protoOutputStream, 1146756268038L);
        }
        protoOutputStream.write(1133871366151L, fillsParent());
        protoOutputStream.write(1133871366152L, this.mAppStopped);
        protoOutputStream.write(1133871366174L, !occludesParent());
        protoOutputStream.write(1133871366168L, this.mVisible);
        protoOutputStream.write(1133871366153L, this.mVisibleRequested);
        protoOutputStream.write(1133871366154L, isClientVisible());
        protoOutputStream.write(1133871366155L, this.mDeferHidingClient);
        protoOutputStream.write(1133871366156L, this.mReportedDrawn);
        protoOutputStream.write(1133871366157L, this.reportedVisible);
        protoOutputStream.write(1120986464270L, this.mNumInterestingWindows);
        protoOutputStream.write(1120986464271L, this.mNumDrawnWindows);
        protoOutputStream.write(1133871366160L, this.allDrawn);
        protoOutputStream.write(1133871366161L, this.mLastAllDrawn);
        WindowState windowState = this.mStartingWindow;
        if (windowState != null) {
            windowState.writeIdentifierToProto(protoOutputStream, 1146756268051L);
        }
        protoOutputStream.write(1133871366164L, isStartingWindowDisplayed());
        protoOutputStream.write(1133871366345L, this.startingMoved);
        protoOutputStream.write(1133871366166L, this.mVisibleSetFromTransferredStartingWindow);
        protoOutputStream.write(1138166333467L, this.mState.toString());
        protoOutputStream.write(1133871366172L, isRootOfTask());
        if (hasProcess()) {
            protoOutputStream.write(1120986464285L, this.app.getPid());
        }
        protoOutputStream.write(1133871366175L, this.pictureInPictureArgs.isAutoEnterEnabled());
        protoOutputStream.write(1133871366176L, inSizeCompatMode());
        protoOutputStream.write(1108101562401L, getMinAspectRatio());
        protoOutputStream.write(1133871366178L, providesMaxBounds());
        protoOutputStream.write(1133871366179L, this.mEnableRecentsScreenshot);
        protoOutputStream.write(1120986464292L, this.mLastDropInputMode);
        protoOutputStream.write(1120986464293L, getOverrideOrientation());
        protoOutputStream.write(1133871366182L, shouldSendCompatFakeFocus());
        protoOutputStream.write(1133871366183L, this.mLetterboxUiController.shouldForceRotateForCameraCompat());
        protoOutputStream.write(1133871366184L, this.mLetterboxUiController.shouldRefreshActivityForCameraCompat());
        protoOutputStream.write(1133871366185L, this.mLetterboxUiController.shouldRefreshActivityViaPauseForCameraCompat());
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j, int i) {
        if (i != 2 || isVisible()) {
            long start = protoOutputStream.start(j);
            dumpDebug(protoOutputStream, i);
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeNameToProto(ProtoOutputStream protoOutputStream, long j) {
        protoOutputStream.write(j, this.shortComponentName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void writeIdentifierToProto(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1120986464257L, System.identityHashCode(this));
        protoOutputStream.write(1120986464258L, this.mUserId);
        protoOutputStream.write(1138166333443L, this.intent.getComponent().flattenToShortString());
        protoOutputStream.end(start);
    }

    void logLaunchTime() {
        ApplicationInfo applicationInfo;
        if (this.launchTimeStartOppo != 0) {
            long uptimeMillis = SystemClock.uptimeMillis() - this.launchTimeStartOppo;
            ActivityInfo activityInfo = this.info;
            if (activityInfo != null && (applicationInfo = activityInfo.applicationInfo) != null) {
                this.mActivityRecordExt.notifyLaunchTime(applicationInfo, this.mActivityComponent.getClassName(), uptimeMillis);
            }
            this.launchTimeStartOppo = 0L;
        }
    }

    void setLaunchTimeStart() {
        if (this.launchTimeStartOppo == 0) {
            this.launchTimeStartOppo = SystemClock.uptimeMillis();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class CompatDisplayInsets {
        private final int mHeight;
        final boolean mIsFloating;
        final boolean mIsInFixedOrientationLetterbox;

        @Configuration.Orientation
        final int mOriginalRequestedOrientation;
        final int mOriginalRotation;
        private final int mWidth;
        final Rect[] mNonDecorInsets = new Rect[4];
        final Rect[] mStableInsets = new Rect[4];

        CompatDisplayInsets(DisplayContent displayContent, ActivityRecord activityRecord, Rect rect) {
            int rotation;
            this.mOriginalRotation = displayContent.getRotation();
            boolean tasksAreFloating = activityRecord.getWindowConfiguration().tasksAreFloating();
            this.mIsFloating = tasksAreFloating;
            this.mOriginalRequestedOrientation = activityRecord.getRequestedConfigurationOrientation();
            if (tasksAreFloating) {
                Rect bounds = activityRecord.getWindowConfiguration().getBounds();
                this.mWidth = bounds.width();
                this.mHeight = bounds.height();
                Rect rect2 = new Rect();
                for (int i = 0; i < 4; i++) {
                    this.mNonDecorInsets[i] = rect2;
                    this.mStableInsets[i] = rect2;
                }
                this.mIsInFixedOrientationLetterbox = false;
                return;
            }
            Task task = activityRecord.getTask();
            boolean z = rect != null;
            this.mIsInFixedOrientationLetterbox = z;
            rect = z ? rect : task != null ? task.getBounds() : displayContent.getBounds();
            if (task != null) {
                rotation = task.getConfiguration().windowConfiguration.getRotation();
            } else {
                rotation = displayContent.getConfiguration().windowConfiguration.getRotation();
            }
            Point rotationZeroDimensions = getRotationZeroDimensions(rect, rotation);
            this.mWidth = rotationZeroDimensions.x;
            this.mHeight = rotationZeroDimensions.y;
            Rect rect3 = rect.equals(displayContent.getBounds()) ? null : new Rect();
            DisplayPolicy displayPolicy = displayContent.getDisplayPolicy();
            int i2 = 0;
            while (i2 < 4) {
                this.mNonDecorInsets[i2] = new Rect();
                this.mStableInsets[i2] = new Rect();
                boolean z2 = i2 == 1 || i2 == 3;
                int i3 = z2 ? displayContent.mBaseDisplayHeight : displayContent.mBaseDisplayWidth;
                int i4 = z2 ? displayContent.mBaseDisplayWidth : displayContent.mBaseDisplayHeight;
                DisplayPolicy.DecorInsets.Info decorInsetsInfo = displayPolicy.getDecorInsetsInfo(i2, i3, i4);
                this.mNonDecorInsets[i2].set(decorInsetsInfo.mNonDecorInsets);
                this.mStableInsets[i2].set(decorInsetsInfo.mConfigInsets);
                if (rect3 != null) {
                    rect3.set(rect);
                    displayContent.rotateBounds(rotation, i2, rect3);
                    updateInsetsForBounds(rect3, i3, i4, this.mNonDecorInsets[i2]);
                    updateInsetsForBounds(rect3, i3, i4, this.mStableInsets[i2]);
                }
                i2++;
            }
        }

        private static Point getRotationZeroDimensions(Rect rect, int i) {
            boolean z = true;
            if (i != 1 && i != 3) {
                z = false;
            }
            int width = rect.width();
            int height = rect.height();
            return z ? new Point(height, width) : new Point(width, height);
        }

        private static void updateInsetsForBounds(Rect rect, int i, int i2, Rect rect2) {
            rect2.left = Math.max(0, rect2.left - rect.left);
            rect2.top = Math.max(0, rect2.top - rect.top);
            rect2.right = Math.max(0, (rect.right - i) + rect2.right);
            rect2.bottom = Math.max(0, (rect.bottom - i2) + rect2.bottom);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void getBoundsByRotation(Rect rect, int i) {
            boolean z = true;
            if (i != 1 && i != 3) {
                z = false;
            }
            rect.set(0, 0, z ? this.mHeight : this.mWidth, z ? this.mWidth : this.mHeight);
        }

        void getFrameByOrientation(Rect rect, int i) {
            int max = Math.max(this.mWidth, this.mHeight);
            int min = Math.min(this.mWidth, this.mHeight);
            boolean z = i == 2;
            int i2 = z ? max : min;
            if (z) {
                max = min;
            }
            rect.set(0, 0, i2, max);
        }

        void getContainerBounds(Rect rect, Rect rect2, int i, int i2, boolean z, boolean z2) {
            getFrameByOrientation(rect2, i2);
            if (this.mIsFloating) {
                rect.set(rect2);
                return;
            }
            getBoundsByRotation(rect, i);
            int width = rect.width();
            int height = rect.height();
            boolean z3 = (rect2.width() > rect2.height()) != (width > height);
            if (z3 && z2 && z) {
                if (i2 == 2) {
                    float f = width;
                    rect2.bottom = (int) ((f * f) / height);
                    rect2.right = width;
                } else {
                    rect2.bottom = height;
                    float f2 = height;
                    rect2.right = (int) ((f2 * f2) / width);
                }
                rect2.offset(ActivityRecord.getCenterOffset(this.mWidth, rect2.width()), 0);
            }
            rect.set(rect2);
            if (z3) {
                Rect rect3 = this.mNonDecorInsets[i];
                rect2.offset(rect3.left, rect3.top);
                rect.offset(rect3.left, rect3.top);
            } else if (i != -1) {
                TaskFragment.intersectWithInsetsIfFits(rect, rect2, this.mNonDecorInsets[i]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class AppSaturationInfo {
        float[] mMatrix;
        float[] mTranslation;

        private AppSaturationInfo() {
            this.mMatrix = new float[9];
            this.mTranslation = new float[3];
        }

        void setSaturation(float[] fArr, float[] fArr2) {
            float[] fArr3 = this.mMatrix;
            System.arraycopy(fArr, 0, fArr3, 0, fArr3.length);
            float[] fArr4 = this.mTranslation;
            System.arraycopy(fArr2, 0, fArr4, 0, fArr4.length);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public RemoteAnimationTarget createRemoteAnimationTarget(RemoteAnimationController.RemoteAnimationRecord remoteAnimationRecord) {
        WindowState findMainWindow = findMainWindow();
        if ((this.task == null || findMainWindow == null) && !this.mActivityRecordExt.forceCreateRemoteAnimationTarget(this)) {
            return null;
        }
        boolean z = false;
        Rect calculateInsetsForAnimationTarget = this.mActivityRecordExt.calculateInsetsForAnimationTarget(this, findMainWindow != null ? findMainWindow.getInsetsStateWithVisibilityOverride().calculateInsets(this.task.getBounds(), WindowInsets.Type.systemBars(), false).toRect() : new Rect());
        InsetUtils.addInsets(calculateInsetsForAnimationTarget, getLetterboxInsets());
        int i = this.task.mTaskId;
        int mode = remoteAnimationRecord.getMode();
        SurfaceControl surfaceControl = remoteAnimationRecord.mAdapter.mCapturedLeash;
        boolean z2 = !fillsParent();
        Rect rect = new Rect();
        int prefixOrderIndex = getPrefixOrderIndex();
        RemoteAnimationController.RemoteAnimationAdapterWrapper remoteAnimationAdapterWrapper = remoteAnimationRecord.mAdapter;
        Point point = remoteAnimationAdapterWrapper.mPosition;
        Rect rect2 = remoteAnimationAdapterWrapper.mLocalBounds;
        Rect rect3 = remoteAnimationAdapterWrapper.mEndBounds;
        WindowConfiguration windowConfiguration = this.task.getWindowConfiguration();
        RemoteAnimationController.RemoteAnimationAdapterWrapper remoteAnimationAdapterWrapper2 = remoteAnimationRecord.mThumbnailAdapter;
        RemoteAnimationTarget remoteAnimationTarget = new RemoteAnimationTarget(i, mode, surfaceControl, z2, rect, calculateInsetsForAnimationTarget, prefixOrderIndex, point, rect2, rect3, windowConfiguration, false, remoteAnimationAdapterWrapper2 != null ? remoteAnimationAdapterWrapper2.mCapturedLeash : null, remoteAnimationRecord.mStartBounds, this.task.getTaskInfo(), checkEnterPictureInPictureAppOpsState());
        remoteAnimationTarget.setShowBackdrop(remoteAnimationRecord.mShowBackdrop);
        StartingData startingData = this.mStartingData;
        if (startingData != null && startingData.hasImeSurface()) {
            z = true;
        }
        remoteAnimationTarget.setWillShowImeOnTarget(z);
        remoteAnimationTarget.hasAnimatingParent = remoteAnimationRecord.hasAnimatingParent();
        return remoteAnimationTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void getAnimationFrames(Rect rect, Rect rect2, Rect rect3, Rect rect4) {
        WindowState findMainWindow = findMainWindow();
        if (findMainWindow == null) {
            this.mActivityRecordExt.setupAppFrameForCompatMode(rect, getBounds(), this);
        } else {
            findMainWindow.getAnimationFrames(rect, rect2, rect3, rect4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPictureInPictureParams(PictureInPictureParams pictureInPictureParams) {
        this.pictureInPictureArgs.copyOnlySet(pictureInPictureParams);
        adjustPictureInPictureParamsIfNeeded(getBounds());
        getTask().getRootTask().onPictureInPictureParamsChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShouldDockBigOverlays(boolean z) {
        this.shouldDockBigOverlays = z;
        getTask().getRootTask().onShouldDockBigOverlaysChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean isSyncFinished(BLASTSyncEngine.SyncGroup syncGroup) {
        if (this.mActivityRecordExt.syncFinishedForOptimizeStartup(this)) {
            return true;
        }
        if (!super.isSyncFinished(syncGroup)) {
            return false;
        }
        DisplayContent displayContent = this.mDisplayContent;
        if (displayContent != null && displayContent.mUnknownAppVisibilityController.isVisibilityUnknown(this)) {
            return false;
        }
        if (!isVisibleRequested()) {
            return true;
        }
        if (this.mPendingRelaunchCount > 0 || !isAttached()) {
            return false;
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            if (((WindowState) this.mChildren.get(size)).isVisibleRequested()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void finishSync(SurfaceControl.Transaction transaction, BLASTSyncEngine.SyncGroup syncGroup, boolean z) {
        if (getSyncGroup() == null || syncGroup == getSyncGroup()) {
            this.mLastAllReadyAtSync = allSyncFinished();
            super.finishSync(transaction, syncGroup, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearSizeCompatModeIfNeeded() {
        if (this.mActivityRecordExt.inOplusCompatEnabled()) {
            getResolvedOverrideConfiguration().unset();
            float f = this.mSizeCompatScale;
            this.mInSizeCompatModeForBounds = false;
            this.mSizeCompatScale = 1.0f;
            this.mSizeCompatBounds = null;
            this.mCompatDisplayInsets = null;
            if (1.0f != f) {
                forAllWindows((Consumer<WindowState>) new ActivityRecord$$ExternalSyntheticLambda12(), false);
            }
            if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                Slog.d(TAG, "clearSizeCompatModeIfNeeded " + this + " " + Debug.getCallers(5));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Point getMinDimensions() {
        ActivityInfo.WindowLayout windowLayout = this.info.windowLayout;
        if (windowLayout == null) {
            return null;
        }
        return new Point(windowLayout.minWidth, windowLayout.minHeight);
    }

    private void adjustPictureInPictureParamsIfNeeded(Rect rect) {
        PictureInPictureParams pictureInPictureParams = this.pictureInPictureArgs;
        if (pictureInPictureParams == null || !pictureInPictureParams.hasSourceBoundsHint()) {
            return;
        }
        this.pictureInPictureArgs.getSourceRectHint().offset(rect.left, rect.top);
    }

    private void applyLocaleOverrideIfNeeded(Configuration configuration) {
        ActivityTaskManagerInternal.PackageConfig findPackageConfiguration;
        LocaleList localeList;
        ComponentName componentName;
        Task task;
        boolean z = false;
        if (isEmbedded() || ((task = this.task) != null && task.mAlignActivityLocaleWithTask)) {
            Task task2 = this.task;
            if (task2 != null && (componentName = task2.realActivity) != null && !componentName.getPackageName().equals(this.packageName)) {
                z = true;
            }
            if (!z || (findPackageConfiguration = this.mAtmService.mPackageConfigPersister.findPackageConfiguration(this.task.realActivity.getPackageName(), this.mUserId)) == null || (localeList = findPackageConfiguration.mLocales) == null || localeList.isEmpty()) {
                return;
            }
            configuration.setLocales(findPackageConfiguration.mLocales);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldSendCompatFakeFocus() {
        return this.mLetterboxUiController.shouldSendFakeFocus() && inMultiWindowMode() && !inPinnedWindowingMode() && !inFreeformWindowingMode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canCaptureSnapshot() {
        if (!isSurfaceShowing() || findMainWindow() == null) {
            return false;
        }
        return forAllWindows(new ToBooleanFunction() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda3
            public final boolean apply(Object obj) {
                boolean lambda$canCaptureSnapshot$28;
                lambda$canCaptureSnapshot$28 = ActivityRecord.lambda$canCaptureSnapshot$28((WindowState) obj);
                return lambda$canCaptureSnapshot$28;
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$canCaptureSnapshot$28(WindowState windowState) {
        WindowStateAnimator windowStateAnimator = windowState.mWinAnimator;
        return windowStateAnimator != null && windowStateAnimator.getShown() && windowState.mWinAnimator.mLastAlpha > 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overrideCustomTransition(boolean z, int i, int i2, int i3) {
        CustomAppTransition customAnimation = getCustomAnimation(z);
        if (customAnimation == null) {
            customAnimation = new CustomAppTransition();
            if (z) {
                this.mCustomOpenTransition = customAnimation;
            } else {
                this.mCustomCloseTransition = customAnimation;
            }
        }
        customAnimation.mEnterAnim = i;
        customAnimation.mExitAnim = i2;
        customAnimation.mBackgroundColor = i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearCustomTransition(boolean z) {
        if (z) {
            this.mCustomOpenTransition = null;
        } else {
            this.mCustomCloseTransition = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CustomAppTransition getCustomAnimation(boolean z) {
        return z ? this.mCustomOpenTransition : this.mCustomCloseTransition;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class CustomAppTransition {
        int mBackgroundColor;
        int mEnterAnim;
        int mExitAnim;

        CustomAppTransition() {
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    static class Builder {
        private ActivityInfo mActivityInfo;
        private final ActivityTaskManagerService mAtmService;
        private WindowProcessController mCallerApp;
        private boolean mComponentSpecified;
        private Configuration mConfiguration;
        private long mCreateTime;
        private Intent mIntent;
        private String mLaunchedFromFeature;
        private String mLaunchedFromPackage;
        private int mLaunchedFromPid;
        private int mLaunchedFromUid;
        private ActivityOptions mOptions;
        private PersistableBundle mPersistentState;
        private int mRequestCode;
        private String mResolvedType;
        private ActivityRecord mResultTo;
        private String mResultWho;
        private boolean mRootVoiceInteraction;
        private ActivityRecord mSourceRecord;
        private ActivityManager.TaskDescription mTaskDescription;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(ActivityTaskManagerService activityTaskManagerService) {
            this.mAtmService = activityTaskManagerService;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setCaller(WindowProcessController windowProcessController) {
            this.mCallerApp = windowProcessController;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setLaunchedFromPid(int i) {
            this.mLaunchedFromPid = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setLaunchedFromUid(int i) {
            this.mLaunchedFromUid = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setLaunchedFromPackage(String str) {
            this.mLaunchedFromPackage = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setLaunchedFromFeature(String str) {
            this.mLaunchedFromFeature = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setIntent(Intent intent) {
            this.mIntent = intent;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setResolvedType(String str) {
            this.mResolvedType = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setActivityInfo(ActivityInfo activityInfo) {
            this.mActivityInfo = activityInfo;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setResultTo(ActivityRecord activityRecord) {
            this.mResultTo = activityRecord;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setResultWho(String str) {
            this.mResultWho = str;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setRequestCode(int i) {
            this.mRequestCode = i;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setComponentSpecified(boolean z) {
            this.mComponentSpecified = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setRootVoiceInteraction(boolean z) {
            this.mRootVoiceInteraction = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setActivityOptions(ActivityOptions activityOptions) {
            this.mOptions = activityOptions;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setConfiguration(Configuration configuration) {
            this.mConfiguration = configuration;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setSourceRecord(ActivityRecord activityRecord) {
            this.mSourceRecord = activityRecord;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setPersistentState(PersistableBundle persistableBundle) {
            this.mPersistentState = persistableBundle;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setTaskDescription(ActivityManager.TaskDescription taskDescription) {
            this.mTaskDescription = taskDescription;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder setCreateTime(long j) {
            this.mCreateTime = j;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ActivityRecord build() {
            if (this.mConfiguration == null) {
                this.mConfiguration = this.mAtmService.getConfiguration();
            }
            ActivityTaskManagerService activityTaskManagerService = this.mAtmService;
            return new ActivityRecord(activityTaskManagerService, this.mCallerApp, this.mLaunchedFromPid, this.mLaunchedFromUid, this.mLaunchedFromPackage, this.mLaunchedFromFeature, this.mIntent, this.mResolvedType, this.mActivityInfo, this.mConfiguration, this.mResultTo, this.mResultWho, this.mRequestCode, this.mComponentSpecified, this.mRootVoiceInteraction, activityTaskManagerService.mTaskSupervisor, this.mOptions, this.mSourceRecord, this.mPersistentState, this.mTaskDescription, this.mCreateTime);
        }
    }

    public IActivityRecordWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class ActivityRecordWrapper implements IActivityRecordWrapper {
        private ActivityRecordWrapper() {
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public String getLaunchedFromPackage() {
            return ActivityRecord.this.launchedFromPackage;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public String getPackageName() {
            return ActivityRecord.this.packageName;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getLaunchedFromPid() {
            return ActivityRecord.this.launchedFromPid;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getLaunchedFromUid() {
            return ActivityRecord.this.launchedFromUid;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public ApplicationInfo getAppliationInfo() {
            return ActivityRecord.this.info.applicationInfo;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public Intent getIntent() {
            return ActivityRecord.this.intent;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public ActivityRecord getAppToken() {
            return ActivityRecord.this;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public String getshortComponentName() {
            return ActivityRecord.this.shortComponentName;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public boolean isActivityTypeHome() {
            return ActivityRecord.this.isActivityTypeHome();
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getResultToUserId() {
            return ActivityRecord.this.resultTo.mUserId;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public String getResultToPackageName() {
            return ActivityRecord.this.resultTo.packageName;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public String getProcessName() {
            return ActivityRecord.this.processName;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public IActivityRecordExt getExtImpl() {
            return ActivityRecord.this.mActivityRecordExt;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public MergedConfiguration getLastReportedConfiguration() {
            return ActivityRecord.this.mLastReportedConfiguration;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getConfigurationChanges(Configuration configuration) {
            return ActivityRecord.this.getConfigurationChanges(configuration);
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public boolean shouldRelaunchLocked(int i, Configuration configuration) {
            return ActivityRecord.this.shouldRelaunchLocked(i, configuration);
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public RemoteTransition getPendingRemoteTransition() {
            return ActivityRecord.this.mPendingRemoteTransition;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getPid() {
            return ActivityRecord.this.getPid();
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getUid() {
            return ActivityRecord.this.getUid();
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public long getLaunchTickTime() {
            return ActivityRecord.this.launchTickTime;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public boolean isNowVisible() {
            return ActivityRecord.this.nowVisible;
        }
    }
}
