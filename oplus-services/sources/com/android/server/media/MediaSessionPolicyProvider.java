package com.android.server.media;

import android.content.Context;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class MediaSessionPolicyProvider {
    static final int SESSION_POLICY_IGNORE_BUTTON_RECEIVER = 1;
    static final int SESSION_POLICY_IGNORE_BUTTON_SESSION = 2;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    @interface SessionPolicy {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSessionPoliciesForApplication(int i, String str) {
        return 0;
    }

    public MediaSessionPolicyProvider(Context context) {
    }
}
