package ic;

import hc.KotlinJvmBinaryClass;
import ic.KotlinClassHeader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.BitEncoding;
import nc.JvmMetadataVersion;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.SourceElement;
import uc.ClassLiteralValue;
import yb.b0;

/* compiled from: ReadKotlinClassHeaderAnnotationVisitor.java */
/* renamed from: ic.b, reason: use source file name */
/* loaded from: classes2.dex */
public class ReadKotlinClassHeaderAnnotationVisitor implements KotlinJvmBinaryClass.c {

    /* renamed from: j, reason: collision with root package name */
    private static final boolean f12724j = "true".equals(System.getProperty("kotlin.ignore.old.metadata"));

    /* renamed from: k, reason: collision with root package name */
    private static final Map<ClassId, KotlinClassHeader.a> f12725k;

    /* renamed from: a, reason: collision with root package name */
    private int[] f12726a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f12727b = null;

    /* renamed from: c, reason: collision with root package name */
    private int f12728c = 0;

    /* renamed from: d, reason: collision with root package name */
    private String f12729d = null;

    /* renamed from: e, reason: collision with root package name */
    private String[] f12730e = null;

    /* renamed from: f, reason: collision with root package name */
    private String[] f12731f = null;

    /* renamed from: g, reason: collision with root package name */
    private String[] f12732g = null;

    /* renamed from: h, reason: collision with root package name */
    private KotlinClassHeader.a f12733h = null;

    /* renamed from: i, reason: collision with root package name */
    private String[] f12734i = null;

    /* compiled from: ReadKotlinClassHeaderAnnotationVisitor.java */
    /* renamed from: ic.b$b */
    /* loaded from: classes2.dex */
    private static abstract class b implements KotlinJvmBinaryClass.b {

        /* renamed from: a, reason: collision with root package name */
        private final List<String> f12735a = new ArrayList();

