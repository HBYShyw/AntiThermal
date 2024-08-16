package me;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: JvmOkio.kt */
/* loaded from: classes2.dex */
public final /* synthetic */ class o {

    /* renamed from: a, reason: collision with root package name */
    private static final Logger f15511a = Logger.getLogger("okio.Okio");

    public static final boolean b(AssertionError assertionError) {
        za.k.e(assertionError, "<this>");
        if (assertionError.getCause() == null) {
            return false;
        }
        String message = assertionError.getMessage();
        return message != null ? sd.v.I(message, "getsockname failed", false, 2, null) : false;
    }

    public static final Sink c(Socket socket) {
        za.k.e(socket, "<this>");
        z zVar = new z(socket);
        OutputStream outputStream = socket.getOutputStream();
        za.k.d(outputStream, "getOutputStream()");
        return zVar.z(new r(outputStream, zVar));
    }

    public static final Source d(InputStream inputStream) {
        za.k.e(inputStream, "<this>");
        return new m(inputStream, new Timeout());
    }

    public static final Source e(Socket socket) {
        za.k.e(socket, "<this>");
        z zVar = new z(socket);
        InputStream inputStream = socket.getInputStream();
        za.k.d(inputStream, "getInputStream()");
        return zVar.A(new m(inputStream, zVar));
    }
}
