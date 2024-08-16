package androidx.appcompat.widget;

import android.R;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$id;
import androidx.core.content.ContextCompat;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.WeakHashMap;
import r.ResourceCursorAdapter;

/* compiled from: SuggestionsAdapter.java */
/* renamed from: androidx.appcompat.widget.b0, reason: use source file name */
/* loaded from: classes.dex */
class SuggestionsAdapter extends ResourceCursorAdapter implements View.OnClickListener {
    private int A;
    private int B;
    private int C;

    /* renamed from: p, reason: collision with root package name */
    private final SearchView f1163p;

    /* renamed from: q, reason: collision with root package name */
    private final SearchableInfo f1164q;

    /* renamed from: r, reason: collision with root package name */
    private final Context f1165r;

    /* renamed from: s, reason: collision with root package name */
    private final WeakHashMap<String, Drawable.ConstantState> f1166s;

    /* renamed from: t, reason: collision with root package name */
    private final int f1167t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f1168u;

    /* renamed from: v, reason: collision with root package name */
    private int f1169v;

    /* renamed from: w, reason: collision with root package name */
    private ColorStateList f1170w;

    /* renamed from: x, reason: collision with root package name */
    private int f1171x;

    /* renamed from: y, reason: collision with root package name */
    private int f1172y;

    /* renamed from: z, reason: collision with root package name */
    private int f1173z;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SuggestionsAdapter.java */
    /* renamed from: androidx.appcompat.widget.b0$a */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        public final TextView f1174a;

        /* renamed from: b, reason: collision with root package name */
        public final TextView f1175b;

        /* renamed from: c, reason: collision with root package name */
        public final ImageView f1176c;

        /* renamed from: d, reason: collision with root package name */
        public final ImageView f1177d;

        /* renamed from: e, reason: collision with root package name */
        public final ImageView f1178e;

