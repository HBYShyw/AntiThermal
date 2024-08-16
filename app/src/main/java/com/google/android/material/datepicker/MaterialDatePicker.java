package com.google.android.material.datepicker;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.t;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import c.AppCompatResources;
import c4.MaterialShapeDrawable;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$drawable;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.R$string;
import com.google.android.material.R$style;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.internal.EdgeToEdgeUtils;
import com.google.android.material.internal.ViewUtils;
import java.util.Iterator;
import java.util.LinkedHashSet;
import s3.InsetDialogOnTouchListener;
import z3.MaterialAttributes;

/* compiled from: MaterialDatePicker.java */
/* renamed from: com.google.android.material.datepicker.g, reason: use source file name */
/* loaded from: classes.dex */
public final class MaterialDatePicker<S> extends DialogFragment {
    static final Object Q = "CONFIRM_BUTTON_TAG";
    static final Object R = "CANCEL_BUTTON_TAG";
    static final Object S = "TOGGLE_BUTTON_TAG";
    private PickerFragment<S> A;
    private CalendarConstraints B;
    private MaterialCalendar<S> C;
    private int D;
    private CharSequence E;
    private boolean F;
    private int G;
    private int H;
    private CharSequence I;
    private int J;
    private CharSequence K;
    private TextView L;
    private CheckableImageButton M;
    private MaterialShapeDrawable N;
    private Button O;
    private boolean P;

    /* renamed from: u, reason: collision with root package name */
    private final LinkedHashSet<MaterialPickerOnPositiveButtonClickListener<? super S>> f8745u = new LinkedHashSet<>();

    /* renamed from: v, reason: collision with root package name */
    private final LinkedHashSet<View.OnClickListener> f8746v = new LinkedHashSet<>();

    /* renamed from: w, reason: collision with root package name */
    private final LinkedHashSet<DialogInterface.OnCancelListener> f8747w = new LinkedHashSet<>();

    /* renamed from: x, reason: collision with root package name */
    private final LinkedHashSet<DialogInterface.OnDismissListener> f8748x = new LinkedHashSet<>();

    /* renamed from: y, reason: collision with root package name */
    private int f8749y;

    /* renamed from: z, reason: collision with root package name */
    private DateSelector<S> f8750z;

    /* compiled from: MaterialDatePicker.java */
    /* renamed from: com.google.android.material.datepicker.g$a */
    /* loaded from: classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Iterator it = MaterialDatePicker.this.f8745u.iterator();
            while (it.hasNext()) {
                ((MaterialPickerOnPositiveButtonClickListener) it.next()).a(MaterialDatePicker.this.E0());
            }
            MaterialDatePicker.this.g0();
        }
    }

    /* compiled from: MaterialDatePicker.java */
    /* renamed from: com.google.android.material.datepicker.g$b */
    /* loaded from: classes.dex */
    class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Iterator it = MaterialDatePicker.this.f8746v.iterator();
            while (it.hasNext()) {
                ((View.OnClickListener) it.next()).onClick(view);
            }
            MaterialDatePicker.this.g0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialDatePicker.java */
    /* renamed from: com.google.android.material.datepicker.g$c */
    /* loaded from: classes.dex */
    public class c implements t {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f8753a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f8754b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f8755c;

        c(int i10, View view, int i11) {
            this.f8753a = i10;
            this.f8754b = view;
            this.f8755c = i11;
        }

        @Override // androidx.core.view.t
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            int i10 = windowInsetsCompat.f(WindowInsetsCompat.l.c()).f2186b;
            if (this.f8753a >= 0) {
                this.f8754b.getLayoutParams().height = this.f8753a + i10;
                View view2 = this.f8754b;
                view2.setLayoutParams(view2.getLayoutParams());
            }
            View view3 = this.f8754b;
            view3.setPadding(view3.getPaddingLeft(), this.f8755c + i10, this.f8754b.getPaddingRight(), this.f8754b.getPaddingBottom());
            return windowInsetsCompat;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialDatePicker.java */
    /* renamed from: com.google.android.material.datepicker.g$d */
    /* loaded from: classes.dex */
    public class d extends OnSelectionChangedListener<S> {
        d() {
        }

        @Override // com.google.android.material.datepicker.OnSelectionChangedListener
        public void a() {
            MaterialDatePicker.this.O.setEnabled(false);
        }

        @Override // com.google.android.material.datepicker.OnSelectionChangedListener
        public void b(S s7) {
            MaterialDatePicker.this.L0();
            MaterialDatePicker.this.O.setEnabled(MaterialDatePicker.this.B0().f());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialDatePicker.java */
    /* renamed from: com.google.android.material.datepicker.g$e */
    /* loaded from: classes.dex */
    public class e implements View.OnClickListener {
        e() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            MaterialDatePicker.this.O.setEnabled(MaterialDatePicker.this.B0().f());
            MaterialDatePicker.this.M.toggle();
            MaterialDatePicker materialDatePicker = MaterialDatePicker.this;
            materialDatePicker.M0(materialDatePicker.M);
            MaterialDatePicker.this.K0();
        }
    }

