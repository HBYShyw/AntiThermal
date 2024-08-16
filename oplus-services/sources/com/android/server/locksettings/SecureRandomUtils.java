package com.android.server.locksettings;

import java.security.SecureRandom;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SecureRandomUtils {
    private static final SecureRandom RNG = new SecureRandom();

    public static byte[] randomBytes(int i) {
        byte[] bArr = new byte[i];
        RNG.nextBytes(bArr);
        return bArr;
    }

    public static long randomLong() {
        return RNG.nextLong();
    }
}
