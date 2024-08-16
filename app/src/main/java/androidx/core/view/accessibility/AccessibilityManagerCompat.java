package androidx.core.view.accessibility;

import android.view.accessibility.AccessibilityManager;

/* compiled from: AccessibilityManagerCompat.java */
/* renamed from: androidx.core.view.accessibility.c, reason: use source file name */
/* loaded from: classes.dex */
public final class AccessibilityManagerCompat {

    /* compiled from: AccessibilityManagerCompat.java */
    /* renamed from: androidx.core.view.accessibility.c$a */
    /* loaded from: classes.dex */
    static class a {
        static boolean a(AccessibilityManager accessibilityManager, b bVar) {
            return accessibilityManager.addTouchExplorationStateChangeListener(new c(bVar));
        }

        static boolean b(AccessibilityManager accessibilityManager, b bVar) {
            return accessibilityManager.removeTouchExplorationStateChangeListener(new c(bVar));
        }
    }

    /* compiled from: AccessibilityManagerCompat.java */
    /* renamed from: androidx.core.view.accessibility.c$b */
    /* loaded from: classes.dex */
    public interface b {
        void onTouchExplorationStateChanged(boolean z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AccessibilityManagerCompat.java */
    /* renamed from: androidx.core.view.accessibility.c$c */
    /* loaded from: classes.dex */
    public static final class c implements AccessibilityManager.TouchExplorationStateChangeListener {

        /* renamed from: a, reason: collision with root package name */
        final b f2314a;

        c(b bVar) {
            this.f2314a = bVar;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof c) {
                return this.f2314a.equals(((c) obj).f2314a);
            }
            return false;
        }

        public int hashCode() {
            return this.f2314a.hashCode();
        }

        @Override // android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener
        public void onTouchExplorationStateChanged(boolean z10) {
            this.f2314a.onTouchExplorationStateChanged(z10);
        }
    }

    public static boolean a(AccessibilityManager accessibilityManager, b bVar) {
        return a.a(accessibilityManager, bVar);
    }

    @Deprecated
    public static boolean b(AccessibilityManager accessibilityManager) {
        return accessibilityManager.isTouchExplorationEnabled();
    }

    public static boolean c(AccessibilityManager accessibilityManager, b bVar) {
        return a.b(accessibilityManager, bVar);
    }
}
