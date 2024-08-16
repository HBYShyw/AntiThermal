package com.android.server.locksettings;

import android.content.Context;
import android.os.Binder;
import android.service.gatekeeper.IGateKeeperService;
import com.android.internal.widget.ICheckCredentialProgressCallback;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.VerifyCredentialResponse;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ILockSettingsServiceExt {
    default void ensureMigrateMultiAppUserLockKeys() {
    }

    default IGateKeeperService getGateKeeperService() {
        return null;
    }

    default long getSyntheticPasswordHandle(int i) {
        return 0L;
    }

    default boolean hookCheckOnePlusMultiAppUser(int i) {
        return false;
    }

    default void hookOnStart() {
    }

    default void hookOnSystemReady() {
    }

    default boolean hookShouldUnlockProfile(int i) {
        return true;
    }

    default boolean hooktieManagedProfileLockIfNecessary(int i, LockscreenCredential lockscreenCredential) {
        return false;
    }

    default void init(SyntheticPasswordManager syntheticPasswordManager, Context context, LockSettingsStorage lockSettingsStorage) {
    }

    default boolean isOplusMultiAppUserId(int i) {
        return false;
    }

    default boolean isSyntheticPasswordBasedCredential(int i) {
        return false;
    }

    default void notifyCredentialVerified(ICheckCredentialProgressCallback iCheckCredentialProgressCallback) {
    }

    default void notifyPasswordChanged(LockscreenCredential lockscreenCredential, int i) {
    }

    default void notifyPasswordDerivation(LockscreenCredential lockscreenCredential, int i) {
    }

    default void notifyVoldDecryptAEKey(int i, byte[] bArr, byte[] bArr2) {
    }

    default void resetTimeoutFlag(VerifyCredentialResponse verifyCredentialResponse) {
    }

    default void setBinderExtension(Binder binder) {
    }

    default boolean setLockCredential(LockscreenCredential lockscreenCredential, LockscreenCredential lockscreenCredential2, int i) {
        return false;
    }

    default void setLong(String str, long j, int i) {
    }

    default void tryRemoveLockscreenCredentialForMultiApp(int i, boolean z) {
    }

    default void writeSecretToTee(VerifyCredentialResponse verifyCredentialResponse, LockscreenCredential lockscreenCredential, int i, int i2) {
    }
}
