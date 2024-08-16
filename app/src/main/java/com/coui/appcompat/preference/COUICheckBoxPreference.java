package com.coui.appcompat.preference;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.coui.appcompat.checkbox.COUICheckBox;
import com.support.list.R$attr;
import com.support.list.R$dimen;
import com.support.list.R$style;
import kotlin.Metadata;
import za.k;

/* compiled from: COUICheckBoxPreference.kt */
@Metadata(bv = {}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u00012\u00020\u0002B\u001d\b\u0016\u0012\b\u0010 \u001a\u0004\u0018\u00010\u0012\u0012\b\u0010\"\u001a\u0004\u0018\u00010!¢\u0006\u0004\b#\u0010$B%\b\u0016\u0012\b\u0010 \u001a\u0004\u0018\u00010\u0012\u0012\b\u0010\"\u001a\u0004\u0018\u00010!\u0012\u0006\u0010%\u001a\u00020\u000b¢\u0006\u0004\b#\u0010&B-\b\u0016\u0012\b\u0010 \u001a\u0004\u0018\u00010\u0012\u0012\b\u0010\"\u001a\u0004\u0018\u00010!\u0012\u0006\u0010%\u001a\u00020\u000b\u0012\u0006\u0010'\u001a\u00020\u000b¢\u0006\u0004\b#\u0010(J\u0012\u0010\u0006\u001a\u00020\u00052\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016J\n\u0010\n\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010\f\u001a\u00020\u000bH\u0016J\b\u0010\r\u001a\u00020\u000bH\u0016R\u0018\u0010\u0011\u001a\u0004\u0018\u00010\u000e8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u000f\u0010\u0010R\u0018\u0010\u0015\u001a\u0004\u0018\u00010\u00128\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0013\u0010\u0014R\u0018\u0010\u0019\u001a\u0004\u0018\u00010\u00168\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0017\u0010\u0018R\u0018\u0010\u001c\u001a\u0004\u0018\u00010\t8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001a\u0010\u001bR\u0016\u0010\u001f\u001a\u00020\u000b8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001d\u0010\u001e¨\u0006)"}, d2 = {"Lcom/coui/appcompat/preference/COUICheckBoxPreference;", "Landroidx/preference/CheckBoxPreference;", "Landroidx/recyclerview/widget/COUIRecyclerView$b;", "Landroidx/preference/l;", "holder", "Lma/f0;", "onBindViewHolder", "", "drawDivider", "Landroid/view/View;", "getDividerStartAlignView", "", "getDividerStartInset", "getDividerEndInset", "Lcom/coui/appcompat/checkbox/COUICheckBox;", "e", "Lcom/coui/appcompat/checkbox/COUICheckBox;", "mCheckBox", "Landroid/content/Context;", "f", "Landroid/content/Context;", "mContext", "Landroid/widget/TextView;", "g", "Landroid/widget/TextView;", "mTitleView", "h", "Landroid/view/View;", "mItemView", "i", "I", "mDividerDefaultHorizontalPadding", "context", "Landroid/util/AttributeSet;", "attrs", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyleAttr", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "coui-support-lists_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class COUICheckBoxPreference extends CheckBoxPreference implements COUIRecyclerView.b {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private COUICheckBox mCheckBox;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private Context mContext;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private TextView mTitleView;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private View mItemView;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private int mDividerDefaultHorizontalPadding;

    public COUICheckBoxPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiCheckBoxPreferenceStyle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean d(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() != 0) {
            return false;
        }
        view.performHapticFeedback(302);
        return false;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public boolean drawDivider() {
        if (!(this.mItemView instanceof COUICardListSelectedItemLayout)) {
            return false;
        }
        int b10 = COUICardListHelper.b(this);
        return b10 == 1 || b10 == 2;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    /* renamed from: getDividerEndInset, reason: from getter */
    public int getMDividerDefaultHorizontalPadding() {
        return this.mDividerDefaultHorizontalPadding;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public View getDividerStartAlignView() {
        return this.mTitleView;
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView.b
    public int getDividerStartInset() {
        return this.mDividerDefaultHorizontalPadding;
    }

    @Override // androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        View view;
        super.onBindViewHolder(preferenceViewHolder);
        this.mItemView = preferenceViewHolder == null ? null : preferenceViewHolder.itemView;
        View a10 = preferenceViewHolder == null ? null : preferenceViewHolder.a(R.id.title);
        this.mTitleView = a10 instanceof TextView ? (TextView) a10 : null;
        View a11 = preferenceViewHolder == null ? null : preferenceViewHolder.a(R.id.checkbox);
        COUICheckBox cOUICheckBox = a11 instanceof COUICheckBox ? (COUICheckBox) a11 : null;
        this.mCheckBox = cOUICheckBox;
        if (cOUICheckBox != null) {
            cOUICheckBox.setState(this.mChecked ? 2 : 0);
        }
        if (preferenceViewHolder != null && (view = preferenceViewHolder.itemView) != null) {
            view.setOnTouchListener(new View.OnTouchListener() { // from class: com.coui.appcompat.preference.c
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view2, MotionEvent motionEvent) {
                    boolean d10;
                    d10 = COUICheckBoxPreference.d(view2, motionEvent);
                    return d10;
                }
            });
        }
        k.b(preferenceViewHolder);
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
    }

    public COUICheckBoxPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Preference_COUI_COUICheckBoxPreference);
    }

    public COUICheckBoxPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.mContext = context;
        this.mDividerDefaultHorizontalPadding = getContext().getResources().getDimensionPixelSize(R$dimen.coui_preference_divider_default_horizontal_padding);
    }
}
