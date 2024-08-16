package com.android.server.om;

import android.content.om.OverlayInfo;
import android.content.om.OverlayableInfo;
import android.os.FabricatedOverlayInfo;
import android.os.FabricatedOverlayInternal;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Slog;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class IdmapManager {
    static final int IDMAP_IS_MODIFIED = 2;
    static final int IDMAP_IS_VERIFIED = 1;
    static final int IDMAP_NOT_EXIST = 0;
    private static final boolean VENDOR_IS_Q_OR_LATER;
    private final String mConfigSignaturePackage;
    private final IdmapDaemon mIdmapDaemon;
    private final PackageManagerHelper mPackageManager;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface IdmapStatus {
    }

    static {
        boolean z = true;
        try {
            if (Integer.parseInt(SystemProperties.get("ro.vndk.version", "29")) < 29) {
                z = false;
            }
        } catch (NumberFormatException unused) {
        }
        VENDOR_IS_Q_OR_LATER = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IdmapManager(IdmapDaemon idmapDaemon, PackageManagerHelper packageManagerHelper) {
        this.mPackageManager = packageManagerHelper;
        this.mIdmapDaemon = idmapDaemon;
        this.mConfigSignaturePackage = packageManagerHelper.getConfigSignaturePackage();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int createIdmap(AndroidPackage androidPackage, PackageState packageState, AndroidPackage androidPackage2, String str, String str2, int i) {
        if (OverlayManagerService.DEBUG) {
            Slog.d("OverlayManager", "create idmap for " + androidPackage.getPackageName() + " and " + androidPackage2.getPackageName());
        }
        String path = androidPackage.getSplits().get(0).getPath();
        try {
            int calculateFulfilledPolicies = calculateFulfilledPolicies(androidPackage, packageState, androidPackage2, i);
            boolean enforceOverlayable = enforceOverlayable(packageState, androidPackage2);
            if (this.mIdmapDaemon.verifyIdmap(path, str, str2, calculateFulfilledPolicies, enforceOverlayable, i)) {
                return 1;
            }
            return this.mIdmapDaemon.createIdmap(path, str, str2, calculateFulfilledPolicies, enforceOverlayable, i) != null ? 3 : 0;
        } catch (Exception e) {
            Slog.w("OverlayManager", "failed to generate idmap for " + path + " and " + str, e);
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeIdmap(OverlayInfo overlayInfo, int i) {
        if (OverlayManagerService.DEBUG) {
            Slog.d("OverlayManager", "remove idmap for " + overlayInfo.baseCodePath);
        }
        try {
            return this.mIdmapDaemon.removeIdmap(overlayInfo.baseCodePath, i);
        } catch (Exception e) {
            Slog.w("OverlayManager", "failed to remove idmap for " + overlayInfo.baseCodePath, e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean idmapExists(OverlayInfo overlayInfo) {
        return this.mIdmapDaemon.idmapExists(overlayInfo.baseCodePath, overlayInfo.userId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<FabricatedOverlayInfo> getFabricatedOverlayInfos() {
        return this.mIdmapDaemon.getFabricatedOverlayInfos();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FabricatedOverlayInfo createFabricatedOverlay(FabricatedOverlayInternal fabricatedOverlayInternal) {
        return this.mIdmapDaemon.createFabricatedOverlay(fabricatedOverlayInternal);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteFabricatedOverlay(String str) {
        return this.mIdmapDaemon.deleteFabricatedOverlay(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String dumpIdmap(String str) {
        return this.mIdmapDaemon.dumpIdmap(str);
    }

    private boolean enforceOverlayable(PackageState packageState, AndroidPackage androidPackage) {
        if (androidPackage.getTargetSdkVersion() >= 29) {
            return true;
        }
        if (packageState.isVendor()) {
            return VENDOR_IS_Q_OR_LATER;
        }
        return (packageState.isSystem() || androidPackage.isSignedWithPlatformKey()) ? false : true;
    }

    private int calculateFulfilledPolicies(AndroidPackage androidPackage, PackageState packageState, AndroidPackage androidPackage2, int i) {
        int i2 = this.mPackageManager.signaturesMatching(androidPackage.getPackageName(), androidPackage2.getPackageName(), i) ? 17 : 1;
        if (matchesActorSignature(androidPackage, androidPackage2, i)) {
            i2 |= 128;
        }
        if (!TextUtils.isEmpty(this.mConfigSignaturePackage) && this.mPackageManager.signaturesMatching(this.mConfigSignaturePackage, androidPackage2.getPackageName(), i)) {
            i2 |= 256;
        }
        return packageState.isVendor() ? i2 | 4 : packageState.isProduct() ? i2 | 8 : packageState.isOdm() ? i2 | 32 : packageState.isOem() ? i2 | 64 : (packageState.isSystem() || packageState.isSystemExt()) ? i2 | 2 : i2;
    }

    private boolean matchesActorSignature(AndroidPackage androidPackage, AndroidPackage androidPackage2, int i) {
        String str;
        String overlayTargetOverlayableName = androidPackage2.getOverlayTargetOverlayableName();
        if (overlayTargetOverlayableName == null) {
            return false;
        }
        try {
            OverlayableInfo overlayableForTarget = this.mPackageManager.getOverlayableForTarget(androidPackage.getPackageName(), overlayTargetOverlayableName, i);
            if (overlayableForTarget == null || (str = overlayableForTarget.actor) == null) {
                return false;
            }
            return this.mPackageManager.signaturesMatching((String) OverlayActorEnforcer.getPackageNameForActor(str, this.mPackageManager.getNamedActors()).first, androidPackage2.getPackageName(), i);
        } catch (IOException unused) {
            return false;
        }
    }
}
