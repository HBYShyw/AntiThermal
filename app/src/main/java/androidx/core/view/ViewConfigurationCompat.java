package androidx.core.view;

import android.content.Context;
import android.view.ViewConfiguration;

/* compiled from: ViewConfigurationCompat.java */
/* renamed from: androidx.core.view.a0, reason: use source file name */
/* loaded from: classes.dex */
public final class ViewConfigurationCompat {

    /* compiled from: ViewConfigurationCompat.java */
    /* renamed from: androidx.core.view.a0$a */
    /* loaded from: classes.dex */
    static class a {
        static float a(ViewConfiguration viewConfiguration) {
            return viewConfiguration.getScaledHorizontalScrollFactor();
        }

        static float b(ViewConfiguration viewConfiguration) {
            return viewConfiguration.getScaledVerticalScrollFactor();
        }
    }

    /* compiled from: ViewConfigurationCompat.java */
    /* renamed from: androidx.core.view.a0$b */
    /* loaded from: classes.dex */
    static class b {
        static int a(ViewConfiguration viewConfiguration) {
            return viewConfiguration.getScaledHoverSlop();
        }

        static boolean b(ViewConfiguration viewConfiguration) {
            return viewConfiguration.shouldShowMenuShortcutsWhenKeyboardPresent();
        }
    }

    public static float a(ViewConfiguration viewConfiguration, Context context) {
        return a.a(viewConfiguration);
    }

    public static float b(ViewConfiguration viewConfiguration, Context context) {
        return a.b(viewConfiguration);
    }

    public static boolean c(ViewConfiguration viewConfiguration, Context context) {
        return b.b(viewConfiguration);
    }
}
