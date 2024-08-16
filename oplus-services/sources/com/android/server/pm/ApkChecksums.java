package com.android.server.pm;

import android.content.Context;
import android.content.pm.ApkChecksum;
import android.content.pm.Checksum;
import android.content.pm.IOnChecksumsReadyListener;
import android.content.pm.PackageManagerInternal;
import android.content.pm.Signature;
import android.content.pm.parsing.ApkLiteParseUtils;
import android.content.pm.parsing.result.ParseResult;
import android.content.pm.parsing.result.ParseTypeImpl;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.incremental.IncrementalManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Pair;
import android.util.Slog;
import android.util.apk.ApkSignatureSchemeV2Verifier;
import android.util.apk.ApkSignatureSchemeV3Verifier;
import android.util.apk.ApkSignatureSchemeV4Verifier;
import android.util.apk.ApkSignatureVerifier;
import android.util.apk.ApkSigningBlockUtils;
import android.util.apk.ByteBufferFactory;
import android.util.apk.SignatureInfo;
import android.util.apk.SignatureNotFoundException;
import android.util.apk.VerityBuilder;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.security.VerityUtils;
import com.android.server.pm.pkg.AndroidPackage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.security.DigestException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ApkChecksums {
    static final String ALGO_MD5 = "MD5";
    static final String ALGO_SHA1 = "SHA1";
    static final String ALGO_SHA256 = "SHA256";
    static final String ALGO_SHA512 = "SHA512";
    private static final String DIGESTS_FILE_EXTENSION = ".digests";
    private static final String DIGESTS_SIGNATURE_FILE_EXTENSION = ".signature";
    private static final Certificate[] EMPTY_CERTIFICATE_ARRAY = new Certificate[0];
    static final int MAX_BUFFER_SIZE = 131072;
    static final int MIN_BUFFER_SIZE = 4096;
    private static final long PROCESS_REQUIRED_CHECKSUMS_DELAY_MILLIS = 1000;
    private static final long PROCESS_REQUIRED_CHECKSUMS_TIMEOUT_MILLIS = 86400000;
    static final String TAG = "ApkChecksums";

    private static int getChecksumKindForContentDigestAlgo(int i) {
        if (i != 1) {
            return i != 2 ? -1 : 64;
        }
        return 32;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        private final Producer<Context> mContext;
        private final Producer<Handler> mHandlerProducer;
        private final Producer<IncrementalManager> mIncrementalManagerProducer;
        private final Producer<PackageManagerInternal> mPackageManagerInternalProducer;

        /* JADX INFO: Access modifiers changed from: package-private */
        @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public interface Producer<T> {
            T produce();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Injector(Producer<Context> producer, Producer<Handler> producer2, Producer<IncrementalManager> producer3, Producer<PackageManagerInternal> producer4) {
            this.mContext = producer;
            this.mHandlerProducer = producer2;
            this.mIncrementalManagerProducer = producer3;
            this.mPackageManagerInternalProducer = producer4;
        }

        public Context getContext() {
            return this.mContext.produce();
        }

        public Handler getHandler() {
            return this.mHandlerProducer.produce();
        }

        public IncrementalManager getIncrementalManager() {
            return this.mIncrementalManagerProducer.produce();
        }

        public PackageManagerInternal getPackageManagerInternal() {
            return this.mPackageManagerInternalProducer.produce();
        }
    }

    public static String buildDigestsPathForApk(String str) {
        if (!ApkLiteParseUtils.isApkPath(str)) {
            throw new IllegalStateException("Code path is not an apk " + str);
        }
        return str.substring(0, str.length() - 4) + DIGESTS_FILE_EXTENSION;
    }

    public static String buildSignaturePathForDigests(String str) {
        return str + DIGESTS_SIGNATURE_FILE_EXTENSION;
    }

    public static boolean isDigestOrDigestSignatureFile(File file) {
        String name = file.getName();
        return name.endsWith(DIGESTS_FILE_EXTENSION) || name.endsWith(DIGESTS_SIGNATURE_FILE_EXTENSION);
    }

    public static File findDigestsForFile(File file) {
        File file2 = new File(buildDigestsPathForApk(file.getAbsolutePath()));
        if (file2.exists()) {
            return file2;
        }
        return null;
    }

    public static File findSignatureForDigests(File file) {
        File file2 = new File(buildSignaturePathForDigests(file.getAbsolutePath()));
        if (file2.exists()) {
            return file2;
        }
        return null;
    }

    public static void writeChecksums(OutputStream outputStream, Checksum[] checksumArr) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {
            for (Checksum checksum : checksumArr) {
                Checksum.writeToStream(dataOutputStream, checksum);
            }
            dataOutputStream.close();
        } catch (Throwable th) {
            try {
                dataOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static Checksum[] readChecksums(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            Checksum[] readChecksums = readChecksums(fileInputStream);
            fileInputStream.close();
            return readChecksums;
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static Checksum[] readChecksums(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        try {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < 100; i++) {
                try {
                    arrayList.add(Checksum.readFromStream(dataInputStream));
                } catch (EOFException unused) {
                }
            }
            Checksum[] checksumArr = (Checksum[]) arrayList.toArray(new Checksum[arrayList.size()]);
            dataInputStream.close();
            return checksumArr;
        } catch (Throwable th) {
            try {
                dataInputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static Certificate[] verifySignature(Checksum[] checksumArr, byte[] bArr) throws NoSuchAlgorithmException, IOException, SignatureException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            writeChecksums(byteArrayOutputStream, checksumArr);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            PKCS7 pkcs7 = new PKCS7(bArr);
            X509Certificate[] certificates = pkcs7.getCertificates();
            if (certificates == null || certificates.length == 0) {
                throw new SignatureException("Signature missing certificates");
            }
            SignerInfo[] verify = pkcs7.verify(byteArray);
            if (verify == null || verify.length == 0) {
                throw new SignatureException("Verification failed");
            }
            ArrayList arrayList = new ArrayList(verify.length);
            for (SignerInfo signerInfo : verify) {
                ArrayList certificateChain = signerInfo.getCertificateChain(pkcs7);
                if (certificateChain == null) {
                    throw new SignatureException("Verification passed, but certification chain is empty.");
                }
                arrayList.addAll(certificateChain);
            }
            return (Certificate[]) arrayList.toArray(new Certificate[arrayList.size()]);
        } catch (Throwable th) {
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static void getChecksums(List<Pair<String, File>> list, int i, int i2, String str, Certificate[] certificateArr, IOnChecksumsReadyListener iOnChecksumsReadyListener, Injector injector) {
        Trace.traceBegin(262144L, "PackageManagerBg getChecksums");
        ArrayList arrayList = new ArrayList(list.size());
        int size = list.size();
        for (int i3 = 0; i3 < size; i3++) {
            String str2 = (String) list.get(i3).first;
            File file = (File) list.get(i3).second;
            ArrayMap arrayMap = new ArrayMap();
            arrayList.add(arrayMap);
            try {
                getAvailableApkChecksums(str2, file, i | i2, str, certificateArr, arrayMap, injector);
            } catch (Throwable th) {
                Slog.e(TAG, "Preferred checksum calculation error", th);
            }
        }
        processRequiredChecksums(list, arrayList, i2, iOnChecksumsReadyListener, injector, SystemClock.uptimeMillis());
        Trace.traceEnd(262144L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v0 */
    /* JADX WARN: Type inference failed for: r9v1, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v6 */
    /* JADX WARN: Type inference failed for: r9v7 */
    /* JADX WARN: Type inference failed for: r9v8 */
    public static void processRequiredChecksums(final List<Pair<String, File>> list, final List<Map<Integer, ApkChecksum>> list2, final int i, final IOnChecksumsReadyListener iOnChecksumsReadyListener, final Injector injector, final long j) {
        int i2;
        ?? r9 = list;
        Trace.traceBegin(262144L, "PackageManagerBg processRequiredChecksums");
        boolean z = SystemClock.uptimeMillis() - j >= 86400000;
        ArrayList arrayList = new ArrayList();
        int size = list.size();
        int i3 = 0;
        while (i3 < size) {
            String str = (String) ((Pair) r9.get(i3)).first;
            File file = (File) ((Pair) r9.get(i3)).second;
            Map<Integer, ApkChecksum> map = list2.get(i3);
            if (z && i == 0) {
                i2 = i3;
            } else {
                try {
                    if (needToWait(file, i, map, injector)) {
                        Handler handler = injector.getHandler();
                        i2 = i3;
                        r9 = TAG;
                        handler.postDelayed(new Runnable() { // from class: com.android.server.pm.ApkChecksums$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                ApkChecksums.processRequiredChecksums(list, list2, i, iOnChecksumsReadyListener, injector, j);
                            }
                        }, 1000L);
                        return;
                    }
                    try {
                        i2 = i3;
                        getRequiredApkChecksums(str, file, i, map);
                    } catch (Throwable th) {
                        th = th;
                    }
                    th = th;
                } catch (Throwable th2) {
                    th = th2;
                    i2 = i3;
                    r9 = TAG;
                }
                Slog.e((String) r9, "Required checksum calculation error", th);
                i3 = i2 + 1;
                r9 = list;
            }
            arrayList.addAll(map.values());
            i3 = i2 + 1;
            r9 = list;
        }
        try {
            iOnChecksumsReadyListener.onChecksumsReady(arrayList);
        } catch (RemoteException e) {
            Slog.w(TAG, e);
        }
        Trace.traceEnd(262144L);
    }

    private static void getAvailableApkChecksums(String str, File file, int i, String str2, Certificate[] certificateArr, Map<Integer, ApkChecksum> map, Injector injector) {
        Map<Integer, ApkChecksum> extractHashFromV2V3Signature;
        ApkChecksum extractHashFromFS;
        String absolutePath = file.getAbsolutePath();
        if (isRequired(1, i, map) && (extractHashFromFS = extractHashFromFS(str, absolutePath)) != null) {
            map.put(Integer.valueOf(extractHashFromFS.getType()), extractHashFromFS);
        }
        if ((isRequired(32, i, map) || isRequired(64, i, map)) && (extractHashFromV2V3Signature = extractHashFromV2V3Signature(str, absolutePath, i)) != null) {
            map.putAll(extractHashFromV2V3Signature);
        }
        getInstallerChecksums(str, file, i, str2, certificateArr, map, injector);
    }

    private static void getInstallerChecksums(String str, File file, int i, String str2, Certificate[] certificateArr, Map<Integer, ApkChecksum> map, Injector injector) {
        File findDigestsForFile;
        Signature[] signatures;
        Signature[] pastSigningCertificates;
        Signature signature;
        if (PackageManagerServiceUtils.isInstalledByAdb(str2)) {
            return;
        }
        if ((certificateArr == null || certificateArr.length != 0) && (findDigestsForFile = findDigestsForFile(file)) != null) {
            File findSignatureForDigests = findSignatureForDigests(findDigestsForFile);
            try {
                Checksum[] readChecksums = readChecksums(findDigestsForFile);
                if (findSignatureForDigests != null) {
                    Certificate[] verifySignature = verifySignature(readChecksums, Files.readAllBytes(findSignatureForDigests.toPath()));
                    if (verifySignature != null && verifySignature.length != 0) {
                        signatures = new Signature[verifySignature.length];
                        int length = verifySignature.length;
                        for (int i2 = 0; i2 < length; i2++) {
                            signatures[i2] = new Signature(verifySignature[i2].getEncoded());
                        }
                        pastSigningCertificates = null;
                    }
                    Slog.e(TAG, "Error validating signature");
                    return;
                }
                AndroidPackage androidPackage = injector.getPackageManagerInternal().getPackage(str2);
                if (androidPackage == null) {
                    Slog.e(TAG, "Installer package not found.");
                    return;
                } else {
                    signatures = androidPackage.getSigningDetails().getSignatures();
                    pastSigningCertificates = androidPackage.getSigningDetails().getPastSigningCertificates();
                }
                if (signatures != null && signatures.length != 0 && (signature = signatures[0]) != null) {
                    byte[] byteArray = signature.toByteArray();
                    Set<Signature> convertToSet = convertToSet(certificateArr);
                    if (convertToSet != null && !convertToSet.isEmpty()) {
                        Signature isTrusted = isTrusted(signatures, convertToSet);
                        if (isTrusted == null) {
                            isTrusted = isTrusted(pastSigningCertificates, convertToSet);
                        }
                        if (isTrusted == null) {
                            return;
                        } else {
                            byteArray = isTrusted.toByteArray();
                        }
                    }
                    for (Checksum checksum : readChecksums) {
                        ApkChecksum apkChecksum = map.get(Integer.valueOf(checksum.getType()));
                        if (apkChecksum != null && !Arrays.equals(apkChecksum.getValue(), checksum.getValue())) {
                            throw new InvalidParameterException("System digest " + checksum.getType() + " mismatch, can't bind installer-provided digests to the APK.");
                        }
                    }
                    for (Checksum checksum2 : readChecksums) {
                        if (isRequired(checksum2.getType(), i, map)) {
                            map.put(Integer.valueOf(checksum2.getType()), new ApkChecksum(str, checksum2, str2, byteArray));
                        }
                    }
                    return;
                }
                Slog.e(TAG, "Can't obtain certificates.");
            } catch (IOException e) {
                Slog.e(TAG, "Error reading .digests or .signature", e);
            } catch (InvalidParameterException | NoSuchAlgorithmException | SignatureException e2) {
                Slog.e(TAG, "Error validating digests. Invalid digests will be removed", e2);
                try {
                    Files.deleteIfExists(findDigestsForFile.toPath());
                    if (findSignatureForDigests != null) {
                        Files.deleteIfExists(findSignatureForDigests.toPath());
                    }
                } catch (IOException unused) {
                }
            } catch (CertificateEncodingException e3) {
                Slog.e(TAG, "Error encoding trustedInstallers", e3);
            }
        }
    }

    private static boolean needToWait(File file, int i, Map<Integer, ApkChecksum> map, Injector injector) throws IOException {
        if (!isRequired(1, i, map) && !isRequired(2, i, map) && !isRequired(4, i, map) && !isRequired(8, i, map) && !isRequired(16, i, map) && !isRequired(32, i, map) && !isRequired(64, i, map)) {
            return false;
        }
        String absolutePath = file.getAbsolutePath();
        if (!IncrementalManager.isIncrementalPath(absolutePath)) {
            return false;
        }
        IncrementalManager incrementalManager = injector.getIncrementalManager();
        if (incrementalManager == null) {
            Slog.e(TAG, "IncrementalManager is missing.");
            return false;
        }
        if (incrementalManager.openStorage(absolutePath) == null) {
            Slog.e(TAG, "IncrementalStorage is missing for a path on IncFs: " + absolutePath);
            return false;
        }
        return !r4.isFileFullyLoaded(absolutePath);
    }

    private static void getRequiredApkChecksums(String str, File file, int i, Map<Integer, ApkChecksum> map) {
        String absolutePath = file.getAbsolutePath();
        if (isRequired(1, i, map)) {
            try {
                map.put(1, new ApkChecksum(str, 1, verityHashForFile(file, VerityBuilder.generateFsVerityRootHash(absolutePath, (byte[]) null, new ByteBufferFactory() { // from class: com.android.server.pm.ApkChecksums.1
                    public ByteBuffer create(int i2) {
                        return ByteBuffer.allocate(i2);
                    }
                }))));
            } catch (IOException | DigestException | NoSuchAlgorithmException e) {
                Slog.e(TAG, "Error calculating WHOLE_MERKLE_ROOT_4K_SHA256", e);
            }
        }
        calculateChecksumIfRequested(map, str, file, i, 2);
        calculateChecksumIfRequested(map, str, file, i, 4);
        calculateChecksumIfRequested(map, str, file, i, 8);
        calculateChecksumIfRequested(map, str, file, i, 16);
        calculatePartialChecksumsIfRequested(map, str, file, i);
    }

    private static boolean isRequired(int i, int i2, Map<Integer, ApkChecksum> map) {
        return ((i2 & i) == 0 || map.containsKey(Integer.valueOf(i))) ? false : true;
    }

    private static Set<Signature> convertToSet(Certificate[] certificateArr) throws CertificateEncodingException {
        if (certificateArr == null) {
            return null;
        }
        ArraySet arraySet = new ArraySet(certificateArr.length);
        for (Certificate certificate : certificateArr) {
            arraySet.add(new Signature(certificate.getEncoded()));
        }
        return arraySet;
    }

    private static Signature isTrusted(Signature[] signatureArr, Set<Signature> set) {
        if (signatureArr == null) {
            return null;
        }
        for (Signature signature : signatureArr) {
            if (set.contains(signature)) {
                return signature;
            }
        }
        return null;
    }

    private static boolean containsFile(File file, String str) {
        if (file == null) {
            return false;
        }
        return FileUtils.contains(file.getAbsolutePath(), str);
    }

    private static ApkChecksum extractHashFromFS(String str, String str2) {
        byte[] fsverityDigest;
        if (!containsFile(Environment.getProductDirectory(), str2) && (fsverityDigest = VerityUtils.getFsverityDigest(str2)) != null) {
            return new ApkChecksum(str, 1, fsverityDigest);
        }
        try {
            byte[] bArr = (byte[]) ApkSignatureSchemeV4Verifier.extractCertificates(str2).contentDigests.getOrDefault(3, null);
            if (bArr != null) {
                return new ApkChecksum(str, 1, verityHashForFile(new File(str2), bArr));
            }
        } catch (SecurityException e) {
            Slog.e(TAG, "V4 signature error", e);
        } catch (SignatureNotFoundException unused) {
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] verityHashForFile(File file, byte[] bArr) {
        try {
            ByteBuffer allocate = ByteBuffer.allocate(256);
            allocate.order(ByteOrder.LITTLE_ENDIAN);
            allocate.put((byte) 1);
            allocate.put((byte) 1);
            allocate.put((byte) 12);
            allocate.put((byte) 0);
            allocate.putInt(0);
            allocate.putLong(file.length());
            allocate.put(bArr);
            for (int i = 0; i < 208; i++) {
                allocate.put((byte) 0);
            }
            allocate.flip();
            MessageDigest messageDigest = MessageDigest.getInstance(ALGO_SHA256);
            messageDigest.update(allocate);
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            Slog.e(TAG, "Device does not support MessageDigest algorithm", e);
            return null;
        }
    }

    private static Map<Integer, ApkChecksum> extractHashFromV2V3Signature(String str, String str2, int i) {
        Map map;
        byte[] bArr;
        byte[] bArr2;
        ParseResult verifySignaturesInternal = ApkSignatureVerifier.verifySignaturesInternal(ParseTypeImpl.forDefaultParsing(), str2, 2, false);
        if (verifySignaturesInternal.isError()) {
            if (!(verifySignaturesInternal.getException() instanceof SignatureNotFoundException)) {
                Slog.e(TAG, "Signature verification error", verifySignaturesInternal.getException());
            }
            map = null;
        } else {
            map = ((ApkSignatureVerifier.SigningDetailsWithDigests) verifySignaturesInternal.getResult()).contentDigests;
        }
        if (map == null) {
            return null;
        }
        ArrayMap arrayMap = new ArrayMap();
        if ((i & 32) != 0 && (bArr2 = (byte[]) map.getOrDefault(1, null)) != null) {
            arrayMap.put(32, new ApkChecksum(str, 32, bArr2));
        }
        if ((i & 64) != 0 && (bArr = (byte[]) map.getOrDefault(2, null)) != null) {
            arrayMap.put(64, new ApkChecksum(str, 64, bArr));
        }
        return arrayMap;
    }

    private static String getMessageDigestAlgoForChecksumKind(int i) throws NoSuchAlgorithmException {
        if (i == 2) {
            return ALGO_MD5;
        }
        if (i == 4) {
            return ALGO_SHA1;
        }
        if (i == 8) {
            return ALGO_SHA256;
        }
        if (i == 16) {
            return ALGO_SHA512;
        }
        throw new NoSuchAlgorithmException("Invalid checksum type: " + i);
    }

    private static void calculateChecksumIfRequested(Map<Integer, ApkChecksum> map, String str, File file, int i, int i2) {
        byte[] apkChecksum;
        if ((i & i2) == 0 || map.containsKey(Integer.valueOf(i2)) || (apkChecksum = getApkChecksum(file, i2)) == null) {
            return;
        }
        map.put(Integer.valueOf(i2), new ApkChecksum(str, i2, apkChecksum));
    }

    private static byte[] getApkChecksum(File file, int i) {
        int max = (int) Math.max(4096L, Math.min(131072L, file.length()));
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[max];
                MessageDigest messageDigest = MessageDigest.getInstance(getMessageDigestAlgoForChecksumKind(i));
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read != -1) {
                        messageDigest.update(bArr, 0, read);
                    } else {
                        byte[] digest = messageDigest.digest();
                        fileInputStream.close();
                        return digest;
                    }
                }
            } catch (Throwable th) {
                try {
                    fileInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException e) {
            Slog.e(TAG, "Error reading " + file.getAbsolutePath() + " to compute hash.", e);
            return null;
        } catch (NoSuchAlgorithmException e2) {
            Slog.e(TAG, "Device does not support MessageDigest algorithm", e2);
            return null;
        }
    }

    private static int[] getContentDigestAlgos(boolean z, boolean z2) {
        if (z && z2) {
            return new int[]{1, 2};
        }
        if (z) {
            return new int[]{1};
        }
        return new int[]{2};
    }

    private static void calculatePartialChecksumsIfRequested(Map<Integer, ApkChecksum> map, String str, File file, int i) {
        SignatureInfo signatureInfo;
        boolean z = ((i & 32) == 0 || map.containsKey(32)) ? false : true;
        boolean z2 = ((i & 64) == 0 || map.containsKey(64)) ? false : true;
        if (z || z2) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                try {
                    try {
                        try {
                            signatureInfo = ApkSignatureSchemeV3Verifier.findSignature(randomAccessFile);
                        } catch (SignatureNotFoundException unused) {
                            signatureInfo = null;
                        }
                    } catch (Throwable th) {
                        try {
                            randomAccessFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (SignatureNotFoundException unused2) {
                    signatureInfo = ApkSignatureSchemeV2Verifier.findSignature(randomAccessFile);
                }
                if (signatureInfo == null) {
                    Slog.e(TAG, "V2/V3 signatures not found in " + file.getAbsolutePath());
                    randomAccessFile.close();
                    return;
                }
                int[] contentDigestAlgos = getContentDigestAlgos(z, z2);
                byte[][] computeContentDigestsPer1MbChunk = ApkSigningBlockUtils.computeContentDigestsPer1MbChunk(contentDigestAlgos, randomAccessFile.getFD(), signatureInfo);
                int length = contentDigestAlgos.length;
                for (int i2 = 0; i2 < length; i2++) {
                    int checksumKindForContentDigestAlgo = getChecksumKindForContentDigestAlgo(contentDigestAlgos[i2]);
                    if (checksumKindForContentDigestAlgo != -1) {
                        map.put(Integer.valueOf(checksumKindForContentDigestAlgo), new ApkChecksum(str, checksumKindForContentDigestAlgo, computeContentDigestsPer1MbChunk[i2]));
                    }
                }
                randomAccessFile.close();
            } catch (IOException | DigestException e) {
                Slog.e(TAG, "Error computing hash.", e);
            }
        }
    }
}
