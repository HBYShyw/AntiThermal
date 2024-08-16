package id;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.collections.SetsJVM;
import kotlin.collections.r;
import kotlin.collections.s0;
import oc.Name;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;

/* compiled from: ErrorScope.kt */
/* renamed from: id.f, reason: use source file name */
/* loaded from: classes2.dex */
public class ErrorScope implements zc.h {

    /* renamed from: b, reason: collision with root package name */
    private final ErrorScopeKind f12761b;

    /* renamed from: c, reason: collision with root package name */
    private final String f12762c;

    public ErrorScope(ErrorScopeKind errorScopeKind, String... strArr) {
        za.k.e(errorScopeKind, "kind");
        za.k.e(strArr, "formatParams");
        this.f12761b = errorScopeKind;
        String b10 = errorScopeKind.b();
        Object[] copyOf = Arrays.copyOf(strArr, strArr.length);
        String format = String.format(b10, Arrays.copyOf(copyOf, copyOf.length));
        za.k.d(format, "format(this, *args)");
        this.f12762c = format;
    }

    @Override // zc.h
    public Set<Name> b() {
        Set<Name> e10;
        e10 = s0.e();
        return e10;
    }

    @Override // zc.h
    public Set<Name> d() {
        Set<Name> e10;
        e10 = s0.e();
        return e10;
    }

    @Override // zc.ResolutionScope
    public ClassifierDescriptor e(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        String format = String.format(ErrorEntity.ERROR_CLASS.b(), Arrays.copyOf(new Object[]{name}, 1));
        za.k.d(format, "format(this, *args)");
        Name i10 = Name.i(format);
        za.k.d(i10, "special(ErrorEntity.ERRO…S.debugText.format(name))");
        return new ErrorClassDescriptor(i10);
    }

    @Override // zc.h
    public Set<Name> f() {
        Set<Name> e10;
        e10 = s0.e();
        return e10;
    }

    @Override // zc.ResolutionScope
    public Collection<DeclarationDescriptor> g(zc.d dVar, ya.l<? super Name, Boolean> lVar) {
        List j10;
        za.k.e(dVar, "kindFilter");
        za.k.e(lVar, "nameFilter");
        j10 = r.j();
        return j10;
    }

    @Override // zc.h
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public Set<SimpleFunctionDescriptor> c(Name name, xb.b bVar) {
        Set<SimpleFunctionDescriptor> d10;
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        d10 = SetsJVM.d(new ErrorFunctionDescriptor(ErrorUtils.f12833a.h()));
        return d10;
    }

    @Override // zc.h
    /* renamed from: i, reason: merged with bridge method [inline-methods] */
    public Set<PropertyDescriptor> a(Name name, xb.b bVar) {
        za.k.e(name, "name");
        za.k.e(bVar, "location");
        return ErrorUtils.f12833a.j();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String j() {
        return this.f12762c;
    }

    public String toString() {
        return "ErrorScope{" + this.f12762c + '}';
    }
}
