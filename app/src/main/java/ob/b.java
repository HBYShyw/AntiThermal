package ob;

import fd.LockBasedStorageManager;
import mb.KotlinBuiltIns;
import rb.c;
import za.DefaultConstructorMarker;

/* compiled from: JvmBuiltInsCustomizer.kt */
/* loaded from: classes2.dex */
final class b extends KotlinBuiltIns {

    /* renamed from: h, reason: collision with root package name */
    public static final a f16337h = new a(null);

    /* renamed from: i, reason: collision with root package name */
    private static final KotlinBuiltIns f16338i = new b();

    /* compiled from: JvmBuiltInsCustomizer.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final KotlinBuiltIns a() {
            return b.f16338i;
        }
    }

    private b() {
        super(new LockBasedStorageManager("FallbackBuiltIns"));
        f(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // mb.KotlinBuiltIns
    /* renamed from: F0, reason: merged with bridge method [inline-methods] */
    public c.a M() {
        return c.a.f17689a;
    }
}
