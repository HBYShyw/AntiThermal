package com.coui.appcompat.expandable;

import android.R;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.HeterogeneousExpandableList;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import m1.COUIMoveEaseInterpolator;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUIExpandableListView extends ExpandableListView {

    /* renamed from: e, reason: collision with root package name */
    private ExpandableListView.OnGroupClickListener f5875e;

    /* renamed from: f, reason: collision with root package name */
    private f f5876f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ExpandableListView.OnGroupClickListener {
        a() {
        }

        @Override // android.widget.ExpandableListView.OnGroupClickListener
        public boolean onGroupClick(ExpandableListView expandableListView, View view, int i10, long j10) {
            if (COUIExpandableListView.this.f5875e == null || !COUIExpandableListView.this.f5875e.onGroupClick(expandableListView, view, i10, j10)) {
                COUIExpandableListView cOUIExpandableListView = COUIExpandableListView.this;
                if (ExpandableListView.getPackedPositionGroup(cOUIExpandableListView.getExpandableListPosition(cOUIExpandableListView.getLastVisiblePosition())) == i10) {
                    COUIExpandableListView cOUIExpandableListView2 = COUIExpandableListView.this;
                    if (cOUIExpandableListView2.getChildAt(cOUIExpandableListView2.getChildCount() - 1).getBottom() >= COUIExpandableListView.this.getHeight() - COUIExpandableListView.this.getListPaddingBottom() && !expandableListView.isGroupExpanded(i10)) {
                        return false;
                    }
                }
                COUIExpandableListView.this.playSoundEffect(0);
                if (expandableListView.isGroupExpanded(i10)) {
                    COUIExpandableListView.this.collapseGroup(i10);
                } else {
                    COUIExpandableListView.this.expandGroup(i10);
                }
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class b extends View {

        /* renamed from: e, reason: collision with root package name */
        private List<View> f5878e;

        /* renamed from: f, reason: collision with root package name */
        private Drawable f5879f;

        /* renamed from: g, reason: collision with root package name */
        private int f5880g;

        /* renamed from: h, reason: collision with root package name */
        private int f5881h;

        public b(Context context) {
            super(context);
            this.f5878e = new ArrayList();
            COUIDarkModeUtil.b(this, false);
        }

        public void a(View view) {
            this.f5878e.add(view);
        }

        public void b() {
            this.f5878e.clear();
        }

        public void c(Drawable drawable, int i10, int i11) {
            if (drawable != null) {
                this.f5879f = drawable;
                this.f5880g = i10;
                this.f5881h = i11;
                drawable.setBounds(0, 0, i10, i11);
            }
        }

        @Override // android.view.View
        public void dispatchDraw(Canvas canvas) {
            canvas.save();
            Drawable drawable = this.f5879f;
            if (drawable != null) {
                drawable.setBounds(0, 0, this.f5880g, this.f5881h);
            }
            int size = this.f5878e.size();
            int i10 = 0;
            for (int i11 = 0; i11 < size; i11++) {
                View view = this.f5878e.get(i11);
                canvas.save();
                int measuredHeight = view.getMeasuredHeight();
                i10 += measuredHeight;
                canvas.clipRect(0, 0, getWidth(), measuredHeight);
                view.draw(canvas);
                canvas.restore();
                Drawable drawable2 = this.f5879f;
                if (drawable2 != null) {
                    i10 += this.f5881h;
                    drawable2.draw(canvas);
                    canvas.translate(0.0f, this.f5881h);
                }
                canvas.translate(0.0f, measuredHeight);
                if (i10 > canvas.getHeight()) {
                    break;
                }
            }
            canvas.restore();
        }

        @Override // android.view.View
        protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
            super.onLayout(z10, i10, i11, i12, i13);
            int i14 = i13 - i11;
            int size = this.f5878e.size();
            int i15 = 0;
            for (int i16 = 0; i16 < size; i16++) {
                View view = this.f5878e.get(i16);
                int measuredHeight = view.getMeasuredHeight();
                view.layout(i10, i11, view.getMeasuredWidth() + i10, measuredHeight + i11);
                i15 = i15 + measuredHeight + this.f5881h;
                if (i15 > i14) {
                    return;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private static abstract class c implements Animator.AnimatorListener {
        private c() {
        }

        /* synthetic */ c(a aVar) {
            this();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class d extends ValueAnimator {

        /* renamed from: e, reason: collision with root package name */
        private WeakReference<COUIExpandableListView> f5882e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f5883f;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class a implements ValueAnimator.AnimatorUpdateListener {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ boolean f5884a;

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ int f5885b;

            /* renamed from: c, reason: collision with root package name */
            final /* synthetic */ boolean f5886c;

            /* renamed from: d, reason: collision with root package name */
            final /* synthetic */ View f5887d;

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ e f5888e;

            a(boolean z10, int i10, boolean z11, View view, e eVar) {
                this.f5884a = z10;
                this.f5885b = i10;
                this.f5886c = z11;
                this.f5887d = view;
                this.f5888e = eVar;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int i10;
                COUIExpandableListView cOUIExpandableListView = (COUIExpandableListView) d.this.f5882e.get();
                if (cOUIExpandableListView == null) {
                    Log.e("COUIExpandableListView", "onAnimationUpdate: expandable list is null");
                    d.this.e();
                    return;
                }
                int packedPositionGroup = ExpandableListView.getPackedPositionGroup(cOUIExpandableListView.getExpandableListPosition(cOUIExpandableListView.getFirstVisiblePosition()));
                long expandableListPosition = cOUIExpandableListView.getExpandableListPosition(cOUIExpandableListView.getLastVisiblePosition());
                int packedPositionGroup2 = ExpandableListView.getPackedPositionGroup(expandableListPosition);
                int packedPositionChild = ExpandableListView.getPackedPositionChild(expandableListPosition);
                if (!d.this.f5883f && !this.f5884a && (packedPositionGroup > (i10 = this.f5885b) || packedPositionGroup2 < i10)) {
                    Log.d("COUIExpandableListView", "onAnimationUpdate: all is screen out, first:" + packedPositionGroup + ",groupPos:" + this.f5885b + ",last:" + packedPositionGroup2);
                    d.this.e();
                    return;
                }
                if (!d.this.f5883f && !this.f5884a && this.f5886c && packedPositionGroup2 == this.f5885b && packedPositionChild == 0) {
                    Log.d("COUIExpandableListView", "onAnimationUpdate: expand is screen over, last:" + packedPositionGroup2);
                    d.this.e();
                    return;
                }
                if (!d.this.f5883f && this.f5884a && this.f5886c && this.f5887d.getBottom() > cOUIExpandableListView.getBottom()) {
                    Log.d("COUIExpandableListView", "onAnimationUpdate3: " + this.f5887d.getBottom() + "," + cOUIExpandableListView.getBottom());
                    d.this.e();
                    return;
                }
                d.this.f5883f = false;
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                this.f5888e.f5895f = intValue;
                this.f5887d.getLayoutParams().height = intValue;
                this.f5887d.requestLayout();
            }
        }

        public d(COUIExpandableListView cOUIExpandableListView, long j10, TimeInterpolator timeInterpolator) {
            this.f5882e = new WeakReference<>(cOUIExpandableListView);
            setDuration(j10);
            setInterpolator(timeInterpolator);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void e() {
            removeAllUpdateListeners();
            end();
        }

        public void f(boolean z10, boolean z11, int i10, View view, e eVar, int i11, int i12) {
            this.f5883f = true;
            setIntValues(i11, i12);
            removeAllUpdateListeners();
            addUpdateListener(new a(z11, i10, z10, view, eVar));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class f extends BaseExpandableListAdapter {

        /* renamed from: b, reason: collision with root package name */
        private COUIExpandableListView f5897b;

        /* renamed from: f, reason: collision with root package name */
        private ExpandableListAdapter f5901f;

        /* renamed from: g, reason: collision with root package name */
        private final DataSetObserver f5902g;

        /* renamed from: a, reason: collision with root package name */
        private SparseArray<e> f5896a = new SparseArray<>();

        /* renamed from: c, reason: collision with root package name */
        private SparseArray<d> f5898c = new SparseArray<>();

        /* renamed from: d, reason: collision with root package name */
        private SparseArray<List<View>> f5899d = new SparseArray<>();

        /* renamed from: e, reason: collision with root package name */
        private SparseArray<List<View>> f5900e = new SparseArray<>();

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class a extends c {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ b f5903a;

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ int f5904b;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(b bVar, int i10) {
                super(null);
                this.f5903a = bVar;
                this.f5904b = i10;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                this.f5903a.b();
                f.this.q(this.f5904b);
                f.this.notifyDataSetChanged();
                this.f5903a.setTag(0);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class b extends c {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ b f5906a;

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ int f5907b;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            b(b bVar, int i10) {
                super(null);
                this.f5906a = bVar;
                this.f5907b = i10;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                this.f5906a.b();
                f.this.q(this.f5907b);
                f.this.f5897b.d(this.f5907b);
                this.f5906a.setTag(0);
            }
        }

        /* loaded from: classes.dex */
        protected class c extends DataSetObserver {
            protected c() {
            }

            @Override // android.database.DataSetObserver
            public void onChanged() {
                f.this.notifyDataSetChanged();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                f.this.notifyDataSetInvalidated();
            }
        }

        f(ExpandableListAdapter expandableListAdapter, COUIExpandableListView cOUIExpandableListView) {
            c cVar = new c();
            this.f5902g = cVar;
            this.f5897b = cOUIExpandableListView;
            ExpandableListAdapter expandableListAdapter2 = this.f5901f;
            if (expandableListAdapter2 != null) {
                expandableListAdapter2.unregisterDataSetObserver(cVar);
            }
            this.f5901f = expandableListAdapter;
            expandableListAdapter.registerDataSetObserver(cVar);
        }

        private void e(View view, int i10, int i11) {
            int m10 = m(i10, i11);
            List<View> list = this.f5900e.get(m10);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(view);
            this.f5900e.put(m10, list);
        }

        private void f(b bVar, int i10, boolean z10, int i11) {
            e l10 = l(i10);
            d dVar = this.f5898c.get(i10);
            if (dVar == null) {
                dVar = new d(this.f5897b, 400L, new COUIMoveEaseInterpolator());
                this.f5898c.put(i10, dVar);
            } else {
                dVar.removeAllListeners();
                dVar.cancel();
            }
            d dVar2 = dVar;
            int i12 = l10.f5895f;
            dVar2.f(false, z10, i10, bVar, l10, i12 == -1 ? i11 : i12, 0);
            dVar2.addListener(new b(bVar, i10));
            dVar2.start();
            bVar.setTag(2);
        }

        private void g(b bVar, int i10, boolean z10, int i11) {
            e l10 = l(i10);
            d dVar = this.f5898c.get(i10);
            if (dVar == null) {
                dVar = new d(this.f5897b, 400L, new COUIMoveEaseInterpolator());
                this.f5898c.put(i10, dVar);
            } else {
                dVar.removeAllListeners();
                dVar.cancel();
            }
            d dVar2 = dVar;
            int i12 = l10.f5895f;
            if (i12 == -1) {
                i12 = 0;
            }
            dVar2.f(true, z10, i10, bVar, l10, i12, i11);
            dVar2.addListener(new a(bVar, i10));
            dVar2.start();
            bVar.setTag(1);
        }

        private View i(int i10, boolean z10, View view) {
            e l10 = l(i10);
            if (!(view instanceof b)) {
                view = new b(this.f5897b.getContext());
                view.setLayoutParams(new AbsListView.LayoutParams(-1, 0));
            }
            b bVar = (b) view;
            bVar.b();
            bVar.c(this.f5897b.getDivider(), this.f5897b.getMeasuredWidth(), this.f5897b.getDividerHeight());
            int k10 = k(l10.f5891b, i10, bVar);
            l10.f5893d = bVar;
            l10.f5894e = k10;
            Object tag = bVar.getTag();
            int intValue = tag != null ? ((Integer) tag).intValue() : 0;
            boolean z11 = l10.f5891b;
            if (z11 && intValue != 1) {
                g(bVar, i10, z10, k10);
            } else if (!z11 && intValue != 2) {
                f(bVar, i10, z10, k10);
            } else {
                Log.e("COUIExpandableListView", "getAnimationView: state is no match:" + intValue);
            }
            return view;
        }

        private View j(int i10, int i11) {
            List<View> list = this.f5899d.get(m(i10, i11));
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list.remove(0);
        }

        private int k(boolean z10, int i10, b bVar) {
            int bottom;
            this.f5897b.getChildCount();
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.f5897b.getWidth(), 1073741824);
            int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
            if (z10 && this.f5897b.getLayoutParams().height == -2) {
                bottom = this.f5897b.getContext().getResources().getDisplayMetrics().heightPixels;
            } else {
                bottom = this.f5897b.getBottom();
            }
            int i11 = bottom;
            int childrenCount = this.f5901f.getChildrenCount(i10);
            int i12 = 0;
            int i13 = 0;
            while (i12 < childrenCount) {
                View childView = this.f5901f.getChildView(i10, i12, i12 == childrenCount + (-1), j(i10, i12), this.f5897b);
                e(childView, i10, i12);
                AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) childView.getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = (AbsListView.LayoutParams) h();
                    childView.setLayoutParams(layoutParams);
                }
                int i14 = layoutParams.height;
                int makeMeasureSpec3 = i14 > 0 ? View.MeasureSpec.makeMeasureSpec(i14, 1073741824) : makeMeasureSpec2;
                childView.setLayoutDirection(this.f5897b.getLayoutDirection());
                childView.measure(makeMeasureSpec, makeMeasureSpec3);
                i13 += childView.getMeasuredHeight();
                bVar.a(childView);
                if ((!z10 && i13 + 0 > i11) || (z10 && i13 > (i11 + 0) * 2)) {
                    break;
                }
                i12++;
            }
            return i13;
        }

        private e l(int i10) {
            e eVar = this.f5896a.get(i10);
            if (eVar != null) {
                return eVar;
            }
            e eVar2 = new e(null);
            this.f5896a.put(i10, eVar2);
            return eVar2;
        }

        private int m(int i10, int i11) {
            ExpandableListAdapter expandableListAdapter = this.f5901f;
            if (!(expandableListAdapter instanceof HeterogeneousExpandableList)) {
                return 1;
            }
            int childType = ((HeterogeneousExpandableList) expandableListAdapter).getChildType(i10, i11) + 1;
            if (childType >= 0) {
                return childType;
            }
            throw new RuntimeException("getChildType must is greater than 0");
        }

        private void n() {
            for (int i10 = 0; i10 < this.f5900e.size(); i10++) {
                List<View> valueAt = this.f5900e.valueAt(i10);
                int keyAt = this.f5900e.keyAt(i10);
                List<View> list = this.f5899d.get(keyAt);
                if (list == null) {
                    list = new ArrayList<>();
                    this.f5899d.put(keyAt, list);
                }
                list.addAll(valueAt);
            }
            this.f5900e.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean o(int i10) {
            b bVar;
            b bVar2;
            e l10 = l(i10);
            boolean z10 = l10.f5890a;
            if (z10 && l10.f5891b && (bVar2 = l10.f5893d) != null) {
                l10.f5891b = false;
                f(bVar2, i10, l10.f5892c, l10.f5895f);
                return false;
            }
            if (z10 && !l10.f5891b && (bVar = l10.f5893d) != null) {
                g(bVar, i10, l10.f5892c, l10.f5894e);
                l10.f5891b = true;
                return false;
            }
            l10.f5890a = true;
            l10.f5891b = false;
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean p(int i10) {
            e l10 = l(i10);
            if (l10.f5890a && l10.f5891b) {
                return false;
            }
            l10.f5890a = true;
            l10.f5891b = true;
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void q(int i10) {
            e l10 = l(i10);
            l10.f5895f = -1;
            l10.f5890a = false;
            n();
        }

        @Override // android.widget.ExpandableListAdapter
        public Object getChild(int i10, int i11) {
            return this.f5901f.getChild(i10, i10);
        }

        @Override // android.widget.ExpandableListAdapter
        public long getChildId(int i10, int i11) {
            return this.f5901f.getChildId(i10, i11);
        }

        @Override // android.widget.BaseExpandableListAdapter, android.widget.HeterogeneousExpandableList
        public final int getChildType(int i10, int i11) {
            if (l(i10).f5890a) {
                return Integer.MIN_VALUE;
            }
            return m(i10, i11);
        }

        @Override // android.widget.BaseExpandableListAdapter, android.widget.HeterogeneousExpandableList
        public final int getChildTypeCount() {
            ExpandableListAdapter expandableListAdapter = this.f5901f;
            if (expandableListAdapter instanceof HeterogeneousExpandableList) {
                return ((HeterogeneousExpandableList) expandableListAdapter).getChildTypeCount() + 1;
            }
            return 2;
        }

        @Override // android.widget.ExpandableListAdapter
        public final View getChildView(int i10, int i11, boolean z10, View view, ViewGroup viewGroup) {
            e l10 = l(i10);
            l10.f5892c = z10;
            if (l10.f5890a) {
                return i(i10, z10 && i10 == getGroupCount() - 1, view);
            }
            return this.f5901f.getChildView(i10, i11, z10, view, viewGroup);
        }

        @Override // android.widget.ExpandableListAdapter
        public final int getChildrenCount(int i10) {
            if (l(i10).f5890a) {
                return 1;
            }
            return this.f5901f.getChildrenCount(i10);
        }

        @Override // android.widget.ExpandableListAdapter
        public Object getGroup(int i10) {
            return this.f5901f.getGroup(i10);
        }

        @Override // android.widget.ExpandableListAdapter
        public int getGroupCount() {
            return this.f5901f.getGroupCount();
        }

        @Override // android.widget.ExpandableListAdapter
        public long getGroupId(int i10) {
            return this.f5901f.getGroupId(i10);
        }

        @Override // android.widget.ExpandableListAdapter
        public View getGroupView(int i10, boolean z10, View view, ViewGroup viewGroup) {
            return this.f5901f.getGroupView(i10, z10, view, viewGroup);
        }

        protected ViewGroup.LayoutParams h() {
            return new AbsListView.LayoutParams(-1, -2, 0);
        }

        @Override // android.widget.ExpandableListAdapter
        public boolean hasStableIds() {
            return this.f5901f.hasStableIds();
        }

        @Override // android.widget.ExpandableListAdapter
        public boolean isChildSelectable(int i10, int i11) {
            if (l(i10).f5890a) {
                return false;
            }
            return this.f5901f.isChildSelectable(i10, i11);
        }
    }

    public COUIExpandableListView(Context context) {
        this(context, null);
    }

    private void c() {
        setDivider(null);
        setChildDivider(null);
        setGroupIndicator(null);
        super.setOnGroupClickListener(new a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(int i10) {
        super.collapseGroup(i10);
    }

    @Override // android.widget.ExpandableListView
    public boolean collapseGroup(int i10) {
        boolean o10 = this.f5876f.o(i10);
        if (o10) {
            this.f5876f.notifyDataSetChanged();
        }
        return o10;
    }

    @Override // android.widget.ExpandableListView
    public boolean expandGroup(int i10) {
        if (!this.f5876f.p(i10)) {
            return false;
        }
        boolean expandGroup = super.expandGroup(i10);
        if (expandGroup) {
            return expandGroup;
        }
        this.f5876f.q(i10);
        return expandGroup;
    }

    @Override // android.widget.ExpandableListView
    public void setAdapter(ExpandableListAdapter expandableListAdapter) {
        f fVar = new f(expandableListAdapter, this);
        this.f5876f = fVar;
        super.setAdapter(fVar);
    }

    @Override // android.widget.ExpandableListView
    public void setChildDivider(Drawable drawable) {
        if (drawable == null) {
            super.setChildDivider(null);
            return;
        }
        throw new RuntimeException("cannot set childDivider.");
    }

    @Override // android.widget.ListView
    public void setDivider(Drawable drawable) {
        if (drawable == null) {
            super.setDivider(null);
            return;
        }
        throw new RuntimeException("cannot set divider");
    }

    @Override // android.widget.ExpandableListView
    public void setGroupIndicator(Drawable drawable) {
        if (drawable == null) {
            super.setGroupIndicator(null);
            return;
        }
        throw new RuntimeException("cannot set groupIndicator.");
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams.height != -2) {
            super.setLayoutParams(layoutParams);
            return;
        }
        throw new RuntimeException("cannot set wrap_content");
    }

    @Override // android.widget.ExpandableListView
    public void setOnGroupClickListener(ExpandableListView.OnGroupClickListener onGroupClickListener) {
        this.f5875e = onGroupClickListener;
    }

    public COUIExpandableListView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.listViewStyle);
    }

    public COUIExpandableListView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class e {

        /* renamed from: a, reason: collision with root package name */
        boolean f5890a;

        /* renamed from: b, reason: collision with root package name */
        boolean f5891b;

        /* renamed from: c, reason: collision with root package name */
        boolean f5892c;

        /* renamed from: d, reason: collision with root package name */
        b f5893d;

        /* renamed from: e, reason: collision with root package name */
        int f5894e;

        /* renamed from: f, reason: collision with root package name */
        int f5895f;

        private e() {
            this.f5890a = false;
            this.f5891b = false;
            this.f5892c = false;
            this.f5895f = -1;
        }

        /* synthetic */ e(a aVar) {
            this();
        }
    }
}
