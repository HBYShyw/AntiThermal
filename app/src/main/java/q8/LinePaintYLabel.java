package q8;

import android.content.Context;
import android.graphics.DashPathEffect;
import com.oplus.battery.R;

/* compiled from: LinePaintYLabel.java */
/* renamed from: q8.c, reason: use source file name */
/* loaded from: classes2.dex */
public class LinePaintYLabel extends LinePaint {

    /* renamed from: a, reason: collision with root package name */
    private Context f16935a;

    public LinePaintYLabel(Context context) {
        super(context);
        this.f16935a = context;
        a();
        b();
        c();
    }

    public void a() {
        setColor(this.f16935a.getResources().getColor(R.color.curve_y_label_line_color));
    }

    public void b() {
        float dimensionPixelSize = this.f16935a.getResources().getDimensionPixelSize(R.dimen.usage_graph_dot_size);
        float dimensionPixelSize2 = this.f16935a.getResources().getDimensionPixelSize(R.dimen.usage_graph_dot_interval);
        setPathEffect(new DashPathEffect(new float[]{dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize, dimensionPixelSize2}, 0.0f));
    }

    public void c() {
        setStrokeWidth(this.f16935a.getResources().getDimensionPixelSize(R.dimen.usage_graph_divider_size));
    }
}
