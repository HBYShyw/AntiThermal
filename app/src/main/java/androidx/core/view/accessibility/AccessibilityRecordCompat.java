package androidx.core.view.accessibility;

import android.view.View;
import android.view.accessibility.AccessibilityRecord;

/* compiled from: AccessibilityRecordCompat.java */
/* renamed from: androidx.core.view.accessibility.f, reason: use source file name */
/* loaded from: classes.dex */
public class AccessibilityRecordCompat {

    /* compiled from: AccessibilityRecordCompat.java */
    /* renamed from: androidx.core.view.accessibility.f$a */
    /* loaded from: classes.dex */
    static class a {
        static int a(AccessibilityRecord accessibilityRecord) {
            return accessibilityRecord.getMaxScrollX();
        }

        static int b(AccessibilityRecord accessibilityRecord) {
            return accessibilityRecord.getMaxScrollY();
        }

        static void c(AccessibilityRecord accessibilityRecord, int i10) {
            accessibilityRecord.setMaxScrollX(i10);
        }

        static void d(AccessibilityRecord accessibilityRecord, int i10) {
            accessibilityRecord.setMaxScrollY(i10);
        }
    }

    /* compiled from: AccessibilityRecordCompat.java */
    /* renamed from: androidx.core.view.accessibility.f$b */
    /* loaded from: classes.dex */
    static class b {
        static void a(AccessibilityRecord accessibilityRecord, View view, int i10) {
            accessibilityRecord.setSource(view, i10);
        }
    }

    public static void a(AccessibilityRecord accessibilityRecord, int i10) {
        a.c(accessibilityRecord, i10);
    }

    public static void b(AccessibilityRecord accessibilityRecord, int i10) {
        a.d(accessibilityRecord, i10);
    }

    public static void c(AccessibilityRecord accessibilityRecord, View view, int i10) {
        b.a(accessibilityRecord, view, i10);
    }
}
