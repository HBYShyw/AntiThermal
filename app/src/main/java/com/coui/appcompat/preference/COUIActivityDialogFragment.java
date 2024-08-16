package com.coui.appcompat.preference;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.preference.ListPreferenceDialogFragmentCompat;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.AppBarLayout;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$style;
import com.support.list.R$bool;
import com.support.list.R$dimen;
import com.support.list.R$id;
import com.support.list.R$layout;
import j3.COUIVersionUtil;
import w1.COUIDarkModeUtil;

/* compiled from: COUIActivityDialogFragment.java */
/* renamed from: com.coui.appcompat.preference.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIActivityDialogFragment extends ListPreferenceDialogFragmentCompat {
    private AppCompatDialog F;
    private int G;

    /* compiled from: COUIActivityDialogFragment.java */
    /* renamed from: com.coui.appcompat.preference.a$a */
    /* loaded from: classes.dex */
    class a extends AppCompatDialog {
        a(Context context, int i10) {
            super(context, i10);
        }

        @Override // android.app.Dialog, android.view.Window.Callback
        public boolean onMenuItemSelected(int i10, MenuItem menuItem) {
            if (menuItem.getItemId() == 16908332) {
                dismiss();
                return true;
            }
            return super.onMenuItemSelected(i10, menuItem);
        }
    }

    /* compiled from: COUIActivityDialogFragment.java */
    /* renamed from: com.coui.appcompat.preference.a$b */
    /* loaded from: classes.dex */
    class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIActivityDialogFragment.this.F.dismiss();
        }
    }

    /* compiled from: COUIActivityDialogFragment.java */
    /* renamed from: com.coui.appcompat.preference.a$c */
    /* loaded from: classes.dex */
    class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ AppBarLayout f7058e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ ListView f7059f;

        c(AppBarLayout appBarLayout, ListView listView) {
            this.f7058e = appBarLayout;
            this.f7059f = listView;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUIActivityDialogFragment.this.isAdded()) {
                int measuredHeight = this.f7058e.getMeasuredHeight() + COUIActivityDialogFragment.this.getResources().getDimensionPixelSize(R$dimen.support_preference_listview_margin_top);
                View view = new View(this.f7058e.getContext());
                view.setVisibility(4);
                view.setLayoutParams(new AbsListView.LayoutParams(-1, measuredHeight));
                this.f7059f.addHeaderView(view);
            }
        }
    }

    /* compiled from: COUIActivityDialogFragment.java */
    /* renamed from: com.coui.appcompat.preference.a$d */
    /* loaded from: classes.dex */
    class d extends e {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ListView f7061e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ AppCompatDialog f7062f;

        /* compiled from: COUIActivityDialogFragment.java */
        /* renamed from: com.coui.appcompat.preference.a$d$a */
        /* loaded from: classes.dex */
        class a implements View.OnClickListener {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ int f7064e;

            a(int i10) {
                this.f7064e = i10;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                COUIActivityDialogFragment.this.G = this.f7064e;
                COUIActivityDialogFragment.this.onClick(null, -1);
                d.this.f7062f.dismiss();
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(Context context, int i10, int i11, CharSequence[] charSequenceArr, ListView listView, AppCompatDialog appCompatDialog) {
            super(context, i10, i11, charSequenceArr);
            this.f7061e = listView;
            this.f7062f = appCompatDialog;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i10, View view, ViewGroup viewGroup) {
            View view2 = super.getView(i10, view, viewGroup);
            if (i10 == COUIActivityDialogFragment.this.G) {
                ListView listView = this.f7061e;
                listView.setItemChecked(listView.getHeaderViewsCount() + i10, true);
            }
            View findViewById = view2.findViewById(R$id.item_divider);
            int count = getCount();
            if (findViewById != null) {
                if (count != 1 && i10 != count - 1) {
                    findViewById.setVisibility(0);
                } else {
                    findViewById.setVisibility(8);
                }
            }
            view2.setOnClickListener(new a(i10));
            COUICardListHelper.d(view2, COUICardListHelper.a(COUIActivityDialogFragment.this.E0().j().length, i10));
            return view2;
        }
    }

    /* compiled from: COUIActivityDialogFragment.java */
    /* renamed from: com.coui.appcompat.preference.a$e */
    /* loaded from: classes.dex */
    private static class e extends ArrayAdapter<CharSequence> {
        e(Context context, int i10, int i11, CharSequence[] charSequenceArr) {
            super(context, i10, i11, charSequenceArr);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public long getItemId(int i10) {
            return i10;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public boolean hasStableIds() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public COUIActivityDialogPreference E0() {
        return (COUIActivityDialogPreference) r0();
    }

    public static int F0(Context context) {
        int identifier = context.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            try {
                return context.getApplicationContext().getResources().getDimensionPixelSize(identifier);
            } catch (Exception e10) {
                e10.printStackTrace();
            }
        }
        return -1;
    }

    private View G0(Context context) {
        int F0 = F0(context);
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, F0));
        return imageView;
    }

    public static COUIActivityDialogFragment H0(String str) {
        COUIActivityDialogFragment cOUIActivityDialogFragment = new COUIActivityDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        cOUIActivityDialogFragment.setArguments(bundle);
        return cOUIActivityDialogFragment;
    }

    @Override // androidx.preference.PreferenceDialogFragmentCompat, androidx.fragment.app.DialogFragment
    public Dialog k0(Bundle bundle) {
        a aVar = new a(getActivity(), R$style.Theme_COUI_ActivityDialog);
        this.F = aVar;
        if (aVar.getWindow() != null) {
            Window window = aVar.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(1024);
            window.setStatusBarColor(0);
            int systemUiVisibility = decorView.getSystemUiVisibility();
            int b10 = COUIVersionUtil.b();
            boolean z10 = getResources().getBoolean(R$bool.list_status_white_enabled);
            if (b10 >= 6 || b10 == 0) {
                window.addFlags(Integer.MIN_VALUE);
                decorView.setSystemUiVisibility(COUIDarkModeUtil.a(aVar.getContext()) ? systemUiVisibility & (-8193) & (-17) : !z10 ? systemUiVisibility | 8192 : systemUiVisibility | 256);
            }
        }
        View inflate = LayoutInflater.from(getActivity()).inflate(R$layout.coui_preference_listview, (ViewGroup) null);
        COUIToolbar cOUIToolbar = (COUIToolbar) inflate.findViewById(R$id.toolbar);
        cOUIToolbar.setNavigationIcon(ContextCompat.e(cOUIToolbar.getContext(), R$drawable.coui_back_arrow));
        cOUIToolbar.setNavigationOnClickListener(new b());
        AppBarLayout appBarLayout = (AppBarLayout) inflate.findViewById(R$id.abl);
        ListView listView = (ListView) inflate.findViewById(R$id.coui_preference_listview);
        View findViewById = inflate.findViewById(R$id.divider_line);
        if (getResources().getBoolean(R$bool.is_dialog_preference_immersive)) {
            findViewById.setVisibility(8);
        }
        ViewCompat.y0(listView, true);
        View G0 = G0(appBarLayout.getContext());
        appBarLayout.addView(G0, 0, G0.getLayoutParams());
        appBarLayout.post(new c(appBarLayout, listView));
        if (E0() != null) {
            this.G = E0().i(E0().n());
            cOUIToolbar.setTitle(E0().f());
            listView.setAdapter((ListAdapter) new d(getActivity(), R$layout.coui_preference_listview_item, R$id.checkedtextview, E0().j(), listView, aVar));
        }
        listView.setChoiceMode(1);
        aVar.setContentView(inflate);
        return aVar;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (r0() == null) {
            g0();
        }
    }

    @Override // androidx.preference.ListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat
    public void v0(boolean z10) {
        COUIActivityDialogPreference E0 = E0();
        if (!z10 || this.G < 0) {
            return;
        }
        String charSequence = E0().m()[this.G].toString();
        if (E0.callChangeListener(charSequence)) {
            E0.p(charSequence);
        }
    }
}
