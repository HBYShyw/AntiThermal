package com.oplus.wrapper.provider;

import android.net.Uri;
import android.provider.CallLog;

/* loaded from: classes.dex */
public class CallLog {

    /* loaded from: classes.dex */
    public static class Calls {
        public static final Uri SHADOW_CONTENT_URI = getShadowContentUri();

        private static Uri getShadowContentUri() {
            return CallLog.Calls.SHADOW_CONTENT_URI;
        }
    }
}
