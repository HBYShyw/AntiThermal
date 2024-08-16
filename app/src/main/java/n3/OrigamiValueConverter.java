package n3;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: OrigamiValueConverter.java */
/* renamed from: n3.d, reason: use source file name */
/* loaded from: classes.dex */
public class OrigamiValueConverter {
    public static double a(double d10) {
        return d10 == UserProfileInfo.Constant.NA_LAT_LON ? UserProfileInfo.Constant.NA_LAT_LON : 25.0d + ((d10 - 8.0d) * 3.0d);
    }

    public static double b(double d10) {
        return d10 == UserProfileInfo.Constant.NA_LAT_LON ? UserProfileInfo.Constant.NA_LAT_LON : 8.0d + ((d10 - 25.0d) / 3.0d);
    }

    public static double c(double d10) {
        return d10 == UserProfileInfo.Constant.NA_LAT_LON ? UserProfileInfo.Constant.NA_LAT_LON : 30.0d + ((d10 - 194.0d) / 3.62d);
    }

    public static double d(double d10) {
        return d10 == UserProfileInfo.Constant.NA_LAT_LON ? UserProfileInfo.Constant.NA_LAT_LON : 194.0d + ((d10 - 30.0d) * 3.62d);
    }
}
