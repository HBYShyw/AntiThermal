package hc;

import cd.KotlinMetadataFinder;
import oc.ClassId;
import za.DefaultConstructorMarker;

/* compiled from: KotlinClassFinder.kt */
/* loaded from: classes2.dex */
public interface p extends KotlinMetadataFinder {

    /* compiled from: KotlinClassFinder.kt */
    /* loaded from: classes2.dex */
    public static abstract class a {

        /* compiled from: KotlinClassFinder.kt */
        /* renamed from: hc.p$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0047a extends a {

            /* renamed from: a, reason: collision with root package name */
            private final byte[] f12198a;

            public final byte[] b() {
                return this.f12198a;
            }
        }

        /* compiled from: KotlinClassFinder.kt */
        /* loaded from: classes2.dex */
        public static final class b extends a {

            /* renamed from: a, reason: collision with root package name */
            private final KotlinJvmBinaryClass f12199a;

            /* renamed from: b, reason: collision with root package name */
            private final byte[] f12200b;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public b(KotlinJvmBinaryClass kotlinJvmBinaryClass, byte[] bArr) {
                super(null);
                za.k.e(kotlinJvmBinaryClass, "kotlinJvmBinaryClass");
                this.f12199a = kotlinJvmBinaryClass;
                this.f12200b = bArr;
            }

            public final KotlinJvmBinaryClass b() {
                return this.f12199a;
            }

            public /* synthetic */ b(KotlinJvmBinaryClass kotlinJvmBinaryClass, byte[] bArr, int i10, DefaultConstructorMarker defaultConstructorMarker) {
                this(kotlinJvmBinaryClass, (i10 & 2) != 0 ? null : bArr);
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final KotlinJvmBinaryClass a() {
            b bVar = this instanceof b ? (b) this : null;
            if (bVar != null) {
                return bVar.b();
            }
            return null;
        }
    }

    a b(ClassId classId);

    a c(fc.g gVar);
}
