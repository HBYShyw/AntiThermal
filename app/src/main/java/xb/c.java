package xb;

import za.k;

/* compiled from: LookupTracker.kt */
/* loaded from: classes2.dex */
public interface c {

    /* compiled from: LookupTracker.kt */
    /* loaded from: classes2.dex */
    public static final class a implements c {

        /* renamed from: a, reason: collision with root package name */
        public static final a f19665a = new a();

        private a() {
        }

        @Override // xb.c
        public boolean a() {
            return false;
        }

        @Override // xb.c
        public void b(String str, e eVar, String str2, f fVar, String str3) {
            k.e(str, "filePath");
            k.e(eVar, "position");
            k.e(str2, "scopeFqName");
            k.e(fVar, "scopeKind");
            k.e(str3, "name");
        }
    }

    boolean a();

    void b(String str, e eVar, String str2, f fVar, String str3);
}
