package com.android.server.integrity;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.integrity.AppInstallMetadata;
import android.content.integrity.IAppIntegrityManager;
import android.content.integrity.IntegrityUtils;
import android.content.integrity.Rule;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ParceledListSlice;
import android.content.pm.Signature;
import android.content.pm.SigningDetails;
import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.pm.parsing.result.ParseTypeImpl;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Pair;
import android.util.Slog;
import android.util.apk.SourceStampVerificationResult;
import android.util.apk.SourceStampVerifier;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.LocalServices;
import com.android.server.integrity.AppIntegrityManagerServiceImpl;
import com.android.server.integrity.engine.RuleEvaluationEngine;
import com.android.server.integrity.model.IntegrityCheckResult;
import com.android.server.integrity.model.RuleMetadata;
import com.android.server.pm.PackageManagerServiceUtils;
import com.android.server.pm.parsing.PackageParser2;
import com.android.server.pm.parsing.pkg.ParsedPackage;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.parsing.ParsingPackageUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AppIntegrityManagerServiceImpl extends IAppIntegrityManager.Stub {
    public static final String ADB_INSTALLER = "adb";
    private static final String ALLOWED_INSTALLERS_METADATA_NAME = "allowed-installers";
    private static final String ALLOWED_INSTALLER_DELIMITER = ",";
    private static final String BASE_APK_FILE = "base.apk";
    public static final boolean DEBUG_INTEGRITY_COMPONENT = false;
    private static final String INSTALLER_PACKAGE_CERT_DELIMITER = "\\|";
    private static final Set<String> PACKAGE_INSTALLER = new HashSet(Arrays.asList("com.google.android.packageinstaller", "com.android.packageinstaller"));
    private static final String PACKAGE_MIME_TYPE = "application/vnd.android.package-archive";
    private static final String TAG = "AppIntegrityManagerServiceImpl";
    private static final String UNKNOWN_INSTALLER = "";
    private final Context mContext;
    private final RuleEvaluationEngine mEvaluationEngine;
    private final Handler mHandler;
    private final IntegrityFileManager mIntegrityFileManager;
    private final PackageManagerInternal mPackageManagerInternal;
    private final Supplier<PackageParser2> mParserSupplier;

    public static AppIntegrityManagerServiceImpl create(Context context) {
        HandlerThread handlerThread = new HandlerThread("AppIntegrityManagerServiceHandler");
        handlerThread.start();
        return new AppIntegrityManagerServiceImpl(context, (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class), new Supplier() { // from class: com.android.server.integrity.AppIntegrityManagerServiceImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return PackageParser2.forParsingFileWithDefaults();
            }
        }, RuleEvaluationEngine.getRuleEvaluationEngine(), IntegrityFileManager.getInstance(), handlerThread.getThreadHandler());
    }

    @VisibleForTesting
    AppIntegrityManagerServiceImpl(Context context, PackageManagerInternal packageManagerInternal, Supplier<PackageParser2> supplier, RuleEvaluationEngine ruleEvaluationEngine, IntegrityFileManager integrityFileManager, Handler handler) {
        this.mContext = context;
        this.mPackageManagerInternal = packageManagerInternal;
        this.mParserSupplier = supplier;
        this.mEvaluationEngine = ruleEvaluationEngine;
        this.mIntegrityFileManager = integrityFileManager;
        this.mHandler = handler;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_NEEDS_INTEGRITY_VERIFICATION");
        try {
            intentFilter.addDataType(PACKAGE_MIME_TYPE);
            context.registerReceiver(new AnonymousClass1(), intentFilter, null, handler);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Mime type malformed: should never happen.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.integrity.AppIntegrityManagerServiceImpl$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            if ("android.intent.action.PACKAGE_NEEDS_INTEGRITY_VERIFICATION".equals(intent.getAction())) {
                AppIntegrityManagerServiceImpl.this.mHandler.post(new Runnable() { // from class: com.android.server.integrity.AppIntegrityManagerServiceImpl$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AppIntegrityManagerServiceImpl.AnonymousClass1.this.lambda$onReceive$0(intent);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(Intent intent) {
            AppIntegrityManagerServiceImpl.this.handleIntegrityVerification(intent);
        }
    }

    public void updateRuleSet(final String str, final ParceledListSlice<Rule> parceledListSlice, final IntentSender intentSender) {
        final String callerPackageNameOrThrow = getCallerPackageNameOrThrow(Binder.getCallingUid());
        this.mHandler.post(new Runnable() { // from class: com.android.server.integrity.AppIntegrityManagerServiceImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AppIntegrityManagerServiceImpl.this.lambda$updateRuleSet$0(str, callerPackageNameOrThrow, parceledListSlice, intentSender);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$updateRuleSet$0(String str, String str2, ParceledListSlice parceledListSlice, IntentSender intentSender) {
        boolean z;
        try {
            this.mIntegrityFileManager.writeRules(str, str2, parceledListSlice.getList());
            z = 1;
        } catch (Exception e) {
            Slog.e(TAG, "Error writing rules.", e);
            z = 0;
        }
        FrameworkStatsLog.write(248, z, str2, str);
        Intent intent = new Intent();
        intent.putExtra("android.content.integrity.extra.STATUS", !z);
        try {
            intentSender.sendIntent(this.mContext, 0, intent, null, null);
        } catch (Exception e2) {
            Slog.e(TAG, "Error sending status feedback.", e2);
        }
    }

    public String getCurrentRuleSetVersion() {
        getCallerPackageNameOrThrow(Binder.getCallingUid());
        RuleMetadata readMetadata = this.mIntegrityFileManager.readMetadata();
        return (readMetadata == null || readMetadata.getVersion() == null) ? UNKNOWN_INSTALLER : readMetadata.getVersion();
    }

    public String getCurrentRuleSetProvider() {
        getCallerPackageNameOrThrow(Binder.getCallingUid());
        RuleMetadata readMetadata = this.mIntegrityFileManager.readMetadata();
        return (readMetadata == null || readMetadata.getRuleProvider() == null) ? UNKNOWN_INSTALLER : readMetadata.getRuleProvider();
    }

    public ParceledListSlice<Rule> getCurrentRules() {
        List<Rule> emptyList = Collections.emptyList();
        try {
            emptyList = this.mIntegrityFileManager.readRules(null);
        } catch (Exception e) {
            Slog.e(TAG, "Error getting current rules", e);
        }
        return new ParceledListSlice<>(emptyList);
    }

    public List<String> getWhitelistedRuleProviders() {
        return getAllowedRuleProviderSystemApps();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleIntegrityVerification(Intent intent) {
        int intExtra = intent.getIntExtra("android.content.pm.extra.VERIFICATION_ID", -1);
        try {
            String installerPackageName = getInstallerPackageName(intent);
            if (!integrityCheckIncludesRuleProvider() && isRuleProvider(installerPackageName)) {
                this.mPackageManagerInternal.setIntegrityVerificationResult(intExtra, 1);
                return;
            }
            String stringExtra = intent.getStringExtra("android.intent.extra.PACKAGE_NAME");
            Pair<SigningDetails, Bundle> packageSigningAndMetadata = getPackageSigningAndMetadata(intent.getData());
            if (packageSigningAndMetadata == null) {
                Slog.w(TAG, "Cannot parse package " + stringExtra);
                this.mPackageManagerInternal.setIntegrityVerificationResult(intExtra, 1);
                return;
            }
            SigningDetails signingDetails = (SigningDetails) packageSigningAndMetadata.first;
            List<String> certificateFingerprint = getCertificateFingerprint(stringExtra, signingDetails);
            List<String> certificateLineage = getCertificateLineage(stringExtra, signingDetails);
            List<String> installerCertificateFingerprint = getInstallerCertificateFingerprint(installerPackageName);
            AppInstallMetadata.Builder builder = new AppInstallMetadata.Builder();
            builder.setPackageName(getPackageNameNormalized(stringExtra));
            builder.setAppCertificates(certificateFingerprint);
            builder.setAppCertificateLineage(certificateLineage);
            builder.setVersionCode(intent.getLongExtra("android.intent.extra.LONG_VERSION_CODE", -1L));
            builder.setInstallerName(getPackageNameNormalized(installerPackageName));
            builder.setInstallerCertificates(installerCertificateFingerprint);
            builder.setIsPreInstalled(isSystemApp(stringExtra));
            builder.setAllowedInstallersAndCert(getAllowedInstallers((Bundle) packageSigningAndMetadata.second));
            extractSourceStamp(intent.getData(), builder);
            AppInstallMetadata build = builder.build();
            IntegrityCheckResult evaluate = this.mEvaluationEngine.evaluate(build);
            if (!evaluate.getMatchedRules().isEmpty()) {
                Slog.i(TAG, String.format("Integrity check of %s result: %s due to %s", stringExtra, evaluate.getEffect(), evaluate.getMatchedRules()));
            }
            FrameworkStatsLog.write(247, stringExtra, certificateFingerprint.toString(), build.getVersionCode(), installerPackageName, evaluate.getLoggingResponse(), evaluate.isCausedByAppCertRule(), evaluate.isCausedByInstallerRule());
            this.mPackageManagerInternal.setIntegrityVerificationResult(intExtra, evaluate.getEffect() == IntegrityCheckResult.Effect.ALLOW ? 1 : 0);
        } catch (IllegalArgumentException e) {
            Slog.e(TAG, "Invalid input to integrity verification", e);
            this.mPackageManagerInternal.setIntegrityVerificationResult(intExtra, 0);
        } catch (Exception e2) {
            Slog.e(TAG, "Error handling integrity verification", e2);
            this.mPackageManagerInternal.setIntegrityVerificationResult(intExtra, 1);
        }
    }

    private String getInstallerPackageName(Intent intent) {
        String stringExtra = intent.getStringExtra("android.content.pm.extra.VERIFICATION_INSTALLER_PACKAGE");
        if (PackageManagerServiceUtils.isInstalledByAdb(stringExtra)) {
            return ADB_INSTALLER;
        }
        int intExtra = intent.getIntExtra("android.content.pm.extra.VERIFICATION_INSTALLER_UID", -1);
        if (intExtra < 0) {
            Slog.e(TAG, "Installer cannot be determined: installer: " + stringExtra + " installer UID: " + intExtra);
            return UNKNOWN_INSTALLER;
        }
        if (!getPackageListForUid(intExtra).contains(stringExtra)) {
            return UNKNOWN_INSTALLER;
        }
        if (!PACKAGE_INSTALLER.contains(stringExtra)) {
            return stringExtra;
        }
        int intExtra2 = intent.getIntExtra("android.intent.extra.ORIGINATING_UID", -1);
        if (intExtra2 < 0) {
            Slog.e(TAG, "Installer is package installer but originating UID not found.");
            return UNKNOWN_INSTALLER;
        }
        List<String> packageListForUid = getPackageListForUid(intExtra2);
        if (packageListForUid.isEmpty()) {
            Slog.e(TAG, "No package found associated with originating UID " + intExtra2);
            return UNKNOWN_INSTALLER;
        }
        return packageListForUid.get(0);
    }

    private String getPackageNameNormalized(String str) {
        if (str.length() <= 32) {
            return str;
        }
        try {
            return IntegrityUtils.getHexDigest(MessageDigest.getInstance("SHA-256").digest(str.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private List<String> getInstallerCertificateFingerprint(String str) {
        if (str.equals(ADB_INSTALLER) || str.equals(UNKNOWN_INSTALLER)) {
            return Collections.emptyList();
        }
        AndroidPackage androidPackage = this.mPackageManagerInternal.getPackage(str);
        if (androidPackage == null) {
            Slog.w(TAG, "Installer package " + str + " not found.");
            return Collections.emptyList();
        }
        return getCertificateFingerprint(androidPackage.getPackageName(), androidPackage.getSigningDetails());
    }

    private List<String> getCertificateFingerprint(String str, SigningDetails signingDetails) {
        ArrayList arrayList = new ArrayList();
        for (Signature signature : getSignatures(str, signingDetails)) {
            arrayList.add(getFingerprint(signature));
        }
        return arrayList;
    }

    private List<String> getCertificateLineage(String str, SigningDetails signingDetails) {
        ArrayList arrayList = new ArrayList();
        for (Signature signature : getSignatureLineage(str, signingDetails)) {
            arrayList.add(getFingerprint(signature));
        }
        return arrayList;
    }

    private Map<String, String> getAllowedInstallers(Bundle bundle) {
        String string;
        HashMap hashMap = new HashMap();
        if (bundle != null && (string = bundle.getString(ALLOWED_INSTALLERS_METADATA_NAME)) != null) {
            for (String str : string.split(ALLOWED_INSTALLER_DELIMITER)) {
                String[] split = str.split(INSTALLER_PACKAGE_CERT_DELIMITER);
                if (split.length == 2) {
                    hashMap.put(getPackageNameNormalized(split[0]), split[1]);
                } else if (split.length == 1) {
                    hashMap.put(getPackageNameNormalized(split[0]), UNKNOWN_INSTALLER);
                }
            }
        }
        return hashMap;
    }

    private void extractSourceStamp(Uri uri, AppInstallMetadata.Builder builder) {
        SourceStampVerificationResult verify;
        File installationPath = getInstallationPath(uri);
        if (installationPath == null) {
            throw new IllegalArgumentException("Installation path is null, package not found");
        }
        if (installationPath.isDirectory()) {
            try {
                Stream<Path> list = Files.list(installationPath.toPath());
                try {
                    verify = SourceStampVerifier.verify((List) list.map(new Function() { // from class: com.android.server.integrity.AppIntegrityManagerServiceImpl$$ExternalSyntheticLambda2
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            String lambda$extractSourceStamp$1;
                            lambda$extractSourceStamp$1 = AppIntegrityManagerServiceImpl.lambda$extractSourceStamp$1((Path) obj);
                            return lambda$extractSourceStamp$1;
                        }
                    }).collect(Collectors.toList()));
                    list.close();
                } finally {
                }
            } catch (IOException unused) {
                throw new IllegalArgumentException("Could not read APK directory");
            }
        } else {
            verify = SourceStampVerifier.verify(installationPath.getAbsolutePath());
        }
        builder.setIsStampPresent(verify.isPresent());
        builder.setIsStampVerified(verify.isVerified());
        builder.setIsStampTrusted(verify.isVerified());
        if (verify.isVerified()) {
            try {
                builder.setStampCertificateHash(IntegrityUtils.getHexDigest(MessageDigest.getInstance("SHA-256").digest(((X509Certificate) verify.getCertificate()).getEncoded())));
            } catch (NoSuchAlgorithmException | CertificateEncodingException e) {
                throw new IllegalArgumentException("Error computing source stamp certificate digest", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$extractSourceStamp$1(Path path) {
        return path.toAbsolutePath().toString();
    }

    private static Signature[] getSignatures(String str, SigningDetails signingDetails) {
        Signature[] signatures = signingDetails.getSignatures();
        if (signatures != null && signatures.length >= 1) {
            return signatures;
        }
        throw new IllegalArgumentException("Package signature not found in " + str);
    }

    private static Signature[] getSignatureLineage(String str, SigningDetails signingDetails) {
        Signature[] signatures = getSignatures(str, signingDetails);
        Signature[] pastSigningCertificates = signingDetails.getPastSigningCertificates();
        if (signatures.length != 1 || ArrayUtils.isEmpty(pastSigningCertificates)) {
            return signatures;
        }
        Signature[] signatureArr = new Signature[signatures.length + pastSigningCertificates.length];
        int i = 0;
        while (i < signatures.length) {
            signatureArr[i] = signatures[i];
            i++;
        }
        for (Signature signature : pastSigningCertificates) {
            signatureArr[i] = signature;
            i++;
        }
        return signatureArr;
    }

    private static String getFingerprint(Signature signature) {
        X509Certificate x509Certificate;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(signature.toByteArray());
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
            if (certificateFactory != null) {
                try {
                    x509Certificate = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
                } catch (CertificateException e) {
                    throw new RuntimeException("Error getting X509Certificate", e);
                }
            } else {
                x509Certificate = null;
            }
            if (x509Certificate == null) {
                throw new RuntimeException("X509 Certificate not found");
            }
            try {
                return IntegrityUtils.getHexDigest(MessageDigest.getInstance("SHA-256").digest(x509Certificate.getEncoded()));
            } catch (NoSuchAlgorithmException | CertificateEncodingException e2) {
                throw new IllegalArgumentException("Error error computing fingerprint", e2);
            }
        } catch (CertificateException e3) {
            throw new RuntimeException("Error getting CertificateFactory", e3);
        }
    }

    private Pair<SigningDetails, Bundle> getPackageSigningAndMetadata(Uri uri) {
        File installationPath = getInstallationPath(uri);
        if (installationPath == null) {
            throw new IllegalArgumentException("Installation path is null, package not found");
        }
        try {
            PackageParser2 packageParser2 = this.mParserSupplier.get();
            try {
                ParsedPackage parsePackage = packageParser2.parsePackage(installationPath, 0, false);
                ParseResult<SigningDetails> signingDetails = ParsingPackageUtils.getSigningDetails((ParseInput) ParseTypeImpl.forDefaultParsing(), parsePackage, true);
                if (signingDetails.isError()) {
                    Slog.w(TAG, signingDetails.getErrorMessage(), signingDetails.getException());
                    packageParser2.close();
                    return null;
                }
                Pair<SigningDetails, Bundle> create = Pair.create((SigningDetails) signingDetails.getResult(), parsePackage.getMetaData());
                packageParser2.close();
                return create;
            } finally {
            }
        } catch (Exception e) {
            Slog.w(TAG, "Exception reading " + uri, e);
            return null;
        }
    }

    private PackageInfo getMultiApkInfo(File file) {
        PackageInfo packageArchiveInfo = this.mContext.getPackageManager().getPackageArchiveInfo(new File(file, BASE_APK_FILE).getAbsolutePath(), 134217856);
        if (packageArchiveInfo == null) {
            File[] listFiles = file.listFiles();
            int length = listFiles.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                File file2 = listFiles[i];
                if (!file2.isDirectory()) {
                    try {
                        packageArchiveInfo = this.mContext.getPackageManager().getPackageArchiveInfo(file2.getAbsolutePath(), 134217856);
                    } catch (Exception e) {
                        Slog.w(TAG, "Exception reading " + file2, e);
                    }
                    if (packageArchiveInfo != null) {
                        Slog.i(TAG, "Found package info from " + file2);
                        break;
                    }
                }
                i++;
            }
        }
        if (packageArchiveInfo != null) {
            return packageArchiveInfo;
        }
        throw new IllegalArgumentException("Base package info cannot be found from installation directory");
    }

    private File getInstallationPath(Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Null data uri");
        }
        if (!"file".equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("Unsupported scheme for " + uri);
        }
        File file = new File(uri.getPath());
        if (!file.exists()) {
            throw new IllegalArgumentException("Cannot find file for " + uri);
        }
        if (file.canRead()) {
            return file;
        }
        throw new IllegalArgumentException("Cannot read file for " + uri);
    }

    private String getCallerPackageNameOrThrow(int i) {
        String callingRulePusherPackageName = getCallingRulePusherPackageName(i);
        if (callingRulePusherPackageName != null) {
            return callingRulePusherPackageName;
        }
        throw new SecurityException("Only system packages specified in config_integrityRuleProviderPackages are allowed to call this method.");
    }

    private String getCallingRulePusherPackageName(int i) {
        List<String> allowedRuleProviderSystemApps = getAllowedRuleProviderSystemApps();
        List<String> packageListForUid = getPackageListForUid(i);
        ArrayList arrayList = new ArrayList();
        for (String str : packageListForUid) {
            if (allowedRuleProviderSystemApps.contains(str)) {
                arrayList.add(str);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return (String) arrayList.get(0);
    }

    private boolean isRuleProvider(String str) {
        Iterator<String> it = getAllowedRuleProviderSystemApps().iterator();
        while (it.hasNext()) {
            if (it.next().matches(str)) {
                return true;
            }
        }
        return false;
    }

    private List<String> getAllowedRuleProviderSystemApps() {
        List<String> asList = Arrays.asList(this.mContext.getResources().getStringArray(R.array.config_tether_usb_regexs));
        ArrayList arrayList = new ArrayList();
        for (String str : asList) {
            if (isSystemApp(str)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    private boolean isSystemApp(String str) {
        try {
            ApplicationInfo applicationInfo = this.mContext.getPackageManager().getPackageInfo(str, 0).applicationInfo;
            if (applicationInfo != null) {
                return applicationInfo.isSystemApp();
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private boolean integrityCheckIncludesRuleProvider() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "verify_integrity_for_rule_provider", 0) == 1;
    }

    private List<String> getPackageListForUid(int i) {
        try {
            return Arrays.asList(this.mContext.getPackageManager().getPackagesForUid(i));
        } catch (NullPointerException unused) {
            Slog.w(TAG, String.format("No packages were found for uid: %d", Integer.valueOf(i)));
            return List.of();
        }
    }
}
