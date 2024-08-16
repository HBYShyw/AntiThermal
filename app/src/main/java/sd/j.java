package sd;

import com.oplus.deepthinker.sdk.app.awareness.fence.impl.SpecifiedLocationFence;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import za.DefaultConstructorMarker;

/* compiled from: Regex.kt */
/* loaded from: classes2.dex */
public final class j implements Serializable {

    /* renamed from: f, reason: collision with root package name */
    public static final a f18497f = new a(null);

    /* renamed from: e, reason: collision with root package name */
    private final Pattern f18498e;

    /* compiled from: Regex.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: Regex.kt */
    /* loaded from: classes2.dex */
    private static final class b implements Serializable {

        /* renamed from: g, reason: collision with root package name */
        public static final a f18499g = new a(null);
        private static final long serialVersionUID = 0;

        /* renamed from: e, reason: collision with root package name */
        private final String f18500e;

        /* renamed from: f, reason: collision with root package name */
        private final int f18501f;

        /* compiled from: Regex.kt */
        /* loaded from: classes2.dex */
        public static final class a {
            private a() {
            }

            public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }

        public b(String str, int i10) {
            za.k.e(str, SpecifiedLocationFence.BUNDLE_KEY_PATTERN);
            this.f18500e = str;
            this.f18501f = i10;
        }

        private final Object readResolve() {
            Pattern compile = Pattern.compile(this.f18500e, this.f18501f);
            za.k.d(compile, "compile(pattern, flags)");
            return new j(compile);
        }
    }

    public j(Pattern pattern) {
        za.k.e(pattern, "nativePattern");
        this.f18498e = pattern;
    }

    private final Object writeReplace() {
        String pattern = this.f18498e.pattern();
        za.k.d(pattern, "nativePattern.pattern()");
        return new b(pattern, this.f18498e.flags());
    }

    public final h a(CharSequence charSequence) {
        h c10;
        za.k.e(charSequence, "input");
        Matcher matcher = this.f18498e.matcher(charSequence);
        za.k.d(matcher, "nativePattern.matcher(input)");
        c10 = k.c(matcher, charSequence);
        return c10;
    }

    public final boolean b(CharSequence charSequence) {
        za.k.e(charSequence, "input");
        return this.f18498e.matcher(charSequence).matches();
    }

    public final String c(CharSequence charSequence, String str) {
        za.k.e(charSequence, "input");
        za.k.e(str, "replacement");
        String replaceAll = this.f18498e.matcher(charSequence).replaceAll(str);
        za.k.d(replaceAll, "nativePattern.matcher(in…).replaceAll(replacement)");
        return replaceAll;
    }

    public String toString() {
        String pattern = this.f18498e.toString();
        za.k.d(pattern, "nativePattern.toString()");
        return pattern;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public j(String str) {
        this(r2);
        za.k.e(str, SpecifiedLocationFence.BUNDLE_KEY_PATTERN);
        Pattern compile = Pattern.compile(str);
        za.k.d(compile, "compile(pattern)");
    }
}
