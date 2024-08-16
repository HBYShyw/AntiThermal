package com.android.server.biometrics.sensors;

import android.content.Context;
import android.hardware.biometrics.BiometricAuthenticator;
import android.os.IBinder;
import android.util.Slog;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class InternalEnumerateClient<T> extends HalClientMonitor<T> implements EnumerateConsumer {
    private static final String TAG = "Biometrics/InternalEnumerateClient";
    private List<? extends BiometricAuthenticator.Identifier> mEnrolledList;
    private List<BiometricAuthenticator.Identifier> mUnknownHALTemplates;
    private BiometricUtils mUtils;

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 6;
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InternalEnumerateClient(Context context, Supplier<T> supplier, IBinder iBinder, int i, String str, List<? extends BiometricAuthenticator.Identifier> list, BiometricUtils biometricUtils, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext) {
        super(context, supplier, iBinder, null, i, str, 0, i2, biometricLogger, biometricContext);
        this.mUnknownHALTemplates = new ArrayList();
        this.mEnrolledList = list;
        this.mUtils = biometricUtils;
    }

    @Override // com.android.server.biometrics.sensors.EnumerateConsumer
    public void onEnumerationResult(BiometricAuthenticator.Identifier identifier, int i) {
        handleEnumeratedTemplate(identifier);
        if (i == 0) {
            doTemplateCleanup();
            this.mCallback.onClientFinished(this, true);
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(ClientMonitorCallback clientMonitorCallback) {
        super.start(clientMonitorCallback);
        startHalOperation();
    }

    private void handleEnumeratedTemplate(BiometricAuthenticator.Identifier identifier) {
        if (identifier == null) {
            Slog.d(TAG, "Null identifier");
            return;
        }
        Slog.v(TAG, "handleEnumeratedTemplate: " + identifier.getBiometricId());
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= this.mEnrolledList.size()) {
                break;
            }
            if (this.mEnrolledList.get(i).getBiometricId() == identifier.getBiometricId()) {
                this.mEnrolledList.remove(i);
                z = true;
                break;
            }
            i++;
        }
        if (!z && identifier.getBiometricId() != 0) {
            this.mUnknownHALTemplates.add(identifier);
        }
        Slog.v(TAG, "Matched: " + z);
    }

    private void doTemplateCleanup() {
        if (this.mEnrolledList == null) {
            Slog.d(TAG, "Null enrolledList");
            return;
        }
        for (int i = 0; i < this.mEnrolledList.size(); i++) {
            BiometricAuthenticator.Identifier identifier = this.mEnrolledList.get(i);
            Slog.e(TAG, "doTemplateCleanup(): Removing dangling template from framework: " + identifier.getBiometricId() + " " + ((Object) identifier.getName()));
            this.mUtils.removeBiometricForUser(getContext(), getTargetUserId(), identifier.getBiometricId());
            getLogger().logUnknownEnrollmentInFramework();
        }
        this.mEnrolledList.clear();
    }

    public List<BiometricAuthenticator.Identifier> getUnknownHALTemplates() {
        return this.mUnknownHALTemplates;
    }
}
