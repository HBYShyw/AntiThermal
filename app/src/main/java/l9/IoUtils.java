package l9;

import java.io.Closeable;
import java.io.IOException;

/* compiled from: IoUtils.java */
/* renamed from: l9.d, reason: use source file name */
/* loaded from: classes2.dex */
public class IoUtils {
    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e10) {
                LogUtils.b("IoUtils", "closeQuietly error :" + e10);
            }
        }
    }
}
