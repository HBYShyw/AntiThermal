package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.content.Context;
import android.hardware.biometrics.fingerprint.IFingerprint;
import android.hardware.biometrics.fingerprint.ISession;
import android.hardware.biometrics.fingerprint.ISessionCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.StartUserClient;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintStartUserClient extends StartUserClient<IFingerprint, ISession> {
    private static final String TAG = "FingerprintStartUserClient";
    private final ISessionCallback mSessionCallback;

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    public FingerprintStartUserClient(Context context, Supplier<IFingerprint> supplier, IBinder iBinder, int i, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext, ISessionCallback iSessionCallback, StartUserClient.UserStartedCallback<ISession> userStartedCallback) {
        super(context, supplier, iBinder, i, i2, biometricLogger, biometricContext, userStartedCallback);
        this.mSessionCallback = iSessionCallback;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(ClientMonitorCallback clientMonitorCallback) {
        super.start(clientMonitorCallback);
        startHalOperation();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void startHalOperation() {
        try {
            IFingerprint freshDaemon = getFreshDaemon();
            int interfaceVersion = freshDaemon.getInterfaceVersion();
            int hookTargetUserId = this.mBaseClientMonitorExt.hookTargetUserId(getTargetUserId());
            Slog.d(TAG, " TargetUserId: " + getTargetUserId() + " ,hookTargetUserIdToGid: " + hookTargetUserId);
            ISession createSession = freshDaemon.createSession(getSensorId(), hookTargetUserId, this.mSessionCallback);
            if (createSession != null) {
                Binder.allowBlocking(createSession.asBinder());
            } else {
                Slog.e(TAG, "newSession is null");
            }
            this.mUserStartedCallback.onUserStarted(getTargetUserId(), createSession, interfaceVersion);
            getCallback().onClientFinished(this, true);
        } catch (RemoteException e) {
            Slog.e(TAG, "Remote exception", e);
            getCallback().onClientFinished(this, false);
        }
    }
}
