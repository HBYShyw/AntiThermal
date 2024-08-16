package r;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import r.CursorFilter;

/* compiled from: CursorAdapter.java */
/* renamed from: r.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class CursorAdapter extends BaseAdapter implements Filterable, CursorFilter.a {

    /* renamed from: e, reason: collision with root package name */
    protected boolean f17441e;

    /* renamed from: f, reason: collision with root package name */
    protected boolean f17442f;

    /* renamed from: g, reason: collision with root package name */
    protected Cursor f17443g;

    /* renamed from: h, reason: collision with root package name */
    protected Context f17444h;

    /* renamed from: i, reason: collision with root package name */
    protected int f17445i;

    /* renamed from: j, reason: collision with root package name */
    protected a f17446j;

    /* renamed from: k, reason: collision with root package name */
    protected DataSetObserver f17447k;

    /* renamed from: l, reason: collision with root package name */
    protected CursorFilter f17448l;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: CursorAdapter.java */
    /* renamed from: r.a$a */
    /* loaded from: classes.dex */
    public class a extends ContentObserver {
        a() {
            super(new Handler());
        }

        @Override // android.database.ContentObserver
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            CursorAdapter.this.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: CursorAdapter.java */
    /* renamed from: r.a$b */
    /* loaded from: classes.dex */
    public class b extends DataSetObserver {
        b() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            CursorAdapter cursorAdapter = CursorAdapter.this;
            cursorAdapter.f17441e = true;
            cursorAdapter.notifyDataSetChanged();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            CursorAdapter cursorAdapter = CursorAdapter.this;
            cursorAdapter.f17441e = false;
            cursorAdapter.notifyDataSetInvalidated();
        }
    }

    public CursorAdapter(Context context, Cursor cursor, boolean z10) {
        b(context, cursor, z10 ? 1 : 2);
    }

    public abstract void a(View view, Context context, Cursor cursor);

    void b(Context context, Cursor cursor, int i10) {
        if ((i10 & 1) == 1) {
            i10 |= 2;
            this.f17442f = true;
        } else {
            this.f17442f = false;
        }
        boolean z10 = cursor != null;
        this.f17443g = cursor;
        this.f17441e = z10;
        this.f17444h = context;
        this.f17445i = z10 ? cursor.getColumnIndexOrThrow("_id") : -1;
        if ((i10 & 2) == 2) {
            this.f17446j = new a();
            this.f17447k = new b();
        } else {
            this.f17446j = null;
            this.f17447k = null;
        }
        if (z10) {
            a aVar = this.f17446j;
            if (aVar != null) {
                cursor.registerContentObserver(aVar);
            }
            DataSetObserver dataSetObserver = this.f17447k;
            if (dataSetObserver != null) {
                cursor.registerDataSetObserver(dataSetObserver);
            }
        }
    }

    public abstract View c(Context context, Cursor cursor, ViewGroup viewGroup);

    public void changeCursor(Cursor cursor) {
        Cursor f10 = f(cursor);
        if (f10 != null) {
            f10.close();
        }
    }

    public abstract CharSequence convertToString(Cursor cursor);

    public abstract View d(Context context, Cursor cursor, ViewGroup viewGroup);

    protected void e() {
        Cursor cursor;
        if (!this.f17442f || (cursor = this.f17443g) == null || cursor.isClosed()) {
            return;
        }
        this.f17441e = this.f17443g.requery();
    }

    public Cursor f(Cursor cursor) {
        Cursor cursor2 = this.f17443g;
        if (cursor == cursor2) {
            return null;
        }
        if (cursor2 != null) {
            a aVar = this.f17446j;
            if (aVar != null) {
                cursor2.unregisterContentObserver(aVar);
            }
            DataSetObserver dataSetObserver = this.f17447k;
            if (dataSetObserver != null) {
                cursor2.unregisterDataSetObserver(dataSetObserver);
            }
        }
        this.f17443g = cursor;
        if (cursor != null) {
            a aVar2 = this.f17446j;
            if (aVar2 != null) {
                cursor.registerContentObserver(aVar2);
            }
            DataSetObserver dataSetObserver2 = this.f17447k;
            if (dataSetObserver2 != null) {
                cursor.registerDataSetObserver(dataSetObserver2);
            }
            this.f17445i = cursor.getColumnIndexOrThrow("_id");
            this.f17441e = true;
            notifyDataSetChanged();
        } else {
            this.f17445i = -1;
            this.f17441e = false;
            notifyDataSetInvalidated();
        }
        return cursor2;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        Cursor cursor;
        if (!this.f17441e || (cursor = this.f17443g) == null) {
            return 0;
        }
        return cursor.getCount();
    }

    @Override // r.CursorFilter.a
    public Cursor getCursor() {
        return this.f17443g;
    }

    @Override // android.widget.BaseAdapter, android.widget.SpinnerAdapter
    public View getDropDownView(int i10, View view, ViewGroup viewGroup) {
        if (!this.f17441e) {
            return null;
        }
        this.f17443g.moveToPosition(i10);
        if (view == null) {
            view = c(this.f17444h, this.f17443g, viewGroup);
        }
        a(view, this.f17444h, this.f17443g);
        return view;
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        if (this.f17448l == null) {
            this.f17448l = new CursorFilter(this);
        }
        return this.f17448l;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i10) {
        Cursor cursor;
        if (!this.f17441e || (cursor = this.f17443g) == null) {
            return null;
        }
        cursor.moveToPosition(i10);
        return this.f17443g;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i10) {
        Cursor cursor;
        if (this.f17441e && (cursor = this.f17443g) != null && cursor.moveToPosition(i10)) {
            return this.f17443g.getLong(this.f17445i);
        }
        return 0L;
    }

    @Override // android.widget.Adapter
    public View getView(int i10, View view, ViewGroup viewGroup) {
        if (this.f17441e) {
            if (this.f17443g.moveToPosition(i10)) {
                if (view == null) {
                    view = d(this.f17444h, this.f17443g, viewGroup);
                }
                a(view, this.f17444h, this.f17443g);
                return view;
            }
            throw new IllegalStateException("couldn't move cursor to position " + i10);
        }
        throw new IllegalStateException("this should only be called when the cursor is valid");
    }
}
