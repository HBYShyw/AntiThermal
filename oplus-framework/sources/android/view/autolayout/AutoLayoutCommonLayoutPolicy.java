package android.view.autolayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class AutoLayoutCommonLayoutPolicy implements IAutoLayoutLayoutPolicy {
    private static final String TAG;
    private static final String THIS;

    static {
        String simpleName = AutoLayoutCommonLayoutPolicy.class.getSimpleName();
        TAG = simpleName;
        THIS = simpleName;
    }

    @Override // android.view.autolayout.IAutoLayoutLayoutPolicy
    public int[] beforeLayout(View view, int l, int t, int r, int b) {
        return new int[]{l, t, r, b};
    }

    @Override // android.view.autolayout.IAutoLayoutLayoutPolicy
    public void afterLayout(View view) {
        if (view.getVisibility() == 0 && (view instanceof ImageView)) {
            ImageView imageView = (ImageView) view;
            if (imageView.getTop() < 0) {
                handleTopBannerImageView(imageView);
            }
        }
    }

    @Override // android.view.autolayout.IAutoLayoutLayoutPolicy
    public ViewGroup.LayoutParams setLayoutParams(ViewGroup.LayoutParams params) {
        return params;
    }

    private void handleTopBannerImageView(ImageView imageView) {
        int topOffset = Math.abs(imageView.getTop());
        imageView.setLeftTopRightBottom(imageView.getLeft(), 0, imageView.getRight(), imageView.getBottom());
        int newMeasureSpecHeight = View.MeasureSpec.makeMeasureSpec(imageView.getMeasuredHeight() - topOffset, 1073741824);
        int newMeasureSpecWidth = View.MeasureSpec.makeMeasureSpec(imageView.getMeasuredWidth(), 1073741824);
        imageView.measure(newMeasureSpecWidth, newMeasureSpecHeight);
    }
}
