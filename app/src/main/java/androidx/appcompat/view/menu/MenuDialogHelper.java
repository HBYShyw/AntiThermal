package androidx.appcompat.view.menu;

import android.content.DialogInterface;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.R$layout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuPresenter;
import com.oplus.statistics.DataTypeConstants;

/* compiled from: MenuDialogHelper.java */
/* renamed from: androidx.appcompat.view.menu.h, reason: use source file name */
/* loaded from: classes.dex */
class MenuDialogHelper implements DialogInterface.OnKeyListener, DialogInterface.OnClickListener, DialogInterface.OnDismissListener, MenuPresenter.a {

    /* renamed from: e, reason: collision with root package name */
    private MenuBuilder f737e;

    /* renamed from: f, reason: collision with root package name */
    private AlertDialog f738f;

    /* renamed from: g, reason: collision with root package name */
    ListMenuPresenter f739g;

    /* renamed from: h, reason: collision with root package name */
    private MenuPresenter.a f740h;

    public MenuDialogHelper(MenuBuilder menuBuilder) {
        this.f737e = menuBuilder;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter.a
    public boolean a(MenuBuilder menuBuilder) {
        MenuPresenter.a aVar = this.f740h;
        if (aVar != null) {
            return aVar.a(menuBuilder);
        }
        return false;
    }

    public void b() {
        AlertDialog alertDialog = this.f738f;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void c(IBinder iBinder) {
        MenuBuilder menuBuilder = this.f737e;
        AlertDialog.a aVar = new AlertDialog.a(menuBuilder.getContext());
        ListMenuPresenter listMenuPresenter = new ListMenuPresenter(aVar.b(), R$layout.abc_list_menu_item_layout);
        this.f739g = listMenuPresenter;
        listMenuPresenter.setCallback(this);
        this.f737e.addMenuPresenter(this.f739g);
        aVar.c(this.f739g.a(), this);
        View headerView = menuBuilder.getHeaderView();
        if (headerView != null) {
            aVar.e(headerView);
        } else {
            aVar.f(menuBuilder.getHeaderIcon()).t(menuBuilder.getHeaderTitle());
        }
        aVar.n(this);
        AlertDialog a10 = aVar.a();
        this.f738f = a10;
        a10.setOnDismissListener(this);
        WindowManager.LayoutParams attributes = this.f738f.getWindow().getAttributes();
        attributes.type = DataTypeConstants.PAGE_VISIT;
        if (iBinder != null) {
            attributes.token = iBinder;
        }
        attributes.flags |= 131072;
        this.f738f.show();
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i10) {
        this.f737e.performItemAction((MenuItemImpl) this.f739g.a().getItem(i10), 0);
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter.a
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z10) {
        if (z10 || menuBuilder == this.f737e) {
            b();
        }
        MenuPresenter.a aVar = this.f740h;
        if (aVar != null) {
            aVar.onCloseMenu(menuBuilder, z10);
        }
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        this.f739g.onCloseMenu(this.f737e, true);
    }

    @Override // android.content.DialogInterface.OnKeyListener
    public boolean onKey(DialogInterface dialogInterface, int i10, KeyEvent keyEvent) {
        Window window;
        View decorView;
        KeyEvent.DispatcherState keyDispatcherState;
        View decorView2;
        KeyEvent.DispatcherState keyDispatcherState2;
        if (i10 == 82 || i10 == 4) {
            if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                Window window2 = this.f738f.getWindow();
                if (window2 != null && (decorView2 = window2.getDecorView()) != null && (keyDispatcherState2 = decorView2.getKeyDispatcherState()) != null) {
                    keyDispatcherState2.startTracking(keyEvent, this);
                    return true;
                }
            } else if (keyEvent.getAction() == 1 && !keyEvent.isCanceled() && (window = this.f738f.getWindow()) != null && (decorView = window.getDecorView()) != null && (keyDispatcherState = decorView.getKeyDispatcherState()) != null && keyDispatcherState.isTracking(keyEvent)) {
                this.f737e.close(true);
                dialogInterface.dismiss();
                return true;
            }
        }
        return this.f737e.performShortcut(i10, keyEvent, 0);
    }
}
