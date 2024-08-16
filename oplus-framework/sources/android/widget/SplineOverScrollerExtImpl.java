package android.widget;

import android.common.OplusFrameworkFactory;
import android.content.Context;
import android.util.Pair;
import android.view.debug.IOplusViewDebugManager;
import android.widget.OverScroller;
import com.oplus.dynamicframerate.DynamicFrameRateController;
import com.oplus.scrolloptim.ScrOptController;

/* loaded from: classes.dex */
public class SplineOverScrollerExtImpl implements ISplineOverScrollerExt {
    private static final int NUM_5 = 5;
    private static FlingOptimizerOverScroller sFlingOptimizerOverScroller = null;
    private OplusOverScrollerExtImpl mOplusOverScrollerExtImpl;
    private IOplusViewDebugManager mOplusViewDebugManager;
    private IOplusScrollOptimizationHelper mOptHelper;
    private OverScroller.SplineOverScroller mSplineOverScroller;

    public SplineOverScrollerExtImpl(Object splineOverScroller) {
        if (splineOverScroller instanceof OverScroller.SplineOverScroller) {
            this.mSplineOverScroller = (OverScroller.SplineOverScroller) splineOverScroller;
        }
        this.mOptHelper = (IOplusScrollOptimizationHelper) OplusFrameworkFactory.getInstance().getFeature(IOplusScrollOptimizationHelper.DEFAULT, new Object[0]);
        this.mOplusOverScrollerExtImpl = new OplusOverScrollerExtImpl();
    }

    public void hookSaveCurrVeloAccuCount() {
        this.mOptHelper.saveCurrVeloAccuCount();
    }

    public void hookSetAbortTime(long time) {
        this.mOptHelper.setAbortTime(time);
    }

    public void hookResetVeloAccuCount() {
        this.mOptHelper.resetVeloAccuCount();
    }

    public void hookStartFling(long now, float currVelo, int velo, boolean finished) {
        this.mOptHelper.setFlingParam(now, currVelo, velo, finished);
    }

    public Pair<Integer, Double> hookEndFling(Context context, int velocity, float flingFriction) {
        int mDuration;
        double totalDistance;
        if (this.mOplusOverScrollerExtImpl.getFlingOpt() && !this.mOplusOverScrollerExtImpl.getCustomizationFling() && !this.mOplusOverScrollerExtImpl.getOptHelperEnable()) {
            if (sFlingOptimizerOverScroller == null) {
                sFlingOptimizerOverScroller = new FlingOptimizerOverScroller(context);
            }
            mDuration = sFlingOptimizerOverScroller.getSplineFlingDurationTuning(velocity, flingFriction);
            totalDistance = sFlingOptimizerOverScroller.getSplineFlingDistanceTuning(velocity, flingFriction);
        } else {
            mDuration = this.mSplineOverScroller.getWrapper().getSplineFlingDuration(velocity);
            totalDistance = this.mSplineOverScroller.getWrapper().getSplineFlingDistance(velocity);
            if (this.mOplusOverScrollerExtImpl.getOptHelperEnable()) {
                mDuration = (int) (mDuration * this.mOptHelper.getCustomizedDurationCoef(velocity));
                totalDistance = (int) (this.mOptHelper.getCustomizedDistanceCoef(velocity) * totalDistance);
            }
        }
        return new Pair<>(Integer.valueOf(mDuration), Double.valueOf(totalDistance));
    }

    public void onFlingStart(int duration, int velocity, int position) {
        DynamicFrameRateController.getInstance().getAnimationSpeedAware().onFlingStart(duration, velocity, position);
        ScrOptController.getInstance().getSceneManager().onFlingStart();
    }

    public void onFlingFinish() {
        DynamicFrameRateController.getInstance().getAnimationSpeedAware().onFlingFinish();
        ScrOptController.getInstance().getSceneManager().onFlingFinish();
    }

    public void onFlingPositionUpdate(int velocity, int position) {
        DynamicFrameRateController.getInstance().getAnimationSpeedAware().onFlingPositionUpdate(velocity, position);
        ScrOptController.getInstance().getSceneManager().onFlingPositionUpdate();
    }

    public void onFlingStateUpdate(int state) {
        DynamicFrameRateController.getInstance().getAnimationSpeedAware().onFlingStateUpdate(state);
    }

    public void markOnStartScroll() {
        getViewDebugManager().markOnStartScroll();
    }

    public void markOnFling() {
        getViewDebugManager().markOnFling();
    }

    public long markOnUpdateStart(long startTime, long currentTime) {
        return getViewDebugManager().markOnUpdateStart(startTime, currentTime);
    }

    public void markOnUpdateSpline(int splineDuration, float t, long currentTime, int offset) {
        getViewDebugManager().markOnUpdateSpline(splineDuration, t, currentTime, offset);
    }

    public void markOnUpdateEnd() {
        getViewDebugManager().markOnUpdateEnd();
    }

    public IOplusViewDebugManager getViewDebugManager() {
        if (this.mOplusViewDebugManager == null) {
            this.mOplusViewDebugManager = (IOplusViewDebugManager) OplusFrameworkFactory.getInstance().getFeature(IOplusViewDebugManager.mDefault, new Object[0]);
        }
        return this.mOplusViewDebugManager;
    }
}
