package g2;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import com.support.appcompat.R$integer;
import g2.PreciseClickHelper;
import java.util.ArrayList;

/* compiled from: COUIClickSelectMenu.java */
/* renamed from: g2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIClickSelectMenu {

    /* renamed from: a, reason: collision with root package name */
    private final COUIPopupListWindow f11490a;

    /* renamed from: b, reason: collision with root package name */
    private PreciseClickHelper f11491b;

    /* renamed from: c, reason: collision with root package name */
    private PreciseClickHelper.c f11492c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f11493d = true;

    /* renamed from: e, reason: collision with root package name */
    private InputMethodManager f11494e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIClickSelectMenu.java */
    /* renamed from: g2.a$a */
    /* loaded from: classes.dex */
    public class a implements PreciseClickHelper.c {

        /* compiled from: COUIClickSelectMenu.java */
        /* renamed from: g2.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class RunnableC0036a implements Runnable {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ View f11496e;

            RunnableC0036a(View view) {
                this.f11496e = view;
            }

            @Override // java.lang.Runnable
            public void run() {
                COUIClickSelectMenu.this.j(this.f11496e);
            }
        }

        a() {
        }

        @Override // g2.PreciseClickHelper.c
        public void a(View view, int i10, int i11) {
            if (COUIClickSelectMenu.this.f11492c != null) {
                COUIClickSelectMenu.this.f11492c.a(view, i10, i11);
            }
            COUIClickSelectMenu.this.f11490a.L(-i10, -i11, i10 - view.getWidth(), i11 - view.getHeight());
            if (COUIClickSelectMenu.this.f11494e != null && COUIClickSelectMenu.this.f11494e.hideSoftInputFromWindow(view.getWindowToken(), 0)) {
                view.postDelayed(new RunnableC0036a(view), view.getContext().getResources().getInteger(R$integer.support_menu_click_select_time));
            } else {
                COUIClickSelectMenu.this.j(view);
            }
        }
    }

    public COUIClickSelectMenu(Context context, View view) {
        COUIPopupListWindow cOUIPopupListWindow = new COUIPopupListWindow(context);
        this.f11490a = cOUIPopupListWindow;
        if (view != null) {
            cOUIPopupListWindow.I(view);
        }
        this.f11494e = (InputMethodManager) context.getSystemService("input_method");
    }

    public void d() {
        if (this.f11490a.isShowing()) {
            this.f11490a.dismiss();
        } else if (this.f11490a.x() == null) {
            this.f11490a.U();
        }
    }

    public void e(View view, ArrayList<PopupListItem> arrayList) {
        if (arrayList.size() <= 0) {
            return;
        }
        this.f11490a.J(arrayList);
        this.f11490a.b(true);
        view.setClickable(true);
        view.setLongClickable(true);
        this.f11491b = new PreciseClickHelper(view, new a());
    }

    public void f(View view, ArrayList<PopupListItem> arrayList, int i10) {
        e(view, arrayList);
        this.f11490a.N(i10);
    }

    public void g(boolean z10) {
        PreciseClickHelper preciseClickHelper = this.f11491b;
        if (preciseClickHelper != null) {
            this.f11493d = z10;
            if (z10) {
                preciseClickHelper.c();
            } else {
                preciseClickHelper.d();
            }
        }
    }

    public void h(AdapterView.OnItemClickListener onItemClickListener) {
        this.f11490a.M(onItemClickListener);
    }

    public void i(PreciseClickHelper.c cVar) {
        this.f11492c = cVar;
    }

    public void j(View view) {
        if (this.f11493d) {
            this.f11490a.R(view);
        }
    }
}
