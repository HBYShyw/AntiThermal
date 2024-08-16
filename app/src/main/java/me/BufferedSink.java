package me;

import java.nio.channels.WritableByteChannel;

/* compiled from: BufferedSink.kt */
/* renamed from: me.e, reason: use source file name */
/* loaded from: classes2.dex */
public interface BufferedSink extends Sink, WritableByteChannel {
    BufferedSink E(String str);

    BufferedSink F(g gVar);

    BufferedSink O(byte[] bArr, int i10, int i11);

    BufferedSink R(String str, int i10, int i11);

    BufferedSink T(long j10);

    d a();

    BufferedSink f0(byte[] bArr);

    @Override // me.Sink, java.io.Flushable
    void flush();

    BufferedSink k(int i10);

    BufferedSink p(int i10);

    BufferedSink t(int i10);
}
