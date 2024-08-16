package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.io.IOException;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: ResourcesCompat.java */
/* renamed from: androidx.core.content.res.f, reason: use source file name */
/* loaded from: classes.dex */
public final class ResourcesCompat {

    /* renamed from: a, reason: collision with root package name */
    private static final ThreadLocal<TypedValue> f2160a = new ThreadLocal<>();

    /* renamed from: b, reason: collision with root package name */
    private static final WeakHashMap<d, SparseArray<c>> f2161b = new WeakHashMap<>(0);

    /* renamed from: c, reason: collision with root package name */
    private static final Object f2162c = new Object();

    /* compiled from: ResourcesCompat.java */
    /* renamed from: androidx.core.content.res.f$a */
    /* loaded from: classes.dex */
    static class a {
        static Drawable a(Resources resources, int i10, Resources.Theme theme) {
            return resources.getDrawable(i10, theme);
        }

        static Drawable b(Resources resources, int i10, int i11, Resources.Theme theme) {
            return resources.getDrawableForDensity(i10, i11, theme);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ResourcesCompat.java */
    /* renamed from: androidx.core.content.res.f$b */
    /* loaded from: classes.dex */
    public static class b {
        static int a(Resources resources, int i10, Resources.Theme theme) {
            return resources.getColor(i10, theme);
        }

        static ColorStateList b(Resources resources, int i10, Resources.Theme theme) {
            return resources.getColorStateList(i10, theme);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ResourcesCompat.java */
    /* renamed from: androidx.core.content.res.f$c */
    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: a, reason: collision with root package name */
        final ColorStateList f2163a;

        /* renamed from: b, reason: collision with root package name */
        final Configuration f2164b;

        /* renamed from: c, reason: collision with root package name */
        final int f2165c;

        c(ColorStateList colorStateList, Configuration configuration, Resources.Theme theme) {
            this.f2163a = colorStateList;
            this.f2164b = configuration;
            this.f2165c = theme == null ? 0 : theme.hashCode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ResourcesCompat.java */
    /* renamed from: androidx.core.content.res.f$d */
    /* loaded from: classes.dex */
    public static final class d {

        /* renamed from: a, reason: collision with root package name */
        final Resources f2166a;

        /* renamed from: b, reason: collision with root package name */
        final Resources.Theme f2167b;

        d(Resources resources, Resources.Theme theme) {
            this.f2166a = resources;
            this.f2167b = theme;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || d.class != obj.getClass()) {
                return false;
            }
            d dVar = (d) obj;
            return this.f2166a.equals(dVar.f2166a) && ObjectsCompat.a(this.f2167b, dVar.f2167b);
        }

        public int hashCode() {
            return ObjectsCompat.b(this.f2166a, this.f2167b);
        }
    }

    /* compiled from: ResourcesCompat.java */
    /* renamed from: androidx.core.content.res.f$e */
    /* loaded from: classes.dex */
    public static abstract class e {
        public static Handler e(Handler handler) {
            return handler == null ? new Handler(Looper.getMainLooper()) : handler;
        }

        public final void c(final int i10, Handler handler) {
            e(handler).post(new Runnable() { // from class: androidx.core.content.res.g
                @Override // java.lang.Runnable
                public final void run() {
                    ResourcesCompat.e.this.f(i10);
                }
            });
        }

        public final void d(final Typeface typeface, Handler handler) {
            e(handler).post(new Runnable() { // from class: androidx.core.content.res.h
                @Override // java.lang.Runnable
                public final void run() {
                    ResourcesCompat.e.this.g(typeface);
                }
            });
        }

        /* renamed from: h, reason: merged with bridge method [inline-methods] */
        public abstract void f(int i10);

        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public abstract void g(Typeface typeface);
    }

    /* compiled from: ResourcesCompat.java */
    /* renamed from: androidx.core.content.res.f$f */
    /* loaded from: classes.dex */
    public static final class f {

        /* compiled from: ResourcesCompat.java */
        /* renamed from: androidx.core.content.res.f$f$a */
        /* loaded from: classes.dex */
        static class a {
            static void a(Resources.Theme theme) {
                theme.rebase();
            }
        }

        public static void a(Resources.Theme theme) {
            a.a(theme);
        }
    }

    private static void a(d dVar, int i10, ColorStateList colorStateList, Resources.Theme theme) {
        synchronized (f2162c) {
            WeakHashMap<d, SparseArray<c>> weakHashMap = f2161b;
            SparseArray<c> sparseArray = weakHashMap.get(dVar);
            if (sparseArray == null) {
                sparseArray = new SparseArray<>();
                weakHashMap.put(dVar, sparseArray);
            }
            sparseArray.append(i10, new c(colorStateList, dVar.f2166a.getConfiguration(), theme));
        }
    }

    private static ColorStateList b(d dVar, int i10) {
        c cVar;
        Resources.Theme theme;
        synchronized (f2162c) {
            SparseArray<c> sparseArray = f2161b.get(dVar);
            if (sparseArray != null && sparseArray.size() > 0 && (cVar = sparseArray.get(i10)) != null) {
                if (cVar.f2164b.equals(dVar.f2166a.getConfiguration()) && (((theme = dVar.f2167b) == null && cVar.f2165c == 0) || (theme != null && cVar.f2165c == theme.hashCode()))) {
                    return cVar.f2163a;
                }
                sparseArray.remove(i10);
            }
            return null;
        }
    }

    public static Typeface c(Context context, int i10) {
        if (context.isRestricted()) {
            return null;
        }
        return n(context, i10, new TypedValue(), 0, null, null, false, true);
    }

    public static int d(Resources resources, int i10, Resources.Theme theme) {
        return b.a(resources, i10, theme);
    }

    public static ColorStateList e(Resources resources, int i10, Resources.Theme theme) {
        d dVar = new d(resources, theme);
        ColorStateList b10 = b(dVar, i10);
        if (b10 != null) {
            return b10;
        }
        ColorStateList l10 = l(resources, i10, theme);
        if (l10 != null) {
            a(dVar, i10, l10, theme);
            return l10;
        }
        return b.b(resources, i10, theme);
    }

    public static Drawable f(Resources resources, int i10, Resources.Theme theme) {
        return a.a(resources, i10, theme);
    }

    public static Drawable g(Resources resources, int i10, int i11, Resources.Theme theme) {
        return a.b(resources, i10, i11, theme);
    }

    public static Typeface h(Context context, int i10) {
        if (context.isRestricted()) {
            return null;
        }
        return n(context, i10, new TypedValue(), 0, null, null, false, false);
    }

    public static Typeface i(Context context, int i10, TypedValue typedValue, int i11, e eVar) {
        if (context.isRestricted()) {
            return null;
        }
        return n(context, i10, typedValue, i11, eVar, null, true, false);
    }

    public static void j(Context context, int i10, e eVar, Handler handler) {
        Preconditions.d(eVar);
        if (context.isRestricted()) {
            eVar.c(-4, handler);
        } else {
            n(context, i10, new TypedValue(), 0, eVar, handler, false, false);
        }
    }

    private static TypedValue k() {
        ThreadLocal<TypedValue> threadLocal = f2160a;
        TypedValue typedValue = threadLocal.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        threadLocal.set(typedValue2);
        return typedValue2;
    }

    private static ColorStateList l(Resources resources, int i10, Resources.Theme theme) {
        if (m(resources, i10)) {
            return null;
        }
        try {
            return ColorStateListInflaterCompat.a(resources, resources.getXml(i10), theme);
        } catch (Exception e10) {
            Log.w("ResourcesCompat", "Failed to inflate ColorStateList, leaving it to the framework", e10);
            return null;
        }
    }

    private static boolean m(Resources resources, int i10) {
        TypedValue k10 = k();
        resources.getValue(i10, k10, true);
        int i11 = k10.type;
        return i11 >= 28 && i11 <= 31;
    }

    private static Typeface n(Context context, int i10, TypedValue typedValue, int i11, e eVar, Handler handler, boolean z10, boolean z11) {
        Resources resources = context.getResources();
        resources.getValue(i10, typedValue, true);
        Typeface o10 = o(context, resources, typedValue, i10, i11, eVar, handler, z10, z11);
        if (o10 != null || eVar != null || z11) {
            return o10;
        }
        throw new Resources.NotFoundException("Font resource ID #0x" + Integer.toHexString(i10) + " could not be retrieved.");
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00b7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Typeface o(Context context, Resources resources, TypedValue typedValue, int i10, int i11, e eVar, Handler handler, boolean z10, boolean z11) {
        CharSequence charSequence = typedValue.string;
        if (charSequence != null) {
            String charSequence2 = charSequence.toString();
            if (!charSequence2.startsWith("res/")) {
                if (eVar != null) {
                    eVar.c(-3, handler);
                }
                return null;
            }
            Typeface f10 = TypefaceCompat.f(resources, i10, charSequence2, typedValue.assetCookie, i11);
            if (f10 != null) {
                if (eVar != null) {
                    eVar.d(f10, handler);
                }
                return f10;
            }
            if (z11) {
                return null;
            }
            try {
                if (charSequence2.toLowerCase().endsWith(".xml")) {
                    FontResourcesParserCompat.b b10 = FontResourcesParserCompat.b(resources.getXml(i10), resources);
                    if (b10 == null) {
                        Log.e("ResourcesCompat", "Failed to find font-family tag");
                        if (eVar != null) {
                            eVar.c(-3, handler);
                        }
                        return null;
                    }
                    return TypefaceCompat.c(context, b10, resources, i10, charSequence2, typedValue.assetCookie, i11, eVar, handler, z10);
                }
                Typeface d10 = TypefaceCompat.d(context, resources, i10, charSequence2, typedValue.assetCookie, i11);
                if (eVar != null) {
                    if (d10 != null) {
                        eVar.d(d10, handler);
                    } else {
                        eVar.c(-3, handler);
                    }
                }
                return d10;
            } catch (IOException e10) {
                Log.e("ResourcesCompat", "Failed to read xml resource " + charSequence2, e10);
                if (eVar != null) {
                    eVar.c(-3, handler);
                }
                return null;
            } catch (XmlPullParserException e11) {
                Log.e("ResourcesCompat", "Failed to parse xml resource " + charSequence2, e11);
                if (eVar != null) {
                }
                return null;
            }
        }
        throw new Resources.NotFoundException("Resource \"" + resources.getResourceName(i10) + "\" (" + Integer.toHexString(i10) + ") is not a Font: " + typedValue);
    }
}
