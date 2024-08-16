package androidx.core.view.accessibility;

import android.view.accessibility.AccessibilityEvent;

/* compiled from: AccessibilityEventCompat.java */
/* renamed from: androidx.core.view.accessibility.b, reason: use source file name */
/* loaded from: classes.dex */
public final class AccessibilityEventCompat {

    /* compiled from: AccessibilityEventCompat.java */
    /* renamed from: androidx.core.view.accessibility.b$a */
    /* loaded from: classes.dex */
    static class a {
        static int a(AccessibilityEvent accessibilityEvent) {
            return accessibilityEvent.getContentChangeTypes();
        }

        static void b(AccessibilityEvent accessibilityEvent, int i10) {
            accessibilityEvent.setContentChangeTypes(i10);
        }
    }

    public static int a(AccessibilityEvent accessibilityEvent) {
        return a.a(accessibilityEvent);
    }

    public static void b(AccessibilityEvent accessibilityEvent, int i10) {
        a.b(accessibilityEvent, i10);
    }
}
