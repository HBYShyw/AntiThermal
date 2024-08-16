package xc;

import oc.ClassId;
import oc.FqName;

/* compiled from: JvmClassName.java */
/* renamed from: xc.d, reason: use source file name */
/* loaded from: classes2.dex */
public class JvmClassName {

    /* renamed from: a, reason: collision with root package name */
    private final String f19698a;

    /* renamed from: b, reason: collision with root package name */
    private FqName f19699b;

    private JvmClassName(String str) {
        if (str == null) {
            a(5);
        }
        this.f19698a = str;
    }

    private static /* synthetic */ void a(int i10) {
        String str = (i10 == 3 || i10 == 6 || i10 == 7 || i10 == 8) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 3 || i10 == 6 || i10 == 7 || i10 == 8) ? 2 : 3];
        switch (i10) {
            case 1:
                objArr[0] = "classId";
                break;
            case 2:
            case 4:
                objArr[0] = "fqName";
                break;
            case 3:
            case 6:
            case 7:
            case 8:
                objArr[0] = "kotlin/reflect/jvm/internal/impl/resolve/jvm/JvmClassName";
                break;
            case 5:
            default:
                objArr[0] = "internalName";
                break;
        }
        if (i10 == 3) {
            objArr[1] = "byFqNameWithoutInnerClasses";
        } else if (i10 == 6) {
            objArr[1] = "getFqNameForClassNameWithoutDollars";
        } else if (i10 == 7) {
            objArr[1] = "getPackageFqName";
        } else if (i10 != 8) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/resolve/jvm/JvmClassName";
        } else {
            objArr[1] = "getInternalName";
        }
        switch (i10) {
            case 1:
                objArr[2] = "byClassId";
                break;
            case 2:
            case 4:
                objArr[2] = "byFqNameWithoutInnerClasses";
                break;
            case 3:
            case 6:
            case 7:
            case 8:
                break;
            case 5:
                objArr[2] = "<init>";
                break;
            default:
                objArr[2] = "byInternalName";
                break;
        }
        String format = String.format(str, objArr);
        if (i10 != 3 && i10 != 6 && i10 != 7 && i10 != 8) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    public static JvmClassName b(ClassId classId) {
        if (classId == null) {
            a(1);
        }
        FqName h10 = classId.h();
        String replace = classId.i().b().replace('.', '$');
        if (h10.d()) {
            return new JvmClassName(replace);
        }
        return new JvmClassName(h10.b().replace('.', '/') + "/" + replace);
    }

    public static JvmClassName c(FqName fqName) {
        if (fqName == null) {
            a(2);
        }
        JvmClassName jvmClassName = new JvmClassName(fqName.b().replace('.', '/'));
        jvmClassName.f19699b = fqName;
        return jvmClassName;
    }

    public static JvmClassName d(String str) {
        if (str == null) {
            a(0);
        }
        return new JvmClassName(str);
    }

    public FqName e() {
        return new FqName(this.f19698a.replace('/', '.'));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.f19698a.equals(((JvmClassName) obj).f19698a);
    }

    public String f() {
        String str = this.f19698a;
        if (str == null) {
            a(8);
        }
        return str;
    }

    public FqName g() {
        int lastIndexOf = this.f19698a.lastIndexOf("/");
        if (lastIndexOf == -1) {
            FqName fqName = FqName.f16431c;
            if (fqName == null) {
                a(7);
            }
            return fqName;
        }
        return new FqName(this.f19698a.substring(0, lastIndexOf).replace('/', '.'));
    }

    public int hashCode() {
        return this.f19698a.hashCode();
    }

    public String toString() {
        return this.f19698a;
    }
}
