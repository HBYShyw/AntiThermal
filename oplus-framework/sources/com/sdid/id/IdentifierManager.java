package com.sdid.id;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import com.android.id.impl.IdProviderImpl;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class IdentifierManager {
    private static final String APPLICATION_ANONYMOUS_ID = "AUID";
    private static final String KEY_PKG_NAME = "pkgName";
    private static final String KEY_RESULT = "result";
    private static final String KEY_SIGNATURE = "signature";
    private static final String MEHTOD = "getDUID";
    private static final String OPEN_ANONYMOUS_ID = "OUID";
    private static final int OPEN_ANONYMOUS_ID_AVAILABLE = 1;
    private static final int OPEN_ANONYMOUS_ID_DISAVAILABLE = 0;
    private static final String OPEN_ANONYMOUS_ID_SETTING_KEY = "openid_toggle";
    private static final String UNIQUE_DEVICE_ID = "GUID";
    private static final String VENDER_ANONYMOUS_ID = "DUID";
    private static final Uri DUID_URI = Uri.parse("content://com.oplus.omes.ids_provider");
    private static final IdProviderImpl sProvider = new IdProviderImpl();

    public static boolean isSupported() {
        return true;
    }

    public static boolean isLimited(Context context) {
        return isLimitedInner(context);
    }

    public static String getUDID(Context context) {
        return sProvider.getStdid(context, UNIQUE_DEVICE_ID);
    }

    public static String getOAID(Context context) {
        return sProvider.getStdid(context, OPEN_ANONYMOUS_ID);
    }

    public static String getVAID(Context context) {
        return getVAIDInner(context);
    }

    public static String getAAID(Context context) {
        return sProvider.getStdid(context, APPLICATION_ANONYMOUS_ID);
    }

    private static boolean isLimitedInner(Context context) {
        if (context == null) {
            return true;
        }
        int selfOAIDStatus = IdProviderImpl.checkSelfOAIDPermission(context);
        if (selfOAIDStatus != -2) {
            return selfOAIDStatus != 0;
        }
        int userId = context.getUserId();
        return Settings.Secure.getIntForUser(context.getContentResolver(), OPEN_ANONYMOUS_ID_SETTING_KEY, 1, userId) == 0;
    }

    private static String getVAIDInner(Context context) {
        ContentProviderClient client;
        String duid = "";
        Bundle params = new Bundle();
        params.putString("pkgName", context.getPackageName());
        params.putString(KEY_SIGNATURE, getSingInfo(context));
        try {
            client = context.getContentResolver().acquireUnstableContentProviderClient(DUID_URI);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (client != null) {
            try {
                Bundle result = client.call(MEHTOD, null, params);
                duid = result != null ? result.getString("result") : "";
                if (client != null) {
                    client.close();
                }
                return duid;
            } finally {
            }
        }
        if (client != null) {
            client.close();
        }
        return "";
    }

    private static String getSingInfo(Context context) {
        Signature[] signs = getSignatures(context, context.getPackageName());
        if (signs == null) {
            return null;
        }
        String tmp = getSignatureString(signs[0], "SHA1");
        return tmp;
    }

    private static Signature[] getSignatures(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 64);
            return packageInfo.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private static String getSignatureString(Signature sig, String type) {
        byte[] hexBytes = sig.toByteArray();
        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            if (digest == null) {
                return null;
            }
            byte[] digestBytes = digest.digest(hexBytes);
            StringBuilder sb = new StringBuilder();
            for (byte digestByte : digestBytes) {
                sb.append(Integer.toHexString((digestByte & 255) | 256).substring(1, 3));
            }
            String fingerprint = sb.toString();
            return fingerprint;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
