package x2;

import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import java.util.Observable;

/* compiled from: ObservableStep.java */
/* renamed from: x2.c, reason: use source file name */
/* loaded from: classes.dex */
public class ObservableStep extends Observable {

    /* renamed from: a, reason: collision with root package name */
    private int f19508a;

    /* renamed from: b, reason: collision with root package name */
    private int f19509b = CpuLevelConfig.ThermalCpuLevelPolicy.CPU_POWER_DEFAULT;

    /* renamed from: c, reason: collision with root package name */
    private int f19510c = -999;

    public int a() {
        return this.f19509b;
    }

    public int b() {
        return this.f19510c;
    }

    public int c() {
        return this.f19508a;
    }

    public void d(int i10) {
        if (i10 < this.f19510c) {
            throw new IllegalArgumentException("maximum cannot be smaller than mMini");
        }
        if (i10 <= 9999) {
            this.f19509b = i10;
            if (this.f19508a > i10) {
                f(i10);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("maximum cannot be bigger than '9999'");
    }

    public void e(int i10) {
        if (i10 > this.f19509b) {
            throw new IllegalArgumentException("minimum cannot be bigger than mMini");
        }
        if (i10 >= -999) {
            this.f19510c = i10;
            if (this.f19508a < i10) {
                f(i10);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("minimum cannot be smaller than '-999'");
    }

    public void f(int i10) {
        int min = Math.min(Math.max(i10, b()), a());
        int i11 = this.f19508a;
        this.f19508a = min;
        setChanged();
        notifyObservers(Integer.valueOf(i11));
    }
}
