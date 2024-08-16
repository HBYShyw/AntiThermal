package q8;

import android.content.Context;
import android.graphics.Paint;
import com.oplus.battery.R;

/* compiled from: TextPaintXLabel.java */
/* renamed from: q8.f, reason: use source file name */
/* loaded from: classes2.dex */
public class TextPaintXLabel extends TextPaint {

    /* renamed from: a, reason: collision with root package name */
    private Context f16937a;

    public TextPaintXLabel(Context context) {
        super(context);
        this.f16937a = context;
        a();
        b();
    }

    public void a() {
        setColor(this.f16937a.getResources().getColor(R.color.curve_y_label_color));
    }

    public void b() {
        setTextAlign(Paint.Align.CENTER);
    }
}
