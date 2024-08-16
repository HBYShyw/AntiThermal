package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import androidx.appcompat.resources.R$drawable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import g0.VectorDrawableCompat;
import j.LongSparseArray;
import j.LruCache;
import j.SimpleArrayMap;
import j.SparseArrayCompat;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: ResourceManagerInternal.java */
/* renamed from: androidx.appcompat.widget.y, reason: use source file name */
/* loaded from: classes.dex */
public final class ResourceManagerInternal {

    /* renamed from: i, reason: collision with root package name */
    private static ResourceManagerInternal f1345i;

    /* renamed from: a, reason: collision with root package name */
    private WeakHashMap<Context, SparseArrayCompat<ColorStateList>> f1347a;

    /* renamed from: b, reason: collision with root package name */
    private SimpleArrayMap<String, b> f1348b;

    /* renamed from: c, reason: collision with root package name */
    private SparseArrayCompat<String> f1349c;

    /* renamed from: d, reason: collision with root package name */
    private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable.ConstantState>>> f1350d = new WeakHashMap<>(0);

    /* renamed from: e, reason: collision with root package name */
    private TypedValue f1351e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f1352f;

    /* renamed from: g, reason: collision with root package name */
    private c f1353g;

    /* renamed from: h, reason: collision with root package name */
    private static final PorterDuff.Mode f1344h = PorterDuff.Mode.SRC_IN;

    /* renamed from: j, reason: collision with root package name */
    private static final a f1346j = new a(6);

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ResourceManagerInternal.java */
    /* renamed from: androidx.appcompat.widget.y$a */
    /* loaded from: classes.dex */
    public static class a extends LruCache<Integer, PorterDuffColorFilter> {
        public a(int i10) {
            super(i10);
        }

        private static int h(int i10, PorterDuff.Mode mode) {
            return ((i10 + 31) * 31) + mode.hashCode();
        }

        PorterDuffColorFilter i(int i10, PorterDuff.Mode mode) {
            return c(Integer.valueOf(h(i10, mode)));
        }

