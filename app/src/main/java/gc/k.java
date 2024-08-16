package gc;

import java.util.List;
import za.DefaultConstructorMarker;

/* compiled from: predefinedEnhancementInfo.kt */
/* loaded from: classes2.dex */
public final class k {

    /* renamed from: a, reason: collision with root package name */
    private final q f11711a;

    /* renamed from: b, reason: collision with root package name */
    private final List<q> f11712b;

    public k() {
        this(null, 0 == true ? 1 : 0, 3, 0 == true ? 1 : 0);
    }

    public k(q qVar, List<q> list) {
        za.k.e(list, "parametersInfo");
        this.f11711a = qVar;
        this.f11712b = list;
    }

    public final List<q> a() {
        return this.f11712b;
    }

    public final q b() {
        return this.f11711a;
    }

    public /* synthetic */ k(q qVar, List list, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? null : qVar, (i10 & 2) != 0 ? kotlin.collections.r.j() : list);
    }
}
