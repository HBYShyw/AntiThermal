package com.android.server.policy;

import android.R;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.DisplayManagerInternal;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.view.DisplayInfo;
import android.view.IDisplayFoldListener;
import com.android.server.DisplayThread;
import com.android.server.LocalServices;
import com.android.server.wm.WindowManagerInternal;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DisplayFoldController {
    private final int mDisplayId;
    private final DisplayManagerInternal mDisplayManagerInternal;
    private String mFocusedApp;
    private Boolean mFolded;
    private final Rect mFoldedArea;
    private final Handler mHandler;
    private final WindowManagerInternal mWindowManagerInternal;
    private Rect mOverrideFoldedArea = new Rect();
    private final DisplayInfo mNonOverrideDisplayInfo = new DisplayInfo();
    private final RemoteCallbackList<IDisplayFoldListener> mListeners = new RemoteCallbackList<>();
    private final DisplayFoldDurationLogger mDurationLogger = new DisplayFoldDurationLogger();

    DisplayFoldController(Context context, WindowManagerInternal windowManagerInternal, DisplayManagerInternal displayManagerInternal, int i, Rect rect, Handler handler) {
        this.mWindowManagerInternal = windowManagerInternal;
        this.mDisplayManagerInternal = displayManagerInternal;
        this.mDisplayId = i;
        this.mFoldedArea = new Rect(rect);
        this.mHandler = handler;
        ((DeviceStateManager) context.getSystemService(DeviceStateManager.class)).registerCallback(new HandlerExecutor(handler), new DeviceStateManager.FoldStateListener(context, new Consumer() { // from class: com.android.server.policy.DisplayFoldController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayFoldController.this.lambda$new$0((Boolean) obj);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Boolean bool) {
        setDeviceFolded(bool.booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishedGoingToSleep() {
        this.mDurationLogger.onFinishedGoingToSleep();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishedWakingUp() {
        this.mDurationLogger.onFinishedWakingUp(this.mFolded);
    }

    private void setDeviceFolded(boolean z) {
        Rect rect;
        Boolean bool = this.mFolded;
        if (bool == null || bool.booleanValue() != z) {
            if (!this.mOverrideFoldedArea.isEmpty()) {
                rect = this.mOverrideFoldedArea;
            } else {
                rect = !this.mFoldedArea.isEmpty() ? this.mFoldedArea : null;
            }
            if (rect != null) {
                if (z) {
                    this.mDisplayManagerInternal.getNonOverrideDisplayInfo(this.mDisplayId, this.mNonOverrideDisplayInfo);
                    int width = ((this.mNonOverrideDisplayInfo.logicalWidth - rect.width()) / 2) - rect.left;
                    int height = ((this.mNonOverrideDisplayInfo.logicalHeight - rect.height()) / 2) - rect.top;
                    this.mDisplayManagerInternal.setDisplayScalingDisabled(this.mDisplayId, true);
                    this.mWindowManagerInternal.setForcedDisplaySize(this.mDisplayId, rect.width(), rect.height());
                    this.mDisplayManagerInternal.setDisplayOffsets(this.mDisplayId, -width, -height);
                } else {
                    this.mDisplayManagerInternal.setDisplayScalingDisabled(this.mDisplayId, false);
                    this.mWindowManagerInternal.clearForcedDisplaySize(this.mDisplayId);
                    this.mDisplayManagerInternal.setDisplayOffsets(this.mDisplayId, 0, 0);
                }
            }
            this.mDurationLogger.setDeviceFolded(z);
            this.mDurationLogger.logFocusedAppWithFoldState(z, this.mFocusedApp);
            this.mFolded = Boolean.valueOf(z);
            int beginBroadcast = this.mListeners.beginBroadcast();
            for (int i = 0; i < beginBroadcast; i++) {
                try {
                    this.mListeners.getBroadcastItem(i).onDisplayFoldChanged(this.mDisplayId, z);
                } catch (RemoteException unused) {
                }
            }
            this.mListeners.finishBroadcast();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerDisplayFoldListener(final IDisplayFoldListener iDisplayFoldListener) {
        this.mListeners.register(iDisplayFoldListener);
        if (this.mFolded == null) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.android.server.policy.DisplayFoldController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DisplayFoldController.this.lambda$registerDisplayFoldListener$1(iDisplayFoldListener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerDisplayFoldListener$1(IDisplayFoldListener iDisplayFoldListener) {
        try {
            iDisplayFoldListener.onDisplayFoldChanged(this.mDisplayId, this.mFolded.booleanValue());
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterDisplayFoldListener(IDisplayFoldListener iDisplayFoldListener) {
        this.mListeners.unregister(iDisplayFoldListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOverrideFoldedArea(Rect rect) {
        this.mOverrideFoldedArea.set(rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getFoldedArea() {
        if (!this.mOverrideFoldedArea.isEmpty()) {
            return this.mOverrideFoldedArea;
        }
        return this.mFoldedArea;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDefaultDisplayFocusChanged(String str) {
        this.mFocusedApp = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DisplayFoldController create(Context context, int i) {
        Rect rect;
        WindowManagerInternal windowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
        DisplayManagerInternal displayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);
        String string = context.getResources().getString(R.string.config_wwan_network_service_class);
        if (string == null || string.isEmpty()) {
            rect = new Rect();
        } else {
            rect = Rect.unflattenFromString(string);
        }
        return new DisplayFoldController(context, windowManagerInternal, displayManagerInternal, i, rect, DisplayThread.getHandler());
    }
}
