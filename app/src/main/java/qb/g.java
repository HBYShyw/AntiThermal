package qb;

import java.util.Iterator;
import java.util.List;
import kotlin.collections.p;
import oc.FqName;

/* compiled from: Annotations.kt */
/* loaded from: classes2.dex */
public interface g extends Iterable<AnnotationDescriptor>, ab.a {

    /* renamed from: b, reason: collision with root package name */
    public static final a f17195b = a.f17196a;

    /* compiled from: Annotations.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f17196a = new a();

        /* renamed from: b, reason: collision with root package name */
        private static final g f17197b = new C0091a();

        /* compiled from: Annotations.kt */
        /* renamed from: qb.g$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0091a implements g {
            C0091a() {
            }

            @Override // qb.g
            public boolean a(FqName fqName) {
                return b.b(this, fqName);
            }

            public Void d(FqName fqName) {
                za.k.e(fqName, "fqName");
                return null;
            }

            @Override // qb.g
            public boolean isEmpty() {
                return true;
            }

            @Override // java.lang.Iterable
            public Iterator<AnnotationDescriptor> iterator() {
                return p.j().iterator();
            }

            @Override // qb.g
            public /* bridge */ /* synthetic */ AnnotationDescriptor j(FqName fqName) {
                return (AnnotationDescriptor) d(fqName);
            }

            public String toString() {
                return "EMPTY";
            }
        }

        private a() {
        }

        public final g a(List<? extends AnnotationDescriptor> list) {
            za.k.e(list, "annotations");
            return list.isEmpty() ? f17197b : new AnnotationsImpl(list);
        }

        public final g b() {
            return f17197b;
        }
    }

    /* compiled from: Annotations.kt */
    /* loaded from: classes2.dex */
    public static final class b {
        public static AnnotationDescriptor a(g gVar, FqName fqName) {
            AnnotationDescriptor annotationDescriptor;
            za.k.e(fqName, "fqName");
            Iterator<AnnotationDescriptor> it = gVar.iterator();
            while (true) {
                if (!it.hasNext()) {
                    annotationDescriptor = null;
                    break;
                }
                annotationDescriptor = it.next();
                if (za.k.a(annotationDescriptor.d(), fqName)) {
                    break;
                }
            }
            return annotationDescriptor;
        }

        public static boolean b(g gVar, FqName fqName) {
            za.k.e(fqName, "fqName");
            return gVar.j(fqName) != null;
        }
    }

    boolean a(FqName fqName);

    boolean isEmpty();

    AnnotationDescriptor j(FqName fqName);
}
