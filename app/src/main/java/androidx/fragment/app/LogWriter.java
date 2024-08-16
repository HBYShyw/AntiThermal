package androidx.fragment.app;

import android.util.Log;
import java.io.Writer;

/* compiled from: LogWriter.java */
/* renamed from: androidx.fragment.app.w, reason: use source file name */
/* loaded from: classes.dex */
final class LogWriter extends Writer {

    /* renamed from: e, reason: collision with root package name */
    private final String f3041e;

    /* renamed from: f, reason: collision with root package name */
    private StringBuilder f3042f = new StringBuilder(128);

    /* JADX INFO: Access modifiers changed from: package-private */
    public LogWriter(String str) {
        this.f3041e = str;
    }

    private void b() {
        if (this.f3042f.length() > 0) {
            Log.d(this.f3041e, this.f3042f.toString());
            StringBuilder sb2 = this.f3042f;
            sb2.delete(0, sb2.length());
        }
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        b();
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
        b();
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i10, int i11) {
        for (int i12 = 0; i12 < i11; i12++) {
            char c10 = cArr[i10 + i12];
            if (c10 == '\n') {
                b();
            } else {
                this.f3042f.append(c10);
            }
        }
    }
}
