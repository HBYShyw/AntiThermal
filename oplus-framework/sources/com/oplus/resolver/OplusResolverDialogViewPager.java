package com.oplus.resolver;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import com.android.internal.widget.RecyclerView;
import com.oplus.widget.OplusGridView;
import com.oplus.widget.OplusPagerAdapter;
import com.oplus.widget.OplusViewPager;
import java.util.List;

/* loaded from: classes.dex */
public class OplusResolverDialogViewPager extends OplusViewPager {
    public OplusResolverDialogViewPager(Context context) {
        this(context, null);
    }

    public OplusResolverDialogViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(0);
        addOnPageChangeListener(new OplusViewPager.SimpleOnPageChangeListener() { // from class: com.oplus.resolver.OplusResolverDialogViewPager.1
            @Override // com.oplus.widget.OplusViewPager.SimpleOnPageChangeListener, com.oplus.widget.OplusViewPager.OnPageChangeListener
            public void onPageSelected(int position) {
                Object current = OplusResolverDialogViewPager.this.getCurrent();
                if (current instanceof RecyclerView) {
                    RecyclerView.Adapter adapter = ((RecyclerView) current).getAdapter();
                    if (adapter instanceof OplusResolverGridAdapter) {
                        ((OplusResolverGridAdapter) adapter).setCurrentPagerNumber(position);
                    }
                }
            }
        });
    }

    @Override // com.oplus.widget.OplusViewPager
    public int getCurrentItem() {
        return super.getCurrentItem();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.widget.OplusViewPager, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(536870911, 0));
            int h = child.getMeasuredHeight();
            if (h > height) {
                height = h;
            }
        }
        int i2 = getPaddingTop();
        int height2 = i2 + height + getPaddingBottom();
        int height3 = View.MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(height3, height2);
    }

    @Deprecated
    public void setGridViewList(List<OplusGridView> listOplusGridView, List<ResolveInfo> riList, Intent intent, CheckBox checkbox, Dialog alertDialog) {
    }

    public void setResolverItemEventListener(OplusPagerAdapter.OplusResolverItemEventListener listener) {
        OplusPagerAdapter adapter = getAdapter();
        if (adapter != null) {
            adapter.setOplusResolverItemEventListener(listener);
        }
    }
}
