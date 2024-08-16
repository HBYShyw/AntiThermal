package j0;

import i0.EncryptException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import s0.CipherContainer;
import s0.CryptoParameters;
import t0.InvalidAlgorithmException;
import t0.InvalidArgumentException;

/* compiled from: AesUtil.java */
/* renamed from: j0.a, reason: use source file name */
/* loaded from: classes.dex */
public class AesUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final CryptoParameters.b f12929a = CryptoParameters.b.f17974f;

    /* compiled from: AesUtil.java */
    /* renamed from: j0.a$a */
    /* loaded from: classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f12930a;

        static {
            int[] iArr = new int[CryptoParameters.b.values().length];
            f12930a = iArr;
            try {
                iArr[CryptoParameters.b.f17974f.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f12930a[CryptoParameters.b.f17975g.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f12930a[CryptoParameters.b.f17973e.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static byte[] a(CryptoParameters cryptoParameters) {
        try {
            if (cryptoParameters != null) {
                if (cryptoParameters.g() != null) {
                    if (cryptoParameters.h() instanceof SecretKey) {
                        SecretKey secretKey = (SecretKey) cryptoParameters.h();
                        CryptoParameters.b bVar = f12929a;
                        if (cryptoParameters.d() != null) {
                            bVar = cryptoParameters.d();
                        }
                        Cipher cipher = Cipher.getInstance(bVar.b());
                        int i10 = a.f12930a[bVar.ordinal()];
                        if (i10 == 1 || i10 == 2) {
                            cipher.init(2, secretKey, new IvParameterSpec(cryptoParameters.g()));
                        } else if (i10 == 3) {
                            cipher.init(2, secretKey, new GCMParameterSpec(cryptoParameters.e(), cryptoParameters.g()));
                            if (cryptoParameters.c() != null) {
                                cipher.updateAAD(cryptoParameters.c());
                            }
                        } else {
                            throw new InvalidAlgorithmException(bVar.b());
                        }
                        return cipher.doFinal(cryptoParameters.f());
                    }
                    throw new InvalidKeyException("Only supports 'SecretKey' type, not '" + cryptoParameters.h().getClass().getName() + "'. Please reset a SecretKey.");
                }
                throw new InvalidArgumentException("iv is null");
            }
            throw new InvalidArgumentException("cryptoParameters is null");
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | InvalidAlgorithmException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }

    public static CipherContainer b(CryptoParameters cryptoParameters) {
        try {
            if (cryptoParameters != null) {
                if (cryptoParameters.h() instanceof SecretKey) {
                    SecretKey secretKey = (SecretKey) cryptoParameters.h();
                    CryptoParameters.b bVar = f12929a;
                    if (cryptoParameters.d() != null) {
                        bVar = cryptoParameters.d();
                    }
                    CryptoParameters.b bVar2 = CryptoParameters.b.f17973e;
                    if (bVar == bVar2) {
                        if (cryptoParameters.e() != 128 && cryptoParameters.g() == null) {
                            throw new InvalidArgumentException("authentication tag bit-length must be set at the same time as IV, currently iv is not set");
                        }
                        if (cryptoParameters.e() <= 0 || cryptoParameters.e() % 8 != 0) {
                            throw new InvalidArgumentException("authentication tag bit-length must be a multiple of 8 and not zero or negative");
                        }
                    }
                    Cipher cipher = Cipher.getInstance(bVar.b());
                    if (cryptoParameters.g() != null) {
                        int i10 = a.f12930a[bVar.ordinal()];
                        if (i10 == 1 || i10 == 2) {
                            cipher.init(1, secretKey, new IvParameterSpec(cryptoParameters.g()));
                        } else if (i10 == 3) {
                            cipher.init(1, secretKey, new GCMParameterSpec(cryptoParameters.e(), cryptoParameters.g()));
                        } else {
                            throw new InvalidAlgorithmException(bVar.b());
                        }
                    } else {
                        cipher.init(1, secretKey);
                    }
                    if (bVar == bVar2 && cryptoParameters.c() != null) {
                        cipher.updateAAD(cryptoParameters.c());
                    }
                    return new CipherContainer(cipher.doFinal(cryptoParameters.f()), cipher.getIV());
                }
                throw new InvalidKeyException("Only supports 'SecretKey' type, not '" + cryptoParameters.h().getClass().getName() + "'. Please reset a SecretKey.");
            }
            throw new InvalidArgumentException("cryptoParameters is null");
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | InvalidAlgorithmException | InvalidArgumentException e10) {
            throw new EncryptException(e10);
        }
    }
}
