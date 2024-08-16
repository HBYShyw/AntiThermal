package com.android.server.locksettings;

import android.hardware.rebootescrow.IRebootEscrow;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.util.NoSuchElementException;
import javax.crypto.SecretKey;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class RebootEscrowProviderHalImpl implements RebootEscrowProviderInterface {
    private static final String TAG = "RebootEscrowProviderHal";
    private final Injector mInjector;

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public int getType() {
        return 0;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class Injector {
        Injector() {
        }

        public IRebootEscrow getRebootEscrow() {
            try {
                return IRebootEscrow.Stub.asInterface(ServiceManager.getService("android.hardware.rebootescrow.IRebootEscrow/default"));
            } catch (NoSuchElementException unused) {
                Slog.i(RebootEscrowProviderHalImpl.TAG, "Device doesn't implement RebootEscrow HAL");
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RebootEscrowProviderHalImpl() {
        this.mInjector = new Injector();
    }

    @VisibleForTesting
    RebootEscrowProviderHalImpl(Injector injector) {
        this.mInjector = injector;
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public boolean hasRebootEscrowSupport() {
        return this.mInjector.getRebootEscrow() != null;
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public RebootEscrowKey getAndClearRebootEscrowKey(SecretKey secretKey) {
        IRebootEscrow rebootEscrow = this.mInjector.getRebootEscrow();
        if (rebootEscrow == null) {
            Slog.w(TAG, "Had reboot escrow data for users, but RebootEscrow HAL is unavailable");
            return null;
        }
        try {
            byte[] retrieveKey = rebootEscrow.retrieveKey();
            if (retrieveKey == null) {
                Slog.w(TAG, "Had reboot escrow data for users, but could not retrieve key");
                return null;
            }
            if (retrieveKey.length != 32) {
                Slog.e(TAG, "IRebootEscrow returned key of incorrect size " + retrieveKey.length);
                return null;
            }
            int i = 0;
            for (byte b : retrieveKey) {
                i |= b;
            }
            if (i == 0) {
                Slog.w(TAG, "IRebootEscrow returned an all-zeroes key");
                return null;
            }
            rebootEscrow.storeKey(new byte[32]);
            return RebootEscrowKey.fromKeyBytes(retrieveKey);
        } catch (ServiceSpecificException e) {
            Slog.w(TAG, "Got service-specific exception: " + e.errorCode);
            return null;
        } catch (RemoteException unused) {
            Slog.w(TAG, "Could not retrieve escrow data");
            return null;
        }
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public void clearRebootEscrowKey() {
        IRebootEscrow rebootEscrow = this.mInjector.getRebootEscrow();
        if (rebootEscrow == null) {
            return;
        }
        try {
            rebootEscrow.storeKey(new byte[32]);
        } catch (RemoteException | ServiceSpecificException unused) {
            Slog.w(TAG, "Could not call RebootEscrow HAL to shred key");
        }
    }

    @Override // com.android.server.locksettings.RebootEscrowProviderInterface
    public boolean storeRebootEscrowKey(RebootEscrowKey rebootEscrowKey, SecretKey secretKey) {
        IRebootEscrow rebootEscrow = this.mInjector.getRebootEscrow();
        if (rebootEscrow == null) {
            Slog.w(TAG, "Escrow marked as ready, but RebootEscrow HAL is unavailable");
            return false;
        }
        try {
            rebootEscrow.storeKey(rebootEscrowKey.getKeyBytes());
            Slog.i(TAG, "Reboot escrow key stored with RebootEscrow HAL");
            return true;
        } catch (RemoteException | ServiceSpecificException e) {
            Slog.e(TAG, "Failed escrow secret to RebootEscrow HAL", e);
            return false;
        }
    }
}
