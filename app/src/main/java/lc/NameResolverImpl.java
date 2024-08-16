package lc;

import java.util.LinkedList;
import java.util.List;
import jc.o;
import jc.p;
import kotlin.collections._Collections;
import ma.t;
import za.k;

/* compiled from: NameResolverImpl.kt */
/* renamed from: lc.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class NameResolverImpl implements NameResolver {

    /* renamed from: a, reason: collision with root package name */
    private final p f14695a;

    /* renamed from: b, reason: collision with root package name */
    private final o f14696b;

    /* compiled from: NameResolverImpl.kt */
    /* renamed from: lc.d$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f14697a;

        static {
            int[] iArr = new int[o.c.EnumC0066c.values().length];
            try {
                iArr[o.c.EnumC0066c.CLASS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[o.c.EnumC0066c.PACKAGE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[o.c.EnumC0066c.LOCAL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f14697a = iArr;
        }
    }

    public NameResolverImpl(p pVar, o oVar) {
        k.e(pVar, "strings");
        k.e(oVar, "qualifiedNames");
        this.f14695a = pVar;
        this.f14696b = oVar;
    }

    private final t<List<String>, List<String>, Boolean> c(int i10) {
        LinkedList linkedList = new LinkedList();
        LinkedList linkedList2 = new LinkedList();
        boolean z10 = false;
        while (i10 != -1) {
            o.c q10 = this.f14696b.q(i10);
            String q11 = this.f14695a.q(q10.u());
            o.c.EnumC0066c s7 = q10.s();
            k.b(s7);
            int i11 = a.f14697a[s7.ordinal()];
            if (i11 == 1) {
                linkedList2.addFirst(q11);
            } else if (i11 == 2) {
                linkedList.addFirst(q11);
            } else if (i11 == 3) {
                linkedList2.addFirst(q11);
                z10 = true;
            }
            i10 = q10.t();
        }
        return new t<>(linkedList, linkedList2, Boolean.valueOf(z10));
    }

    @Override // lc.NameResolver
    public String a(int i10) {
        String c02;
        String c03;
        t<List<String>, List<String>, Boolean> c10 = c(i10);
        List<String> a10 = c10.a();
        c02 = _Collections.c0(c10.b(), ".", null, null, 0, null, null, 62, null);
        if (a10.isEmpty()) {
            return c02;
        }
        StringBuilder sb2 = new StringBuilder();
        c03 = _Collections.c0(a10, "/", null, null, 0, null, null, 62, null);
        sb2.append(c03);
        sb2.append('/');
        sb2.append(c02);
        return sb2.toString();
    }

    @Override // lc.NameResolver
    public boolean b(int i10) {
        return c(i10).d().booleanValue();
    }

    @Override // lc.NameResolver
    public String getString(int i10) {
        String q10 = this.f14695a.q(i10);
        k.d(q10, "strings.getString(index)");
        return q10;
    }
}
