package xb;

import java.io.Serializable;
import za.DefaultConstructorMarker;

/* compiled from: LookupLocation.kt */
/* loaded from: classes2.dex */
public final class e implements Serializable {

    /* renamed from: g, reason: collision with root package name */
    public static final a f19688g = new a(null);

    /* renamed from: h, reason: collision with root package name */
    private static final e f19689h = new e(-1, -1);

    /* renamed from: e, reason: collision with root package name */
    private final int f19690e;

    /* renamed from: f, reason: collision with root package name */
    private final int f19691f;

    /* compiled from: LookupLocation.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final e a() {
            return e.f19689h;
        }
    }

    public e(int i10, int i11) {
        this.f19690e = i10;
        this.f19691f = i11;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof e)) {
            return false;
        }
        e eVar = (e) obj;
        return this.f19690e == eVar.f19690e && this.f19691f == eVar.f19691f;
    }

    public int hashCode() {
        return (Integer.hashCode(this.f19690e) * 31) + Integer.hashCode(this.f19691f);
    }

    public String toString() {
        return "Position(line=" + this.f19690e + ", column=" + this.f19691f + ')';
    }
}
