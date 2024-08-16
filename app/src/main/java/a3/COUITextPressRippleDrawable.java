package a3;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import u2.COUIStateListUtil;
import v1.COUIContextUtil;

/* compiled from: COUITextPressRippleDrawable.java */
/* renamed from: a3.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUITextPressRippleDrawable extends RippleDrawable {

    /* renamed from: e, reason: collision with root package name */
    private static final int f32e = Color.parseColor("#00000000");

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public COUITextPressRippleDrawable(Context context) {
        super(COUIStateListUtil.a(r0, r1), new ColorDrawable(r1), new COUITextPressMaskDrawable());
        int a10 = COUIContextUtil.a(context, R$attr.couiColorRipplePressBackground);
        int i10 = f32e;
        a(context);
    }

    private void a(Context context) {
        int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.text_ripple_bg_padding_horizontal);
        int dimensionPixelOffset2 = context.getResources().getDimensionPixelOffset(R$dimen.text_ripple_bg_padding_vertical);
        setPadding(dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset, dimensionPixelOffset2);
    }
}
