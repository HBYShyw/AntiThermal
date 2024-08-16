package sd;

import java.util.List;

/* compiled from: MatchResult.kt */
/* loaded from: classes2.dex */
public interface h {

    /* compiled from: MatchResult.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        public static b a(h hVar) {
            return new b(hVar);
        }
    }

    /* compiled from: MatchResult.kt */
    /* loaded from: classes2.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final h f18489a;

        public b(h hVar) {
            za.k.e(hVar, "match");
            this.f18489a = hVar;
        }

        public final h a() {
            return this.f18489a;
        }
    }

    b a();

    List<String> b();
}
