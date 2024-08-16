package com.coui.appcompat.searchhistory;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.coui.appcompat.chip.COUIChip;
import com.coui.appcompat.searchhistory.COUIFlowLayout;
import com.google.android.material.R$styleable;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.support.component.R$drawable;
import com.support.component.R$layout;
import com.support.control.R$style;
import fb._Ranges;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import kotlin.Metadata;
import kotlin.collections.MutableCollections;
import kotlin.collections._Collections;
import kotlin.collections.m0;
import kotlin.collections.r;
import kotlin.collections.s;
import m1.COUIMoveEaseInterpolator;
import ma.u;
import rd.Sequence;
import rd._Sequences;
import w1.COUIDarkModeUtil;
import ya.l;
import z2.COUIChangeTextUtil;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.k;

/* compiled from: COUIFlowLayout.kt */
@Metadata(bv = {}, d1 = {"\u0000\u009a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u001f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0016\u0018\u0000 \u00052\u00020\u0001:\u0007\u0089\u0001(\u008a\u0001\u008b\u0001B9\b\u0007\u0012\b\u0010\u0082\u0001\u001a\u00030\u0081\u0001\u0012\f\b\u0002\u0010\u0084\u0001\u001a\u0005\u0018\u00010\u0083\u0001\u0012\t\b\u0002\u0010\u0085\u0001\u001a\u00020\f\u0012\t\b\u0002\u0010\u0086\u0001\u001a\u00020\f¢\u0006\u0006\b\u0087\u0001\u0010\u0088\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\b\u0010\u0005\u001a\u00020\u0004H\u0002J\b\u0010\u0006\u001a\u00020\u0002H\u0002J\b\u0010\u0007\u001a\u00020\u0002H\u0002J\u0014\u0010\u000b\u001a\u00020\u0002*\u00020\b2\u0006\u0010\n\u001a\u00020\tH\u0002J\u0014\u0010\u000e\u001a\u00020\u0004*\u00020\t2\u0006\u0010\r\u001a\u00020\fH\u0002J\b\u0010\u0010\u001a\u00020\u000fH\u0002J\b\u0010\u0011\u001a\u00020\u000fH\u0002J\b\u0010\u0012\u001a\u00020\u0002H\u0002J\b\u0010\u0013\u001a\u00020\u0002H\u0002J\u0010\u0010\u0014\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\tH\u0002J\u0010\u0010\u0015\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\tH\u0002J\u0018\u0010\u0018\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\fH\u0002J\b\u0010\u0019\u001a\u00020\u0002H\u0002J\b\u0010\u001a\u001a\u00020\u000fH\u0003J\u0010\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\fH\u0002J\b\u0010\u001c\u001a\u00020\fH\u0002J\b\u0010\u001d\u001a\u00020\fH\u0002J\u0016\u0010!\u001a\u00020\f2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0\u001eH\u0002J\b\u0010#\u001a\u00020\"H\u0002J\b\u0010$\u001a\u00020\u0002H\u0014J\u0018\u0010%\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\fH\u0014J0\u0010)\u001a\u00020\u00022\u0006\u0010&\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\f2\u0006\u0010'\u001a\u00020\f2\u0006\u0010(\u001a\u00020\fH\u0014J \u0010/\u001a\u00020\u00042\u0006\u0010+\u001a\u00020*2\u0006\u0010,\u001a\u00020\b2\u0006\u0010.\u001a\u00020-H\u0014J\u0014\u00102\u001a\u00020\u00022\f\u00101\u001a\b\u0012\u0004\u0012\u0002000\u001eJ\u0006\u00103\u001a\u00020\u0002J\u000e\u00106\u001a\u00020\u00022\u0006\u00105\u001a\u000204J\u000e\u00109\u001a\u00020\u00022\u0006\u00108\u001a\u000207J\u000e\u0010:\u001a\u00020\u00022\u0006\u00108\u001a\u000207R\"\u0010A\u001a\u00020\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b;\u0010<\u001a\u0004\b=\u0010>\"\u0004\b?\u0010@R\"\u0010E\u001a\u00020\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bB\u0010<\u001a\u0004\bC\u0010>\"\u0004\bD\u0010@R\"\u0010L\u001a\u00020\u00048\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bF\u0010G\u001a\u0004\bH\u0010I\"\u0004\bJ\u0010KR\"\u0010P\u001a\u00020\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bM\u0010<\u001a\u0004\bN\u0010>\"\u0004\bO\u0010@R\"\u0010T\u001a\u00020\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bQ\u0010<\u001a\u0004\bR\u0010>\"\u0004\bS\u0010@R*\u0010U\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u00048\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u000e\u0010G\u001a\u0004\bU\u0010I\"\u0004\bV\u0010KR0\u0010Z\u001a\u001e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u0002000Wj\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u000200`X8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b!\u0010YR\u001a\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0[8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\\R\u001a\u0010`\u001a\b\u0012\u0004\u0012\u00020^0]8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b3\u0010_R\u0018\u00105\u001a\u0004\u0018\u0001048\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0012\u0010aR\u0016\u0010d\u001a\u00020\b8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bb\u0010cR\u0016\u0010f\u001a\u00020\b8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\be\u0010cR\u0018\u0010g\u001a\u0004\u0018\u00010\b8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0013\u0010cR\u0016\u0010h\u001a\u00020\f8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b'\u0010<R\u0016\u0010i\u001a\u00020\f8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0019\u0010<R\u0016\u0010k\u001a\u00020\t8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0018\u0010jR\u0016\u0010m\u001a\u00020\t8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bl\u0010jR\u0018\u0010p\u001a\u0004\u0018\u0001078\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bn\u0010oR\u0018\u0010r\u001a\u0004\u0018\u0001078\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bq\u0010oR\u0014\u0010u\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\bs\u0010tR\u0014\u0010w\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\bv\u0010tR\u0014\u0010y\u001a\u00020\f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\bx\u0010>R\u0014\u0010{\u001a\u00020\f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\bz\u0010>R\u0017\u0010~\u001a\b\u0012\u0004\u0012\u00020\b0\u001e8F¢\u0006\u0006\u001a\u0004\b|\u0010}R\u0018\u0010\u0080\u0001\u001a\b\u0012\u0004\u0012\u00020\b0\u001e8F¢\u0006\u0006\u001a\u0004\b\u007f\u0010}¨\u0006\u008c\u0001"}, d2 = {"Lcom/coui/appcompat/searchhistory/COUIFlowLayout;", "Landroid/view/ViewGroup;", "Lma/f0;", "l", "", "x", "A", "C", "Landroid/view/View;", "", "alpha", "B", "", ThermalBaseConfig.Item.ATTR_VALUE, "j", "Lcom/coui/appcompat/searchhistory/COUIPressFeedbackImageView;", "getExpandButton", "getFoldButton", "n", "q", "setHiddenViewsAlpha", "setVisibleViewsAlpha", "widthMeasureSpec", "heightMeasureSpec", "t", "s", "getBaseExpandButton", "y", "getExpandedStateHeight", "getFoldedStateHeight", "", "Lcom/coui/appcompat/searchhistory/COUIFlowLayout$c;", "lines", "k", "Lcom/coui/appcompat/chip/COUIChip;", "getChip", "onDetachedFromWindow", "onMeasure", "changed", "r", "b", "onLayout", "Landroid/graphics/Canvas;", "canvas", "child", "", "drawingTime", "drawChild", "Lcom/coui/appcompat/searchhistory/COUIFlowLayout$b;", "items", "setItems", "m", "Lcom/coui/appcompat/searchhistory/COUIFlowLayout$d;", "onItemClickListener", "setOnItemClickListener", "Landroid/view/View$OnClickListener;", "clickListener", "setExpandOnClickListener", "setFoldOnClickListener", "e", "I", "getItemSpacing", "()I", "setItemSpacing", "(I)V", "itemSpacing", "f", "getLineSpacing", "setLineSpacing", "lineSpacing", "g", "Z", "getExpandable", "()Z", "setExpandable", "(Z)V", "expandable", "h", "getMaxRowFolded", "setMaxRowFolded", "maxRowFolded", "i", "getMaxRowUnfolded", "setMaxRowUnfolded", "maxRowUnfolded", "isExpand", "setExpand", "Ljava/util/LinkedHashMap;", "Lkotlin/collections/LinkedHashMap;", "Ljava/util/LinkedHashMap;", "itemCache", "", "Ljava/util/List;", "Ljava/util/concurrent/ConcurrentLinkedQueue;", "Landroid/animation/ValueAnimator;", "Ljava/util/concurrent/ConcurrentLinkedQueue;", "runningAnimators", "Lcom/coui/appcompat/searchhistory/COUIFlowLayout$d;", "o", "Landroid/view/View;", "expandButton", "p", "foldButton", "foldLineRemovedChip", "expandedStateHeight", "foldedStateHeight", "F", "tempHiddenViewsAlphaFlg", "u", "tempVisibleViewsAlphaFlg", "v", "Landroid/view/View$OnClickListener;", "expandOnClickListener", "w", "foldOnClickListener", "getFoldButtonAlpha", "()F", "foldButtonAlpha", "getExpandButtonAlpha", "expandButtonAlpha", "getMaxRow", "maxRow", "getContainerLayoutHeight", "containerLayoutHeight", "getHiddenChips", "()Ljava/util/List;", "hiddenChips", "getVisibleChips", "visibleChips", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "defStyleAttr", "defStyleRes", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "a", "c", "d", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public class COUIFlowLayout extends ViewGroup {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private int itemSpacing;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private int lineSpacing;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private boolean expandable;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private int maxRowFolded;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private int maxRowUnfolded;

    /* renamed from: j, reason: collision with root package name and from kotlin metadata */
    private boolean isExpand;

    /* renamed from: k, reason: collision with root package name and from kotlin metadata */
    private final LinkedHashMap<Integer, b> itemCache;

    /* renamed from: l, reason: collision with root package name and from kotlin metadata */
    private final List<c> lines;

    /* renamed from: m, reason: collision with root package name and from kotlin metadata */
    private final ConcurrentLinkedQueue<ValueAnimator> runningAnimators;

    /* renamed from: n, reason: collision with root package name and from kotlin metadata */
    private d onItemClickListener;

    /* renamed from: o, reason: collision with root package name and from kotlin metadata */
    private View expandButton;

    /* renamed from: p, reason: collision with root package name and from kotlin metadata */
    private View foldButton;

    /* renamed from: q, reason: collision with root package name and from kotlin metadata */
    private View foldLineRemovedChip;

    /* renamed from: r, reason: collision with root package name and from kotlin metadata */
    private int expandedStateHeight;

    /* renamed from: s, reason: collision with root package name and from kotlin metadata */
    private int foldedStateHeight;

    /* renamed from: t, reason: collision with root package name and from kotlin metadata */
    private float tempHiddenViewsAlphaFlg;

    /* renamed from: u, reason: collision with root package name and from kotlin metadata */
    private float tempVisibleViewsAlphaFlg;

    /* renamed from: v, reason: collision with root package name and from kotlin metadata */
    private View.OnClickListener expandOnClickListener;

    /* renamed from: w, reason: collision with root package name and from kotlin metadata */
    private View.OnClickListener foldOnClickListener;

    /* compiled from: COUIFlowLayout.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H&¨\u0006\u0004"}, d2 = {"Lcom/coui/appcompat/searchhistory/COUIFlowLayout$b;", "", "", "getContent", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public interface b {
        String getContent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIFlowLayout.kt */
    @Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010!\n\u0002\b\u0015\b\u0002\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0014\u001a\u00020\u0002\u0012\u0006\u0010\u0015\u001a\u00020\u0002¢\u0006\u0004\b)\u0010*J \u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0002H\u0002J\u000e\u0010\n\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\bJ\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\bJ\u000e\u0010\r\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\bJ&\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\fR\u0014\u0010\u0014\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\n\u0010\u0013R\u0014\u0010\u0015\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\r\u0010\u0013R\u001d\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\b0\u00168\u0006¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001aR\"\u0010 \u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0019\u0010\u0013\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\"\u0010\"\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0017\u0010\u001d\"\u0004\b!\u0010\u001fR$\u0010(\u001a\u0004\u0018\u00010\b8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0007\u0010#\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'¨\u0006+"}, d2 = {"Lcom/coui/appcompat/searchhistory/COUIFlowLayout$c;", "", "", "left", "top", "bottom", "Lma/f0;", "f", "Landroid/view/View;", "view", "a", "g", "", "b", "offsetLeft", "offsetTop", "maxHeight", "isRtl", "e", "I", "maxWidth", "horizontalSpace", "", "c", "Ljava/util/List;", "d", "()Ljava/util/List;", "views", "getUsedWidth", "()I", "setUsedWidth", "(I)V", "usedWidth", "setHeight", "height", "Landroid/view/View;", "getRemovedView", "()Landroid/view/View;", "h", "(Landroid/view/View;)V", "removedView", "<init>", "(II)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class c {

        /* renamed from: a, reason: collision with root package name and from kotlin metadata */
        private final int maxWidth;

        /* renamed from: b, reason: collision with root package name and from kotlin metadata */
        private final int horizontalSpace;

        /* renamed from: c, reason: collision with root package name and from kotlin metadata */
        private final List<View> views = new ArrayList();

        /* renamed from: d, reason: collision with root package name and from kotlin metadata */
        private int usedWidth;

        /* renamed from: e, reason: collision with root package name and from kotlin metadata */
        private int height;

        /* renamed from: f, reason: collision with root package name and from kotlin metadata */
        private View removedView;

        public c(int i10, int i11) {
            this.maxWidth = i10;
            this.horizontalSpace = i11;
        }

        private final void f(int i10, int i11, int i12) {
            View view = this.removedView;
            if (view == null) {
                return;
            }
            view.layout(i10, i11, view.getMeasuredWidth() + i10, i12);
        }

        public final void a(View view) {
            k.e(view, "view");
            int size = this.views.size();
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();
            if (size == 0) {
                this.usedWidth = Math.min(measuredWidth, this.maxWidth);
                this.height = measuredHeight;
            } else {
                this.usedWidth += measuredWidth + this.horizontalSpace;
                this.height = Integer.max(measuredHeight, this.height);
            }
            this.views.add(view);
        }

        public final boolean b(View view) {
            k.e(view, "view");
            if (this.views.size() == 0) {
                return true;
            }
            return (this.usedWidth + this.horizontalSpace) + view.getMeasuredWidth() <= this.maxWidth;
        }

        /* renamed from: c, reason: from getter */
        public final int getHeight() {
            return this.height;
        }

        public final List<View> d() {
            return this.views;
        }

        public final void e(int i10, int i11, int i12, boolean z10) {
            int f10;
            int f11;
            for (View view : this.views) {
                int measuredWidth = view.getMeasuredWidth();
                int measuredHeight = view.getMeasuredHeight();
                f10 = _Ranges.f(i11, i12);
                int i13 = i10 + measuredWidth;
                f11 = _Ranges.f(measuredHeight + f10, i12);
                if (z10) {
                    int i14 = this.maxWidth;
                    view.layout(i14 - i13, f10, i14 - i10, f11);
                } else {
                    view.layout(i10, f10, i13, f11);
                }
                if (view instanceof ImageView) {
                    f(i10, f10, f11);
                }
                i10 += measuredWidth + this.horizontalSpace;
            }
        }

        public final void g(View view) {
            k.e(view, "view");
            if (this.views.size() != 0 && this.views.contains(view)) {
                this.usedWidth -= view.getMeasuredWidth() + this.horizontalSpace;
                this.views.remove(view);
            }
        }

        public final void h(View view) {
            this.removedView = view;
        }
    }

    /* compiled from: COUIFlowLayout.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bæ\u0080\u0001\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/searchhistory/COUIFlowLayout$d;", "", "Lcom/coui/appcompat/searchhistory/COUIFlowLayout$b;", "item", "Lma/f0;", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public interface d {
        void a(b bVar);
    }

    /* compiled from: Animator.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\t"}, d2 = {"androidx/core/animation/AnimatorKt$addListener$listener$1", "Landroid/animation/Animator$AnimatorListener;", "Landroid/animation/Animator;", "animator", "Lma/f0;", "onAnimationRepeat", "onAnimationEnd", "onAnimationCancel", "onAnimationStart", "core-ktx_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class e implements Animator.AnimatorListener {
        public e() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            k.e(animator, "animator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            k.e(animator, "animator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            k.e(animator, "animator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            k.e(animator, "animator");
            COUIFlowLayout.this.setExpand(true);
        }
    }

    /* compiled from: Animator.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\t"}, d2 = {"androidx/core/animation/AnimatorKt$addListener$listener$1", "Landroid/animation/Animator$AnimatorListener;", "Landroid/animation/Animator;", "animator", "Lma/f0;", "onAnimationRepeat", "onAnimationEnd", "onAnimationCancel", "onAnimationStart", "core-ktx_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class f implements Animator.AnimatorListener {
        public f() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            k.e(animator, "animator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            k.e(animator, "animator");
            COUIFlowLayout.this.setExpand(false);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            k.e(animator, "animator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            k.e(animator, "animator");
        }
    }

    /* compiled from: COUIFlowLayout.kt */
    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"Landroid/view/View;", "child", "", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    static final class g extends Lambda implements l<View, Boolean> {
        g() {
            super(1);
        }

        public final boolean a(View view) {
            k.e(view, "child");
            List list = COUIFlowLayout.this.lines;
            ArrayList arrayList = new ArrayList();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                MutableCollections.z(arrayList, ((c) it.next()).d());
            }
            return (arrayList.contains(view) || k.a(view, COUIFlowLayout.this.foldLineRemovedChip)) ? false : true;
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Boolean invoke(View view) {
            return Boolean.valueOf(a(view));
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIFlowLayout(Context context) {
        this(context, null, 0, 0, 14, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIFlowLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIFlowLayout(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0, 8, null);
        k.e(context, "context");
    }

    public /* synthetic */ COUIFlowLayout(Context context, AttributeSet attributeSet, int i10, int i11, int i12, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i12 & 2) != 0 ? null : attributeSet, (i12 & 4) != 0 ? 0 : i10, (i12 & 8) != 0 ? 0 : i11);
    }

    private final void A() {
        ConcurrentLinkedQueue<ValueAnimator> concurrentLinkedQueue = this.runningAnimators;
        if (concurrentLinkedQueue == null || concurrentLinkedQueue.isEmpty()) {
            return;
        }
        while (true) {
            ValueAnimator poll = this.runningAnimators.poll();
            if (poll == null) {
                return;
            } else {
                poll.cancel();
            }
        }
    }

    private final void B(View view, float f10) {
        view.setVisibility(j(f10, 0) ? 4 : 0);
        view.setAlpha(f10);
    }

    private final void C() {
        B(this.foldButton, getFoldButtonAlpha());
        B(this.expandButton, getExpandButtonAlpha());
        Iterator<T> it = getVisibleChips().iterator();
        while (it.hasNext()) {
            B((View) it.next(), 1.0f);
        }
        Iterator<T> it2 = getHiddenChips().iterator();
        while (it2.hasNext()) {
            B((View) it2.next(), getFoldButtonAlpha());
        }
        View view = this.foldLineRemovedChip;
        if (view == null) {
            return;
        }
        B(view, getFoldButtonAlpha());
    }

    @SuppressLint({"ClickableViewAccessibility", "CustomViewStyleable"})
    private final COUIPressFeedbackImageView getBaseExpandButton() {
        Context context = getContext();
        k.d(context, "context");
        COUIPressFeedbackImageView cOUIPressFeedbackImageView = new COUIPressFeedbackImageView(context);
        cOUIPressFeedbackImageView.setScaleType(ImageView.ScaleType.CENTER);
        TypedArray obtainStyledAttributes = cOUIPressFeedbackImageView.getContext().obtainStyledAttributes(R$style.Widget_COUI_Chip, R$styleable.Chip);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.Chip_chipMinHeight, 0);
        cOUIPressFeedbackImageView.setLayoutParams(new ViewGroup.LayoutParams(dimensionPixelSize, dimensionPixelSize));
        obtainStyledAttributes.recycle();
        COUIDarkModeUtil.b(cOUIPressFeedbackImageView, false);
        return cOUIPressFeedbackImageView;
    }

    private final COUIChip getChip() {
        View inflate = View.inflate(getContext(), R$layout.coui_component_item_search_history, null);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.coui.appcompat.chip.COUIChip");
        COUIChip cOUIChip = (COUIChip) inflate;
        COUIChangeTextUtil.c(cOUIChip, 4);
        cOUIChip.setOnClickListener(new View.OnClickListener() { // from class: r2.d
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                COUIFlowLayout.u(COUIFlowLayout.this, view);
            }
        });
        return cOUIChip;
    }

    private final int getContainerLayoutHeight() {
        if (this.isExpand) {
            return this.expandedStateHeight;
        }
        return this.foldedStateHeight;
    }

    private final COUIPressFeedbackImageView getExpandButton() {
        COUIPressFeedbackImageView baseExpandButton = getBaseExpandButton();
        baseExpandButton.setImageResource(R$drawable.coui_component_expand_arrow_drop_down);
        baseExpandButton.setOnClickListener(new View.OnClickListener() { // from class: r2.e
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                COUIFlowLayout.v(COUIFlowLayout.this, view);
            }
        });
        return baseExpandButton;
    }

    private final float getExpandButtonAlpha() {
        return this.isExpand ? 0.0f : 1.0f;
    }

    private final int getExpandedStateHeight() {
        return k(this.lines) + getPaddingTop() + getPaddingBottom();
    }

    private final COUIPressFeedbackImageView getFoldButton() {
        COUIPressFeedbackImageView baseExpandButton = getBaseExpandButton();
        baseExpandButton.setImageResource(R$drawable.coui_component_expand_arrow_drop_up);
        baseExpandButton.setOnClickListener(new View.OnClickListener() { // from class: r2.f
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                COUIFlowLayout.w(COUIFlowLayout.this, view);
            }
        });
        return baseExpandButton;
    }

    private final float getFoldButtonAlpha() {
        return this.isExpand ? 1.0f : 0.0f;
    }

    private final int getFoldedStateHeight() {
        List<c> list = this.lines;
        ArrayList arrayList = new ArrayList();
        int i10 = 0;
        for (Object obj : list) {
            int i11 = i10 + 1;
            if (i10 < 0) {
                r.t();
            }
            if (i10 < getMaxRowFolded()) {
                arrayList.add(obj);
            }
            i10 = i11;
        }
        return k(arrayList) + getPaddingTop() + getPaddingBottom();
    }

    private final int getMaxRow() {
        if (this.isExpand) {
            return this.maxRowUnfolded;
        }
        return this.maxRowFolded;
    }

    private final boolean j(float f10, int i10) {
        return ((double) (f10 - ((float) i10))) < 0.001d;
    }

    private final int k(List<c> lines) {
        Iterator<T> it = lines.iterator();
        int i10 = 0;
        while (it.hasNext()) {
            i10 += ((c) it.next()).getHeight();
        }
        return i10 + (this.lineSpacing * (lines.size() - 1));
    }

    private final void l() {
        A();
    }

    private final void n() {
        if (x()) {
            A();
        } else {
            this.tempHiddenViewsAlphaFlg = -1.0f;
            this.tempVisibleViewsAlphaFlg = -1.0f;
            A();
        }
        float[] fArr = new float[2];
        float f10 = this.tempVisibleViewsAlphaFlg;
        if (f10 < 0.0f) {
            f10 = 1.0f;
        }
        fArr[0] = f10;
        fArr[1] = 0.0f;
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("expand_button_alpha", fArr);
        float[] fArr2 = new float[2];
        float f11 = this.tempHiddenViewsAlphaFlg;
        fArr2[0] = f11 >= 0.0f ? f11 : 0.0f;
        fArr2[1] = 1.0f;
        PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat("fold_button_alpha", fArr2);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setValues(ofFloat2);
        valueAnimator.setInterpolator(new COUIMoveEaseInterpolator());
        valueAnimator.setDuration(400L);
        valueAnimator.setStartDelay(100L);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: r2.a
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                COUIFlowLayout.o(COUIFlowLayout.this, valueAnimator2);
            }
        });
        valueAnimator.start();
        this.runningAnimators.add(valueAnimator);
        ValueAnimator valueAnimator2 = new ValueAnimator();
        valueAnimator2.setValues(ofFloat);
        valueAnimator2.setInterpolator(new COUIMoveEaseInterpolator());
        valueAnimator2.setDuration(250L);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: r2.c
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                COUIFlowLayout.p(COUIFlowLayout.this, valueAnimator3);
            }
        });
        valueAnimator2.addListener(new e());
        valueAnimator2.start();
        this.runningAnimators.add(valueAnimator2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void o(COUIFlowLayout cOUIFlowLayout, ValueAnimator valueAnimator) {
        k.e(cOUIFlowLayout, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue("fold_button_alpha");
        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        float floatValue = ((Float) animatedValue).floatValue();
        cOUIFlowLayout.tempHiddenViewsAlphaFlg = floatValue;
        cOUIFlowLayout.setHiddenViewsAlpha(floatValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void p(COUIFlowLayout cOUIFlowLayout, ValueAnimator valueAnimator) {
        k.e(cOUIFlowLayout, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue("expand_button_alpha");
        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        float floatValue = ((Float) animatedValue).floatValue();
        cOUIFlowLayout.tempVisibleViewsAlphaFlg = floatValue;
        cOUIFlowLayout.setVisibleViewsAlpha(floatValue);
    }

    private final void q() {
        if (x()) {
            A();
        } else {
            this.tempHiddenViewsAlphaFlg = -1.0f;
            this.tempVisibleViewsAlphaFlg = -1.0f;
            A();
        }
        float[] fArr = new float[2];
        float f10 = this.tempVisibleViewsAlphaFlg;
        if (f10 < 0.0f) {
            f10 = 0.0f;
        }
        fArr[0] = f10;
        fArr[1] = 1.0f;
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("expand_button_alpha", fArr);
        float[] fArr2 = new float[2];
        float f11 = this.tempHiddenViewsAlphaFlg;
        fArr2[0] = f11 >= 0.0f ? f11 : 1.0f;
        fArr2[1] = 0.0f;
        PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat("fold_button_alpha", fArr2);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setValues(ofFloat, ofFloat2);
        valueAnimator.setInterpolator(new COUIMoveEaseInterpolator());
        valueAnimator.setDuration(300L);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: r2.b
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                COUIFlowLayout.r(COUIFlowLayout.this, valueAnimator2);
            }
        });
        valueAnimator.addListener(new f());
        valueAnimator.start();
        this.runningAnimators.add(valueAnimator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void r(COUIFlowLayout cOUIFlowLayout, ValueAnimator valueAnimator) {
        k.e(cOUIFlowLayout, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue("fold_button_alpha");
        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        cOUIFlowLayout.tempHiddenViewsAlphaFlg = ((Float) animatedValue).floatValue();
        Object animatedValue2 = valueAnimator.getAnimatedValue("expand_button_alpha");
        Objects.requireNonNull(animatedValue2, "null cannot be cast to non-null type kotlin.Float");
        cOUIFlowLayout.tempVisibleViewsAlphaFlg = ((Float) animatedValue2).floatValue();
        cOUIFlowLayout.setHiddenViewsAlpha(cOUIFlowLayout.tempHiddenViewsAlphaFlg);
        cOUIFlowLayout.setVisibleViewsAlpha(cOUIFlowLayout.tempVisibleViewsAlphaFlg);
    }

    private final void s() {
        Object g02;
        Object e02;
        Object g03;
        ArrayList arrayList = new ArrayList(this.lines);
        int size = arrayList.size();
        int i10 = this.maxRowFolded;
        if (size < i10 - 1) {
            return;
        }
        c cVar = (c) arrayList.get(i10 - 1);
        g02 = _Collections.g0(cVar.d());
        View view = (View) g02;
        if (view != null && !cVar.b(this.foldButton)) {
            cVar.g(view);
            cVar.h(view);
            this.foldLineRemovedChip = view;
        }
        cVar.a(this.expandButton);
        if (this.isExpand) {
            e02 = _Collections.e0(arrayList);
            c cVar2 = (c) e02;
            g03 = _Collections.g0(cVar2.d());
            View view2 = (View) g03;
            if (view2 != null && !cVar2.b(this.expandButton)) {
                cVar2.g(view2);
            }
            cVar2.a(this.foldButton);
        }
    }

    private final void setHiddenViewsAlpha(float f10) {
        B(this.foldButton, f10);
        Iterator<T> it = getHiddenChips().iterator();
        while (it.hasNext()) {
            B((View) it.next(), f10);
        }
        View view = this.foldLineRemovedChip;
        if (view == null) {
            return;
        }
        B(view, f10);
    }

    private final void setVisibleViewsAlpha(float f10) {
        B(this.expandButton, f10);
        Iterator<T> it = getVisibleChips().iterator();
        while (it.hasNext()) {
            B((View) it.next(), 1.0f);
        }
    }

    private final void t(int i10, int i11) {
        this.lines.clear();
        this.foldLineRemovedChip = null;
        int size = (View.MeasureSpec.getSize(i10) - getPaddingLeft()) - getPaddingRight();
        c cVar = new c(size, this.itemSpacing);
        this.lines.add(cVar);
        measureChild(this.expandButton, i10, i11);
        measureChild(this.foldButton, i10, i11);
        int childCount = getChildCount();
        if (childCount <= 0) {
            return;
        }
        int i12 = 0;
        boolean z10 = false;
        while (true) {
            int i13 = i12 + 1;
            View childAt = getChildAt(i12);
            if (!(childAt instanceof ImageView)) {
                if (z10) {
                    k.d(childAt, "view");
                    B(childAt, 0.0f);
                } else {
                    measureChild(childAt, i10, i11);
                    k.d(childAt, "view");
                    if (cVar.b(childAt)) {
                        cVar.a(childAt);
                    } else if (this.lines.size() >= getMaxRow()) {
                        B(childAt, 0.0f);
                        z10 = true;
                    } else {
                        cVar = new c(size, this.itemSpacing);
                        cVar.a(childAt);
                        this.lines.add(cVar);
                    }
                }
            }
            if (i13 >= childCount) {
                return;
            } else {
                i12 = i13;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void u(COUIFlowLayout cOUIFlowLayout, View view) {
        d dVar;
        k.e(cOUIFlowLayout, "this$0");
        b bVar = cOUIFlowLayout.itemCache.get(Integer.valueOf(view.getId()));
        if (bVar == null || (dVar = cOUIFlowLayout.onItemClickListener) == null) {
            return;
        }
        dVar.a(bVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void v(COUIFlowLayout cOUIFlowLayout, View view) {
        k.e(cOUIFlowLayout, "this$0");
        cOUIFlowLayout.n();
        View.OnClickListener onClickListener = cOUIFlowLayout.expandOnClickListener;
        if (onClickListener == null || onClickListener == null) {
            return;
        }
        onClickListener.onClick(view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void w(COUIFlowLayout cOUIFlowLayout, View view) {
        k.e(cOUIFlowLayout, "this$0");
        cOUIFlowLayout.q();
        View.OnClickListener onClickListener = cOUIFlowLayout.foldOnClickListener;
        if (onClickListener == null || onClickListener == null) {
            return;
        }
        onClickListener.onClick(view);
    }

    private final boolean x() {
        ConcurrentLinkedQueue<ValueAnimator> concurrentLinkedQueue = this.runningAnimators;
        if ((concurrentLinkedQueue instanceof Collection) && concurrentLinkedQueue.isEmpty()) {
            return false;
        }
        Iterator<T> it = concurrentLinkedQueue.iterator();
        while (it.hasNext()) {
            if (((ValueAnimator) it.next()).isRunning()) {
                return true;
            }
        }
        return false;
    }

    private final boolean y(int widthMeasureSpec) {
        int measuredWidth;
        int size = (View.MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()) - getPaddingRight();
        int childCount = getChildCount();
        if (childCount > 0) {
            int i10 = 0;
            int i11 = 0;
            int i12 = 0;
            int i13 = 0;
            while (true) {
                int i14 = i10 + 1;
                View childAt = getChildAt(i10);
                if (!(childAt instanceof ImageView)) {
                    if (i11 == 0) {
                        i13++;
                        i11++;
                        i12 = Math.min(childAt.getMeasuredWidth(), size);
                    } else {
                        if (childAt.getMeasuredWidth() + i12 + this.itemSpacing > size) {
                            i13++;
                            i11 = 0;
                            i12 = 0;
                        }
                        if (i11 == 0) {
                            measuredWidth = Math.min(childAt.getMeasuredWidth(), size);
                        } else {
                            measuredWidth = childAt.getMeasuredWidth() + this.itemSpacing;
                        }
                        i12 += measuredWidth;
                        i11++;
                    }
                    if (i13 > this.maxRowFolded) {
                        return true;
                    }
                }
                if (i14 >= childCount) {
                    break;
                }
                i10 = i14;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void z(COUIFlowLayout cOUIFlowLayout, View view, d dVar, View view2) {
        k.e(cOUIFlowLayout, "this$0");
        k.e(view, "$view");
        k.e(dVar, "$onItemClickListener");
        b bVar = cOUIFlowLayout.itemCache.get(Integer.valueOf(((COUIChip) view).getId()));
        if (bVar == null) {
            return;
        }
        dVar.a(bVar);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        k.e(canvas, "canvas");
        k.e(child, "child");
        if (child.getTop() >= getContainerLayoutHeight() || child.getHeight() == 0) {
            return false;
        }
        return super.drawChild(canvas, child, drawingTime);
    }

    public final boolean getExpandable() {
        return this.expandable;
    }

    public final List<View> getHiddenChips() {
        List<c> list = this.lines;
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = list.iterator();
        int i10 = 0;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            int i11 = i10 + 1;
            if (i10 < 0) {
                r.t();
            }
            if (i10 >= getMaxRowFolded()) {
                arrayList.add(next);
            }
            i10 = i11;
        }
        ArrayList arrayList2 = new ArrayList();
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            MutableCollections.z(arrayList2, ((c) it2.next()).d());
        }
        ArrayList arrayList3 = new ArrayList();
        for (Object obj : arrayList2) {
            if (!(((View) obj) instanceof ImageView)) {
                arrayList3.add(obj);
            }
        }
        return arrayList3;
    }

    public final int getItemSpacing() {
        return this.itemSpacing;
    }

    public final int getLineSpacing() {
        return this.lineSpacing;
    }

    public final int getMaxRowFolded() {
        return this.maxRowFolded;
    }

    public final int getMaxRowUnfolded() {
        return this.maxRowUnfolded;
    }

    public final List<View> getVisibleChips() {
        List<c> list = this.lines;
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            MutableCollections.z(arrayList, ((c) it.next()).d());
        }
        ArrayList arrayList2 = new ArrayList();
        for (Object obj : arrayList) {
            View view = (View) obj;
            if ((getHiddenChips().contains(view) || (view instanceof ImageView)) ? false : true) {
                arrayList2.add(obj);
            }
        }
        return arrayList2;
    }

    public final void m() {
        this.lines.clear();
        this.foldLineRemovedChip = null;
        removeAllViews();
        this.itemCache.clear();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        l();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        Sequence m10;
        int paddingTop = getPaddingTop();
        boolean z11 = getLayoutDirection() == 1;
        for (c cVar : this.lines) {
            cVar.e(getPaddingStart(), paddingTop, getContainerLayoutHeight(), z11);
            paddingTop += cVar.getHeight() + getLineSpacing();
        }
        m10 = _Sequences.m(androidx.core.view.ViewGroup.b(this), new g());
        Iterator it = m10.iterator();
        while (it.hasNext()) {
            ((View) it.next()).layout(0, 0, 0, 0);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i11);
        if (this.itemCache.isEmpty()) {
            setMeasuredDimension(View.MeasureSpec.getSize(i10), View.MeasureSpec.makeMeasureSpec(0, mode));
            return;
        }
        t(i10, i11);
        this.expandedStateHeight = getExpandedStateHeight();
        this.foldedStateHeight = getFoldedStateHeight();
        if (y(i10) && this.expandable) {
            s();
        }
        if (!x()) {
            C();
        }
        setMeasuredDimension(i10, View.MeasureSpec.makeMeasureSpec(getContainerLayoutHeight(), mode));
    }

    public final void setExpand(boolean z10) {
        this.isExpand = z10;
        requestLayout();
    }

    public final void setExpandOnClickListener(View.OnClickListener onClickListener) {
        k.e(onClickListener, "clickListener");
        this.expandOnClickListener = onClickListener;
    }

    public final void setExpandable(boolean z10) {
        this.expandable = z10;
    }

    public final void setFoldOnClickListener(View.OnClickListener onClickListener) {
        k.e(onClickListener, "clickListener");
        this.foldOnClickListener = onClickListener;
    }

    public final void setItemSpacing(int i10) {
        this.itemSpacing = i10;
    }

    public final void setItems(List<? extends b> list) {
        int u7;
        k.e(list, "items");
        this.itemCache.clear();
        LinkedHashMap<Integer, b> linkedHashMap = this.itemCache;
        u7 = s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(u.a(Integer.valueOf(View.generateViewId()), (b) it.next()));
        }
        m0.o(linkedHashMap, arrayList);
        removeAllViews();
        this.expandButton = getExpandButton();
        this.foldButton = getFoldButton();
        for (Map.Entry<Integer, b> entry : this.itemCache.entrySet()) {
            COUIChip chip = getChip();
            chip.setId(entry.getKey().intValue());
            chip.setText(entry.getValue().getContent());
            addView(chip);
        }
        addView(this.expandButton);
        addView(this.foldButton);
    }

    public final void setLineSpacing(int i10) {
        this.lineSpacing = i10;
    }

    public final void setMaxRowFolded(int i10) {
        this.maxRowFolded = i10;
    }

    public final void setMaxRowUnfolded(int i10) {
        this.maxRowUnfolded = i10;
    }

    public final void setOnItemClickListener(final d dVar) {
        k.e(dVar, "onItemClickListener");
        for (final View view : androidx.core.view.ViewGroup.b(this)) {
            if (view instanceof COUIChip) {
                if (view.getVisibility() == 0) {
                    view.setOnClickListener(new View.OnClickListener() { // from class: r2.g
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            COUIFlowLayout.z(COUIFlowLayout.this, view, dVar, view2);
                        }
                    });
                }
            }
        }
        this.onItemClickListener = dVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIFlowLayout(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        k.e(context, "context");
        this.itemCache = new LinkedHashMap<>();
        this.lines = new ArrayList();
        this.runningAnimators = new ConcurrentLinkedQueue<>();
        this.expandButton = getExpandButton();
        this.foldButton = getFoldButton();
        this.tempHiddenViewsAlphaFlg = -1.0f;
        this.tempVisibleViewsAlphaFlg = -1.0f;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.support.component.R$styleable.COUIFlowLayout, i10, i11);
        setMaxRowFolded(obtainStyledAttributes.getInteger(com.support.component.R$styleable.COUIFlowLayout_maxRowFolded, Integer.MAX_VALUE));
        setMaxRowUnfolded(obtainStyledAttributes.getInteger(com.support.component.R$styleable.COUIFlowLayout_maxRowUnfolded, Integer.MAX_VALUE));
        setLineSpacing(obtainStyledAttributes.getDimensionPixelOffset(com.support.component.R$styleable.COUIFlowLayout_lineSpacing, 0));
        setItemSpacing(obtainStyledAttributes.getDimensionPixelOffset(com.support.component.R$styleable.COUIFlowLayout_itemSpacing, 0));
        setExpandable(obtainStyledAttributes.getBoolean(com.support.component.R$styleable.COUIFlowLayout_expandable, true));
        obtainStyledAttributes.recycle();
        if (this.expandable) {
            return;
        }
        this.maxRowUnfolded = this.maxRowFolded;
    }
}
