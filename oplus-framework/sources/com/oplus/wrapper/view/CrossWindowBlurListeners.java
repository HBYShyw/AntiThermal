package com.oplus.wrapper.view;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class CrossWindowBlurListeners {
    public static final boolean CROSS_WINDOW_BLUR_SUPPORTED = getCrossWindowBlurSupported();
    private static final Object SLOCK = new Object();
    private static volatile CrossWindowBlurListeners sCrossWindowBlurListeners;
    private final android.view.CrossWindowBlurListeners mCrossWindowBlurListeners;

    private CrossWindowBlurListeners(android.view.CrossWindowBlurListeners crossWindowBlurListeners) {
        this.mCrossWindowBlurListeners = crossWindowBlurListeners;
    }

    private static boolean getCrossWindowBlurSupported() {
        return android.view.CrossWindowBlurListeners.CROSS_WINDOW_BLUR_SUPPORTED;
    }

    public static CrossWindowBlurListeners getInstance() {
        CrossWindowBlurListeners instance = sCrossWindowBlurListeners;
        if (instance == null) {
            synchronized (SLOCK) {
                instance = sCrossWindowBlurListeners;
                if (instance == null) {
                    instance = new CrossWindowBlurListeners(android.view.CrossWindowBlurListeners.getInstance());
                    sCrossWindowBlurListeners = instance;
                }
            }
        }
        return instance;
    }

    public void addListener(Executor executor, Consumer<Boolean> listener) {
        this.mCrossWindowBlurListeners.addListener(executor, listener);
    }

    public void removeListener(Consumer<Boolean> listener) {
        this.mCrossWindowBlurListeners.removeListener(listener);
    }
}
