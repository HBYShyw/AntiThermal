package androidx.transition;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

/* compiled from: ViewGroupOverlayApi18.java */
/* loaded from: classes.dex */
class y implements z {

    /* renamed from: a, reason: collision with root package name */
    private final ViewGroupOverlay f4168a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y(ViewGroup viewGroup) {
        this.f4168a = viewGroup.getOverlay();
    }

    @Override // androidx.transition.c0
    public void add(Drawable drawable) {
        this.f4168a.add(drawable);
    }

    @Override // androidx.transition.c0
    public void remove(Drawable drawable) {
        this.f4168a.remove(drawable);
    }

    @Override // androidx.transition.z
    public void add(View view) {
        this.f4168a.add(view);
    }

    @Override // androidx.transition.z
    public void remove(View view) {
        this.f4168a.remove(view);
    }
}
