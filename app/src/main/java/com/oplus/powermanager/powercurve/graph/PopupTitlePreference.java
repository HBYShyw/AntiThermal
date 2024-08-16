package com.oplus.powermanager.powercurve.graph;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.preference.COUIPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.powercurve.graph.PopupTitlePreference;
import g2.COUIPopupListWindow;
import g2.PopupListItem;
import java.util.ArrayList;
import l8.IBatteryPageUpdate;

/* loaded from: classes2.dex */
public class PopupTitlePreference extends COUIPreference {
    private IBatteryPageUpdate F;
    private boolean G;
    private boolean H;
    private TextView I;
    private View J;
    private LinearLayout K;
    private COUIPopupListWindow L;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class a extends ArrayList<PopupListItem> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f10316e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ String f10317f;

        a(String str, String str2) {
            this.f10316e = str;
            this.f10317f = str2;
            add(new PopupListItem(str, true));
            add(new PopupListItem(str2, true));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class b implements PopupWindow.OnDismissListener {
        b() {
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            PopupTitlePreference.this.n(false);
        }
    }

    public PopupTitlePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.F = null;
        this.G = false;
        this.H = false;
        j();
    }

    private void j() {
        setLayoutResource(R.layout.popup_title_preference);
        setSelectable(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void l(View view) {
        o();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void m(String str, String str2, AdapterView adapterView, View view, int i10, long j10) {
        if (i10 == 0) {
            this.F.Z();
            this.I.setText(str);
        } else if (i10 == 1) {
            this.F.n();
            this.I.setText(str2);
        }
        this.L.dismiss();
    }

    private void o() {
        COUIPopupListWindow cOUIPopupListWindow = this.L;
        if (cOUIPopupListWindow == null) {
            this.L = new COUIPopupListWindow(getContext());
            getContext().getResources();
            final String string = getContext().getResources().getString(R.string.battery_ui_power_consumption);
            final String string2 = getContext().getResources().getString(R.string.battery_ui_usage_time);
            this.L.J(new a(string, string2));
            this.L.b(true);
            this.L.M(new AdapterView.OnItemClickListener() { // from class: p8.b
                @Override // android.widget.AdapterView.OnItemClickListener
                public final void onItemClick(AdapterView adapterView, View view, int i10, long j10) {
                    PopupTitlePreference.this.m(string, string2, adapterView, view, i10, j10);
                }
            });
            this.L.setOnDismissListener(new b());
            this.L.R(this.I);
            n(true);
            return;
        }
        if (cOUIPopupListWindow.isShowing()) {
            this.L.dismiss();
        } else {
            this.L.R(this.I);
            n(true);
        }
    }

    public void n(boolean z10) {
        if (this.H != z10) {
            this.H = z10;
            View view = this.J;
            if (view != null) {
                view.setRotation(z10 ? 180.0f : 0.0f);
            }
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.I = (TextView) preferenceViewHolder.a(R.id.popup_tap);
        this.J = preferenceViewHolder.a(R.id.expand);
        LinearLayout linearLayout = (LinearLayout) preferenceViewHolder.a(R.id.popup_layout);
        this.K = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: p8.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PopupTitlePreference.this.l(view);
            }
        });
    }

    public PopupTitlePreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.F = null;
        this.G = false;
        this.H = false;
        j();
    }
}
