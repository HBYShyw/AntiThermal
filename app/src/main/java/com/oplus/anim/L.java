package com.oplus.anim;

import android.content.Context;
import androidx.core.os.TraceCompat;
import java.io.File;
import p4.DefaultEffectiveNetworkFetcher;
import p4.EffectiveNetworkCacheProvider;
import p4.EffectiveNetworkFetcher;
import p4.NetworkCache;
import p4.NetworkFetcher;

/* compiled from: L.java */
/* renamed from: com.oplus.anim.m, reason: use source file name */
/* loaded from: classes.dex */
public class L {

    /* renamed from: a, reason: collision with root package name */
    public static boolean f9733a = false;

    /* renamed from: b, reason: collision with root package name */
    private static boolean f9734b = false;

    /* renamed from: c, reason: collision with root package name */
    private static String[] f9735c;

    /* renamed from: d, reason: collision with root package name */
    private static long[] f9736d;

    /* renamed from: e, reason: collision with root package name */
    private static int f9737e;

    /* renamed from: f, reason: collision with root package name */
    private static int f9738f;

    /* renamed from: g, reason: collision with root package name */
    private static EffectiveNetworkFetcher f9739g;

    /* renamed from: h, reason: collision with root package name */
    private static EffectiveNetworkCacheProvider f9740h;

    /* renamed from: i, reason: collision with root package name */
    private static volatile NetworkFetcher f9741i;

    /* renamed from: j, reason: collision with root package name */
    private static volatile NetworkCache f9742j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: L.java */
    /* renamed from: com.oplus.anim.m$a */
    /* loaded from: classes.dex */
    public static class a implements EffectiveNetworkCacheProvider {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Context f9743a;

        a(Context context) {
            this.f9743a = context;
        }

        @Override // p4.EffectiveNetworkCacheProvider
        public File a() {
            return new File(this.f9743a.getCacheDir(), "anim_network_cache");
        }
    }

    public static void a(String str) {
        if (f9734b) {
            int i10 = f9737e;
            if (i10 == 20) {
                f9738f++;
                return;
            }
            f9735c[i10] = str;
            f9736d[i10] = System.nanoTime();
            TraceCompat.a(str);
            f9737e++;
        }
    }

    public static float b(String str) {
        int i10 = f9738f;
        if (i10 > 0) {
            f9738f = i10 - 1;
            return 0.0f;
        }
        if (!f9734b) {
            return 0.0f;
        }
        int i11 = f9737e - 1;
        f9737e = i11;
        if (i11 != -1) {
            if (str.equals(f9735c[i11])) {
                TraceCompat.b();
                return ((float) (System.nanoTime() - f9736d[f9737e])) / 1000000.0f;
            }
            throw new IllegalStateException("Unbalanced trace call " + str + ". Expected " + f9735c[f9737e] + ".");
        }
        throw new IllegalStateException("Can't end trace section. There are none.");
    }

    public static NetworkCache c(Context context) {
        Context applicationContext = context.getApplicationContext();
        NetworkCache networkCache = f9742j;
        if (networkCache == null) {
            synchronized (NetworkCache.class) {
                networkCache = f9742j;
                if (networkCache == null) {
                    EffectiveNetworkCacheProvider effectiveNetworkCacheProvider = f9740h;
                    if (effectiveNetworkCacheProvider == null) {
                        effectiveNetworkCacheProvider = new a(applicationContext);
                    }
                    networkCache = new NetworkCache(effectiveNetworkCacheProvider);
                    f9742j = networkCache;
                }
            }
        }
        return networkCache;
    }

    public static NetworkFetcher d(Context context) {
        NetworkFetcher networkFetcher = f9741i;
        if (networkFetcher == null) {
            synchronized (NetworkFetcher.class) {
                networkFetcher = f9741i;
                if (networkFetcher == null) {
                    NetworkCache c10 = c(context);
                    EffectiveNetworkFetcher effectiveNetworkFetcher = f9739g;
                    if (effectiveNetworkFetcher == null) {
                        effectiveNetworkFetcher = new DefaultEffectiveNetworkFetcher();
                    }
                    networkFetcher = new NetworkFetcher(c10, effectiveNetworkFetcher);
                    f9741i = networkFetcher;
                }
            }
        }
        return networkFetcher;
    }
}
