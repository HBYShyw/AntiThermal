package oc;

import java.util.List;

/* compiled from: FqName.java */
/* renamed from: oc.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class FqName {

    /* renamed from: c, reason: collision with root package name */
    public static final FqName f16431c = new FqName("");

    /* renamed from: a, reason: collision with root package name */
    private final FqNameUnsafe f16432a;

    /* renamed from: b, reason: collision with root package name */
    private transient FqName f16433b;

    public FqName(String str) {
        if (str == null) {
            a(1);
        }
        this.f16432a = new FqNameUnsafe(str, this);
    }

    private static /* synthetic */ void a(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 11:
                str = "@NotNull method %s.%s must not return null";
                break;
            case 8:
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 11:
                i11 = 2;
                break;
            case 8:
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        switch (i10) {
            case 1:
            case 2:
            case 3:
                objArr[0] = "fqName";
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 11:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/name/FqName";
                break;
            case 8:
                objArr[0] = "name";
                break;
            case 12:
                objArr[0] = "segment";
                break;
            case 13:
                objArr[0] = "shortName";
                break;
            default:
                objArr[0] = "names";
                break;
        }
        switch (i10) {
            case 4:
                objArr[1] = "asString";
                break;
            case 5:
                objArr[1] = "toUnsafe";
                break;
            case 6:
            case 7:
                objArr[1] = "parent";
                break;
            case 8:
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/name/FqName";
                break;
            case 9:
                objArr[1] = "shortName";
                break;
            case 10:
                objArr[1] = "shortNameOrSpecial";
                break;
            case 11:
                objArr[1] = "pathSegments";
                break;
        }
        switch (i10) {
            case 1:
            case 2:
            case 3:
                objArr[2] = "<init>";
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 11:
                break;
            case 8:
                objArr[2] = "child";
                break;
            case 12:
                objArr[2] = "startsWith";
                break;
            case 13:
                objArr[2] = "topLevel";
                break;
            default:
                objArr[2] = "fromSegments";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 11:
                throw new IllegalStateException(format);
            case 8:
            default:
                throw new IllegalArgumentException(format);
        }
    }

    public static FqName k(Name name) {
        if (name == null) {
            a(13);
        }
        return new FqName(FqNameUnsafe.m(name));
    }

    public String b() {
        String b10 = this.f16432a.b();
        if (b10 == null) {
            a(4);
        }
        return b10;
    }

    public FqName c(Name name) {
        if (name == null) {
            a(8);
        }
        return new FqName(this.f16432a.c(name), this);
    }

    public boolean d() {
        return this.f16432a.e();
    }

    public FqName e() {
        FqName fqName = this.f16433b;
        if (fqName != null) {
            if (fqName == null) {
                a(6);
            }
            return fqName;
        }
        if (!d()) {
            FqName fqName2 = new FqName(this.f16432a.g());
            this.f16433b = fqName2;
            return fqName2;
        }
        throw new IllegalStateException("root");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof FqName) && this.f16432a.equals(((FqName) obj).f16432a);
    }

    public List<Name> f() {
        List<Name> h10 = this.f16432a.h();
        if (h10 == null) {
            a(11);
        }
        return h10;
    }

    public Name g() {
        Name i10 = this.f16432a.i();
        if (i10 == null) {
            a(9);
        }
        return i10;
    }

    public Name h() {
        Name j10 = this.f16432a.j();
        if (j10 == null) {
            a(10);
        }
        return j10;
    }

    public int hashCode() {
        return this.f16432a.hashCode();
    }

    public boolean i(Name name) {
        if (name == null) {
            a(12);
        }
        return this.f16432a.k(name);
    }

    public FqNameUnsafe j() {
        FqNameUnsafe fqNameUnsafe = this.f16432a;
        if (fqNameUnsafe == null) {
            a(5);
        }
        return fqNameUnsafe;
    }

    public String toString() {
        return this.f16432a.toString();
    }

    public FqName(FqNameUnsafe fqNameUnsafe) {
        if (fqNameUnsafe == null) {
            a(2);
        }
        this.f16432a = fqNameUnsafe;
    }

    private FqName(FqNameUnsafe fqNameUnsafe, FqName fqName) {
        if (fqNameUnsafe == null) {
            a(3);
        }
        this.f16432a = fqNameUnsafe;
        this.f16433b = fqName;
    }
}
