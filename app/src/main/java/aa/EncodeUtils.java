package aa;

import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.LruCache;
import b6.LocalLog;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/* compiled from: EncodeUtils.java */
/* renamed from: aa.b, reason: use source file name */
/* loaded from: classes2.dex */
public class EncodeUtils {

    /* renamed from: a, reason: collision with root package name */
    private static final char[] f120a = "0123456789abcdef".toCharArray();

    /* renamed from: b, reason: collision with root package name */
    private static final String f121b = SystemProperties.get("ro.build.version.release", "null");

    /* renamed from: c, reason: collision with root package name */
    private static LruCache<String, String> f122c = new LruCache<>(200);

    private static String a(byte[] bArr) {
        char[] cArr = new char[bArr.length * 2];
        for (int i10 = 0; i10 < bArr.length; i10++) {
            int i11 = bArr[i10] & 255;
            int i12 = i10 * 2;
            char[] cArr2 = f120a;
            cArr[i12] = cArr2[i11 >>> 4];
            cArr[i12 + 1] = cArr2[i11 & 15];
        }
        return new String(cArr);
    }

    public static String b(String str) {
        byte[] digest;
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String str2 = f122c.get(str);
        if (!TextUtils.isEmpty(str2)) {
            return str2;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            if (messageDigest == null || (digest = messageDigest.digest(f121b.getBytes(StandardCharsets.UTF_8))) == null || digest.length <= 0) {
                return "";
            }
            byte[] digest2 = messageDigest.digest((str + a(digest)).getBytes(StandardCharsets.UTF_8));
            if (digest2 == null || digest2.length <= 0) {
                return "";
            }
            String a10 = a(digest2);
            f122c.put(str, a10);
            return a10;
        } catch (Exception e10) {
            LocalLog.b("EncodeUtils", "CatchException getHashPackageName():" + e10.toString());
            return "";
        }
    }
}
