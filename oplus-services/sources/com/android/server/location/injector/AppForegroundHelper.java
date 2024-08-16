package com.android.server.location.injector;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class AppForegroundHelper {
    protected static final int FOREGROUND_IMPORTANCE_CUTOFF = 125;
    private final CopyOnWriteArrayList<AppForegroundListener> mListeners = new CopyOnWriteArrayList<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface AppForegroundListener {
        void onAppForegroundChanged(int i, boolean z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean isForeground(int i) {
        return i <= FOREGROUND_IMPORTANCE_CUTOFF;
    }

    public abstract boolean isAppForeground(int i);

    public final void addListener(AppForegroundListener appForegroundListener) {
        this.mListeners.add(appForegroundListener);
    }

    public final void removeListener(AppForegroundListener appForegroundListener) {
        this.mListeners.remove(appForegroundListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void notifyAppForeground(int i, boolean z) {
        Iterator<AppForegroundListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onAppForegroundChanged(i, z);
        }
    }
}
