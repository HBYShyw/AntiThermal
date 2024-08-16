package kotlinx.coroutines.internal;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.lang.Comparable;
import java.util.Arrays;
import kotlin.Metadata;
import kotlinx.coroutines.internal.g0;

/* compiled from: ThreadSafeHeap.kt */
@Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0013\b\u0017\u0018\u0000*\u0012\b\u0000\u0010\u0003*\u00020\u0001*\b\u0012\u0004\u0012\u00028\u00000\u00022\u00060\u0004j\u0002`\u0005B\u0007¢\u0006\u0004\b&\u0010'J\u0018\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\u0006H\u0082\u0010¢\u0006\u0004\b\t\u0010\nJ\u0018\u0010\u000b\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\u0006H\u0082\u0010¢\u0006\u0004\b\u000b\u0010\nJ\u0017\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\fH\u0002¢\u0006\u0004\b\r\u0010\u000eJ\u001f\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0006H\u0002¢\u0006\u0004\b\u0010\u0010\u0011J\u000f\u0010\u0012\u001a\u0004\u0018\u00018\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u000f\u0010\u0007\u001a\u0004\u0018\u00018\u0000¢\u0006\u0004\b\u0007\u0010\u0013J\u0015\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0014\u001a\u00028\u0000¢\u0006\u0004\b\u0016\u0010\u0017J\u0011\u0010\u0018\u001a\u0004\u0018\u00018\u0000H\u0001¢\u0006\u0004\b\u0018\u0010\u0013J\u0017\u0010\u001a\u001a\u00028\u00002\u0006\u0010\u0019\u001a\u00020\u0006H\u0001¢\u0006\u0004\b\u001a\u0010\u001bJ\u0017\u0010\u001c\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00028\u0000H\u0001¢\u0006\u0004\b\u001c\u0010\u001dR \u0010\u001c\u001a\f\u0012\u0006\u0012\u0004\u0018\u00018\u0000\u0018\u00010\f8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001c\u0010\u001eR$\u0010\"\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00068F@BX\u0086\u000e¢\u0006\f\u001a\u0004\b \u0010!\"\u0004\b\u000f\u0010\nR\u0011\u0010%\u001a\u00020\u00158F¢\u0006\u0006\u001a\u0004\b#\u0010$¨\u0006("}, d2 = {"Lkotlinx/coroutines/internal/f0;", "Lkotlinx/coroutines/internal/g0;", "", "T", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "", "i", "Lma/f0;", "l", "(I)V", "k", "", "f", "()[Lkotlinx/coroutines/internal/g0;", "j", "m", "(II)V", "e", "()Lkotlinx/coroutines/internal/g0;", "node", "", "g", "(Lkotlinx/coroutines/internal/g0;)Z", "b", ThermalBaseConfig.Item.ATTR_INDEX, "h", "(I)Lkotlinx/coroutines/internal/g0;", "a", "(Lkotlinx/coroutines/internal/g0;)V", "[Lkotlinx/coroutines/internal/g0;", ThermalBaseConfig.Item.ATTR_VALUE, "c", "()I", "size", "d", "()Z", "isEmpty", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public class f0<T extends g0 & Comparable<? super T>> {
    private volatile /* synthetic */ int _size = 0;
    private T[] a;

    private final T[] f() {
        T[] tArr = this.a;
        if (tArr == null) {
            T[] tArr2 = (T[]) new g0[4];
            this.a = tArr2;
            return tArr2;
        }
        if (get_size() < tArr.length) {
            return tArr;
        }
        Object[] copyOf = Arrays.copyOf(tArr, get_size() * 2);
        za.k.d(copyOf, "copyOf(this, newSize)");
        T[] tArr3 = (T[]) ((g0[]) copyOf);
        this.a = tArr3;
        return tArr3;
    }

    private final void j(int i10) {
        this._size = i10;
    }

    private final void k(int i10) {
        while (true) {
            int i11 = (i10 * 2) + 1;
            if (i11 >= get_size()) {
                return;
            }
            T[] tArr = this.a;
            za.k.b(tArr);
            int i12 = i11 + 1;
            if (i12 < get_size()) {
                T t7 = tArr[i12];
                za.k.b(t7);
                T t10 = tArr[i11];
                za.k.b(t10);
                if (((Comparable) t7).compareTo(t10) < 0) {
                    i11 = i12;
                }
            }
            T t11 = tArr[i10];
            za.k.b(t11);
            T t12 = tArr[i11];
            za.k.b(t12);
            if (((Comparable) t11).compareTo(t12) <= 0) {
                return;
            }
            m(i10, i11);
            i10 = i11;
        }
    }

    private final void l(int i10) {
        while (i10 > 0) {
            T[] tArr = this.a;
            za.k.b(tArr);
            int i11 = (i10 - 1) / 2;
            T t7 = tArr[i11];
            za.k.b(t7);
            T t10 = tArr[i10];
            za.k.b(t10);
            if (((Comparable) t7).compareTo(t10) <= 0) {
                return;
            }
            m(i10, i11);
            i10 = i11;
        }
    }

    private final void m(int i10, int j10) {
        T[] tArr = this.a;
        za.k.b(tArr);
        T t7 = tArr[j10];
        za.k.b(t7);
        T t10 = tArr[i10];
        za.k.b(t10);
        tArr[i10] = t7;
        tArr[j10] = t10;
        t7.e(i10);
        t10.e(j10);
    }

    public final void a(T node) {
        node.b(this);
        T[] f10 = f();
        int i10 = get_size();
        j(i10 + 1);
        f10[i10] = node;
        node.e(i10);
        l(i10);
    }

    public final T b() {
        T[] tArr = this.a;
        if (tArr != null) {
            return tArr[0];
        }
        return null;
    }

    /* renamed from: c, reason: from getter */
    public final int get_size() {
        return this._size;
    }

    public final boolean d() {
        return get_size() == 0;
    }

    public final T e() {
        T b10;
        synchronized (this) {
            b10 = b();
        }
        return b10;
    }

    public final boolean g(T node) {
        boolean z10;
        synchronized (this) {
            if (node.c() == null) {
                z10 = false;
            } else {
                h(node.j());
                z10 = true;
            }
        }
        return z10;
    }

    public final T h(int index) {
        T[] tArr = this.a;
        za.k.b(tArr);
        j(get_size() - 1);
        if (index < get_size()) {
            m(index, get_size());
            int i10 = (index - 1) / 2;
            if (index > 0) {
                T t7 = tArr[index];
                za.k.b(t7);
                T t10 = tArr[i10];
                za.k.b(t10);
                if (((Comparable) t7).compareTo(t10) < 0) {
                    m(index, i10);
                    l(i10);
                }
            }
            k(index);
        }
        T t11 = tArr[get_size()];
        za.k.b(t11);
        t11.b(null);
        t11.e(-1);
        tArr[get_size()] = null;
        return t11;
    }

    public final T i() {
        T h10;
        synchronized (this) {
            h10 = get_size() > 0 ? h(0) : null;
        }
        return h10;
    }
}
