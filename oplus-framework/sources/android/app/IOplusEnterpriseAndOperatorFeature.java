package android.app;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.pm.ApplicationInfo;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusEnterpriseAndOperatorFeature extends IOplusCommonFeature {
    public static final IOplusEnterpriseAndOperatorFeature DEFAULT = new IOplusEnterpriseAndOperatorFeature() { // from class: android.app.IOplusEnterpriseAndOperatorFeature.1
    };
    public static final String NAME = "IOplusEnterpriseAndOperatorFeature";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusEnterpriseAndOperatorFeature;
    }

    /* renamed from: getDefault, reason: merged with bridge method [inline-methods] */
    default IOplusEnterpriseAndOperatorFeature m6getDefault() {
        return DEFAULT;
    }

    default void addCustomMdmJarToPath(List<String> outPaths) {
    }

    default boolean isPackageContainsOplusCertificates(String packageName) throws RemoteException {
        return false;
    }

    default void addCustomMdmJarToPath(boolean isActivityThreadExist, ApplicationInfo aInfo, List<String> outZipPaths) {
    }
}
