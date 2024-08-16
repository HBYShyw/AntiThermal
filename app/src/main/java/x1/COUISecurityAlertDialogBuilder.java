package x1;

import android.R;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$string;
import u1.COUIClickableSpan;
import z2.COUIChangeTextUtil;

/* compiled from: COUISecurityAlertDialogBuilder.java */
/* renamed from: x1.d, reason: use source file name */
/* loaded from: classes.dex */
public class COUISecurityAlertDialogBuilder extends COUIAlertDialogBuilder {
    private Context M;
    private AlertDialog N;
    private boolean O;
    private boolean P;
    private String Q;
    private boolean R;
    private int S;
    private int T;
    private g U;
    private f V;
    private DialogInterface.OnKeyListener W;

    /* compiled from: COUISecurityAlertDialogBuilder.java */
    /* renamed from: x1.d$a */
    /* loaded from: classes.dex */
    class a implements DialogInterface.OnKeyListener {
        a() {
        }

        @Override // android.content.DialogInterface.OnKeyListener
        public boolean onKey(DialogInterface dialogInterface, int i10, KeyEvent keyEvent) {
            if (i10 == 4 && keyEvent.getAction() == 0) {
                if ((COUISecurityAlertDialogBuilder.this.N != null && COUISecurityAlertDialogBuilder.this.N.isShowing()) && COUISecurityAlertDialogBuilder.this.U != null) {
                    COUISecurityAlertDialogBuilder.this.U.a(-2, COUISecurityAlertDialogBuilder.this.P);
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUISecurityAlertDialogBuilder.java */
    /* renamed from: x1.d$b */
    /* loaded from: classes.dex */
    public class b implements COUIClickableSpan.a {
        b() {
        }

        @Override // u1.COUIClickableSpan.a
        public void a() {
            if (COUISecurityAlertDialogBuilder.this.V != null) {
                COUISecurityAlertDialogBuilder.this.V.a();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUISecurityAlertDialogBuilder.java */
    /* renamed from: x1.d$c */
    /* loaded from: classes.dex */
    public class c implements View.OnTouchListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f19501e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f19502f;

        c(int i10, int i11) {
            this.f19501e = i10;
            this.f19502f = i11;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (!(view instanceof TextView)) {
                return false;
            }
            int actionMasked = motionEvent.getActionMasked();
            int offsetForPosition = ((TextView) view).getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
            int i10 = this.f19501e;
            boolean z10 = offsetForPosition <= i10 || offsetForPosition >= i10 + this.f19502f;
            if (actionMasked != 0) {
                if (actionMasked == 1 || actionMasked == 3) {
                    view.setPressed(false);
                    view.postInvalidateDelayed(70L);
                }
            } else {
                if (z10) {
                    return true;
                }
                view.setPressed(true);
                view.invalidate();
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUISecurityAlertDialogBuilder.java */
    /* renamed from: x1.d$d */
    /* loaded from: classes.dex */
    public class d implements CompoundButton.OnCheckedChangeListener {
        d() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z10) {
            COUISecurityAlertDialogBuilder.this.P = z10;
            if (COUISecurityAlertDialogBuilder.this.U != null) {
                COUISecurityAlertDialogBuilder.this.U.a(0, COUISecurityAlertDialogBuilder.this.P);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUISecurityAlertDialogBuilder.java */
    /* renamed from: x1.d$e */
    /* loaded from: classes.dex */
    public class e implements DialogInterface.OnClickListener {
        e() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            if (COUISecurityAlertDialogBuilder.this.U != null) {
                COUISecurityAlertDialogBuilder.this.U.a(i10, COUISecurityAlertDialogBuilder.this.P);
            }
        }
    }

    /* compiled from: COUISecurityAlertDialogBuilder.java */
    /* renamed from: x1.d$f */
    /* loaded from: classes.dex */
    public interface f {
        void a();
    }

    /* compiled from: COUISecurityAlertDialogBuilder.java */
    /* renamed from: x1.d$g */
    /* loaded from: classes.dex */
    public interface g {
        void a(int i10, boolean z10);
    }

    public COUISecurityAlertDialogBuilder(Context context, int i10, int i11) {
        super(context, i10, i11);
        this.O = true;
        this.W = new a();
        this.M = context;
        w0();
    }

    private void A0() {
        y0();
        z0();
        x0();
    }

    private DialogInterface.OnClickListener t0() {
        return new e();
    }

    private SpannableStringBuilder u0(String str, int i10, int i11) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        COUIClickableSpan cOUIClickableSpan = new COUIClickableSpan(this.M);
        cOUIClickableSpan.a(new b());
        spannableStringBuilder.setSpan(cOUIClickableSpan, i10, i11 + i10, 33);
        return spannableStringBuilder;
    }

    private View.OnTouchListener v0(int i10, int i11) {
        return new c(i10, i11);
    }

    private void w0() {
        this.Q = this.M.getString(R$string.coui_security_alertdialog_checkbox_msg);
    }

    private void x0() {
        AlertDialog alertDialog = this.N;
        if (alertDialog == null) {
            return;
        }
        View findViewById = alertDialog.findViewById(R$id.coui_security_alert_dialog_checkbox);
        if (findViewById instanceof AppCompatCheckBox) {
            if (!this.O) {
                findViewById.setVisibility(8);
                return;
            }
            findViewById.setVisibility(0);
            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox) findViewById;
            appCompatCheckBox.setChecked(this.P);
            appCompatCheckBox.setText(this.Q);
            appCompatCheckBox.setTextSize(0, COUIChangeTextUtil.e(this.M.getResources().getDimensionPixelSize(R$dimen.coui_security_alert_dialog_checkbox_text_size), this.M.getResources().getConfiguration().fontScale, 5));
            appCompatCheckBox.setOnCheckedChangeListener(new d());
        }
    }

    private void y0() {
        AlertDialog alertDialog = this.N;
        if (alertDialog == null) {
            return;
        }
        View findViewById = alertDialog.findViewById(R.id.message);
        if (findViewById instanceof TextView) {
            ((TextView) findViewById).setTextSize(0, (int) COUIChangeTextUtil.e(this.M.getResources().getDimensionPixelSize(R$dimen.coui_alert_dialog_builder_message_text_size), this.M.getResources().getConfiguration().fontScale, 5));
        }
    }

    private void z0() {
        TextView textView;
        String string;
        String string2;
        AlertDialog alertDialog = this.N;
        if (alertDialog == null || (textView = (TextView) alertDialog.findViewById(R$id.coui_security_alertdialog_statement)) == null) {
            return;
        }
        if (!this.R) {
            textView.setVisibility(8);
            return;
        }
        int i10 = this.T;
        if (i10 <= 0) {
            string = this.M.getString(R$string.coui_security_alertdailog_privacy);
        } else {
            string = this.M.getString(i10);
        }
        int i11 = this.S;
        if (i11 <= 0) {
            string2 = this.M.getString(R$string.coui_security_alertdailog_statement, string);
        } else {
            string2 = this.M.getString(i11, string);
        }
        int indexOf = string2.indexOf(string);
        int length = string.length();
        textView.setVisibility(0);
        textView.setHighlightColor(0);
        textView.setText(u0(string2, indexOf, length));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setOnTouchListener(v0(indexOf, length));
    }

    public COUISecurityAlertDialogBuilder B0(int i10) {
        this.Q = this.M.getString(i10);
        return this;
    }

    public COUISecurityAlertDialogBuilder C0(String str) {
        this.Q = str;
        return this;
    }

    public COUISecurityAlertDialogBuilder D0(boolean z10) {
        this.P = z10;
        return this;
    }

    public COUISecurityAlertDialogBuilder E0(boolean z10) {
        this.O = z10;
        return this;
    }

    public COUISecurityAlertDialogBuilder F0(int i10) {
        super.a0(i10, t0());
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.a
    /* renamed from: G0, reason: merged with bridge method [inline-methods] */
    public COUISecurityAlertDialogBuilder n(DialogInterface.OnKeyListener onKeyListener) {
        this.W = onKeyListener;
        super.n(onKeyListener);
        return this;
    }

    public COUISecurityAlertDialogBuilder H0(g gVar) {
        this.U = gVar;
        return this;
    }

    public COUISecurityAlertDialogBuilder I0(int i10) {
        super.e0(i10, t0());
        return this;
    }

    public COUISecurityAlertDialogBuilder J0(String str) {
        super.p(str, t0());
        return this;
    }

    public COUISecurityAlertDialogBuilder K0(boolean z10) {
        this.R = z10;
        return this;
    }

    @Override // x1.COUIAlertDialogBuilder, androidx.appcompat.app.AlertDialog.a
    public AlertDialog a() {
        super.n(this.W);
        AlertDialog a10 = super.a();
        this.N = a10;
        return a10;
    }

    @Override // x1.COUIAlertDialogBuilder
    public void m0() {
        super.m0();
        A0();
    }

    @Override // x1.COUIAlertDialogBuilder, androidx.appcompat.app.AlertDialog.a
    public AlertDialog w() {
        this.N = super.w();
        A0();
        return this.N;
    }
}
