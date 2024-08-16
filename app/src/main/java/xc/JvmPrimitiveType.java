package xc;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mb.PrimitiveType;
import oc.FqName;

/* compiled from: JvmPrimitiveType.java */
/* renamed from: xc.e, reason: use source file name */
/* loaded from: classes2.dex */
public enum JvmPrimitiveType {
    BOOLEAN(PrimitiveType.BOOLEAN, "boolean", "Z", "java.lang.Boolean"),
    CHAR(PrimitiveType.CHAR, "char", "C", "java.lang.Character"),
    BYTE(PrimitiveType.BYTE, "byte", "B", "java.lang.Byte"),
    SHORT(PrimitiveType.SHORT, "short", "S", "java.lang.Short"),
    INT(PrimitiveType.INT, "int", "I", "java.lang.Integer"),
    FLOAT(PrimitiveType.FLOAT, "float", "F", "java.lang.Float"),
    LONG(PrimitiveType.LONG, "long", "J", "java.lang.Long"),
    DOUBLE(PrimitiveType.DOUBLE, "double", "D", "java.lang.Double");


    /* renamed from: q, reason: collision with root package name */
    private static final Set<FqName> f19708q = new HashSet();

    /* renamed from: r, reason: collision with root package name */
    private static final Map<String, JvmPrimitiveType> f19709r = new HashMap();

    /* renamed from: s, reason: collision with root package name */
    private static final Map<PrimitiveType, JvmPrimitiveType> f19710s = new EnumMap(PrimitiveType.class);

    /* renamed from: t, reason: collision with root package name */
    private static final Map<String, JvmPrimitiveType> f19711t = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    private final PrimitiveType f19713e;

    /* renamed from: f, reason: collision with root package name */
    private final String f19714f;

    /* renamed from: g, reason: collision with root package name */
    private final String f19715g;

    /* renamed from: h, reason: collision with root package name */
    private final FqName f19716h;

    static {
        for (JvmPrimitiveType jvmPrimitiveType : values()) {
            f19708q.add(jvmPrimitiveType.g());
            f19709r.put(jvmPrimitiveType.e(), jvmPrimitiveType);
            f19710s.put(jvmPrimitiveType.f(), jvmPrimitiveType);
            f19711t.put(jvmPrimitiveType.d(), jvmPrimitiveType);
        }
    }

    JvmPrimitiveType(PrimitiveType primitiveType, String str, String str2, String str3) {
        if (primitiveType == null) {
            a(6);
        }
        if (str == null) {
            a(7);
        }
        if (str2 == null) {
            a(8);
        }
        if (str3 == null) {
            a(9);
        }
        this.f19713e = primitiveType;
        this.f19714f = str;
        this.f19715g = str2;
        this.f19716h = new FqName(str3);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0020  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0045 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x007d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x002a  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0034  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0015  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static /* synthetic */ void a(int i10) {
        String str;
        int i11;
        Object[] objArr;
        if (i10 != 2 && i10 != 4) {
            switch (i10) {
                case 10:
                case 11:
                case 12:
                case 13:
                    break;
                default:
                    str = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                    break;
            }
            if (i10 != 2 && i10 != 4) {
                switch (i10) {
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                        break;
                    default:
                        i11 = 3;
                        break;
                }
                objArr = new Object[i11];
                switch (i10) {
                    case 1:
                    case 7:
                        objArr[0] = "name";
                        break;
                    case 2:
                    case 4:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                        objArr[0] = "kotlin/reflect/jvm/internal/impl/resolve/jvm/JvmPrimitiveType";
                        break;
                    case 3:
                        objArr[0] = "type";
                        break;
                    case 5:
                    case 8:
                        objArr[0] = "desc";
                        break;
                    case 6:
                        objArr[0] = "primitiveType";
                        break;
                    case 9:
                        objArr[0] = "wrapperClassName";
                        break;
                    default:
                        objArr[0] = "className";
                        break;
                }
                if (i10 == 2 && i10 != 4) {
                    switch (i10) {
                        case 10:
                            objArr[1] = "getPrimitiveType";
                            break;
                        case 11:
                            objArr[1] = "getJavaKeywordName";
                            break;
                        case 12:
                            objArr[1] = "getDesc";
                            break;
                        case 13:
                            objArr[1] = "getWrapperFqName";
                            break;
                        default:
                            objArr[1] = "kotlin/reflect/jvm/internal/impl/resolve/jvm/JvmPrimitiveType";
                            break;
                    }
                } else {
                    objArr[1] = "get";
                }
                switch (i10) {
                    case 1:
                    case 3:
                        objArr[2] = "get";
                        break;
                    case 2:
                    case 4:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                        break;
                    case 5:
                        objArr[2] = "getByDesc";
                        break;
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        objArr[2] = "<init>";
                        break;
                    default:
                        objArr[2] = "isWrapperClassName";
                        break;
                }
                String format = String.format(str, objArr);
                if (i10 != 2 && i10 != 4) {
                    switch (i10) {
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                            break;
                        default:
                            throw new IllegalArgumentException(format);
                    }
                }
                throw new IllegalStateException(format);
            }
            i11 = 2;
            objArr = new Object[i11];
            switch (i10) {
            }
            if (i10 == 2) {
            }
            objArr[1] = "get";
            switch (i10) {
            }
            String format2 = String.format(str, objArr);
            if (i10 != 2) {
                switch (i10) {
                }
            }
            throw new IllegalStateException(format2);
        }
        str = "@NotNull method %s.%s must not return null";
        if (i10 != 2) {
            switch (i10) {
            }
            objArr = new Object[i11];
            switch (i10) {
            }
            if (i10 == 2) {
            }
            objArr[1] = "get";
            switch (i10) {
            }
            String format22 = String.format(str, objArr);
            if (i10 != 2) {
            }
            throw new IllegalStateException(format22);
        }
        i11 = 2;
        objArr = new Object[i11];
        switch (i10) {
        }
        if (i10 == 2) {
        }
        objArr[1] = "get";
        switch (i10) {
        }
        String format222 = String.format(str, objArr);
        if (i10 != 2) {
        }
        throw new IllegalStateException(format222);
    }

    public static JvmPrimitiveType b(String str) {
        if (str == null) {
            a(1);
        }
        JvmPrimitiveType jvmPrimitiveType = f19709r.get(str);
        if (jvmPrimitiveType != null) {
            return jvmPrimitiveType;
        }
        throw new AssertionError("Non-primitive type name passed: " + str);
    }

    public static JvmPrimitiveType c(PrimitiveType primitiveType) {
        if (primitiveType == null) {
            a(3);
        }
        JvmPrimitiveType jvmPrimitiveType = f19710s.get(primitiveType);
        if (jvmPrimitiveType == null) {
            a(4);
        }
        return jvmPrimitiveType;
    }

    public String d() {
        String str = this.f19715g;
        if (str == null) {
            a(12);
        }
        return str;
    }

    public String e() {
        String str = this.f19714f;
        if (str == null) {
            a(11);
        }
        return str;
    }

    public PrimitiveType f() {
        PrimitiveType primitiveType = this.f19713e;
        if (primitiveType == null) {
            a(10);
        }
        return primitiveType;
    }

    public FqName g() {
        FqName fqName = this.f19716h;
        if (fqName == null) {
            a(13);
        }
        return fqName;
    }
}
