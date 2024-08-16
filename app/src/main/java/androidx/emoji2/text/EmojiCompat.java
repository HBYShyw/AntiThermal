package androidx.emoji2.text;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import androidx.core.util.Preconditions;
import j.ArraySet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: EmojiCompat.java */
/* renamed from: androidx.emoji2.text.e, reason: use source file name */
/* loaded from: classes.dex */
public class EmojiCompat {

    /* renamed from: n, reason: collision with root package name */
    private static final Object f2580n = new Object();

    /* renamed from: o, reason: collision with root package name */
    private static final Object f2581o = new Object();

    /* renamed from: p, reason: collision with root package name */
    private static volatile EmojiCompat f2582p;

    /* renamed from: b, reason: collision with root package name */
    private final Set<e> f2584b;

    /* renamed from: e, reason: collision with root package name */
    private final b f2587e;

    /* renamed from: f, reason: collision with root package name */
    final g f2588f;

    /* renamed from: g, reason: collision with root package name */
    final boolean f2589g;

    /* renamed from: h, reason: collision with root package name */
    final boolean f2590h;

    /* renamed from: i, reason: collision with root package name */
    final int[] f2591i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f2592j;

    /* renamed from: k, reason: collision with root package name */
    private final int f2593k;

    /* renamed from: l, reason: collision with root package name */
    private final int f2594l;

    /* renamed from: m, reason: collision with root package name */
    private final d f2595m;

    /* renamed from: a, reason: collision with root package name */
    private final ReadWriteLock f2583a = new ReentrantReadWriteLock();

    /* renamed from: c, reason: collision with root package name */
    private volatile int f2585c = 3;

    /* renamed from: d, reason: collision with root package name */
    private final Handler f2586d = new Handler(Looper.getMainLooper());

    /* compiled from: EmojiCompat.java */
    /* renamed from: androidx.emoji2.text.e$a */
    /* loaded from: classes.dex */
    private static final class a extends b {

        /* renamed from: b, reason: collision with root package name */
        private volatile EmojiProcessor f2596b;

        /* renamed from: c, reason: collision with root package name */
        private volatile MetadataRepo f2597c;

        /* compiled from: EmojiCompat.java */
        /* renamed from: androidx.emoji2.text.e$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class C0003a extends h {
            C0003a() {
            }

            @Override // androidx.emoji2.text.EmojiCompat.h
            public void a(Throwable th) {
                a.this.f2599a.m(th);
            }

            @Override // androidx.emoji2.text.EmojiCompat.h
            public void b(MetadataRepo metadataRepo) {
                a.this.d(metadataRepo);
            }
        }

        a(EmojiCompat emojiCompat) {
            super(emojiCompat);
        }

        @Override // androidx.emoji2.text.EmojiCompat.b
        void a() {
            try {
                this.f2599a.f2588f.a(new C0003a());
            } catch (Throwable th) {
                this.f2599a.m(th);
            }
        }

        @Override // androidx.emoji2.text.EmojiCompat.b
        CharSequence b(CharSequence charSequence, int i10, int i11, int i12, boolean z10) {
            return this.f2596b.h(charSequence, i10, i11, i12, z10);
        }

        @Override // androidx.emoji2.text.EmojiCompat.b
        void c(EditorInfo editorInfo) {
            editorInfo.extras.putInt("android.support.text.emoji.emojiCompat_metadataVersion", this.f2597c.e());
            editorInfo.extras.putBoolean("android.support.text.emoji.emojiCompat_replaceAll", this.f2599a.f2589g);
        }

        void d(MetadataRepo metadataRepo) {
            if (metadataRepo == null) {
                this.f2599a.m(new IllegalArgumentException("metadataRepo cannot be null"));
                return;
            }
            this.f2597c = metadataRepo;
            MetadataRepo metadataRepo2 = this.f2597c;
            i iVar = new i();
            d dVar = this.f2599a.f2595m;
            EmojiCompat emojiCompat = this.f2599a;
            this.f2596b = new EmojiProcessor(metadataRepo2, iVar, dVar, emojiCompat.f2590h, emojiCompat.f2591i);
            this.f2599a.n();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EmojiCompat.java */
    /* renamed from: androidx.emoji2.text.e$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        final EmojiCompat f2599a;

        b(EmojiCompat emojiCompat) {
            this.f2599a = emojiCompat;
        }

        void a() {
            throw null;
        }

        CharSequence b(CharSequence charSequence, int i10, int i11, int i12, boolean z10) {
            throw null;
        }

        void c(EditorInfo editorInfo) {
            throw null;
        }
    }

    /* compiled from: EmojiCompat.java */
    /* renamed from: androidx.emoji2.text.e$c */
    /* loaded from: classes.dex */
    public static abstract class c {

