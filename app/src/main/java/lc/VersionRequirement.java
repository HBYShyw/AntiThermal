package lc;

import java.util.List;
import jc.v;
import jc.w;
import kotlin.collections.r;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: VersionRequirement.kt */
/* renamed from: lc.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class VersionRequirement {

    /* renamed from: b, reason: collision with root package name */
    public static final a f14699b = new a(null);

    /* renamed from: c, reason: collision with root package name */
    private static final VersionRequirement f14700c;

    /* renamed from: a, reason: collision with root package name */
    private final List<v> f14701a;

    /* compiled from: VersionRequirement.kt */
    /* renamed from: lc.h$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final VersionRequirement a(w wVar) {
            k.e(wVar, "table");
            if (wVar.q() == 0) {
                return b();
            }
            List<v> r10 = wVar.r();
            k.d(r10, "table.requirementList");
            return new VersionRequirement(r10, null);
        }

        public final VersionRequirement b() {
            return VersionRequirement.f14700c;
        }
    }

    static {
        List j10;
        j10 = r.j();
        f14700c = new VersionRequirement(j10);
    }

    private VersionRequirement(List<v> list) {
        this.f14701a = list;
    }

    public /* synthetic */ VersionRequirement(List list, DefaultConstructorMarker defaultConstructorMarker) {
        this(list);
    }
}
