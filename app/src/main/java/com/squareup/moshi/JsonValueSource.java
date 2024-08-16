package com.squareup.moshi;

import me.BufferedSource;
import me.Source;
import me.Timeout;
import me.d;
import me.g;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class JsonValueSource implements Source {
    private final d buffer;
    private boolean closed;
    private long limit;
    private final d prefix;
    private final BufferedSource source;
    private int stackSize;
    private g state;
    static final g STATE_JSON = g.d("[]{}\"'/#");
    static final g STATE_SINGLE_QUOTED = g.d("'\\");
    static final g STATE_DOUBLE_QUOTED = g.d("\"\\");
    static final g STATE_END_OF_LINE_COMMENT = g.d("\r\n");
    static final g STATE_C_STYLE_COMMENT = g.d("*");
    static final g STATE_END_OF_JSON = g.f15494i;

    JsonValueSource(BufferedSource bufferedSource) {
        this(bufferedSource, new d(), STATE_JSON, 0);
    }

    private void advanceLimit(long j10) {
        while (true) {
            long j11 = this.limit;
            if (j11 >= j10) {
                return;
            }
            g gVar = this.state;
            g gVar2 = STATE_END_OF_JSON;
            if (gVar == gVar2) {
                return;
            }
            if (j11 == this.buffer.v0()) {
                if (this.limit > 0) {
                    return;
                } else {
                    this.source.p0(1L);
                }
            }
            long U = this.buffer.U(this.state, this.limit);
            if (U == -1) {
                this.limit = this.buffer.v0();
            } else {
                byte L = this.buffer.L(U);
                g gVar3 = this.state;
                g gVar4 = STATE_JSON;
                if (gVar3 == gVar4) {
                    if (L == 34) {
                        this.state = STATE_DOUBLE_QUOTED;
                        this.limit = U + 1;
                    } else if (L == 35) {
                        this.state = STATE_END_OF_LINE_COMMENT;
                        this.limit = U + 1;
                    } else if (L == 39) {
                        this.state = STATE_SINGLE_QUOTED;
                        this.limit = U + 1;
                    } else if (L != 47) {
                        if (L != 91) {
                            if (L != 93) {
                                if (L != 123) {
                                    if (L != 125) {
                                    }
                                }
                            }
                            int i10 = this.stackSize - 1;
                            this.stackSize = i10;
                            if (i10 == 0) {
                                this.state = gVar2;
                            }
                            this.limit = U + 1;
                        }
                        this.stackSize++;
                        this.limit = U + 1;
                    } else {
                        long j12 = 2 + U;
                        this.source.p0(j12);
                        long j13 = U + 1;
                        byte L2 = this.buffer.L(j13);
                        if (L2 == 47) {
                            this.state = STATE_END_OF_LINE_COMMENT;
                            this.limit = j12;
                        } else if (L2 == 42) {
                            this.state = STATE_C_STYLE_COMMENT;
                            this.limit = j12;
                        } else {
                            this.limit = j13;
                        }
                    }
                } else if (gVar3 == STATE_SINGLE_QUOTED || gVar3 == STATE_DOUBLE_QUOTED) {
                    if (L == 92) {
                        long j14 = U + 2;
                        this.source.p0(j14);
                        this.limit = j14;
                    } else {
                        if (this.stackSize > 0) {
                            gVar2 = gVar4;
                        }
                        this.state = gVar2;
                        this.limit = U + 1;
                    }
                } else if (gVar3 == STATE_C_STYLE_COMMENT) {
                    long j15 = 2 + U;
                    this.source.p0(j15);
                    long j16 = U + 1;
                    if (this.buffer.L(j16) == 47) {
                        this.limit = j15;
                        this.state = gVar4;
                    } else {
                        this.limit = j16;
                    }
                } else if (gVar3 == STATE_END_OF_LINE_COMMENT) {
                    this.limit = U + 1;
                    this.state = gVar4;
                } else {
                    throw new AssertionError();
                }
            }
        }
    }

    @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.closed = true;
    }

    public void discard() {
        this.closed = true;
        while (this.state != STATE_END_OF_JSON) {
            advanceLimit(8192L);
            this.source.V(this.limit);
        }
    }

    @Override // me.Source
    public long read(d dVar, long j10) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (j10 == 0) {
            return 0L;
        }
        if (!this.prefix.s()) {
            long read = this.prefix.read(dVar, j10);
            long j11 = j10 - read;
            if (this.buffer.s()) {
                return read;
            }
            long read2 = read(dVar, j11);
            return read2 != -1 ? read + read2 : read;
        }
        advanceLimit(j10);
        long j12 = this.limit;
        if (j12 == 0) {
            if (this.state == STATE_END_OF_JSON) {
                return -1L;
            }
            throw new AssertionError();
        }
        long min = Math.min(j10, j12);
        dVar.write(this.buffer, min);
        this.limit -= min;
        return min;
    }

    @Override // me.Source
    public Timeout timeout() {
        return this.source.timeout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JsonValueSource(BufferedSource bufferedSource, d dVar, g gVar, int i10) {
        this.limit = 0L;
        this.closed = false;
        this.source = bufferedSource;
        this.buffer = bufferedSource.a();
        this.prefix = dVar;
        this.state = gVar;
        this.stackSize = i10;
    }
}
