package com.oplus.oms.split.splitinstall;

import com.oplus.oms.split.core.splitinstall.SplitInstallListenerRegistry;
import java.util.List;

/* loaded from: classes.dex */
interface SplitInstallSessionManager {
    void changeSessionState(int i, int i2);

    void emitSessionState(SplitInstallInternalSessionState splitInstallInternalSessionState);

    SplitInstallInternalSessionState getSessionState(int i);

    List<SplitInstallInternalSessionState> getSessionStates();

    boolean isActiveSessionsLimitExceeded();

    boolean isIncompatibleWithExistingSession(List<String> list);

    void removeSessionState(int i);

    void setInstallListenerRegistry(SplitInstallListenerRegistry splitInstallListenerRegistry);

    void setSessionState(int i, SplitInstallInternalSessionState splitInstallInternalSessionState);
}
