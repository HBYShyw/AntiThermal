package com.android.server.pm;

import android.content.Context;
import android.util.Slog;
import com.mediatek.internal.os.CustomPolicy;
import com.mediatek.server.MtkSystemServiceFactory;
import com.mediatek.server.powerhal.PowerHalManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageManagerServiceSocExtImpl implements IPackageManagerServiceSocExt {
    private static boolean DEBUG_PMS = true;
    private static final String TAG = "PackageManager";
    private Context mContext;
    private PackageManagerService mPms;
    private PowerHalManager mPowerHalManager = MtkSystemServiceFactory.getInstance().makePowerHalManager();

    public PackageManagerServiceSocExtImpl(Object obj) {
        PackageManagerService packageManagerService = (PackageManagerService) obj;
        this.mPms = packageManagerService;
        this.mContext = packageManagerService.mContext;
        Slog.d(TAG, "PackageManagerServiceSocExtImpl instance(MTK) create!");
    }

    @Override // com.android.server.pm.IPackageManagerServiceSocExt
    public void setInstallationBoost(boolean z) {
        PowerHalManager powerHalManager = this.mPowerHalManager;
        if (powerHalManager != null) {
            powerHalManager.setInstallationBoost(z);
        }
    }

    @Override // com.android.server.pm.IPackageManagerServiceSocExt
    public void registerHbtRusOnSystemReady() {
        CustomPolicy.ConfigLoader.registerRusListener();
    }
}
