package y0;

import a1.CryptoKeyStore;
import b1.EcKeyGenParameterSpec;
import com.oplus.deepthinker.sdk.app.awareness.fence.impl.SpecifiedLocationFence;
import com.oplus.thermalcontrol.ThermalControlConfig;
import e1.Base64Utils;
import e1.DateUtil;
import e1.HttpUtil;
import e1.KeyUtil;
import e1.SceneUtil;
import e1.ThreadUtil;
import e1.i;
import i0.EncryptException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import m0.CryptoCore;
import org.json.JSONException;
import org.json.JSONObject;
import s0.BizPublicKeyConfig;
import s0.NegotiationAlgorithmEnum;
import s0.NegotiationParam;
import s0.SceneConfig;
import s0.n;
import t0.BizDataNotFoundException;
import t0.InvalidAlgorithmException;
import t0.InvalidArgumentException;
import x0.LocalBizKeyPairs;
import x0.UpgradeCertResponse;
import z0.CipherStatePair;
import z0.NoiseCipherEnum;
import z0.NoiseDHEnum;
import z0.NoiseHandshakeEnum;
import z0.NoiseHashEnum;
import z0.NoiseNegotiationParam;
import z0.NoiseSceneData;
import z0.NonceModeEnum;
import zd.ResponseBody;
import zd.b0;

