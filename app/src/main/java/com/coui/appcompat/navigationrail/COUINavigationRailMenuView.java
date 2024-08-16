package com.coui.appcompat.navigationrail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigationrail.NavigationRailMenuView;

@SuppressLint({"RestrictedApi"})
/* loaded from: classes.dex */
public class COUINavigationRailMenuView extends NavigationRailMenuView {

    /* loaded from: classes.dex */
    class a implements View.OnLongClickListener {
        a() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return true;
        }
    }

    public COUINavigationRailMenuView(Context context) {
        super(context);
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        super.addView(view);
        view.setOnLongClickListener(new a());
    }

    @Override // com.google.android.material.navigationrail.NavigationRailMenuView, com.google.android.material.navigation.NavigationBarMenuView
    protected NavigationBarItemView f(Context context) {
        return new COUINavigationRailItemView(context);
    }
}
