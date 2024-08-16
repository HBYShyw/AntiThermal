package androidx.core.provider;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.Preconditions;

/* compiled from: FontsContractCompat.java */
/* renamed from: androidx.core.provider.g, reason: use source file name */
/* loaded from: classes.dex */
public class FontsContractCompat {

    /* compiled from: FontsContractCompat.java */
    /* renamed from: androidx.core.provider.g$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private final int f2251a;

        /* renamed from: b, reason: collision with root package name */
        private final b[] f2252b;

        @Deprecated
        public a(int i10, b[] bVarArr) {
            this.f2251a = i10;
            this.f2252b = bVarArr;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static a a(int i10, b[] bVarArr) {
            return new a(i10, bVarArr);
        }

        public b[] b() {
            return this.f2252b;
        }

        public int c() {
            return this.f2251a;
        }
    }

    /* compiled from: FontsContractCompat.java */
    /* renamed from: androidx.core.provider.g$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private final Uri f2253a;

        /* renamed from: b, reason: collision with root package name */
        private final int f2254b;

        /* renamed from: c, reason: collision with root package name */
        private final int f2255c;

        /* renamed from: d, reason: collision with root package name */
        private final boolean f2256d;

        /* renamed from: e, reason: collision with root package name */
        private final int f2257e;

        @Deprecated
        public b(Uri uri, int i10, int i11, boolean z10, int i12) {
            this.f2253a = (Uri) Preconditions.d(uri);
            this.f2254b = i10;
            this.f2255c = i11;
            this.f2256d = z10;
            this.f2257e = i12;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static b a(Uri uri, int i10, int i11, boolean z10, int i12) {
            return new b(uri, i10, i11, z10, i12);
        }

        public int b() {
            return this.f2257e;
        }

        public int c() {
            return this.f2254b;
        }

        public Uri d() {
            return this.f2253a;
        }

        public int e() {
            return this.f2255c;
        }

        public boolean f() {
            return this.f2256d;
        }
    }

    /* compiled from: FontsContractCompat.java */
    /* renamed from: androidx.core.provider.g$c */
    /* loaded from: classes.dex */
    public static class c {
        public void a(int i10) {
            throw null;
        }

        public void b(Typeface typeface) {
            throw null;
        }
    }

    public static Typeface a(Context context, CancellationSignal cancellationSignal, b[] bVarArr) {
        return TypefaceCompat.b(context, cancellationSignal, bVarArr, 0);
    }

    public static a b(Context context, CancellationSignal cancellationSignal, FontRequest fontRequest) {
        return FontProvider.e(context, fontRequest, cancellationSignal);
    }

    public static Typeface c(Context context, FontRequest fontRequest, int i10, boolean z10, int i11, Handler handler, c cVar) {
        CallbackWithHandler callbackWithHandler = new CallbackWithHandler(cVar, handler);
        if (z10) {
            return FontRequestWorker.e(context, fontRequest, callbackWithHandler, i10, i11);
        }
        return FontRequestWorker.d(context, fontRequest, i10, null, callbackWithHandler);
    }
}
