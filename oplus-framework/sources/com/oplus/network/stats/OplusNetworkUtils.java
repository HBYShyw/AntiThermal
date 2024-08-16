package com.oplus.network.stats;

import android.os.SystemProperties;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/* loaded from: classes.dex */
public class OplusNetworkUtils {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    private static final String TAG = "NetworkUtils";

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            char[] cArr = HEX_ARRAY;
            hexChars[j * 2] = cArr[v >>> 4];
            hexChars[(j * 2) + 1] = cArr[v & 15];
        }
        return new String(hexChars);
    }

    public static String getHashPackageName(String appName) {
        byte[] hashVersion;
        String androidVersion = SystemProperties.get("ro.build.version.release", "null");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            if (digest != null && (hashVersion = digest.digest(androidVersion.getBytes(StandardCharsets.UTF_8))) != null && hashVersion.length > 0) {
                String versionStrHash = bytesToHex(hashVersion);
                byte[] hash = digest.digest((appName + versionStrHash).getBytes(StandardCharsets.UTF_8));
                if (hash != null && hash.length > 0) {
                    return bytesToHex(hash);
                }
                return "";
            }
            return "";
        } catch (Exception e) {
            Log.e(TAG, "CatchException getHashPackageName():" + e.toString());
            return "";
        }
    }
}
