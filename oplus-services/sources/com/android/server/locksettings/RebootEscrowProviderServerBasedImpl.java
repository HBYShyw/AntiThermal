package com.android.server.locksettings;

import android.content.Context;
import android.os.RemoteException;
import android.provider.DeviceConfig;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.locksettings.ResumeOnRebootServiceProvider;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.crypto.SecretKey;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class RebootEscrowProviderServerBasedImpl implements RebootEscrowProviderInterface {
    private static final long DEFAULT_SERVER_BLOB_LIFETIME_IN_MILLIS = 600000;
    private static final long DEFAULT_SERVICE_TIMEOUT_IN_SECONDS = 10;
    private static final String TAG = "RebootEscrowProviderServerBased";
    private final Injector mInjector;
    private byte[] mServerBlob;
    private final LockSettingsStorage mStorage;

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public int getType() {
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        private ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection mServiceConnection;

        Injector(Context context) {
            this.mServiceConnection = null;
            ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection serviceConnection = new ResumeOnRebootServiceProvider(context).getServiceConnection();
            this.mServiceConnection = serviceConnection;
            if (serviceConnection == null) {
                Slog.e(RebootEscrowProviderServerBasedImpl.TAG, "Failed to resolve resume on reboot server service.");
            }
        }

        Injector(ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection resumeOnRebootServiceConnection) {
            this.mServiceConnection = resumeOnRebootServiceConnection;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection getServiceConnection() {
            return this.mServiceConnection;
        }

        long getServiceTimeoutInSeconds() {
            return DeviceConfig.getLong("ota", "server_based_service_timeout_in_seconds", RebootEscrowProviderServerBasedImpl.DEFAULT_SERVICE_TIMEOUT_IN_SECONDS);
        }

        long getServerBlobLifetimeInMillis() {
            return DeviceConfig.getLong("ota", "server_based_server_blob_lifetime_in_millis", 600000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RebootEscrowProviderServerBasedImpl(Context context, LockSettingsStorage lockSettingsStorage) {
        this(lockSettingsStorage, new Injector(context));
    }

    @VisibleForTesting
    RebootEscrowProviderServerBasedImpl(LockSettingsStorage lockSettingsStorage, Injector injector) {
        this.mStorage = lockSettingsStorage;
        this.mInjector = injector;
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public boolean hasRebootEscrowSupport() {
        return this.mInjector.getServiceConnection() != null;
    }

    private byte[] unwrapServerBlob(byte[] bArr, SecretKey secretKey) throws TimeoutException, RemoteException, IOException {
        ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection serviceConnection = this.mInjector.getServiceConnection();
        if (serviceConnection == null) {
            Slog.w(TAG, "Had reboot escrow data for users, but resume on reboot server service is unavailable");
            return null;
        }
        byte[] decrypt = AesEncryptionUtil.decrypt(secretKey, bArr);
        if (decrypt == null) {
            Slog.w(TAG, "Decrypted server blob should not be null");
            return null;
        }
        serviceConnection.bindToService(this.mInjector.getServiceTimeoutInSeconds());
        byte[] unwrap = serviceConnection.unwrap(decrypt, this.mInjector.getServiceTimeoutInSeconds());
        serviceConnection.unbindService();
        return unwrap;
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public RebootEscrowKey getAndClearRebootEscrowKey(SecretKey secretKey) throws IOException {
        if (this.mServerBlob == null) {
            this.mServerBlob = this.mStorage.readRebootEscrowServerBlob();
        }
        this.mStorage.removeRebootEscrowServerBlob();
        if (this.mServerBlob == null) {
            Slog.w(TAG, "Failed to read reboot escrow server blob from storage");
            return null;
        }
        if (secretKey == null) {
            Slog.w(TAG, "Failed to decrypt the escrow key; decryption key from keystore is null.");
            return null;
        }
        Slog.i(TAG, "Loaded reboot escrow server blob from storage");
        try {
            byte[] unwrapServerBlob = unwrapServerBlob(this.mServerBlob, secretKey);
            if (unwrapServerBlob == null) {
                Slog.w(TAG, "Decrypted reboot escrow key bytes should not be null");
                return null;
            }
            if (unwrapServerBlob.length != 32) {
                Slog.e(TAG, "Decrypted reboot escrow key has incorrect size " + unwrapServerBlob.length);
                return null;
            }
            return RebootEscrowKey.fromKeyBytes(unwrapServerBlob);
        } catch (RemoteException | TimeoutException e) {
            Slog.w(TAG, "Failed to decrypt the server blob ", e);
            return null;
        }
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public void clearRebootEscrowKey() {
        this.mStorage.removeRebootEscrowServerBlob();
    }

    private byte[] wrapEscrowKey(byte[] bArr, SecretKey secretKey) throws TimeoutException, RemoteException, IOException {
        ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection serviceConnection = this.mInjector.getServiceConnection();
        if (serviceConnection == null) {
            Slog.w(TAG, "Failed to encrypt the reboot escrow key: resume on reboot server service is unavailable");
            return null;
        }
        serviceConnection.bindToService(this.mInjector.getServiceTimeoutInSeconds());
        byte[] wrapBlob = serviceConnection.wrapBlob(bArr, this.mInjector.getServerBlobLifetimeInMillis(), this.mInjector.getServiceTimeoutInSeconds());
        serviceConnection.unbindService();
        if (wrapBlob == null) {
            Slog.w(TAG, "Server encrypted reboot escrow key cannot be null");
            return null;
        }
        return AesEncryptionUtil.encrypt(secretKey, wrapBlob);
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public boolean storeRebootEscrowKey(RebootEscrowKey rebootEscrowKey, SecretKey secretKey) {
        this.mStorage.removeRebootEscrowServerBlob();
        try {
            byte[] wrapEscrowKey = wrapEscrowKey(rebootEscrowKey.getKeyBytes(), secretKey);
            if (wrapEscrowKey == null) {
                Slog.w(TAG, "Failed to encrypt the reboot escrow key");
                return false;
            }
            this.mStorage.writeRebootEscrowServerBlob(wrapEscrowKey);
            Slog.i(TAG, "Reboot escrow key encrypted and stored.");
            return true;
        } catch (RemoteException | IOException | TimeoutException e) {
            Slog.w(TAG, "Failed to encrypt the reboot escrow key ", e);
            return false;
        }
    }
}
