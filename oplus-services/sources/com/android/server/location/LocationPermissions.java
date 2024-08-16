package com.android.server.location;

import android.content.Context;
import android.os.Binder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class LocationPermissions {
    public static final int PERMISSION_COARSE = 1;
    public static final int PERMISSION_FINE = 2;
    public static final int PERMISSION_NONE = 0;

    @Target({ElementType.TYPE_USE})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface PermissionLevel {
    }

    public static boolean checkLocationPermission(int i, int i2) {
        return i >= i2;
    }

    public static String asPermission(int i) {
        if (i == 1) {
            return "android.permission.ACCESS_COARSE_LOCATION";
        }
        if (i == 2) {
            return "android.permission.ACCESS_FINE_LOCATION";
        }
        throw new IllegalArgumentException();
    }

    public static int asAppOp(int i) {
        if (i == 1) {
            return 0;
        }
        if (i == 2) {
            return 1;
        }
        throw new IllegalArgumentException();
    }

    public static void enforceCallingOrSelfLocationPermission(Context context, int i) {
        enforceLocationPermission(Binder.getCallingUid(), getPermissionLevel(context, Binder.getCallingUid(), Binder.getCallingPid()), i);
    }

    public static void enforceLocationPermission(Context context, int i, int i2, int i3) {
        enforceLocationPermission(i, getPermissionLevel(context, i, i2), i3);
    }

    public static void enforceLocationPermission(int i, int i2, int i3) {
        if (checkLocationPermission(i2, i3)) {
            return;
        }
        if (i3 != 1) {
            if (i3 != 2) {
                return;
            }
            throw new SecurityException("uid " + i + " does not have android.permission.ACCESS_FINE_LOCATION.");
        }
        throw new SecurityException("uid " + i + " does not have android.permission.ACCESS_COARSE_LOCATION or android.permission.ACCESS_FINE_LOCATION.");
    }

    public static void enforceCallingOrSelfBypassPermission(Context context) {
        enforceBypassPermission(context, Binder.getCallingUid(), Binder.getCallingPid());
    }

    public static void enforceBypassPermission(Context context, int i, int i2) {
        if (context.checkPermission("android.permission.LOCATION_BYPASS", i2, i) == 0) {
            return;
        }
        throw new SecurityException("uid" + i + " does not have android.permission.LOCATION_BYPASS.");
    }

    public static boolean checkCallingOrSelfLocationPermission(Context context, int i) {
        return checkLocationPermission(getCallingOrSelfPermissionLevel(context), i);
    }

    public static boolean checkLocationPermission(Context context, int i, int i2, int i3) {
        return checkLocationPermission(getPermissionLevel(context, i, i2), i3);
    }

    public static int getCallingOrSelfPermissionLevel(Context context) {
        return getPermissionLevel(context, Binder.getCallingUid(), Binder.getCallingPid());
    }

    public static int getPermissionLevel(Context context, int i, int i2) {
        if (context.checkPermission("android.permission.ACCESS_FINE_LOCATION", i2, i) == 0) {
            return 2;
        }
        return context.checkPermission("android.permission.ACCESS_COARSE_LOCATION", i2, i) == 0 ? 1 : 0;
    }

    private LocationPermissions() {
    }
}
