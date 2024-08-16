package com.android.server.pm.pkg;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SELinuxUtil {
    public static final String COMPLETE_STR = ":complete";
    private static final String INSTANT_APP_STR = ":ephemeralapp";

    public static String getSeinfoUser(PackageUserState packageUserState) {
        return packageUserState.isInstantApp() ? ":ephemeralapp:complete" : COMPLETE_STR;
    }
}
