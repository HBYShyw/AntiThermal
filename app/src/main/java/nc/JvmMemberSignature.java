package nc;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: JvmMemberSignature.kt */
/* renamed from: nc.d, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class JvmMemberSignature {

    /* compiled from: JvmMemberSignature.kt */
    /* renamed from: nc.d$a */
    /* loaded from: classes2.dex */
    public static final class a extends JvmMemberSignature {

        /* renamed from: a, reason: collision with root package name */
        private final String f15989a;

        /* renamed from: b, reason: collision with root package name */
        private final String f15990b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(String str, String str2) {
            super(null);
            k.e(str, "name");
            k.e(str2, "desc");
            this.f15989a = str;
            this.f15990b = str2;
        }

        @Override // nc.JvmMemberSignature
        public String a() {
            return c() + COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR + b();
        }

        @Override // nc.JvmMemberSignature
        public String b() {
            return this.f15990b;
        }

        @Override // nc.JvmMemberSignature
        public String c() {
            return this.f15989a;
        }

        public final String d() {
            return c();
        }

        public final String e() {
            return b();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return k.a(c(), aVar.c()) && k.a(b(), aVar.b());
        }

        public int hashCode() {
            return (c().hashCode() * 31) + b().hashCode();
        }
    }

    /* compiled from: JvmMemberSignature.kt */
    /* renamed from: nc.d$b */
    /* loaded from: classes2.dex */
    public static final class b extends JvmMemberSignature {

        /* renamed from: a, reason: collision with root package name */
        private final String f15991a;

        /* renamed from: b, reason: collision with root package name */
        private final String f15992b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(String str, String str2) {
            super(null);
            k.e(str, "name");
            k.e(str2, "desc");
            this.f15991a = str;
            this.f15992b = str2;
        }

        @Override // nc.JvmMemberSignature
        public String a() {
            return c() + b();
        }

        @Override // nc.JvmMemberSignature
        public String b() {
            return this.f15992b;
        }

        @Override // nc.JvmMemberSignature
        public String c() {
            return this.f15991a;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof b)) {
                return false;
            }
            b bVar = (b) obj;
            return k.a(c(), bVar.c()) && k.a(b(), bVar.b());
        }

        public int hashCode() {
            return (c().hashCode() * 31) + b().hashCode();
        }
    }

    private JvmMemberSignature() {
    }

    public /* synthetic */ JvmMemberSignature(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract String a();

    public abstract String b();

    public abstract String c();

    public final String toString() {
        return a();
    }
}
