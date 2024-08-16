package k0;

import l0.AuthTypeEnum;
import l0.Authentication;

/* compiled from: AuthenticationFactory.java */
/* renamed from: k0.a, reason: use source file name */
/* loaded from: classes.dex */
public class AuthenticationFactory {

    /* compiled from: AuthenticationFactory.java */
    /* renamed from: k0.a$a */
    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f13999a;

        static {
            int[] iArr = new int[AuthTypeEnum.values().length];
            f13999a = iArr;
            try {
                iArr[AuthTypeEnum.DEVICE_CERT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f13999a[AuthTypeEnum.PSK.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f13999a[AuthTypeEnum.SIMPLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f13999a[AuthTypeEnum.REGISTER_PUB.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public Authentication a(AuthTypeEnum authTypeEnum) {
        int i10 = a.f13999a[authTypeEnum.ordinal()];
        if (i10 == 1) {
            return new DeviceCertAuth();
        }
        if (i10 == 2) {
            return new GroupKeyAuth();
        }
        if (i10 == 3) {
            return new SimpleAuth();
        }
        if (i10 != 4) {
            return null;
        }
        return new RegisterPubAuth();
    }
}
