package android.view;

import android.content.pm.ApplicationInfo;
import android.content.pm.IApplicationInfoExt;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowInsets;
import com.android.internal.policy.DecorView;

/* loaded from: classes.dex */
public class OplusTaskBarUtils {
    public static final int OPLUS_TASKBAR_FORCE_MARGIN = 2;
    public static final int OPLUS_TASKBAR_FORCE_RELAYOUT = 8;
    public static final int OPLUS_TASKBAR_REAPPLY_INSET = 4;
    private static final String TAG = "OplusTaskBarUtils";
    private static final int TASKBAR_INSET_FLAG = Integer.MIN_VALUE;
    private static int sTaskBarFlag = 0;
    private int mLastTaskBarHeight;
    private int mTaskBarHeight;

    /* loaded from: classes.dex */
    private static final class OplusTaskBarHolder {
        private static final OplusTaskBarUtils INSTANCE = new OplusTaskBarUtils();

        private OplusTaskBarHolder() {
        }
    }

    private OplusTaskBarUtils() {
    }

    public static OplusTaskBarUtils getInstance() {
        return OplusTaskBarHolder.INSTANCE;
    }

    public void initTaskBarApplicationInfo(ApplicationInfo appInfo) {
        IApplicationInfoExt appInfoExt = appInfo == null ? null : appInfo.mApplicationInfoExt;
        if (appInfoExt != null) {
            sTaskBarFlag = appInfoExt.getTaskBarFlag();
        }
        if (sTaskBarFlag != 0) {
            Log.d(TAG, "initTaskBarApplicationInfo: " + appInfo + " " + flagToString(sTaskBarFlag));
        }
    }

    public void updateTaskBarInset(View view, WindowInsets insets, InsetsState insetsState) {
        if (view == null) {
            return;
        }
        fixWhiteBottomWhenSurfaceViewTop(view, insetsState);
        if (isTaskBar(view)) {
            if (reApplyInset(sTaskBarFlag)) {
                Log.d(TAG, "reApplyInset");
                view.onApplyWindowInsets(insets);
                view.requestLayout();
            }
            if (forceRelayout(sTaskBarFlag)) {
                int decorViewHeight = view.getHeight();
                Log.d(TAG, "forceRelayout: decorHeight =" + decorViewHeight);
                if (decorViewHeight != 0 && this.mTaskBarHeight != 0) {
                    forceLayout(view, decorViewHeight);
                }
                view.invalidate();
            }
        }
    }

    public int forceMarginByTaskBar(WindowInsets insets, DecorView decorView, int lastBottom) {
        if (!isMarginByTaskBar(sTaskBarFlag)) {
            return lastBottom;
        }
        if (decorView != null && decorView.getViewRootImpl() != null && decorView.getViewRootImpl().getInsetsController() != null) {
            InsetsState insetsState = decorView.getViewRootImpl().getInsetsController().getState();
            InsetsSource taskbar = insetsState != null ? insetsState.getWrapper().getExtImpl().peekNavigationBarSource(WindowInsets.Type.navigationBars(), Integer.MIN_VALUE) : null;
            if (insets != null && taskbar != null && taskbar.isVisible() && !taskbar.getFrame().isEmpty()) {
                Log.d(TAG, "forceMarginByTaskBar");
                return insets.getInsets(WindowInsets.Type.navigationBars()).bottom;
            }
        }
        return lastBottom;
    }

    private static void fixWhiteBottomWhenSurfaceViewTop(View view, InsetsState insetsState) {
        InsetsSource taskbar = insetsState != null ? insetsState.getWrapper().getExtImpl().peekNavigationBarSource(WindowInsets.Type.navigationBars(), Integer.MIN_VALUE) : null;
        if (taskbar != null && taskbar.isVisible() && !taskbar.getFrame().isEmpty() && view != null && view.getViewRootImpl() != null) {
            view.getViewRootImpl().requestInvalidateRootRenderNode();
        }
    }

    private boolean isSystemView(View view) {
        int id = view.getId();
        return id != -1 && ((-16777216) & id) == 16777216;
    }

    private boolean isTaskBar(View view) {
        if (sTaskBarFlag == 0) {
            return false;
        }
        if (view != null && view.getViewRootImpl() != null && view.getViewRootImpl().getInsetsController() != null) {
            InsetsState insetsState = view.getViewRootImpl().getInsetsController().getState();
            WindowInsets insets = view.getViewRootImpl().getWindowInsets(false);
            InsetsSource taskbar = insetsState != null ? insetsState.getWrapper().getExtImpl().peekNavigationBarSource(WindowInsets.Type.navigationBars(), Integer.MIN_VALUE) : null;
            if (insets != null && taskbar != null && taskbar.isVisible()) {
                Log.d(TAG, "viewRoot: " + ((Object) view.getViewRootImpl().getTitle()));
                int taskBarHeight = taskbar.getFrame().height();
                int i = this.mTaskBarHeight;
                if (taskBarHeight != i && taskBarHeight != 0) {
                    if (i == 0) {
                        i = taskBarHeight;
                    }
                    this.mLastTaskBarHeight = i;
                    this.mTaskBarHeight = taskBarHeight;
                }
                Log.d(TAG, "mTaskBarHeight = " + this.mTaskBarHeight + " mLastTaskBarHeight = " + this.mLastTaskBarHeight);
                return true;
            }
        }
        this.mTaskBarHeight = 0;
        this.mLastTaskBarHeight = 0;
        return false;
    }

    private void forceLayout(View view, int decorViewHeight) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!isSystemView(view) && layoutParams != null) {
            if ((layoutParams instanceof ViewGroup.MarginLayoutParams) && this.mLastTaskBarHeight == ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin) {
                Log.d(TAG, "forceLayout: magin " + view);
                ViewGroup.MarginLayoutParams ml = (ViewGroup.MarginLayoutParams) layoutParams;
                ml.bottomMargin = this.mTaskBarHeight;
                view.requestLayout();
            }
            if (layoutParams.height == decorViewHeight - this.mLastTaskBarHeight) {
                Log.d(TAG, "forceLayout: layout " + view);
                layoutParams.height = decorViewHeight - this.mTaskBarHeight;
                view.requestLayout();
            }
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                if (group.getChildAt(i) != null) {
                    forceLayout(group.getChildAt(i), decorViewHeight);
                }
            }
        }
    }

    private static boolean isMarginByTaskBar(int flag) {
        return (flag & 2) != 0;
    }

    private static boolean reApplyInset(int flag) {
        return (flag & 4) != 0;
    }

    private static boolean forceRelayout(int flag) {
        return (flag & 8) != 0;
    }

    private static String flagToString(int flag) {
        StringBuilder stringBuilder = new StringBuilder();
        if ((flag & 2) != 0) {
            stringBuilder.append(" OPLUS_TASKBAR_FORCE_MARGIN ").append(" ");
        }
        if ((flag & 4) != 0) {
            stringBuilder.append(" OPLUS_TASKBAR_REAPPLY_INSET ").append(" ");
        }
        if ((flag & 8) != 0) {
            stringBuilder.append(" OPLUS_TASKBAR_FORCE_RELAYOUT ").append(" ");
        }
        return stringBuilder.toString();
    }
}
