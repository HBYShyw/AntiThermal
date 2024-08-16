package me;

import java.io.Closeable;
import java.io.Flushable;

/* compiled from: Sink.kt */
/* renamed from: me.y, reason: use source file name */
/* loaded from: classes2.dex */
public interface Sink extends Closeable, Flushable {
    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();

    void flush();

    Timeout timeout();

    void write(d dVar, long j10);
}
