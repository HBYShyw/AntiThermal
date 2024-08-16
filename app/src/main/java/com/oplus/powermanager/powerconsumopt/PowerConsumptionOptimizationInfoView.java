package com.oplus.powermanager.powerconsumopt;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.oplus.statistics.DataTypeConstants;

/* loaded from: classes2.dex */
public class PowerConsumptionOptimizationInfoView extends COUICardListSelectedItemLayout {
    private ImageView D;
    private TextView E;
    private TextView F;
    private boolean G;
    private Drawable H;
    private View I;
    private RelativeLayout J;
    private Handler K;

    /* loaded from: classes2.dex */
    class a extends Handler {
        a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 1001 || PowerConsumptionOptimizationInfoView.this.D == null) {
                return;
            }
            PowerConsumptionOptimizationInfoView.this.D.setImageDrawable(PowerConsumptionOptimizationInfoView.this.H);
        }
    }

    public PowerConsumptionOptimizationInfoView(Context context) {
        super(context);
        this.G = true;
        this.K = new a(Looper.getMainLooper());
        ((LinearLayout) this).mContext = context;
        u(context);
    }

    private void u(Context context) {
        LayoutInflater.from(context).inflate(R.layout.power_consumption_optimization_item, this);
        this.D = (ImageView) findViewById(android.R.id.icon);
        this.E = (TextView) findViewById(android.R.id.title);
        this.F = (TextView) findViewById(R.id.pco_text);
        this.I = findViewById(R.id.divider_line);
        this.J = (RelativeLayout) findViewById(R.id.relativeLayout);
    }

    public void setDividerLineVisibility(int i10) {
        this.I.setVisibility(i10);
    }

    public void setIcon(Drawable drawable) {
        this.H = drawable;
        this.K.removeMessages(DataTypeConstants.USER_ACTION);
        this.K.sendEmptyMessage(DataTypeConstants.USER_ACTION);
    }

    public void setLayoutHeight(int i10) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
        layoutParams.height = i10;
        relativeLayout.setLayoutParams(layoutParams);
    }

    public void setText(String str) {
        TextView textView = this.F;
        if (textView != null) {
            textView.setText(str);
        }
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

    public boolean v() {
        return this.G;
    }

    public void w() {
        this.D.setVisibility(0);
    }

    public PowerConsumptionOptimizationInfoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.G = true;
        this.K = new a(Looper.getMainLooper());
        ((LinearLayout) this).mContext = context;
        u(context);
    }

    public PowerConsumptionOptimizationInfoView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.G = true;
        this.K = new a(Looper.getMainLooper());
        ((LinearLayout) this).mContext = context;
        u(context);
    }
}
