package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.provider.FontsContractCompat;
import j.LruCache;

/* compiled from: TypefaceCompat.java */
/* renamed from: androidx.core.graphics.e, reason: use source file name */
/* loaded from: classes.dex */
public class TypefaceCompat {

    /* renamed from: a, reason: collision with root package name */
    private static final TypefaceCompatBaseImpl f2205a = new TypefaceCompatApi29Impl();

    /* renamed from: b, reason: collision with root package name */
    private static final LruCache<String, Typeface> f2206b = new LruCache<>(16);

    /* compiled from: TypefaceCompat.java */
    /* renamed from: androidx.core.graphics.e$a */
    /* loaded from: classes.dex */
    public static class a extends FontsContractCompat.c {

        /* renamed from: a, reason: collision with root package name */
        private ResourcesCompat.e f2207a;

        public a(ResourcesCompat.e eVar) {
            this.f2207a = eVar;
        }

        @Override // androidx.core.provider.FontsContractCompat.c
        public void a(int i10) {
            ResourcesCompat.e eVar = this.f2207a;
            if (eVar != null) {
                eVar.f(i10);
            }
        }

        @Override // androidx.core.provider.FontsContractCompat.c
        public void b(Typeface typeface) {
            ResourcesCompat.e eVar = this.f2207a;
            if (eVar != null) {
                eVar.g(typeface);
            }
        }
    }

    public static Typeface a(Context context, Typeface typeface, int i10) {
        if (context != null) {
            return Typeface.create(typeface, i10);
        }
        throw new IllegalArgumentException("Context cannot be null");
    }

    public static Typeface b(Context context, CancellationSignal cancellationSignal, FontsContractCompat.b[] bVarArr, int i10) {
        return f2205a.b(context, cancellationSignal, bVarArr, i10);
    }

    public static Typeface c(Context context, FontResourcesParserCompat.b bVar, Resources resources, int i10, String str, int i11, int i12, ResourcesCompat.e eVar, Handler handler, boolean z10) {
        Typeface a10;
        if (bVar instanceof FontResourcesParserCompat.e) {
            FontResourcesParserCompat.e eVar2 = (FontResourcesParserCompat.e) bVar;
            Typeface g6 = g(eVar2.c());
            if (g6 != null) {
                if (eVar != null) {
                    eVar.d(g6, handler);
                }
                return g6;
            }
            boolean z11 = !z10 ? eVar != null : eVar2.a() != 0;
            int d10 = z10 ? eVar2.d() : -1;
            a10 = FontsContractCompat.c(context, eVar2.b(), i12, z11, d10, ResourcesCompat.e.e(handler), new a(eVar));
        } else {
            a10 = f2205a.a(context, (FontResourcesParserCompat.c) bVar, resources, i12);
            if (eVar != null) {
                if (a10 != null) {
                    eVar.d(a10, handler);
                } else {
                    eVar.c(-3, handler);
                }
            }
        }
        if (a10 != null) {
            f2206b.d(e(resources, i10, str, i11, i12), a10);
        }
        return a10;
    }

    public static Typeface d(Context context, Resources resources, int i10, String str, int i11, int i12) {
        Typeface c10 = f2205a.c(context, resources, i10, str, i12);
        if (c10 != null) {
            f2206b.d(e(resources, i10, str, i11, i12), c10);
        }
        return c10;
    }

    private static String e(Resources resources, int i10, String str, int i11, int i12) {
        return resources.getResourcePackageName(i10) + '-' + str + '-' + i11 + '-' + i10 + '-' + i12;
    }

    public static Typeface f(Resources resources, int i10, String str, int i11, int i12) {
        return f2206b.c(e(resources, i10, str, i11, i12));
    }

    private static Typeface g(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        Typeface create = Typeface.create(str, 0);
        Typeface create2 = Typeface.create(Typeface.DEFAULT, 0);
        if (create == null || create.equals(create2)) {
            return null;
        }
        return create;
    }
}
