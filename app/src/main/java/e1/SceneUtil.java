package e1;

import a1.CryptoKeyStore;
import android.content.Context;
import android.content.SharedPreferences;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import i0.EncryptException;
import j0.AesUtil;
import j0.HashUtil;
import java.nio.charset.StandardCharsets;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.SecretKey;
import m0.CryptoCore;
import o0.BizCertMemoryDataSource;
import org.json.JSONException;
import org.json.JSONObject;
import q0.EciesSceneData;
import q0.RsaSceneData;
import s0.BizPublicKeyConfig;
import s0.CipherContainer;
import s0.CryptoParameters;
import s0.NegotiationAlgorithmEnum;
import s0.SceneConfig;
import s0.n;
import t0.BizDataNotFoundException;
import t0.InvalidArgumentException;
import t0.SceneNotFoundException;
import x0.LocalBizKeyPairs;
import x0.UpgradeCertResponse;
import y0.NoiseUtil;
import z0.NoiseSceneData;

/* compiled from: SceneUtil.java */
/* renamed from: e1.j, reason: use source file name */
/* loaded from: classes.dex */
public class SceneUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final AtomicLong f10961a = new AtomicLong();

    /* renamed from: b, reason: collision with root package name */
    private static final Pattern f10962b = Pattern.compile("([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}");

    private static boolean a(JSONObject jSONObject, String str, SceneConfig sceneConfig, BizCertMemoryDataSource bizCertMemoryDataSource, boolean z10) {
        String string = jSONObject.getString("negotiation_alg");
        if (sceneConfig.c() != null && string.equals(sceneConfig.c().name())) {
            if (string.equals(NegotiationAlgorithmEnum.RSA.name()) || string.equals(NegotiationAlgorithmEnum.EC.name()) || NoiseUtil.n(NegotiationAlgorithmEnum.a(string))) {
                UpgradeCertResponse upgradeCertResponse = null;
                if (!z10) {
                    try {
                        upgradeCertResponse = bizCertMemoryDataSource.f(str);
                    } catch (BizDataNotFoundException unused) {
                    }
                }
                if (upgradeCertResponse != null && upgradeCertResponse.d() != null) {
                    try {
                        if (!jSONObject.optString("cert_encrypt_sha256").equals(Base64Utils.b(HashUtil.c(upgradeCertResponse.d().getEncoded())))) {
                            i.a("SceneUtil", "compareCertMemoryData the biz certificate has been changed, discard this record.");
                            return false;
                        }
                    } catch (NoSuchAlgorithmException | CertificateEncodingException e10) {
                        i.d("SceneUtil", "compareCertMemoryData getCert4Encrypt fail. " + e10);
                        return false;
                    }
                } else {
                    BizPublicKeyConfig b10 = bizCertMemoryDataSource.b(str);
                    if (b10 != null && b10.a() != null) {
                        if (!jSONObject.optString("cert_key_encrypt").equals(b10.a())) {
                            i.a("SceneUtil", "compareCertMemoryData the biz public key has been changed, discard this record.");
                            return false;
                        }
                    } else {
                        i.a("SceneUtil", "compareCertMemoryData the biz public key is missing, discard this record.");
                        return false;
                    }
                }
            }
            if (NoiseUtil.o(NegotiationAlgorithmEnum.a(string))) {
                LocalBizKeyPairs d10 = bizCertMemoryDataSource.d(str);
                if (d10 != null && d10.d() != null) {
                    if (!jSONObject.getString("local_app_key_encrypt").equals(Base64Utils.b(d10.d().getPublic().getEncoded()))) {
                        i.a("SceneUtil", "compareCertMemoryData the application key has been changed, discard this record.");
                        return false;
                    }
                } else {
                    i.a("SceneUtil", "compareCertMemoryData the application key is missing, discard this record.");
                    return false;
                }
            }
            return true;
        }
        i.a("SceneUtil", "compareCertMemoryData negotiation algorithm change, discard this record.");
        return false;
    }

    public static Map<String, SceneConfig> b(List<SceneConfig> list) {
        HashMap hashMap = new HashMap();
        if (list == null) {
            return hashMap;
        }
        for (SceneConfig sceneConfig : list) {
            if (sceneConfig != null) {
                hashMap.put(sceneConfig.d(), sceneConfig);
            }
        }
        return hashMap;
    }

    public static Map<String, UpgradeCertResponse> c(Set<String> set) {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        if (set != null && !set.isEmpty()) {
            for (String str : set) {
                if (str != null) {
                    concurrentHashMap.put(str, new UpgradeCertResponse());
                }
            }
        }
        return concurrentHashMap;
    }

    private static String d(String str, SecretKey secretKey) {
        CipherContainer c10 = CipherUtil.c(str);
        return new String(AesUtil.a(new CryptoParameters.c().j(CryptoParameters.b.f17973e).k(c10.a()).m(secretKey).l(c10.b()).h()), StandardCharsets.UTF_8);
    }

    private static String e(String str, SecretKey secretKey) {
        return CipherUtil.d(AesUtil.b(new CryptoParameters.c().j(CryptoParameters.b.f17973e).m(secretKey).k(str.getBytes(StandardCharsets.UTF_8)).h()));
    }

    public static boolean f(JSONObject jSONObject, String str, NegotiationAlgorithmEnum negotiationAlgorithmEnum, BizCertMemoryDataSource bizCertMemoryDataSource, boolean z10) {
        if (negotiationAlgorithmEnum == NegotiationAlgorithmEnum.NOISE_NN) {
            i.a("SceneUtil", "importCertMemoryData long-term reuse of keys is not supported in noise nn");
            return false;
        }
        jSONObject.put("negotiation_alg", negotiationAlgorithmEnum.name());
        if (negotiationAlgorithmEnum == NegotiationAlgorithmEnum.RSA || negotiationAlgorithmEnum == NegotiationAlgorithmEnum.EC || NoiseUtil.n(negotiationAlgorithmEnum)) {
            UpgradeCertResponse upgradeCertResponse = null;
            if (!z10) {
                try {
                    upgradeCertResponse = bizCertMemoryDataSource.f(str);
                } catch (BizDataNotFoundException unused) {
                }
            }
            if (upgradeCertResponse != null && upgradeCertResponse.d() != null) {
                try {
                    jSONObject.put("cert_encrypt_sha256", Base64Utils.b(HashUtil.c(upgradeCertResponse.d().getEncoded())));
                } catch (NoSuchAlgorithmException | CertificateEncodingException e10) {
                    i.d("SceneUtil", "importCertMemoryData getCert4Encrypt fail " + e10);
                    return false;
                }
            } else {
                BizPublicKeyConfig b10 = bizCertMemoryDataSource.b(str);
                if (b10 != null && b10.a() != null) {
                    jSONObject.put("cert_key_encrypt", b10.a());
                } else {
                    i.d("SceneUtil", "importCertMemoryData biz public key lost(" + str + ")");
                    return false;
                }
            }
        }
        if (NoiseUtil.o(negotiationAlgorithmEnum)) {
            LocalBizKeyPairs d10 = bizCertMemoryDataSource.d(str);
            if (d10 != null && d10.d() != null) {
                jSONObject.put("local_app_key_encrypt", Base64Utils.b(d10.d().getPublic().getEncoded()));
            } else {
                i.d("SceneUtil", "importCertMemoryData localBizKeyPairs lost(" + str + ")");
                return false;
            }
        }
        return true;
    }

    public static boolean g(JSONObject jSONObject, n nVar, String str, String str2) {
        jSONObject.put("sha256_host", HashUtil.b(str));
        if (nVar instanceof RsaSceneData) {
            jSONObject.put("type", "rsa");
        } else if (nVar instanceof EciesSceneData) {
            jSONObject.put("type", "ec");
        } else if (nVar instanceof NoiseSceneData) {
            jSONObject.put("type", "noise");
            jSONObject.put("device_id", str2);
        } else {
            i.d("SceneUtil", "importSceneData unexpected sceneData type " + nVar.getClass().getName());
            return false;
        }
        jSONObject.put("scene_data", nVar.a());
        jSONObject.put("expired_time", nVar.d());
        return true;
    }

    public static Set<String> h(String[] strArr, Map<String, BizPublicKeyConfig> map, BizCertMemoryDataSource bizCertMemoryDataSource) {
        HashSet hashSet = new HashSet();
        if (strArr != null) {
            for (String str : strArr) {
                if (str != null) {
                    hashSet.add(str);
                }
            }
        }
        if (map != null) {
            for (String str2 : map.keySet()) {
                bizCertMemoryDataSource.h(str2, map.get(str2));
            }
        }
        return hashSet;
    }

    public static Set<String> i(String[] strArr, Set<String> set) {
        HashSet hashSet = new HashSet();
        if (strArr != null) {
            for (String str : strArr) {
                if (str != null && set.contains(str)) {
                    hashSet.add(str);
                } else {
                    i.a("SceneUtil", "initNeedRegisterPubKeyBizSet please set the relevant biz hostname.");
                }
            }
        }
        return hashSet;
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x011d A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Map<String, n> j(CryptoCore cryptoCore, Set<String> set) {
        char c10;
        SceneConfig s7;
        String string;
        JSONObject jSONObject;
        n m10;
        if (Thread.currentThread().isInterrupted()) {
            i.a("SceneUtil", "loadLocalSceneDataInSP current thread interrupted.");
            return null;
        }
        Context q10 = cryptoCore.q();
        if (set.isEmpty()) {
            FileUtil.b(q10, "pki_sdk_crypto_scene_data_sp");
            return null;
        }
        try {
            SecretKey e10 = CryptoKeyStore.e(q10, "pki_sdk_scene_data_sp_alias", "pki_sdk_crypto_scene_data_sp");
            char c11 = 0;
            SharedPreferences sharedPreferences = q10.getSharedPreferences("pki_sdk_crypto_scene_data_sp", 0);
            Map<String, ?> all = sharedPreferences.getAll();
            if (all.isEmpty()) {
                return null;
            }
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            for (String str : all.keySet()) {
                String[] r10 = r(str);
                if (r10 == null) {
                    i.a("SceneUtil", "loadLocalSceneDataInSP invalid biz, discard this record.");
                } else {
                    String str2 = r10[c11];
                    String str3 = r10[1];
                    i.a("SceneUtil", "loadLocalSceneDataInSP load sceneData(" + str2 + ", " + str3 + ").");
                    if (!set.contains(str2)) {
                        i.a("SceneUtil", "loadLocalSceneDataInSP invalid biz, discard this record.");
                    } else {
                        try {
                            s7 = cryptoCore.s(str3);
                            string = sharedPreferences.getString(str, "");
                        } catch (SceneNotFoundException unused) {
                            c10 = c11;
                            i.a("SceneUtil", "loadLocalSceneDataInSP invalid scene, discard this record.");
                        }
                        if (string.equals("")) {
                            i.d("SceneUtil", "loadLocalSceneDataInSP corrupted data, discard this record.");
                        } else {
                            try {
                                jSONObject = new JSONObject(d(string, e10));
                            } catch (EncryptException | JSONException | InvalidArgumentException e11) {
                                e = e11;
                                c10 = c11;
                            }
                            if (!s7.e()) {
                                i.a("SceneUtil", "loadLocalSceneDataInSP no long-term reuse requirements, discard this record.");
                            } else {
                                try {
                                } catch (EncryptException | JSONException | InvalidArgumentException e12) {
                                    e = e12;
                                    c10 = 0;
                                }
                                if (a(jSONObject, str2, s7, cryptoCore.p(), cryptoCore.x(str2)) && (m10 = m(jSONObject, s7, cryptoCore.o(str2), cryptoCore.r())) != null) {
                                    String[] strArr = new String[2];
                                    c10 = 0;
                                    try {
                                        strArr[0] = str2;
                                        strArr[1] = str3;
                                        String p10 = p(strArr);
                                        hashMap.put(p10, m10);
                                        hashMap2.put(p10, string);
                                    } catch (EncryptException | JSONException | InvalidArgumentException e13) {
                                        e = e13;
                                        i.d("SceneUtil", "loadLocalSceneDataInSP load a sceneData record error. " + e);
                                        if (Thread.currentThread().isInterrupted()) {
                                        }
                                        c11 = c10;
                                    }
                                    if (Thread.currentThread().isInterrupted()) {
                                        i.a("SceneUtil", "loadLocalSceneDataInSP current thread interrupted.");
                                        return null;
                                    }
                                    c11 = c10;
                                }
                                c11 = 0;
                            }
                        }
                    }
                }
            }
            if (hashMap2.size() != sharedPreferences.getAll().size()) {
                s(sharedPreferences, hashMap2);
            }
            return hashMap;
        } catch (KeyStoreException e14) {
            i.b("SceneUtil", "loadLocalSceneDataInSP get secret key error. " + e14);
            FileUtil.b(q10, "pki_sdk_crypto_scene_data_sp");
            return null;
        }
    }

    public static Map<String, String> k(Map<String, String> map) {
        HashMap hashMap = new HashMap();
        if (map == null) {
            return hashMap;
        }
        map.remove(null);
        for (String str : map.keySet()) {
            String str2 = map.get(str);
            String l10 = l(str2);
            if (l10 != null) {
                hashMap.put(str, l10);
            } else {
                i.a("SceneUtil", "organizeBizMap domain name is not compliant, biz = " + str + ", domain name = " + str2);
            }
        }
        return hashMap;
    }

    public static String l(String str) {
        if (str == null) {
            return null;
        }
        Matcher matcher = f10962b.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private static n m(JSONObject jSONObject, SceneConfig sceneConfig, String str, String str2) {
        n noiseSceneData;
        String optString = jSONObject.optString("sha256_host");
        if ((optString.equals("") && str != null) || ((!optString.equals("") && str == null) || (!optString.equals("") && !optString.equals(HashUtil.b(str))))) {
            i.a("SceneUtil", "restoreSceneData host name has changed, discard this record.");
            return null;
        }
        long j10 = jSONObject.getLong("expired_time");
        long a10 = DateUtil.a();
        if (a10 > j10) {
            i.a("SceneUtil", "restoreSceneData the valid time has expired, discard this record.");
            return null;
        }
        String string = jSONObject.getString("type");
        if (string.equals("rsa")) {
            noiseSceneData = new RsaSceneData();
        } else if (string.equals("ec")) {
            noiseSceneData = new EciesSceneData();
        } else if (string.equals("noise")) {
            if (!jSONObject.getString("device_id").equals(str2)) {
                i.a("SceneUtil", "restoreSceneData unique id has changed, discard this record.");
                return null;
            }
            noiseSceneData = new NoiseSceneData();
        } else {
            throw new IllegalStateException("Unexpected type: " + string);
        }
        if (!noiseSceneData.h(jSONObject.getJSONObject("scene_data"))) {
            i.a("SceneUtil", "restoreSceneData the sceneData restoration failed, discard this record.");
            return null;
        }
        if (noiseSceneData.b() != sceneConfig.a()) {
            i.a("SceneUtil", "restoreSceneData symmetric algorithm change, discard this record.");
            return null;
        }
        noiseSceneData.j(j10 - a10);
        noiseSceneData.k(j10);
        return noiseSceneData;
    }

    public static void n(Context context, String str, String str2, String str3) {
        try {
            context.getSharedPreferences("pki_sdk_crypto_scene_data_sp", 0).edit().putString(p(str, str2), e(str3, CryptoKeyStore.e(context, "pki_sdk_scene_data_sp_alias", "pki_sdk_crypto_scene_data_sp"))).apply();
            i.a("SceneUtil", "saveSceneDataMemoryData sceneData(" + str + ", " + str2 + ") save success");
        } catch (EncryptException | JSONException | InvalidArgumentException e10) {
            i.d("SceneUtil", "saveSceneDataMemoryData sceneData(" + str + ", " + str2 + ") save error." + e10);
        }
    }

    public static void o(SceneConfig sceneConfig, n nVar) {
        nVar.l(sceneConfig.d());
        nVar.i(sceneConfig.a());
        nVar.j(sceneConfig.b() * 1000);
        long a10 = DateUtil.a() + nVar.c();
        if (sceneConfig.f()) {
            nVar.m((10000 * a10) + new SecureRandom().nextInt(DataLinkConstants.RUS_UPDATE));
        } else {
            nVar.m(f10961a.getAndIncrement());
        }
        nVar.k(a10);
    }

    public static String p(String... strArr) {
        return q(":s:", strArr);
    }

    private static String q(String str, String... strArr) {
        StringBuilder sb2 = new StringBuilder();
        for (String str2 : strArr) {
            sb2.append(str2);
            sb2.append(str);
        }
        sb2.delete(sb2.length() - str.length(), sb2.length());
        return new String(sb2);
    }

    public static String[] r(String str) {
        String[] split = str.split(":s:");
        if (split.length == 2) {
            return split;
        }
        return null;
    }

    private static void s(SharedPreferences sharedPreferences, Map<String, String> map) {
        if (map.isEmpty()) {
            sharedPreferences.edit().clear().apply();
            return;
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        for (String str : map.keySet()) {
            edit.putString(str, map.get(str));
        }
        edit.apply();
    }
}
