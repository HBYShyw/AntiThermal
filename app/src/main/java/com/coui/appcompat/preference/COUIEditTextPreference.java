package com.coui.appcompat.preference;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.support.list.R$dimen;
import com.support.list.R$styleable;

/* loaded from: classes.dex */
public class COUIEditTextPreference extends EditTextPreference implements COUICardSupportInterface, COUIRecyclerView.b {

    /* renamed from: m, reason: collision with root package name */
    private int f6928m;

    /* renamed from: n, reason: collision with root package name */
    private Context f6929n;

    /* renamed from: o, reason: collision with root package name */
    private CharSequence f6930o;

    /* renamed from: p, reason: collision with root package name */
    private Drawable f6931p;

    /* renamed from: q, reason: collision with root package name */
    private CharSequence f6932q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f6933r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f6934s;

    /* renamed from: t, reason: collision with root package name */
    private Point f6935t;

    /* renamed from: u, reason: collision with root package name */
    private View f6936u;

    /* renamed from: v, reason: collision with root package name */
    private View f6937v;

    /* renamed from: w, reason: collision with root package name */
    private TextView f6938w;

    /* loaded from: classes.dex */
    class a implements View.OnTouchListener {
        a() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 0) {
                return false;
            }
            COUIEditTextPreference.this.f6935t.set((int) motionEvent.getX(), (int) motionEvent.getY());
            return false;
        }
    }

    public COUIEditTextPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f6935t = new Point();
        this.f6929n = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPreference, 0, 0);
        this.f6932q = obtainStyledAttributes.getText(R$styleable.COUIPreference_couiAssignment);
        this.f6931p = obtainStyledAttributes.getDrawable(R$styleable.COUIPreference_coui_jump_mark);
        this.f6930o = obtainStyledAttributes.getText(R$styleable.COUIPreference_coui_jump_status1);
        this.f6934s = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_isSupportCardUse, true);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.couiEditTextPreference, 0, 0);
        this.f6933r = obtainStyledAttributes2.getBoolean(R$styleable.couiEditTextPreference_couiSupportEmptyInput, false);
        obtainStyledAttributes2.recycle();
        this.f6928m = getContext().getResources().getDimensionPixelSize(R$dimen.coui_preference_divider_default_horizontal_padding);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public boolean drawDivider() {
        if (!(this.f6937v instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int b10 = COUICardListHelper.b(this);
        return b10 == 1 || b10 == 2;
    }

    public CharSequence getAssignment() {
        return this.f6932q;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerEndAlignView() {
        return super.getDividerEndAlignView();
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerEndInset() {
        return this.f6928m;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerStartAlignView() {
        return this.f6938w;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerStartInset() {
        return this.f6928m;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.f6934s;
    }

    public boolean n() {
        return this.f6933r;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.f6937v = preferenceViewHolder.itemView;
        COUIPreferenceUtils.a(preferenceViewHolder, this.f6931p, this.f6930o, getAssignment());
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
        this.f6938w = (TextView) preferenceViewHolder.a(R.id.title);
        View view = preferenceViewHolder.itemView;
        this.f6936u = view;
        view.setOnTouchListener(new a());
    }
}
