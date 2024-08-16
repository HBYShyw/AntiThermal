package com.oplus.resolver;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class NearbyUtil {
    public static ComponentName getNearbySharingComponent(Context context) {
        String nearbyComponent = Settings.Secure.getString(context.getContentResolver(), "nearby_sharing_component");
        if (TextUtils.isEmpty(nearbyComponent)) {
            nearbyComponent = context.getString(R.string.config_pdp_reject_multi_conn_to_same_pdn_not_allowed);
        }
        if (TextUtils.isEmpty(nearbyComponent)) {
            return null;
        }
        return ComponentName.unflattenFromString(nearbyComponent);
    }
}
