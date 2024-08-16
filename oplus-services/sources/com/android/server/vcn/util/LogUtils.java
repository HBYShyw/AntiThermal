package com.android.server.vcn.util;

import android.os.ParcelUuid;
import com.android.internal.util.HexDump;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LogUtils {
    public static String getHashedSubscriptionGroup(ParcelUuid parcelUuid) {
        if (parcelUuid == null) {
            return null;
        }
        return HexDump.toHexString(parcelUuid.hashCode());
    }
}
