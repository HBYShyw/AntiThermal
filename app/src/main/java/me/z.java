package me;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: JvmOkio.kt */
/* loaded from: classes2.dex */
public final class z extends AsyncTimeout {

    /* renamed from: o, reason: collision with root package name */
    private final Socket f15545o;

    public z(Socket socket) {
        za.k.e(socket, "socket");
        this.f15545o = socket;
    }

    @Override // me.AsyncTimeout
    protected void B() {
        Logger logger;
        Logger logger2;
        try {
            this.f15545o.close();
        } catch (AssertionError e10) {
            if (n.c(e10)) {
                logger2 = o.f15511a;
                logger2.log(Level.WARNING, "Failed to close timed out socket " + this.f15545o, (Throwable) e10);
                return;
            }
            throw e10;
        } catch (Exception e11) {
            logger = o.f15511a;
            logger.log(Level.WARNING, "Failed to close timed out socket " + this.f15545o, (Throwable) e11);
        }
    }

    @Override // me.AsyncTimeout
    protected IOException x(IOException iOException) {
        SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
        if (iOException != null) {
            socketTimeoutException.initCause(iOException);
        }
        return socketTimeoutException;
    }
}
