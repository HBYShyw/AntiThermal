package android.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ScrollView;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.view.ScrollCaptureInternal;
import com.android.internal.view.ScrollCaptureViewSupport;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import com.oplus.screenshot.IOplusScrollCaptureResponseListener;
import com.oplus.screenshot.OplusLongshotComponentName;
import com.oplus.screenshot.OplusLongshotDump;
import com.oplus.screenshot.OplusLongshotUtils;
import com.oplus.screenshot.OplusLongshotViewBase;
import com.oplus.screenshot.OplusLongshotViewInfo;
import com.oplus.screenshot.OplusScreenshotManager;
import com.oplus.screenshot.OplusScrollCaptureConnectionInner;
import com.oplus.screenshot.OplusScrollCaptureResponse;
import com.oplus.screenshot.OplusScrollCaptureResponseInner;
import com.oplus.screenshot.OplusScrollCaptureSearchResults;
import com.oplus.screenshot.OplusScrollCaptureViewSupport;
import com.oplus.util.OplusLog;
import com.oplus.util.OplusTypeCastingHelper;
import com.oplus.view.analysis.OplusWindowNode;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import libcore.io.IoUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class OplusLongshotViewDump {
    private static final ContentComparator CONTENT_COMPARATOR = new ContentComparator();
    private static final boolean DBG = true;
    private static final boolean FEATURE_ADJUST_WINDOW = false;
    private static final boolean FEATURE_DUMP_MIN_SIZE = false;
    private static final String JSON_BOUNDS_IN_WINDOW = "bounds_in_window";
    private static final String JSON_CAPTURE_SCROLL_LIST = "scroll_capture";
    private static final String JSON_CHILD_HASH = "child_hash";
    private static final String JSON_CHILD_LIST = "child_list";
    private static final String JSON_CHILD_RECT_CLIP = "child_rect_clip";
    private static final String JSON_CHILD_RECT_FULL = "child_rect_full";
    private static final String JSON_CHILD_SCROLLY = "child_scrollY";
    private static final String JSON_FLOAT_LIST = "float_list";
    private static final String JSON_FLOAT_RECT = "float_rect";
    private static final String JSON_PACAKGE_NAME = "package_name";
    private static final String JSON_PARENT_HASH = "parent_hash";
    private static final String JSON_PARENT_RECT_CLIP = "parent_rect_clip";
    private static final String JSON_PARENT_RECT_FULL = "parent_rect_full";
    private static final String JSON_SCROLL_CAPTURE_ENABLE = "scroll_capture_enable";
    private static final String JSON_SCROLL_CAPTURE_IMPL = "app_impl";
    private static final String JSON_SCROLL_CHILD = "scroll_child";
    private static final String JSON_SCROLL_LIST = "scroll_list";
    private static final String JSON_SCROLL_RECT = "scroll_rect";
    private static final String JSON_SIDE_LIST = "side_list";
    private static final String JSON_SIDE_RECT = "side_rect";
    private static final String JSON_VIEW_CLASS_NAME = "view_class_name";
    private static final String JSON_VIEW_HASH = "view_hash";
    private static final String JSON_VIEW_ID = "view_id";
    private static final String JSON_VIEW_UNSUPPORTED = "view_unsupported";
    private static final String JSON_WINDOW_BOUNDS = "window_bounds";
    private static final String JSON_WINDOW_LAYER = "window_layer";
    private static final String JSON_WINDOW_LIST = "window_list";
    private static final String JSON_WINDOW_NAVIBAR = "window_navibar";
    private static final String JSON_WINDOW_RECT_DECOR = "window_rect_decor";
    private static final String JSON_WINDOW_RECT_VISIBLE = "window_rect_visible";
    private static final String JSON_WINDOW_STATBAR = "window_statbar";
    private static final String JSON_WINDOW_TITLE = "window_title";
    private static final String PARAM_SCROLL_CAPTURE_DELAY = "scroll_capture_delay=";
    private static final String PARAM_SCROLL_CAPTURE_DUMP = "scroll_capture=";
    private static final String PARAM_SCROLL_CAPTURE_EXTRAS = "scroll_capture_compatible=";
    private static final String PARAM_SCROLL_RESET = "scroll_reset=";
    private static final String TAG = "LongshotDump/OplusLongshotViewDump";
    private final OplusLongshotViewInfo mViewInfo = new OplusLongshotViewInfo();
    private final Rect mTempRect1 = new Rect();
    private final Rect mTempRect2 = new Rect();
    private final List<ViewNode> mScrollNodes = new ArrayList();
    private final List<View> mSmallViews = new ArrayList();
    private final List<Rect> mFloatRects = new ArrayList();
    private final List<Rect> mSideRects = new ArrayList();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final List<ScrollCaptureTarget> mTargets = new ArrayList();
    private ViewNode mScrollNode = null;
    private int mDumpCount = 0;
    private int mMinListHeight = 0;
    private int mCoverHeight = 0;
    private int mScreenHeight = 0;
    private int mScreenWidght = 0;
    private int mMinScrollHeight = 0;
    private int mMinScrollDistance = 0;

    public OplusWindowNode collectWindow(View view, boolean isStatusBar, boolean isNavigationBar) {
        return new OplusWindowNode(view, isStatusBar, isNavigationBar);
    }

    public void dumpViewRoot(ViewRootImpl viewAncestor, ParcelFileDescriptor fd, List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows, boolean isLongshot, String[] args) {
        OplusLog.d(true, TAG, "dumpViewRoot:" + Arrays.toString(args));
        Resources res = viewAncestor.mContext.getResources();
        try {
            this.mMinScrollHeight = res.getDimensionPixelOffset(201654386);
            this.mMinScrollDistance = res.getDimensionPixelOffset(201654387);
            this.mMinListHeight = res.getDimensionPixelOffset(201654388);
            this.mCoverHeight = res.getDimensionPixelOffset(201654389);
            this.mScreenHeight = res.getDisplayMetrics().heightPixels;
            this.mScreenWidght = res.getDisplayMetrics().widthPixels;
            if (isLongshot) {
                dumpLongshot(viewAncestor, fd, (OplusLongshotDump) null, systemWindows, floatWindows, args);
            } else {
                dumpScreenshot(viewAncestor, fd, null, systemWindows, floatWindows);
            }
        } catch (Resources.NotFoundException e) {
            IoUtils.closeQuietly(fd);
            OplusLog.e(true, TAG, " ERROR : " + Log.getStackTraceString(e));
        }
    }

    public void injectInputBegin() {
        ViewNode viewNode = this.mScrollNode;
        if (viewNode != null) {
            viewNode.disableOverScroll();
        }
    }

    public void injectInputEnd() {
        ViewNode viewNode = this.mScrollNode;
        if (viewNode != null) {
            viewNode.resetOverScroll();
        }
    }

    public void reset() {
        OplusLog.d(true, TAG, "reset ViewDump");
        injectInputEnd();
        this.mScrollNode = null;
        clearList();
    }

    public void requestScrollCapture(final ViewRootImpl viewAncestor, final IOplusScrollCaptureResponseListener listener, Bundle extras) {
        beforeScrollCaptureSearch(viewAncestor, extras);
        final OplusScrollCaptureSearchResults results = new OplusScrollCaptureSearchResults(createExcutor(viewAncestor, extras));
        scrollCaptureSearch(viewAncestor, results);
        Runnable onComplete = new Runnable() { // from class: android.view.OplusLongshotViewDump$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                OplusLongshotViewDump.this.lambda$requestScrollCapture$0(viewAncestor, listener, results);
            }
        };
        results.setOnCompleteListener(onComplete);
        if (!results.isComplete()) {
            Handler handler = this.mHandler;
            Objects.requireNonNull(results);
            handler.postDelayed(new OplusLongshotViewDump$$ExternalSyntheticLambda1(results), viewAncestor.getScrollCaptureRequestTimeout());
        }
    }

    private Executor createExcutor(ViewRootImpl viewAncestor, Bundle extras) {
        if (extras == null || !extras.containsKey("scroll_capture_delay")) {
            return viewAncestor.mContext.getMainExecutor();
        }
        long delayMillis = extras.getLong("scroll_capture_delay", 0L);
        return new MainThreadExecutor(this.mHandler, delayMillis);
    }

    private void beforeScrollCaptureSearch(ViewRootImpl viewAncestor, Bundle extras) {
        if (extras == null || extras.isEmpty()) {
            OplusLog.i(true, TAG, "beforeScrollCaptureSearch: extras is null or empty");
            return;
        }
        ScrollCaptureInternal scrollCaptureInternal = viewAncestor.mAttachInfo.mScrollCaptureInternal;
        if (scrollCaptureInternal == null) {
            OplusLog.d(true, TAG, "mAttachInfo.mScrollCaptureInternal is null");
            scrollCaptureInternal = new ScrollCaptureInternal();
            viewAncestor.mAttachInfo.mScrollCaptureInternal = scrollCaptureInternal;
        }
        scrollCaptureInternal.getWrapper().getExtImpl().setExtras(extras);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: dispatchScrollCaptureSearchResponse, reason: merged with bridge method [inline-methods] */
    public void lambda$requestScrollCapture$0(ViewRootImpl viewAncestor, IOplusScrollCaptureResponseListener listener, OplusScrollCaptureSearchResults results) {
        List<OplusScrollCaptureResponse> responses = new ArrayList<>();
        List<ScrollCaptureTarget> targets = results.getTargets();
        for (ScrollCaptureTarget selectedTarget : targets) {
            if (selectedTarget == null) {
                try {
                    listener.onScrollCaptureResponse(responses);
                    return;
                } catch (RemoteException e) {
                    OplusLog.e(true, TAG, "Failed to send scroll capture search result", (Throwable) e);
                    return;
                }
            }
            if (selectedTarget.getScrollBounds() == null) {
                OplusLog.d(true, TAG, "" + selectedTarget + "scroll bounds is null");
            } else {
                OplusScrollCaptureResponseInner.Builder response = new OplusScrollCaptureResponseInner.Builder();
                response.setWindowTitle(viewAncestor.getTitle().toString());
                response.setPackageName(viewAncestor.mContext.getPackageName());
                response.addMessage("[" + targets.indexOf(selectedTarget) + "]");
                StringWriter writer = new StringWriter();
                IndentingPrintWriter pw = new IndentingPrintWriter(writer);
                selectedTarget.dump(pw);
                pw.flush();
                response.addMessage(writer.toString());
                response.setDescription("Connected");
                Rect boundsInWindow = new Rect();
                View containingView = selectedTarget.getContainingView();
                containingView.getLocationInWindow(viewAncestor.mAttachInfo.mTmpLocation);
                boundsInWindow.set(selectedTarget.getScrollBounds());
                boundsInWindow.offset(viewAncestor.mAttachInfo.mTmpLocation[0], viewAncestor.mAttachInfo.mTmpLocation[1]);
                response.setBoundsInWindow(boundsInWindow);
                Rect boundsOnScreen = new Rect();
                viewAncestor.mView.getLocationOnScreen(viewAncestor.mAttachInfo.mTmpLocation);
                boundsOnScreen.set(0, 0, viewAncestor.mView.getWidth(), viewAncestor.mView.getHeight());
                boundsOnScreen.offset(viewAncestor.mAttachInfo.mTmpLocation[0], viewAncestor.mAttachInfo.mTmpLocation[1]);
                response.setWindowBounds(boundsOnScreen);
                OplusScrollCaptureConnectionInner connection = new OplusScrollCaptureConnectionInner(viewAncestor.mView.getContext().getMainExecutor(), selectedTarget);
                response.setConnection(connection);
                responses.add(new OplusScrollCaptureResponse(response.build()));
            }
        }
        try {
            listener.onScrollCaptureResponse(responses);
        } catch (RemoteException e2) {
            OplusLog.w(true, TAG, "Failed to send scroll capture search response.", (Throwable) e2);
            Iterator<OplusScrollCaptureResponse> it = responses.iterator();
            while (it.hasNext()) {
                it.next().close();
            }
        }
    }

    private void scrollCaptureSearch(ViewRootImpl viewAncestor, final OplusScrollCaptureSearchResults results) {
        ScrollCaptureSearchResults androidResults = results.getResults();
        viewAncestor.getWrapper().collectRootScrollCaptureTargets(androidResults);
        for (ScrollCaptureTarget target : androidResults.getTargets()) {
            results.addTarget(target);
        }
        View rootView = viewAncestor.mView;
        if (rootView != null) {
            Point point = new Point();
            Rect rect = new Rect(0, 0, rootView.getWidth(), rootView.getHeight());
            viewAncestor.getChildVisibleRect(rootView, rect, point);
            Objects.requireNonNull(results);
            rootView.dispatchScrollCaptureSearch(rect, point, new Consumer() { // from class: android.view.OplusLongshotViewDump$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    OplusScrollCaptureSearchResults.this.addTarget((ScrollCaptureTarget) obj);
                }
            });
        }
    }

    private void dumpScrollCapture(final ViewRootImpl viewAncestor, long delayMillis, final Runnable completed) {
        final OplusScrollCaptureSearchResults results = new OplusScrollCaptureSearchResults(new MainThreadExecutor(this.mHandler, delayMillis));
        scrollCaptureSearch(viewAncestor, results);
        Runnable onComplete = new Runnable() { // from class: android.view.OplusLongshotViewDump$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                OplusLongshotViewDump.this.lambda$dumpScrollCapture$1(viewAncestor, results, completed);
            }
        };
        results.setOnCompleteListener(onComplete);
        if (!results.isComplete()) {
            Handler handler = this.mHandler;
            Objects.requireNonNull(results);
            handler.postDelayed(new OplusLongshotViewDump$$ExternalSyntheticLambda1(results), viewAncestor.getScrollCaptureRequestTimeout());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onScrollCaptureResults, reason: merged with bridge method [inline-methods] */
    public void lambda$dumpScrollCapture$1(ViewRootImpl viewAncestor, OplusScrollCaptureSearchResults results, Runnable completed) {
        this.mTargets.clear();
        for (ScrollCaptureTarget target : results.getTargets()) {
            if (target.getScrollBounds() == null) {
                OplusLog.v(true, TAG, "scroll bounds is null: " + target);
            }
            this.mTargets.add(target);
        }
        completed.run();
    }

    private String packScrollCapture() {
        if (!this.mTargets.isEmpty()) {
            JSONObject jsonNode = new JSONObject();
            JSONArray jsScrollCaptureArray = new JSONArray();
            scrollCaptureToJson(jsScrollCaptureArray, this.mTargets);
            try {
                jsonNode.put(JSON_CAPTURE_SCROLL_LIST, jsScrollCaptureArray);
            } catch (JSONException e) {
                OplusLog.e(true, TAG, "scrollNodesToJson:" + Log.getStackTraceString(e));
            } catch (Exception e2) {
                OplusLog.e(true, TAG, "scrollNodesToJson:" + Log.getStackTraceString(e2));
            }
            return jsonNode.toString();
        }
        return null;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(9:4|(11:5|6|7|(1:9)(1:41)|(1:11)|12|(1:14)|15|(2:(1:18)|(1:20))|21|22)|(6:27|28|29|30|32|33)|40|28|29|30|32|33) */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00cd, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00ee, code lost:
    
        com.oplus.util.OplusLog.e(true, android.view.OplusLongshotViewDump.TAG, "scrollNodesToJson:" + android.util.Log.getStackTraceString(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00cb, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00d2, code lost:
    
        com.oplus.util.OplusLog.e(true, android.view.OplusLongshotViewDump.TAG, "scrollNodesToJson:" + android.util.Log.getStackTraceString(r0));
     */
    /* JADX WARN: Incorrect condition in loop: B:3:0x0019 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void scrollCaptureToJson(JSONArray array, List<ScrollCaptureTarget> targets) {
        int[] tmpLocation = new int[2];
        Iterator<ScrollCaptureTarget> it = targets.iterator();
        ViewRootImpl viewAncestor = null;
        String windowTitle = null;
        String packageName = null;
        boolean boundsOnScreen = false;
        while (boundsOnScreen) {
            ScrollCaptureTarget target = it.next();
            JSONObject jsonTarget = new JSONObject();
            try {
                View view = target.getContainingView();
                jsonTarget.put(JSON_VIEW_HASH, System.identityHashCode(view));
                jsonTarget.put(JSON_VIEW_ID, getViewId(view));
                jsonTarget.put(JSON_VIEW_CLASS_NAME, getViewClassName(view));
                if (target.getScrollBounds() != null) {
                    getBoundsInWindow(target, tmpLocation, this.mTempRect1);
                    jsonTarget.put(JSON_BOUNDS_IN_WINDOW, this.mTempRect1.flattenToString());
                    jsonTarget.put(JSON_SCROLL_CAPTURE_ENABLE, true);
                } else {
                    jsonTarget.put(JSON_SCROLL_CAPTURE_ENABLE, false);
                }
                if (!boundsOnScreen) {
                    getBoundsOnScreen(target, tmpLocation, this.mTempRect2);
                    boundsOnScreen = true;
                }
                jsonTarget.put(JSON_WINDOW_BOUNDS, this.mTempRect2.flattenToString());
                if (packageName == null) {
                    packageName = view.getContext().getPackageName();
                }
                jsonTarget.put("package_name", packageName);
                if (windowTitle == null) {
                    if (viewAncestor == null) {
                        viewAncestor = view.getViewRootImpl();
                    }
                    if (viewAncestor != null) {
                        windowTitle = viewAncestor.getTitle().toString();
                    }
                }
                jsonTarget.put(JSON_WINDOW_TITLE, windowTitle);
            } catch (JSONException e) {
                e = e;
            } catch (Exception e2) {
                e = e2;
            }
            if (!(target.getCallback() instanceof ScrollCaptureViewSupport) && !(target.getCallback() instanceof OplusScrollCaptureViewSupport)) {
                jsonTarget.put(JSON_SCROLL_CAPTURE_IMPL, true);
                array.put(jsonTarget);
            }
            jsonTarget.put(JSON_SCROLL_CAPTURE_IMPL, false);
            array.put(jsonTarget);
        }
    }

    private String getViewId(View view) {
        return String.valueOf(view.getId());
    }

    private String getViewClassName(View view) {
        return view.getClass().getName();
    }

    private void getBoundsInWindow(ScrollCaptureTarget target, int[] location, Rect boundsInWindow) {
        View containingView = target.getContainingView();
        containingView.getLocationInWindow(location);
        boundsInWindow.set(target.getScrollBounds());
        boundsInWindow.offset(location[0], location[1]);
    }

    private void getBoundsOnScreen(ScrollCaptureTarget target, int[] location, Rect boundsOnScreen) {
        View view = target.getContainingView().getRootView();
        view.getLocationOnScreen(location);
        boundsOnScreen.set(0, 0, view.getWidth(), view.getHeight());
        boundsOnScreen.offset(location[0], location[1]);
    }

    private long getTimeSpend(long timeStart) {
        return SystemClock.uptimeMillis() - timeStart;
    }

    private void printMsg(String msg) {
        OplusLog.d(true, TAG, msg);
    }

    private void printSpend(String tag, long timeSpend) {
        String logSpend = tag + " : spend=" + timeSpend;
        printMsg(logSpend);
    }

    private void printTag(String tag) {
        printMsg("========== " + tag);
    }

    private void printScrollNodes(String prefix, List<ViewNode> scrollNodes) {
        for (ViewNode viewNode : scrollNodes) {
            printMsg(prefix + viewNode);
            printScrollNodes(prefix + "    ", viewNode.getChildList());
        }
    }

    private boolean canScrollVertically(View view) {
        if (view.canScrollVertically(1) || view.canScrollVertically(-1)) {
            return true;
        }
        return false;
    }

    private boolean isScrollableView(View view) {
        CharSequence accessibilityName = OplusViewCompat.getAccessibilityClassName(view);
        if (accessibilityName != null) {
            return accessibilityName.equals("android.widget.ListView") || accessibilityName.equals("android.widget.ScrollView");
        }
        return false;
    }

    private boolean isScrollable(View view) {
        return canScrollVertically(view) || isScrollableView(view);
    }

    private boolean isInvalidScrollDistance(View view, int height) {
        int scrollExtent = view.computeVerticalScrollExtent();
        int scrollRange = view.computeVerticalScrollRange();
        return (scrollRange - scrollExtent) * height < this.mMinScrollDistance * scrollExtent;
    }

    private boolean isValidScrollNode(View view, boolean isChildScrollNode) {
        int width = view.getWidth();
        int i = this.mScreenWidght;
        if (i > 0 && width < i / 2) {
            OplusLog.d(true, TAG, "    ! isValidScrollNode 1 : mScreenWidght=" + this.mScreenWidght + ", width=" + width);
            return false;
        }
        int height = view.getHeight();
        if (view instanceof ScrollView) {
            if (height < this.mMinScrollHeight) {
                if (isChildScrollNode) {
                    OplusLog.d(true, TAG, "    ! isValidScrollNode 2 : height=" + height + ", mMinScrollHeight=" + this.mMinScrollHeight + ", isChildScrollNode=" + isChildScrollNode);
                    return false;
                }
                if (isInvalidScrollDistance(view, height)) {
                    OplusLog.d(true, TAG, "    ! isValidScrollNode 3 : height=" + height + ", mMinScrollHeight=" + this.mMinScrollHeight + ", mMinScrollDistance=" + this.mMinScrollDistance);
                    return false;
                }
            }
        } else if (height < this.mMinListHeight) {
            Rect rect = new Rect();
            view.getBoundsOnScreen(rect, true);
            int i2 = rect.bottom;
            int i3 = this.mScreenHeight;
            if (i2 < i3 / 2) {
                OplusLog.d(true, TAG, "    ! isValidScrollNode 4 : mScreenHeight=" + this.mScreenHeight + ", mMinListHeight=" + this.mMinListHeight + ", rect=" + rect + ", height=" + height);
                return false;
            }
            if (i3 - rect.bottom > this.mMinListHeight) {
                OplusLog.d(true, TAG, "    ! isValidScrollNode 5 : mScreenHeight=" + this.mScreenHeight + ", mMinListHeight=" + this.mMinListHeight + ", rect=" + rect + ", height=" + height);
                return false;
            }
        }
        return true;
    }

    private boolean isGalleryRoot(View view) {
        String packageName = view.getContext().getPackageName();
        return OplusLongshotUtils.isGallery(packageName) && (view instanceof GLSurfaceView);
    }

    private void appendLongshotInfo(View view, OplusLongshotViewInfo info) {
    }

    private void dumpViewNodes(View view, OplusLongshotViewInfo info) {
        if (view == null || !view.isVisibleToUser()) {
            return;
        }
        appendLongshotInfo(view, info);
        ViewExtImpl viewExt = (ViewExtImpl) OplusTypeCastingHelper.typeCasting(ViewExtImpl.class, view.getViewWrapper().getViewExt());
        if (viewExt == null) {
            return;
        }
        if (view instanceof ViewGroup) {
            viewExt.findViewsLongshotInfo(info);
            ViewGroup group = (ViewGroup) view;
            int childrenCount = group.getChildCount();
            ArrayList<View> preorderedList = group.buildOrderedChildList();
            boolean customOrder = false;
            boolean noPreorder = preorderedList == null;
            if (noPreorder && group.isChildrenDrawingOrderEnabled()) {
                customOrder = true;
            }
            for (int i = 0; i < childrenCount; i++) {
                int childIndex = customOrder ? group.getChildDrawingOrder(childrenCount, i) : i;
                View child = noPreorder ? group.getChildAt(childIndex) : preorderedList.get(childIndex);
                dumpViewNodes(child, info);
            }
            if (preorderedList != null) {
                preorderedList.clear();
                return;
            }
            return;
        }
        viewExt.findViewsLongshotInfo(info);
    }

    private void dumpScrollNodes(ViewNode scrollNode, View view, Point minSize, List<View> small, int recursion, OplusLongshotViewInfo info) {
        ViewExtImpl viewExt;
        int recursion2;
        ViewNode scrollNode2;
        int recursion3;
        ViewNode scrollNode3 = scrollNode;
        if (view == null || !view.isVisibleToUser() || (viewExt = (ViewExtImpl) OplusTypeCastingHelper.typeCasting(ViewExtImpl.class, view.getViewWrapper().getViewExt())) == null) {
            return;
        }
        if (view instanceof ViewGroup) {
            viewExt.findViewsLongshotInfo(info);
            int i = 1;
            view.getBoundsOnScreen(this.mTempRect1, true);
            if (minSize != null) {
                if (this.mTempRect1.width() < minSize.x) {
                    return;
                }
                if (this.mTempRect1.height() < minSize.y) {
                    if (small != null) {
                        small.add(view);
                        return;
                    }
                    return;
                }
            }
            this.mDumpCount++;
            boolean scrollable = isScrollable(view);
            if (!scrollable) {
                recursion2 = recursion;
                scrollNode2 = scrollNode3;
            } else {
                long timeStart = SystemClock.uptimeMillis();
                StringBuilder msg = new StringBuilder();
                msg.append("    ");
                AccessibilityNodeInfo nodeInfo = view.createAccessibilityNodeInfo();
                if (nodeInfo != null && nodeInfo.isScrollable()) {
                    boolean isChildScrollNode = scrollNode3 != null;
                    if (isValidScrollNode(view, isChildScrollNode)) {
                        view.getBoundsOnScreen(this.mTempRect2, false);
                        ViewNode viewNode = new ViewNode(view, nodeInfo.getClassName(), this.mTempRect1, this.mTempRect2);
                        if (isChildScrollNode) {
                            scrollNode3.addChild(viewNode);
                            msg.append("isChildScrollNode : ");
                        } else {
                            this.mScrollNodes.add(viewNode);
                            msg.append("isScrollNode : ");
                        }
                        scrollNode3 = viewNode;
                        long timeSpend = getTimeSpend(timeStart);
                        viewNode.setSpend(timeSpend);
                        msg.append(viewNode);
                        recursion3 = recursion + 1;
                    } else {
                        msg.append("----rmScrollNode : ");
                        recursion3 = recursion;
                    }
                } else {
                    long timeSpend2 = getTimeSpend(timeStart);
                    msg.append("---noScrollNode : ");
                    msg.append(view);
                    msg.append(":spend=");
                    msg.append(timeSpend2);
                    recursion3 = recursion;
                }
                i = 1;
                OplusLog.d(true, TAG, msg.toString());
                scrollNode2 = scrollNode3;
                recursion2 = recursion3;
            }
            if (recursion2 > i) {
                return;
            }
            ViewGroup group = (ViewGroup) view;
            int childrenCount = group.getChildCount();
            ArrayList<View> preorderedList = group.buildOrderedChildList();
            if (preorderedList != null || !group.isChildrenDrawingOrderEnabled()) {
                i = 0;
            }
            boolean customOrder = i;
            Point minSize2 = minSize;
            List<View> small2 = small;
            int i2 = 0;
            while (i2 < childrenCount) {
                int childIndex = getAndVerifyPreorderedIndex(childrenCount, i2, customOrder, group);
                View child = getAndVerifyPreorderedView(preorderedList, group, childIndex);
                List<View> list = null;
                minSize2 = scrollable ? null : minSize2;
                if (!scrollable) {
                    list = small2;
                }
                small2 = list;
                dumpScrollNodes(scrollNode2, child, minSize2, small2, recursion2, info);
                i2++;
                customOrder = customOrder;
                preorderedList = preorderedList;
            }
            ArrayList<View> preorderedList2 = preorderedList;
            if (preorderedList2 != null) {
                preorderedList2.clear();
                return;
            }
            return;
        }
        viewExt.findViewsLongshotInfo(info);
    }

    private int getAndVerifyPreorderedIndex(int childrenCount, int i, boolean customOrder, ViewGroup group) {
        if (customOrder) {
            int childIndex1 = group.getChildDrawingOrder(childrenCount, i);
            if (childIndex1 >= childrenCount) {
                OplusLog.e(true, TAG, "getChildDrawingOrder() returned invalid index " + childIndex1 + " (child count is " + childrenCount + ")");
                return childIndex1;
            }
            return childIndex1;
        }
        return i;
    }

    private static View getAndVerifyPreorderedView(ArrayList<View> preorderedList, ViewGroup group, int childIndex) {
        if (preorderedList != null && preorderedList.size() > childIndex) {
            View child = preorderedList.get(childIndex);
            if (child == null) {
                OplusLog.e(true, TAG, "Invalid preorderedList contained null child at index " + childIndex);
                return child;
            }
            return child;
        }
        return group.getChildAt(childIndex);
    }

    private void dumpHierarchyLongshot(OplusLongshotDump result, View view) {
        long timeStart = SystemClock.uptimeMillis();
        printTag("dumpHierarchyLongshot");
        this.mViewInfo.reset();
        dumpScrollNodes(null, view, null, null, 0, this.mViewInfo);
        if (this.mScrollNodes.isEmpty()) {
            for (View v : this.mSmallViews) {
                dumpScrollNodes(null, v, null, null, 0, this.mViewInfo);
            }
        }
        this.mSmallViews.clear();
        printScrollNodes("dumpHierarchyLongshot : ", this.mScrollNodes);
        long timeSpend = getTimeSpend(timeStart);
        printSpend("dumpHierarchyLongshot", timeSpend);
        if (result != null) {
            result.setScrollCount(this.mScrollNodes.size());
            result.setDumpCount(this.mDumpCount);
            result.setSpendDump(timeSpend);
        }
    }

    private void dumpHierarchyScreenshot(View view) {
        long timeStart = SystemClock.uptimeMillis();
        printTag("dumpHierarchyScreenshot");
        this.mViewInfo.reset();
        dumpViewNodes(view, this.mViewInfo);
        long timeSpend = getTimeSpend(timeStart);
        printSpend("dumpHierarchyScreenshot", timeSpend);
    }

    private String getAccessibilityName(ViewNode viewNode) {
        CharSequence accessibilityName = viewNode.getAccessibilityName();
        if (accessibilityName != null) {
            return accessibilityName.toString();
        }
        return null;
    }

    private void selectScrollNodes(List<ViewNode> scrollNodes) {
        if (scrollNodes.isEmpty()) {
            return;
        }
        while (scrollNodes.size() > 1) {
            scrollNodes.remove(0);
        }
        ViewNode viewNode = scrollNodes.get(0);
        this.mScrollNode = viewNode;
        selectScrollNodes(viewNode.getChildList());
    }

    private boolean updateCoverRect(OplusLongshotViewUtils utils, Rect dstRect, Rect srcRect, Rect coverRect) {
        printMsg("    updateCoverRect : dstRect= " + dstRect + ", srcRect= " + srcRect + ", coverRect= " + coverRect);
        if (!Rect.intersects(coverRect, srcRect)) {
            return false;
        }
        Rect rect = new Rect(srcRect);
        int diffT = coverRect.top - rect.top;
        int diffB = rect.bottom - coverRect.bottom;
        if (diffT == 0 && diffB == 0) {
            printMsg("    updateCoverRect : diffT = diffB = 0");
            return false;
        }
        if (diffB > diffT) {
            rect.top = coverRect.bottom;
        } else {
            rect.bottom = coverRect.top;
        }
        boolean update = isLargeHeight(rect, srcRect);
        if (!update) {
            int top = Math.max(srcRect.top, coverRect.top);
            int bottom = Math.min(srcRect.bottom, coverRect.bottom);
            if (bottom > top && bottom - top <= this.mCoverHeight) {
                update = true;
            } else if (utils.isBottomBarRect(coverRect, srcRect)) {
                update = true;
            }
        }
        if (update) {
            this.mFloatRects.add(new Rect(coverRect));
            int top2 = Math.max(dstRect.top, rect.top);
            int bottom2 = Math.min(dstRect.bottom, rect.bottom);
            if (bottom2 <= top2) {
                dstRect.setEmpty();
            } else {
                dstRect.top = top2;
                dstRect.bottom = bottom2;
            }
        }
        return update;
    }

    private boolean isSmallWidth(Rect dst, Rect src) {
        return dst.width() < src.width() / 3;
    }

    private boolean isLargeHeight(Rect dst, Rect src) {
        return dst.height() > (src.height() * 2) / 5;
    }

    private boolean isVerticalBar(Rect dst, Rect src) {
        return isLargeHeight(dst, src) && isSmallWidth(dst, src);
    }

    private boolean isInvalidIntersect(Rect rect, Rect srcRect) {
        return !rect.intersect(srcRect) || isVerticalBar(rect, srcRect);
    }

    private void printContentView(OplusLongshotViewContent content, String tag, Rect rect) {
        StringBuilder msg = new StringBuilder();
        msg.append("    ");
        msg.append(tag);
        msg.append(" : {");
        msg.append(content);
        if (rect != null) {
            msg.append("} => ");
            msg.append(rect);
        }
        printMsg(msg.toString());
    }

    private void checkCoverContents(OplusLongshotViewUtils utils, List<OplusLongshotViewContent> coverContents, Rect rootRect) {
        List<OplusLongshotViewContent> largeContents = new ArrayList<>();
        Map<OplusLongshotViewContent, List<OplusLongshotViewContent>> smallContentMap = new HashMap<>();
        for (OplusLongshotViewContent coverContent : coverContents) {
            OplusLongshotViewContent parent = coverContent.getParent();
            if (parent != null) {
                if (utils.isLargeCoverRect(parent.getRect(), rootRect, false)) {
                    largeContents.add(coverContent);
                } else if (smallContentMap.containsKey(parent)) {
                    smallContentMap.get(parent).add(coverContent);
                } else {
                    List<OplusLongshotViewContent> smallContents = new ArrayList<>();
                    smallContents.add(coverContent);
                    smallContentMap.put(parent, smallContents);
                }
            }
        }
        coverContents.clear();
        coverContents.addAll(largeContents);
        for (Map.Entry<OplusLongshotViewContent, List<OplusLongshotViewContent>> entry : smallContentMap.entrySet()) {
            List<OplusLongshotViewContent> smallContents2 = entry.getValue();
            if (!smallContents2.isEmpty()) {
                this.mTempRect1.setEmpty();
                for (OplusLongshotViewContent smallContent : smallContents2) {
                    this.mTempRect1.union(smallContent.getRect());
                }
                coverContents.add(new OplusLongshotViewContent(entry.getKey().getView(), this.mTempRect1, null));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void calcScrollRectForViews(OplusLongshotViewUtils utils, Rect dstRect, Rect srcRect, View view) {
        List<OplusLongshotViewContent> coverContents = new ArrayList<>();
        List<OplusLongshotViewContent> sideContents = new ArrayList<>();
        ViewGroup group = view;
        ViewParent parent = view.getParent();
        while (parent instanceof ViewGroup) {
            ViewGroup group2 = parent;
            if (group2.getChildCount() <= 1) {
                group = group2;
                group.getBoundsOnScreen(this.mTempRect1, true);
                OplusLog.d(true, TAG, "    nofindCoverRect : rootRect=" + srcRect + ", srcRect=" + this.mTempRect1 + ", group=" + group);
            } else {
                ViewGroup viewGroup = group;
                group = group2;
                utils.findCoverRect(1, group2, viewGroup, coverContents, sideContents, srcRect, null, false);
            }
            parent = group.getParent();
        }
        Collections.sort(coverContents, CONTENT_COMPARATOR);
        checkCoverContents(utils, coverContents, srcRect);
        printMsg("-------------------------calcScrollRectForViews : coverContents=" + coverContents.size() + ", sideContents=" + sideContents.size());
        for (OplusLongshotViewContent coverContent : coverContents) {
            boolean result = updateCoverRect(utils, dstRect, srcRect, coverContent.getRect());
            String tag = result ? SplitPathManager.UPDATE : "skip  ";
            printContentView(coverContent, tag, dstRect);
        }
        for (OplusLongshotViewContent sideContent : sideContents) {
            this.mSideRects.add(new Rect(sideContent.getRect()));
            printContentView(sideContent, "sidebar", null);
        }
    }

    private void calcScrollRectForWindow(OplusLongshotViewUtils utils, Rect dstRect, Rect srcRect, OplusWindowNode window) {
        this.mTempRect1.set(window.getCoverRect());
        if (isInvalidIntersect(this.mTempRect1, srcRect)) {
            return;
        }
        boolean result = updateCoverRect(utils, dstRect, srcRect, this.mTempRect1);
        String tag = result ? SplitPathManager.UPDATE : "skip  ";
        printMsg("    " + tag + " : {" + window + "} => " + dstRect);
    }

    private void calcScrollRectForWindows(OplusLongshotViewUtils utils, Rect dstRect, Rect srcRect, List<OplusWindowNode> windows) {
        if (windows != null) {
            for (OplusWindowNode window : windows) {
                calcScrollRectForWindow(utils, dstRect, srcRect, window);
            }
        }
    }

    private void calcScrollRect(OplusLongshotDump result, OplusLongshotViewUtils utils, ViewNode scrollNode, List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows) {
        OplusLog.d(true, TAG, "==========calcScrollRect====: ");
        if (scrollNode == null) {
            OplusLog.d(true, TAG, "  calcScrollRect, scrollNode=null");
            return;
        }
        long timeStart = SystemClock.uptimeMillis();
        printTag("calcScrollRect");
        View view = scrollNode.getView();
        Rect srcRect = new Rect();
        view.getBoundsOnScreen(srcRect, true);
        Rect dstRect = new Rect(srcRect);
        calcScrollRectForViews(utils, dstRect, srcRect, view);
        scrollNode.setScrollRect(dstRect);
        printMsg("calcScrollRect : scrollRect=" + dstRect);
        long timeSpend = getTimeSpend(timeStart);
        printSpend("calcScrollRect", timeSpend);
        if (result != null) {
            result.setScrollRect(dstRect);
            result.setSpendCalc(timeSpend);
        }
    }

    private void calcScrollRects(OplusLongshotDump result, OplusLongshotViewUtils utils, List<ViewNode> scrollNodes, List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows) {
        for (ViewNode scrollNode : scrollNodes) {
            if (result != null) {
                result.setScrollComponent(OplusLongshotComponentName.create((OplusLongshotViewBase) scrollNode.getView(), getAccessibilityName(scrollNode)));
            }
            calcScrollRect(result, utils, scrollNode, systemWindows, floatWindows);
            calcScrollRects(result, utils, scrollNode.getChildList(), systemWindows, floatWindows);
        }
    }

    private void reportDumpResult(Context context, OplusLongshotDump result) {
        OplusScreenshotManager sm;
        if (result != null && (sm = OplusLongshotUtils.getScreenshotManager(context)) != null) {
            sm.reportLongshotDumpResult(result);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01a0  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01aa A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0176  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x01aa A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01aa A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void scrollNodesToJson(JSONArray jSONArray, List<ViewNode> scrollNodes, boolean isChild) {
        Iterator<ViewNode> it;
        ViewGroup group;
        JSONObject jSONObject;
        Rect scrollRect;
        JSONArray jsChildArray;
        int childrenCount;
        boolean noPreorder;
        boolean customOrder;
        int i;
        int childIndex;
        View view;
        View view2;
        Rect scrollRect2;
        Iterator<ViewNode> it2 = scrollNodes.iterator();
        while (it2.hasNext()) {
            ViewNode viewNode = it2.next();
            View view3 = viewNode.getView();
            if (view3 instanceof ViewGroup) {
                ArrayList<View> preorderedList = null;
                try {
                    group = (ViewGroup) view3;
                    jSONObject = new JSONObject();
                    jSONObject.put(JSON_PARENT_HASH, System.identityHashCode(group));
                    group.getBoundsOnScreen(this.mTempRect1, false);
                    jSONObject.put(JSON_PARENT_RECT_FULL, this.mTempRect1.flattenToString());
                    group.getBoundsOnScreen(this.mTempRect1, true);
                    jSONObject.put(JSON_PARENT_RECT_CLIP, this.mTempRect1.flattenToString());
                    scrollRect = viewNode.getScrollRect();
                    jSONObject.put(JSON_SCROLL_RECT, scrollRect.flattenToString());
                } catch (JSONException e) {
                    e = e;
                } catch (Exception e2) {
                    e = e2;
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    try {
                        jSONObject.put(JSON_SCROLL_CHILD, isChild);
                        jsChildArray = new JSONArray();
                        childrenCount = group.getChildCount();
                        preorderedList = group.buildOrderedChildList();
                        noPreorder = preorderedList == null;
                    } catch (JSONException e3) {
                        e = e3;
                        it = it2;
                        OplusLog.e(true, TAG, "scrollNodesToJson:" + Log.getStackTraceString(e));
                        if (preorderedList == null) {
                        }
                        preorderedList.clear();
                        scrollNodesToJson(jSONArray, viewNode.getChildList(), true);
                        it2 = it;
                    } catch (Exception e4) {
                        e = e4;
                        it = it2;
                        OplusLog.e(true, TAG, "scrollNodesToJson:" + Log.getStackTraceString(e));
                        if (preorderedList == null) {
                        }
                    }
                    if (noPreorder) {
                        try {
                        } catch (JSONException e5) {
                            e = e5;
                            it = it2;
                            OplusLog.e(true, TAG, "scrollNodesToJson:" + Log.getStackTraceString(e));
                            if (preorderedList == null) {
                            }
                            preorderedList.clear();
                            scrollNodesToJson(jSONArray, viewNode.getChildList(), true);
                            it2 = it;
                        } catch (Exception e6) {
                            e = e6;
                            it = it2;
                            OplusLog.e(true, TAG, "scrollNodesToJson:" + Log.getStackTraceString(e));
                            if (preorderedList == null) {
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (preorderedList != null) {
                            }
                            throw th;
                        }
                        if (group.isChildrenDrawingOrderEnabled()) {
                            customOrder = true;
                            i = 0;
                            while (i < childrenCount) {
                                int childIndex2 = customOrder ? group.getChildDrawingOrder(childrenCount, i) : i;
                                if (noPreorder) {
                                    childIndex = childIndex2;
                                    view = group.getChildAt(childIndex);
                                } else {
                                    childIndex = childIndex2;
                                    view = preorderedList.get(childIndex);
                                }
                                View child = view;
                                it = it2;
                                if (child == null) {
                                    view2 = view3;
                                    scrollRect2 = scrollRect;
                                } else {
                                    try {
                                        if (child.isVisibleToUser()) {
                                            JSONObject jsChild = new JSONObject();
                                            view2 = view3;
                                            try {
                                                try {
                                                    scrollRect2 = scrollRect;
                                                    jsChild.put(JSON_CHILD_HASH, System.identityHashCode(child));
                                                    child.getBoundsOnScreen(this.mTempRect1, false);
                                                    jsChild.put(JSON_CHILD_RECT_FULL, this.mTempRect1.flattenToString());
                                                    child.getBoundsOnScreen(this.mTempRect1, true);
                                                    jsChild.put(JSON_CHILD_RECT_CLIP, this.mTempRect1.flattenToString());
                                                    if (group instanceof ScrollView) {
                                                        jsChild.put(JSON_CHILD_SCROLLY, child.getScrollY());
                                                    }
                                                    jsChildArray.put(jsChild);
                                                } catch (JSONException e7) {
                                                    e = e7;
                                                    OplusLog.e(true, TAG, "scrollNodesToJson:" + Log.getStackTraceString(e));
                                                    if (preorderedList == null) {
                                                    }
                                                    preorderedList.clear();
                                                    scrollNodesToJson(jSONArray, viewNode.getChildList(), true);
                                                    it2 = it;
                                                } catch (Exception e8) {
                                                    e = e8;
                                                    OplusLog.e(true, TAG, "scrollNodesToJson:" + Log.getStackTraceString(e));
                                                    if (preorderedList == null) {
                                                    }
                                                }
                                            } catch (Throwable th3) {
                                                th = th3;
                                                if (preorderedList != null) {
                                                }
                                                throw th;
                                            }
                                        } else {
                                            view2 = view3;
                                            scrollRect2 = scrollRect;
                                        }
                                    } catch (JSONException e9) {
                                        e = e9;
                                        OplusLog.e(true, TAG, "scrollNodesToJson:" + Log.getStackTraceString(e));
                                        if (preorderedList == null) {
                                            scrollNodesToJson(jSONArray, viewNode.getChildList(), true);
                                            it2 = it;
                                        }
                                        preorderedList.clear();
                                        scrollNodesToJson(jSONArray, viewNode.getChildList(), true);
                                        it2 = it;
                                    } catch (Exception e10) {
                                        e = e10;
                                        OplusLog.e(true, TAG, "scrollNodesToJson:" + Log.getStackTraceString(e));
                                        if (preorderedList == null) {
                                            preorderedList.clear();
                                            scrollNodesToJson(jSONArray, viewNode.getChildList(), true);
                                            it2 = it;
                                        } else {
                                            scrollNodesToJson(jSONArray, viewNode.getChildList(), true);
                                            it2 = it;
                                        }
                                    }
                                }
                                i++;
                                it2 = it;
                                view3 = view2;
                                scrollRect = scrollRect2;
                            }
                            it = it2;
                            jSONObject.put(JSON_CHILD_LIST, jsChildArray);
                            jSONArray.put(jSONObject);
                            if (preorderedList == null) {
                            }
                            preorderedList.clear();
                        }
                    }
                    customOrder = false;
                    i = 0;
                    while (i < childrenCount) {
                    }
                    it = it2;
                    jSONObject.put(JSON_CHILD_LIST, jsChildArray);
                    jSONArray.put(jSONObject);
                    if (preorderedList == null) {
                    }
                    preorderedList.clear();
                } catch (Throwable th4) {
                    th = th4;
                    if (preorderedList != null) {
                        preorderedList.clear();
                    }
                    throw th;
                }
            } else {
                it = it2;
            }
            scrollNodesToJson(jSONArray, viewNode.getChildList(), true);
            it2 = it;
        }
    }

    private List<OplusWindowNode> mergeWindowList(List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows) {
        List<OplusWindowNode> windows = new ArrayList<>();
        if (systemWindows != null) {
            windows.addAll(systemWindows);
        }
        if (floatWindows != null) {
            windows.addAll(floatWindows);
        }
        return windows;
    }

    private JSONArray windowNodesToJson(List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows) {
        JSONArray jsWindowArray = null;
        List<OplusWindowNode> windows = mergeWindowList(systemWindows, floatWindows);
        if (!windows.isEmpty()) {
            jsWindowArray = new JSONArray();
            for (OplusWindowNode window : windows) {
                try {
                    JSONObject jsWindow = new JSONObject();
                    jsWindow.put(JSON_WINDOW_STATBAR, window.isStatusBar());
                    jsWindow.put(JSON_WINDOW_NAVIBAR, window.isNavigationBar());
                    jsWindow.put(JSON_WINDOW_LAYER, window.getSurfaceLayer());
                    jsWindow.put(JSON_WINDOW_RECT_DECOR, window.getDecorRect().flattenToString());
                    jsWindow.put(JSON_WINDOW_RECT_VISIBLE, window.getCoverRect().flattenToString());
                    jsWindowArray.put(jsWindow);
                } catch (JSONException e) {
                    OplusLog.e(true, TAG, "windowNodesToJson:" + Log.getStackTraceString(e));
                } catch (Exception e2) {
                    OplusLog.e(true, TAG, "windowNodesToJson:" + Log.getStackTraceString(e2));
                }
            }
        }
        return jsWindowArray;
    }

    private JSONArray sideRectsToJson(List<Rect> sideRects) {
        JSONArray jsSideArray = null;
        if (!sideRects.isEmpty()) {
            jsSideArray = new JSONArray();
            for (Rect rect : sideRects) {
                try {
                    JSONObject jsSide = new JSONObject();
                    jsSide.put(JSON_SIDE_RECT, rect.flattenToString());
                    jsSideArray.put(jsSide);
                } catch (JSONException e) {
                    OplusLog.e(true, TAG, "sideRectsToJson:" + Log.getStackTraceString(e));
                } catch (Exception e2) {
                    OplusLog.e(true, TAG, "sideRectsToJson:" + Log.getStackTraceString(e2));
                }
            }
        }
        return jsSideArray;
    }

    private JSONArray floatRectsToJson(List<Rect> floatRects) {
        JSONArray jsFloatArray = null;
        if (!floatRects.isEmpty()) {
            jsFloatArray = new JSONArray();
            for (Rect rect : floatRects) {
                try {
                    JSONObject jsFloat = new JSONObject();
                    jsFloat.put(JSON_FLOAT_RECT, rect.flattenToString());
                    jsFloatArray.put(jsFloat);
                } catch (JSONException e) {
                    OplusLog.e(true, TAG, "floatRectsToJson:" + Log.getStackTraceString(e));
                } catch (Exception e2) {
                    OplusLog.e(true, TAG, "floatRectsToJson:" + Log.getStackTraceString(e2));
                }
            }
        }
        return jsFloatArray;
    }

    private JSONObject getJSONObject(JSONObject jsonNode) {
        if (jsonNode == null) {
            return new JSONObject();
        }
        return jsonNode;
    }

    private String packJsonNode(OplusLongshotDump result, PrintWriter pw, List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows) {
        long timeStart = SystemClock.uptimeMillis();
        printTag("packJsonNode");
        JSONObject jsonNode = null;
        try {
            if (this.mViewInfo.isUnsupported()) {
                jsonNode = new JSONObject();
                jsonNode.put(JSON_VIEW_UNSUPPORTED, true);
            } else {
                if (!this.mScrollNodes.isEmpty()) {
                    JSONArray jsScrollArray = new JSONArray();
                    scrollNodesToJson(jsScrollArray, this.mScrollNodes, false);
                    jsonNode = getJSONObject(null);
                    jsonNode.put(JSON_SCROLL_LIST, jsScrollArray);
                }
                JSONArray jsSideArray = sideRectsToJson(this.mSideRects);
                if (jsSideArray != null) {
                    jsonNode = getJSONObject(jsonNode);
                    jsonNode.put(JSON_SIDE_LIST, jsSideArray);
                }
                JSONArray jsFloatArray = floatRectsToJson(this.mFloatRects);
                if (jsFloatArray != null) {
                    jsonNode = getJSONObject(jsonNode);
                    jsonNode.put(JSON_FLOAT_LIST, jsFloatArray);
                }
                JSONArray jsWindowArray = windowNodesToJson(systemWindows, floatWindows);
                if (jsWindowArray != null) {
                    jsonNode = getJSONObject(jsonNode);
                    jsonNode.put(JSON_WINDOW_LIST, jsWindowArray);
                }
            }
        } catch (JSONException e) {
            OplusLog.e(true, TAG, "packJsonNode:" + Log.getStackTraceString(e));
        } catch (Exception e2) {
            OplusLog.e(true, TAG, "packJsonNode:" + Log.getStackTraceString(e2));
        }
        String jsonPack = jsonNode != null ? jsonNode.toString() : null;
        long timeSpend = getTimeSpend(timeStart);
        printSpend("packJsonNode", timeSpend);
        if (result != null) {
            result.setSpendPack(timeSpend);
        }
        return jsonPack;
    }

    private LongshotDumpParam parseParam(String[] args) {
        LongshotDumpParam param = new LongshotDumpParam();
        if (args == null) {
            return param;
        }
        OplusLog.v(true, TAG, "parseParam args: " + Arrays.toString(args));
        for (String arg : args) {
            if (!TextUtils.isEmpty(arg)) {
                OplusLog.v(true, TAG, "parseParam for.args:: " + arg);
                if (arg.startsWith(PARAM_SCROLL_CAPTURE_DUMP)) {
                    parseIsDumpScrollCapture(param, arg);
                } else if (arg.startsWith(PARAM_SCROLL_CAPTURE_DELAY)) {
                    parseScrollCaptureDelay(param, arg);
                } else if (arg.startsWith(PARAM_SCROLL_CAPTURE_EXTRAS)) {
                    parseScrollCaptureExtras(param, arg);
                } else if (arg.startsWith(PARAM_SCROLL_RESET)) {
                    parseIsScrollReset(param, arg);
                }
            }
        }
        return param;
    }

    private void parseIsScrollReset(LongshotDumpParam param, String arg) {
        param.mIsScrollReset = !"false".equals(arg.substring(PARAM_SCROLL_RESET.length()));
    }

    private void parseScrollCaptureExtras(LongshotDumpParam param, String arg) {
        String extrasJson = arg.substring(PARAM_SCROLL_CAPTURE_EXTRAS.length());
        Bundle extras = new Bundle();
        try {
            JSONObject json = new JSONObject(extrasJson);
            Iterator<String> it = json.keys();
            while (it.hasNext()) {
                String key = it.next();
                int type = json.optInt(key);
                extras.putInt(key, type);
            }
            param.mExtras.putBundle("scroll_capture_compatible", extras);
        } catch (JSONException e) {
            OplusLog.w(true, TAG, "scroll capture extras error.", (Throwable) e);
        }
    }

    private void parseScrollCaptureDelay(LongshotDumpParam param, String arg) {
        try {
            param.mDelay = Long.parseLong(arg.substring(PARAM_SCROLL_CAPTURE_DELAY.length()));
            param.mExtras.putLong("scroll_capture_delay", param.mDelay);
        } catch (NumberFormatException e) {
            OplusLog.w(true, TAG, "scroll capture delay error.", (Throwable) e);
        }
    }

    private void parseIsDumpScrollCapture(LongshotDumpParam param, String arg) {
        param.mIsDumpScrollCapture = "true".equals(arg.substring(PARAM_SCROLL_CAPTURE_DUMP.length()));
    }

    private void dumpLongshot(Context context, String tag, ParcelFileDescriptor fd, OplusLongshotDump result, List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows) {
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        PrintWriter pw = null;
        try {
            try {
                pw = new FastPrintWriter(new FileOutputStream(fd.getFileDescriptor()));
                String jsonPack = packJsonNode(result, pw, systemWindows, floatWindows);
                if (jsonPack != null) {
                    pw.println(jsonPack);
                }
                String scrollCaptureJson = packScrollCapture();
                if (scrollCaptureJson != null) {
                    pw.println(scrollCaptureJson);
                }
                pw.flush();
            } catch (Exception e) {
                OplusLog.e(true, TAG, tag + " ERROR : " + Log.getStackTraceString(e));
            }
        } finally {
            reportDumpResult(context, result);
            clearList();
            IoUtils.closeQuietly(fd);
            IoUtils.closeQuietly(pw);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void dumpLongshot(final ViewRootImpl viewAncestor, final ParcelFileDescriptor fd, final OplusLongshotDump result, final List<OplusWindowNode> systemWindows, final List<OplusWindowNode> floatWindows, String[] args) {
        final String tag = "dumpLongshot";
        OplusLog.d(true, TAG, "dumpLongshot : viewAncestor.mView=" + viewAncestor.mView);
        dumpLongshot(viewAncestor, "dumpLongshot", result, systemWindows, floatWindows);
        LongshotDumpParam params = parseParam(args);
        if (params.mIsDumpScrollCapture) {
            beforeScrollCaptureSearch(viewAncestor, params.mExtras);
            dumpScrollCapture(viewAncestor, params.mDelay, new Runnable() { // from class: android.view.OplusLongshotViewDump$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    OplusLongshotViewDump.this.lambda$dumpLongshot$2(viewAncestor, tag, fd, result, systemWindows, floatWindows);
                }
            });
        } else {
            dumpLongshot(viewAncestor.mContext, "dumpLongshot", fd, result, systemWindows, floatWindows);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpLongshot$2(ViewRootImpl viewAncestor, String tag, ParcelFileDescriptor fd, OplusLongshotDump result, List systemWindows, List floatWindows) {
        dumpLongshot(viewAncestor.mContext, tag, fd, result, (List<OplusWindowNode>) systemWindows, (List<OplusWindowNode>) floatWindows);
    }

    private void dumpLongshot(ViewRootImpl viewAncestor, String tag, OplusLongshotDump result, List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows) {
        this.mScrollNode = null;
        this.mDumpCount = 0;
        dumpHierarchyLongshot(result, viewAncestor.mView);
        selectScrollNodes(this.mScrollNodes);
        OplusLog.d(true, TAG, tag + " : mScrollNode=" + this.mScrollNode);
        OplusLongshotViewUtils utils = new OplusLongshotViewUtils(viewAncestor.mContext);
        calcScrollRects(result, utils, this.mScrollNodes, systemWindows, floatWindows);
    }

    private void dumpScreenshot(ViewRootImpl viewAncestor, PrintWriter pw, String tag) {
        dumpHierarchyScreenshot(viewAncestor.mView);
    }

    private void dumpScreenshot(ViewRootImpl viewAncestor, ParcelFileDescriptor fd, OplusLongshotDump result, List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows) {
        OplusLog.d(true, TAG, "dumpScreenshot : viewAncestor.mView=" + viewAncestor.mView);
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        PrintWriter pw = null;
        try {
            try {
                pw = new FastPrintWriter(new FileOutputStream(fd.getFileDescriptor()));
                dumpScreenshot(viewAncestor, pw, "dumpScreenshot");
                String jsonPack = packJsonNode(result, pw, systemWindows, floatWindows);
                if (jsonPack != null) {
                    pw.println(jsonPack);
                }
                pw.flush();
            } catch (Exception e) {
                OplusLog.e(true, TAG, "dumpScreenshot ERROR : " + Log.getStackTraceString(e));
            }
        } finally {
            reportDumpResult(viewAncestor.mContext, result);
            clearList();
            IoUtils.closeQuietly(fd);
            IoUtils.closeQuietly(pw);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void clearList() {
        this.mScrollNodes.clear();
        this.mSmallViews.clear();
        this.mFloatRects.clear();
        this.mSideRects.clear();
        this.mTargets.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MainThreadExecutor implements Executor {
        private final long mDelayMillis;
        private final Handler mHandler;

        private MainThreadExecutor(Handler handler, long delayMillis) {
            this.mHandler = handler;
            this.mDelayMillis = delayMillis;
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable command) {
            if (!this.mHandler.postDelayed(command, this.mDelayMillis)) {
                OplusLog.e(true, OplusLongshotViewDump.TAG, "post command to MainThreadExecutor failed!");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LongshotDumpParam {
        long mDelay;
        Bundle mExtras;
        boolean mIsDumpScrollCapture;
        boolean mIsScrollReset;

        private LongshotDumpParam() {
            this.mIsDumpScrollCapture = false;
            this.mIsScrollReset = true;
            this.mDelay = 0L;
            this.mExtras = new Bundle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ViewNode {
        private final CharSequence mAccessibilityName;
        private final List<ViewNode> mChildList;
        private final CharSequence mClassName;
        private final Rect mClipRect;
        private final Rect mFullRect;
        private int mOverScrollMode;
        private final Rect mScrollRect;
        private long mSpend;
        private final View mView;

        public ViewNode(View view, CharSequence accessibilityName, Rect clipRect, Rect fullRect) {
            Rect rect = new Rect();
            this.mClipRect = rect;
            Rect rect2 = new Rect();
            this.mFullRect = rect2;
            this.mScrollRect = new Rect();
            this.mChildList = new ArrayList();
            this.mSpend = 0L;
            this.mOverScrollMode = -1;
            this.mView = view;
            this.mAccessibilityName = accessibilityName;
            this.mClassName = view.getClass().getName();
            rect.set(clipRect);
            rect2.set(fullRect);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("View[");
            if (this.mAccessibilityName != null) {
                sb.append("accessibility=");
                sb.append(this.mAccessibilityName.toString());
                sb.append(":");
            }
            if (this.mClassName != null) {
                sb.append("class=");
                sb.append(this.mClassName.toString());
                sb.append(":");
            }
            sb.append("clip=");
            sb.append(this.mClipRect);
            sb.append(":full=");
            sb.append(this.mFullRect);
            sb.append(":spend=");
            sb.append(this.mSpend);
            sb.append("]");
            return sb.toString();
        }

        public View getView() {
            return this.mView;
        }

        public void addChild(ViewNode viewNode) {
            this.mChildList.add(viewNode);
        }

        public List<ViewNode> getChildList() {
            return this.mChildList;
        }

        public CharSequence getAccessibilityName() {
            return this.mAccessibilityName;
        }

        public CharSequence getClassName() {
            return this.mClassName;
        }

        public Rect getClipRect() {
            return this.mClipRect;
        }

        public Rect getFullRect() {
            return this.mFullRect;
        }

        public void setSpend(long spend) {
            this.mSpend = spend;
        }

        public void setScrollRect(Rect rect) {
            this.mScrollRect.set(rect);
        }

        public Rect getScrollRect() {
            return this.mScrollRect;
        }

        public void disableOverScroll() {
            ViewRootImpl root = this.mView.getViewRootImpl();
            if (root != null) {
                IViewRootImplExt iViewRootImplExt = root.getWrapper().getExtImpl();
                if (iViewRootImplExt != null) {
                    OplusLog.d(true, OplusLongshotViewDump.TAG, "LongshotViewRoot Connected is " + iViewRootImplExt.isConnected());
                }
                this.mOverScrollMode = this.mView.getOverScrollMode();
                OplusLog.d(true, OplusLongshotViewDump.TAG, "disableOverScroll : " + this.mOverScrollMode);
                this.mView.setOverScrollMode(2);
            }
        }

        public void resetOverScroll() {
            ViewRootImpl root = this.mView.getViewRootImpl();
            if (root != null) {
                IViewRootImplExt iViewRootImplExt = root.getWrapper().getExtImpl();
                if (iViewRootImplExt != null) {
                    OplusLog.d(true, OplusLongshotViewDump.TAG, "LongshotViewRoot Connected is " + iViewRootImplExt.isConnected());
                }
                if (this.mOverScrollMode >= 0) {
                    OplusLog.d(true, OplusLongshotViewDump.TAG, "resetOverScroll : " + this.mOverScrollMode);
                    this.mView.setOverScrollMode(this.mOverScrollMode);
                    this.mOverScrollMode = -1;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ContentComparator implements Comparator<OplusLongshotViewContent> {
        private ContentComparator() {
        }

        @Override // java.util.Comparator
        public int compare(OplusLongshotViewContent content1, OplusLongshotViewContent content2) {
            OplusLongshotViewContent parent1 = content1.getParent();
            OplusLongshotViewContent parent2 = content2.getParent();
            int result = 0;
            if (parent1 != null && parent2 != null && (result = rectCompare(parent1.getRect(), parent2.getRect())) == 0) {
                int hash1 = System.identityHashCode(parent1.getView());
                int hash2 = System.identityHashCode(parent2.getView());
                result = hash2 - hash1;
            }
            if (result == 0) {
                return rectCompare(content1.getRect(), content2.getRect());
            }
            return result;
        }

        private int rectCompare(Rect rect1, Rect rect2) {
            if (rect2.top > rect1.top) {
                return 1;
            }
            if (rect2.top < rect1.top) {
                return -1;
            }
            if (rect2.bottom < rect1.bottom) {
                return 1;
            }
            if (rect2.bottom > rect1.bottom) {
                return -1;
            }
            if (rect2.left > rect1.left) {
                return 1;
            }
            if (rect2.left < rect1.left) {
                return -1;
            }
            if (rect2.right < rect1.right) {
                return 1;
            }
            return rect2.right > rect1.right ? -1 : 0;
        }
    }
}