        /* renamed from: a, reason: collision with root package name */
        final g f2600a;

        /* renamed from: b, reason: collision with root package name */
        boolean f2601b;

        /* renamed from: c, reason: collision with root package name */
        boolean f2602c;

        /* renamed from: d, reason: collision with root package name */
        int[] f2603d;

        /* renamed from: e, reason: collision with root package name */
        Set<e> f2604e;

        /* renamed from: f, reason: collision with root package name */
        boolean f2605f;

        /* renamed from: g, reason: collision with root package name */
        int f2606g = -16711936;

        /* renamed from: h, reason: collision with root package name */
        int f2607h = 0;

        /* renamed from: i, reason: collision with root package name */
        d f2608i = new DefaultGlyphChecker();

        /* JADX INFO: Access modifiers changed from: protected */
        public c(g gVar) {
            Preconditions.e(gVar, "metadataLoader cannot be null.");
            this.f2600a = gVar;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final g a() {
            return this.f2600a;
        }

        public c b(int i10) {
            this.f2607h = i10;
            return this;
        }
    }

    /* compiled from: EmojiCompat.java */
    /* renamed from: androidx.emoji2.text.e$d */
    /* loaded from: classes.dex */
    public interface d {
        boolean a(CharSequence charSequence, int i10, int i11, int i12);
    }

    /* compiled from: EmojiCompat.java */
    /* renamed from: androidx.emoji2.text.e$e */
    /* loaded from: classes.dex */
    public static abstract class e {
        public void a(Throwable th) {
        }

        public void b() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EmojiCompat.java */
    /* renamed from: androidx.emoji2.text.e$f */
    /* loaded from: classes.dex */
    public static class f implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private final List<e> f2609e;

        /* renamed from: f, reason: collision with root package name */
        private final Throwable f2610f;

        /* renamed from: g, reason: collision with root package name */
        private final int f2611g;

        f(e eVar, int i10) {
            this(Arrays.asList((e) Preconditions.e(eVar, "initCallback cannot be null")), i10, null);
        }

        @Override // java.lang.Runnable
        public void run() {
            int size = this.f2609e.size();
            int i10 = 0;
            if (this.f2611g != 1) {
                while (i10 < size) {
                    this.f2609e.get(i10).a(this.f2610f);
                    i10++;
                }
            } else {
                while (i10 < size) {
                    this.f2609e.get(i10).b();
                    i10++;
                }
            }
        }

        f(Collection<e> collection, int i10) {
            this(collection, i10, null);
        }

        f(Collection<e> collection, int i10, Throwable th) {
            Preconditions.e(collection, "initCallbacks cannot be null");
            this.f2609e = new ArrayList(collection);
            this.f2611g = i10;
            this.f2610f = th;
        }
    }

    /* compiled from: EmojiCompat.java */
    /* renamed from: androidx.emoji2.text.e$g */
    /* loaded from: classes.dex */
    public interface g {
        void a(h hVar);
    }

    /* compiled from: EmojiCompat.java */
    /* renamed from: androidx.emoji2.text.e$h */
    /* loaded from: classes.dex */
    public static abstract class h {
        public abstract void a(Throwable th);

