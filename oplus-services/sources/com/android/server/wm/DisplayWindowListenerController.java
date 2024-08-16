package com.android.server.wm;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.IntArray;
import android.view.IDisplayWindowListener;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DisplayWindowListenerController {
    RemoteCallbackList<IDisplayWindowListener> mDisplayListeners = new RemoteCallbackList<>();
    private final WindowManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayWindowListenerController(WindowManagerService windowManagerService) {
        this.mService = windowManagerService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] registerListener(IDisplayWindowListener iDisplayWindowListener) {
        int[] array;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mDisplayListeners.register(iDisplayWindowListener);
                final IntArray intArray = new IntArray();
                this.mService.mAtmService.mRootWindowContainer.forAllDisplays(new Consumer() { // from class: com.android.server.wm.DisplayWindowListenerController$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DisplayWindowListenerController.lambda$registerListener$0(intArray, (DisplayContent) obj);
                    }
                });
                array = intArray.toArray();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return array;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$registerListener$0(IntArray intArray, DisplayContent displayContent) {
        intArray.add(displayContent.mDisplayId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterListener(IDisplayWindowListener iDisplayWindowListener) {
        this.mDisplayListeners.unregister(iDisplayWindowListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchDisplayAdded(DisplayContent displayContent) {
        int beginBroadcast = this.mDisplayListeners.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                this.mDisplayListeners.getBroadcastItem(i).onDisplayAdded(displayContent.mDisplayId);
            } catch (RemoteException unused) {
            }
        }
        this.mDisplayListeners.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchDisplayChanged(DisplayContent displayContent, Configuration configuration) {
        boolean z = false;
        for (int i = 0; i < displayContent.getParent().getChildCount(); i++) {
            if (displayContent.getParent().getChildAt(i) == displayContent) {
                z = true;
            }
        }
        if (z) {
            int beginBroadcast = this.mDisplayListeners.beginBroadcast();
            for (int i2 = 0; i2 < beginBroadcast; i2++) {
                try {
                    this.mDisplayListeners.getBroadcastItem(i2).onDisplayConfigurationChanged(displayContent.getDisplayId(), configuration);
                } catch (RemoteException unused) {
                }
            }
            this.mDisplayListeners.finishBroadcast();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchDisplayRemoved(DisplayContent displayContent) {
        int beginBroadcast = this.mDisplayListeners.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                this.mDisplayListeners.getBroadcastItem(i).onDisplayRemoved(displayContent.mDisplayId);
            } catch (RemoteException unused) {
            }
        }
        this.mDisplayListeners.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchFixedRotationStarted(DisplayContent displayContent, int i) {
        int beginBroadcast = this.mDisplayListeners.beginBroadcast();
        for (int i2 = 0; i2 < beginBroadcast; i2++) {
            try {
                this.mDisplayListeners.getBroadcastItem(i2).onFixedRotationStarted(displayContent.mDisplayId, i);
            } catch (RemoteException unused) {
            }
        }
        this.mDisplayListeners.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchFixedRotationFinished(DisplayContent displayContent) {
        int beginBroadcast = this.mDisplayListeners.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                this.mDisplayListeners.getBroadcastItem(i).onFixedRotationFinished(displayContent.mDisplayId);
            } catch (RemoteException unused) {
            }
        }
        this.mDisplayListeners.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchKeepClearAreasChanged(DisplayContent displayContent, Set<Rect> set, Set<Rect> set2) {
        int beginBroadcast = this.mDisplayListeners.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                this.mDisplayListeners.getBroadcastItem(i).onKeepClearAreasChanged(displayContent.mDisplayId, new ArrayList(set), new ArrayList(set2));
            } catch (RemoteException unused) {
            }
        }
        this.mDisplayListeners.finishBroadcast();
    }
}
