package com.coui.appcompat.tips.marquee;

import android.content.Context;
import android.util.AttributeSet;
import c3.IDefaultTopTips;
import com.coui.appcompat.tips.def.COUIDefaultTopTips;
import com.coui.appcompat.tips.def.COUIDefaultTopTipsView;

/* loaded from: classes.dex */
public class COUIMarqueeTopTips extends COUIDefaultTopTips {

    /* renamed from: t, reason: collision with root package name */
    private COUIDefaultTopTipsView f7955t;

    public COUIMarqueeTopTips(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.tips.def.COUIDefaultTopTips
    public IDefaultTopTips d() {
        COUIDefaultTopTipsView cOUIDefaultTopTipsView = (COUIDefaultTopTipsView) super.d();
        this.f7955t = cOUIDefaultTopTipsView;
        return cOUIDefaultTopTipsView;
    }

    public COUIMarqueeTopTips(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIMarqueeTopTips(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
