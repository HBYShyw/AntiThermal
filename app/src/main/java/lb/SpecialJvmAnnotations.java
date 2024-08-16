package lb;

import hc.KotlinJvmBinaryClass;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections.r;
import oc.ClassId;
import oc.FqName;
import pb.SourceElement;
import yb.JvmAbi;
import yb.b0;
import za.k;
import za.w;

/* compiled from: SpecialJvmAnnotations.kt */
/* renamed from: lb.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class SpecialJvmAnnotations {

    /* renamed from: a, reason: collision with root package name */
    public static final SpecialJvmAnnotations f14656a = new SpecialJvmAnnotations();

    /* renamed from: b, reason: collision with root package name */
    private static final Set<ClassId> f14657b;

    /* renamed from: c, reason: collision with root package name */
    private static final ClassId f14658c;

    /* compiled from: SpecialJvmAnnotations.kt */
    /* renamed from: lb.a$a */
    /* loaded from: classes2.dex */
    public static final class a implements KotlinJvmBinaryClass.c {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ w f14659a;

        a(w wVar) {
            this.f14659a = wVar;
        }

        @Override // hc.KotlinJvmBinaryClass.c
        public void a() {
        }

        @Override // hc.KotlinJvmBinaryClass.c
        public KotlinJvmBinaryClass.a b(ClassId classId, SourceElement sourceElement) {
            k.e(classId, "classId");
            k.e(sourceElement, "source");
            if (!k.a(classId, JvmAbi.f20005a.a())) {
                return null;
            }
            this.f14659a.f20374e = true;
            return null;
        }
    }

    static {
        List m10;
        m10 = r.m(b0.f20018a, b0.f20028k, b0.f20029l, b0.f20021d, b0.f20023f, b0.f20026i);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Iterator it = m10.iterator();
        while (it.hasNext()) {
            linkedHashSet.add(ClassId.m((FqName) it.next()));
        }
        f14657b = linkedHashSet;
        ClassId m11 = ClassId.m(b0.f20027j);
        k.d(m11, "topLevel(JvmAnnotationNames.REPEATABLE_ANNOTATION)");
        f14658c = m11;
    }

    private SpecialJvmAnnotations() {
    }

    public final ClassId a() {
        return f14658c;
    }

    public final Set<ClassId> b() {
        return f14657b;
    }

    public final boolean c(KotlinJvmBinaryClass kotlinJvmBinaryClass) {
        k.e(kotlinJvmBinaryClass, "klass");
        w wVar = new w();
        kotlinJvmBinaryClass.d(new a(wVar), null);
        return wVar.f20374e;
    }
}
