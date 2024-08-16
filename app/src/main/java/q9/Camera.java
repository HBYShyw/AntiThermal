package q9;

import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: Camera.java */
/* renamed from: q9.d, reason: use source file name */
/* loaded from: classes2.dex */
public class Camera extends BasicPowerItem {

    /* renamed from: b, reason: collision with root package name */
    private final double f16964b;

    /* renamed from: c, reason: collision with root package name */
    public long f16965c;

    /* renamed from: d, reason: collision with root package name */
    public double f16966d;

    /* renamed from: e, reason: collision with root package name */
    public long f16967e;

    /* renamed from: f, reason: collision with root package name */
    public double f16968f;

    /* renamed from: g, reason: collision with root package name */
    public long f16969g;

    /* renamed from: h, reason: collision with root package name */
    public long f16970h;

    /* renamed from: i, reason: collision with root package name */
    public double f16971i;

    /* renamed from: j, reason: collision with root package name */
    public double f16972j;

    /* renamed from: k, reason: collision with root package name */
    public long f16973k;

    /* renamed from: l, reason: collision with root package name */
    public long f16974l;

    /* renamed from: m, reason: collision with root package name */
    public double f16975m;

    /* renamed from: n, reason: collision with root package name */
    public double f16976n;

    public Camera(PowerProfile powerProfile) {
        super(powerProfile);
        this.f16965c = 0L;
        this.f16966d = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16967e = 0L;
        this.f16968f = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16969g = 0L;
        this.f16970h = 0L;
        this.f16971i = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16972j = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16973k = 0L;
        this.f16974l = 0L;
        this.f16975m = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16976n = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16964b = powerProfile.getAveragePower("camera.avg");
    }

    public void a() {
        this.f16965c = 0L;
        this.f16966d = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16969g = 0L;
        this.f16970h = 0L;
        this.f16971i = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16972j = UserProfileInfo.Constant.NA_LAT_LON;
    }

    public void b(UidSipper uidSipper, boolean z10) {
        long cameraTurnedOnTimeUs = uidSipper.getCameraTurnedOnTimeUs() / 1000;
        double d10 = (cameraTurnedOnTimeUs * this.f16964b) / 3600000.0d;
        if (z10) {
            this.f16965c = cameraTurnedOnTimeUs;
            this.f16966d = d10;
            this.f16967e += cameraTurnedOnTimeUs - cameraTurnedOnTimeUs;
            this.f16968f += d10 - d10;
            return;
        }
        this.f16967e += cameraTurnedOnTimeUs - this.f16965c;
        this.f16968f += d10 - this.f16966d;
        this.f16965c = cameraTurnedOnTimeUs;
        this.f16966d = d10;
    }

    public void c(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        long cameraTurnedOnTimeUs = uidSipper.getCameraTurnedOnTimeUs() / 1000;
        double d10 = (cameraTurnedOnTimeUs * this.f16964b) / 3600000.0d;
        if (z10 && z11) {
            this.f16969g = cameraTurnedOnTimeUs;
            this.f16971i = d10;
            return;
        }
        if (z10 && !z11) {
            if (!z12) {
                this.f16969g = cameraTurnedOnTimeUs;
                this.f16971i = d10;
                this.f16974l += cameraTurnedOnTimeUs - this.f16970h;
                this.f16976n += d10 - this.f16972j;
            } else {
                this.f16974l += cameraTurnedOnTimeUs - this.f16970h;
                this.f16976n += d10 - this.f16972j;
                this.f16970h = cameraTurnedOnTimeUs;
                this.f16972j = d10;
            }
        }
        if (z10 || z11) {
            return;
        }
        this.f16970h = cameraTurnedOnTimeUs;
        this.f16972j = d10;
        this.f16973k += cameraTurnedOnTimeUs - this.f16969g;
        this.f16975m += d10 - this.f16971i;
    }

    public void d() {
        this.f16967e = 0L;
        this.f16968f = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16973k = 0L;
        this.f16974l = 0L;
        this.f16975m = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16976n = UserProfileInfo.Constant.NA_LAT_LON;
    }
}
