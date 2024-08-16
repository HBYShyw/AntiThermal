package androidx.core.text;

import java.util.Locale;

/* compiled from: TextDirectionHeuristicsCompat.java */
/* renamed from: androidx.core.text.e, reason: use source file name */
/* loaded from: classes.dex */
public final class TextDirectionHeuristicsCompat {

    /* renamed from: a, reason: collision with root package name */
    public static final TextDirectionHeuristicCompat f2292a = new e(null, false);

    /* renamed from: b, reason: collision with root package name */
    public static final TextDirectionHeuristicCompat f2293b = new e(null, true);

    /* renamed from: c, reason: collision with root package name */
    public static final TextDirectionHeuristicCompat f2294c;

    /* renamed from: d, reason: collision with root package name */
    public static final TextDirectionHeuristicCompat f2295d;

    /* renamed from: e, reason: collision with root package name */
    public static final TextDirectionHeuristicCompat f2296e;

    /* renamed from: f, reason: collision with root package name */
    public static final TextDirectionHeuristicCompat f2297f;

    /* compiled from: TextDirectionHeuristicsCompat.java */
    /* renamed from: androidx.core.text.e$a */
    /* loaded from: classes.dex */
    private static class a implements c {

        /* renamed from: b, reason: collision with root package name */
        static final a f2298b = new a(true);

        /* renamed from: a, reason: collision with root package name */
        private final boolean f2299a;

        private a(boolean z10) {
            this.f2299a = z10;
        }

        @Override // androidx.core.text.TextDirectionHeuristicsCompat.c
        public int a(CharSequence charSequence, int i10, int i11) {
            int i12 = i11 + i10;
            boolean z10 = false;
            while (i10 < i12) {
                int a10 = TextDirectionHeuristicsCompat.a(Character.getDirectionality(charSequence.charAt(i10)));
                if (a10 != 0) {
                    if (a10 != 1) {
                        continue;
                        i10++;
                        z10 = z10;
                    } else if (!this.f2299a) {
                        return 1;
                    }
                } else if (this.f2299a) {
                    return 0;
                }
                z10 = true;
                i10++;
                z10 = z10;
            }
            if (z10) {
                return this.f2299a ? 1 : 0;
            }
            return 2;
        }
    }

    /* compiled from: TextDirectionHeuristicsCompat.java */
    /* renamed from: androidx.core.text.e$b */
    /* loaded from: classes.dex */
    private static class b implements c {

        /* renamed from: a, reason: collision with root package name */
        static final b f2300a = new b();

        private b() {
        }

        @Override // androidx.core.text.TextDirectionHeuristicsCompat.c
        public int a(CharSequence charSequence, int i10, int i11) {
            int i12 = i11 + i10;
            int i13 = 2;
            while (i10 < i12 && i13 == 2) {
                i13 = TextDirectionHeuristicsCompat.b(Character.getDirectionality(charSequence.charAt(i10)));
                i10++;
            }
            return i13;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TextDirectionHeuristicsCompat.java */
    /* renamed from: androidx.core.text.e$c */
    /* loaded from: classes.dex */
    public interface c {
        int a(CharSequence charSequence, int i10, int i11);
    }

    /* compiled from: TextDirectionHeuristicsCompat.java */
    /* renamed from: androidx.core.text.e$d */
    /* loaded from: classes.dex */
    private static abstract class d implements TextDirectionHeuristicCompat {

        /* renamed from: a, reason: collision with root package name */
        private final c f2301a;

        d(c cVar) {
            this.f2301a = cVar;
        }

        private boolean c(CharSequence charSequence, int i10, int i11) {
            int a10 = this.f2301a.a(charSequence, i10, i11);
            if (a10 == 0) {
                return true;
            }
            if (a10 != 1) {
                return b();
            }
            return false;
        }

        @Override // androidx.core.text.TextDirectionHeuristicCompat
        public boolean a(CharSequence charSequence, int i10, int i11) {
            if (charSequence != null && i10 >= 0 && i11 >= 0 && charSequence.length() - i11 >= i10) {
                if (this.f2301a == null) {
                    return b();
                }
                return c(charSequence, i10, i11);
            }
            throw new IllegalArgumentException();
        }

        protected abstract boolean b();
    }

    /* compiled from: TextDirectionHeuristicsCompat.java */
    /* renamed from: androidx.core.text.e$e */
    /* loaded from: classes.dex */
    private static class e extends d {

        /* renamed from: b, reason: collision with root package name */
        private final boolean f2302b;

        e(c cVar, boolean z10) {
            super(cVar);
            this.f2302b = z10;
        }

        @Override // androidx.core.text.TextDirectionHeuristicsCompat.d
        protected boolean b() {
            return this.f2302b;
        }
    }

    /* compiled from: TextDirectionHeuristicsCompat.java */
    /* renamed from: androidx.core.text.e$f */
    /* loaded from: classes.dex */
    private static class f extends d {

        /* renamed from: b, reason: collision with root package name */
        static final f f2303b = new f();

        f() {
            super(null);
        }

        @Override // androidx.core.text.TextDirectionHeuristicsCompat.d
        protected boolean b() {
            return TextUtilsCompat.a(Locale.getDefault()) == 1;
        }
    }

    static {
        b bVar = b.f2300a;
        f2294c = new e(bVar, false);
        f2295d = new e(bVar, true);
        f2296e = new e(a.f2298b, false);
        f2297f = f.f2303b;
    }

    static int a(int i10) {
        if (i10 != 0) {
            return (i10 == 1 || i10 == 2) ? 0 : 2;
        }
        return 1;
    }

    static int b(int i10) {
        if (i10 != 0) {
            if (i10 == 1 || i10 == 2) {
                return 0;
            }
            switch (i10) {
                case 14:
                case 15:
                    break;
                case 16:
                case 17:
                    return 0;
                default:
                    return 2;
            }
        }
        return 1;
    }
}
