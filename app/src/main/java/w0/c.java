package w0;

import a1.CryptoKeyStore;
import android.content.Context;
import android.content.SharedPreferences;
import b1.EcKeyGenParameterSpec;
import e1.Base64Utils;
import e1.CipherUtil;
import e1.DateUtil;
import e1.FileUtil;
import e1.i;
import i0.EncryptException;
import j0.AesUtil;
import j0.CertUtil;
import j0.HashUtil;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.crypto.SecretKey;
import o0.BizCertMemoryDataSource;
import org.json.JSONException;
import org.json.JSONObject;
import s0.CertParameters;
import s0.CipherContainer;
import s0.CryptoParameters;
import t0.BizDataNotFoundException;
import t0.InvalidArgumentException;
import x0.LocalBizKeyPairs;
import x0.UpgradeCertResponse;

/* compiled from: Util.java */
/* loaded from: classes.dex */
public class c {
    private static int a(long j10, int i10) {
        if (j10 == -1) {
            return -1;
        }
        return i10 - ((int) ((DateUtil.a() - j10) / 1000));
    }

    private static String b(String str, SecretKey secretKey) {
        CipherContainer c10 = CipherUtil.c(str);
        return new String(AesUtil.a(new CryptoParameters.c().j(CryptoParameters.b.f17973e).k(c10.a()).m(secretKey).l(c10.b()).h()), StandardCharsets.UTF_8);
    }

    private static JSONObject c(String str, SecretKey secretKey) {
        if (str.equals("")) {
            return null;
        }
        try {
            return new JSONObject(b(str, secretKey));
        } catch (EncryptException | JSONException | InvalidArgumentException e10) {
            i.d("Util", "decryptHashHostToJSONObject decrypt error. " + e10);
            return null;
        }
    }

