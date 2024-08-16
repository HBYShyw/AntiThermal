package com.mediatek.server.pm.hbtpackage;

import android.provider.DeviceConfig;
import com.android.internal.os.ZygoteConfigSocExtImpl;
import com.android.server.pm.InstructionSets;
import com.mediatek.internal.os.PolicySelector;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class HBTPackage {
    public static void HBTcheckUpdate(String str, String[] strArr, String[] strArr2) {
        if (ZygoteConfigSocExtImpl.getInstance((Object) null).isApp32BoostEnabled() && ZygoteConfigSocExtImpl.sDispatchPolicy != null && ZygoteConfigSocExtImpl.DYNAMIC_POLICY == 1) {
            if (str == null) {
                if (!PolicySelector.MTK_HBT_ON_64BIT_ONLY_CHIP) {
                    return;
                }
            } else if (!ZygoteConfigSocExtImpl.sDispatchPolicy.checkPackageName(str)) {
                return;
            }
            String[] dexCodeInstructionSets = InstructionSets.getDexCodeInstructionSets(strArr);
            String[] dexCodeInstructionSets2 = InstructionSets.getDexCodeInstructionSets(strArr2);
            if (dexCodeInstructionSets2[0].equals("arm") && dexCodeInstructionSets[0].equals("arm64")) {
                Integer num = new Integer(DeviceConfig.getInt("vendor_system_native", "hbt_target_installed", 1));
                if (num.intValue() == 1 && ZygoteConfigSocExtImpl.getInstance((Object) null).isApp32BoostEnabled()) {
                    DeviceConfig.setProperty("vendor_system_native", "zygote_HBT", "0", false);
                }
                DeviceConfig.setProperty("vendor_system_native", "hbt_target_installed", Integer.valueOf(num.intValue() - 1).toString(), false);
                return;
            }
            if (dexCodeInstructionSets2[0].equals("arm64") && dexCodeInstructionSets[0].equals("arm")) {
                Integer num2 = new Integer(DeviceConfig.getInt("vendor_system_native", "hbt_target_installed", 0));
                if (num2.intValue() == 0 && ZygoteConfigSocExtImpl.getInstance((Object) null).isApp32BoostEnabled()) {
                    DeviceConfig.setProperty("vendor_system_native", "zygote_HBT", "1", false);
                }
                DeviceConfig.setProperty("vendor_system_native", "hbt_target_installed", Integer.valueOf(num2.intValue() + 1).toString(), false);
            }
        }
    }

    public static void HBTcheckInstall(String str, String[] strArr) {
        if (ZygoteConfigSocExtImpl.DYNAMIC_POLICY == 2) {
            DeviceConfig.setProperty("vendor_system_native", "no_hbt_dyn_off", "false", false);
            return;
        }
        if (ZygoteConfigSocExtImpl.getInstance((Object) null).isApp32BoostEnabled() && ZygoteConfigSocExtImpl.sDispatchPolicy != null && ZygoteConfigSocExtImpl.DYNAMIC_POLICY == 1) {
            if (str == null) {
                if (!PolicySelector.MTK_HBT_ON_64BIT_ONLY_CHIP) {
                    return;
                }
            } else if (!ZygoteConfigSocExtImpl.sDispatchPolicy.checkPackageName(str)) {
                return;
            }
            for (String str2 : InstructionSets.getDexCodeInstructionSets(strArr)) {
                if (str2.equals("arm")) {
                    Integer num = new Integer(DeviceConfig.getInt("vendor_system_native", "hbt_target_installed", 0));
                    if (num.intValue() == 0 && ZygoteConfigSocExtImpl.getInstance((Object) null).isApp32BoostEnabled()) {
                        DeviceConfig.setProperty("vendor_system_native", "zygote_HBT", "1", false);
                    }
                    DeviceConfig.setProperty("vendor_system_native", "hbt_target_installed", Integer.valueOf(num.intValue() + 1).toString(), false);
                    return;
                }
            }
        }
    }

    public static void HBTcheckUninstall(String str, String[] strArr) {
        if (ZygoteConfigSocExtImpl.getInstance((Object) null).isApp32BoostEnabled() && ZygoteConfigSocExtImpl.sDispatchPolicy != null && ZygoteConfigSocExtImpl.DYNAMIC_POLICY == 1) {
            if (str == null) {
                if (!PolicySelector.MTK_HBT_ON_64BIT_ONLY_CHIP) {
                    return;
                }
            } else if (!ZygoteConfigSocExtImpl.sDispatchPolicy.checkPackageName(str)) {
                return;
            }
            for (String str2 : InstructionSets.getDexCodeInstructionSets(strArr)) {
                if (str2.equals("arm")) {
                    Integer num = new Integer(DeviceConfig.getInt("vendor_system_native", "hbt_target_installed", 1));
                    if (num.intValue() == 1 && ZygoteConfigSocExtImpl.getInstance((Object) null).isApp32BoostEnabled()) {
                        DeviceConfig.setProperty("vendor_system_native", "zygote_HBT", "0", false);
                    }
                    DeviceConfig.setProperty("vendor_system_native", "hbt_target_installed", Integer.valueOf(num.intValue() - 1).toString(), false);
                    return;
                }
            }
        }
    }

    public static boolean HBTcheckStatus(String[] strArr, String str) {
        if (!ZygoteConfigSocExtImpl.getInstance((Object) null).isApp32BoostEnabled() || !PolicySelector.MTK_HBT_ON_64BIT_ONLY_CHIP || ZygoteConfigSocExtImpl.sDispatchPolicy == null) {
            return true;
        }
        for (String str2 : InstructionSets.getDexCodeInstructionSets(strArr)) {
            if (str2.equals("arm")) {
                if (str == null) {
                    return PolicySelector.MTK_HBT_ON_64BIT_ONLY_CHIP;
                }
                return ZygoteConfigSocExtImpl.sDispatchPolicy.checkPackageName(str);
            }
        }
        return true;
    }
}
