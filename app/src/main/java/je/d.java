package je;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import kotlin.Metadata;

/* compiled from: AndroidLog.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\b\u0010\tJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\b\u0010\u0006\u001a\u00020\u0004H\u0016J\b\u0010\u0007\u001a\u00020\u0004H\u0016¨\u0006\n"}, d2 = {"Lje/d;", "Ljava/util/logging/Handler;", "Ljava/util/logging/LogRecord;", "record", "Lma/f0;", "publish", "flush", "close", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class d extends Handler {

    /* renamed from: a, reason: collision with root package name */
    public static final d f13913a = new d();

    private d() {
    }

    @Override // java.util.logging.Handler
    public void close() {
    }

    @Override // java.util.logging.Handler
    public void flush() {
    }

    @Override // java.util.logging.Handler
    public void publish(LogRecord logRecord) {
        int b10;
        za.k.e(logRecord, "record");
        c cVar = c.f13910a;
        String loggerName = logRecord.getLoggerName();
        za.k.d(loggerName, "record.loggerName");
        b10 = e.b(logRecord);
        String message = logRecord.getMessage();
        za.k.d(message, "record.message");
        cVar.a(loggerName, b10, message, logRecord.getThrown());
    }
}
