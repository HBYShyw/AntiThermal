package fb;

import java.util.NoSuchElementException;
import kotlin.collections.PrimitiveIterators;

/* compiled from: ProgressionIterators.kt */
/* renamed from: fb.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class ProgressionIterators extends PrimitiveIterators {

    /* renamed from: e, reason: collision with root package name */
    private final int f11410e;

    /* renamed from: f, reason: collision with root package name */
    private final int f11411f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f11412g;

    /* renamed from: h, reason: collision with root package name */
    private int f11413h;

    public ProgressionIterators(int i10, int i11, int i12) {
        this.f11410e = i12;
        this.f11411f = i11;
        boolean z10 = true;
        if (i12 <= 0 ? i10 < i11 : i10 > i11) {
            z10 = false;
        }
        this.f11412g = z10;
        this.f11413h = z10 ? i10 : i11;
    }

    @Override // kotlin.collections.PrimitiveIterators
    public int b() {
        int i10 = this.f11413h;
        if (i10 == this.f11411f) {
            if (this.f11412g) {
                this.f11412g = false;
            } else {
                throw new NoSuchElementException();
            }
        } else {
            this.f11413h = this.f11410e + i10;
        }
        return i10;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.f11412g;
    }
}
