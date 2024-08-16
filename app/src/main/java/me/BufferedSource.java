package me;

import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

/* compiled from: BufferedSource.kt */
/* renamed from: me.f, reason: use source file name */
/* loaded from: classes2.dex */
public interface BufferedSource extends Source, ReadableByteChannel {
    long I(g gVar);

    String J(Charset charset);

    byte M();

    void V(long j10);

    boolean W(long j10);

    String Z();

    d a();

    byte[] b0(long j10);

    d g();

    long g0(g gVar);

    g h(long j10);

    short k0();

    long l0(Sink sink);

    BufferedSource n0();

    int o();

    void p0(long j10);

    long r0();

    boolean s();

    InputStream s0();

    String x(long j10);

    int z(q qVar);
}
