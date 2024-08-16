package com.oplus.oms.split.core.splitinstall;

import android.content.Context;

/* loaded from: classes.dex */
public class OplusSplitInstallManagerFactory {
    private OplusSplitInstallManagerFactory() {
    }

    public static <S extends OplusSplitInstallSessionState> OplusSplitInstallManager<S> create(Context context, OplusSplitInstallSessionStateFactory<S> stateFactory) {
        return new OplusSplitInstallManagerImpl(context.getApplicationContext(), stateFactory);
    }
}
