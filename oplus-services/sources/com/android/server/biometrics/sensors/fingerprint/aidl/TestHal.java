package com.android.server.biometrics.sensors.fingerprint.aidl;

import android.hardware.biometrics.common.ICancellationSignal;
import android.hardware.biometrics.common.OperationContext;
import android.hardware.biometrics.fingerprint.IFingerprint;
import android.hardware.biometrics.fingerprint.ISession;
import android.hardware.biometrics.fingerprint.ISessionCallback;
import android.hardware.biometrics.fingerprint.PointerContext;
import android.hardware.biometrics.fingerprint.SensorProps;
import android.hardware.keymaster.HardwareAuthToken;
import android.os.RemoteException;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TestHal extends IFingerprint.Stub {
    private static final String TAG = "fingerprint.aidl.TestHal";

    public String getInterfaceHash() {
        return "637371b53fb7faf9bd43aa51b72c23852d6e6d96";
    }

    public int getInterfaceVersion() {
        return 3;
    }

    public SensorProps[] getSensorProps() {
        Slog.w(TAG, "getSensorProps");
        return new SensorProps[0];
    }

    public ISession createSession(int i, int i2, final ISessionCallback iSessionCallback) {
        Slog.w(TAG, "createSession, sensorId: " + i + " userId: " + i2);
        return new ISession.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.1
            public String getInterfaceHash() {
                return "637371b53fb7faf9bd43aa51b72c23852d6e6d96";
            }

            public int getInterfaceVersion() {
                return 3;
            }

            public void generateChallenge() throws RemoteException {
                Slog.w(TestHal.TAG, "generateChallenge");
                iSessionCallback.onChallengeGenerated(0L);
            }

            public void revokeChallenge(long j) throws RemoteException {
                Slog.w(TestHal.TAG, "revokeChallenge: " + j);
                iSessionCallback.onChallengeRevoked(j);
            }

            public ICancellationSignal enroll(HardwareAuthToken hardwareAuthToken) {
                Slog.w(TestHal.TAG, "enroll");
                return new ICancellationSignal.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.1.1
                    public String getInterfaceHash() {
                        return "a9ebb97f5abea1dc6800b69d821ef61944e80e65";
                    }

                    public int getInterfaceVersion() {
                        return 3;
                    }

                    public void cancel() throws RemoteException {
                        iSessionCallback.onError((byte) 5, 0);
                    }
                };
            }

            public ICancellationSignal authenticate(long j) {
                Slog.w(TestHal.TAG, "authenticate");
                return new ICancellationSignal.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.1.2
                    public String getInterfaceHash() {
                        return "a9ebb97f5abea1dc6800b69d821ef61944e80e65";
                    }

                    public int getInterfaceVersion() {
                        return 3;
                    }

                    public void cancel() throws RemoteException {
                        iSessionCallback.onError((byte) 5, 0);
                    }
                };
            }

            public ICancellationSignal detectInteraction() {
                Slog.w(TestHal.TAG, "detectInteraction");
                return new ICancellationSignal.Stub() { // from class: com.android.server.biometrics.sensors.fingerprint.aidl.TestHal.1.3
                    public String getInterfaceHash() {
                        return "a9ebb97f5abea1dc6800b69d821ef61944e80e65";
                    }

                    public int getInterfaceVersion() {
                        return 3;
                    }

                    public void cancel() throws RemoteException {
                        iSessionCallback.onError((byte) 5, 0);
                    }
                };
            }

            public void enumerateEnrollments() throws RemoteException {
                Slog.w(TestHal.TAG, "enumerateEnrollments");
                iSessionCallback.onEnrollmentsEnumerated(new int[0]);
            }

            public void removeEnrollments(int[] iArr) throws RemoteException {
                Slog.w(TestHal.TAG, "removeEnrollments");
                iSessionCallback.onEnrollmentsRemoved(iArr);
            }

            public void getAuthenticatorId() throws RemoteException {
                Slog.w(TestHal.TAG, "getAuthenticatorId");
                iSessionCallback.onAuthenticatorIdRetrieved(0L);
            }

            public void invalidateAuthenticatorId() throws RemoteException {
                Slog.w(TestHal.TAG, "invalidateAuthenticatorId");
                iSessionCallback.onAuthenticatorIdInvalidated(0L);
            }

            public void resetLockout(HardwareAuthToken hardwareAuthToken) throws RemoteException {
                Slog.w(TestHal.TAG, "resetLockout");
                iSessionCallback.onLockoutCleared();
            }

            public void close() throws RemoteException {
                Slog.w(TestHal.TAG, "close");
                iSessionCallback.onSessionClosed();
            }

            public void onPointerDown(int i3, int i4, int i5, float f, float f2) {
                Slog.w(TestHal.TAG, "onPointerDown");
            }

            public void onPointerUp(int i3) {
                Slog.w(TestHal.TAG, "onPointerUp");
            }

            public void onUiReady() {
                Slog.w(TestHal.TAG, "onUiReady");
            }

            public ICancellationSignal authenticateWithContext(long j, OperationContext operationContext) {
                return authenticate(j);
            }

            public ICancellationSignal enrollWithContext(HardwareAuthToken hardwareAuthToken, OperationContext operationContext) {
                return enroll(hardwareAuthToken);
            }

            public ICancellationSignal detectInteractionWithContext(OperationContext operationContext) {
                return detectInteraction();
            }

            public void onPointerDownWithContext(PointerContext pointerContext) {
                onPointerDown(pointerContext.pointerId, (int) pointerContext.x, (int) pointerContext.y, pointerContext.minor, pointerContext.major);
            }

            public void onPointerUpWithContext(PointerContext pointerContext) {
                onPointerUp(pointerContext.pointerId);
            }

            public void onContextChanged(OperationContext operationContext) {
                Slog.w(TestHal.TAG, "onContextChanged");
            }

            public void onPointerCancelWithContext(PointerContext pointerContext) {
                Slog.w(TestHal.TAG, "onPointerCancelWithContext");
            }

            public void setIgnoreDisplayTouches(boolean z) {
                Slog.w(TestHal.TAG, "setIgnoreDisplayTouches");
            }
        };
    }
}
