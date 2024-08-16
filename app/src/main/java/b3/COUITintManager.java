package b3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import j.LruCache;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/* compiled from: COUITintManager.java */
/* renamed from: b3.a, reason: use source file name */
/* loaded from: classes.dex */
public final class COUITintManager {

    /* renamed from: c, reason: collision with root package name */
    public static final boolean f4549c = false;

    /* renamed from: d, reason: collision with root package name */
    private static final PorterDuff.Mode f4550d = PorterDuff.Mode.SRC_IN;

    /* renamed from: e, reason: collision with root package name */
    private static final WeakHashMap<Context, COUITintManager> f4551e = new WeakHashMap<>();

    /* renamed from: f, reason: collision with root package name */
    private static final a f4552f = new a(6);

    /* renamed from: a, reason: collision with root package name */
    private final WeakReference<Context> f4553a;

    /* renamed from: b, reason: collision with root package name */
    private SparseArray<ColorStateList> f4554b;

    /* compiled from: COUITintManager.java */
    /* renamed from: b3.a$a */
    /* loaded from: classes.dex */
    private static class a extends LruCache<Integer, PorterDuffColorFilter> {
        public a(int i10) {
            super(i10);
        }
    }

    private COUITintManager(Context context) {
        this.f4553a = new WeakReference<>(context);
    }

    public static COUITintManager a(Context context) {
        WeakHashMap<Context, COUITintManager> weakHashMap = f4551e;
        COUITintManager cOUITintManager = weakHashMap.get(context);
        if (cOUITintManager != null) {
            return cOUITintManager;
        }
        COUITintManager cOUITintManager2 = new COUITintManager(context);
        weakHashMap.put(context, cOUITintManager2);
        return cOUITintManager2;
    }

    public Drawable b(int i10) {
        return c(i10, false);
    }

    public Drawable c(int i10, boolean z10) {
        Context context = this.f4553a.get();
        if (context == null) {
            return null;
        }
        Drawable e10 = ContextCompat.e(context, i10);
        if (e10 != null) {
            e10 = e10.mutate();
            ColorStateList d10 = d(i10);
            if (d10 != null) {
                Drawable l10 = DrawableCompat.l(e10);
                DrawableCompat.i(l10, d10);
                PorterDuff.Mode e11 = e(i10);
                if (e11 == null) {
                    return l10;
                }
                DrawableCompat.j(l10, e11);
                return l10;
            }
            if (!f(i10, e10) && z10) {
                return null;
            }
        }
        return e10;
    }

    public final ColorStateList d(int i10) {
        if (this.f4553a.get() == null) {
            return null;
        }
        SparseArray<ColorStateList> sparseArray = this.f4554b;
        ColorStateList colorStateList = sparseArray != null ? sparseArray.get(i10) : null;
        if (colorStateList != null) {
            if (this.f4554b == null) {
                this.f4554b = new SparseArray<>();
            }
            this.f4554b.append(i10, colorStateList);
        }
        return colorStateList;
    }

    final PorterDuff.Mode e(int i10) {
        return null;
    }

    public final boolean f(int i10, Drawable drawable) {
        this.f4553a.get();
        return false;
    }
}
