package com.android.server.devicepolicy;

import android.content.Context;
import android.content.pm.VerifierDeviceIdentity;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.security.identity.Util;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.nio.ByteBuffer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class EnterpriseSpecificIdCalculator {
    private static final int ESID_LENGTH = 16;
    private static final int PADDED_ENTERPRISE_ID_LENGTH = 64;
    private static final int PADDED_HW_ID_LENGTH = 16;
    private static final int PADDED_PROFILE_OWNER_LENGTH = 64;
    private final String mImei;
    private final String mMacAddress;
    private final String mMeid;
    private final String mSerialNumber;

    @VisibleForTesting
    EnterpriseSpecificIdCalculator(String str, String str2, String str3, String str4) {
        this.mImei = str;
        this.mMeid = str2;
        this.mSerialNumber = str3;
        this.mMacAddress = str4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EnterpriseSpecificIdCalculator(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        Preconditions.checkState(telephonyManager != null, "Unable to access telephony service");
        this.mImei = telephonyManager.getImei(0);
        this.mMeid = telephonyManager.getMeid(0);
        this.mSerialNumber = Build.getSerial();
        WifiManager wifiManager = (WifiManager) context.getSystemService(WifiManager.class);
        Preconditions.checkState(wifiManager != null, "Unable to access WiFi service");
        String[] factoryMacAddresses = wifiManager.getFactoryMacAddresses();
        if (factoryMacAddresses == null || factoryMacAddresses.length == 0) {
            this.mMacAddress = "";
        } else {
            this.mMacAddress = factoryMacAddresses[0];
        }
    }

    private static String getPaddedTruncatedString(String str, int i) {
        return String.format("%" + i + "s", str).substring(0, i);
    }

    private static String getPaddedHardwareIdentifier(String str) {
        if (str == null) {
            str = "";
        }
        return getPaddedTruncatedString(str, 16);
    }

    String getPaddedImei() {
        return getPaddedHardwareIdentifier(this.mImei);
    }

    String getPaddedMeid() {
        return getPaddedHardwareIdentifier(this.mMeid);
    }

    String getPaddedSerialNumber() {
        return getPaddedHardwareIdentifier(this.mSerialNumber);
    }

    String getPaddedProfileOwnerName(String str) {
        return getPaddedTruncatedString(str, 64);
    }

    String getPaddedEnterpriseId(String str) {
        return getPaddedTruncatedString(str, 64);
    }

    public String calculateEnterpriseId(String str, String str2) {
        boolean z = true;
        Preconditions.checkArgument(!TextUtils.isEmpty(str), "owner package must be specified.");
        if (str2 != null && str2.isEmpty()) {
            z = false;
        }
        Preconditions.checkArgument(z, "enterprise ID must either be null or non-empty.");
        if (str2 == null) {
            str2 = "";
        }
        byte[] bytes = getPaddedSerialNumber().getBytes();
        byte[] bytes2 = getPaddedImei().getBytes();
        byte[] bytes3 = getPaddedMeid().getBytes();
        byte[] bytes4 = this.mMacAddress.getBytes();
        ByteBuffer allocate = ByteBuffer.allocate(bytes.length + bytes2.length + bytes3.length + bytes4.length);
        allocate.put(bytes);
        allocate.put(bytes2);
        allocate.put(bytes3);
        allocate.put(bytes4);
        byte[] bytes5 = getPaddedProfileOwnerName(str).getBytes();
        byte[] bytes6 = getPaddedEnterpriseId(str2).getBytes();
        ByteBuffer allocate2 = ByteBuffer.allocate(bytes5.length + bytes6.length);
        allocate2.put(bytes5);
        allocate2.put(bytes6);
        ByteBuffer wrap = ByteBuffer.wrap(Util.computeHkdf("HMACSHA256", allocate.array(), (byte[]) null, allocate2.array(), 16));
        return new VerifierDeviceIdentity(wrap.getLong()).toString() + new VerifierDeviceIdentity(wrap.getLong()).toString();
    }
}
