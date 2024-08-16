package com.oplus.oms.split.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import com.oplus.exfunction.ExFunctionManager;
import com.oplus.oms.split.signature.D;
import com.oplus.oms.split.signature.G;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class SignatureValidator {
    private static List<String> DEFAULT_OPLUS_PLUGIN_SIGNATURE_LIST = Collections.singletonList("B0:A9:BB:FC:05:EE:E5:E7:D0:A2:C9:7C:03:05:86:E1:5B:B3:30:11:52:07:8F:54:47:3B:B8:2D:F6:D8:C8:18");
    private static final int INDEX_TWO = 2;
    private static final String TAG = "SignatureValidator";

    private SignatureValidator() {
    }

    public static boolean validateSplit(Context context, File splitFile) {
        Signature[] signatures;
        if (context == null || FileUtil.fileNotExists(splitFile) || (signatures = getAppSignature(context)) == null || signatures.length == 0) {
            return false;
        }
        ArrayList<X509Certificate> signatureList = new ArrayList<>();
        for (Signature signature : signatures) {
            X509Certificate x509Certificate = decodeCertificate(signature);
            if (x509Certificate != null) {
                signatureList.add(x509Certificate);
            }
        }
        if (!signatureList.isEmpty() && a(splitFile.getAbsolutePath(), signatureList)) {
            return true;
        }
        String splitApkSha256 = getSplitApkSignaturesSHA256(splitFile.getAbsolutePath(), "SHA256");
        if (TextUtils.isEmpty(splitApkSha256)) {
            SplitLog.i(TAG, "get split apk sha256 error", new Object[0]);
            return false;
        }
        if (!DEFAULT_OPLUS_PLUGIN_SIGNATURE_LIST.contains(splitApkSha256)) {
            return false;
        }
        SplitLog.d(TAG, "the split apk use default oplus signature", new Object[0]);
        return true;
    }

    private static String getSplitApkSignaturesSHA256(String var1, String algorithm) {
        try {
            X509Certificate[][] var3 = G.a(var1);
            if (var3 == null || var3.length == 0) {
                return null;
            }
            try {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                byte[] publicKey = md.digest(var3[0][0].getEncoded());
                String hexString = byte2HexFormatted(publicKey);
                return hexString;
            } catch (NoSuchAlgorithmException var8) {
                var8.printStackTrace();
                return "";
            } catch (CertificateEncodingException var9) {
                var9.printStackTrace();
                return "";
            }
        } catch (D | IOException var14) {
            SplitLog.e(TAG, "split " + var1 + " is not signed.", var14);
            return null;
        }
    }

    private static boolean a(String var1, List<X509Certificate> var2) {
        try {
            X509Certificate[][] var3 = G.a(var1);
            if (var3 == null || var3.length == 0 || var3[0].length == 0) {
                SplitLog.e(TAG, "Downloaded split " + var1 + " is not signed.", new Object[0]);
                return false;
            }
            if (var2.isEmpty()) {
                SplitLog.e(TAG, "No certificates found for app.", new Object[0]);
                return false;
            }
            for (X509Certificate var8 : var2) {
                boolean var9 = false;
                int var11 = var3.length;
                int var12 = 0;
                while (true) {
                    if (var12 >= var11) {
                        break;
                    }
                    if (!var3[var12][0].equals(var8)) {
                        var12++;
                    } else {
                        var9 = true;
                        break;
                    }
                }
                if (!var9) {
                    SplitLog.d(TAG, "There's an app certificate that doesn't sign the split.", new Object[0]);
                    return false;
                }
            }
            return true;
        } catch (Exception var14) {
            SplitLog.e(TAG, "split " + var1 + " is not signed.", var14);
            return false;
        }
    }

    private static Signature[] getAppSignature(Context context) {
        if (context == null) {
            return new Signature[0];
        }
        PackageManager packageManager = context.getPackageManager();
        String name = context.getPackageName();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(name, ExFunctionManager.USER_FLAG_REPAIR_MODE);
            return packageInfo.signingInfo.getApkContentsSigners();
        } catch (PackageManager.NameNotFoundException e) {
            SplitLog.w(TAG, "getAppSignature NameNotFoundException", new Object[0]);
            return new Signature[0];
        }
    }

    private static X509Certificate decodeCertificate(Signature var0) {
        if (var0 == null) {
            return null;
        }
        try {
            return (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(var0.toByteArray()));
        } catch (CertificateException var2) {
            SplitLog.e(TAG, "Cannot decode certificate.", var2);
            return null;
        }
    }

    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) {
                h = "0" + h;
            }
            if (l > 2) {
                h = h.substring(l - 2, l);
            }
            str.append(h.toUpperCase());
            if (i < arr.length - 1) {
                str.append(':');
            }
        }
        return str.toString();
    }
}
