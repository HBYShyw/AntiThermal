package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import java.lang.ref.WeakReference;

/* compiled from: TintResources.java */
/* renamed from: androidx.appcompat.widget.g0, reason: use source file name */
/* loaded from: classes.dex */
class TintResources extends ResourcesWrapper {

    /* renamed from: b, reason: collision with root package name */
    private final WeakReference<Context> f1224b;

    public TintResources(Context context, Resources resources) {
        super(resources);
        this.f1224b = new WeakReference<>(context);
    }

    @Override // android.content.res.Resources
    public Drawable getDrawable(int i10) {
        Drawable a10 = a(i10);
        Context context = this.f1224b.get();
        if (a10 != null && context != null) {
            ResourceManagerInternal.g().w(context, i10, a10);
        }
        return a10;
    }
}
