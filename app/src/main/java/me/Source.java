package me;

import java.io.Closeable;

/* compiled from: Source.kt */
/* renamed from: me.a0, reason: use source file name */
/* loaded from: classes2.dex */
public interface Source extends Closeable {
    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();

    long read(d dVar, long j10);

    Timeout timeout();
}
