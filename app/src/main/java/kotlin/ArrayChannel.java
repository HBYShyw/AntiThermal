package kotlin;

import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.collections._ArraysJvm;
import kotlinx.coroutines.internal.Symbol;
import ma.NoWhenBranchMatchedException;
import ma.Unit;
import ya.l;
import za.k;

/* compiled from: ArrayChannel.kt */
@Metadata(bv = {}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u0002B9\u0012\u0006\u0010!\u001a\u00020\u0003\u0012\u0006\u0010#\u001a\u00020\"\u0012 \u0010&\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\t\u0018\u00010$j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`%¢\u0006\u0004\b'\u0010(J\u0019\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0004\u001a\u00020\u0003H\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u001f\u0010\n\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\b\u001a\u00028\u0000H\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u0017\u0010\f\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0003H\u0002¢\u0006\u0004\b\f\u0010\rJ\u0017\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\b\u001a\u00028\u0000H\u0014¢\u0006\u0004\b\u000f\u0010\u0010J\u0011\u0010\u0011\u001a\u0004\u0018\u00010\u000eH\u0014¢\u0006\u0004\b\u0011\u0010\u0012J\u001d\u0010\u0016\u001a\u00020\u00152\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u0013H\u0014¢\u0006\u0004\b\u0016\u0010\u0017R\u0014\u0010\u001a\u001a\u00020\u00158DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019R\u0014\u0010\u001c\u001a\u00020\u00158DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0019R\u0014\u0010 \u001a\u00020\u001d8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u001f¨\u0006)"}, d2 = {"Lvd/d;", "E", "Lvd/a;", "", "currentSize", "Lkotlinx/coroutines/internal/a0;", "z", "(I)Lkotlinx/coroutines/internal/a0;", "element", "Lma/f0;", "x", "(ILjava/lang/Object;)V", "y", "(I)V", "", "i", "(Ljava/lang/Object;)Ljava/lang/Object;", "v", "()Ljava/lang/Object;", "Lvd/o;", "receive", "", "q", "(Lvd/o;)Z", "r", "()Z", "isBufferAlwaysEmpty", "s", "isBufferEmpty", "", "c", "()Ljava/lang/String;", "bufferDebugString", "capacity", "Lvd/e;", "onBufferOverflow", "Lkotlin/Function1;", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "onUndeliveredElement", "<init>", "(ILvd/e;Lya/l;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: vd.d, reason: use source file name */
/* loaded from: classes2.dex */
public class ArrayChannel<E> extends kotlin.a<E> {

    /* renamed from: e, reason: collision with root package name */
    private final int f19285e;

    /* renamed from: f, reason: collision with root package name */
    private final BufferOverflow f19286f;

    /* renamed from: g, reason: collision with root package name */
    private final ReentrantLock f19287g;

    /* renamed from: h, reason: collision with root package name */
    private Object[] f19288h;

    /* renamed from: i, reason: collision with root package name */
    private int f19289i;
    private volatile /* synthetic */ int size;

    /* compiled from: ArrayChannel.kt */
    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    /* renamed from: vd.d$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f19290a;

        static {
            int[] iArr = new int[BufferOverflow.values().length];
            iArr[BufferOverflow.SUSPEND.ordinal()] = 1;
            iArr[BufferOverflow.DROP_LATEST.ordinal()] = 2;
            iArr[BufferOverflow.DROP_OLDEST.ordinal()] = 3;
            f19290a = iArr;
        }
    }

    public ArrayChannel(int i10, BufferOverflow bufferOverflow, l<? super E, Unit> lVar) {
        super(lVar);
        this.f19285e = i10;
        this.f19286f = bufferOverflow;
        if (i10 >= 1) {
            this.f19287g = new ReentrantLock();
            Object[] objArr = new Object[Math.min(i10, 8)];
            _ArraysJvm.n(objArr, Function1.f19275a, 0, 0, 6, null);
            this.f19288h = objArr;
            this.size = 0;
            return;
        }
        throw new IllegalArgumentException(("ArrayChannel capacity must be at least 1, but " + i10 + " was specified").toString());
    }

    private final void x(int currentSize, E element) {
        if (currentSize < this.f19285e) {
            y(currentSize);
            Object[] objArr = this.f19288h;
            objArr[(this.f19289i + currentSize) % objArr.length] = element;
        } else {
            Object[] objArr2 = this.f19288h;
            int i10 = this.f19289i;
            objArr2[i10 % objArr2.length] = null;
            objArr2[(currentSize + i10) % objArr2.length] = element;
            this.f19289i = (i10 + 1) % objArr2.length;
        }
    }

    private final void y(int currentSize) {
        Object[] objArr = this.f19288h;
        if (currentSize >= objArr.length) {
            int min = Math.min(objArr.length * 2, this.f19285e);
            Object[] objArr2 = new Object[min];
            for (int i10 = 0; i10 < currentSize; i10++) {
                Object[] objArr3 = this.f19288h;
                objArr2[i10] = objArr3[(this.f19289i + i10) % objArr3.length];
            }
            _ArraysJvm.m(objArr2, Function1.f19275a, currentSize, min);
            this.f19288h = objArr2;
            this.f19289i = 0;
        }
    }

    private final Symbol z(int currentSize) {
        if (currentSize < this.f19285e) {
            this.size = currentSize + 1;
            return null;
        }
        int i10 = a.f19290a[this.f19286f.ordinal()];
        if (i10 == 1) {
            return Function1.f19277c;
        }
        if (i10 == 2) {
            return Function1.f19276b;
        }
        if (i10 == 3) {
            return null;
        }
        throw new NoWhenBranchMatchedException();
    }

    @Override // kotlin.c
    protected String c() {
        return "(buffer:capacity=" + this.f19285e + ",size=" + this.size + ')';
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x001b, code lost:
    
        if (r1 == 0) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x001d, code lost:
    
        r2 = l();
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0021, code lost:
    
        if (r2 != null) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0026, code lost:
    
        if ((r2 instanceof kotlin.j) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x002e, code lost:
    
        za.k.b(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0036, code lost:
    
        if (r2.j(r5, null) == null) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0038, code lost:
    
        r4.size = r1;
        r4 = ma.Unit.f15173a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x003c, code lost:
    
        r0.unlock();
        r2.i(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0046, code lost:
    
        return r2.c();
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0028, code lost:
    
        r4.size = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x002d, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0047, code lost:
    
        x(r1, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x004f, code lost:
    
        return kotlin.Function1.f19276b;
     */
    @Override // kotlin.c
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object i(E element) {
        ReentrantLock reentrantLock = this.f19287g;
        reentrantLock.lock();
        try {
            int i10 = this.size;
            j<?> d10 = d();
            if (d10 != null) {
                return d10;
            }
            Symbol z10 = z(i10);
            if (z10 != null) {
                return z10;
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlin.a
    public boolean q(o<? super E> receive) {
        ReentrantLock reentrantLock = this.f19287g;
        reentrantLock.lock();
        try {
            return super.q(receive);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlin.a
    protected final boolean r() {
        return false;
    }

    @Override // kotlin.a
    protected final boolean s() {
        return this.size == 0;
    }

    @Override // kotlin.a
    protected Object v() {
        ReentrantLock reentrantLock = this.f19287g;
        reentrantLock.lock();
        try {
            int i10 = this.size;
            if (i10 == 0) {
                Object d10 = d();
                if (d10 == null) {
                    d10 = Function1.f19278d;
                }
                return d10;
            }
            Object[] objArr = this.f19288h;
            int i11 = this.f19289i;
            Object obj = objArr[i11];
            s sVar = null;
            objArr[i11] = null;
            this.size = i10 - 1;
            Object obj2 = Function1.f19278d;
            boolean z10 = false;
            if (i10 == this.f19285e) {
                s sVar2 = null;
                while (true) {
                    s m10 = m();
                    if (m10 == null) {
                        sVar = sVar2;
                        break;
                    }
                    k.b(m10);
                    if (m10.H(null) != null) {
                        obj2 = m10.getF19284h();
                        z10 = true;
                        sVar = m10;
                        break;
                    }
                    m10.I();
                    sVar2 = m10;
                }
            }
            if (obj2 != Function1.f19278d && !(obj2 instanceof j)) {
                this.size = i10;
                Object[] objArr2 = this.f19288h;
                objArr2[(this.f19289i + i10) % objArr2.length] = obj2;
            }
            this.f19289i = (this.f19289i + 1) % this.f19288h.length;
            Unit unit = Unit.f15173a;
            if (z10) {
                k.b(sVar);
                sVar.F();
            }
            return obj;
        } finally {
            reentrantLock.unlock();
        }
    }
}
