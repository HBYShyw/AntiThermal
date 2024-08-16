package l2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import u2.COUIStateListUtil;
import v1.COUIContextUtil;

/* compiled from: COUIPressRippleDrawable.java */
/* renamed from: l2.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPressRippleDrawable extends RippleDrawable {

    /* renamed from: e, reason: collision with root package name */
    private static final int f14579e = Color.parseColor("#00000000");

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public COUIPressRippleDrawable(Context context, int i10) {
        super(COUIStateListUtil.a(r0, r1), new ColorDrawable(r1), new COUIPressMaskDrawable(i10));
        int a10 = COUIContextUtil.a(context, R$attr.couiColorRipplePressBackground);
        int i11 = f14579e;
        a(context);
    }

    private void a(Context context) {
        int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.text_ripple_bg_padding_horizontal);
        int dimensionPixelOffset2 = context.getResources().getDimensionPixelOffset(R$dimen.text_ripple_bg_padding_vertical);
        setPadding(dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset, dimensionPixelOffset2);
    }
}