        public a(View view) {
            this.f1174a = (TextView) view.findViewById(R.id.text1);
            this.f1175b = (TextView) view.findViewById(R.id.text2);
            this.f1176c = (ImageView) view.findViewById(R.id.icon1);
            this.f1177d = (ImageView) view.findViewById(R.id.icon2);
            this.f1178e = (ImageView) view.findViewById(R$id.edit_query);
        }
    }

    public SuggestionsAdapter(Context context, SearchView searchView, SearchableInfo searchableInfo, WeakHashMap<String, Drawable.ConstantState> weakHashMap) {
        super(context, searchView.getSuggestionRowLayout(), null, true);
        this.f1168u = false;
        this.f1169v = 1;
        this.f1171x = -1;
        this.f1172y = -1;
        this.f1173z = -1;
        this.A = -1;
        this.B = -1;
        this.C = -1;
        this.f1163p = searchView;
        this.f1164q = searchableInfo;
        this.f1167t = searchView.getSuggestionCommitIconResId();
        this.f1165r = context;
        this.f1166s = weakHashMap;
    }

    private Drawable g(String str) {
        Drawable.ConstantState constantState = this.f1166s.get(str);
        if (constantState == null) {
            return null;
        }
        return constantState.newDrawable();
    }

    private CharSequence h(CharSequence charSequence) {
        if (this.f1170w == null) {
            TypedValue typedValue = new TypedValue();
            this.f1165r.getTheme().resolveAttribute(R$attr.textColorSearchUrl, typedValue, true);
            this.f1170w = this.f1165r.getResources().getColorStateList(typedValue.resourceId);
        }
        SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan(new TextAppearanceSpan(null, 0, 0, this.f1170w, null), 0, charSequence.length(), 33);
        return spannableString;
    }

    private Drawable i(ComponentName componentName) {
        PackageManager packageManager = this.f1165r.getPackageManager();
        try {
            ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, 128);
            int iconResource = activityInfo.getIconResource();
            if (iconResource == 0) {
                return null;
            }
            Drawable drawable = packageManager.getDrawable(componentName.getPackageName(), iconResource, activityInfo.applicationInfo);
            if (drawable != null) {
                return drawable;
            }
            Log.w("SuggestionsAdapter", "Invalid icon resource " + iconResource + " for " + componentName.flattenToShortString());
            return null;
        } catch (PackageManager.NameNotFoundException e10) {
            Log.w("SuggestionsAdapter", e10.toString());
            return null;
        }
    }

    private Drawable j(ComponentName componentName) {
        String flattenToShortString = componentName.flattenToShortString();
        if (this.f1166s.containsKey(flattenToShortString)) {
            Drawable.ConstantState constantState = this.f1166s.get(flattenToShortString);
            if (constantState == null) {
                return null;
            }
            return constantState.newDrawable(this.f1165r.getResources());
        }
        Drawable i10 = i(componentName);
        this.f1166s.put(flattenToShortString, i10 != null ? i10.getConstantState() : null);
        return i10;
    }

    public static String k(Cursor cursor, String str) {
        return s(cursor, cursor.getColumnIndex(str));
    }

    private Drawable l() {
        Drawable j10 = j(this.f1164q.getSearchActivity());
        return j10 != null ? j10 : this.f1165r.getPackageManager().getDefaultActivityIcon();
    }

    private Drawable m(Uri uri) {
        try {
            if ("android.resource".equals(uri.getScheme())) {
                try {
                    return n(uri);
                } catch (Resources.NotFoundException unused) {
                    throw new FileNotFoundException("Resource does not exist: " + uri);
                }
            }
            InputStream openInputStream = this.f1165r.getContentResolver().openInputStream(uri);
            if (openInputStream != null) {
                try {
                    return Drawable.createFromStream(openInputStream, null);
                } finally {
                    try {
                        openInputStream.close();
                    } catch (IOException e10) {
                        Log.e("SuggestionsAdapter", "Error closing icon stream for " + uri, e10);
                    }
                }
            }
            throw new FileNotFoundException("Failed to open " + uri);
        } catch (FileNotFoundException e11) {
            Log.w("SuggestionsAdapter", "Icon not found: " + uri + ", " + e11.getMessage());
            return null;
        }
        Log.w("SuggestionsAdapter", "Icon not found: " + uri + ", " + e11.getMessage());
        return null;
    }

    private Drawable o(String str) {
        if (str == null || str.isEmpty() || "0".equals(str)) {
            return null;
        }
        try {
            int parseInt = Integer.parseInt(str);
            String str2 = "android.resource://" + this.f1165r.getPackageName() + "/" + parseInt;
            Drawable g6 = g(str2);
            if (g6 != null) {
                return g6;
            }
            Drawable e10 = ContextCompat.e(this.f1165r, parseInt);
            w(str2, e10);
            return e10;
        } catch (Resources.NotFoundException unused) {
            Log.w("SuggestionsAdapter", "Icon resource not found: " + str);
            return null;
        } catch (NumberFormatException unused2) {
            Drawable g10 = g(str);
            if (g10 != null) {
                return g10;
            }
            Drawable m10 = m(Uri.parse(str));
            w(str, m10);
            return m10;
        }
    }

    private Drawable p(Cursor cursor) {
        int i10 = this.A;
        if (i10 == -1) {
            return null;
        }
        Drawable o10 = o(cursor.getString(i10));
        return o10 != null ? o10 : l();
    }

    private Drawable q(Cursor cursor) {
        int i10 = this.B;
        if (i10 == -1) {
            return null;
        }
        return o(cursor.getString(i10));
    }

    private static String s(Cursor cursor, int i10) {
        if (i10 == -1) {
            return null;
        }
        try {
            return cursor.getString(i10);
        } catch (Exception e10) {
            Log.e("SuggestionsAdapter", "unexpected error retrieving valid column from cursor, did the remote process die?", e10);
            return null;
        }
    }

    private void u(ImageView imageView, Drawable drawable, int i10) {
        imageView.setImageDrawable(drawable);
        if (drawable == null) {
            imageView.setVisibility(i10);
            return;
        }
        imageView.setVisibility(0);
        drawable.setVisible(false, false);
        drawable.setVisible(true, false);
    }

    private void v(TextView textView, CharSequence charSequence) {
        textView.setText(charSequence);
        if (TextUtils.isEmpty(charSequence)) {
            textView.setVisibility(8);
        } else {
            textView.setVisibility(0);
        }
    }

    private void w(String str, Drawable drawable) {
        if (drawable != null) {
            this.f1166s.put(str, drawable.getConstantState());
        }
    }

    private void x(Cursor cursor) {
        Bundle extras = cursor != null ? cursor.getExtras() : null;
        if (extras != null) {
            extras.getBoolean("in_progress");
        }
    }

    @Override // r.CursorAdapter
    public void a(View view, Context context, Cursor cursor) {
        CharSequence s7;
        a aVar = (a) view.getTag();
        int i10 = this.C;
        int i11 = i10 != -1 ? cursor.getInt(i10) : 0;
        if (aVar.f1174a != null) {
            v(aVar.f1174a, s(cursor, this.f1171x));
        }
        if (aVar.f1175b != null) {
            String s10 = s(cursor, this.f1173z);
            if (s10 != null) {
                s7 = h(s10);
            } else {
                s7 = s(cursor, this.f1172y);
            }
            if (TextUtils.isEmpty(s7)) {
                TextView textView = aVar.f1174a;
                if (textView != null) {
                    textView.setSingleLine(false);
                    aVar.f1174a.setMaxLines(2);
                }
            } else {
                TextView textView2 = aVar.f1174a;
                if (textView2 != null) {
                    textView2.setSingleLine(true);
                    aVar.f1174a.setMaxLines(1);
                }
            }
            v(aVar.f1175b, s7);
        }
        ImageView imageView = aVar.f1176c;
        if (imageView != null) {
            u(imageView, p(cursor), 4);
        }
        ImageView imageView2 = aVar.f1177d;
        if (imageView2 != null) {
            u(imageView2, q(cursor), 8);
        }
        int i12 = this.f1169v;
        if (i12 != 2 && (i12 != 1 || (i11 & 1) == 0)) {
            aVar.f1178e.setVisibility(8);
            return;
        }
        aVar.f1178e.setVisibility(0);
        aVar.f1178e.setTag(aVar.f1174a.getText());
        aVar.f1178e.setOnClickListener(this);
    }

    @Override // r.CursorAdapter, r.CursorFilter.a
    public void changeCursor(Cursor cursor) {
        if (this.f1168u) {
            Log.w("SuggestionsAdapter", "Tried to change cursor after adapter was closed.");
            if (cursor != null) {
                cursor.close();
                return;
            }
            return;
        }
        try {
            super.changeCursor(cursor);
            if (cursor != null) {
                this.f1171x = cursor.getColumnIndex("suggest_text_1");
                this.f1172y = cursor.getColumnIndex("suggest_text_2");
                this.f1173z = cursor.getColumnIndex("suggest_text_2_url");
                this.A = cursor.getColumnIndex("suggest_icon_1");
                this.B = cursor.getColumnIndex("suggest_icon_2");
                this.C = cursor.getColumnIndex("suggest_flags");
            }
        } catch (Exception e10) {
            Log.e("SuggestionsAdapter", "error changing cursor and caching columns", e10);
        }
    }

    @Override // r.CursorAdapter, r.CursorFilter.a
    public CharSequence convertToString(Cursor cursor) {
        String k10;
        String k11;
        if (cursor == null) {
            return null;
        }
        String k12 = k(cursor, "suggest_intent_query");
        if (k12 != null) {
            return k12;
        }
        if (this.f1164q.shouldRewriteQueryFromData() && (k11 = k(cursor, "suggest_intent_data")) != null) {
            return k11;
        }
        if (!this.f1164q.shouldRewriteQueryFromText() || (k10 = k(cursor, "suggest_text_1")) == null) {
            return null;
        }
        return k10;
    }

    @Override // r.ResourceCursorAdapter, r.CursorAdapter
    public View d(Context context, Cursor cursor, ViewGroup viewGroup) {
        View d10 = super.d(context, cursor, viewGroup);
        d10.setTag(new a(d10));
        ((ImageView) d10.findViewById(R$id.edit_query)).setImageResource(this.f1167t);
        return d10;
    }

    @Override // r.CursorAdapter, android.widget.BaseAdapter, android.widget.SpinnerAdapter
    public View getDropDownView(int i10, View view, ViewGroup viewGroup) {
        try {
            return super.getDropDownView(i10, view, viewGroup);
        } catch (RuntimeException e10) {
            Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", e10);
            View c10 = this.c(this.f1165r, this.getCursor(), viewGroup);
            if (c10 != null) {
                ((a) c10.getTag()).f1174a.setText(e10.toString());
            }
            return c10;
        }
    }

    @Override // r.CursorAdapter, android.widget.Adapter
    public View getView(int i10, View view, ViewGroup viewGroup) {
        try {
            return super.getView(i10, view, viewGroup);
        } catch (RuntimeException e10) {
            Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", e10);
            View d10 = this.d(this.f1165r, this.getCursor(), viewGroup);
            if (d10 != null) {
                ((a) d10.getTag()).f1174a.setText(e10.toString());
            }
            return d10;
        }
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return false;
    }

    Drawable n(Uri uri) {
        int parseInt;
        String authority = uri.getAuthority();
        if (!TextUtils.isEmpty(authority)) {
            try {
                Resources resourcesForApplication = this.f1165r.getPackageManager().getResourcesForApplication(authority);
                List<String> pathSegments = uri.getPathSegments();
                if (pathSegments != null) {
                    int size = pathSegments.size();
                    if (size == 1) {
                        try {
                            parseInt = Integer.parseInt(pathSegments.get(0));
                        } catch (NumberFormatException unused) {
                            throw new FileNotFoundException("Single path segment is not a resource ID: " + uri);
                        }
                    } else if (size == 2) {
                        parseInt = resourcesForApplication.getIdentifier(pathSegments.get(1), pathSegments.get(0), authority);
                    } else {
                        throw new FileNotFoundException("More than two path segments: " + uri);
                    }
                    if (parseInt != 0) {
                        return resourcesForApplication.getDrawable(parseInt);
                    }
                    throw new FileNotFoundException("No resource found for: " + uri);
                }
                throw new FileNotFoundException("No path: " + uri);
            } catch (PackageManager.NameNotFoundException unused2) {
                throw new FileNotFoundException("No package found for authority: " + uri);
            }
        }
        throw new FileNotFoundException("No authority: " + uri);
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        x(getCursor());
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        x(getCursor());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Object tag = view.getTag();
        if (tag instanceof CharSequence) {
            this.f1163p.onQueryRefine((CharSequence) tag);
        }
    }

    Cursor r(SearchableInfo searchableInfo, String str, int i10) {
        String suggestAuthority;
        String[] strArr = null;
        if (searchableInfo == null || (suggestAuthority = searchableInfo.getSuggestAuthority()) == null) {
            return null;
        }
        Uri.Builder fragment = new Uri.Builder().scheme("content").authority(suggestAuthority).query("").fragment("");
        String suggestPath = searchableInfo.getSuggestPath();
        if (suggestPath != null) {
            fragment.appendEncodedPath(suggestPath);
        }
        fragment.appendPath("search_suggest_query");
        String suggestSelection = searchableInfo.getSuggestSelection();
        if (suggestSelection != null) {
            strArr = new String[]{str};
        } else {
            fragment.appendPath(str);
        }
        String[] strArr2 = strArr;
        if (i10 > 0) {
            fragment.appendQueryParameter("limit", String.valueOf(i10));
        }
        return this.f1165r.getContentResolver().query(fragment.build(), null, suggestSelection, strArr2, null);
    }

    @Override // r.CursorFilter.a
    public Cursor runQueryOnBackgroundThread(CharSequence charSequence) {
        String charSequence2 = charSequence == null ? "" : charSequence.toString();
        if (this.f1163p.getVisibility() == 0 && this.f1163p.getWindowVisibility() == 0) {
            try {
                Cursor r10 = r(this.f1164q, charSequence2, 50);
                if (r10 != null) {
                    r10.getCount();
                    return r10;
                }
            } catch (RuntimeException e10) {
                Log.w("SuggestionsAdapter", "Search suggestions query threw an exception.", e10);
            }
        }
        return null;
    }

    public void t(int i10) {
        this.f1169v = i10;
    }
}
