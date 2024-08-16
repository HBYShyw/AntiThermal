package android.view.autolayout;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import com.oplus.autolayout.IAutoLayoutAdapterExt;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusAutoLayout2Manager extends IAutoLayoutAdapterExt {
    public static final IOplusAutoLayout2Manager mDefault = new IOplusAutoLayout2Manager() { // from class: android.view.autolayout.IOplusAutoLayout2Manager.1
    };

    default Bundle autoLayoutCall(Bundle bundle) {
        return null;
    }

    default boolean checkIfHasCover(Context context, String packageName, String signFun, String filePath, byte[] fileSignature, boolean toDelete) {
        return false;
    }

    default void makeCover(Context context, String packageName) {
    }

    default void preMakePaths(ApplicationInfo aInfo, List<String> outZipPaths) {
    }

    default void hookApplication(Application app) {
    }

    default void setActivityPolicy(int policy) {
    }

    default void setWindowMode(boolean isEnable) {
    }
}
