package com.oplus.deepthinker.sdk.app.deepthinkermanager;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.userprofile.labels.ResidenceLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.StayLocationLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.WifiLocationLabel;
import java.util.List;

/* loaded from: classes.dex */
public interface IEnvironmentDomainManager {
    default List<ResidenceLabel> getCompanyLabels() {
        return null;
    }

    default List<ResidenceLabel> getHomeLabels() {
        return null;
    }

    default int getInOutDoorState() {
        return 0;
    }

    default int getInOutDoorState(Bundle bundle) {
        return 0;
    }

    default List<ResidenceLabel> getResidenceLabels() {
        return null;
    }

    default List<String> getSmartGpsBssidList() {
        return null;
    }

    default List<StayLocationLabel> getStayLocationLabels() {
        return null;
    }

    default List<WifiLocationLabel> getWifiLocationLabels() {
        return null;
    }
}
