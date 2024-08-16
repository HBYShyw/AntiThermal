package com.android.server.biometrics.sensors.face.aidl;

import android.content.Context;
import android.hardware.biometrics.face.IFace;
import android.hardware.biometrics.face.ISession;
import android.hardware.biometrics.face.ISessionCallback;
import android.os.Binder;
import android.os.IBinder;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.StartUserClient;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceStartUserClient extends StartUserClient<IFace, ISession> {
    private static final String TAG = "FaceStartUserClient";
    private final ISessionCallback mSessionCallback;

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    public FaceStartUserClient(Context context, Supplier<IFace> supplier, IBinder iBinder, int i, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext, ISessionCallback iSessionCallback, StartUserClient.UserStartedCallback<ISession> userStartedCallback) {
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
            IFace freshDaemon = getFreshDaemon();
            int interfaceVersion = freshDaemon.getInterfaceVersion();
            ISession createSession = freshDaemon.createSession(getSensorId(), getTargetUserId(), this.mSessionCallback);
            if (createSession != null) {
                Binder.allowBlocking(createSession.asBinder());
            } else {
                Slog.e(TAG, "newSession is null");
            }
            this.mUserStartedCallback.onUserStarted(getTargetUserId(), createSession, interfaceVersion);
            getCallback().onClientFinished(this, true);
        } catch (Exception e) {
            Slog.e(TAG, "Remote exception", e);
            getCallback().onClientFinished(this, false);
        }
    }
}
