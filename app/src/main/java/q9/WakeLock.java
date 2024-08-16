package q9;

import android.os.health.TimerStat;
import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: WakeLock.java */
/* renamed from: q9.m, reason: use source file name */
/* loaded from: classes2.dex */
public class WakeLock extends BasicPowerItem {

    /* renamed from: b, reason: collision with root package name */
    public long f17101b;

    /* renamed from: c, reason: collision with root package name */
    public long f17102c;

    /* renamed from: d, reason: collision with root package name */
    public double f17103d;

    /* renamed from: e, reason: collision with root package name */
    public long f17104e;

    /* renamed from: f, reason: collision with root package name */
    public long f17105f;

    /* renamed from: g, reason: collision with root package name */
    public double f17106g;

    /* renamed from: h, reason: collision with root package name */
    public long f17107h;

    /* renamed from: i, reason: collision with root package name */
    public double f17108i;

    /* renamed from: j, reason: collision with root package name */
    public long f17109j;

    /* renamed from: k, reason: collision with root package name */
    public double f17110k;

    /* renamed from: l, reason: collision with root package name */
    private final double f17111l;

    public WakeLock(PowerProfile powerProfile) {
        super(powerProfile);
        this.f17101b = 0L;
        this.f17102c = 0L;
        this.f17103d = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17104e = 0L;
        this.f17105f = 0L;
        this.f17106g = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17107h = 0L;
        this.f17108i = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17109j = 0L;
        this.f17110k = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17111l = powerProfile.getAveragePower("cpu.idle");
    }

    public void a() {
        this.f17107h = 0L;
        this.f17108i = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17101b = 0L;
        this.f17104e = 0L;
    }

    public void b(UidSipper uidSipper, boolean z10) {
        long totalWakelockTimeMs = uidSipper.getTotalWakelockTimeMs();
        uidSipper.getBackgroundWakelockTimeMs();
        if (z10) {
            this.f17107h = totalWakelockTimeMs;
            this.f17109j += totalWakelockTimeMs - totalWakelockTimeMs;
        } else {
            this.f17109j += totalWakelockTimeMs - this.f17107h;
            this.f17107h = totalWakelockTimeMs;
        }
        this.f17110k = (this.f17109j * this.f17111l) / 3600000.0d;
    }

    public void c(UidSipper uidSipper, boolean z10, boolean z11, boolean z12, String str) {
        if (uidSipper == null) {
            return;
        }
        TimerStat wakelockTimer = uidSipper.getWakelockTimer(1);
        if (wakelockTimer != null) {
            long time = (wakelockTimer.getTime() + 500) / 1000;
            wakelockTimer.getCount();
        }
        long totalWakelockTimeMs = uidSipper.getTotalWakelockTimeMs();
        uidSipper.getBackgroundWakelockTimeMs();
        if (z10 && z11) {
            this.f17101b = totalWakelockTimeMs;
        }
        if (z10 && !z11) {
            if (!z12) {
                this.f17101b = totalWakelockTimeMs;
                this.f17105f += totalWakelockTimeMs - this.f17104e;
            } else {
                this.f17105f += totalWakelockTimeMs - this.f17104e;
                this.f17104e = totalWakelockTimeMs;
            }
        }
        if (!z10 && !z11) {
            this.f17104e = totalWakelockTimeMs;
            this.f17102c += totalWakelockTimeMs - this.f17101b;
        }
        double d10 = this.f17102c;
        double d11 = this.f17111l;
        this.f17103d = (d10 * d11) / 3600000.0d;
        this.f17106g = (this.f17105f * d11) / 3600000.0d;
    }

    public void d() {
        this.f17102c = 0L;
        this.f17103d = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17105f = 0L;
        this.f17106g = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17109j = 0L;
        this.f17110k = UserProfileInfo.Constant.NA_LAT_LON;
    }
}
