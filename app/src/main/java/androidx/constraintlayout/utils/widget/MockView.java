package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.widget.R$styleable;

/* loaded from: classes.dex */
public class MockView extends View {

    /* renamed from: e, reason: collision with root package name */
    private Paint f1795e;

    /* renamed from: f, reason: collision with root package name */
    private Paint f1796f;

    /* renamed from: g, reason: collision with root package name */
    private Paint f1797g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f1798h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f1799i;

    /* renamed from: j, reason: collision with root package name */
    protected String f1800j;

    /* renamed from: k, reason: collision with root package name */
    private Rect f1801k;

    /* renamed from: l, reason: collision with root package name */
    private int f1802l;

    /* renamed from: m, reason: collision with root package name */
    private int f1803m;

    /* renamed from: n, reason: collision with root package name */
    private int f1804n;

    /* renamed from: o, reason: collision with root package name */
    private int f1805o;

    public MockView(Context context) {
        super(context);
        this.f1795e = new Paint();
        this.f1796f = new Paint();
        this.f1797g = new Paint();
        this.f1798h = true;
        this.f1799i = true;
        this.f1800j = null;
        this.f1801k = new Rect();
        this.f1802l = Color.argb(255, 0, 0, 0);
        this.f1803m = Color.argb(255, 200, 200, 200);
        this.f1804n = Color.argb(255, 50, 50, 50);
        this.f1805o = 4;
        a(context, null);
    }

    private void a(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.MockView);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.MockView_mock_label) {
                    this.f1800j = obtainStyledAttributes.getString(index);
                } else if (index == R$styleable.MockView_mock_showDiagonals) {
                    this.f1798h = obtainStyledAttributes.getBoolean(index, this.f1798h);
                } else if (index == R$styleable.MockView_mock_diagonalsColor) {
                    this.f1802l = obtainStyledAttributes.getColor(index, this.f1802l);
                } else if (index == R$styleable.MockView_mock_labelBackgroundColor) {
                    this.f1804n = obtainStyledAttributes.getColor(index, this.f1804n);
                } else if (index == R$styleable.MockView_mock_labelColor) {
                    this.f1803m = obtainStyledAttributes.getColor(index, this.f1803m);
                } else if (index == R$styleable.MockView_mock_showLabel) {
                    this.f1799i = obtainStyledAttributes.getBoolean(index, this.f1799i);
                }
            }
        }
        if (this.f1800j == null) {
            try {
                this.f1800j = context.getResources().getResourceEntryName(getId());
            } catch (Exception unused) {
            }
        }
        this.f1795e.setColor(this.f1802l);
        this.f1795e.setAntiAlias(true);
        this.f1796f.setColor(this.f1803m);
        this.f1796f.setAntiAlias(true);
        this.f1797g.setColor(this.f1804n);
        this.f1805o = Math.round(this.f1805o * (getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (this.f1798h) {
            width--;
            height--;
            float f10 = width;
            float f11 = height;
            canvas.drawLine(0.0f, 0.0f, f10, f11, this.f1795e);
            canvas.drawLine(0.0f, f11, f10, 0.0f, this.f1795e);
            canvas.drawLine(0.0f, 0.0f, f10, 0.0f, this.f1795e);
            canvas.drawLine(f10, 0.0f, f10, f11, this.f1795e);
            canvas.drawLine(f10, f11, 0.0f, f11, this.f1795e);
            canvas.drawLine(0.0f, f11, 0.0f, 0.0f, this.f1795e);
        }
        String str = this.f1800j;
        if (str == null || !this.f1799i) {
            return;
        }
        this.f1796f.getTextBounds(str, 0, str.length(), this.f1801k);
        float width2 = (width - this.f1801k.width()) / 2.0f;
        float height2 = ((height - this.f1801k.height()) / 2.0f) + this.f1801k.height();
        this.f1801k.offset((int) width2, (int) height2);
        Rect rect = this.f1801k;
        int i10 = rect.left;
        int i11 = this.f1805o;
        rect.set(i10 - i11, rect.top - i11, rect.right + i11, rect.bottom + i11);
        canvas.drawRect(this.f1801k, this.f1797g);
        canvas.drawText(this.f1800j, width2, height2, this.f1796f);
    }

    public MockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1795e = new Paint();
        this.f1796f = new Paint();
        this.f1797g = new Paint();
        this.f1798h = true;
        this.f1799i = true;
        this.f1800j = null;
        this.f1801k = new Rect();
        this.f1802l = Color.argb(255, 0, 0, 0);
        this.f1803m = Color.argb(255, 200, 200, 200);
        this.f1804n = Color.argb(255, 50, 50, 50);
        this.f1805o = 4;
        a(context, attributeSet);
    }

    public MockView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f1795e = new Paint();
        this.f1796f = new Paint();
        this.f1797g = new Paint();
        this.f1798h = true;
        this.f1799i = true;
        this.f1800j = null;
        this.f1801k = new Rect();
        this.f1802l = Color.argb(255, 0, 0, 0);
        this.f1803m = Color.argb(255, 200, 200, 200);
        this.f1804n = Color.argb(255, 50, 50, 50);
        this.f1805o = 4;
        a(context, attributeSet);
    }
}
