package cd;

import cd.ProtoContainer;
import java.util.List;
import lc.NameResolver;

/* compiled from: AnnotationLoader.kt */
/* renamed from: cd.f, reason: use source file name */
/* loaded from: classes2.dex */
public interface AnnotationLoader<A> {
    List<A> a(ProtoContainer protoContainer, jc.n nVar);

    List<A> c(ProtoContainer protoContainer, jc.n nVar);

    List<A> d(jc.q qVar, NameResolver nameResolver);

    List<A> e(ProtoContainer protoContainer, qc.q qVar, AnnotatedCallableKind annotatedCallableKind);

    List<A> g(ProtoContainer protoContainer, qc.q qVar, AnnotatedCallableKind annotatedCallableKind);

    List<A> h(ProtoContainer protoContainer, jc.g gVar);

    List<A> i(ProtoContainer.a aVar);

    List<A> j(jc.s sVar, NameResolver nameResolver);

    List<A> k(ProtoContainer protoContainer, qc.q qVar, AnnotatedCallableKind annotatedCallableKind, int i10, jc.u uVar);
}
