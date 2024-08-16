package com.android.server.wm;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.IWindow;
import android.view.InputApplicationHandle;
import android.view.InputChannel;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class EmbeddedWindowController {
    private static final String TAG = "WindowManager";
    private final ActivityTaskManagerService mAtmService;
    private final Object mGlobalLock;
    private ArrayMap<IBinder, EmbeddedWindow> mWindows = new ArrayMap<>();
    private ArrayMap<IBinder, EmbeddedWindow> mWindowsByFocusToken = new ArrayMap<>();
    private ArrayMap<IBinder, EmbeddedWindow> mWindowsByWindowToken = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public EmbeddedWindowController(ActivityTaskManagerService activityTaskManagerService) {
        this.mAtmService = activityTaskManagerService;
        this.mGlobalLock = activityTaskManagerService.getGlobalLock();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(final IBinder iBinder, EmbeddedWindow embeddedWindow) {
        try {
            this.mWindows.put(iBinder, embeddedWindow);
            final IBinder focusGrantToken = embeddedWindow.getFocusGrantToken();
            this.mWindowsByFocusToken.put(focusGrantToken, embeddedWindow);
            this.mWindowsByWindowToken.put(embeddedWindow.getWindowToken(), embeddedWindow);
            updateProcessController(embeddedWindow);
            embeddedWindow.mClient.asBinder().linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.wm.EmbeddedWindowController$$ExternalSyntheticLambda0
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    EmbeddedWindowController.this.lambda$add$0(iBinder, focusGrantToken);
                }
            }, 0);
        } catch (RemoteException unused) {
            this.mWindows.remove(iBinder);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$add$0(IBinder iBinder, IBinder iBinder2) {
        synchronized (this.mGlobalLock) {
            this.mWindows.remove(iBinder);
            this.mWindowsByFocusToken.remove(iBinder2);
        }
    }

    private void updateProcessController(EmbeddedWindow embeddedWindow) {
        if (embeddedWindow.mHostActivityRecord == null) {
            return;
        }
        WindowProcessController processController = this.mAtmService.getProcessController(embeddedWindow.mOwnerPid, embeddedWindow.mOwnerUid);
        if (processController == null) {
            Slog.w(TAG, "Could not find the embedding process.");
        } else {
            processController.addHostActivity(embeddedWindow.mHostActivityRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove(IWindow iWindow) {
        for (int size = this.mWindows.size() - 1; size >= 0; size--) {
            EmbeddedWindow valueAt = this.mWindows.valueAt(size);
            if (valueAt.mClient.asBinder() == iWindow.asBinder()) {
                this.mWindows.removeAt(size).onRemoved();
                this.mWindowsByFocusToken.remove(valueAt.getFocusGrantToken());
                this.mWindowsByWindowToken.remove(valueAt.getWindowToken());
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWindowRemoved(WindowState windowState) {
        for (int size = this.mWindows.size() - 1; size >= 0; size--) {
            EmbeddedWindow valueAt = this.mWindows.valueAt(size);
            if (valueAt.mHostWindowState == windowState) {
                this.mWindows.removeAt(size).onRemoved();
                this.mWindowsByFocusToken.remove(valueAt.getFocusGrantToken());
                this.mWindowsByWindowToken.remove(valueAt.getWindowToken());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EmbeddedWindow get(IBinder iBinder) {
        return this.mWindows.get(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EmbeddedWindow getByFocusToken(IBinder iBinder) {
        return this.mWindowsByFocusToken.get(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EmbeddedWindow getByWindowToken(IBinder iBinder) {
        return this.mWindowsByWindowToken.get(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onActivityRemoved(ActivityRecord activityRecord) {
        WindowProcessController processController;
        for (int size = this.mWindows.size() - 1; size >= 0; size--) {
            EmbeddedWindow valueAt = this.mWindows.valueAt(size);
            if (valueAt.mHostActivityRecord == activityRecord && (processController = this.mAtmService.getProcessController(valueAt.mOwnerPid, valueAt.mOwnerUid)) != null) {
                processController.removeHostActivity(activityRecord);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class EmbeddedWindow implements InputTarget {
        final IWindow mClient;
        final int mDisplayId;
        private final IBinder mFocusGrantToken;
        final ActivityRecord mHostActivityRecord;
        final WindowState mHostWindowState;
        InputChannel mInputChannel;
        private boolean mIsFocusable;
        final String mName;
        final int mOwnerPid;
        final int mOwnerUid;
        public Session mSession;
        final int mWindowType;
        final WindowManagerService mWmService;

        @Override // com.android.server.wm.InputTarget
        public boolean canScreenshotIme() {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public EmbeddedWindow(Session session, WindowManagerService windowManagerService, IWindow iWindow, WindowState windowState, int i, int i2, int i3, int i4, IBinder iBinder, String str, boolean z) {
            String str2;
            this.mSession = session;
            this.mWmService = windowManagerService;
            this.mClient = iWindow;
            this.mHostWindowState = windowState;
            this.mHostActivityRecord = windowState != null ? windowState.mActivityRecord : null;
            this.mOwnerUid = i;
            this.mOwnerPid = i2;
            this.mWindowType = i3;
            this.mDisplayId = i4;
            this.mFocusGrantToken = iBinder;
            if (windowState != null) {
                str2 = "-" + windowState.getWindowTag().toString();
            } else {
                str2 = "";
            }
            this.mIsFocusable = z;
            this.mName = "Embedded{" + str + str2 + "}";
        }

        public String toString() {
            return this.mName;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public InputApplicationHandle getApplicationHandle() {
            WindowState windowState = this.mHostWindowState;
            if (windowState == null || windowState.mInputWindowHandle.getInputApplicationHandle() == null) {
                return null;
            }
            return new InputApplicationHandle(this.mHostWindowState.mInputWindowHandle.getInputApplicationHandle());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public InputChannel openInputChannel() {
            InputChannel createInputChannel = this.mWmService.mInputManager.createInputChannel(toString());
            this.mInputChannel = createInputChannel;
            return createInputChannel;
        }

        void onRemoved() {
            InputChannel inputChannel = this.mInputChannel;
            if (inputChannel != null) {
                this.mWmService.mInputManager.removeInputChannel(inputChannel.getToken());
                this.mInputChannel.dispose();
                this.mInputChannel = null;
            }
        }

        @Override // com.android.server.wm.InputTarget
        public WindowState getWindowState() {
            return this.mHostWindowState;
        }

        @Override // com.android.server.wm.InputTarget
        public int getDisplayId() {
            return this.mDisplayId;
        }

        @Override // com.android.server.wm.InputTarget
        public DisplayContent getDisplayContent() {
            return this.mWmService.mRoot.getDisplayContent(getDisplayId());
        }

        @Override // com.android.server.wm.InputTarget
        public IWindow getIWindow() {
            return this.mClient;
        }

        public IBinder getWindowToken() {
            return this.mClient.asBinder();
        }

        @Override // com.android.server.wm.InputTarget
        public int getPid() {
            return this.mOwnerPid;
        }

        @Override // com.android.server.wm.InputTarget
        public int getUid() {
            return this.mOwnerUid;
        }

        IBinder getFocusGrantToken() {
            return this.mFocusGrantToken;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public IBinder getInputChannelToken() {
            InputChannel inputChannel = this.mInputChannel;
            if (inputChannel != null) {
                return inputChannel.getToken();
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setIsFocusable(boolean z) {
            this.mIsFocusable = z;
        }

        @Override // com.android.server.wm.InputTarget
        public boolean receiveFocusFromTapOutside() {
            return this.mIsFocusable;
        }

        private void handleTap(boolean z) {
            if (this.mInputChannel != null) {
                WindowState windowState = this.mHostWindowState;
                if (windowState != null) {
                    this.mWmService.grantEmbeddedWindowFocus(null, windowState.mClient, this.mFocusGrantToken, z);
                    if (z) {
                        this.mHostWindowState.handleTapOutsideFocusInsideSelf();
                        return;
                    }
                    return;
                }
                this.mWmService.grantEmbeddedWindowFocus(this.mSession, this.mFocusGrantToken, z);
            }
        }

        @Override // com.android.server.wm.InputTarget
        public void handleTapOutsideFocusOutsideSelf() {
            handleTap(false);
        }

        @Override // com.android.server.wm.InputTarget
        public void handleTapOutsideFocusInsideSelf() {
            handleTap(true);
        }

        @Override // com.android.server.wm.InputTarget
        public boolean shouldControlIme() {
            return this.mHostWindowState != null;
        }

        @Override // com.android.server.wm.InputTarget
        public InsetsControlTarget getImeControlTarget() {
            WindowState windowState = this.mHostWindowState;
            if (windowState != null) {
                return windowState.getImeControlTarget();
            }
            return this.mWmService.getDefaultDisplayContentLocked().mRemoteInsetsControlTarget;
        }

        @Override // com.android.server.wm.InputTarget
        public boolean isInputMethodClientFocus(int i, int i2) {
            return i == this.mOwnerUid && i2 == this.mOwnerPid;
        }

        @Override // com.android.server.wm.InputTarget
        public ActivityRecord getActivityRecord() {
            return this.mHostActivityRecord;
        }

        @Override // com.android.server.wm.InputTarget
        public void dumpProto(ProtoOutputStream protoOutputStream, long j, int i) {
            long start = protoOutputStream.start(j);
            long start2 = protoOutputStream.start(1146756268034L);
            protoOutputStream.write(1120986464257L, System.identityHashCode(this));
            protoOutputStream.write(1138166333443L, "EmbeddedWindow");
            protoOutputStream.end(start2);
            protoOutputStream.end(start);
        }
    }
}
