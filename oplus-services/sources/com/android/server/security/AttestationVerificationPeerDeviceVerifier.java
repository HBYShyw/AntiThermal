package com.android.server.security;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.security.attestationverification.AttestationVerificationManager;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.security.AndroidKeystoreAttestationVerificationAttributes;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AttestationVerificationPeerDeviceVerifier {
    private static final String ANDROID_SYSTEM_PACKAGE_NAME = "AndroidSystem";
    private static final Set<String> ANDROID_SYSTEM_PACKAGE_NAME_SET;
    private static final boolean DEBUG;
    private static final int MAX_PATCH_AGE_MONTHS = 12;
    private static final String PARAM_OWNED_BY_SYSTEM = "android.key_owned_by_system";
    private static final String TAG = "AVF";
    private final CertPathValidator mCertPathValidator;
    private final CertificateFactory mCertificateFactory;
    private final Context mContext;
    private final boolean mRevocationEnabled;
    private final LocalDate mTestLocalPatchDate;
    private final LocalDate mTestSystemDate;
    private final Set<TrustAnchor> mTrustAnchors;

    static {
        DEBUG = Build.IS_DEBUGGABLE && Log.isLoggable(TAG, 2);
        ANDROID_SYSTEM_PACKAGE_NAME_SET = Collections.singleton(ANDROID_SYSTEM_PACKAGE_NAME);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AttestationVerificationPeerDeviceVerifier(Context context) throws Exception {
        Objects.requireNonNull(context);
        this.mContext = context;
        this.mCertificateFactory = CertificateFactory.getInstance("X.509");
        this.mCertPathValidator = CertPathValidator.getInstance("PKIX");
        this.mTrustAnchors = getTrustAnchors();
        this.mRevocationEnabled = true;
        this.mTestSystemDate = null;
        this.mTestLocalPatchDate = null;
    }

    @VisibleForTesting
    AttestationVerificationPeerDeviceVerifier(Context context, Set<TrustAnchor> set, boolean z, LocalDate localDate, LocalDate localDate2) throws Exception {
        Objects.requireNonNull(context);
        this.mContext = context;
        this.mCertificateFactory = CertificateFactory.getInstance("X.509");
        this.mCertPathValidator = CertPathValidator.getInstance("PKIX");
        this.mTrustAnchors = set;
        this.mRevocationEnabled = z;
        this.mTestSystemDate = localDate;
        this.mTestLocalPatchDate = localDate2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int verifyAttestation(int i, Bundle bundle, byte[] bArr) {
        if (this.mCertificateFactory == null) {
            debugVerboseLog("Unable to access CertificateFactory");
            return 2;
        }
        if (this.mCertPathValidator == null) {
            debugVerboseLog("Unable to access CertPathValidator");
            return 2;
        }
        if (!validateAttestationParameters(i, bundle)) {
            return 2;
        }
        try {
            List<X509Certificate> certificates = getCertificates(bArr);
            validateCertificateChain(certificates);
            X509Certificate x509Certificate = certificates.get(0);
            AndroidKeystoreAttestationVerificationAttributes fromCertificate = AndroidKeystoreAttestationVerificationAttributes.fromCertificate(x509Certificate);
            if (checkAttestationForPeerDeviceProfile(fromCertificate)) {
                return !checkLocalBindingRequirements(x509Certificate, fromCertificate, i, bundle) ? 2 : 1;
            }
            return 2;
        } catch (IOException | InvalidAlgorithmParameterException | CertPathValidatorException | CertificateException e) {
            debugVerboseLog("Unable to parse/validate Android Attestation certificate(s)", e);
            return 2;
        } catch (RuntimeException e2) {
            debugVerboseLog("Unexpected error", e2);
            return 2;
        }
    }

    private List<X509Certificate> getCertificates(byte[] bArr) throws CertificateException {
        ArrayList arrayList = new ArrayList();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        while (byteArrayInputStream.available() > 0) {
            arrayList.add((X509Certificate) this.mCertificateFactory.generateCertificate(byteArrayInputStream));
        }
        return arrayList;
    }

    private boolean validateAttestationParameters(int i, Bundle bundle) {
        if (i != 2 && i != 3) {
            debugVerboseLog("Binding type is not supported: " + i);
            return false;
        }
        if (bundle.size() < 1) {
            debugVerboseLog("At least 1 requirement is required.");
            return false;
        }
        if (i == 2 && !bundle.containsKey("localbinding.public_key")) {
            debugVerboseLog("Requirements does not contain key: localbinding.public_key");
            return false;
        }
        if (i != 3 || bundle.containsKey("localbinding.challenge")) {
            return true;
        }
        debugVerboseLog("Requirements does not contain key: localbinding.challenge");
        return false;
    }

    private void validateCertificateChain(List<X509Certificate> list) throws CertificateException, CertPathValidatorException, InvalidAlgorithmParameterException {
        if (list.size() < 2) {
            debugVerboseLog("Certificate chain less than 2 in size.");
            throw new CertificateException("Certificate chain less than 2 in size.");
        }
        CertPath generateCertPath = this.mCertificateFactory.generateCertPath(list);
        PKIXParameters pKIXParameters = new PKIXParameters(this.mTrustAnchors);
        if (this.mRevocationEnabled) {
            pKIXParameters.addCertPathChecker(new AndroidRevocationStatusListChecker());
        }
        pKIXParameters.setRevocationEnabled(false);
        this.mCertPathValidator.validate(generateCertPath, pKIXParameters);
    }

    private Set<TrustAnchor> getTrustAnchors() throws CertPathValidatorException {
        HashSet hashSet = new HashSet();
        try {
            for (String str : getTrustAnchorResources()) {
                hashSet.add(new TrustAnchor((X509Certificate) this.mCertificateFactory.generateCertificate(new ByteArrayInputStream(getCertificateBytes(str))), null));
            }
            return Collections.unmodifiableSet(hashSet);
        } catch (CertificateException e) {
            e.printStackTrace();
            throw new CertPathValidatorException("Invalid trust anchor certificate.", e);
        }
    }

    private byte[] getCertificateBytes(String str) {
        return str.replaceAll("\\s+", "\n").replaceAll("-BEGIN\\nCERTIFICATE-", "-BEGIN CERTIFICATE-").replaceAll("-END\\nCERTIFICATE-", "-END CERTIFICATE-").getBytes(StandardCharsets.UTF_8);
    }

    private String[] getTrustAnchorResources() {
        return this.mContext.getResources().getStringArray(17236219);
    }

    private boolean checkLocalBindingRequirements(X509Certificate x509Certificate, AndroidKeystoreAttestationVerificationAttributes androidKeystoreAttestationVerificationAttributes, int i, Bundle bundle) {
        if (i != 2) {
            if (i == 3) {
                if (!checkAttestationChallenge(androidKeystoreAttestationVerificationAttributes, bundle.getByteArray("localbinding.challenge"))) {
                    debugVerboseLog("Provided challenge does not match leaf certificate challenge.");
                    return false;
                }
            } else {
                throw new IllegalArgumentException("Unsupported local binding type " + AttestationVerificationManager.localBindingTypeToString(i));
            }
        } else if (!checkPublicKey(x509Certificate, bundle.getByteArray("localbinding.public_key"))) {
            debugVerboseLog("Provided public key does not match leaf certificate public key.");
            return false;
        }
        if (!bundle.containsKey(PARAM_OWNED_BY_SYSTEM)) {
            return true;
        }
        if (bundle.getBoolean(PARAM_OWNED_BY_SYSTEM)) {
            if (checkOwnedBySystem(x509Certificate, androidKeystoreAttestationVerificationAttributes)) {
                return true;
            }
            debugVerboseLog("Certificate public key is not owned by the AndroidSystem.");
            return false;
        }
        throw new IllegalArgumentException("The value of the requirement key android.key_owned_by_system cannot be false. You can remove the key if you don't want to verify it.");
    }

    private boolean checkAttestationForPeerDeviceProfile(AndroidKeystoreAttestationVerificationAttributes androidKeystoreAttestationVerificationAttributes) {
        if (androidKeystoreAttestationVerificationAttributes.getAttestationVersion() < 3) {
            debugVerboseLog("Attestation version is not at least 3 (Keymaster 4).");
            return false;
        }
        if (androidKeystoreAttestationVerificationAttributes.getKeymasterVersion() < 4) {
            debugVerboseLog("Keymaster version is not at least 4.");
            return false;
        }
        if (androidKeystoreAttestationVerificationAttributes.getKeyOsVersion() < 100000) {
            debugVerboseLog("Android OS version is not 10+.");
            return false;
        }
        if (!androidKeystoreAttestationVerificationAttributes.isAttestationHardwareBacked()) {
            debugVerboseLog("Key is not HW backed.");
            return false;
        }
        if (!androidKeystoreAttestationVerificationAttributes.isKeymasterHardwareBacked()) {
            debugVerboseLog("Keymaster is not HW backed.");
            return false;
        }
        if (androidKeystoreAttestationVerificationAttributes.getVerifiedBootState() != AndroidKeystoreAttestationVerificationAttributes.VerifiedBootState.VERIFIED) {
            debugVerboseLog("Boot state not Verified.");
            return false;
        }
        try {
            if (!androidKeystoreAttestationVerificationAttributes.isVerifiedBootLocked()) {
                debugVerboseLog("Verified boot state is not locked.");
                return false;
            }
            if (!isValidPatchLevel(androidKeystoreAttestationVerificationAttributes.getKeyOsPatchLevel())) {
                debugVerboseLog("OS patch level is not within valid range.");
                return false;
            }
            if (!isValidPatchLevel(androidKeystoreAttestationVerificationAttributes.getKeyBootPatchLevel())) {
                debugVerboseLog("Boot patch level is not within valid range.");
                return false;
            }
            if (!isValidPatchLevel(androidKeystoreAttestationVerificationAttributes.getKeyVendorPatchLevel())) {
                debugVerboseLog("Vendor patch level is not within valid range.");
                return false;
            }
            if (isValidPatchLevel(androidKeystoreAttestationVerificationAttributes.getKeyBootPatchLevel())) {
                return true;
            }
            debugVerboseLog("Boot patch level is not within valid range.");
            return false;
        } catch (IllegalStateException e) {
            debugVerboseLog("VerifiedBootLocked is not set.", e);
            return false;
        }
    }

    private boolean checkPublicKey(Certificate certificate, byte[] bArr) {
        return Arrays.equals(certificate.getPublicKey().getEncoded(), bArr);
    }

    private boolean checkAttestationChallenge(AndroidKeystoreAttestationVerificationAttributes androidKeystoreAttestationVerificationAttributes, byte[] bArr) {
        return Arrays.equals(androidKeystoreAttestationVerificationAttributes.getAttestationChallenge().toByteArray(), bArr);
    }

    private boolean checkOwnedBySystem(X509Certificate x509Certificate, AndroidKeystoreAttestationVerificationAttributes androidKeystoreAttestationVerificationAttributes) {
        Set<String> keySet = androidKeystoreAttestationVerificationAttributes.getApplicationPackageNameVersion().keySet();
        if (ANDROID_SYSTEM_PACKAGE_NAME_SET.equals(keySet)) {
            return true;
        }
        debugVerboseLog("Owner is not system, packages=" + keySet);
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0063, code lost:
    
        if (java.time.temporal.ChronoUnit.MONTHS.between(r7, r8) <= 12) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0065, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0079, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0076, code lost:
    
        if (java.time.temporal.ChronoUnit.MONTHS.between(r8, r7) <= 12) goto L24;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean isValidPatchLevel(int i) {
        LocalDate localDate = this.mTestSystemDate;
        if (localDate == null) {
            localDate = LocalDate.now(ZoneId.systemDefault());
        }
        boolean z = false;
        try {
            LocalDate localDate2 = this.mTestLocalPatchDate;
            if (localDate2 == null) {
                localDate2 = LocalDate.parse(Build.VERSION.SECURITY_PATCH);
            }
            if (ChronoUnit.MONTHS.between(localDate2, localDate) > 12) {
                return true;
            }
            String valueOf = String.valueOf(i);
            if (valueOf.length() != 6 && valueOf.length() != 8) {
                debugVerboseLog("Patch level is not in format YYYYMM or YYYYMMDD");
                return false;
            }
            LocalDate of = LocalDate.of(Integer.parseInt(valueOf.substring(0, 4)), Integer.parseInt(valueOf.substring(4, 6)), 1);
            if (of.compareTo((ChronoLocalDate) localDate2) <= 0) {
                if (of.compareTo((ChronoLocalDate) localDate2) >= 0) {
                    return true;
                }
            }
        } catch (Throwable unused) {
            debugVerboseLog("Build.VERSION.SECURITY_PATCH: " + Build.VERSION.SECURITY_PATCH + " is not in format YYYY-MM-DD");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class AndroidRevocationStatusListChecker extends PKIXCertPathChecker {
        private static final String REASON_PROPERTY_KEY = "reason";
        private static final String STATUS_PROPERTY_KEY = "status";
        private static final String TOP_LEVEL_JSON_PROPERTY_KEY = "entries";
        private JSONObject mJsonStatusMap;
        private String mStatusUrl;

        @Override // java.security.cert.PKIXCertPathChecker
        public Set<String> getSupportedExtensions() {
            return null;
        }

        @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
        public boolean isForwardCheckingSupported() {
            return false;
        }

        private AndroidRevocationStatusListChecker() {
        }

        @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
        public void init(boolean z) throws CertPathValidatorException {
            String revocationListUrl = getRevocationListUrl();
            this.mStatusUrl = revocationListUrl;
            if (revocationListUrl == null || revocationListUrl.isEmpty()) {
                throw new CertPathValidatorException("R.string.vendor_required_attestation_revocation_list_url is empty.");
            }
            this.mJsonStatusMap = getStatusMap(this.mStatusUrl);
        }

        @Override // java.security.cert.PKIXCertPathChecker
        public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
            String bigInteger = ((X509Certificate) certificate).getSerialNumber().toString(16);
            if (bigInteger == null) {
                throw new CertPathValidatorException("Certificate serial number can not be null.");
            }
            if (this.mJsonStatusMap.has(bigInteger)) {
                JSONObject jSONObject = this.mJsonStatusMap.getJSONObject(bigInteger);
                throw new CertPathValidatorException("Invalid certificate with serial number " + bigInteger + " has status " + jSONObject.getString(STATUS_PROPERTY_KEY) + " because reason " + jSONObject.getString("reason"));
            }
        }

        private JSONObject getStatusMap(String str) throws CertPathValidatorException {
            try {
                try {
                    InputStream openStream = new URL(str).openStream();
                    try {
                        JSONObject jSONObject = new JSONObject(new String(openStream.readAllBytes(), StandardCharsets.UTF_8)).getJSONObject(TOP_LEVEL_JSON_PROPERTY_KEY);
                        openStream.close();
                        return jSONObject;
                    } finally {
                    }
                } catch (Throwable th) {
                    throw new CertPathValidatorException("Unable to parse revocation status from " + this.mStatusUrl, th);
                }
            } catch (Throwable th2) {
                throw new CertPathValidatorException("Unable to get revocation status from " + this.mStatusUrl, th2);
            }
        }

        private String getRevocationListUrl() {
            return AttestationVerificationPeerDeviceVerifier.this.mContext.getResources().getString(17041817);
        }
    }

    private static void debugVerboseLog(String str, Throwable th) {
        if (DEBUG) {
            Slog.v(TAG, str, th);
        }
    }

    private static void debugVerboseLog(String str) {
        if (DEBUG) {
            Slog.v(TAG, str);
        }
    }
}