/* compiled from: NoiseUtil.java */
/* renamed from: y0.f, reason: use source file name */
/* loaded from: classes.dex */
public class NoiseUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final Object f19790a = new Object();

    /* renamed from: b, reason: collision with root package name */
    private static final Object f19791b = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: NoiseUtil.java */
    /* renamed from: y0.f$a */
    /* loaded from: classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f19792a;

        static {
            int[] iArr = new int[NegotiationAlgorithmEnum.values().length];
            f19792a = iArr;
            try {
                iArr[NegotiationAlgorithmEnum.NOISE_NK.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f19792a[NegotiationAlgorithmEnum.NOISE_KK.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f19792a[NegotiationAlgorithmEnum.NOISE_IK.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f19792a[NegotiationAlgorithmEnum.NOISE_IX.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f19792a[NegotiationAlgorithmEnum.NOISE_NN.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] b(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = bArr.length > bArr2.length ? bArr : bArr2;
        if (bArr.length > bArr2.length) {
            bArr = bArr2;
        }
        byte[] bArr4 = new byte[bArr3.length];
        System.arraycopy(bArr3, 0, bArr4, 0, bArr3.length);
        for (int i10 = 0; i10 < bArr.length; i10++) {
            bArr4[i10] = (byte) (bArr3[i10] ^ bArr[i10]);
        }
        return bArr4;
    }

    private static void c(KeyPair keyPair) {
        if (keyPair != null) {
            if (keyPair.getPublic().getAlgorithm().equals("EC")) {
                return;
            }
            throw new InvalidKeyException("Current scene only supports EC key, not " + keyPair.getPublic().getAlgorithm());
        }
        throw new InvalidKeyException("Missing application key");
    }

    private static void d(PublicKey publicKey) {
        if (publicKey != null) {
            if (publicKey.getAlgorithm().equals("EC")) {
                return;
            }
            throw new InvalidKeyException("Current scene only supports EC key, not " + publicKey.getAlgorithm() + ". Please specify the correct biz or biz public key");
        }
        throw new InvalidKeyException("Missing biz public key");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00ca  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0186  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0067  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static n e(CryptoCore cryptoCore, String str, String str2, NegotiationParam negotiationParam) {
        PublicKey publicKey;
        String r10 = cryptoCore.r();
        if (r10 != null) {
            SceneConfig s7 = cryptoCore.s(str2);
            long j10 = 0;
            KeyPair keyPair = null;
            if (n(s7.c())) {
                if (cryptoCore.x(str)) {
                    publicKey = null;
                } else {
                    try {
                        if (cryptoCore.j(str)) {
                            UpgradeCertResponse h10 = cryptoCore.p().f(str).h();
                            publicKey = h10.d().getPublicKey();
                            try {
                                j10 = h10.f();
                            } catch (BizDataNotFoundException unused) {
                                i.a("NoiseUtil", "createAndSaveSceneData no valid domain name set");
                                if (publicKey == null) {
                                }
                                if (publicKey == null) {
                                }
                                if (!o(s7.c())) {
                                }
                                NoiseSceneData h11 = h(s7);
                                HandshakeState f10 = f(s7, keyPair, publicKey, negotiationParam);
                                String name = f10.f().name();
                                JSONObject jSONObject = new JSONObject();
                                jSONObject.put("deviceId", r10);
                                jSONObject.put("biz", str);
                                jSONObject.put("certVersion", j10);
                                jSONObject.put("negotiationVersion", h11.f());
                                jSONObject.put(ThermalControlConfig.CONFIG_TYPE_SCENE, str2);
                                jSONObject.put(SpecifiedLocationFence.BUNDLE_KEY_PATTERN, name);
                                if (!s7.f()) {
                                }
                                return h11;
                            }
                        } else {
                            publicKey = null;
                        }
                    } catch (BizDataNotFoundException unused2) {
                        publicKey = null;
                    }
                    if (publicKey == null) {
                        i.a("NoiseUtil", "createAndSaveSceneData missing " + str + " online certificate");
                    }
                }
                if (publicKey == null) {
                    BizPublicKeyConfig b10 = cryptoCore.p().b(str);
                    if (b10 != null && b10.a() != null) {
                        try {
                            publicKey = KeyUtil.b(Base64Utils.a(b10.a()), "EC");
                            j10 = b10.b();
                        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidArgumentException e10) {
                            i.b("NoiseUtil", "createAndSaveSceneData generatePublic error. " + e10);
                        }
                    }
                    if (publicKey == null) {
                        i.a("NoiseUtil", "createAndSaveSceneData missing " + str + " hardcoded public key");
                    }
                }
            } else {
                publicKey = null;
            }
            if (!o(s7.c())) {
                if (cryptoCore.l(str)) {
                    LocalBizKeyPairs d10 = cryptoCore.p().d(str);
                    if (d10 != null) {
                        keyPair = d10.g().d();
                    }
                } else {
                    throw new InvalidKeyException("Application public key without pre-registration or registration failure");
                }
            } else if (m(s7.c())) {
                String str3 = "pki_sdk_noise_local_key4Encrypt&" + str;
                keyPair = CryptoKeyStore.i(cryptoCore.q(), str3);
                if (keyPair == null) {
                    synchronized (f19791b) {
                        keyPair = CryptoKeyStore.i(cryptoCore.q(), str3);
                        if (keyPair == null) {
                            keyPair = CryptoKeyStore.h(cryptoCore.q(), new EcKeyGenParameterSpec.b(str3, 64).f(new Date(DateUtil.a() + 15724800000L)).e());
                        }
                    }
                }
            }
            NoiseSceneData h112 = h(s7);
            HandshakeState f102 = f(s7, keyPair, publicKey, negotiationParam);
            String name2 = f102.f().name();
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("deviceId", r10);
            jSONObject2.put("biz", str);
            jSONObject2.put("certVersion", j10);
            jSONObject2.put("negotiationVersion", h112.f());
            jSONObject2.put(ThermalControlConfig.CONFIG_TYPE_SCENE, str2);
            jSONObject2.put(SpecifiedLocationFence.BUNDLE_KEY_PATTERN, name2);
            if (!s7.f()) {
                synchronized (f19790a) {
                    n t7 = cryptoCore.t(str, s7.d());
                    if (t7 != null && !t7.g() && (t7 instanceof NoiseSceneData)) {
                        h112 = (NoiseSceneData) t7;
                    }
                    i.a("NoiseUtil", "createAndSaveSceneData send a handshake(" + name2 + ") message to pki server, using server public key version " + j10 + ", biz is " + str);
                    k(f102, jSONObject2, cryptoCore.o(str), h112);
                    cryptoCore.K(str, h112);
                    i.a("NoiseUtil", "createAndSaveSceneData adopt and save to cryptoCore");
                }
            } else {
                i.a("NoiseUtil", "createAndSaveSceneData send a handshake(" + name2 + ") message to pki server, using server public key version " + j10 + ", biz is " + str);
                k(f102, jSONObject2, cryptoCore.o(str), h112);
            }
            return h112;
        }
        throw new InvalidArgumentException("DeviceId is null");
    }

    private static HandshakeState f(SceneConfig sceneConfig, KeyPair keyPair, PublicKey publicKey, NegotiationParam negotiationParam) {
        NoiseHandshakeEnum noiseHandshakeEnum;
        NoiseDHEnum noiseDHEnum = NoiseDHEnum.SECP256R1;
        NoiseHashEnum noiseHashEnum = NoiseHashEnum.SHA256;
        int i10 = a.f19792a[sceneConfig.c().ordinal()];
        if (i10 == 1) {
            noiseHandshakeEnum = NoiseHandshakeEnum.NK;
            d(publicKey);
        } else if (i10 == 2) {
            noiseHandshakeEnum = NoiseHandshakeEnum.KK;
            d(publicKey);
            c(keyPair);
        } else if (i10 == 3) {
            noiseHandshakeEnum = NoiseHandshakeEnum.IK;
            d(publicKey);
            c(keyPair);
        } else if (i10 == 4) {
            noiseHandshakeEnum = NoiseHandshakeEnum.IX;
            c(keyPair);
        } else if (i10 == 5) {
            noiseHandshakeEnum = NoiseHandshakeEnum.NN;
        } else {
            throw new InvalidAlgorithmException(sceneConfig.c().name());
        }
        HandshakeState handshakeState = new HandshakeState(noiseHandshakeEnum, noiseDHEnum, NoiseCipherEnum.AESGCM, noiseHashEnum, NonceModeEnum.DEFAULT, 1);
        handshakeState.t(keyPair);
        handshakeState.s(publicKey);
        if (negotiationParam instanceof NoiseNegotiationParam) {
            NoiseNegotiationParam noiseNegotiationParam = (NoiseNegotiationParam) negotiationParam;
            handshakeState.q(noiseNegotiationParam.a());
            handshakeState.r(noiseNegotiationParam.b());
        }
        handshakeState.j();
        return handshakeState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MessageDigest g(NoiseHashEnum noiseHashEnum) {
        if (noiseHashEnum == NoiseHashEnum.SHA256) {
            return MessageDigest.getInstance("SHA-256");
        }
        throw new NoSuchAlgorithmException("Unknown Noise hash algorithm name: " + noiseHashEnum.name());
    }

    private static NoiseSceneData h(SceneConfig sceneConfig) {
        NoiseSceneData noiseSceneData = new NoiseSceneData();
        SceneUtil.o(sceneConfig, noiseSceneData);
        return noiseSceneData;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String i(NoiseHandshakeEnum noiseHandshakeEnum, NoiseDHEnum noiseDHEnum, NoiseCipherEnum noiseCipherEnum, NoiseHashEnum noiseHashEnum) {
        return "Noise_" + noiseHandshakeEnum.toString() + "_" + noiseDHEnum.a() + "_" + noiseCipherEnum.toString() + "_" + noiseHashEnum.toString();
    }

    private static void j(HandshakeState handshakeState, byte[] bArr, JSONObject jSONObject, String str) {
        c v7 = handshakeState.v(bArr);
        if (v7 == null) {
            i.b("NoiseUtil", "handshake writeMessage fail, message is null");
            return;
        }
        jSONObject.put("buffer", v7.n());
        b0 b0Var = null;
        try {
            try {
            } catch (IOException e10) {
                i.b("NoiseUtil", "handshake execute fail: " + e10.getClass().getName());
                if (0 == 0) {
                    return;
                }
            }
            if (Thread.currentThread().isInterrupted()) {
                i.a("NoiseUtil", "handshake current thread is interrupted before send a handshake");
                return;
            }
            String c10 = HttpUtil.c(str, "/crypto/agreement/noise");
            if (c10 != null) {
                b0Var = HttpUtil.b(c10, jSONObject.toString());
                int code = b0Var.getCode();
                i.a("NoiseUtil", "handshake server response code " + code);
                if (code == 200) {
                    ResponseBody f20511k = b0Var.getF20511k();
                    if (f20511k == null) {
                        i.b("NoiseUtil", "handshake returns null on responses");
                        b0Var.close();
                        return;
                    }
                    JSONObject jSONObject2 = new JSONObject(f20511k.v());
                    if (jSONObject2.getInt("code") == 200) {
                        handshakeState.m(c.a(jSONObject2.getString("data")));
                        if (!handshakeState.i()) {
                            throw new EncryptException("Abnormal handshake status");
                        }
                    } else {
                        i.b("NoiseUtil", "handshake failed to receive handshake message, biz = " + jSONObject.optString("biz") + ", message = " + jSONObject2.optString("message") + ", traceId = " + jSONObject2.optString("traceId"));
                    }
                }
                b0Var.close();
                return;
            }
            throw new InvalidArgumentException("Missing available url");
        } catch (Throwable th) {
            if (0 != 0) {
                b0Var.close();
            }
            throw th;
        }
    }

    private static void k(final HandshakeState handshakeState, final JSONObject jSONObject, final String str, NoiseSceneData noiseSceneData) {
        Future<?> c10 = ThreadUtil.c(new Runnable() { // from class: y0.e
            @Override // java.lang.Runnable
            public final void run() {
                NoiseUtil.p(HandshakeState.this, jSONObject, str);
            }
        });
        try {
            c10.get(15L, TimeUnit.SECONDS);
            if (handshakeState.i()) {
                CipherStatePair d10 = handshakeState.d();
                r(d10, NonceModeEnum.RANDOM_IV);
                noiseSceneData.o(d10);
                return;
            }
            throw new EncryptException("Noise negotiation error");
        } catch (InterruptedException | ExecutionException | TimeoutException e10) {
            c10.cancel(true);
            throw new TimeoutException("noise time out: " + e10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void l(int i10, byte[] bArr, int i11) {
        if (bArr.length - i11 < 4) {
            return;
        }
        int i12 = 0;
        while (i12 < 4) {
            int i13 = i12 + 1;
            bArr[i12 + i11] = (byte) ((i10 >> (32 - (i13 * 8))) & 255);
            i12 = i13;
        }
    }

    private static boolean m(NegotiationAlgorithmEnum negotiationAlgorithmEnum) {
        int i10 = a.f19792a[negotiationAlgorithmEnum.ordinal()];
        return i10 == 2 || i10 == 3 || i10 == 4;
    }

    public static boolean n(NegotiationAlgorithmEnum negotiationAlgorithmEnum) {
        int i10 = a.f19792a[negotiationAlgorithmEnum.ordinal()];
        return i10 == 1 || i10 == 2 || i10 == 3;
    }

    public static boolean o(NegotiationAlgorithmEnum negotiationAlgorithmEnum) {
        return a.f19792a[negotiationAlgorithmEnum.ordinal()] == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void p(HandshakeState handshakeState, JSONObject jSONObject, String str) {
        try {
            j(handshakeState, null, jSONObject, str);
        } catch (EncryptException | JSONException | InvalidArgumentException e10) {
            i.b("NoiseUtil", "handshake0 noise handshake error, biz is " + jSONObject.optString("biz") + ". " + e10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void q(long j10, byte[] bArr, int i10) {
        if (bArr.length - i10 < 8) {
            return;
        }
        for (int i11 = 0; i11 < 8; i11++) {
            bArr[i11 + i10] = (byte) ((j10 >> (64 - (r2 * 8))) & 255);
        }
    }

    static void r(CipherStatePair cipherStatePair, NonceModeEnum nonceModeEnum) {
        cipherStatePair.b().h(nonceModeEnum);
        cipherStatePair.a().h(nonceModeEnum);
    }
}
