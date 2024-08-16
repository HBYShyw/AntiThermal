package oc;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import ya.l;

/* compiled from: FqNameUnsafe.java */
/* renamed from: oc.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class FqNameUnsafe {

    /* renamed from: e, reason: collision with root package name */
    private static final Name f16434e = Name.i("<root>");

    /* renamed from: f, reason: collision with root package name */
    private static final Pattern f16435f = Pattern.compile("\\.");

    /* renamed from: g, reason: collision with root package name */
    private static final l<String, Name> f16436g = new a();

    /* renamed from: a, reason: collision with root package name */
    private final String f16437a;

    /* renamed from: b, reason: collision with root package name */
    private transient FqName f16438b;

    /* renamed from: c, reason: collision with root package name */
    private transient FqNameUnsafe f16439c;

    /* renamed from: d, reason: collision with root package name */
    private transient Name f16440d;

    /* compiled from: FqNameUnsafe.java */
    /* renamed from: oc.d$a */
    /* loaded from: classes2.dex */
    static class a implements l<String, Name> {
        a() {
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Name invoke(String str) {
            return Name.e(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FqNameUnsafe(String str, FqName fqName) {
        if (str == null) {
            a(0);
        }
        if (fqName == null) {
            a(1);
        }
        this.f16437a = str;
        this.f16438b = fqName;
    }

    private static /* synthetic */ void a(int i10) {
        String str;
        int i11;
        switch (i10) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 17:
                str = "@NotNull method %s.%s must not return null";
                break;
            case 9:
            case 15:
            case 16:
            default:
                str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
        }
        switch (i10) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 17:
                i11 = 2;
                break;
            case 9:
            case 15:
            case 16:
            default:
                i11 = 3;
                break;
        }
        Object[] objArr = new Object[i11];
        if (i10 != 1) {
            switch (i10) {
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 17:
                    objArr[0] = "kotlin/reflect/jvm/internal/impl/name/FqNameUnsafe";
                    break;
                case 9:
                    objArr[0] = "name";
                    break;
                case 15:
                    objArr[0] = "segment";
                    break;
                case 16:
                    objArr[0] = "shortName";
                    break;
                default:
                    objArr[0] = "fqName";
                    break;
            }
        } else {
            objArr[0] = "safe";
        }
        switch (i10) {
            case 4:
                objArr[1] = "asString";
                break;
            case 5:
            case 6:
                objArr[1] = "toSafe";
                break;
            case 7:
            case 8:
                objArr[1] = "parent";
                break;
            case 9:
            case 15:
            case 16:
            default:
                objArr[1] = "kotlin/reflect/jvm/internal/impl/name/FqNameUnsafe";
                break;
            case 10:
            case 11:
                objArr[1] = "shortName";
                break;
            case 12:
            case 13:
                objArr[1] = "shortNameOrSpecial";
                break;
            case 14:
                objArr[1] = "pathSegments";
                break;
            case 17:
                objArr[1] = "toString";
                break;
        }
        switch (i10) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 17:
                break;
            case 9:
                objArr[2] = "child";
                break;
            case 15:
                objArr[2] = "startsWith";
                break;
            case 16:
                objArr[2] = "topLevel";
                break;
            default:
                objArr[2] = "<init>";
                break;
        }
        String format = String.format(str, objArr);
        switch (i10) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 17:
                throw new IllegalStateException(format);
            case 9:
            case 15:
            case 16:
            default:
                throw new IllegalArgumentException(format);
        }
    }

    private void d() {
        int lastIndexOf = this.f16437a.lastIndexOf(46);
        if (lastIndexOf >= 0) {
            this.f16440d = Name.e(this.f16437a.substring(lastIndexOf + 1));
            this.f16439c = new FqNameUnsafe(this.f16437a.substring(0, lastIndexOf));
        } else {
            this.f16440d = Name.e(this.f16437a);
            this.f16439c = FqName.f16431c.j();
        }
    }

    public static FqNameUnsafe m(Name name) {
        if (name == null) {
            a(16);
        }
        return new FqNameUnsafe(name.b(), FqName.f16431c.j(), name);
    }

    public String b() {
        String str = this.f16437a;
        if (str == null) {
            a(4);
        }
        return str;
    }

    public FqNameUnsafe c(Name name) {
        String str;
        if (name == null) {
            a(9);
        }
        if (e()) {
            str = name.b();
        } else {
            str = this.f16437a + "." + name.b();
        }
        return new FqNameUnsafe(str, this, name);
    }

    public boolean e() {
        return this.f16437a.isEmpty();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof FqNameUnsafe) && this.f16437a.equals(((FqNameUnsafe) obj).f16437a);
    }

    public boolean f() {
        return this.f16438b != null || b().indexOf(60) < 0;
    }

    public FqNameUnsafe g() {
        FqNameUnsafe fqNameUnsafe = this.f16439c;
        if (fqNameUnsafe != null) {
            if (fqNameUnsafe == null) {
                a(7);
            }
            return fqNameUnsafe;
        }
        if (!e()) {
            d();
            FqNameUnsafe fqNameUnsafe2 = this.f16439c;
            if (fqNameUnsafe2 == null) {
                a(8);
            }
            return fqNameUnsafe2;
        }
        throw new IllegalStateException("root");
    }

    public List<Name> h() {
        List<Name> emptyList = e() ? Collections.emptyList() : kotlin.collections.j.R(f16435f.split(this.f16437a), f16436g);
        if (emptyList == null) {
            a(14);
        }
        return emptyList;
    }

    public int hashCode() {
        return this.f16437a.hashCode();
    }

    public Name i() {
        Name name = this.f16440d;
        if (name != null) {
            if (name == null) {
                a(10);
            }
            return name;
        }
        if (!e()) {
            d();
            Name name2 = this.f16440d;
            if (name2 == null) {
                a(11);
            }
            return name2;
        }
        throw new IllegalStateException("root");
    }

    public Name j() {
        if (e()) {
            Name name = f16434e;
            if (name == null) {
                a(12);
            }
            return name;
        }
        Name i10 = i();
        if (i10 == null) {
            a(13);
        }
        return i10;
    }

    public boolean k(Name name) {
        if (name == null) {
            a(15);
        }
        if (e()) {
            return false;
        }
        int indexOf = this.f16437a.indexOf(46);
        String str = this.f16437a;
        String b10 = name.b();
        if (indexOf == -1) {
            indexOf = this.f16437a.length();
        }
        return str.regionMatches(0, b10, 0, indexOf);
    }

    public FqName l() {
        FqName fqName = this.f16438b;
        if (fqName != null) {
            if (fqName == null) {
                a(5);
            }
            return fqName;
        }
        FqName fqName2 = new FqName(this);
        this.f16438b = fqName2;
        return fqName2;
    }

    public String toString() {
        String b10 = e() ? f16434e.b() : this.f16437a;
        if (b10 == null) {
            a(17);
        }
        return b10;
    }

    public FqNameUnsafe(String str) {
        if (str == null) {
            a(2);
        }
        this.f16437a = str;
    }

    private FqNameUnsafe(String str, FqNameUnsafe fqNameUnsafe, Name name) {
        if (str == null) {
            a(3);
        }
        this.f16437a = str;
        this.f16439c = fqNameUnsafe;
        this.f16440d = name;
    }
}
