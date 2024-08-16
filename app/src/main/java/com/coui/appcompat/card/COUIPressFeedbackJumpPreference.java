package com.coui.appcompat.card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.card.COUIPressFeedbackJumpPreference;
import com.coui.appcompat.preference.COUIJumpPreference;
import com.support.list.R$attr;
import h2.COUIPressFeedbackHelper;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: COUIPressFeedbackJumpPreference.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b\u0016\u0018\u00002\u00020\u0001B1\b\u0007\u0012\u0006\u0010\u000f\u001a\u00020\u000e\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0010\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0012¢\u0006\u0004\b\u0015\u0010\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0017R$\u0010\r\u001a\u0004\u0018\u00010\u00068\u0004@\u0004X\u0084\u000e¢\u0006\u0012\n\u0004\b\u0007\u0010\b\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f¨\u0006\u0017"}, d2 = {"Lcom/coui/appcompat/card/COUIPressFeedbackJumpPreference;", "Lcom/coui/appcompat/preference/COUIJumpPreference;", "Landroidx/preference/l;", "holder", "Lma/f0;", "onBindViewHolder", "Landroid/view/View;", "F", "Landroid/view/View;", "i", "()Landroid/view/View;", "setRootView", "(Landroid/view/View;)V", "rootView", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "", "defStyleAttr", "defStyleRes", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public class COUIPressFeedbackJumpPreference extends COUIJumpPreference {

    /* renamed from: F, reason: from kotlin metadata */
    private View rootView;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIPressFeedbackJumpPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIPressFeedbackJumpPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0, 8, null);
        k.e(context, "context");
    }

    public /* synthetic */ COUIPressFeedbackJumpPreference(Context context, AttributeSet attributeSet, int i10, int i11, int i12, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i12 & 2) != 0 ? null : attributeSet, (i12 & 4) != 0 ? R$attr.couiJumpPreferenceStyle : i10, (i12 & 8) != 0 ? 0 : i11);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean j(COUIPressFeedbackJumpPreference cOUIPressFeedbackJumpPreference, COUIPressFeedbackHelper cOUIPressFeedbackHelper, View view, MotionEvent motionEvent) {
        k.e(cOUIPressFeedbackJumpPreference, "this$0");
        k.e(cOUIPressFeedbackHelper, "$pressFeedbackHelper");
        k.e(view, "$noName_0");
        k.e(motionEvent, "event");
        View rootView = cOUIPressFeedbackJumpPreference.getRootView();
        if (k.a(rootView == null ? null : Boolean.valueOf(rootView.isEnabled()), Boolean.TRUE)) {
            int action = motionEvent.getAction();
            if (action == 0) {
                cOUIPressFeedbackHelper.m(true);
            } else if (action == 1 || action == 3) {
                cOUIPressFeedbackHelper.m(false);
            }
        }
        return false;
    }

    /* renamed from: i, reason: from getter */
    protected final View getRootView() {
        return this.rootView;
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    @SuppressLint({"ClickableViewAccessibility"})
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        k.e(preferenceViewHolder, "holder");
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.rootView = view;
        final COUIPressFeedbackHelper cOUIPressFeedbackHelper = new COUIPressFeedbackHelper(view, 0);
        View view2 = this.rootView;
        if (k.a(view2 == null ? null : Boolean.valueOf(view2.isEnabled()), Boolean.TRUE)) {
            View view3 = this.rootView;
            if (view3 == null) {
                return;
            }
            view3.setOnTouchListener(new View.OnTouchListener() { // from class: t1.f
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view4, MotionEvent motionEvent) {
                    boolean j10;
                    j10 = COUIPressFeedbackJumpPreference.j(COUIPressFeedbackJumpPreference.this, cOUIPressFeedbackHelper, view4, motionEvent);
                    return j10;
                }
            });
            return;
        }
        View view4 = this.rootView;
        if (view4 == null) {
            return;
        }
        view4.setOnTouchListener(null);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIPressFeedbackJumpPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        k.e(context, "context");
    }
}
