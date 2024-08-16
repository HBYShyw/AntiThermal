package com.android.server.locksettings;

import android.service.gatekeeper.IGateKeeperService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ILockSettingsServiceWrapper {
    default IGateKeeperService getGateKeeperService() {
        return null;
    }

    default boolean hasUnifiedChallenge(int i) {
        return false;
    }

    default boolean isSyntheticPasswordBasedCredentialLocked(int i) {
        return false;
    }

    default boolean migrateProfileLockKeys() {
        return false;
    }
}
