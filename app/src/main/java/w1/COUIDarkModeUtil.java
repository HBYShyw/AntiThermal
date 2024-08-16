package w1;

import android.content.Context;
import android.view.View;

/* compiled from: COUIDarkModeUtil.java */
/* renamed from: w1.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIDarkModeUtil {
    public static boolean a(Context context) {
        return 32 == (context.getResources().getConfiguration().uiMode & 48);
    }

    public static void b(View view, boolean z10) {
        view.setForceDarkAllowed(z10);
    }
}
