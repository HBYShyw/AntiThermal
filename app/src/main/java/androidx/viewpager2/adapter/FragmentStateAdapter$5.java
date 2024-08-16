package androidx.viewpager2.adapter;

import android.os.Handler;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.h;
import androidx.lifecycle.o;

/* loaded from: classes.dex */
class FragmentStateAdapter$5 implements LifecycleEventObserver {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ Handler f4227e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ Runnable f4228f;

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void a(o oVar, h.b bVar) {
        if (bVar == h.b.ON_DESTROY) {
            this.f4227e.removeCallbacks(this.f4228f);
            oVar.getLifecycle().c(this);
        }
    }
}
