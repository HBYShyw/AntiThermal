package qd;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/* compiled from: SmartList.java */
/* renamed from: qd.e, reason: use source file name */
/* loaded from: classes2.dex */
public class SmartList<E> extends AbstractList<E> implements RandomAccess {

    /* renamed from: e, reason: collision with root package name */
    private int f17426e;

    /* renamed from: f, reason: collision with root package name */
    private Object f17427f;

    /* compiled from: SmartList.java */
    /* renamed from: qd.e$b */
    /* loaded from: classes2.dex */
    private static class b<T> implements Iterator<T> {

        /* renamed from: e, reason: collision with root package name */
        private static final b f17428e = new b();

        private b() {
        }

        public static <T> b<T> a() {
            return f17428e;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.Iterator
        public T next() {
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new IllegalStateException();
        }
    }

    /* compiled from: SmartList.java */
    /* renamed from: qd.e$c */
    /* loaded from: classes2.dex */
    private class c extends d<E> {

        /* renamed from: f, reason: collision with root package name */
        private final int f17429f;

        public c() {
            super();
            this.f17429f = ((AbstractList) SmartList.this).modCount;
        }

        @Override // qd.SmartList.d
        protected void a() {
            if (((AbstractList) SmartList.this).modCount == this.f17429f) {
                return;
            }
            throw new ConcurrentModificationException("ModCount: " + ((AbstractList) SmartList.this).modCount + "; expected: " + this.f17429f);
        }

        @Override // qd.SmartList.d
        protected E b() {
            return (E) SmartList.this.f17427f;
        }

        @Override // java.util.Iterator
        public void remove() {
            a();
            SmartList.this.clear();
        }
    }

    /* compiled from: SmartList.java */
    /* renamed from: qd.e$d */
    /* loaded from: classes2.dex */
    private static abstract class d<T> implements Iterator<T> {

        /* renamed from: e, reason: collision with root package name */
        private boolean f17431e;

        private d() {
        }

        protected abstract void a();

        protected abstract T b();

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return !this.f17431e;
        }

        @Override // java.util.Iterator
        public final T next() {
            if (!this.f17431e) {
                this.f17431e = true;
                a();
                return b();
            }
            throw new NoSuchElementException();
        }
    }

