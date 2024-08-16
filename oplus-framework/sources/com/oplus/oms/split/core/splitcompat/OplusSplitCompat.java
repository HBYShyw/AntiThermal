package com.oplus.oms.split.core.splitcompat;

import android.content.Context;
import com.oplus.oms.split.core.splitinstall.LoadedSplitFetcherSingleton;
import com.oplus.oms.split.core.splitinstall.SplitSessionLoaderSingleton;
import com.oplus.oms.split.core.tasks.TaskExecutors;
import com.oplus.oms.split.splitload.SplitLoadManager;
import com.oplus.oms.split.splitload.SplitLoadManagerImpl;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public class OplusSplitCompat {
    private static final AtomicReference<OplusSplitCompat> REFERENCE = new AtomicReference<>(null);

    private OplusSplitCompat() {
    }

    public static boolean install(Context context) {
        AtomicReference<OplusSplitCompat> atomicReference = REFERENCE;
        if (atomicReference.compareAndSet(null, new OplusSplitCompat())) {
            OplusSplitCompat compat = atomicReference.get();
            SplitSessionLoaderSingleton.set(new SplitSessionLoaderImpl(TaskExecutors.MAIN_THREAD));
            LoadedSplitFetcherSingleton.set(new LoadedSplitFetcherImpl(compat));
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasInstance() {
        return REFERENCE.get() != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Set<String> getLoadedSplits() {
        SplitLoadManager loadManager = SplitLoadManagerImpl.getInstance();
        return loadManager.getLoadedSplitNames();
    }
}