    private static void d(Set<String> set, Context context) {
        for (String str : set) {
            FileUtil.b(context, "pki_sdk_version_certs_sp_" + str);
            try {
                CryptoKeyStore.g("pki_sdk_version_certs_alias_" + str);
            } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e10) {
                i.d("Util", "deleteBizVersionCertificateSP delete key entry error. " + e10);
            }
        }
    }

    public static void e(Context context, String str, String str2) {
        String u7 = u("pki_sdk_key4Encrypt", str, str2);
        String u10 = u("pki_sdk_key4Sign", str, str2);
        try {
            CryptoKeyStore.f(context, u7);
            CryptoKeyStore.f(context, u10);
        } catch (EncryptException e10) {
            i.d("Util", "deleteLocalBizKeyPairsKeystoreData delete key pair error. " + e10);
        }
    }

    private static String f(UpgradeCertResponse upgradeCertResponse, SecretKey secretKey) {
        if (upgradeCertResponse == null || upgradeCertResponse.g()) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("bizId", upgradeCertResponse.c());
        jSONObject.put("version", upgradeCertResponse.f());
        jSONObject.put("cert4Sign", Base64Utils.b(upgradeCertResponse.e().getEncoded()));
        jSONObject.put("cert4Encrypt", Base64Utils.b(upgradeCertResponse.d().getEncoded()));
        return h(jSONObject.toString(), secretKey);
    }

    public static String g(LocalBizKeyPairs localBizKeyPairs, String str, String str2, Context context) {
        if (localBizKeyPairs == null) {
            return null;
        }
        SecretKey e10 = CryptoKeyStore.e(context, "pki_sdk_all_localKeys_alias_v2", "pki_sdk_register_local_key_sp");
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("bizId", localBizKeyPairs.c());
        jSONObject.put("version", localBizKeyPairs.f());
        jSONObject.put("localKey4Sign", Base64Utils.b(localBizKeyPairs.e().getPublic().getEncoded()));
        jSONObject.put("localKey4Encrypt", Base64Utils.b(localBizKeyPairs.d().getPublic().getEncoded()));
        jSONObject.put("sha256_host", HashUtil.b(str));
        jSONObject.put("deviceId", str2);
        return h(jSONObject.toString(), e10);
    }

    private static String h(String str, SecretKey secretKey) {
        return CipherUtil.d(AesUtil.b(new CryptoParameters.c().j(CryptoParameters.b.f17973e).m(secretKey).k(str.getBytes(StandardCharsets.UTF_8)).h()));
    }

    private static String i(Map<String, String> map, SecretKey secretKey) {
        JSONObject jSONObject = new JSONObject();
        for (String str : map.keySet()) {
            try {
                jSONObject.put(str, HashUtil.b(map.get(str)));
            } catch (JSONException e10) {
                i.d("Util", "encryptHashHost json put error. " + e10);
            }
        }
        return h(jSONObject.toString(), secretKey);
    }

    private static boolean j(Context context, BizCertMemoryDataSource bizCertMemoryDataSource, String str, String str2, SecretKey secretKey) {
        try {
            UpgradeCertResponse r10 = r(b(str2, secretKey), str, -1L, context);
            UpgradeCertResponse f10 = bizCertMemoryDataSource.f(str);
            if (f10 == null || r10 == null) {
                return false;
            }
            f10.b(r10);
            return true;
        } catch (EncryptException | JSONException | BizDataNotFoundException | InvalidArgumentException e10) {
            i.d("Util", "extractBizCert read " + str + " record error. " + e10);
            return false;
        }
    }

    public static LocalBizKeyPairs k(Context context, String str, String str2) {
        String u7 = u("pki_sdk_key4Encrypt", str, str2);
        String u10 = u("pki_sdk_key4Sign", str, str2);
        try {
            KeyPair h10 = CryptoKeyStore.h(context, new EcKeyGenParameterSpec.b(u7, 64).e());
            KeyPair h11 = CryptoKeyStore.h(context, new EcKeyGenParameterSpec.b(u10, 4).e());
            LocalBizKeyPairs localBizKeyPairs = new LocalBizKeyPairs();
            localBizKeyPairs.a(str, h11, h10, str2);
            return localBizKeyPairs;
        } catch (EncryptException e10) {
            i.b("Util", "generateLocalBizKeyPairs generate " + str + " LocalBizKeyPairs failed. " + e10);
            e(context, str, str2);
            return null;
        }
    }

    private static boolean l(JSONObject jSONObject, Map<String, String> map, Set<String> set, Set<String> set2) {
        HashMap hashMap = new HashMap(map);
        Iterator<String> keys = jSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            try {
                String string = jSONObject.getString(next);
                if (hashMap.containsKey(next) && string.equals(HashUtil.b((String) hashMap.get(next)))) {
                    set.add(next);
                } else {
                    set2.add(next);
                }
            } catch (JSONException unused) {
                set2.add(next);
            }
            hashMap.remove(next);
        }
        set2.addAll(hashMap.keySet());
        return set2.isEmpty();
    }

    public static Set<String> m(Context context, BizCertMemoryDataSource bizCertMemoryDataSource, Map<String, String> map) {
        File c10 = FileUtil.c("allbizcerts", context);
        if (c10.exists()) {
            i.a("Util", "loadAllBizCertRecordInSP old BIZ_CERTS_FILE delete: " + c10.delete());
        }
        SecretKey secretKey = null;
        try {
            secretKey = CryptoKeyStore.e(context, "pki_sdk_all_bizCerts_alias_v2", "pki_sdk_biz_certs_sp");
        } catch (KeyStoreException e10) {
            i.d("Util", "loadAllBizCertRecordInSP secretKey get error, unable to read biz certs. " + e10);
        }
        if (secretKey == null) {
            d(map.keySet(), context);
            return new HashSet();
        }
        return n(secretKey, context, bizCertMemoryDataSource, map);
    }

    private static Set<String> n(SecretKey secretKey, Context context, BizCertMemoryDataSource bizCertMemoryDataSource, Map<String, String> map) {
        HashSet hashSet = new HashSet();
        SharedPreferences sharedPreferences = context.getSharedPreferences("pki_sdk_biz_certs_sp", 0);
        String string = sharedPreferences.getString("sha256_host", "");
        JSONObject c10 = c(string, secretKey);
        if (c10 == null) {
            c10 = new JSONObject();
        }
        HashSet<String> hashSet2 = new HashSet();
        HashSet hashSet3 = new HashSet();
        boolean l10 = l(c10, map, hashSet2, hashSet3);
        HashMap hashMap = new HashMap();
        if (!l10) {
            try {
                string = i(map, secretKey);
            } catch (EncryptException | JSONException | InvalidArgumentException e10) {
                i.b("Util", "loadAllBizCertRecordInSP re-encrypt hash host error. " + e10);
                string = "";
            }
            d(hashSet3, context);
        }
        if (!string.equals("")) {
            hashMap.put("sha256_host", string);
        }
        for (String str : hashSet2) {
            String string2 = sharedPreferences.getString(str, "");
            if (!string2.equals("") && j(context, bizCertMemoryDataSource, str, string2, secretKey)) {
                hashMap.put(str, string2);
                hashSet.add(str);
            }
        }
        String string3 = sharedPreferences.getString("modifiedDate", "");
        if (!string3.equals("")) {
            try {
                long parseLong = Long.parseLong(b(string3, secretKey));
                if (parseLong != 0) {
                    bizCertMemoryDataSource.g(parseLong);
                    hashMap.put("modifiedDate", string3);
                }
            } catch (EncryptException | NumberFormatException | JSONException | InvalidArgumentException e11) {
                i.d("Util", "loadAllBizCertRecordInSP load modified date fail. " + e11);
            }
        }
        if (!l10 || hashMap.size() != sharedPreferences.getAll().size()) {
            w(sharedPreferences, hashMap);
        }
        return hashSet;
    }

    private static LocalBizKeyPairs o(String str, Context context, Set<String> set, Map<String, String> map, String str2) {
        JSONObject jSONObject = new JSONObject(str);
        String string = jSONObject.getString("bizId");
        String string2 = jSONObject.getString("version");
        if (set.contains(string)) {
            String optString = jSONObject.optString("deviceId", "");
            if (!optString.equals("") && optString.equals(str2)) {
                String optString2 = jSONObject.optString("sha256_host", "");
                if (!optString2.equals("") && optString2.equals(HashUtil.b(map.get(string)))) {
                    try {
                        String string3 = jSONObject.getString("localKey4Sign");
                        String string4 = jSONObject.getString("localKey4Encrypt");
                        KeyPair i10 = CryptoKeyStore.i(context, u("pki_sdk_key4Encrypt", string, string2));
                        KeyPair i11 = CryptoKeyStore.i(context, u("pki_sdk_key4Sign", string, string2));
                        if (i10 != null && i11 != null) {
                            if (!string4.equals(Base64Utils.b(i10.getPublic().getEncoded()))) {
                                i.d("Util", "parseLocalKeysRecord public key record for encrypt does not match, biz = " + string);
                                return null;
                            }
                            if (!string3.equals(Base64Utils.b(i11.getPublic().getEncoded()))) {
                                i.d("Util", "parseLocalKeysRecord public key record for sign does not match, biz = " + string);
                                return null;
                            }
                            LocalBizKeyPairs localBizKeyPairs = new LocalBizKeyPairs();
                            localBizKeyPairs.a(string, i11, i10, string2);
                            i.a("Util", "parseLocalKeysRecord successfully read a record, biz = " + string + ", version = " + string2);
                            return localBizKeyPairs;
                        }
                        i.a("Util", "parseLocalKeysRecord key pair is lost, biz = " + string + ", version = " + string2);
                    } catch (EncryptException | JSONException e10) {
                        e(context, string, string2);
                        i.d("Util", "parseLocalKeysRecord failed to get key data. " + e10);
                    }
                } else {
                    i.a("Util", "parseLocalKeysRecord host name has changed, biz = " + string);
                    e(context, string, string2);
                    return null;
                }
            } else {
                i.a("Util", "parseLocalKeysRecord device id has changed, biz = " + string);
                e(context, string, string2);
                return null;
            }
        } else {
            i.a("Util", "parseLocalKeysRecord the record that is no longer used, biz = " + string);
            e(context, string, string2);
        }
        return null;
    }

    public static void p(Context context, Set<String> set, BizCertMemoryDataSource bizCertMemoryDataSource, Map<String, String> map, String str) {
        File c10 = FileUtil.c("allregisterkeysrecord", context);
        if (c10.exists()) {
            i.a("Util", "readAllLocalKeysRecordInSP old REGISTER_KEY_FILE delete: " + c10.delete());
        }
        try {
            SecretKey e10 = CryptoKeyStore.e(context, "pki_sdk_all_localKeys_alias_v2", "pki_sdk_register_local_key_sp");
            SharedPreferences sharedPreferences = context.getSharedPreferences("pki_sdk_register_local_key_sp", 0);
            Map<String, ?> all = sharedPreferences.getAll();
            if (all.isEmpty()) {
                return;
            }
            HashMap hashMap = new HashMap();
            for (String str2 : all.keySet()) {
                String string = sharedPreferences.getString(str2, "");
                if (string.equals("")) {
                    i.d("Util", "readAllLocalKeysRecordInSP corrupted data, discard this record.");
                } else {
                    try {
                        String b10 = b(string, e10);
                        if (str2.equals("modifiedDate")) {
                            long parseLong = Long.parseLong(b10);
                            if (parseLong != 0) {
                                bizCertMemoryDataSource.i(parseLong);
                                hashMap.put(str2, string);
                            }
                        } else {
                            try {
                                LocalBizKeyPairs o10 = o(b10, context, set, map, str);
                                if (o10 != null) {
                                    String c11 = o10.c();
                                    if (str2.equals(c11)) {
                                        bizCertMemoryDataSource.j(c11, o10);
                                        hashMap.put(c11, string);
                                    } else {
                                        i.b("Util", "readAllLocalKeysRecordInSP the record have been illegally altered, biz = " + c11 + ", k = " + str2);
                                    }
                                }
                            } catch (EncryptException | NumberFormatException | JSONException | InvalidArgumentException e11) {
                                e = e11;
                                i.d("Util", "readAllLocalKeysRecordInSP error parsing a record. " + e);
                            }
                        }
                    } catch (EncryptException | NumberFormatException | JSONException | InvalidArgumentException e12) {
                        e = e12;
                    }
                }
            }
            if (!hashMap.isEmpty() && (hashMap.size() != 1 || !hashMap.containsKey("modifiedDate"))) {
                if (hashMap.size() != all.size()) {
                    w(sharedPreferences, hashMap);
                    return;
                }
                return;
            }
            FileUtil.b(context, "pki_sdk_register_local_key_sp");
        } catch (KeyStoreException e13) {
            i.d("Util", "readAllLocalKeysRecordInSP secretKey generation error, unable to read register pub key record. " + e13);
        }
    }

    public static int q(long j10) {
        return a(j10, 15724800);
    }

    private static UpgradeCertResponse r(String str, String str2, long j10, Context context) {
        JSONObject jSONObject;
        String string;
        try {
            jSONObject = new JSONObject(str);
            string = jSONObject.getString("bizId");
        } catch (EncryptException | IOException | CertificateException | JSONException | InvalidArgumentException e10) {
            i.d("Util", "restoreBizCertificate error parsing a record. " + e10);
        }
        if (!string.equals(str2)) {
            i.a("Util", "restoreBizCertificate non-specified biz, get biz id = " + string);
            return null;
        }
        long j11 = jSONObject.getLong("version");
        if (j10 != -1 && j11 != j10) {
            i.a("Util", "restoreBizCertificate non-specified version, get version = " + j11);
            return null;
        }
        X509Certificate e11 = CertUtil.e(Base64Utils.a(jSONObject.getString("cert4Sign")));
        X509Certificate e12 = CertUtil.e(Base64Utils.a(jSONObject.getString("cert4Encrypt")));
        CertParameters s7 = new CertParameters.b().u(context).v(e11).s();
        CertParameters s10 = new CertParameters.b().u(context).v(e12).s();
        if (CertUtil.a(s7) && CertUtil.a(s10)) {
            UpgradeCertResponse upgradeCertResponse = new UpgradeCertResponse();
            upgradeCertResponse.a(str2, e11, e12, j11);
            i.a("Util", "restoreBizCertificate successfully read " + str2 + "(" + j11 + ") certificate.");
            return upgradeCertResponse;
        }
        return null;
    }

    public static void s(Context context, Set<String> set, BizCertMemoryDataSource bizCertMemoryDataSource, long j10) {
        SecretKey e10 = CryptoKeyStore.e(context, "pki_sdk_all_bizCerts_alias_v2", "pki_sdk_biz_certs_sp");
        SharedPreferences sharedPreferences = context.getSharedPreferences("pki_sdk_biz_certs_sp", 0);
        if (j10 == -1 && sharedPreferences.getString("modifiedDate", "").equals("")) {
            j10 = DateUtil.a();
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        ArrayList arrayList = new ArrayList();
        for (String str : set) {
            try {
                String f10 = f(bizCertMemoryDataSource.f(str), e10);
                if (f10 != null) {
                    edit.putString(str, f10);
                    arrayList.add(f10);
                }
            } catch (EncryptException | CertificateEncodingException | JSONException | BizDataNotFoundException | InvalidArgumentException e11) {
                i.d("Util", "saveBizCertRecordInSP pack " + str + " record error. " + e11);
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        if (j10 != -1) {
            bizCertMemoryDataSource.g(j10);
            try {
                edit.putString("modifiedDate", h(String.valueOf(j10), e10));
            } catch (EncryptException | JSONException | InvalidArgumentException e12) {
                i.d("Util", "saveBizCertRecordInSP encrypt upgrade time error. " + e12);
            }
        }
        edit.apply();
    }

    public static void t(Context context, Map<String, String> map, long j10) {
        if (map.isEmpty()) {
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("pki_sdk_register_local_key_sp", 0);
        if (j10 == -1 && sharedPreferences.getString("modifiedDate", "").equals("")) {
            j10 = DateUtil.a();
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        for (String str : map.keySet()) {
            edit.putString(str, map.get(str));
        }
        if (j10 != -1) {
            try {
                edit.putString("modifiedDate", h(String.valueOf(j10), CryptoKeyStore.e(context, "pki_sdk_all_localKeys_alias_v2", "pki_sdk_register_local_key_sp")));
            } catch (EncryptException | KeyStoreException | JSONException | InvalidArgumentException e10) {
                i.d("Util", "saveLocalKeysRecordInSP encrypt upgrade time error. " + e10);
            }
        }
        edit.apply();
    }

    private static String u(String... strArr) {
        return v("&", strArr);
    }

    private static String v(String str, String... strArr) {
        StringBuilder sb2 = new StringBuilder();
        for (String str2 : strArr) {
            sb2.append(str2);
            sb2.append(str);
        }
        sb2.delete(sb2.length() - str.length(), sb2.length());
        return new String(sb2);
    }

    private static void w(SharedPreferences sharedPreferences, Map<String, String> map) {
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

    public static int x(long j10, int i10) {
        return a(j10, i10);
    }
}
