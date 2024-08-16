package android.view;

import android.app.ActivityThread;
import android.common.OplusFeatureCache;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import com.oplus.screenmode.IOplusAutoResolutionFeature;

/* loaded from: classes.dex */
public class DisplayInfoExtImpl implements IDisplayInfoExt {
    private static final boolean DEBUG = true;
    IOplusAutoResolutionFeature feature = (IOplusAutoResolutionFeature) OplusFeatureCache.get(IOplusAutoResolutionFeature.DEFAULT);

    public DisplayInfoExtImpl(Object base) {
    }

    public boolean supportDisplayCompat() {
        IOplusAutoResolutionFeature iOplusAutoResolutionFeature = this.feature;
        return iOplusAutoResolutionFeature != null && iOplusAutoResolutionFeature.supportDisplayCompat();
    }

    private CompatibilityInfo getCompatibilityInfo() {
        IOplusAutoResolutionFeature iOplusAutoResolutionFeature = this.feature;
        if (iOplusAutoResolutionFeature != null) {
            return iOplusAutoResolutionFeature.getCompatibilityInfo();
        }
        return null;
    }

    public void overrideDisplayMetricsIfNeed(DisplayMetrics outMetrics) {
        CompatibilityInfo compatInfo;
        if (supportDisplayCompat() && (compatInfo = getCompatibilityInfo()) != null) {
            compatInfo.applyToDisplayMetrics(outMetrics);
        }
    }

    public void overrideDisplayMetricsIfNeed(DisplayMetrics outMetrics, Configuration configuration, int logicalDensityDpi, float physicalXDpi, float physicalYDpi) {
        Configuration processConfiguration = null;
        if (useConfigurationInfo(configuration)) {
            updateMetrics(outMetrics, configuration, logicalDensityDpi, physicalXDpi, physicalYDpi);
            return;
        }
        ActivityThread am = ActivityThread.currentActivityThread();
        if (am != null) {
            processConfiguration = am.mOplusActivityThreadExt.getFakeDisplayConfiguration();
        }
        if (useConfigurationInfo(processConfiguration)) {
            updateMetrics(outMetrics, processConfiguration, logicalDensityDpi, physicalXDpi, physicalYDpi);
        }
    }

    private void updateMetrics(DisplayMetrics inoutMetrics, Configuration configuration, int logicalDensityDpi, float physicalXDpi, float physicalYDpi) {
        if (configuration != null) {
            inoutMetrics.densityDpi = configuration.densityDpi;
            inoutMetrics.noncompatDensityDpi = configuration.densityDpi;
            inoutMetrics.density = configuration.densityDpi * 0.00625f;
            inoutMetrics.noncompatDensity = configuration.densityDpi * 0.00625f;
            inoutMetrics.scaledDensity = inoutMetrics.density;
            inoutMetrics.noncompatScaledDensity = inoutMetrics.density;
            float densityScale = (inoutMetrics.densityDpi * 1.0f) / logicalDensityDpi;
            inoutMetrics.xdpi = physicalXDpi * densityScale;
            inoutMetrics.noncompatXdpi = physicalXDpi * densityScale;
            inoutMetrics.ydpi = physicalYDpi * densityScale;
            inoutMetrics.noncompatYdpi = physicalYDpi * densityScale;
            inoutMetrics.noncompatWidthPixels = configuration.windowConfiguration.getBounds().width();
            inoutMetrics.widthPixels = configuration.windowConfiguration.getBounds().width();
            inoutMetrics.noncompatHeightPixels = configuration.windowConfiguration.getBounds().height();
            inoutMetrics.heightPixels = configuration.windowConfiguration.getBounds().height();
        }
    }

    private boolean useConfigurationInfo(Configuration configuration) {
        return configuration != null && configuration.getOplusExtraConfiguration().getScenario() == 3;
    }
}
