package com.android.server.location.gnss;

import android.content.Context;
import android.location.GnssStatus;
import android.os.Bundle;
import android.os.Handler;
import com.android.server.location.gnss.hal.GnssNative;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IGnssLocationProviderSocExt {
    default void init(Context context, Handler handler, NtpNetworkTimeHelper ntpNetworkTimeHelper) {
    }

    default int onDeleteAidingData(Bundle bundle, int i) {
        return i;
    }

    default void onGnssLocationProviderInitialize() {
    }

    default void onReportSvStatus(GnssStatus gnssStatus) {
    }

    default void onRequestLocation(GnssNative gnssNative) {
    }
}
