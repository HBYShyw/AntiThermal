package pb;

import gd.TypeConstructor;
import java.util.Collection;
import ma.Unit;

/* compiled from: SupertypeLoopChecker.kt */
/* renamed from: pb.d1, reason: use source file name */
/* loaded from: classes2.dex */
public interface SupertypeLoopChecker {

    /* compiled from: SupertypeLoopChecker.kt */
    /* renamed from: pb.d1$a */
    /* loaded from: classes2.dex */
    public static final class a implements SupertypeLoopChecker {

        /* renamed from: a, reason: collision with root package name */
        public static final a f16675a = new a();

        private a() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // pb.SupertypeLoopChecker
        public Collection<gd.g0> a(TypeConstructor typeConstructor, Collection<? extends gd.g0> collection, ya.l<? super TypeConstructor, ? extends Iterable<? extends gd.g0>> lVar, ya.l<? super gd.g0, Unit> lVar2) {
            za.k.e(typeConstructor, "currentTypeConstructor");
            za.k.e(collection, "superTypes");
            za.k.e(lVar, "neighbors");
            za.k.e(lVar2, "reportLoop");
            return collection;
        }
    }

    Collection<gd.g0> a(TypeConstructor typeConstructor, Collection<? extends gd.g0> collection, ya.l<? super TypeConstructor, ? extends Iterable<? extends gd.g0>> lVar, ya.l<? super gd.g0, Unit> lVar2);
}
