package sd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import fb.PrimitiveRanges;

/* compiled from: Regex.kt */
/* loaded from: classes2.dex */
public final class f {

    /* renamed from: a, reason: collision with root package name */
    private final String f18487a;

    /* renamed from: b, reason: collision with root package name */
    private final PrimitiveRanges f18488b;

    public f(String str, PrimitiveRanges primitiveRanges) {
        za.k.e(str, ThermalBaseConfig.Item.ATTR_VALUE);
        za.k.e(primitiveRanges, "range");
        this.f18487a = str;
        this.f18488b = primitiveRanges;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof f)) {
            return false;
        }
        f fVar = (f) obj;
        return za.k.a(this.f18487a, fVar.f18487a) && za.k.a(this.f18488b, fVar.f18488b);
    }

    public int hashCode() {
        return (this.f18487a.hashCode() * 31) + this.f18488b.hashCode();
    }

    public String toString() {
        return "MatchGroup(value=" + this.f18487a + ", range=" + this.f18488b + ')';
    }
}
