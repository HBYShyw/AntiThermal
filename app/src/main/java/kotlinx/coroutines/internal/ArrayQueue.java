package kotlinx.coroutines.internal;

import java.util.Objects;
import kotlin.Metadata;

/* compiled from: ArrayQueue.kt */
@Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0010\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\u00020\u0001B\u0007¢\u0006\u0004\b\u0015\u0010\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0002J\u0015\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00028\u0000¢\u0006\u0004\b\u0006\u0010\u0007J\u000f\u0010\b\u001a\u0004\u0018\u00018\u0000¢\u0006\u0004\b\b\u0010\tR\u001e\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\n8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0006\u0010\u000bR\u0016\u0010\u000f\u001a\u00020\r8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0004\u0010\u000eR\u0016\u0010\u0011\u001a\u00020\r8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0010\u0010\u000eR\u0011\u0010\u0014\u001a\u00020\u00128F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0013¨\u0006\u0017"}, d2 = {"Lkotlinx/coroutines/internal/a;", "", "T", "Lma/f0;", "b", "element", "a", "(Ljava/lang/Object;)V", "d", "()Ljava/lang/Object;", "", "[Ljava/lang/Object;", "elements", "", "I", "head", "c", "tail", "", "()Z", "isEmpty", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: kotlinx.coroutines.internal.a, reason: use source file name */
/* loaded from: classes2.dex */
public class ArrayQueue<T> {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private Object[] elements = new Object[16];

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private int head;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private int tail;

    private final void b() {
        Object[] objArr = this.elements;
        int length = objArr.length;
        Object[] objArr2 = new Object[length << 1];
        kotlin.collections.j.i(objArr, objArr2, 0, this.head, 0, 10, null);
        Object[] objArr3 = this.elements;
        int length2 = objArr3.length;
        int i10 = this.head;
        kotlin.collections.j.i(objArr3, objArr2, length2 - i10, 0, i10, 4, null);
        this.elements = objArr2;
        this.head = 0;
        this.tail = length;
    }

    public final void a(T element) {
        Object[] objArr = this.elements;
        int i10 = this.tail;
        objArr[i10] = element;
        int length = (objArr.length - 1) & (i10 + 1);
        this.tail = length;
        if (length == this.head) {
            b();
        }
    }

    public final boolean c() {
        return this.head == this.tail;
    }

    public final T d() {
        int i10 = this.head;
        if (i10 == this.tail) {
            return null;
        }
        Object[] objArr = this.elements;
        T t7 = (T) objArr[i10];
        objArr[i10] = null;
        this.head = (i10 + 1) & (objArr.length - 1);
        Objects.requireNonNull(t7, "null cannot be cast to non-null type T of kotlinx.coroutines.internal.ArrayQueue");
        return t7;
    }
}
