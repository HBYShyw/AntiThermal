package com.android.server.wm;

import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import android.view.IRotationWatcher;
import java.io.PrintWriter;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class RotationWatcherController {
    private volatile boolean mHasProposedRotationListeners;
    private final WindowManagerService mService;
    private final ArrayList<DisplayRotationWatcher> mDisplayRotationWatchers = new ArrayList<>();
    private final ArrayList<ProposedRotationListener> mProposedRotationListeners = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public RotationWatcherController(WindowManagerService windowManagerService) {
        this.mService = windowManagerService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerDisplayRotationWatcher(IRotationWatcher iRotationWatcher, int i) {
        IBinder asBinder = iRotationWatcher.asBinder();
        for (int size = this.mDisplayRotationWatchers.size() - 1; size >= 0; size--) {
            if (asBinder == this.mDisplayRotationWatchers.get(size).mWatcher.asBinder()) {
                throw new IllegalArgumentException("Registering existed rotation watcher");
            }
        }
        register(asBinder, new DisplayRotationWatcher(this.mService, iRotationWatcher, i), this.mDisplayRotationWatchers);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerProposedRotationListener(IRotationWatcher iRotationWatcher, IBinder iBinder) {
        IBinder asBinder = iRotationWatcher.asBinder();
        for (int size = this.mProposedRotationListeners.size() - 1; size >= 0; size--) {
            ProposedRotationListener proposedRotationListener = this.mProposedRotationListeners.get(size);
            if (iBinder == proposedRotationListener.mToken || asBinder == proposedRotationListener.mWatcher.asBinder()) {
                Slog.w("WindowManager", "Register rotation listener to a registered token, uid=" + Binder.getCallingUid());
                return;
            }
        }
        register(asBinder, new ProposedRotationListener(this.mService, iRotationWatcher, iBinder), this.mProposedRotationListeners);
        this.mHasProposedRotationListeners = !this.mProposedRotationListeners.isEmpty();
    }

    private static <T extends RotationWatcher> void register(IBinder iBinder, T t, ArrayList<T> arrayList) {
        try {
            iBinder.linkToDeath(t, 0);
            arrayList.add(t);
        } catch (RemoteException unused) {
        }
    }

    private static <T extends RotationWatcher> boolean unregister(IRotationWatcher iRotationWatcher, ArrayList<T> arrayList) {
        IBinder asBinder = iRotationWatcher.asBinder();
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            T t = arrayList.get(size);
            if (asBinder == t.mWatcher.asBinder()) {
                arrayList.remove(size);
                t.unlinkToDeath();
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRotationWatcher(IRotationWatcher iRotationWatcher) {
        if (unregister(iRotationWatcher, this.mProposedRotationListeners)) {
            this.mHasProposedRotationListeners = !this.mProposedRotationListeners.isEmpty();
        } else {
            unregister(iRotationWatcher, this.mDisplayRotationWatchers);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchDisplayRotationChange(int i, int i2) {
        for (int size = this.mDisplayRotationWatchers.size() - 1; size >= 0; size--) {
            DisplayRotationWatcher displayRotationWatcher = this.mDisplayRotationWatchers.get(size);
            if (displayRotationWatcher.mDisplayId == i) {
                displayRotationWatcher.notifyRotation(i2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchProposedRotation(DisplayContent displayContent, int i) {
        for (int size = this.mProposedRotationListeners.size() - 1; size >= 0; size--) {
            ProposedRotationListener proposedRotationListener = this.mProposedRotationListeners.get(size);
            WindowContainer<?> associatedWindowContainer = getAssociatedWindowContainer(proposedRotationListener.mToken);
            if (associatedWindowContainer != null) {
                if (associatedWindowContainer.mDisplayContent == displayContent) {
                    proposedRotationListener.notifyRotation(i);
                }
            } else {
                this.mProposedRotationListeners.remove(size);
                this.mHasProposedRotationListeners = !this.mProposedRotationListeners.isEmpty();
                proposedRotationListener.unlinkToDeath();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasProposedRotationListeners() {
        return this.mHasProposedRotationListeners;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowContainer<?> getAssociatedWindowContainer(IBinder iBinder) {
        ActivityRecord forTokenLocked = ActivityRecord.forTokenLocked(iBinder);
        return forTokenLocked != null ? forTokenLocked : this.mService.mWindowContextListenerController.getContainer(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        if (!this.mDisplayRotationWatchers.isEmpty()) {
            printWriter.print("  mDisplayRotationWatchers: [");
            for (int size = this.mDisplayRotationWatchers.size() - 1; size >= 0; size--) {
                printWriter.print(' ');
                DisplayRotationWatcher displayRotationWatcher = this.mDisplayRotationWatchers.get(size);
                printWriter.print(displayRotationWatcher.mOwnerUid);
                printWriter.print("->");
                printWriter.print(displayRotationWatcher.mDisplayId);
            }
            printWriter.println(']');
        }
        if (this.mProposedRotationListeners.isEmpty()) {
            return;
        }
        printWriter.print("  mProposedRotationListeners: [");
        for (int size2 = this.mProposedRotationListeners.size() - 1; size2 >= 0; size2--) {
            printWriter.print(' ');
            ProposedRotationListener proposedRotationListener = this.mProposedRotationListeners.get(size2);
            printWriter.print(proposedRotationListener.mOwnerUid);
            printWriter.print("->");
            printWriter.print(getAssociatedWindowContainer(proposedRotationListener.mToken));
        }
        printWriter.println(']');
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class DisplayRotationWatcher extends RotationWatcher {
        final int mDisplayId;

        DisplayRotationWatcher(WindowManagerService windowManagerService, IRotationWatcher iRotationWatcher, int i) {
            super(windowManagerService, iRotationWatcher);
            this.mDisplayId = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class ProposedRotationListener extends RotationWatcher {
        final IBinder mToken;

        ProposedRotationListener(WindowManagerService windowManagerService, IRotationWatcher iRotationWatcher, IBinder iBinder) {
            super(windowManagerService, iRotationWatcher);
            this.mToken = iBinder;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class RotationWatcher implements IBinder.DeathRecipient {
        final int mOwnerUid = Binder.getCallingUid();
        final IRotationWatcher mWatcher;
        final WindowManagerService mWms;

        RotationWatcher(WindowManagerService windowManagerService, IRotationWatcher iRotationWatcher) {
            this.mWms = windowManagerService;
            this.mWatcher = iRotationWatcher;
        }

        void notifyRotation(int i) {
            try {
                this.mWatcher.onRotationChanged(i);
            } catch (RemoteException unused) {
            }
        }

        void unlinkToDeath() {
            this.mWatcher.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mWms.removeRotationWatcher(this.mWatcher);
        }
    }
}
