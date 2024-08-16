package com.android.server.wm;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.window.DisplayAreaInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.wm.DeviceStateController;
import com.android.server.wm.RemoteDisplayChangeController;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class PhysicalDisplaySwitchTransitionLauncher {
    private final ActivityTaskManagerService mAtmService;
    private final Context mContext;
    private DeviceStateController.DeviceState mDeviceState;
    private final DisplayContent mDisplayContent;
    private boolean mShouldRequestTransitionOnDisplaySwitch;
    private Transition mTransition;
    private final TransitionController mTransitionController;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public PhysicalDisplaySwitchTransitionLauncher(DisplayContent displayContent, TransitionController transitionController) {
        this(displayContent, r0.mAtmService, r0.mContext, transitionController);
        WindowManagerService windowManagerService = displayContent.mWmService;
    }

    @VisibleForTesting
    public PhysicalDisplaySwitchTransitionLauncher(DisplayContent displayContent, ActivityTaskManagerService activityTaskManagerService, Context context, TransitionController transitionController) {
        this.mShouldRequestTransitionOnDisplaySwitch = false;
        this.mDeviceState = DeviceStateController.DeviceState.UNKNOWN;
        this.mDisplayContent = displayContent;
        this.mAtmService = activityTaskManagerService;
        this.mContext = context;
        this.mTransitionController = transitionController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void foldStateChanged(DeviceStateController.DeviceState deviceState) {
        if (this.mDeviceState == DeviceStateController.DeviceState.FOLDED && (deviceState == DeviceStateController.DeviceState.HALF_FOLDED || deviceState == DeviceStateController.DeviceState.OPEN)) {
            this.mShouldRequestTransitionOnDisplaySwitch = true;
        } else if (deviceState != DeviceStateController.DeviceState.HALF_FOLDED && deviceState != DeviceStateController.DeviceState.OPEN) {
            this.mShouldRequestTransitionOnDisplaySwitch = false;
        }
        this.mDeviceState = deviceState;
    }

    public void requestDisplaySwitchTransitionIfNeeded(int i, int i2, int i3, int i4, int i5) {
        if (this.mShouldRequestTransitionOnDisplaySwitch && this.mTransitionController.isShellTransitionsEnabled() && this.mDisplayContent.getLastHasContent()) {
            if (this.mContext.getResources().getBoolean(17891875) && ValueAnimator.areAnimatorsEnabled()) {
                TransitionRequestInfo.DisplayChange displayChange = new TransitionRequestInfo.DisplayChange(i);
                displayChange.setStartAbsBounds(new Rect(0, 0, i2, i3));
                displayChange.setEndAbsBounds(new Rect(0, 0, i4, i5));
                displayChange.setPhysicalDisplayChanged(true);
                TransitionController transitionController = this.mTransitionController;
                DisplayContent displayContent = this.mDisplayContent;
                Transition requestTransitionIfNeeded = transitionController.requestTransitionIfNeeded(6, 0, displayContent, displayContent, null, displayChange);
                if (requestTransitionIfNeeded != null) {
                    this.mDisplayContent.mAtmService.startLaunchPowerMode(2);
                    this.mTransition = requestTransitionIfNeeded;
                }
                this.mShouldRequestTransitionOnDisplaySwitch = false;
            }
        }
    }

    public void onDisplayUpdated(int i, int i2, DisplayAreaInfo displayAreaInfo) {
        if (this.mTransition == null || this.mDisplayContent.mRemoteDisplayChangeController.performRemoteDisplayChange(i, i2, displayAreaInfo, new RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback() { // from class: com.android.server.wm.PhysicalDisplaySwitchTransitionLauncher$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback
            public final void onContinueRemoteDisplayChange(WindowContainerTransaction windowContainerTransaction) {
                PhysicalDisplaySwitchTransitionLauncher.this.continueDisplayUpdate(windowContainerTransaction);
            }
        })) {
            return;
        }
        markTransitionAsReady();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void continueDisplayUpdate(WindowContainerTransaction windowContainerTransaction) {
        if (this.mTransition == null) {
            return;
        }
        if (windowContainerTransaction != null) {
            this.mAtmService.mWindowOrganizerController.applyTransaction(windowContainerTransaction);
        }
        markTransitionAsReady();
    }

    private void markTransitionAsReady() {
        Transition transition = this.mTransition;
        if (transition == null) {
            return;
        }
        transition.setAllReady();
        this.mTransition = null;
    }
}
