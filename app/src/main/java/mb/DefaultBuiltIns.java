package mb;

import fd.LockBasedStorageManager;
import za.DefaultConstructorMarker;

/* compiled from: DefaultBuiltIns.kt */
/* renamed from: mb.e, reason: use source file name */
/* loaded from: classes2.dex */
public final class DefaultBuiltIns extends KotlinBuiltIns {

    /* renamed from: h, reason: collision with root package name */
    public static final a f15215h;

    /* renamed from: i, reason: collision with root package name */
    private static final DefaultBuiltIns f15216i;

    /* compiled from: DefaultBuiltIns.kt */
    /* renamed from: mb.e$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final DefaultBuiltIns a() {
            return DefaultBuiltIns.f15216i;
        }
    }

    static {
        DefaultConstructorMarker defaultConstructorMarker = null;
        f15215h = new a(defaultConstructorMarker);
        f15216i = new DefaultBuiltIns(false, 1, defaultConstructorMarker);
    }

    public DefaultBuiltIns(boolean z10) {
        super(new LockBasedStorageManager("DefaultBuiltIns"));
        if (z10) {
            f(false);
        }
    }

    public /* synthetic */ DefaultBuiltIns(boolean z10, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? true : z10);
    }
}
