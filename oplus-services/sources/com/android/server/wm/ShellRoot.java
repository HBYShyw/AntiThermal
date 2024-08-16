package com.android.server.wm;

import android.graphics.Point;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import android.view.DisplayInfo;
import android.view.IWindow;
import android.view.SurfaceControl;
import android.view.animation.Animation;
import com.android.server.wm.WindowToken;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ShellRoot {
    private static final String TAG = "ShellRoot";
    private IWindow mAccessibilityWindow;
    private IBinder.DeathRecipient mAccessibilityWindowDeath;
    private IWindow mClient;
    private final IBinder.DeathRecipient mDeathRecipient;
    private final DisplayContent mDisplayContent;
    private final int mShellRootLayer;
    private SurfaceControl mSurfaceControl;
    private WindowToken mToken;
    private int mWindowType;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShellRoot(IWindow iWindow, DisplayContent displayContent, final int i) {
        this.mSurfaceControl = null;
        this.mDisplayContent = displayContent;
        this.mShellRootLayer = i;
        IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() { // from class: com.android.server.wm.ShellRoot$$ExternalSyntheticLambda0
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                ShellRoot.this.lambda$new$0(i);
            }
        };
        this.mDeathRecipient = deathRecipient;
        try {
            iWindow.asBinder().linkToDeath(deathRecipient, 0);
            this.mClient = iWindow;
            if (i == 0) {
                this.mWindowType = 2034;
            } else if (i == 1) {
                this.mWindowType = 2038;
            } else {
                throw new IllegalArgumentException(i + " is not an acceptable shell root layer.");
            }
            WindowToken build = new WindowToken.Builder(displayContent.mWmService, iWindow.asBinder(), this.mWindowType).setDisplayContent(displayContent).setPersistOnEmpty(true).setOwnerCanManageAppTokens(true).build();
            this.mToken = build;
            this.mSurfaceControl = build.makeChildSurface(null).setContainerLayer().setName("Shell Root Leash " + displayContent.getDisplayId()).setCallsite(TAG).build();
            this.mToken.getPendingTransaction().show(this.mSurfaceControl);
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to add shell root layer " + i + " on display " + displayContent.getDisplayId(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        this.mDisplayContent.removeShellRoot(i);
    }

    int getWindowType() {
        return this.mWindowType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        IWindow iWindow = this.mClient;
        if (iWindow != null) {
            iWindow.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            this.mClient = null;
        }
        WindowToken windowToken = this.mToken;
        if (windowToken != null) {
            windowToken.removeImmediately();
            this.mToken = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl getSurfaceControl() {
        return this.mSurfaceControl;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IWindow getClient() {
        return this.mClient;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startAnimation(Animation animation) {
        WindowToken windowToken = this.mToken;
        if (windowToken.windowType != 2034) {
            return;
        }
        DisplayInfo fixedRotationTransformDisplayInfo = windowToken.getFixedRotationTransformDisplayInfo();
        if (fixedRotationTransformDisplayInfo == null) {
            fixedRotationTransformDisplayInfo = this.mDisplayContent.getDisplayInfo();
        }
        animation.initialize(fixedRotationTransformDisplayInfo.logicalWidth, fixedRotationTransformDisplayInfo.logicalHeight, fixedRotationTransformDisplayInfo.appWidth, fixedRotationTransformDisplayInfo.appHeight);
        animation.restrictDuration(10000L);
        animation.scaleCurrentDuration(this.mDisplayContent.mWmService.getWindowAnimationScaleLocked());
        LocalAnimationAdapter localAnimationAdapter = new LocalAnimationAdapter(new WindowAnimationSpec(animation, new Point(0, 0), false, 0.0f), this.mDisplayContent.mWmService.mSurfaceAnimationRunner);
        WindowToken windowToken2 = this.mToken;
        windowToken2.startAnimation(windowToken2.getPendingTransaction(), localAnimationAdapter, false, 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder getAccessibilityWindowToken() {
        IWindow iWindow = this.mAccessibilityWindow;
        if (iWindow != null) {
            return iWindow.asBinder();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAccessibilityWindow(IWindow iWindow) {
        IWindow iWindow2 = this.mAccessibilityWindow;
        if (iWindow2 != null) {
            iWindow2.asBinder().unlinkToDeath(this.mAccessibilityWindowDeath, 0);
        }
        this.mAccessibilityWindow = iWindow;
        if (iWindow != null) {
            try {
                this.mAccessibilityWindowDeath = new IBinder.DeathRecipient() { // from class: com.android.server.wm.ShellRoot$$ExternalSyntheticLambda1
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        ShellRoot.this.lambda$setAccessibilityWindow$1();
                    }
                };
                this.mAccessibilityWindow.asBinder().linkToDeath(this.mAccessibilityWindowDeath, 0);
            } catch (RemoteException unused) {
                this.mAccessibilityWindow = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAccessibilityWindow$1() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mDisplayContent.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAccessibilityWindow = null;
                setAccessibilityWindow(null);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }
}
