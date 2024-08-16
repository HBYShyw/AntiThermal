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
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.support.list.R$dimen;
import com.support.list.R$styleable;

/* loaded from: classes.dex */
public class COUIMultiSelectListPreference extends MultiSelectListPreference implements COUICardSupportInterface, COUIRecyclerView.b {

    /* renamed from: n, reason: collision with root package name */
    Context f6960n;

    /* renamed from: o, reason: collision with root package name */
    CharSequence f6961o;

    /* renamed from: p, reason: collision with root package name */
    Drawable f6962p;

    /* renamed from: q, reason: collision with root package name */
    private int f6963q;

    /* renamed from: r, reason: collision with root package name */
    private CharSequence f6964r;

    /* renamed from: s, reason: collision with root package name */
    private CharSequence[] f6965s;

    /* renamed from: t, reason: collision with root package name */
    private boolean f6966t;

    /* renamed from: u, reason: collision with root package name */
    private Point f6967u;

    /* renamed from: v, reason: collision with root package name */
    private View f6968v;

    /* renamed from: w, reason: collision with root package name */
    private View f6969w;

    /* renamed from: x, reason: collision with root package name */
    private TextView f6970x;

    /* loaded from: classes.dex */
    class a implements View.OnTouchListener {
        a() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 0) {
                return false;
            }
            COUIMultiSelectListPreference.this.f6967u.set((int) motionEvent.getX(), (int) motionEvent.getY());
            return false;
        }
    }

    public COUIMultiSelectListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f6967u = new Point();
        this.f6960n = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPreference, 0, 0);
        this.f6966t = obtainStyledAttributes.getBoolean(R$styleable.COUIPreference_isSupportCardUse, true);
        this.f6964r = obtainStyledAttributes.getText(R$styleable.COUIPreference_couiAssignment);
        this.f6962p = obtainStyledAttributes.getDrawable(R$styleable.COUIPreference_coui_jump_mark);
        this.f6961o = obtainStyledAttributes.getText(R$styleable.COUIPreference_coui_jump_status1);
        obtainStyledAttributes.recycle();
        this.f6963q = getContext().getResources().getDimensionPixelSize(R$dimen.coui_preference_divider_default_horizontal_padding);
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public boolean drawDivider() {
        if (!(this.f6969w instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int b10 = COUICardListHelper.b(this);
        return b10 == 1 || b10 == 2;
    }

    public CharSequence getAssignment() {
        return this.f6964r;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerEndAlignView() {
        return super.getDividerEndAlignView();
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerEndInset() {
        return this.f6963q;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerStartAlignView() {
        return this.f6970x;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerStartInset() {
        return this.f6963q;
    }

    @Override // com.coui.appcompat.preference.COUICardSupportInterface
    public boolean isSupportCardUse() {
        return this.f6966t;
    }

    public Point o() {
        return this.f6967u;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.f6969w = preferenceViewHolder.itemView;
        COUIPreferenceUtils.a(preferenceViewHolder, this.f6962p, this.f6961o, getAssignment());
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
        this.f6970x = (TextView) preferenceViewHolder.a(R.id.title);
        View view = preferenceViewHolder.itemView;
        this.f6968v = view;
        view.setOnTouchListener(new a());
    }

    public View p() {
        return this.f6968v;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence[] q() {
        return this.f6965s;
    }
}
