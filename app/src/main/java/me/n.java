package me;

import java.io.InputStream;
import java.net.Socket;

/* loaded from: classes2.dex */
public final class n {
    public static final BufferedSink a(Sink sink) {
        return Okio.a(sink);
    }

    public static final BufferedSource b(Source source) {
        return Okio.b(source);
    }

    public static final boolean c(AssertionError assertionError) {
        return o.b(assertionError);
    }

    public static final Sink d(Socket socket) {
        return o.c(socket);
    }

    public static final Source e(InputStream inputStream) {
        return o.d(inputStream);
    }

    public static final Source f(Socket socket) {
        return o.e(socket);
    }
}