    private void A0(Window window) {
        if (this.P) {
            return;
        }
        View findViewById = requireView().findViewById(R$id.fullscreen_header);
        EdgeToEdgeUtils.applyEdgeToEdge(window, true, ViewUtils.getBackgroundColor(findViewById), null);
        ViewCompat.z0(findViewById, new c(findViewById.getLayoutParams().height, findViewById, findViewById.getPaddingTop()));
        this.P = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DateSelector<S> B0() {
        if (this.f8750z == null) {
            this.f8750z = (DateSelector) getArguments().getParcelable("DATE_SELECTOR_KEY");
        }
        return this.f8750z;
    }

    private static int D0(Context context) {
        Resources resources = context.getResources();
        int dimensionPixelOffset = resources.getDimensionPixelOffset(R$dimen.mtrl_calendar_content_padding);
        int i10 = Month.n().f8663h;
        return (dimensionPixelOffset * 2) + (resources.getDimensionPixelSize(R$dimen.mtrl_calendar_day_width) * i10) + ((i10 - 1) * resources.getDimensionPixelOffset(R$dimen.mtrl_calendar_month_horizontal_padding));
    }

    private int F0(Context context) {
        int i10 = this.f8749y;
        return i10 != 0 ? i10 : B0().d(context);
    }

    private void G0(Context context) {
        this.M.setTag(S);
        this.M.setImageDrawable(z0(context));
        this.M.setChecked(this.G != 0);
        ViewCompat.l0(this.M, null);
        M0(this.M);
        this.M.setOnClickListener(new e());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean H0(Context context) {
        return J0(context, R.attr.windowFullscreen);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean I0(Context context) {
        return J0(context, R$attr.nestedScrollable);
    }

    static boolean J0(Context context, int i10) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(MaterialAttributes.d(context, R$attr.materialCalendarStyle, MaterialCalendar.class.getCanonicalName()), new int[]{i10});
        boolean z10 = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        return z10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void K0() {
        PickerFragment<S> pickerFragment;
        int F0 = F0(requireContext());
        this.C = MaterialCalendar.v0(B0(), F0, this.B);
        if (this.M.isChecked()) {
            pickerFragment = MaterialTextInputPicker.f0(B0(), F0, this.B);
        } else {
            pickerFragment = this.C;
        }
        this.A = pickerFragment;
        L0();
        FragmentTransaction m10 = getChildFragmentManager().m();
        m10.r(R$id.mtrl_calendar_frame, this.A);
        m10.k();
        this.A.d0(new d());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void L0() {
        String C0 = C0();
        this.L.setContentDescription(String.format(getString(R$string.mtrl_picker_announce_current_selection), C0));
        this.L.setText(C0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void M0(CheckableImageButton checkableImageButton) {
        String string;
        if (this.M.isChecked()) {
            string = checkableImageButton.getContext().getString(R$string.mtrl_picker_toggle_to_calendar_input_mode);
        } else {
            string = checkableImageButton.getContext().getString(R$string.mtrl_picker_toggle_to_text_input_mode);
        }
        this.M.setContentDescription(string);
    }

    private static Drawable z0(Context context) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842912}, AppCompatResources.b(context, R$drawable.material_ic_calendar_black_24dp));
        stateListDrawable.addState(new int[0], AppCompatResources.b(context, R$drawable.material_ic_edit_black_24dp));
        return stateListDrawable;
    }

    public String C0() {
        return B0().a(getContext());
    }

    public final S E0() {
        return B0().h();
    }

    @Override // androidx.fragment.app.DialogFragment
    public final Dialog k0(Bundle bundle) {
        Dialog dialog = new Dialog(requireContext(), F0(requireContext()));
        Context context = dialog.getContext();
        this.F = H0(context);
        int d10 = MaterialAttributes.d(context, R$attr.colorSurface, MaterialDatePicker.class.getCanonicalName());
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(context, null, R$attr.materialCalendarStyle, R$style.Widget_MaterialComponents_MaterialCalendar);
        this.N = materialShapeDrawable;
        materialShapeDrawable.P(context);
        this.N.a0(ColorStateList.valueOf(d10));
        this.N.Z(ViewCompat.t(dialog.getWindow().getDecorView()));
        return dialog;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        Iterator<DialogInterface.OnCancelListener> it = this.f8747w.iterator();
        while (it.hasNext()) {
            it.next().onCancel(dialogInterface);
        }
        super.onCancel(dialogInterface);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            bundle = getArguments();
        }
        this.f8749y = bundle.getInt("OVERRIDE_THEME_RES_ID");
        this.f8750z = (DateSelector) bundle.getParcelable("DATE_SELECTOR_KEY");
        this.B = (CalendarConstraints) bundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
        this.D = bundle.getInt("TITLE_TEXT_RES_ID_KEY");
        this.E = bundle.getCharSequence("TITLE_TEXT_KEY");
        this.G = bundle.getInt("INPUT_MODE_KEY");
        this.H = bundle.getInt("POSITIVE_BUTTON_TEXT_RES_ID_KEY");
        this.I = bundle.getCharSequence("POSITIVE_BUTTON_TEXT_KEY");
        this.J = bundle.getInt("NEGATIVE_BUTTON_TEXT_RES_ID_KEY");
        this.K = bundle.getCharSequence("NEGATIVE_BUTTON_TEXT_KEY");
    }

    @Override // androidx.fragment.app.Fragment
    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(this.F ? R$layout.mtrl_picker_fullscreen : R$layout.mtrl_picker_dialog, viewGroup);
        Context context = inflate.getContext();
        if (this.F) {
            inflate.findViewById(R$id.mtrl_calendar_frame).setLayoutParams(new LinearLayout.LayoutParams(D0(context), -2));
        } else {
            inflate.findViewById(R$id.mtrl_calendar_main_pane).setLayoutParams(new LinearLayout.LayoutParams(D0(context), -1));
        }
        TextView textView = (TextView) inflate.findViewById(R$id.mtrl_picker_header_selection_text);
        this.L = textView;
        ViewCompat.n0(textView, 1);
        this.M = (CheckableImageButton) inflate.findViewById(R$id.mtrl_picker_header_toggle);
        TextView textView2 = (TextView) inflate.findViewById(R$id.mtrl_picker_title_text);
        CharSequence charSequence = this.E;
        if (charSequence != null) {
            textView2.setText(charSequence);
        } else {
            textView2.setText(this.D);
        }
        G0(context);
        this.O = (Button) inflate.findViewById(R$id.confirm_button);
        if (B0().f()) {
            this.O.setEnabled(true);
        } else {
            this.O.setEnabled(false);
        }
        this.O.setTag(Q);
        CharSequence charSequence2 = this.I;
        if (charSequence2 != null) {
            this.O.setText(charSequence2);
        } else {
            int i10 = this.H;
            if (i10 != 0) {
                this.O.setText(i10);
            }
        }
        this.O.setOnClickListener(new a());
        Button button = (Button) inflate.findViewById(R$id.cancel_button);
        button.setTag(R);
        CharSequence charSequence3 = this.K;
        if (charSequence3 != null) {
            button.setText(charSequence3);
        } else {
            int i11 = this.J;
            if (i11 != 0) {
                button.setText(i11);
            }
        }
        button.setOnClickListener(new b());
        return inflate;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        Iterator<DialogInterface.OnDismissListener> it = this.f8748x.iterator();
        while (it.hasNext()) {
            it.next().onDismiss(dialogInterface);
        }
        ViewGroup viewGroup = (ViewGroup) getView();
        if (viewGroup != null) {
            viewGroup.removeAllViews();
        }
        super.onDismiss(dialogInterface);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("OVERRIDE_THEME_RES_ID", this.f8749y);
        bundle.putParcelable("DATE_SELECTOR_KEY", this.f8750z);
        CalendarConstraints.b bVar = new CalendarConstraints.b(this.B);
        if (this.C.q0() != null) {
            bVar.b(this.C.q0().f8665j);
        }
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", bVar.a());
        bundle.putInt("TITLE_TEXT_RES_ID_KEY", this.D);
        bundle.putCharSequence("TITLE_TEXT_KEY", this.E);
        bundle.putInt("POSITIVE_BUTTON_TEXT_RES_ID_KEY", this.H);
        bundle.putCharSequence("POSITIVE_BUTTON_TEXT_KEY", this.I);
        bundle.putInt("NEGATIVE_BUTTON_TEXT_RES_ID_KEY", this.J);
        bundle.putCharSequence("NEGATIVE_BUTTON_TEXT_KEY", this.K);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Window window = o0().getWindow();
        if (this.F) {
            window.setLayout(-1, -1);
            window.setBackgroundDrawable(this.N);
            A0(window);
        } else {
            window.setLayout(-2, -2);
            int dimensionPixelOffset = getResources().getDimensionPixelOffset(R$dimen.mtrl_calendar_dialog_background_inset);
            Rect rect = new Rect(dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset);
            window.setBackgroundDrawable(new InsetDrawable((Drawable) this.N, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset));
            window.getDecorView().setOnTouchListener(new InsetDialogOnTouchListener(o0(), rect));
        }
        K0();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStop() {
        this.A.e0();
        super.onStop();
    }
}
