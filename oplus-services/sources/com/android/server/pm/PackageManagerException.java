package com.android.server.pm;

import com.android.server.pm.Installer;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageManagerException extends Exception {
    public static final int INTERNAL_ERROR_APEX_MORE_THAN_ONE_FILE = -37;
    public static final int INTERNAL_ERROR_APEX_NOT_DIRECTORY = -36;
    public static final int INTERNAL_ERROR_DECOMPRESS_STUB = -11;
    public static final int INTERNAL_ERROR_DERIVING_ABI = -4;
    public static final int INTERNAL_ERROR_DUP_STATIC_SHARED_LIB_PROVIDER = -13;
    public static final int INTERNAL_ERROR_INSTALL_MISSING_CHILD_SESSIONS = -20;
    public static final int INTERNAL_ERROR_MISSING_SETTING_FOR_MOVE = -3;
    public static final int INTERNAL_ERROR_MOVE = -2;
    public static final int INTERNAL_ERROR_NATIVE_LIBRARY_COPY = -1;
    public static final int INTERNAL_ERROR_NOT_PRIV_SHARED_USER = -19;
    public static final int INTERNAL_ERROR_OVERLAY_LOW_TARGET_SDK = -16;
    public static final int INTERNAL_ERROR_OVERLAY_SIGNATURE1 = -17;
    public static final int INTERNAL_ERROR_OVERLAY_SIGNATURE2 = -18;
    public static final int INTERNAL_ERROR_SHARED_LIB_INSTALLED_TWICE = -6;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_ACTIVITY = -27;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_BROADCAST_RECEIVER = -30;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_CONTENT_PROVIDER = -29;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_DYNAMIC = -25;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_FEATURE = -32;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_INSTANT = -23;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_LOW_SDK = -22;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_OVERLAY_TARGETS = -35;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_PERMISSION = -33;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_PERMISSION_GROUP = -31;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_PROTECTED_BROADCAST = -34;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_RENAMED = -24;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_SERVICE = -28;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_SHARED_USER = -26;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_VERSION_CODES_ORDER = -14;
    public static final int INTERNAL_ERROR_STORAGE_INVALID_NOT_INSTALLED_FOR_USER = -9;
    public static final int INTERNAL_ERROR_STORAGE_INVALID_PACKAGE_UNKNOWN = -7;
    public static final int INTERNAL_ERROR_STORAGE_INVALID_SHOULD_NOT_HAVE_STORAGE = -10;
    public static final int INTERNAL_ERROR_STORAGE_INVALID_VOLUME_UNKNOWN = -8;
    public static final int INTERNAL_ERROR_SYSTEM_OVERLAY_STATIC = -15;
    public static final int INTERNAL_ERROR_UPDATED_VERSION_BETTER_THAN_SYSTEM = -12;
    public static final int INTERNAL_ERROR_VERIFY_MISSING_CHILD_SESSIONS = -21;
    public static final int INTERNAL_ERROR_VERITY_SETUP = -5;
    public final int error;
    public final int internalErrorCode;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface InternalErrorCode {
    }

    public static PackageManagerException ofInternalError(String str, int i) {
        return new PackageManagerException(-110, str, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PackageManagerException(int i, String str, int i2) {
        super(str);
        this.error = i;
        this.internalErrorCode = i2;
    }

    public PackageManagerException(int i, String str) {
        super(str);
        this.error = i;
        this.internalErrorCode = 0;
    }

    public PackageManagerException(int i, String str, Throwable th) {
        super(str, th);
        this.error = i;
        this.internalErrorCode = 0;
    }

    public PackageManagerException(Throwable th) {
        super(th);
        this.error = -110;
        this.internalErrorCode = 0;
    }

    public static PackageManagerException from(Installer.InstallerException installerException) throws PackageManagerException {
        throw new PackageManagerException(-110, installerException.getMessage(), installerException.getCause());
    }
}
