package com.android.server.pm;

import android.text.TextUtils;
import com.android.internal.util.ArrayUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class KnownPackages {
    public static final int LAST_KNOWN_PACKAGE = 19;
    public static final int PACKAGE_AMBIENT_CONTEXT_DETECTION = 18;
    public static final int PACKAGE_APP_PREDICTOR = 12;
    public static final int PACKAGE_BROWSER = 5;
    public static final int PACKAGE_COMPANION = 15;
    public static final int PACKAGE_CONFIGURATOR = 10;
    public static final int PACKAGE_DOCUMENTER = 9;
    public static final int PACKAGE_INCIDENT_REPORT_APPROVER = 11;
    public static final int PACKAGE_INSTALLER = 2;
    public static final int PACKAGE_OVERLAY_CONFIG_SIGNATURE = 13;
    public static final int PACKAGE_PERMISSION_CONTROLLER = 7;
    public static final int PACKAGE_RECENTS = 17;
    public static final int PACKAGE_RETAIL_DEMO = 16;
    public static final int PACKAGE_SETUP_WIZARD = 1;
    public static final int PACKAGE_SYSTEM = 0;
    public static final int PACKAGE_SYSTEM_TEXT_CLASSIFIER = 6;
    public static final int PACKAGE_UNINSTALLER = 3;
    public static final int PACKAGE_VERIFIER = 4;
    public static final int PACKAGE_WEARABLE_SENSING = 19;
    public static final int PACKAGE_WELLBEING = 8;
    public static final int PACKAGE_WIFI = 14;
    private final String mAmbientContextDetectionPackage;
    private final String mAppPredictionServicePackage;
    private final String mCompanionPackage;
    private final String mConfiguratorPackage;
    private final DefaultAppProvider mDefaultAppProvider;
    private final String mDefaultTextClassifierPackage;
    private final String mIncidentReportApproverPackage;
    private final String mOverlayConfigSignaturePackage;
    private final String mRecentsPackage;
    private final String mRequiredInstallerPackage;
    private final String mRequiredPermissionControllerPackage;
    private final String mRequiredUninstallerPackage;
    private final String[] mRequiredVerifierPackages;
    private final String mRetailDemoPackage;
    private final String mSetupWizardPackage;
    private final String mSystemTextClassifierPackageName;
    private final String mWearableSensingPackage;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface KnownPackage {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String knownPackageToString(int i) {
        switch (i) {
            case 0:
                return "System";
            case 1:
                return "Setup Wizard";
            case 2:
                return "Installer";
            case 3:
                return "Uninstaller";
            case 4:
                return "Verifier";
            case 5:
                return "Browser";
            case 6:
                return "System Text Classifier";
            case 7:
                return "Permission Controller";
            case 8:
                return "Wellbeing";
            case 9:
                return "Documenter";
            case 10:
                return "Configurator";
            case 11:
                return "Incident Report Approver";
            case 12:
                return "App Predictor";
            case 13:
                return "Overlay Config Signature";
            case 14:
                return "Wi-Fi";
            case 15:
                return "Companion";
            case 16:
                return "Retail Demo";
            case 17:
                return "Recents";
            case 18:
                return "Ambient Context Detection";
            case 19:
                return "Wearable sensing";
            default:
                return "Unknown";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KnownPackages(DefaultAppProvider defaultAppProvider, String str, String str2, String str3, String[] strArr, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15) {
        this.mDefaultAppProvider = defaultAppProvider;
        this.mRequiredInstallerPackage = str;
        this.mRequiredUninstallerPackage = str2;
        this.mSetupWizardPackage = str3;
        this.mRequiredVerifierPackages = strArr;
        this.mDefaultTextClassifierPackage = str4;
        this.mSystemTextClassifierPackageName = str5;
        this.mRequiredPermissionControllerPackage = str6;
        this.mConfiguratorPackage = str7;
        this.mIncidentReportApproverPackage = str8;
        this.mAmbientContextDetectionPackage = str9;
        this.mWearableSensingPackage = str10;
        this.mAppPredictionServicePackage = str11;
        this.mCompanionPackage = str12;
        this.mRetailDemoPackage = str13;
        this.mOverlayConfigSignaturePackage = str14;
        this.mRecentsPackage = str15;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String[] getKnownPackageNames(Computer computer, int i, int i2) {
        switch (i) {
            case 0:
                return new String[]{PackageManagerService.PLATFORM_PACKAGE_NAME};
            case 1:
                return computer.filterOnlySystemPackages(this.mSetupWizardPackage);
            case 2:
                return computer.filterOnlySystemPackages(this.mRequiredInstallerPackage);
            case 3:
                return computer.filterOnlySystemPackages(this.mRequiredUninstallerPackage);
            case 4:
                return computer.filterOnlySystemPackages(this.mRequiredVerifierPackages);
            case 5:
                return new String[]{this.mDefaultAppProvider.getDefaultBrowser(i2)};
            case 6:
                return computer.filterOnlySystemPackages(this.mDefaultTextClassifierPackage, this.mSystemTextClassifierPackageName);
            case 7:
                return computer.filterOnlySystemPackages(this.mRequiredPermissionControllerPackage);
            case 8:
            case 9:
            case 14:
            default:
                return (String[]) ArrayUtils.emptyArray(String.class);
            case 10:
                return computer.filterOnlySystemPackages(this.mConfiguratorPackage);
            case 11:
                return computer.filterOnlySystemPackages(this.mIncidentReportApproverPackage);
            case 12:
                return computer.filterOnlySystemPackages(this.mAppPredictionServicePackage);
            case 13:
                return computer.filterOnlySystemPackages(this.mOverlayConfigSignaturePackage);
            case 15:
                return computer.filterOnlySystemPackages(this.mCompanionPackage);
            case 16:
                if (TextUtils.isEmpty(this.mRetailDemoPackage)) {
                    return (String[]) ArrayUtils.emptyArray(String.class);
                }
                return new String[]{this.mRetailDemoPackage};
            case 17:
                return computer.filterOnlySystemPackages(this.mRecentsPackage);
            case 18:
                return computer.filterOnlySystemPackages(this.mAmbientContextDetectionPackage);
            case 19:
                return computer.filterOnlySystemPackages(this.mWearableSensingPackage);
        }
    }
}
