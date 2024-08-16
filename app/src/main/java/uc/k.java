package uc;

import id.ErrorType;
import id.ErrorTypeKind;
import id.ErrorUtils;
import ma.Unit;
import pb.ModuleDescriptor;
import za.DefaultConstructorMarker;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public abstract class k extends g<Unit> {

    /* renamed from: b, reason: collision with root package name */
    public static final a f18996b = new a(null);

    /* compiled from: constantValues.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final k a(String str) {
            za.k.e(str, "message");
            return new b(str);
        }
    }

    /* compiled from: constantValues.kt */
    /* loaded from: classes2.dex */
    public static final class b extends k {

        /* renamed from: c, reason: collision with root package name */
        private final String f18997c;

        public b(String str) {
            za.k.e(str, "message");
            this.f18997c = str;
        }

        @Override // uc.g
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public ErrorType a(ModuleDescriptor moduleDescriptor) {
            za.k.e(moduleDescriptor, "module");
            return ErrorUtils.d(ErrorTypeKind.f12806n0, this.f18997c);
        }

        @Override // uc.g
        public String toString() {
            return this.f18997c;
        }
    }

    public k() {
        super(Unit.f15173a);
    }

    @Override // uc.g
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public Unit b() {
        throw new UnsupportedOperationException();
    }
}
