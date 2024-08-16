package oc;

/* compiled from: Name.java */
/* renamed from: oc.f, reason: use source file name */
/* loaded from: classes2.dex */
public final class Name implements Comparable<Name> {

    /* renamed from: e, reason: collision with root package name */
    private final String f16442e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f16443f;

    private Name(String str, boolean z10) {
        if (str == null) {
            a(0);
        }
        this.f16442e = str;
        this.f16443f = z10;
    }

    private static /* synthetic */ void a(int i10) {
        String str = (i10 == 1 || i10 == 2 || i10 == 3 || i10 == 4) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 1 || i10 == 2 || i10 == 3 || i10 == 4) ? 2 : 3];
        if (i10 == 1 || i10 == 2 || i10 == 3 || i10 == 4) {
            objArr[0] = "kotlin/reflect/jvm/internal/impl/name/Name";
        } else {
            objArr[0] = "name";
        }
        if (i10 == 1) {
            objArr[1] = "asString";
        } else if (i10 == 2) {
            objArr[1] = "getIdentifier";
        } else if (i10 == 3 || i10 == 4) {
            objArr[1] = "asStringStripSpecialMarkers";
        } else {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/name/Name";
        }
        switch (i10) {
            case 1:
            case 2:
            case 3:
            case 4:
                break;
            case 5:
                objArr[2] = "identifier";
                break;
            case 6:
                objArr[2] = "isValidIdentifier";
                break;
            case 7:
                objArr[2] = "special";
                break;
            case 8:
                objArr[2] = "guessByFirstCharacter";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        if (i10 != 1 && i10 != 2 && i10 != 3 && i10 != 4) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    public static Name e(String str) {
        if (str == null) {
            a(8);
        }
        if (str.startsWith("<")) {
            return i(str);
        }
        return f(str);
    }

    public static Name f(String str) {
        if (str == null) {
            a(5);
        }
        return new Name(str, false);
    }

    public static boolean h(String str) {
        if (str == null) {
            a(6);
        }
        if (str.isEmpty() || str.startsWith("<")) {
            return false;
        }
        for (int i10 = 0; i10 < str.length(); i10++) {
            char charAt = str.charAt(i10);
            if (charAt == '.' || charAt == '/' || charAt == '\\') {
                return false;
            }
        }
        return true;
    }

    public static Name i(String str) {
        if (str == null) {
            a(7);
        }
        if (str.startsWith("<")) {
            return new Name(str, true);
        }
        throw new IllegalArgumentException("special name must start with '<': " + str);
    }

    public String b() {
        String str = this.f16442e;
        if (str == null) {
            a(1);
        }
        return str;
    }

    @Override // java.lang.Comparable
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public int compareTo(Name name) {
        return this.f16442e.compareTo(name.f16442e);
    }

    public String d() {
        if (!this.f16443f) {
            String b10 = b();
            if (b10 == null) {
                a(2);
            }
            return b10;
        }
        throw new IllegalStateException("not identifier: " + this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Name)) {
            return false;
        }
        Name name = (Name) obj;
        return this.f16443f == name.f16443f && this.f16442e.equals(name.f16442e);
    }

    public boolean g() {
        return this.f16443f;
    }

    public int hashCode() {
        return (this.f16442e.hashCode() * 31) + (this.f16443f ? 1 : 0);
    }

    public String toString() {
        return this.f16442e;
    }
}
