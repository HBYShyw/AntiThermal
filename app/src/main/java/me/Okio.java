package me;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Okio.kt */
/* renamed from: me.p, reason: use source file name */
/* loaded from: classes2.dex */
public final /* synthetic */ class Okio {
    public static final BufferedSink a(Sink sink) {
        za.k.e(sink, "<this>");
        return new RealBufferedSink(sink);
    }

    public static final BufferedSource b(Source source) {
        za.k.e(source, "<this>");
        return new RealBufferedSource(source);
    }
}
