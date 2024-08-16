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
import androidx.preference.ListPreference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.support.list.R$dimen;
import com.support.list.R$styleable;

/* loaded from: classes.dex */
public class COUIListPreference extends ListPreference implements COUICardSupportInterface, COUIRecyclerView.b {

    /* renamed from: p, reason: collision with root package name */
    CharSequence f6942p;

    /* renamed from: q, reason: collision with root package name */
    Drawable f6943q;

    /* renamed from: r, reason: collision with root package name */
    CharSequence[] f6944r;

    /* renamed from: s, reason: collision with root package name */
    private int f6945s;

    /* renamed from: t, reason: collision with root package name */
    private CharSequence f6946t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f6947u;

    /* renamed from: v, reason: collision with root package name */
    private Point f6948v;

    /* renamed from: w, reason: collision with root package name */
    private View f6949w;

    /* renamed from: x, reason: collision with root package name */
    private View f6950x;

    /* renamed from: y, reason: collision with root package name */
    private TextView f6951y;

    /* loaded from: classes.dex */
    class a implements View.OnTouchListener {
        a() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 0) {
                return false;
            }
            COUIListPreference.this.f6948v.set((int) motionEvent.getX(), (int) motionEvent.getY());
            return false;
        }
    }

    public COUIListPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public boolean drawDivider() {
        if (!(this.f6950x instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int b10 = COUICardListHelper.b(this);
        return b10 == 1 || b10 == 2;
    }

    public CharSequence getAssignment() {
        return this.f6946t;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerEndAlignView() {
        return super.getDividerEndAlignView();
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerEndInset() {
        return this.f6945s;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerStartAlignView() {
        return this.f6951y;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerStartInset() {
        return this.f6945s;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.f6947u;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.f6950x = preferenceViewHolder.itemView;
        COUIPreferenceUtils.a(preferenceViewHolder, this.f6943q, this.f6942p, getAssignment());
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
        this.f6951y = (TextView) preferenceViewHolder.a(R.id.title);
        View view = preferenceViewHolder.itemView;
        this.f6949w = view;
        view.setOnTouchListener(new a());
    }

    public Point r() {
        return this.f6948v;
    }

    public View s() {
        return this.f6949w;
    }

    public CharSequence[] t() {
        return this.f6944r;
    }

    public COUIListPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet);
        this.f6948v = new Point();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPreference, i10, 0);
        this.f6947u = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_isSupportCardUse, true);
        this.f6946t = obtainStyledAttributes.getText(R$styleable.COUIPreference_couiAssignment);
        this.f6943q = obtainStyledAttributes.getDrawable(R$styleable.COUIPreference_coui_jump_mark);
        this.f6942p = obtainStyledAttributes.getText(R$styleable.COUIPreference_coui_jump_status1);
        obtainStyledAttributes.recycle();
        this.f6945s = getContext().getResources().getDimensionPixelSize(R$dimen.coui_preference_divider_default_horizontal_padding);
    }

    public COUIListPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }
}
