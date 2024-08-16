package rc;

import java.util.ArrayList;
import java.util.Set;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import za.DefaultConstructorMarker;

/* compiled from: DescriptorRenderer.kt */
/* loaded from: classes2.dex */
public enum e {
    VISIBILITY(true),
    MODALITY(true),
    OVERRIDE(true),
    ANNOTATIONS(false),
    INNER(true),
    MEMBER_KIND(true),
    DATA(true),
    INLINE(true),
    EXPECT(true),
    ACTUAL(true),
    CONST(true),
    LATEINIT(true),
    FUN(true),
    VALUE(true);


    /* renamed from: f, reason: collision with root package name */
    public static final a f17738f = new a(null);

    /* renamed from: g, reason: collision with root package name */
    public static final Set<e> f17739g;

    /* renamed from: h, reason: collision with root package name */
    public static final Set<e> f17740h;

    /* renamed from: e, reason: collision with root package name */
    private final boolean f17756e;

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        Set<e> D0;
        Set<e> r02;
        e[] values = values();
        ArrayList arrayList = new ArrayList();
        for (e eVar : values) {
            if (eVar.f17756e) {
                arrayList.add(eVar);
            }
        }
        D0 = _Collections.D0(arrayList);
        f17739g = D0;
        r02 = _Arrays.r0(values());
        f17740h = r02;
    }

    e(boolean z10) {
        this.f17756e = z10;
    }
}
