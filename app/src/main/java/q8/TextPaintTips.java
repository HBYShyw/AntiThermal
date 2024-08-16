package q8;

import android.content.Context;
import android.graphics.Paint;
import com.oplus.battery.R;

/* compiled from: TextPaintTips.java */
/* renamed from: q8.e, reason: use source file name */
/* loaded from: classes2.dex */
public class TextPaintTips extends TextPaint {

    /* renamed from: a, reason: collision with root package name */
    private Context f16936a;

    public TextPaintTips(Context context) {
        super(context);
        this.f16936a = context;
        a();
        b();
    }

    public void a() {
        setColor(this.f16936a.getResources().getColor(R.color.curve_tips_color));
    }

    public void b() {
        setTextAlign(Paint.Align.LEFT);
    }
}
