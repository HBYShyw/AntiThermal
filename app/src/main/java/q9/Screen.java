package q9;

import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: Screen.java */
/* renamed from: q9.j, reason: use source file name */
/* loaded from: classes2.dex */
public class Screen extends BasicPowerItem {

    /* renamed from: b, reason: collision with root package name */
    public long f17058b;

    /* renamed from: c, reason: collision with root package name */
    public double f17059c;

    /* renamed from: d, reason: collision with root package name */
    public double f17060d;

    /* renamed from: e, reason: collision with root package name */
    public long f17061e;

    /* renamed from: f, reason: collision with root package name */
    public long[] f17062f;

    /* renamed from: g, reason: collision with root package name */
    public long[] f17063g;

    public Screen(PowerProfile powerProfile) {
        super(powerProfile);
        this.f17058b = 0L;
        this.f17059c = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17060d = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17061e = 0L;
        this.f17062f = r3;
        this.f17063g = r2;
        long[] jArr = {0, 0, 0, 0, 0};
        long[] jArr2 = {0, 0, 0, 0, 0};
        this.f16963a = powerProfile;
    }

    public void a(boolean z10, boolean z11) {
        if (z10 || z11) {
            return;
        }
        double averagePower = (this.f17061e * this.f16963a.getAveragePower("screen.on")) / 3600000.0d;
        this.f17059c = averagePower;
        this.f17060d = averagePower;
    }

    public void b(long j10, boolean z10, boolean z11, UidSipper uidSipper) {
        int i10 = 0;
        if (z10) {
            this.f17058b = j10 / 1000;
            while (i10 < 5) {
                this.f17062f[i10] = uidSipper.getScreenBrightnessTime(i10) / 1000;
                i10++;
            }
            return;
        }
        if (z10 || z11) {
            return;
        }
        long j11 = j10 / 1000;
        this.f17061e += j11 - this.f17058b;
        this.f17058b = j11;
        while (i10 < 5) {
            long[] jArr = this.f17063g;
            long j12 = jArr[i10];
            long screenBrightnessTime = uidSipper.getScreenBrightnessTime(i10) / 1000;
            long[] jArr2 = this.f17062f;
            jArr[i10] = j12 + (screenBrightnessTime - jArr2[i10]);
            jArr2[i10] = uidSipper.getScreenBrightnessTime(i10) / 1000;
            i10++;
        }
    }

    public void c() {
        this.f17058b = 0L;
        this.f17059c = UserProfileInfo.Constant.NA_LAT_LON;
        long[] jArr = this.f17062f;
        jArr[0] = 0;
        jArr[1] = 0;
        jArr[2] = 0;
        jArr[3] = 0;
        jArr[4] = 0;
    }

    public void d() {
        this.f17060d = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17061e = 0L;
        long[] jArr = this.f17063g;
        jArr[0] = 0;
        jArr[1] = 0;
        jArr[2] = 0;
        jArr[3] = 0;
        jArr[4] = 0;
    }
}
