package cc;

import fc.r;
import fc.w;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.collections.s0;
import oc.Name;

/* compiled from: DeclaredMemberIndex.kt */
/* loaded from: classes2.dex */
public interface b {

    /* compiled from: DeclaredMemberIndex.kt */
    /* loaded from: classes2.dex */
    public static final class a implements b {

        /* renamed from: a, reason: collision with root package name */
        public static final a f5048a = new a();

        private a() {
        }

        @Override // cc.b
        public Set<Name> a() {
            Set<Name> e10;
            e10 = s0.e();
            return e10;
        }

        @Override // cc.b
        public fc.n b(Name name) {
            za.k.e(name, "name");
            return null;
        }

        @Override // cc.b
        public w c(Name name) {
            za.k.e(name, "name");
            return null;
        }

        @Override // cc.b
        public Set<Name> e() {
            Set<Name> e10;
            e10 = s0.e();
            return e10;
        }

        @Override // cc.b
        public Set<Name> f() {
            Set<Name> e10;
            e10 = s0.e();
            return e10;
        }

        @Override // cc.b
        /* renamed from: g, reason: merged with bridge method [inline-methods] */
        public List<r> d(Name name) {
            List<r> j10;
            za.k.e(name, "name");
            j10 = kotlin.collections.r.j();
            return j10;
        }
    }

    Set<Name> a();

    fc.n b(Name name);

    w c(Name name);

    Collection<r> d(Name name);

    Set<Name> e();

    Set<Name> f();
}
