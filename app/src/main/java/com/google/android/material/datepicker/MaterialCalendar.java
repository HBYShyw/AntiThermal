package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.core.util.Pair;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R$dimen;
import com.google.android.material.R$id;
import com.google.android.material.R$integer;
import com.google.android.material.R$layout;
import com.google.android.material.R$string;
import com.google.android.material.button.MaterialButton;
import java.util.Calendar;
import java.util.Iterator;

/* compiled from: MaterialCalendar.java */
/* renamed from: com.google.android.material.datepicker.f, reason: use source file name */
/* loaded from: classes.dex */
public final class MaterialCalendar<S> extends PickerFragment<S> {

    /* renamed from: p, reason: collision with root package name */
    static final Object f8712p = "MONTHS_VIEW_GROUP_TAG";

    /* renamed from: q, reason: collision with root package name */
    static final Object f8713q = "NAVIGATION_PREV_TAG";

    /* renamed from: r, reason: collision with root package name */
    static final Object f8714r = "NAVIGATION_NEXT_TAG";

    /* renamed from: s, reason: collision with root package name */
    static final Object f8715s = "SELECTOR_TOGGLE_TAG";

    /* renamed from: f, reason: collision with root package name */
    private int f8716f;

    /* renamed from: g, reason: collision with root package name */
    private DateSelector<S> f8717g;

    /* renamed from: h, reason: collision with root package name */
    private CalendarConstraints f8718h;

    /* renamed from: i, reason: collision with root package name */
    private Month f8719i;

    /* renamed from: j, reason: collision with root package name */
    private k f8720j;

    /* renamed from: k, reason: collision with root package name */
    private CalendarStyle f8721k;

    /* renamed from: l, reason: collision with root package name */
    private RecyclerView f8722l;

    /* renamed from: m, reason: collision with root package name */
    private RecyclerView f8723m;

    /* renamed from: n, reason: collision with root package name */
    private View f8724n;

    /* renamed from: o, reason: collision with root package name */
    private View f8725o;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$a */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f8726e;

