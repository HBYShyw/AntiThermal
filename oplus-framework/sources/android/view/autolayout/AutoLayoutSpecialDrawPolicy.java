package android.view.autolayout;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.android.internal.policy.DecorView;

/* loaded from: classes.dex */
public class AutoLayoutSpecialDrawPolicy implements IAutoLayoutDrawPolicy {
    @Override // android.view.autolayout.IAutoLayoutDrawPolicy
    public void beforeUpdateDisplayListIfDirty(View view) {
        if (view != null && (view.getRootView() instanceof DecorView)) {
            analyze(view);
        }
    }

    private boolean analyze(View view) {
        if (view.getVisibility() != 0) {
            return false;
        }
        beforeAnalyze(view);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = group.getChildAt(i);
                if (analyze(child)) {
                    return true;
                }
            }
        }
        return afterAnalyze(view);
    }

    private void beforeAnalyze(View view) {
    }

    private boolean afterAnalyze(View view) {
        if ((view instanceof WebView) || view.getClass().getSimpleName().contains("WebView")) {
            int displayWidth = AutoLayoutPolicyFactory.getUnFoldDisplayWidth();
            if (Math.abs(displayWidth - Math.abs(view.getWidth())) >= 100) {
                return view.getLeft() == 420 && view.getRight() == displayWidth - 420;
            }
            view.layout(420, view.getTop(), displayWidth - 420, view.getBottom());
            return true;
        }
        return false;
    }
}
