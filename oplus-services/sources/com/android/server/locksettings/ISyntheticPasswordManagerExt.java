package com.android.server.locksettings;

import android.content.Context;
import com.android.internal.widget.LockscreenCredential;
import com.android.server.locksettings.SyntheticPasswordManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ISyntheticPasswordManagerExt {
    default boolean isBootFromOTA() {
        return false;
    }

    default boolean isMemoryLow() {
        return false;
    }

    default boolean updateCreateParam(Context context, byte[] bArr, SyntheticPasswordManager.PasswordData passwordData, int i, long j, int i2) {
        return false;
    }

    default boolean updateVerifyParam(Context context, LockscreenCredential lockscreenCredential, SyntheticPasswordManager.PasswordData passwordData, int i, long j, LockSettingsStorage lockSettingsStorage) {
        return false;
    }
}
