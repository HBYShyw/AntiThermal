package com.oplus.powermanager.fuelgaue.basic.customized;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.coui.appcompat.cardlist.COUICardListSelectedItemLayout;
import com.oplus.battery.R;

/* loaded from: classes.dex */
public class PowerProtectInfoView extends COUICardListSelectedItemLayout {
    private ImageView D;
    private TextView E;
    private ImageView F;
    private boolean G;
    private boolean H;
    private ImageView I;
    private View J;
    private RelativeLayout K;

    public PowerProtectInfoView(Context context) {
        super(context);
        this.G = false;
        this.H = true;
        ((LinearLayout) this).mContext = context;
        s(context);
    }

    private void s(Context context) {
        LayoutInflater.from(context).inflate(R.layout.pm_power_protect_list_item, this);
        this.D = (ImageView) findViewById(android.R.id.icon);
        this.E = (TextView) findViewById(android.R.id.title);
        this.F = (ImageView) findViewById(R.id.pm_consumption_new_dot);
        this.I = (ImageView) findViewById(R.id.item_arrow_icon);
        this.J = findViewById(R.id.divider_line);
        this.K = (RelativeLayout) findViewById(R.id.relativeLayout);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = -1;
        } else {
            layoutParams = new LinearLayout.LayoutParams(-1, -2);
        }
        setLayoutParams(layoutParams);
    }

    public String getTitle() {
        TextView textView = this.E;
        return textView != null ? textView.getText().toString() : "";
    }

    public void setDividerLineVisibility(int i10) {
        this.J.setVisibility(i10);
    }

    public void setDotVisible(boolean z10) {
        this.G = z10;
        ImageView imageView = this.F;
        if (imageView != null) {
            imageView.setVisibility(z10 ? 0 : 4);
        }
    }

    public void setIcon(Drawable drawable) {
        ImageView imageView = this.D;
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }

    public void setLayoutHeight(int i10) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
        layoutParams.height = i10;
        relativeLayout.setLayoutParams(layoutParams);
    }

    public void setTitle(String str) {
        TextView textView = this.E;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void setTitleColor(int i10) {
        TextView textView = this.E;
        if (textView != null) {
            textView.setTextColor(i10);
        }
    }

    public void setTitleSize(float f10) {
        this.E.setTextSize(0, f10);
    }

    public PowerProtectInfoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.G = false;
        this.H = true;
        ((LinearLayout) this).mContext = context;
        s(context);
    }

    public PowerProtectInfoView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.G = false;
        this.H = true;
        ((LinearLayout) this).mContext = context;
        s(context);
    }
}
