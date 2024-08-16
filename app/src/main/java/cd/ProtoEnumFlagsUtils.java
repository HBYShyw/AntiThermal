package cd;

import pb.CallableMemberDescriptor;
import pb.DescriptorVisibilities;

/* compiled from: ProtoEnumFlagsUtils.kt */
/* renamed from: cd.b0, reason: use source file name */
/* loaded from: classes2.dex */
public final class ProtoEnumFlagsUtils {

    /* compiled from: ProtoEnumFlagsUtils.kt */
    /* renamed from: cd.b0$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f5202a;

        /* renamed from: b, reason: collision with root package name */
        public static final /* synthetic */ int[] f5203b;

        /* renamed from: c, reason: collision with root package name */
        public static final /* synthetic */ int[] f5204c;

        static {
            int[] iArr = new int[jc.j.values().length];
            try {
                iArr[jc.j.DECLARATION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[jc.j.FAKE_OVERRIDE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[jc.j.DELEGATION.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[jc.j.SYNTHESIZED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            f5202a = iArr;
            int[] iArr2 = new int[CallableMemberDescriptor.a.values().length];
            try {
                iArr2[CallableMemberDescriptor.a.DECLARATION.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr2[CallableMemberDescriptor.a.FAKE_OVERRIDE.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr2[CallableMemberDescriptor.a.DELEGATION.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                iArr2[CallableMemberDescriptor.a.SYNTHESIZED.ordinal()] = 4;
            } catch (NoSuchFieldError unused8) {
            }
            f5203b = iArr2;
            int[] iArr3 = new int[jc.x.values().length];
            try {
                iArr3[jc.x.INTERNAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                iArr3[jc.x.PRIVATE.ordinal()] = 2;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                iArr3[jc.x.PRIVATE_TO_THIS.ordinal()] = 3;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                iArr3[jc.x.PROTECTED.ordinal()] = 4;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                iArr3[jc.x.PUBLIC.ordinal()] = 5;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                iArr3[jc.x.LOCAL.ordinal()] = 6;
            } catch (NoSuchFieldError unused14) {
            }
            f5204c = iArr3;
        }
    }

    public static final pb.u a(ProtoEnumFlags protoEnumFlags, jc.x xVar) {
        za.k.e(protoEnumFlags, "<this>");
        switch (xVar == null ? -1 : a.f5204c[xVar.ordinal()]) {
            case 1:
                pb.u uVar = DescriptorVisibilities.f16732d;
                za.k.d(uVar, "INTERNAL");
                return uVar;
            case 2:
                pb.u uVar2 = DescriptorVisibilities.f16729a;
                za.k.d(uVar2, "PRIVATE");
                return uVar2;
            case 3:
                pb.u uVar3 = DescriptorVisibilities.f16730b;
                za.k.d(uVar3, "PRIVATE_TO_THIS");
                return uVar3;
            case 4:
                pb.u uVar4 = DescriptorVisibilities.f16731c;
                za.k.d(uVar4, "PROTECTED");
                return uVar4;
            case 5:
                pb.u uVar5 = DescriptorVisibilities.f16733e;
                za.k.d(uVar5, "PUBLIC");
                return uVar5;
            case 6:
                pb.u uVar6 = DescriptorVisibilities.f16734f;
                za.k.d(uVar6, "LOCAL");
                return uVar6;
            default:
                pb.u uVar7 = DescriptorVisibilities.f16729a;
                za.k.d(uVar7, "PRIVATE");
                return uVar7;
        }
    }

    public static final CallableMemberDescriptor.a b(ProtoEnumFlags protoEnumFlags, jc.j jVar) {
        za.k.e(protoEnumFlags, "<this>");
        int i10 = jVar == null ? -1 : a.f5202a[jVar.ordinal()];
        if (i10 == 1) {
            return CallableMemberDescriptor.a.DECLARATION;
        }
        if (i10 == 2) {
            return CallableMemberDescriptor.a.FAKE_OVERRIDE;
        }
        if (i10 == 3) {
            return CallableMemberDescriptor.a.DELEGATION;
        }
        if (i10 != 4) {
            return CallableMemberDescriptor.a.DECLARATION;
        }
        return CallableMemberDescriptor.a.SYNTHESIZED;
    }
}
