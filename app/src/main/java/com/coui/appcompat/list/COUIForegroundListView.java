package com.coui.appcompat.list;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.MenuItemHoverListener;
import g2.DefaultAdapter;
import g2.PopupListItem;

/* loaded from: classes.dex */
public class COUIForegroundListView extends ListView {

    /* renamed from: e, reason: collision with root package name */
    private int f6269e;

    /* renamed from: f, reason: collision with root package name */
    private int f6270f;

    /* renamed from: g, reason: collision with root package name */
    private MenuItemHoverListener f6271g;

    /* renamed from: h, reason: collision with root package name */
    private MenuItem f6272h;

    /* renamed from: i, reason: collision with root package name */
    private Paint f6273i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f6274j;

    /* renamed from: k, reason: collision with root package name */
    private float f6275k;

    /* renamed from: l, reason: collision with root package name */
    private Path f6276l;

    /* renamed from: m, reason: collision with root package name */
    private RectF f6277m;

    public COUIForegroundListView(Context context) {
        super(context);
        this.f6273i = new Paint();
        this.f6275k = 0.0f;
        this.f6277m = null;
        b(context);
    }

    private Path a() {
        Path path = this.f6276l;
        RectF rectF = new RectF(0.0f, 0.0f, getWidth(), getHeight());
        float f10 = this.f6275k;
        path.addRoundRect(rectF, new float[]{f10, f10, f10, f10, f10, f10, f10, f10}, Path.Direction.CW);
        return this.f6276l;
    }

    private void b(Context context) {
        if (1 == context.getResources().getConfiguration().getLayoutDirection()) {
            this.f6269e = 21;
            this.f6270f = 22;
        } else {
            this.f6269e = 22;
            this.f6270f = 21;
        }
    }

    @Override // android.view.View
    public boolean isInTouchMode() {
        return this.f6274j || super.isInTouchMode();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.f6275k > 0.0f) {
            canvas.clipPath(this.f6276l);
        }
        super.onDraw(canvas);
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int i10;
        MenuAdapter menuAdapter;
        int pointToPosition;
        int i11;
        if (this.f6271g != null) {
            ListAdapter adapter = getAdapter();
            if (adapter instanceof HeaderViewListAdapter) {
                HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
                i10 = headerViewListAdapter.getHeadersCount();
                menuAdapter = (MenuAdapter) headerViewListAdapter.getWrappedAdapter();
            } else {
                i10 = 0;
                menuAdapter = (MenuAdapter) adapter;
            }
            MenuItemImpl menuItemImpl = null;
            if (motionEvent.getAction() != 10 && (pointToPosition = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY())) != -1 && (i11 = pointToPosition - i10) >= 0 && i11 < menuAdapter.getCount()) {
                menuItemImpl = menuAdapter.getItem(i11);
            }
            MenuItem menuItem = this.f6272h;
            if (menuItem != menuItemImpl) {
                MenuBuilder b10 = menuAdapter.b();
                if (menuItem != null) {
                    this.f6271g.h(b10, menuItem);
                }
                this.f6272h = menuItemImpl;
                if (menuItemImpl != null) {
                    this.f6271g.e(b10, menuItemImpl);
                }
            }
        }
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i10, KeyEvent keyEvent) {
        View selectedView = getSelectedView();
        if (!(selectedView instanceof LinearLayout)) {
            return super.onKeyDown(i10, keyEvent);
        }
        LinearLayout linearLayout = (LinearLayout) selectedView;
        ListAdapter adapter = getAdapter();
        if (linearLayout != null && i10 == this.f6269e && (adapter instanceof DefaultAdapter)) {
            if (linearLayout.isEnabled() && ((PopupListItem) ((DefaultAdapter) adapter).getItem(getSelectedItemPosition())).h()) {
                performItemClick(linearLayout, getSelectedItemPosition(), getSelectedItemId());
            }
            return true;
        }
        if (linearLayout != null && i10 == this.f6270f) {
            setSelection(-1);
            return true;
        }
        return super.onKeyDown(i10, keyEvent);
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        Path path = this.f6276l;
        if (path == null) {
            this.f6276l = new Path();
        } else {
            path.reset();
        }
        this.f6277m = new RectF(0.0f, 0.0f, getWidth(), getHeight());
        a();
    }

    public void setHoverListener(MenuItemHoverListener menuItemHoverListener) {
        this.f6271g = menuItemHoverListener;
    }

    public void setListSelectionHidden(boolean z10) {
        this.f6274j = z10;
    }

    public void setRadius(float f10) {
        this.f6275k = f10;
    }

    public COUIForegroundListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f6273i = new Paint();
        this.f6275k = 0.0f;
        this.f6277m = null;
        b(context);
    }

    public COUIForegroundListView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6273i = new Paint();
        this.f6275k = 0.0f;
        this.f6277m = null;
        b(context);
    }
}
