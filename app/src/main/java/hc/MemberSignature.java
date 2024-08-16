package hc;

import lc.NameResolver;
import ma.NoWhenBranchMatchedException;
import mc.JvmProtoBuf;
import nc.JvmMemberSignature;
import za.DefaultConstructorMarker;

/* compiled from: MemberSignature.kt */
/* renamed from: hc.u, reason: use source file name */
/* loaded from: classes2.dex */
public final class MemberSignature {

    /* renamed from: b, reason: collision with root package name */
    public static final a f12206b = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final String f12207a;

    /* compiled from: MemberSignature.kt */
    /* renamed from: hc.u$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final MemberSignature a(String str, String str2) {
            za.k.e(str, "name");
            za.k.e(str2, "desc");
            return new MemberSignature(str + '#' + str2, null);
        }

        public final MemberSignature b(JvmMemberSignature jvmMemberSignature) {
            za.k.e(jvmMemberSignature, "signature");
            if (jvmMemberSignature instanceof JvmMemberSignature.b) {
                return d(jvmMemberSignature.c(), jvmMemberSignature.b());
            }
            if (jvmMemberSignature instanceof JvmMemberSignature.a) {
                return a(jvmMemberSignature.c(), jvmMemberSignature.b());
            }
            throw new NoWhenBranchMatchedException();
        }

        public final MemberSignature c(NameResolver nameResolver, JvmProtoBuf.c cVar) {
            za.k.e(nameResolver, "nameResolver");
            za.k.e(cVar, "signature");
            return d(nameResolver.getString(cVar.s()), nameResolver.getString(cVar.r()));
        }

        public final MemberSignature d(String str, String str2) {
            za.k.e(str, "name");
            za.k.e(str2, "desc");
            return new MemberSignature(str + str2, null);
        }

        public final MemberSignature e(MemberSignature memberSignature, int i10) {
            za.k.e(memberSignature, "signature");
            return new MemberSignature(memberSignature.a() + '@' + i10, null);
        }
    }

    private MemberSignature(String str) {
        this.f12207a = str;
    }

    public /* synthetic */ MemberSignature(String str, DefaultConstructorMarker defaultConstructorMarker) {
        this(str);
    }

    public final String a() {
        return this.f12207a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof MemberSignature) && za.k.a(this.f12207a, ((MemberSignature) obj).f12207a);
    }

    public int hashCode() {
        return this.f12207a.hashCode();
    }

    public String toString() {
        return "MemberSignature(signature=" + this.f12207a + ')';
    }
}