        PorterDuffColorFilter j(int i10, PorterDuff.Mode mode, PorterDuffColorFilter porterDuffColorFilter) {
            return d(Integer.valueOf(h(i10, mode)), porterDuffColorFilter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ResourceManagerInternal.java */
    /* renamed from: androidx.appcompat.widget.y$b */
    /* loaded from: classes.dex */
    public interface b {
        Drawable a(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme);
    }

    /* compiled from: ResourceManagerInternal.java */
    /* renamed from: androidx.appcompat.widget.y$c */
    /* loaded from: classes.dex */
    public interface c {
        boolean a(Context context, int i10, Drawable drawable);

        PorterDuff.Mode b(int i10);

        Drawable c(ResourceManagerInternal resourceManagerInternal, Context context, int i10);

        ColorStateList d(Context context, int i10);

        boolean e(Context context, int i10, Drawable drawable);
    }

    private synchronized boolean a(Context context, long j10, Drawable drawable) {
        Drawable.ConstantState constantState = drawable.getConstantState();
        if (constantState == null) {
            return false;
        }
        LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.f1350d.get(context);
        if (longSparseArray == null) {
            longSparseArray = new LongSparseArray<>();
            this.f1350d.put(context, longSparseArray);
        }
        longSparseArray.j(j10, new WeakReference<>(constantState));
        return true;
    }

    private void b(Context context, int i10, ColorStateList colorStateList) {
        if (this.f1347a == null) {
            this.f1347a = new WeakHashMap<>();
        }
        SparseArrayCompat<ColorStateList> sparseArrayCompat = this.f1347a.get(context);
        if (sparseArrayCompat == null) {
            sparseArrayCompat = new SparseArrayCompat<>();
            this.f1347a.put(context, sparseArrayCompat);
        }
        sparseArrayCompat.a(i10, colorStateList);
    }

    private void c(Context context) {
        if (this.f1352f) {
            return;
        }
        this.f1352f = true;
        Drawable i10 = i(context, R$drawable.abc_vector_test);
        if (i10 == null || !p(i10)) {
            this.f1352f = false;
            throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
        }
    }

    private static long d(TypedValue typedValue) {
        return (typedValue.assetCookie << 32) | typedValue.data;
    }

    private Drawable e(Context context, int i10) {
        if (this.f1351e == null) {
            this.f1351e = new TypedValue();
        }
        TypedValue typedValue = this.f1351e;
        context.getResources().getValue(i10, typedValue, true);
        long d10 = d(typedValue);
        Drawable h10 = h(context, d10);
        if (h10 != null) {
            return h10;
        }
        c cVar = this.f1353g;
        Drawable c10 = cVar == null ? null : cVar.c(this, context, i10);
        if (c10 != null) {
            c10.setChangingConfigurations(typedValue.changingConfigurations);
            a(context, d10, c10);
        }
        return c10;
    }

    private static PorterDuffColorFilter f(ColorStateList colorStateList, PorterDuff.Mode mode, int[] iArr) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return k(colorStateList.getColorForState(iArr, 0), mode);
    }

    public static synchronized ResourceManagerInternal g() {
        ResourceManagerInternal resourceManagerInternal;
        synchronized (ResourceManagerInternal.class) {
            if (f1345i == null) {
                ResourceManagerInternal resourceManagerInternal2 = new ResourceManagerInternal();
                f1345i = resourceManagerInternal2;
                o(resourceManagerInternal2);
            }
            resourceManagerInternal = f1345i;
        }
        return resourceManagerInternal;
    }

    private synchronized Drawable h(Context context, long j10) {
        LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.f1350d.get(context);
        if (longSparseArray == null) {
            return null;
        }
        WeakReference<Drawable.ConstantState> e10 = longSparseArray.e(j10);
        if (e10 != null) {
            Drawable.ConstantState constantState = e10.get();
            if (constantState != null) {
                return constantState.newDrawable(context.getResources());
            }
            longSparseArray.k(j10);
        }
        return null;
    }

    public static synchronized PorterDuffColorFilter k(int i10, PorterDuff.Mode mode) {
        PorterDuffColorFilter i11;
        synchronized (ResourceManagerInternal.class) {
            a aVar = f1346j;
            i11 = aVar.i(i10, mode);
            if (i11 == null) {
                i11 = new PorterDuffColorFilter(i10, mode);
                aVar.j(i10, mode, i11);
            }
        }
        return i11;
    }

    private ColorStateList m(Context context, int i10) {
        SparseArrayCompat<ColorStateList> sparseArrayCompat;
        WeakHashMap<Context, SparseArrayCompat<ColorStateList>> weakHashMap = this.f1347a;
        if (weakHashMap == null || (sparseArrayCompat = weakHashMap.get(context)) == null) {
            return null;
        }
        return sparseArrayCompat.e(i10);
    }

    private static void o(ResourceManagerInternal resourceManagerInternal) {
    }

    private static boolean p(Drawable drawable) {
        return (drawable instanceof VectorDrawableCompat) || "android.graphics.drawable.VectorDrawable".equals(drawable.getClass().getName());
    }

    private Drawable q(Context context, int i10) {
        int next;
        SimpleArrayMap<String, b> simpleArrayMap = this.f1348b;
        if (simpleArrayMap == null || simpleArrayMap.isEmpty()) {
            return null;
        }
        SparseArrayCompat<String> sparseArrayCompat = this.f1349c;
        if (sparseArrayCompat != null) {
            String e10 = sparseArrayCompat.e(i10);
            if ("appcompat_skip_skip".equals(e10) || (e10 != null && this.f1348b.get(e10) == null)) {
                return null;
            }
        } else {
            this.f1349c = new SparseArrayCompat<>();
        }
        if (this.f1351e == null) {
            this.f1351e = new TypedValue();
        }
        TypedValue typedValue = this.f1351e;
        Resources resources = context.getResources();
        resources.getValue(i10, typedValue, true);
        long d10 = d(typedValue);
        Drawable h10 = h(context, d10);
        if (h10 != null) {
            return h10;
        }
        CharSequence charSequence = typedValue.string;
        if (charSequence != null && charSequence.toString().endsWith(".xml")) {
            try {
                XmlResourceParser xml = resources.getXml(i10);
                AttributeSet asAttributeSet = Xml.asAttributeSet(xml);
                do {
                    next = xml.next();
                    if (next == 2) {
                        break;
                    }
                } while (next != 1);
                if (next == 2) {
                    String name = xml.getName();
                    this.f1349c.a(i10, name);
                    b bVar = this.f1348b.get(name);
                    if (bVar != null) {
                        h10 = bVar.a(context, xml, asAttributeSet, context.getTheme());
                    }
                    if (h10 != null) {
                        h10.setChangingConfigurations(typedValue.changingConfigurations);
                        a(context, d10, h10);
                    }
                } else {
                    throw new XmlPullParserException("No start tag found");
                }
            } catch (Exception e11) {
                Log.e("ResourceManagerInternal", "Exception while inflating drawable", e11);
            }
        }
        if (h10 == null) {
            this.f1349c.a(i10, "appcompat_skip_skip");
        }
        return h10;
    }

    private Drawable u(Context context, int i10, boolean z10, Drawable drawable) {
        ColorStateList l10 = l(context, i10);
        if (l10 != null) {
            if (t.a(drawable)) {
                drawable = drawable.mutate();
            }
            Drawable l11 = DrawableCompat.l(drawable);
            DrawableCompat.i(l11, l10);
            PorterDuff.Mode n10 = n(i10);
            if (n10 == null) {
                return l11;
            }
            DrawableCompat.j(l11, n10);
            return l11;
        }
        c cVar = this.f1353g;
        if ((cVar == null || !cVar.e(context, i10, drawable)) && !w(context, i10, drawable) && z10) {
            return null;
        }
        return drawable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void v(Drawable drawable, TintInfo tintInfo, int[] iArr) {
        int[] state = drawable.getState();
        if (t.a(drawable)) {
            if (!(drawable.mutate() == drawable)) {
                Log.d("ResourceManagerInternal", "Mutated drawable is not the same instance as the input.");
                return;
            }
        }
        if ((drawable instanceof LayerDrawable) && drawable.isStateful()) {
            drawable.setState(new int[0]);
            drawable.setState(state);
        }
        boolean z10 = tintInfo.f1214d;
        if (!z10 && !tintInfo.f1213c) {
            drawable.clearColorFilter();
        } else {
            drawable.setColorFilter(f(z10 ? tintInfo.f1211a : null, tintInfo.f1213c ? tintInfo.f1212b : f1344h, iArr));
        }
    }

    public synchronized Drawable i(Context context, int i10) {
        return j(context, i10, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Drawable j(Context context, int i10, boolean z10) {
        Drawable q10;
        c(context);
        q10 = q(context, i10);
        if (q10 == null) {
            q10 = e(context, i10);
        }
        if (q10 == null) {
            q10 = ContextCompat.e(context, i10);
        }
        if (q10 != null) {
            q10 = u(context, i10, z10, q10);
        }
        if (q10 != null) {
            t.b(q10);
        }
        return q10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ColorStateList l(Context context, int i10) {
        ColorStateList m10;
        m10 = m(context, i10);
        if (m10 == null) {
            c cVar = this.f1353g;
            m10 = cVar == null ? null : cVar.d(context, i10);
            if (m10 != null) {
                b(context, i10, m10);
            }
        }
        return m10;
    }

    PorterDuff.Mode n(int i10) {
        c cVar = this.f1353g;
        if (cVar == null) {
            return null;
        }
        return cVar.b(i10);
    }

    public synchronized void r(Context context) {
        LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.f1350d.get(context);
        if (longSparseArray != null) {
            longSparseArray.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Drawable s(Context context, VectorEnabledTintResources vectorEnabledTintResources, int i10) {
        Drawable q10 = q(context, i10);
        if (q10 == null) {
            q10 = vectorEnabledTintResources.a(i10);
        }
        if (q10 == null) {
            return null;
        }
        return u(context, i10, false, q10);
    }

    public synchronized void t(c cVar) {
        this.f1353g = cVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean w(Context context, int i10, Drawable drawable) {
        c cVar = this.f1353g;
        return cVar != null && cVar.a(context, i10, drawable);
    }
}
