package com.android.server.biometrics.sensors.face.aidl;

import android.content.Context;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.face.Face;
import android.os.IBinder;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.BiometricUtils;
import com.android.server.biometrics.sensors.InternalCleanupClient;
import com.android.server.biometrics.sensors.InternalEnumerateClient;
import com.android.server.biometrics.sensors.RemovalClient;
import com.android.server.biometrics.sensors.face.FaceUtils;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceInternalCleanupClient extends InternalCleanupClient<Face, AidlSession> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public FaceInternalCleanupClient(Context context, Supplier<AidlSession> supplier, int i, String str, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext, BiometricUtils<Face> biometricUtils, Map<Integer, Long> map) {
        super(context, supplier, i, str, i2, biometricLogger, biometricContext, biometricUtils, map);
    }

    @Override // com.android.server.biometrics.sensors.InternalCleanupClient
    protected InternalEnumerateClient<AidlSession> getEnumerateClient(Context context, Supplier<AidlSession> supplier, IBinder iBinder, int i, String str, List<Face> list, BiometricUtils<Face> biometricUtils, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext) {
        return new FaceInternalEnumerateClient(context, supplier, iBinder, i, str, list, biometricUtils, i2, biometricLogger, biometricContext);
    }

    @Override // com.android.server.biometrics.sensors.InternalCleanupClient
    protected RemovalClient<Face, AidlSession> getRemovalClient(Context context, Supplier<AidlSession> supplier, IBinder iBinder, int i, int i2, String str, BiometricUtils<Face> biometricUtils, int i3, BiometricLogger biometricLogger, BiometricContext biometricContext, Map<Integer, Long> map) {
        return new FaceRemovalClient(context, supplier, iBinder, null, new int[]{i}, i2, str, biometricUtils, i3, biometricLogger, biometricContext, map);
    }

    @Override // com.android.server.biometrics.sensors.InternalCleanupClient
    protected void onAddUnknownTemplate(int i, BiometricAuthenticator.Identifier identifier) {
        FaceUtils.getInstance(getSensorId()).addBiometricForUser(getContext(), getTargetUserId(), (Face) identifier);
    }
}
