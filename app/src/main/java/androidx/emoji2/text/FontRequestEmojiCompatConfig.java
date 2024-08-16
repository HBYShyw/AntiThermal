package androidx.emoji2.text;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.os.TraceCompat;
import androidx.core.provider.FontRequest;
import androidx.core.provider.FontsContractCompat;
import androidx.core.util.Preconditions;
import androidx.emoji2.text.EmojiCompat;
import androidx.emoji2.text.FontRequestEmojiCompatConfig;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/* compiled from: FontRequestEmojiCompatConfig.java */
/* renamed from: androidx.emoji2.text.j, reason: use source file name */
/* loaded from: classes.dex */
public class FontRequestEmojiCompatConfig extends EmojiCompat.c {

    /* renamed from: j, reason: collision with root package name */
    private static final a f2637j = new a();

    /* compiled from: FontRequestEmojiCompatConfig.java */
    /* renamed from: androidx.emoji2.text.j$a */
    /* loaded from: classes.dex */
    public static class a {
        public Typeface a(Context context, FontsContractCompat.b bVar) {
            return FontsContractCompat.a(context, null, new FontsContractCompat.b[]{bVar});
        }

        public FontsContractCompat.a b(Context context, FontRequest fontRequest) {
            return FontsContractCompat.b(context, null, fontRequest);
        }

        public void c(Context context, ContentObserver contentObserver) {
            context.getContentResolver().unregisterContentObserver(contentObserver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: FontRequestEmojiCompatConfig.java */
    /* renamed from: androidx.emoji2.text.j$b */
    /* loaded from: classes.dex */
    public static class b implements EmojiCompat.g {

        /* renamed from: a, reason: collision with root package name */
        private final Context f2638a;

        /* renamed from: b, reason: collision with root package name */
        private final FontRequest f2639b;

        /* renamed from: c, reason: collision with root package name */
        private final a f2640c;

        /* renamed from: d, reason: collision with root package name */
        private final Object f2641d = new Object();

        /* renamed from: e, reason: collision with root package name */
        private Handler f2642e;

        /* renamed from: f, reason: collision with root package name */
        private Executor f2643f;

        /* renamed from: g, reason: collision with root package name */
        private ThreadPoolExecutor f2644g;

        /* renamed from: h, reason: collision with root package name */
        EmojiCompat.h f2645h;

        /* renamed from: i, reason: collision with root package name */
        private ContentObserver f2646i;

        /* renamed from: j, reason: collision with root package name */
        private Runnable f2647j;

        b(Context context, FontRequest fontRequest, a aVar) {
            Preconditions.e(context, "Context cannot be null");
            Preconditions.e(fontRequest, "FontRequest cannot be null");
            this.f2638a = context.getApplicationContext();
            this.f2639b = fontRequest;
            this.f2640c = aVar;
        }

        private void b() {
            synchronized (this.f2641d) {
                this.f2645h = null;
                ContentObserver contentObserver = this.f2646i;
                if (contentObserver != null) {
                    this.f2640c.c(this.f2638a, contentObserver);
                    this.f2646i = null;
                }
                Handler handler = this.f2642e;
                if (handler != null) {
                    handler.removeCallbacks(this.f2647j);
                }
                this.f2642e = null;
                ThreadPoolExecutor threadPoolExecutor = this.f2644g;
                if (threadPoolExecutor != null) {
                    threadPoolExecutor.shutdown();
                }
                this.f2643f = null;
                this.f2644g = null;
            }
        }

        private FontsContractCompat.b e() {
            try {
                FontsContractCompat.a b10 = this.f2640c.b(this.f2638a, this.f2639b);
                if (b10.c() == 0) {
                    FontsContractCompat.b[] b11 = b10.b();
                    if (b11 != null && b11.length != 0) {
                        return b11[0];
                    }
                    throw new RuntimeException("fetchFonts failed (empty result)");
                }
                throw new RuntimeException("fetchFonts failed (" + b10.c() + ")");
            } catch (PackageManager.NameNotFoundException e10) {
                throw new RuntimeException("provider not found", e10);
            }
        }

        @Override // androidx.emoji2.text.EmojiCompat.g
        public void a(EmojiCompat.h hVar) {
            Preconditions.e(hVar, "LoaderCallback cannot be null");
            synchronized (this.f2641d) {
                this.f2645h = hVar;
            }
            d();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void c() {
            synchronized (this.f2641d) {
                if (this.f2645h == null) {
                    return;
                }
                try {
                    FontsContractCompat.b e10 = e();
                    int b10 = e10.b();
                    if (b10 == 2) {
                        synchronized (this.f2641d) {
                        }
                    }
                    if (b10 == 0) {
                        try {
                            TraceCompat.a("EmojiCompat.FontRequestEmojiCompatConfig.buildTypeface");
                            Typeface a10 = this.f2640c.a(this.f2638a, e10);
                            ByteBuffer a11 = TypefaceCompatUtil.a(this.f2638a, null, e10.d());
                            if (a11 != null && a10 != null) {
                                MetadataRepo b11 = MetadataRepo.b(a10, a11);
                                TraceCompat.b();
                                synchronized (this.f2641d) {
                                    EmojiCompat.h hVar = this.f2645h;
                                    if (hVar != null) {
                                        hVar.b(b11);
                                    }
                                }
                                b();
                                return;
                            }
                            throw new RuntimeException("Unable to open file.");
                        } catch (Throwable th) {
                            TraceCompat.b();
                            throw th;
                        }
                    }
                    throw new RuntimeException("fetchFonts result is not OK. (" + b10 + ")");
                } catch (Throwable th2) {
                    synchronized (this.f2641d) {
                        EmojiCompat.h hVar2 = this.f2645h;
                        if (hVar2 != null) {
                            hVar2.a(th2);
                        }
                        b();
                    }
                }
            }
        }

        void d() {
            synchronized (this.f2641d) {
                if (this.f2645h == null) {
                    return;
                }
                if (this.f2643f == null) {
                    ThreadPoolExecutor b10 = ConcurrencyHelpers.b("emojiCompat");
                    this.f2644g = b10;
                    this.f2643f = b10;
                }
                this.f2643f.execute(new Runnable() { // from class: androidx.emoji2.text.k
                    @Override // java.lang.Runnable
                    public final void run() {
                        FontRequestEmojiCompatConfig.b.this.c();
                    }
                });
            }
        }

        public void f(Executor executor) {
            synchronized (this.f2641d) {
                this.f2643f = executor;
            }
        }
    }

    public FontRequestEmojiCompatConfig(Context context, FontRequest fontRequest) {
        super(new b(context, fontRequest, f2637j));
    }

    public FontRequestEmojiCompatConfig c(Executor executor) {
        ((b) a()).f(executor);
        return this;
    }
}
