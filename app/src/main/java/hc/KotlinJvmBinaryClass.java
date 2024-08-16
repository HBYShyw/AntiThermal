package hc;

import ic.KotlinClassHeader;
import oc.ClassId;
import oc.Name;
import pb.SourceElement;
import uc.ClassLiteralValue;

/* compiled from: KotlinJvmBinaryClass.kt */
/* renamed from: hc.r, reason: use source file name */
/* loaded from: classes2.dex */
public interface KotlinJvmBinaryClass {

    /* compiled from: KotlinJvmBinaryClass.kt */
    /* renamed from: hc.r$a */
    /* loaded from: classes2.dex */
    public interface a {
        void a();

        a b(Name name, ClassId classId);

        void c(Name name, Object obj);

        b d(Name name);

        void e(Name name, ClassId classId, Name name2);

        void f(Name name, ClassLiteralValue classLiteralValue);
    }

    /* compiled from: KotlinJvmBinaryClass.kt */
    /* renamed from: hc.r$b */
    /* loaded from: classes2.dex */
    public interface b {
        void a();

        void b(Object obj);

        a c(ClassId classId);

        void d(ClassId classId, Name name);

        void e(ClassLiteralValue classLiteralValue);
    }

    /* compiled from: KotlinJvmBinaryClass.kt */
    /* renamed from: hc.r$c */
    /* loaded from: classes2.dex */
    public interface c {
        void a();

        a b(ClassId classId, SourceElement sourceElement);
    }

    /* compiled from: KotlinJvmBinaryClass.kt */
    /* renamed from: hc.r$d */
    /* loaded from: classes2.dex */
    public interface d {
        e a(Name name, String str);

        c b(Name name, String str, Object obj);
    }

    /* compiled from: KotlinJvmBinaryClass.kt */
    /* renamed from: hc.r$e */
    /* loaded from: classes2.dex */
    public interface e extends c {
        a c(int i10, ClassId classId, SourceElement sourceElement);
    }

    String a();

    KotlinClassHeader b();

    void c(d dVar, byte[] bArr);

    void d(c cVar, byte[] bArr);

    ClassId e();
}
