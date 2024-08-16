package androidx.transition;

import android.view.ViewGroup;

/* compiled from: Scene.java */
/* renamed from: androidx.transition.o, reason: use source file name */
/* loaded from: classes.dex */
public class Scene {

    /* renamed from: a, reason: collision with root package name */
    private ViewGroup f4124a;

    /* renamed from: b, reason: collision with root package name */
    private Runnable f4125b;

    public static Scene b(ViewGroup viewGroup) {
        return (Scene) viewGroup.getTag(R$id.transition_current_scene);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(ViewGroup viewGroup, Scene scene) {
        viewGroup.setTag(R$id.transition_current_scene, scene);
    }

    public void a() {
        Runnable runnable;
        if (b(this.f4124a) != this || (runnable = this.f4125b) == null) {
            return;
        }
        runnable.run();
    }
}
