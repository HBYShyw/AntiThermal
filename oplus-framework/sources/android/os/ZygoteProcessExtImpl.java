package android.os;

import android.common.OplusFeatureCache;
import android.telephony.PhoneNumberUtilsExtImpl;
import android.util.Log;
import com.oplus.hiddenapi.IOplusHiddenApiManager;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ZygoteProcessExtImpl implements IZygoteProcessExt {
    public void addArgsInStartViaZygote(String packageName, ArrayList<String> argsForZygote) {
        if (packageName != null && packageName.length() > 0) {
            List<String> exemptions = null;
            try {
                exemptions = ((IOplusHiddenApiManager) OplusFeatureCache.get(IOplusHiddenApiManager.DEFAULT)).getExemptions(packageName);
            } catch (Exception ex) {
                Log.d("ZygoteProcess", "fail to get hidden api exemptions: " + packageName + ", " + ex);
            }
            if (exemptions != null && !exemptions.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("--oplus-hidden-api-exemptions=");
                int sz = exemptions.size();
                for (int i = 0; i < sz; i++) {
                    if (i != 0) {
                        sb.append(PhoneNumberUtilsExtImpl.PAUSE);
                    }
                    sb.append(exemptions.get(i));
                }
                argsForZygote.add(sb.toString());
            }
        }
    }
}
