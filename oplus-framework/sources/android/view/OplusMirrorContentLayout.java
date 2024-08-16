package android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RenderNode;
import android.util.AttributeSet;
import com.oplus.bracket.OplusBracketLog;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class OplusMirrorContentLayout extends View {
    private static final String TAG = "OplusMirrorContentLayout";
    private CopyOnWriteArrayList<View> mSortedTargetViews;
    private CopyOnWriteArrayList<View> mTargetViews;
    private boolean mViewSizeLimit;

    public OplusMirrorContentLayout(Context context) {
        this(context, null);
    }

    public OplusMirrorContentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusMirrorContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mViewSizeLimit = false;
        this.mTargetViews = new CopyOnWriteArrayList<>();
        this.mSortedTargetViews = new CopyOnWriteArrayList<>();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!this.mTargetViews.isEmpty()) {
            int size = this.mTargetViews.size();
            for (int i = 0; i < size; i++) {
                View view = this.mTargetViews.get(i);
                RenderNode renderNode = view.updateDisplayListIfDirty();
                OplusBracketLog.d(TAG, "Mirror content start drawing:" + view + ", renderNode.hasDisplayList():" + renderNode.hasDisplayList() + ", viewSizeLimit:" + this.mViewSizeLimit);
                if (renderNode.hasDisplayList() && OplusViewMirrorUtils.checkViewSizeConstraints(this.mViewSizeLimit, view, getViewRootImpl())) {
                    canvas.enableZ();
                    canvas.drawRenderNode(renderNode);
                    canvas.disableZ();
                }
                OplusBracketLog.i(TAG, "Mirror content end drawing:" + view);
            }
        }
    }

    public void addTargetView(View view) {
        this.mTargetViews.add(view);
        OplusBracketLog.i(TAG, "addTargetView:" + view);
    }

    public void addTargetViews(CopyOnWriteArrayList<View> targetViews) {
        this.mTargetViews.addAll(targetViews);
        Iterator<View> it = this.mTargetViews.iterator();
        while (it.hasNext()) {
            View view = it.next();
            OplusBracketLog.i(TAG, "addTargetView:" + view);
        }
    }

    public void removeTargetView(View view) {
        if (this.mTargetViews.contains(view)) {
            this.mTargetViews.remove(view);
        }
    }

    public void release() {
        this.mTargetViews.clear();
    }

    public void updateViewByOrder(View view) {
        if (this.mTargetViews.contains(view) && !this.mSortedTargetViews.contains(view)) {
            OplusBracketLog.d(TAG, "updateViewByOrder view:" + view);
            this.mSortedTargetViews.add(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setViewSizeLimit(boolean viewSizeLimit) {
        this.mViewSizeLimit = viewSizeLimit;
    }
}
