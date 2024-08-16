package com.oplus.anim;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import l4.EffectiveCompositionCache;
import me.BufferedSource;
import q4.EffectiveCompositionParser;

/* compiled from: EffectiveCompositionFactory.java */
/* renamed from: com.oplus.anim.g, reason: use source file name */
/* loaded from: classes.dex */
public class EffectiveCompositionFactory {

    /* renamed from: a, reason: collision with root package name */
    private static final Map<String, EffectiveAnimationTask<EffectiveAnimationComposition>> f9710a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    private static final byte[] f9711b = {80, 75, 3, 4};

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveCompositionFactory.java */
    /* renamed from: com.oplus.anim.g$a */
    /* loaded from: classes.dex */
    public static class a implements Callable<EffectiveAnimationResult<EffectiveAnimationComposition>> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ EffectiveAnimationComposition f9712a;

        a(EffectiveAnimationComposition effectiveAnimationComposition) {
            this.f9712a = effectiveAnimationComposition;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public EffectiveAnimationResult<EffectiveAnimationComposition> call() {
            return new EffectiveAnimationResult<>(this.f9712a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveCompositionFactory.java */
    /* renamed from: com.oplus.anim.g$b */
    /* loaded from: classes.dex */
    public static class b implements EffectiveAnimationListener<EffectiveAnimationComposition> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f9713a;

        b(String str) {
            this.f9713a = str;
        }

        @Override // com.oplus.anim.EffectiveAnimationListener
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(EffectiveAnimationComposition effectiveAnimationComposition) {
            EffectiveCompositionFactory.f9710a.remove(this.f9713a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveCompositionFactory.java */
    /* renamed from: com.oplus.anim.g$c */
    /* loaded from: classes.dex */
    public static class c implements EffectiveAnimationListener<Throwable> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f9714a;

        c(String str) {
            this.f9714a = str;
        }

        @Override // com.oplus.anim.EffectiveAnimationListener
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(Throwable th) {
            EffectiveCompositionFactory.f9710a.remove(this.f9714a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveCompositionFactory.java */
    /* renamed from: com.oplus.anim.g$d */
    /* loaded from: classes.dex */
    public static class d implements Callable<EffectiveAnimationResult<EffectiveAnimationComposition>> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Context f9715a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ String f9716b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ String f9717c;

        d(Context context, String str, String str2) {
            this.f9715a = context;
            this.f9716b = str;
            this.f9717c = str2;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public EffectiveAnimationResult<EffectiveAnimationComposition> call() {
            EffectiveAnimationResult<EffectiveAnimationComposition> c10 = L.d(this.f9715a).c(this.f9716b, this.f9717c);
            if (this.f9717c != null && c10.b() != null) {
                EffectiveCompositionCache.b().c(this.f9717c, c10.b());
            }
            return c10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveCompositionFactory.java */
    /* renamed from: com.oplus.anim.g$e */
    /* loaded from: classes.dex */
    public static class e implements Callable<EffectiveAnimationResult<EffectiveAnimationComposition>> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Context f9718a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ String f9719b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ String f9720c;

        e(Context context, String str, String str2) {
            this.f9718a = context;
            this.f9719b = str;
            this.f9720c = str2;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public EffectiveAnimationResult<EffectiveAnimationComposition> call() {
            return EffectiveCompositionFactory.g(this.f9718a, this.f9719b, this.f9720c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveCompositionFactory.java */
    /* renamed from: com.oplus.anim.g$f */
    /* loaded from: classes.dex */
    public static class f implements Callable<EffectiveAnimationResult<EffectiveAnimationComposition>> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ WeakReference f9721a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ Context f9722b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f9723c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ String f9724d;

        f(WeakReference weakReference, Context context, int i10, String str) {
            this.f9721a = weakReference;
            this.f9722b = context;
            this.f9723c = i10;
            this.f9724d = str;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public EffectiveAnimationResult<EffectiveAnimationComposition> call() {
            Context context = (Context) this.f9721a.get();
            if (context == null) {
                context = this.f9722b;
            }
            return EffectiveCompositionFactory.p(context, this.f9723c, this.f9724d);
        }
    }

    /* compiled from: EffectiveCompositionFactory.java */
    /* renamed from: com.oplus.anim.g$g */
    /* loaded from: classes.dex */
    static class g implements Callable<EffectiveAnimationResult<EffectiveAnimationComposition>> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ InputStream f9725a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ String f9726b;

        g(InputStream inputStream, String str) {
            this.f9725a = inputStream;
            this.f9726b = str;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public EffectiveAnimationResult<EffectiveAnimationComposition> call() {
            return EffectiveCompositionFactory.i(this.f9725a, this.f9726b);
        }
    }

    private static EffectiveAnimationTask<EffectiveAnimationComposition> b(String str, Callable<EffectiveAnimationResult<EffectiveAnimationComposition>> callable) {
        EffectiveAnimationComposition a10 = str == null ? null : EffectiveCompositionCache.b().a(str);
        float f10 = Resources.getSystem().getDisplayMetrics().density;
        if (a10 != null && a10.d() == f10) {
            s4.e.a("EffectiveCompositionFactory::cached Composition isn't null, cacheKey is " + str);
            return new EffectiveAnimationTask<>(new a(a10));
        }
        if (a10 != null && a10.d() != f10) {
            s4.e.a("EffectiveCompositionFactory::cachedComposition density = " + a10.d() + "; curDensity = " + f10);
        }
        if (str != null) {
            Map<String, EffectiveAnimationTask<EffectiveAnimationComposition>> map = f9710a;
            if (map.containsKey(str)) {
                return map.get(str);
            }
        }
        EffectiveAnimationTask<EffectiveAnimationComposition> effectiveAnimationTask = new EffectiveAnimationTask<>(callable);
        if (str != null) {
            effectiveAnimationTask.f(new b(str));
            effectiveAnimationTask.e(new c(str));
            f9710a.put(str, effectiveAnimationTask);
        }
        return effectiveAnimationTask;
    }

    private static EffectiveImageAsset c(EffectiveAnimationComposition effectiveAnimationComposition, String str) {
        for (EffectiveImageAsset effectiveImageAsset : effectiveAnimationComposition.k().values()) {
            if (effectiveImageAsset.b().equals(str)) {
                return effectiveImageAsset;
            }
        }
        return null;
    }

    public static EffectiveAnimationTask<EffectiveAnimationComposition> d(Context context, String str) {
        return e(context, str, "asset_" + str);
    }

    public static EffectiveAnimationTask<EffectiveAnimationComposition> e(Context context, String str, String str2) {
        return b(str2, new e(context.getApplicationContext(), str, str2));
    }

    public static EffectiveAnimationResult<EffectiveAnimationComposition> f(Context context, String str) {
        return g(context, str, "asset_" + str);
    }

    public static EffectiveAnimationResult<EffectiveAnimationComposition> g(Context context, String str, String str2) {
        try {
            if (!str.endsWith(".zip") && !str.endsWith(".lottie")) {
                return i(context.getAssets().open(str), str2);
            }
            return s(new ZipInputStream(context.getAssets().open(str)), str2);
        } catch (IOException e10) {
            return new EffectiveAnimationResult<>((Throwable) e10);
        }
    }

    public static EffectiveAnimationTask<EffectiveAnimationComposition> h(InputStream inputStream, String str) {
        return b(str, new g(inputStream, str));
    }

    public static EffectiveAnimationResult<EffectiveAnimationComposition> i(InputStream inputStream, String str) {
        return j(inputStream, str, true);
    }

    private static EffectiveAnimationResult<EffectiveAnimationComposition> j(InputStream inputStream, String str, boolean z10) {
        try {
            return k(r4.c.a0(me.n.b(me.n.e(inputStream))), str);
        } finally {
            if (z10) {
                s4.h.c(inputStream);
            }
        }
    }

    public static EffectiveAnimationResult<EffectiveAnimationComposition> k(r4.c cVar, String str) {
        return l(cVar, str, true);
    }

    private static EffectiveAnimationResult<EffectiveAnimationComposition> l(r4.c cVar, String str, boolean z10) {
        try {
            try {
                EffectiveAnimationComposition a10 = EffectiveCompositionParser.a(cVar);
                if (str != null) {
                    EffectiveCompositionCache.b().c(str, a10);
                }
                EffectiveAnimationResult<EffectiveAnimationComposition> effectiveAnimationResult = new EffectiveAnimationResult<>(a10);
                if (z10) {
                    s4.h.c(cVar);
                }
                return effectiveAnimationResult;
            } catch (Exception e10) {
                EffectiveAnimationResult<EffectiveAnimationComposition> effectiveAnimationResult2 = new EffectiveAnimationResult<>(e10);
                if (z10) {
                    s4.h.c(cVar);
                }
                return effectiveAnimationResult2;
            }
        } catch (Throwable th) {
            if (z10) {
                s4.h.c(cVar);
            }
            throw th;
        }
    }

    public static EffectiveAnimationTask<EffectiveAnimationComposition> m(Context context, int i10) {
        return n(context, i10, w(context, i10));
    }

    public static EffectiveAnimationTask<EffectiveAnimationComposition> n(Context context, int i10, String str) {
        return b(str, new f(new WeakReference(context), context.getApplicationContext(), i10, str));
    }

    public static EffectiveAnimationResult<EffectiveAnimationComposition> o(Context context, int i10) {
        return p(context, i10, w(context, i10));
    }

    public static EffectiveAnimationResult<EffectiveAnimationComposition> p(Context context, int i10, String str) {
        try {
            BufferedSource b10 = me.n.b(me.n.e(context.getResources().openRawResource(i10)));
            if (v(b10).booleanValue()) {
                return s(new ZipInputStream(b10.s0()), str);
            }
            return i(b10.s0(), str);
        } catch (Resources.NotFoundException e10) {
            return new EffectiveAnimationResult<>((Throwable) e10);
        }
    }

    public static EffectiveAnimationTask<EffectiveAnimationComposition> q(Context context, String str) {
        return r(context, str, "url_" + str);
    }

    public static EffectiveAnimationTask<EffectiveAnimationComposition> r(Context context, String str, String str2) {
        return b(str2, new d(context, str, str2));
    }

    public static EffectiveAnimationResult<EffectiveAnimationComposition> s(ZipInputStream zipInputStream, String str) {
        s4.e.a("EffectiveCompositionFactory::fromZipStreamSync cacheKey = " + str);
        try {
            return t(zipInputStream, str, null);
        } finally {
            s4.h.c(zipInputStream);
        }
    }

    private static EffectiveAnimationResult<EffectiveAnimationComposition> t(ZipInputStream zipInputStream, String str, BitmapFactory.Options options) {
        HashMap hashMap = new HashMap();
        s4.e.a("EffectiveCompositionFactory::fromZipStreamSyncInternal cacheKey = " + str);
        try {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("EffectiveCompositionFactory::fromZipStreamSyncInternal entry == null ? ");
            sb2.append(nextEntry == null);
            s4.e.a(sb2.toString());
            EffectiveAnimationComposition effectiveAnimationComposition = null;
            while (nextEntry != null) {
                s4.e.a("EffectiveCompositionFactory::fromZipStreamSyncInternal entry.getName() = " + nextEntry.getName());
                String name = nextEntry.getName();
                if (!name.endsWith("__MACOSX") && !name.endsWith("../")) {
                    if (nextEntry.getName().equalsIgnoreCase("manifest.json")) {
                        zipInputStream.closeEntry();
                    } else if (nextEntry.getName().endsWith(".json")) {
                        effectiveAnimationComposition = l(r4.c.a0(me.n.b(me.n.e(zipInputStream))), null, false).b();
                    } else {
                        if (!name.endsWith(".png") && !name.endsWith(".webp") && !name.endsWith(".jpg") && !name.endsWith(".jpeg")) {
                            zipInputStream.closeEntry();
                        }
                        String[] split = name.split("/");
                        hashMap.put(split[split.length - 1], BitmapFactory.decodeStream(zipInputStream, null, options));
                    }
                    nextEntry = zipInputStream.getNextEntry();
                }
                zipInputStream.closeEntry();
                nextEntry = zipInputStream.getNextEntry();
            }
            if (effectiveAnimationComposition == null) {
                return new EffectiveAnimationResult<>((Throwable) new IllegalArgumentException("Unable to parse composition"));
            }
            for (Map.Entry entry : hashMap.entrySet()) {
                EffectiveImageAsset c10 = c(effectiveAnimationComposition, (String) entry.getKey());
                if (c10 != null) {
                    c10.f(s4.h.m((Bitmap) entry.getValue(), c10.e(), c10.c()));
                }
            }
            for (Map.Entry<String, EffectiveImageAsset> entry2 : effectiveAnimationComposition.k().entrySet()) {
                if (entry2.getValue().a() == null) {
                    return new EffectiveAnimationResult<>((Throwable) new IllegalStateException("There is no image for " + entry2.getValue().b()));
                }
            }
            if (str != null) {
                EffectiveCompositionCache.b().c(str, effectiveAnimationComposition);
            }
            return new EffectiveAnimationResult<>(effectiveAnimationComposition);
        } catch (IOException e10) {
            return new EffectiveAnimationResult<>((Throwable) e10);
        }
    }

    private static boolean u(Context context) {
        return (context.getResources().getConfiguration().uiMode & 48) == 32;
    }

    private static Boolean v(BufferedSource bufferedSource) {
        try {
            BufferedSource n02 = bufferedSource.n0();
            for (byte b10 : f9711b) {
                if (n02.M() != b10) {
                    return Boolean.FALSE;
                }
            }
            n02.close();
            return Boolean.TRUE;
        } catch (Exception e10) {
            s4.e.b("Failed to check zip file header", e10);
            return Boolean.FALSE;
        }
    }

    private static String w(Context context, int i10) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("rawRes");
        sb2.append(u(context) ? "_night_" : "_day_");
        sb2.append(i10);
        return sb2.toString();
    }
}
