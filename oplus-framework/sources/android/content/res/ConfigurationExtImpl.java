package android.content.res;

import android.common.OplusFeatureCache;
import android.view.autolayout.IOplusAutoLayoutManager;

/* loaded from: classes.dex */
public class ConfigurationExtImpl implements IConfigurationExt {
    private Configuration mConfiguration;

    public ConfigurationExtImpl(Object configImpl) {
        this.mConfiguration = (Configuration) configImpl;
    }

    public void hookSetTo(Configuration configuration) {
        getAutoLayoutManager().setTo(configuration);
    }

    public void hookUpdateFrom(Configuration configuration) {
        getAutoLayoutManager().updateFrom(configuration);
    }

    private IOplusAutoLayoutManager getAutoLayoutManager() {
        return (IOplusAutoLayoutManager) OplusFeatureCache.getOrCreate(IOplusAutoLayoutManager.mDefault, new Object[0]);
    }

    public void setMultiWindowConfigTo(Configuration other) {
        if (other == null) {
            return;
        }
        this.mConfiguration.orientation = other.orientation;
        this.mConfiguration.screenLayout = other.screenLayout;
        this.mConfiguration.screenWidthDp = other.screenWidthDp;
        this.mConfiguration.screenHeightDp = other.screenHeightDp;
        this.mConfiguration.smallestScreenWidthDp = other.smallestScreenWidthDp;
        this.mConfiguration.densityDpi = other.densityDpi;
        this.mConfiguration.compatScreenWidthDp = other.compatScreenWidthDp;
        this.mConfiguration.compatScreenHeightDp = other.compatScreenHeightDp;
        this.mConfiguration.compatSmallestScreenWidthDp = other.compatSmallestScreenWidthDp;
        this.mConfiguration.windowConfiguration.setTo(other.windowConfiguration);
        this.mConfiguration.getOplusExtraConfiguration().setTo(other.getOplusExtraConfiguration());
    }
}