        public abstract void b(MetadataRepo metadataRepo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EmojiCompat.java */
    /* renamed from: androidx.emoji2.text.e$i */
    /* loaded from: classes.dex */
    public static class i {
        i() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public EmojiSpan a(EmojiMetadata emojiMetadata) {
            return new TypefaceEmojiSpan(emojiMetadata);
        }
    }

    private EmojiCompat(c cVar) {
        this.f2589g = cVar.f2601b;
        this.f2590h = cVar.f2602c;
        this.f2591i = cVar.f2603d;
        this.f2592j = cVar.f2605f;
        this.f2593k = cVar.f2606g;
        this.f2588f = cVar.f2600a;
        this.f2594l = cVar.f2607h;
        this.f2595m = cVar.f2608i;
        ArraySet arraySet = new ArraySet();
        this.f2584b = arraySet;
        Set<e> set = cVar.f2604e;
        if (set != null && !set.isEmpty()) {
            arraySet.addAll(cVar.f2604e);
        }
        this.f2587e = new a(this);
        l();
    }

    public static EmojiCompat b() {
        EmojiCompat emojiCompat;
        synchronized (f2580n) {
            emojiCompat = f2582p;
            Preconditions.f(emojiCompat != null, "EmojiCompat is not initialized.\n\nYou must initialize EmojiCompat prior to referencing the EmojiCompat instance.\n\nThe most likely cause of this error is disabling the EmojiCompatInitializer\neither explicitly in AndroidManifest.xml, or by including\nandroidx.emoji2:emoji2-bundled.\n\nAutomatic initialization is typically performed by EmojiCompatInitializer. If\nyou are not expecting to initialize EmojiCompat manually in your application,\nplease check to ensure it has not been removed from your APK's manifest. You can\ndo this in Android Studio using Build > Analyze APK.\n\nIn the APK Analyzer, ensure that the startup entry for\nEmojiCompatInitializer and InitializationProvider is present in\n AndroidManifest.xml. If it is missing or contains tools:node=\"remove\", and you\nintend to use automatic configuration, verify:\n\n  1. Your application does not include emoji2-bundled\n  2. All modules do not contain an exclusion manifest rule for\n     EmojiCompatInitializer or InitializationProvider. For more information\n     about manifest exclusions see the documentation for the androidx startup\n     library.\n\nIf you intend to use emoji2-bundled, please call EmojiCompat.init. You can\nlearn more in the documentation for BundledEmojiCompatConfig.\n\nIf you intended to perform manual configuration, it is recommended that you call\nEmojiCompat.init immediately on application startup.\n\nIf you still cannot resolve this issue, please open a bug with your specific\nconfiguration to help improve error message.");
        }
        return emojiCompat;
    }

    public static boolean e(InputConnection inputConnection, Editable editable, int i10, int i11, boolean z10) {
        return EmojiProcessor.c(inputConnection, editable, i10, i11, z10);
    }

    public static boolean f(Editable editable, int i10, KeyEvent keyEvent) {
        return EmojiProcessor.d(editable, i10, keyEvent);
    }

    public static EmojiCompat g(c cVar) {
        EmojiCompat emojiCompat = f2582p;
        if (emojiCompat == null) {
            synchronized (f2580n) {
                emojiCompat = f2582p;
                if (emojiCompat == null) {
                    emojiCompat = new EmojiCompat(cVar);
                    f2582p = emojiCompat;
                }
            }
        }
        return emojiCompat;
    }

    public static boolean h() {
        return f2582p != null;
    }

    private boolean j() {
        return d() == 1;
    }

    private void l() {
        this.f2583a.writeLock().lock();
        try {
            if (this.f2594l == 0) {
                this.f2585c = 0;
            }
            this.f2583a.writeLock().unlock();
            if (d() == 0) {
                this.f2587e.a();
            }
        } catch (Throwable th) {
            this.f2583a.writeLock().unlock();
            throw th;
        }
    }

    public int c() {
        return this.f2593k;
    }

    public int d() {
        this.f2583a.readLock().lock();
        try {
            return this.f2585c;
        } finally {
            this.f2583a.readLock().unlock();
        }
    }

    public boolean i() {
        return this.f2592j;
    }

    public void k() {
        Preconditions.f(this.f2594l == 1, "Set metadataLoadStrategy to LOAD_STRATEGY_MANUAL to execute manual loading");
        if (j()) {
            return;
        }
        this.f2583a.writeLock().lock();
        try {
            if (this.f2585c == 0) {
                return;
            }
            this.f2585c = 0;
            this.f2583a.writeLock().unlock();
            this.f2587e.a();
        } finally {
            this.f2583a.writeLock().unlock();
        }
    }

    void m(Throwable th) {
        ArrayList arrayList = new ArrayList();
        this.f2583a.writeLock().lock();
        try {
            this.f2585c = 2;
            arrayList.addAll(this.f2584b);
            this.f2584b.clear();
            this.f2583a.writeLock().unlock();
            this.f2586d.post(new f(arrayList, this.f2585c, th));
        } catch (Throwable th2) {
            this.f2583a.writeLock().unlock();
            throw th2;
        }
    }

    void n() {
        ArrayList arrayList = new ArrayList();
        this.f2583a.writeLock().lock();
        try {
            this.f2585c = 1;
            arrayList.addAll(this.f2584b);
            this.f2584b.clear();
            this.f2583a.writeLock().unlock();
            this.f2586d.post(new f(arrayList, this.f2585c));
        } catch (Throwable th) {
            this.f2583a.writeLock().unlock();
            throw th;
        }
    }

    public CharSequence o(CharSequence charSequence) {
        return p(charSequence, 0, charSequence == null ? 0 : charSequence.length());
    }

    public CharSequence p(CharSequence charSequence, int i10, int i11) {
        return q(charSequence, i10, i11, Integer.MAX_VALUE);
    }

    public CharSequence q(CharSequence charSequence, int i10, int i11, int i12) {
        return r(charSequence, i10, i11, i12, 0);
    }

    public CharSequence r(CharSequence charSequence, int i10, int i11, int i12, int i13) {
        boolean z10;
        Preconditions.f(j(), "Not initialized yet");
        Preconditions.c(i10, "start cannot be negative");
        Preconditions.c(i11, "end cannot be negative");
        Preconditions.c(i12, "maxEmojiCount cannot be negative");
        Preconditions.a(i10 <= i11, "start should be <= than end");
        if (charSequence == null) {
            return null;
        }
        Preconditions.a(i10 <= charSequence.length(), "start should be < than charSequence length");
        Preconditions.a(i11 <= charSequence.length(), "end should be < than charSequence length");
        if (charSequence.length() == 0 || i10 == i11) {
            return charSequence;
        }
        if (i13 != 1) {
            z10 = i13 != 2 ? this.f2589g : false;
        } else {
            z10 = true;
        }
        return this.f2587e.b(charSequence, i10, i11, i12, z10);
    }

    public void s(e eVar) {
        Preconditions.e(eVar, "initCallback cannot be null");
        this.f2583a.writeLock().lock();
        try {
            if (this.f2585c != 1 && this.f2585c != 2) {
                this.f2584b.add(eVar);
            }
            this.f2586d.post(new f(eVar, this.f2585c));
        } finally {
            this.f2583a.writeLock().unlock();
        }
    }

    public void t(e eVar) {
        Preconditions.e(eVar, "initCallback cannot be null");
        this.f2583a.writeLock().lock();
        try {
            this.f2584b.remove(eVar);
        } finally {
            this.f2583a.writeLock().unlock();
        }
    }

    public void u(EditorInfo editorInfo) {
        if (!j() || editorInfo == null) {
            return;
        }
        if (editorInfo.extras == null) {
            editorInfo.extras = new Bundle();
        }
        this.f2587e.c(editorInfo);
    }
}
