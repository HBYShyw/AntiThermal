package com.coui.appcompat.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;
import com.support.appcompat.R$styleable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import m2.COUIShapePath;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUICodeInputView extends RelativeLayout {

    /* renamed from: e, reason: collision with root package name */
    private final int f5684e;

    /* renamed from: f, reason: collision with root package name */
    private final int f5685f;

    /* renamed from: g, reason: collision with root package name */
    private int f5686g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f5687h;

    /* renamed from: i, reason: collision with root package name */
    private List<String> f5688i;

    /* renamed from: j, reason: collision with root package name */
    private EditText f5689j;

    /* renamed from: k, reason: collision with root package name */
    private LinearLayout f5690k;

    /* renamed from: l, reason: collision with root package name */
    private List<CodeItemView> f5691l;

    /* renamed from: m, reason: collision with root package name */
    private d f5692m;

    /* renamed from: n, reason: collision with root package name */
    private e f5693n;

    /* renamed from: o, reason: collision with root package name */
    private int f5694o;

    /* renamed from: p, reason: collision with root package name */
    private int f5695p;

    /* renamed from: q, reason: collision with root package name */
    private int f5696q;

    /* loaded from: classes.dex */
    public static class CodeItemView extends View {

        /* renamed from: e, reason: collision with root package name */
        private int f5697e;

        /* renamed from: f, reason: collision with root package name */
        private int f5698f;

        /* renamed from: g, reason: collision with root package name */
        private int f5699g;

        /* renamed from: h, reason: collision with root package name */
        private int f5700h;

        /* renamed from: i, reason: collision with root package name */
        private int f5701i;

        /* renamed from: j, reason: collision with root package name */
        private TextPaint f5702j;

        /* renamed from: k, reason: collision with root package name */
        private Paint f5703k;

        /* renamed from: l, reason: collision with root package name */
        private Paint f5704l;

        /* renamed from: m, reason: collision with root package name */
        private Paint f5705m;

        /* renamed from: n, reason: collision with root package name */
        private Path f5706n;

        /* renamed from: o, reason: collision with root package name */
        private String f5707o;

        /* renamed from: p, reason: collision with root package name */
        private boolean f5708p;

        /* renamed from: q, reason: collision with root package name */
        private boolean f5709q;

        public CodeItemView(Context context) {
            super(context);
            this.f5697e = getResources().getDimensionPixelSize(R$dimen.coui_code_input_cell_text_size);
            this.f5698f = getResources().getDimensionPixelSize(R$dimen.coui_code_input_cell_radius);
            this.f5699g = getResources().getDimensionPixelSize(R$dimen.coui_code_input_cell_stroke_width);
            this.f5700h = getResources().getDimensionPixelSize(R$dimen.coui_code_input_cell_security_circle_radius);
            this.f5701i = COUIContextUtil.d(getContext(), R$color.coui_code_input_security_circle_color);
            this.f5702j = new TextPaint();
            this.f5703k = new Paint();
            this.f5704l = new Paint();
            this.f5705m = new Paint();
            this.f5706n = new Path();
            this.f5707o = "";
            this.f5702j.setTextSize(this.f5697e);
            this.f5702j.setAntiAlias(true);
            this.f5702j.setColor(COUIContextUtil.a(getContext(), R$attr.couiColorPrimaryNeutral));
            this.f5703k.setColor(COUIContextUtil.a(getContext(), R$attr.couiColorCardBackground));
            this.f5704l.setColor(COUIContextUtil.a(getContext(), R$attr.couiColorPrimary));
            this.f5704l.setStyle(Paint.Style.STROKE);
            this.f5704l.setStrokeWidth(this.f5699g);
            this.f5705m.setColor(this.f5701i);
            this.f5705m.setAntiAlias(true);
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            Path a10 = COUIShapePath.a(this.f5706n, new RectF(0.0f, 0.0f, getWidth(), getHeight()), this.f5698f);
            this.f5706n = a10;
            canvas.drawPath(a10, this.f5703k);
            if (this.f5708p) {
                float f10 = this.f5699g >> 1;
                Path a11 = COUIShapePath.a(this.f5706n, new RectF(f10, f10, r0 - r2, r1 - r2), this.f5698f);
                this.f5706n = a11;
                canvas.drawPath(a11, this.f5704l);
            }
            if (TextUtils.isEmpty(this.f5707o)) {
                return;
            }
            if (this.f5709q) {
                canvas.drawCircle(r0 / 2, r1 / 2, this.f5700h, this.f5705m);
                return;
            }
            float measureText = (r0 / 2) - (this.f5702j.measureText(this.f5707o) / 2.0f);
            Paint.FontMetricsInt fontMetricsInt = this.f5702j.getFontMetricsInt();
            canvas.drawText(this.f5707o, measureText, (r1 / 2) - ((fontMetricsInt.descent + fontMetricsInt.ascent) / 2), this.f5702j);
        }

        public void setEnableSecurity(boolean z10) {
            this.f5709q = z10;
        }

        public void setIsSelected(boolean z10) {
            this.f5708p = z10;
        }

        public void setNumber(String str) {
            this.f5707o = str;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements TextWatcher {
        a() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (editable == null || editable.length() <= 0) {
                return;
            }
            COUICodeInputView.this.f5689j.setText("");
            if (COUICodeInputView.this.f5688i.size() < COUICodeInputView.this.f5686g) {
                String trim = editable.toString().trim();
                if (trim.length() > 1) {
                    if (trim.length() > COUICodeInputView.this.f5686g) {
                        trim = trim.substring(0, COUICodeInputView.this.f5686g);
                    }
                    List asList = Arrays.asList(trim.split(""));
                    COUICodeInputView.this.f5688i = new ArrayList(asList);
                } else {
                    COUICodeInputView.this.f5688i.add(trim);
                }
            }
            COUICodeInputView.this.m();
            COUICodeInputView.this.i();
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements View.OnKeyListener {
        b() {
        }

        @Override // android.view.View.OnKeyListener
        public boolean onKey(View view, int i10, KeyEvent keyEvent) {
            COUICodeInputView cOUICodeInputView = COUICodeInputView.this;
            if (!cOUICodeInputView.k(cOUICodeInputView.f5688i) || i10 != 67 || keyEvent.getAction() != 0 || COUICodeInputView.this.f5688i.size() <= 0) {
                return false;
            }
            COUICodeInputView.this.f5688i.remove(COUICodeInputView.this.f5688i.size() - 1);
            COUICodeInputView.this.m();
            COUICodeInputView.this.i();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements View.OnFocusChangeListener {
        c() {
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z10) {
            CodeItemView codeItemView = (CodeItemView) COUICodeInputView.this.f5691l.get(Math.min(COUICodeInputView.this.f5688i.size(), COUICodeInputView.this.f5686g - 1));
            codeItemView.setIsSelected(z10);
            codeItemView.invalidate();
        }
    }

    /* loaded from: classes.dex */
    public interface d {
        void a(String str);

        void b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class e implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private View f5713e;

        private e() {
        }

        /* synthetic */ e(a aVar) {
            this();
        }

        public void a(View view) {
            this.f5713e = view;
        }

        @Override // java.lang.Runnable
        public void run() {
            View view = this.f5713e;
            if (view != null) {
                view.requestLayout();
                this.f5713e = null;
            }
        }
    }

    public COUICodeInputView(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        if (this.f5692m == null) {
            return;
        }
        if (this.f5688i.size() == this.f5686g) {
            this.f5692m.a(getPhoneCode());
        } else {
            this.f5692m.b();
        }
    }

    private void j(View view) {
        this.f5695p = getResources().getDimensionPixelSize(R$dimen.coui_code_input_cell_width);
        this.f5694o = getResources().getDimensionPixelSize(R$dimen.coui_code_input_cell_margin_horizontal);
        this.f5696q = getResources().getDimensionPixelSize(R$dimen.coui_code_input_cell_height);
        this.f5690k = (LinearLayout) view.findViewById(R$id.code_container_layout);
        for (int i10 = 0; i10 < this.f5686g; i10++) {
            CodeItemView codeItemView = new CodeItemView(getContext());
            codeItemView.setEnableSecurity(this.f5687h);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.f5695p, -1);
            layoutParams.setMarginStart(this.f5694o);
            layoutParams.setMarginEnd(this.f5694o);
            this.f5690k.addView(codeItemView, layoutParams);
            this.f5691l.add(codeItemView);
        }
        this.f5691l.get(0).setIsSelected(true);
        EditText editText = (EditText) view.findViewById(R$id.code_container_edittext);
        this.f5689j = editText;
        editText.requestFocus();
        this.f5689j.addTextChangedListener(new a());
        this.f5689j.setOnKeyListener(new b());
        this.f5689j.setOnFocusChangeListener(new c());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean k(List<String> list) {
        return !list.isEmpty();
    }

    private void l() {
        double min = Math.min(getResources().getConfiguration().screenWidthDp, 360.0d) / 360.0d;
        for (int i10 = 0; i10 < this.f5690k.getChildCount(); i10++) {
            View childAt = this.f5690k.getChildAt(i10);
            if (childAt == null) {
                return;
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            layoutParams.width = (int) (this.f5695p * min);
            layoutParams.setMarginStart((int) (this.f5694o * min));
            layoutParams.setMarginEnd((int) (this.f5694o * min));
            layoutParams.height = (int) (this.f5696q * min);
        }
        this.f5693n.a(this.f5690k);
        post(this.f5693n);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() {
        int size = this.f5688i.size();
        int i10 = 0;
        while (i10 < this.f5686g) {
            String str = size > i10 ? this.f5688i.get(i10) : "";
            CodeItemView codeItemView = this.f5691l.get(i10);
            codeItemView.setNumber(str);
            int i11 = this.f5686g;
            if (size == i11 && i10 == i11 - 1) {
                codeItemView.setIsSelected(true);
            } else {
                codeItemView.setIsSelected(size == i10);
            }
            codeItemView.invalidate();
            i10++;
        }
    }

    public String getPhoneCode() {
        StringBuilder sb2 = new StringBuilder();
        Iterator<String> it = this.f5688i.iterator();
        while (it.hasNext()) {
            sb2.append(it.next());
        }
        return sb2.toString();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        e eVar = this.f5693n;
        if (eVar != null) {
            removeCallbacks(eVar);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        if (i10 != i12) {
            l();
        }
    }

    public void setOnInputListener(d dVar) {
        this.f5692m = dVar;
    }

    public COUICodeInputView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUICodeInputView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f5684e = 6;
        this.f5685f = 360;
        this.f5687h = false;
        this.f5688i = new ArrayList();
        this.f5691l = new ArrayList();
        this.f5693n = new e(null);
        COUIDarkModeUtil.b(this, false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUICodeInputView, i10, 0);
        this.f5686g = obtainStyledAttributes.getInteger(R$styleable.COUICodeInputView_couiCodeInputCount, 6);
        this.f5687h = obtainStyledAttributes.getBoolean(R$styleable.COUICodeInputView_couiEnableSecurityInput, false);
        obtainStyledAttributes.recycle();
        j(LayoutInflater.from(context).inflate(R$layout.coui_phone_code_layout, this));
    }
}
