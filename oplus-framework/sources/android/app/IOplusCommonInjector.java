package android.app;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.pm.PackageParser;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;

/* loaded from: classes.dex */
public interface IOplusCommonInjector extends IOplusCommonFeature {
    public static final IOplusCommonInjector DEFAULT = new IOplusCommonInjector() { // from class: android.app.IOplusCommonInjector.1
    };

    /* renamed from: getDefault, reason: merged with bridge method [inline-methods] */
    default IOplusCommonInjector m5getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusCommonInjector;
    }

    default void onCreateForActivity(Activity activity, Bundle savedInstanceState) {
    }

    default void onCreateForApplication(Application application) {
    }

    default void applyConfigurationToResourcesForResourcesManager(Configuration config, int changes) {
    }

    default void onConfigurationChangedForApplication(Application application, Configuration newConfig) {
    }

    default void hookPreloadResources(Resources res, String tag) {
    }

    default void hookActivityAliasTheme(PackageParser.Activity a, Resources res, XmlResourceParser parser, PackageParser.Activity target) {
    }
}
