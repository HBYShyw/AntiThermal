package com.oplus.oms.split.core.splitinstall;

import android.os.Bundle;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionState;

/* loaded from: classes.dex */
public interface OplusSplitInstallSessionStateFactory<S extends OplusSplitInstallSessionState> {
    S create(Bundle bundle);

    S newState(S s, int i);

    S newState(S s, int i, int i2);
}