        a(int i10) {
            this.f8726e = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            MaterialCalendar.this.f8723m.smoothScrollToPosition(this.f8726e);
        }
    }

    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$b */
    /* loaded from: classes.dex */
    class b extends AccessibilityDelegateCompat {
        b() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.X(null);
        }
    }

    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$c */
    /* loaded from: classes.dex */
    class c extends SmoothCalendarLayoutManager {
        final /* synthetic */ int I;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(Context context, int i10, boolean z10, int i11) {
            super(context, i10, z10);
            this.I = i11;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearLayoutManager
        public void O1(RecyclerView.z zVar, int[] iArr) {
            if (this.I == 0) {
                iArr[0] = MaterialCalendar.this.f8723m.getWidth();
                iArr[1] = MaterialCalendar.this.f8723m.getWidth();
            } else {
                iArr[0] = MaterialCalendar.this.f8723m.getHeight();
                iArr[1] = MaterialCalendar.this.f8723m.getHeight();
            }
        }
    }

    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$d */
    /* loaded from: classes.dex */
    class d implements l {
        d() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.android.material.datepicker.MaterialCalendar.l
        public void a(long j10) {
            if (MaterialCalendar.this.f8718h.o().e(j10)) {
                MaterialCalendar.this.f8717g.i(j10);
                Iterator<OnSelectionChangedListener<S>> it = MaterialCalendar.this.f8777e.iterator();
                while (it.hasNext()) {
                    it.next().b(MaterialCalendar.this.f8717g.h());
                }
                MaterialCalendar.this.f8723m.getAdapter().notifyDataSetChanged();
                if (MaterialCalendar.this.f8722l != null) {
                    MaterialCalendar.this.f8722l.getAdapter().notifyDataSetChanged();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$e */
    /* loaded from: classes.dex */
    public class e extends RecyclerView.o {

        /* renamed from: a, reason: collision with root package name */
        private final Calendar f8730a = UtcDates.l();

        /* renamed from: b, reason: collision with root package name */
        private final Calendar f8731b = UtcDates.l();

        e() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.o
        public void g(Canvas canvas, RecyclerView recyclerView, RecyclerView.z zVar) {
            int width;
            if ((recyclerView.getAdapter() instanceof YearGridAdapter) && (recyclerView.getLayoutManager() instanceof GridLayoutManager)) {
                YearGridAdapter yearGridAdapter = (YearGridAdapter) recyclerView.getAdapter();
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                for (Pair<Long, Long> pair : MaterialCalendar.this.f8717g.b()) {
                    Long l10 = pair.f2305a;
                    if (l10 != null && pair.f2306b != null) {
                        this.f8730a.setTimeInMillis(l10.longValue());
                        this.f8731b.setTimeInMillis(pair.f2306b.longValue());
                        int e10 = yearGridAdapter.e(this.f8730a.get(1));
                        int e11 = yearGridAdapter.e(this.f8731b.get(1));
                        View C = gridLayoutManager.C(e10);
                        View C2 = gridLayoutManager.C(e11);
                        int X2 = e10 / gridLayoutManager.X2();
                        int X22 = e11 / gridLayoutManager.X2();
                        int i10 = X2;
                        while (i10 <= X22) {
                            View C3 = gridLayoutManager.C(gridLayoutManager.X2() * i10);
                            if (C3 != null) {
                                int top = C3.getTop() + MaterialCalendar.this.f8721k.f8693d.c();
                                int bottom = C3.getBottom() - MaterialCalendar.this.f8721k.f8693d.b();
                                int left = i10 == X2 ? C.getLeft() + (C.getWidth() / 2) : 0;
                                if (i10 == X22) {
                                    width = C2.getLeft() + (C2.getWidth() / 2);
                                } else {
                                    width = recyclerView.getWidth();
                                }
                                canvas.drawRect(left, top, width, bottom, MaterialCalendar.this.f8721k.f8697h);
                            }
                            i10++;
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$f */
    /* loaded from: classes.dex */
    public class f extends AccessibilityDelegateCompat {
        f() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            String string;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            if (MaterialCalendar.this.f8725o.getVisibility() == 0) {
                string = MaterialCalendar.this.getString(R$string.mtrl_picker_toggle_to_year_selection);
            } else {
                string = MaterialCalendar.this.getString(R$string.mtrl_picker_toggle_to_day_selection);
            }
            accessibilityNodeInfoCompat.g0(string);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$g */
    /* loaded from: classes.dex */
    public class g extends RecyclerView.t {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ MonthsPagerAdapter f8734a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ MaterialButton f8735b;

        g(MonthsPagerAdapter monthsPagerAdapter, MaterialButton materialButton) {
            this.f8734a = monthsPagerAdapter;
            this.f8735b = materialButton;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.t
        public void onScrollStateChanged(RecyclerView recyclerView, int i10) {
            if (i10 == 0) {
                recyclerView.announceForAccessibility(this.f8735b.getText());
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.t
        public void onScrolled(RecyclerView recyclerView, int i10, int i11) {
            int e22;
            if (i10 < 0) {
                e22 = MaterialCalendar.this.u0().b2();
            } else {
                e22 = MaterialCalendar.this.u0().e2();
            }
            MaterialCalendar.this.f8719i = this.f8734a.d(e22);
            this.f8735b.setText(this.f8734a.e(e22));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$h */
    /* loaded from: classes.dex */
    public class h implements View.OnClickListener {
        h() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            MaterialCalendar.this.z0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$i */
    /* loaded from: classes.dex */
    public class i implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ MonthsPagerAdapter f8738e;

        i(MonthsPagerAdapter monthsPagerAdapter) {
            this.f8738e = monthsPagerAdapter;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int b22 = MaterialCalendar.this.u0().b2() + 1;
            if (b22 < MaterialCalendar.this.f8723m.getAdapter().getItemCount()) {
                MaterialCalendar.this.x0(this.f8738e.d(b22));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$j */
    /* loaded from: classes.dex */
    public class j implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ MonthsPagerAdapter f8740e;

        j(MonthsPagerAdapter monthsPagerAdapter) {
            this.f8740e = monthsPagerAdapter;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int e22 = MaterialCalendar.this.u0().e2() - 1;
            if (e22 >= 0) {
                MaterialCalendar.this.x0(this.f8740e.d(e22));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$k */
    /* loaded from: classes.dex */
    public enum k {
        DAY,
        YEAR
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialCalendar.java */
    /* renamed from: com.google.android.material.datepicker.f$l */
    /* loaded from: classes.dex */
    public interface l {
        void a(long j10);
    }

    private void m0(View view, MonthsPagerAdapter monthsPagerAdapter) {
        MaterialButton materialButton = (MaterialButton) view.findViewById(R$id.month_navigation_fragment_toggle);
        materialButton.setTag(f8715s);
        ViewCompat.l0(materialButton, new f());
        MaterialButton materialButton2 = (MaterialButton) view.findViewById(R$id.month_navigation_previous);
        materialButton2.setTag(f8713q);
        MaterialButton materialButton3 = (MaterialButton) view.findViewById(R$id.month_navigation_next);
        materialButton3.setTag(f8714r);
        this.f8724n = view.findViewById(R$id.mtrl_calendar_year_selector_frame);
        this.f8725o = view.findViewById(R$id.mtrl_calendar_day_selector_frame);
        y0(k.DAY);
        materialButton.setText(this.f8719i.r());
        this.f8723m.addOnScrollListener(new g(monthsPagerAdapter, materialButton));
        materialButton.setOnClickListener(new h());
        materialButton3.setOnClickListener(new i(monthsPagerAdapter));
        materialButton2.setOnClickListener(new j(monthsPagerAdapter));
    }

    private RecyclerView.o n0() {
        return new e();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int s0(Context context) {
        return context.getResources().getDimensionPixelSize(R$dimen.mtrl_calendar_day_height);
    }

    private static int t0(Context context) {
        Resources resources = context.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.mtrl_calendar_navigation_height) + resources.getDimensionPixelOffset(R$dimen.mtrl_calendar_navigation_top_padding) + resources.getDimensionPixelOffset(R$dimen.mtrl_calendar_navigation_bottom_padding);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.mtrl_calendar_days_of_week_height);
        int i10 = MonthAdapter.f8763j;
        return dimensionPixelSize + dimensionPixelSize2 + (resources.getDimensionPixelSize(R$dimen.mtrl_calendar_day_height) * i10) + ((i10 - 1) * resources.getDimensionPixelOffset(R$dimen.mtrl_calendar_month_vertical_padding)) + resources.getDimensionPixelOffset(R$dimen.mtrl_calendar_bottom_padding);
    }

    public static <T> MaterialCalendar<T> v0(DateSelector<T> dateSelector, int i10, CalendarConstraints calendarConstraints) {
        MaterialCalendar<T> materialCalendar = new MaterialCalendar<>();
        Bundle bundle = new Bundle();
        bundle.putInt("THEME_RES_ID_KEY", i10);
        bundle.putParcelable("GRID_SELECTOR_KEY", dateSelector);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", calendarConstraints);
        bundle.putParcelable("CURRENT_MONTH_KEY", calendarConstraints.r());
        materialCalendar.setArguments(bundle);
        return materialCalendar;
    }

    private void w0(int i10) {
        this.f8723m.post(new a(i10));
    }

    @Override // com.google.android.material.datepicker.PickerFragment
    public boolean d0(OnSelectionChangedListener<S> onSelectionChangedListener) {
        return super.d0(onSelectionChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CalendarConstraints o0() {
        return this.f8718h;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            bundle = getArguments();
        }
        this.f8716f = bundle.getInt("THEME_RES_ID_KEY");
        this.f8717g = (DateSelector) bundle.getParcelable("GRID_SELECTOR_KEY");
        this.f8718h = (CalendarConstraints) bundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
        this.f8719i = (Month) bundle.getParcelable("CURRENT_MONTH_KEY");
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i10;
        int i11;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), this.f8716f);
        this.f8721k = new CalendarStyle(contextThemeWrapper);
        LayoutInflater cloneInContext = layoutInflater.cloneInContext(contextThemeWrapper);
        Month s7 = this.f8718h.s();
        if (MaterialDatePicker.H0(contextThemeWrapper)) {
            i10 = R$layout.mtrl_calendar_vertical;
            i11 = 1;
        } else {
            i10 = R$layout.mtrl_calendar_horizontal;
            i11 = 0;
        }
        View inflate = cloneInContext.inflate(i10, viewGroup, false);
        inflate.setMinimumHeight(t0(requireContext()));
        GridView gridView = (GridView) inflate.findViewById(R$id.mtrl_calendar_days_of_week);
        ViewCompat.l0(gridView, new b());
        gridView.setAdapter((ListAdapter) new DaysOfWeekAdapter());
        gridView.setNumColumns(s7.f8663h);
        gridView.setEnabled(false);
        this.f8723m = (RecyclerView) inflate.findViewById(R$id.mtrl_calendar_months);
        this.f8723m.setLayoutManager(new c(getContext(), i11, false, i11));
        this.f8723m.setTag(f8712p);
        MonthsPagerAdapter monthsPagerAdapter = new MonthsPagerAdapter(contextThemeWrapper, this.f8717g, this.f8718h, new d());
        this.f8723m.setAdapter(monthsPagerAdapter);
        int integer = contextThemeWrapper.getResources().getInteger(R$integer.mtrl_calendar_year_selector_span);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R$id.mtrl_calendar_year_selector_frame);
        this.f8722l = recyclerView;
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            this.f8722l.setLayoutManager(new GridLayoutManager((Context) contextThemeWrapper, integer, 1, false));
            this.f8722l.setAdapter(new YearGridAdapter(this));
            this.f8722l.addItemDecoration(n0());
        }
        if (inflate.findViewById(R$id.month_navigation_fragment_toggle) != null) {
            m0(inflate, monthsPagerAdapter);
        }
        if (!MaterialDatePicker.H0(contextThemeWrapper)) {
            new PagerSnapHelper().b(this.f8723m);
        }
        this.f8723m.scrollToPosition(monthsPagerAdapter.f(this.f8719i));
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("THEME_RES_ID_KEY", this.f8716f);
        bundle.putParcelable("GRID_SELECTOR_KEY", this.f8717g);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", this.f8718h);
        bundle.putParcelable("CURRENT_MONTH_KEY", this.f8719i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CalendarStyle p0() {
        return this.f8721k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month q0() {
        return this.f8719i;
    }

    public DateSelector<S> r0() {
        return this.f8717g;
    }

    LinearLayoutManager u0() {
        return (LinearLayoutManager) this.f8723m.getLayoutManager();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x0(Month month) {
        MonthsPagerAdapter monthsPagerAdapter = (MonthsPagerAdapter) this.f8723m.getAdapter();
        int f10 = monthsPagerAdapter.f(month);
        int f11 = f10 - monthsPagerAdapter.f(this.f8719i);
        boolean z10 = Math.abs(f11) > 3;
        boolean z11 = f11 > 0;
        this.f8719i = month;
        if (z10 && z11) {
            this.f8723m.scrollToPosition(f10 - 3);
            w0(f10);
        } else if (z10) {
            this.f8723m.scrollToPosition(f10 + 3);
            w0(f10);
        } else {
            w0(f10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y0(k kVar) {
        this.f8720j = kVar;
        if (kVar == k.YEAR) {
            this.f8722l.getLayoutManager().z1(((YearGridAdapter) this.f8722l.getAdapter()).e(this.f8719i.f8662g));
            this.f8724n.setVisibility(0);
            this.f8725o.setVisibility(8);
        } else if (kVar == k.DAY) {
            this.f8724n.setVisibility(8);
            this.f8725o.setVisibility(0);
            x0(this.f8719i);
        }
    }

    void z0() {
        k kVar = this.f8720j;
        k kVar2 = k.YEAR;
        if (kVar == kVar2) {
            y0(k.DAY);
        } else if (kVar == k.DAY) {
            y0(kVar2);
        }
    }
}
