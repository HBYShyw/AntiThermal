package me;

/* compiled from: PeekSource.kt */
/* renamed from: me.s, reason: use source file name */
/* loaded from: classes2.dex */
public final class PeekSource implements Source {

    /* renamed from: e, reason: collision with root package name */
    private final BufferedSource f15517e;

    /* renamed from: f, reason: collision with root package name */
    private final d f15518f;

    /* renamed from: g, reason: collision with root package name */
    private Segment f15519g;

    /* renamed from: h, reason: collision with root package name */
    private int f15520h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f15521i;

    /* renamed from: j, reason: collision with root package name */
    private long f15522j;

    public PeekSource(BufferedSource bufferedSource) {
        za.k.e(bufferedSource, "upstream");
        this.f15517e = bufferedSource;
        d a10 = bufferedSource.a();
        this.f15518f = a10;
        Segment segment = a10.f15484e;
        this.f15519g = segment;
        this.f15520h = segment != null ? segment.f15532b : -1;
    }

    @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f15521i = true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0028, code lost:
    
        if (r5 == r6.f15532b) goto L15;
     */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002d  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006f  */
    @Override // me.Source
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public long read(d dVar, long j10) {
        Segment segment;
        za.k.e(dVar, "sink");
        boolean z10 = false;
        if (j10 >= 0) {
            if (!this.f15521i) {
                Segment segment2 = this.f15519g;
                if (segment2 != null) {
                    Segment segment3 = this.f15518f.f15484e;
                    if (segment2 == segment3) {
                        int i10 = this.f15520h;
                        za.k.b(segment3);
                    }
                    if (z10) {
                        throw new IllegalStateException("Peek source is invalid because upstream source was used".toString());
                    }
                    if (j10 == 0) {
                        return 0L;
                    }
                    if (!this.f15517e.W(this.f15522j + 1)) {
                        return -1L;
                    }
                    if (this.f15519g == null && (segment = this.f15518f.f15484e) != null) {
                        this.f15519g = segment;
                        za.k.b(segment);
                        this.f15520h = segment.f15532b;
                    }
                    long min = Math.min(j10, this.f15518f.v0() - this.f15522j);
                    this.f15518f.w(dVar, this.f15522j, min);
                    this.f15522j += min;
                    return min;
                }
                z10 = true;
                if (z10) {
                }
            } else {
                throw new IllegalStateException("closed".toString());
            }
        } else {
            throw new IllegalArgumentException(("byteCount < 0: " + j10).toString());
        }
    }

    @Override // me.Source
    public Timeout timeout() {
        return this.f15517e.timeout();
    }
}
