package com.google.android.material.datepicker;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;

/* compiled from: MaterialTextInputPicker.java */
/* renamed from: com.google.android.material.datepicker.i, reason: use source file name */
/* loaded from: classes.dex */
public final class MaterialTextInputPicker<S> extends PickerFragment<S> {

    /* renamed from: f, reason: collision with root package name */
    private int f8759f;

    /* renamed from: g, reason: collision with root package name */
    private DateSelector<S> f8760g;

    /* renamed from: h, reason: collision with root package name */
    private CalendarConstraints f8761h;

    /* compiled from: MaterialTextInputPicker.java */
    /* renamed from: com.google.android.material.datepicker.i$a */
    /* loaded from: classes.dex */
    class a extends OnSelectionChangedListener<S> {
        a() {
        }

        @Override // com.google.android.material.datepicker.OnSelectionChangedListener
        public void a() {
            Iterator<OnSelectionChangedListener<S>> it = MaterialTextInputPicker.this.f8777e.iterator();
            while (it.hasNext()) {
                it.next().a();
            }
        }

        @Override // com.google.android.material.datepicker.OnSelectionChangedListener
        public void b(S s7) {
            Iterator<OnSelectionChangedListener<S>> it = MaterialTextInputPicker.this.f8777e.iterator();
            while (it.hasNext()) {
                it.next().b(s7);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> MaterialTextInputPicker<T> f0(DateSelector<T> dateSelector, int i10, CalendarConstraints calendarConstraints) {
        MaterialTextInputPicker<T> materialTextInputPicker = new MaterialTextInputPicker<>();
        Bundle bundle = new Bundle();
        bundle.putInt("THEME_RES_ID_KEY", i10);
        bundle.putParcelable("DATE_SELECTOR_KEY", dateSelector);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", calendarConstraints);
        materialTextInputPicker.setArguments(bundle);
        return materialTextInputPicker;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            bundle = getArguments();
        }
        this.f8759f = bundle.getInt("THEME_RES_ID_KEY");
        this.f8760g = (DateSelector) bundle.getParcelable("DATE_SELECTOR_KEY");
        this.f8761h = (CalendarConstraints) bundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.f8760g.c(layoutInflater.cloneInContext(new ContextThemeWrapper(getContext(), this.f8759f)), viewGroup, bundle, this.f8761h, new a());
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("THEME_RES_ID_KEY", this.f8759f);
        bundle.putParcelable("DATE_SELECTOR_KEY", this.f8760g);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", this.f8761h);
    }
}
