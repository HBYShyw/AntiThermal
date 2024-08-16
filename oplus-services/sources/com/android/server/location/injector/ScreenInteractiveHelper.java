package com.android.server.location.injector;

import android.util.Log;
import com.android.server.location.LocationManagerService;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class ScreenInteractiveHelper {
    private final CopyOnWriteArrayList<ScreenInteractiveChangedListener> mListeners = new CopyOnWriteArrayList<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ScreenInteractiveChangedListener {
        void onScreenInteractiveChanged(boolean z);
    }

    public abstract boolean isInteractive();

    public final void addListener(ScreenInteractiveChangedListener screenInteractiveChangedListener) {
        this.mListeners.add(screenInteractiveChangedListener);
    }

    public final void removeListener(ScreenInteractiveChangedListener screenInteractiveChangedListener) {
        this.mListeners.remove(screenInteractiveChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void notifyScreenInteractiveChanged(boolean z) {
        if (LocationManagerService.D) {
            Log.d(LocationManagerService.TAG, "screen interactive is now " + z);
        }
        Iterator<ScreenInteractiveChangedListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onScreenInteractiveChanged(z);
        }
    }
}
