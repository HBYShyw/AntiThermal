package ub;

import hc.KotlinJvmBinaryClass;
import ic.KotlinClassHeader;
import ic.ReadKotlinClassHeaderAnnotationVisitor;
import oc.ClassId;
import sd.StringsJVM;
import vb.reflectClassUtil;
import za.DefaultConstructorMarker;

/* compiled from: ReflectKotlinClass.kt */
/* loaded from: classes2.dex */
public final class f implements KotlinJvmBinaryClass {

    /* renamed from: c, reason: collision with root package name */
    public static final a f18974c = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final Class<?> f18975a;

    /* renamed from: b, reason: collision with root package name */
    private final KotlinClassHeader f18976b;

    /* compiled from: ReflectKotlinClass.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final f a(Class<?> cls) {
            za.k.e(cls, "klass");
            ReadKotlinClassHeaderAnnotationVisitor readKotlinClassHeaderAnnotationVisitor = new ReadKotlinClassHeaderAnnotationVisitor();
            c.f18972a.b(cls, readKotlinClassHeaderAnnotationVisitor);
            KotlinClassHeader m10 = readKotlinClassHeaderAnnotationVisitor.m();
            DefaultConstructorMarker defaultConstructorMarker = null;
            if (m10 == null) {
                return null;
            }
            return new f(cls, m10, defaultConstructorMarker);
        }
    }

    private f(Class<?> cls, KotlinClassHeader kotlinClassHeader) {
        this.f18975a = cls;
        this.f18976b = kotlinClassHeader;
    }

    public /* synthetic */ f(Class cls, KotlinClassHeader kotlinClassHeader, DefaultConstructorMarker defaultConstructorMarker) {
        this(cls, kotlinClassHeader);
    }

    @Override // hc.KotlinJvmBinaryClass
    public String a() {
        String y4;
        StringBuilder sb2 = new StringBuilder();
        String name = this.f18975a.getName();
        za.k.d(name, "klass.name");
        y4 = StringsJVM.y(name, '.', '/', false, 4, null);
        sb2.append(y4);
        sb2.append(".class");
        return sb2.toString();
    }

    @Override // hc.KotlinJvmBinaryClass
    public KotlinClassHeader b() {
        return this.f18976b;
    }

    @Override // hc.KotlinJvmBinaryClass
    public void c(KotlinJvmBinaryClass.d dVar, byte[] bArr) {
        za.k.e(dVar, "visitor");
        c.f18972a.i(this.f18975a, dVar);
    }

    @Override // hc.KotlinJvmBinaryClass
    public void d(KotlinJvmBinaryClass.c cVar, byte[] bArr) {
        za.k.e(cVar, "visitor");
        c.f18972a.b(this.f18975a, cVar);
    }

    @Override // hc.KotlinJvmBinaryClass
    public ClassId e() {
        return reflectClassUtil.a(this.f18975a);
    }

    public boolean equals(Object obj) {
        return (obj instanceof f) && za.k.a(this.f18975a, ((f) obj).f18975a);
    }

    public final Class<?> f() {
        return this.f18975a;
    }

    public int hashCode() {
        return this.f18975a.hashCode();
    }

    public String toString() {
        return f.class.getName() + ": " + this.f18975a;
    }
}
