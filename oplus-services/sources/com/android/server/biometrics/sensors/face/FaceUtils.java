package com.android.server.biometrics.sensors.face;

import android.content.Context;
import android.hardware.face.Face;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.server.biometrics.sensors.BiometricUtils;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceUtils implements BiometricUtils<Face> {
    private static final String LEGACY_FACE_FILE = "settings_face.xml";
    private static final Object sInstanceLock = new Object();
    private static SparseArray<FaceUtils> sInstances;
    private final String mFileName;

    @GuardedBy({"this"})
    private final SparseArray<FaceUserState> mUserStates = new SparseArray<>();

    public static FaceUtils getInstance(int i) {
        return getInstance(i, LEGACY_FACE_FILE);
    }

    private static FaceUtils getInstance(int i, String str) {
        FaceUtils faceUtils;
        synchronized (sInstanceLock) {
            if (sInstances == null) {
                sInstances = new SparseArray<>();
            }
            if (sInstances.get(i) == null) {
                if (str == null) {
                    str = "settings_face_" + i + ".xml";
                }
                sInstances.put(i, new FaceUtils(str));
            }
            faceUtils = sInstances.get(i);
        }
        return faceUtils;
    }

    public static FaceUtils getLegacyInstance(int i) {
        return getInstance(i, LEGACY_FACE_FILE);
    }

    private FaceUtils(String str) {
        this.mFileName = str;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public List<Face> getBiometricsForUser(Context context, int i) {
        return getStateForUser(context, i).getBiometrics();
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void addBiometricForUser(Context context, int i, Face face) {
        getStateForUser(context, i).addBiometric(face);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void removeBiometricForUser(Context context, int i, int i2) {
        getStateForUser(context, i).removeBiometric(i2);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void renameBiometricForUser(Context context, int i, int i2, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        getStateForUser(context, i).renameBiometric(i2, charSequence);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public CharSequence getUniqueName(Context context, int i) {
        return getStateForUser(context, i).getUniqueName();
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void setInvalidationInProgress(Context context, int i, boolean z) {
        getStateForUser(context, i).setInvalidationInProgress(z);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public boolean isInvalidationInProgress(Context context, int i) {
        return getStateForUser(context, i).isInvalidationInProgress();
    }

    private FaceUserState getStateForUser(Context context, int i) {
        FaceUserState faceUserState;
        synchronized (this) {
            faceUserState = this.mUserStates.get(i);
            if (faceUserState == null) {
                faceUserState = new FaceUserState(context, i, this.mFileName);
                this.mUserStates.put(i, faceUserState);
            }
        }
        return faceUserState;
    }
}
