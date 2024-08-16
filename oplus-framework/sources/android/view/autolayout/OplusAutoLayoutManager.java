package android.view.autolayout;

import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.Thread;
import java.util.List;

/* loaded from: classes.dex */
public class OplusAutoLayoutManager implements IOplusAutoLayoutManager {
    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void hookHandleBindApplication(ApplicationInfo appInfo, Context context) {
        AutoLayoutPolicyFactory.initAutoLayoutApplicationInfo(appInfo, context);
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void hookPerformLaunchActivity(ActivityInfo activityInfo, Configuration configuration) {
        if (!isAppInAutoLayoutList()) {
            return;
        }
        AutoLayoutPolicyFactory.setCurrentPolicy(AutoLayoutPolicyFactory.getActivityPolicy(activityInfo, configuration, 3).intValue());
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void hookPerformResumeActivity(ActivityInfo activityInfo, Configuration configuration) {
        if (!isAppInAutoLayoutList()) {
            return;
        }
        AutoLayoutPolicyFactory.setCurrentPolicy(AutoLayoutPolicyFactory.getActivityPolicy(activityInfo, configuration, 2).intValue());
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void hookConfigurationChangedActivity(ActivityInfo activityInfo, Configuration configuration) {
        if (!isAppInAutoLayoutList()) {
            return;
        }
        AutoLayoutPolicyFactory.setCurrentPolicy(AutoLayoutPolicyFactory.getActivityPolicy(activityInfo, configuration, 1).intValue());
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void handleBindApplication() {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: android.view.autolayout.OplusAutoLayoutManager.1
            @Override // java.lang.Thread.UncaughtExceptionHandler
            public void uncaughtException(Thread t, Throwable e) {
                AutoLayoutDebug.onCrash(e);
            }
        });
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public DisplayMetrics getAutoLayoutDisplayMetrics(DisplayMetrics originalMetrics) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutPolicyFactory.getPolicy(3).getMeasurePolicy().setOriginalDisplayMetrics(originalMetrics);
            return modifyAutoLayoutMetrics(originalMetrics);
        }
        return originalMetrics;
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void beforeUpdateDisplayListIfDirty(View view) {
        if (isInAutoLayoutList()) {
            AutoLayoutDebug.startTraceSection("#beforeUpdateDisplayListIfDirty");
            if (isContainNormalType()) {
                AutoLayoutPolicyFactory.getPolicy(3).getDrawPolicy().beforeUpdateDisplayListIfDirty(view);
            }
            if (isContainSpecialType()) {
                AutoLayoutPolicyFactory.getPolicy(4).getDrawPolicy().beforeUpdateDisplayListIfDirty(view);
            }
            AutoLayoutDebug.endTraceSection();
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void beforeDraw(View view, Canvas canvas) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutDebug.startTraceSection("#beforeDraw");
            AutoLayoutPolicyFactory.getPolicy(3).getDrawPolicy().beforeDraw(view, canvas);
            AutoLayoutDebug.endTraceSection();
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void afterDraw(View view, Canvas canvas) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutDebug.startTraceSection("#afterDraw");
            AutoLayoutPolicyFactory.getPolicy(3).getDrawPolicy().afterDraw(view, canvas);
            AutoLayoutDebug.endTraceSection();
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public int[] beforeMeasure(View view, int widthMeasureSpec, int heightMeasureSpec) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            return AutoLayoutPolicyFactory.getPolicy(3).getMeasurePolicy().beforeMeasure(view, widthMeasureSpec, heightMeasureSpec);
        }
        return new int[]{widthMeasureSpec, heightMeasureSpec};
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public int[] hookSetMeasureDimension(View view, int measuredWidth, int measuredHeight) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            return AutoLayoutPolicyFactory.getPolicy(3).getMeasurePolicy().hookSetMeasureDimension(view, measuredWidth, measuredHeight);
        }
        return new int[]{measuredWidth, measuredHeight};
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void afterMeasure(View view) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutDebug.startTraceSection("#afterMeasure");
            AutoLayoutPolicyFactory.getPolicy(3).getMeasurePolicy().afterMeasure(view);
            AutoLayoutDebug.endTraceSection();
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public int[] beforeLayout(View view, int l, int t, int r, int b) {
        int[] layoutResult = {l, t, r, b};
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutDebug.startTraceSection("#beforeLayout");
            int[] layoutResult2 = AutoLayoutPolicyFactory.getPolicy(3).getLayoutPolicy().beforeLayout(view, l, t, r, b);
            AutoLayoutDebug.endTraceSection();
            return layoutResult2;
        }
        return layoutResult;
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void afterLayout(View view) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutDebug.startTraceSection("#afterLayout");
            AutoLayoutPolicyFactory.getPolicy(3).getLayoutPolicy().afterLayout(view);
            AutoLayoutDebug.endTraceSection();
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void setTo(Configuration configuration) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            float density = configuration.densityDpi / 160.0f;
            configuration.screenWidthDp = (int) (AutoLayoutPolicyFactory.getAutoDisplayWidth() / density);
            configuration.smallestScreenWidthDp = (int) (AutoLayoutPolicyFactory.getAutoDisplayWidth() / density);
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void updateFrom(Configuration configuration) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            float density = configuration.densityDpi / 160.0f;
            configuration.screenWidthDp = (int) (AutoLayoutPolicyFactory.getAutoDisplayWidth() / density);
            configuration.smallestScreenWidthDp = (int) (AutoLayoutPolicyFactory.getAutoDisplayWidth() / density);
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public ViewGroup.LayoutParams hookSetLayoutParams(ViewGroup.LayoutParams params) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            return AutoLayoutPolicyFactory.getPolicy(3).getLayoutPolicy().setLayoutParams(params);
        }
        return params;
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutDebug.startTraceSection("#drawBitmap 1");
            AutoLayoutPolicyFactory.getPolicy(3).getDrawPolicy().drawBitmap(bitmap, left, top, paint);
            AutoLayoutDebug.endTraceSection();
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutDebug.startTraceSection("#drawBitmap 2");
            AutoLayoutPolicyFactory.getPolicy(3).getDrawPolicy().drawBitmap(bitmap, matrix, paint);
            AutoLayoutDebug.endTraceSection();
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public Rect drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutDebug.startTraceSection("#drawBitmap 3");
            Rect result = AutoLayoutPolicyFactory.getPolicy(3).getDrawPolicy().drawBitmap(bitmap, src, dst, paint);
            AutoLayoutDebug.endTraceSection();
            return result;
        }
        return dst;
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint) {
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutDebug.startTraceSection("#drawBitmap 4");
            AutoLayoutPolicyFactory.getPolicy(3).getDrawPolicy().drawBitmap(bitmap, src, dst, paint);
            AutoLayoutDebug.endTraceSection();
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void start() {
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutPolicyFactory.getPolicy(3).getDrawPolicy().start();
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public void end() {
        if (isInAutoLayoutList() && isContainNormalType()) {
            AutoLayoutPolicyFactory.getPolicy(3).getDrawPolicy().end();
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public ImageView.ScaleType modifyScaleType(ImageView.ScaleType scaleType) {
        if (isInAutoLayoutList() && isContainNormalType() && scaleType == ImageView.ScaleType.CENTER_CROP) {
            return ImageView.ScaleType.CENTER_INSIDE;
        }
        return scaleType;
    }

    @Override // android.view.autolayout.IOplusAutoLayoutManager
    public String dumpString(Object viewInfo) {
        if (isInAutoLayoutList() || AutoLayoutDebug.isDebug()) {
            return viewInfo.toString();
        }
        return "";
    }

    @Override // android.view.autolayout.IOplusAutoLayout2Manager
    public Bundle autoLayoutCall(Bundle bundle) {
        if (AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed()) {
            return AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().autoLayoutCall(bundle);
        }
        return null;
    }

    @Override // android.view.autolayout.IOplusAutoLayout2Manager
    public boolean checkIfHasCover(Context context, String packageName, String signFun, String filePath, byte[] fileSignature, boolean toDelete) {
        return AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed() && AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().checkIfHasCover(context, packageName, signFun, filePath, fileSignature, toDelete);
    }

    @Override // android.view.autolayout.IOplusAutoLayout2Manager
    public void makeCover(Context context, String packageName) {
        if (AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed()) {
            AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().makeCover(context, packageName);
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayout2Manager
    public void preMakePaths(ApplicationInfo aInfo, List<String> outZipPaths) {
        if (AutoLayoutPolicyFactory.getIsNeedCover() && AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed()) {
            AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().preMakePaths(aInfo, outZipPaths);
        }
    }

    @Override // android.view.autolayout.IOplusAutoLayout2Manager
    public void hookApplication(Application app) {
        if (AutoLayoutPolicyFactory.getIsNeedCover() && AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed()) {
            AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().hookApplication(app);
        }
    }

    public void onAttachedToWindow(View recyclerView, Object layoutManager) {
        if (AutoLayoutPolicyFactory.hasCover() && AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed()) {
            AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().onAttachedToWindow(recyclerView, layoutManager);
        }
    }

    public int getSpanSize(Object obj, int in) {
        if (AutoLayoutPolicyFactory.hasCover()) {
            return AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed() ? AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().getSpanSize(obj, in) : in;
        }
        return in;
    }

    public boolean checkFindItemPositions(Object obj, int[] iArr) {
        return AutoLayoutPolicyFactory.hasCover() && AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed() && AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().checkFindItemPositions(obj, iArr);
    }

    public int[] findFirstVisibleItemPositions(Object obj, int[] iArr) {
        if (AutoLayoutPolicyFactory.hasCover()) {
            return AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed() ? AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().findFirstVisibleItemPositions(obj, iArr) : iArr;
        }
        return iArr;
    }

    public int[] findFirstCompletelyVisibleItemPositions(Object obj, int[] iArr) {
        if (AutoLayoutPolicyFactory.hasCover()) {
            return AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed() ? AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().findFirstCompletelyVisibleItemPositions(obj, iArr) : iArr;
        }
        return iArr;
    }

    public int[] findLastVisibleItemPositions(Object obj, int[] iArr) {
        if (AutoLayoutPolicyFactory.hasCover()) {
            return AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed() ? AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().findLastVisibleItemPositions(obj, iArr) : iArr;
        }
        return iArr;
    }

    public int[] findLastCompletelyVisibleItemPositions(Object obj, int[] iArr) {
        if (AutoLayoutPolicyFactory.hasCover()) {
            return AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed() ? AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().findLastCompletelyVisibleItemPositions(obj, iArr) : iArr;
        }
        return iArr;
    }

    public void getItemOffsets(Rect outRect, View view, View parent, Object state) {
        if (AutoLayoutPolicyFactory.hasCover() && AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed()) {
            AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().getItemOffsets(outRect, view, parent, state);
        }
    }

    public void attachViewPagerToWindow(View viewPager) {
        if (AutoLayoutPolicyFactory.hasCover() && AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed()) {
            AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().attachViewPagerToWindow(viewPager);
        }
    }

    public DisplayMetrics setDisplayMetrics(DisplayMetrics dm) {
        if (AutoLayoutPolicyFactory.hasCover()) {
            return AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed() ? AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().setDisplayMetrics(dm) : dm;
        }
        return dm;
    }

    public void onTinkerSuccess(Object obj) {
        if (AutoLayoutPolicyFactory.hasCover() && AutoLayoutPolicyFactory.createAutoLayout2MgrImplIfNeed()) {
            AutoLayoutPolicyFactory.getAutoLayout2MgrImpl().onTinkerSuccess(obj);
        }
    }

    private DisplayMetrics modifyAutoLayoutMetrics(DisplayMetrics metrics) {
        DisplayMetrics autoLayoutDisplayMetrics = new DisplayMetrics();
        autoLayoutDisplayMetrics.setTo(metrics);
        autoLayoutDisplayMetrics.widthPixels = AutoLayoutPolicyFactory.getAutoDisplayWidth();
        autoLayoutDisplayMetrics.noncompatWidthPixels = AutoLayoutPolicyFactory.getAutoDisplayWidth();
        return autoLayoutDisplayMetrics;
    }

    private boolean isInAutoLayoutList() {
        return AutoLayoutPolicyFactory.getCurrentPolicy() != -1;
    }

    private boolean isContainNormalType() {
        return (AutoLayoutPolicyFactory.getCurrentPolicy() & 1) != 0;
    }

    private boolean isContainWidgetType() {
        return (AutoLayoutPolicyFactory.getCurrentPolicy() & 2) != 0;
    }

    private boolean isContainSpecialType() {
        return (AutoLayoutPolicyFactory.getCurrentPolicy() & 4) != 0;
    }

    private boolean isAppInAutoLayoutList() {
        return AutoLayoutPolicyFactory.getIsAppInAutoLayoutList();
    }
}
