package androidx.core.provider;

import android.graphics.Typeface;
import android.os.Handler;
import androidx.core.provider.FontRequestWorker;
import androidx.core.provider.FontsContractCompat;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CallbackWithHandler.java */
/* renamed from: androidx.core.provider.a, reason: use source file name */
/* loaded from: classes.dex */
public class CallbackWithHandler {

    /* renamed from: a, reason: collision with root package name */
    private final FontsContractCompat.c f2219a;

    /* renamed from: b, reason: collision with root package name */
    private final Handler f2220b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CallbackWithHandler.java */
    /* renamed from: androidx.core.provider.a$a */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ FontsContractCompat.c f2221e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Typeface f2222f;

        a(FontsContractCompat.c cVar, Typeface typeface) {
            this.f2221e = cVar;
            this.f2222f = typeface;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f2221e.b(this.f2222f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CallbackWithHandler.java */
    /* renamed from: androidx.core.provider.a$b */
    /* loaded from: classes.dex */
    public class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ FontsContractCompat.c f2224e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f2225f;

        b(FontsContractCompat.c cVar, int i10) {
            this.f2224e = cVar;
            this.f2225f = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f2224e.a(this.f2225f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CallbackWithHandler(FontsContractCompat.c cVar, Handler handler) {
        this.f2219a = cVar;
        this.f2220b = handler;
    }

    private void a(int i10) {
        this.f2220b.post(new b(this.f2219a, i10));
    }

    private void c(Typeface typeface) {
        this.f2220b.post(new a(this.f2219a, typeface));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(FontRequestWorker.e eVar) {
        if (eVar.a()) {
            c(eVar.f2249a);
        } else {
            a(eVar.f2250b);
        }
    }
}
