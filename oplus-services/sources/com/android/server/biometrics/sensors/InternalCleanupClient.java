package com.android.server.biometrics.sensors;

import android.content.Context;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.BiometricAuthenticator.Identifier;
import android.os.Build;
import android.os.IBinder;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class InternalCleanupClient<S extends BiometricAuthenticator.Identifier, T> extends HalClientMonitor<T> implements EnumerateConsumer, RemovalConsumer, EnrollmentModifier {
    private static final String TAG = "Biometrics/InternalCleanupClient";
    private final Map<Integer, Long> mAuthenticatorIds;
    private final BiometricUtils<S> mBiometricUtils;
    private BaseClientMonitor mCurrentTask;
    private final ClientMonitorCallback mEnumerateCallback;
    private boolean mFavorHalEnrollments;
    private final boolean mHasEnrollmentsBeforeStarting;
    private final ClientMonitorCallback mRemoveCallback;
    private final ArrayList<UserTemplate> mUnknownHALTemplates;

    protected abstract InternalEnumerateClient<T> getEnumerateClient(Context context, Supplier<T> supplier, IBinder iBinder, int i, String str, List<S> list, BiometricUtils<S> biometricUtils, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext);

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 7;
    }

    protected abstract RemovalClient<S, T> getRemovalClient(Context context, Supplier<T> supplier, IBinder iBinder, int i, int i2, String str, BiometricUtils<S> biometricUtils, int i3, BiometricLogger biometricLogger, BiometricContext biometricContext, Map<Integer, Long> map);

    protected void onAddUnknownTemplate(int i, BiometricAuthenticator.Identifier identifier) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void startHalOperation() {
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class UserTemplate {
        final BiometricAuthenticator.Identifier mIdentifier;
        final int mUserId;

        UserTemplate(BiometricAuthenticator.Identifier identifier, int i) {
            this.mIdentifier = identifier;
            this.mUserId = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InternalCleanupClient(Context context, Supplier<T> supplier, int i, String str, int i2, BiometricLogger biometricLogger, BiometricContext biometricContext, BiometricUtils<S> biometricUtils, Map<Integer, Long> map) {
        super(context, supplier, null, null, i, str, 0, i2, biometricLogger, biometricContext);
        this.mUnknownHALTemplates = new ArrayList<>();
        this.mFavorHalEnrollments = false;
        this.mEnumerateCallback = new ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.InternalCleanupClient.1
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(BaseClientMonitor baseClientMonitor, boolean z) {
                List<BiometricAuthenticator.Identifier> unknownHALTemplates = ((InternalEnumerateClient) InternalCleanupClient.this.mCurrentTask).getUnknownHALTemplates();
                Slog.d(InternalCleanupClient.TAG, "Enumerate onClientFinished: " + baseClientMonitor + ", success: " + z);
                if (!unknownHALTemplates.isEmpty()) {
                    Slog.w(InternalCleanupClient.TAG, "Adding " + unknownHALTemplates.size() + " templates for deletion");
                }
                Iterator<BiometricAuthenticator.Identifier> it = unknownHALTemplates.iterator();
                while (it.hasNext()) {
                    InternalCleanupClient.this.mUnknownHALTemplates.add(new UserTemplate(it.next(), InternalCleanupClient.this.mCurrentTask.getTargetUserId()));
                }
                if (InternalCleanupClient.this.mUnknownHALTemplates.isEmpty()) {
                    return;
                }
                if (InternalCleanupClient.this.mFavorHalEnrollments && Build.isDebuggable()) {
                    try {
                        Iterator it2 = InternalCleanupClient.this.mUnknownHALTemplates.iterator();
                        while (it2.hasNext()) {
                            UserTemplate userTemplate = (UserTemplate) it2.next();
                            Slog.i(InternalCleanupClient.TAG, "Adding unknown HAL template: " + userTemplate.mIdentifier.getBiometricId());
                            InternalCleanupClient.this.onAddUnknownTemplate(userTemplate.mUserId, userTemplate.mIdentifier);
                        }
                        return;
                    } finally {
                        InternalCleanupClient internalCleanupClient = InternalCleanupClient.this;
                        internalCleanupClient.mCallback.onClientFinished(internalCleanupClient, z);
                    }
                }
                InternalCleanupClient.this.startCleanupUnknownHalTemplates();
            }
        };
        this.mRemoveCallback = new ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.InternalCleanupClient.2
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(BaseClientMonitor baseClientMonitor, boolean z) {
                if (InternalCleanupClient.this.mUnknownHALTemplates != null && InternalCleanupClient.this.mUnknownHALTemplates.size() != 0) {
                    Slog.d(InternalCleanupClient.TAG, "next mUnknownHALTemplates  size: " + InternalCleanupClient.this.mUnknownHALTemplates.size());
                    UserTemplate userTemplate = (UserTemplate) InternalCleanupClient.this.mUnknownHALTemplates.get(0);
                    InternalCleanupClient.this.mUnknownHALTemplates.remove(userTemplate);
                    InternalCleanupClient internalCleanupClient = InternalCleanupClient.this;
                    Context context2 = internalCleanupClient.getContext();
                    InternalCleanupClient internalCleanupClient2 = InternalCleanupClient.this;
                    internalCleanupClient.mCurrentTask = internalCleanupClient.getRemovalClient(context2, internalCleanupClient2.mLazyDaemon, internalCleanupClient2.getToken(), userTemplate.mIdentifier.getBiometricId(), userTemplate.mUserId, InternalCleanupClient.this.getContext().getPackageName(), InternalCleanupClient.this.mBiometricUtils, InternalCleanupClient.this.getSensorId(), InternalCleanupClient.this.getLogger(), InternalCleanupClient.this.getBiometricContext(), InternalCleanupClient.this.mAuthenticatorIds);
                    InternalCleanupClient.this.mCurrentTask.start(InternalCleanupClient.this.mRemoveCallback);
                    return;
                }
                Slog.d(InternalCleanupClient.TAG, "Remove onClientFinished: " + baseClientMonitor + ", success: " + z);
                if (InternalCleanupClient.this.mUnknownHALTemplates.isEmpty()) {
                    InternalCleanupClient internalCleanupClient3 = InternalCleanupClient.this;
                    internalCleanupClient3.mCallback.onClientFinished(internalCleanupClient3, z);
                } else {
                    InternalCleanupClient.this.startCleanupUnknownHalTemplates();
                }
            }
        };
        this.mBiometricUtils = biometricUtils;
        this.mAuthenticatorIds = map;
        this.mHasEnrollmentsBeforeStarting = !biometricUtils.getBiometricsForUser(context, i).isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startCleanupUnknownHalTemplates() {
        Slog.d(TAG, "startCleanupUnknownHalTemplates, size: " + this.mUnknownHALTemplates.size());
        UserTemplate userTemplate = this.mUnknownHALTemplates.get(0);
        this.mUnknownHALTemplates.remove(userTemplate);
        this.mCurrentTask = getRemovalClient(getContext(), this.mLazyDaemon, getToken(), userTemplate.mIdentifier.getBiometricId(), userTemplate.mUserId, getContext().getPackageName(), this.mBiometricUtils, getSensorId(), getLogger(), getBiometricContext(), this.mAuthenticatorIds);
        getLogger().logUnknownEnrollmentInHal();
        this.mCurrentTask.start(this.mRemoveCallback);
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(ClientMonitorCallback clientMonitorCallback) {
        super.start(clientMonitorCallback);
        List<S> biometricsForUser = this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId());
        this.mCurrentTask = getEnumerateClient(getContext(), this.mLazyDaemon, getToken(), getTargetUserId(), getOwnerString(), biometricsForUser, this.mBiometricUtils, getSensorId(), getLogger(), getBiometricContext());
        Slog.d(TAG, "Starting enumerate: " + this.mCurrentTask + " enrolledList size:" + biometricsForUser.size());
        this.mCurrentTask.start(this.mEnumerateCallback);
    }

    @Override // com.android.server.biometrics.sensors.RemovalConsumer
    public void onRemoved(BiometricAuthenticator.Identifier identifier, int i) {
        BaseClientMonitor baseClientMonitor = this.mCurrentTask;
        if (!(baseClientMonitor instanceof RemovalClient)) {
            Slog.e(TAG, "onRemoved received during client: " + this.mCurrentTask.getClass().getSimpleName());
            return;
        }
        ((RemovalClient) baseClientMonitor).onRemoved(identifier, i);
    }

    @Override // com.android.server.biometrics.sensors.EnrollmentModifier
    public boolean hasEnrollmentStateChanged() {
        return (this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId()).isEmpty() ^ true) != this.mHasEnrollmentsBeforeStarting;
    }

    @Override // com.android.server.biometrics.sensors.EnrollmentModifier
    public boolean hasEnrollments() {
        return !this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId()).isEmpty();
    }

    @Override // com.android.server.biometrics.sensors.EnumerateConsumer
    public void onEnumerationResult(BiometricAuthenticator.Identifier identifier, int i) {
        if (!(this.mCurrentTask instanceof InternalEnumerateClient)) {
            Slog.e(TAG, "onEnumerationResult received during client: " + this.mCurrentTask.getClass().getSimpleName());
            return;
        }
        Slog.d(TAG, "onEnumerated, remaining: " + i);
        ((EnumerateConsumer) this.mCurrentTask).onEnumerationResult(identifier, i);
    }

    public void setFavorHalEnrollments() {
        this.mFavorHalEnrollments = true;
    }

    @VisibleForTesting
    public InternalEnumerateClient<T> getCurrentEnumerateClient() {
        return (InternalEnumerateClient) this.mCurrentTask;
    }

    @VisibleForTesting
    public RemovalClient<S, T> getCurrentRemoveClient() {
        return (RemovalClient) this.mCurrentTask;
    }

    @VisibleForTesting
    public ArrayList<UserTemplate> getUnknownHALTemplates() {
        return this.mUnknownHALTemplates;
    }
}
