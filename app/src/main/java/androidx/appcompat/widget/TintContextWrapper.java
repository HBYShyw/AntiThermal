package androidx.appcompat.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* compiled from: TintContextWrapper.java */
/* renamed from: androidx.appcompat.widget.e0, reason: use source file name */
/* loaded from: classes.dex */
public class TintContextWrapper extends ContextWrapper {

    /* renamed from: c, reason: collision with root package name */
    private static final Object f1201c = new Object();

    /* renamed from: d, reason: collision with root package name */
    private static ArrayList<WeakReference<TintContextWrapper>> f1202d;

    /* renamed from: a, reason: collision with root package name */
    private final Resources f1203a;

    /* renamed from: b, reason: collision with root package name */
    private final Resources.Theme f1204b;

    private TintContextWrapper(Context context) {
        super(context);
        if (VectorEnabledTintResources.c()) {
            VectorEnabledTintResources vectorEnabledTintResources = new VectorEnabledTintResources(this, context.getResources());
            this.f1203a = vectorEnabledTintResources;
            Resources.Theme newTheme = vectorEnabledTintResources.newTheme();
            this.f1204b = newTheme;
            newTheme.setTo(context.getTheme());
            return;
        }
        this.f1203a = new TintResources(this, context.getResources());
        this.f1204b = null;
    }

    private static boolean a(Context context) {
        return ((context instanceof TintContextWrapper) || (context.getResources() instanceof TintResources) || (context.getResources() instanceof VectorEnabledTintResources) || !VectorEnabledTintResources.c()) ? false : true;
    }

    public static Context b(Context context) {
        if (!a(context)) {
            return context;
        }
        synchronized (f1201c) {
            ArrayList<WeakReference<TintContextWrapper>> arrayList = f1202d;
            if (arrayList == null) {
                f1202d = new ArrayList<>();
            } else {
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    WeakReference<TintContextWrapper> weakReference = f1202d.get(size);
                    if (weakReference == null || weakReference.get() == null) {
                        f1202d.remove(size);
                    }
                }
                for (int size2 = f1202d.size() - 1; size2 >= 0; size2--) {
                    WeakReference<TintContextWrapper> weakReference2 = f1202d.get(size2);
                    TintContextWrapper tintContextWrapper = weakReference2 != null ? weakReference2.get() : null;
                    if (tintContextWrapper != null && tintContextWrapper.getBaseContext() == context) {
                        return tintContextWrapper;
                    }
                }
            }
            TintContextWrapper tintContextWrapper2 = new TintContextWrapper(context);
            f1202d.add(new WeakReference<>(tintContextWrapper2));
            return tintContextWrapper2;
        }
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public AssetManager getAssets() {
        return this.f1203a.getAssets();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        return this.f1203a;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources.Theme getTheme() {
        Resources.Theme theme = this.f1204b;
        return theme == null ? super.getTheme() : theme;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setTheme(int i10) {
        Resources.Theme theme = this.f1204b;
        if (theme == null) {
            super.setTheme(i10);
        } else {
            theme.applyStyle(i10, true);
        }
    }
}
