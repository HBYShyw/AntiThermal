package oc;

/* compiled from: ClassId.java */
/* renamed from: oc.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class ClassId {

    /* renamed from: a, reason: collision with root package name */
    private final FqName f16428a;

    /* renamed from: b, reason: collision with root package name */
    private final FqName f16429b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f16430c;

    public ClassId(FqName fqName, FqName fqName2, boolean z10) {
        if (fqName == null) {
            a(1);
        }
        if (fqName2 == null) {
            a(2);
        }
        this.f16428a = fqName;
        this.f16429b = fqName2;
        this.f16430c = z10;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x009e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x004d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void a(int i10) {
        String str;
        int i11;
        if (i10 != 5 && i10 != 6 && i10 != 7 && i10 != 9) {
            switch (i10) {
                case 13:
                case 14:
                case 15:
                case 16:
                    break;
                default:
                    str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                    break;
            }
            if (i10 != 5 && i10 != 6 && i10 != 7 && i10 != 9) {
                switch (i10) {
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        break;
                    default:
                        i11 = 3;
                        break;
                }
                Object[] objArr = new Object[i11];
                switch (i10) {
                    case 1:
                    case 3:
                        objArr[0] = "packageFqName";
                        break;
                    case 2:
                        objArr[0] = "relativeClassName";
                        break;
                    case 4:
                        objArr[0] = "topLevelName";
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 9:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        objArr[0] = "kotlin/reflect/jvm/internal/impl/name/ClassId";
                        break;
                    case 8:
                        objArr[0] = "name";
                        break;
                    case 10:
                        objArr[0] = "segment";
                        break;
                    case 11:
                    case 12:
                        objArr[0] = "string";
                        break;
                    default:
                        objArr[0] = "topLevelFqName";
                        break;
                }
                if (i10 != 5) {
                    objArr[1] = "getPackageFqName";
                } else if (i10 == 6) {
                    objArr[1] = "getRelativeClassName";
                } else if (i10 == 7) {
                    objArr[1] = "getShortClassName";
                } else if (i10 != 9) {
                    switch (i10) {
                        case 13:
                        case 14:
                            objArr[1] = "asString";
                            break;
                        case 15:
                        case 16:
                            objArr[1] = "asFqNameString";
                            break;
                        default:
                            objArr[1] = "kotlin/reflect/jvm/internal/impl/name/ClassId";
                            break;
                    }
                } else {
                    objArr[1] = "asSingleFqName";
                }
                switch (i10) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        objArr[2] = "<init>";
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 9:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        break;
                    case 8:
                        objArr[2] = "createNestedClassId";
                        break;
                    case 10:
                        objArr[2] = "startsWith";
                        break;
                    case 11:
                    case 12:
                        objArr[2] = "fromString";
                        break;
                    default:
                        objArr[2] = "topLevel";
                        break;
                }
                String format = String.format(str, objArr);
                if (i10 != 5 && i10 != 6 && i10 != 7 && i10 != 9) {
                    switch (i10) {
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                            break;
                        default:
                            throw new IllegalArgumentException(format);
                    }
                }
                throw new IllegalStateException(format);
            }
            i11 = 2;
            Object[] objArr2 = new Object[i11];
            switch (i10) {
            }
            if (i10 != 5) {
            }
            switch (i10) {
            }
            String format2 = String.format(str, objArr2);
            if (i10 != 5) {
                switch (i10) {
                }
            }
            throw new IllegalStateException(format2);
        }
        str = "@NotNull method %s.%s must not return null";
        if (i10 != 5) {
            switch (i10) {
            }
            Object[] objArr22 = new Object[i11];
            switch (i10) {
            }
            if (i10 != 5) {
            }
            switch (i10) {
            }
            String format22 = String.format(str, objArr22);
            if (i10 != 5) {
            }
            throw new IllegalStateException(format22);
        }
        i11 = 2;
        Object[] objArr222 = new Object[i11];
        switch (i10) {
        }
        if (i10 != 5) {
        }
        switch (i10) {
        }
        String format222 = String.format(str, objArr222);
        if (i10 != 5) {
        }
        throw new IllegalStateException(format222);
    }

    public static ClassId e(String str) {
        if (str == null) {
            a(11);
        }
        return f(str, false);
    }

    public static ClassId f(String str, boolean z10) {
        String str2;
        if (str == null) {
            a(12);
        }
        int lastIndexOf = str.lastIndexOf("/");
        if (lastIndexOf == -1) {
            str2 = "";
        } else {
            String replace = str.substring(0, lastIndexOf).replace('/', '.');
            str = str.substring(lastIndexOf + 1);
            str2 = replace;
        }
        return new ClassId(new FqName(str2), new FqName(str), z10);
    }

    public static ClassId m(FqName fqName) {
        if (fqName == null) {
            a(0);
        }
        return new ClassId(fqName.e(), fqName.g());
    }

    public FqName b() {
        if (this.f16428a.d()) {
            FqName fqName = this.f16429b;
            if (fqName == null) {
                a(9);
            }
            return fqName;
        }
        return new FqName(this.f16428a.b() + "." + this.f16429b.b());
    }

    public String c() {
        if (this.f16428a.d()) {
            String b10 = this.f16429b.b();
            if (b10 == null) {
                a(13);
            }
            return b10;
        }
        String str = this.f16428a.b().replace('.', '/') + "/" + this.f16429b.b();
        if (str == null) {
            a(14);
        }
        return str;
    }

    public ClassId d(Name name) {
        if (name == null) {
            a(8);
        }
        return new ClassId(h(), this.f16429b.c(name), this.f16430c);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ClassId.class != obj.getClass()) {
            return false;
        }
        ClassId classId = (ClassId) obj;
        return this.f16428a.equals(classId.f16428a) && this.f16429b.equals(classId.f16429b) && this.f16430c == classId.f16430c;
    }

    public ClassId g() {
        FqName e10 = this.f16429b.e();
        if (e10.d()) {
            return null;
        }
        return new ClassId(h(), e10, this.f16430c);
    }

    public FqName h() {
        FqName fqName = this.f16428a;
        if (fqName == null) {
            a(5);
        }
        return fqName;
    }

    public int hashCode() {
        return (((this.f16428a.hashCode() * 31) + this.f16429b.hashCode()) * 31) + Boolean.valueOf(this.f16430c).hashCode();
    }

    public FqName i() {
        FqName fqName = this.f16429b;
        if (fqName == null) {
            a(6);
        }
        return fqName;
    }

    public Name j() {
        Name g6 = this.f16429b.g();
        if (g6 == null) {
            a(7);
        }
        return g6;
    }

    public boolean k() {
        return this.f16430c;
    }

    public boolean l() {
        return !this.f16429b.e().d();
    }

    public String toString() {
        if (!this.f16428a.d()) {
            return c();
        }
        return "/" + c();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ClassId(FqName fqName, Name name) {
        this(fqName, FqName.k(name), false);
        if (fqName == null) {
            a(3);
        }
        if (name == null) {
            a(4);
        }
    }
}
