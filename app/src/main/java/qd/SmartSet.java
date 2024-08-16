package qd;

import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.collections._Arrays;
import kotlin.collections.s0;
import za.DefaultConstructorMarker;
import za.TypeIntrinsics;
import za.k;

/* compiled from: SmartSet.kt */
/* renamed from: qd.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class SmartSet<T> extends AbstractSet<T> {

    /* renamed from: g, reason: collision with root package name */
    public static final b f17432g = new b(null);

    /* renamed from: e, reason: collision with root package name */
    private Object f17433e;

    /* renamed from: f, reason: collision with root package name */
    private int f17434f;

    /* compiled from: SmartSet.kt */
    /* renamed from: qd.f$a */
    /* loaded from: classes2.dex */
    private static final class a<T> implements Iterator<T>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final Iterator<T> f17435e;

        public a(T[] tArr) {
            k.e(tArr, ThermalWindowConfigInfo.TAG_ARRAY);
            this.f17435e = za.b.a(tArr);
        }

        @Override // java.util.Iterator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public Void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f17435e.hasNext();
        }

        @Override // java.util.Iterator
        public T next() {
            return this.f17435e.next();
        }
    }

    /* compiled from: SmartSet.kt */
    /* renamed from: qd.f$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final <T> SmartSet<T> a() {
            return new SmartSet<>(null);
        }

        public final <T> SmartSet<T> b(Collection<? extends T> collection) {
            k.e(collection, "set");
            SmartSet<T> smartSet = new SmartSet<>(null);
            smartSet.addAll(collection);
            return smartSet;
        }
    }

    /* compiled from: SmartSet.kt */
    /* renamed from: qd.f$c */
    /* loaded from: classes2.dex */
    private static final class c<T> implements Iterator<T>, ab.a {

        /* renamed from: e, reason: collision with root package name */
        private final T f17436e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f17437f = true;

        public c(T t7) {
            this.f17436e = t7;
        }

        @Override // java.util.Iterator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public Void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f17437f;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.f17437f) {
                this.f17437f = false;
                return this.f17436e;
            }
            throw new NoSuchElementException();
        }
    }

    private SmartSet() {
    }

    public /* synthetic */ SmartSet(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public static final <T> SmartSet<T> c() {
        return f17432g.a();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(T t7) {
        boolean v7;
        Object[] objArr;
        LinkedHashSet f10;
        if (size() == 0) {
            this.f17433e = t7;
        } else if (size() == 1) {
            if (k.a(this.f17433e, t7)) {
                return false;
            }
            this.f17433e = new Object[]{this.f17433e, t7};
        } else if (size() < 5) {
            Object obj = this.f17433e;
            k.c(obj, "null cannot be cast to non-null type kotlin.Array<T of org.jetbrains.kotlin.utils.SmartSet>");
            Object[] objArr2 = (Object[]) obj;
            v7 = _Arrays.v(objArr2, t7);
            if (v7) {
                return false;
            }
            if (size() == 4) {
                f10 = s0.f(Arrays.copyOf(objArr2, objArr2.length));
                f10.add(t7);
                objArr = f10;
            } else {
                Object[] copyOf = Arrays.copyOf(objArr2, size() + 1);
                k.d(copyOf, "copyOf(this, newSize)");
                copyOf[copyOf.length - 1] = t7;
                objArr = copyOf;
            }
            this.f17433e = objArr;
        } else {
            Object obj2 = this.f17433e;
            k.c(obj2, "null cannot be cast to non-null type kotlin.collections.MutableSet<T of org.jetbrains.kotlin.utils.SmartSet>");
            if (!TypeIntrinsics.c(obj2).add(t7)) {
                return false;
            }
        }
        e(size() + 1);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.f17433e = null;
        e(0);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        boolean v7;
        if (size() == 0) {
            return false;
        }
        if (size() == 1) {
            return k.a(this.f17433e, obj);
        }
        if (size() >= 5) {
            Object obj2 = this.f17433e;
            k.c(obj2, "null cannot be cast to non-null type kotlin.collections.Set<T of org.jetbrains.kotlin.utils.SmartSet>");
            return ((Set) obj2).contains(obj);
        }
        Object obj3 = this.f17433e;
        k.c(obj3, "null cannot be cast to non-null type kotlin.Array<T of org.jetbrains.kotlin.utils.SmartSet>");
        v7 = _Arrays.v((Object[]) obj3, obj);
        return v7;
    }

    public int d() {
        return this.f17434f;
    }

    public void e(int i10) {
        this.f17434f = i10;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<T> iterator() {
        Iterator<T> aVar;
        if (size() == 0) {
            return Collections.emptySet().iterator();
        }
        if (size() == 1) {
            aVar = new c<>(this.f17433e);
        } else {
            if (size() >= 5) {
                Object obj = this.f17433e;
                k.c(obj, "null cannot be cast to non-null type kotlin.collections.MutableSet<T of org.jetbrains.kotlin.utils.SmartSet>");
                return TypeIntrinsics.c(obj).iterator();
            }
            Object obj2 = this.f17433e;
            k.c(obj2, "null cannot be cast to non-null type kotlin.Array<T of org.jetbrains.kotlin.utils.SmartSet>");
            aVar = new a<>((Object[]) obj2);
        }
        return aVar;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final /* bridge */ int size() {
        return d();
    }
}
