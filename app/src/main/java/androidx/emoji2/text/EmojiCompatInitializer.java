package androidx.emoji2.text;

import android.content.Context;
import androidx.core.os.TraceCompat;
import androidx.emoji2.text.EmojiCompat;
import androidx.emoji2.text.EmojiCompatInitializer;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.ProcessLifecycleInitializer;
import androidx.startup.AppInitializer;
import e0.Initializer;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/* loaded from: classes.dex */
public class EmojiCompatInitializer implements Initializer<Boolean> {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class a extends EmojiCompat.c {
        protected a(Context context) {
            super(new b(context));
            b(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class b implements EmojiCompat.g {

        /* renamed from: a, reason: collision with root package name */
        private final Context f2572a;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class a extends EmojiCompat.h {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ EmojiCompat.h f2573a;

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ ThreadPoolExecutor f2574b;

            a(EmojiCompat.h hVar, ThreadPoolExecutor threadPoolExecutor) {
                this.f2573a = hVar;
                this.f2574b = threadPoolExecutor;
            }

            @Override // androidx.emoji2.text.EmojiCompat.h
            public void a(Throwable th) {
                try {
                    this.f2573a.a(th);
                } finally {
                    this.f2574b.shutdown();
                }
            }

            @Override // androidx.emoji2.text.EmojiCompat.h
            public void b(MetadataRepo metadataRepo) {
                try {
                    this.f2573a.b(metadataRepo);
                } finally {
                    this.f2574b.shutdown();
                }
            }
        }

        b(Context context) {
            this.f2572a = context.getApplicationContext();
        }

        @Override // androidx.emoji2.text.EmojiCompat.g
        public void a(final EmojiCompat.h hVar) {
            final ThreadPoolExecutor b10 = ConcurrencyHelpers.b("EmojiCompatInitializer");
            b10.execute(new Runnable() { // from class: androidx.emoji2.text.f
                @Override // java.lang.Runnable
                public final void run() {
                    EmojiCompatInitializer.b.this.d(hVar, b10);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public void d(EmojiCompat.h hVar, ThreadPoolExecutor threadPoolExecutor) {
            try {
                FontRequestEmojiCompatConfig a10 = DefaultEmojiCompatConfig.a(this.f2572a);
                if (a10 != null) {
                    a10.c(threadPoolExecutor);
                    a10.a().a(new a(hVar, threadPoolExecutor));
                    return;
                }
                throw new RuntimeException("EmojiCompat font provider not available on this device.");
            } catch (Throwable th) {
                hVar.a(th);
                threadPoolExecutor.shutdown();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                TraceCompat.a("EmojiCompat.EmojiCompatInitializer.run");
                if (EmojiCompat.h()) {
                    EmojiCompat.b().k();
                }
            } finally {
                TraceCompat.b();
            }
        }
    }

    @Override // e0.Initializer
    public List<Class<? extends Initializer<?>>> a() {
        return Collections.singletonList(ProcessLifecycleInitializer.class);
    }

    @Override // e0.Initializer
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public Boolean b(Context context) {
        EmojiCompat.g(new a(context));
        d(context);
        return Boolean.TRUE;
    }

    void d(Context context) {
        final androidx.lifecycle.h lifecycle = ((androidx.lifecycle.o) AppInitializer.e(context).f(ProcessLifecycleInitializer.class)).getLifecycle();
        lifecycle.a(new DefaultLifecycleObserver() { // from class: androidx.emoji2.text.EmojiCompatInitializer.1
            @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
            public void onResume(androidx.lifecycle.o oVar) {
                EmojiCompatInitializer.this.e();
                lifecycle.c(this);
            }
        });
    }

    void e() {
        ConcurrencyHelpers.d().postDelayed(new c(), 500L);
    }
}
