package android.widget;

import android.common.OplusFrameworkFactory;
import android.content.ContentResolver;
import android.content.Context;
import android.os.OplusPropertyList;
import android.os.OplusSystemProperties;
import android.provider.Settings;
import android.util.BoostFramework;
import android.util.Log;
import android.view.animation.Interpolator;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.dynamicvsync.IOplusDynamicVsyncFeature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class OplusOverScrollerExtImpl implements IOplusOverScrollerExt {
    private static final String OPLUS_SCROLL_RUS_SETTINGS_NAME = "spring_overscroller_package_list";
    private static final int VSYNC_DURATION = 5000;
    private Context mContext;
    private IOplusDynamicVsyncFeature mDynamicVsyncFeature;
    private boolean mForceUsingSpring;
    private IOplusScrollOptimizationHelper mScrollOptHelper;
    private OplusSpringOverScroller mSpringOverScroller;
    private static final String TAG = OplusOverScrollerExtImpl.class.getSimpleName();
    private static boolean sOptHelperEnable = false;
    private static Object sLock = new Object();
    private static boolean sFlingOpt = false;
    private static boolean sCustomizationFling = false;

    public OplusOverScrollerExtImpl() {
        this(null);
    }

    public OplusOverScrollerExtImpl(Object os) {
        this.mForceUsingSpring = false;
        this.mDynamicVsyncFeature = (IOplusDynamicVsyncFeature) getFactory().getFeature(IOplusDynamicVsyncFeature.DEFAULT, new Object[0]);
        this.mScrollOptHelper = (IOplusScrollOptimizationHelper) getFactory().getFeature(IOplusScrollOptimizationHelper.DEFAULT, new Object[0]);
    }

    public void setFriction(float friction) {
        this.mSpringOverScroller.setFriction(friction);
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mSpringOverScroller.setInterpolator(interpolator);
    }

    public void forceFinished(boolean finished) {
        this.mSpringOverScroller.forceFinished(finished);
    }

    public void extendDuration(int extend) {
    }

    public void setFinalX(int newX) {
        this.mSpringOverScroller.setFinalX(newX);
    }

    public void setFinalY(int newY) {
        this.mSpringOverScroller.setFinalY(newY);
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        this.mSpringOverScroller.startScroll(startX, startY, dx, dy);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mSpringOverScroller.startScroll(startX, startY, dx, dy, duration);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        this.mDynamicVsyncFeature.flingEvent(this.mContext.getPackageName(), 5000);
        this.mSpringOverScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        this.mDynamicVsyncFeature.flingEvent(this.mContext.getPackageName(), 5000);
        this.mSpringOverScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY);
        hookFling(minX, maxX, minY, maxY);
    }

    public void notifyHorizontalEdgeReached(int startX, int finalX, int overX) {
        this.mSpringOverScroller.notifyHorizontalEdgeReached(startX, finalX, overX);
    }

    public void notifyVerticalEdgeReached(int startY, int finalY, int overY) {
        this.mSpringOverScroller.notifyVerticalEdgeReached(startY, finalY, overY);
    }

    public void abortAnimation() {
        this.mSpringOverScroller.abortAnimation();
    }

    public int getStartX() {
        return this.mSpringOverScroller.getOplusStartX();
    }

    public int getStartY() {
        return this.mSpringOverScroller.getOplusStartY();
    }

    public int getDuration() {
        return this.mSpringOverScroller.getDuration();
    }

    public int getCurrX() {
        return this.mSpringOverScroller.getCurrX();
    }

    public int getCurrY() {
        return this.mSpringOverScroller.getCurrY();
    }

    public int getFinalX() {
        return this.mSpringOverScroller.getFinalX();
    }

    public int getFinalY() {
        return this.mSpringOverScroller.getFinalY();
    }

    public int timePassed() {
        return this.mSpringOverScroller.timePassed();
    }

    public float getCurrVelocity() {
        return this.mSpringOverScroller.getCurrVelocity();
    }

    public boolean isOverScrolled() {
        return this.mSpringOverScroller.isOverScrolled();
    }

    public boolean isScrollingInDirection(float xvel, float yvel) {
        return this.mSpringOverScroller.isScrollingInDirection(xvel, yvel);
    }

    public boolean springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) {
        this.mDynamicVsyncFeature.flingEvent(this.mContext.getPackageName(), 5000);
        return this.mSpringOverScroller.springBack(startX, startY, minX, maxX, minY, maxY);
    }

    public boolean computeScrollOffset() {
        return this.mSpringOverScroller.computeScrollOffset();
    }

    public boolean getForceUsingSpring() {
        return this.mForceUsingSpring;
    }

    public boolean isFinished() {
        return this.mSpringOverScroller.isFinished();
    }

    private OplusFrameworkFactory getFactory() {
        return OplusFrameworkFactory.getInstance();
    }

    private boolean hasSpringFeature() {
        return OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_SPRING_OVER_SCROLLER_SUPPORT);
    }

    public boolean initScrollAndConfigFromRus(Context context) {
        String packageName = context.getPackageName();
        if (packageName != null && packageName.contains("gallery3d")) {
            return false;
        }
        try {
            ContentResolver resolver = context.getContentResolver();
            String packageList = Settings.Global.getString(resolver, OPLUS_SCROLL_RUS_SETTINGS_NAME);
            if (packageList != null && packageList.length() != 0) {
                String subString = packageList.substring(1, packageList.length() - 1);
                List<String> list = new ArrayList<>(Arrays.asList(subString.split(", ")));
                if (list.contains("disable.all.spring")) {
                    return false;
                }
                boolean isInList = list.contains(packageName);
                Log.d(TAG, "Success to read Oplus Scroll Rus list, " + packageName + " is enabled? " + isInList);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).contains("spring_config")) {
                        String configStr = list.get(i);
                        this.mSpringOverScroller.setSpringConfigFromRus(configStr);
                    }
                }
                return isInList;
            }
            return false;
        } catch (Throwable t) {
            Log.d(TAG, "Failed to read Oplus Scroll Rus list, " + t.getMessage());
            return false;
        }
    }

    public boolean hookAbortAnimation(ISplineOverScrollerExt mSplineOverScrollerExtX, ISplineOverScrollerExt mSplineOverScrollerExtY) {
        BoostFramework.ScrollOptimizer.setFlingFlag(0);
        mSplineOverScrollerExtX.hookSaveCurrVeloAccuCount();
        mSplineOverScrollerExtY.hookSaveCurrVeloAccuCount();
        long current = System.currentTimeMillis();
        mSplineOverScrollerExtX.hookSetAbortTime(current);
        mSplineOverScrollerExtY.hookSetAbortTime(current);
        return false;
    }

    public boolean hookOverScroller(Context context, Interpolator interpolator) {
        this.mContext = context;
        this.mSpringOverScroller = new OplusSpringOverScroller(context, interpolator);
        this.mForceUsingSpring = hasSpringFeature() && initScrollAndConfigFromRus(this.mContext);
        return true;
    }

    public Interpolator getInterpolator(Interpolator interpolator) {
        synchronized (sLock) {
            boolean optHelperEnable = getOptHelperEnable();
            sOptHelperEnable = optHelperEnable;
            if (interpolator != null || !optHelperEnable || !interpolatorValid()) {
                return interpolator;
            }
            return this.mScrollOptHelper.getInterpolator();
        }
    }

    public boolean getOptHelperEnable() {
        return this.mScrollOptHelper.enable();
    }

    public boolean interpolatorValid() {
        return this.mScrollOptHelper.interpolatorValid();
    }

    public boolean getCustomizationFling() {
        boolean z;
        synchronized (sLock) {
            z = sCustomizationFling;
        }
        return z;
    }

    public boolean getFlingOpt() {
        boolean z;
        synchronized (sLock) {
            z = sFlingOpt;
        }
        return z;
    }

    public boolean hookCheckFlingFlag() {
        synchronized (sLock) {
            return (!sFlingOpt || sCustomizationFling || sOptHelperEnable) ? false : true;
        }
    }

    private void hookFling(int minX, int maxX, int minY, int maxY) {
        synchronized (sLock) {
            sCustomizationFling = ((minX == Integer.MIN_VALUE || minX == 0) && (minY == Integer.MIN_VALUE || minY == 0) && ((maxX == Integer.MAX_VALUE || maxX == 0) && (maxY == Integer.MAX_VALUE || maxY == 0))) ? false : true;
            if (OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_LIST_OPTIMIZE)) {
                sFlingOpt = OplusSystemProperties.getBoolean(OplusPropertyList.PROPETY_LIST_OPTIMIZE_ENABLE, false);
            }
        }
    }
}
