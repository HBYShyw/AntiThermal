package com.google.android.material.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.core.util.Pair;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R$id;
import com.google.android.material.internal.ViewUtils;
import java.util.Calendar;
import java.util.Iterator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class MaterialCalendarGridView extends GridView {

    /* renamed from: e, reason: collision with root package name */
    private final Calendar f8657e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f8658f;

    /* loaded from: classes.dex */
    class a extends AccessibilityDelegateCompat {
        a() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.X(null);
        }
    }

    public MaterialCalendarGridView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    private void a(int i10, Rect rect) {
        if (i10 == 33) {
            setSelection(getAdapter().i());
        } else if (i10 == 130) {
            setSelection(getAdapter().b());
        } else {
            super.onFocusChanged(true, i10, rect);
        }
    }

    private View c(int i10) {
        return getChildAt(i10 - getFirstVisiblePosition());
    }

    private static int d(View view) {
        return view.getLeft() + (view.getWidth() / 2);
    }

    private static boolean e(Long l10, Long l11, Long l12, Long l13) {
        return l10 == null || l11 == null || l12 == null || l13 == null || l12.longValue() > l11.longValue() || l13.longValue() < l10.longValue();
    }

    @Override // android.widget.GridView, android.widget.AdapterView
    /* renamed from: b, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public MonthAdapter getAdapter2() {
        return (MonthAdapter) super.getAdapter();
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getAdapter().notifyDataSetChanged();
    }

    @Override // android.view.View
    protected final void onDraw(Canvas canvas) {
        int a10;
        int d10;
        int a11;
        int d11;
        int width;
        int i10;
        int left;
        int left2;
        MaterialCalendarGridView materialCalendarGridView = this;
        super.onDraw(canvas);
        MonthAdapter adapter = getAdapter();
        DateSelector<?> dateSelector = adapter.f8765f;
        CalendarStyle calendarStyle = adapter.f8767h;
        int max = Math.max(adapter.b(), getFirstVisiblePosition());
        int min = Math.min(adapter.i(), getLastVisiblePosition());
        Long item = adapter.getItem(max);
        Long item2 = adapter.getItem(min);
        Iterator<Pair<Long, Long>> it = dateSelector.b().iterator();
        while (it.hasNext()) {
            Pair<Long, Long> next = it.next();
            Long l10 = next.f2305a;
            if (l10 == null) {
                materialCalendarGridView = this;
            } else if (next.f2306b != null) {
                long longValue = l10.longValue();
                long longValue2 = next.f2306b.longValue();
                if (!e(item, item2, Long.valueOf(longValue), Long.valueOf(longValue2))) {
                    boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
                    if (longValue < item.longValue()) {
                        if (adapter.f(max)) {
                            left2 = 0;
                        } else if (!isLayoutRtl) {
                            left2 = materialCalendarGridView.c(max - 1).getRight();
                        } else {
                            left2 = materialCalendarGridView.c(max - 1).getLeft();
                        }
                        d10 = left2;
                        a10 = max;
                    } else {
                        materialCalendarGridView.f8657e.setTimeInMillis(longValue);
                        a10 = adapter.a(materialCalendarGridView.f8657e.get(5));
                        d10 = d(materialCalendarGridView.c(a10));
                    }
                    if (longValue2 > item2.longValue()) {
                        if (adapter.g(min)) {
                            left = getWidth();
                        } else if (!isLayoutRtl) {
                            left = materialCalendarGridView.c(min).getRight();
                        } else {
                            left = materialCalendarGridView.c(min).getLeft();
                        }
                        d11 = left;
                        a11 = min;
                    } else {
                        materialCalendarGridView.f8657e.setTimeInMillis(longValue2);
                        a11 = adapter.a(materialCalendarGridView.f8657e.get(5));
                        d11 = d(materialCalendarGridView.c(a11));
                    }
                    int itemId = (int) adapter.getItemId(a10);
                    int i11 = max;
                    int i12 = min;
                    int itemId2 = (int) adapter.getItemId(a11);
                    while (itemId <= itemId2) {
                        int numColumns = getNumColumns() * itemId;
                        int numColumns2 = (numColumns + getNumColumns()) - 1;
                        View c10 = materialCalendarGridView.c(numColumns);
                        int top = c10.getTop() + calendarStyle.f8690a.c();
                        MonthAdapter monthAdapter = adapter;
                        int bottom = c10.getBottom() - calendarStyle.f8690a.b();
                        if (!isLayoutRtl) {
                            i10 = numColumns > a10 ? 0 : d10;
                            width = a11 > numColumns2 ? getWidth() : d11;
                        } else {
                            int i13 = a11 > numColumns2 ? 0 : d11;
                            width = numColumns > a10 ? getWidth() : d10;
                            i10 = i13;
                        }
                        canvas.drawRect(i10, top, width, bottom, calendarStyle.f8697h);
                        itemId++;
                        materialCalendarGridView = this;
                        it = it;
                        adapter = monthAdapter;
                    }
                    materialCalendarGridView = this;
                    max = i11;
                    min = i12;
                }
            }
        }
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    protected void onFocusChanged(boolean z10, int i10, Rect rect) {
        if (z10) {
            a(i10, rect);
        } else {
            super.onFocusChanged(false, i10, rect);
        }
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i10, KeyEvent keyEvent) {
        if (!super.onKeyDown(i10, keyEvent)) {
            return false;
        }
        if (getSelectedItemPosition() == -1 || getSelectedItemPosition() >= getAdapter().b()) {
            return true;
        }
        if (19 != i10) {
            return false;
        }
        setSelection(getAdapter().b());
        return true;
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    public void onMeasure(int i10, int i11) {
        if (this.f8658f) {
            super.onMeasure(i10, View.MeasureSpec.makeMeasureSpec(16777215, Integer.MIN_VALUE));
            getLayoutParams().height = getMeasuredHeight();
            return;
        }
        super.onMeasure(i10, i11);
    }

    @Override // android.widget.GridView, android.widget.AdapterView
    public void setSelection(int i10) {
        if (i10 < getAdapter().b()) {
            super.setSelection(getAdapter().b());
        } else {
            super.setSelection(i10);
        }
    }

    public MaterialCalendarGridView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f8657e = UtcDates.l();
        if (MaterialDatePicker.H0(getContext())) {
            setNextFocusLeftId(R$id.cancel_button);
            setNextFocusRightId(R$id.confirm_button);
        }
        this.f8658f = MaterialDatePicker.I0(getContext());
        ViewCompat.l0(this, new a());
    }

    @Override // android.widget.AdapterView
    public final void setAdapter(ListAdapter listAdapter) {
        if (listAdapter instanceof MonthAdapter) {
            super.setAdapter(listAdapter);
            return;
        }
        throw new IllegalArgumentException(String.format("%1$s must have its Adapter set to a %2$s", MaterialCalendarGridView.class.getCanonicalName(), MonthAdapter.class.getCanonicalName()));
    }
}
