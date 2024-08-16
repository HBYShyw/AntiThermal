package androidx.lifecycle;

import androidx.lifecycle.h;
import kotlin.Metadata;

/* compiled from: SavedStateHandleSupport.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u000b\u001a\u00020\b¢\u0006\u0004\b\f\u0010\rJ\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016R\u0014\u0010\u000b\u001a\u00020\b8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\t\u0010\n¨\u0006\u000e"}, d2 = {"Landroidx/lifecycle/SavedStateHandleAttacher;", "Landroidx/lifecycle/l;", "Landroidx/lifecycle/o;", "source", "Landroidx/lifecycle/h$b;", "event", "Lma/f0;", "a", "Landroidx/lifecycle/c0;", "e", "Landroidx/lifecycle/c0;", "provider", "<init>", "(Landroidx/lifecycle/c0;)V", "lifecycle-viewmodel-savedstate_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SavedStateHandleAttacher implements LifecycleEventObserver {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private final c0 provider;

    public SavedStateHandleAttacher(c0 c0Var) {
        za.k.e(c0Var, "provider");
        this.provider = c0Var;
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void a(o oVar, h.b bVar) {
        za.k.e(oVar, "source");
        za.k.e(bVar, "event");
        if (bVar == h.b.ON_CREATE) {
            oVar.getLifecycle().c(this);
            this.provider.d();
        } else {
            throw new IllegalStateException(("Next event must be ON_CREATE, it was " + bVar).toString());
        }
    }
}
