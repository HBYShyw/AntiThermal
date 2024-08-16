package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

/* compiled from: CanvasUtils.java */
/* renamed from: androidx.transition.b, reason: use source file name */
/* loaded from: classes.dex */
class CanvasUtils {
    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"SoonBlockedPrivateApi"})
    public static void a(Canvas canvas, boolean z10) {
        if (z10) {
            canvas.enableZ();
        } else {
            canvas.disableZ();
        }
    }
}
