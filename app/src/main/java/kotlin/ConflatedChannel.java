package kotlin;

import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.i0;
import kotlinx.coroutines.internal.u;
import ma.Unit;
import ya.l;

/* compiled from: ConflatedChannel.kt */
@Metadata(bv = {}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u0002B)\u0012 \u0010\u001a\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0018\u0018\u00010\u0017j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\u0019¢\u0006\u0004\b\u001b\u0010\u001cJ\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0002J\u0017\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H\u0014¢\u0006\u0004\b\u0007\u0010\bJ\n\u0010\t\u001a\u0004\u0018\u00010\u0003H\u0014J\u0016\u0010\r\u001a\u00020\f2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0014R\u0014\u0010\u0010\u001a\u00020\f8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0012\u001a\u00020\f8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u000fR\u0014\u0010\u0016\u001a\u00020\u00138TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015¨\u0006\u001d"}, d2 = {"Lvd/m;", "E", "Lvd/a;", "", "element", "Lkotlinx/coroutines/internal/i0;", "x", "i", "(Ljava/lang/Object;)Ljava/lang/Object;", "v", "Lvd/o;", "receive", "", "q", "r", "()Z", "isBufferAlwaysEmpty", "s", "isBufferEmpty", "", "c", "()Ljava/lang/String;", "bufferDebugString", "Lkotlin/Function1;", "Lma/f0;", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "onUndeliveredElement", "<init>", "(Lya/l;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: vd.m, reason: use source file name */
/* loaded from: classes2.dex */
public class ConflatedChannel<E> extends a<E> {

    /* renamed from: e, reason: collision with root package name */
    private final ReentrantLock f19302e;

    /* renamed from: f, reason: collision with root package name */
    private Object f19303f;

    public ConflatedChannel(l<? super E, Unit> lVar) {
        super(lVar);
        this.f19302e = new ReentrantLock();
        this.f19303f = Function1.f19275a;
    }

    private final i0 x(Object element) {
        l<E, Unit> lVar;
        Object obj = this.f19303f;
        i0 i0Var = null;
        if (obj != Function1.f19275a && (lVar = this.f19282b) != null) {
            i0Var = u.d(lVar, obj, null, 2, null);
        }
        this.f19303f = element;
        return i0Var;
    }

    @Override // kotlin.c
    protected String c() {
        ReentrantLock reentrantLock = this.f19302e;
        reentrantLock.lock();
        try {
            return "(value=" + this.f19303f + ')';
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x0015, code lost:
    
        r1 = l();
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0019, code lost:
    
        if (r1 != null) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x001e, code lost:
    
        if ((r1 instanceof kotlin.j) == false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0024, code lost:
    
        za.k.b(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002c, code lost:
    
        if (r1.j(r4, null) == null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x002e, code lost:
    
        r3 = ma.Unit.f15173a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0030, code lost:
    
        r0.unlock();
        r1.i(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x003a, code lost:
    
        return r1.c();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0023, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x003b, code lost:
    
        r3 = x(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x003f, code lost:
    
        if (r3 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0046, code lost:
    
        return kotlin.Function1.f19276b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0047, code lost:
    
        throw r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0013, code lost:
    
        if (r3.f19303f == kotlin.Function1.f19275a) goto L9;
     */
    @Override // kotlin.c
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object i(E element) {
        ReentrantLock reentrantLock = this.f19302e;
        reentrantLock.lock();
        try {
            j<?> d10 = d();
            if (d10 != null) {
                return d10;
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlin.a
    public boolean q(o<? super E> receive) {
        ReentrantLock reentrantLock = this.f19302e;
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
        ReentrantLock reentrantLock = this.f19302e;
        reentrantLock.lock();
        try {
            return this.f19303f == Function1.f19275a;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlin.a
    protected Object v() {
        ReentrantLock reentrantLock = this.f19302e;
        reentrantLock.lock();
        try {
            Object obj = this.f19303f;
            Symbol symbol = Function1.f19275a;
            if (obj != symbol) {
                this.f19303f = symbol;
                Unit unit = Unit.f15173a;
                return obj;
            }
            Object d10 = d();
            if (d10 == null) {
                d10 = Function1.f19278d;
            }
            return d10;
        } finally {
            reentrantLock.unlock();
        }
    }
}
