package yb;

import za.DefaultConstructorMarker;

/* compiled from: ReportLevel.kt */
/* renamed from: yb.g0, reason: use source file name */
/* loaded from: classes2.dex */
public enum ReportLevel {
    IGNORE("ignore"),
    WARN("warn"),
    STRICT("strict");


    /* renamed from: f, reason: collision with root package name */
    public static final a f20080f = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private final String f20085e;

    /* compiled from: ReportLevel.kt */
    /* renamed from: yb.g0$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    ReportLevel(String str) {
        this.f20085e = str;
    }

    public final String b() {
        return this.f20085e;
    }

    public final boolean c() {
        return this == IGNORE;
    }

    public final boolean d() {
        return this == WARN;
    }
}
