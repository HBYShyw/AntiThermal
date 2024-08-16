package android.view.autolayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.policy.DecorView;

/* loaded from: classes.dex */
public class AutoLayoutCommonDrawPolicy implements IAutoLayoutDrawPolicy {
    @Override // android.view.autolayout.IAutoLayoutDrawPolicy
    public void beforeUpdateDisplayListIfDirty(View view) {
        if (view != null && (view.getRootView() instanceof DecorView)) {
            analyze(view);
        }
    }

    @Override // android.view.autolayout.IAutoLayoutDrawPolicy
    public void beforeDraw(View view, Canvas canvas) {
        AutoLayoutCanvas.getInstance(canvas).setView(view);
    }

    @Override // android.view.autolayout.IAutoLayoutDrawPolicy
    public void afterDraw(View view, Canvas canvas) {
    }

    @Override // android.view.autolayout.IAutoLayoutDrawPolicy
    public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
        AutoLayoutCanvas.getInstance().drawBitmap(bitmap, left, top, paint);
    }

    @Override // android.view.autolayout.IAutoLayoutDrawPolicy
    public void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {
        AutoLayoutCanvas.getInstance().drawBitmap(bitmap, matrix, paint);
    }

    @Override // android.view.autolayout.IAutoLayoutDrawPolicy
    public Rect drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint) {
        return AutoLayoutCanvas.getInstance().drawBitmap(bitmap, src, dst, paint);
    }

    @Override // android.view.autolayout.IAutoLayoutDrawPolicy
    public void drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint) {
        AutoLayoutCanvas.getInstance().drawBitmap(bitmap, src, dst, paint);
    }

    @Override // android.view.autolayout.IAutoLayoutDrawPolicy
    public void start() {
        AutoLayoutCanvas.getInstance().start();
    }

    @Override // android.view.autolayout.IAutoLayoutDrawPolicy
    public void end() {
        AutoLayoutCanvas.getInstance().end();
    }

    private void analyze(View view) {
        if (view.getVisibility() != 0) {
            return;
        }
        beforeAnalyze(view);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = group.getChildAt(i);
                analyze(child);
            }
        }
        afterAnalyze(view);
    }

    private void beforeAnalyze(View view) {
        handleViewWidth(view);
        handleViewAlignment(view);
        handleViewSize(view);
        handleViewPadding(view);
        handleViewMargin(view);
    }

    private void afterAnalyze(View view) {
    }

    private void handleViewWidth(View view) {
        if ((view instanceof ViewGroup) && view.getWidth() == 1920) {
            ViewGroup group = (ViewGroup) view;
            int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = group.getChildAt(i);
                if ((child instanceof ViewGroup) && Math.abs(1076 - Math.abs(child.getWidth())) < 100) {
                    child.layout(child.getLeft(), child.getTop(), group.getWidth() - child.getLeft(), child.getBottom());
                }
            }
        }
    }

    private void handleViewAlignment(View view) {
    }

    private void handleViewSize(View view) {
    }

    private void handleViewPadding(View view) {
    }

    private void handleViewMargin(View view) {
    }
}
