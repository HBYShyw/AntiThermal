package com.android.server.biometrics.sensors.face.hidl;

import android.content.Context;
import android.hardware.biometrics.face.V1_0.IBiometricsFace;
import android.hardware.biometrics.face.V1_0.IBiometricsFaceClientCallback;
import android.hardware.biometrics.face.V1_0.OptionalBool;
import android.hardware.biometrics.face.V1_0.OptionalUint64;
import android.hardware.face.Face;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.sensors.face.FaceUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TestHal extends IBiometricsFace.Stub {
    private static final String TAG = "face.hidl.TestHal";
    private IBiometricsFaceClientCallback mCallback;
    private final Context mContext;
    private final int mSensorId;
    private int mUserId;

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int revokeChallenge() {
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int setFeature(int i, boolean z, ArrayList<Byte> arrayList, int i2) {
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int userActivity() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TestHal(Context context, int i) {
        this.mContext = context;
        this.mSensorId = i;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public OptionalUint64 setCallback(IBiometricsFaceClientCallback iBiometricsFaceClientCallback) {
        this.mCallback = iBiometricsFaceClientCallback;
        new OptionalUint64().status = 0;
        return new OptionalUint64();
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int setActiveUser(int i, String str) {
        this.mUserId = i;
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public OptionalUint64 generateChallenge(int i) {
        Slog.w(TAG, "generateChallenge");
        OptionalUint64 optionalUint64 = new OptionalUint64();
        optionalUint64.status = 0;
        optionalUint64.value = 0L;
        return optionalUint64;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int enroll(ArrayList<Byte> arrayList, int i, ArrayList<Integer> arrayList2) {
        Slog.w(TAG, "enroll");
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public OptionalBool getFeature(int i, int i2) {
        OptionalBool optionalBool = new OptionalBool();
        optionalBool.status = 0;
        optionalBool.value = true;
        return optionalBool;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public OptionalUint64 getAuthenticatorId() {
        OptionalUint64 optionalUint64 = new OptionalUint64();
        optionalUint64.status = 0;
        optionalUint64.value = 0L;
        return optionalUint64;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int cancel() throws RemoteException {
        IBiometricsFaceClientCallback iBiometricsFaceClientCallback = this.mCallback;
        if (iBiometricsFaceClientCallback == null) {
            return 0;
        }
        iBiometricsFaceClientCallback.onError(0L, 0, 5, 0);
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int enumerate() throws RemoteException {
        Slog.w(TAG, "enumerate");
        IBiometricsFaceClientCallback iBiometricsFaceClientCallback = this.mCallback;
        if (iBiometricsFaceClientCallback != null) {
            iBiometricsFaceClientCallback.onEnumerate(0L, new ArrayList<>(), 0);
        }
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int remove(int i) throws RemoteException {
        Slog.w(TAG, "remove");
        IBiometricsFaceClientCallback iBiometricsFaceClientCallback = this.mCallback;
        if (iBiometricsFaceClientCallback == null) {
            return 0;
        }
        if (i == 0) {
            List<Face> biometricsForUser = FaceUtils.getInstance(this.mSensorId).getBiometricsForUser(this.mContext, this.mUserId);
            ArrayList<Integer> arrayList = new ArrayList<>();
            Iterator<Face> it = biometricsForUser.iterator();
            while (it.hasNext()) {
                arrayList.add(Integer.valueOf(it.next().getBiometricId()));
            }
            this.mCallback.onRemoved(0L, arrayList, this.mUserId);
            return 0;
        }
        iBiometricsFaceClientCallback.onRemoved(0L, new ArrayList<>(Collections.singletonList(Integer.valueOf(i))), this.mUserId);
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int authenticate(long j) {
        Slog.w(TAG, "authenticate");
        return 0;
    }

    @Override // android.hardware.biometrics.face.V1_0.IBiometricsFace
    public int resetLockout(ArrayList<Byte> arrayList) {
        Slog.w(TAG, "resetLockout");
        return 0;
    }
}
