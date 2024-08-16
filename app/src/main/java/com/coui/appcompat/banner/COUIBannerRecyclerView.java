package com.coui.appcompat.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.support.nearx.R$drawable;
import i3.COUIDisplayUtil;

/* loaded from: classes.dex */
public class COUIBannerRecyclerView extends COUIRecyclerView {
    public COUIBannerRecyclerView(Context context) {
        this(context, null);
    }

    private void H() {
        setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        setHorizontalItemAlign(1);
        setIsUseNativeOverScroll(true);
        setHorizontalFlingFriction(ViewConfiguration.getScrollFriction() * 2.5f);
        setPadding(COUIDisplayUtil.a(24), 0, COUIDisplayUtil.a(24), 0);
        setClipToPadding(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), 0);
        dividerItemDecoration.l(getContext().getResources().getDrawable(R$drawable.coui_item_decoration_8dp));
        addItemDecoration(dividerItemDecoration);
    }

    public COUIBannerRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIBannerRecyclerView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        H();
    }
}
