package rc;

import gd.TypeProjection;
import gd.g0;
import java.util.Set;
import kotlin.collections.s0;
import ma.NoWhenBranchMatchedException;
import ma.Unit;
import mb.KotlinBuiltIns;
import oc.FqNameUnsafe;
import oc.Name;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptorWithTypeParameters;
import pb.DeclarationDescriptor;
import pb.TypeAliasDescriptor;
import pb.ValueParameterDescriptor;
import qb.AnnotationDescriptor;
import qb.AnnotationUseSiteTarget;
import rc.ClassifierNamePolicy;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: DescriptorRenderer.kt */
/* loaded from: classes2.dex */
public abstract class c {

    /* renamed from: a */
    public static final k f17702a;

    /* renamed from: b */
    public static final c f17703b;

    /* renamed from: c */
    public static final c f17704c;

    /* renamed from: d */
    public static final c f17705d;

    /* renamed from: e */
    public static final c f17706e;

    /* renamed from: f */
    public static final c f17707f;

    /* renamed from: g */
    public static final c f17708g;

    /* renamed from: h */
    public static final c f17709h;

    /* renamed from: i */
    public static final c f17710i;

    /* renamed from: j */
    public static final c f17711j;

    /* renamed from: k */
    public static final c f17712k;

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<rc.f, Unit> {

        /* renamed from: e */
        public static final a f17713e = new a();

        a() {
            super(1);
        }

        public final void a(rc.f fVar) {
            Set<? extends rc.e> e10;
            za.k.e(fVar, "$this$withOptions");
            fVar.e(false);
            e10 = s0.e();
            fVar.c(e10);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(rc.f fVar) {
            a(fVar);
            return Unit.f15173a;
        }
    }

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<rc.f, Unit> {

        /* renamed from: e */
        public static final b f17714e = new b();

        b() {
            super(1);
        }

        public final void a(rc.f fVar) {
            Set<? extends rc.e> e10;
            za.k.e(fVar, "$this$withOptions");
            fVar.e(false);
            e10 = s0.e();
            fVar.c(e10);
            fVar.g(true);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(rc.f fVar) {
            a(fVar);
            return Unit.f15173a;
        }
    }

    /* compiled from: DescriptorRenderer.kt */
    /* renamed from: rc.c$c */
    /* loaded from: classes2.dex */
    static final class C0097c extends Lambda implements ya.l<rc.f, Unit> {

        /* renamed from: e */
        public static final C0097c f17715e = new C0097c();

        C0097c() {
            super(1);
        }

        public final void a(rc.f fVar) {
            za.k.e(fVar, "$this$withOptions");
            fVar.e(false);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(rc.f fVar) {
            a(fVar);
            return Unit.f15173a;
        }
    }

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.l<rc.f, Unit> {

        /* renamed from: e */
        public static final d f17716e = new d();

        d() {
            super(1);
        }

        public final void a(rc.f fVar) {
            Set<? extends rc.e> e10;
            za.k.e(fVar, "$this$withOptions");
            e10 = s0.e();
            fVar.c(e10);
            fVar.o(ClassifierNamePolicy.b.f17700a);
            fVar.k(rc.k.ONLY_NON_SYNTHESIZED);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(rc.f fVar) {
            a(fVar);
            return Unit.f15173a;
        }
    }

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements ya.l<rc.f, Unit> {

        /* renamed from: e */
        public static final e f17717e = new e();

        e() {
            super(1);
        }

        public final void a(rc.f fVar) {
            za.k.e(fVar, "$this$withOptions");
            fVar.h(true);
            fVar.o(ClassifierNamePolicy.a.f17699a);
            fVar.c(rc.e.f17740h);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(rc.f fVar) {
            a(fVar);
            return Unit.f15173a;
        }
    }

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    static final class f extends Lambda implements ya.l<rc.f, Unit> {

        /* renamed from: e */
        public static final f f17718e = new f();

        f() {
            super(1);
        }

        public final void a(rc.f fVar) {
            za.k.e(fVar, "$this$withOptions");
            fVar.c(rc.e.f17739g);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(rc.f fVar) {
            a(fVar);
            return Unit.f15173a;
        }
    }

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    static final class g extends Lambda implements ya.l<rc.f, Unit> {

        /* renamed from: e */
        public static final g f17719e = new g();

        g() {
            super(1);
        }

        public final void a(rc.f fVar) {
            za.k.e(fVar, "$this$withOptions");
            fVar.c(rc.e.f17740h);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(rc.f fVar) {
            a(fVar);
            return Unit.f15173a;
        }
    }

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    static final class h extends Lambda implements ya.l<rc.f, Unit> {

        /* renamed from: e */
        public static final h f17720e = new h();

        h() {
            super(1);
        }

        public final void a(rc.f fVar) {
            za.k.e(fVar, "$this$withOptions");
            fVar.d(m.HTML);
            fVar.c(rc.e.f17740h);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(rc.f fVar) {
            a(fVar);
            return Unit.f15173a;
        }
    }

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    static final class i extends Lambda implements ya.l<rc.f, Unit> {

        /* renamed from: e */
        public static final i f17721e = new i();

        i() {
            super(1);
        }

        public final void a(rc.f fVar) {
            Set<? extends rc.e> e10;
            za.k.e(fVar, "$this$withOptions");
            fVar.e(false);
            e10 = s0.e();
            fVar.c(e10);
            fVar.o(ClassifierNamePolicy.b.f17700a);
            fVar.p(true);
            fVar.k(rc.k.NONE);
            fVar.j(true);
            fVar.i(true);
            fVar.g(true);
            fVar.b(true);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(rc.f fVar) {
            a(fVar);
            return Unit.f15173a;
        }
    }

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    static final class j extends Lambda implements ya.l<rc.f, Unit> {

        /* renamed from: e */
        public static final j f17722e = new j();

        j() {
            super(1);
        }

        public final void a(rc.f fVar) {
            za.k.e(fVar, "$this$withOptions");
            fVar.o(ClassifierNamePolicy.b.f17700a);
            fVar.k(rc.k.ONLY_NON_SYNTHESIZED);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(rc.f fVar) {
            a(fVar);
            return Unit.f15173a;
        }
    }

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    public static final class k {

        /* compiled from: DescriptorRenderer.kt */
        /* loaded from: classes2.dex */
        public /* synthetic */ class a {

            /* renamed from: a */
            public static final /* synthetic */ int[] f17723a;

            static {
                int[] iArr = new int[ClassKind.values().length];
                try {
                    iArr[ClassKind.CLASS.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[ClassKind.INTERFACE.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    iArr[ClassKind.ENUM_CLASS.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                try {
                    iArr[ClassKind.OBJECT.ordinal()] = 4;
                } catch (NoSuchFieldError unused4) {
                }
                try {
                    iArr[ClassKind.ANNOTATION_CLASS.ordinal()] = 5;
                } catch (NoSuchFieldError unused5) {
                }
                try {
                    iArr[ClassKind.ENUM_ENTRY.ordinal()] = 6;
                } catch (NoSuchFieldError unused6) {
                }
                f17723a = iArr;
            }
        }

        private k() {
        }

        public /* synthetic */ k(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String a(ClassifierDescriptorWithTypeParameters classifierDescriptorWithTypeParameters) {
            za.k.e(classifierDescriptorWithTypeParameters, "classifier");
            if (classifierDescriptorWithTypeParameters instanceof TypeAliasDescriptor) {
                return "typealias";
            }
            if (classifierDescriptorWithTypeParameters instanceof ClassDescriptor) {
                ClassDescriptor classDescriptor = (ClassDescriptor) classifierDescriptorWithTypeParameters;
                if (classDescriptor.F()) {
                    return "companion object";
                }
                switch (a.f17723a[classDescriptor.getKind().ordinal()]) {
                    case 1:
                        return "class";
                    case 2:
                        return "interface";
                    case 3:
                        return "enum class";
                    case 4:
                        return "object";
                    case 5:
                        return "annotation class";
                    case 6:
                        return "enum entry";
                    default:
                        throw new NoWhenBranchMatchedException();
                }
            }
            throw new AssertionError("Unexpected classifier: " + classifierDescriptorWithTypeParameters);
        }

        public final c b(ya.l<? super rc.f, Unit> lVar) {
            za.k.e(lVar, "changeOptions");
            DescriptorRendererOptionsImpl descriptorRendererOptionsImpl = new DescriptorRendererOptionsImpl();
            lVar.invoke(descriptorRendererOptionsImpl);
            descriptorRendererOptionsImpl.l0();
            return new DescriptorRendererImpl(descriptorRendererOptionsImpl);
        }
    }

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    public interface l {

        /* compiled from: DescriptorRenderer.kt */
        /* loaded from: classes2.dex */
        public static final class a implements l {

            /* renamed from: a */
            public static final a f17724a = new a();

            private a() {
            }

            @Override // rc.c.l
            public void a(ValueParameterDescriptor valueParameterDescriptor, int i10, int i11, StringBuilder sb2) {
                za.k.e(valueParameterDescriptor, "parameter");
                za.k.e(sb2, "builder");
            }

            @Override // rc.c.l
            public void b(int i10, StringBuilder sb2) {
                za.k.e(sb2, "builder");
                sb2.append("(");
            }

            @Override // rc.c.l
            public void c(int i10, StringBuilder sb2) {
                za.k.e(sb2, "builder");
                sb2.append(")");
            }

            @Override // rc.c.l
            public void d(ValueParameterDescriptor valueParameterDescriptor, int i10, int i11, StringBuilder sb2) {
                za.k.e(valueParameterDescriptor, "parameter");
                za.k.e(sb2, "builder");
                if (i10 != i11 - 1) {
                    sb2.append(", ");
                }
            }
        }

        void a(ValueParameterDescriptor valueParameterDescriptor, int i10, int i11, StringBuilder sb2);

        void b(int i10, StringBuilder sb2);

        void c(int i10, StringBuilder sb2);

        void d(ValueParameterDescriptor valueParameterDescriptor, int i10, int i11, StringBuilder sb2);
    }

    static {
        k kVar = new k(null);
        f17702a = kVar;
        f17703b = kVar.b(C0097c.f17715e);
        f17704c = kVar.b(a.f17713e);
        f17705d = kVar.b(b.f17714e);
        f17706e = kVar.b(d.f17716e);
        f17707f = kVar.b(i.f17721e);
        f17708g = kVar.b(f.f17718e);
        f17709h = kVar.b(g.f17719e);
        f17710i = kVar.b(j.f17722e);
        f17711j = kVar.b(e.f17717e);
        f17712k = kVar.b(h.f17720e);
    }

    public static /* synthetic */ String s(c cVar, AnnotationDescriptor annotationDescriptor, AnnotationUseSiteTarget annotationUseSiteTarget, int i10, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: renderAnnotation");
        }
        if ((i10 & 2) != 0) {
            annotationUseSiteTarget = null;
        }
        return cVar.r(annotationDescriptor, annotationUseSiteTarget);
    }

    public abstract String q(DeclarationDescriptor declarationDescriptor);

    public abstract String r(AnnotationDescriptor annotationDescriptor, AnnotationUseSiteTarget annotationUseSiteTarget);

    public abstract String t(String str, String str2, KotlinBuiltIns kotlinBuiltIns);

    public abstract String u(FqNameUnsafe fqNameUnsafe);

    public abstract String v(Name name, boolean z10);

    public abstract String w(g0 g0Var);

    public abstract String x(TypeProjection typeProjection);

    public final c y(ya.l<? super rc.f, Unit> lVar) {
        za.k.e(lVar, "changeOptions");
        za.k.c(this, "null cannot be cast to non-null type org.jetbrains.kotlin.renderer.DescriptorRendererImpl");
        DescriptorRendererOptionsImpl q10 = ((DescriptorRendererImpl) this).g0().q();
        lVar.invoke(q10);
        q10.l0();
        return new DescriptorRendererImpl(q10);
    }
}
