package com.android.id.impl;

import android.app.Activity;
import android.app.ActivityThread;
import android.app.OplusNotificationManager;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class IdProviderImpl {
    private static final String ACTION_REQUEST_OAID_PERMISSION = "com.oplus.omes.REQUEST_OAID_PERMISSION";
    private static final String ANDROID_REQUEST_PERMISSIONS_WHO = "@android:requestPermissions:";
    private static final String CLASSNAME_GRANT_OAID_PERM_ACTIVITY = "com.heytap.openid.oaidcontrolled.view.GrantOAIDPermissionActivity";
    private static final String OMES_CHECK_OAID_PERMISSION_METHOD = "checkSelfOAIDPermission";
    private static final String OMES_CHECK_OAID_RESULT_KEY = "oaidStatus";
    private static final String OMES_GET_OAID_METHOD = "OUID";
    private static final Uri OMES_OAID_STATUS_URI = Uri.parse("content://com.oplus.omes.oaid_status_provider");
    private static final String OPENID_PKG_NAME = "com.heytap.openid";
    private static final int PERMISSION_DENIED = -1;
    private static final int PERMISSION_GRANTED = 0;
    private static final int PERMISSION_NOT_SUPPORT = -2;
    private static final String PERMISSION_OAID_NAME = "com.oplus.permission.OBTAIN_OAID";
    private static final String TAG = "IdProviderImpl";

    public String getStdid(Context context, String type) {
        if (OMES_GET_OAID_METHOD.equals(type)) {
            return getOUIDInner(context);
        }
        OplusNotificationManager onm = new OplusNotificationManager();
        return onm.getStdid(context.getPackageName(), Binder.getCallingUid(), type);
    }

    @Deprecated
    public String getGUID(Context context) {
        return getStdid(context, "GUID");
    }

    @Deprecated
    public String getOUID(Context context) {
        return getStdid(context, OMES_GET_OAID_METHOD);
    }

    @Deprecated
    public String getDUID(Context context) {
        return getStdid(context, "DUID");
    }

    @Deprecated
    public String getAUID(Context context) {
        return getStdid(context, "AUID");
    }

    public String getAPID(Context context) {
        return getStdid(context, "APID");
    }

    @Deprecated
    public boolean checkGetStdid(Context context, String type) {
        OplusNotificationManager onm = new OplusNotificationManager();
        return onm.checkGetStdid(context.getPackageName(), Binder.getCallingUid(), type);
    }

    @Deprecated
    public boolean checkGetGUID(Context context) {
        return checkGetStdid(context, "GUID");
    }

    @Deprecated
    public boolean checkGetAPID(Context context) {
        return checkGetStdid(context, "APID");
    }

    public static int checkSelfOAIDPermission(Context context) {
        try {
            ContentProviderClient client = context.getContentResolver().acquireUnstableContentProviderClient(OMES_OAID_STATUS_URI);
            try {
                if (client == null) {
                    Log.w(TAG, "check oaid failed: client is null.");
                    if (client != null) {
                        client.close();
                    }
                    return -2;
                }
                Bundle result = client.call(OMES_CHECK_OAID_PERMISSION_METHOD, null, null);
                if (result == null) {
                    Log.w(TAG, "check oaid failed: result is null.");
                    if (client != null) {
                        client.close();
                    }
                    return -2;
                }
                int i = result.getInt(OMES_CHECK_OAID_RESULT_KEY, -1);
                if (client != null) {
                    client.close();
                }
                return i;
            } finally {
            }
        } catch (RemoteException e) {
            Log.w(TAG, "check oaid remote exception: " + e.getMessage());
            return -2;
        }
    }

    public static void requestOAIDPermission(Activity activity, int requestCode) {
        Intent intent = new Intent(ACTION_REQUEST_OAID_PERMISSION);
        String[] permissions = {PERMISSION_OAID_NAME};
        intent.putExtra("android.content.pm.extra.REQUEST_PERMISSIONS_NAMES", permissions);
        intent.putExtra("android.intent.extra.UID", Process.myUid());
        intent.setClassName(OPENID_PKG_NAME, CLASSNAME_GRANT_OAID_PERM_ACTIVITY);
        if (activity.getPackageManager().resolveActivity(intent, 65536) == null) {
            Log.w(TAG, "can not resolve oaid activity");
        } else {
            activity.startActivityForResult(ANDROID_REQUEST_PERMISSIONS_WHO, intent, requestCode, null);
        }
    }

    @Deprecated
    public String getOpenid(Context context, String type) {
        return getStdid(context, type);
    }

    @Deprecated
    public boolean checkGetOpenid(Context context, String type) {
        return checkGetStdid(context, type);
    }

    private static String getOUIDInner(Context context) {
        boolean isOpenIDApkCall = isOpenIDApkCalling();
        if (isOpenIDApkCall) {
            OplusNotificationManager onm = new OplusNotificationManager();
            return onm.getStdid(context.getPackageName(), Binder.getCallingUid(), OMES_GET_OAID_METHOD);
        }
        return getOUIDByOpenIDApk(context);
    }

    private static boolean isOpenIDApkCalling() {
        return OPENID_PKG_NAME.equals(ActivityThread.currentPackageName());
    }

    private static String getOUIDByOpenIDApk(Context context) {
        Bundle bundle = new Bundle();
        try {
            ContentProviderClient client = context.getContentResolver().acquireUnstableContentProviderClient(OMES_OAID_STATUS_URI);
            try {
                if (client == null) {
                    Log.w(TAG, "get oaid failed: client is null.");
                    if (client != null) {
                        client.close();
                    }
                    return "";
                }
                Bundle result = client.call(OMES_GET_OAID_METHOD, null, bundle);
                if (result == null) {
                    Log.w(TAG, "get oaid failed: result is null.");
                    if (client != null) {
                        client.close();
                    }
                    return "";
                }
                String string = result.getString(OMES_GET_OAID_METHOD, "");
                if (client != null) {
                    client.close();
                }
                return string;
            } finally {
            }
        } catch (RemoteException e) {
            Log.w(TAG, "get oaid remote exception: " + e.getMessage());
            return "";
        }
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
