package androidx.recyclerview.widget;

import android.annotation.SuppressLint;
import androidx.core.os.TraceCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: GapWorker.java */
/* renamed from: androidx.recyclerview.widget.h, reason: use source file name */
/* loaded from: classes.dex */
public final class GapWorker implements Runnable {

    /* renamed from: i, reason: collision with root package name */
    static final ThreadLocal<GapWorker> f3763i = new ThreadLocal<>();

    /* renamed from: j, reason: collision with root package name */
    static Comparator<c> f3764j = new a();

    /* renamed from: f, reason: collision with root package name */
    long f3766f;

    /* renamed from: g, reason: collision with root package name */
    long f3767g;

    /* renamed from: e, reason: collision with root package name */
    ArrayList<RecyclerView> f3765e = new ArrayList<>();

    /* renamed from: h, reason: collision with root package name */
    private ArrayList<c> f3768h = new ArrayList<>();

    /* compiled from: GapWorker.java */
    /* renamed from: androidx.recyclerview.widget.h$a */
    /* loaded from: classes.dex */
    class a implements Comparator<c> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(c cVar, c cVar2) {
            RecyclerView recyclerView = cVar.f3776d;
            if ((recyclerView == null) != (cVar2.f3776d == null)) {
                return recyclerView == null ? 1 : -1;
            }
            boolean z10 = cVar.f3773a;
            if (z10 != cVar2.f3773a) {
                return z10 ? -1 : 1;
            }
            int i10 = cVar2.f3774b - cVar.f3774b;
            if (i10 != 0) {
                return i10;
            }
            int i11 = cVar.f3775c - cVar2.f3775c;
            if (i11 != 0) {
                return i11;
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: GapWorker.java */
    @SuppressLint({"VisibleForTests"})
    /* renamed from: androidx.recyclerview.widget.h$b */
    /* loaded from: classes.dex */
    public static class b implements RecyclerView.p.c {

        /* renamed from: a, reason: collision with root package name */
        int f3769a;

        /* renamed from: b, reason: collision with root package name */
        int f3770b;

        /* renamed from: c, reason: collision with root package name */
        int[] f3771c;

        /* renamed from: d, reason: collision with root package name */
        int f3772d;

        @Override // androidx.recyclerview.widget.RecyclerView.p.c
        public void a(int i10, int i11) {
            if (i10 < 0) {
                throw new IllegalArgumentException("Layout positions must be non-negative");
            }
            if (i11 >= 0) {
                int i12 = this.f3772d * 2;
                int[] iArr = this.f3771c;
                if (iArr == null) {
                    int[] iArr2 = new int[4];
                    this.f3771c = iArr2;
                    Arrays.fill(iArr2, -1);
                } else if (i12 >= iArr.length) {
                    int[] iArr3 = new int[i12 * 2];
                    this.f3771c = iArr3;
                    System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
                }
                int[] iArr4 = this.f3771c;
                iArr4[i12] = i10;
                iArr4[i12 + 1] = i11;
                this.f3772d++;
                return;
            }
            throw new IllegalArgumentException("Pixel distance must be non-negative");
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void b() {
            int[] iArr = this.f3771c;
            if (iArr != null) {
                Arrays.fill(iArr, -1);
            }
            this.f3772d = 0;
        }

        void c(RecyclerView recyclerView, boolean z10) {
            this.f3772d = 0;
            int[] iArr = this.f3771c;
            if (iArr != null) {
                Arrays.fill(iArr, -1);
            }
            RecyclerView.p pVar = recyclerView.mLayout;
            if (recyclerView.mAdapter == null || pVar == null || !pVar.w0()) {
                return;
            }
            if (z10) {
                if (!recyclerView.mAdapterHelper.p()) {
                    pVar.p(recyclerView.mAdapter.getItemCount(), this);
                }
            } else if (!recyclerView.hasPendingAdapterUpdates()) {
                pVar.o(this.f3769a, this.f3770b, recyclerView.mState, this);
            }
            int i10 = this.f3772d;
            if (i10 > pVar.f3498m) {
                pVar.f3498m = i10;
                pVar.f3499n = z10;
                recyclerView.mRecycler.K();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean d(int i10) {
            if (this.f3771c != null) {
                int i11 = this.f3772d * 2;
                for (int i12 = 0; i12 < i11; i12 += 2) {
                    if (this.f3771c[i12] == i10) {
                        return true;
                    }
                }
            }
            return false;
        }

        void e(int i10, int i11) {
            this.f3769a = i10;
            this.f3770b = i11;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: GapWorker.java */
    /* renamed from: androidx.recyclerview.widget.h$c */
    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: a, reason: collision with root package name */
        public boolean f3773a;

        /* renamed from: b, reason: collision with root package name */
        public int f3774b;

        /* renamed from: c, reason: collision with root package name */
        public int f3775c;

        /* renamed from: d, reason: collision with root package name */
        public RecyclerView f3776d;

        /* renamed from: e, reason: collision with root package name */
        public int f3777e;

        c() {
        }

        public void a() {
            this.f3773a = false;
            this.f3774b = 0;
            this.f3775c = 0;
            this.f3776d = null;
            this.f3777e = 0;
        }
    }

    private void b() {
        c cVar;
        int size = this.f3765e.size();
        int i10 = 0;
        for (int i11 = 0; i11 < size; i11++) {
            RecyclerView recyclerView = this.f3765e.get(i11);
            if (recyclerView.getWindowVisibility() == 0) {
                recyclerView.mPrefetchRegistry.c(recyclerView, false);
                i10 += recyclerView.mPrefetchRegistry.f3772d;
            }
        }
        this.f3768h.ensureCapacity(i10);
        int i12 = 0;
        for (int i13 = 0; i13 < size; i13++) {
            RecyclerView recyclerView2 = this.f3765e.get(i13);
            if (recyclerView2.getWindowVisibility() == 0) {
                b bVar = recyclerView2.mPrefetchRegistry;
                int abs = Math.abs(bVar.f3769a) + Math.abs(bVar.f3770b);
                for (int i14 = 0; i14 < bVar.f3772d * 2; i14 += 2) {
                    if (i12 >= this.f3768h.size()) {
                        cVar = new c();
                        this.f3768h.add(cVar);
                    } else {
                        cVar = this.f3768h.get(i12);
                    }
                    int[] iArr = bVar.f3771c;
                    int i15 = iArr[i14 + 1];
                    cVar.f3773a = i15 <= abs;
                    cVar.f3774b = abs;
                    cVar.f3775c = i15;
                    cVar.f3776d = recyclerView2;
                    cVar.f3777e = iArr[i14];
                    i12++;
                }
            }
        }
        Collections.sort(this.f3768h, f3764j);
    }

    private void c(c cVar, long j10) {
        RecyclerView.c0 i10 = i(cVar.f3776d, cVar.f3777e, cVar.f3773a ? Long.MAX_VALUE : j10);
        if (i10 == null || i10.mNestedRecyclerView == null || !i10.isBound() || i10.isInvalid()) {
            return;
        }
        h(i10.mNestedRecyclerView.get(), j10);
    }

    private void d(long j10) {
        for (int i10 = 0; i10 < this.f3768h.size(); i10++) {
            c cVar = this.f3768h.get(i10);
            if (cVar.f3776d == null) {
                return;
            }
            c(cVar, j10);
            cVar.a();
        }
    }

    static boolean e(RecyclerView recyclerView, int i10) {
        int j10 = recyclerView.mChildHelper.j();
        for (int i11 = 0; i11 < j10; i11++) {
            RecyclerView.c0 childViewHolderInt = RecyclerView.getChildViewHolderInt(recyclerView.mChildHelper.i(i11));
            if (childViewHolderInt.mPosition == i10 && !childViewHolderInt.isInvalid()) {
                return true;
            }
        }
        return false;
    }

    private void h(RecyclerView recyclerView, long j10) {
        if (recyclerView == null) {
            return;
        }
        if (recyclerView.mDataSetHasChangedAfterLayout && recyclerView.mChildHelper.j() != 0) {
            recyclerView.removeAndRecycleViews();
        }
        b bVar = recyclerView.mPrefetchRegistry;
        bVar.c(recyclerView, true);
        if (bVar.f3772d != 0) {
            try {
                TraceCompat.a("RV Nested Prefetch");
                recyclerView.mState.f(recyclerView.mAdapter);
                for (int i10 = 0; i10 < bVar.f3772d * 2; i10 += 2) {
                    i(recyclerView, bVar.f3771c[i10], j10);
                }
            } finally {
                TraceCompat.b();
            }
        }
    }

    private RecyclerView.c0 i(RecyclerView recyclerView, int i10, long j10) {
        if (e(recyclerView, i10)) {
            return null;
        }
        RecyclerView.v vVar = recyclerView.mRecycler;
        try {
            recyclerView.onEnterLayoutOrScroll();
            RecyclerView.c0 I = vVar.I(i10, false, j10);
            if (I != null) {
                if (I.isBound() && !I.isInvalid()) {
                    vVar.B(I.itemView);
                } else {
                    vVar.a(I, false);
                }
            }
            return I;
        } finally {
            recyclerView.onExitLayoutOrScroll(false);
        }
    }

    public void a(RecyclerView recyclerView) {
        this.f3765e.add(recyclerView);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(RecyclerView recyclerView, int i10, int i11) {
        if (recyclerView.isAttachedToWindow() && this.f3766f == 0) {
            this.f3766f = recyclerView.getNanoTime();
            recyclerView.post(this);
        }
        recyclerView.mPrefetchRegistry.e(i10, i11);
    }

    void g(long j10) {
        b();
        d(j10);
    }

    public void j(RecyclerView recyclerView) {
        this.f3765e.remove(recyclerView);
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            TraceCompat.a("RV Prefetch");
            if (!this.f3765e.isEmpty()) {
                int size = this.f3765e.size();
                long j10 = 0;
                for (int i10 = 0; i10 < size; i10++) {
                    RecyclerView recyclerView = this.f3765e.get(i10);
                    if (recyclerView.getWindowVisibility() == 0) {
                        j10 = Math.max(recyclerView.getDrawingTime(), j10);
                    }
                }
                if (j10 != 0) {
                    g(TimeUnit.MILLISECONDS.toNanos(j10) + this.f3767g);
                }
            }
        } finally {
            this.f3766f = 0L;
            TraceCompat.b();
        }
    }
}
