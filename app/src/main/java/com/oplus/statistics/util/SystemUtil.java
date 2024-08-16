package com.oplus.statistics.util;

import android.content.Context;
import android.os.UserManager;

/* loaded from: classes2.dex */
public class SystemUtil {
    public static boolean isSystemUser(Context context) {
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        return userManager != null && userManager.isSystemUser();
    }
}
