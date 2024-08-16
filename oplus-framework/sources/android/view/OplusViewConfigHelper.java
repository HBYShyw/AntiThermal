package android.view;

import android.content.Context;
import android.util.Log;
import com.oplus.util.OplusContextUtil;

/* loaded from: classes.dex */
public class OplusViewConfigHelper implements IOplusViewConfigHelper {
    private int mColorOverDist;
    private boolean mForceUsingSpring;
    private boolean mIsColorStyle;
    private boolean mOptEnable;

    public OplusViewConfigHelper(Context context) {
        this.mColorOverDist = context.getResources().getDisplayMetrics().heightPixels;
        this.mIsColorStyle = OplusContextUtil.isOplusOSStyle(context);
    }

    @Override // android.view.IOplusViewConfigHelper
    public int getScaledOverscrollDistance(int dist) {
        if (this.mIsColorStyle && !this.mOptEnable && this.mForceUsingSpring) {
            Log.d("TestOverScroll", "getScaledOverscrollDistance: a mColorOverDist: " + this.mColorOverDist);
            return this.mColorOverDist;
        }
        Log.d("TestOverScroll", "getScaledOverscrollDistance: b");
        return dist;
    }

    @Override // android.view.IOplusViewConfigHelper
    public int getScaledOverflingDistance(int dist) {
        if (this.mIsColorStyle && !this.mOptEnable && this.mForceUsingSpring) {
            return this.mColorOverDist;
        }
        return dist;
    }

    @Override // android.view.IOplusViewConfigHelper
    public int calcRealOverScrollDist(int dist, int scrollY) {
        if (this.mIsColorStyle && !this.mOptEnable && this.mForceUsingSpring) {
            Log.d("TestOverScroll", "calcRealOverScrollDist: a-scrollY: " + scrollY);
            return (int) ((dist * (1.0f - ((Math.abs(scrollY) * 1.0f) / this.mColorOverDist))) / 3.0f);
        }
        return dist;
    }

    @Override // android.view.IOplusViewConfigHelper
    public int calcRealOverScrollDist(int dist, int scrollY, int range) {
        if (this.mIsColorStyle && !this.mOptEnable && this.mForceUsingSpring && (scrollY < 0 || scrollY > range)) {
            int overScrollY = scrollY;
            if (scrollY > range) {
                overScrollY = scrollY - range;
            }
            Log.d("TestOverScroll", "calcRealOverScrollDist: b-scrollY: " + scrollY);
            return (int) ((dist * (1.0f - ((Math.abs(overScrollY) * 1.0f) / this.mColorOverDist))) / 3.0f);
        }
        return dist;
    }

    @Override // android.view.IOplusViewConfigHelper
    public void setOptEnable(boolean enable) {
        this.mOptEnable = enable;
    }

    @Override // android.view.IOplusViewConfigHelper
    public void setForceUsingSpring(boolean enable) {
        this.mForceUsingSpring = enable;
    }
}
