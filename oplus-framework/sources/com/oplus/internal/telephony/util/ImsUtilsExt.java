package com.oplus.internal.telephony.util;

import android.content.Context;
import com.android.internal.telephony.util.QtiImsUtils;

/* loaded from: classes.dex */
public class ImsUtilsExt {
    public static final int CRS_TYPE_AUDIO = 1;
    public static final int CRS_TYPE_INVALID = 0;
    public static final int CRS_TYPE_VIDEO = 2;
    public static final String EXTRA_PHONE_ID = "phoneId";

    public static boolean isSimLessRttSupported(int phoneId, Context context) {
        return QtiImsUtils.isSimLessRttSupported(phoneId, context);
    }

    public static int getVideoServiceClass() {
        return 512;
    }
}
