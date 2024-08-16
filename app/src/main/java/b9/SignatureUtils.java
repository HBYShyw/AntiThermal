package b9;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import java.nio.charset.Charset;
import java.security.MessageDigest;

/* compiled from: SignatureUtils.java */
/* renamed from: b9.e, reason: use source file name */
/* loaded from: classes2.dex */
public class SignatureUtils {

    /* renamed from: a, reason: collision with root package name */
    private static Charset f4623a = Charset.forName("UTF-8");

    private static Signature[] a(Context context, String str) {
        if (str != null && str.length() != 0) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 64);
                if (packageInfo == null) {
                    return null;
                }
                return packageInfo.signatures;
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return null;
    }

    public static String b(Context context, String str) {
        Signature[] a10 = a(context, str);
        return (a10 == null || a10.length == 0) ? "" : c(a10[0].toCharsString());
    }

    private static String c(String str) {
        try {
            return d(MessageDigest.getInstance("SHA-256").digest(str.getBytes(f4623a)));
        } catch (Exception unused) {
            return "";
        }
    }

    private static String d(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (int i10 = 0; i10 < bArr.length; i10++) {
            stringBuffer.append(Character.forDigit((bArr[i10] & 240) >> 4, 16));
            stringBuffer.append(Character.forDigit(bArr[i10] & 15, 16));
        }
        return stringBuffer.toString();
    }
}
