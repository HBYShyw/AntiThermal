package sd;

import fb.PrimitiveRanges;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import kotlin.collections.AbstractCollection;
import kotlin.collections.AbstractList;
import kotlin.collections._Collections;
import rd.Sequence;
import rd._Sequences;
import sd.h;
import za.Lambda;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Regex.kt */
/* loaded from: classes2.dex */
public final class i implements h {

    /* renamed from: a, reason: collision with root package name */
    private final Matcher f18490a;

    /* renamed from: b, reason: collision with root package name */
    private final CharSequence f18491b;

    /* renamed from: c, reason: collision with root package name */
    private final g f18492c;

    /* renamed from: d, reason: collision with root package name */
    private List<String> f18493d;

    /* compiled from: Regex.kt */
    /* loaded from: classes2.dex */
    public static final class a extends AbstractList<String> {
        a() {
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj instanceof String) {
                return e((String) obj);
            }
            return false;
        }

        @Override // kotlin.collections.AbstractCollection
        public int d() {
            return i.this.d().groupCount() + 1;
        }

        public /* bridge */ boolean e(String str) {
            return super.contains(str);
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        /* renamed from: f, reason: merged with bridge method [inline-methods] */
        public String get(int i10) {
            String group = i.this.d().group(i10);
            return group == null ? "" : group;
        }

        public /* bridge */ int g(String str) {
            return super.indexOf(str);
        }

        public /* bridge */ int h(String str) {
            return super.lastIndexOf(str);
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int indexOf(Object obj) {
            if (obj instanceof String) {
                return g((String) obj);
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int lastIndexOf(Object obj) {
            if (obj instanceof String) {
                return h((String) obj);
            }
            return -1;
        }
    }

    /* compiled from: Regex.kt */
    /* loaded from: classes2.dex */
    public static final class b extends AbstractCollection<f> implements g {

        /* compiled from: Regex.kt */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.l<Integer, f> {
            a() {
                super(1);
            }

            public final f a(int i10) {
                return b.this.f(i10);
            }

            @Override // ya.l
            public /* bridge */ /* synthetic */ f invoke(Integer num) {
                return a(num.intValue());
            }
        }

        b() {
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj == null ? true : obj instanceof f) {
                return e((f) obj);
            }
            return false;
        }

        @Override // kotlin.collections.AbstractCollection
        public int d() {
            return i.this.d().groupCount() + 1;
        }

        public /* bridge */ boolean e(f fVar) {
            return super.contains(fVar);
        }

        public f f(int i10) {
            PrimitiveRanges d10;
            d10 = k.d(i.this.d(), i10);
            if (d10.l().intValue() < 0) {
                return null;
            }
            String group = i.this.d().group(i10);
            za.k.d(group, "matchResult.group(index)");
            return new f(group, d10);
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Iterator<f> iterator() {
            PrimitiveRanges k10;
            Sequence K;
            Sequence w10;
            k10 = kotlin.collections.r.k(this);
            K = _Collections.K(k10);
            w10 = _Sequences.w(K, new a());
            return w10.iterator();
        }
    }

    public i(Matcher matcher, CharSequence charSequence) {
        za.k.e(matcher, "matcher");
        za.k.e(charSequence, "input");
        this.f18490a = matcher;
        this.f18491b = charSequence;
        this.f18492c = new b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final MatchResult d() {
        return this.f18490a;
    }

    @Override // sd.h
    public h.b a() {
        return h.a.a(this);
    }

    @Override // sd.h
    public List<String> b() {
        if (this.f18493d == null) {
            this.f18493d = new a();
        }
        List<String> list = this.f18493d;
        za.k.b(list);
        return list;
    }
}
