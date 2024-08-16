package rb;

import gd.g0;
import java.util.Collection;
import java.util.List;
import kotlin.collections.r;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.SimpleFunctionDescriptor;
import za.k;

/* compiled from: AdditionalClassPartsProvider.kt */
/* renamed from: rb.a, reason: use source file name */
/* loaded from: classes2.dex */
public interface AdditionalClassPartsProvider {

    /* compiled from: AdditionalClassPartsProvider.kt */
    /* renamed from: rb.a$a */
    /* loaded from: classes2.dex */
    public static final class a implements AdditionalClassPartsProvider {

        /* renamed from: a, reason: collision with root package name */
        public static final a f17688a = new a();

        private a() {
        }

        @Override // rb.AdditionalClassPartsProvider
        public Collection<Name> a(ClassDescriptor classDescriptor) {
            List j10;
            k.e(classDescriptor, "classDescriptor");
            j10 = r.j();
            return j10;
        }

        @Override // rb.AdditionalClassPartsProvider
        public Collection<SimpleFunctionDescriptor> b(Name name, ClassDescriptor classDescriptor) {
            List j10;
            k.e(name, "name");
            k.e(classDescriptor, "classDescriptor");
            j10 = r.j();
            return j10;
        }

        @Override // rb.AdditionalClassPartsProvider
        public Collection<g0> c(ClassDescriptor classDescriptor) {
            List j10;
            k.e(classDescriptor, "classDescriptor");
            j10 = r.j();
            return j10;
        }

        @Override // rb.AdditionalClassPartsProvider
        public Collection<ClassConstructorDescriptor> e(ClassDescriptor classDescriptor) {
            List j10;
            k.e(classDescriptor, "classDescriptor");
            j10 = r.j();
            return j10;
        }
    }

    Collection<Name> a(ClassDescriptor classDescriptor);

    Collection<SimpleFunctionDescriptor> b(Name name, ClassDescriptor classDescriptor);

    Collection<g0> c(ClassDescriptor classDescriptor);

    Collection<ClassConstructorDescriptor> e(ClassDescriptor classDescriptor);
}
