package androidx.window.core;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import ma.h;
import ma.j;
import sd.StringsJVM;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: Version.kt */
@Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\n\b\u0000\u0018\u0000 !2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\"B)\b\u0002\u0012\u0006\u0010\u000f\u001a\u00020\u0005\u0012\u0006\u0010\u0011\u001a\u00020\u0005\u0012\u0006\u0010\u0013\u001a\u00020\u0005\u0012\u0006\u0010\u0018\u001a\u00020\u0002¢\u0006\u0004\b\u001f\u0010 J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0011\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0000H\u0096\u0002J\u0013\u0010\t\u001a\u00020\b2\b\u0010\u0004\u001a\u0004\u0018\u00010\u0007H\u0096\u0002J\b\u0010\n\u001a\u00020\u0005H\u0016R\u0017\u0010\u000f\u001a\u00020\u00058\u0006¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u0011\u001a\u00020\u00058\u0006¢\u0006\f\n\u0004\b\u0010\u0010\f\u001a\u0004\b\u000b\u0010\u000eR\u0017\u0010\u0013\u001a\u00020\u00058\u0006¢\u0006\f\n\u0004\b\u0012\u0010\f\u001a\u0004\b\u0010\u0010\u000eR\u0017\u0010\u0018\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0016\u0010\u0017R\u001b\u0010\u001e\u001a\u00020\u00198BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u001a\u0010\u001b\u001a\u0004\b\u001c\u0010\u001d¨\u0006#"}, d2 = {"Landroidx/window/core/Version;", "", "", "toString", "other", "", "b", "", "", "equals", "hashCode", "e", "I", "d", "()I", "major", "f", "minor", "g", "patch", "h", "Ljava/lang/String;", "getDescription", "()Ljava/lang/String;", "description", "Ljava/math/BigInteger;", "bigInteger$delegate", "Lma/h;", "c", "()Ljava/math/BigInteger;", "bigInteger", "<init>", "(IIILjava/lang/String;)V", "j", "Companion", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class Version implements Comparable<Version> {

    /* renamed from: j, reason: collision with root package name and from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* renamed from: k, reason: collision with root package name */
    private static final Version f4337k = new Version(0, 0, 0, "");

    /* renamed from: l, reason: collision with root package name */
    private static final Version f4338l = new Version(0, 1, 0, "");

    /* renamed from: m, reason: collision with root package name */
    private static final Version f4339m;

    /* renamed from: n, reason: collision with root package name */
    private static final Version f4340n;

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private final int major;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private final int minor;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private final int patch;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private final String description;

    /* renamed from: i, reason: collision with root package name */
    private final h f4345i;

    /* compiled from: Version.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\b\u0080\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rJ\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0007R\u0017\u0010\u0006\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0006\u0010\u0007\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\n\u0010\u000b¨\u0006\u000e"}, d2 = {"Landroidx/window/core/Version$Companion;", "", "", "versionString", "Landroidx/window/core/Version;", "b", "VERSION_0_1", "Landroidx/window/core/Version;", "a", "()Landroidx/window/core/Version;", "VERSION_PATTERN_STRING", "Ljava/lang/String;", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Version a() {
            return Version.f4338l;
        }

        public final Version b(String versionString) {
            boolean s7;
            String group;
            if (versionString == null) {
                return null;
            }
            s7 = StringsJVM.s(versionString);
            if (s7) {
                return null;
            }
            Matcher matcher = Pattern.compile("(\\d+)(?:\\.(\\d+))(?:\\.(\\d+))(?:-(.+))?").matcher(versionString);
            if (!matcher.matches() || (group = matcher.group(1)) == null) {
                return null;
            }
            int parseInt = Integer.parseInt(group);
            String group2 = matcher.group(2);
            if (group2 == null) {
                return null;
            }
            int parseInt2 = Integer.parseInt(group2);
            String group3 = matcher.group(3);
            if (group3 == null) {
                return null;
            }
            int parseInt3 = Integer.parseInt(group3);
            String group4 = matcher.group(4) != null ? matcher.group(4) : "";
            k.d(group4, "description");
            return new Version(parseInt, parseInt2, parseInt3, group4, null);
        }
    }

    static {
        Version version = new Version(1, 0, 0, "");
        f4339m = version;
        f4340n = version;
    }

    private Version(int i10, int i11, int i12, String str) {
        h b10;
        this.major = i10;
        this.minor = i11;
        this.patch = i12;
        this.description = str;
        b10 = j.b(new Version$bigInteger$2(this));
        this.f4345i = b10;
    }

    public /* synthetic */ Version(int i10, int i11, int i12, String str, DefaultConstructorMarker defaultConstructorMarker) {
        this(i10, i11, i12, str);
    }

    private final BigInteger c() {
        Object value = this.f4345i.getValue();
        k.d(value, "<get-bigInteger>(...)");
        return (BigInteger) value;
    }

    @Override // java.lang.Comparable
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public int compareTo(Version other) {
        k.e(other, "other");
        return c().compareTo(other.c());
    }

    /* renamed from: d, reason: from getter */
    public final int getMajor() {
        return this.major;
    }

    /* renamed from: e, reason: from getter */
    public final int getMinor() {
        return this.minor;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Version)) {
            return false;
        }
        Version version = (Version) other;
        return this.major == version.major && this.minor == version.minor && this.patch == version.patch;
    }

    /* renamed from: f, reason: from getter */
    public final int getPatch() {
        return this.patch;
    }

    public int hashCode() {
        return ((((527 + this.major) * 31) + this.minor) * 31) + this.patch;
    }

    public String toString() {
        boolean s7;
        String str;
        s7 = StringsJVM.s(this.description);
        if (!s7) {
            str = '-' + this.description;
        } else {
            str = "";
        }
        return this.major + '.' + this.minor + '.' + this.patch + str;
    }
}
