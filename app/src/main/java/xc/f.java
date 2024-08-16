package xc;

import bc.g;
import java.util.Collection;
import java.util.List;
import kotlin.collections.r;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.SimpleFunctionDescriptor;

/* compiled from: SyntheticJavaPartsProvider.kt */
/* loaded from: classes2.dex */
public interface f {

    /* renamed from: a, reason: collision with root package name */
    public static final a f19717a = a.f19718a;

    /* compiled from: SyntheticJavaPartsProvider.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f19718a = new a();

        /* renamed from: b, reason: collision with root package name */
        private static final xc.a f19719b;

        static {
            List j10;
            j10 = r.j();
            f19719b = new xc.a(j10);
        }

        private a() {
        }

        public final xc.a a() {
            return f19719b;
        }
    }

    List<Name> a(g gVar, ClassDescriptor classDescriptor);

    void b(g gVar, ClassDescriptor classDescriptor, Name name, List<ClassDescriptor> list);

    void c(g gVar, ClassDescriptor classDescriptor, Name name, Collection<SimpleFunctionDescriptor> collection);

    void d(g gVar, ClassDescriptor classDescriptor, List<ClassConstructorDescriptor> list);

    List<Name> e(g gVar, ClassDescriptor classDescriptor);

    List<Name> f(g gVar, ClassDescriptor classDescriptor);

    void g(g gVar, ClassDescriptor classDescriptor, Name name, Collection<SimpleFunctionDescriptor> collection);
}
