package androidx.lifecycle;

/* compiled from: DefaultLifecycleObserver.java */
/* renamed from: androidx.lifecycle.c, reason: use source file name */
/* loaded from: classes.dex */
public interface DefaultLifecycleObserver extends FullLifecycleObserver {
    @Override // androidx.lifecycle.FullLifecycleObserver
    default void onCreate(o oVar) {
    }

    @Override // androidx.lifecycle.FullLifecycleObserver
    default void onDestroy(o oVar) {
    }

    @Override // androidx.lifecycle.FullLifecycleObserver
    default void onPause(o oVar) {
    }

    @Override // androidx.lifecycle.FullLifecycleObserver
    default void onResume(o oVar) {
    }

    @Override // androidx.lifecycle.FullLifecycleObserver
    default void onStart(o oVar) {
    }

    @Override // androidx.lifecycle.FullLifecycleObserver
    default void onStop(o oVar) {
    }
}
