package nd;

import za.DefaultConstructorMarker;

/* compiled from: modifierChecks.kt */
/* loaded from: classes2.dex */
public abstract class g {

    /* renamed from: a, reason: collision with root package name */
    private final boolean f16016a;

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class a extends g {

        /* renamed from: b, reason: collision with root package name */
        public static final a f16017b = new a();

        private a() {
            super(false, null);
        }
    }

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class b extends g {

        /* renamed from: b, reason: collision with root package name */
        private final String f16018b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(String str) {
            super(false, null);
            za.k.e(str, "error");
            this.f16018b = str;
        }
    }

    /* compiled from: modifierChecks.kt */
    /* loaded from: classes2.dex */
    public static final class c extends g {

        /* renamed from: b, reason: collision with root package name */
        public static final c f16019b = new c();

        private c() {
            super(true, null);
        }
    }

    private g(boolean z10) {
        this.f16016a = z10;
    }

    public /* synthetic */ g(boolean z10, DefaultConstructorMarker defaultConstructorMarker) {
        this(z10);
    }

    public final boolean a() {
        return this.f16016a;
    }
}
