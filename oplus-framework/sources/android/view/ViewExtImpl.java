package android.view;

import android.common.OplusFeatureCache;
import android.common.OplusFrameworkFactory;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemProperties;
import android.text.ITextJustificationHooks;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.autolayout.AutoLayoutViewInfo;
import android.view.autolayout.IOplusAutoLayoutManager;
import android.view.debug.IOplusViewDebugManager;
import android.view.performance.IOplusViewPerfInjector;
import android.view.performance.OplusViewPerfInjector;
import android.view.rgbnormalize.IOplusRGBNormalizeManager;
import android.view.viewextract.IOplusViewExtractManager;
import android.webkit.WebView;
import com.oplus.bracket.OplusBracketModeManager;
import com.oplus.darkmode.IOplusDarkModeManager;
import com.oplus.debug.InputLog;
import com.oplus.dynamicframerate.DynamicFrameRateController;
import com.oplus.screenshot.OplusLongshotViewInfo;
import com.oplus.screenshot.OplusLongshotViewInt;
import com.oplus.scrolloptim.ScrOptController;
import com.oplus.util.OplusReflectDataUtils;
import com.oplus.view.IOplusScrollBarEffect;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class ViewExtImpl implements IViewExt, OplusLongshotViewInt {
    public static final int CRUDE_STATE_BACKGROUND = 1;
    private static final int DEPTH = 8;
    private String mAssignNullStack;
    private Field mFieldText;
    protected Layout mLayout;
    private RenderNode mMirrorRenderNode;
    private IOplusViewPerfInjector mOplusViewPerfInjector;
    public ITextJustificationHooks mTextJustificationHooksImpl;
    private View mView;
    protected IOplusViewHooks mViewHooks;
    private AutoLayoutViewInfo mViewInfo;
    private static final String TAG = ViewExtImpl.class.getSimpleName();
    private static final boolean mDebugWebView = SystemProperties.getBoolean("persist.sys.view.debug_webview", false);
    private static int mDebugVersion = -1;
    private boolean mKeepMeasureSpec = false;
    private int mCrudeState = 0;
    private int mOriginWebSettingForceDark = -1;
    private int mViewType = 0;
    private boolean mIsPreScrollConsumed = false;
    private boolean mHasInit = false;

    public ViewExtImpl(Object view) {
        this.mView = (View) view;
        this.mOplusViewPerfInjector = new OplusViewPerfInjector(this.mView);
    }

    public void initViewHooks(Resources r) {
        this.mViewHooks = (IOplusViewHooks) OplusFrameworkFactory.getInstance().getFeature(IOplusViewHooks.DEFAULT, new Object[]{this.mView, r});
    }

    public IOplusViewPerfInjector getOplusViewPerfInjector() {
        return this.mOplusViewPerfInjector;
    }

    public int getScrollX() {
        return this.mView.getViewWrapper().getScrollX();
    }

    public int getScrollY() {
        return this.mView.getViewWrapper().getScrollY();
    }

    protected void invalidateParentCaches() {
        this.mView.invalidateParentCaches();
    }

    protected boolean awakenScrollBars() {
        return this.mView.awakenScrollBars();
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.mView.onScrollChanged(l, t, oldl, oldt);
    }

    public void postInvalidateOnAnimation() {
        this.mView.postInvalidateOnAnimation();
    }

    protected void setValueScrollY(int value) {
        this.mView.getViewWrapper().setScrollY(value);
    }

    protected void setValueScrollX(int value) {
        this.mView.getViewWrapper().setScrollX(value);
    }

    public void setScrollXForColor(int x) {
        if (getScrollX() != x) {
            int oldX = getScrollX();
            setValueScrollX(x);
            invalidateParentCaches();
            onScrollChanged(getScrollX(), getScrollY(), oldX, getScrollY());
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        }
    }

    public void setScrollYForColor(int y) {
        if (getScrollY() != y) {
            int oldY = getScrollY();
            setValueScrollY(y);
            invalidateParentCaches();
            onScrollChanged(getScrollX(), getScrollY(), getScrollX(), oldY);
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        }
    }

    public boolean isOplusStyle() {
        IOplusViewHooks iOplusViewHooks = this.mViewHooks;
        return iOplusViewHooks != null && iOplusViewHooks.isOplusStyle();
    }

    public boolean isOplusOSStyle() {
        IOplusViewHooks iOplusViewHooks = this.mViewHooks;
        return iOplusViewHooks != null && iOplusViewHooks.isOplusOSStyle();
    }

    public void hookPerformClick() {
        IOplusViewHooks iOplusViewHooks = this.mViewHooks;
        if (iOplusViewHooks != null) {
            iOplusViewHooks.performClick();
        }
    }

    @Override // com.oplus.screenshot.OplusLongshotViewBase
    public int computeLongScrollRange() {
        return computeVerticalScrollRange();
    }

    protected int computeVerticalScrollRange() {
        return this.mView.computeVerticalScrollRange();
    }

    @Override // com.oplus.screenshot.OplusLongshotViewBase
    public int computeLongScrollOffset() {
        return computeVerticalScrollOffset();
    }

    protected int computeVerticalScrollOffset() {
        return this.mView.getViewWrapper().getScrollY();
    }

    @Override // com.oplus.screenshot.OplusLongshotViewBase
    public int computeLongScrollExtent() {
        return computeVerticalScrollExtent();
    }

    protected int computeVerticalScrollExtent() {
        return this.mView.computeVerticalScrollExtent();
    }

    @Override // com.oplus.screenshot.OplusLongshotViewBase
    public Context getContext() {
        return this.mView.getContext();
    }

    @Override // com.oplus.screenshot.OplusLongshotViewBase
    public boolean canLongScroll() {
        return canScrollVertically(1);
    }

    protected boolean canScrollVertically(int i) {
        return this.mView.canScrollVertically(i);
    }

    @Override // com.oplus.screenshot.OplusLongshotViewBase
    public boolean isLongshotVisibleToUser() {
        if (getVisibility() != 0) {
            return false;
        }
        return isVisibleToUser();
    }

    protected int getVisibility() {
        return this.mView.getVisibility();
    }

    protected boolean isVisibleToUser() {
        return this.mView.isVisibleToUser();
    }

    @Override // com.oplus.screenshot.OplusLongshotViewBase
    public boolean findViewsLongshotInfo(OplusLongshotViewInfo info) {
        return this.mViewHooks.findViewsLongshotInfo(info);
    }

    @Override // com.oplus.screenshot.OplusLongshotViewInt
    public void onLongshotOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
    }

    protected boolean hookOverScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        int maxOverScrollY2;
        boolean isLongshotConnected = this.mViewHooks.isLongshotConnected();
        if (!isLongshotConnected) {
            maxOverScrollY2 = maxOverScrollY;
        } else {
            maxOverScrollY2 = 0;
        }
        boolean result = overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY2, isTouchEvent);
        if (isLongshotConnected) {
            return this.mViewHooks.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY2, isTouchEvent, getScrollY(), result);
        }
        return result;
    }

    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return this.mView.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    public int getOplusViewType() {
        return this.mViewType;
    }

    public void setOplusViewTypeLocked(int viewType) {
        this.mViewType = viewType;
    }

    public void setOriginWebSettingForceDark(int forceDark) {
        this.mOriginWebSettingForceDark = forceDark;
    }

    public int getOriginWebSettingForceDark() {
        return this.mOriginWebSettingForceDark;
    }

    public void setCrudeState(int state) {
        this.mCrudeState = state;
    }

    public int getCrudeState() {
        return this.mCrudeState;
    }

    public void setUsageForceDarkAlgorithmType(int type) {
        RenderNode renderNode = getRenderNode();
        if (renderNode != null) {
            renderNode.mRenderNodeExt.setUsageForceDarkAlgorithmType(type);
            invalidate();
        }
    }

    protected RenderNode getRenderNode() {
        return this.mView.getViewWrapper().getRenderNode();
    }

    protected void invalidate() {
        this.mView.invalidate();
    }

    public void setParaSpacing(float add) {
        ITextJustificationHooks iTextJustificationHooks = this.mTextJustificationHooksImpl;
        if (iTextJustificationHooks != null) {
            iTextJustificationHooks.setTextViewParaSpacing(this, add, this.mLayout);
        }
    }

    public final boolean debugWebViewDraw() {
        if (this.mView instanceof WebView) {
            return mDebugWebView;
        }
        return false;
    }

    public float getParaSpacing() {
        ITextJustificationHooks iTextJustificationHooks = this.mTextJustificationHooksImpl;
        if (iTextJustificationHooks != null) {
            return iTextJustificationHooks.getTextViewParaSpacing(this);
        }
        return 0.0f;
    }

    public Bitmap getColorCustomDrawingCache(Rect clip, int viewTop) {
        return this.mViewHooks.getOplusCustomDrawingCache(clip, viewTop, this.mView.mPrivateFlags);
    }

    boolean isDebugVersion() {
        if (mDebugVersion == -1) {
            mDebugVersion = "1".equals(SystemProperties.get("persist.sys.agingtest")) ? 1 : 0;
        }
        return mDebugVersion == 1;
    }

    public void hookAssignParent(ViewParent parent) {
        if (parent == null && isDebugVersion()) {
            this.mAssignNullStack = "mParent:" + this.mView.getViewWrapper().getParent() + " parent:" + parent + " " + Debug.getCallers(8);
        }
    }

    public void hookStartDraw(View view, Canvas canvas) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).markOnDraw(view, canvas);
    }

    public void hookAfterDispatchDraw(View view, Canvas canvas) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).markDrawFadingEdge(view, canvas);
    }

    public void hookAfterDrawCanvas(View view, Canvas canvas) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).markForeground(view, canvas);
    }

    public void hookDrawBackground(View view, Canvas canvas) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).markBackground(view, canvas);
    }

    public void getDrawableRenderNode(View view, Canvas canvas) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).markBackground(view, canvas);
    }

    public void hookSizeChange(View view) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).markViewTypeBySize(view);
    }

    public void hookRequestLayout() {
        try {
            this.mView.getViewWrapper().getParent().requestLayout();
        } catch (NullPointerException e) {
            if (isDebugVersion()) {
                Log.e("View", "mAssignNullStack:" + this.mAssignNullStack);
            }
            throw e;
        }
    }

    public void hookOverScrollBy(int scrollX, int scrollY, int scrollRangeX, int scrollRangeY) {
        this.mViewHooks.getScrollBarEffect().onOverScrolled(scrollX, scrollY, scrollRangeX, scrollRangeY);
    }

    public void onTouchEvent(MotionEvent event) {
    }

    public boolean hookDispatchNestedScroll() {
        if (isOplusOSStyle()) {
            return this.mIsPreScrollConsumed;
        }
        return true;
    }

    public void hookTextView(Layout mlayout) {
        this.mTextJustificationHooksImpl = (ITextJustificationHooks) OplusFrameworkFactory.getInstance().getFeature(ITextJustificationHooks.DEFAULT, new Object[0]);
        this.mLayout = mlayout;
    }

    public float getTextViewDefaultLineMulti(Object textview, float pxSize, float oriValue) {
        return this.mTextJustificationHooksImpl.getTextViewDefaultLineMulti(textview, pxSize, oriValue);
    }

    public float getTextViewParaSpacing(Object textview) {
        return this.mTextJustificationHooksImpl.getTextViewParaSpacing(textview);
    }

    public void setLayout(Layout layout) {
        this.mLayout = layout;
    }

    public IViewRootImplExt getViewRootImpl() {
        if (this.mView.getViewRootImpl() != null) {
            return this.mView.getViewRootImpl().getWrapper().getExtImpl();
        }
        return null;
    }

    public SurfaceControl getViewRootSurfaceControl() {
        if (this.mView.getViewRootImpl() != null) {
            return this.mView.getViewRootImpl().getWrapper().getSurfaceControl();
        }
        return null;
    }

    public IOplusScrollBarEffect hookScrollBar() {
        return this.mViewHooks.getScrollBarEffect();
    }

    public boolean initialAwakenScrollBars() {
        return this.mViewHooks.needHook();
    }

    public boolean hookIsTouchPressed() {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setContentDescriptionForFieldAndSetRealClassName(AccessibilityNodeInfo info) {
        if (TextUtils.isEmpty(info.getText()) && TextUtils.isEmpty(info.getContentDescription())) {
            try {
                if (!this.mHasInit) {
                    this.mFieldText = OplusReflectDataUtils.getInstance().getTextField(getContext(), this.mView.getClass());
                    this.mHasInit = true;
                }
                Field field = this.mFieldText;
                if (field != null) {
                    info.setContentDescription((CharSequence) field.get(this.mView));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void logEvent(int level, String tag, InputEvent event, String info) {
        if (level != 1) {
            if (level == 2) {
                InputLog.v(tag, info + event);
                return;
            }
            if (level == 21) {
                if (event instanceof KeyEvent) {
                    KeyEvent kev = (KeyEvent) event;
                    if (InputLog.isVerboseAction(kev.getAction())) {
                        InputLog.v(tag, info + event);
                        return;
                    }
                    return;
                }
                if (event instanceof MotionEvent) {
                    MotionEvent mev = (MotionEvent) event;
                    if (InputLog.isVerboseAction(mev.getAction())) {
                        InputLog.v(tag, info + event);
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        InputLog.debugEventHandled(tag, event, info + (InputLog.isLevelDebug() ? this.mView : this.mView.getClass().getName()));
    }

    public View getView() {
        return this.mView;
    }

    public void initView() {
        this.mOplusViewPerfInjector.initView();
    }

    public void checkBoostAnimation(Animation animation) {
        this.mOplusViewPerfInjector.checkBoostAnimation(animation);
    }

    public void checkBoostBuildDrawingCache() {
        this.mOplusViewPerfInjector.checkBoostBuildDrawingCache();
    }

    public void checkBoostTouchEvent(int action) {
        this.mOplusViewPerfInjector.checkBoostTouchEvent(action);
    }

    public void checkBoostOnPerformClick(View.OnClickListener onClickListener) {
        this.mOplusViewPerfInjector.checkBoostOnPerformClick(onClickListener);
    }

    public void checkNeedBoostedPropertyAnimator(ViewPropertyAnimator animator) {
        this.mOplusViewPerfInjector.checkNeedBoostedPropertyAnimator(animator);
    }

    public void adjustImageViewLayerType(int width, int height) {
        this.mOplusViewPerfInjector.getOplusAdjustlayerTypeInstance().adjustImageViewLayerType(width, height);
    }

    public boolean checkMutiTouchView() {
        return this.mOplusViewPerfInjector.getOplusAdjustlayerTypeInstance().checkMutiTouchView();
    }

    public void ignoreSpecailViewDescendantInvalidated(ViewParent p) {
        this.mOplusViewPerfInjector.ignoreSpecailViewDescendantInvalidated(p);
    }

    public boolean isIgnoreSpecailViewDescendantInvalidated() {
        return this.mOplusViewPerfInjector.isIgnoreSpecailViewDescendantInvalidated();
    }

    public boolean disableOnClick(View view, InputEvent event) {
        return OplusBracketModeManager.getInstance().disableOnClick(view, event);
    }

    public boolean isClickDisabled() {
        if (this.mView.getViewRootImpl() != null) {
            return this.mView.getViewRootImpl().getWrapper().getExtImpl().isClickDisabled();
        }
        return false;
    }

    public void setOplusResampleTouch(boolean enabled) {
        Log.d("View", "setOplusResampleTouch, enabled = " + enabled);
        ViewRootImpl.WindowInputEventReceiver windowInputEventReceiver = null;
        if (this.mView.getViewRootImpl() != null) {
            windowInputEventReceiver = this.mView.getViewRootImpl().getWrapper().getInputEventReceiver();
        }
        if (windowInputEventReceiver != null) {
            windowInputEventReceiver.setOplusResampleTouch(enabled);
        } else {
            Log.w("View", "setOplusResampleTouch failed, windowInputEventReceiver is null");
        }
    }

    public boolean shouldFilterByMultiSearch(Resources resources) {
        return (resources == null || resources.getConfiguration().windowConfiguration == null || resources.getConfiguration().windowConfiguration.getWindowingMode() != 6) ? false : true;
    }

    public boolean drawWithMirrorModeIfNeeded(Canvas canvas) {
        if (this.mView.mAttachInfo != null && this.mView.mAttachInfo.mViewRootImpl != null && this.mView.mAttachInfo.mViewRootImpl.getWrapper().getExtImpl().shouldDrawOnMirrorContent(this.mView)) {
            Log.d("View", "drawWithMirrorModeIfNeeded " + this.mView);
            if (this.mMirrorRenderNode == null) {
                this.mMirrorRenderNode = RenderNode.create(getClass().getName(), new ViewAnimationHostBridge(this.mView));
            }
            ((RecordingCanvas) canvas).drawRenderNode(this.mMirrorRenderNode);
            OplusViewMirrorManager.getInstance().setHostViewRefresh(true);
            return true;
        }
        return false;
    }

    public void dispatchAttachedToWindow() {
        if (this.mView != null) {
            OplusViewMirrorManager.getInstance().markTargetViewIfNeed(this.mView.getViewRootImpl(), this.mView);
        }
    }

    public void dispatchDetachedFromWindow() {
        if (this.mView != null) {
            OplusViewMirrorManager.getInstance().clearTargetViewIfNeed(this.mView.getViewRootImpl(), this.mView);
        }
    }

    public void beforeUpdateDisplayListIfDirty(View view) {
        getAutoLayoutManager().beforeUpdateDisplayListIfDirty(view);
    }

    public void beforeDraw(View view, Canvas canvas) {
        getAutoLayoutManager().beforeDraw(view, canvas);
        getRgbNormalizeManager().beforeDraw(view);
    }

    public void afterDraw(View view, Canvas canvas) {
        getAutoLayoutManager().afterDraw(view, canvas);
    }

    public int[] beforeMeasure(View view, int widthMeasureSpec, int heightMeasureSpec) {
        getViewDebugManager().markMeasureStart(view, widthMeasureSpec, heightMeasureSpec);
        return getAutoLayoutManager().beforeMeasure(view, widthMeasureSpec, heightMeasureSpec);
    }

    public int[] hookSetMeasureDimension(View view, int measuredWidth, int measuredHeight) {
        return getAutoLayoutManager().hookSetMeasureDimension(view, measuredWidth, measuredHeight);
    }

    public void afterMeasure(View view) {
        getAutoLayoutManager().afterMeasure(view);
        OplusFlexibleViewManager.getInstance().hookAfterMeasure(view);
    }

    public int[] beforeLayout(View view, int l, int t, int r, int b) {
        return getAutoLayoutManager().beforeLayout(view, l, t, r, b);
    }

    public void afterLayout(View view) {
        getAutoLayoutManager().afterLayout(view);
    }

    public Object getViewInfo() {
        if (this.mViewInfo == null) {
            this.mViewInfo = new AutoLayoutViewInfo();
        }
        return this.mViewInfo;
    }

    public String getViewInfoStr() {
        return getAutoLayoutManager().dumpString(getViewInfo());
    }

    public void setKeepMeasureSpec(boolean keep) {
        this.mKeepMeasureSpec = keep;
    }

    public boolean shouldKeepMeasureSpec() {
        return this.mKeepMeasureSpec;
    }

    public ViewGroup.LayoutParams hookSetLayoutParams(ViewGroup.LayoutParams params) {
        return getAutoLayoutManager().hookSetLayoutParams(params);
    }

    private IOplusAutoLayoutManager getAutoLayoutManager() {
        return (IOplusAutoLayoutManager) OplusFeatureCache.getOrCreate(IOplusAutoLayoutManager.mDefault, new Object[0]);
    }

    private IOplusRGBNormalizeManager getRgbNormalizeManager() {
        return (IOplusRGBNormalizeManager) OplusFeatureCache.getOrCreate(IOplusRGBNormalizeManager.DEFAULT, new Object[0]);
    }

    public void onEventHandled(Object vri, MotionEvent ev) {
        DynamicFrameRateController.getInstance().getAnimationSpeedAware().onEventHandled(vri, ev);
        ScrOptController.getInstance().getSceneManager().onEventHandled(ev);
    }

    public void onScrollBarFadeStart(int duration) {
        DynamicFrameRateController.getInstance().getAnimationSpeedAware().onScrollBarFadeStart(duration);
    }

    public void onScrollBarFadeEnd() {
        DynamicFrameRateController.getInstance().getAnimationSpeedAware().onScrollBarFadeEnd();
    }

    public void onScrollChangedHook(int l, int t, int oldl, int oldt) {
        DynamicFrameRateController.getInstance().getAnimationSpeedAware().onScrollChanged(l, t, oldl, oldt);
        ScrOptController.getInstance().getSceneManager().onScrollChanged();
    }

    public void markOnRequestLayout() {
        getViewDebugManager().markOnRequestLayout();
    }

    public void markOnInvalidate() {
        getViewDebugManager().markOnInvalidate();
    }

    public void markOnFocusChange(boolean gainFocus, boolean hasWindowFocus, View view) {
        getViewDebugManager().markOnFocusChange(gainFocus, hasWindowFocus, view);
    }

    public IOplusViewDebugManager getViewDebugManager() {
        return (IOplusViewDebugManager) OplusFeatureCache.getOrCreate(IOplusViewDebugManager.mDefault, new Object[0]);
    }

    public boolean isInMirageDisplayMode(View.AttachInfo attachInfo) {
        Display display;
        if (attachInfo == null || (display = attachInfo.mDisplay) == null) {
            return false;
        }
        boolean mirageDisplayIsAdded = false;
        int displayId = display.getDisplayId();
        String displayName = display.getName();
        boolean currDisplayIsMirageDisplay = displayId >= 10000 && "Mirage_car_display".equals(displayName);
        try {
            mirageDisplayIsAdded = this.mView.getViewRootImpl().getWrapper().getExtImpl().isMirageDisplayAdded();
        } catch (Exception e) {
            Log.d(TAG, "Failed to get MirageDisplay Status " + e.getMessage());
        }
        return currDisplayIsMirageDisplay || mirageDisplayIsAdded;
    }

    public void extractViewInfo(View view, Bundle bundle) {
        ((IOplusViewExtractManager) OplusFeatureCache.getOrCreate(IOplusViewExtractManager.DEFAULT, new Object[0])).extractViewInfo(view, bundle);
    }

    public void initViewExtract(View view) {
        ((IOplusViewExtractManager) OplusFeatureCache.getOrCreate(IOplusViewExtractManager.DEFAULT, new Object[0])).initViewExtract(view);
    }

    public void appendViewExtractInfo(View view, ViewStructure info) {
        ((IOplusViewExtractManager) OplusFeatureCache.getOrCreate(IOplusViewExtractManager.DEFAULT, new Object[0])).appendViewExtractInfo(view, info);
    }

    public boolean isViewExtract(int flag) {
        return ((IOplusViewExtractManager) OplusFeatureCache.getOrCreate(IOplusViewExtractManager.DEFAULT, new Object[0])).isViewExtract(flag);
    }

    public int addViewExtractFlag(int flag, int viewFlags) {
        return ((IOplusViewExtractManager) OplusFeatureCache.getOrCreate(IOplusViewExtractManager.DEFAULT, new Object[0])).addViewExtractFlag(flag, viewFlags);
    }

    public void modifyOutShadowMetricsIfNeeded(Context context, Point outShadowMetrics, Point shadowSize) {
        String packageName = context.getPackageName();
        if (packageName.contains("cts")) {
            Log.d(TAG, "Skip modifyOutShadowMetricsIfNeeded since running in a cts process");
            return;
        }
        String str = TAG;
        Log.d(str, "start modifyOutShadowMetricsIfNeeded with origin " + outShadowMetrics);
        int offsetX = (int) (shadowSize.x * 0.1f);
        int offsetY = (int) (shadowSize.y * 0.1f);
        outShadowMetrics.set((shadowSize.x / 2) + offsetX, shadowSize.y + offsetY);
        Log.d(str, "modifyOutShadowMetricsIfNeeded to " + outShadowMetrics);
    }

    public CharSequence getRealClassName(AccessibilityNodeInfo nodeinfo) {
        if (nodeinfo.hasExtras()) {
            return nodeinfo.getExtras().getCharSequence("realClassName");
        }
        return null;
    }

    public void setRealClassName(CharSequence className, AccessibilityNodeInfo nodeinfo) {
        Bundle extras = nodeinfo.getExtras();
        if (extras != null) {
            extras.putCharSequence("realClassName", className);
        }
    }
}
