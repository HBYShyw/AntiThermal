package p4;

import android.util.Pair;
import com.oplus.anim.EffectiveAnimationComposition;
import com.oplus.anim.EffectiveAnimationResult;
import com.oplus.anim.EffectiveCompositionFactory;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/* compiled from: NetworkFetcher.java */
/* renamed from: p4.h, reason: use source file name */
/* loaded from: classes.dex */
public class NetworkFetcher {

    /* renamed from: a, reason: collision with root package name */
    private final NetworkCache f16584a;

    /* renamed from: b, reason: collision with root package name */
    private final EffectiveNetworkFetcher f16585b;

    public NetworkFetcher(NetworkCache networkCache, EffectiveNetworkFetcher effectiveNetworkFetcher) {
        this.f16584a = networkCache;
        this.f16585b = effectiveNetworkFetcher;
    }

    private EffectiveAnimationComposition a(String str, String str2) {
        Pair<FileExtension, InputStream> a10;
        EffectiveAnimationResult<EffectiveAnimationComposition> i10;
        if (str2 == null || (a10 = this.f16584a.a(str)) == null) {
            return null;
        }
        FileExtension fileExtension = (FileExtension) a10.first;
        InputStream inputStream = (InputStream) a10.second;
        if (fileExtension == FileExtension.ZIP) {
            i10 = EffectiveCompositionFactory.s(new ZipInputStream(inputStream), str);
        } else {
            i10 = EffectiveCompositionFactory.i(inputStream, str);
        }
        if (i10.b() != null) {
            return i10.b();
        }
        return null;
    }

    private EffectiveAnimationResult<EffectiveAnimationComposition> b(String str, String str2) {
        s4.e.a("Fetching " + str);
        Closeable closeable = null;
        try {
            try {
                EffectiveFetchResult a10 = this.f16585b.a(str);
                if (a10.c0()) {
                    EffectiveAnimationResult<EffectiveAnimationComposition> d10 = d(str, a10.Q(), a10.D(), str2);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Completed fetch from network. Success: ");
                    sb2.append(d10.b() != null);
                    s4.e.a(sb2.toString());
                    try {
                        a10.close();
                    } catch (IOException e10) {
                        s4.e.d("EffectiveFetchResult close failed ", e10);
                    }
                    return d10;
                }
                EffectiveAnimationResult<EffectiveAnimationComposition> effectiveAnimationResult = new EffectiveAnimationResult<>(new IllegalArgumentException(a10.G()));
                try {
                    a10.close();
                } catch (IOException e11) {
                    s4.e.d("EffectiveFetchResult close failed ", e11);
                }
                return effectiveAnimationResult;
            } catch (Exception e12) {
                EffectiveAnimationResult<EffectiveAnimationComposition> effectiveAnimationResult2 = new EffectiveAnimationResult<>(e12);
                if (0 != 0) {
                    try {
                        closeable.close();
                    } catch (IOException e13) {
                        s4.e.d("EffectiveFetchResult close failed ", e13);
                    }
                }
                return effectiveAnimationResult2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    closeable.close();
                } catch (IOException e14) {
                    s4.e.d("EffectiveFetchResult close failed ", e14);
                }
            }
            throw th;
        }
    }

    private EffectiveAnimationResult<EffectiveAnimationComposition> d(String str, InputStream inputStream, String str2, String str3) {
        FileExtension fileExtension;
        EffectiveAnimationResult<EffectiveAnimationComposition> f10;
        if (str2 == null) {
            str2 = "application/json";
        }
        if (!str2.contains("application/zip") && !str.split("\\?")[0].endsWith(".lottie")) {
            s4.e.a("Received json response.");
            fileExtension = FileExtension.JSON;
            f10 = e(str, inputStream, str3);
        } else {
            s4.e.a("Handling zip response.");
            fileExtension = FileExtension.ZIP;
            f10 = f(str, inputStream, str3);
        }
        if (str3 != null && f10.b() != null) {
            this.f16584a.e(str, fileExtension);
        }
        return f10;
    }

    private EffectiveAnimationResult<EffectiveAnimationComposition> e(String str, InputStream inputStream, String str2) {
        if (str2 == null) {
            return EffectiveCompositionFactory.i(inputStream, null);
        }
        return EffectiveCompositionFactory.i(new FileInputStream(this.f16584a.f(str, inputStream, FileExtension.JSON).getAbsolutePath()), str);
    }

    private EffectiveAnimationResult<EffectiveAnimationComposition> f(String str, InputStream inputStream, String str2) {
        if (str2 == null) {
            return EffectiveCompositionFactory.s(new ZipInputStream(inputStream), null);
        }
        return EffectiveCompositionFactory.s(new ZipInputStream(new FileInputStream(this.f16584a.f(str, inputStream, FileExtension.ZIP))), str);
    }

    public EffectiveAnimationResult<EffectiveAnimationComposition> c(String str, String str2) {
        EffectiveAnimationComposition a10 = a(str, str2);
        if (a10 != null) {
            return new EffectiveAnimationResult<>(a10);
        }
        s4.e.a("Animation for " + str + " not found in cache. Fetching from network.");
        return b(str, str2);
    }
}