    private static /* synthetic */ void c(int i10) {
        String str = (i10 == 2 || i10 == 3 || i10 == 5 || i10 == 6 || i10 == 7) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 2 || i10 == 3 || i10 == 5 || i10 == 6 || i10 == 7) ? 2 : 3];
        switch (i10) {
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/utils/SmartList";
                break;
            case 4:
                objArr[0] = "a";
                break;
            default:
                objArr[0] = "elements";
                break;
        }
        if (i10 == 2 || i10 == 3) {
            objArr[1] = "iterator";
        } else if (i10 == 5 || i10 == 6 || i10 == 7) {
            objArr[1] = "toArray";
        } else {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/utils/SmartList";
        }
        switch (i10) {
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
                break;
            case 4:
                objArr[2] = "toArray";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        if (i10 != 2 && i10 != 3 && i10 != 5 && i10 != 6 && i10 != 7) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e10) {
        int i10 = this.f17426e;
        if (i10 == 0) {
            this.f17427f = e10;
        } else if (i10 == 1) {
            this.f17427f = new Object[]{this.f17427f, e10};
        } else {
            Object[] objArr = (Object[]) this.f17427f;
            int length = objArr.length;
            if (i10 >= length) {
                int i11 = ((length * 3) / 2) + 1;
                int i12 = i10 + 1;
                if (i11 < i12) {
                    i11 = i12;
                }
                Object[] objArr2 = new Object[i11];
                this.f17427f = objArr2;
                System.arraycopy(objArr, 0, objArr2, 0, length);
                objArr = objArr2;
            }
            objArr[this.f17426e] = e10;
        }
        this.f17426e++;
        ((AbstractList) this).modCount++;
        return true;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.f17427f = null;
        this.f17426e = 0;
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i10) {
        int i11;
        if (i10 >= 0 && i10 < (i11 = this.f17426e)) {
            if (i11 == 1) {
                return (E) this.f17427f;
            }
            return (E) ((Object[]) this.f17427f)[i10];
        }
        throw new IndexOutOfBoundsException("Index: " + i10 + ", Size: " + this.f17426e);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        int i10 = this.f17426e;
        if (i10 == 0) {
            b a10 = b.a();
            if (a10 == null) {
                c(2);
            }
            return a10;
        }
        if (i10 == 1) {
            return new c();
        }
        Iterator<E> it = super.iterator();
        if (it == null) {
            c(3);
        }
        return it;
    }

    @Override // java.util.AbstractList, java.util.List
    public E remove(int i10) {
        int i11;
        E e10;
        if (i10 >= 0 && i10 < (i11 = this.f17426e)) {
            if (i11 == 1) {
                e10 = (E) this.f17427f;
                this.f17427f = null;
            } else {
                Object[] objArr = (Object[]) this.f17427f;
                Object obj = objArr[i10];
                if (i11 == 2) {
                    this.f17427f = objArr[1 - i10];
                } else {
                    int i12 = (i11 - i10) - 1;
                    if (i12 > 0) {
                        System.arraycopy(objArr, i10 + 1, objArr, i10, i12);
                    }
                    objArr[this.f17426e - 1] = null;
                }
                e10 = (E) obj;
            }
            this.f17426e--;
            ((AbstractList) this).modCount++;
            return e10;
        }
        throw new IndexOutOfBoundsException("Index: " + i10 + ", Size: " + this.f17426e);
    }

    @Override // java.util.AbstractList, java.util.List
    public E set(int i10, E e10) {
        int i11;
        if (i10 < 0 || i10 >= (i11 = this.f17426e)) {
            throw new IndexOutOfBoundsException("Index: " + i10 + ", Size: " + this.f17426e);
        }
        if (i11 == 1) {
            E e11 = (E) this.f17427f;
            this.f17427f = e10;
            return e11;
        }
        Object[] objArr = (Object[]) this.f17427f;
        E e12 = (E) objArr[i10];
        objArr[i10] = e10;
        return e12;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.f17426e;
    }

    @Override // java.util.List
    public void sort(Comparator<? super E> comparator) {
        int i10 = this.f17426e;
        if (i10 >= 2) {
            Arrays.sort((Object[]) this.f17427f, 0, i10, comparator);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public <T> T[] toArray(T[] tArr) {
        if (tArr == 0) {
            c(4);
        }
        int length = tArr.length;
        int i10 = this.f17426e;
        if (i10 == 1) {
            if (length != 0) {
                tArr[0] = this.f17427f;
            } else {
                T[] tArr2 = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), 1));
                tArr2[0] = this.f17427f;
                return tArr2;
            }
        } else {
            if (length < i10) {
                T[] tArr3 = (T[]) Arrays.copyOf((Object[]) this.f17427f, i10, tArr.getClass());
                if (tArr3 == null) {
                    c(6);
                }
                return tArr3;
            }
            if (i10 != 0) {
                System.arraycopy(this.f17427f, 0, tArr, 0, i10);
            }
        }
        int i11 = this.f17426e;
        if (length > i11) {
            tArr[i11] = 0;
        }
        return tArr;
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i10, E e10) {
        int i11;
        if (i10 >= 0 && i10 <= (i11 = this.f17426e)) {
            if (i11 == 0) {
                this.f17427f = e10;
            } else if (i11 == 1 && i10 == 0) {
                this.f17427f = new Object[]{e10, this.f17427f};
            } else {
                Object[] objArr = new Object[i11 + 1];
                if (i11 == 1) {
                    objArr[0] = this.f17427f;
                } else {
                    Object[] objArr2 = (Object[]) this.f17427f;
                    System.arraycopy(objArr2, 0, objArr, 0, i10);
                    System.arraycopy(objArr2, i10, objArr, i10 + 1, this.f17426e - i10);
                }
                objArr[i10] = e10;
                this.f17427f = objArr;
            }
            this.f17426e++;
            ((AbstractList) this).modCount++;
            return;
        }
        throw new IndexOutOfBoundsException("Index: " + i10 + ", Size: " + this.f17426e);
    }
}
