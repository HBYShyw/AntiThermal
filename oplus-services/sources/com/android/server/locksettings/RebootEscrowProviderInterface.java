package com.android.server.locksettings;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.crypto.SecretKey;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface RebootEscrowProviderInterface {
    public static final int TYPE_HAL = 0;
    public static final int TYPE_SERVER_BASED = 1;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface RebootEscrowProviderType {
    }

    void clearRebootEscrowKey();

    RebootEscrowKey getAndClearRebootEscrowKey(SecretKey secretKey) throws IOException;

    int getType();

    boolean hasRebootEscrowSupport();

    boolean storeRebootEscrowKey(RebootEscrowKey rebootEscrowKey, SecretKey secretKey);
}
