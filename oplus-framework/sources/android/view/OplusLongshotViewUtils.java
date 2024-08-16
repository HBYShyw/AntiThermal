package android.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.oplus.util.OplusLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public class OplusLongshotViewUtils {
    private static final boolean DBG = true;
    private static final RectComparator RECT_COMPARATOR = new RectComparator();
    private static final String TAG = "LongshotDump/OplusLongshotViewUtils";
    private final Rect mTempRect1 = new Rect();
    private final Rect mTempRect2 = new Rect();
    private final Rect mTempRect3 = new Rect();

    public OplusLongshotViewUtils(Context context) {
    }

    private boolean needUpdateParent(Rect viewRect, Rect rootRect, OplusLongshotViewContent parent, boolean keepLargeRect) {
        if (isLargeCoverRect(viewRect, rootRect, keepLargeRect)) {
            return false;
        }
        return isLargeCoverRect(parent.getRect(), rootRect, keepLargeRect);
    }

    public void findCoverRect(int recursive, ViewGroup group, View view, List<OplusLongshotViewContent> coverContents, List<OplusLongshotViewContent> sideContents, Rect rootRect, OplusLongshotViewContent parent, boolean keepLargeRect) {
        ArrayList<View> preorderedList;
        int i;
        ArrayList<View> preorderedList2;
        int childrenCount;
        String prefix;
        Rect rootRect2;
        boolean z;
        View child;
        OplusLongshotViewContent parent2;
        int childIndex;
        OplusLongshotViewContent parent3;
        int i2;
        Rect srcRect = new Rect();
        boolean z2 = true;
        group.getBoundsOnScreen(srcRect, true);
        Rect rootRect3 = rootRect == null ? srcRect : rootRect;
        boolean z3 = false;
        boolean initParent = parent == null;
        String prefix2 = getPrefix(recursive);
        String str = TAG;
        OplusLog.d(true, TAG, prefix2 + "findCoverRect : rootRect=" + rootRect3 + ", srcRect=" + srcRect + ", group=" + group + ", keepLargeRect=" + keepLargeRect);
        int childrenCount2 = group.getChildCount();
        ArrayList<View> preorderedList3 = group.buildOrderedChildList();
        boolean noPreorder = preorderedList3 == null;
        if (noPreorder && group.isChildrenDrawingOrderEnabled()) {
            z3 = true;
        }
        boolean customOrder = z3;
        OplusLongshotViewContent parent4 = parent;
        int i3 = childrenCount2 - 1;
        while (true) {
            if (i3 < 0) {
                preorderedList = preorderedList3;
                break;
            }
            int childIndex2 = customOrder ? group.getChildDrawingOrder(childrenCount2, i3) : i3;
            View child2 = noPreorder ? group.getChildAt(childIndex2) : preorderedList3.get(childIndex2);
            if (child2 == null) {
                i = i3;
                preorderedList2 = preorderedList3;
                childrenCount = childrenCount2;
                prefix = prefix2;
                rootRect2 = rootRect3;
                z = z2;
            } else {
                if (child2 == view) {
                    preorderedList = preorderedList3;
                    break;
                }
                if (child2.isVisibleToUser()) {
                    child2.getBoundsOnScreen(this.mTempRect3, z2);
                    if (!Rect.intersects(this.mTempRect3, rootRect3)) {
                        i = i3;
                        preorderedList2 = preorderedList3;
                        childrenCount = childrenCount2;
                        prefix = prefix2;
                        rootRect2 = rootRect3;
                        z = z2;
                    } else if (isTransparentGroup(child2)) {
                        if (initParent) {
                            childIndex = childIndex2;
                            parent3 = new OplusLongshotViewContent(child2, this.mTempRect3, null);
                        } else {
                            childIndex = childIndex2;
                            parent3 = parent4;
                        }
                        if (isWaterMarkGroup(rootRect3, (ViewGroup) child2)) {
                            i2 = i3;
                            OplusLog.d(true, str, prefix2 + "  skipCoverRect : isWaterMarkGroup=" + new OplusLongshotViewContent(child2, this.mTempRect3, parent3));
                        } else {
                            i2 = i3;
                            if (isSideBarGroup(rootRect3, (ViewGroup) child2, sideContents)) {
                                OplusLongshotViewContent viewContent = new OplusLongshotViewContent(child2, this.mTempRect3, parent3);
                                OplusLog.d(true, str, prefix2 + "  skipCoverRect : isSideBarGroup=" + viewContent);
                                sideContents.add(viewContent);
                            } else {
                                OplusLongshotViewContent parent5 = needUpdateParent(this.mTempRect3, rootRect3, parent3, keepLargeRect) ? new OplusLongshotViewContent(child2, this.mTempRect3, null) : parent3;
                                List<OplusLongshotViewContent> childCoverContents = new ArrayList<>();
                                i = i2;
                                preorderedList2 = preorderedList3;
                                childrenCount = childrenCount2;
                                findCoverRect(recursive + 1, (ViewGroup) child2, null, childCoverContents, sideContents, rootRect3, parent5, keepLargeRect);
                                coverContents.addAll(childCoverContents);
                                prefix = prefix2;
                                parent4 = parent5;
                                str = str;
                                rootRect2 = rootRect3;
                                z = true;
                            }
                        }
                        i = i2;
                        parent4 = parent3;
                        preorderedList2 = preorderedList3;
                        childrenCount = childrenCount2;
                        prefix = prefix2;
                        rootRect2 = rootRect3;
                        z = true;
                    } else {
                        i = i3;
                        preorderedList2 = preorderedList3;
                        childrenCount = childrenCount2;
                        String str2 = str;
                        String prefix3 = prefix2;
                        Rect rootRect4 = rootRect3;
                        if (hasVisibleContent(prefix3, child2, rootRect4, keepLargeRect, "noCoverContent")) {
                            if (initParent) {
                                child = child2;
                                parent2 = new OplusLongshotViewContent(child, this.mTempRect3, null);
                            } else {
                                child = child2;
                                parent2 = parent4;
                            }
                            OplusLongshotViewContent viewContent2 = new OplusLongshotViewContent(child, this.mTempRect3, parent2);
                            rootRect2 = rootRect4;
                            if (isSideBarRect(this.mTempRect3, rootRect2)) {
                                prefix = prefix3;
                                str = str2;
                                OplusLog.d(true, str, prefix + "  skipCoverRect : isSideBarView=" + viewContent2);
                                if (sideContents != null) {
                                    sideContents.add(viewContent2);
                                    z = true;
                                } else {
                                    z = true;
                                }
                            } else {
                                prefix = prefix3;
                                str = str2;
                                z = true;
                                OplusLog.d(true, str, prefix + "  addCoverRect : " + viewContent2);
                                coverContents.add(viewContent2);
                            }
                            parent4 = parent2;
                        } else {
                            prefix = prefix3;
                            str = str2;
                            rootRect2 = rootRect4;
                            z = true;
                        }
                    }
                } else {
                    i = i3;
                    preorderedList2 = preorderedList3;
                    childrenCount = childrenCount2;
                    prefix = prefix2;
                    rootRect2 = rootRect3;
                    z = z2;
                }
            }
            i3 = i - 1;
            rootRect3 = rootRect2;
            z2 = z;
            preorderedList3 = preorderedList2;
            prefix2 = prefix;
            childrenCount2 = childrenCount;
        }
        if (preorderedList != null) {
            preorderedList.clear();
        }
    }

    public boolean isBottomBarRect(Rect viewRect, Rect rootRect) {
        return viewRect.width() == rootRect.width() && viewRect.bottom == rootRect.bottom;
    }

    public boolean isLargeCoverRect(Rect viewRect, Rect rootRect, boolean keepLargeRect) {
        if (!keepLargeRect) {
            if (viewRect.contains(rootRect)) {
                return true;
            }
            Rect intRect = new Rect();
            return intRect.setIntersect(viewRect, rootRect) && intRect.width() >= getMinSize(rootRect.width()) && intRect.height() >= getMinSize(rootRect.height());
        }
        return false;
    }

    private int getMinSize(int size) {
        return (size * 3) / 4;
    }

    private String getPrefix(int recursive) {
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < recursive; i++) {
            prefix.append("    ");
        }
        return prefix.toString();
    }

    private boolean isSmallCoverRect(Rect viewRect, Rect rootRect) {
        return viewRect.width() <= 1 && viewRect.height() <= 1;
    }

    private boolean isCenterCoverRect(Rect viewRect, Rect rootRect) {
        Rect centerRect = new Rect();
        centerRect.set(rootRect);
        centerRect.inset(rootRect.width() / 3, rootRect.height() / 3);
        return centerRect.contains(viewRect);
    }

    private boolean isTransparentDrawable(Drawable drawable) {
        return drawable == null || -2 == drawable.getOpacity();
    }

    private boolean isTransparentGroup(View view) {
        if (!(view instanceof GridView) && (view instanceof ViewGroup)) {
            return isTransparentDrawable(view.getBackground());
        }
        return false;
    }

    private void initCenterRect(Rect centerRect, Rect rootRect) {
        centerRect.set(rootRect);
        centerRect.inset(rootRect.width() / 4, rootRect.height() / 4);
    }

    private void printNoContentLog(String prefix, String tag, String type, Rect rect, View view) {
        if (prefix != null && tag != null) {
            OplusLog.d(true, TAG, prefix + "  " + tag + " : " + type + "=" + rect + ":" + view);
        }
    }

    private boolean hasVisibleContent(String prefix, View view, Rect rootRect, boolean keepLargeRect, String tag) {
        view.getBoundsOnScreen(this.mTempRect1, true);
        if (isCenterCoverRect(this.mTempRect1, rootRect)) {
            printNoContentLog(prefix, tag, "CenterCover", this.mTempRect1, view);
            return false;
        }
        if (!isTransparentDrawable(view.getBackground())) {
            return true;
        }
        if (view instanceof TextView) {
            TextView contentView = (TextView) view;
            Drawable[] drawables = contentView.getCompoundDrawables();
            for (Drawable drawable : drawables) {
                if (!isTransparentDrawable(drawable)) {
                    return true;
                }
            }
            if (!TextUtils.isEmpty(contentView.getHint()) || !TextUtils.isEmpty(contentView.getText())) {
                return true;
            }
            printNoContentLog(prefix, tag, "TextView", this.mTempRect1, view);
            return false;
        }
        if (view instanceof ImageView) {
            if (!isTransparentDrawable(((ImageView) view).getDrawable())) {
                return true;
            }
            printNoContentLog(prefix, tag, "ImageView", this.mTempRect1, view);
            return false;
        }
        if (isLargeCoverRect(this.mTempRect1, rootRect, keepLargeRect)) {
            printNoContentLog(prefix, tag, "LargeCover", this.mTempRect1, view);
            return false;
        }
        if (!isSmallCoverRect(this.mTempRect1, rootRect)) {
            return true;
        }
        printNoContentLog(prefix, tag, "SmallCover", this.mTempRect1, view);
        return false;
    }

    private boolean isNeighboringRect(Rect rect1, Rect rect2) {
        return rect1.top == rect2.bottom;
    }

    private boolean isSameLineRect(Rect rect1, Rect rect2) {
        return rect1.top == rect2.top && rect1.bottom == rect2.bottom;
    }

    private boolean isSideBarRect(Rect coverRect, Rect rootRect) {
        return coverRect.width() <= rootRect.width() / 3 && coverRect.height() >= (rootRect.height() * 2) / 5;
    }

    private boolean findSideBarContent(View view, Rect rootRect, Rect itemRect) {
        if (itemRect != null) {
            view.getBoundsOnScreen(this.mTempRect1, true);
            if (itemRect.isEmpty()) {
                itemRect.set(this.mTempRect1);
            }
        }
        if (!isTransparentGroup(view)) {
            return hasVisibleContent(null, view, rootRect, false, null);
        }
        boolean result = false;
        ViewGroup group = (ViewGroup) view;
        int childrenCount = group.getChildCount();
        ArrayList<View> preorderedList = group.buildOrderedChildList();
        boolean noPreorder = preorderedList == null;
        boolean customOrder = noPreorder && group.isChildrenDrawingOrderEnabled();
        int i = childrenCount - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            int childIndex = customOrder ? group.getChildDrawingOrder(childrenCount, i) : i;
            View child = noPreorder ? group.getChildAt(childIndex) : preorderedList.get(childIndex);
            if (child != null && child.isVisibleToUser() && findSideBarContent(child, rootRect, null)) {
                result = true;
                break;
            }
            i--;
        }
        if (preorderedList != null) {
            preorderedList.clear();
        }
        return result;
    }

    private boolean isWaterMarkGroup(Rect rootRect, ViewGroup group) {
        this.mTempRect2.setEmpty();
        boolean allTextView = true;
        int i = group.getChildCount() - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            View child = group.getChildAt(i);
            if (child instanceof TextView) {
                child.getBoundsOnScreen(this.mTempRect1, true);
                this.mTempRect2.union(this.mTempRect1);
                i--;
            } else {
                allTextView = false;
                break;
            }
        }
        initCenterRect(this.mTempRect1, rootRect);
        return allTextView && Rect.intersects(this.mTempRect2, this.mTempRect1);
    }

    private boolean isLargeWidth(View view, Rect rect) {
        return view.getWidth() > (rect.width() * 2) / 3;
    }

    private boolean isSideBarGroup(Rect rootRect, ViewGroup group, List<OplusLongshotViewContent> sideContents) {
        boolean noPreorder;
        boolean customOrder = false;
        if (sideContents == null || isLargeWidth(group, rootRect)) {
            return false;
        }
        Rect itemRect = new Rect();
        List<Rect> rects = new ArrayList<>();
        int childrenCount = group.getChildCount();
        ArrayList<View> preorderedList = group.buildOrderedChildList();
        if (preorderedList != null) {
            noPreorder = false;
        } else {
            noPreorder = true;
        }
        if (noPreorder && group.isChildrenDrawingOrderEnabled()) {
            customOrder = true;
        }
        for (int i = childrenCount - 1; i >= 0; i--) {
            int childIndex = customOrder ? group.getChildDrawingOrder(childrenCount, i) : i;
            View child = noPreorder ? group.getChildAt(childIndex) : preorderedList.get(childIndex);
            if (child != null && child.isVisibleToUser() && !isLargeWidth(child, rootRect) && findSideBarContent(child, rootRect, itemRect)) {
                child.getBoundsOnScreen(this.mTempRect1, true);
                rects.add(new Rect(this.mTempRect1));
            }
        }
        if (preorderedList != null) {
            preorderedList.clear();
        }
        Collections.sort(rects, RECT_COMPARATOR);
        this.mTempRect1.setEmpty();
        Rect last = new Rect();
        for (Rect rect : rects) {
            if (last.isEmpty() || isNeighboringRect(last, rect)) {
                this.mTempRect1.union(rect);
            }
            last.set(rect);
        }
        return isSideBarRect(this.mTempRect1, rootRect);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class RectComparator implements Comparator<Rect> {
        private RectComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Rect rect1, Rect rect2) {
            int result = rect2.top - rect1.top;
            if (result != 0) {
                return rect2.left - rect1.left;
            }
            return result;
        }
    }
}
