package nd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.Arrays;
import java.util.Iterator;
import kotlin.collections._Arrays;
import za.DefaultConstructorMarker;

/* compiled from: ArrayMap.kt */
/* loaded from: classes2.dex */
public final class d<T> extends c<T> {

    /* renamed from: g, reason: collision with root package name */
    public static final a f16010g = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private Object[] f16011e;

    /* renamed from: f, reason: collision with root package name */
    private int f16012f;

    /* compiled from: ArrayMap.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: ArrayMap.kt */
    /* loaded from: classes2.dex */
    public static final class b extends kotlin.collections.b<T> {

        /* renamed from: g, reason: collision with root package name */
        private int f16013g = -1;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ d<T> f16014h;

        b(d<T> dVar) {
            this.f16014h = dVar;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.collections.b
        protected void b() {
            do {
                int i10 = this.f16013g + 1;
                this.f16013g = i10;
                if (i10 >= ((d) this.f16014h).f16011e.length) {
                    break;
                }
            } while (((d) this.f16014h).f16011e[this.f16013g] == null);
            if (this.f16013g >= ((d) this.f16014h).f16011e.length) {
                d();
                return;
            }
            Object obj = ((d) this.f16014h).f16011e[this.f16013g];
            za.k.c(obj, "null cannot be cast to non-null type T of org.jetbrains.kotlin.util.ArrayMapImpl");
            e(obj);
        }
    }

    private d(Object[] objArr, int i10) {
        super(null);
        this.f16011e = objArr;
        this.f16012f = i10;
    }

    private final void g(int i10) {
        Object[] objArr = this.f16011e;
        if (objArr.length <= i10) {
            Object[] copyOf = Arrays.copyOf(objArr, objArr.length * 2);
            za.k.d(copyOf, "copyOf(this, newSize)");
            this.f16011e = copyOf;
        }
    }

    @Override // nd.c
    public int d() {
        return this.f16012f;
    }

    @Override // nd.c
    public void e(int i10, T t7) {
        za.k.e(t7, ThermalBaseConfig.Item.ATTR_VALUE);
        g(i10);
        if (this.f16011e[i10] == null) {
            this.f16012f = d() + 1;
        }
        this.f16011e[i10] = t7;
    }

    @Override // nd.c
    public T get(int i10) {
        Object F;
        F = _Arrays.F(this.f16011e, i10);
        return (T) F;
    }

    @Override // nd.c, java.lang.Iterable
    public Iterator<T> iterator() {
        return new b(this);
    }

    public d() {
        this(new Object[20], 0);
    }
}
