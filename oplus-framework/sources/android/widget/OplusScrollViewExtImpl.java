package android.widget;

import android.common.OplusFeatureCache;
import android.common.OplusFrameworkFactory;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.debug.IOplusViewDebugManager;
import com.oplus.util.OplusContextUtil;

/* loaded from: classes.dex */
public class OplusScrollViewExtImpl implements IOplusScrollViewExt {
    private static final String TAG = "ScrollView";
    private static boolean sForceUsingSpring;
    private static boolean sOptHelperEnable = ((IOplusScrollOptimizationHelper) OplusFrameworkFactory.getInstance().getFeature(IOplusScrollOptimizationHelper.DEFAULT, new Object[0])).enable();
    private Context mContext;

    public OplusScrollViewExtImpl(Object base) {
    }

    public OverScroller createSpringOverScrollerInstance(Context context) {
        this.mContext = context;
        return SpringOverScroller.newInstance(context, true);
    }

    public void onOverScrolled(OverScroller scroller, int scrollRange, int x, int y) {
        if (isOplusOSStyle()) {
            scroller.springBack(x, y, 0, 0, 0, scrollRange);
        }
    }

    public boolean canFling(boolean canFling) {
        return canFling || isOplusOSStyle();
    }

    public boolean shouldDisplayEdgeEffects(boolean shouldDisplay) {
        return shouldDisplay && !isOplusOSStyle();
    }

    private boolean isOplusOSStyle() {
        return OplusContextUtil.isOplusOSStyle(this.mContext);
    }

    public void hookInitScrollView(Context context) {
        this.mContext = context;
        String packageName = context.getPackageName();
        try {
            String packageList = Settings.System.getString(context.getContentResolver(), "spring_overscroller_package_list");
            if (packageList != null) {
                sForceUsingSpring = packageList.contains(packageName);
            }
        } catch (Throwable th) {
            Log.d(TAG, toString());
        }
    }

    public void markOnTouchEventMove(int y, int deltaY, int lastMotionY) {
        getViewDebugManager().markOnTouchEventMove(y, deltaY, lastMotionY);
    }

    public void markOnOverScrolled(int deltaY) {
        getViewDebugManager().markOnOverScrolled(deltaY);
    }

    public IOplusViewDebugManager getViewDebugManager() {
        return (IOplusViewDebugManager) OplusFeatureCache.getOrCreate(IOplusViewDebugManager.mDefault, new Object[0]);
    }
}
