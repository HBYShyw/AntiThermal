package q9;

import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: FlashLight.java */
/* renamed from: q9.f, reason: use source file name */
/* loaded from: classes2.dex */
public class FlashLight extends BasicPowerItem {

    /* renamed from: b, reason: collision with root package name */
    private final double f16999b;

    /* renamed from: c, reason: collision with root package name */
    public long f17000c;

    /* renamed from: d, reason: collision with root package name */
    public double f17001d;

    /* renamed from: e, reason: collision with root package name */
    public long f17002e;

    /* renamed from: f, reason: collision with root package name */
    public double f17003f;

    /* renamed from: g, reason: collision with root package name */
    public long f17004g;

    /* renamed from: h, reason: collision with root package name */
    public long f17005h;

    /* renamed from: i, reason: collision with root package name */
    public double f17006i;

    /* renamed from: j, reason: collision with root package name */
    public double f17007j;

    /* renamed from: k, reason: collision with root package name */
    public long f17008k;

    /* renamed from: l, reason: collision with root package name */
    public long f17009l;

    /* renamed from: m, reason: collision with root package name */
    public double f17010m;

    /* renamed from: n, reason: collision with root package name */
    public double f17011n;

    public FlashLight(PowerProfile powerProfile) {
        super(powerProfile);
        this.f17000c = 0L;
        this.f17001d = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17002e = 0L;
        this.f17003f = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17004g = 0L;
        this.f17005h = 0L;
        this.f17006i = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17007j = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17008k = 0L;
        this.f17009l = 0L;
        this.f17010m = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17011n = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16999b = powerProfile.getAveragePower("camera.flashlight");
    }

    public void a() {
        this.f17000c = 0L;
        this.f17001d = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17004g = 0L;
        this.f17005h = 0L;
        this.f17006i = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17007j = UserProfileInfo.Constant.NA_LAT_LON;
    }

    public void b(UidSipper uidSipper, boolean z10) {
        long flashlightTurnedOnTimeUs = uidSipper.getFlashlightTurnedOnTimeUs() / 1000;
        double d10 = (flashlightTurnedOnTimeUs * this.f16999b) / 3600000.0d;
        if (z10) {
            this.f17000c = flashlightTurnedOnTimeUs;
            this.f17001d = d10;
            this.f17002e += flashlightTurnedOnTimeUs - flashlightTurnedOnTimeUs;
            this.f17003f += d10 - d10;
            return;
        }
        this.f17002e += flashlightTurnedOnTimeUs - this.f17000c;
        this.f17003f += d10 - this.f17001d;
        this.f17000c = flashlightTurnedOnTimeUs;
        this.f17001d = d10;
    }

    public void c(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        long flashlightTurnedOnTimeUs = uidSipper.getFlashlightTurnedOnTimeUs() / 1000;
        double d10 = (flashlightTurnedOnTimeUs * this.f16999b) / 3600000.0d;
        if (z10 && z11) {
            this.f17004g = flashlightTurnedOnTimeUs;
            this.f17006i = d10;
            return;
        }
        if (z10 && !z11) {
            if (!z12) {
                this.f17004g = flashlightTurnedOnTimeUs;
                this.f17006i = d10;
                this.f17009l += flashlightTurnedOnTimeUs - this.f17005h;
                this.f17011n += d10 - this.f17007j;
            } else {
                this.f17009l += flashlightTurnedOnTimeUs - this.f17005h;
                this.f17011n += d10 - this.f17007j;
                this.f17005h = flashlightTurnedOnTimeUs;
                this.f17007j = d10;
            }
        }
        if (z10 || z11) {
            return;
        }
        this.f17005h = flashlightTurnedOnTimeUs;
        this.f17007j = d10;
        this.f17008k += flashlightTurnedOnTimeUs - this.f17004g;
        this.f17010m += d10 - this.f17006i;
    }

    public void d() {
        this.f17002e = 0L;
        this.f17003f = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17008k = 0L;
        this.f17009l = 0L;
        this.f17010m = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17011n = UserProfileInfo.Constant.NA_LAT_LON;
    }
}
