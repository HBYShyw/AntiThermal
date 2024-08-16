package q9;

import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;

/* compiled from: Alarm.java */
/* renamed from: q9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class Alarm extends BasicPowerItem {

    /* renamed from: b, reason: collision with root package name */
    public int f16939b;

    /* renamed from: c, reason: collision with root package name */
    public int f16940c;

    /* renamed from: d, reason: collision with root package name */
    public int f16941d;

    /* renamed from: e, reason: collision with root package name */
    public int f16942e;

    /* renamed from: f, reason: collision with root package name */
    public int f16943f;

    /* renamed from: g, reason: collision with root package name */
    public int f16944g;

    public Alarm(PowerProfile powerProfile) {
        super(powerProfile);
        this.f16939b = 0;
        this.f16940c = 0;
        this.f16941d = 0;
        this.f16942e = 0;
        this.f16943f = 0;
        this.f16944g = 0;
    }

    private int b(UidSipper uidSipper) {
        return (int) uidSipper.getAlarmCounts();
    }

    public void a() {
        this.f16939b = 0;
        this.f16941d = 0;
        this.f16943f = 0;
    }

    public void c(UidSipper uidSipper, boolean z10) {
        int b10 = b(uidSipper);
        if (z10) {
            this.f16939b = b10;
            this.f16940c += b10 - b10;
        } else {
            this.f16940c += b10 - this.f16939b;
            this.f16939b = b10;
        }
    }

    public void d(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        if (uidSipper == null) {
            return;
        }
        if (z10 && z11) {
            this.f16941d = b(uidSipper);
        }
        if (z10 && !z11) {
            int b10 = b(uidSipper);
            if (!z12) {
                this.f16941d = b10;
                this.f16944g += b10 - this.f16943f;
            } else {
                this.f16944g += b10 - this.f16943f;
                this.f16943f = b10;
            }
        }
        if (z10 || z11) {
            return;
        }
        int b11 = b(uidSipper);
        this.f16943f = b11;
        this.f16942e += b11 - this.f16941d;
    }

    public void e() {
        this.f16940c = 0;
        this.f16942e = 0;
        this.f16944g = 0;
    }
}
