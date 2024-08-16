package androidx.transition;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewOverlay;

/* compiled from: ViewOverlayApi18.java */
/* loaded from: classes.dex */
class b0 implements c0 {

    /* renamed from: a, reason: collision with root package name */
    private final ViewOverlay f4091a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b0(View view) {
        this.f4091a = view.getOverlay();
    }

    @Override // androidx.transition.c0
    public void add(Drawable drawable) {
        this.f4091a.add(drawable);
    }

    @Override // androidx.transition.c0
    public void remove(Drawable drawable) {
        this.f4091a.remove(drawable);
    }
}
