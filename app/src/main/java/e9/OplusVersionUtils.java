package e9;

import android.util.Log;
import com.oplus.os.OplusBuild;

/* compiled from: OplusVersionUtils.java */
/* renamed from: e9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class OplusVersionUtils {

    /* renamed from: a, reason: collision with root package name */
    public static final boolean f10973a = a();

    private static boolean a() {
        try {
            return OplusBuild.getOplusOSVERSION() >= ((Integer) Class.forName("com.oplus.os.OplusBuild").getField("OplusOS_11_3").get(null)).intValue();
        } catch (Exception e10) {
            Log.w("OplusVersionUtils", "isOsVersion_11_3: " + e10.toString());
            return false;
        }
    }
}
