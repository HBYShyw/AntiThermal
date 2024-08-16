package q8;

import android.content.Context;
import android.graphics.Paint;
import com.oplus.battery.R;

/* compiled from: TextPaintYLabel.java */
/* renamed from: q8.g, reason: use source file name */
/* loaded from: classes2.dex */
public class TextPaintYLabel extends TextPaint {

    /* renamed from: a, reason: collision with root package name */
    private Context f16938a;

    public TextPaintYLabel(Context context) {
        super(context);
        this.f16938a = context;
        a();
        b();
    }

    public void a() {
        setColor(this.f16938a.getResources().getColor(R.color.curve_y_label_color));
    }

    public void b() {
        setTextAlign(Paint.Align.LEFT);
    }
}
