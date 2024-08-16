package me;

import java.util.concurrent.atomic.AtomicReference;

/* compiled from: SegmentPool.kt */
/* renamed from: me.w, reason: use source file name */
/* loaded from: classes2.dex */
public final class SegmentPool {

    /* renamed from: a, reason: collision with root package name */
    public static final SegmentPool f15538a = new SegmentPool();

    /* renamed from: b, reason: collision with root package name */
    private static final int f15539b = 65536;

    /* renamed from: c, reason: collision with root package name */
    private static final Segment f15540c = new Segment(new byte[0], 0, 0, false, false);

    /* renamed from: d, reason: collision with root package name */
    private static final int f15541d;

    /* renamed from: e, reason: collision with root package name */
    private static final AtomicReference<Segment>[] f15542e;

    static {
        int highestOneBit = Integer.highestOneBit((Runtime.getRuntime().availableProcessors() * 2) - 1);
        f15541d = highestOneBit;
        AtomicReference<Segment>[] atomicReferenceArr = new AtomicReference[highestOneBit];
        for (int i10 = 0; i10 < highestOneBit; i10++) {
            atomicReferenceArr[i10] = new AtomicReference<>();
        }
        f15542e = atomicReferenceArr;
    }

    private SegmentPool() {
    }

    private final AtomicReference<Segment> a() {
        return f15542e[(int) (Thread.currentThread().getId() & (f15541d - 1))];
    }

    public static final void b(Segment segment) {
        AtomicReference<Segment> a10;
        Segment segment2;
        Segment andSet;
        za.k.e(segment, "segment");
        if (segment.f15536f == null && segment.f15537g == null) {
            if (segment.f15534d || (andSet = (a10 = f15538a.a()).getAndSet((segment2 = f15540c))) == segment2) {
                return;
            }
            int i10 = andSet != null ? andSet.f15533c : 0;
            if (i10 >= f15539b) {
                a10.set(andSet);
                return;
            }
            segment.f15536f = andSet;
            segment.f15532b = 0;
            segment.f15533c = i10 + 8192;
            a10.set(segment);
            return;
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    public static final Segment c() {
        AtomicReference<Segment> a10 = f15538a.a();
        Segment segment = f15540c;
        Segment andSet = a10.getAndSet(segment);
        if (andSet == segment) {
            return new Segment();
        }
        if (andSet == null) {
            a10.set(null);
            return new Segment();
        }
        a10.set(andSet.f15536f);
        andSet.f15536f = null;
        andSet.f15533c = 0;
        return andSet;
    }
}
