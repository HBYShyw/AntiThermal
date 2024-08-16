package xc;

import bc.g;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.MutableCollections;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.SimpleFunctionDescriptor;
import za.k;

/* compiled from: SyntheticJavaPartsProvider.kt */
/* loaded from: classes2.dex */
public final class a implements f {

    /* renamed from: b, reason: collision with root package name */
    private final List<f> f19695b;

    /* JADX WARN: Multi-variable type inference failed */
    public a(List<? extends f> list) {
        k.e(list, "inner");
        this.f19695b = list;
    }

    @Override // xc.f
    public List<Name> a(g gVar, ClassDescriptor classDescriptor) {
        k.e(gVar, "<this>");
        k.e(classDescriptor, "thisDescriptor");
        List<f> list = this.f19695b;
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            MutableCollections.z(arrayList, ((f) it.next()).a(gVar, classDescriptor));
        }
        return arrayList;
    }

    @Override // xc.f
    public void b(g gVar, ClassDescriptor classDescriptor, Name name, List<ClassDescriptor> list) {
        k.e(gVar, "<this>");
        k.e(classDescriptor, "thisDescriptor");
        k.e(name, "name");
        k.e(list, "result");
        Iterator<T> it = this.f19695b.iterator();
        while (it.hasNext()) {
            ((f) it.next()).b(gVar, classDescriptor, name, list);
        }
    }

    @Override // xc.f
    public void c(g gVar, ClassDescriptor classDescriptor, Name name, Collection<SimpleFunctionDescriptor> collection) {
        k.e(gVar, "<this>");
        k.e(classDescriptor, "thisDescriptor");
        k.e(name, "name");
        k.e(collection, "result");
        Iterator<T> it = this.f19695b.iterator();
        while (it.hasNext()) {
            ((f) it.next()).c(gVar, classDescriptor, name, collection);
        }
    }

    @Override // xc.f
    public void d(g gVar, ClassDescriptor classDescriptor, List<ClassConstructorDescriptor> list) {
        k.e(gVar, "<this>");
        k.e(classDescriptor, "thisDescriptor");
        k.e(list, "result");
        Iterator<T> it = this.f19695b.iterator();
        while (it.hasNext()) {
            ((f) it.next()).d(gVar, classDescriptor, list);
        }
    }

    @Override // xc.f
    public List<Name> e(g gVar, ClassDescriptor classDescriptor) {
        k.e(gVar, "<this>");
        k.e(classDescriptor, "thisDescriptor");
        List<f> list = this.f19695b;
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            MutableCollections.z(arrayList, ((f) it.next()).e(gVar, classDescriptor));
        }
        return arrayList;
    }

    @Override // xc.f
    public List<Name> f(g gVar, ClassDescriptor classDescriptor) {
        k.e(gVar, "<this>");
        k.e(classDescriptor, "thisDescriptor");
        List<f> list = this.f19695b;
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            MutableCollections.z(arrayList, ((f) it.next()).f(gVar, classDescriptor));
        }
        return arrayList;
    }

    @Override // xc.f
    public void g(g gVar, ClassDescriptor classDescriptor, Name name, Collection<SimpleFunctionDescriptor> collection) {
        k.e(gVar, "<this>");
        k.e(classDescriptor, "thisDescriptor");
        k.e(name, "name");
        k.e(collection, "result");
        Iterator<T> it = this.f19695b.iterator();
        while (it.hasNext()) {
            ((f) it.next()).g(gVar, classDescriptor, name, collection);
        }
    }
}
