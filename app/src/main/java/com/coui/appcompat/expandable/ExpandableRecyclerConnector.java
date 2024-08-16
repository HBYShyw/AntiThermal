package com.coui.appcompat.expandable;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import androidx.recyclerview.widget.COUILinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import m1.COUIMoveEaseInterpolator;
import w1.COUIDarkModeUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ExpandableRecyclerConnector extends RecyclerView.h {

    /* renamed from: e, reason: collision with root package name */
    private COUIExpandableRecyclerAdapter f5921e;

    /* renamed from: g, reason: collision with root package name */
    private int f5923g;

    /* renamed from: j, reason: collision with root package name */
    private COUIExpandableRecyclerView f5926j;

    /* renamed from: a, reason: collision with root package name */
    private SparseArray<i> f5917a = new SparseArray<>();

    /* renamed from: b, reason: collision with root package name */
    private SparseArray<h> f5918b = new SparseArray<>();

    /* renamed from: c, reason: collision with root package name */
    private SparseArray<List<RecyclerView.c0>> f5919c = new SparseArray<>();

    /* renamed from: d, reason: collision with root package name */
    private SparseArray<List<RecyclerView.c0>> f5920d = new SparseArray<>();

    /* renamed from: h, reason: collision with root package name */
    private int f5924h = Integer.MAX_VALUE;

    /* renamed from: i, reason: collision with root package name */
    private final RecyclerView.j f5925i = new j();

    /* renamed from: k, reason: collision with root package name */
    private SparseArray<Integer> f5927k = new SparseArray<>();

    /* renamed from: f, reason: collision with root package name */
    private ArrayList<GroupMetadata> f5922f = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class GroupMetadata implements Parcelable, Comparable<GroupMetadata> {
        public static final Parcelable.Creator<GroupMetadata> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f5928e;

        /* renamed from: f, reason: collision with root package name */
        int f5929f;

        /* renamed from: g, reason: collision with root package name */
        int f5930g;

        /* renamed from: h, reason: collision with root package name */
        long f5931h;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<GroupMetadata> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public GroupMetadata createFromParcel(Parcel parcel) {
                return GroupMetadata.l(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readLong());
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public GroupMetadata[] newArray(int i10) {
                return new GroupMetadata[i10];
            }
        }

        private GroupMetadata() {
        }

        static GroupMetadata l(int i10, int i11, int i12, long j10) {
            GroupMetadata groupMetadata = new GroupMetadata();
            groupMetadata.f5928e = i10;
            groupMetadata.f5929f = i11;
            groupMetadata.f5930g = i12;
            groupMetadata.f5931h = j10;
            return groupMetadata;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // java.lang.Comparable
        /* renamed from: j, reason: merged with bridge method [inline-methods] */
        public int compareTo(GroupMetadata groupMetadata) {
            if (groupMetadata != null) {
                return this.f5930g - groupMetadata.f5930g;
            }
            throw new IllegalArgumentException();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            parcel.writeInt(this.f5928e);
            parcel.writeInt(this.f5929f);
            parcel.writeInt(this.f5930g);
            parcel.writeLong(this.f5931h);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f5932e;

        a(int i10) {
            this.f5932e = i10;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ExpandableRecyclerConnector.this.f5926j.K(view, this.f5932e);
        }
    }

    /* loaded from: classes.dex */
    class b implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f5934e;

        b(int i10) {
            this.f5934e = i10;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ExpandableRecyclerConnector.this.f5926j.K(view, this.f5934e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends g {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ f f5936a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f5937b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f5938c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(f fVar, int i10, int i11) {
            super(null);
            this.f5936a = fVar;
            this.f5937b = i10;
            this.f5938c = i11;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            f fVar = this.f5936a;
            if (fVar != null) {
                fVar.b();
                ExpandableRecyclerConnector.this.D(this.f5937b);
                ExpandableRecyclerConnector.this.x(true, true);
                ExpandableRecyclerConnector expandableRecyclerConnector = ExpandableRecyclerConnector.this;
                expandableRecyclerConnector.notifyItemRangeChanged(this.f5938c - 1, (expandableRecyclerConnector.getItemCount() - this.f5938c) + 1);
                this.f5936a.setTag(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d extends g {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ f f5940a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f5941b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(f fVar, int i10) {
            super(null);
            this.f5940a = fVar;
            this.f5941b = i10;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            f fVar = this.f5940a;
            if (fVar != null) {
                fVar.b();
                ExpandableRecyclerConnector.this.D(this.f5941b);
                ExpandableRecyclerConnector.this.h(this.f5941b);
                this.f5940a.setTag(0);
            }
        }
    }

    /* loaded from: classes.dex */
    static class e extends RecyclerView.c0 {
        public e(View view) {
            super(view);
            view.setLayoutParams(new AbsListView.LayoutParams(-1, 0));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class f extends View {

        /* renamed from: e, reason: collision with root package name */
        private List<View> f5943e;

        public f(Context context) {
            super(context);
            this.f5943e = new ArrayList();
            COUIDarkModeUtil.b(this, false);
        }

        public void a(View view) {
            this.f5943e.add(view);
        }

        public void b() {
            this.f5943e.clear();
        }

        @Override // android.view.View
        public void dispatchDraw(Canvas canvas) {
            canvas.save();
            int size = this.f5943e.size();
            int i10 = 0;
            for (int i11 = 0; i11 < size; i11++) {
                View view = this.f5943e.get(i11);
                canvas.save();
                int measuredHeight = view.getMeasuredHeight();
                i10 += measuredHeight;
                canvas.clipRect(0, 0, getWidth(), measuredHeight);
                view.draw(canvas);
                canvas.restore();
                canvas.translate(0.0f, measuredHeight);
                if (i10 > canvas.getHeight()) {
                    break;
                }
            }
            canvas.restore();
        }

        @Override // android.view.View
        protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
            int i14 = i13 - i11;
            int size = this.f5943e.size();
            int i15 = 0;
            for (int i16 = 0; i16 < size; i16++) {
                View view = this.f5943e.get(i16);
                int measuredHeight = view.getMeasuredHeight();
                i15 += measuredHeight;
                view.layout(i10, i11, view.getMeasuredWidth() + i10, measuredHeight + i11);
                if (i15 > i14) {
                    return;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private static abstract class g implements Animator.AnimatorListener {
        private g() {
        }

        /* synthetic */ g(a aVar) {
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
    public static class h extends ValueAnimator {

        /* renamed from: e, reason: collision with root package name */
        private WeakReference<COUIExpandableRecyclerView> f5944e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f5945f;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class a implements ValueAnimator.AnimatorUpdateListener {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ boolean f5946a;

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ int f5947b;

            /* renamed from: c, reason: collision with root package name */
            final /* synthetic */ boolean f5948c;

            /* renamed from: d, reason: collision with root package name */
            final /* synthetic */ View f5949d;

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ i f5950e;

            a(boolean z10, int i10, boolean z11, View view, i iVar) {
                this.f5946a = z10;
                this.f5947b = i10;
                this.f5948c = z11;
                this.f5949d = view;
                this.f5950e = iVar;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int i10;
                COUIExpandableRecyclerView cOUIExpandableRecyclerView = (COUIExpandableRecyclerView) h.this.f5944e.get();
                if (cOUIExpandableRecyclerView == null) {
                    h.this.e();
                    return;
                }
                int b22 = ((COUILinearLayoutManager) cOUIExpandableRecyclerView.getLayoutManager()).b2();
                int e22 = ((COUILinearLayoutManager) cOUIExpandableRecyclerView.getLayoutManager()).e2();
                if (!h.this.f5945f && !this.f5946a && (b22 > (i10 = this.f5947b) || e22 < i10)) {
                    Log.d("ExpandRecyclerConnector", "onAnimationUpdate1: " + b22 + "," + e22 + "," + this.f5947b);
                    h.this.e();
                    return;
                }
                if (!h.this.f5945f && !this.f5946a && this.f5948c && this.f5947b == e22) {
                    Log.d("ExpandRecyclerConnector", "onAnimationUpdate2: " + e22 + "," + this.f5947b);
                    h.this.e();
                    return;
                }
                if (this.f5949d == null) {
                    Log.d("ExpandRecyclerConnector", "onAnimationUpdate4: view == null");
                    h.this.e();
                    return;
                }
                if (!h.this.f5945f && this.f5946a && this.f5948c && this.f5949d.getBottom() > cOUIExpandableRecyclerView.getBottom()) {
                    Log.d("ExpandRecyclerConnector", "onAnimationUpdate3: " + this.f5949d.getBottom() + "," + cOUIExpandableRecyclerView.getBottom());
                    h.this.e();
                    return;
                }
                h.this.f5945f = false;
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                this.f5950e.f5956e = intValue;
                this.f5949d.getLayoutParams().height = intValue;
                cOUIExpandableRecyclerView.requestLayout();
            }
        }

        public h(COUIExpandableRecyclerView cOUIExpandableRecyclerView, long j10, TimeInterpolator timeInterpolator) {
            this.f5944e = new WeakReference<>(cOUIExpandableRecyclerView);
            setDuration(j10);
            setInterpolator(timeInterpolator);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void e() {
            removeAllUpdateListeners();
            end();
        }

        public void f(boolean z10, boolean z11, int i10, View view, i iVar, int i11, int i12) {
            Log.d("ExpandRecyclerConnector", "setParam: " + z10 + ", isLastChild:" + z11 + " ,flatPos:" + i10 + " ,start:" + i11 + " ,end:" + i12);
            this.f5945f = true;
            setIntValues(i11, i12);
            removeAllUpdateListeners();
            addUpdateListener(new a(z11, i10, z10, view, iVar));
        }
    }

    /* loaded from: classes.dex */
    public static class k {

        /* renamed from: d, reason: collision with root package name */
        private static ArrayList<k> f5958d = new ArrayList<>(5);

        /* renamed from: a, reason: collision with root package name */
        public ExpandableRecyclerPosition f5959a;

        /* renamed from: b, reason: collision with root package name */
        public GroupMetadata f5960b;

        /* renamed from: c, reason: collision with root package name */
        public int f5961c;

        private k() {
        }

        private static k a() {
            synchronized (f5958d) {
                if (f5958d.size() > 0) {
                    k remove = f5958d.remove(0);
                    remove.e();
                    return remove;
                }
                return new k();
            }
        }

        static k c(int i10, int i11, int i12, int i13, GroupMetadata groupMetadata, int i14) {
            k a10 = a();
            a10.f5959a = ExpandableRecyclerPosition.b(i11, i12, i13, i10);
            a10.f5960b = groupMetadata;
            a10.f5961c = i14;
            return a10;
        }

        private void e() {
            ExpandableRecyclerPosition expandableRecyclerPosition = this.f5959a;
            if (expandableRecyclerPosition != null) {
                expandableRecyclerPosition.c();
                this.f5959a = null;
            }
            this.f5960b = null;
            this.f5961c = 0;
        }

        public boolean b() {
            return this.f5960b != null;
        }

        public void d() {
            e();
            synchronized (f5958d) {
                if (f5958d.size() < 5) {
                    f5958d.add(this);
                }
            }
        }
    }

    public ExpandableRecyclerConnector(COUIExpandableRecyclerAdapter cOUIExpandableRecyclerAdapter, COUIExpandableRecyclerView cOUIExpandableRecyclerView) {
        this.f5926j = cOUIExpandableRecyclerView;
        z(cOUIExpandableRecyclerAdapter);
    }

    private boolean C(int i10) {
        i u7 = u(i10);
        if (u7.f5952a && u7.f5953b) {
            return false;
        }
        u7.f5952a = true;
        u7.f5953b = true;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void D(int i10) {
        i u7 = u(i10);
        u7.f5952a = false;
        u7.f5956e = -1;
        y();
    }

    private void f(RecyclerView.c0 c0Var, int i10, int i11) {
        int v7 = v(i10, i11);
        List<RecyclerView.c0> list = this.f5920d.get(v7);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(c0Var);
        this.f5920d.put(v7, list);
    }

    private void g(f fVar, int i10, int i11, int i12) {
        Log.d("ExpandRecyclerConnector", "collapseAnimationStart:" + i10 + " ,groupPos:" + i11 + " , height:" + i12);
        i u7 = u(i11);
        h hVar = this.f5918b.get(i11);
        if (hVar == null) {
            hVar = new h(this.f5926j, 400L, new COUIMoveEaseInterpolator());
            this.f5918b.put(i11, hVar);
        } else {
            hVar.removeAllListeners();
            hVar.cancel();
        }
        boolean z10 = i10 == getItemCount() - 1;
        int i13 = u7.f5956e;
        hVar.f(false, z10, i10, fVar, u7, i13 == -1 ? i12 : i13, 0);
        hVar.addListener(new d(fVar, i11));
        hVar.start();
        if (fVar != null) {
            fVar.setTag(2);
        }
    }

    private void k(f fVar, int i10, int i11, int i12) {
        Log.d("ExpandRecyclerConnector", "expandAnimationStart:" + i10 + " ,groupPos:" + i11 + " , height:" + i12);
        i u7 = u(i11);
        h hVar = this.f5918b.get(i11);
        if (hVar == null) {
            hVar = new h(this.f5926j, 400L, new COUIMoveEaseInterpolator());
            this.f5918b.put(i11, hVar);
        } else {
            hVar.removeAllListeners();
            hVar.cancel();
        }
        boolean z10 = i10 == getItemCount() - 1;
        int i13 = u7.f5956e;
        hVar.f(true, z10, i10, fVar, u7, i13 == -1 ? 0 : i13, i12);
        hVar.addListener(new c(fVar, i11, i10));
        hVar.start();
        if (fVar != null) {
            fVar.setTag(1);
        }
    }

    private RecyclerView.c0 p(int i10, int i11) {
        List<RecyclerView.c0> list = this.f5919c.get(v(i10, i11));
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.remove(0);
    }

    private int q(boolean z10, int i10, f fVar) {
        int bottom;
        int J = this.f5926j.getLayoutManager().J();
        int bottom2 = J > 0 ? this.f5926j.getLayoutManager().I(J - 1).getBottom() : 0;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.f5926j.getWidth(), 1073741824);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        if (z10 && this.f5926j.getLayoutParams().height == -2) {
            bottom = this.f5926j.getContext().getResources().getDisplayMetrics().heightPixels;
        } else {
            bottom = this.f5926j.getBottom();
        }
        int childrenCount = this.f5921e.getChildrenCount(i10);
        int i11 = 0;
        for (int i12 = 0; i12 < childrenCount; i12++) {
            RecyclerView.c0 p10 = p(i10, i12);
            if (p10 == null) {
                p10 = this.f5921e.a(this.f5926j, v(i10, i12));
            }
            f(p10, i10, i12);
            View view = p10.itemView;
            this.f5921e.e(i10, i12, false, p10);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = o();
                view.setLayoutParams(layoutParams);
            }
            int i13 = layoutParams.height;
            int makeMeasureSpec3 = i13 > 0 ? View.MeasureSpec.makeMeasureSpec(i13, 1073741824) : makeMeasureSpec2;
            view.setLayoutDirection(this.f5926j.getLayoutDirection());
            view.measure(makeMeasureSpec, makeMeasureSpec3);
            i11 += view.getMeasuredHeight();
            fVar.a(view);
            if ((!z10 && i11 + bottom2 > bottom) || (z10 && i11 > (bottom - bottom2) * 2)) {
                break;
            }
        }
        return i11;
    }

    private i u(int i10) {
        i iVar = this.f5917a.get(i10);
        if (iVar != null) {
            return iVar;
        }
        i iVar2 = new i(null);
        this.f5917a.put(i10, iVar2);
        return iVar2;
    }

    private int v(int i10, int i11) {
        return this.f5921e.getChildType(i10, i11) + this.f5921e.getGroupTypeCount();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void x(boolean z10, boolean z11) {
        int r10;
        ArrayList<GroupMetadata> arrayList = this.f5922f;
        int size = arrayList.size();
        int i10 = 0;
        this.f5923g = 0;
        if (z11) {
            boolean z12 = false;
            for (int i11 = size - 1; i11 >= 0; i11--) {
                GroupMetadata groupMetadata = arrayList.get(i11);
                int n10 = n(groupMetadata.f5931h, groupMetadata.f5930g);
                if (n10 != groupMetadata.f5930g) {
                    if (n10 == -1) {
                        arrayList.remove(i11);
                        size--;
                    }
                    groupMetadata.f5930g = n10;
                    if (!z12) {
                        z12 = true;
                    }
                }
            }
            if (z12) {
                Collections.sort(arrayList);
            }
        }
        int i12 = 0;
        int i13 = 0;
        while (i10 < size) {
            GroupMetadata groupMetadata2 = arrayList.get(i10);
            int i14 = groupMetadata2.f5929f;
            if (i14 != -1 && !z10) {
                r10 = i14 - groupMetadata2.f5928e;
            } else {
                r10 = r(groupMetadata2.f5930g);
            }
            this.f5923g += r10;
            int i15 = groupMetadata2.f5930g;
            int i16 = i12 + (i15 - i13);
            groupMetadata2.f5928e = i16;
            i12 = i16 + r10;
            groupMetadata2.f5929f = i12;
            i10++;
            i13 = i15;
        }
    }

    private void y() {
        for (int i10 = 0; i10 < this.f5920d.size(); i10++) {
            List<RecyclerView.c0> valueAt = this.f5920d.valueAt(i10);
            int keyAt = this.f5920d.keyAt(i10);
            List<RecyclerView.c0> list = this.f5919c.get(keyAt);
            if (list == null) {
                list = new ArrayList<>();
                this.f5919c.put(keyAt, list);
            }
            list.addAll(valueAt);
        }
        this.f5920d.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A(ArrayList<GroupMetadata> arrayList) {
        COUIExpandableRecyclerAdapter cOUIExpandableRecyclerAdapter;
        if (arrayList == null || (cOUIExpandableRecyclerAdapter = this.f5921e) == null) {
            return;
        }
        int groupCount = cOUIExpandableRecyclerAdapter.getGroupCount();
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size).f5930g >= groupCount) {
                return;
            }
        }
        this.f5922f = arrayList;
        x(true, false);
    }

    public boolean B(int i10) {
        f fVar;
        ExpandableRecyclerPosition b10 = ExpandableRecyclerPosition.b(2, i10, -1, -1);
        k t7 = t(b10);
        b10.c();
        View C = t7 != null ? ((COUILinearLayoutManager) this.f5926j.getLayoutManager()).C(t7.f5959a.f5965c) : null;
        if (C != null && C.getBottom() >= this.f5926j.getHeight() - this.f5926j.getPaddingBottom()) {
            GroupMetadata groupMetadata = t7.f5960b;
            int i11 = groupMetadata.f5928e;
            this.f5922f.remove(groupMetadata);
            x(false, false);
            notifyItemChanged(i11);
            this.f5921e.onGroupCollapsed(t7.f5960b.f5930g);
            return false;
        }
        i u7 = u(i10);
        boolean z10 = u7.f5952a;
        if (z10 && u7.f5953b) {
            u7.f5953b = false;
            if (t7 != null && (fVar = u7.f5955d) != null) {
                g(fVar, t7.f5960b.f5928e, i10, u7.f5956e);
            }
            return false;
        }
        if (z10 && !u7.f5953b) {
            if (t7 != null) {
                k(u7.f5955d, t7.f5960b.f5928e, i10, u7.f5954c);
            }
            u7.f5953b = true;
            return false;
        }
        u7.f5952a = true;
        u7.f5953b = false;
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public int getItemCount() {
        return this.f5921e.getGroupCount() + this.f5923g;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public long getItemId(int i10) {
        long combinedChildId;
        k w10 = w(i10);
        long groupId = this.f5921e.getGroupId(w10.f5959a.f5963a);
        ExpandableRecyclerPosition expandableRecyclerPosition = w10.f5959a;
        int i11 = expandableRecyclerPosition.f5966d;
        if (i11 == 2) {
            combinedChildId = this.f5921e.getCombinedGroupId(groupId);
        } else if (i11 == 1) {
            combinedChildId = this.f5921e.getCombinedChildId(groupId, this.f5921e.getChildId(expandableRecyclerPosition.f5963a, expandableRecyclerPosition.f5964b));
        } else {
            throw new RuntimeException("Flat list position is of unknown type");
        }
        w10.d();
        return combinedChildId;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public int getItemViewType(int i10) {
        int v7;
        k w10 = w(i10);
        ExpandableRecyclerPosition expandableRecyclerPosition = w10.f5959a;
        if (expandableRecyclerPosition.f5966d == 2) {
            v7 = this.f5921e.getGroupType(expandableRecyclerPosition.f5963a);
        } else {
            v7 = u(expandableRecyclerPosition.f5963a).f5952a ? Integer.MIN_VALUE : v(expandableRecyclerPosition.f5963a, expandableRecyclerPosition.f5964b);
        }
        this.f5927k.put(v7, Integer.valueOf(expandableRecyclerPosition.f5966d));
        w10.d();
        return v7;
    }

    boolean h(int i10) {
        ExpandableRecyclerPosition b10 = ExpandableRecyclerPosition.b(2, i10, -1, -1);
        k t7 = t(b10);
        b10.c();
        if (t7 == null) {
            return false;
        }
        return i(t7);
    }

    boolean i(k kVar) {
        GroupMetadata groupMetadata = kVar.f5960b;
        if (groupMetadata == null) {
            return false;
        }
        this.f5922f.remove(groupMetadata);
        x(false, false);
        notifyItemRangeChanged(0, getItemCount());
        this.f5921e.onGroupCollapsed(kVar.f5960b.f5930g);
        return true;
    }

    public void j() {
        x(true, true);
        notifyItemRangeChanged(0, getItemCount());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean l(int i10) {
        ExpandableRecyclerPosition b10 = ExpandableRecyclerPosition.b(2, i10, -1, -1);
        k t7 = t(b10);
        b10.c();
        if (t7 == null) {
            return false;
        }
        return m(t7);
    }

    boolean m(k kVar) {
        if (kVar.f5959a.f5963a >= 0) {
            if (this.f5924h == 0 || kVar.f5960b != null) {
                return false;
            }
            if (this.f5922f.size() >= this.f5924h) {
                GroupMetadata groupMetadata = this.f5922f.get(0);
                int indexOf = this.f5922f.indexOf(groupMetadata);
                h(groupMetadata.f5930g);
                int i10 = kVar.f5961c;
                if (i10 > indexOf) {
                    kVar.f5961c = i10 - 1;
                }
            }
            int i11 = kVar.f5959a.f5963a;
            GroupMetadata l10 = GroupMetadata.l(-1, -1, i11, this.f5921e.getGroupId(i11));
            View C = ((COUILinearLayoutManager) this.f5926j.getLayoutManager()).C(kVar.f5959a.f5965c);
            if (C != null && C.getBottom() >= this.f5926j.getHeight() - this.f5926j.getPaddingBottom()) {
                this.f5922f.add(kVar.f5961c, l10);
                x(false, false);
                this.f5921e.onGroupExpanded(l10.f5930g);
                notifyItemChanged(l10.f5928e);
                return false;
            }
            if (!C(l10.f5930g)) {
                return false;
            }
            this.f5922f.add(kVar.f5961c, l10);
            x(false, false);
            notifyItemRangeChanged(0, getItemCount());
            this.f5921e.onGroupExpanded(l10.f5930g);
            return true;
        }
        throw new RuntimeException("Need group");
    }

    int n(long j10, int i10) {
        int groupCount;
        COUIExpandableRecyclerAdapter cOUIExpandableRecyclerAdapter = this.f5921e;
        if (cOUIExpandableRecyclerAdapter == null || (groupCount = cOUIExpandableRecyclerAdapter.getGroupCount()) == 0 || j10 == Long.MIN_VALUE) {
            return -1;
        }
        int i11 = groupCount - 1;
        int min = Math.min(i11, Math.max(0, i10));
        long uptimeMillis = SystemClock.uptimeMillis() + 100;
        int i12 = min;
        int i13 = i12;
        boolean z10 = false;
        while (SystemClock.uptimeMillis() <= uptimeMillis) {
            if (cOUIExpandableRecyclerAdapter.getGroupId(min) != j10) {
                boolean z11 = i12 == i11;
                boolean z12 = i13 == 0;
                if (z11 && z12) {
                    break;
                }
                if (z12 || (z10 && !z11)) {
                    i12++;
                    z10 = false;
                    min = i12;
                } else if (z11 || (!z10 && !z12)) {
                    i13--;
                    z10 = true;
                    min = i13;
                }
            } else {
                return min;
            }
        }
        return -1;
    }

    protected ViewGroup.LayoutParams o() {
        return new AbsListView.LayoutParams(-1, -2, 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public void onBindViewHolder(RecyclerView.c0 c0Var, int i10) {
        k w10 = w(i10);
        int i11 = w10.f5959a.f5963a;
        i u7 = u(i11);
        c0Var.itemView.setOnClickListener(null);
        ExpandableRecyclerPosition expandableRecyclerPosition = w10.f5959a;
        int i12 = expandableRecyclerPosition.f5966d;
        if (i12 == 2) {
            this.f5921e.c(i11, w10.b(), c0Var);
            c0Var.itemView.setOnClickListener(new a(i10));
        } else {
            if (u7.f5952a) {
                f fVar = (f) c0Var.itemView;
                fVar.b();
                int q10 = q(u7.f5953b, i11, fVar);
                u7.f5954c = q10;
                u7.f5955d = fVar;
                Object tag = fVar.getTag();
                int intValue = tag != null ? ((Integer) tag).intValue() : 0;
                boolean z10 = u7.f5953b;
                if (z10 && intValue != 1) {
                    k(fVar, i10, i11, q10);
                } else if (!z10 && intValue != 2) {
                    g(fVar, i10, i11, q10);
                } else {
                    Log.e("ExpandRecyclerConnector", "onBindViewHolder: state is no match:" + intValue);
                }
            } else if (i12 == 1) {
                this.f5921e.e(i11, expandableRecyclerPosition.f5964b, w10.f5960b.f5929f == i10, c0Var);
                if (this.f5921e.isChildSelectable(i11, w10.f5959a.f5964b)) {
                    c0Var.itemView.setOnClickListener(new b(i10));
                }
            } else {
                throw new RuntimeException("Flat list position is of unknown type");
            }
        }
        w10.d();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public RecyclerView.c0 onCreateViewHolder(ViewGroup viewGroup, int i10) {
        Integer num = this.f5927k.get(i10);
        int intValue = num != null ? num.intValue() : 0;
        if (i10 == Integer.MIN_VALUE) {
            return new e(new f(viewGroup.getContext()));
        }
        if (intValue == 2) {
            return this.f5921e.f(viewGroup, i10);
        }
        if (intValue == 1) {
            return this.f5921e.a(viewGroup, i10);
        }
        throw new RuntimeException("Flat list position is of unknown type");
    }

    int r(int i10) {
        if (u(i10).f5952a) {
            return 1;
        }
        return this.f5921e.getChildrenCount(i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<GroupMetadata> s() {
        return this.f5922f;
    }

    k t(ExpandableRecyclerPosition expandableRecyclerPosition) {
        ArrayList<GroupMetadata> arrayList = this.f5922f;
        int size = arrayList.size();
        int i10 = size - 1;
        if (size == 0) {
            int i11 = expandableRecyclerPosition.f5963a;
            return k.c(i11, expandableRecyclerPosition.f5966d, i11, expandableRecyclerPosition.f5964b, null, 0);
        }
        int i12 = 0;
        int i13 = 0;
        while (i13 <= i10) {
            int i14 = ((i10 - i13) / 2) + i13;
            GroupMetadata groupMetadata = arrayList.get(i14);
            int i15 = expandableRecyclerPosition.f5963a;
            int i16 = groupMetadata.f5930g;
            if (i15 > i16) {
                i13 = i14 + 1;
            } else if (i15 < i16) {
                i10 = i14 - 1;
            } else if (i15 == i16) {
                int i17 = expandableRecyclerPosition.f5966d;
                if (i17 == 2) {
                    return k.c(groupMetadata.f5928e, i17, i15, expandableRecyclerPosition.f5964b, groupMetadata, i14);
                }
                if (i17 != 1) {
                    return null;
                }
                int i18 = groupMetadata.f5928e;
                int i19 = expandableRecyclerPosition.f5964b;
                return k.c(i18 + i19 + 1, i17, i15, i19, groupMetadata, i14);
            }
            i12 = i14;
        }
        if (expandableRecyclerPosition.f5966d != 2) {
            return null;
        }
        if (i13 > i12) {
            GroupMetadata groupMetadata2 = arrayList.get(i13 - 1);
            int i20 = groupMetadata2.f5929f;
            int i21 = expandableRecyclerPosition.f5963a;
            return k.c(i20 + (i21 - groupMetadata2.f5930g), expandableRecyclerPosition.f5966d, i21, expandableRecyclerPosition.f5964b, null, i13);
        }
        if (i10 >= i12) {
            return null;
        }
        int i22 = i10 + 1;
        GroupMetadata groupMetadata3 = arrayList.get(i22);
        int i23 = groupMetadata3.f5928e;
        int i24 = groupMetadata3.f5930g;
        int i25 = expandableRecyclerPosition.f5963a;
        return k.c(i23 - (i24 - i25), expandableRecyclerPosition.f5966d, i25, expandableRecyclerPosition.f5964b, null, i22);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k w(int i10) {
        int i11;
        ArrayList<GroupMetadata> arrayList = this.f5922f;
        int size = arrayList.size();
        int i12 = size - 1;
        if (size == 0) {
            return k.c(i10, 2, i10, -1, null, 0);
        }
        int i13 = 0;
        int i14 = i12;
        int i15 = 0;
        while (i13 <= i14) {
            int i16 = ((i14 - i13) / 2) + i13;
            GroupMetadata groupMetadata = arrayList.get(i16);
            int i17 = groupMetadata.f5929f;
            if (i10 > i17) {
                i13 = i16 + 1;
            } else {
                int i18 = groupMetadata.f5928e;
                if (i10 < i18) {
                    i14 = i16 - 1;
                } else {
                    if (i10 == i18) {
                        return k.c(i10, 2, groupMetadata.f5930g, -1, groupMetadata, i16);
                    }
                    if (i10 <= i17) {
                        return k.c(i10, 1, groupMetadata.f5930g, i10 - (i18 + 1), groupMetadata, i16);
                    }
                }
            }
            i15 = i16;
        }
        if (i13 > i15) {
            GroupMetadata groupMetadata2 = arrayList.get(i13 - 1);
            i11 = (i10 - groupMetadata2.f5929f) + groupMetadata2.f5930g;
        } else if (i14 < i15) {
            i13 = i14 + 1;
            GroupMetadata groupMetadata3 = arrayList.get(i13);
            i11 = groupMetadata3.f5930g - (groupMetadata3.f5928e - i10);
        } else {
            throw new RuntimeException("Unknown state");
        }
        return k.c(i10, 2, i11, -1, null, i13);
    }

    public void z(COUIExpandableRecyclerAdapter cOUIExpandableRecyclerAdapter) {
        COUIExpandableRecyclerAdapter cOUIExpandableRecyclerAdapter2 = this.f5921e;
        if (cOUIExpandableRecyclerAdapter2 != null) {
            cOUIExpandableRecyclerAdapter2.d(this.f5925i);
        }
        this.f5921e = cOUIExpandableRecyclerAdapter;
        setHasStableIds(cOUIExpandableRecyclerAdapter.hasStableIds());
        cOUIExpandableRecyclerAdapter.b(this.f5925i);
    }

    /* loaded from: classes.dex */
    protected class j extends RecyclerView.j {
        protected j() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onChanged() {
            ExpandableRecyclerConnector.this.x(true, true);
            ExpandableRecyclerConnector.this.notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeChanged(int i10, int i11) {
            ExpandableRecyclerConnector.this.x(true, true);
            ExpandableRecyclerConnector.this.notifyItemRangeChanged(i10, i11);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeInserted(int i10, int i11) {
            ExpandableRecyclerConnector.this.x(true, true);
            ExpandableRecyclerConnector.this.notifyItemRangeInserted(i10, i11);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeMoved(int i10, int i11, int i12) {
            ExpandableRecyclerConnector.this.x(true, true);
            ExpandableRecyclerConnector.this.notifyItemMoved(i10, i11);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeRemoved(int i10, int i11) {
            ExpandableRecyclerConnector.this.x(true, true);
            ExpandableRecyclerConnector.this.notifyItemRangeRemoved(i10, i11);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeChanged(int i10, int i11, Object obj) {
            onItemRangeChanged(i10, i11);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class i {

        /* renamed from: a, reason: collision with root package name */
        boolean f5952a;

        /* renamed from: b, reason: collision with root package name */
        boolean f5953b;

        /* renamed from: c, reason: collision with root package name */
        int f5954c;

        /* renamed from: d, reason: collision with root package name */
        f f5955d;

        /* renamed from: e, reason: collision with root package name */
        int f5956e;

        private i() {
            this.f5952a = false;
            this.f5953b = false;
            this.f5954c = -1;
            this.f5956e = -1;
        }

        /* synthetic */ i(a aVar) {
            this();
        }
    }
}