        private static /* synthetic */ void f(int i10) {
            Object[] objArr = new Object[3];
            if (i10 == 1) {
                objArr[0] = "enumEntryName";
            } else if (i10 == 2) {
                objArr[0] = "classLiteralValue";
            } else if (i10 != 3) {
                objArr[0] = "enumClassId";
            } else {
                objArr[0] = "classId";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/kotlin/header/ReadKotlinClassHeaderAnnotationVisitor$CollectStringArrayAnnotationVisitor";
            if (i10 == 2) {
                objArr[2] = "visitClassLiteral";
            } else if (i10 != 3) {
                objArr[2] = "visitEnum";
            } else {
                objArr[2] = "visitAnnotation";
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // hc.KotlinJvmBinaryClass.b
        public void a() {
            g((String[]) this.f12735a.toArray(new String[0]));
        }

        @Override // hc.KotlinJvmBinaryClass.b
        public void b(Object obj) {
            if (obj instanceof String) {
                this.f12735a.add((String) obj);
            }
        }

        @Override // hc.KotlinJvmBinaryClass.b
        public KotlinJvmBinaryClass.a c(ClassId classId) {
            if (classId != null) {
                return null;
            }
            f(3);
            return null;
        }

        @Override // hc.KotlinJvmBinaryClass.b
        public void d(ClassId classId, Name name) {
            if (classId == null) {
                f(0);
            }
            if (name == null) {
                f(1);
            }
        }

        @Override // hc.KotlinJvmBinaryClass.b
        public void e(ClassLiteralValue classLiteralValue) {
            if (classLiteralValue == null) {
                f(2);
            }
        }

        protected abstract void g(String[] strArr);
    }

    /* compiled from: ReadKotlinClassHeaderAnnotationVisitor.java */
    /* renamed from: ic.b$c */
    /* loaded from: classes2.dex */
    private class c implements KotlinJvmBinaryClass.a {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: ReadKotlinClassHeaderAnnotationVisitor.java */
        /* renamed from: ic.b$c$a */
        /* loaded from: classes2.dex */
        public class a extends b {
            a() {
            }

            private static /* synthetic */ void f(int i10) {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "result", "kotlin/reflect/jvm/internal/impl/load/kotlin/header/ReadKotlinClassHeaderAnnotationVisitor$KotlinMetadataArgumentVisitor$1", "visitEnd"));
            }

            @Override // ic.ReadKotlinClassHeaderAnnotationVisitor.b
            protected void g(String[] strArr) {
                if (strArr == null) {
                    f(0);
                }
                ReadKotlinClassHeaderAnnotationVisitor.this.f12730e = strArr;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: ReadKotlinClassHeaderAnnotationVisitor.java */
        /* renamed from: ic.b$c$b */
        /* loaded from: classes2.dex */
        public class b extends b {
            b() {
            }

            private static /* synthetic */ void f(int i10) {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "result", "kotlin/reflect/jvm/internal/impl/load/kotlin/header/ReadKotlinClassHeaderAnnotationVisitor$KotlinMetadataArgumentVisitor$2", "visitEnd"));
            }

            @Override // ic.ReadKotlinClassHeaderAnnotationVisitor.b
            protected void g(String[] strArr) {
                if (strArr == null) {
                    f(0);
                }
                ReadKotlinClassHeaderAnnotationVisitor.this.f12731f = strArr;
            }
        }

        private c() {
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 == 1) {
                objArr[0] = "enumClassId";
            } else if (i10 == 2) {
                objArr[0] = "enumEntryName";
            } else if (i10 != 3) {
                objArr[0] = "classLiteralValue";
            } else {
                objArr[0] = "classId";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/kotlin/header/ReadKotlinClassHeaderAnnotationVisitor$KotlinMetadataArgumentVisitor";
            if (i10 == 1 || i10 == 2) {
                objArr[2] = "visitEnum";
            } else if (i10 != 3) {
                objArr[2] = "visitClassLiteral";
            } else {
                objArr[2] = "visitAnnotation";
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        private KotlinJvmBinaryClass.b h() {
            return new a();
        }

        private KotlinJvmBinaryClass.b i() {
            return new b();
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void a() {
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public KotlinJvmBinaryClass.a b(Name name, ClassId classId) {
            if (classId != null) {
                return null;
            }
            g(3);
            return null;
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void c(Name name, Object obj) {
            if (name == null) {
                return;
            }
            String b10 = name.b();
            if ("k".equals(b10)) {
                if (obj instanceof Integer) {
                    ReadKotlinClassHeaderAnnotationVisitor.this.f12733h = KotlinClassHeader.a.c(((Integer) obj).intValue());
                    return;
                }
                return;
            }
            if ("mv".equals(b10)) {
                if (obj instanceof int[]) {
                    ReadKotlinClassHeaderAnnotationVisitor.this.f12726a = (int[]) obj;
                    return;
                }
                return;
            }
            if ("xs".equals(b10)) {
                if (obj instanceof String) {
                    String str = (String) obj;
                    if (str.isEmpty()) {
                        return;
                    }
                    ReadKotlinClassHeaderAnnotationVisitor.this.f12727b = str;
                    return;
                }
                return;
            }
            if ("xi".equals(b10)) {
                if (obj instanceof Integer) {
                    ReadKotlinClassHeaderAnnotationVisitor.this.f12728c = ((Integer) obj).intValue();
                    return;
                }
                return;
            }
            if ("pn".equals(b10) && (obj instanceof String)) {
                String str2 = (String) obj;
                if (str2.isEmpty()) {
                    return;
                }
                ReadKotlinClassHeaderAnnotationVisitor.this.f12729d = str2;
            }
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public KotlinJvmBinaryClass.b d(Name name) {
            String b10 = name != null ? name.b() : null;
            if ("d1".equals(b10)) {
                return h();
            }
            if ("d2".equals(b10)) {
                return i();
            }
            return null;
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void e(Name name, ClassId classId, Name name2) {
            if (classId == null) {
                g(1);
            }
            if (name2 == null) {
                g(2);
            }
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void f(Name name, ClassLiteralValue classLiteralValue) {
            if (classLiteralValue == null) {
                g(0);
            }
        }
    }

    /* compiled from: ReadKotlinClassHeaderAnnotationVisitor.java */
    /* renamed from: ic.b$d */
    /* loaded from: classes2.dex */
    private class d implements KotlinJvmBinaryClass.a {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: ReadKotlinClassHeaderAnnotationVisitor.java */
        /* renamed from: ic.b$d$a */
        /* loaded from: classes2.dex */
        public class a extends b {
            a() {
            }

            private static /* synthetic */ void f(int i10) {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "result", "kotlin/reflect/jvm/internal/impl/load/kotlin/header/ReadKotlinClassHeaderAnnotationVisitor$KotlinSerializedIrArgumentVisitor$1", "visitEnd"));
            }

            @Override // ic.ReadKotlinClassHeaderAnnotationVisitor.b
            protected void g(String[] strArr) {
                if (strArr == null) {
                    f(0);
                }
                ReadKotlinClassHeaderAnnotationVisitor.this.f12734i = strArr;
            }
        }

        private d() {
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 == 1) {
                objArr[0] = "enumClassId";
            } else if (i10 == 2) {
                objArr[0] = "enumEntryName";
            } else if (i10 != 3) {
                objArr[0] = "classLiteralValue";
            } else {
                objArr[0] = "classId";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/kotlin/header/ReadKotlinClassHeaderAnnotationVisitor$KotlinSerializedIrArgumentVisitor";
            if (i10 == 1 || i10 == 2) {
                objArr[2] = "visitEnum";
            } else if (i10 != 3) {
                objArr[2] = "visitClassLiteral";
            } else {
                objArr[2] = "visitAnnotation";
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        private KotlinJvmBinaryClass.b h() {
            return new a();
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void a() {
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public KotlinJvmBinaryClass.a b(Name name, ClassId classId) {
            if (classId != null) {
                return null;
            }
            g(3);
            return null;
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void c(Name name, Object obj) {
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public KotlinJvmBinaryClass.b d(Name name) {
            if ("b".equals(name != null ? name.b() : null)) {
                return h();
            }
            return null;
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void e(Name name, ClassId classId, Name name2) {
            if (classId == null) {
                g(1);
            }
            if (name2 == null) {
                g(2);
            }
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void f(Name name, ClassLiteralValue classLiteralValue) {
            if (classLiteralValue == null) {
                g(0);
            }
        }
    }

    /* compiled from: ReadKotlinClassHeaderAnnotationVisitor.java */
    /* renamed from: ic.b$e */
    /* loaded from: classes2.dex */
    private class e implements KotlinJvmBinaryClass.a {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: ReadKotlinClassHeaderAnnotationVisitor.java */
        /* renamed from: ic.b$e$a */
        /* loaded from: classes2.dex */
        public class a extends b {
            a() {
            }

            private static /* synthetic */ void f(int i10) {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "data", "kotlin/reflect/jvm/internal/impl/load/kotlin/header/ReadKotlinClassHeaderAnnotationVisitor$OldDeprecatedAnnotationArgumentVisitor$1", "visitEnd"));
            }

            @Override // ic.ReadKotlinClassHeaderAnnotationVisitor.b
            protected void g(String[] strArr) {
                if (strArr == null) {
                    f(0);
                }
                ReadKotlinClassHeaderAnnotationVisitor.this.f12730e = strArr;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: ReadKotlinClassHeaderAnnotationVisitor.java */
        /* renamed from: ic.b$e$b */
        /* loaded from: classes2.dex */
        public class b extends b {
            b() {
            }

            private static /* synthetic */ void f(int i10) {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "data", "kotlin/reflect/jvm/internal/impl/load/kotlin/header/ReadKotlinClassHeaderAnnotationVisitor$OldDeprecatedAnnotationArgumentVisitor$2", "visitEnd"));
            }

            @Override // ic.ReadKotlinClassHeaderAnnotationVisitor.b
            protected void g(String[] strArr) {
                if (strArr == null) {
                    f(0);
                }
                ReadKotlinClassHeaderAnnotationVisitor.this.f12731f = strArr;
            }
        }

        private e() {
        }

        private static /* synthetic */ void g(int i10) {
            Object[] objArr = new Object[3];
            if (i10 == 1) {
                objArr[0] = "enumClassId";
            } else if (i10 == 2) {
                objArr[0] = "enumEntryName";
            } else if (i10 != 3) {
                objArr[0] = "classLiteralValue";
            } else {
                objArr[0] = "classId";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/load/kotlin/header/ReadKotlinClassHeaderAnnotationVisitor$OldDeprecatedAnnotationArgumentVisitor";
            if (i10 == 1 || i10 == 2) {
                objArr[2] = "visitEnum";
            } else if (i10 != 3) {
                objArr[2] = "visitClassLiteral";
            } else {
                objArr[2] = "visitAnnotation";
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        private KotlinJvmBinaryClass.b h() {
            return new a();
        }

        private KotlinJvmBinaryClass.b i() {
            return new b();
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void a() {
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public KotlinJvmBinaryClass.a b(Name name, ClassId classId) {
            if (classId != null) {
                return null;
            }
            g(3);
            return null;
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void c(Name name, Object obj) {
            if (name == null) {
                return;
            }
            String b10 = name.b();
            if ("version".equals(b10)) {
                if (obj instanceof int[]) {
                    ReadKotlinClassHeaderAnnotationVisitor.this.f12726a = (int[]) obj;
                }
            } else if ("multifileClassName".equals(b10)) {
                ReadKotlinClassHeaderAnnotationVisitor.this.f12727b = obj instanceof String ? (String) obj : null;
            }
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public KotlinJvmBinaryClass.b d(Name name) {
            String b10 = name != null ? name.b() : null;
            if (!"data".equals(b10) && !"filePartClassNames".equals(b10)) {
                if ("strings".equals(b10)) {
                    return i();
                }
                return null;
            }
            return h();
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void e(Name name, ClassId classId, Name name2) {
            if (classId == null) {
                g(1);
            }
            if (name2 == null) {
                g(2);
            }
        }

        @Override // hc.KotlinJvmBinaryClass.a
        public void f(Name name, ClassLiteralValue classLiteralValue) {
            if (classLiteralValue == null) {
                g(0);
            }
        }
    }

    static {
        HashMap hashMap = new HashMap();
        f12725k = hashMap;
        hashMap.put(ClassId.m(new FqName("kotlin.jvm.internal.KotlinClass")), KotlinClassHeader.a.CLASS);
        hashMap.put(ClassId.m(new FqName("kotlin.jvm.internal.KotlinFileFacade")), KotlinClassHeader.a.FILE_FACADE);
        hashMap.put(ClassId.m(new FqName("kotlin.jvm.internal.KotlinMultifileClass")), KotlinClassHeader.a.MULTIFILE_CLASS);
        hashMap.put(ClassId.m(new FqName("kotlin.jvm.internal.KotlinMultifileClassPart")), KotlinClassHeader.a.MULTIFILE_CLASS_PART);
        hashMap.put(ClassId.m(new FqName("kotlin.jvm.internal.KotlinSyntheticClass")), KotlinClassHeader.a.SYNTHETIC_CLASS);
    }

    private static /* synthetic */ void d(int i10) {
        Object[] objArr = new Object[3];
        if (i10 != 1) {
            objArr[0] = "classId";
        } else {
            objArr[0] = "source";
        }
        objArr[1] = "kotlin/reflect/jvm/internal/impl/load/kotlin/header/ReadKotlinClassHeaderAnnotationVisitor";
        objArr[2] = "visitAnnotation";
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
    }

    private boolean n() {
        KotlinClassHeader.a aVar = this.f12733h;
        return aVar == KotlinClassHeader.a.CLASS || aVar == KotlinClassHeader.a.FILE_FACADE || aVar == KotlinClassHeader.a.MULTIFILE_CLASS_PART;
    }

    @Override // hc.KotlinJvmBinaryClass.c
    public void a() {
    }

    @Override // hc.KotlinJvmBinaryClass.c
    public KotlinJvmBinaryClass.a b(ClassId classId, SourceElement sourceElement) {
        KotlinClassHeader.a aVar;
        if (classId == null) {
            d(0);
        }
        if (sourceElement == null) {
            d(1);
        }
        FqName b10 = classId.b();
        if (b10.equals(b0.f20018a)) {
            return new c();
        }
        if (b10.equals(b0.f20036s)) {
            return new d();
        }
        if (f12724j || this.f12733h != null || (aVar = f12725k.get(classId)) == null) {
            return null;
        }
        this.f12733h = aVar;
        return new e();
    }

    public KotlinClassHeader m() {
        if (this.f12733h == null || this.f12726a == null) {
            return null;
        }
        JvmMetadataVersion jvmMetadataVersion = new JvmMetadataVersion(this.f12726a, (this.f12728c & 8) != 0);
        if (!jvmMetadataVersion.h()) {
            this.f12732g = this.f12730e;
            this.f12730e = null;
        } else if (n() && this.f12730e == null) {
            return null;
        }
        String[] strArr = this.f12734i;
        return new KotlinClassHeader(this.f12733h, jvmMetadataVersion, this.f12730e, this.f12732g, this.f12731f, this.f12727b, this.f12728c, this.f12729d, strArr != null ? BitEncoding.e(strArr) : null);
    }
}
