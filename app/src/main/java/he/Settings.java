package he;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import za.DefaultConstructorMarker;

/* compiled from: Settings.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001:\u0001\bB\u0007¢\u0006\u0004\b\u0015\u0010\u0016J\u0019\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0086\u0002J\u000e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0002J\u0011\u0010\b\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0086\u0002J\u0006\u0010\t\u001a\u00020\u0002J\u0006\u0010\n\u001a\u00020\u0002J\u000e\u0010\f\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u0002J\u000e\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\r\u001a\u00020\u0000R\u0011\u0010\u0012\u001a\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0014\u001a\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0011¨\u0006\u0017"}, d2 = {"Lhe/m;", "", "", "id", ThermalBaseConfig.Item.ATTR_VALUE, "h", "", "f", "a", "i", "d", "defaultValue", "e", "other", "Lma/f0;", "g", "b", "()I", "headerTableSize", "c", "initialWindowSize", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: he.m, reason: use source file name */
/* loaded from: classes2.dex */
public final class Settings {

    /* renamed from: c, reason: collision with root package name */
    public static final a f12466c = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private int f12467a;

    /* renamed from: b, reason: collision with root package name */
    private final int[] f12468b = new int[10];

    /* compiled from: Settings.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\f\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rR\u0014\u0010\u0003\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0014\u0010\u0005\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0005\u0010\u0004R\u0014\u0010\u0006\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0004R\u0014\u0010\u0007\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0007\u0010\u0004R\u0014\u0010\b\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\b\u0010\u0004R\u0014\u0010\t\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\t\u0010\u0004R\u0014\u0010\n\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\n\u0010\u0004R\u0014\u0010\u000b\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000b\u0010\u0004¨\u0006\u000e"}, d2 = {"Lhe/m$a;", "", "", "COUNT", "I", "DEFAULT_INITIAL_WINDOW_SIZE", "ENABLE_PUSH", "HEADER_TABLE_SIZE", "INITIAL_WINDOW_SIZE", "MAX_CONCURRENT_STREAMS", "MAX_FRAME_SIZE", "MAX_HEADER_LIST_SIZE", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: he.m$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public final int a(int id2) {
        return this.f12468b[id2];
    }

    public final int b() {
        if ((this.f12467a & 2) != 0) {
            return this.f12468b[1];
        }
        return -1;
    }

    public final int c() {
        if ((this.f12467a & 128) != 0) {
            return this.f12468b[7];
        }
        return 65535;
    }

    public final int d() {
        if ((this.f12467a & 16) != 0) {
            return this.f12468b[4];
        }
        return Integer.MAX_VALUE;
    }

    public final int e(int defaultValue) {
        return (this.f12467a & 32) != 0 ? this.f12468b[5] : defaultValue;
    }

    public final boolean f(int id2) {
        return (this.f12467a & (1 << id2)) != 0;
    }

    public final void g(Settings settings) {
        za.k.e(settings, "other");
        int i10 = 0;
        while (i10 < 10) {
            int i11 = i10 + 1;
            if (settings.f(i10)) {
                h(i10, settings.a(i10));
            }
            i10 = i11;
        }
    }

    public final Settings h(int id2, int value) {
        if (id2 >= 0) {
            int[] iArr = this.f12468b;
            if (id2 < iArr.length) {
                this.f12467a = (1 << id2) | this.f12467a;
                iArr[id2] = value;
            }
        }
        return this;
    }

    public final int i() {
        return Integer.bitCount(this.f12467a);
    }
}
