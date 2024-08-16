package com.android.server.wm;

import com.android.internal.os.BackgroundThread;
import com.android.server.wm.WindowManagerInternal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
final class TaskSystemBarsListenerController {
    private final HashSet<WindowManagerInternal.TaskSystemBarsListener> mListeners = new HashSet<>();
    private final Executor mBackgroundExecutor = BackgroundThread.getExecutor();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerListener(WindowManagerInternal.TaskSystemBarsListener taskSystemBarsListener) {
        this.mListeners.add(taskSystemBarsListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterListener(WindowManagerInternal.TaskSystemBarsListener taskSystemBarsListener) {
        this.mListeners.remove(taskSystemBarsListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchTransientSystemBarVisibilityChanged(final int i, final boolean z, final boolean z2) {
        final HashSet hashSet = new HashSet(this.mListeners);
        this.mBackgroundExecutor.execute(new Runnable() { // from class: com.android.server.wm.TaskSystemBarsListenerController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TaskSystemBarsListenerController.lambda$dispatchTransientSystemBarVisibilityChanged$0(hashSet, i, z, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dispatchTransientSystemBarVisibilityChanged$0(HashSet hashSet, int i, boolean z, boolean z2) {
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            ((WindowManagerInternal.TaskSystemBarsListener) it.next()).onTransientSystemBarsVisibilityChanged(i, z, z2);
        }
    }
}
