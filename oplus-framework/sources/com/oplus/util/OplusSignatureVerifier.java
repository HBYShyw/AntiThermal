package com.oplus.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Binder;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.collect.Sets;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class OplusSignatureVerifier {
    private static final String TAG = "SignatureVerifier";
    private static OplusSignatureUpdater mColorSignatureUpdater;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static Set<String> sWhiteList = Sets.newHashSet();

    public static void initUpdater(OplusSignatureUpdater updater) {
        mColorSignatureUpdater = updater;
    }

    public static boolean verificaionPass(Context context) {
        int uid = Binder.getCallingUid();
        String packageName = getPackageForUid(context, uid);
        if (DEBUG) {
            Log.d(TAG, "verificaionPass packageName:" + packageName);
        }
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        if (sWhiteList.contains(packageName)) {
            Log.d(TAG, "verificaionPass sWhiteList contain " + packageName);
            return true;
        }
        if (isSystemApp(context, packageName)) {
            sWhiteList.add(packageName);
            Log.d(TAG, "verificaionPass isSystemApp sWhiteList add " + packageName);
            return true;
        }
        return verifySignature(context, packageName);
    }

    private static boolean isSystemApp(Context context, String packageName) {
        ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if ((info.flags & 1) == 0 && (info.flags & 128) == 0) {
            return false;
        }
        Log.d(TAG, "isSystemApp true");
        return true;
    }

    private static boolean verifySignature(Context context, String packageName) {
        String signature = getMD5Signature(context, packageName);
        OplusSignatureUpdater oplusSignatureUpdater = mColorSignatureUpdater;
        if (oplusSignatureUpdater == null) {
            Log.e(TAG, "should initUpdater first");
            return false;
        }
        List<String> signatures = oplusSignatureUpdater.getSignatures();
        if (!signatures.contains(signature)) {
            return false;
        }
        Log.d(TAG, "verifySignature contains signature:" + signature);
        sWhiteList.add(packageName);
        return true;
    }

    public static String getMD5Signature(Context context, String packageName) {
        Signature[] signatures = getSignatures(context, packageName);
        if (signatures == null) {
            Log.w(TAG, "sigutures is null");
            return null;
        }
        byte[] cert = signatures[0].toByteArray();
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            String md5HexString = null;
            try {
                X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(cert));
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] publicKey = md.digest(certificate.getEncoded());
                    StringBuilder sb = new StringBuilder();
                    for (byte digestByte : publicKey) {
                        sb.append(Integer.toHexString((digestByte & 255) | 256).substring(1, 3));
                    }
                    md5HexString = sb.toString();
                    Log.d(TAG, "getMD5Signature -- md5HexString = " + md5HexString);
                    return md5HexString;
                } catch (Exception e) {
                    Log.w(TAG, "getMD5Signature -- 3 error: " + e);
                    return md5HexString;
                }
            } catch (CertificateException e2) {
                Log.w(TAG, "getMD5Signature -- 2 error: " + e2);
                return null;
            }
        } catch (CertificateException e3) {
            Log.w(TAG, "getMD5Signature -- 1 error: " + e3);
            return null;
        }
    }

    private static Signature[] getSignatures(Context context, String packageName) {
        if (context == null || packageName == null) {
            Log.w(TAG, "getSignatures packageName is null");
            return null;
        }
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 64);
            if (packageInfo == null) {
                Log.w(TAG, "getSignatures packageInfo is null");
                return null;
            }
            return packageInfo.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "getSignatures error: " + e);
            return null;
        }
    }

    private static String getPackageForUid(Context context, int uid) {
        String[] packages;
        PackageManager pm = context.getPackageManager();
        if (pm == null || (packages = pm.getPackagesForUid(uid)) == null || packages.length == 0) {
            return null;
        }
        return packages[0];
    }
}
