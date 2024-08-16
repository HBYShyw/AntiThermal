package com.oplus.powermanager.fuelgaue.basic.customized;

import android.content.Context;
import android.content.pm.OplusPackageManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import b6.LocalLog;
import b9.PowerSipper;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.oplus.battery.R;
import java.util.ArrayList;
import java.util.List;
import l8.IPowerRankingUpdate;

/* loaded from: classes.dex */
public class PowerRankingPreference extends Preference {

    /* renamed from: e, reason: collision with root package name */
    private IPowerRankingUpdate f10252e;

    /* renamed from: f, reason: collision with root package name */
    private GridView f10253f;

    /* renamed from: g, reason: collision with root package name */
    private a f10254g;

    /* renamed from: h, reason: collision with root package name */
    private ArrayList<PowerSipper> f10255h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f10256i;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class a extends BaseAdapter {

        /* renamed from: e, reason: collision with root package name */
        ArrayList<PowerSipper> f10257e;

        /* renamed from: f, reason: collision with root package name */
        Context f10258f;

        /* renamed from: com.oplus.powermanager.fuelgaue.basic.customized.PowerRankingPreference$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public static class C0025a {

            /* renamed from: a, reason: collision with root package name */
            ImageView f10259a;
        }

        public a(Context context, ArrayList<PowerSipper> arrayList) {
            this.f10257e = arrayList;
            this.f10258f = context;
        }

        @Override // android.widget.Adapter
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public PowerSipper getItem(int i10) {
            return this.f10257e.get(i10);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.f10257e.size();
        }

        @Override // android.widget.Adapter
        public long getItemId(int i10) {
            return i10;
        }

        @Override // android.widget.Adapter
        public View getView(int i10, View view, ViewGroup viewGroup) {
            C0025a c0025a;
            if (view == null) {
                view = View.inflate(this.f10258f, R.layout.power_ranking_gridview_layout, null);
                c0025a = new C0025a();
                c0025a.f10259a = (ImageView) view.findViewById(R.id.power_ranking_grid_img);
                view.setTag(c0025a);
            } else {
                c0025a = (C0025a) view.getTag();
            }
            new OplusPackageManager(this.f10258f);
            c0025a.f10259a.setImageDrawable(getItem(i10).f4614r);
            return view;
        }
    }

    public PowerRankingPreference(Context context) {
        this(context, null);
        setLayoutResource(R.layout.power_ranking_preference_layout);
    }

    public void c(IPowerRankingUpdate iPowerRankingUpdate) {
        this.f10252e = iPowerRankingUpdate;
    }

    public void d(Context context, List<PowerSipper> list) {
        this.f10255h.clear();
        if (list != null && list.size() > 0) {
            this.f10255h.addAll(list.subList(0, Math.min(list.size(), 3)));
            a aVar = this.f10254g;
            if (aVar == null) {
                a aVar2 = new a(context, this.f10255h);
                this.f10254g = aVar2;
                GridView gridView = this.f10253f;
                if (gridView != null) {
                    gridView.setAdapter((ListAdapter) aVar2);
                }
            } else {
                aVar.notifyDataSetChanged();
            }
            GridView gridView2 = this.f10253f;
            if (gridView2 != null && gridView2.getVisibility() != 0) {
                this.f10253f.setVisibility(0);
            }
        } else {
            GridView gridView3 = this.f10253f;
            if (gridView3 != null) {
                gridView3.setVisibility(8);
            }
            GridView gridView4 = this.f10253f;
            if (gridView4 != null && this.f10254g != null) {
                gridView4.setAdapter((ListAdapter) null);
            }
            this.f10254g = null;
        }
        this.f10256i = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    @Override // androidx.preference.Preference
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
        GridView gridView = (GridView) preferenceViewHolder.itemView.findViewById(R.id.power_ranking_top_three_grid_view);
        this.f10253f = gridView;
        gridView.setEnabled(false);
        this.f10253f.setClickable(false);
        this.f10253f.setLongClickable(false);
        if (!this.f10256i) {
            this.f10252e.O(0L, 0L);
        }
        a aVar = this.f10254g;
        if (aVar != null && aVar.f10257e != null) {
            if (this.f10253f.getAdapter() != null) {
                ListAdapter adapter = this.f10253f.getAdapter();
                a aVar2 = this.f10254g;
                if (adapter == aVar2) {
                    aVar2.notifyDataSetChanged();
                    if (this.f10253f.getVisibility() == 0) {
                        this.f10253f.setVisibility(0);
                        return;
                    }
                    return;
                }
            }
            this.f10253f.setAdapter((ListAdapter) this.f10254g);
            if (this.f10253f.getVisibility() == 0) {
            }
        } else if (this.f10256i) {
            this.f10253f.setVisibility(8);
        }
    }

    @Override // androidx.preference.Preference
    public void onDetached() {
        LocalLog.a("PowerRankingPreference", "onDetached");
        c(null);
        super.onDetached();
    }

    public PowerRankingPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PowerRankingPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public PowerRankingPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f10252e = null;
        this.f10255h = new ArrayList<>();
        this.f10256i = false;
        setLayoutResource(R.layout.power_ranking_preference_layout);
    }
}
