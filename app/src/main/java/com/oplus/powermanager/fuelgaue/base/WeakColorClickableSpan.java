package com.oplus.powermanager.fuelgaue.base;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import com.oplus.battery.R;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class WeakColorClickableSpan extends ClickableSpan {
    private static final String TAG = "WeakColorClickableSpan";
    private SpannableStrClickListener mClickReference;
    private WeakReference<Context> mContextWeakReference;
    private Intent mIntent;
    private int mLinkResColor;

    /* loaded from: classes.dex */
    public interface SpannableStrClickListener {
        void onClick(Context context);
    }

    public WeakColorClickableSpan(Context context, Intent intent) {
        this(context, intent, R.color.wireless_link_color);
    }

    @Override // android.text.style.ClickableSpan
    public void onClick(View view) {
        WeakReference<Context> weakReference;
        if (this.mIntent == null || (weakReference = this.mContextWeakReference) == null) {
            return;
        }
        Context context = weakReference.get();
        if (context != null) {
            this.mClickReference.onClick(context);
        } else {
            Log.d(TAG, "onClick: context is null.");
        }
    }

    public void setStatusBarClickListener(SpannableStrClickListener spannableStrClickListener) {
        this.mClickReference = spannableStrClickListener;
    }

    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        WeakReference<Context> weakReference;
        Context context;
        if (this.mLinkResColor == -1 || (weakReference = this.mContextWeakReference) == null || textPaint == null || (context = weakReference.get()) == null) {
            return;
        }
        textPaint.setColor(context.getColor(this.mLinkResColor));
    }

    public WeakColorClickableSpan(Context context, Intent intent, int i10) {
        this.mLinkResColor = -1;
        this.mContextWeakReference = new WeakReference<>(context);
        this.mIntent = intent;
        this.mLinkResColor = i10;
    }
}
