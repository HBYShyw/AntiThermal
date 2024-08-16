package l9;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: NumberUtils.java */
/* renamed from: l9.g, reason: use source file name */
/* loaded from: classes2.dex */
public class NumberUtils {
    public static double a(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e10) {
            LogUtils.a("NumberUtils", str + " parse failed, " + e10);
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
    }

    public static int b(String str, int i10) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e10) {
            LogUtils.a("NumberUtils", str + " parse failed, " + e10);
            return i10;
        }
    }

    public static long c(String str, long j10) {
        try {
            return Long.parseLong(str);
        } catch (Exception e10) {
            LogUtils.a("NumberUtils", str + " parse long failed, " + e10);
            return j10;
        }
    }
}
