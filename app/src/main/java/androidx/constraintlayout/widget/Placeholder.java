package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import m.ConstraintWidget;

/* loaded from: classes.dex */
public class Placeholder extends View {

    /* renamed from: e, reason: collision with root package name */
    private int f1909e;

    /* renamed from: f, reason: collision with root package name */
    private View f1910f;

    /* renamed from: g, reason: collision with root package name */
    private int f1911g;

    public Placeholder(Context context) {
        super(context);
        this.f1909e = -1;
        this.f1910f = null;
        this.f1911g = 4;
        a(null);
    }

    private void a(AttributeSet attributeSet) {
        super.setVisibility(this.f1911g);
        this.f1909e = -1;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ConstraintLayout_placeholder);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.ConstraintLayout_placeholder_content) {
                    this.f1909e = obtainStyledAttributes.getResourceId(index, this.f1909e);
                } else if (index == R$styleable.ConstraintLayout_placeholder_placeholder_emptyVisibility) {
                    this.f1911g = obtainStyledAttributes.getInt(index, this.f1911g);
                }
            }
        }
    }

    public void b(ConstraintLayout constraintLayout) {
        if (this.f1910f == null) {
            return;
        }
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) this.f1910f.getLayoutParams();
        layoutParams2.f1873n0.E0(0);
        ConstraintWidget.b z10 = layoutParams.f1873n0.z();
        ConstraintWidget.b bVar = ConstraintWidget.b.FIXED;
        if (z10 != bVar) {
            layoutParams.f1873n0.F0(layoutParams2.f1873n0.Q());
        }
        if (layoutParams.f1873n0.N() != bVar) {
            layoutParams.f1873n0.i0(layoutParams2.f1873n0.w());
        }
        layoutParams2.f1873n0.E0(8);
    }

    public void c(ConstraintLayout constraintLayout) {
        if (this.f1909e == -1 && !isInEditMode()) {
            setVisibility(this.f1911g);
        }
        View findViewById = constraintLayout.findViewById(this.f1909e);
        this.f1910f = findViewById;
        if (findViewById != null) {
            ((ConstraintLayout.LayoutParams) findViewById.getLayoutParams()).f1849b0 = true;
            this.f1910f.setVisibility(0);
            setVisibility(0);
        }
    }

    public View getContent() {
        return this.f1910f;
    }

    public int getEmptyVisibility() {
        return this.f1911g;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            canvas.drawRGB(223, 223, 223);
            Paint paint = new Paint();
            paint.setARGB(255, EventType.SCENE_MODE_FILE_DOWNLOAD, EventType.SCENE_MODE_FILE_DOWNLOAD, EventType.SCENE_MODE_FILE_DOWNLOAD);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, 0));
            Rect rect = new Rect();
            canvas.getClipBounds(rect);
            paint.setTextSize(rect.height());
            int height = rect.height();
            int width = rect.width();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.getTextBounds("?", 0, 1, rect);
            canvas.drawText("?", ((width / 2.0f) - (rect.width() / 2.0f)) - rect.left, ((height / 2.0f) + (rect.height() / 2.0f)) - rect.bottom, paint);
        }
    }

    public void setContentId(int i10) {
        View findViewById;
        if (this.f1909e == i10) {
            return;
        }
        View view = this.f1910f;
        if (view != null) {
            view.setVisibility(0);
            ((ConstraintLayout.LayoutParams) this.f1910f.getLayoutParams()).f1849b0 = false;
            this.f1910f = null;
        }
        this.f1909e = i10;
        if (i10 == -1 || (findViewById = ((View) getParent()).findViewById(i10)) == null) {
            return;
        }
        findViewById.setVisibility(8);
    }

    public void setEmptyVisibility(int i10) {
        this.f1911g = i10;
    }

    public Placeholder(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1909e = -1;
        this.f1910f = null;
        this.f1911g = 4;
        a(attributeSet);
    }

    public Placeholder(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f1909e = -1;
        this.f1910f = null;
        this.f1911g = 4;
        a(attributeSet);
    }
}
