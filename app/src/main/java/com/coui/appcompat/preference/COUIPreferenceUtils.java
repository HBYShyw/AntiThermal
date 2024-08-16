package com.coui.appcompat.preference;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.imageview.COUIRoundImageView;
import com.support.list.R$dimen;
import com.support.list.R$id;

/* compiled from: COUIPreferenceUtils.java */
/* renamed from: com.coui.appcompat.preference.i, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPreferenceUtils {

    /* compiled from: COUIPreferenceUtils.java */
    /* renamed from: com.coui.appcompat.preference.i$a */
    /* loaded from: classes.dex */
    class a implements View.OnTouchListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ TextView f7076e;

        a(TextView textView) {
            this.f7076e = textView;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            int selectionStart = this.f7076e.getSelectionStart();
            int selectionEnd = this.f7076e.getSelectionEnd();
            int offsetForPosition = this.f7076e.getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
            boolean z10 = selectionStart == selectionEnd || offsetForPosition <= selectionStart || offsetForPosition >= selectionEnd;
            if (actionMasked != 0) {
                if (actionMasked == 1 || actionMasked == 3) {
                    this.f7076e.setPressed(false);
                    this.f7076e.postInvalidateDelayed(70L);
                }
            } else {
                if (z10) {
                    return false;
                }
                this.f7076e.setPressed(true);
                this.f7076e.invalidate();
            }
            return false;
        }
    }

    public static void a(PreferenceViewHolder preferenceViewHolder, Drawable drawable, CharSequence charSequence, CharSequence charSequence2) {
        b(preferenceViewHolder, drawable, charSequence, charSequence2, 0);
    }

    public static void b(PreferenceViewHolder preferenceViewHolder, Drawable drawable, CharSequence charSequence, CharSequence charSequence2, int i10) {
        ImageView imageView = (ImageView) preferenceViewHolder.a(R$id.coui_preference_widget_jump);
        if (imageView != null) {
            if (drawable != null) {
                imageView.setImageDrawable(drawable);
                imageView.setVisibility(0);
            } else {
                imageView.setVisibility(8);
            }
        }
        View a10 = preferenceViewHolder.a(R.id.icon);
        View a11 = preferenceViewHolder.a(R$id.img_layout);
        if (a11 != null) {
            if (a10 != null) {
                a11.setVisibility(a10.getVisibility());
            } else {
                a11.setVisibility(8);
            }
        }
        TextView textView = (TextView) preferenceViewHolder.a(R$id.coui_statusText1);
        if (textView != null) {
            if (!TextUtils.isEmpty(charSequence)) {
                textView.setText(charSequence);
                textView.setVisibility(0);
            } else {
                textView.setVisibility(8);
            }
        }
        TextView textView2 = (TextView) preferenceViewHolder.a(R$id.assignment);
        if (textView2 != null) {
            if (!TextUtils.isEmpty(charSequence2)) {
                textView2.setText(charSequence2);
                textView2.setVisibility(0);
                if (i10 != 0) {
                    textView2.setTextColor(i10);
                    return;
                }
                return;
            }
            textView2.setVisibility(8);
        }
    }

    public static void c(PreferenceViewHolder preferenceViewHolder, Context context, int i10, boolean z10, int i11, boolean z11) {
        View a10 = preferenceViewHolder.a(R.id.icon);
        if (a10 == null || !(a10 instanceof COUIRoundImageView)) {
            return;
        }
        if (z11) {
            COUIRoundImageView cOUIRoundImageView = (COUIRoundImageView) a10;
            cOUIRoundImageView.setHasBorder(z10);
            cOUIRoundImageView.setBorderRectRadius(0);
            cOUIRoundImageView.setType(i11);
            return;
        }
        COUIRoundImageView cOUIRoundImageView2 = (COUIRoundImageView) a10;
        Drawable drawable = cOUIRoundImageView2.getDrawable();
        if (drawable != null && i10 == 14) {
            i10 = drawable.getIntrinsicHeight() / 6;
            Resources resources = context.getResources();
            int i12 = R$dimen.coui_preference_icon_min_radius;
            if (i10 < resources.getDimensionPixelOffset(i12)) {
                i10 = context.getResources().getDimensionPixelOffset(i12);
            } else {
                Resources resources2 = context.getResources();
                int i13 = R$dimen.coui_preference_icon_max_radius;
                if (i10 > resources2.getDimensionPixelOffset(i13)) {
                    i10 = context.getResources().getDimensionPixelOffset(i13);
                }
            }
        }
        cOUIRoundImageView2.setHasBorder(z10);
        cOUIRoundImageView2.setBorderRectRadius(i10);
        cOUIRoundImageView2.setType(i11);
    }

    public static void d(Context context, PreferenceViewHolder preferenceViewHolder) {
        TextView textView = (TextView) preferenceViewHolder.a(R.id.summary);
        if (textView != null) {
            textView.setHighlightColor(context.getResources().getColor(R.color.transparent));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setOnTouchListener(new a(textView));
        }
    }

    public static void e(PreferenceViewHolder preferenceViewHolder, ColorStateList colorStateList) {
        TextView textView = (TextView) preferenceViewHolder.a(R.id.summary);
        if (textView == null || colorStateList == null) {
            return;
        }
        textView.setTextColor(colorStateList);
    }

    public static void f(Context context, PreferenceViewHolder preferenceViewHolder, ColorStateList colorStateList) {
        TextView textView = (TextView) preferenceViewHolder.a(R.id.title);
        if (textView == null || colorStateList == null) {
            return;
        }
        textView.setTextColor(colorStateList);
    }
}
