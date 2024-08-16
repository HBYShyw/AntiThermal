package android.telephony.data;

import android.os.Build;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.oplus.exfunction.ExFunctionManager;
import com.oplus.zoomwindow.OplusZoomWindowManager;
import java.util.Objects;

/* loaded from: classes.dex */
public class ApnSettingExtImpl implements IApnSettingExt {
    private static final String FASTWEB = "apn.fastweb.it";
    private static final String KWZAIN = "pps";
    private static final String LOG_TAG = "ApnSettingExtImpl";
    private static final int UNSPECIFIED_INT = -1;

    public ApnSettingExtImpl(Object base) {
        Rlog.d(LOG_TAG, "ApnSettingExtImpl new");
    }

    public int oemGetApnTypesBitmaskFromString(String types, String operatorNumeric) {
        if (TextUtils.isEmpty(types)) {
            if (operatorNumeric != null && operatorNumeric.equals("44010")) {
                Rlog.d(LOG_TAG, "Add additional APN types for Rakuten MVNO.");
                int newResult = ApnSetting.getApnTypesBitmaskFromString("default,mms,supl,dun,fota,cbs,hipri");
                return newResult;
            }
            return 255;
        }
        int result = ApnSetting.getApnTypesBitmaskFromString(types);
        return result;
    }

    public boolean oemMergeApnIgnoreProtocolType(ApnSetting firstApn, ApnSetting secondApn) {
        if (!Objects.equals(firstApn.getApnName(), FASTWEB) || !Objects.equals(secondApn.getApnName(), FASTWEB)) {
            return xorEqualsInt(firstApn.getProtocol(), secondApn.getProtocol()) && xorEqualsInt(firstApn.getRoamingProtocol(), secondApn.getRoamingProtocol());
        }
        Rlog.d(LOG_TAG, "Merge APN for Fastweb.");
        return true;
    }

    private static boolean xorEqualsInt(int first, int second) {
        return first == -1 || second == -1 || Objects.equals(Integer.valueOf(first), Integer.valueOf(second));
    }

    public boolean oemMergeApnIgnoreUserPasswordAuthType(ApnSetting firstApn, ApnSetting secondApn) {
        if (!Objects.equals(firstApn.getApnName(), KWZAIN) || !Objects.equals(secondApn.getApnName(), KWZAIN)) {
            return xorEqualsString(firstApn.getUser(), secondApn.getUser()) && xorEqualsString(firstApn.getPassword(), secondApn.getPassword()) && xorEqualsInt(firstApn.getAuthType(), secondApn.getAuthType());
        }
        Rlog.d(LOG_TAG, "merge APN for Kuwait Zain pps MI/MMS.");
        return true;
    }

    private boolean xorEqualsString(String first, String second) {
        return TextUtils.isEmpty(first) || TextUtils.isEmpty(second) || first.equals(second);
    }

    public boolean oemEqualsProfileId(int oldProfileId, int newProfileId) {
        if (Build.isMtkPlatform()) {
            return true;
        }
        return Objects.equals(Integer.valueOf(oldProfileId), Integer.valueOf(newProfileId));
    }

    public ArrayMap oemGetSliceApnTypeStringMap() {
        Rlog.d(LOG_TAG, "oemGetSliceApnTypeStringMap");
        ArrayMap map = new ArrayMap();
        map.clear();
        if (Build.isMtkPlatform()) {
            map.put("slice1", 131072);
            map.put("slice2", 262144);
            map.put("slice3", 524288);
            map.put("slice4", 1048576);
            map.put("slice5", 2097152);
            map.put("slice6", Integer.valueOf(OplusZoomWindowManager.FLAG_TOUCH_OUTSIDE_CONTROL_VIEW));
            map.put("slice7", 8388608);
            map.put("slice8", 16777216);
            map.put("slice9", 33554432);
            map.put("slice10", 67108864);
            map.put("slice11", Integer.valueOf(ExFunctionManager.USER_FLAG_REPAIR_MODE));
            map.put("slice12", 268435456);
            map.put("slice13", 536870912);
            map.put("slice14", 1073741824);
            map.put("slice15", Integer.MIN_VALUE);
        } else {
            Rlog.d(LOG_TAG, "not supported slice type");
        }
        return map;
    }

    public ArrayMap oemGetSliceApnTypeIntMap() {
        Rlog.d(LOG_TAG, "oemGetSliceApnTypeIntMap");
        ArrayMap map = new ArrayMap();
        map.clear();
        if (Build.isMtkPlatform()) {
            map.put(131072, "slice1");
            map.put(262144, "slice2");
            map.put(524288, "slice3");
            map.put(1048576, "slice4");
            map.put(2097152, "slice5");
            map.put(Integer.valueOf(OplusZoomWindowManager.FLAG_TOUCH_OUTSIDE_CONTROL_VIEW), "slice6");
            map.put(8388608, "slice7");
            map.put(16777216, "slice8");
            map.put(33554432, "slice9");
            map.put(Integer.valueOf(ExFunctionManager.USER_FLAG_REPAIR_MODE), "slice11");
            map.put(268435456, "slice12");
            map.put(536870912, "slice13");
            map.put(1073741824, "slice14");
            map.put(Integer.MIN_VALUE, "slice15");
        } else {
            Rlog.d(LOG_TAG, "not supported slice type");
        }
        return map;
    }
}
