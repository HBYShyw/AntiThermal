package com.android.server.wm;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Slog;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import android.view.Display;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.ActivityTaskManagerService;
import com.android.server.wm.IKeyguardControllerExt;
import com.android.server.wm.KeyguardController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class KeyguardController {
    private static final int DEFER_WAKE_TRANSITION_TIMEOUT_MS = 5000;
    static final String KEYGUARD_SLEEP_TOKEN_TAG = "keyguard";
    private static final String TAG = "ActivityTaskManager";
    private static IKeyguardControllerExt.IStaticExt mKeyguardControllerStaticExt = (IKeyguardControllerExt.IStaticExt) ExtLoader.type(IKeyguardControllerExt.IStaticExt.class).create();
    private RootWindowContainer mRootWindowContainer;
    private final ActivityTaskManagerService mService;
    private final ActivityTaskManagerInternal.SleepTokenAcquirer mSleepTokenAcquirer;
    private final ActivityTaskSupervisor mTaskSupervisor;
    private boolean mWaitingForWakeTransition;
    private WindowManagerService mWindowManager;
    private final SparseArray<KeyguardDisplayState> mDisplayStates = new SparseArray<>();
    private final Runnable mResetWaitTransition = new Runnable() { // from class: com.android.server.wm.KeyguardController$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardController.this.lambda$new$0();
        }
    };
    private KeyguardControllerWrapper mKeyguardControllerWrapper = new KeyguardControllerWrapper();
    private IKeyguardControllerExt mKeyguardControllerExt = (IKeyguardControllerExt) ExtLoader.type(IKeyguardControllerExt.class).base(this).create();

    private int convertTransitFlags(int i) {
        int i2 = (i & 1) != 0 ? 257 : 256;
        if ((i & 2) != 0) {
            i2 |= 2;
        }
        if ((i & 4) != 0) {
            i2 |= 4;
        }
        if ((i & 8) != 0) {
            i2 |= 8;
        }
        return (i & 16) != 0 ? i2 | 512 : i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KeyguardController(ActivityTaskManagerService activityTaskManagerService, ActivityTaskSupervisor activityTaskSupervisor) {
        this.mService = activityTaskManagerService;
        this.mTaskSupervisor = activityTaskSupervisor;
        Objects.requireNonNull(activityTaskManagerService);
        this.mSleepTokenAcquirer = new ActivityTaskManagerService.SleepTokenAcquirerImpl(KEYGUARD_SLEEP_TOKEN_TAG);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowManager(WindowManagerService windowManagerService) {
        this.mWindowManager = windowManagerService;
        this.mRootWindowContainer = this.mService.mRootWindowContainer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAodShowing(int i) {
        return getDisplayState(i).mAodShowing;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyguardOrAodShowing(int i) {
        KeyguardDisplayState displayState = getDisplayState(i);
        return ((!displayState.mKeyguardShowing && !displayState.mAodShowing) || displayState.mKeyguardGoingAway || isDisplayOccluded(i)) ? false : true;
    }

    boolean isKeyguardUnoccludedOrAodShowing(int i) {
        KeyguardDisplayState displayState = getDisplayState(i);
        if (i == 0 && displayState.mAodShowing) {
            return !displayState.mKeyguardGoingAway;
        }
        return isKeyguardOrAodShowing(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyguardShowing(int i) {
        KeyguardDisplayState displayState = getDisplayState(i);
        return (!displayState.mKeyguardShowing || displayState.mKeyguardGoingAway || isDisplayOccluded(i)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyguardLocked(int i) {
        KeyguardDisplayState displayState = getDisplayState(i);
        return displayState.mKeyguardShowing && !displayState.mKeyguardGoingAway;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean topActivityOccludesKeyguard(ActivityRecord activityRecord) {
        return getDisplayState(activityRecord.getDisplayId()).mTopOccludesActivity == activityRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyguardGoingAway(int i) {
        KeyguardDisplayState displayState = getDisplayState(i);
        return displayState.mKeyguardGoingAway && displayState.mKeyguardShowing;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public void setKeyguardShown(int i, boolean z, boolean z2) {
        if (this.mRootWindowContainer.getDisplayContent(i).isKeyguardAlwaysUnlocked()) {
            Slog.i(TAG, "setKeyguardShown ignoring always unlocked display " + i);
            return;
        }
        KeyguardDisplayState displayState = getDisplayState(i);
        boolean z3 = true;
        byte b = z2 != displayState.mAodShowing;
        byte b2 = displayState.mAodShowing && !z2;
        byte b3 = displayState.mKeyguardGoingAway && z;
        if (z == displayState.mKeyguardShowing && (b3 == false || b2 != false)) {
            z3 = false;
        }
        if (b2 != false) {
            updateDeferTransitionForAod(false);
        }
        if (!z3 && b == false) {
            setWakeTransitionReady();
            return;
        }
        EventLogTags.writeWmSetKeyguardShown(i, z ? 1 : 0, z2 ? 1 : 0, displayState.mKeyguardGoingAway ? 1 : 0, displayState.mOccluded ? 1 : 0, "setKeyguardShown");
        if ((((z2 ? 1 : 0) ^ (z ? 1 : 0)) != 0 || (z2 && b != false && z3)) && !displayState.mKeyguardGoingAway && Display.isOnState(this.mRootWindowContainer.getDefaultDisplay().getDisplayInfo().state)) {
            this.mWindowManager.mTaskSnapshotController.snapshotForSleeping(0);
        }
        displayState.mKeyguardShowing = z;
        displayState.mAodShowing = z2;
        if (z3) {
            displayState.mKeyguardGoingAway = false;
            if (z) {
                displayState.mDismissalRequested = false;
            }
            if (b3 != false) {
                DisplayContent defaultDisplay = this.mRootWindowContainer.getDefaultDisplay();
                defaultDisplay.requestTransitionAndLegacyPrepare(3, 2048);
                defaultDisplay.mWallpaperController.showWallpaperInTransition(false);
                this.mWindowManager.executeAppTransition();
            }
        }
        updateKeyguardSleepToken();
        this.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
        InputMethodManagerInternal.get().updateImeWindowStatus(false);
        setWakeTransitionReady();
        if (b != false) {
            this.mWindowManager.mWindowPlacerLocked.performSurfacePlacement();
        }
        this.mKeyguardControllerExt.updateKeyguardExitAnimStateIfNeeded(z, i);
    }

    private void setWakeTransitionReady() {
        if (this.mWindowManager.mAtmService.getTransitionController().getCollectingTransitionType() == 11) {
            this.mWindowManager.mAtmService.getTransitionController().setReady(this.mRootWindowContainer.getDefaultDisplay());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void keyguardGoingAway(int i, int i2) {
        KeyguardDisplayState displayState = getDisplayState(i);
        if (!displayState.mKeyguardShowing || displayState.mKeyguardGoingAway) {
            return;
        }
        Trace.traceBegin(32L, "keyguardGoingAway");
        this.mService.deferWindowLayout();
        displayState.mKeyguardGoingAway = true;
        try {
            this.mKeyguardControllerExt.keyguardGoingAway(i2);
            this.mKeyguardControllerExt.enableOrientationListenerWhenKeyguradGoingAway(this.mRootWindowContainer.getDefaultDisplay().mDisplayContent, i2);
            EventLogTags.writeWmSetKeyguardShown(i, displayState.mKeyguardShowing ? 1 : 0, displayState.mAodShowing ? 1 : 0, 1, displayState.mOccluded ? 1 : 0, "keyguardGoingAway");
            if (this.mKeyguardControllerExt.ifSkipTransition(i)) {
                updateKeyguardSleepToken();
                return;
            }
            int convertTransitFlags = convertTransitFlags(i2);
            DisplayContent defaultDisplay = this.mRootWindowContainer.getDefaultDisplay();
            defaultDisplay.prepareAppTransition(7, convertTransitFlags);
            defaultDisplay.mAtmService.getTransitionController().requestTransitionIfNeeded(4, convertTransitFlags, null, defaultDisplay);
            updateKeyguardSleepToken();
            if (!this.mKeyguardControllerExt.skipShowWallpaper(i2, this.mRootWindowContainer)) {
                defaultDisplay.mWallpaperController.showWallpaperInTransition(true);
            }
            this.mRootWindowContainer.resumeFocusedTasksTopActivities();
            this.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
            this.mRootWindowContainer.addStartingWindowsForVisibleActivities();
            this.mWindowManager.executeAppTransition();
        } finally {
            this.mService.continueWindowLayout();
            Trace.traceEnd(32L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dismissKeyguard(IBinder iBinder, IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
        if (forTokenLocked == null || !forTokenLocked.visibleIgnoringKeyguard) {
            failCallback(iKeyguardDismissCallback);
            return;
        }
        Slog.i(TAG, "Activity requesting to dismiss Keyguard: " + forTokenLocked);
        if (forTokenLocked.getTurnScreenOnFlag() && forTokenLocked.isTopRunningActivity()) {
            this.mTaskSupervisor.wakeUp("dismissKeyguard");
        }
        if (this.mKeyguardControllerExt.dismissKeyguard(this.mService.mContext, forTokenLocked, false)) {
            return;
        }
        this.mWindowManager.dismissKeyguard(iKeyguardDismissCallback, charSequence);
    }

    private void failCallback(IKeyguardDismissCallback iKeyguardDismissCallback) {
        try {
            iKeyguardDismissCallback.onDismissError();
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to call callback", e);
        }
    }

    boolean canShowActivityWhileKeyguardShowing(ActivityRecord activityRecord) {
        KeyguardDisplayState displayState = getDisplayState(activityRecord.getDisplayId());
        return activityRecord.containsDismissKeyguardWindow() && canDismissKeyguard() && !displayState.mAodShowing && (displayState.mDismissalRequested || (activityRecord.canShowWhenLocked() && displayState.mDismissingKeyguardActivity != activityRecord));
    }

    boolean canShowWhileOccluded(boolean z, boolean z2) {
        return z2 || (z && !this.mWindowManager.isKeyguardSecure(this.mService.getCurrentUserId()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkKeyguardVisibility(ActivityRecord activityRecord) {
        if (activityRecord.mDisplayContent.canShowWithInsecureKeyguard() && canDismissKeyguard()) {
            return true;
        }
        if (this.mKeyguardControllerExt.checkKeyguardVisibility(activityRecord, this)) {
            Slog.d(TAG, "checkKeyguardVisibility: intercept ActivityRecord:" + activityRecord);
            return false;
        }
        if (isKeyguardOrAodShowing(activityRecord.mDisplayContent.getDisplayId())) {
            return canShowActivityWhileKeyguardShowing(activityRecord);
        }
        if (isKeyguardLocked(activityRecord.getDisplayId())) {
            return canShowWhileOccluded(activityRecord.containsDismissKeyguardWindow(), activityRecord.canShowWhenLocked());
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateVisibility() {
        for (int childCount = this.mRootWindowContainer.getChildCount() - 1; childCount >= 0; childCount--) {
            DisplayContent childAt = this.mRootWindowContainer.getChildAt(childCount);
            if (!childAt.isRemoving() && !childAt.isRemoved()) {
                KeyguardDisplayState displayState = getDisplayState(childAt.mDisplayId);
                displayState.updateVisibility(this, childAt);
                if (displayState.mRequestDismissKeyguard) {
                    handleDismissKeyguard(childAt.getDisplayId());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOccludedChanged(int i, ActivityRecord activityRecord) {
        if (i != 0) {
            updateKeyguardSleepToken(i);
            return;
        }
        TransitionController transitionController = this.mRootWindowContainer.mTransitionController;
        boolean isDisplayOccluded = isDisplayOccluded(i);
        boolean isKeyguardLocked = isKeyguardLocked(i);
        boolean z = isKeyguardLocked && !transitionController.isCollecting();
        this.mWindowManager.mPolicy.onKeyguardOccludedChangedLw(isDisplayOccluded);
        Slog.d(TAG, "handleOccludedChanged occlude = " + isDisplayOccluded + " performTransition = " + isKeyguardLocked + " executeTransition = " + z);
        this.mService.deferWindowLayout();
        try {
            if (isKeyguardLocked(i)) {
                if (isDisplayOccluded) {
                    this.mRootWindowContainer.getDefaultDisplay().requestTransitionAndLegacyPrepare(8, 4096, activityRecord != null ? activityRecord.getRootTask() : null);
                } else {
                    this.mRootWindowContainer.getDefaultDisplay().requestTransitionAndLegacyPrepare(9, 8192);
                }
            } else if (transitionController.inTransition()) {
                ArrayList<Runnable> arrayList = transitionController.mStateValidators;
                WindowManagerPolicy windowManagerPolicy = this.mWindowManager.mPolicy;
                Objects.requireNonNull(windowManagerPolicy);
                arrayList.add(new KeyguardController$$ExternalSyntheticLambda0(windowManagerPolicy));
            } else {
                this.mWindowManager.mPolicy.applyKeyguardOcclusionChange();
            }
            updateKeyguardSleepToken(i);
            if (isKeyguardLocked && z) {
                this.mWindowManager.executeAppTransition();
            }
        } finally {
            this.mService.continueWindowLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKeyguardGoingAwayChanged(DisplayContent displayContent) {
        this.mService.deferWindowLayout();
        try {
            displayContent.prepareAppTransition(7, 0);
            displayContent.mAtmService.getTransitionController().requestTransitionIfNeeded(1, 256, null, displayContent);
            updateKeyguardSleepToken();
            this.mWindowManager.executeAppTransition();
        } finally {
            this.mService.continueWindowLayout();
        }
    }

    private void handleDismissKeyguard(int i) {
        if (this.mWindowManager.isKeyguardSecure(this.mService.getCurrentUserId())) {
            this.mWindowManager.dismissKeyguard(null, null);
            KeyguardDisplayState displayState = getDisplayState(i);
            displayState.mDismissalRequested = true;
            DisplayContent defaultDisplay = this.mRootWindowContainer.getDefaultDisplay();
            if (displayState.mKeyguardShowing && canDismissKeyguard() && defaultDisplay.mAppTransition.containsTransitRequest(9)) {
                this.mWindowManager.executeAppTransition();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDisplayOccluded(int i) {
        return getDisplayState(i).mOccluded;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getTopOccludingActivity(int i) {
        return getDisplayState(i).mTopOccludesActivity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getDismissKeyguardActivity(int i) {
        return getDisplayState(i).mDismissingKeyguardActivity;
    }

    boolean canDismissKeyguard() {
        return this.mWindowManager.mPolicy.isKeyguardTrustedLw() || !this.mWindowManager.isKeyguardSecure(this.mService.getCurrentUserId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isShowingDream() {
        return getDisplayState(0).mShowingDream;
    }

    private void updateKeyguardSleepToken() {
        for (int childCount = this.mRootWindowContainer.getChildCount() - 1; childCount >= 0; childCount--) {
            updateKeyguardSleepToken(this.mRootWindowContainer.getChildAt(childCount).mDisplayId);
        }
    }

    private void updateKeyguardSleepToken(int i) {
        KeyguardDisplayState displayState = getDisplayState(i);
        if (isKeyguardUnoccludedOrAodShowing(i) && !this.mKeyguardControllerExt.skipAcquireSleepToken(i)) {
            displayState.mSleepTokenAcquirer.acquire(i);
        } else {
            displayState.mSleepTokenAcquirer.release(i);
        }
    }

    private KeyguardDisplayState getDisplayState(int i) {
        KeyguardDisplayState keyguardDisplayState = this.mDisplayStates.get(i);
        if (keyguardDisplayState != null) {
            return keyguardDisplayState;
        }
        KeyguardDisplayState keyguardDisplayState2 = new KeyguardDisplayState(this.mService, i, this.mSleepTokenAcquirer);
        this.mDisplayStates.append(i, keyguardDisplayState2);
        return keyguardDisplayState2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDisplayRemoved(int i) {
        KeyguardDisplayState keyguardDisplayState = this.mDisplayStates.get(i);
        if (keyguardDisplayState != null) {
            keyguardDisplayState.onRemoved();
            this.mDisplayStates.remove(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWindowManager.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                updateDeferTransitionForAod(false);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateDeferTransitionForAod(boolean z) {
        if (z != this.mWaitingForWakeTransition && this.mService.getTransitionController().isCollecting()) {
            if (z && isAodShowing(0)) {
                this.mWaitingForWakeTransition = true;
                this.mWindowManager.mAtmService.getTransitionController().deferTransitionReady();
                this.mWindowManager.mH.postDelayed(this.mResetWaitTransition, 5000L);
            } else {
                if (z) {
                    return;
                }
                this.mWaitingForWakeTransition = false;
                this.mWindowManager.mAtmService.getTransitionController().continueTransitionReady();
                this.mWindowManager.mH.removeCallbacks(this.mResetWaitTransition);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class KeyguardDisplayState {
        private boolean mAodShowing;
        private boolean mDismissalRequested;
        private ActivityRecord mDismissingKeyguardActivity;
        private final int mDisplayId;
        private boolean mKeyguardGoingAway;
        private boolean mKeyguardShowing;
        private boolean mOccluded;
        private boolean mRequestDismissKeyguard;
        private final ActivityTaskManagerService mService;
        private boolean mShowingDream;
        private final ActivityTaskManagerInternal.SleepTokenAcquirer mSleepTokenAcquirer;
        private ActivityRecord mTopOccludesActivity;
        private ActivityRecord mTopTurnScreenOnActivity;

        KeyguardDisplayState(ActivityTaskManagerService activityTaskManagerService, int i, ActivityTaskManagerInternal.SleepTokenAcquirer sleepTokenAcquirer) {
            this.mService = activityTaskManagerService;
            this.mDisplayId = i;
            this.mSleepTokenAcquirer = sleepTokenAcquirer;
        }

        void onRemoved() {
            this.mTopOccludesActivity = null;
            this.mDismissingKeyguardActivity = null;
            this.mTopTurnScreenOnActivity = null;
            this.mSleepTokenAcquirer.release(this.mDisplayId);
        }

        void updateVisibility(KeyguardController keyguardController, DisplayContent displayContent) {
            boolean z;
            boolean z2 = this.mOccluded;
            boolean z3 = this.mKeyguardGoingAway;
            ActivityRecord activityRecord = this.mDismissingKeyguardActivity;
            boolean z4 = false;
            this.mRequestDismissKeyguard = false;
            this.mOccluded = false;
            this.mShowingDream = false;
            this.mTopOccludesActivity = null;
            this.mDismissingKeyguardActivity = null;
            this.mTopTurnScreenOnActivity = null;
            Task rootTaskForControllingOccluding = getRootTaskForControllingOccluding(displayContent);
            ActivityRecord topNonFinishingActivity = rootTaskForControllingOccluding != null ? rootTaskForControllingOccluding.getTopNonFinishingActivity() : null;
            if (topNonFinishingActivity != null) {
                boolean containsDismissKeyguardWindow = topNonFinishingActivity.containsDismissKeyguardWindow();
                boolean canShowWhenLocked = topNonFinishingActivity.canShowWhenLocked();
                if (this.mService.mTaskSupervisor.getKeyguardController().getWrapper().getExtImpl().checkKeyguardVisibility(topNonFinishingActivity, keyguardController)) {
                    Slog.d(KeyguardController.TAG, "updateVisibility check showWhenLocked state:  checkKeyguardVisibility: intercept ActivityRecord:" + topNonFinishingActivity);
                    containsDismissKeyguardWindow = false;
                    canShowWhenLocked = false;
                }
                if (topNonFinishingActivity.getWrapper().getExtImpl().isDisableshowWhenLockByRecents()) {
                    Slog.d(KeyguardController.TAG, "updateVisibility check showWhenLocked state:  checkKeyguardVisibility: intercept ActivityRecord:" + topNonFinishingActivity + ", isDisableshowWhenLockByRecents = true");
                    topNonFinishingActivity.getWrapper().getExtImpl().setDisableshowWhenLockByRecents(false);
                    containsDismissKeyguardWindow = false;
                    canShowWhenLocked = false;
                }
                if (containsDismissKeyguardWindow) {
                    this.mDismissingKeyguardActivity = topNonFinishingActivity;
                }
                if (topNonFinishingActivity.getTurnScreenOnFlag() && topNonFinishingActivity.currentLaunchCanTurnScreenOn()) {
                    this.mTopTurnScreenOnActivity = topNonFinishingActivity;
                }
                if (topNonFinishingActivity.mDismissKeyguard && this.mKeyguardShowing) {
                    this.mKeyguardGoingAway = true;
                } else if (topNonFinishingActivity.canShowWhenLocked() && canShowWhenLocked) {
                    this.mTopOccludesActivity = topNonFinishingActivity;
                }
                topNonFinishingActivity.mDismissKeyguard = false;
                boolean z5 = this.mTopOccludesActivity != null || (this.mDismissingKeyguardActivity != null && rootTaskForControllingOccluding.topRunningActivity() == this.mDismissingKeyguardActivity && keyguardController.canShowWhileOccluded(true, false));
                boolean z6 = z5;
                if (this.mDisplayId != 0) {
                    z6 = z5 | (displayContent.canShowWithInsecureKeyguard() && keyguardController.canDismissKeyguard());
                }
                z = z6;
            } else {
                z = false;
            }
            boolean z7 = displayContent.getDisplayPolicy().isShowingDreamLw() && topNonFinishingActivity != null && topNonFinishingActivity.getActivityType() == 5;
            this.mShowingDream = z7;
            boolean z8 = z7 || z;
            this.mOccluded = z8;
            ActivityRecord activityRecord2 = this.mDismissingKeyguardActivity;
            this.mRequestDismissKeyguard = (activityRecord == activityRecord2 || z8 || this.mKeyguardGoingAway || activityRecord2 == null) ? false : true;
            if (z8 && this.mKeyguardShowing && !displayContent.isSleeping() && !topNonFinishingActivity.fillsParent() && displayContent.mWallpaperController.getWallpaperTarget() == null) {
                displayContent.pendingLayoutChanges |= 4;
            }
            KeyguardController.mKeyguardControllerStaticExt.setAppLayoutChanges(this.mOccluded, this.mKeyguardShowing, displayContent, topNonFinishingActivity, 4);
            if (this.mTopTurnScreenOnActivity != null && !this.mService.mWindowManager.mPowerManager.isInteractive() && (this.mRequestDismissKeyguard || z)) {
                keyguardController.mTaskSupervisor.wakeUp("handleTurnScreenOn");
                this.mTopTurnScreenOnActivity.setCurrentLaunchCanTurnScreenOn(false);
            }
            boolean z9 = this.mOccluded;
            if (z2 != z9) {
                int i = this.mDisplayId;
                if (i == 0) {
                    EventLogTags.writeWmSetKeyguardShown(i, this.mKeyguardShowing ? 1 : 0, this.mAodShowing ? 1 : 0, this.mKeyguardGoingAway ? 1 : 0, z9 ? 1 : 0, "updateVisibility");
                }
                keyguardController.handleOccludedChanged(this.mDisplayId, this.mTopOccludesActivity);
                Slog.d(KeyguardController.TAG, "updateVisibility occlude from:" + z2 + " to " + this.mOccluded + "," + this.mDisplayId + "," + this.mShowingDream + "," + this.mTopOccludesActivity);
            } else {
                if (!z3 && this.mKeyguardGoingAway) {
                    keyguardController.handleKeyguardGoingAwayChanged(displayContent);
                }
                if (z4 || topNonFinishingActivity == null) {
                }
                if (this.mOccluded || this.mKeyguardGoingAway) {
                    displayContent.mTransitionController.collect(topNonFinishingActivity);
                    return;
                }
                return;
            }
            z4 = true;
            if (z4) {
            }
        }

        private Task getRootTaskForControllingOccluding(DisplayContent displayContent) {
            return displayContent.getRootTask(new Predicate() { // from class: com.android.server.wm.KeyguardController$KeyguardDisplayState$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$getRootTaskForControllingOccluding$0;
                    lambda$getRootTaskForControllingOccluding$0 = KeyguardController.KeyguardDisplayState.lambda$getRootTaskForControllingOccluding$0((Task) obj);
                    return lambda$getRootTaskForControllingOccluding$0;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$getRootTaskForControllingOccluding$0(Task task) {
            return (task == null || !task.isFocusableAndVisible() || task.inPinnedWindowingMode() || task.getWrapper().getExtImpl().isZoomMode(task.getWindowingMode())) ? false : true;
        }

        void dumpStatus(PrintWriter printWriter, String str) {
            printWriter.println(str + " KeyguardShowing=" + this.mKeyguardShowing + " AodShowing=" + this.mAodShowing + " KeyguardGoingAway=" + this.mKeyguardGoingAway + " DismissalRequested=" + this.mDismissalRequested + "  Occluded=" + this.mOccluded + " DismissingKeyguardActivity=" + this.mDismissingKeyguardActivity + " TurnScreenOnActivity=" + this.mTopTurnScreenOnActivity + " at display=" + this.mDisplayId + " mTopOccludesActivity=" + this.mTopOccludesActivity);
        }

        void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
            long start = protoOutputStream.start(j);
            protoOutputStream.write(1120986464257L, this.mDisplayId);
            protoOutputStream.write(1133871366146L, this.mKeyguardShowing);
            protoOutputStream.write(1133871366147L, this.mAodShowing);
            protoOutputStream.write(1133871366148L, this.mOccluded);
            protoOutputStream.write(1133871366149L, this.mKeyguardGoingAway);
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        KeyguardDisplayState displayState = getDisplayState(0);
        printWriter.println(str + "KeyguardController:");
        printWriter.println(str + "  mKeyguardShowing=" + displayState.mKeyguardShowing);
        printWriter.println(str + "  mAodShowing=" + displayState.mAodShowing);
        printWriter.println(str + "  mKeyguardGoingAway=" + displayState.mKeyguardGoingAway);
        dumpDisplayStates(printWriter, str);
        printWriter.println(str + "  mDismissalRequested=" + displayState.mDismissalRequested);
        printWriter.println();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        KeyguardDisplayState displayState = getDisplayState(0);
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1133871366147L, displayState.mAodShowing);
        protoOutputStream.write(1133871366145L, displayState.mKeyguardShowing);
        protoOutputStream.write(1133871366149L, displayState.mKeyguardGoingAway);
        writeDisplayStatesToProto(protoOutputStream, 2246267895812L);
        protoOutputStream.end(start);
    }

    private void dumpDisplayStates(PrintWriter printWriter, String str) {
        for (int i = 0; i < this.mDisplayStates.size(); i++) {
            this.mDisplayStates.valueAt(i).dumpStatus(printWriter, str);
        }
    }

    private void writeDisplayStatesToProto(ProtoOutputStream protoOutputStream, long j) {
        for (int i = 0; i < this.mDisplayStates.size(); i++) {
            this.mDisplayStates.valueAt(i).dumpDebug(protoOutputStream, j);
        }
    }

    public IKeyguardControllerWrapper getWrapper() {
        return this.mKeyguardControllerWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class KeyguardControllerWrapper implements IKeyguardControllerWrapper {
        private KeyguardControllerWrapper() {
        }

        @Override // com.android.server.wm.IKeyguardControllerWrapper
        public IKeyguardControllerExt getExtImpl() {
            return KeyguardController.this.mKeyguardControllerExt;
        }
    }
}
