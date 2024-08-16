package com.oplus.oms.split.splitinstall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.core.splitinstall.SplitInstallListenerRegistry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SplitInstallSessionManagerImpl implements SplitInstallSessionManager {
    private static final String TAG = "SplitInstallSessionManagerImpl";
    private final Context mContext;
    private final String mPackageName;
    private SplitInstallListenerRegistry mSplitInstallListenerRegistry;
    private final SparseArray<SplitInstallInternalSessionState> mActiveSessionStates = new SparseArray<>();
    private final Object mLock = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitInstallSessionManagerImpl(Context context) {
        this.mContext = context;
        this.mPackageName = context.getPackageName();
    }

    @Override // com.oplus.oms.split.splitinstall.SplitInstallSessionManager
    public void setInstallListenerRegistry(SplitInstallListenerRegistry registry) {
        this.mSplitInstallListenerRegistry = registry;
    }

    @Override // com.oplus.oms.split.splitinstall.SplitInstallSessionManager
    public void setSessionState(int sessionId, SplitInstallInternalSessionState sessionState) {
        synchronized (this.mLock) {
            if (sessionId != 0) {
                if (this.mActiveSessionStates.get(sessionId) == null) {
                    this.mActiveSessionStates.put(sessionId, sessionState);
                }
            }
        }
    }

    @Override // com.oplus.oms.split.splitinstall.SplitInstallSessionManager
    public void changeSessionState(int sessionId, int status) {
        synchronized (this.mLock) {
            SplitInstallInternalSessionState sessionState = this.mActiveSessionStates.get(sessionId);
            if (sessionState != null) {
                sessionState.setStatus(status);
                if (status == 7 || status == 6 || status == 10) {
                    removeSessionState(sessionId);
                }
            }
        }
    }

    @Override // com.oplus.oms.split.splitinstall.SplitInstallSessionManager
    public void removeSessionState(int sessionId) {
        synchronized (this.mLock) {
            if (sessionId != 0) {
                this.mActiveSessionStates.remove(sessionId);
            }
        }
    }

    @Override // com.oplus.oms.split.splitinstall.SplitInstallSessionManager
    public boolean isActiveSessionsLimitExceeded() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mActiveSessionStates.size(); i++) {
                SplitInstallInternalSessionState state = this.mActiveSessionStates.valueAt(i);
                if (state.status() == 2) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // com.oplus.oms.split.splitinstall.SplitInstallSessionManager
    public SplitInstallInternalSessionState getSessionState(int sessionId) {
        SplitInstallInternalSessionState splitInstallInternalSessionState;
        synchronized (this.mLock) {
            splitInstallInternalSessionState = this.mActiveSessionStates.get(sessionId);
        }
        return splitInstallInternalSessionState;
    }

    @Override // com.oplus.oms.split.splitinstall.SplitInstallSessionManager
    public List<SplitInstallInternalSessionState> getSessionStates() {
        List<SplitInstallInternalSessionState> asList;
        synchronized (this.mLock) {
            asList = asList(this.mActiveSessionStates);
        }
        return asList;
    }

    @Override // com.oplus.oms.split.splitinstall.SplitInstallSessionManager
    public boolean isIncompatibleWithExistingSession(List<String> moduleNames) {
        boolean incompatibleWithExistingSession;
        if (moduleNames == null || moduleNames.isEmpty()) {
            return false;
        }
        synchronized (this.mLock) {
            incompatibleWithExistingSession = false;
            List<SplitInstallInternalSessionState> sessionStates = getSessionStates();
            for (int i = 0; i < sessionStates.size(); i++) {
                SplitInstallInternalSessionState sessionState = sessionStates.get(i);
                Iterator<String> it = moduleNames.iterator();
                while (true) {
                    if (it.hasNext()) {
                        String moduleName = it.next();
                        if (sessionState.moduleNames().contains(moduleName)) {
                            incompatibleWithExistingSession = true;
                            break;
                        }
                        if (incompatibleWithExistingSession) {
                            break;
                        }
                    }
                }
            }
        }
        return incompatibleWithExistingSession;
    }

    @Override // com.oplus.oms.split.splitinstall.SplitInstallSessionManager
    public void emitSessionState(SplitInstallInternalSessionState sessionState) {
        Bundle bundle = SplitInstallInternalSessionState.transform2Bundle(sessionState);
        Intent intent = new Intent();
        intent.putExtra("session_state", bundle);
        this.mSplitInstallListenerRegistry.changeStatus(intent);
        SplitLog.i(TAG, "sendEmitSessionState: sessionId: %d, status: %d", Integer.valueOf(sessionState.sessionId()), Integer.valueOf(sessionState.status()));
    }

    private static <C> List<C> asList(SparseArray<C> sparseArray) {
        List<C> arrayList = new ArrayList<>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }
        return arrayList;
    }
}
