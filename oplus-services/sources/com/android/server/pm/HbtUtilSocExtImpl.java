package com.android.server.pm;

import android.content.pm.ApplicationInfo;
import android.os.Build;
import com.mediatek.internal.os.PolicySelector;
import com.mediatek.server.pm.hbtpackage.HBTPackage;
import dalvik.system.VMRuntime;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class HbtUtilSocExtImpl implements IHbtUtilSocExt {
    static boolean DEBUG = false;
    static final String TAG = "HbtUtilSocExtImpl";
    private static volatile HbtUtilSocExtImpl sInstance;

    private HbtUtilSocExtImpl(Object obj) {
    }

    public static HbtUtilSocExtImpl getInstance(Object obj) {
        if (sInstance == null) {
            synchronized (HbtUtilSocExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new HbtUtilSocExtImpl(obj);
                }
            }
        }
        return sInstance;
    }

    @Override // com.android.server.pm.IHbtUtilSocExt
    public void hbtCheckUpdate(String str, String[] strArr, String[] strArr2) {
        HBTPackage.HBTcheckUpdate(str, strArr, strArr2);
    }

    @Override // com.android.server.pm.IHbtUtilSocExt
    public void hbtCheckInstall(PackageSetting packageSetting, PackageSetting packageSetting2, boolean z) {
        if (z) {
            HBTPackage.HBTcheckUpdate(packageSetting2.getPackageName(), InstructionSets.getAppDexInstructionSets(packageSetting.getPrimaryCpuAbi(), packageSetting.getSecondaryCpuAbi()), InstructionSets.getAppDexInstructionSets(packageSetting2.getPrimaryCpuAbiLegacy(), packageSetting2.getSecondaryCpuAbiLegacy()));
        } else {
            HBTPackage.HBTcheckInstall(packageSetting.getPackageName(), InstructionSets.getAppDexInstructionSets(packageSetting.getPrimaryCpuAbi(), packageSetting.getSecondaryCpuAbi()));
        }
    }

    @Override // com.android.server.pm.IHbtUtilSocExt
    public void hbtCheckUninstall(String str, String[] strArr) {
        HBTPackage.HBTcheckUninstall(str, strArr);
    }

    @Override // com.android.server.pm.IHbtUtilSocExt
    public void hbtCheckStatus(String str, String[] strArr) {
        HBTPackage.HBTcheckStatus(strArr, str);
    }

    @Override // com.android.server.pm.IHbtUtilSocExt
    public boolean isHbt64BitOnlyChip() {
        return PolicySelector.MTK_HBT_ON_64BIT_ONLY_CHIP;
    }

    @Override // com.android.server.pm.IHbtUtilSocExt
    public String getHbtPrimaryCpuAbi(ApplicationInfo applicationInfo) {
        if (Build.MTK_HBT_ON_64BIT_ONLY_CHIP && applicationInfo.packageName.equals("com.android.webview") && !VMRuntime.getRuntime().is64Bit()) {
            return Build.MTK_HBT_SUPPORTED_32_BIT_ABIS[0];
        }
        return applicationInfo.primaryCpuAbi;
    }
}
