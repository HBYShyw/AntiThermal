package com.android.server.locksettings.recoverablekeystore.serialization;

import android.security.keystore.recovery.KeyChainProtectionParams;
import android.security.keystore.recovery.KeyChainSnapshot;
import android.security.keystore.recovery.KeyDerivationParams;
import android.security.keystore.recovery.WrappedApplicationKey;
import android.util.Base64;
import android.util.Xml;
import com.android.modules.utils.TypedXmlPullParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class KeyChainSnapshotDeserializer {
    public static KeyChainSnapshot deserialize(InputStream inputStream) throws KeyChainSnapshotParserException, IOException {
        try {
            return deserializeInternal(inputStream);
        } catch (XmlPullParserException e) {
            throw new KeyChainSnapshotParserException("Malformed KeyChainSnapshot XML", e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:61:0x007b, code lost:
    
        if (r3.equals("thmCertPath") == false) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static KeyChainSnapshot deserializeInternal(InputStream inputStream) throws IOException, XmlPullParserException, KeyChainSnapshotParserException {
        TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(inputStream);
        resolvePullParser.nextTag();
        resolvePullParser.require(2, KeyChainSnapshotSchema.NAMESPACE, "keyChainSnapshot");
        KeyChainSnapshot.Builder builder = new KeyChainSnapshot.Builder();
        while (true) {
            char c = 3;
            if (resolvePullParser.next() != 3) {
                if (resolvePullParser.getEventType() == 2) {
                    String name = resolvePullParser.getName();
                    name.hashCode();
                    switch (name.hashCode()) {
                        case -1719931702:
                            if (name.equals("maxAttempts")) {
                                c = 0;
                                break;
                            }
                            break;
                        case -1388433662:
                            if (name.equals("backendPublicKey")) {
                                c = 1;
                                break;
                            }
                            break;
                        case -1370381871:
                            if (name.equals("recoveryKeyMaterial")) {
                                c = 2;
                                break;
                            }
                            break;
                        case -1368437758:
                            break;
                        case 481270388:
                            if (name.equals("snapshotVersion")) {
                                c = 4;
                                break;
                            }
                            break;
                        case 1190285858:
                            if (name.equals("applicationKeysList")) {
                                c = 5;
                                break;
                            }
                            break;
                        case 1352257591:
                            if (name.equals("counterId")) {
                                c = 6;
                                break;
                            }
                            break;
                        case 1596875199:
                            if (name.equals("keyChainProtectionParamsList")) {
                                c = 7;
                                break;
                            }
                            break;
                        case 1806980777:
                            if (name.equals("serverParams")) {
                                c = '\b';
                                break;
                            }
                            break;
                    }
                    c = 65535;
                    switch (c) {
                        case 0:
                            builder.setMaxAttempts(readIntTag(resolvePullParser, "maxAttempts"));
                            break;
                        case 1:
                            break;
                        case 2:
                            builder.setEncryptedRecoveryKeyBlob(readBlobTag(resolvePullParser, "recoveryKeyMaterial"));
                            break;
                        case 3:
                            try {
                                builder.setTrustedHardwareCertPath(readCertPathTag(resolvePullParser, "thmCertPath"));
                                break;
                            } catch (CertificateException e) {
                                throw new KeyChainSnapshotParserException("Could not set trustedHardwareCertPath", e);
                            }
                        case 4:
                            builder.setSnapshotVersion(readIntTag(resolvePullParser, "snapshotVersion"));
                            break;
                        case 5:
                            builder.setWrappedApplicationKeys(readWrappedApplicationKeys(resolvePullParser));
                            break;
                        case 6:
                            builder.setCounterId(readLongTag(resolvePullParser, "counterId"));
                            break;
                        case 7:
                            builder.setKeyChainProtectionParams(readKeyChainProtectionParamsList(resolvePullParser));
                            break;
                        case '\b':
                            builder.setServerParams(readBlobTag(resolvePullParser, "serverParams"));
                            break;
                        default:
                            throw new KeyChainSnapshotParserException(String.format(Locale.US, "Unexpected tag %s in keyChainSnapshot", name));
                    }
                }
            } else {
                resolvePullParser.require(3, KeyChainSnapshotSchema.NAMESPACE, "keyChainSnapshot");
                try {
                    return builder.build();
                } catch (NullPointerException e2) {
                    throw new KeyChainSnapshotParserException("Failed to build KeyChainSnapshot", e2);
                }
            }
        }
    }

    private static List<WrappedApplicationKey> readWrappedApplicationKeys(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException, KeyChainSnapshotParserException {
        typedXmlPullParser.require(2, KeyChainSnapshotSchema.NAMESPACE, "applicationKeysList");
        ArrayList arrayList = new ArrayList();
        while (typedXmlPullParser.next() != 3) {
            if (typedXmlPullParser.getEventType() == 2) {
                arrayList.add(readWrappedApplicationKey(typedXmlPullParser));
            }
        }
        typedXmlPullParser.require(3, KeyChainSnapshotSchema.NAMESPACE, "applicationKeysList");
        return arrayList;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004b, code lost:
    
        switch(r8) {
            case 0: goto L41;
            case 1: goto L40;
            case 2: goto L42;
            default: goto L39;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x005f, code lost:
    
        throw new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException(java.lang.String.format(java.util.Locale.US, "Unexpected tag %s in wrappedApplicationKey", r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0068, code lost:
    
        r0.setEncryptedKeyMaterial(readBlobTag(r9, "keyMaterial"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0070, code lost:
    
        r0.setMetadata(readBlobTag(r9, "keyMetadata"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0060, code lost:
    
        r0.setAlias(readStringTag(r9, "alias"));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static WrappedApplicationKey readWrappedApplicationKey(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException, KeyChainSnapshotParserException {
        typedXmlPullParser.require(2, KeyChainSnapshotSchema.NAMESPACE, "applicationKey");
        WrappedApplicationKey.Builder builder = new WrappedApplicationKey.Builder();
        while (typedXmlPullParser.next() != 3) {
            if (typedXmlPullParser.getEventType() == 2) {
                String name = typedXmlPullParser.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -1712279890:
                        if (name.equals("keyMetadata")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -963209050:
                        if (name.equals("keyMaterial")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 92902992:
                        if (name.equals("alias")) {
                            c = 2;
                            break;
                        }
                        break;
                }
            }
        }
        typedXmlPullParser.require(3, KeyChainSnapshotSchema.NAMESPACE, "applicationKey");
        try {
            return builder.build();
        } catch (NullPointerException e) {
            throw new KeyChainSnapshotParserException("Failed to build WrappedApplicationKey", e);
        }
    }

    private static List<KeyChainProtectionParams> readKeyChainProtectionParamsList(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException, KeyChainSnapshotParserException {
        typedXmlPullParser.require(2, KeyChainSnapshotSchema.NAMESPACE, "keyChainProtectionParamsList");
        ArrayList arrayList = new ArrayList();
        while (typedXmlPullParser.next() != 3) {
            if (typedXmlPullParser.getEventType() == 2) {
                arrayList.add(readKeyChainProtectionParams(typedXmlPullParser));
            }
        }
        typedXmlPullParser.require(3, KeyChainSnapshotSchema.NAMESPACE, "keyChainProtectionParamsList");
        return arrayList;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004c, code lost:
    
        switch(r7) {
            case 0: goto L41;
            case 1: goto L40;
            case 2: goto L42;
            default: goto L39;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0060, code lost:
    
        throw new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException(java.lang.String.format(java.util.Locale.US, "Unexpected tag %s in keyChainProtectionParams", r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0069, code lost:
    
        r0.setUserSecretType(readIntTag(r8, "userSecretType"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0071, code lost:
    
        r0.setLockScreenUiFormat(readIntTag(r8, "lockScreenUiType"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0061, code lost:
    
        r0.setKeyDerivationParams(readKeyDerivationParams(r8));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static KeyChainProtectionParams readKeyChainProtectionParams(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException, KeyChainSnapshotParserException {
        typedXmlPullParser.require(2, KeyChainSnapshotSchema.NAMESPACE, "keyChainProtectionParams");
        KeyChainProtectionParams.Builder builder = new KeyChainProtectionParams.Builder();
        while (typedXmlPullParser.next() != 3) {
            if (typedXmlPullParser.getEventType() == 2) {
                String name = typedXmlPullParser.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -776797115:
                        if (name.equals("lockScreenUiType")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -696958923:
                        if (name.equals("userSecretType")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 912448924:
                        if (name.equals("keyDerivationParams")) {
                            c = 2;
                            break;
                        }
                        break;
                }
            }
        }
        typedXmlPullParser.require(3, KeyChainSnapshotSchema.NAMESPACE, "keyChainProtectionParams");
        try {
            return builder.build();
        } catch (NullPointerException e) {
            throw new KeyChainSnapshotParserException("Failed to build KeyChainProtectionParams", e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0040, code lost:
    
        if (r6.equals("salt") == false) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static KeyDerivationParams readKeyDerivationParams(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException, KeyChainSnapshotParserException {
        KeyDerivationParams createSha256Params;
        typedXmlPullParser.require(2, KeyChainSnapshotSchema.NAMESPACE, "keyDerivationParams");
        byte[] bArr = null;
        int i = -1;
        int i2 = -1;
        while (true) {
            boolean z = true;
            if (typedXmlPullParser.next() == 3) {
                if (bArr == null) {
                    throw new KeyChainSnapshotParserException("salt was not set in keyDerivationParams");
                }
                if (i == 1) {
                    createSha256Params = KeyDerivationParams.createSha256Params(bArr);
                } else if (i == 2) {
                    createSha256Params = KeyDerivationParams.createScryptParams(bArr, i2);
                } else {
                    throw new KeyChainSnapshotParserException("Unknown algorithm in keyDerivationParams");
                }
                typedXmlPullParser.require(3, KeyChainSnapshotSchema.NAMESPACE, "keyDerivationParams");
                return createSha256Params;
            }
            if (typedXmlPullParser.getEventType() == 2) {
                String name = typedXmlPullParser.getName();
                name.hashCode();
                switch (name.hashCode()) {
                    case -973274212:
                        if (name.equals("memoryDifficulty")) {
                            z = false;
                            break;
                        }
                        break;
                    case 3522646:
                        break;
                    case 225490031:
                        if (name.equals("algorithm")) {
                            z = 2;
                            break;
                        }
                        break;
                }
                z = -1;
                switch (z) {
                    case false:
                        i2 = readIntTag(typedXmlPullParser, "memoryDifficulty");
                        break;
                    case true:
                        bArr = readBlobTag(typedXmlPullParser, "salt");
                        break;
                    case true:
                        i = readIntTag(typedXmlPullParser, "algorithm");
                        break;
                    default:
                        throw new KeyChainSnapshotParserException(String.format(Locale.US, "Unexpected tag %s in keyDerivationParams", name));
                }
            }
        }
    }

    private static int readIntTag(TypedXmlPullParser typedXmlPullParser, String str) throws IOException, XmlPullParserException, KeyChainSnapshotParserException {
        String str2 = KeyChainSnapshotSchema.NAMESPACE;
        typedXmlPullParser.require(2, str2, str);
        String readText = readText(typedXmlPullParser);
        typedXmlPullParser.require(3, str2, str);
        try {
            return Integer.valueOf(readText).intValue();
        } catch (NumberFormatException e) {
            throw new KeyChainSnapshotParserException(String.format(Locale.US, "%s expected int but got '%s'", str, readText), e);
        }
    }

    private static long readLongTag(TypedXmlPullParser typedXmlPullParser, String str) throws IOException, XmlPullParserException, KeyChainSnapshotParserException {
        String str2 = KeyChainSnapshotSchema.NAMESPACE;
        typedXmlPullParser.require(2, str2, str);
        String readText = readText(typedXmlPullParser);
        typedXmlPullParser.require(3, str2, str);
        try {
            return Long.valueOf(readText).longValue();
        } catch (NumberFormatException e) {
            throw new KeyChainSnapshotParserException(String.format(Locale.US, "%s expected long but got '%s'", str, readText), e);
        }
    }

    private static String readStringTag(TypedXmlPullParser typedXmlPullParser, String str) throws IOException, XmlPullParserException {
        String str2 = KeyChainSnapshotSchema.NAMESPACE;
        typedXmlPullParser.require(2, str2, str);
        String readText = readText(typedXmlPullParser);
        typedXmlPullParser.require(3, str2, str);
        return readText;
    }

    private static byte[] readBlobTag(TypedXmlPullParser typedXmlPullParser, String str) throws IOException, XmlPullParserException, KeyChainSnapshotParserException {
        String str2 = KeyChainSnapshotSchema.NAMESPACE;
        typedXmlPullParser.require(2, str2, str);
        String readText = readText(typedXmlPullParser);
        typedXmlPullParser.require(3, str2, str);
        try {
            return Base64.decode(readText, 0);
        } catch (IllegalArgumentException e) {
            throw new KeyChainSnapshotParserException(String.format(Locale.US, "%s expected base64 encoded bytes but got '%s'", str, readText), e);
        }
    }

    private static CertPath readCertPathTag(TypedXmlPullParser typedXmlPullParser, String str) throws IOException, XmlPullParserException, KeyChainSnapshotParserException {
        try {
            return CertificateFactory.getInstance("X.509").generateCertPath(new ByteArrayInputStream(readBlobTag(typedXmlPullParser, str)));
        } catch (CertificateException e) {
            throw new KeyChainSnapshotParserException("Could not parse CertPath in tag " + str, e);
        }
    }

    private static String readText(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
        if (typedXmlPullParser.next() != 4) {
            return "";
        }
        String text = typedXmlPullParser.getText();
        typedXmlPullParser.nextTag();
        return text;
    }

    private KeyChainSnapshotDeserializer() {
    }
}
