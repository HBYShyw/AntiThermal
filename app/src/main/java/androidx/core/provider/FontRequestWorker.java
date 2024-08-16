package androidx.core.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.provider.FontsContractCompat;
import androidx.core.util.Consumer;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import j.LruCache;
import j.SimpleArrayMap;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FontRequestWorker.java */
/* renamed from: androidx.core.provider.f, reason: use source file name */
/* loaded from: classes.dex */
public class FontRequestWorker {

    /* renamed from: a, reason: collision with root package name */
    static final LruCache<String, Typeface> f2235a = new LruCache<>(16);

    /* renamed from: b, reason: collision with root package name */
    private static final ExecutorService f2236b = RequestExecutor.a("fonts-androidx", 10, DataLinkConstants.RUS_UPDATE);

    /* renamed from: c, reason: collision with root package name */
    static final Object f2237c = new Object();

    /* renamed from: d, reason: collision with root package name */
    static final SimpleArrayMap<String, ArrayList<Consumer<e>>> f2238d = new SimpleArrayMap<>();

    /* compiled from: FontRequestWorker.java */
    /* renamed from: androidx.core.provider.f$a */
    /* loaded from: classes.dex */
    class a implements Callable<e> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f2239a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ Context f2240b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ FontRequest f2241c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f2242d;

        a(String str, Context context, FontRequest fontRequest, int i10) {
            this.f2239a = str;
            this.f2240b = context;
            this.f2241c = fontRequest;
            this.f2242d = i10;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public e call() {
            return FontRequestWorker.c(this.f2239a, this.f2240b, this.f2241c, this.f2242d);
        }
    }

    /* compiled from: FontRequestWorker.java */
    /* renamed from: androidx.core.provider.f$b */
    /* loaded from: classes.dex */
    class b implements Consumer<e> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ CallbackWithHandler f2243a;

        b(CallbackWithHandler callbackWithHandler) {
            this.f2243a = callbackWithHandler;
        }

        @Override // androidx.core.util.Consumer
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public void accept(e eVar) {
            if (eVar == null) {
                eVar = new e(-3);
            }
            this.f2243a.b(eVar);
        }
    }

    /* compiled from: FontRequestWorker.java */
    /* renamed from: androidx.core.provider.f$c */
    /* loaded from: classes.dex */
    class c implements Callable<e> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f2244a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ Context f2245b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ FontRequest f2246c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f2247d;

        c(String str, Context context, FontRequest fontRequest, int i10) {
            this.f2244a = str;
            this.f2245b = context;
            this.f2246c = fontRequest;
            this.f2247d = i10;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public e call() {
            try {
                return FontRequestWorker.c(this.f2244a, this.f2245b, this.f2246c, this.f2247d);
            } catch (Throwable unused) {
                return new e(-3);
            }
        }
    }

    /* compiled from: FontRequestWorker.java */
    /* renamed from: androidx.core.provider.f$d */
    /* loaded from: classes.dex */
    class d implements Consumer<e> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f2248a;

        d(String str) {
            this.f2248a = str;
        }

        @Override // androidx.core.util.Consumer
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public void accept(e eVar) {
            synchronized (FontRequestWorker.f2237c) {
                SimpleArrayMap<String, ArrayList<Consumer<e>>> simpleArrayMap = FontRequestWorker.f2238d;
                ArrayList<Consumer<e>> arrayList = simpleArrayMap.get(this.f2248a);
                if (arrayList == null) {
                    return;
                }
                simpleArrayMap.remove(this.f2248a);
                for (int i10 = 0; i10 < arrayList.size(); i10++) {
                    arrayList.get(i10).accept(eVar);
                }
            }
        }
    }

    private static String a(FontRequest fontRequest, int i10) {
        return fontRequest.d() + "-" + i10;
    }

    @SuppressLint({"WrongConstant"})
    private static int b(FontsContractCompat.a aVar) {
        int i10 = 1;
        if (aVar.c() != 0) {
            return aVar.c() != 1 ? -3 : -2;
        }
        FontsContractCompat.b[] b10 = aVar.b();
        if (b10 != null && b10.length != 0) {
            i10 = 0;
            for (FontsContractCompat.b bVar : b10) {
                int b11 = bVar.b();
                if (b11 != 0) {
                    if (b11 < 0) {
                        return -3;
                    }
                    return b11;
                }
            }
        }
        return i10;
    }

    static e c(String str, Context context, FontRequest fontRequest, int i10) {
        LruCache<String, Typeface> lruCache = f2235a;
        Typeface c10 = lruCache.c(str);
        if (c10 != null) {
            return new e(c10);
        }
        try {
            FontsContractCompat.a e10 = FontProvider.e(context, fontRequest, null);
            int b10 = b(e10);
            if (b10 != 0) {
                return new e(b10);
            }
            Typeface b11 = TypefaceCompat.b(context, null, e10.b(), i10);
            if (b11 != null) {
                lruCache.d(str, b11);
                return new e(b11);
            }
            return new e(-3);
        } catch (PackageManager.NameNotFoundException unused) {
            return new e(-1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Typeface d(Context context, FontRequest fontRequest, int i10, Executor executor, CallbackWithHandler callbackWithHandler) {
        String a10 = a(fontRequest, i10);
        Typeface c10 = f2235a.c(a10);
        if (c10 != null) {
            callbackWithHandler.b(new e(c10));
            return c10;
        }
        b bVar = new b(callbackWithHandler);
        synchronized (f2237c) {
            SimpleArrayMap<String, ArrayList<Consumer<e>>> simpleArrayMap = f2238d;
            ArrayList<Consumer<e>> arrayList = simpleArrayMap.get(a10);
            if (arrayList != null) {
                arrayList.add(bVar);
                return null;
            }
            ArrayList<Consumer<e>> arrayList2 = new ArrayList<>();
            arrayList2.add(bVar);
            simpleArrayMap.put(a10, arrayList2);
            c cVar = new c(a10, context, fontRequest, i10);
            if (executor == null) {
                executor = f2236b;
            }
            RequestExecutor.b(executor, cVar, new d(a10));
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Typeface e(Context context, FontRequest fontRequest, CallbackWithHandler callbackWithHandler, int i10, int i11) {
        String a10 = a(fontRequest, i10);
        Typeface c10 = f2235a.c(a10);
        if (c10 != null) {
            callbackWithHandler.b(new e(c10));
            return c10;
        }
        if (i11 == -1) {
            e c11 = c(a10, context, fontRequest, i10);
            callbackWithHandler.b(c11);
            return c11.f2249a;
        }
        try {
            e eVar = (e) RequestExecutor.c(f2236b, new a(a10, context, fontRequest, i10), i11);
            callbackWithHandler.b(eVar);
            return eVar.f2249a;
        } catch (InterruptedException unused) {
            callbackWithHandler.b(new e(-3));
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FontRequestWorker.java */
    /* renamed from: androidx.core.provider.f$e */
    /* loaded from: classes.dex */
    public static final class e {

        /* renamed from: a, reason: collision with root package name */
        final Typeface f2249a;

        /* renamed from: b, reason: collision with root package name */
        final int f2250b;

        e(int i10) {
            this.f2249a = null;
            this.f2250b = i10;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @SuppressLint({"WrongConstant"})
        public boolean a() {
            return this.f2250b == 0;
        }

        @SuppressLint({"WrongConstant"})
        e(Typeface typeface) {
            this.f2249a = typeface;
            this.f2250b = 0;
        }
    }
}
