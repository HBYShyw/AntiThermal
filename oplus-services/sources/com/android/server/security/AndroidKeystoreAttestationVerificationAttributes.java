package com.android.server.security;

import com.android.framework.protobuf.ByteString;
import com.android.internal.org.bouncycastle.asn1.ASN1Boolean;
import com.android.internal.org.bouncycastle.asn1.ASN1Encodable;
import com.android.internal.org.bouncycastle.asn1.ASN1Enumerated;
import com.android.internal.org.bouncycastle.asn1.ASN1InputStream;
import com.android.internal.org.bouncycastle.asn1.ASN1Integer;
import com.android.internal.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.android.internal.org.bouncycastle.asn1.ASN1OctetString;
import com.android.internal.org.bouncycastle.asn1.ASN1Sequence;
import com.android.internal.org.bouncycastle.asn1.ASN1TaggedObject;
import com.android.internal.org.bouncycastle.asn1.x509.Certificate;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class AndroidKeystoreAttestationVerificationAttributes {
    private static final String ANDROID_KEYMASTER_KEY_DESCRIPTION_EXTENSION_OID = "1.3.6.1.4.1.11129.2.1.17";
    private static final int ATTESTATION_CHALLENGE_INDEX = 4;
    private static final int ATTESTATION_SECURITY_LEVEL_INDEX = 1;
    private static final int ATTESTATION_VERSION_INDEX = 0;
    private static final int HW_AUTH_NONE = 0;
    private static final int HW_ENFORCED_INDEX = 7;
    private static final int KEYMASTER_SECURITY_LEVEL_INDEX = 3;
    private static final int KEYMASTER_UNIQUE_ID_INDEX = 5;
    private static final int KEYMASTER_VERSION_INDEX = 2;
    private static final int KM_SECURITY_LEVEL_SOFTWARE = 0;
    private static final int KM_SECURITY_LEVEL_STRONG_BOX = 2;
    private static final int KM_SECURITY_LEVEL_TRUSTED_ENVIRONMENT = 1;
    private static final int KM_TAG_ALL_APPLICATIONS = 600;
    private static final int KM_TAG_ATTESTATION_APPLICATION_ID = 709;
    private static final int KM_TAG_ATTESTATION_ID_BRAND = 710;
    private static final int KM_TAG_ATTESTATION_ID_DEVICE = 711;
    private static final int KM_TAG_ATTESTATION_ID_PRODUCT = 712;
    private static final int KM_TAG_BOOT_PATCHLEVEL = 719;
    private static final int KM_TAG_NO_AUTH_REQUIRED = 503;
    private static final int KM_TAG_OS_PATCHLEVEL = 706;
    private static final int KM_TAG_OS_VERSION = 705;
    private static final int KM_TAG_ROOT_OF_TRUST = 704;
    private static final int KM_TAG_UNLOCKED_DEVICE_REQUIRED = 509;
    private static final int KM_TAG_VENDOR_PATCHLEVEL = 718;
    private static final int KM_VERIFIED_BOOT_STATE_FAILED = 3;
    private static final int KM_VERIFIED_BOOT_STATE_SELF_SIGNED = 1;
    private static final int KM_VERIFIED_BOOT_STATE_UNVERIFIED = 2;
    private static final int KM_VERIFIED_BOOT_STATE_VERIFIED = 0;
    private static final int PACKAGE_INFO_NAME_INDEX = 0;
    private static final int PACKAGE_INFO_SET_INDEX = 0;
    private static final int PACKAGE_INFO_VERSION_INDEX = 1;
    private static final int PACKAGE_SIGNATURE_SET_INDEX = 1;
    private static final int SW_ENFORCED_INDEX = 6;
    private static final int VERIFIED_BOOT_HASH_INDEX = 3;
    private static final int VERIFIED_BOOT_KEY_INDEX = 0;
    private static final int VERIFIED_BOOT_LOCKED_INDEX = 1;
    private static final int VERIFIED_BOOT_STATE_INDEX = 2;
    private ByteString mAttestationChallenge;
    private boolean mAttestationHardwareBacked;
    private SecurityLevel mAttestationSecurityLevel;
    private Integer mAttestationVersion;
    private String mDeviceBrand;
    private String mDeviceName;
    private String mDeviceProductName;
    private boolean mKeyAllowedForAllApplications;
    private Integer mKeyAuthenticatorType;
    private Integer mKeyBootPatchLevel;
    private Integer mKeyOsPatchLevel;
    private Integer mKeyOsVersion;
    private Boolean mKeyRequiresUnlockedDevice;
    private Integer mKeyVendorPatchLevel;
    private boolean mKeymasterHardwareBacked;
    private SecurityLevel mKeymasterSecurityLevel;
    private ByteString mKeymasterUniqueId;
    private Integer mKeymasterVersion;
    private ByteString mVerifiedBootHash;
    private ByteString mVerifiedBootKey;
    private Boolean mVerifiedBootLocked;
    private VerifiedBootState mVerifiedBootState;
    private Map<String, Long> mApplicationPackageNameVersion = null;
    private List<ByteString> mApplicationCertificateDigests = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum SecurityLevel {
        SOFTWARE,
        TRUSTED_ENVIRONMENT,
        STRONG_BOX
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum VerifiedBootState {
        VERIFIED,
        SELF_SIGNED,
        UNVERIFIED,
        FAILED
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AndroidKeystoreAttestationVerificationAttributes fromCertificate(X509Certificate x509Certificate) throws CertificateEncodingException, IOException {
        return new AndroidKeystoreAttestationVerificationAttributes(x509Certificate);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAttestationVersion() {
        return this.mAttestationVersion.intValue();
    }

    SecurityLevel getAttestationSecurityLevel() {
        return this.mAttestationSecurityLevel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAttestationHardwareBacked() {
        return this.mAttestationHardwareBacked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getKeymasterVersion() {
        return this.mKeymasterVersion.intValue();
    }

    SecurityLevel getKeymasterSecurityLevel() {
        return this.mKeymasterSecurityLevel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeymasterHardwareBacked() {
        return this.mKeymasterHardwareBacked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteString getAttestationChallenge() {
        return this.mAttestationChallenge;
    }

    ByteString getKeymasterUniqueId() {
        return this.mKeymasterUniqueId;
    }

    String getDeviceBrand() {
        return this.mDeviceBrand;
    }

    String getDeviceName() {
        return this.mDeviceName;
    }

    String getDeviceProductName() {
        return this.mDeviceProductName;
    }

    boolean isKeyAllowedForAllApplications() {
        return this.mKeyAllowedForAllApplications;
    }

    int getKeyAuthenticatorType() {
        Integer num = this.mKeyAuthenticatorType;
        if (num == null) {
            throw new IllegalStateException("KeyAuthenticatorType is not set.");
        }
        return num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getKeyBootPatchLevel() {
        Integer num = this.mKeyBootPatchLevel;
        if (num == null) {
            throw new IllegalStateException("KeyBootPatchLevel is not set.");
        }
        return num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getKeyOsPatchLevel() {
        Integer num = this.mKeyOsPatchLevel;
        if (num == null) {
            throw new IllegalStateException("KeyOsPatchLevel is not set.");
        }
        return num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getKeyVendorPatchLevel() {
        Integer num = this.mKeyVendorPatchLevel;
        if (num == null) {
            throw new IllegalStateException("KeyVendorPatchLevel is not set.");
        }
        return num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getKeyOsVersion() {
        Integer num = this.mKeyOsVersion;
        if (num == null) {
            throw new IllegalStateException("KeyOsVersion is not set.");
        }
        return num.intValue();
    }

    boolean isKeyRequiresUnlockedDevice() {
        Boolean bool = this.mKeyRequiresUnlockedDevice;
        if (bool == null) {
            throw new IllegalStateException("KeyRequiresUnlockedDevice is not set.");
        }
        return bool.booleanValue();
    }

    ByteString getVerifiedBootHash() {
        return this.mVerifiedBootHash;
    }

    ByteString getVerifiedBootKey() {
        return this.mVerifiedBootKey;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isVerifiedBootLocked() {
        Boolean bool = this.mVerifiedBootLocked;
        if (bool == null) {
            throw new IllegalStateException("VerifiedBootLocked is not set.");
        }
        return bool.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VerifiedBootState getVerifiedBootState() {
        return this.mVerifiedBootState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, Long> getApplicationPackageNameVersion() {
        return Collections.unmodifiableMap(this.mApplicationPackageNameVersion);
    }

    List<ByteString> getApplicationCertificateDigests() {
        return Collections.unmodifiableList(this.mApplicationCertificateDigests);
    }

    private AndroidKeystoreAttestationVerificationAttributes(X509Certificate x509Certificate) throws CertificateEncodingException, IOException {
        this.mAttestationVersion = null;
        this.mAttestationSecurityLevel = null;
        this.mAttestationHardwareBacked = false;
        this.mKeymasterVersion = null;
        this.mKeymasterSecurityLevel = null;
        this.mKeymasterHardwareBacked = false;
        this.mAttestationChallenge = null;
        this.mKeymasterUniqueId = null;
        this.mDeviceBrand = null;
        this.mDeviceName = null;
        this.mDeviceProductName = null;
        this.mKeyAllowedForAllApplications = false;
        this.mKeyAuthenticatorType = null;
        this.mKeyBootPatchLevel = null;
        this.mKeyOsPatchLevel = null;
        this.mKeyOsVersion = null;
        this.mKeyVendorPatchLevel = null;
        this.mKeyRequiresUnlockedDevice = null;
        this.mVerifiedBootHash = null;
        this.mVerifiedBootKey = null;
        this.mVerifiedBootLocked = null;
        this.mVerifiedBootState = null;
        ASN1Sequence extensionParsedValue = Certificate.getInstance(new ASN1InputStream(x509Certificate.getEncoded()).readObject()).getTBSCertificate().getExtensions().getExtensionParsedValue(new ASN1ObjectIdentifier(ANDROID_KEYMASTER_KEY_DESCRIPTION_EXTENSION_OID));
        if (extensionParsedValue == null) {
            throw new CertificateEncodingException("No attestation extension found in certificate.");
        }
        this.mAttestationVersion = Integer.valueOf(getIntegerFromAsn1(extensionParsedValue.getObjectAt(0)));
        SecurityLevel securityLevelEnum = getSecurityLevelEnum(extensionParsedValue.getObjectAt(1));
        this.mAttestationSecurityLevel = securityLevelEnum;
        SecurityLevel securityLevel = SecurityLevel.TRUSTED_ENVIRONMENT;
        this.mAttestationHardwareBacked = securityLevelEnum == securityLevel;
        this.mAttestationChallenge = getOctetsFromAsn1(extensionParsedValue.getObjectAt(4));
        this.mKeymasterVersion = Integer.valueOf(getIntegerFromAsn1(extensionParsedValue.getObjectAt(2)));
        this.mKeymasterUniqueId = getOctetsFromAsn1(extensionParsedValue.getObjectAt(5));
        SecurityLevel securityLevelEnum2 = getSecurityLevelEnum(extensionParsedValue.getObjectAt(3));
        this.mKeymasterSecurityLevel = securityLevelEnum2;
        this.mKeymasterHardwareBacked = securityLevelEnum2 == securityLevel;
        for (ASN1TaggedObject aSN1TaggedObject : extensionParsedValue.getObjectAt(6).toArray()) {
            int tagNo = aSN1TaggedObject.getTagNo();
            if (tagNo == KM_TAG_UNLOCKED_DEVICE_REQUIRED) {
                this.mKeyRequiresUnlockedDevice = getBoolFromAsn1(aSN1TaggedObject.getObject());
            } else if (tagNo == KM_TAG_ATTESTATION_APPLICATION_ID) {
                parseAttestationApplicationId(getOctetsFromAsn1(aSN1TaggedObject.getObject()).toByteArray());
            }
        }
        for (ASN1TaggedObject aSN1TaggedObject2 : extensionParsedValue.getObjectAt(7).toArray()) {
            int tagNo2 = aSN1TaggedObject2.getTagNo();
            if (tagNo2 == KM_TAG_NO_AUTH_REQUIRED) {
                this.mKeyAuthenticatorType = 0;
            } else if (tagNo2 == 600) {
                this.mKeyAllowedForAllApplications = true;
            } else if (tagNo2 == KM_TAG_VENDOR_PATCHLEVEL) {
                this.mKeyVendorPatchLevel = Integer.valueOf(getIntegerFromAsn1(aSN1TaggedObject2.getObject()));
            } else if (tagNo2 != KM_TAG_BOOT_PATCHLEVEL) {
                switch (tagNo2) {
                    case KM_TAG_ROOT_OF_TRUST /* 704 */:
                        ASN1Sequence object = aSN1TaggedObject2.getObject();
                        this.mVerifiedBootKey = getOctetsFromAsn1(object.getObjectAt(0));
                        this.mVerifiedBootLocked = getBoolFromAsn1(object.getObjectAt(1));
                        this.mVerifiedBootState = getVerifiedBootStateEnum(object.getObjectAt(2));
                        if (this.mAttestationVersion.intValue() >= 3) {
                            this.mVerifiedBootHash = getOctetsFromAsn1(object.getObjectAt(3));
                            break;
                        } else {
                            break;
                        }
                    case KM_TAG_OS_VERSION /* 705 */:
                        this.mKeyOsVersion = Integer.valueOf(getIntegerFromAsn1(aSN1TaggedObject2.getObject()));
                        break;
                    case KM_TAG_OS_PATCHLEVEL /* 706 */:
                        this.mKeyOsPatchLevel = Integer.valueOf(getIntegerFromAsn1(aSN1TaggedObject2.getObject()));
                        break;
                    default:
                        switch (tagNo2) {
                            case KM_TAG_ATTESTATION_ID_BRAND /* 710 */:
                                this.mDeviceBrand = getUtf8FromOctetsFromAsn1(aSN1TaggedObject2.getObject());
                                break;
                            case KM_TAG_ATTESTATION_ID_DEVICE /* 711 */:
                                this.mDeviceName = getUtf8FromOctetsFromAsn1(aSN1TaggedObject2.getObject());
                                break;
                            case KM_TAG_ATTESTATION_ID_PRODUCT /* 712 */:
                                this.mDeviceProductName = getUtf8FromOctetsFromAsn1(aSN1TaggedObject2.getObject());
                                break;
                        }
                }
            } else {
                this.mKeyBootPatchLevel = Integer.valueOf(getIntegerFromAsn1(aSN1TaggedObject2.getObject()));
            }
        }
    }

    private void parseAttestationApplicationId(byte[] bArr) throws IOException {
        ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(new ASN1InputStream(bArr).readObject());
        HashMap hashMap = new HashMap();
        for (ASN1Sequence aSN1Sequence2 : aSN1Sequence.getObjectAt(0).toArray()) {
            hashMap.put(getUtf8FromOctetsFromAsn1(aSN1Sequence2.getObjectAt(0)), Long.valueOf(getLongFromAsn1(aSN1Sequence2.getObjectAt(1))));
        }
        ArrayList arrayList = new ArrayList();
        for (ASN1Encodable aSN1Encodable : aSN1Sequence.getObjectAt(1).toArray()) {
            arrayList.add(getOctetsFromAsn1(aSN1Encodable));
        }
        this.mApplicationPackageNameVersion = Collections.unmodifiableMap(hashMap);
        this.mApplicationCertificateDigests = Collections.unmodifiableList(arrayList);
    }

    private VerifiedBootState getVerifiedBootStateEnum(ASN1Encodable aSN1Encodable) {
        int enumFromAsn1 = getEnumFromAsn1(aSN1Encodable);
        if (enumFromAsn1 == 0) {
            return VerifiedBootState.VERIFIED;
        }
        if (enumFromAsn1 == 1) {
            return VerifiedBootState.SELF_SIGNED;
        }
        if (enumFromAsn1 == 2) {
            return VerifiedBootState.UNVERIFIED;
        }
        if (enumFromAsn1 == 3) {
            return VerifiedBootState.FAILED;
        }
        throw new IllegalArgumentException("Invalid verified boot state.");
    }

    private SecurityLevel getSecurityLevelEnum(ASN1Encodable aSN1Encodable) {
        int enumFromAsn1 = getEnumFromAsn1(aSN1Encodable);
        if (enumFromAsn1 == 0) {
            return SecurityLevel.SOFTWARE;
        }
        if (enumFromAsn1 == 1) {
            return SecurityLevel.TRUSTED_ENVIRONMENT;
        }
        if (enumFromAsn1 == 2) {
            return SecurityLevel.STRONG_BOX;
        }
        throw new IllegalArgumentException("Invalid security level.");
    }

    private ByteString getOctetsFromAsn1(ASN1Encodable aSN1Encodable) {
        return ByteString.copyFrom(((ASN1OctetString) aSN1Encodable).getOctets());
    }

    private String getUtf8FromOctetsFromAsn1(ASN1Encodable aSN1Encodable) {
        return new String(((ASN1OctetString) aSN1Encodable).getOctets(), StandardCharsets.UTF_8);
    }

    private int getIntegerFromAsn1(ASN1Encodable aSN1Encodable) {
        return ((ASN1Integer) aSN1Encodable).getValue().intValueExact();
    }

    private long getLongFromAsn1(ASN1Encodable aSN1Encodable) {
        return ((ASN1Integer) aSN1Encodable).getValue().longValueExact();
    }

    private int getEnumFromAsn1(ASN1Encodable aSN1Encodable) {
        return ((ASN1Enumerated) aSN1Encodable).getValue().intValueExact();
    }

    private Boolean getBoolFromAsn1(ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable instanceof ASN1Boolean) {
            return Boolean.valueOf(((ASN1Boolean) aSN1Encodable).isTrue());
        }
        return null;
    }
}
